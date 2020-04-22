package dev.memestudio.toolbox.cql.core.resolver.expression;

import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import io.vavr.control.Option;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;

import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * @author meme
 */
public class EqualsToResolver implements Resolver<EqualsTo> {

    @Override
    public UnaryOperator<ResolvingContext> resolve(EqualsTo equalsTo) {
        UnaryOperator<ResolvingContext> leftOp = Resolvers.resolve(equalsTo.getLeftExpression());
        UnaryOperator<ResolvingContext> rightOp = Resolvers.resolve(equalsTo.getRightExpression());
        return context -> context.withCondition(Option.of(context.getCondition())
                                                      .getOrElse(() -> __ -> true)
                                                      .and(colObj -> Objects.equals(
                                                              leftOp.apply(context)
                                                                    .getResolver()
                                                                    .apply(colObj),
                                                              rightOp.apply(context)
                                                                     .getResolver()
                                                                     .apply(colObj))));

    }
}
