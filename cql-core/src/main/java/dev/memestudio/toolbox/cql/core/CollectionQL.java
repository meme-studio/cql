package dev.memestudio.toolbox.cql.core;

import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.schema.Statement;
import io.vavr.control.Try;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;

import java.util.function.Function;

import static io.vavr.API.Option;

@UtilityClass
public class CollectionQL {

    @SneakyThrows
    public Statement statement(String... statement) {
        return Statement.of(Resolvers.resolve(
                Try.of(() -> CCJSqlParserUtil.parse(String.join(" ", statement)))
                   .getOrElseThrow(throwsException())));
    }

    private Function<Throwable, CqlException> throwsException() {
        return t -> new CqlException(
                Option(t.getMessage()).getOrElse(
                        () -> Option(t.getCause()).map(Throwable::getMessage)
                                                  .getOrNull()));
    }
}
