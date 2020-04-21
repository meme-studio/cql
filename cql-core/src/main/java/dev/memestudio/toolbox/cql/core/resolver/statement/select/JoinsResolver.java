package dev.memestudio.toolbox.cql.core.resolver.statement.select;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import io.vavr.collection.Stream;
import net.sf.jsqlparser.statement.select.Join;

import java.util.List;
import java.util.function.UnaryOperator;

public class JoinsResolver implements Resolver<List<Join>> {

    @Override
    public UnaryOperator<ResolvingContext> parse(List<Join> joins) {
        return context -> context.withResult(Stream.ofAll(joins)
                                                   .map(Resolvers::resolve)
                                                   .map(op -> op.apply(context))
                                                   .last()
                                                   .getResult());
    }
}
