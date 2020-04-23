package dev.memestudio.toolbox.cql.core.resolver.statement.select;

import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import net.sf.jsqlparser.statement.select.AllColumns;

import java.util.function.UnaryOperator;

/**
 * @author meme
 */
public class AllColumnsResolver implements Resolver<AllColumns> {
    @Override
    public UnaryOperator<ResolvingContext> resolve(AllColumns allColumns) {
        return context -> context.withSingleResultSet(false)
                                 .withMapper(UnaryOperator.identity());
    }
}
