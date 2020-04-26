package dev.memestudio.toolbox.cql.core.schema;

import dev.memestudio.toolbox.cql.core.CqlException;
import io.vavr.API;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import lombok.AllArgsConstructor;

/**
 * @author meme
 */
@AllArgsConstructor(staticName = "from")
public class Datasource {

    private final Map<String, List<Map<String, Object>>> tables;

    public static Datasource from(java.util.Map<String, Iterable<java.util.Map<String, Object>>> tables) {
        return Datasource.from(HashMap.ofAll(tables)
                                      .mapValues(List::ofAll)
                                      .mapValues(table -> table.map(HashMap::ofAll)));
    }

    public static Datasource from(String tableName, Iterable<java.util.Map<String, Object>> table) {
        return Datasource.from(API.Map(tableName, List.ofAll(table).map(HashMap::ofAll)));
    }

    public Datasource addTable(String tableName, Iterable<java.util.Map<String, Object>> table) {
        tables.put(tableName, List.ofAll(table).map(HashMap::ofAll));
        return this;
    }

    public Datasource addTable(String tableName, List<Map<String, Object>> table) {
        tables.put(tableName, table);
        return this;
    }

    public Datasource addTables(java.util.Map<String, Iterable<java.util.Map<String, Object>>> tables) {
        this.tables.merge(HashMap.ofAll(tables)
                                 .mapValues(List::ofAll)
                                 .mapValues(table -> table.map(HashMap::ofAll)));
        return this;
    }

    public List<Map<String, Object>> getTable(String name) {
        return tables.get(name)
                     .getOrElseThrow(() -> new CqlException("No such table '", name, "'"));
    }


}
