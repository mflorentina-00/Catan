package catan.game.development;

import catan.game.player.Player;

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
