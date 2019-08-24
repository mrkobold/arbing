package arbing;

import lombok.extern.slf4j.Slf4j;
import matching.MatchingEvents;
import model.Event;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class ArbEngine {
    public static Set<MatchingEvents> getArbs(Set<MatchingEvents> matchingEvents) {
        log.debug("Searching for arbs...");
        Set<MatchingEvents> arbs = new HashSet<>();
        for (MatchingEvents matchingEvent : matchingEvents) {
            Event e1 = matchingEvent.getE1();
            Event e2 = matchingEvent.getE2();
            float bigPlayer1 = e1.getWin1() > e2.getWin1() ? e1.getWin1() : e2.getWin1();
            float bigPlayer2 = e1.getWin2() > e2.getWin2() ? e1.getWin2() : e2.getWin2();

            if (bigPlayer1 * bigPlayer2 / (bigPlayer1 + bigPlayer2) > 1) {
                arbs.add(matchingEvent);
            }
        }
        log.debug("Arb searching done. |arbs|={}", arbs.size());
        return arbs;
    }
}
