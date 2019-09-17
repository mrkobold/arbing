package nodeutils;

import org.json.JSONArray;

public final class JsonTraversalHelper {

    private static JsonNode getNode(JsonNode rootNode, String property) {
        return getNodeResolvedProperty(rootNode, property);
    }

    public static JsonNode getNodeResolvedProperty(JsonNode rootNode, String property) {
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

    public static JSONArray getJsonArray(JsonNode rootNode, String property) {
        JsonNode node = getNode(rootNode, property);
        return ((JsonArrayNode) node).get();
    }
}
