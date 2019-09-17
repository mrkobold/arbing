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
public class RedSevensProvider extends Provider {

    private static final String urlString = "https://sb1capi-altenar.biahosted.com/Sportsbook/GetEvents?timezoneOffset=-180&langId=7&skinName=redsevens&configId=1&culture=ro&countryCode=RO&deviceType=Mobile&sportids=0&categoryids=100153%2C100160%2C100159%2C100447%2C100154%2C100155%2C100478&champids=0&group=AllEvents&period=periodall&withLive=false&outrightsDisplay=none&couponType=0&startDate=2019-08-24T10%3A51%3A00.000Z&endDate=2019-08-31T10%3A51%3A00.000Z";

    public RedSevensProvider() throws Exception {
        super("redSevens");
    }

    @Override
    List<Event> parseEventsFromJSONString(String jsonString) {
        List<Event> eventSet = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray events = jsonObject.getJSONObject("Result").getJSONArray("Items").getJSONObject(0).getJSONArray("Events");
        for (int i = 0; i < events.length(); i++) {
            Optional<Event> optionalEvent = getEventOptional(events.getJSONObject(i));
            optionalEvent.ifPresent(eventSet::add);
        }
        return eventSet;
    }

    private Optional<Event> getEventOptional(JSONObject sonEvent) {
        try {
            JSONArray competitors = sonEvent.getJSONArray("Competitors");
            String player1 = competitors.getJSONObject(0).getString("Name");
            String player2 = competitors.getJSONObject(1).getString("Name");
            Date date = DATE_FORMAT.parse(sonEvent.getString("EventDate"));
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
            return Optional.of(new Event(id, getName(), matchName, player1, player2, date, win1, win2));
        } catch (Exception e) {
            log.warn("couldn't parse an event in scraper: {}", getName());
            return Optional.empty();
        }
    }
}
