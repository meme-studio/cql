package dev.memestudio.toolbox.cql.core.resolver.expression.function;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import lombok.Getter;

import java.util.function.Function;


public class Sum implements Fn {

    @Getter
    private final String name = "sum";

    @Override
    public Function<Map<String, Object>, Object> apply(Stream<Map<String, Object>> resultSet,
                                                       List<Function<Map<String, Object>, Object>> parameters) {
        return row -> resultSet.map(parameters.get(0)).sum();
    }
}
