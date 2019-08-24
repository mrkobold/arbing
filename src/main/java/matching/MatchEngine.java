package matching;

import lombok.extern.slf4j.Slf4j;
import model.Event;

import java.util.*;

@Slf4j
public final class MatchEngine {

    public static Set<Match> getMatches(Map<String, List<Event>> events) {
        log.debug("Searching for matches...");
        Set<Match> matches = new HashSet<>();
        int iUnibet = 0, iRedSevens = 0;

        List<Event> unibetEvents = events.get("unibet");
        List<Event> redSevensEvents = events.get("redSevens");
        while (iUnibet < unibetEvents.size() &&
                iRedSevens < redSevensEvents.size()) {
            if (unibetEvents.get(iUnibet).getDate().before(redSevensEvents.get(iRedSevens).getDate())) {
                iUnibet++;
                continue;
            }
            if (unibetEvents.get(iUnibet).getDate().after(redSevensEvents.get(iRedSevens).getDate())) {
                iRedSevens++;
                continue;
            }

            Date groupDate = unibetEvents.get(iUnibet).getDate();
            int unibetCurrent = iUnibet;
            int redSevensCurrent = iRedSevens;
            Event currUnibetEvt, currRedSevensEvt;
            while (unibetCurrent < unibetEvents.size() && (currUnibetEvt = unibetEvents.get(unibetCurrent)).getDate().equals(groupDate)) {
                while (redSevensCurrent < redSevensEvents.size() && (currRedSevensEvt = redSevensEvents.get(redSevensCurrent)).getDate().equals(groupDate)) {
                    if (match(currUnibetEvt, currRedSevensEvt)) {
                        matches.add(new Match(currUnibetEvt, currRedSevensEvt));
                    }
                    redSevensCurrent++;
                }
                redSevensCurrent = iRedSevens;
                unibetCurrent++;
            }
            iUnibet = unibetCurrent;
            iRedSevens = redSevensCurrent;
        }
        log.debug("Match searching done. |matches|={}", matches.size());
        return matches;
    }

    private static boolean match(Event e1, Event e2) {
        List<String> e1Names = getNames(e1);
        List<String> e2Names = getNames(e2);
        List<String> intersection = new ArrayList<>(e1Names);
        intersection.retainAll(e2Names);
        return intersection.size() > 0;
    }

    private static List<String> getNames(Event e1) {
        String e1PlayerTrimmed = e1.getPlayer1().replaceAll("\\s+", "");
        return Arrays.asList(e1PlayerTrimmed.split(","));
    }
}
