package dev.memestudio.toolbox.cql.core.resolver.statement.select;

import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import net.sf.jsqlparser.statement.select.Join;

import java.util.function.UnaryOperator;

/**
 * @author meme
 */
public class JoinResolver implements Resolver<Join> {

    @Override
    public UnaryOperator<ResolvingContext> parse(Join join) {
        return context -> {
            Resolvers.resolve(join.getRightItem()).apply(context);
            Resolvers.resolve(join.getOnExpression()).apply(context);
            return context;
        };
    }
}
