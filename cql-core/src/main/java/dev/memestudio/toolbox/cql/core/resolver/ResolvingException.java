package dev.memestudio.toolbox.cql.core.resolver;

/**
 * @author meme
 */
public class ResolvingException extends IllegalStateException {

    private static final long serialVersionUID = 342074498438887132L;

    public ResolvingException(String... message) {
        super(String.join("", message));
    }
}
