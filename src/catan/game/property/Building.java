package catan.game.property;

import catan.game.enumeration.BuildingType;

import java.util.Objects;

public class Building extends Property {
    private int id;
    private BuildingType buildingType;

    public Building(BuildingType buildingType) {
        this.id = -1;
        this.buildingType = buildingType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Building)) return false;
        if (!super.equals(o)) return false;
        Building that = (Building) o;
        return getId() == that.getId() &&
                getBuildingType() == that.getBuildingType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId(), getBuildingType());
    }

    @Override
    public String toString() {
        return "Intersection{" +
                "ID=" + id +
                ", buildingType=" + buildingType +
                ", owner=" + owner +
                '}';
    }
}
