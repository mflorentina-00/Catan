package catan.game.card.development;

import catan.game.Player;
import catan.game.enumeration.ResourceType;

import java.util.List;

//TODO
public class Monopoly extends Development {
    private ResourceType resourceType;
    private List<Player> players;

    public Monopoly() {
        super();
        resourceType = null;
        players = null;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    @Override
    public boolean use() {
        // foreach player in lista de playeri din lobby
        // daca player-ul nu este player-ul curent
        // mutam resursa de la el la player-ul curent
        return true;
    }
}
