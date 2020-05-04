package catan.game.card.development;

import catan.game.Player;

public abstract class Development {
    protected Player owner;

    public Development() {
        owner = null;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
