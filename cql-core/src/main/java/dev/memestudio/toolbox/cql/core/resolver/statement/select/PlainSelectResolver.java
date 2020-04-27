package dev.memestudio.toolbox.cql.core.resolver.statement.select;

import dev.memestudio.toolbox.cql.core.CqlException;
import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import dev.memestudio.toolbox.cql.core.util.Validates;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.PlainSelect;

import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;

import static io.vavr.API.Option;
import static io.vavr.Predicates.noneOf;

public class PlainSelectResolver implements Resolver<PlainSelect> {

    @Override
    public UnaryOperator<ResolvingContext> resolve(PlainSelect select) {
        //from where group-by avg,sum having select distinct order-by limit
        UnaryOperator<ResolvingContext> formOp = formOp(select);
        UnaryOperator<ResolvingContext> joinsOp = joinOp(select);
        UnaryOperator<ResolvingContext> whereOp = whereOp(select);
        UnaryOperator<ResolvingContext> groupByOp = groupByOp(select);
        UnaryOperator<ResolvingContext> havingOp = havingOp(select);
        UnaryOperator<ResolvingContext> distinctOp = distinctOp(select);
        UnaryOperator<ResolvingContext> orderByOp = orderByOp(select);
        UnaryOperator<ResolvingContext> selectOp = selectOp(select);
        UnaryOperator<ResolvingContext> offsetOp = offsetOp(select);
        UnaryOperator<ResolvingContext> limitOp = limitOp(select);

        return context ->
                limitOp.apply(
                        offsetOp.apply(
                                selectOp.apply(
                                        orderByOp.apply(
                                                distinctOp.apply(
                                                        havingOp.apply(
                                                                groupByOp.apply(
                                                                        whereOp.apply(
                                                                                joinsOp.apply(
                                                                                        formOp.apply(context))))))))));
    }

    private UnaryOperator<ResolvingContext> distinctOp(PlainSelect select) {
        return Option.of(select.getDistinct())
                     .map(Resolvers::resolve)
                     .getOrElse(UnaryOperator::identity);
    }

    private UnaryOperator<ResolvingContext> havingOp(PlainSelect select) {
        return Option.of(select.getHaving())
                     .map(Resolvers::resolve)
                     .getOrElse(UnaryOperator::identity);
    }

    private UnaryOperator<ResolvingContext> groupByOp(PlainSelect select) {
        return Option.of(select.getGroupBy())
                     .map(Resolvers::resolve)
                     .getOrElse(UnaryOperator::identity);
    }

    private UnaryOperator<ResolvingContext> selectOp(PlainSelect select) {
        return Option(select.getSelectItems())
                .map(Stream::ofAll)
                .map(joins -> joins.map(Resolvers::resolve)
                                   .toList())
                .<UnaryOperator<ResolvingContext>>map(
                        ops ->
                                context ->
                                        context.withTransformResult(result ->
                                                result.map(row -> Stream.ofAll(ops)
                                                                        .map(op -> op.apply(context))
                                                                        .map(ResolvingContext::getMapper)
                                                                        .flatMap(mapper -> mapper.apply(row))
                                                                        .toMap(Tuple2::_1, Tuple2::_2))))
                .getOrElse(UnaryOperator::identity);
    }

    private UnaryOperator<ResolvingContext> limitOp(PlainSelect select) {
        return Option.of(select.getLimit())
                     .filter(noneOf(Limit::isLimitAll, Limit::isLimitNull))
                     .map(Limit::getRowCount)
                     .map(Resolvers::resolve)
                .<UnaryOperator<ResolvingContext>>map(op ->
                        context -> context.withTransformResult(result -> result.take(((Number) op.apply(context)
                                                                                                 .getResolver()
                                                                                                 .apply(null)).intValue())))
                .getOrElse(UnaryOperator::identity);
    }

    private UnaryOperator<ResolvingContext> offsetOp(PlainSelect select) {
        Validates.isTrue(
                !(Objects.nonNull(select.getOffset()) &&
                        Objects.nonNull(Option(select.getLimit()).map(Limit::getOffset)
                                                                 .getOrNull())),
                () -> new CqlException("Duplicate defined offset"));

        return Option(select.getOffset()).map(Resolvers::resolve)
                                         .orElse(() -> Option(select.getLimit()).map(Limit::getOffset)
                                                                                .map(Resolvers::resolve))
                .<UnaryOperator<ResolvingContext>>map(op ->
                        context -> context.withTransformResult(result -> result.drop(((Number) op.apply(context)
                                                                                                 .getResolver()
                                                                                                 .apply(null)).intValue())))
                .getOrElse(UnaryOperator::identity);
    }


    private UnaryOperator<ResolvingContext> orderByOp(PlainSelect select) {
        return Option.of(select.getOrderByElements())
                     .map(List::ofAll)
                     .map(orderBys -> orderBys.map(Resolvers::resolve))
                .<UnaryOperator<ResolvingContext>>map(
                        ops ->
                                context ->
                                        context.withTransformResult(
                                                result -> result.sorted(ops.map(op -> op.apply(context))
                                                                           .map(ResolvingContext::getComparator)
                                                                           .reduceLeft(Comparator::thenComparing))))
                .getOrElse(UnaryOperator::identity);
    }

    private UnaryOperator<ResolvingContext> whereOp(PlainSelect select) {
        return Option.of(select.getWhere())
                     .map(Resolvers::resolve)
                .<UnaryOperator<ResolvingContext>>map(op ->
                        ctx -> ctx.withTransformResult(
                                result -> result.filter(Option(op.apply(ctx)
                                                                 .getCondition())
                                        .getOrElse(() -> __ -> true))))
                .getOrElse(UnaryOperator::identity);
    }

    private UnaryOperator<ResolvingContext> formOp(PlainSelect select) {
        return Option.of(select.getFromItem())
                     .map(Resolvers::resolve)
                     .getOrElse(ctx -> ctx.withResult(Stream.empty()));
    }

    private UnaryOperator<ResolvingContext> joinOp(PlainSelect select) {
        return Option(select.getJoins())
                .map(List::ofAll)
                .map(joins -> joins.map(Resolvers::resolve))
                .<UnaryOperator<ResolvingContext>>map(ops -> context -> {
                    AtomicReference<ResolvingContext> ref = new AtomicReference<>(context);
                    return context.withResult(Stream.ofAll(ops)
                                                    .map(op -> op.apply(ref.get()))
                                                    .peek(ref::set)
                                                    .last()
                                                    .getResult());
                })
                .getOrElse(UnaryOperator::identity);
    }
}
