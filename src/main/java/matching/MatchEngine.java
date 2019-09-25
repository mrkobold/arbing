package matching;

import lombok.extern.slf4j.Slf4j;
import model.Event;

import java.util.*;

@Slf4j
public final class MatchEngine {

    private static final Comparator<Event> comparator = Comparator.comparing(Event::getDate);
    private static final Date INFINITE_DATE = new Date(253370757600000L); // 9999-01-01

    public static Set<MatchingEvents> getMatches(Map<String, List<Event>> events) {
        log.debug("Sorting events for each scraper by date...");
        events.values().forEach(l -> l.sort(comparator));

        log.debug("Searching for matching events...");
        Set<MatchingEvents> matchingEvents = new HashSet<>();

        List<Integer> timeblockStarts = initArrayList(events.size(), 0);
        List<List<Event>> eventsLists = new ArrayList<>(events.values());

        while (removeFinishedListsStayMin2(eventsLists, timeblockStarts)) {
            Date t_min = findMinDate(eventsLists, timeblockStarts);
            matchingEvents.addAll(getMatchesPlayedAtTMIN(eventsLists, t_min, timeblockStarts));
        }
        return matchingEvents;
    }

    private static Collection<MatchingEvents> getMatchesPlayedAtTMIN(List<List<Event>> eventsLists, Date t_min, List<Integer> timeblockStarts) {
        List<Integer> timeblockStartsTMIN = initArrayList(eventsLists.size(), null);
        List<Integer> timeblockEndsTMIN = initArrayList(eventsLists.size(), null);
        findIntervalsOfTMIN(eventsLists, t_min, timeblockStarts, timeblockStartsTMIN, timeblockEndsTMIN);

        Set<MatchingEvents> result = new HashSet<>();
        for (int i = 0; i < eventsLists.size(); i++) {
            for (int j = i + 1; j < eventsLists.size(); j++) {
                result.addAll(getMatchingEvents(eventsLists, timeblockStartsTMIN, timeblockEndsTMIN, i, j));
            }
        }

        stepInTimeBlockStarts(eventsLists, timeblockStarts, timeblockEndsTMIN);
        return result;
    }

    private static void stepInTimeBlockStarts(List<List<Event>> eventsLists, List<Integer> timeblockStarts, List<Integer> timeblockEndsTMIN) {
        for (int i = 0; i < eventsLists.size(); i++) {
            if (timeblockEndsTMIN.get(i) != null) {
                timeblockStarts.set(i, timeblockEndsTMIN.get(i) + 1);
            }
        }
    }

    private static void findIntervalsOfTMIN(List<List<Event>> eventsLists,
                                            Date t_min,
                                            List<Integer> timeblockStarts,
                                            List<Integer> timeblockStartsTMIN,
                                            List<Integer> timeblockEndsTMIN) {
        for (int i = 0; i < eventsLists.size(); i++) {
            List<Event> currentList = eventsLists.get(i);
            if (currentList.get(timeblockStarts.get(i)).getDate().equals(t_min)) {
                timeblockStartsTMIN.set(i, timeblockStarts.get(i));
                int blockEnd = timeblockStarts.get(i);
                while (blockEnd < currentList.size() && currentList.get(blockEnd).getDate().equals(t_min)) {
                    blockEnd++;
                }
                timeblockEndsTMIN.set(i, blockEnd - 1);
            }
        }
    }

    private static Set<MatchingEvents> getMatchingEvents(List<List<Event>> eventsLists,
                                                         List<Integer> timeblockStartsTMIN,
                                                         List<Integer> timeblockEndsTMIN,
                                                         int i, int j) {
        if (timeblockStartsTMIN.get(i) == null || timeblockStartsTMIN.get(j) == null) {
            return Collections.emptySet();
        }

        Set<MatchingEvents> result = new HashSet<>();
        for (Event e1 : eventsLists.get(i).subList(timeblockStartsTMIN.get(i), timeblockEndsTMIN.get(i) + 1)) {
            for (Event e2 : eventsLists.get(j).subList(timeblockStartsTMIN.get(j), timeblockEndsTMIN.get(j) + 1)) {
                if (isMatch(e1, e2)) {
                    result.add(new MatchingEvents(e1, e2));
                }
            }
        }
        return result;
    }

    private static Date findMinDate(List<List<Event>> eventLists, List<Integer> timeBlockStarts) {
        Date t_min = INFINITE_DATE;
        for (int i = 0; i < eventLists.size(); i++) {
            List<Event> current_list = eventLists.get(i);
            Date current_t_min = current_list.get(timeBlockStarts.get(i)).getDate();
            if (current_t_min.before(t_min)) {
                t_min = current_t_min;
            }
        }
        return t_min;
    }

    private static boolean removeFinishedListsStayMin2(List<List<Event>> eventsLists, List<Integer> timeBlockStarts) {
        Iterator<List<Event>> iterator = eventsLists.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            List<Event> list = iterator.next();
            if (list.size() == timeBlockStarts.get(i)) {
                iterator.remove();
                timeBlockStarts.remove(i);
                i--;
            }
            i++;
        }
        return eventsLists.size() >= 2;
    }

    private static List<Integer> initArrayList(int length, Integer val) {
        List<Integer> result = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            result.add(val);
        }
        return result;
    }

    private static boolean isMatch(Event e1, Event e2) {
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
