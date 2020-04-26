package dev.memestudio.toolbox.cql.core.resolver.schema;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import io.vavr.control.Option;
import net.sf.jsqlparser.schema.Column;

import java.util.function.UnaryOperator;

public class ColumnResolver implements Resolver<Column> {
    @Override
    public UnaryOperator<ResolvingContext> resolve(Column column) {
        return context -> context.withColumnName(column.getFullyQualifiedName())
                                 .withResolver(row -> Option.of(column.getFullyQualifiedName())
                                                            .flatMap(row::get)
                                                            .getOrNull());
    }

}
