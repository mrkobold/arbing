package nodeutils;

public class JsonDoubleNode implements JsonNode {
    private final Double aDouble;

    JsonDoubleNode(Double aDouble) {
        this.aDouble = aDouble;
    }

    @Override
    public Double get() {
        return aDouble;
    }
}
