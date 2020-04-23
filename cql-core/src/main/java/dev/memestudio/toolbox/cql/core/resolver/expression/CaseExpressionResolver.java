package dev.memestudio.toolbox.cql.core.resolver.expression;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import dev.memestudio.toolbox.cql.core.util.Validates;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import net.sf.jsqlparser.expression.CaseExpression;

import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * @author meme
 */
public class CaseExpressionResolver implements Resolver<CaseExpression> {
    @Override
    public UnaryOperator<ResolvingContext> resolve(CaseExpression caseExpression) {
        UnaryOperator<ResolvingContext> switchOp = Option.of(caseExpression.getSwitchExpression())
                                                         .map(Resolvers::resolve)
                                                         .getOrElse(UnaryOperator::identity);
        UnaryOperator<ResolvingContext> elseOp = Resolvers.resolve(caseExpression.getElseExpression());
        List<UnaryOperator<ResolvingContext>> whenOps = Stream.ofAll(caseExpression.getWhenClauses())
                                                              .map(Resolvers::resolve)
                                                              .toList();
        return context -> context.withResolver(row -> {
            List<Object> values = resolveValues(whenOps, context.withResolver(switchOp.apply(context)
                                                                                      .getResolver()), row);
            Validates.isTrue(values.size() < 2, () -> new IllegalStateException(caseExpression.toString()));
            return values.getOrElse(elseOp.apply(context)
                                          .getResolver()
                                          .apply(row));
        });

    }

    private List<Object> resolveValues(List<UnaryOperator<ResolvingContext>> whenOps, ResolvingContext context, Map<String, Object> row) {
        return whenOps.map(op -> op.apply(context))
                      .filter(ctx -> ctx.getCondition()
                                        .test(row))
                      .map(ctx -> ctx.getResolver()
                                     .apply(row));
    }
}
