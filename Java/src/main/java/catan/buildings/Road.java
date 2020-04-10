package buildings;

import java.util.ArrayList;
import java.util.List;

public class Road {
    private Player owner;
    private List<Intersection> coordinates = new ArrayList<Intersection>();

    public Road(Intersection start, Intersection end){
        coordinates.add(start);
        coordinates.add(end);
    }

    public Road(){

    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public List<Intersection> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Intersection> coordinates) {
        this.coordinates = coordinates;
    }

    public Intersection getStart(){
        return coordinates.get(0);
    }

    public Intersection getEnd(){
        return coordinates.get(1);
    }

    public Intersection commonIntersection(Road road){
        if(!((this.getStart() == road.getStart()) && (this.getEnd() == road.getEnd()))) {
            if (this.getEnd() == road.getStart() || this.getEnd() == road.getEnd())
                return this.getEnd();
            if (this.getStart() == road.getStart() || this.getStart() == road.getEnd())
                return this.getStart();
        }

        return null;

    }

    public boolean hasCommonIntersection(Road road){
       if(commonIntersection(road)!=null)
            return true;
       else
        return false;
    }
}
