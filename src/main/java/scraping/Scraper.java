package scraping;

import model.Event;

import java.util.List;

public interface Scraper {
    List<Event> getEvents();

    String getName();
}
