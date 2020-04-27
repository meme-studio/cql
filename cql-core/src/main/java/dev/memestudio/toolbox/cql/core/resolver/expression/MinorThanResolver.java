package dev.memestudio.toolbox.cql.core.resolver.expression;

import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;

import java.util.function.UnaryOperator;

public class MinorThanResolver extends BinaryExpressionResolver<MinorThan> {
    @Override
    protected UnaryOperator<ResolvingContext> resolve(UnaryOperator<ResolvingContext> leftOp, UnaryOperator<ResolvingContext> rightOp) {
        return context -> context.withAppendedCondition(
                condition -> condition.and(
                        row -> Comparators.isMinorThan(
                                leftOp.apply(context)
                                      .getResolver()
                                      .apply(row),
                                rightOp.apply(context)
                                       .getResolver()
                                       .apply(row))));
    }
}
