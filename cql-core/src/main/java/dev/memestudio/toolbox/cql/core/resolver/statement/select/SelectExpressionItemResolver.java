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
    public UnaryOperator<ResolvingContext> parse(SelectExpressionItem selectExpressionItem) {
        return context -> context.withMapper(row -> resolveSelectItem(selectExpressionItem, context, row));
    }

    private Map<String, Object> resolveSelectItem(SelectExpressionItem selectExpressionItem, ResolvingContext context, Map<String, Object> row) {
        Object computed = Resolvers.resolve(selectExpressionItem.getExpression())
                                   .apply(context)
                                   .getResolver()
                                   .apply(row);
        String certainName = Objects.nonNull(selectExpressionItem.getAlias()) ? selectExpressionItem.getAlias().getName() : selectExpressionItem.toString();
        return Collections.singletonMap(certainName, computed);
    }
}
