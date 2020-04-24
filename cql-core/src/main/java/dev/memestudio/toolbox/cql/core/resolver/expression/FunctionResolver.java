package dev.memestudio.toolbox.cql.core.resolver.expression;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import dev.memestudio.toolbox.cql.core.resolver.expression.function.Fn;
import dev.memestudio.toolbox.cql.core.resolver.expression.function.Fns;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;

import java.util.function.UnaryOperator;

/**
 * @author meme
 */
public class FunctionResolver implements Resolver<Function> {
    @Override
    public UnaryOperator<ResolvingContext> resolve(Function function) {
        List<UnaryOperator<ResolvingContext>> parameterOps = Option.of(function.getParameters())
                                                                   .map(ExpressionList::getExpressions)
                                                                   .map(Stream::ofAll)
                                                                   .getOrElse(Stream.empty())
                                                                   .map(Resolvers::resolve)
                                                                   .toList();
        Fn fn = Fns.get(function.getName());
        return context -> {
            Stream<Map<String, Object>> result = context.getResult();
            Stream<Map<String, Object>> resultCopy = result.toStream();
            return context.withResolver(fn.apply(result, Stream.ofAll(parameterOps)
                                                               .map(op -> op.apply(context))
                                                               .map(ResolvingContext::getResolver)
                                                               .toList()))
                          .withResult(resultCopy);
        };
    }
}
