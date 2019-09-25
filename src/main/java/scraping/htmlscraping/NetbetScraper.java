package scraping.htmlscraping;

import lombok.extern.slf4j.Slf4j;
import model.Event;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import scraping.Scraper;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class NetbetScraper implements Scraper {
    static {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Mr. Kobold\\Desktop\\chromeDriver\\chromedriver_win32\\chromedriver.exe");
    }

    private final String name = "netbet";
    private final Date date = new Date();

    @Override
    public List<Event> getEvents() {
        ChromeDriver driver = new ChromeDriver();
        driver.get("https://sport.netbet.ro/tenis/");
        return new ArrayList<>(getEvents(driver));
    }

    private Set<Event> getEvents(ChromeDriver driver) {
        Set<Event> matches = new HashSet<>();
        Boolean done = false;
        try {
            Thread.sleep(1000);
            while (!done) {
                List<WebElement> currentElements = driver.findElements(By.xpath("//div[" +
                        "contains(@class, 'rj-ev-list__ev-card') and " +
                        "contains(@class, 'rj-ev-list__ev-card--upcoming') and " +
                        "contains(@class, 'rj-ev-list__ev-card--multiview')]"));
                Set<String> eventStrings = currentElements.stream().map(WebElement::getText).collect(Collectors.toSet());
                Set<Event> currentBatch = getEventsFrom(eventStrings);
                matches.addAll(currentBatch);
                driver.executeScript("window.scrollBy(0,500)");
                done = (Boolean) driver.executeScript("return (window.innerHeight + window.scrollY) >= document.body.offsetHeight");
                Thread.sleep(300);
            }
        } catch (Exception ex) {
            log.warn("Could not parse ");
        }
        return matches;
    }

    private Set<Event> getEventsFrom(Set<String> eventStrings) {
        Set<Event> result = new HashSet<>();
        for (String eventString : eventStrings) {
            String[] parts = eventString.split("\n");
            Event event = new Event(name, parts[0], parts[1], getDateFrom(parts[2], parts[3]), Float.parseFloat(parts[4]), Float.parseFloat(parts[5]));
            if (event.getDate().after(date)) {
                result.add(event);
            }
        }
        return result;
    }

    private static Date getDateFrom(String date, String time) {
        List<Integer> dateParts = Arrays.stream(date.split("/")).map(Integer::parseInt).collect(Collectors.toList());
        List<Integer> timeParts = Arrays.stream(time.split(":")).map(Integer::parseInt).collect(Collectors.toList());
        return new Date(2019 - 1900, dateParts.get(1) - 1, dateParts.get(0), timeParts.get(0), timeParts.get(1));
    }

    @Override
    public String getName() {
        return name;
    }
}
