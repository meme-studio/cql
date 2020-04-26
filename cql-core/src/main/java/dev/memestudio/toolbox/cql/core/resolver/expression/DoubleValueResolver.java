package dev.memestudio.toolbox.cql.core.resolver.expression;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import net.sf.jsqlparser.expression.DoubleValue;

import java.util.function.UnaryOperator;

/**
 * @author meme
 */
public class DoubleValueResolver implements Resolver<DoubleValue> {
    @Override
    public UnaryOperator<ResolvingContext> resolve(DoubleValue doubleValue) {
        return context -> context.withResolver(__ -> doubleValue.getValue());
    }
}
