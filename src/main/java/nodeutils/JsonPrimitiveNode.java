package nodeutils;

public class JsonPrimitiveNode<P> implements JsonNode<P> {

    private final P value;

    JsonPrimitiveNode(P value) {
        this.value = value;
    }

    @Override
    public P get() {
        return value;
    }
}
