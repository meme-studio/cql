package dev.memestudio.toolbox.cql.core.resolver;

import dev.memestudio.toolbox.cql.core.resolver.expression.*;
import dev.memestudio.toolbox.cql.core.resolver.schema.ColumnResolver;
import dev.memestudio.toolbox.cql.core.resolver.schema.TableResolver;
import dev.memestudio.toolbox.cql.core.resolver.statement.select.*;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;

import java.util.function.UnaryOperator;

/**
 * @author meme
 */
@UtilityClass
public class Resolvers {

    private final Map<Class<?>, Resolver<?>> RESOLVERS =
            HashMap.<Class<?>, Resolver<?>>empty()
                    .put(AndExpression.class, new AndExpressionResolver())
                    .put(OrExpression.class, new OrExpressionResolver())
                    .put(NotExpression.class, new NotExpressionResolver())
                    .put(Parenthesis.class, new ParenthesisResolver())
                    .put(Column.class, new ColumnResolver())
                    .put(EqualsTo.class, new EqualsToResolver())
                    .put(LongValue.class, new LongValueResolver())
                    .put(PlainSelect.class, new PlainSelectResolver())
                    .put(Select.class, new SelectResolver())
                    .put(Table.class, new TableResolver())
                    .put(NotEqualsTo.class, new NotEqualsToResolver())
                    .put(SelectExpressionItem.class, new SelectExpressionItemResolver())
                    .put(AllColumns.class, new AllColumnsResolver())
                    .put(Join.class, new JoinResolver())
                    .put(CaseExpression.class, new CaseExpressionResolver())
                    .put(WhenClause.class, new WhenClauseResolver())
                    .put(StringValue.class, new StringValueResolver())
                    .put(SubSelect.class, new SubSelectResolver())
                    .put(Function.class, new FunctionResolver());

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> UnaryOperator<ResolvingContext> resolve(@NonNull T type) {
        return RESOLVERS.get(type.getClass())
                        .map(resolver -> ((Resolver) resolver).resolve(type))
                        .getOrElseThrow(() -> new ResolvingException("Syntax '", type.toString(), "' not supported"));
    }
}
