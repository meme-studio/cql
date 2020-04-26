package dev.memestudio.toolbox.cql.core.resolver.expression;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import net.sf.jsqlparser.expression.NullValue;

import java.util.function.UnaryOperator;

public class NullValueResolver implements Resolver<NullValue> {
    @Override
    public UnaryOperator<ResolvingContext> resolve(NullValue nullValue) {
        return context -> context.withResolver(__ -> null);
    }
}
