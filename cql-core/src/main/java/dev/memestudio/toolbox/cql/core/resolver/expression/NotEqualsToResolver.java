package dev.memestudio.toolbox.cql.core.resolver.expression;

import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;

import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * @author meme
 */
public class NotEqualsToResolver extends BinaryExpressionResolver<NotEqualsTo> {

    @Override
    protected UnaryOperator<ResolvingContext> resolve(UnaryOperator<ResolvingContext> leftOp, UnaryOperator<ResolvingContext> rightOp) {
        return context -> context.withAppendedCondition(
                condition -> condition.and(
                        row -> !Objects.equals(leftOp.apply(context)
                                                     .getResolver()
                                                     .apply(row),
                                rightOp.apply(context)
                                       .getResolver()
                                       .apply(row))));
    }
}
