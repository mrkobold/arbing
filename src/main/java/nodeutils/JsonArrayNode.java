package nodeutils;

import org.json.JSONArray;

public class JsonArrayNode<T extends JSONArray> implements JsonNode<T> {

    private final T array;

    JsonArrayNode(T array) {
        this.array = array;
    }

    @Override
    public JsonNode step(String step) {
        Integer i = Integer.parseInt(step);
        return JsonNode.from(array.get(i));
    }

    @Override
    public T get() {
        return array;
    }
}
