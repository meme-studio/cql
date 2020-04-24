package dev.memestudio.toolbox.cql.core.resolver.schema;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingException;
import dev.memestudio.toolbox.cql.core.util.Validates;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import net.sf.jsqlparser.schema.Column;

import java.util.Objects;
import java.util.function.UnaryOperator;

public class ColumnResolver implements Resolver<Column> {
    @Override
    public UnaryOperator<ResolvingContext> resolve(Column column) {
        return context -> context.withResolver(row ->
                row.get(column.getFullyQualifiedName())
                   .getOrElse(() -> resolveColumnValue(column, context, row)));
    }

    private Object resolveColumnValue(Column column, ResolvingContext context, Map<String, Object> row) {
        List<Object> value = context.getTableNames()
                                    .map(tableName -> row.get(tableName + "." + column.getColumnName())
                                                         .getOrNull())
                                    .filter(Objects::nonNull)
                                    .toList();
        Validates.isTrue(value.size() < 2,
                () -> new ResolvingException("Ambiguous column name '", column.getColumnName(), "'"));
        return value.getOrNull();
    }
}
