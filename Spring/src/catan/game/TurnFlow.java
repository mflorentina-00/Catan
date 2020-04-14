package catan.game;

import com.github.ankzz.dynamicfsm.action.FSMAction;
import com.github.ankzz.dynamicfsm.fsm.FSM;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

//TODO Clasă pentru ordinea acțiunilor (de preferat cea din pdf-ul cu reguli)
public class TurnFlow {
    public FSM f;
    public final Game game;

    TurnFlow(Game game) throws IOException, SAXException, ParserConfigurationException {
    f = new FSM("stateConfig.xml", new FSMAction() {
        @Override
        public boolean action(String curState, String message, String nextState, Object args) {
            return true;
        }
    });
    this.game=game;
    f.setAction("endTurn", new FSMAction() {
        @Override
        public boolean action(String curState, String message, String nextState, Object args) {
            return game.changeTurn();
        }
    });
    System.out.println(f.getCurrentState());
    f.ProcessFSM("rollNotASeven");
    System.out.println(f.getCurrentState());
    f.ProcessFSM("tradeBetweenPlayers");
    System.out.println(f.getCurrentState());
    }
}
