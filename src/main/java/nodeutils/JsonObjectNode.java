package nodeutils;

import org.json.JSONObject;

public class JsonObjectNode<K extends JSONObject> implements JsonNode<K> {

    private final K object;

    JsonObjectNode(K object) {
        this.object = object;
    }

    @Override
    public JsonNode step(String[] recipe) {
        return JsonNode.from(object.get(recipe[1]));
    }

    @Override
    public K get() {
        return object;
    }
}
