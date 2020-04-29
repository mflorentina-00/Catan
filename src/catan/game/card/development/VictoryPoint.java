package catan.game.card.development;

public class VictoryPoint extends Development {
    public VictoryPoint() {
        super();
    }

    public boolean use() {
        owner.addVictoryPoint();
        return true;
    }
}
