package dev.memestudio.toolbox.cql.core.resolver.expression.function;

import dev.memestudio.toolbox.cql.core.CqlException;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import lombok.experimental.UtilityClass;

import java.util.ServiceLoader;
import java.util.function.Function;

/**
 * @author meme
 */
@UtilityClass
public class Fns {

    private final Map<String, Fn> FNS = List.ofAll(ServiceLoader.load(Fn.class))
                                            .toMap(Fn::getName, Function.identity());

    public Fn get(String name) {
        return FNS.get(name)
                  .getOrElseThrow(() -> new CqlException("Function '", name, "' not supported"));
    }

}
