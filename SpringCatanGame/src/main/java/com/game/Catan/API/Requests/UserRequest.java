package com.game.Catan.API.Requests;

import com.game.Catan.API.BaseResponse;
import com.game.Catan.Game.GameClass;

public class UserRequest implements GameRequest {
    private String gameId;
    private String userUniqueID;
    private String command;

    public BaseResponse run(){
        GameClass game=obj.games.get(gameId);
        if(game==null)
            return new BaseResponse(102,"The game does not exist");
        if(game.getPlayers().get(userUniqueID)==null)
            return new BaseResponse(102,"The player does not exist");
        String[] tokens=command.split("[/]+");
        if(tokens[0].equalsIgnoreCase("buyRoad")){
            //TODO implement the logic
            return new BaseResponse(100,"Buying road");
        }
        if(tokens[0].equalsIgnoreCase("buyHouse")){
            //TODO implement the logic
            return new BaseResponse(100,"Buying house");
        }
        if(tokens[0].equalsIgnoreCase("buyCity")){
            //TODO implement the logic
            return new BaseResponse(100,"Buying city");
        }
        return new BaseResponse(100,"Command Unknown");
    }

    public UserRequest() {
    }

    public UserRequest(String gameId, String userUniqueID, String command) {
        this.gameId = gameId;
        this.userUniqueID = userUniqueID;
        this.command = command;
    }

    public String getGameId() {
        return gameId;
    }

    public String getCommand() {
        return command;
    }

    public String getUserUniqueID() {
        return userUniqueID;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setUserUniqueID(String userUniqueID) {
        this.userUniqueID = userUniqueID;
    }
    public void setGameId(String gameId) { this.gameId = gameId; }
}
