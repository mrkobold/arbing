package arbing;

import matching.MatchingEvents;
import model.Event;
import org.junit.Test;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;

public class ArbEngineTest {

    @Test
    public void testGetArbs() {
        Event e1 = new Event("1", "a", "a", "a", "a", new Date(), 2.4f, 1.1f);
        Event e2 = new Event("1", "a", "a", "a", "a", new Date(), 1.1f, 3f);

        MatchingEvents matchingEvents = new MatchingEvents(e1, e2);

        Set<MatchingEvents> arbs = ArbEngine.getArbs(Collections.singleton(matchingEvents));
        assertEquals(1, arbs.size());
    }

}
