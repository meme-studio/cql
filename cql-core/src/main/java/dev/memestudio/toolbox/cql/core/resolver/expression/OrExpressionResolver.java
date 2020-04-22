package dev.memestudio.toolbox.cql.core.resolver.expression;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import io.vavr.control.Option;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;

import java.util.function.UnaryOperator;

/**
 * @author meme
 */
public class OrExpressionResolver implements Resolver<OrExpression> {

    @Override
    public UnaryOperator<ResolvingContext> resolve(OrExpression orExpression) {
        UnaryOperator<ResolvingContext> leftOp = Resolvers.resolve(orExpression.getLeftExpression());
        UnaryOperator<ResolvingContext> rightOp = Resolvers.resolve(orExpression.getRightExpression());
        return context -> context.withCondition(Option.of(context.getCondition())
                                                      .getOrElse(() -> __ -> true)
                                                      .and(leftOp.apply(context)
                                                                 .getCondition()
                                                                 .or(rightOp.apply(context)
                                                                            .getCondition())));
    }
}
