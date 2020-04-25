package catan;

import catan.API.Response;
import catan.API.HttpClientPost;
import catan.API.request.GameRequest;
import catan.API.request.ManagerRequest;
import catan.API.request.Status;
import catan.API.request.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        else {
            HashMap<String,String> args=GameRequest.getMapFromData(response.getData());
            return args.get("gameId");
        }
    }

    public String startGame(String gameId) throws IOException {
        Map<String,String> payload = new HashMap<>();
        payload.put("gameId",gameId);
        String jsonArgs = new ObjectMapper().writeValueAsString(payload);
        Response response;
        response = HttpClientPost.managerPostTo(new ManagerRequest(username, password,
                "startGame",jsonArgs));
        if (response.getCode() == Status.ERROR)
            return null;
        else
            return response.getStatus();
    }
    public boolean setNoPlayers(String gameId, Integer no) throws IOException {
        Map<String,String> payload = new HashMap<>();
        payload.put("gameId",gameId);
        payload.put("nrPlayers",no.toString());
        String jsonArgs = new ObjectMapper().writeValueAsString(payload);
        Response response;
        response = HttpClientPost.managerPostTo(new ManagerRequest(username, password,
                "setMaxPlayers",jsonArgs));
        return response.getCode() != Status.ERROR;
    }

    public String addPlayer(String gameId) throws IOException {
        Map<String,String> payload = new HashMap<>();
        payload.put("gameId",gameId);
        String jsonArgs = new ObjectMapper().writeValueAsString(payload);
        Response response;
        response = HttpClientPost.managerPostTo(new ManagerRequest(username, password,
                "addPlayer",jsonArgs));
        if (response.getCode() == Status.ERROR)
            return null;
        else {
            HashMap<String,String> args=GameRequest.getMapFromData(response.getData());
            return args.get("playerId");
        }
    }

    // endregion

    // region User Commands

    public boolean rollDice(String gameID,String playerId) throws  IOException{
        Response response;
        response=HttpClientPost.userPostTo(new UserRequest(gameID,playerId,"rollDice",""));
        return response.getCode()!=Status.ERROR;
    }
    public boolean buyRoad(String gameId, String playerId, Integer spot) throws IOException {
        Map<String,String> payload = new HashMap<>();
        payload.put("spot",spot.toString());
        String jsonArgs = new ObjectMapper().writeValueAsString(payload);
        Response response;
        response = HttpClientPost.userPostTo( new UserRequest(gameId, playerId,
                "buyRoad/" + spot,jsonArgs));
        return response.getCode() != Status.ERROR;
    }
    public boolean endTurn(String gameId, String playerId) throws IOException {
        Response response;
        response = HttpClientPost.userPostTo(new UserRequest(gameId, playerId,
                "endTurn",""));
        return response.getCode() != Status.ERROR;
    }
    public boolean buyHouse(String gameId, String playerId, Integer spot) throws IOException {
        Map<String,String> payload = new HashMap<>();
        payload.put("spot",spot.toString());
        String jsonArgs = new ObjectMapper().writeValueAsString(payload);
        Response response;
        response = HttpClientPost.userPostTo( new UserRequest(gameId, playerId,
                "buyHouse",jsonArgs));
        return response.getCode() != Status.ERROR;
    }
    public boolean buyCity(String gameId, String playerId, Integer spot) throws IOException {
        Map<String,String> payload = new HashMap<>();
        payload.put("spot",spot.toString());
        String jsonArgs = new ObjectMapper().writeValueAsString(payload);
        Response response;
        response = HttpClientPost.userPostTo(new UserRequest(gameId, playerId,
                "buyCity",jsonArgs));
        return response.getCode() != Status.ERROR;
    }
    public boolean playDevCard(String gameId, String playerId, Integer spot) throws IOException {
        Response response;
        response = HttpClientPost.userPostTo(new UserRequest(gameId, playerId,
                "playDevCard",""));
        return response.getCode() != Status.ERROR;
    }
    public boolean tradeBetweenPlayers(String gameId, String playerId) throws IOException {
        Response response;
        response = HttpClientPost.userPostTo( new UserRequest(gameId, playerId,
                "tradeBetweenPlayers",""));
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
            rollDice(gameID,playersID.get(0));
            tradeBetweenPlayers(gameID,playersID.get(0));
            buyHouse(gameID, playersID.get(0), 20);
            rollDice(gameID,playersID.get(0));
            sleep(100);
            buyRoad(gameID, playersID.get(1), 42);
            playDevCard(gameID,playersID.get(0),20);
            endTurn(gameID, playersID.get(0));
            sleep(100);
            rollDice(gameID,playersID.get(1));
            tradeBetweenPlayers(gameID,playersID.get(1));
            buyHouse(gameID, playersID.get(1), 22);
            sleep(100);
            endTurn(gameID, playersID.get(1));
        }
    }
}
