package catan.API.request;

import catan.API.response.Response;
import catan.Application;
import catan.game.game.Game;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.HttpStatus;

import java.util.Map;

public class UserRequest implements GameRequest {
    private String gameId;
    private String playerId;
    private String command;
    private Map<String, Object> arguments;

    public UserRequest(String gameId, String playerId, String command, Map<String, Object> arguments) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.command = command;
        this.arguments = arguments;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) { this.gameId = gameId; }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Map<String, Object> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, Object> arguments) { this.arguments = arguments; }

    public Response run() {
        Game game = Application.games.get(gameId);
        if (game == null) {
            return new Response(HttpStatus.SC_ACCEPTED, "The game does not exist.",null);
        }
        if (game.getPlayer(playerId) == null) {
            return new Response(HttpStatus.SC_ACCEPTED,"The player does not exist.",null);
        }
        try {
            return game.playTurn(playerId, command, arguments);
        } catch (JsonProcessingException e) {
            return new Response(HttpStatus.SC_ACCEPTED, "Wrong command.", null);
        }
    }
}
