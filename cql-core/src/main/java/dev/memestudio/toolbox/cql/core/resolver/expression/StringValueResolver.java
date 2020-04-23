package dev.memestudio.toolbox.cql.core.resolver.expression;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import net.sf.jsqlparser.expression.StringValue;

import java.util.function.UnaryOperator;

/**
 * @author meme
 */
public class StringValueResolver implements Resolver<StringValue> {
    @Override
    public UnaryOperator<ResolvingContext> resolve(StringValue stringValue) {
        return context -> context.withResolver(__ -> stringValue.getNotExcapedValue());
    }
}
