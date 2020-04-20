package catan.API.request;

import catan.API.Response;
import catan.Application;
import catan.game.gameType.Game;

//TODO Create function or class for every request (Command Pattern style)
//TODO Add request to give resources when the turn starts (kind of manager request I think)
public class UserRequest implements GameRequest {
    private String gameId;
    private String userUniqueID;
    private String command;
    private String jsonArgs;

    public Response run() {
        Game game = Application.games.get(gameId);
        if (game == null)
            return new Response(Status.ERROR, "The game does not exist.");
        if (game.getPlayers().get(userUniqueID) == null)
            return new Response(Status.ERROR,"The player does not exist.");
        String[] tokens = command.split("[/]+");
        return game.playTurn(userUniqueID, tokens[0],jsonArgs);
    }

    public UserRequest(String gameId, String userUniqueID, String command, String jsonArgs) {
        this.gameId = gameId;
        this.userUniqueID = userUniqueID;
        this.command = command;
        this.jsonArgs = jsonArgs;
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

    public String getJsonArgs() {
        return jsonArgs;
    }
}
