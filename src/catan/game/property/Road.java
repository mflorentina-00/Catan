package catan.game.property;

import java.util.Objects;

public class Road extends Property {
    private Building start;
    private Building end;

    public Road() {
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

    public Building getCommonIntersection(Road road) {
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
        return Objects.equals(getStart(), road.getStart()) &&
                Objects.equals(getEnd(), road.getEnd());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getStart(), getEnd());
    }

    @Override
    public String toString() {
        return "Road{" +
                "start=" + start +
                ", end=" + end +
                ", owner=" + owner +
                '}';
    }
}
