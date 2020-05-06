package catan.game.game;

import catan.API.Response;
import catan.game.Player;
import catan.game.board.Board;
import catan.game.board.Tile;
import catan.game.card.Bank;
import catan.game.enumeration.ResourceType;
import catan.game.property.Building;
import catan.game.property.Road;
import catan.game.rule.Component;
import catan.game.rule.VictoryPoint;
import com.github.ankzz.dynamicfsm.fsm.FSM;
import javafx.util.Pair;
import org.apache.http.HttpStatus;

import java.util.*;

//TODO: Add functions to return available spots for buildings and roads.
//TODO: Add function to return available players to stole a resource when moving the robber.
public abstract class Game {
    protected Bank bank;
    protected Board board;
    protected Map<String, Player> players;
    protected List<String> playerOrder;
    protected int maxPlayers;
    protected String currentPlayer;
    protected Pair<String, Integer> currentLargestArmy;
    protected Pair<String, Integer> currentLongestRoad;
    protected Map<ResourceType, Integer> tradeOffer;
    protected Map<ResourceType, Integer> tradeRequest;

    public Game() {
        bank = null;
        board = new Board();
        players = new HashMap<>();
        playerOrder = new ArrayList<>();
        maxPlayers = 0;
        currentPlayer = null;
        currentLargestArmy = null;
        currentLongestRoad = null;
        tradeOffer = null;
        tradeRequest = null;
    }

    //region Getters

    public Bank getBank() {
        return bank;
    }

    public Board getBoard() {
        return board;
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public List<String> getPlayerOrder() {
        return playerOrder;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getNoPlayers() {
        return players.size();
    }

    public Player getPlayer(String playerId) {
        return players.get(playerId);
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public Pair<String, Integer> getCurrentLargestArmy() {
        return currentLargestArmy;
    }

    public Pair<String, Integer> getCurrentLongestRoad() {
        return currentLongestRoad;
    }

    public Map<ResourceType, Integer> getTradeOffer() {
        return tradeOffer;
    }

    public Map<ResourceType, Integer> getTradeRequest() {
        return tradeRequest;
    }

    //endregion

    //region Setters

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setPlayers(Map<String, Player> players) {
        this.players = players;
    }

    public void setPlayerOrder(List<String> playerOrder) {
        this.playerOrder = playerOrder;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setCurrentLargestArmy(Pair<String, Integer> currentLargestArmy) {
        this.currentLargestArmy = currentLargestArmy;
    }

    public void setCurrentLongestRoad(Pair<String, Integer> currentLongestRoad) {
        this.currentLongestRoad = currentLongestRoad;
    }

    public void setTradeOffer(Map<ResourceType, Integer> tradeOffer) {
        this.tradeOffer = tradeOffer;
    }

    public void setTradeRequest(Map<ResourceType, Integer> tradeRequest) {
        this.tradeRequest = tradeRequest;
    }

    //endregion

    //region Turn

    public void addPlayer(String playerId, Player player) {
        players.put(playerId, player);
    }

    public void addNextPlayer(String playerId) {
        playerOrder.add(playerId);
    }

    public void changeTurn() {
        updateBonusPoints();
        if (currentPlayerWon()) {
            //TODO: End game and show ranking.
        }
        int nextPlayer = (playerOrder.indexOf(currentPlayer) + 1) % playerOrder.size();
        currentPlayer = playerOrder.get(nextPlayer);
        players.get(currentPlayer).getTurnFlow().state.ProcessFSM("restart");
    }

    protected void updateBonusPoints() {
        // Update largest army.
        int usedKnights = players.get(currentPlayer).getUsedKnights();
        if (currentLargestArmy == null) {
            if (usedKnights >= Component.KNIGHTS_FOR_LARGEST_ARMY) {
                players.get(currentPlayer).takeLargestArmy();
                currentLargestArmy = new Pair<>(currentPlayer, usedKnights);
            }
        }
        else if (usedKnights > currentLargestArmy.getValue() &&
                !(currentPlayer.equals(currentLargestArmy.getKey()))) {
            players.get(currentLargestArmy.getKey()).giveLargestArmy();
            players.get(currentPlayer).takeLargestArmy();
            currentLargestArmy = new Pair<>(currentPlayer, usedKnights);
        }

        // Update largest army.
        int builtRoads = players.get(currentPlayer).getBuiltRoads();
        if (currentLongestRoad == null) {
            if (builtRoads >= Component.ROADS_FOR_LONGEST_ROAD) {
                players.get(currentPlayer).takeLongestRoad();
                currentLongestRoad = new Pair<>(currentPlayer, builtRoads);
            }
        }
        else if (builtRoads > currentLongestRoad.getValue() &&
                !(currentPlayer.equals(currentLongestRoad.getKey()))) {
            players.get(currentLongestRoad.getKey()).giveLongestRoad();
            players.get(currentPlayer).takeLongestRoad();
            currentLongestRoad = new Pair<>(currentPlayer, builtRoads);
        }
    }

    protected boolean currentPlayerWon() {
        return players.get(currentPlayer).getVictoryPoints() >= VictoryPoint.FINISH_VICTORY_POINTS;
    }

    public Response playTurn(String playerId, String command, Map<String, String> requestArguments) {
        // TODO: Add support for "synchronize" command.
        // TODO: Add support for "buildRoad" command when using RoadBuilding development.
        // TODO: Add suport for "getResource" command when using YearOfPlenty development.
        if (playerId.equals(currentPlayer)) {
            players.get(playerId).getTurnFlow().state.setShareData(requestArguments);
            players.get(playerId).getTurnFlow().state.ProcessFSM(command);
            Response response = players.get(playerId).getTurnFlow().response;
            // Reset player response.
            players.get(playerId).getTurnFlow().response = new Response(HttpStatus.SC_BAD_REQUEST, "Wrong command.", "");
            return response;
        }
        else if (command.equals("discardResources")) {
            //TODO: Add discardResources functionality.
        }
        else if (command.equals("wantToTrade")) {
            //TODO: Add wantToTrade functionality.
        }
        return new Response(HttpStatus.SC_FORBIDDEN, "Not your turn.", "");
    }

    public boolean startGame() {
        if (playerOrder.size() == 0) {
            return false;
        }
        bank = new Bank(new ArrayList<>(players.values()));
        for (Player player : players.values()) {
            player.setBank(bank);
        }
        currentPlayer = playerOrder.get(0);
        return true;
    }

    //endregion

    //region Dice

    public void rollDice() {
        Random dice = new Random();
        int firstDice = dice.nextInt(6) + 1;
        int secondDice = dice.nextInt(6) + 1;
        Map<String, Object> responseArguments = initializeRollDiceResponse();
        responseArguments.put("dice_1", firstDice);
        responseArguments.put("dice_2", secondDice);
        int diceSum = firstDice + secondDice;
        FSM currentState = players.get(currentPlayer).getTurnFlow().state;
        if (diceSum != 7) {
            responseArguments.putAll(giveResourcesFromDice(diceSum, responseArguments));
            currentState.setShareData(responseArguments);
            currentState.ProcessFSM("rollNotSeven");
        }
        else {
            for (String player : playerOrder) {
                int playerIndex = playerOrder.indexOf(player);
                responseArguments.put("resourcesToDiscard_" + playerIndex, players.get(player).getResourceNumber() / 2);
            }
            currentState.setShareData(responseArguments);
            currentState.ProcessFSM("rollSeven");
        }
    }

    public Map<String, Object> giveResourcesFromDice(int diceSum, Map<String, Object> responseArguments) {
        List<Tile> tiles = board.getTilesFromNumber(diceSum);
        for (Tile tile : tiles) {
            ResourceType resource = tile.getResource();
            List<Building> buildings = board.getAdjacentBuildings(tile);
            int neededResources = getNeededResources(buildings);
            if (!bank.existsResource(resource, neededResources)) {
                continue;
            }
            if (board.getRobberPosition().getId() == tile.getId()) {
                continue;
            }
            for (Building building : buildings) {
                Player owner = building.getOwner();
                if (owner != null) {
                    String playerId = owner.getId();
                    String argument = resource.toString() + '_' + playerOrder.indexOf(playerId);
                    int previousValue = (int) responseArguments.get(argument);
                    switch (building.getBuildingType()) {
                        case Settlement:
                            bank.takeResource(resource);
                            players.get(playerId).takeResource(resource);
                            responseArguments.put(argument, previousValue + 1);
                            break;
                        case City:
                            bank.takeResource(resource, 2);
                            players.get(playerId).takeResource(resource, 2);
                            responseArguments.put(argument, previousValue + 2);
                    }
                }
            }
        }
        return responseArguments;
    }

    public Map<String, Object> initializeRollDiceResponse() {
        Map<String, Object> responseArguments = new HashMap<>();
        for (String player : playerOrder) {
            int playerIndex = playerOrder.indexOf(player);
            responseArguments.put("player_" + playerIndex, player);
            for (ResourceType resource : ResourceType.values()) {
                if (resource != ResourceType.desert) {
                    responseArguments.put(resource.toString() + '_' + playerIndex, 0);
                }
            }
            responseArguments.put("resourcesToDiscard_" + playerIndex, 0);
        }
        return responseArguments;
    }

    public int getNeededResources(List<Building> buildings) {
        int neededResources = 0;
        for (Building building : buildings) {
            switch (building.getBuildingType()) {
                case Settlement:
                    ++neededResources;
                    break;
                case City:
                    neededResources += 2;
            }
        }
        return neededResources;
    }

    //endregion

    //region Buy

    protected int currentPlayerIndex() {
        for (int i = 0; i < playerOrder.size(); i++) {
            if (playerOrder.get(i).equals(currentPlayer))
                return i;
        }
        return -1;
    }

    protected boolean isTwoRoadsDistance(int buildingId) {
        if (board.getBuildings() == null || board.getBuildings().size() == 0) {
            return true;
        }
        List<Integer> neighborIntersections = board.getIntersectionGraph().getNeighborIntersections(buildingId);
        for (Integer neighbour : neighborIntersections) {
            if (board.getBuildings().get(neighbour).getOwner() != null)
                return false;
        }
        return true;
    }

    //TODO: Verify if player has settlement resources.
    public boolean buySettlement(int intersectionId) {

        Player player = players.get(currentPlayer);
        if (!isTwoRoadsDistance(intersectionId))
            return false;

        Building settlement = bank.takeSettlement(player);
        if (settlement == null || !player.buildSettlement(settlement))
            return false;

        settlement.setOwner(player);
        return true;
    }

    public boolean buyCity(int intersectionId) {
        Player player = players.get(currentPlayer);
        Building building = board.getBuildings().get(intersectionId);

        if (building == null || !building.getOwner().equals(player))
            return false;

        Building city = bank.takeCity(player);
        if (city == null)
            return false;
        return player.buildCity(city);
    }

    public boolean buyRoad(int intersectionId1, int intersectionId2) {
        Player player = players.get(currentPlayer);
        Building firstBuilding = board.getBuildings().get(intersectionId1);
        Building secondBuilding = board.getBuildings().get(intersectionId2);

        if (firstBuilding == null || secondBuilding == null)
            return false;
        if (!((firstBuilding.getOwner() == null || firstBuilding.getOwner().equals(player)) &&
                (secondBuilding.getOwner() == null || secondBuilding.getOwner().equals(player))))
            return false;
        Road road = bank.takeRoad(player);
        if (road == null)
            return false;
        return player.buyRoad(road);
    }

    //endregion

    //region Trade

    public void playerTrade(List<Player> playersThatAccepted, List<Pair<ResourceType, Integer>> offer,
                          List<Pair<ResourceType, Integer>> request) {
        Player traderPlayer = players.get(currentPlayer);
        Random rand = new Random();
        int index = rand.nextInt(playersThatAccepted.size());
        Player trader = playersThatAccepted.get(index);
        traderPlayer.updateTradeResources(offer, request);
        trader.updateTradeResources(request, offer);
    }

    //endregion

    //region Robber

    public boolean giveResources(List<Pair<String, Map<ResourceType, Integer>>> playersRes) {
        for (Pair<String, Map<ResourceType, Integer>> pair : playersRes) {
            players.get(pair.getKey()).removeResources(pair.getValue());
        }
        return true;

    }

    public boolean moveRobber(int tileId) {
        board.setRobberPosition(board.getTiles().get(tileId));
        return true;
    }

    public boolean stealResource(Pair<String, String> playerPair) {
        ResourceType type = players.get(playerPair.getValue()).removeRandomResources();
        players.get(playerPair.getValue()).takeResource(type);
        return true;
    }

    //endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;
        Game game = (Game) o;
        return Objects.equals(getPlayers(), game.getPlayers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlayers());
    }
}
