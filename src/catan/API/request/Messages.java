package catan.API.request;

import java.util.HashMap;
import java.util.Map;

public class Messages {
    private static Messages instance = null;
    private Map<Code, String> messages;

    private Messages() {
        messages = new HashMap<>();
        messages.put(Code.RolledSeven, "Rolled a seven.");
        messages.put(Code.RolledNotSeven, "Rolled not a seven.");
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
