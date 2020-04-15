package catan;

import catan.API.Response;
import catan.API.HttpClientPost;
import catan.API.request.ManagerRequest;
import catan.API.request.Status;
import catan.API.request.UserRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class ConnectivitySimulation {
    String username = "silviu";
    String password = "1234";
    String gameID = null;
    List<String> playersID = new ArrayList<>();

    // region Manager Commands

    public String createGame() throws IOException {
        Response response;
        response = HttpClientPost.managerPostTo(new ManagerRequest(username, password, "newGame",""));
        if (response.getCode() == Status.ERROR)
            return null;
        else
            return response.getStatus();
    }

    public String startGame(String gameId) throws IOException {
        Response response;
        response = HttpClientPost.managerPostTo(new ManagerRequest(username, password,
                "startGame/"  + gameId,""));
        if (response.getCode() == Status.ERROR)
            return null;
        else
            return response.getStatus();
    }
    public boolean setNoPlayers(String gameId, Integer no) throws IOException {
        Response response;
        response = HttpClientPost.managerPostTo(new ManagerRequest(username, password,
                "setMaxPlayers/" + gameId + "/" + no,""));
        return response.getCode() != Status.ERROR;
    }

    public String addPlayer(String gameId) throws IOException {
        Response response;
        response = HttpClientPost.managerPostTo(new ManagerRequest(username, password,
                "addPlayer/" + gameId,""));
        if (response.getCode() == Status.ERROR)
            return null;
        else
            return response.getStatus();
    }

    // endregion

    // region User Commands

    public boolean buyRoad(String gameId, String playerId, Integer spot) throws IOException {
        Response response;
        response = HttpClientPost.userPostTo("SHARED_KEY", new UserRequest(gameId, playerId,
                "buyRoad/" + spot,""));
        return response.getCode() != Status.ERROR;
    }
    public boolean endTurn(String gameId, String playerId) throws IOException {
        Response response;
        response = HttpClientPost.userPostTo("SHARED_KEY", new UserRequest(gameId, playerId,
                "endTurn",""));
        return response.getCode() != Status.ERROR;
    }
    public boolean buyHouse(String gameId, String playerId, Integer spot) throws IOException {
        Response response;
        response = HttpClientPost.userPostTo("SHARED_KEY", new UserRequest(gameId, playerId,
                "buyHouse/" + spot,""));
        return response.getCode() != Status.ERROR;
    }
    public boolean buyCity(String gameId, String playerId, Integer spot) throws IOException {
        Response response;
        response = HttpClientPost.userPostTo("SHARED_KEY", new UserRequest(gameId, playerId,
                "buyCity/" + spot,""));
        return response.getCode() != Status.ERROR;
    }
    public boolean playDevCard(String gameId, String playerId, Integer spot) throws IOException {
        Response response;
        response = HttpClientPost.userPostTo("SHARED_KEY", new UserRequest(gameId, playerId,
                "playDevCard/" + spot,""));
        return response.getCode() != Status.ERROR;
    }



    //endregion

    public void simulation() throws IOException, InterruptedException {
        // Configure the game
        gameID = createGame();
        setNoPlayers(gameID, 2);
        playersID.add(addPlayer(gameID));
        playersID.add(addPlayer(gameID));
        playersID.add(addPlayer(gameID));
        setNoPlayers(gameID,1);
        startGame(gameID);
        // Run the game
        while (true) {
            buyCity(gameID, playersID.get(0), 20);
            sleep(100);
            buyRoad(gameID, playersID.get(1), 42);
            playDevCard(gameID,playersID.get(0),20);
            endTurn(gameID, playersID.get(0));
            sleep(100);
            buyHouse(gameID, playersID.get(1), 14);
            sleep(100);
            endTurn(gameID, playersID.get(1));
        }
    }
}
