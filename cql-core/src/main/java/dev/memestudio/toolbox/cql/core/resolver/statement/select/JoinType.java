package dev.memestudio.toolbox.cql.core.resolver.statement.select;

import dev.memestudio.toolbox.cql.core.resolver.ResolvingException;
import io.vavr.Function3;
import io.vavr.Tuple2;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import io.vavr.control.Try;
import lombok.experimental.UtilityClass;
import net.sf.jsqlparser.statement.select.Join;

import java.util.function.Predicate;

import static io.vavr.API.*;
import static io.vavr.Predicates.isIn;

@UtilityClass
public class JoinType {


    private final int JOIN_TYPE_NULL = 0;
    private final int JOIN_TYPE_OUTER = 1;
    private final int JOIN_TYPE_RIGHT = 1 << 1;
    private final int JOIN_TYPE_LEFT = 1 << 2;
    private final int JOIN_TYPE_NATURAL = 1 << 3;
    private final int JOIN_TYPE_FULL = 1 << 4;
    private final int JOIN_TYPE_INNER = 1 << 5;
    private final int JOIN_TYPE_SIMPLE = 1 << 6;
    private final int JOIN_TYPE_CROSS = 1 << 7;
    private final int JOIN_TYPE_SEMI = 1 << 8;
    private final int JOIN_TYPE_STRAIGHT = 1 << 9;

    public Function3<Stream<Map<String, Object>>, Stream<Map<String, Object>>, Predicate<Map<String, Object>>, Stream<Map<String, Object>>> getResolver(Join join) {
        int types =
                Stream.of(
                        join.isOuter(),
                        join.isRight(),
                        join.isLeft(),
                        join.isNatural(),
                        join.isFull(),
                        join.isInner(),
                        join.isSimple(),
                        join.isCross(),
                        join.isSemi(),
                        join.isStraight())
                      .zipWithIndex()
                      .map(tuple2 -> tuple2.map2(index -> 1 << index))
                      .filter(Tuple2::_1)
                      .map(Tuple2::_2)
                      .sum()
                      .intValue();
        return getResolver(types, join.toString());
    }

    private Function3<Stream<Map<String, Object>>, Stream<Map<String, Object>>, Predicate<Map<String, Object>>, Stream<Map<String, Object>>> getResolver(int types, String joinExp) {
        return Match(types).of(
                Case($(isIn(JOIN_TYPE_LEFT, JOIN_TYPE_LEFT | JOIN_TYPE_OUTER)), (left, right, condition) -> left.flatMap(leftRow -> right.map(leftRow::merge)
                                                                                                                                         .filter(condition)
                                                                                                                                         .orElse(() -> Stream.of(leftRow)))),
                Case($(isIn(JOIN_TYPE_RIGHT, JOIN_TYPE_RIGHT | JOIN_TYPE_OUTER)), (left, right, condition) -> right.flatMap(rightRow -> left.map(rightRow::merge)
                                                                                                                                            .filter(condition)
                                                                                                                                            .orElse(() -> Stream.of(rightRow)))),
                Case($(), () -> Try.<Function3<Stream<Map<String, Object>>, Stream<Map<String, Object>>, Predicate<Map<String, Object>>, Stream<Map<String, Object>>>>of(() -> {
                    throw new ResolvingException("Join type '", joinExp, "' not supported");
                }).get())
        );
    }


}
