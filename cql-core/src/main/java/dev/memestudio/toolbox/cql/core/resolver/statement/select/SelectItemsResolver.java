package dev.memestudio.toolbox.cql.core.resolver.statement.select;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import io.vavr.collection.Stream;
import net.sf.jsqlparser.statement.select.SelectItem;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class SelectItemsResolver implements Resolver<List<SelectItem>> {

    @Override
    public UnaryOperator<ResolvingContext> resolve(List<SelectItem> selectItems) {
        List<UnaryOperator<ResolvingContext>> selectItemOps = Stream.ofAll(selectItems)
                                                                    .map(Resolvers::resolve)
                                                                    .toJavaList();
        return context -> context.withMapper(
                row -> Stream.ofAll(selectItemOps)
                             .map(op -> op.apply(context))
                             .map(ResolvingContext::getMapper)
                             .map(mapper -> mapper.apply(row))
                             .flatMap(Map::entrySet)
                             .filter(entry -> Objects.nonNull(entry.getValue()))
                             .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    }

}
