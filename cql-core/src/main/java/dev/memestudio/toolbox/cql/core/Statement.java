package dev.memestudio.toolbox.cql.core;

import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.function.UnaryOperator;

@AllArgsConstructor(staticName = "of", access = AccessLevel.PACKAGE)
@FieldDefaults(makeFinal = true)
public class Statement {

    UnaryOperator<ResolvingContext> resolvingContext;

    public ResultSet tables(Map<String, List<Map<String, Object>>> tables) {
        return ResultSet.of(resolvingContext.apply(ResolvingContext.tables(tables))
                                            .getResult());
    }

}
