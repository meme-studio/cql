package dev.memestudio.toolbox.cql.core.resolver.statement.select;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.statement.select.SubSelect;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;

import static io.vavr.API.Option;

public class SubSelectResolver implements Resolver<SubSelect> {
    @Override
    public UnaryOperator<ResolvingContext> resolve(SubSelect subSelect) {
        Option<String> tableName =
                Option(subSelect.getAlias())
                        .map(Alias::getName);
        UnaryOperator<ResolvingContext> selectOp = Resolvers.resolve(subSelect.getSelectBody());
        return context -> {
            AtomicReference<Stream<Map<String, Object>>> ref = new AtomicReference<>(selectOp.apply(context).getResult());
            tableName.peek(name -> ref.set(ref.get()
                                              .map(row -> row.mapKeys(colName -> colName.replaceFirst(".*\\.", name + '.')))));
            return context.withResult(ref.get());
        };

    }
}
