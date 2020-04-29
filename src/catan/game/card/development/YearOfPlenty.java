package catan.game.card.development;

import catan.game.card.Bank;
import catan.game.enumeration.ResourceType;

public class YearOfPlenty extends Development {
    private Bank bank;
    private ResourceType resourceType;

    public YearOfPlenty() {
        super();
        resourceType = null;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    // folosim metoda "use" de doua ori, pentru a fi siguri ca primeste doua resurse
    @Override
    public boolean use() {
        if (bank == null || resourceType == null) {
            return false;
        }
        owner.addResource(resourceType);
        return true;
    }
}
