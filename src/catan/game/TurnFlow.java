package catan.game;

import catan.API.Response;
import catan.game.game.Game;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankzz.dynamicfsm.action.FSMAction;
import com.github.ankzz.dynamicfsm.fsm.FSM;
import org.apache.http.HttpStatus;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//TODO: The responses must be set in each game function.
public class TurnFlow {
    public final Game game;
    public FSM state;
    public Response response;

    TurnFlow(Game game) throws IOException, SAXException, ParserConfigurationException {
        this.game = game;
        state = new FSM("turnFlow.xml", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                response = new Response(HttpStatus.SC_NOT_FOUND, "The message has no assigned function.", "");
                return true;
            }
        });
        state.setAction("rollDice", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                game.rollDice();
                return false;
            }
        });
        state.setAction("rollSeven", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                try {
                    String data = new ObjectMapper().writeValueAsString(arguments);
                    response = new Response(HttpStatus.SC_OK, "Rolled a seven.", data);
                    return true;
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        });
        state.setAction("rollNotSeven", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                try {
                    String data = new ObjectMapper().writeValueAsString(arguments);
                    response = new Response(HttpStatus.SC_OK, "Rolled not a seven.", data);
                    return true;
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        });
        state.setAction("moveRobber", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                Map<String, Integer> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, Integer>>(){});
                int tile = requestArguments.get("tile");
                //TODO: Add game.moveRobber(int tile).
                response = new Response(HttpStatus.SC_OK, "Moved robber successfully.", "");
                return true;
            }
        });
        state.setAction("stealResource", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                Map<String, String> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, String>>(){});
                String playerId = requestArguments.get("playerId");
                //TODO: Add game.stealResource(String playerId).
                response = new Response(HttpStatus.SC_OK, "Stole resource successfully.", "");
                return true;
            }
        });
        state.setAction("playerTrade", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                //TODO: Create offerRequest map.
                //TODO: Call game.playerTrade(Map<String, Integer> offerRequest).
                response = new Response(HttpStatus.SC_OK, "Started trading successfully.", "");
                return true;
            }
        });
        state.setAction("sendOpponents", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                //TODO: Add game.sendOpponents().
                response = new Response(HttpStatus.SC_OK, "Sent possible trade partners successfully.", "");
                return true;
            }
        });
        state.setAction("selectOpponent", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                Map<String, String> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, String>>(){});
                if (requestArguments != null) {
                    String playerId = requestArguments.get("playerId");
                }
                //TODO: Add game.selectOpponent(String playerId).
                response = new Response(HttpStatus.SC_OK, "Selected trade partner successfully.", "");
                return true;
            }
        });
        state.setAction("bankTrade", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                Map<String, String> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, String>>(){});
                String offer = requestArguments.get("offer");
                String request = requestArguments.get("request");
                //TODO: Add game.bankTrade(String offer, String request).
                response = new Response(HttpStatus.SC_OK, "Made trade with bank successfully.", "");
                return true;
            }
        });
        state.setAction("portTrade", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                Map<String, String> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, String>>(){});
                int port = Integer.parseInt(requestArguments.get("port"));
                String offer = requestArguments.get("offer");
                String request = requestArguments.get("request");
                //TODO: Add game.portTrade(int port, String offer, String request).
                response = new Response(HttpStatus.SC_OK, "Made trade with a port successfully.", "");
                return true;
            }
        });
        state.setAction("buyRoad", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                Map<String, Integer> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, Integer>>(){});
                int start = requestArguments.get("start");
                int end = requestArguments.get("end");
                if (game.buyRoad(start, end)) {
                    response = new Response(HttpStatus.SC_OK, "Bought road successfully.", "");
                    return true;
                }
                response = new Response(HttpStatus.SC_FORBIDDEN, "Cannot buy settlement.", "");
                return false;            }
        });
        fsm.setAction("placeHouse", new FSMAction() {
            @Override
            public boolean action(String curState, String message, String nextState, Object args) {
                response = new Response(HttpStatus.SC_OK, "House placed successfully!", "");
                if (!game.placeInitSettlement(Integer.parseInt(((HashMap<String, String>) args).get("spot")))) {
                    response = new Response(HttpStatus.SC_FORBIDDEN, "Placing the house is not possible!", "");
                    return false;
                }
                 return true;
            }
        });
        fsm.setAction("placeRoad", new FSMAction() {
            @Override
            public boolean action(String curState, String message, String nextState, Object args) {
                response = new Response(HttpStatus.SC_OK, "Road placed successfully!", "");
                if (!game.placeInitRoad(Integer.parseInt(((HashMap<String, String>) args).get("start")),
                        Integer.parseInt(((HashMap<String, String>) args).get("end")))) {
                    response = new Response(HttpStatus.SC_FORBIDDEN, "Placing the road is not possible!", "");
                    return false;
                }
                return game.changeTurn();
            }
        });
        fsm.setAction("buyHouse", new FSMAction() {
        state.setAction("buySettlement", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                Map<String, Integer> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, Integer>>(){});
                int intersection = requestArguments.get("intersection");
                if (game.buySettlement(intersection)) {
                    response = new Response(HttpStatus.SC_OK, "Bought settlement successfully.", "");
                    return true;
                }
                response = new Response(HttpStatus.SC_FORBIDDEN, "Cannot buy settlement.", "");
                return false;
            }
        });
        state.setAction("buyCity", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                Map<String, Integer> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, Integer>>(){});
                int intersection = requestArguments.get("intersection");
                if (game.buyCity(intersection)) {
                    response = new Response(HttpStatus.SC_OK, "Bought city successfully.", "");
                    return true;
                }
                response = new Response(HttpStatus.SC_FORBIDDEN, "Cannot buy city.", "");
                return false;
            }
        });
        state.setAction("buyDevelopment", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                Map<String, String> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, String>>(){});
                String development = requestArguments.get("development");
                //TODO: Add game.buyDevelopment(String development).
                response = new Response(HttpStatus.SC_OK, "Bought development successfully.", "");
                return true;
            }
        });
        state.setAction("useDevelopment", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                Map<String, String> requestArguments = new ObjectMapper().convertValue(arguments,
                        new TypeReference<HashMap<String, String>>(){});
                String development = requestArguments.get("development");
                String resource = requestArguments.get("resource");
                //TODO: Add game.useDevelopment(String development, String resource).
                state.ProcessFSM("useRoadBuilding");
                response = new Response(HttpStatus.SC_OK, "Used development successfully.", "");
                return true;
            }
        });
        state.setAction("endTurn", new FSMAction() {
            @Override
            public boolean action(String currentState, String message, String nextState, Object arguments) {
                game.changeTurn();
                response = new Response(HttpStatus.SC_OK, "Turn changed successfully.", "");
                return true;
            }
        });
    }
}
