package catan.util;

import catan.API.response.Code;
import catan.game.enumeration.Resource;

public class Helper {
    /**
     * In order to get String from ResourceType, use ResourceType.lumber.toString().
     */
    public static Resource getResourceTypeFromString(String resource) {
        switch (resource) {
            case "lumber":
                return Resource.lumber;
            case "wool":
                return Resource.wool;
            case "grain":
                return Resource.grain;
            case "brick":
                return Resource.brick;
            case "ore":
                return Resource.ore;
            case "desert":
                return Resource.desert;
            default:
                return null;
        }
    }

    public static Code getPlayerCodeFromResourceType(Resource resource) {
        switch (resource) {
            case lumber:
                return Code.PlayerNoLumber;
            case wool:
                return Code.PlayerNoWool;
            case grain:
                return Code.PlayerNoGrain;
            case brick:
                return Code.PlayerNoBrick;
            case ore:
                return Code.PlayerNoOre;
            default:
                return null;
        }
    }

    public static Code getBankCodeFromResourceType(Resource resource) {
        switch (resource) {
            case lumber:
                return Code.BankNoLumber;
            case wool:
                return Code.BankNoWool;
            case grain:
                return Code.BankNoGrain;
            case brick:
                return Code.BankNoBrick;
            case ore:
                return Code.BankNoOre;
            default:
                return null;
        }
    }
}
