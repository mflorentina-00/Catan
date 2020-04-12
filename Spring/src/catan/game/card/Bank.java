package catan.game.card;

import catan.game.Player;
import catan.game.card.development.*;

import catan.game.enumeration.PlayerType;
import catan.game.enumeration.ResourceType;

import java.util.*;

import catan.game.property.Intersection;
import catan.game.property.Road;

import catan.game.rule.Component;

public class Bank {
    private Map<ResourceType, Integer> resources;
    private Stack<Knight> knights;
    private Stack<Monopoly> monopolies;
    private Stack<RoadBuilding> roadBuildings;
    private Stack<VictoryPoint> victoryPoints;
    private Stack<YearOfPlenty> yearsOfPlenty;
    private Map<PlayerType, Stack<Road>> roads;
    private Map<PlayerType, Stack<Intersection>> settlements;
    private Map<PlayerType, Stack<Intersection>> cities;

    public Bank() {
        createResources();
        createDevelopments();
        createProperties();
    }

    public boolean getResource(ResourceType resource) {
        int resourceNumber = resources.get(resource);
        if (existsResource(resource)) {
            resources.put(resource, --resourceNumber);
            return true;
        }
        return false;
    }

    public boolean existsResource(ResourceType resource) {
        return resources.get(resource) > 0;
    }

    public Knight getKnight(Player player) {
        if (!knights.isEmpty()) {
            Knight knight = knights.pop();
            knight.setOwner(player);
            return knight;
        }
        return null;
    }

    public Monopoly getMonopoly(Player player) {
        if (!monopolies.isEmpty()) {
            Monopoly monopoly = monopolies.pop();
            monopoly.setOwner(player);
            return monopoly;
        }
        return null;
    }

    public RoadBuilding getRoadBuilding(Player player) {
        if (!roadBuildings.isEmpty()) {
            RoadBuilding roadBuilding = roadBuildings.pop();
            roadBuilding.setOwner(player);
            return roadBuilding;
        }
        return null;
    }

    public VictoryPoint getVictoryPoint(Player player) {
        if (!victoryPoints.isEmpty()) {
            VictoryPoint victoryPoint = victoryPoints.pop();
            victoryPoint.setOwner(player);
            return victoryPoint;
        }
        return null;
    }

    public YearOfPlenty getYearOfPlenty(Player player) {
        if (!yearsOfPlenty.isEmpty()) {
            YearOfPlenty yearOfPlenty = yearsOfPlenty.pop();
            yearOfPlenty.setOwner(player);
            return yearOfPlenty;
        }
        return null;
    }

    public Road getRoad(Player player, PlayerType playerType) {
        Stack<Road> playerRoads = roads.get(playerType);
        if (!playerRoads.isEmpty()) {
            Road road = playerRoads.pop();
            road.setOwner(player);
            return road;
        }
        return null;
    }

    public Intersection getSettlement(Player player, PlayerType playerType) {
        Stack<Intersection> playerSettlements = settlements.get(playerType);
        if (!playerSettlements.isEmpty()) {
            Intersection settlement = playerSettlements.pop();
            settlement.setOwner(player);
            return settlement;
        }
        return null;
    }

    public Intersection getCity(Player player, PlayerType playerType) {
        Stack<Intersection> playerCities = cities.get(playerType);
        if (!playerCities.isEmpty()) {
            Intersection city = playerCities.pop();
            city.setOwner(player);
            return city;
        }
        return null;
    }

    public void restoreSettlement(Player player, PlayerType playerType, Intersection intersection) {
        intersection.setOwner(null);
        Stack<Intersection> playerSettlements = settlements.get(playerType);
        playerSettlements.push(intersection);
        settlements.put(playerType, playerSettlements);
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
        roads = new HashMap<>(Component.NO_OF_PLAYERS);
        for (PlayerType player : PlayerType.values()) {
            Stack<Road> playerRoads = new Stack<>();
            for (int index = 0; index < Component.ROADS; ++index) {
                playerRoads.push(new Road());
            }
            roads.put(player, playerRoads);
        }
    }

    private void createSettlements() {
        settlements = new HashMap<>(Component.NO_OF_PLAYERS);
        for (PlayerType player : PlayerType.values()) {
            Stack<Intersection> playerSettlements = new Stack<>();
            for (int index = 0; index < Component.SETTLEMENTS; ++index) {
                playerSettlements.push(new Intersection());
            }
            settlements.put(player, playerSettlements);
        }
    }

    private void createCities() {
        cities = new HashMap<>(Component.NO_OF_PLAYERS);
        for (PlayerType player : PlayerType.values()) {
            Stack<Intersection> playerCities = new Stack<>();
            for (int index = 0; index < Component.CITIES; ++index) {
                playerCities.push(new Intersection());
            }
            cities.put(player, playerCities);
        }
    }
}
