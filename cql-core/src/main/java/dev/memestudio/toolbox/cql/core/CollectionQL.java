package dev.memestudio.toolbox.cql.core;

import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.schema.Statement;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;

@UtilityClass
public class CollectionQL {

    @SneakyThrows
    public Statement statement(String... statement) {
        return Statement.of(Resolvers.resolve(
                CCJSqlParserUtil.parse(String.join(" ", statement))));
    }
}
