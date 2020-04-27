package dev.memestudio.toolbox.cql.core.resolver.schema;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import dev.memestudio.toolbox.cql.core.util.SchemaUtil;
import io.vavr.control.Option;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.schema.Table;

import java.util.function.UnaryOperator;

public class TableResolver implements Resolver<Table> {

    @Override
    public UnaryOperator<ResolvingContext> resolve(Table table) {
        String certainTableName = SchemaUtil.removeGraveAccents(Option.of(table.getAlias())
                                                                      .map(Alias::getName)
                                                                      .getOrElse(table::getName));
        String tableName = SchemaUtil.removeGraveAccents(table.getName());
        return context -> context.withResult(context.getDatasource()
                                                    .getTable(tableName)
                                                    .map(row -> row.mapKeys(columnName -> SchemaUtil.extendColumnName(certainTableName, columnName)))
                                                    .toStream());
    }

}
