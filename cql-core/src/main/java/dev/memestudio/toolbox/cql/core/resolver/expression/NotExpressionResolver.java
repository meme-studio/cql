package dev.memestudio.toolbox.cql.core.resolver.expression;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import net.sf.jsqlparser.expression.NotExpression;

import java.util.function.UnaryOperator;

/**
 * @author meme
 */
public class NotExpressionResolver implements Resolver<NotExpression> {

    @Override
    public UnaryOperator<ResolvingContext> resolve(NotExpression notExpression) {
        UnaryOperator<ResolvingContext> notOp = Resolvers.resolve(notExpression.getExpression());
        return context -> context.withAppendedCondition(condition -> condition.and(notOp.apply(context)
                                                                                        .getCondition()
                                                                                        .negate()));

    }
}
