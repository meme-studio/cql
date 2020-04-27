package dev.memestudio.toolbox.cql.core.util;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

@UtilityClass
public class SchemaUtil {

    private final Pattern CHANGE_COLUMN_NAME_PATTERN = Pattern.compile(".+?(?=\\.)");

    public String removeGraveAccents(String schemaName) {
        return schemaName.replaceAll("`", "");
    }

    public String changeColumnName(String tableName, String columnName) {
        return CHANGE_COLUMN_NAME_PATTERN.matcher(columnName)
                                         .replaceFirst(tableName);
    }


    public String extendColumnName(String tableName, String columnName) {
        return String.format("%s.%s", tableName, columnName);
    }

}
