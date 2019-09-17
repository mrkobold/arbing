package nodeutils;

import org.json.JSONArray;
import org.json.JSONObject;

import static nodeutils.JsonTraversalHelper.uncheckedCast;

public interface JsonNode<T> {

    JsonNode step(String step);

    T get();

    static <K> JsonNode<K> from(K object) {
        if (object instanceof JSONArray) {
            return uncheckedCast(new JsonArrayNode<>((JSONArray) object));
        }
        if (object instanceof JSONObject) {
            return uncheckedCast(new JsonObjectNode<>((JSONObject) object));
        }
        return new JsonPrimitiveNode<>(object);
    }
}
