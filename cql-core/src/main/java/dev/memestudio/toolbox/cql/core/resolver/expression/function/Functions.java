package dev.memestudio.toolbox.cql.core.resolver.expression.function;

import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import io.vavr.collection.Stream;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.function.UnaryOperator;

import static io.vavr.API.unchecked;

@UtilityClass
public class Functions {

    private final List<Class<? extends Function>> FUNCTION_TYPES =
            Arrays.asList(
                    Sum.class,
                    Count.class
            );

    private final Map<String, Function> FUNCTIONS = Collections.unmodifiableMap(new HashMap<String, Function>() {{
        Stream.ofAll(FUNCTION_TYPES)
              .map(unchecked(Class::newInstance))
              .forEach(function -> put(function.getName(), function));
    }});

    public UnaryOperator<ResolvingContext> apply(String name, List<UnaryOperator<ResolvingContext>> parameterOps) {
        return FUNCTIONS.get(name).apply(parameterOps);
    }

}
