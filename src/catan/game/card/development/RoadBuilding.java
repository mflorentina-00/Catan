package catan.game.card.development;

import catan.game.property.Building;
import javafx.util.Pair;
import org.apache.http.HttpStatus;

public class RoadBuilding extends Development {
    private Building start;
    private Building end;

    public RoadBuilding() {
        super();
        start = null;
        end = null;
    }

    public Building getStart() {
        return start;
    }

    public void setStart(Building start) {
        this.start = start;
    }

    public Building getEnd() {
        return end;
    }

    public void setEnd(Building end) {
        this.end = end;
    }

    public Pair<Integer, String> buildRoad() {
        if (owner == null || start == null || end == null) {
            return new Pair<>(HttpStatus.SC_FORBIDDEN, "Owner, start or end were not set.");
        }
        return owner.buildRoad(start, end);
    }
}
