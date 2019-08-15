package matching;

import lombok.AllArgsConstructor;
import lombok.Getter;
import model.Event;

@AllArgsConstructor
public class Match {
    @Getter
    private final Event e1;
    @Getter
    private final Event e2;

    @Override
    public String toString() {
        return e1.toString() + " ::" + e2.toString();
    }
}
