package catan.game;

import catan.game.enumeration.ResourceType;
import catan.game.board.Board;
import javafx.util.Pair;

import java.util.*;


public class Game {
    private Map<String,Player> players;
    private int maxPlayers;
    private Board board;
    private String playerTurn;
    private List<String> playersOrder = new ArrayList<>();



    public Game() {
        players=new HashMap<>();
        board =new Board();
    }
    //region Getters

    public Map<String, Player> getPlayers() {
        return players;
    }


    public int getMaxPlayers() {
        return maxPlayers;
    }
    //endregion
    //region Setters
    public void setPlayers(Map<String, Player> players) {
        this.players = players;
    }
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }
    //endregion
    //region Custom Functions
    public void addPlayerOrder(String userID){
        playersOrder.add(userID);
    }
    public boolean changeTurn(){
        int i=0;
        for (String set:
                playersOrder) {
            if(set.equals(playerTurn))
                break;
            i++;
        }
        if(i==playersOrder.size()-1)
            i=0;
        else
            i=i+1;
        playerTurn=playersOrder.get(i);
        //TODO make restart go to start when necessary;
        players.get(playerTurn).getStateAuto().f.ProcessFSM("Restart");
        return true;
    }

    public boolean playTurn(String playerId,String command){
        if(playerId.equals(playerTurn)){
            players.get(playerId).getStateAuto().f.ProcessFSM(command);
            return true;
        }
        return false;
    }
    public boolean startGame(){
        if(playersOrder.size()==0)
            return false;
        playerTurn=playersOrder.get(0);
        return true;
    }


    public List<Player> playerAcceptingTrade(Player player, List<Pair<ResourceType,Integer>> offer,
                                                    List<Pair<ResourceType,Integer>> request){
        List<Player> playersThatAccept=new ArrayList<>();
        for(String playerId:players.keySet()){
            /*
                TODO W8 players Responses
                    if response is YES and
                    and the below verification then we add the player to the possible pick
             */
            if(players.get(playerId).canMakeTrade(request)){
                playersThatAccept.add(players.get(playerId));
            }
        }
        return playersThatAccept;
    }
    public void playerWhichTrade(Player player, Player player1, List<Pair<ResourceType, Integer>> offer,
                                 List<Pair<ResourceType, Integer>> request) {
        /*
            TODO add player1 notify
         */
        player.tradeHappened(offer, request);
        player1.tradeHappened(request,offer);
    }

    //endregion
    //region Util
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game = (Game) o;
        return Objects.equals(players, game.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(players);
    }
    //endregion

}
