package trial;

import model.Event;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.*;
import java.util.stream.Collectors;

public class SeleniumScrapingLearning {
    static {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Mr. Kobold\\Desktop\\chromeDriver\\chromedriver_win32\\chromedriver.exe");
    }

    public static void main(String[] args) throws InterruptedException {
        ChromeDriver driver = new ChromeDriver();
        driver.get("https://sport.netbet.ro/tenis/");
        Set<Event> matches = new HashSet<>();
        Boolean done = false;

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

        System.out.println();
    }

    private static Set<Event> getEventsFrom(Set<String> eventStrings) {
        Set<Event> result = new HashSet<>();
        for (String eventString : eventStrings) {
            String[] parts = eventString.split("\n");
            Event event = new Event("testScraper", parts[0], parts[1], getDateFrom(parts[2], parts[3]), Float.parseFloat(parts[4]), Float.parseFloat(parts[5]));
            result.add(event);
        }
        return result;
    }

    private static Date getDateFrom(String date, String time) {
        List<Integer> dateParts = Arrays.stream(date.split("/")).map(Integer::parseInt).collect(Collectors.toList());
        List<Integer> timeParts = Arrays.stream(time.split(":")).map(Integer::parseInt).collect(Collectors.toList());
        return new Date(2019 - 1900, dateParts.get(1) - 1, dateParts.get(0), timeParts.get(0), timeParts.get(1));
    }
}
