package dev.memestudio.toolbox.cql.core;

/**
 * @author meme
 */
public class CqlException extends IllegalStateException {

    private static final long serialVersionUID = 342074498438887132L;

    public CqlException(String... message) {
        super(String.join("", message));
    }
}
