package dev.memestudio.toolbox.cql.core.resolver.statement.select;

import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import io.vavr.Tuple2;
import io.vavr.collection.Stream;
import net.sf.jsqlparser.statement.select.Join;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public class JoinsResolver implements Resolver<List<Join>> {

    @Override
    public UnaryOperator<ResolvingContext> parse(List<Join> joins) {
        return context -> context.withResult(Stream.ofAll(joins)
                                                   .map(Resolvers::resolve)
                                                   .map(op -> op.apply(context))
                                                   .map(ResolvingContext::getResult)
                                                   .reduce(this::combineResults));


    }

    private Stream<Map<String, Object>> combineResults(Stream<Map<String, Object>> result1, Stream<Map<String, Object>> result2) {
        return Stream.ofAll(result1.crossProduct(result2))
                     .map(this::combineResults);
    }

    private Map<String, Object> combineResults(Tuple2<Map<String, Object>, Map<String, Object>> tuple2) {
        Map<String, Object> result = new HashMap<>();
        result.putAll(tuple2._1());
        result.putAll(tuple2._2());
        return result;
    }
}
