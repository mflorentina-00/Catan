import java.util.ArrayList;
import java.awt.Color;
import java.util.List;

public class Player {

    /* Fields */

    private int id;
    private ArrayList<Road> roads;
    private ArrayList<Intersection> settlements;
    private ArrayList<Intersection> cities;

    // free means "without cost" (it doesn't mean "available")
    private int remainingRoads;
    private int remainingSettlements;
    private int freeCities; // used for debugging

    private List<Resource> resourceCards;
    private List<DevelopmentCards> developmentCards;

    private int usedArmyCards;

    private int publicVP;
    private int hiddenVP;
    private boolean hasLongestRoad;
    private boolean hasLargestArmy;

    /* Constructors */

    public Player(int id) {
        this.id=id;
        roads = new ArrayList<Road>(Rules.INITIAL_FREE_ROADS);
        settlements = new ArrayList<Intersection>(Rules.INITIAL_FREE_SETTLEMENTS);
        cities = new ArrayList<Intersection>(Rules.INITIAL_FREE_CITIES);

        remainingRoads = Rules.INITIAL_FREE_ROADS;
        remainingSettlements = Rules.INITIAL_FREE_SETTLEMENTS;
        freeCities = 0;

        resourceCards = new ArrayList<Resource>();
        developmentCards=new ArrayList<DevelopmentCards>();


        publicVP = 0;
        hiddenVP = 0;
        usedArmyCards=0;
        hasLongestRoad = false;
        hasLargestArmy = false;
    }

    /* Getters */

    public int getId() { return id; }
    public ArrayList<Road> getRoads() { return roads; }
    public ArrayList<Intersection> getSettlements() { return settlements; }
    public ArrayList<Intersection> getCities() { return cities; }

    public List<Resource> getResourceCards() { return resourceCards; }
    public List<DevelopmentCards> getDevelopmentCards() { return developmentCards; }

    public int getLargestArmy() { return usedArmyCards; }
    public int getPublicVP() { return publicVP; }
    public int getVP() { return publicVP + hiddenVP; }


    public void giveLongestRoad() {
        hasLongestRoad = true;
        publicVP += Rules.LONGEST_ROAD_VP;
    }
    public void takeLongestRoad() {
        hasLongestRoad = false;
        publicVP -= Rules.LONGEST_ROAD_VP;
    }
    public void giveLargestArmy() {
        hasLargestArmy = true;
        publicVP += Rules.LARGEST_ARMY_VP;
    }
    public void takeLargestArmy() {
        hasLargestArmy = false;
        publicVP -= Rules.LARGEST_ARMY_VP;
    }


    @Override
    public String toString() {
        return "Player{ id= "+id+"}";
    }

}