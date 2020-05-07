package catan.game.property;

import catan.game.enumeration.Building;

import java.util.Objects;

public class Intersection extends Property {
    private int id;
    private Building building;

    public Intersection(int id) {
        super();
        this.id = id;
        this.building = Building.None;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Intersection)) return false;
        if (!super.equals(o)) return false;
        Intersection intersection = (Intersection) o;
        return getId() == intersection.getId() &&
                getBuilding() == intersection.getBuilding();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId(), getBuilding());
    }

    @Override
    public String toString() {
        return "Building{" +
                "owner=" + owner +
                ", id=" + id +
                ", buildingType=" + building +
                '}';
    }
}
