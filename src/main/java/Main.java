import arbing.ArbEngine;
import lombok.extern.slf4j.Slf4j;
import matching.MatchEngine;
import matching.MatchingEvents;
import model.Event;
import scrapers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
        log.debug("Started up...");

        List<Provider> providers = Arrays.asList(
                new UnibetProvider(),
                new RedSevensProvider()
        );

        Map<String, List<Event>> scrapedEvents = ScraperUtil.scrapeEvents(providers);

        Set<MatchingEvents> matchingEvents = MatchEngine.getMatches(scrapedEvents);

        Set<MatchingEvents> arbs = ArbEngine.getArbs(matchingEvents);
    }


}