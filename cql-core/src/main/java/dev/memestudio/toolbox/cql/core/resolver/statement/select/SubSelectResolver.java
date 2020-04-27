package dev.memestudio.toolbox.cql.core.resolver.statement.select;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import dev.memestudio.toolbox.cql.core.util.SchemaUtil;
import io.vavr.control.Option;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.statement.select.SubSelect;

import java.util.function.UnaryOperator;

import static io.vavr.API.Option;

public class SubSelectResolver implements Resolver<SubSelect> {
    @Override
    public UnaryOperator<ResolvingContext> resolve(SubSelect subSelect) {
        Option<String> tableName =
                Option(subSelect.getAlias())
                        .map(Alias::getName)
                        .map(SchemaUtil::removeGraveAccents);
        UnaryOperator<ResolvingContext> selectOp = Resolvers.resolve(subSelect.getSelectBody());
        return context -> {
            ResolvingContext ctx = selectOp.apply(context);
            return tableName.map(
                    name -> ctx.withTransformResult(
                            result -> result.map(
                                    row -> row.mapKeys(
                                            columnName -> SchemaUtil.changeColumnName(name, columnName)))))
                            .getOrElse(ctx);
        };
    }


}
