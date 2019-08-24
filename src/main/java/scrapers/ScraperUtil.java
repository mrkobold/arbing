package scrapers;

import lombok.extern.slf4j.Slf4j;
import model.Event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public final class ScraperUtil {
    public static Map<String, List<Event>> scrapeEvents(List<Provider> providers) {
        log.debug("Begin event scraping...");
        Map<String, List<Event>> scrapedEvents = new HashMap<>();
        for (Provider provider : providers) {
            scrapedEvents.put(provider.getName(), provider.getEvents());
        }
        log.debug("Finished event scraping...");
        return scrapedEvents;
    }
}
