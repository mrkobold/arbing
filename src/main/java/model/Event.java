package model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
public class Event {

    @Getter
    private final String source;
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
        return player1 + " " + player2 + " " + win1 + "/" + win2;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Event)) {
            return false;
        }

        Event otherEvent = (Event) other;
        return otherEvent.source.equals(source) &&
                otherEvent.player1.equals(player1) &&
                otherEvent.player2.equals(player2) &&
                otherEvent.date.equals(date);
    }
}
