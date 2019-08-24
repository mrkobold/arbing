package scrapers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import model.Event;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class Provider {
    final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Getter
    String name;
    private HttpURLConnection connection;

    Provider(String name, String urlString) throws IOException {
        this.name = name;
        URL url = new URL(urlString);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
    }

    // TODO tennis/football/etc
    List<Event> getEvents() {
        List<Event> events = getOptionalEventList().orElse(Collections.emptyList());
        log.debug("{} scraper finished. |events|={}", name, events.size());
        return events;
    }

    private Optional<List<Event>> getOptionalEventList() {
        Optional<String> jsonString = getOptionalJsonString();
        return jsonString.map(this::parseEventsFromJSON);
    }

    abstract List<Event> parseEventsFromJSON(String jsonString);

    private Optional<String> getOptionalJsonString() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            return Optional.ofNullable(in.readLine());
        } catch (Exception e) {
            log.warn("Couldn't read json string in scraper: {}", name);
            return Optional.empty();
        }
    }
}
