package dev.memestudio.toolbox.cql.core.resolver.expression;

import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;

import java.util.function.UnaryOperator;

public class GreaterThanEqualsResolver extends BinaryExpressionResolver<GreaterThanEquals> {

    @Override
    protected UnaryOperator<ResolvingContext> resolve(UnaryOperator<ResolvingContext> leftOp, UnaryOperator<ResolvingContext> rightOp) {
        return context -> context.withAppendedCondition(
                condition -> condition.and(
                        row -> Comparators.isGreaterThanEquals(
                                leftOp.apply(context)
                                      .getResolver()
                                      .apply(row),
                                rightOp.apply(context)
                                       .getResolver()
                                       .apply(row))));
    }
}
