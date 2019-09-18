package scrapers;

import lombok.extern.slf4j.Slf4j;
import model.Event;
import nodeutils.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static nodeutils.JsonTraversalHelper.getContent;

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
        JSONArray eventsArray = getContent(documentRoot, getProperty("events.array"));
        for (int i = 0; i < eventsArray.length(); i++) {
            Optional<Event> optionalEvent = getOptionalEvent(JsonNode.from(eventsArray.getJSONObject(i)));
            optionalEvent.map(eventList::add);
        }
        return eventList;
    }

    private Optional<Event> getOptionalEvent(JsonNode eventRoot) {
        try {
            Date start = DATE_FORMAT.parse(getContent(eventRoot, getProperty("event.date")));

            JsonNode<JSONArray> competitors = JsonNode.from(getContent(eventRoot, "Competitors"));
            String player1 = getContent(competitors, getProperty("player1.name"));
            String player2 = getContent(competitors, getProperty("player2.name"));

            JsonNode<JSONArray> oddsNode = JsonNode.from(getContent(getOddsNode(eventRoot), "Items"));
            Double win1;
            Double win2;

            if (getContent(oddsNode, "0|Name").equals("1")) {
                win1 = getContent(oddsNode, "0|Price");
                win2 = getContent(oddsNode, "1|Price");
            } else {
                win1 = getContent(oddsNode, "1|Price");
                win2 = getContent(oddsNode, "0|Price");
            }

            return Optional.of(new Event(getName(), player1, player2, start, win1.floatValue(), win2.floatValue()));
        } catch (Exception e) {
            log.warn("couldn't parse an event in scraper: {}", getName());
            return Optional.empty();
        }
    }

    private JsonNode<JSONObject> getOddsNode(JsonNode eventRoot) {
        JSONArray oddsTypesArray = getContent(eventRoot, "Items");
        for (int i = 0; i < oddsTypesArray.length(); i++) {
            JsonNode<JSONObject> oddsNode = JsonNode.from(oddsTypesArray.getJSONObject(i));
            String content = getContent(oddsNode, "Name");
            if (content.contains("tig")) {
                return oddsNode;
            }
        }
        return null;
    }
}
