package dev.memestudio.toolbox.cql.core.resolver.statement.select;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import io.vavr.collection.Stream;
import net.sf.jsqlparser.statement.select.Distinct;

import java.util.function.UnaryOperator;

public class DistinctResolver implements Resolver<Distinct> {
    @Override
    public UnaryOperator<ResolvingContext> resolve(Distinct distinct) {
        return context -> context.withTransformResult(Stream::distinct);
    }
}
