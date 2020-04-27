package catan.API.request;

import catan.API.Response;
import catan.Application;
import catan.game.Player;
import catan.game.gameType.BaseGame;
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


    public Response run() throws JsonProcessingException {
        HashMap<String,String> args=null;
        if(!arguments.equals(""))
            args=GameRequest.getMapFromData(arguments);

        if (command.equalsIgnoreCase("newGame")) {
            String gameKey = randString.nextString();
            if (arguments.equalsIgnoreCase("{\"scenario\": \"SettlersOfCatan\"}")) {
                Application.games.put(gameKey, new BaseGame());
                Map<String,String> payload = new HashMap<>();
                payload.put("gameId",gameKey);
                String jsonArgs = new ObjectMapper().writeValueAsString(payload);
                return new Response(Status.SUCCESS, "Game created successfully",jsonArgs);
            }
            else {
                return new Response(Status.ERROR, "The scenario is not implemented.","");
            }
        }
        if (command.equalsIgnoreCase("startGame")) {
            String gameKey =  args.get("gameId");
            if (Application.games.get(gameKey) == null)
            {
                return new Response(Status.ERROR, "The game does not exist.","");
            }
            if (Application.games.get(gameKey).startGame()) {
                Map <String, String> arguments = new HashMap<>();
                arguments.put("board", Application.games.get(gameKey).getBoard().getBoardJSON());
                arguments.put("ports", Application.games.get(gameKey).getBoard().getPortsJSON());
                String json = new ObjectMapper().writeValueAsString(arguments);
                return new Response(Status.SUCCESS, "Game started.", json);
            }
            return new Response(Status.ERROR,"Game couldn't start.","");
        }
        else if (command.equalsIgnoreCase("setMaxPlayers")) {
            String gameKey = args.get("gameId");
            int playersNum = Integer.parseInt(args.get("maxPlayers"));
            if (Application.games.get(gameKey) == null)
                return new Response(Status.ERROR, "The game does not exist.","");
            if (Application.games.get(gameKey).getPlayers().size()>playersNum)
                return new Response(Status.ERROR, "There are already to many players.","");
            Application.games.get(gameKey).setMaxPlayers(playersNum);
            return new Response(Status.SUCCESS, "Size fixed successfully.","");
        }
        else if (command.equalsIgnoreCase("addPlayer")) {
            String gameKey = args.get("gameId");
            String userId = randString.nextString();
            if (Application.games.get(gameKey) == null)
                return new Response(Status.ERROR, "The game does not exist.","");
            if (Application.games.get(gameKey).getPlayers().size() == Application.games.get(gameKey).getMaxPlayers())
                return new Response(Status.ERROR, "There is no room left.","");
            Application.games.get(gameKey).getPlayers().put(userId, new Player(userId,Application.games.get(gameKey)));
            Application.games.get(gameKey).addNextPlayer(userId);
            Map<String,String> payload = new HashMap<>();
            payload.put("playerId",userId);
            String jsonArgs = new ObjectMapper().writeValueAsString(payload);
            return new Response(Status.SUCCESS, "Successfully added player",jsonArgs);
        }
        return new Response(Status.SUCCESS,"Guess it's ok?", command);
    }
}
