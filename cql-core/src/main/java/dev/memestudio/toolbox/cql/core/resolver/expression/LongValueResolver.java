package dev.memestudio.toolbox.cql.core.resolver.expression;

import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import net.sf.jsqlparser.expression.LongValue;

import java.util.function.UnaryOperator;

/**
 * @author meme
 */
public class LongValueResolver implements Resolver<LongValue> {
    @Override
    public UnaryOperator<ResolvingContext> resolve(LongValue longValue) {
        return context -> context.withResolver(__ -> longValue.getValue());
    }
}
