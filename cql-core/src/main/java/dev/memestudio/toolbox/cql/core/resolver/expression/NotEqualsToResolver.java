package dev.memestudio.toolbox.cql.core.resolver.expression;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import io.vavr.control.Option;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;

import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * @author meme
 */
public class NotEqualsToResolver implements Resolver<NotEqualsTo> {

    @Override
    public UnaryOperator<ResolvingContext> parse(NotEqualsTo notEqualsTo) {
        return context -> context.withCondition(Option.of(context.getCondition())
                                                      .getOrElse(() -> __ -> true)
                                                      .and(colObj ->
                                                            !Objects.equals(
                                                                    Resolvers.resolve(notEqualsTo.getLeftExpression())
                                                                             .apply(context)
                                                                             .getResolver()
                                                                             .apply(colObj),
                                                                    Resolvers.resolve(notEqualsTo.getRightExpression())
                                                                             .apply(context)
                                                                             .getResolver()
                                                                             .apply(colObj))
                                                    ));

    }
}
