package dev.memestudio.toolbox.cql.core.resolver.expression;

import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.function.BiPredicate;

/**
 * @author meme
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@Slf4j
@UtilityClass
public class Comparators {

    public boolean isMinorThan(Object o1, Object o2) {
        return compare(o1, o2, (_o1, _o2) -> _o1.compareTo(_o2) < 0);
    }

    public boolean isMinorThanEquals(Object o1, Object o2) {
        return compare(o1, o2, (_o1, _o2) -> _o1.compareTo(_o2) <= 0);
    }

    public boolean isGreaterThan(Object o1, Object o2) {
        return compare(o1, o2, (_o1, _o2) -> _o1.compareTo(_o2) > 0);
    }

    public boolean isGreaterThanEquals(Object o1, Object o2) {
        return compare(o1, o2, (_o1, _o2) -> _o1.compareTo(_o2) >= 0);
    }


    private Boolean compare(Object o1, Object o2, BiPredicate<Comparable, Comparable> comparableAction) {
        return Try.of(() -> Option.of(o1)
                                  .map(Comparable.class::cast)
                                  .map(_o -> comparableAction.test(_o, Option.of(o2)
                                                                             .map(Comparable.class::cast)
                                                                             .getOrNull())))
                  .onFailure(t -> log.warn("[{}] {} can not compare to [{}] {}", o1.getClass().getCanonicalName(), o1, o2.getClass().getCanonicalName(), o2))
                  .getOrElse(Option::none)
                  .getOrElse(Boolean.FALSE);
    }


}
