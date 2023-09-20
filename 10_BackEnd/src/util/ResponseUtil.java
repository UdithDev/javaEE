package util;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class ResponseUtil {
    public static JsonObject genJson(String state, String message, JsonArray... data) {
        JsonObjectBuilder object = Json.createObjectBuilder();
        object.add("state", state);
        object.add("message", message);
        if (data.length > 0) {
            object.add("data", data[0]);
        } else {
            object.add("data", "");
        }
        return object.build();
    }
}
