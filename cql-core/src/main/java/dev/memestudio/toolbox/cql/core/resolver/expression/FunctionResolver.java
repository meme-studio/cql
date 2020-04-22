package dev.memestudio.toolbox.cql.core.resolver.expression;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import dev.memestudio.toolbox.cql.core.resolver.expression.function.Functions;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;

import java.util.List;
import java.util.function.UnaryOperator;

public class FunctionResolver implements Resolver<Function> {
    @Override
    public UnaryOperator<ResolvingContext> resolve(Function function) {
        List<UnaryOperator<ResolvingContext>> parameterOps = Option.of(function.getParameters())
                                                                   .map(ExpressionList::getExpressions)
                                                                   .map(Stream::ofAll)
                                                                   .getOrElse(Stream.empty())
                                                                   .map(Resolvers::resolve)
                                                                   .toJavaList();
        return Functions.apply(function.getName(), parameterOps);
    }
}
