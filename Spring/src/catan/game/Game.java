package catan.game;

import catan.API.Response;
import catan.API.request.Status;
import catan.game.board.Board;
import catan.game.card.Bank;
import catan.game.enumeration.ResourceType;
import javafx.util.Pair;

import java.util.*;

public class Game {
    private Bank bank;
    private Board board;
    private Map<String, Player> players;
    private List<String> playerOrder;
    private String currentPlayer;
    private int maxPlayers;

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

    public void addNextPlayer(String userID){
        playerOrder.add(userID);
    }

    public boolean changeTurn() {
        int i = playerOrder.indexOf(currentPlayer);
        if (i == playerOrder.size() - 1)
            i = 0;
        else
            ++i;
        currentPlayer = playerOrder.get(i);
        //TODO make restart go to start when necessary;
        players.get(currentPlayer).getState().fsm.ProcessFSM("Restart");
        return true;
    }

    public Response playTurn(String playerID, String command, String jsonArgs) {
        if (playerID.equals(currentPlayer)) {
            players.get(playerID).getState().fsm.setShareData(jsonArgs);
            players.get(playerID).getState().fsm.ProcessFSM(command);
            return players.get(playerID).getState().response;
        }
        return new Response(Status.ERROR,"Not your turn!");
    }

    public boolean startGame() {
        if (playerOrder.size() == 0) {
            return false;
        }
        bank = new Bank();
        currentPlayer = playerOrder.get(0);
        return true;
    }

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
