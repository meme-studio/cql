package dev.memestudio.toolbox.cql.core.resolver.schema;

import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.util.Validates;
import io.vavr.control.Option;
import net.sf.jsqlparser.schema.Column;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class ColumnResolver implements Resolver<Column> {
    @Override
    public UnaryOperator<ResolvingContext> parse(Column column) {
        return context -> context.withResolver(row ->
            Option.of(row.get(column.getFullyQualifiedName()))
                  .getOrElse(() -> resolveColumnVal(column, context, row)));
    }

    private Object resolveColumnVal(Column column, ResolvingContext context, Map<String, Object> row) {
        List<Object> val = context.getTableNames()
                                  .stream()
                                  .map(tableName -> row.get(tableName + "." + column.getColumnName()))
                                  .collect(Collectors.toList());
        Validates.isTrue(val.size() < 2, () -> new IllegalStateException(column.getColumnName()));
        return val.get(0);
    }
}
