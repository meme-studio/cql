package dev.memestudio.toolbox.cql.core.resolver;

import java.util.function.UnaryOperator;

/**
 * @author meme
 * @param <T>
 */
public interface Resolver<T> {

    UnaryOperator<ResolvingContext> resolve(T type);

}
