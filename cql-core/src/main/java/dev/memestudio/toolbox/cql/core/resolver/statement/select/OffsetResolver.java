package dev.memestudio.toolbox.cql.core.resolver.statement.select;


import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import net.sf.jsqlparser.statement.select.Offset;

import java.util.function.UnaryOperator;

public class OffsetResolver implements Resolver<Offset> {
    @Override
    public UnaryOperator<ResolvingContext> resolve(Offset offset) {
        return context -> context.withResolver(__ -> Math.toIntExact(offset.getOffset()));
    }
}
