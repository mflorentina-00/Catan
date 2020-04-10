package com.game.Catan.Game;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GameClass {
    private Map<String,Player> players = new HashMap<>();
    private int maxPlayers;

    public void setPlayers(Map<String, Player> players) {
        this.players = players;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameClass)) return false;
        GameClass gameClass = (GameClass) o;
        return Objects.equals(players, gameClass.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(players);
    }
}
