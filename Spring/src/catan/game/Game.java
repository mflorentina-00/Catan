package catan.game;

import catan.game.enumeration.ResourceType;
import javafx.util.Pair;

import java.util.*;


public class Game {
    private Map<String,Player> players;
    private int maxPlayers;
    private catan.game.map.Map map;

    public Game() {
        players=new HashMap<>();
        map=new catan.game.map.Map();
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, Player> players) {
        this.players = players;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
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


}
