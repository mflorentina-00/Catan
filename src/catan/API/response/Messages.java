package catan.API.response;

import java.util.HashMap;
import java.util.Map;

public class Messages {
    private static Messages instance = null;
    private Map<Code, String> messages;

    private Messages() {
        messages = new HashMap<>();
        messages.put(Code.RolledSeven, "Rolled a seven.");
        messages.put(Code.RolledNotSeven, "Rolled not a seven.");
        messages.put(Code.PlayerNoLumber, "You have no more lumber.");
        messages.put(Code.PlayerNoWool, "You have no more wool.");
        messages.put(Code.PlayerNoGrain, "You have no more grain.");
        messages.put(Code.PlayerNoBrick, "You have no more brick.");
        messages.put(Code.PlayerNoOre, "You have no more ore.");
        messages.put(Code.BankNoLumber, "The bank has no more lumber.");
        messages.put(Code.BankNoWool, "The bank has no more wool.");
        messages.put(Code.BankNoGrain, "The bank has no more grain.");
        messages.put(Code.BankNoBrick, "The bank has no more brick.");
        messages.put(Code.BankNoOre, "The bank has no more ore.");
        messages.put(Code.NoRoad, "You have no more roads to build.");
        messages.put(Code.NoSettlement, "You have no more settlements to build.");
        messages.put(Code.NoCity, "You have no more cities to build.");
        messages.put(Code.SameTile, "You can not move robber on the same tile.");
    }

    public static Messages getInstance() {
        if (instance == null) {
            instance = new Messages();
        }
        return instance;
    }

    public String getMessage(Code code) {
        return messages.get(code);
    }
}
