package dev.memestudio.toolbox.cql.core.resolver;

import io.vavr.collection.Stream;
import lombok.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@With
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ResolvingContext {

    private Map<String, Collection<Map<String, Object>>> schemas;

    private List<String> tableNames;

    private Stream<Map<String, Object>> result;

    private Predicate<Map<String, Object>> condition;

    private Function<Map<String, Object>, Object> resolver;

    private UnaryOperator<Map<String, Object>> mapper;

    private Comparator<Map<String, Object>> sort;

    private Integer skip;

    private Integer limit;

}
