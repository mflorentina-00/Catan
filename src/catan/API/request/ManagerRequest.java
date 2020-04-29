package catan.API.request;

import catan.API.Response;
import catan.Application;
import catan.game.Player;
import catan.game.gameType.BaseGame;
import catan.game.gameType.Game;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
            String gameKey = randomString.nextString();
            if (this.arguments.equalsIgnoreCase("{\"scenario\": \"SettlersOfCatan\"}")) {
                Application.games.put(gameKey, new BaseGame());
                Map<String, String> payload = new HashMap<>();
                payload.put("gameId", gameKey);
                String responseJson = new ObjectMapper().writeValueAsString(payload);
                return new Response(Status.SUCCESS, "Game created successfully.", responseJson);
            }
            return new Response(Status.ERROR, "The scenario is not implemented.", "");
        }
        else if (command.equalsIgnoreCase("startGame")) {
            if (requestJson != null){
                String gameKey =  requestJson.get("gameId");
                Game game = Application.games.get(gameKey);
                if (game == null) {
                    return new Response(Status.ERROR, "The game does not exist.", "");
                }
                if (game.startGame()) {
                    Map<String, String> payload = new HashMap<>();
                    payload.put("board", game.getBoard().getBoardJson());
                    payload.put("ports", game.getBoard().getPortsJson());
                    String responseJson = new ObjectMapper().writeValueAsString(payload);
                    return new Response(Status.SUCCESS, "Game has started.", responseJson);
                }
            }
            return new Response(Status.ERROR, "Game can not start without players.","");
        }
        else if (command.equalsIgnoreCase("setMaxPlayers")) {
            if (requestJson != null) {
                String gameKey = requestJson.get("gameId");
                Game game = Application.games.get(gameKey);
                if (game == null) {
                    return new Response(Status.ERROR, "The game does not exist.", "");
                }
                int maxPlayers = Integer.parseInt(requestJson.get("maxPlayers"));
                if (game.getNoPlayers() > maxPlayers) {
                    return new Response(Status.ERROR, "There are already more players.", "");
                }
                game.setMaxPlayers(maxPlayers);
                return new Response(Status.SUCCESS, "The maximum number of players was set successfully.", "");
            }
        }
        else if (command.equalsIgnoreCase("addPlayer")) {
            if (requestJson != null) {
                String gameKey = requestJson.get("gameId");
                Game game = Application.games.get(gameKey);
                if (game == null) {
                    return new Response(Status.ERROR, "The game does not exist.", "");
                }
                if (game.getNoPlayers() == game.getMaxPlayers()) {
                    return new Response(Status.ERROR, "There is no room left.","");
                }
                String playerId = randomString.nextString();
                game.addPlayer(playerId, new Player(playerId, Application.games.get(gameKey)));
                game.addNextPlayer(playerId);
                Map<String, String> payload = new HashMap<>();
                payload.put("playerId", playerId);
                String responseJson = new ObjectMapper().writeValueAsString(payload);
                return new Response(Status.SUCCESS, "The player was added successfully.", responseJson);
            }
        }
        return new Response(Status.ERROR,"The command was not implemented", command);
    }
}
