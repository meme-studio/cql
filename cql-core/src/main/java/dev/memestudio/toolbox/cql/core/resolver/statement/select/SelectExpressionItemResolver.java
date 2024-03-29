package dev.memestudio.toolbox.cql.core.resolver.statement.select;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import dev.memestudio.toolbox.cql.core.util.SchemaUtil;
import io.vavr.collection.Map;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;

import java.util.function.UnaryOperator;

import static io.vavr.API.Map;
import static io.vavr.API.Option;

public class SelectExpressionItemResolver implements Resolver<SelectExpressionItem> {

    @Override
    public UnaryOperator<ResolvingContext> resolve(SelectExpressionItem selectExpressionItem) {
        UnaryOperator<ResolvingContext> selectItemOp = Resolvers.resolve(selectExpressionItem.getExpression());
        return context -> context.withMapper(row -> resolveSelectItem(selectItemOp, selectExpressionItem, context, row));
    }

    private Map<String, Object> resolveSelectItem(UnaryOperator<ResolvingContext> selectItemOp, SelectExpressionItem selectExpressionItem, ResolvingContext context, Map<String, Object> row) {
        Object computed = selectItemOp.apply(context)
                                      .getResolver()
                                      .apply(row);
        String certainName = SchemaUtil.removeGraveAccents(Option(selectExpressionItem.getAlias())
                .map(Alias::getName)
                .getOrElse(selectExpressionItem::toString));
        return Map(certainName, computed);
    }
}
