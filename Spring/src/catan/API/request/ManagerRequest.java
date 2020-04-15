package catan.API.request;

import catan.API.Response;
import catan.Application;
import catan.game.Game;
import catan.game.Player;

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

    public Response run() {
        String[] tokens = command.split("[/]+");
        if (tokens[0].equalsIgnoreCase("newGame")) {
            String gameKey = randString.nextString();
            Application.games.put(gameKey, new Game());
            return new Response(Status.SUCCESS, gameKey);
        }
        if (tokens[0].equalsIgnoreCase("startGame")) {
            String gameKey = tokens[1];
            if (Application.games.get(gameKey) == null)
                return new Response(Status.ERROR, "The game does not exist.");
            if (Application.games.get(gameKey).startGame())
                return new Response(Status.SUCCESS,"Game started");
            return new Response(Status.ERROR,"Game couldn't start.");
        }
        else if(tokens[0].equalsIgnoreCase("setMaxPlayers")) {
            String gameKey = tokens[1];
            int playersNum = Integer.parseInt(tokens[2]);
            if (Application.games.get(gameKey) == null)
                return new Response(Status.ERROR, "The game does not exist.");
            if (Application.games.get(gameKey).getPlayers().size()>playersNum)
                return new Response(Status.ERROR, "There are already to many players.");
            Application.games.get(gameKey).setMaxPlayers(playersNum);
        }
        else if (tokens[0].equalsIgnoreCase("addPlayer")) {
            String gameKey = tokens[1];
            String userId = randString.nextString();
            if (Application.games.get(gameKey) == null)
                return new Response(Status.ERROR, "The game does not exist.");
            if (Application.games.get(gameKey).getPlayers().size() == Application.games.get(gameKey).getMaxPlayers())
                return new Response(Status.ERROR, "There is no room left.");
            Application.games.get(gameKey).getPlayers().put(userId, new Player(userId,Application.games.get(gameKey)));
            Application.games.get(gameKey).addNextPlayer(userId);
            return new Response(Status.SUCCESS, userId);
        }
        return new Response(Status.SUCCESS, command);
    }
}
