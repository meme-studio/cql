package dev.memestudio.toolbox.cql.core.resolver.expression.function;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import lombok.experimental.UtilityClass;

import java.util.function.Function;

import static io.vavr.API.List;
import static io.vavr.API.unchecked;

/**
 * @author meme
 */
@UtilityClass
public class Fns {

    private final List<Class<? extends Fn>> FN_TYPES =
            List(
                    Sum.class,
                    Count.class,
                    IfNull.class
            );

    private final Map<String, Fn> FNS = Stream.ofAll(FN_TYPES)
                                              .map(unchecked(Class::newInstance))
                                              .toMap(Fn::getName, Function.identity());


    public Fn get(String name) {
        return FNS.get(name)
                  .getOrElseThrow(() -> new UnsupportedOperationException(String.join("", "Function '", name, "' not supported")));
    }

}
