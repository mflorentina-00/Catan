package catan.game;

import catan.API.Response;
import catan.game.game.Game;
import com.github.ankzz.dynamicfsm.action.FSMAction;
import com.github.ankzz.dynamicfsm.fsm.FSM;
import org.apache.http.HttpStatus;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

//TODO call proper functions for each actions and get response arguments
public class TurnFlow {
    public FSM fsm;
    public final Game game;
    public Response response;


    TurnFlow(Game game) throws IOException, SAXException, ParserConfigurationException {
        this.game = game;
        fsm = new FSM("stateConfig.xml", new FSMAction() {
            @Override
            public boolean action(String curState, String message, String nextState, Object args) {
                response = new Response(HttpStatus.SC_OK, "The message has no assigned function!", "");
                return true;
            }
        });
        fsm.setAction("rollDice", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                Random random = new Random();
                if (random.nextInt(7) + random.nextInt(7) == 7)
                    fsm.ProcessFSM("rollNotASeven");//fsm.ProcessFSM("rollASeven");}
                else
                    fsm.ProcessFSM("rollNotASeven");
                return false;
            }
        });
        fsm.setAction("rollASeven", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response = new Response(HttpStatus.SC_OK, "Rolled a seven!", "");
                return true;
            }
        });
        fsm.setAction("rollNotASeven", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response = new Response(HttpStatus.SC_OK, "Rolled not a seven!", "");
                return true;
            }
        });
        fsm.setAction("giveResources", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response = new Response(HttpStatus.SC_OK, "Give resources!", "");
                return true;
            }
        });
        fsm.setAction("moveRobber", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response = new Response(HttpStatus.SC_OK, "Move robbert!", "");
                return true;
            }
        });
        fsm.setAction("giveSelectedResource", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response = new Response(HttpStatus.SC_OK, "Give selected resource!", "");
                return true;
            }
        });
        fsm.setAction("startTrade", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response = new Response(HttpStatus.SC_OK, "Started trading", "");
                return true;
            }
        });
        fsm.setAction("selectOpponent", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response = new Response(HttpStatus.SC_OK, "Selected opponent", "");
                return true;
            }
        });
        fsm.setAction("endTrade", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response = new Response(HttpStatus.SC_OK, "Ended trading", "");
                return true;
            }
        });
        fsm.setAction("buyRoad", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response = new Response(HttpStatus.SC_OK, "Buy road successfully!", "");
                return true;
            }
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
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response = new Response(HttpStatus.SC_OK, "Buy house successfully!", "");
                if (!game.buySettlement(Integer.parseInt(((HashMap<String, String>) o).get("spot")))) {
                    response = new Response(HttpStatus.SC_FORBIDDEN, "Buying the house is not possible!", "");
                    return false;
                }
                return true;
            }
        });
        fsm.setAction("buyCity", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response = new Response(HttpStatus.SC_OK, "Buy City successfully!", "");
                return true;
            }
        });
        fsm.setAction("playDevCard", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {

                response = new Response(HttpStatus.SC_OK, "Dev Card played successfully!", "");
                return true;
            }
        });
        fsm.setAction("endTurn", new FSMAction() {
            @Override
            public boolean action(String curState, String message, String nextState, Object args) {
                response = new Response(HttpStatus.SC_OK, "Turn changed successfully!", "");
                return game.changeTurn();
            }
        });
    }
}
