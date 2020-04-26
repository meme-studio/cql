package dev.memestudio.toolbox.cql.core.schema;

import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.function.UnaryOperator;

@AllArgsConstructor(staticName = "of")
@FieldDefaults(makeFinal = true)
public class Statement {

    UnaryOperator<ResolvingContext> resolvingContext;

    public ResultSet tables(Datasource tables) {
        return ResultSet.of(resolvingContext.apply(ResolvingContext.tables(tables))
                                            .getResult());
    }

}
