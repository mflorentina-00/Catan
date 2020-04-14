package catan.API.request;

import catan.API.Response;
import catan.Application;
import catan.game.Game;

//TODO Create function or class for every request (Command Pattern style)
//TODO Add request to give resources when the turn starts (kind of manager request I think)
public class UserRequest implements GameRequest {
    private String gameId;
    private String userUniqueID;
    private String command;

    public Response run() {
        Game game = Application.games.get(gameId);
        if (game==null)
            return new Response(Status.ERROR, "The game does not exist.");
        if (game.getPlayers().get(userUniqueID) == null)
            return new Response(Status.ERROR,"The player does not exist.");
        String[] tokens = command.split("[/]+");
        if (tokens[0].equalsIgnoreCase("buyRoad")) {
            if (game.playTurn(userUniqueID, tokens[0])) {
                return new Response(Status.SUCCESS, "Buying road");
            }
            return new Response(Status.ERROR,"Invalid request");
        }
        if (tokens[0].equalsIgnoreCase("buyHouse")) {
            if (game.playTurn(userUniqueID, tokens[0])) {
                return new Response(Status.SUCCESS, "Buying house");
            }
            return new Response(Status.ERROR,"Invalid request");
        }
        if (tokens[0].equalsIgnoreCase("buyCity")) {
            if (game.playTurn(userUniqueID, tokens[0])) {
                return new Response(Status.SUCCESS, "Buying city");
            }
            return new Response(Status.ERROR,"Invalid request");
        }
        if (tokens[0].equalsIgnoreCase("endTurn")) {
            if (game.playTurn(userUniqueID, tokens[0])) {
                return new Response(Status.SUCCESS, "endTurn");
            }
            return new Response(Status.ERROR,"Invalid request");
        }
        return new Response(Status.SUCCESS, "Command unknown");
    }

    public UserRequest(String gameId, String userUniqueID, String command) {
        this.gameId = gameId;
        this.userUniqueID = userUniqueID;
        this.command = command;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) { this.gameId = gameId; }

    public String getUserUniqueID() {
        return userUniqueID;
    }

    public void setUserUniqueID(String userUniqueID) {
        this.userUniqueID = userUniqueID;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
