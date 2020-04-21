package dev.memestudio.toolbox.cql.core.resolver.statement.select;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import net.sf.jsqlparser.statement.select.PlainSelect;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class PlainSelectResolver implements Resolver<PlainSelect> {

    @Override
    public UnaryOperator<ResolvingContext> parse(PlainSelect select) {
        return context -> {
            //from where group-by avg,sum having select distinct order-by limit
            context = Option.of(select.getJoins())
                            .map(Resolvers::resolve)
                            .getOrElse(UnaryOperator.identity())
                            .apply(Option.of(select.getFromItem())
                                         .map(Resolvers::resolve)
                                         .getOrElse(ctx -> ctx.withResult(Stream.empty()))
                                         .apply(context));
            Stream<Map<String, Object>> result = context.getResult();

            if (!result.isEmpty()) {
                context = Option.of(select.getWhere())
                                .map(Resolvers::resolve)
                                .getOrElse(ctx -> ctx.withCondition(__ -> true))
                                .apply(context);
                Predicate<Map<String, Object>> condition = context.getCondition();

                context = Resolvers.resolve(select.getSelectItems()).apply(context);
                UnaryOperator<Map<String, Object>> mapper = context.getMapper();

                context = Option.of(select.getOrderByElements())
                                .map(Resolvers::resolve)
                                .getOrElse(ctx -> ctx.withSort((__, ___) -> 0))
                                .apply(context);
                Comparator<Map<String, Object>> sort = context.getSort();

                context = Option.of(select.getOffset())
                                .map(Resolvers::resolve)
                                .getOrElse(ctx -> ctx.withSkip(() -> 0))
                                .apply(context);
                int skip = context.getSkip().getAsInt();

                context = Option.of(select.getLimit())
                                .map(Resolvers::resolve)
                                .getOrElse(ctx -> ctx.withLimit(() -> Integer.MAX_VALUE))
                                .apply(context);
                int limit = context.getLimit().getAsInt();

                //noinspection ComparatorMethodParameterNotUsed
                result = result.filter(condition)
                               .map(mapper)
                               .distinctBy((__, ___) -> Objects.isNull(select.getDistinct()) ? 1 : 0)
                               .sorted(sort)
                               .drop(skip)
                               .take(limit);
            }

            return context.withResult(result);
        };
    }
}
