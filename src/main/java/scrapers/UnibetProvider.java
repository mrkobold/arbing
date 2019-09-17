package scrapers;

import lombok.extern.slf4j.Slf4j;
import model.Event;
import nodeutils.JsonArrayNode;
import nodeutils.JsonIntegerNode;
import nodeutils.JsonNode;
import nodeutils.JsonObjectNode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static nodeutils.JsonTraversalHelper.*;

@Slf4j
public class UnibetProvider extends Provider {

    public UnibetProvider() throws Exception {
        super("unibet");
    }

    @Override
    List<Event> parseEventsFromJSONString(String jsonString) {
        JSONObject documentRootObject = new JSONObject(jsonString);
        return getEventList(JsonNode.from(documentRootObject));
    }

    private List<Event> getEventList(JsonNode documentRoot) {
        List<Event> eventList = new ArrayList<>();
        JSONArray eventsArray = getJsonArray(documentRoot, "events.array");
        for (int i = 0; i < eventsArray.length(); i++) {
            Optional<Event> optionalEvent = getOptionalEvent(JsonNode.from(eventsArray.getJSONObject(i)));
            optionalEvent.map(eventList::add);
        }
        return eventList;
    }

    private Optional<Event> getOptionalEvent(JsonNode eventRoot) {
        try {
            String eventName = getString(eventRoot, getProperty("event.name"));
            String eventId = Integer.toString(getInteger(eventRoot, getProperty("event.id")));
            Date start = DATE_FORMAT.parse(getString(eventRoot, getProperty("event.date")));

            String player1 = getString(eventRoot, "player1.name");
            String player2 = getString(eventRoot, "player2.name");

            JsonArrayNode betOffersArrayNode = JsonNode.from(getJsonArray(eventRoot, getProperty("event.betOffers.root")));
            JsonObjectNode oddsRootNode = getUnibetMatchOddsRootNode(betOffersArrayNode);

            float win1 = getOdds(oddsRootNode, 1);
            float win2 = getOdds(oddsRootNode, 2);

            return Optional.of(new Event(eventId, getName(), eventName, player1, player2, start, win1, win2));
        } catch (Exception ex) {
            log.warn("couldn't parse an event in scraper: {}", getName());
            return Optional.empty();
        }
    }

    private String getProperty(String propertyKey) {
        return properties.getProperty(propertyKey);
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
}
