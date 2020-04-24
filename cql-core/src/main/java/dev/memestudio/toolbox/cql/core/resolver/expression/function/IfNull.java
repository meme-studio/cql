package dev.memestudio.toolbox.cql.core.resolver.expression.function;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import lombok.Getter;

import java.util.function.Function;

public class IfNull implements Fn {

    @Getter
    private final String name = "ifnull";

    @Override
    public Function<Map<String, Object>, Object> apply(Stream<Map<String, Object>> resultSet,
                                                       List<Function<Map<String, Object>, Object>> parameters) {
        return row -> Option.of(parameters.get(0).apply(row))
                            .getOrElse(() -> parameters.get(1).apply(row));
    }
}
