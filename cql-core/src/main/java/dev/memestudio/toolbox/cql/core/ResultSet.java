package dev.memestudio.toolbox.cql.core;

import dev.memestudio.toolbox.cql.core.util.ObjectTransformer;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;


@AllArgsConstructor(staticName = "of", access = AccessLevel.PACKAGE)
@FieldDefaults(makeFinal = true)
public class ResultSet {

    Stream<Map<String, Object>> result;

    public List<Map<String, Object>> asList() {
        return asStream().toList();
    }

    public Stream<Map<String, Object>> asStream() {
        return result;
    }

    public <T> List<T> asList(Class<T> type) {
        return asStream(type).toList();
    }

    public <T> Stream<T> asStream(Class<T> type) {
        return result.map(row -> ObjectTransformer.convert(row, type));
    }

    public void prettyPrint() {
        //TODO prettyPrint
    }

}
