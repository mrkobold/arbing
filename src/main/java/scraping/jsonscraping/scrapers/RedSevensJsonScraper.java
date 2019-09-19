package scraping.jsonscraping.scrapers;

import lombok.extern.slf4j.Slf4j;
import model.Event;
import scraping.jsonscraping.nodeutils.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.Optional;

import static scraping.jsonscraping.nodeutils.JsonTraversalHelper.getContent;

@Slf4j
public class RedSevensJsonScraper extends JsonScraper {

    public RedSevensJsonScraper() throws Exception {
        super("redSevens");
    }

    @Override
    Optional<Event> getOptionalEvent(JsonNode eventRoot) {
        try {
            Date start = DATE_FORMAT.parse(getContent(eventRoot, getProperty("event.date")));

            JsonNode<JSONArray> competitors = new JsonNode<>(getContent(eventRoot, "Competitors"));
            String player1 = getContent(competitors, getProperty("player1.name"));
            String player2 = getContent(competitors, getProperty("player2.name"));

            JsonNode<JSONArray> oddsNode = new JsonNode<>(getContent(getOddsNode(eventRoot), "Items"));
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
            JsonNode<JSONObject> oddsNode = new JsonNode<>(oddsTypesArray.getJSONObject(i));
            String content = getContent(oddsNode, "Name");
            if (content.contains("tig")) {
                return oddsNode;
            }
        }
        return null;
    }
}
