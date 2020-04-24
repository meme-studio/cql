package dev.memestudio.toolbox.cql.core.resolver.statement.select;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import io.vavr.Function3;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import net.sf.jsqlparser.statement.select.Join;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * @author meme
 */
public class JoinResolver implements Resolver<Join> {

    @Override
    public UnaryOperator<ResolvingContext> resolve(Join join) {
        UnaryOperator<ResolvingContext> rightItemResolving = Resolvers.resolve(join.getRightItem());
        Option<UnaryOperator<ResolvingContext>> expressionResolving = Option.of(join.getOnExpression())
                                                                            .map(Resolvers::resolve);
        Function3<Stream<Map<String, Object>>, Stream<Map<String, Object>>, Predicate<Map<String, Object>>, Stream<Map<String, Object>>> resolver =
                JoinType.getResolver(join);
        return context -> {
            Stream<Map<String, Object>> left = context.getResult();
            ResolvingContext ctx = rightItemResolving.apply(context);
            Stream<Map<String, Object>> right = ctx.getResult();
            List<String> tableNames = context.getTableNames()
                                             .appendAll(ctx.getTableNames());

            Predicate<Map<String, Object>> condition = expressionResolving.map(op -> op.apply(context.withTableNames(tableNames)))
                                                                          .map(ResolvingContext::getCondition)
                                                                          .getOrElse(() -> __ -> true);
            return context.withTableNames(tableNames)
                          .withResult(resolver.apply(left, right, condition));
        };
    }

}
