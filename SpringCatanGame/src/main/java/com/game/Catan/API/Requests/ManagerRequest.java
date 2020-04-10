package com.game.Catan.API.Requests;

import com.game.Catan.API.BaseResponse;
import com.game.Catan.API.RandomString;
import com.game.Catan.Game.GameClass;
import com.game.Catan.Game.Player;

public class ManagerRequest implements GameRequest {
    private String managerId;
    private String managerPass;
    private String command;

    public BaseResponse run(){
        String[] tokens=command.split("[/]+");
        if(tokens[0].equalsIgnoreCase("newGame")){
            String gameKey = randString.nextString();
            obj.games.put(gameKey,new GameClass());
            return new BaseResponse(100,gameKey);
        }
        else if(tokens[0].equalsIgnoreCase("addPlayer"))
        {
            String gameKey = tokens[1];
            String userId = randString.nextString();
            if(obj.games.get(gameKey)==null)
                return new BaseResponse(102,"The game does not exist");
            if(obj.games.get(gameKey).getPlayers().size()==obj.games.get(gameKey).getMaxPlayers())
                return new BaseResponse(102,"There is no room left");
            obj.games.get(gameKey).getPlayers().put(userId,new Player());
            return new BaseResponse(100,userId);
        }
        else if(tokens[0].equalsIgnoreCase("setMaxPlayers"))
        {
            String gameKey = tokens[1];
            Integer playersNum =Integer.valueOf(tokens[2]);
            if(obj.games.get(gameKey)==null)
                return new BaseResponse(102,"The game does not exist");
            if(obj.games.get(gameKey).getPlayers().size()>playersNum)
                return new BaseResponse(102,"There are already to many players");

            obj.games.get(gameKey).setMaxPlayers(playersNum);
        }
        return new BaseResponse(100,command);
    }

    public ManagerRequest() {
    }

    public ManagerRequest(String managerId, String managerPass, String command) {
        this.managerId = managerId;
        this.managerPass = managerPass;
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public String getManagerId() {
        return managerId;
    }

    public String getManagerPass() {
        return managerPass;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public void setManagerPass(String managerPass) {
        this.managerPass = managerPass;
    }
}
