
import lombok.extern.slf4j.Slf4j;
import matching.Match;
import model.Event;
import scrapers.RedSevensScraper;
import scrapers.Scraper;
import scrapers.UnibetScraper;

import java.util.*;

import static matching.MatchEngine.getMatches;
import static scrapers.ScraperUtil.scrapeEvents;

@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
        log.debug("Started up...");

        List<Scraper> scrapers = Arrays.asList(new UnibetScraper(), new RedSevensScraper());

        Map<String, List<Event>> scrapedEvents = scrapeEvents(scrapers);

        Set<Match> matches = getMatches(scrapedEvents);
    }


}