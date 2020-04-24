package dev.memestudio.toolbox.cql.core.resolver.expression;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingException;
import dev.memestudio.toolbox.cql.core.util.Validates;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import net.sf.jsqlparser.expression.CaseExpression;

import java.util.function.UnaryOperator;

import static io.vavr.API.Option;

/**
 * @author meme
 */
public class CaseExpressionResolver implements Resolver<CaseExpression> {
    @Override
    public UnaryOperator<ResolvingContext> resolve(CaseExpression caseExpression) {
        UnaryOperator<ResolvingContext> switchOp = Option.of(caseExpression.getSwitchExpression())
                                                         .map(Resolvers::resolve)
                                                         .getOrElse(UnaryOperator::identity);
        List<UnaryOperator<ResolvingContext>> whenOps = Stream.ofAll(caseExpression.getWhenClauses())
                                                              .map(Resolvers::resolve)
                                                              .toList();
        UnaryOperator<ResolvingContext> elseOp =
                Option(caseExpression.getElseExpression())
                        .map(Resolvers::resolve)
                        .getOrElse(context -> context.withResolver(__ -> null));

        return context -> context.withResolver(row -> {
            List<Object> value = whenOps.map(op -> op.apply(context.withResolver(switchOp.apply(context)
                                                                                         .getResolver())))
                                        .filter(ctx -> ctx.getCondition()
                                                          .test(row))
                                        .map(ctx -> ctx.getResolver()
                                                       .apply(row));
            Validates.isTrue(value.size() < 2,
                    () -> new ResolvingException("Ambiguous case when conditions at '", caseExpression.toString(), "'"));
            return value.getOrElse(elseOp.apply(context)
                                         .getResolver()
                                         .apply(row));
        });

    }
}
