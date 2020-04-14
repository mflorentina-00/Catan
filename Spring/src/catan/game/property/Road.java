package catan.game.property;

import javafx.util.Pair;

import java.util.Objects;

public class Road extends Property {
    private Pair<Intersection, Intersection> coordinates;

    public Road() {
        super();
        coordinates = null;
    }

    public Pair<Intersection, Intersection> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Intersection start, Intersection end) {
        if (start.getID() < end.getID()) {
            coordinates = new Pair<>(start, end);
        }
        else {
            coordinates = new Pair<>(end, start);
        }
    }

    public Intersection getStart(){
        return coordinates.getKey();
    }

    public Intersection getEnd(){
        return coordinates.getValue();
    }

    public Intersection getCommonIntersection(Road road) {
        if (this.equals(road)) {
            return null;
        }
        if (this.getStart().equals(road.getStart())) {
            return this.getStart();
        }
        if (this.getEnd().equals(road.getEnd())) {
            return this.getEnd();
        }
        return null;
    }

    public boolean hasCommonIntersection(Road road) {
        return getCommonIntersection(road) != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Road)) return false;
        if (!super.equals(o)) return false;
        Road road = (Road) o;
        return Objects.equals(getCoordinates(), road.getCoordinates());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCoordinates());
    }

    @Override
    public String toString() {
        return "Road{" +
                "coordinates=" + coordinates +
                '}';
    }
}
