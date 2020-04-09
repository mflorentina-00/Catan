public class RoadBuilding extends DevelopmentCards {

    public RoadBuilding(Player owner){
    this.owner = owner;
    }

    @Override
    public void Action() {
        owner.addRoads(/*coordonate*/);
        owner.addRoads(/*coordonate*/);
    }
}
