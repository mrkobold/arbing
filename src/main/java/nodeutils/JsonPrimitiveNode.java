package nodeutils;

public class JsonPrimitiveNode<P> implements JsonNode<P> {

    private final P value;

    JsonPrimitiveNode(P value) {
        this.value = value;
    }

    @Override
    public JsonNode step(String step) {
        throw new RuntimeException("Reached leaf. No more steps");
    }

    @Override
    public P get() {
        return value;
    }
}
