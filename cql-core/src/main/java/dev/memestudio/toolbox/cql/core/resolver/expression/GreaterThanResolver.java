package dev.memestudio.toolbox.cql.core.resolver.expression;

import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;

import java.util.function.UnaryOperator;

public class GreaterThanResolver extends BinaryExpressionResolver<GreaterThan> {
    @Override
    protected UnaryOperator<ResolvingContext> resolve(UnaryOperator<ResolvingContext> leftOp, UnaryOperator<ResolvingContext> rightOp) {
        return context -> context.withAppendedCondition(
                condition -> condition.and(
                        row -> Comparators.isGreaterThan(
                                leftOp.apply(context)
                                      .getResolver()
                                      .apply(row),
                                rightOp.apply(context)
                                       .getResolver()
                                       .apply(row))));
    }
}
