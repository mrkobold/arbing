package scrapers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import model.Event;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Set;

@Slf4j
public abstract class Scraper {
    @Getter
    private String name;
    HttpURLConnection connection;

    Scraper(String name, String urlString) throws IOException {
        this.name = name;
        URL url = new URL(urlString);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
    }

    public List<Event> getEvents() {
        try {
            List<Event> events = getEventsThrowing();
            log.debug("{} scraper finished. |events|={}", name, events.size());
            return events;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    abstract List<Event> getEventsThrowing() throws Exception;
}
