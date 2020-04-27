package dev.memestudio.toolbox.cql.core.resolver.expression;

import dev.memestudio.toolbox.cql.core.CqlException;
import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import dev.memestudio.toolbox.cql.core.util.Validates;
import io.vavr.collection.List;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ValueListExpression;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static io.vavr.API.*;
import static io.vavr.Predicates.instanceOf;

public abstract class BinaryExpressionResolver<T extends BinaryExpression> implements Resolver<T> {

    protected abstract UnaryOperator<ResolvingContext> resolve(UnaryOperator<ResolvingContext> leftOp, UnaryOperator<ResolvingContext> rightOp);

    @Override
    public UnaryOperator<ResolvingContext> resolve(T expression) {
        Expression left = expression.getLeftExpression();
        Expression right = expression.getRightExpression();
        return Match(left).of(
                Case($(instanceOf(ValueListExpression.class)),
                        lefts -> Match(right).of(
                                Case($(instanceOf(ValueListExpression.class)), rights -> resolve(lefts, rights, expression)),
                                Case($(), () -> {
                                    throw new CqlException("Expression counts not same '", expression.toString(), "'");
                                })
                        )),
                Case($(),
                        () -> Match(right).of(
                                Case($(instanceOf(ValueListExpression.class)), () -> {
                                    throw new CqlException("Expression counts not same '", expression.toString(), "'");
                                }),
                                Case($(), () -> resolve(Resolvers.resolve(left), Resolvers.resolve(right)))
                        ))
        );
    }

    protected UnaryOperator<ResolvingContext> resolve(ValueListExpression lefts, ValueListExpression rights, T expression) {
        java.util.List<Expression> leftExps = lefts.getExpressionList()
                                                   .getExpressions();
        java.util.List<Expression> rightExps = rights.getExpressionList()
                                                     .getExpressions();
        Validates.isTrue(Objects.equals(leftExps.size(), rightExps.size()), () -> new CqlException("Expression counts not same '", expression.toString(), "'"));
        List<UnaryOperator<ResolvingContext>> ops = List.ofAll(leftExps)
                                                        .zipWith(List.ofAll(rightExps),
                                                                (left, right) -> resolve(Resolvers.resolve(left), Resolvers.resolve(right)));
        return context -> context.withAppendedCondition(condition -> condition.and(ops.map(op -> op.apply(context))
                                                                                      .map(ResolvingContext::getCondition)
                                                                                      .reduceLeft(Predicate::and)));
    }
}
