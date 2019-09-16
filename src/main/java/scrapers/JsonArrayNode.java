package scrapers;

import org.json.JSONArray;

public class JsonArrayNode implements JsonNode {

    private final JSONArray array;

    JsonArrayNode(JSONArray array) {
        this.array = array;
    }

    @Override
    public JsonNode step(String[] recipe) {
        Integer i = Integer.parseInt(recipe[1]);
        switch (recipe[0]) {
            case "jsonObject":
                return new JsonObjectNode(array.getJSONObject(i));
            case "jsonArray":
                return new JsonArrayNode(array.getJSONArray(i));
            case "string":
                return new JsonStringNode(array.getString(i));
            case "integer":
                return new JsonIntegerNode(array.getInt(i));
            default:
                return null;
        }
    }

    @Override
    public JSONArray get() {
        return array;
    }
}
