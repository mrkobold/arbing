package scrapers;

import lombok.extern.slf4j.Slf4j;
import model.Event;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

@Slf4j
public class Smart extends Provider {
    private static String url = "https://eu-offering.kambicdn.org/offering/v2018/sbro/listView/tennis.json?lang=ro_RO&market=RO&client_id=2&channel_id=1&ncid=1565702772391&useCombined=true";
    private final Properties properties = new Properties();

    public Smart() throws Exception {
        super("Smart", url);
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        properties.load(loader.getResourceAsStream("unibet-descriptor.properties"));
    }

    @Override
    List<Event> parseEventsFromJSONString(String jsonString) {
        JSONObject documentRoot = new JSONObject(jsonString);
        return getEventsSmart(new JsonObjectNode(documentRoot));
    }

    private List<Event> getEventsSmart(JsonNode documentRoot) {
        List<Event> eventList = new ArrayList<>();
        JSONArray eventsArray = getJsonArray(documentRoot, "events.root");
        for (int i = 0; i < eventsArray.length(); i++) {
            Optional<Event> optionalEvent = getOptionalEvent(JsonNode.from(eventsArray.getJSONObject(i)));
            optionalEvent.map(eventList::add);
        }
        return eventList;
    }

    private Optional<Event> getOptionalEvent(JsonNode eventRoot) {
        try {
            String eventName = getString(eventRoot, "event.name");
            String eventId = Integer.toString(getInteger(eventRoot, "event.id"));
            Date start = DATE_FORMAT.parse(getString(eventRoot, "event.date"));

            String player1 = getString(eventRoot, "player1.name");
            String player2 = getString(eventRoot, "player2.name");

            JsonArrayNode betOffersArrayNode = JsonNode.from(getJsonArray(eventRoot, "event.betOffers.root"));
            JsonObjectNode oddsRootNode = getUnibetMatchOddsRootNode(betOffersArrayNode);

            float win1 = getOdds(oddsRootNode, 1);
            float win2 = getOdds(oddsRootNode, 2);

            return Optional.of(new Event(eventId, getName(), eventName, player1, player2, start, win1, win2));
        } catch (Exception ex) {
            log.warn("couldn't parse an event in scraper: {}", getName());
            return Optional.empty();
        }
    }

    private float getOdds(JsonNode node, int id) {
        id--;
        String pathToMatchOdds = properties.getProperty("event.betOffers.pathToMatchOdds");
        JsonNode oddsNode = getNodeResolvedProperty(node, pathToMatchOdds.replaceAll("\\?", Integer.toString(id)));
        Integer odds = ((JsonIntegerNode) oddsNode).get();
        return odds / 1000f;
    }

    private JsonObjectNode getUnibetMatchOddsRootNode(JsonArrayNode betOffersArrayNode) {
        JSONArray betOffersArray = betOffersArrayNode.get();
        for (int i = 0; i < betOffersArray.length(); i++) {
            JsonObjectNode node = JsonNode.from(betOffersArray.getJSONObject(i));
            String oddsType = getString(node, "event.betOffers.pathToMatchOddsCheck");
            if (oddsType.equals("Match Odds")) {
                return node;
            }
        }
        return null;
    }

    private JsonNode getNode(JsonNode rootNode, String propertyKey) {
        return getNodeResolvedProperty(rootNode, properties.getProperty(propertyKey));
    }

    private JsonNode getNodeResolvedProperty(JsonNode rootNode, String property) {
        String[] steps = property.split("\\|");
        JsonNode currentNode = rootNode;
        for (String step : steps) {
            currentNode = currentNode.step(step.split(":"));
        }
        return currentNode;
    }

    private String getString(JsonNode rootNode, String propertyKey) {
        JsonNode node = getNode(rootNode, propertyKey);
        return ((JsonStringNode) node).get();
    }

    private Integer getInteger(JsonNode rootNode, String propertyKey) {
        JsonNode node = getNode(rootNode, propertyKey);
        return ((JsonIntegerNode) node).get();
    }

    private JSONArray getJsonArray(JsonNode rootNode, String propertyKey) {
        JsonNode node = getNode(rootNode, propertyKey);
        return ((JsonArrayNode) node).get();
    }
}
