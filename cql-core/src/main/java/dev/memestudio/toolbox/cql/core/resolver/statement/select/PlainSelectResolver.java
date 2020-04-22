package dev.memestudio.toolbox.cql.core.resolver.statement.select;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import net.sf.jsqlparser.statement.select.PlainSelect;

import java.util.function.UnaryOperator;

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
        UnaryOperator<ResolvingContext> selectOp = Resolvers.resolve(select.getSelectItems());
        return context -> context.withResult(context.getResult()
                                                    .map(selectOp.apply(context)
                                                                 .getMapper()));
    }

    private UnaryOperator<ResolvingContext> limitOp(PlainSelect select) {
        return Option.of(select.getLimit())
                     .map(Resolvers::resolve)
                .<UnaryOperator<ResolvingContext>>map(op ->
                        ctx -> ctx.withResult(ctx.getResult()
                                                 .take(op.apply(ctx)
                                                         .getLimit()
                                                         .getAsInt())))
                .getOrElse(UnaryOperator::identity);
    }

    private UnaryOperator<ResolvingContext> offsetOp(PlainSelect select) {
        return Option.of(select.getOffset())
                     .map(Resolvers::resolve)
                .<UnaryOperator<ResolvingContext>>map(op ->
                        ctx -> ctx.withResult(ctx.getResult()
                                                 .take(op.apply(ctx)
                                                         .getOffset()
                                                         .getAsInt())))
                .getOrElse(UnaryOperator::identity);
    }

    private UnaryOperator<ResolvingContext> orderByOp(PlainSelect select) {
        return Option.of(select.getOrderByElements())
                     .map(Resolvers::resolve)
                .<UnaryOperator<ResolvingContext>>map(op ->
                        ctx -> ctx.withResult(ctx.getResult()
                                                 .sorted(op.apply(ctx)
                                                           .getSort())))
                .getOrElse(UnaryOperator::identity);
    }

    private UnaryOperator<ResolvingContext> whereOp(PlainSelect select) {
        return Option.of(select.getWhere())
                     .map(Resolvers::resolve)
                .<UnaryOperator<ResolvingContext>>map(op ->
                        ctx -> ctx.withResult(ctx.getResult()
                                                 .filter(op.apply(ctx)
                                                           .getCondition())))
                .getOrElse(UnaryOperator::identity);
    }

    private UnaryOperator<ResolvingContext> formOp(PlainSelect select) {
        return Option.of(select.getFromItem())
                     .map(Resolvers::resolve)
                     .getOrElse(ctx -> ctx.withResult(Stream.empty()));
    }

    private UnaryOperator<ResolvingContext> joinOp(PlainSelect select) {
        return Option.of(select.getJoins())
                     .map(Resolvers::resolve)
                .<UnaryOperator<ResolvingContext>>map(op ->
                        ctx -> ctx.withResult(op.apply(ctx).getResult()))
                .getOrElse(UnaryOperator::identity);
    }
}
