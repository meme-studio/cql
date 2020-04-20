package dev.memestudio.toolbox.cql.core.resolver;

import dev.memestudio.toolbox.cql.core.resolver.expression.*;
import dev.memestudio.toolbox.cql.core.resolver.schema.ColumnResolver;
import dev.memestudio.toolbox.cql.core.resolver.schema.TableResolver;
import dev.memestudio.toolbox.cql.core.resolver.statement.select.*;
import io.vavr.control.Option;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NotExpression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

/**
 * @author meme
 */
@UtilityClass
public class Resolvers {

    private final Map<Class<?>, Resolver<?>> RESOLVERS = new HashMap<Class<?>, Resolver<?>>() {{
        put(AndExpression.class, new AndExpressionResolver());
        put(OrExpression.class, new OrExpressionResolver());
        put(NotExpression.class, new NotExpressionResolver());
        put(Parenthesis.class, new ParenthesisResolver());
        put(Column.class, new ColumnResolver());
        put(EqualsTo.class, new EqualsToResolver());
        put(LongValue.class, new LongValueResolver());
        put(PlainSelect.class, new PlainSelectResolver());
        put(Select.class, new SelectResolver());
        put(Table.class, new TableResolver());
        put(NotEqualsTo.class, new NotEqualsToResolver());
        put(SelectExpressionItem.class, new SelectExpressionItemResolver());
        put(AllColumns.class, new AllColumnsResolver());
    }};

    private final Map<Class<?>, Resolver<?>> COLLECTION_RESOLVERS = new HashMap<Class<?>, Resolver<?>>() {{
        put(Join.class, new JoinsResolver());
        put(SelectItem.class, new SelectItemsResolver());
        put(SelectExpressionItem.class, new SelectItemsResolver());
        put(AllColumns.class, new SelectItemsResolver());
    }};

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> UnaryOperator<ResolvingContext> resolve(@NonNull T type) {
        return Option.of(type)
                     .map(T::getClass)
                     .flatMap(typeClass -> Option.of(RESOLVERS.get(typeClass)))
                     .map(parser -> ((Resolver) parser))
                     .map(parser -> parser.parse(type))
                     .orElse(() -> Option.of(COLLECTION_RESOLVERS.get(getRawType(type)))
                                         .map(parser -> ((Resolver) parser))
                                         .map(parser -> parser.parse(type)))
                     .getOrElseThrow(() ->
                             new UnsupportedOperationException(String.join("", "Syntax '", type.toString(), "' not supported")));
    }

    private <T> Class<?> getRawType(T type) {
        return type instanceof List ? ((List<?>) type).get(0).getClass() : null;
    }
}
