package dev.memestudio.toolbox.cql.core.resolver.statement.select;

import dev.memestudio.toolbox.cql.core.CqlException;
import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.statement.select.TableFunction;

import java.util.function.UnaryOperator;

import static io.vavr.API.Option;

public class TableFunctionResolver implements Resolver<TableFunction> {
    @Override
    public UnaryOperator<ResolvingContext> resolve(TableFunction tableFunction) {
        String tableName = Option(tableFunction.getAlias()).map(Alias::getName)
                                                           .getOrElseThrow(
                                                                   () -> new CqlException("Table Function '", tableFunction.toString(), "' must has certainly table alias"));
        return context -> {
            //TODO
            return null;
        };
    }
}
