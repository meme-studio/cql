package dev.memestudio.toolbox.cql.core.resolver.schema;

import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingException;
import dev.memestudio.toolbox.cql.core.util.Validates;
import io.vavr.control.Option;
import net.sf.jsqlparser.schema.Column;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class ColumnResolver implements Resolver<Column> {
    @Override
    public UnaryOperator<ResolvingContext> resolve(Column column) {
        return context -> context.withResolver(row ->
                Option.of(row.get(column.getFullyQualifiedName()))
                      .getOrElse(() -> resolveColumnValue(column, context, row)));
    }

    private Object resolveColumnValue(Column column, ResolvingContext context, Map<String, Object> row) {
        List<Object> value = context.getTableNames()
                                    .stream()
                                    .map(tableName -> row.get(tableName + "." + column.getColumnName()))
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.toList());
        Validates.isTrue(value.size() < 2,
                () -> new ResolvingException("Ambiguous column name '", column.getColumnName(), "'"));
        return value.isEmpty() ? null : value.get(0);
    }
}
