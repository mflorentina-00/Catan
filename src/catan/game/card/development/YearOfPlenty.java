package catan.game.card.development;

import catan.game.card.Bank;
import catan.game.enumeration.ResourceType;
import javafx.util.Pair;
import org.apache.http.HttpStatus;

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

    public Pair<Integer, String> getResource() {
        if (owner == null || bank == null || resourceType == null) {
            return new Pair<>(HttpStatus.SC_FORBIDDEN, "Owner, bank or resourceType were not set.");
        }
        if (!bank.existsResource(resourceType)) {
            return new Pair<>(HttpStatus.SC_NOT_FOUND, "The bank has no more " + resourceType + ".");
        }
        owner.takeResource(resourceType);
        return new Pair<>(HttpStatus.SC_FORBIDDEN, "The " + resourceType + " from the bank was took successfully.");
    }
}
