package catan.game;

import catan.game.card.development.Knight;
import catan.game.card.development.Monopoly;
import catan.game.card.development.RoadBuilding;
import catan.game.card.development.YearOfPlenty;
import catan.game.enumeration.ResourceType;
import catan.game.property.Intersection;
import catan.game.property.Road;
import catan.game.rule.Component;
import catan.game.rule.Cost;
import catan.game.rule.VictoryPoint;
import javafx.util.Pair;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

public class Player {

    /* Fields */

    private final String ID;
    private final Game game;
    private TurnFlow state;

    private List<Road> roads;
    private List<Intersection> settlements;
    private List<Intersection> cities;
    
    private Map<ResourceType, Integer> resources;
    private List<Knight> knights;
    private List<Monopoly> monopolies;
    private List<RoadBuilding> roadBuildings;
    private List<YearOfPlenty> yearsOfPlenty;

    private int usedArmyCards;

    private boolean hasLongestRoad;
    private boolean hasLargestArmy;

    private int publicVP;
    private int hiddenVP;

    // region Constructors

    public Player(String ID, Game game) {
        this.ID = ID;
        this.game = game;
        try {
            state = new TurnFlow(game);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        
        roads = new ArrayList<>();
        settlements = new ArrayList<>();
        cities = new ArrayList<>();

        resources = new HashMap<>();
        resources.put(ResourceType.Ore,5);
        resources.put(ResourceType.Wool,5);
        resources.put(ResourceType.Grain,5);
        resources.put(ResourceType.Lumber,5);
        resources.put(ResourceType.Brick,5);

        knights = new ArrayList<>();
        monopolies = new ArrayList<>();
        roadBuildings = new ArrayList<>();
        yearsOfPlenty = new ArrayList<>();

        publicVP = 0;
        hiddenVP = 0;
        usedArmyCards = 0;
        hasLongestRoad = false;
        hasLargestArmy = false;
    }

    // endregion
    
    // region Getters

    public String getID() {
        return ID;
    }
    public TurnFlow getState() {
        return state;
    }
    
    public List<Road> getRoads() { return roads; }
    public List<Intersection> getSettlements() { return settlements; }
    public List<Intersection> getCities() { return cities; }

    public Map<ResourceType, Integer> getResources() { return resources; }
    public Integer getResourceNumber(ResourceType resource) { return resources.get(resource); }

    public List<Knight> getKnights() {
        return knights;
    }
    public List<Monopoly> getMonopolies() {
        return monopolies;
    }
    public List<RoadBuilding> getRoadBuildings() {
        return roadBuildings;
    }
    public List<YearOfPlenty> getYearsOfPlenty() {
        return yearsOfPlenty;
    }

    public int getLargestArmy() { return usedArmyCards; }

    public int getPublicVP() { return publicVP; }
    public int getVP() { return publicVP + hiddenVP; }

    // endregion

    // region Setters and Adders

    public void addResource(ResourceType type)  { resources.put(type, resources.get(type) + 1); }

    public void addKnight(Knight knight){
        knights.add(knight);
    }
    public void addMonopolies(Monopoly monopoly){
        monopolies.add(monopoly);
    }
    public void addRoadBuilding(RoadBuilding roadBuilding){
        roadBuildings.add(roadBuilding);
    }
    public void addYearsOfPlenty(YearOfPlenty yearOfPlenty){
        yearsOfPlenty.add(yearOfPlenty);
    }
    public void addVictoryPoint(){
        hiddenVP++;
    }

    // endregion

    // region Road

    // TODO REMINDER: This is used by GAME class
    public boolean buildRoad(Road road) {
        if (!canBuildRoad(road)) { return false; }
        roads.add(road);
        removeRoadResources();
        return true;
    }

    // TODO REMINDER: The GAME class verifies if the id is free
    private boolean canBuildRoad(Road road) {
        if (!canBuildRoad()) { return false; }
        // Cazul de baza cand punem primele doua drumuri
        if (roads.size() < Component.INITIAL_FREE_ROADS) {
            for (Intersection building : settlements) {
                if (building.getID() == road.getStart().getID() || building.getID() == road.getEnd().getID()) {
                    return true;
                }
            }
        }
        // Adaugam un drum doar daca este adiacent cu altul.
        else {
            for (Road value : roads) {
                if (value.hasCommonIntersection(road)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canBuildRoad() {
        return roads.size() < Component.ROADS && hasRoadResources();
    }

    private boolean hasRoadResources() {
        return resources.get(ResourceType.Lumber) >= Cost.ROAD_LUMBER
                && resources.get(ResourceType.Brick) >= Cost.ROAD_BRICK;
    }

    private void removeRoadResources() {
        int bricks = resources.get(ResourceType.Brick);
        int lumbers = resources.get(ResourceType.Lumber);

        resources.put(ResourceType.Brick, bricks - Cost.ROAD_BRICK);
        resources.put(ResourceType.Lumber, lumbers - Cost.ROAD_LUMBER);
    }

    // endregion

    // region Settlement

    // TODO REMINDER: This is used by GAME class
    public boolean buildSettlement(Intersection settlement) {
        if (!canBuildSettlement(settlement)) { return false; }
        settlements.add(settlement);
        removeSettlementResources();
        publicVP++;
        return true;
    }

    // TODO REMINDER: The GAME class verifies if the id is free and not adjacent to another settlement (2 roads rule).
    public boolean canBuildSettlement(Intersection settlement) {
        if (!canBuildSettlement()) { return false; }
        boolean isValid = false;

        if (settlements.size() < Component.INITIAL_FREE_SETTLEMENTS && cities.size() == 0) {
            return true;
        }
        // Adaugam doar la capatul unui drum.
        for (Road road : roads) {
            if (road.getStart().getID() == settlement.getID() || road.getEnd().getID() == settlement.getID())
            {
                    return true;
            }
        }
        return false;
    }

    public boolean canBuildSettlement() {
        return settlements.size() < Component.SETTLEMENTS && hasSettlementResources();
    }

    private boolean hasSettlementResources() {
        return resources.get(ResourceType.Lumber) >= Cost.SETTLEMENT_LUMBER &&
                resources.get(ResourceType.Grain) >= Cost.SETTLEMENT_GRAIN &&
                resources.get(ResourceType.Brick) >= Cost.SETTLEMENT_BRICK &&
                resources.get(ResourceType.Wool) >= Cost.SETTLEMENT_WOOL;
    }

    private void removeSettlementResources() {
        int lumbers = resources.get(ResourceType.Lumber);
        int grains = resources.get(ResourceType.Grain);
        int bricks = resources.get(ResourceType.Brick);
        int wools = resources.get(ResourceType.Wool);

        resources.put(ResourceType.Lumber, lumbers - Cost.SETTLEMENT_LUMBER);
        resources.put(ResourceType.Grain, grains-Cost.SETTLEMENT_GRAIN);
        resources.put(ResourceType.Wool, wools - Cost.SETTLEMENT_WOOL);
        resources.put(ResourceType.Brick, bricks - Cost.SETTLEMENT_BRICK);

    }

    // endregion

    // region City

    // TODO REMINDER: This is used by GAME class
    public boolean buildCity(Intersection city) {
        if (!canBuildCity(city)) { return false; }
        cities.add(city);
        // REMOVE SETTLEMENT
        for (Intersection settlement : settlements) {
            if (settlement.getID() == city.getID()) {
                settlements.remove(settlement);
                break;
            }
        }
        publicVP++;
        removeCityResources();
        return true;
    }

    // TODO REMINDER: The GAME class verifies if the id is possessed by the player.
    public boolean canBuildCity(Intersection city) {
        if (!canBuildCity()) { return false; }
        // Isi construise dinainte un settlement acolo.
        for (Intersection settlement : settlements) {
            if (settlement.getID() == city.getID())
                return true;
        }
        return false;
    }
    
    public boolean canBuildCity() {
        return cities.size() < Component.CITIES && hasCityResources();
    }
    
    private boolean hasCityResources() {
        return resources.get(ResourceType.Ore) >= Cost.CITY_ORES &&
                resources.get(ResourceType.Grain) >= Cost.CITY_GRAINS;
    }

    private void removeCityResources() {
        int ores = resources.get(ResourceType.Ore);
        int grains = resources.get(ResourceType.Grain);

        resources.put(ResourceType.Ore, ores - Cost.CITY_ORES);
        resources.put(ResourceType.Grain, grains - Cost.CITY_GRAINS);

    }

    // endregion

    // TODO: Add development logic.

    // region Trade

    // TODO: understand were to implement playerAcceptingTrade and playerWhichTrade
    public boolean makeTrade(List<Pair<ResourceType, Integer>> offer, List<Pair<ResourceType, Integer>> request) {
        if (!canMakeTrade(offer)) { return false; }
        List<Player> acceptTrade = game.getPlayersWhoAcceptTrade(this, offer, request);
        if (acceptTrade.size() == 0)
            return false;
        else {
            // TODO: wait player's choice of trade partner
            game.setPlayerWhoTrades(this, acceptTrade.get(0), offer, request);
            return true;
        }
    }

    public boolean canMakeTrade(List<Pair<ResourceType, Integer>> offer) {
        for (Pair<ResourceType, Integer> pair : offer) {
            ResourceType resource = pair.getKey();
            if (resources.get(resource) < pair.getValue())
                return false;
        }
        return true;
    }

    public void updateTradeResources(List<Pair<ResourceType, Integer>> offer,
                                     List<Pair<ResourceType,Integer>> request) {
        for (Pair<ResourceType, Integer> pair : offer) {
            ResourceType resource = pair.getKey();
            Integer resourceCount = resources.get(resource);
            resources.put(resource, resourceCount - pair.getValue());
        }
        for (Pair<ResourceType, Integer> pair : request) {
            ResourceType type = pair.getKey();
            Integer resourceCount = resources.get(type);
            resources.put(type, resourceCount + pair.getValue());
        }
    }
    
    // endregion

    // region Bonus Victory Points

    public void takeLongestRoad() {
        hasLongestRoad = true;
        publicVP += VictoryPoint.LONGEST_ROAD;
    }

    public void giveLongestRoad() {
        hasLongestRoad = false;
        publicVP -= VictoryPoint.LONGEST_ROAD;
    }

    public void takeLargestArmy() {
        hasLargestArmy = true;
        publicVP += VictoryPoint.LARGEST_ARMY;
    }
    public void giveLargestArmy() {
        hasLargestArmy = false;
        publicVP -= VictoryPoint.LARGEST_ARMY;
    }

    // endregion

    @Override
    public String toString() {
        return "Player{" +
                "id=" + ID +
                '}';
    }
}
