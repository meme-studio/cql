package dev.memestudio.toolbox.cql.core.resolver;

import dev.memestudio.toolbox.cql.core.CqlException;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Try;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.ParameterizedType;
import java.util.ServiceLoader;
import java.util.function.UnaryOperator;

import static java.util.function.Function.identity;

/**
 * @author meme
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@UtilityClass
public class Resolvers {

    private final Map<Class<?>, Resolver> RESOLVERS = List.ofAll(ServiceLoader.load(Resolver.class))
                                                          .toMap(Resolvers::resolveType, identity());

    private Class<?> resolveType(Resolver resolver) {
        return Try.of(resolver::getClass)
                  .map(type -> Try.of(type::getAnnotatedInterfaces)
                                  .map(types -> types[0])
                                  .getOrElse(type::getAnnotatedSuperclass))
                  .map(AnnotatedType::getType)
                  .map(ParameterizedType.class::cast)
                  .map(ParameterizedType::getActualTypeArguments)
                  .map(types -> types[0])
                  .map(Class.class::cast)
                  .getOrElseThrow(() -> new CqlException("Illegal resolver '", resolver.getClass().getName(), "'"));
    }

    public <T> UnaryOperator<ResolvingContext> resolve(@NonNull T type) {
        return RESOLVERS.get(type.getClass())
                        .map(resolver -> resolver.resolve(type))
                        .getOrElseThrow(() -> new CqlException("Syntax '", type.toString(), "' not supported"));
    }

}
