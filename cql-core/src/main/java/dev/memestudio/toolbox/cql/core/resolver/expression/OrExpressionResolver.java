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
    public UnaryOperator<ResolvingContext> parse(OrExpression orExpression) {
        return context -> context.withCondition(Option.of(context.getCondition())
                                                      .getOrElse(() -> __ -> true)
                                                      .and(Resolvers.resolve(orExpression.getLeftExpression())
                                                                     .apply(context)
                                                                     .getCondition()
                                                                     .or(Resolvers.resolve(orExpression.getRightExpression())
                                                                                  .apply(context)
                                                                                  .getCondition())));
    }
}
