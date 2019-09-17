package nodeutils;

public final class JsonTraversalHelper {

    private static <T> JsonNode<T> getNode(JsonNode rootNode, String path) {
        String[] steps = path.split("\\|");
        JsonNode currentNode = rootNode;
        for (String step : steps) {
            currentNode = currentNode.step(step.split(":"));
        }
        return uncheckedCast(currentNode);
    }

    public static <T> T getContent(JsonNode rootNode, String path) {
        JsonNode<T> node = getNode(rootNode, path);
        return node.get();
    }

    @SuppressWarnings("unchecked")
    static <T> T uncheckedCast(Object o) {
        return (T) o;
    }
}
