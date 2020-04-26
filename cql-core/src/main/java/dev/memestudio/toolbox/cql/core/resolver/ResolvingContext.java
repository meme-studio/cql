package dev.memestudio.toolbox.cql.core.resolver;

import dev.memestudio.toolbox.cql.core.schema.Datasource;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import lombok.*;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * @author meme
 */
@With
@Getter
@RequiredArgsConstructor(staticName = "tables")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResolvingContext {

    @NonNull
    private final Datasource datasource;

    private List<String> tableNames;

    private String columnName;

    private Stream<Map<String, Object>> result;

    private Predicate<Map<String, Object>> condition;

    private Function<Map<String, Object>, Object> resolver;

    private UnaryOperator<Map<String, Object>> mapper;

    private Comparator<Map<String, Object>> sort;

    private IntSupplier offset;

    private IntSupplier limit;

}
