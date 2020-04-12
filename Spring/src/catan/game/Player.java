package catan.game;

import catan.game.enumeration.DevelopmentType;
import catan.game.enumeration.ResourceType;
import catan.game.property.Intersection;
import catan.game.property.Road;
import catan.game.rule.Component;
import catan.game.rule.VictoryPoint;

import java.util.ArrayList;
import java.util.List;

//TODO Implement get things
public class Player {

    /* Fields */

    private int id;

    private List<Road> roads;
    private List<Intersection> settlements;
    private List<Intersection> cities;

    // free means "without cost" (it doesn't mean "available")
    //TODO Numărul total de proprietăți n-ar trebui să fie ținut de bancă și cerut după de jucător?
    private int remainingRoads;
    private int remainingSettlements;
    private int remainingCities; // used for debugging

    //TODO Aici cred că trebuie ca la bancă (dar cu list, nu cu stack).
    private List<ResourceType> resources;
    private List<DevelopmentType> developments;

    private int usedArmyCards;

    private boolean hasLongestRoad;
    private boolean hasLargestArmy;

    private int publicVP;
    private int hiddenVP;

    /* Constructors */

    public Player() {}

    public Player(int id) {
        this.id = id;
        roads = new ArrayList<>(Component.ROADS);
        settlements = new ArrayList<>(Component.SETTLEMENTS);
        cities = new ArrayList<>(Component.CITIES);

        remainingRoads = Component.ROADS;
        remainingSettlements = Component.SETTLEMENTS;
        remainingCities = Component.CITIES;

        resources = new ArrayList<>();
        developments = new ArrayList<>();

        publicVP = 0;
        hiddenVP = 0;
        usedArmyCards = 0;
        hasLongestRoad = false;
        hasLargestArmy = false;
    }

    /* Getters */

    public int getId() { return id; }

    public List<Road> getRoads() { return roads; }
    public List<Intersection> getSettlements() { return settlements; }
    public List<Intersection> getCities() { return cities; }

    public List<ResourceType> getResources() { return resources; }
    public List<DevelopmentType> getDevelopments() { return developments; }

    public int getLargestArmy() { return usedArmyCards; }

    public int getPublicVP() { return publicVP; }
    public int getVP() { return publicVP + hiddenVP; }


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

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                '}';
    }
}
