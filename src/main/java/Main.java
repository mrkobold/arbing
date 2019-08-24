import arbing.ArbEngine;
import lombok.extern.slf4j.Slf4j;
import matching.Match;
import matching.MatchEngine;
import model.Event;
import scrapers.RedSevensScraper;
import scrapers.Scraper;
import scrapers.ScraperUtil;
import scrapers.UnibetScraper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
        log.debug("Started up...");

        List<Scraper> scrapers = Arrays.asList(new UnibetScraper(), new RedSevensScraper());

        Map<String, List<Event>> scrapedEvents = ScraperUtil.scrapeEvents(scrapers);

        Set<Match> matches = MatchEngine.getMatches(scrapedEvents);

        Set<Match> arbs = ArbEngine.getArbs(matches);
    }


}