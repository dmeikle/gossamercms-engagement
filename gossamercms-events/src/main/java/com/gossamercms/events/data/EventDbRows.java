package com.gossamercms.events.data;

import com.gossamercms.mvc.util.ReflectionUtils;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

final class EventDbRows {
    private EventDbRows() {}

    static <T> T mapRowToDto(Map<String, Object> row, Class<T> type) {
        // JDBC 4.2 may return timestamptz as OffsetDateTime; event DTOs use Instant.
        var values = new LinkedHashMap<>(row);
        values.replaceAll((column, value) -> value instanceof OffsetDateTime time ? time.toInstant() : value);
        return ReflectionUtils.mapRowToDto(values, type);
    }
}
