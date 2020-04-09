import java.util.ArrayList;
import java.util.List;

public class Player {
    public int VictoryPoints;
    public int roads;
    public List<Resources> resourcesList = new ArrayList<>();
    public void addVP(int VP ){
        VictoryPoints+=VP;
    }
    public void addResources(Resources res)
    {
        resourcesList.add(res);
    }
    public void addRoads(/*coordonate*/){

    }
}
