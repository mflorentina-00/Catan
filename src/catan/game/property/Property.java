package catan.game.property;

import catan.game.player.Player;

import java.util.Objects;

public abstract class Property {
    protected Player owner;

    public Property() {
        owner = null;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Property)) return false;
        Property property = (Property) o;
        return Objects.equals(getOwner(), property.getOwner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOwner());
    }

    @Override
    public String toString() {
        return "Property{" +
                "owner=" + owner +
                '}';
    }
}
