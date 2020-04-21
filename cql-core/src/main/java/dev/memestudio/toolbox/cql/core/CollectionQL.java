package dev.memestudio.toolbox.cql.core;

import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;

import java.util.function.UnaryOperator;

@UtilityClass
public class CollectionQL {

    @SneakyThrows
    public Statement statement(String... statement) {
        UnaryOperator<ResolvingContext> resolved = Resolvers.resolve(
                CCJSqlParserUtil.parse(String.join(" ", statement)));
        return Statement.of(resolved);
    }
}
