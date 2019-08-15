package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
public class Event {
    @Getter
    private final String id;
    @Getter
    private final String source;
    @Getter
    private final String name;
    @Getter
    private final String player1;
    @Getter
    private final String player2;
    @Getter
    private final Date date;
    @Getter
    private final float win1;
    @Getter
    private final float win2;

    public String toString() {
        return name + " " + win1 + "/" + win2;
    }
}
