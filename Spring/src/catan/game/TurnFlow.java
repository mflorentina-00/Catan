package catan.game;

import catan.API.Response;
import com.github.ankzz.dynamicfsm.action.FSMAction;
import com.github.ankzz.dynamicfsm.fsm.FSM;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class TurnFlow {
    public FSM fsm;
    public final Game game;
    public Response response;


    TurnFlow(Game game) throws IOException, SAXException, ParserConfigurationException {
        this.game = game;
        fsm = new FSM("stateConfig.xml", new FSMAction() {
            @Override
            public boolean action(String curState, String message, String nextState, Object args) {
                response=new Response(100,"Turn changed successfully!");
                return true;
            }
        });
        fsm.setAction("rollASeven", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response=new Response(100,"rolled a seven!");
                return true;
            }
        });
        fsm.setAction("rollNotASeven", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response=new Response(100,"ROBBERT HELL!!");
                return true;
            }
        });
        fsm.setAction("giveResources", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response=new Response(100,"Give resources!");
                return true;
            }
        });
        fsm.setAction("moveRobber", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response=new Response(100,"Move robberT!");
                return true;
            }
        });
        fsm.setAction("giveSelectedResource", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response=new Response(100,"Give selected resource!");
                return true;
            }
        });
        fsm.setAction("tradeBetweenPlayers", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response=new Response(100,"Trade between player");
                return true;
            }
        });
        fsm.setAction("buyRoad", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response=new Response(100,"Buy road successfully!");
                return true;
            }
        });
        fsm.setAction("buyHouse", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response=new Response(100,"Buy house successfully!");
                return true;
            }
        });
        fsm.setAction("buyCity", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response=new Response(100,"Buy City successfully!");
                return true;
            }
        });
        fsm.setAction("playDevCard", new FSMAction() {
            @Override
            public boolean action(String s, String s1, String s2, Object o) {
                response=new Response(100,"Dev Card played successfully!");
                return true;
            }
        });



        fsm.setAction("endTurn", new FSMAction() {
            @Override
            public boolean action(String curState, String message, String nextState, Object args) {
                response=new Response(100,"Turn changed successfully!");
                return game.changeTurn();
            }
        });
        System.out.println(fsm.getCurrentState());
        fsm.ProcessFSM("rollNotASeven");
        System.out.println(fsm.getCurrentState());
        fsm.ProcessFSM("tradeBetweenPlayers");
        System.out.println(fsm.getCurrentState());
    }
}
