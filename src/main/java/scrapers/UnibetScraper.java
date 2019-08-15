package scrapers;

import lombok.extern.slf4j.Slf4j;
import model.Event;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class UnibetScraper extends Scraper {
    private static final String urlString = "https://eu-offering.kambicdn.org/offering/v2018/sbro/listView/tennis.json?lang=ro_RO&market=RO&client_id=2&channel_id=1&ncid=1565702772391&useCombined=true";

    public UnibetScraper() throws Exception {
        super("unibet", urlString);
    }

    @Override
    public List<Event> getEventsThrowing() throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String jsonString = in.readLine();

        return parseEventsFromJSON(jsonString);
    }

    private List<Event> parseEventsFromJSON(String jsonString) throws ParseException {
        List<Event> eventSet = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray events = jsonObject.getJSONArray("events");
        for (int i = 0; i < events.length(); i++) {
            JSONObject rootEvent = events.getJSONObject(i);
            JSONObject eventObject = rootEvent.getJSONObject("event");
            String homeName = eventObject.getString("homeName");
            String awayName = eventObject.getString("awayName");
            Date start = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(eventObject.getString("start"));
            String id = Integer.toString(eventObject.getInt("id"));
            String matchName = eventObject.getString("name");
            float win1 = getForPlayer(rootEvent, 0);
            float win2 = getForPlayer(rootEvent, 1);

            Event event = new Event(id, getName(), matchName, homeName, awayName, start, win1, win2);
            eventSet.add(event);
        }
        return eventSet;
    }

    private static float getForPlayer(JSONObject rootEvent, int id) {
        JSONArray betOffersArray = rootEvent.getJSONArray("betOffers");
        for (int i = 0; i < betOffersArray.length(); i++) {
            if (betOffersArray.getJSONObject(i).getJSONObject("criterion").getString("englishLabel").equals("Match Odds")) {
                return betOffersArray.getJSONObject(i).getJSONArray("outcomes").getJSONObject(id).getInt("odds") / 1000f;
            }
        }
        return 0f;
    }
}
