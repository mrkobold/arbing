package nodeutils;

import org.json.JSONArray;
import org.json.JSONObject;

public interface JsonNode {

    default JsonNode step(String[] recipe) {
        return null;
    }

    Object get();

    static JsonObjectNode from(JSONObject object) {
        return new JsonObjectNode(object);
    }

    static JsonArrayNode from(JSONArray array) {
        return new JsonArrayNode(array);
    }

    static JsonStringNode from(String string) {
        return new JsonStringNode(string);
    }
}
