package nodeutils;

public class JsonStringNode implements JsonNode {

    private final String string;

    JsonStringNode(String string) {
        this.string = string;
    }

    @Override
    public String get() {
        return string;
    }
}
