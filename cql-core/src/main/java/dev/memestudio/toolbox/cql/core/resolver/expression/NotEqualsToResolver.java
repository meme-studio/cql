package dev.memestudio.toolbox.cql.core.resolver.expression;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import io.vavr.control.Option;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;

import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * @author meme
 */
public class NotEqualsToResolver implements Resolver<NotEqualsTo> {

    @Override
    public UnaryOperator<ResolvingContext> resolve(NotEqualsTo notEqualsTo) {
        UnaryOperator<ResolvingContext> leftOp = Resolvers.resolve(notEqualsTo.getLeftExpression());
        UnaryOperator<ResolvingContext> rightOp = Resolvers.resolve(notEqualsTo.getRightExpression());
        return context -> context.withCondition(Option.of(context.getCondition())
                                                      .getOrElse(() -> __ -> true)
                                                      .and(colObj -> !Objects.equals(
                                                              leftOp.apply(context)
                                                                    .getResolver()
                                                                    .apply(colObj),
                                                              rightOp.apply(context)
                                                                     .getResolver()
                                                                     .apply(colObj))));

    }
}
