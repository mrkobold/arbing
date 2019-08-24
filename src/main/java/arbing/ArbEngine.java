package arbing;

import lombok.extern.slf4j.Slf4j;
import matching.Match;
import model.Event;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class ArbEngine {
    public static Set<Match> getArbs(Set<Match> matches) {
        log.debug("Searching for arbs...");
        Set<Match> arbs = new HashSet<>();
        for (Match match : matches) {
            Event e1 = match.getE1();
            Event e2 = match.getE2();
            float bigPlayer1 = e1.getWin1() > e2.getWin1() ? e1.getWin1() : e2.getWin1();
            float bigPlayer2 = e1.getWin2() > e2.getWin2() ? e1.getWin2() : e2.getWin2();

            if (bigPlayer1 * bigPlayer2 / (bigPlayer1 + bigPlayer2) > 1) {
                arbs.add(match);
            }
        }
        log.debug("Arb searching done. |arbs|={}", arbs.size());
        return arbs;
    }
}
