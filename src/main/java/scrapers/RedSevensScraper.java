package scrapers;

import lombok.extern.log4j.Log4j;
import model.Event;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log4j
public class RedSevensScraper extends Scraper {

    private static final String urlString = "https://sb1capi-altenar.biahosted.com/Sportsbook/GetEvents?timezoneOffset=-180&langId=7&skinName=redsevens&configId=1&culture=ro&countryCode=RO&deviceType=Mobile&sportids=4&categoryids=0&champids=0&group=AllEvents&period=periodall&withLive=false&outrightsDisplay=none&couponType=0&startDate=2019-08-13T13%3A36%3A00.000Z&endDate=2019-08-20T13%3A36%3A00.000Z";

    public RedSevensScraper() throws Exception {
        super("redSevens", urlString);
    }

    @Override
    public List<Event> getEventsThrowing() throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String jsonString = in.readLine();

        return parseEventsFromJSON(jsonString);
    }

    private List<Event> parseEventsFromJSON(String jsonString) throws Exception {
        List<Event> eventSet = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray events = jsonObject.getJSONObject("Result").getJSONArray("Items").getJSONObject(0).getJSONArray("Events");
        for (int i = 0; i < events.length(); i++) {
            JSONObject sonEvent = events.getJSONObject(i);

            JSONArray competitors = sonEvent.getJSONArray("Competitors");
            String player1 = competitors.getJSONObject(0).getString("Name");
            String player2 = competitors.getJSONObject(1).getString("Name");
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(sonEvent.getString("EventDate"));
            String id = Long.toString(sonEvent.getLong("Id"));
            String matchName = sonEvent.getString("Name");
            JSONArray odds = sonEvent.getJSONArray("Items").getJSONObject(0).getJSONArray("Items");
            float win1;
            float win2;
            if (odds.getJSONObject(0).get("Name").equals("1")) {
                win1 = (float) odds.getJSONObject(0).getDouble("Price");
                win2 = (float) odds.getJSONObject(1).getDouble("Price");
            } else {
                win1 = (float) odds.getJSONObject(1).getDouble("Price");
                win2 = (float) odds.getJSONObject(0).getDouble("Price");
            }
            Event event = new Event(id, getName(), matchName, player1, player2, date, win1, win2);
            eventSet.add(event);
        }

        return eventSet;
    }
}
