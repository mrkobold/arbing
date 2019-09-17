package nodeutils;

public class JsonIntegerNode implements JsonNode {

    private final Integer integer;

    JsonIntegerNode(Integer integer) {
        this.integer = integer;
    }

    @Override
    public Integer get() {
        return integer;
    }
}
