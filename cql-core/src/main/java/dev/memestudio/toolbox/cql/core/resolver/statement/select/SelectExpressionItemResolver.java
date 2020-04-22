package dev.memestudio.toolbox.cql.core.resolver.statement.select;

import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.UnaryOperator;

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
        String certainName = Objects.nonNull(selectExpressionItem.getAlias()) ? selectExpressionItem.getAlias().getName() : selectExpressionItem.toString();
        return Collections.singletonMap(certainName, computed);
    }
}
