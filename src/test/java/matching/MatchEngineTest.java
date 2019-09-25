package matching;

import model.Event;
import org.junit.Test;

import java.util.*;

import static junit.framework.TestCase.assertEquals;

public class MatchEngineTest {
    private final Date now = new Date();

    @Test
    public void testMatchingAlgorithm() {
        Event e1 = new Event("src1", "player1", "player2", now, 1.0f, 1.0f);
        Event e2 = new Event("src1", "p1", "p2", new Date(now.getTime() + 100), 1.0f, 1.0f);
        Event e3 = new Event("src2", "player1", "player2", now, 1.0f, 1.0f);
        Event e4 = new Event("src2", "p1", "p2", new Date(now.getTime() + 100), 1.0f, 1.0f);
        Event e5 = new Event("src3", "player1", "player2", now, 1.0f, 1.0f);
        Event e6 = new Event("src3", "p1", "p2", new Date(now.getTime() + 100), 1.0f, 1.0f);

        Map<String, List<Event>> events = new HashMap<>();
        List<Event> src1 = new ArrayList<>();
        src1.add(e1);
        src1.add(e2);

        List<Event> src2 = new ArrayList<>();
        src2.add(e3);
        src2.add(e4);

        List<Event> src3 = new ArrayList<>();
        src3.add(e5);
        src3.add(e6);

        events.put(src1.get(0).getSource(), src1);
        events.put(src2.get(0).getSource(), src2);
        events.put(src3.get(0).getSource(), src3);

        Set<MatchingEvents> matches = MatchEngine.getMatches(events);
        assertEquals(6, matches.size());
    }

}