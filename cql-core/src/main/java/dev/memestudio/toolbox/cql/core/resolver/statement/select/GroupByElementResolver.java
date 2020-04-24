package dev.memestudio.toolbox.cql.core.resolver.statement.select;

import dev.memestudio.toolbox.cql.core.resolver.Resolver;
import dev.memestudio.toolbox.cql.core.resolver.Resolvers;
import dev.memestudio.toolbox.cql.core.resolver.ResolvingContext;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import net.sf.jsqlparser.statement.select.GroupByElement;

import java.util.function.UnaryOperator;

/**
 * @author meme
 */
public class GroupByElementResolver implements Resolver<GroupByElement> {
    @Override
    public UnaryOperator<ResolvingContext> resolve(GroupByElement groupByElement) {
        List<UnaryOperator<ResolvingContext>> groupByOps =
                Stream.ofAll(groupByElement.getGroupByExpressions())
                      .map(Resolvers::resolve)
                      .toList();


        return null;
    }
}
