package catan.game.card.development;

import catan.game.enumeration.ResourceType;

//TODO
public class YearOfPlenty extends Development {
    ResourceType firstResourceType;
    ResourceType secondResourceType;

    public YearOfPlenty() {
        super();
        firstResourceType = null;
        secondResourceType = null;
    }

    public ResourceType getFirstResourceType() {
        return firstResourceType;
    }

    public void setFirstResourceType(ResourceType firstResourceType) {
        this.firstResourceType = firstResourceType;
    }

    public ResourceType getSecondResourceType() {
        return secondResourceType;
    }

    public void setSecondResourceType(ResourceType secondResourceType) {
        this.secondResourceType = secondResourceType;
    }

    @Override
    public boolean use() {
        // Mutăm resursele de la bancă la player (dacă mai există).
        return true;
    }
}
