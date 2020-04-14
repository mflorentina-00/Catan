package catan;

import catan.API.Response;
import catan.API.HttpClientPost;
import catan.API.request.ManagerRequest;
import catan.API.request.UserRequest;

import java.io.IOException;
import java.util.Vector;

import static java.lang.Thread.sleep;

public class ConnectivitySimulation {
    String username = "silviu";
    String password = "1234";
    Vector<String> playersId = new Vector<>();
    String gameId = null;

    // region Manager Commands

    public String createGame() throws IOException {
        Response response;
        response = HttpClientPost.managerPostTo(new ManagerRequest(username, password, "newGame"));
        if (response.getCode() == 102)
            return null;
        else
            return response.getStatus();
    }

    public String startGame(String gameId) throws IOException {
        Response response;
        response = HttpClientPost.managerPostTo(new ManagerRequest(username, password, "startGame/"+gameId));
        if (response.getCode() == 102)
            return null;
        else
            return response.getStatus();
    }
    public boolean setNoPlayers(String gameId, Integer no) throws IOException {
        Response response;
        response = HttpClientPost.managerPostTo(new ManagerRequest(username, password,
                "setMaxPlayers/" + gameId + "/" + no));
        return response.getCode() != 102;
    }

    public String addPlayer(String gameId) throws IOException {
        Response response;
        response = HttpClientPost.managerPostTo(new ManagerRequest(username, password,
                "addPlayer/" + gameId));
        if (response.getCode() == 102)
            return null;
        else
            return response.getStatus();
    }

    // endregion

    // region User Commands

    public boolean buyRoad(String gameId, String playerId, Integer spot) throws IOException {
        Response response;
        response = HttpClientPost.userPostTo("SHARED_KEY", new UserRequest(gameId, playerId,
                "buyRoad/" + spot));
        return response.getCode() != 102;
    }
    public boolean endTurn(String gameId, String playerId) throws IOException {
        Response response;
        response = HttpClientPost.userPostTo("SHARED_KEY", new UserRequest(gameId, playerId,
                "endTurn"));
        return response.getCode() != 102;
    }
    public boolean buyHouse(String gameId, String playerId, Integer spot) throws IOException {
        Response response;
        response = HttpClientPost.userPostTo("SHARED_KEY", new UserRequest(gameId, playerId,
                "buyHouse/" + spot));
        return response.getCode() != 102;
    }
    public boolean buyCity(String gameId, String playerId, Integer spot) throws IOException {
        Response response;
        response = HttpClientPost.userPostTo("SHARED_KEY", new UserRequest(gameId, playerId,
                "buyCity/" + spot));
        return response.getCode() != 102;
    }

    //endregion

    public void simulation() throws IOException, InterruptedException {
        // Configure the game
        gameId = createGame();
        setNoPlayers(gameId, 2);
        playersId.add(addPlayer(gameId));
        playersId.add(addPlayer(gameId));
        playersId.add(addPlayer(gameId));
        setNoPlayers(gameId,1);
        startGame(gameId);
        // Run the game
        while (true) {
            buyCity(gameId, playersId.elementAt(0), 20);
            sleep(100);
            buyRoad(gameId, playersId.elementAt(1), 42);
            endTurn(gameId, playersId.elementAt(0));
            sleep(100);
            buyHouse(gameId, playersId.elementAt(1), 14);
            sleep(100);
            endTurn(gameId, playersId.elementAt(1));
        }
    }
}
