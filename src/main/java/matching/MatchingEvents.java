package matching;

import lombok.AllArgsConstructor;
import lombok.Getter;
import model.Event;

@AllArgsConstructor
public class MatchingEvents {
    @Getter
    private final Event e1;
    @Getter
    private final Event e2;

    @Override
    public String toString() {
        return e1.toString() + " ::" + e2.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof MatchingEvents)) {
            return false;
        }
        MatchingEvents other = (MatchingEvents) o;
        return other.e1.equals(e1) && other.e2.equals(e2) ||
                other.e1.equals(e2) && other.e2.equals(e1);
    }
}
