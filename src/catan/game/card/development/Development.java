package catan.game.card.development;

import catan.game.Player;

/**
 * "use" is boolean in order to verify if it can be used
 */
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

    public abstract boolean use();
}
