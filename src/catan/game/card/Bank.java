package catan.game.card;

import catan.game.Player;
import catan.game.card.development.*;
import catan.game.enumeration.ResourceType;
import catan.game.property.Intersection;
import catan.game.property.Road;
import catan.game.rule.Component;

import java.util.*;

public class Bank {
    private List<Player> players;
    private Map<ResourceType, Integer> resources;
    private Stack<Knight> knights;
    private Stack<Monopoly> monopolies;
    private Stack<RoadBuilding> roadBuildings;
    private Stack<VictoryPoint> victoryPoints;
    private Stack<YearOfPlenty> yearsOfPlenty;
    private Map<Player, Stack<Road>> roads;
    private Map<Player, Stack<Intersection>> settlements;
    private Map<Player, Stack<Intersection>> cities;

    public Bank(List<Player> players) {
        this.players = players;
        createResources();
        createDevelopments();
        createProperties();
    }

    public boolean existsResource(ResourceType resourceType, int resourceNumber) {
        return resources.get(resourceType) >= resourceNumber;
    }

    public boolean existsResource(ResourceType resourceType) {
        return existsResource(resourceType, 1);
    }

    public boolean hasRoad(Player player) {
        return !roads.get(player).isEmpty();
    }

    public boolean hasSettlement(Player player) {
        return !settlements.get(player).isEmpty();
    }

    public boolean hasCity(Player player) {
        return !settlements.get(player).isEmpty();
    }

    public boolean getResource(ResourceType resourceType, int resourceNumber) {
        int oldResourceNumber = resources.get(resourceType);
        if (existsResource(resourceType, resourceNumber)) {
            resources.put(resourceType, oldResourceNumber - resourceNumber);
            return true;
        }
        return false;
    }

    public boolean getResource(ResourceType resourceType) {
        return getResource(resourceType, 1);
    }

    public Knight getKnight(Player player) {
        if (knights.isEmpty()) {
            return null;
        }
        Knight knight = knights.pop();
        knight.setOwner(player);
        return knight;
    }

    public Monopoly getMonopoly(Player player) {
        if (monopolies.isEmpty()) {
            return null;
        }
        Monopoly monopoly = monopolies.pop();
        monopoly.setOwner(player);
        return monopoly;
    }

    public RoadBuilding getRoadBuilding(Player player) {
        if (roadBuildings.isEmpty()) {
            return null;
        }
        RoadBuilding roadBuilding = roadBuildings.pop();
        roadBuilding.setOwner(player);
        return roadBuilding;
    }

    public VictoryPoint getVictoryPoint(Player player) {
        if (victoryPoints.isEmpty()) {
            return null;
        }
        VictoryPoint victoryPoint = victoryPoints.pop();
        victoryPoint.setOwner(player);
        return victoryPoint;
    }

    public YearOfPlenty getYearOfPlenty(Player player) {
        if (yearsOfPlenty.isEmpty()) {
            return null;
        }
        YearOfPlenty yearOfPlenty = yearsOfPlenty.pop();
        yearOfPlenty.setOwner(player);
        return yearOfPlenty;
    }

    public Road getRoad(Player player) {
        Stack<Road> playerRoads = roads.get(player);
        if (playerRoads.isEmpty()) {
            return null;
        }
        Road road = playerRoads.pop();
        road.setOwner(player);
        return road;
    }

    public Intersection getSettlement(Player player) {
        Stack<Intersection> playerSettlements = settlements.get(player);
        if (playerSettlements.isEmpty()) {
            return null;
        }
        Intersection settlement = playerSettlements.pop();
        settlement.setOwner(player);
        return settlement;
    }

    public Intersection getCity(Player player) {
        Stack<Intersection> playerCities = cities.get(player);
        if (playerCities.isEmpty()) {
            return null;
        }
        Intersection city = playerCities.pop();
        city.setOwner(player);
        return city;
    }

    public void putResource(ResourceType resourceType, int resourceNumber) {
        int oldResourceNumber = resources.get(resourceType);
        resources.put(resourceType, oldResourceNumber + resourceNumber);
    }

    public void putResource(ResourceType resourceType) {
        putResource(resourceType, 1);
    }

    public void putSettlement(Player player, Intersection intersection) {
        intersection.setOwner(null);
        Stack<Intersection> playerSettlements = settlements.get(player);
        playerSettlements.push(intersection);
        settlements.put(player, playerSettlements);
    }

    private void createResources() {
        resources = new HashMap<>(5);
        for (ResourceType resourceType : ResourceType.values()) {
            resources.put(resourceType, Component.RESOURCES_BY_TYPE);
        }
    }

    private void createDevelopments() {
        createKnights();
        createVictoryPoints();
        createRoadBuildings();
        createYearsOfPlenty();
        createMonopolies();
    }

    private void createKnights() {
        knights = new Stack<>();
        for (int index = 0; index < Component.KNIGHTS; ++index) {
            knights.push(new Knight());
        }
    }

    private void createVictoryPoints() {
        victoryPoints = new Stack<>();
        for (int index = 0; index < Component.VICTORY_POINTS; ++index) {
            victoryPoints.push(new VictoryPoint());
        }
    }

    private void createRoadBuildings() {
        roadBuildings = new Stack<>();
        for (int index = 0; index < Component.ROAD_BUILDINGS; ++index) {
            roadBuildings.push(new RoadBuilding());
        }
    }

    private void createYearsOfPlenty() {
        yearsOfPlenty = new Stack<>();
        for (int index = 0; index < Component.YEARS_OF_PLENTY; ++index) {
            yearsOfPlenty.push(new YearOfPlenty());
        }
    }

    private void createMonopolies() {
        monopolies = new Stack<>();
        for (int index = 0; index < Component.MONOPOLIES; ++index) {
            monopolies.push(new Monopoly());
        }
    }

    private void createProperties() {
        createRoads();
        createSettlements();
        createCities();
    }

    private void createRoads() {
        roads = new HashMap<>();
        for (Player player : players) {
            Stack<Road> playerRoads = new Stack<>();
            for (int index = 0; index < Component.ROADS; ++index) {
                playerRoads.push(new Road());
            }
            roads.put(player, playerRoads);
        }
    }

    private void createSettlements() {
        settlements = new HashMap<>();
        for (Player player : players) {
            Stack<Intersection> playerSettlements = new Stack<>();
            for (int index = 0; index < Component.SETTLEMENTS; ++index) {
                playerSettlements.push(new Intersection());
            }
            settlements.put(player, playerSettlements);
        }
    }

    private void createCities() {
        cities = new HashMap<>();
        for (Player player : players) {
            Stack<Intersection> playerCities = new Stack<>();
            for (int index = 0; index < Component.CITIES; ++index) {
                playerCities.push(new Intersection());
            }
            cities.put(player, playerCities);
        }
    }
}
