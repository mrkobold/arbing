package nodeutils;

import org.json.JSONObject;

public class JsonObjectNode<K extends JSONObject> implements JsonNode<K> {

    private final K object;

    JsonObjectNode(K object) {
        this.object = object;
    }

    @Override
    public JsonNode step(String step) {
        return JsonNode.from(object.get(step));
    }

    @Override
    public K get() {
        return object;
    }
}
