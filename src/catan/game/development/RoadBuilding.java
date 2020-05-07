package catan.game.development;

import catan.game.property.Intersection;
import javafx.util.Pair;
import org.apache.http.HttpStatus;

public class RoadBuilding extends Development {
    private Intersection start;
    private Intersection end;

    public RoadBuilding() {
        super();
        start = null;
        end = null;
    }

    public Intersection getStart() {
        return start;
    }

    public void setStart(Intersection start) {
        this.start = start;
    }

    public Intersection getEnd() {
        return end;
    }

    public void setEnd(Intersection end) {
        this.end = end;
    }

}
