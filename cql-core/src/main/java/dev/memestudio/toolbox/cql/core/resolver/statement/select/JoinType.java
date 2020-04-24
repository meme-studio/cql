package dev.memestudio.toolbox.cql.core.resolver.statement.select;

import dev.memestudio.toolbox.cql.core.resolver.ResolvingException;
import io.vavr.Function3;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import lombok.experimental.UtilityClass;
import net.sf.jsqlparser.statement.select.Join;

import java.util.function.Predicate;

@UtilityClass
public class JoinType {

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

    private final Map<Integer, Function3<Stream<Map<String, Object>>, Stream<Map<String, Object>>, Predicate<Map<String, Object>>, Stream<Map<String, Object>>>> joinTypeResolvers =
            HashMap.<Integer, Function3<Stream<Map<String, Object>>, Stream<Map<String, Object>>, Predicate<Map<String, Object>>, Stream<Map<String, Object>>>>empty()
                    .put(JOIN_TYPE_LEFT, (left, right, condition) -> left.flatMap(leftRow -> right.map(leftRow::merge)
                                                                                                  .filter(condition)
                                                                                                  .orElse(() -> Stream.of(leftRow))))
                    .put(JOIN_TYPE_RIGHT, (left, right, condition) -> right.flatMap(rightRow -> left.map(rightRow::merge)
                                                                                                    .filter(condition)
                                                                                                    .orElse(() -> Stream.of(rightRow))));

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
        return joinTypeResolvers.get(types)
                                .getOrElseThrow(() -> new ResolvingException("Join type '", join.toString(), "' not supported"));
    }

}
