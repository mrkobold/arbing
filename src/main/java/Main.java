import arbing.ArbEngine;
import lombok.extern.slf4j.Slf4j;
import matching.MatchEngine;
import matching.MatchingEvents;
import model.Event;
import scraping.Scraper;
import scraping.ScraperUtil;
import scraping.jsonscraping.scrapers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
        log.debug("Started up...");

        List<Scraper> jsonScrapers = Arrays.asList(
                new UnibetJsonScraper(),
                new RedSevensJsonScraper()
        );

        Map<String, List<Event>> scrapedEvents = ScraperUtil.scrapeEvents(jsonScrapers);

        Set<MatchingEvents> matchingEvents = MatchEngine.getMatches(scrapedEvents);

        Set<MatchingEvents> arbs = ArbEngine.getArbs(matchingEvents);
    }


}