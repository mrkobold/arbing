package scraping.jsonscraping.nodeutils;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonNode<T> {

    private T content;

    public JsonNode(T content) {
        this.content = content;
    }

    public static JsonNode from(String jsonString) {
        if (jsonString.startsWith("[")) {
            return new JsonNode<>(new JSONArray(jsonString));
        }
        return new JsonNode<>(new JSONObject(jsonString));
    }

    JsonNode step(String key) {
        return new JsonNode<>(((JSONObject) content).get(key));
    }

    JsonNode step(int index) {
        return new JsonNode<>(((JSONArray) content).get(index));
    }

    public T get() {
        return content;
    }
}
