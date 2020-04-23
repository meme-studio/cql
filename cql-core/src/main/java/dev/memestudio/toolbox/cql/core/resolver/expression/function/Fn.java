package dev.memestudio.toolbox.cql.core.resolver.expression.function;

import io.vavr.collection.Stream;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 *
 * @author meme
 */
public interface Fn {

    String getName();

    Function<Map<String, Object>, Object> apply(Stream<Map<String, Object>> resultSet, List<Function<Map<String, Object>, Object>> parameters);

}
