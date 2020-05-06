package catan.game.card;

import catan.game.Player;
import catan.game.card.development.*;
import catan.game.enumeration.BuildingType;
import catan.game.enumeration.ResourceType;
import catan.game.property.Building;
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
    private Map<Player, Stack<Building>> settlements;
    private Map<Player, Stack<Building>> cities;

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

    public boolean hasRoads(Player player) {
        return !roads.get(player).isEmpty();
    }

    public boolean hasSettlements(Player player) {
        return !settlements.get(player).isEmpty();
    }

    public boolean hasCities(Player player) {
        return !settlements.get(player).isEmpty();
    }

    public int takeResource(ResourceType resourceType, int resourceNumber) {
        int oldResourceNumber = resources.get(resourceType);
        if (existsResource(resourceType, resourceNumber)) {
            resources.put(resourceType, oldResourceNumber - resourceNumber);
            return resourceNumber;
        }
        return 0;
    }

    public int takeResource(ResourceType resourceType) {
        return takeResource(resourceType, 1);
    }

    public Knight takeKnight(Player player) {
        if (knights.isEmpty()) {
            return null;
        }
        Knight knight = knights.pop();
        knight.setOwner(player);
        return knight;
    }

    public Monopoly takeMonopoly(Player player) {
        if (monopolies.isEmpty()) {
            return null;
        }
        Monopoly monopoly = monopolies.pop();
        monopoly.setOwner(player);
        return monopoly;
    }

    public RoadBuilding takeRoadBuilding(Player player) {
        if (roadBuildings.isEmpty()) {
            return null;
        }
        RoadBuilding roadBuilding = roadBuildings.pop();
        roadBuilding.setOwner(player);
        return roadBuilding;
    }

    public VictoryPoint takeVictoryPoint(Player player) {
        if (victoryPoints.isEmpty()) {
            return null;
        }
        VictoryPoint victoryPoint = victoryPoints.pop();
        victoryPoint.setOwner(player);
        return victoryPoint;
    }

    public YearOfPlenty takeYearOfPlenty(Player player) {
        if (yearsOfPlenty.isEmpty()) {
            return null;
        }
        YearOfPlenty yearOfPlenty = yearsOfPlenty.pop();
        yearOfPlenty.setOwner(player);
        return yearOfPlenty;
    }

    public Road takeRoad(Player player) {
        Stack<Road> playerRoads = roads.get(player);
        if (playerRoads.isEmpty()) {
            return null;
        }
        Road road = playerRoads.pop();
        road.setOwner(player);
        return road;
    }

    public Building takeSettlement(Player player) {
        Stack<Building> playerSettlements = settlements.get(player);
        if (playerSettlements.isEmpty()) {
            return null;
        }
        Building settlement = playerSettlements.pop();
        settlement.setOwner(player);
        return settlement;
    }

    public Building takeCity(Player player) {
        Stack<Building> playerCities = cities.get(player);
        if (playerCities.isEmpty()) {
            return null;
        }
        Building city = playerCities.pop();
        city.setOwner(player);
        return city;
    }

    public void retrieveResource(ResourceType resourceType, int resourceNumber) {
        int oldResourceNumber = resources.get(resourceType);
        resources.put(resourceType, oldResourceNumber + resourceNumber);
    }

    public void retrieveResource(ResourceType resourceType) {
        retrieveResource(resourceType, 1);
    }

    public void retrieveSettlement(Player player, Building building) {
        building.setOwner(null);
        Stack<Building> playerSettlements = settlements.get(player);
        playerSettlements.push(building);
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
            Stack<Building> playerSettlements = new Stack<>();
            for (int index = 0; index < Component.SETTLEMENTS; ++index) {
                playerSettlements.push(new Building(BuildingType.Settlement));
            }
            settlements.put(player, playerSettlements);
        }
    }

    private void createCities() {
        cities = new HashMap<>();
        for (Player player : players) {
            Stack<Building> playerCities = new Stack<>();
            for (int index = 0; index < Component.CITIES; ++index) {
                playerCities.push(new Building(BuildingType.City));
            }
            cities.put(player, playerCities);
        }
    }
}
