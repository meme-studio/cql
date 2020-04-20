package dev.memestudio.toolbox.cql.core.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

/**
 * Validates
 *
 * @author meme
 */
@UtilityClass
public class Validates {

    @SneakyThrows
    public void isTrue(final boolean expression, Supplier<Throwable> ex) {
        if (!expression) throw ex.get();
    }

}
