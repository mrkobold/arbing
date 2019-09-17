package nodeutils;

public class JsonLongNode implements JsonNode {

    private final Long aLong;

    JsonLongNode(Long aLong) {
        this.aLong = aLong;
    }

    @Override
    public Long get() {
        return aLong;
    }
}
