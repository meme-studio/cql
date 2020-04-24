package dev.memestudio.toolbox.cql.core.resolver.schema;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingException;
import io.vavr.Tuple2;
import io.vavr.control.Option;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.schema.Table;

import java.util.function.UnaryOperator;

import static io.vavr.API.List;

public class TableResolver implements Resolver<Table> {

    @Override
    public UnaryOperator<ResolvingContext> resolve(Table table) {
        String certainName = Option.of(table.getAlias())
                                   .map(Alias::getName)
                                   .getOrElse(table::getName);
        return context -> context.withTableNames(List(certainName))
                                 .withResult(context.getTables()
                                                    .get(table.getName())
                                                    .getOrElseThrow(() -> new ResolvingException("No such table '", table.getName(), "'"))
                                                    .map(row -> row.toStream()
                                                                   .map(tuple -> tuple.map1(_1 -> String.format("%s.%s", certainName, _1)))
                                                                   .toMap(Tuple2::_1, Tuple2::_2))
                                                    .toStream());
    }
}
