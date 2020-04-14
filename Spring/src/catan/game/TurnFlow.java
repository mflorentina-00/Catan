package catan.game;

import com.github.ankzz.dynamicfsm.action.FSMAction;
import com.github.ankzz.dynamicfsm.fsm.FSM;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class TurnFlow {
    public FSM fsm;
    public final Game game;

    TurnFlow(Game game) throws IOException, SAXException, ParserConfigurationException {
        this.game = game;
        fsm = new FSM("stateConfig.xml", new FSMAction() {
            @Override
            public boolean action(String curState, String message, String nextState, Object args) {
                return true;
            }
        });
        fsm.setAction("endTurn", new FSMAction() {
            @Override
            public boolean action(String curState, String message, String nextState, Object args) {
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
