package scrapers;

import lombok.extern.slf4j.Slf4j;
import model.Event;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
public class UnibetScraper extends Scraper {
    private static final String urlString = "https://eu-offering.kambicdn.org/offering/v2018/sbro/listView/tennis.json?lang=ro_RO&market=RO&client_id=2&channel_id=1&ncid=1565702772391&useCombined=true";

    public UnibetScraper() throws Exception {
        super("unibet", urlString);
    }

    List<Event> parseEventsFromJSON(String jsonString) {
        List<Event> eventSet = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray events = jsonObject.getJSONArray("events");
        for (int i = 0; i < events.length(); i++) {
            Optional<Event> optionalEvent = getEventOptional(events.getJSONObject(i));
            optionalEvent.ifPresent(eventSet::add);
        }
        return eventSet;
    }

    private Optional<Event> getEventOptional(JSONObject rootEvent) {
        try {
            JSONObject eventObject = rootEvent.getJSONObject("event");
            String homeName = eventObject.getString("homeName");
            String awayName = eventObject.getString("awayName");
            Date start = DATE_FORMAT.parse(eventObject.getString("start"));
            String id = Integer.toString(eventObject.getInt("id"));
            String matchName = eventObject.getString("name");
            float win1 = getForPlayer(rootEvent.getJSONArray("betOffers"), 0);
            float win2 = getForPlayer(rootEvent.getJSONArray("betOffers"), 1);

            return Optional.of(new Event(id, getName(), matchName, homeName, awayName, start, win1, win2));
        } catch (Exception e) {
            log.warn("couldn't parse an event in scraper: {}", getName());
            return Optional.empty();
        }
    }

    private static float getForPlayer(JSONArray betOffersArray, int id) {
        for (int i = 0; i < betOffersArray.length(); i++) {
            if (betOffersArray.getJSONObject(i).getJSONObject("criterion").getString("englishLabel").equals("Match Odds")) {
                return betOffersArray.getJSONObject(i).getJSONArray("outcomes").getJSONObject(id).getInt("odds") / 1000f;
            }
        }
        return 0f;
    }
}
