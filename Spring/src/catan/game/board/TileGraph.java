package catan.game.board;

import java.util.ArrayList;
import java.util.List;

public class TileGraph {
    private Tiles tiles = new Tiles(19);
    TileGraph() {

        tiles.createTiles();
        tiles.createMatrix();
        //tiles.printMatrix();
    }

    public boolean areAdjacent(int source,int target)
    {

        return  tiles.adjentTiles[source][target];

    }


    public List<Integer> getTRing(Integer index)
    {  List<Integer> retList=new ArrayList<>();
       List<Tile> list=tiles.getRing(index);

       for(int i=0;i<list.size();i++)
         retList.add(list.get(i).getID());
       return retList;


    }

}
