package dev.memestudio.toolbox.cql.core.resolver.statement.select;

import dev.memestudio.toolbox.cql.core.CqlException;
import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import io.vavr.collection.Map;
import net.sf.jsqlparser.statement.select.OrderByElement;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.UnaryOperator;

import static io.vavr.API.Option;

public class OrderByElementResolver implements Resolver<OrderByElement> {

    @SuppressWarnings({"unchecked"})
    @Override
    public UnaryOperator<ResolvingContext> resolve(OrderByElement orderByElement) {
        OrderByElement.NullOrdering nullOrdering = orderByElement.getNullOrdering();
        boolean asc = orderByElement.isAsc();
        UnaryOperator<ResolvingContext> colOp = Resolvers.resolve(orderByElement.getExpression());
        return context ->
                Option(colOp.apply(context)
                            .getColumnName())
                        .map(columnName -> {
                            Comparator<Map<String, Object>> comparator =
                                    Comparator.comparing(row -> row.get(columnName)
                                                                   .map(Comparable.class::cast)
                                                                   .getOrElseThrow(() -> new CqlException("No such column '", columnName, "'")));
                            comparator = asc ? comparator : comparator.reversed();
                            comparator = Objects.equals(nullOrdering, OrderByElement.NullOrdering.NULLS_FIRST)
                                    ? Comparator.nullsFirst(comparator) : Comparator.nullsLast(comparator);
                            return context.withComparator(comparator);
                        })
                        .getOrElse(context.withComparator((row1, row2) -> 0));
    }
}
