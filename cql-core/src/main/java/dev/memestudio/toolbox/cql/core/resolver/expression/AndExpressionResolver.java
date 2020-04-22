package dev.memestudio.toolbox.cql.core.resolver.expression;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import io.vavr.control.Option;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;

import java.util.function.UnaryOperator;

/**
 * @author meme
 */
public class AndExpressionResolver implements Resolver<AndExpression> {

    @Override
    public UnaryOperator<ResolvingContext> resolve(AndExpression andExpression) {
        UnaryOperator<ResolvingContext> leftOp = Resolvers.resolve(andExpression.getLeftExpression());
        UnaryOperator<ResolvingContext> rightOp = Resolvers.resolve(andExpression.getRightExpression());
        return context -> context.withCondition(Option.of(context.getCondition())
                                                      .getOrElse(() -> __ -> true)
                                                      .and(leftOp.apply(context)
                                                                 .getCondition()
                                                                 .and(rightOp.apply(context)
                                                                             .getCondition())));

    }
}
