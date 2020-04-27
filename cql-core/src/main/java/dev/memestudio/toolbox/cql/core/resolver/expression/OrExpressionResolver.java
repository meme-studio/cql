package dev.memestudio.toolbox.cql.core.resolver.expression;

import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;

import java.util.function.UnaryOperator;

/**
 * @author meme
 */
public class OrExpressionResolver extends BinaryExpressionResolver<OrExpression> {

    @Override
    protected UnaryOperator<ResolvingContext> resolve(UnaryOperator<ResolvingContext> leftOp, UnaryOperator<ResolvingContext> rightOp) {
        return context -> context.withAppendedCondition(condition -> condition.and(leftOp.apply(context)
                                                                                         .getCondition()
                                                                                         .or(rightOp.apply(context)
                                                                                                    .getCondition())));
    }
}
