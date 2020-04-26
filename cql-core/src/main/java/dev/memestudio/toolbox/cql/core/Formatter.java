package dev.memestudio.toolbox.cql.core;

import dev.memestudio.toolbox.cql.core.schema.ResultSet;

import java.io.OutputStream;

/**
 * @author meme
 */
public interface Formatter {

    Formatter from(ResultSet resultSet);

    void print();

    void printTo(OutputStream os);

}
