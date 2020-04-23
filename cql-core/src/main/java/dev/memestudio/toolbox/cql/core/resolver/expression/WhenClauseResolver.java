package dev.memestudio.toolbox.cql.core.resolver.expression;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import net.sf.jsqlparser.expression.WhenClause;

import java.util.Objects;
import java.util.function.UnaryOperator;

import static io.vavr.API.Option;

public class WhenClauseResolver implements Resolver<WhenClause> {
    @Override
    public UnaryOperator<ResolvingContext> resolve(WhenClause whenClause) {
        UnaryOperator<ResolvingContext> whenOp = Resolvers.resolve(whenClause.getWhenExpression());
        UnaryOperator<ResolvingContext> thenOp = Resolvers.resolve(whenClause.getThenExpression());
        return context ->
                Option(context.getResolver())
                        .map(resolver -> context.withCondition(
                                row -> Objects.equals(
                                        resolver.apply(row),
                                        whenOp.apply(context)
                                              .getResolver()
                                              .apply(row))))
                        .getOrElse(() -> context.withCondition(whenOp.apply(context)
                                                                     .getCondition()))
                        .withResolver(thenOp.apply(context)
                                            .getResolver());
    }
}
