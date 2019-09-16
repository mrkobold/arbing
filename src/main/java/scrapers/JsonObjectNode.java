package scrapers;

import org.json.JSONObject;

public class JsonObjectNode implements JsonNode {

    private final JSONObject object;

    JsonObjectNode(JSONObject object) {
        this.object = object;
    }

    @Override
    public JsonNode step(String[] recipe) {
        switch (recipe[0]) {
            case "jsonObject":
                return new JsonObjectNode(object.getJSONObject(recipe[1]));
            case "jsonArray":
                return new JsonArrayNode(object.getJSONArray(recipe[1]));
            case "string":
                return new JsonStringNode(object.getString(recipe[1]));
            case "integer":
                return new JsonIntegerNode(object.getInt(recipe[1]));
            default:
                return null;
        }
    }

    @Override
    public JSONObject get() {
        return object;
    }
}
