package catan.API.request;

import catan.API.response.Response;
import catan.Application;
import catan.game.player.Player;
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
    private Map<String, Object> arguments;

    public ManagerRequest(String username, String password, String command,  Map<String, Object> arguments) {
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

    public Map<String, Object> getArguments() {
        return arguments;
    }

    public void setArguments( Map<String, Object> arguments) { this.arguments = arguments; }

    public Response run() throws JsonProcessingException {
        if (command.equalsIgnoreCase("newGame")) {
            if (arguments == null || arguments.get("scenario") == null) {
                return new Response(HttpStatus.SC_ACCEPTED, "No scenario is specified.", null);
            }
            if (arguments.get("scenario").equals("SettlersOfCatan")) {
                String gameId;
                do {
                    gameId = randomString.nextString();
                } while (Application.games.containsKey(gameId) || Application.players.contains(gameId));
                Application.games.put(gameId, new BaseGame());
                Map<String, Object> payload = new HashMap<>();
                payload.put("gameId", gameId);
                String responseJson = new ObjectMapper().writeValueAsString(payload);
                return new Response(HttpStatus.SC_OK, "Game created successfully.", payload);
            }
            return new Response(HttpStatus.SC_ACCEPTED, "The scenario is not implemented.", null);
        }
        else if (command.equalsIgnoreCase("startGame")) {
            if (arguments == null) {
                return new Response(HttpStatus.SC_ACCEPTED, "Game can not start without players.",null);
            }
            String gameId = (String) arguments.get("gameId");
            Game game = Application.games.get(gameId);
            if (game == null) {
                return new Response(HttpStatus.SC_ACCEPTED, "The game does not exist.", null);
            }
            if (game.startGame()) {
                Map<String, Object> payload = new HashMap<>();
                payload.put("board", game.getBoard().getBoardArguments());
                payload.put("ports", game.getBoard().getPorts());
                String responseJson = new ObjectMapper().writeValueAsString(payload);
                return new Response(HttpStatus.SC_OK, "Game has started.", payload);
            }
            return new Response(HttpStatus.SC_ACCEPTED, "Game can not start without players.",null);
        }
        else if (command.equalsIgnoreCase("setMaxPlayers")) {
            if (arguments == null) {
                return new Response(HttpStatus.SC_ACCEPTED, "The maximum number of players is not specified.", null);
            }
            String gameId = (String)arguments.get("gameId");
            Game game = Application.games.get(gameId);
            if (game == null) {
                return new Response(HttpStatus.SC_ACCEPTED, "The game does not exist.", null);
            }
            int maxPlayers = (Integer)arguments.get("maxPlayers");
            if (game.getNoPlayers() > maxPlayers) {
                return new Response(HttpStatus.SC_ACCEPTED, "There are already more players.", null);
            }
            game.setMaxPlayers(maxPlayers);
            return new Response(HttpStatus.SC_OK, "The maximum number of players was set successfully.", null);
        }
        else if (command.equalsIgnoreCase("addPlayer")) {
            if (arguments == null) {
                return new Response(HttpStatus.SC_ACCEPTED, "The game identifier is not specified.", null);
            }
            String gameId = (String)arguments.get("gameId");
            Game game = Application.games.get(gameId);
            if (game == null) {
                return new Response(HttpStatus.SC_ACCEPTED, "The game does not exist.", null);
            }
            if (game.getNoPlayers() == game.getMaxPlayers()) {
                return new Response(HttpStatus.SC_ACCEPTED, "There is no room left.",null);
            }
            String playerId;
            do {
                playerId = randomString.nextString();
            } while (Application.games.containsKey(playerId) || Application.players.contains(playerId));
            game.addPlayer(playerId, new Player(playerId, Application.games.get(gameId)));
            game.addNextPlayer(playerId);
            Map<String, Object> payload = new HashMap<>();
            payload.put("playerId", playerId);
            String responseJson = new ObjectMapper().writeValueAsString(payload);
            return new Response(HttpStatus.SC_OK, "The player was added successfully.", payload);
        }
        return new Response(HttpStatus.SC_ACCEPTED,"The command was not implemented.", null);
    }
}
