package dev.memestudio.toolbox.cql.core.resolver.expression;

import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import io.vavr.control.Option;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;

import java.util.function.UnaryOperator;

public class MinorThanEqualsResolver extends BinaryExpressionResolver<MinorThanEquals> {
    @Override
    protected UnaryOperator<ResolvingContext> resolve(UnaryOperator<ResolvingContext> leftOp, UnaryOperator<ResolvingContext> rightOp) {
        return context -> context.withCondition(Option.of(context.getCondition())
                                                      .getOrElse(() -> __ -> true)
                                                      .and(row ->
                                                              Comparators.isMinorThanEquals(
                                                                      leftOp.apply(context)
                                                                            .getResolver()
                                                                            .apply(row),
                                                                      rightOp.apply(context)
                                                                             .getResolver()
                                                                             .apply(row))));
    }

}
