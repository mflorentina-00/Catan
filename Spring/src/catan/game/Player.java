package catan.game;

import catan.game.card.development.Knight;
import catan.game.card.development.Monopoly;
import catan.game.card.development.RoadBuilding;
import catan.game.card.development.YearOfPlenty;
import catan.game.enumeration.DevelopmentType;
import catan.game.enumeration.ResourceType;
import catan.game.property.Intersection;
import catan.game.property.Road;
import catan.game.rule.Component;
import catan.game.rule.Cost;
import catan.game.rule.VictoryPoint;
import javafx.util.Pair;

import java.util.*;

public class Player {

    /* Fields */

    private int id;
    public final Game game;

    private List<Road> roads;
    private List<Intersection> settlements;
    private List<Intersection> cities;

    //TODO Numărul total de proprietăți n-ar trebui să fie ținut de bancă și cerut după de jucător?
    private int remainingRoads;
    private int remainingSettlements;
    private int remainingCities;

    //Am schimbat :))
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

    /* Constructors */

    public Player(Game game) {
        this.game = game;
    }

    public Player(int id,Game game) {
        this.game=game;
        this.id = id;
        roads = new ArrayList<>(Component.ROADS);
        settlements = new ArrayList<>(Component.SETTLEMENTS);
        cities = new ArrayList<>(Component.CITIES);

        remainingRoads = Component.ROADS;
        remainingSettlements = Component.SETTLEMENTS;
        remainingCities = Component.CITIES;

        resources = new HashMap<>();

        knights=new ArrayList<>();
        monopolies=new ArrayList<>();
        roadBuildings=new ArrayList<>();
        yearsOfPlenty=new ArrayList<>();

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

    public Map<ResourceType, Integer> getResources() { return resources; }
    public Integer getResourceNumber(ResourceType type){return resources.get(type);}
    public void addResource(ResourceType type){
        int number=resources.get(type)+1;
        resources.put(type,number);
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

    public int getLargestArmy() { return usedArmyCards; }

    public int getPublicVP() { return publicVP; }
    public int getVP() { return publicVP + hiddenVP; }

    //TODO REMINDER: The GAME class verifies if the id is free
    public boolean canBuildRoad() {
        return (roads.size() < Component.ROADS) &&
                (haveTheResourcesToBuildRoad());
    }
    private boolean haveTheResourcesToBuildRoad() {
        return ((resources.get(ResourceType.Lumber)>= Cost.ROAD_LUMBER) && resources.get(ResourceType.Brick)>=Cost.ROAD_BRICK);
    }
    public boolean canBuildRoad(Road r) {
        if (!canBuildRoad()) { return false; }
        boolean isValid = false;

        //Cazul de baza cand punem primele doua drumuri
        if (roads.size() < Component.INITIAL_FREE_ROADS) {
            for(Intersection intersection:settlements){
                if(intersection.getId()==r.getEnd().getId()||intersection.getId()==r.getStart().getId()){
                    isValid=true;
                }
            }
        }
        //Cazul de baza, adaugam un drum doar daca este adiacent cu altul
        else {
            for (int i = 0; !isValid && i < roads.size(); i++) {
                if (roads.get(i).hasCommonIntersection(r)) { isValid = true; }
            }

        }
        return isValid;
    }
    //TODO REMINDER: This is used by GAME class
    public boolean buildRoad(Road r) {
        if (!canBuildRoad(r)) { return false; }
        roads.add(r);
        removeResourcesForRoad();
        return true;
    }
    private void removeResourcesForRoad() {
        int bricks = resources.get(ResourceType.Brick);
        int lumbers= resources.get(ResourceType.Lumber);

        resources.put(ResourceType.Brick,bricks-Cost.ROAD_BRICK);
        resources.put(ResourceType.Lumber,lumbers-Cost.ROAD_LUMBER);
    }

    //TODO REMINDER: The GAME class verifies if the id is free and not adjacent to another settlement(2 roads rule).
    public boolean canBuildSettlement() {
        return (settlements.size() < Component.SETTLEMENTS) &&
                (haveTheResourcesToBuySettlement());
    }
    private boolean haveTheResourcesToBuySettlement() {
        return (resources.get(ResourceType.Lumber)>=Cost.SETTLEMENT_LUMBER&&resources.get(ResourceType.Grain)>=Cost.SETTLEMENT_GRAIN&&
                resources.get(ResourceType.Brick)>=Cost.SETTLEMENT_BRICK&&resources.get(ResourceType.Wool)>=Cost.SETTLEMENT_WOOL);
    }
    public boolean canBuildSettlement(Intersection settlement) {
        if (!canBuildSettlement()) { return false; }
        boolean isValid = false;

        if(settlements.size()<Component.INITIAL_FREE_SETTLEMENTS&&cities.size()==0){return true;}
        else{
            for(Road road:roads){
                if(road.getStart().getId()==settlement.getId()||road.getEnd().getId()==settlement.getId())
                {
                    isValid=true;
                    break;
                }
            }
        }
        return isValid;
    }
    //TODO REMINDER: This is used by GAME class
    public boolean buildSettlement(Intersection settlement) {
        if (!canBuildSettlement(settlement)) { return false; }
        settlements.add(settlement);
        removeResourcesForSettlement();
        publicVP++;
        return true;
    }
    private void removeResourcesForSettlement() {
        int lumbers= resources.get(ResourceType.Lumber);
        int grains=resources.get(ResourceType.Grain);
        int bricks = resources.get(ResourceType.Brick);
        int wools=resources.get(ResourceType.Wool);

        resources.put(ResourceType.Lumber,lumbers-Cost.SETTLEMENT_LUMBER);
        resources.put(ResourceType.Grain,grains-Cost.SETTLEMENT_GRAIN);
        resources.put(ResourceType.Wool,wools-Cost.SETTLEMENT_WOOL);
        resources.put(ResourceType.Brick,bricks-Cost.SETTLEMENT_BRICK);

    }


    //TODO REMINDER: The GAME class verifies if the id is possessed by the player.
    public boolean canBuildCity() {
        return (cities.size() < Component.CITIES) &&
                (haveTheResourcesToBuyCity());
    }
    private boolean haveTheResourcesToBuyCity() {
        return (resources.get(ResourceType.Ore)>=Cost.CITY_ORES&&resources.get(ResourceType.Grain)>=Cost.CITY_GRAINS);
    }
    public boolean canBuildCity(Intersection city) {
        if (!canBuildCity()) { return false; }
        for (Intersection settlement:settlements){
            if(settlement.getId()==city.getId())
                return true;
        }
        return false;
    }
    //TODO REMINDER: This is used by GAME class
    public boolean buildCity(Intersection city) {
        if(!canBuildCity(city)){return false;}
        cities.add(city);
        //REMOVE SETTLEMENT
        for(Intersection settlement:settlements){
            if(settlement.getId()==city.getId())
            {
                settlements.remove(settlement);
                break;
            }
        }
        publicVP++;
        removeResourcesForCity();
        return true;
    }
    private void removeResourcesForCity() {
        int ores=resources.get(ResourceType.Ore);
        int grains=resources.get(ResourceType.Grain);

        resources.put(ResourceType.Ore,ores-Cost.CITY_ORES);
        resources.put(ResourceType.Grain,grains-Cost.CITY_GRAINS);

    }


    public boolean canMakeTrade(List<Pair<ResourceType,Integer>> offer){
        for(Pair<ResourceType,Integer> pair:offer){
            ResourceType type=pair.getKey();
            if(resources.get(type)<pair.getValue())
                return false;
        }
        return true;
    }
    //TODO: understand were to implement playerAcceptingTrade and playerWhichTrade
    public boolean makeTrade(List<Pair<ResourceType,Integer>> offer,List<Pair<ResourceType,Integer>> request){
        if(!canMakeTrade(offer)){return false;}
        List<Player> acceptingTrade=game.playerAcceptingTrade(this,offer,request);
        if(acceptingTrade.size()==0)
            return false;
        else{
            game.playerWhichTrade(this,acceptingTrade.get(0),offer,request);
            return true;
        }

    }
    public void tradeHappened(List<Pair<ResourceType,Integer>> offer,List<Pair<ResourceType,Integer>> request){
        for(Pair<ResourceType,Integer> pair:offer){
            ResourceType type=pair.getKey();
            Integer resourceCount=resources.get(type);
            resources.put(type,resourceCount-pair.getValue());
        }
        for(Pair<ResourceType,Integer> pair:request){
            ResourceType type=pair.getKey();
            Integer resourceCount=resources.get(type);
            resources.put(type,resourceCount+pair.getValue());
        }
    }




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
