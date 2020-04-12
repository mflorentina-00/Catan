package catan.game.property;

import java.util.Objects;

public class Intersection extends Property {
    private int id;

    public Intersection() {
        id = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Intersection)) return false;
        if (!super.equals(o)) return false;
        Intersection that = (Intersection) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId());
    }

    @Override
    public String toString() {
        return "Intersection{" +
                "id=" + id +
                '}';
    }
}
