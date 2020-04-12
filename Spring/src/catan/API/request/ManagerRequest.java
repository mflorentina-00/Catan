package catan.API.request;

import catan.API.Response;
import catan.Application;
import catan.game.Game;
import catan.game.Player;

public class ManagerRequest implements GameRequest {
    private String managerId;
    private String managerPass;
    private String command;

    public ManagerRequest(String managerId, String managerPass, String command) {
        this.managerId = managerId;
        this.managerPass = managerPass;
        this.command = command;
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
            Application.games.put(gameKey,new Game());
            return new Response(100,gameKey);
        }
        else if (tokens[0].equalsIgnoreCase("addPlayer")) {
            String gameKey = tokens[1];
            String userId = randString.nextString();
            if (Application.games.get(gameKey) == null)
                return new Response(102, "The game does not exist.");
            if (Application.games.get(gameKey).getPlayers().size() == Application.games.get(gameKey).getMaxPlayers())
                return new Response(102, "There is no room left.");
            Application.games.get(gameKey).getPlayers().put(userId, new Player());
            return new Response(100, userId);
        }
        else if(tokens[0].equalsIgnoreCase("setMaxPlayers")) {
            String gameKey = tokens[1];
            int playersNum = Integer.parseInt(tokens[2]);
            if (Application.games.get(gameKey) == null)
                return new Response(102, "The game does not exist.");
            if (Application.games.get(gameKey).getPlayers().size()>playersNum)
                return new Response(102,"There are already to many players.");
            Application.games.get(gameKey).setMaxPlayers(playersNum);
        }
        return new Response(100,command);
    }
}
