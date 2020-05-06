package catan.game;

import catan.game.card.Bank;
import catan.game.card.development.Knight;
import catan.game.card.development.Monopoly;
import catan.game.card.development.RoadBuilding;
import catan.game.card.development.YearOfPlenty;
import catan.game.enumeration.ResourceType;
import catan.game.game.Game;
import catan.game.property.Building;
import catan.game.property.Road;
import catan.game.rule.Component;
import catan.game.rule.Cost;
import catan.game.rule.VictoryPoint;
import javafx.util.Pair;
import org.apache.http.HttpStatus;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

public class Player {

    /* Fields */

    private final String id;
    private final Game game;
    private Bank bank;
    private TurnFlow turnFlow;

    private List<Road> roads;
    private List<Building> settlements;
    private List<Building> cities;

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

    public Player(String id, Game game) {
        this.id = id;
        this.game = game;
        try {
            turnFlow = new TurnFlow(game);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        roads = new ArrayList<>();
        settlements = new ArrayList<>();
        cities = new ArrayList<>();

        resources = new HashMap<>();
        resources.put(ResourceType.ore, 5);
        resources.put(ResourceType.wool, 5);
        resources.put(ResourceType.grain, 5);
        resources.put(ResourceType.lumber, 5);
        resources.put(ResourceType.brick, 5);

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

    public String getId() {
        return id;
    }

    public TurnFlow getTurnFlow() {
        return turnFlow;
    }

    public List<Road> getRoads() {
        return roads;
    }

    public List<Building> getSettlements() {
        return settlements;
    }

    public List<Building> getCities() {
        return cities;
    }

    public Map<ResourceType, Integer> getResources() {
        return resources;
    }

    public int getResourceNumber(ResourceType resourceType) {
        return resources.get(resourceType);
    }

    public int getResourceNumber() {
        int resourceNumber = 0;
        for (ResourceType resourceType : resources.keySet()) {
            resourceNumber += resources.get(resourceType);
        }
        return resourceNumber;
    }

    public ResourceType stealResource(int resourceIndex) {
        int resourceNumber = 0;
        for (ResourceType resourceType : resources.keySet()) {
            resourceNumber += resources.get(resourceType);
            if (resourceNumber + resources.get(resourceType) > resourceIndex) {
                resources.put(resourceType, resources.get(resourceType) - 1);
                return resourceType;
            }
        }
        return null;
    }

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

    public int getUsedKnights() {
        return usedArmyCards;
    }

    public int getPublicVP() {
        return publicVP;
    }

    public int getVictoryPoints() {
        return publicVP + hiddenVP;
    }

    // endregion

    // region Setters and Adders

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public void takeResource(ResourceType resourceType) {
        resources.put(resourceType, resources.get(resourceType) + 1);
    }

    public void takeResource(ResourceType resourceType, int resourceNumber) {
        resources.put(resourceType, resources.get(resourceType) + resourceNumber);
    }

    public void addKnight(Knight knight) {
        knights.add(knight);
    }

    public void addMonopolies(Monopoly monopoly) {
        monopolies.add(monopoly);
    }

    public void addRoadBuilding(RoadBuilding roadBuilding) {
        roadBuildings.add(roadBuilding);
    }

    public void addYearsOfPlenty(YearOfPlenty yearOfPlenty) {
        yearsOfPlenty.add(yearOfPlenty);
    }

    public void addVictoryPoint() {
        hiddenVP++;
    }

    // endregion

    //region Remove
    public boolean removeResources(Map<ResourceType, Integer> playerResources) {
        for (ResourceType type : playerResources.keySet()) {
            int previousResources = resources.get(type);
            if (previousResources < playerResources.get(type))
                return false;
            resources.put(type, previousResources - playerResources.get(type));
        }
        return true;
    }

    public boolean removeResource(ResourceType type) {
        int previousResources = resources.get(type);
        if (previousResources < 1) {
            return false;
        }
        resources.put(type, previousResources - 1);
        return true;
    }

    public boolean removeResource(ResourceType type, int resourceNumber) {
        int previousResources = resources.get(type);
        if (previousResources < resourceNumber) {
            return false;
        }
        resources.put(type, previousResources - resourceNumber);
        return true;
    }

    public ResourceType removeRandomResources() {
        ResourceType[] resourceTypes = ResourceType.values();
        Random random = new Random();
        int i = random.nextInt(resourceTypes.length);
        while (getResourceNumber(resourceTypes[i]) <= 0) {
            i = random.nextInt(resourceTypes.length);
        }
        if (removeResource(resourceTypes[i])) {
            return resourceTypes[i];
        }
        return null;
    }
    //endregion

    // region Road

    // TODO REMINDER: This is used by GAME class

    // Called only for the first two roads or when using RoadBuilding development.
    public Pair<Integer, String> buildRoad(Building start, Building end) {
        if (start.getOwner() != this && end.getOwner() != this) {
            return new Pair<>(HttpStatus.SC_FORBIDDEN, "The road does not connect one of your roads, settlements or cities.");
        }
        if (!bank.hasRoads(this)) {
            return new Pair<>(HttpStatus.SC_NOT_FOUND, "You have no more roads to build.");
        }
        Road road = bank.takeRoad(this);
        road.setStart(start);
        road.setEnd(end);
        roads.add(road);
        return new Pair<>(HttpStatus.SC_OK, "The road was built successfully.");
    }

    public boolean buyRoad(Road road) {
        if (!canBuildRoad(road)) {
            return false;
        }
        roads.add(road);
        removeRoadResources();
        return true;
    }

    // TODO REMINDER: The GAME class verifies if the id is free
    private boolean canBuildRoad(Road road) {
        if (!canBuildRoad()) {
            return false;
        }
        // Cazul de baza cand punem primele doua drumuri
        if (roads.size() < Component.INITIAL_FREE_ROADS) {
            for (Building building : settlements) {
                if (building.getId() == road.getStart().getId() || building.getId() == road.getEnd().getId()) {
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
        return resources.get(ResourceType.lumber) >= Cost.ROAD_LUMBER
                && resources.get(ResourceType.brick) >= Cost.ROAD_BRICK;
    }

    private void removeRoadResources() {
        int bricks = resources.get(ResourceType.brick);
        int lumbers = resources.get(ResourceType.lumber);

        resources.put(ResourceType.brick, bricks - Cost.ROAD_BRICK);
        resources.put(ResourceType.lumber, lumbers - Cost.ROAD_LUMBER);
    }

    // endregion

    // region Settlement

    // TODO REMINDER: This is used by GAME class
    public boolean buildSettlement(Building settlement) {
        if (!canBuildSettlement(settlement)) {
            return false;
        }
        settlements.add(settlement);
        removeSettlementResources();
        publicVP++;
        return true;
    }

    // TODO REMINDER: The GAME class verifies if the id is free and not adjacent to another settlement (2 roads rule).
    public boolean canBuildSettlement(Building settlement) {
        if (!canBuildSettlement()) {
            return false;
        }
        boolean isValid = false;

        if (settlements.size() < Component.INITIAL_FREE_SETTLEMENTS && cities.size() == 0) {
            return true;
        }
        // Adaugam doar la capatul unui drum.
        for (Road road : roads) {
            if (road.getStart().getId() == settlement.getId() || road.getEnd().getId() == settlement.getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean canBuildSettlement() {
        return settlements.size() < Component.SETTLEMENTS && hasSettlementResources();
    }

    private boolean hasSettlementResources() {
        return resources.get(ResourceType.lumber) >= Cost.SETTLEMENT_LUMBER &&
                resources.get(ResourceType.grain) >= Cost.SETTLEMENT_GRAIN &&
                resources.get(ResourceType.brick) >= Cost.SETTLEMENT_BRICK &&
                resources.get(ResourceType.wool) >= Cost.SETTLEMENT_WOOL;
    }

    private void removeSettlementResources() {
        int lumbers = resources.get(ResourceType.lumber);
        int grains = resources.get(ResourceType.grain);
        int bricks = resources.get(ResourceType.brick);
        int wools = resources.get(ResourceType.wool);

        resources.put(ResourceType.lumber, lumbers - Cost.SETTLEMENT_LUMBER);
        resources.put(ResourceType.grain, grains - Cost.SETTLEMENT_GRAIN);
        resources.put(ResourceType.wool, wools - Cost.SETTLEMENT_WOOL);
        resources.put(ResourceType.brick, bricks - Cost.SETTLEMENT_BRICK);

    }

    // endregion

    // region City

    // TODO REMINDER: This is used by GAME class
    public boolean buildCity(Building city) {
        if (!canBuildCity(city)) {
            return false;
        }
        cities.add(city);
        // REMOVE SETTLEMENT
        for (Building settlement : settlements) {
            if (settlement.getId() == city.getId()) {
                settlements.remove(settlement);
                break;
            }
        }
        publicVP++;
        removeCityResources();
        return true;
    }

    // TODO REMINDER: The GAME class verifies if the id is possessed by the player.
    public boolean canBuildCity(Building city) {
        if (!canBuildCity()) {
            return false;
        }
        // Isi construise dinainte un settlement acolo.
        for (Building settlement : settlements) {
            if (settlement.getId() == city.getId())
                return true;
        }
        return false;
    }

    public boolean canBuildCity() {
        return cities.size() < Component.CITIES && hasCityResources();
    }

    private boolean hasCityResources() {
        return resources.get(ResourceType.ore) >= Cost.CITY_ORES &&
                resources.get(ResourceType.grain) >= Cost.CITY_GRAINS;
    }

    private void removeCityResources() {
        int ores = resources.get(ResourceType.ore);
        int grains = resources.get(ResourceType.grain);

        resources.put(ResourceType.ore, ores - Cost.CITY_ORES);
        resources.put(ResourceType.grain, grains - Cost.CITY_GRAINS);

    }

    // endregion

    // region Trade


    public boolean hasResources(Map<ResourceType, Integer> resources) {
        for (ResourceType resource : resources.keySet()) {
            if (this.resources.get(resource) < resources.get(resource))
                return false;
        }
        return true;
    }

    public boolean startTrade(Map<ResourceType, Integer> offer, Map<ResourceType, Integer> request) {
        if (hasResources(offer)) {
            game.setTradeOffer(offer);
            game.setTradeRequest(request);
            return true;
        }
        return false;
    }


    public boolean wantToTrade(String answer) {
        if (hasResources(game.getTradeRequest())) {
            return answer.equalsIgnoreCase("yes");
        }
        return false;
    }

    public boolean selectOpponent(String playerId) {
        if (playerId != null) {
            //TODO: Move offer to specified player.
            //TODO: Move request to current player.
            return true;
        }
        return false;
    }

    public void updateTradeResources(List<Pair<ResourceType, Integer>> offer,
                                     List<Pair<ResourceType, Integer>> request) {
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

    //region Longest Road

    public int getBuiltRoads() {
        int maxRoadLength = 0;
        roads.sort(new Comparator<Road>() {
            @Override
            public int compare(Road road1, Road road2) {
                return road1.getStart().getId() - road2.getStart().getId();
            }
        });
        List<Integer> roadsMax = new ArrayList<>(roads.size());
        Collections.fill(roadsMax, 1);
        for (int i = 1; i < roads.size(); i++) {
            for (int j = i - 1; j >= 0; j--) {
                if (roads.get(i).getStart().getId() == roads.get(j).getEnd().getId()) {
                    roadsMax.add(i, roadsMax.get(j) + 1);
                    break;
                }
            }
            if (roadsMax.get(i) > maxRoadLength)
                maxRoadLength = roadsMax.get(i);
        }
        return maxRoadLength;
    }
    //endregion

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return Objects.equals(getId(), player.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Player{" +
                "id='" + id + '\'' +
                '}';
    }
}