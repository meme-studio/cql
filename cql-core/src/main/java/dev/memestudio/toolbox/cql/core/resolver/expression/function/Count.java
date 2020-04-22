package dev.memestudio.toolbox.cql.core.resolver.expression.function;

import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import io.vavr.collection.Stream;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;


public class Count implements Function {

    @Getter
    private final String name = "count";

    @Override
    public UnaryOperator<ResolvingContext> apply(List<UnaryOperator<ResolvingContext>> parameterOps) {
        return context -> {
            Stream<Map<String, Object>> result = context.getResult();
            Stream<Map<String, Object>> resultCopy = result.toStream();
            return context.withResolver(__ -> result.size())
                          .withResult(resultCopy);
        };
    }
}
