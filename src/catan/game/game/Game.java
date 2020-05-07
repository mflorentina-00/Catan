package catan.game.game;

import catan.API.response.Code;
import catan.API.response.Messages;
import catan.API.response.Response;
import catan.game.player.Player;
import catan.game.board.Board;
import catan.game.board.Tile;
import catan.game.bank.Bank;
import catan.game.enumeration.Building;
import catan.game.enumeration.Resource;
import catan.game.property.Intersection;
import catan.game.property.Road;
import catan.game.rule.Component;
import catan.game.rule.VictoryPoint;
import catan.util.Helper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ankzz.dynamicfsm.fsm.FSM;
import javafx.util.Pair;
import org.apache.http.HttpStatus;

import java.util.*;

public abstract class Game {
    protected Bank bank;
    protected Board board;
    protected Map<String, Player> players;
    protected List<String> playerOrder;
    protected int maxPlayers;
    protected String currentPlayer;
    protected Pair<String, Integer> currentLargestArmy;
    protected Pair<String, Integer> currentLongestRoad;
    protected Map<Resource, Integer> tradeOffer;
    protected Map<Resource, Integer> tradeRequest;
    protected List<String> tradeOpponents;
    protected String tradeOpponent;
    protected boolean notDiscardedAll;
    protected Messages messages;

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
        notDiscardedAll = false;
        messages = Messages.getInstance();
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

    public Map<Resource, Integer> getTradeOffer() {
        return tradeOffer;
    }

    public Map<Resource, Integer> getTradeRequest() {
        return tradeRequest;
    }

    public List<String> getTradeOpponents() {
        return tradeOpponents;
    }

    public String getTradeOpponent() {
        return tradeOpponent;
    }

    public boolean isNotDiscardedAll() {
        return notDiscardedAll;
    }

    public Messages getMessages() {
        return messages;
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

    public void setTradeOffer(Map<Resource, Integer> tradeOffer) {
        this.tradeOffer = tradeOffer;
    }

    public void setTradeRequest(Map<Resource, Integer> tradeRequest) {
        this.tradeRequest = tradeRequest;
    }

    public void setTradeOpponents(List<String> tradeOpponents) {
        this.tradeOpponents = tradeOpponents;
    }

    public void setTradeOpponent(String tradeOpponent) {
        this.tradeOpponent = tradeOpponent;
    }

    public void setNotDiscardedAll(boolean notDiscardedAll) {
        this.notDiscardedAll = notDiscardedAll;
    }

    public void setMessages(Messages messages) {
        this.messages = messages;
    }

    //endregion

    //region Turn

    public void addPlayer(String playerId, Player player) {
        players.put(playerId, player);
    }

    public void addNextPlayer(String playerId) {
        playerOrder.add(playerId);
    }

    public void changeTurn(int direction) {
        updateBonusPoints();
        if (currentPlayerWon()) {
            //TODO: End game and show ranking.
        }
        int nextPlayer = (playerOrder.indexOf(currentPlayer) + direction) % playerOrder.size();
        currentPlayer = playerOrder.get(nextPlayer);
        players.get(currentPlayer).getState().fsm.ProcessFSM("restart");
    }

    protected void updateBonusPoints() {
        // Update largest army.
        int usedKnights = players.get(currentPlayer).getUsedKnights();
        if (currentLargestArmy == null) {
            if (usedKnights >= Component.KNIGHTS_FOR_LARGEST_ARMY) {
                players.get(currentPlayer).takeLargestArmy();
                currentLargestArmy = new Pair<>(currentPlayer, usedKnights);
            }
        } else if (usedKnights > currentLargestArmy.getValue() &&
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
        } else if (builtRoads > currentLongestRoad.getValue() &&
                !(currentPlayer.equals(currentLongestRoad.getKey()))) {
            players.get(currentLongestRoad.getKey()).giveLongestRoad();
            players.get(currentPlayer).takeLongestRoad();
            currentLongestRoad = new Pair<>(currentPlayer, builtRoads);
        }
    }

    protected boolean currentPlayerWon() {
        return players.get(currentPlayer).getVictoryPoints() >= VictoryPoint.FINISH_VICTORY_POINTS;
    }

    public Response playTurn(String playerId, String command, Map<String, String> requestArguments)
            throws JsonProcessingException {
        Response otherResponse = processOtherCommand(playerId, command, requestArguments);
        if (otherResponse != null) {
            return otherResponse;
        }
        if (playerId.equals(currentPlayer)) {
            players.get(playerId).getState().fsm.setShareData(requestArguments);
            players.get(playerId).getState().fsm.ProcessFSM(command);
            Response response = players.get(playerId).getState().response;
            // Reset player response.
            players.get(playerId).getState().response = new Response(HttpStatus.SC_ACCEPTED,
                    "The request is forbidden.", "");
            return response;
        }
        return new Response(HttpStatus.SC_ACCEPTED, "It is not your turn.", "");
    }

    public Response processOtherCommand(String playerId, String command, Map<String, String> requestArguments)
            throws JsonProcessingException {
        Map<String, String> responseArguments = new HashMap<>();
        responseArguments.put("sentAll", "false");
        switch (command) {
            case "discardResources":
                return discardResources(playerId, requestArguments, responseArguments);
            case "wantToTrade":
        }
        if (notDiscardedAll) {
            return new Response(HttpStatus.SC_ACCEPTED, "The request is forbidden.", "");
        }
        return null;
    }

    public Response discardResources(String playerId, Map<String, String> requestArguments,
                                     Map<String, String> responseArguments) throws JsonProcessingException {
        if (!notDiscardedAll) {
            return new Response(HttpStatus.SC_ACCEPTED, "Dice does not sum seven.",
                    new ObjectMapper().writeValueAsString(responseArguments));
        }
        if (players.get(playerId).getResourceNumber() <= 7) {
            return new Response(HttpStatus.SC_ACCEPTED,
                    "The player does not have more than seven resource cards.",
                    new ObjectMapper().writeValueAsString(responseArguments));
        }
        Map<Resource, Integer> resources = new HashMap<>();
        for (String resource : requestArguments.keySet()) {
            Resource resourceType = Helper.getResourceTypeFromString(resource);
            if (resourceType == null) {
                return new Response(HttpStatus.SC_ACCEPTED, "An argument is invalid.", "");
            }
            resources.put(resourceType, Integer.valueOf(requestArguments.get(resource)));
        }
        Code code = discardResources(playerId, resources);
        if (code != null) {
            return new Response(HttpStatus.SC_ACCEPTED, messages.getMessage(code),
                    new ObjectMapper().writeValueAsString(responseArguments));
        }
        for (String player : playerOrder) {
            if (players.get(player).getResourceNumber() > 7) {
                return new Response(HttpStatus.SC_OK, "Discarded resources successfully.",
                        new ObjectMapper().writeValueAsString(responseArguments));
            }
        }
        responseArguments.put("sentAll", "true");
        notDiscardedAll = false;
        return new Response(HttpStatus.SC_OK, "Discarded resources successfully.",
                new ObjectMapper().writeValueAsString(responseArguments));
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
        FSM currentState = players.get(currentPlayer).getState().fsm;
        if (diceSum != 7) {
            notDiscardedAll = false;
            responseArguments.putAll(giveResourcesFromDice(diceSum, responseArguments));
            currentState.setShareData(responseArguments);
            currentState.ProcessFSM("rollNotSeven");
        } else {
            notDiscardedAll = true;
            for (String player : playerOrder) {
                int playerIndex = playerOrder.indexOf(player);
                int resourceNumber = players.get(player).getResourceNumber();
                if (resourceNumber > 7) {
                    responseArguments.put("resourcesToDiscard_" + playerIndex,
                            players.get(player).getResourceNumber() / 2);
                }
            }
            currentState.setShareData(responseArguments);
            currentState.ProcessFSM("rollSeven");
        }
    }

    public Map<String, Object> giveResourcesFromDice(int diceSum, Map<String, Object> responseArguments) {
        List<Tile> tiles = board.getTilesFromNumber(diceSum);
        for (Tile tile : tiles) {
            Resource resource = tile.getResource();
            List<Intersection> intersections = board.getAdjacentIntersections(tile);
            int neededResources = getNeededResources(intersections);
            if (!bank.existsResource(resource, neededResources)) {
                continue;
            }
            if (board.getRobberPosition().getId() == tile.getId()) {
                continue;
            }
            for (Intersection intersection : intersections) {
                Player owner = intersection.getOwner();
                if (owner != null) {
                    String playerId = owner.getId();
                    String argument = resource.toString() + '_' + playerOrder.indexOf(playerId);
                    int previousValue = (int) responseArguments.get(argument);
                    switch (intersection.getBuilding()) {
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
            for (Resource resource : Resource.values()) {
                if (resource != Resource.desert) {
                    responseArguments.put(resource.toString() + '_' + playerIndex, 0);
                }
            }
            responseArguments.put("resourcesToDiscard_" + playerIndex, 0);
        }
        return responseArguments;
    }

    public int getNeededResources(List<Intersection> intersections) {
        int neededResources = 0;
        for (Intersection intersection : intersections) {
            switch (intersection.getBuilding()) {
                case Settlement:
                    ++neededResources;
                    break;
                case City:
                    neededResources += 2;

            }
        }
        return neededResources;
    }

    public Code discardResources(String playerId, Map<Resource, Integer> resourcesToDiscard) {
        Code code = players.get(playerId).removeResources(resourcesToDiscard);
        if (code != null) {
            return code;
        }
        bank.giveResources(resourcesToDiscard);
        return null;
    }

    // TODO de modificat toate functiile care se ocupa de returnarea pozitiilor disponibile dupa ce
    //  facem clasa Intersection in Board si modificam clasa Building
    public boolean isAvailableHousePlacement (Intersection intersection) {
        if (intersection.getBuilding()== Building.Settlement
                || intersection.getBuilding()== Building.City)
            return false;
        for (Intersection building:
             board.getIntersections()) {
            if(board.getAdjacentIntersections(intersection.getId()).contains(building.getId()))
                return false;
        }
        return true;
    }

    // TODO de modificat pentru a returna doar pozitii valide
    public List<Integer> getAvailableRoadPlacements() {
        Player player = players.get(currentPlayer);
        Set<Integer> availableRoadPlacements = new HashSet<>();
        for (Road road:
             player.getRoads()) {
            availableRoadPlacements.addAll(board.getAdjacentIntersections(road.getStart().getId()));
            availableRoadPlacements.addAll(board.getAdjacentIntersections(road.getEnd().getId()));
        }
        return (List<Integer>) availableRoadPlacements;
    }

    public List<Integer> getAvailableHousePlacements() {
        Player player = players.get(currentPlayer);
        Set<Integer> availableHousePlacements = new HashSet<>();
        for (Road road:
             player.getRoads()) {
            if (isAvailableHousePlacement(road.getStart()))
                availableHousePlacements.add(road.getStart().getId());
            if (isAvailableHousePlacement(road.getEnd()))
                availableHousePlacements.add(road.getEnd().getId());
        }
        return (List<Integer>) availableHousePlacements;
    }
    //endregion

    //region place house and road region
    public boolean buildSettlement(int intersection) {
        /*
        Player player = players.get(currentPlayer);
        Building intersection = board.getBuildings().get(intersection);
        if (intersection == null || intersection.getOwner() != null || !isTwoRoadsDistance(intersection))
            return false;
        board.getBuildings().get(intersection).setOwner(player);
        */
        return true;
    }

    public boolean buildRoad(int intersectionId1, int intersectionId2) {
        /*
        Player player = players.get(currentPlayer);
        Building firstIntersection = board.getBuildings().get(intersectionId1);
        Building secondIntersection = board.getBuildings().get(intersectionId2);

        if (firstIntersection == null || secondIntersection == null)
            return false;
        if (!((firstIntersection.getOwner() == null || firstIntersection.getOwner().equals(player)) &&
                (secondIntersection.getOwner() == null || secondIntersection.getOwner().equals(player))))
            return false;
        if (!board.getIntersectionGraph().areAdjacent(intersectionId1, intersectionId2))
            return false;
        Road road = bank.getRoad(player);
        road.setCoordinates(firstIntersection, secondIntersection);
        return player.addRoad(road);
        */
        return true;
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
        if (board.getIntersections() == null || board.getIntersections().size() == 0) {
            return true;
        }
        List<Integer> neighborIntersections = board.getIntersectionGraph().getAdjacentIntersections(buildingId);
        for (Integer neighbour : neighborIntersections) {
            if (board.getIntersections().get(neighbour).getOwner() != null)
                return false;
        }
        return true;
    }

    //TODO: Verify if player has settlement resources.
    public boolean buySettlement(int intersectionId) {
        /*
        Player player = players.get(currentPlayer);
        if (!isTwoRoadsDistance(intersectionId))
            return false;

        Intersection settlement = bank.takeSettlement(player);
        if (settlement == null || !player.buildSettlement(settlement))
            return false;

        settlement.setOwner(player);
        */
        return true;
    }

    public boolean buyCity(int intersectionId) {
        /*
        Player player = players.get(currentPlayer);
        Intersection intersection = board.getIntersections().get(intersectionId);

        if (intersection == null || !intersection.getOwner().equals(player))
            return false;

        Intersection city = bank.takeCity(player);
        if (city == null)
            return false;
        return player.buildCity(city);
         */
        return true;
    }

    public boolean buyRoad(int intersectionId1, int intersectionId2) {
        /*
        Player player = players.get(currentPlayer);
        Intersection firstIntersection = board.getIntersections().get(intersectionId1);
        Intersection secondIntersection = board.getIntersections().get(intersectionId2);

        if (firstIntersection == null || secondIntersection == null)
            return false;
        if (!((firstIntersection.getOwner() == null || firstIntersection.getOwner().equals(player)) &&
                (secondIntersection.getOwner() == null || secondIntersection.getOwner().equals(player))))
            return false;
        Road road = bank.takeRoad(player);
        if (road == null) {
            return false;
        }
        if (!board.getIntersectionGraph().areAdjacent(intersectionId1, intersectionId2)) {
            return false;
        }

        road.setStart(firstIntersection);
        road.setEnd(secondIntersection);
        return player.buyRoad(road);
        */
        return true;
    }

    //endregion

    //region Trade

    public void playerTrade(List<Player> playersThatAccepted, List<Pair<Resource, Integer>> offer,
                            List<Pair<Resource, Integer>> request) {
        Player traderPlayer = players.get(currentPlayer);
        Random rand = new Random();
        int index = rand.nextInt(playersThatAccepted.size());
        Player trader = playersThatAccepted.get(index);
        traderPlayer.updateTradeResources(offer, request);
        trader.updateTradeResources(request, offer);
    }

    //endregion

    //region Robber

    public boolean giveResources(List<Pair<String, Map<Resource, Integer>>> playersRes) {
        for (Pair<String, Map<Resource, Integer>> pair : playersRes) {
            players.get(pair.getKey()).removeResources(pair.getValue());
        }
        return true;

    }

    //TODO:
    public Code moveRobber(int tileId) {
        if (board.getRobberPosition().getId() == tileId) {
            return Code.SameTile;
        }
        board.setRobberPosition(board.getTiles().get(tileId));
        return null;
    }

    public boolean stealResource(Pair<String, String> playerPair) {
        Resource type = players.get(playerPair.getValue()).removeRandomResources();
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
