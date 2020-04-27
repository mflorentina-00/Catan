package catan.game.gameType;

import catan.API.Response;
import catan.API.request.Status;
import catan.game.Player;
import catan.game.board.Board;
import catan.game.board.Tile;
import catan.game.card.Bank;
import catan.game.enumeration.ResourceType;
import catan.game.property.Intersection;
import catan.game.property.Road;
import catan.game.rule.Component;
import catan.game.rule.VictoryPoint;
import javafx.util.Pair;

import java.util.*;

public abstract class Game {
    protected Bank bank;
    protected Board board;
    protected Pair<String,Integer> currentLargestArmy=null;
    protected Pair<String,Integer> currentLongestRoad=null;
    protected Map<String, Player> players;
    protected List<String> playerOrder;
    protected String currentPlayer;
    protected int maxPlayers;

    public Board getBoard() {
        return board;
    }

    public Game() {
        board = new Board();
        players = new HashMap<>();
        playerOrder = new ArrayList<>();
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

    //region turn

    public void addNextPlayer(String userID){
        playerOrder.add(userID);
    }

    public boolean changeTurn() {
        updateBonusPoints();
        //TODO use checkWin return value that ends the game
        checkWin();

        int i = playerOrder.indexOf(currentPlayer);
        if (i == playerOrder.size() - 1)
            i = 0;
        else
            ++i;
        currentPlayer = playerOrder.get(i);
        players.get(currentPlayer).getState().fsm.ProcessFSM("Restart");
        return true;
    }

    protected boolean checkWin() {
        return players.get(currentPlayer).getVP() >= VictoryPoint.FINISH_VICTORY_POINTS;
    }

    protected void updateBonusPoints() {
        if(currentLargestArmy==null){
            if(players.get(currentPlayer).getLargestArmy()>=Component.ROADS_FOR_LONGEST_ROAD){
                players.get(currentPlayer).takeLargestArmy();
                currentLargestArmy=new Pair<>(currentPlayer,players.get(currentPlayer).getLargestArmy());
            }
        }
        else if(players.get(currentPlayer).getLargestArmy()>currentLargestArmy.getValue()&&
                !(currentPlayer.equals(currentLargestArmy.getKey()))
            ){

            players.get(currentLargestArmy.getKey()).giveLargestArmy();
            players.get(currentPlayer).takeLargestArmy();
            currentLargestArmy=new Pair<>(currentPlayer,players.get(currentPlayer).getLargestArmy());
        }

        int longestRoad=players.get(currentPlayer).getLongestRoad();
        if(currentLongestRoad==null){
            if(longestRoad>=Component.ARMY_FOR_LARGEST_ARMY) {
                players.get(currentPlayer).takeLongestRoad();
                currentLongestRoad = new Pair<>(currentPlayer, longestRoad);
            }
        }
        else if(longestRoad>currentLongestRoad.getValue()&&
                !(currentPlayer.equals(currentLongestRoad.getKey()))
                ) {

            players.get(currentLongestRoad.getKey()).giveLongestRoad();
            players.get(currentPlayer).takeLongestRoad();
            currentLongestRoad=new Pair<>(currentPlayer,longestRoad);
        }

    }

    public Response playTurn(String playerID, String command, Map<String,String> jsonArgs) {
        if (playerID.equals(currentPlayer)) {
            players.get(playerID).getState().fsm.setShareData(jsonArgs);
            players.get(playerID).getState().fsm.ProcessFSM(command);
            Response response =  players.get(playerID).getState().response;
            //Reset player response in case that the state does not have the command | the command does not exist
            players.get(playerID).getState().response = new Response(Status.ERROR,"Wrong Command","");
            return response;
        }
        return new Response(Status.ERROR,"Not your turn!","");
    }

    public boolean startGame() {
        if (playerOrder.size() == 0) {
            return false;
        }
        bank = new Bank();
        System.out.println();
        currentPlayer = playerOrder.get(0);
        return true;
    }
    //endregion

    //region give resources+roll dice
    public boolean rollDice(){
        Random dice=new Random();
        int firstDice=dice.nextInt(6)+1;
        int secondDice=dice.nextInt(6)+1;
        int diceSum=firstDice+secondDice;

        if(diceSum!=7){
            if(giveResourcesFromDice(diceSum)){
                players.get(currentPlayer).getState().fsm.ProcessFSM("rollNotASeven");
                return true;
            }
            return false;
        }
        else{
            players.get(currentPlayer).getState().fsm.ProcessFSM("rollASeven");
            return true;
        }

    }

    public boolean giveResourcesFromDice(int diceSum) {
        List<Tile> tiles=board.getTilesFromNumbers(diceSum);
        for(Tile tile:tiles){

            if(!bank.getResource(tile.getResource()))
                return false;
            if(board.getRobberPosition().getID()==tile.getID())
                continue;
            List<Intersection> intersections=board.getIntersectionListFromTile(tile);
            for(Intersection intersection:intersections){
                if(!(intersection.getOwner()==null)){
                    Player owner=intersection.getOwner();
                    players.get(owner.getID()).addResource(tile.getResource());
                }
            }
        }

        return true;
    }

    //endregion

    //region buy_properties
    protected int currentPlayerIndex() {
        for(int i=0;i<playerOrder.size();i++){
            if(playerOrder.get(i).equals(currentPlayer))
                return i;
        }
        return -1;
    }
    protected boolean isTwoRoadsDistance(Intersection intersection) {
        List<Integer> neighborIntersections=board.getIntersectionGraph().getNeighborIntersections(intersection.getID());
        for(Integer neighbour:neighborIntersections){
            if(board.getBuildings().get(neighbour).getOwner()!=null)
                return false;
        }
        return true;
    }
    public boolean buySettlement(int intersectionId){

        Player player=players.get(currentPlayer);
        Intersection intersection=board.getBuildings().get(intersectionId);
        if(intersection==null||intersection.getOwner()!=null ||!isTwoRoadsDistance(intersection))
            return false;

        Intersection settlement=bank.getSettlement(player,currentPlayerIndex());
        if(settlement==null||!player.buildSettlement(settlement))
            return false;

        board.getBuildings().get(intersectionId).setOwner(player);
        return true;
    }
    public boolean buyCity(int intersectionId){
        Player player=players.get(currentPlayer);
        Intersection intersection=board.getBuildings().get(intersectionId);

        if(intersection==null||!intersection.getOwner().equals(player))
            return false;

        Intersection city=bank.getCity(player,currentPlayerIndex());
        if(city==null)
            return false;
        return player.buildCity(city);
    }
    public boolean buyRoad(int intersectionId1,int intersectionId2){
        Player player=players.get(currentPlayer);
       Intersection firstIntersection=board.getBuildings().get(intersectionId1);
       Intersection secondIntersection=board.getBuildings().get(intersectionId2);

        if(firstIntersection==null||secondIntersection==null)
            return false;
        if(!((firstIntersection.getOwner()==null||firstIntersection.getOwner().equals(player))&&
                (secondIntersection.getOwner()==null||secondIntersection.getOwner().equals(player)) ))
            return false;
        Road road=bank.getRoad(player,currentPlayerIndex());
        if(road==null)
            return false;
        return player.buildRoad(road);
    }

    //endregion

    //region trade

    public List<Player> getPlayersWhoAcceptTrade(Player player, List<Pair<ResourceType, Integer>> offer,
                                                 List<Pair<ResourceType, Integer>> request) {
        List<Player> playersThatAccept = new ArrayList<>();
        for(String playerID : players.keySet()) {
            // TODO W8 players's response
            //  if response is YES and
            //  the below verification is true, then we add the player to the possible pick
            if (players.get(playerID).canMakeTrade(request)) {
                playersThatAccept.add(players.get(playerID));
            }
        }
        return playersThatAccept;
    }

    public void setPlayerWhoTrades(Player player, Player trader, List<Pair<ResourceType, Integer>> offer,
                                   List<Pair<ResourceType, Integer>> request) {
        // TODO add trader notify
        player.updateTradeResources(offer, request);
        trader.updateTradeResources(request, offer);
    }
    //endregion

    //region Robber
    public boolean giveResources(List<Pair<String,Map<ResourceType,Integer>>> playersRes){
        for(Pair<String,Map<ResourceType,Integer>> pair :playersRes){
            players.get(pair.getKey()).removeResources(pair.getValue());
        }
        return true;

    }
    public boolean moveRobber(int tileId){
        board.setRobberPosition(board.getTiles().get(tileId));
        return true;
    }
    public boolean giveSelectedResource(Pair<String,String> playerPair){
        ResourceType type=players.get(playerPair.getValue()).removeRandomResources();
        players.get(playerPair.getValue()).addResource(type);
        return true;
    }
    //endregion

    //endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game = (Game) o;
        return getMaxPlayers() == game.getMaxPlayers() &&
                Objects.equals(bank, game.bank) &&
                Objects.equals(board, game.board) &&
                Objects.equals(getPlayers(), game.getPlayers()) &&
                Objects.equals(playerOrder, game.playerOrder) &&
                Objects.equals(currentPlayer, game.currentPlayer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bank, board, getPlayers(), playerOrder, currentPlayer, getMaxPlayers());
    }
}
