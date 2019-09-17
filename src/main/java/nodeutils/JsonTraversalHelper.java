package nodeutils;

import org.json.JSONArray;

public final class JsonTraversalHelper {

    public static JsonNode getNode(JsonNode rootNode, String property) {
        String[] steps = property.split("\\|");
        JsonNode currentNode = rootNode;
        for (String step : steps) {
            currentNode = currentNode.step(step.split(":"));
        }
        return currentNode;
    }

    public static String getString(JsonNode rootNode, String property) {
        JsonNode node = getNode(rootNode, property);
        return ((JsonStringNode) node).get();
    }

    public static Integer getInteger(JsonNode rootNode, String property) {
        JsonNode node = getNode(rootNode, property);
        return ((JsonIntegerNode) node).get();
    }

    public static Long getLong(JsonNode rootNode, String property) {
        JsonNode node = getNode(rootNode, property);
        return ((JsonLongNode) node).get();
    }

    public static Double getDouble(JsonNode rootNode, String property) {
        JsonNode node = getNode(rootNode, property);
        return ((JsonDoubleNode) node).get();
    }

    public static JSONArray getJsonArray(JsonNode rootNode, String property) {
        JsonNode node = getNode(rootNode, property);
        return ((JsonArrayNode) node).get();
    }
}
