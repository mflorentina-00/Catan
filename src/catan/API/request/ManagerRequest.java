package catan.API.request;

import catan.API.Response;
import catan.Application;
import catan.game.Player;
import catan.game.game.BaseGame;
import catan.game.game.Game;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class ManagerRequest implements GameRequest {
    private String username;
    private String password;
    private String command;
    private String arguments;

    public ManagerRequest(String username, String password, String command, String arguments) {
        this.username = username;
        this.password = password;
        this.command = command;
        this.arguments = arguments;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) { this.arguments = arguments; }

    public Response run() throws JsonProcessingException {
        Map<String, String> requestJson = GameRequest.getMapFromData(arguments);

        if (command.equalsIgnoreCase("newGame")) {
            if (this.arguments.equalsIgnoreCase("{\"scenario\": \"SettlersOfCatan\"}")) {
                String gameId;
                do {
                    gameId = randomString.nextString();
                } while (Application.games.containsKey(gameId) || Application.players.contains(gameId));
                Application.games.put(gameId, new BaseGame());
                Map<String, String> payload = new HashMap<>();
                payload.put("gameId", gameId);
                String responseJson = new ObjectMapper().writeValueAsString(payload);
                return new Response(HttpStatus.SC_OK, "Game created successfully.", responseJson);
            }
            return new Response(HttpStatus.SC_NOT_IMPLEMENTED, "The scenario is not implemented.", "");
        }
        else if (command.equalsIgnoreCase("startGame")) {
            if (requestJson != null){
                String gameId =  requestJson.get("gameId");
                Game game = Application.games.get(gameId);
                if (game == null) {
                    return new Response(HttpStatus.SC_NOT_FOUND, "The game does not exist.", "");
                }
                if (game.startGame()) {
                    Map<String, String> payload = new HashMap<>();
                    payload.put("board", game.getBoard().getBoardJson());
                    payload.put("ports", game.getBoard().getPortsJson());
                    String responseJson = new ObjectMapper().writeValueAsString(payload);
                    return new Response(HttpStatus.SC_OK, "Game has started.", responseJson);
                }
            }
            return new Response(HttpStatus.SC_FORBIDDEN, "Game can not start without players.","");
        }
        else if (command.equalsIgnoreCase("setMaxPlayers")) {
            if (requestJson != null) {
                String gameId = requestJson.get("gameId");
                Game game = Application.games.get(gameId);
                if (game == null) {
                    return new Response(HttpStatus.SC_NOT_FOUND, "The game does not exist.", "");
                }
                int maxPlayers = Integer.parseInt(requestJson.get("maxPlayers"));
                if (game.getNoPlayers() > maxPlayers) {
                    return new Response(HttpStatus.SC_FORBIDDEN, "There are already more players.", "");
                }
                game.setMaxPlayers(maxPlayers);
                return new Response(HttpStatus.SC_OK, "The maximum number of players was set successfully.", "");
            }
        }
        else if (command.equalsIgnoreCase("addPlayer")) {
            if (requestJson != null) {
                String gameId = requestJson.get("gameId");
                Game game = Application.games.get(gameId);
                if (game == null) {
                    return new Response(HttpStatus.SC_NOT_FOUND, "The game does not exist.", "");
                }
                if (game.getNoPlayers() == game.getMaxPlayers()) {
                    return new Response(HttpStatus.SC_FORBIDDEN, "There is no room left.","");
                }
                String playerId;
                do {
                    playerId = randomString.nextString();
                } while (Application.games.containsKey(playerId) || Application.players.contains(playerId));
                game.addPlayer(playerId, new Player(playerId, Application.games.get(gameId)));
                game.addNextPlayer(playerId);
                Map<String, String> payload = new HashMap<>();
                payload.put("playerId", playerId);
                String responseJson = new ObjectMapper().writeValueAsString(payload);
                return new Response(HttpStatus.SC_OK, "The player was added successfully.", responseJson);
            }
        }
        return new Response(HttpStatus.SC_NOT_IMPLEMENTED,"The command was not implemented", command);
    }
}
