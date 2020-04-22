package dev.memestudio.toolbox.cql.core.resolver.expression.function;

import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;

import java.util.List;
import java.util.function.UnaryOperator;

/**
 * @author meme
 */
public interface Function {

    String getName();

    UnaryOperator<ResolvingContext> apply(List<UnaryOperator<ResolvingContext>> parameterOps);

}
