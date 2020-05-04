package catan.game.card.development;

import javafx.util.Pair;
import org.apache.http.HttpStatus;

public class VictoryPoint extends Development {
    public VictoryPoint() {
        super();
    }

    public Pair<Integer, String> use() {
        owner.addVictoryPoint();
        return new Pair<>(HttpStatus.SC_OK, "The Victory Point was added successfully.");
    }
}
