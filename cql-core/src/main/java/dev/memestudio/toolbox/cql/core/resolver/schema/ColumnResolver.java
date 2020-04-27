package dev.memestudio.toolbox.cql.core.resolver.schema;

import dev.memestudio.toolbox.cql.core.CqlException;
import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import dev.memestudio.toolbox.cql.core.util.Validates;
import io.vavr.control.Option;
import net.sf.jsqlparser.schema.Column;

import java.util.Objects;
import java.util.function.UnaryOperator;

public class ColumnResolver implements Resolver<Column> {
    @Override
    public UnaryOperator<ResolvingContext> resolve(Column column) {
        Validates.isTrue(Objects.nonNull(column.getTable()),
                () -> new CqlException("Column '", column.getFullyQualifiedName(), "' must has certainly table name"));
        return context -> context.withColumnName(column.getFullyQualifiedName())
                                 .withResolver(row -> Option.of(column.getFullyQualifiedName())
                                                            .flatMap(row::get)
                                                            .getOrNull());
    }

}
