package scrapers;

import lombok.extern.slf4j.Slf4j;
import model.Event;
import nodeutils.JsonArrayNode;
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
public class RedSevensProvider extends Provider {

    public RedSevensProvider() throws Exception {
        super("redSevens");
    }

    @Override
    List<Event> parseEventsFromJSONString(String jsonString) {
        JSONObject documentRootObject = new JSONObject(jsonString);
        return getEventList(JsonNode.from(documentRootObject));
    }

    private List<Event> getEventList(JsonNode documentRoot) {
        List<Event> eventList = new ArrayList<>();
        JSONArray eventsArray = getJsonArray(documentRoot, getProperty("events.array"));
        for (int i = 0; i < eventsArray.length(); i++) {
            Optional<Event> optionalEvent = getOptionalEvent(JsonNode.from(eventsArray.getJSONObject(i)));
            optionalEvent.map(eventList::add);
        }
        return eventList;
    }

    private Optional<Event> getOptionalEvent(JsonNode eventRoot) {
        try {
            String eventName = getString(eventRoot, getProperty("event.name"));
            String eventId = Long.toString(getLong(eventRoot, getProperty("event.id")));
            Date start = DATE_FORMAT.parse(getString(eventRoot, getProperty("event.date")));

            JsonArrayNode competitors = (JsonArrayNode) getNode(eventRoot, "jsonArray:Competitors");
            String player1 = getString(competitors, getProperty("player1.name"));
            String player2 = getString(competitors, getProperty("player2.name"));

            JsonArrayNode oddsNode = (JsonArrayNode) getNode(getOddsNode(eventRoot), "jsonArray:Items");
            float win1;
            float win2;

            if (getString(oddsNode, "jsonObject:0|string:Name").equals("1")) {
                win1 = getDouble(oddsNode, "jsonObject:0|double:Price").floatValue();
                win2 = getDouble(oddsNode, "jsonObject:1|double:Price").floatValue();
            } else {
                win1 = getDouble(oddsNode, "jsonObject:1|double:Price").floatValue();
                win2 = getDouble(oddsNode, "jsonObject:0|double:Price").floatValue();
            }

            return Optional.of(new Event(eventId, getName(), eventName, player1, player2, start, win1, win2));
        } catch (Exception e) {
            log.warn("couldn't parse an event in scraper: {}", getName());
            return Optional.empty();
        }
    }

    private JsonObjectNode getOddsNode(JsonNode eventRoot) {
        JSONArray oddsTypesArray = ((JsonArrayNode) getNode(eventRoot, "jsonArray:Items")).get();
        for (int i = 0; i < oddsTypesArray.length(); i++) {
            JsonObjectNode oddsNode = JsonNode.from(oddsTypesArray.getJSONObject(i));
            if (getString(oddsNode, "string:Name").contains("tig")) {
                return oddsNode;
            }
        }
        return null;
    }
}
