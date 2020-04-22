package dev.memestudio.toolbox.cql.core.resolver.expression;

import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.WhenClause;

import java.util.List;
import java.util.function.UnaryOperator;

/**
 * @author meme
 */
public class CaseExpressionResolver implements Resolver<CaseExpression> {
    @Override
    public UnaryOperator<ResolvingContext> resolve(CaseExpression caseExpression) {
        Expression switchExpression = caseExpression.getSwitchExpression();
        List<WhenClause> whenClauses = caseExpression.getWhenClauses();
        return null;
    }
}
