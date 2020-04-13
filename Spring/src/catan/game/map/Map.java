package catan.game.map;

import catan.game.enumeration.ResourceType;
import catan.game.rule.Component;

import java.util.*;

//TODO Add TileGraph
//TODO Add ResourceShuffle
//TODO Generate MapJSON
public class Map {
    Vector<Tile> tiles;
    IntersectionGraph iG = new IntersectionGraph();
    TileGraph tG = new TileGraph();
    private static ArrayList<ArrayList<Integer>> tileMap;
    private static ArrayList<ArrayList<Integer>> intersectionMap;

    public Map() {
        tiles = new Vector<>();
        generateRandomTiles();
        mapingTilesWithIntersections();
    }

    public void generateRandomTiles() {
        for (int i = 0; i < Component.TILES; i++)
            tiles.add(new Tile(i));
        List<ResourceType> resourcesList = new ArrayList<>();

        for (int i = 0; i < Component.FIELDS_TILE; i++)
            resourcesList.add(ResourceType.Grain);
        for (int i = 0; i < Component.FOREST_TILE; i++)
            resourcesList.add(ResourceType.Lumber);
        for (int i = 0; i < Component.PASTURE_TILE; i++)
            resourcesList.add(ResourceType.Wool);
        for (int i = 0; i < Component.MOUNTAINS_TILE; i++)
            resourcesList.add(ResourceType.Ore);
        for (int i = 0; i < Component.HILLS_TILE; i++)
            resourcesList.add(ResourceType.Brick);
        for (int i = 0; i < Component.DESERT_TILE; i++)
            resourcesList.add(ResourceType.Desert);
        Collections.shuffle(resourcesList);
        int i = 0;
        for (Tile tile :
                tiles) {
            tile.setResource(resourcesList.get(i));
            i++;
        }
        //TODO make 6 and 8 not near each other
        boolean sixNotAdjacentWithEight=true;
        while(sixNotAdjacentWithEight==true){
            sixNotAdjacentWithEight=false;
        Integer[] intArray = {2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12};
        List<Integer> intList = Arrays.asList(intArray);
        Collections.shuffle(intList);
        i = 0;
        for (Tile tile :
                tiles) {
            if (tile.getResource() != ResourceType.Desert) {
                tile.setNumber(intList.get(i));
                i++;
            }
        }

        for (Tile tile : tiles) {
            if (tile.getNumber() == 6) {
                int nrOrd = tile.getID();
                for (int t = 0; t < 19; t++)
                    if (tG.areAdjacent(nrOrd, tiles.get(t).getID()) == true && tiles.get(t).getNumber() == 8) {
                         sixNotAdjacentWithEight=true;
                    }

                if (tile.getNumber() == 8) {
                    int nrOrd1 = tile.getID();
                    for (int j = 0; j < 19; j++)
                        if (tG.areAdjacent(nrOrd1, tiles.get(j).getID()) == true && tiles.get(j).getNumber() == 6) {
                           sixNotAdjacentWithEight=true;
                        }
                }


            }


        }
    }
}

private void mapingTilesWithIntersections() {
    tileMap = new ArrayList<ArrayList<Integer>>(19);
    intersectionMap = new ArrayList<ArrayList<Integer>>(54);
    //Acordam spatiu pentru fiecare mapa
    for (int i = 0; i < 19; i++)
        tileMap.add(new ArrayList<Integer>(6));


    for (int i = 0; i < 54; i++)
        intersectionMap.add(new ArrayList<Integer>(3));


    for (int i = 0; i < 3; i++) {

        List<Integer> tileRing = tG.getTRing(i);
        List<Integer> intersectionRing = iG.getPositions(i);
        int tileRingSize = tileRing.size();
        int intersectionRingSize = intersectionRing.size();
        List<Integer> beforeIntersectionRing;
        int beforeIntersectionRingSize;

        if (i > 0) {
            beforeIntersectionRing = iG.getPositions(i - 1);
            beforeIntersectionRingSize = beforeIntersectionRing.size();
        } else {
            beforeIntersectionRing = null;
            beforeIntersectionRingSize = 0;
        }


        int iIndex1 = 0;
        int iIndex2 = 1;

        for (int j = 0; j < tileRingSize; j++) {
            //cand se lucreaza cu primul inel de tile-uri,acesta avand doar un singur tile
            if (tileRingSize == 1) {
                for (int k = 0; k < tileRingSize; k++) {
                    tileMap.get(tileRing.get(j)).add(intersectionRing.get(k));
                    intersectionMap.get(intersectionRing.get(k)).add(tileRing.get(j));
                }

            }

            int iIndex3 = 3;
            int iIndex4 = iIndex3;
            boolean corner = ((j % i) == 0);
            if (corner) {
                iIndex3++;
                iIndex4--;
            }


            for (int k = 0; k < iIndex3; k++) {
                tileMap.get(tileRing.get(j)).add(intersectionRing.get(iIndex1));
                intersectionMap.get(intersectionRing.get(iIndex1)).add(tileRing.get(j));
                if (k + 1 < iIndex3) {
                    iIndex1 = (iIndex1 + 1) % intersectionRingSize;
                }
            }
            for (int k = 0; k < iIndex4; k++) {
                tileMap.get(tileRing.get(j)).add(beforeIntersectionRing.get(iIndex2));
                intersectionMap.get(beforeIntersectionRing.get(iIndex2)).add(tileRing.get(j));
                if (k + 1 < iIndex4) {
                    iIndex2 = (iIndex2 + 1) % beforeIntersectionRingSize;
                }
            }


        }
    }

}

}
