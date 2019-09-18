package scrapers;

import lombok.extern.slf4j.Slf4j;
import model.Event;
import nodeutils.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.Optional;

import static nodeutils.JsonTraversalHelper.getContent;

@Slf4j
public class UnibetProvider extends Provider {

    public UnibetProvider() throws Exception {
        super("unibet");
    }

    @Override
    Optional<Event> getOptionalEvent(JsonNode<JSONObject> eventRoot) {
        try {
            Date start = DATE_FORMAT.parse(getContent(eventRoot, getProperty("event.date")));

            String player1 = getContent(eventRoot, getProperty("player1.name"));
            String player2 = getContent(eventRoot, getProperty("player2.name"));

            JsonNode<JSONArray> betOffersArrayNode = new JsonNode<>(getContent(eventRoot, getProperty("event.betOffers.root")));
            JsonNode<JSONObject> oddsRootNode = getUnibetMatchOddsRootNode(betOffersArrayNode);

            float win1 = getOdds(oddsRootNode, 1);
            float win2 = getOdds(oddsRootNode, 2);

            return Optional.of(new Event(getName(), player1, player2, start, win1, win2));
        } catch (Exception ex) {
            log.warn("couldn't parse an event in scraper: {}", getName());
            return Optional.empty();
        }
    }

    private float getOdds(JsonNode node, int id) {
        id--;
        String pathToMatchOdds = properties.getProperty("event.betOffers.pathToMatchOdds");
        Integer odds = getContent(node, pathToMatchOdds.replaceAll("\\?", Integer.toString(id)));
        return odds / 1000f;
    }

    private JsonNode<JSONObject> getUnibetMatchOddsRootNode(JsonNode<JSONArray> betOffersArrayNode) {
        JSONArray betOffersArray = betOffersArrayNode.get();
        for (int i = 0; i < betOffersArray.length(); i++) {
            JsonNode<JSONObject> node = new JsonNode<>(betOffersArray.getJSONObject(i));
            String oddsType = getContent(node, getProperty("event.betOffers.pathToMatchOddsCheck"));
            if (oddsType.equals("Match Odds")) {
                return node;
            }
        }
        return null;
    }
}
