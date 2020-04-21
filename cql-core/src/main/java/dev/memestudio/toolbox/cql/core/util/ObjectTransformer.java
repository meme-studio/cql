package dev.memestudio.toolbox.cql.core.util;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class ObjectTransformer {

    public <T> T convert(Object origin, Class<T> type) {
        return null;//TODO ObjectConverter.convert
    }

    public Map<String, Object> combineRows(Map<String, Object> leftRow, Map<String, Object> rightRow) {
        Map<String, Object> combinedRow = new HashMap<>();
        combinedRow.putAll(leftRow);
        combinedRow.putAll(rightRow);
        return combinedRow;
    }
}
