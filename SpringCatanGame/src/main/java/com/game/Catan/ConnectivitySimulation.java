package com.game.Catan;

import com.game.Catan.API.BaseResponse;
import com.game.Catan.API.HttpClientPost;
import com.game.Catan.API.Requests.ManagerRequest;
import com.game.Catan.API.Requests.UserRequest;
import com.game.Catan.Game.Player;

import java.io.IOException;
import java.util.Vector;

import static java.lang.Thread.sleep;

public class ConnectivitySimulation {
    String username="silviu";
    String password="1234";
    Vector<String> playersId=new Vector<>();
    String gameId=null;

    //region Manager Commands

    public String createGame() throws IOException {
        BaseResponse response;
        response = HttpClientPost.managerPostTo(new ManagerRequest(username,password,"newGame"));
        if(response.getCode()==102)
            return null;
        else
            return response.getStatus();
    }
    public boolean setNrPlayers(String gameId,Integer nr) throws IOException {
        BaseResponse response;
        response=HttpClientPost.managerPostTo(new ManagerRequest(username,password,"setMaxPlayers/"+gameId+"/"+Integer.toString(nr)));
        if(response.getCode()==102)
            return false;
        return true;
    }
    public String addPlayer(String gameId) throws IOException {
        BaseResponse response;
        response = HttpClientPost.managerPostTo(new ManagerRequest(username,password,"addPlayer/"+gameId));
        if(response.getCode()==102)
            return null;
        else
            return response.getStatus();
    }

    //endregion

    //region User Commands

    public boolean buyRoad(String gameId,String playerId,Integer spot) throws IOException {
        BaseResponse response;
        response=HttpClientPost.userPostTo("SHARED_KEY",new UserRequest(gameId,playerId, "buyRoad/"+spot));
        if(response.getCode()==102)
            return false;
        return true;
    }
    public boolean buyHouse(String gameId,String playerId,Integer spot) throws IOException {
        BaseResponse response;
        response=HttpClientPost.userPostTo("SHARED_KEY",new UserRequest(gameId,playerId, "buyHouse/"+spot));
        if(response.getCode()==102)
            return false;
        return true;
    }
    public boolean buyCity(String gameId,String playerId,Integer spot) throws IOException {
        BaseResponse response;
        response=HttpClientPost.userPostTo("SHARED_KEY",new UserRequest(gameId,playerId, "buyCity/"+spot));
        if(response.getCode()==102)
            return false;
        return true;
    }


    //endregion

    public void simulation() throws IOException, InterruptedException {
        //Configure the game
        BaseResponse response;
        gameId=createGame();
        setNrPlayers(gameId,2);
        playersId.add(addPlayer(gameId));
        playersId.add(addPlayer(gameId));
        playersId.add(addPlayer(gameId));
        setNrPlayers(gameId,1);
        //Run the game
        while(true) {
            buyCity(gameId,playersId.elementAt(0),20);
            sleep(1000);
            buyRoad(gameId,playersId.elementAt(1),42);
            sleep(1000);
            buyHouse(gameId,playersId.elementAt(2),14);
            sleep(1000);
        }
    }
}
