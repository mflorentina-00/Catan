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
    private String managerId;
    private String managerPass;
    private String command;
    private String jsonArgs;

    public ManagerRequest(String managerId, String managerPass, String command, String jsonArgs) {
        this.managerId = managerId;
        this.managerPass = managerPass;
        this.command = command;
        this.jsonArgs = jsonArgs;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getManagerPass() {
        return managerPass;
    }

    public void setManagerPass(String managerPass) {
        this.managerPass = managerPass;
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


    public Response run() throws JsonProcessingException {
        HashMap<String,String> args=null;
        if(!jsonArgs.equals(""))
            args=GameRequest.getMapFromData(jsonArgs);

        if (command.equalsIgnoreCase("newGame")) {
            String gameKey = randString.nextString();
            Application.games.put(gameKey, new BaseGame());
            Map<String,String> payload = new HashMap<>();
            payload.put("gameId",gameKey);
            String jsonArgs = new ObjectMapper().writeValueAsString(payload);
            return new Response(Status.SUCCESS, "Game created successfully",jsonArgs);
        }
        if (command.equalsIgnoreCase("startGame")) {
            String gameKey =  args.get("gameId");
            if (Application.games.get(gameKey) == null)
                return new Response(Status.ERROR, "The game does not exist.","");
            if (Application.games.get(gameKey).startGame())
                return new Response(Status.SUCCESS,"Game started","");
            return new Response(Status.ERROR,"Game couldn't start.","");
        }
        else if (command.equalsIgnoreCase("setMaxPlayers")) {
            String gameKey = args.get("gameId");
            int playersNum = Integer.parseInt(args.get("nrPlayers"));
            if (Application.games.get(gameKey) == null)
                return new Response(Status.ERROR, "The game does not exist.","");
            if (Application.games.get(gameKey).getPlayers().size()>playersNum)
                return new Response(Status.ERROR, "There are already to many players.","");
            Application.games.get(gameKey).setMaxPlayers(playersNum);
            return  new Response(Status.ERROR, "Size fixed successfully.","");
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
