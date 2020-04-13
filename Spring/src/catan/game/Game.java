package catan.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Game {
    private Map<String,Player> players;
    private int maxPlayers;
    private catan.game.map.Map map;

    public Game() {
        players=new HashMap<>();
        map=new catan.game.map.Map();
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, Player> players) {
        this.players = players;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game = (Game) o;
        return Objects.equals(players, game.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(players);
    }
}
