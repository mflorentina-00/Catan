package catan.game.card.development;

import catan.game.Player;
import catan.game.enumeration.ResourceType;

import java.util.List;

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
        if (owner == null || resourceType == null || players == null) {
            return false;
        }
        for (Player player : players) {
            if (owner != player) {
                int resourceNumber = player.getResourceNumber(resourceType);
                if (resourceNumber != 0) {
                    owner.addResource(resourceType, resourceNumber);
                    player.removeResource(resourceType, resourceNumber);
                }
            }
        }
        return true;
    }
}
