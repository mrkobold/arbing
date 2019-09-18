package scrapers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import model.Event;
import nodeutils.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import static nodeutils.JsonTraversalHelper.getContent;

@Slf4j
public abstract class Provider {
    final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private static ClassLoader loader = Thread.currentThread().getContextClassLoader();
    final Properties properties = new Properties();

    @Getter
    private String name;
    private HttpURLConnection connection;

    Provider(String name) throws IOException {
        this.name = name;
        properties.load(loader.getResourceAsStream(name + "-descriptor.properties"));

        URL url = new URL(properties.getProperty("url"));
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
    }

    List<Event> getEvents() {
        List<Event> events = getOptionalEventList().orElse(Collections.emptyList());
        log.debug("{} scraper finished. |events|={}", name, events.size());
        return events;
    }

    private Optional<List<Event>> getOptionalEventList() {
        Optional<String> jsonString = getOptionalJsonString();
        return jsonString.map(this::parseEventsFromJSONString);
    }

    private List<Event> parseEventsFromJSONString(String jsonString) {
        return getEventList(JsonNode.from(jsonString));
    }

    private List<Event> getEventList(JsonNode documentRoot) {
        List<Event> eventList = new ArrayList<>();
        JSONArray eventsArray = getContent(documentRoot, getProperty("events.array"));
        for (int i = 0; i < eventsArray.length(); i++) {
            Optional<Event> optionalEvent = getOptionalEvent(new JsonNode<>(eventsArray.getJSONObject(i)));
            optionalEvent.map(eventList::add);
        }
        return eventList;
    }

    abstract Optional<Event> getOptionalEvent(JsonNode<JSONObject> eventRoot);

    private Optional<String> getOptionalJsonString() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            return Optional.ofNullable(in.readLine());
        } catch (Exception e) {
            log.warn("Couldn't read json string in scraper: {}", name);
            return Optional.empty();
        }
    }

    String getProperty(String propertyKey) {
        return properties.getProperty(propertyKey);
    }
}
