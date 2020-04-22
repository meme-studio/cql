package dev.memestudio.toolbox.cql.core.resolver.statement.select;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import dev.memestudio.toolbox.cql.core.util.ObjectTransformer;
import dev.memestudio.toolbox.cql.core.util.Validates;
import io.vavr.Function3;
import io.vavr.Tuple2;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import net.sf.jsqlparser.statement.select.Join;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * @author meme
 */
public class JoinResolver implements Resolver<Join> {

    private static final int JOIN_TYPE_OUTER = 1;
    private static final int JOIN_TYPE_RIGHT = 1 << 1;
    private static final int JOIN_TYPE_LEFT = 1 << 2;
    private static final int JOIN_TYPE_NATURAL = 1 << 3;
    private static final int JOIN_TYPE_FULL = 1 << 4;
    private static final int JOIN_TYPE_INNER = 1 << 5;
    private static final int JOIN_TYPE_SIMPLE = 1 << 6;
    private static final int JOIN_TYPE_CROSS = 1 << 7;
    private static final int JOIN_TYPE_SEMI = 1 << 8;
    private static final int JOIN_TYPE_STRAIGHT = 1 << 9;

    private static final Map<Integer, Function3<Stream<Map<String, Object>>, Stream<Map<String, Object>>, Predicate<Map<String, Object>>, Stream<Map<String, Object>>>> joinTypeResolvers =
            new HashMap<Integer, Function3<Stream<Map<String, Object>>, Stream<Map<String, Object>>, Predicate<Map<String, Object>>, Stream<Map<String, Object>>>>() {{
                put(JOIN_TYPE_LEFT, (left, right, condition) -> left.flatMap(leftRow -> right.map(rightRow -> ObjectTransformer.combineRows(leftRow, rightRow))
                                                                                              .filter(condition)
                                                                                              .orElse(() -> Stream.of(leftRow))));
                put(JOIN_TYPE_RIGHT, (left, right, condition) -> right.flatMap(rightRow -> left.map(leftRow -> ObjectTransformer.combineRows(leftRow, rightRow))
                                                                                              .filter(condition)
                                                                                              .orElse(() -> Stream.of(rightRow))));
            }};

    @Override
    public UnaryOperator<ResolvingContext> resolve(Join join) {
        UnaryOperator<ResolvingContext> rightItemResolving = Resolvers.resolve(join.getRightItem());
        Option<UnaryOperator<ResolvingContext>> expressionResolving = Option.of(join.getOnExpression())
                                                                            .map(Resolvers::resolve);
        return context -> {
            Stream<Map<String, Object>> left = context.getResult();

            ResolvingContext ctx = rightItemResolving.apply(context);
            Stream<Map<String, Object>> right = ctx.getResult();

            List<String> tableNames = new ArrayList<>(context.getTableNames());
            tableNames.addAll(ctx.getTableNames());
            Predicate<Map<String, Object>> condition = expressionResolving.map(op -> op.apply(context.withTableNames(tableNames)))
                                                                          .map(ResolvingContext::getCondition)
                                                                          .getOrElse(() -> __ -> true);
            return context.withTableNames(tableNames)
                          .withResult(getJoinTypeResolver(join).apply(left, right, condition));
        };
    }

    private Function3<Stream<Map<String, Object>>, Stream<Map<String, Object>>, Predicate<Map<String, Object>>, Stream<Map<String, Object>>> getJoinTypeResolver(Join join) {
        int types = Stream.of(
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
        Function3<Stream<Map<String, Object>>, Stream<Map<String, Object>>, Predicate<Map<String, Object>>, Stream<Map<String, Object>>> joinTypeResolver = joinTypeResolvers.get(types);
        Validates.isTrue(Objects.nonNull(joinTypeResolver), () -> new UnsupportedOperationException(String.join("", "Join Syntax '", join.toString(), "' not supported")));
        return joinTypeResolver;
    }


}
