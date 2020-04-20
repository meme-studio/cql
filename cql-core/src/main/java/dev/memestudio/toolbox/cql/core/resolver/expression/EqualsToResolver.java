package dev.memestudio.toolbox.cql.core.resolver.expression;

import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import io.vavr.control.Option;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;

import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * @author meme
 */
public class EqualsToResolver implements Resolver<EqualsTo> {

    @Override
    public UnaryOperator<ResolvingContext> parse(EqualsTo equalsTo) {
        return context -> context.withCondition(Option.of(context.getCondition())
                                                      .getOrElse(() -> __ -> true)
                                                      .and(colObj -> Objects.equals(
                                                              Resolvers.resolve(equalsTo.getLeftExpression())
                                                                       .apply(context)
                                                                       .getResolver()
                                                                       .apply(colObj),
                                                              Resolvers.resolve(equalsTo.getRightExpression())
                                                                       .apply(context)
                                                                       .getResolver()
                                                                       .apply(colObj))
                                                      ));

    }
}
