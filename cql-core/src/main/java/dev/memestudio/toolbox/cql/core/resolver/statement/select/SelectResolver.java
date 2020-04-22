package dev.memestudio.toolbox.cql.core.resolver.statement.select;

import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import net.sf.jsqlparser.statement.select.Select;

import java.util.function.UnaryOperator;

public class SelectResolver implements Resolver<Select> {

    @Override
    public UnaryOperator<ResolvingContext> resolve(Select select) {
        return Resolvers.resolve(select.getSelectBody());
    }
}
