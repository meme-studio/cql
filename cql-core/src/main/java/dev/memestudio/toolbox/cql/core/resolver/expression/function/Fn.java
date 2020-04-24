package dev.memestudio.toolbox.cql.core.resolver.expression.function;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;

import java.util.function.Function;

/**
 *
 * @author meme
 */
public interface Fn {

    String getName();

    Function<Map<String, Object>, Object> apply(Stream<Map<String, Object>> resultSet, List<Function<Map<String, Object>, Object>> parameters);

}
