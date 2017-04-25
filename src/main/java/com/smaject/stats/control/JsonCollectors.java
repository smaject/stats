package com.smaject.stats.control;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.Map;
import java.util.stream.Collector;

/**
 * Convert entryset to JsonObject.
 * Inspired by http://www.adam-bien.com/roller/abien/entry/converting_a_map_string_string
 *
 * @author smaject.com
 */
public interface JsonCollectors {

    static <T> Collector<Map.Entry<T, T>, ?, JsonObjectBuilder> toJsonBuilder() {
        return Collector.of(Json::createObjectBuilder, (t, u) -> t.add(String.valueOf(String.valueOf(u.getKey())),
                String.valueOf(u.getValue())), JsonCollectors::merge);
    }

    static JsonObjectBuilder merge(JsonObjectBuilder left, JsonObjectBuilder right) {
        JsonObjectBuilder retVal = Json.createObjectBuilder();
        JsonObject leftObject = left.build();
        JsonObject rightObject = right.build();
        leftObject.keySet().stream().forEach(key -> retVal.add(key, leftObject.get(key)));
        rightObject.keySet().stream().forEach(key -> retVal.add(key, rightObject.get(key)));
        return retVal;
    }

}
