package dev.memestudio.toolbox.cql.core.resolver.expression;

import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import net.sf.jsqlparser.expression.Parenthesis;

import java.util.function.UnaryOperator;

/**
 * @author meme
 */
public class ParenthesisResolver implements Resolver<Parenthesis> {

    @Override
    public UnaryOperator<ResolvingContext> parse(Parenthesis parenthesis) {
        return Resolvers.resolve(parenthesis.getExpression());
    }
}
