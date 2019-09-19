package scraping;

import lombok.extern.slf4j.Slf4j;
import model.Event;
import scraping.jsonscraping.scrapers.JsonScraper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public final class ScraperUtil {
    public static Map<String, List<Event>> scrapeEvents(List<Scraper> scrapers) {
        log.debug("Begin event scraping...");
        Map<String, List<Event>> scrapedEvents = new HashMap<>();
        for (Scraper scraper : scrapers) {
            scrapedEvents.put(scraper.getName(), scraper.getEvents());
        }
        log.debug("Finished event scraping...");
        return scrapedEvents;
    }
}
