package dev.memestudio.toolbox.cql.core.resolver.schema;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import io.vavr.Tuple2;
import io.vavr.collection.Stream;
import net.sf.jsqlparser.schema.Table;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class TableResolver implements Resolver<Table> {

    @Override
    public UnaryOperator<ResolvingContext> parse(Table table) {
        return context -> {
            String certainName = Objects.nonNull(table.getAlias()) ? table.getAlias().getName() : table.getName();
            Collection<Map<String, Object>> collectionTable = context.getSchemas().get(table.getName());
            return context.withTableNames(Collections.singletonList(certainName))
                          .withResult(Stream.ofAll(Stream.ofAll(collectionTable)
                                                         .map(row -> row.entrySet()
                                                                        .stream()
                                                                        .map(entry -> new Tuple2<>(String.format("%s.%s", certainName, entry.getKey()), entry.getValue()))
                                                                        .collect(Collectors.toMap(Tuple2::_1, Tuple2::_2)))));
        };
    }
}
