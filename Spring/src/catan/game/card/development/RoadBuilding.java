package catan.game.card.development;

import catan.game.property.Intersection;

//TODO
public class RoadBuilding extends Development {
    Intersection start;
    Intersection end;

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

    @Override
    public boolean use() {
        // Mutăm road-ul de la bancă la player.
        return true;
    }
}
