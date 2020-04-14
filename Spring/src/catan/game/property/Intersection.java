package catan.game.property;

import java.util.Objects;

public class Intersection extends Property {
    private int ID;

    public Intersection() {
        ID = -1;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Intersection)) return false;
        if (!super.equals(o)) return false;
        Intersection that = (Intersection) o;
        return getID() == that.getID();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getID());
    }

    @Override
    public String toString() {
        return "Intersection{" +
                "id=" + ID +
                '}';
    }
}
