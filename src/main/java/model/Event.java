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
        return source;
//        return player1 + " " + player2 + " " + win1 + "/" + win2;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash = hash * 31 + source.hashCode();
        hash = hash * 31 + player1.hashCode();
        hash = hash * 31 + player2.hashCode();
        hash = hash * 31 + date.hashCode();
        return hash;
    }
}
