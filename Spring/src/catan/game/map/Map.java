package catan.game.map;

import catan.game.enumeration.ResourceType;
import catan.game.rule.Component;

import java.util.*;

//TODO Add TileGraph
//TODO Add ResourceShuffle
//TODO Generate MapJSON
public class Map {
    Vector<Tile> tiles;

    public Map() {
        tiles = new Vector<>();
        generateRandomTiles();
    }

    public void generateRandomTiles(){
        for(int i=0;i<Component.TILES;i++)
            tiles.add(new Tile(i));
        List<ResourceType> resourcesList = new ArrayList<>();

        for(int i=0;i< Component.FIELDS_TILE;i++)
            resourcesList.add(ResourceType.Grain);
        for(int i=0;i< Component.FOREST_TILE;i++)
            resourcesList.add(ResourceType.Lumber);
        for(int i=0;i< Component.PASTURE_TILE;i++)
            resourcesList.add(ResourceType.Wool);
        for(int i=0;i< Component.MOUNTAINS_TILE;i++)
            resourcesList.add(ResourceType.Ore);
        for(int i=0;i< Component.HILLS_TILE;i++)
            resourcesList.add(ResourceType.Brick);
        for(int i=0;i< Component.DESERT_TILE;i++)
            resourcesList.add(ResourceType.Desert);
        Collections.shuffle(resourcesList);
        int i=0;
        for (Tile tile:
                tiles) {
            tile.setResource(resourcesList.get(i));
            i++;
        }
        //TODO make 6 and 8 not near each other
        Integer[] intArray = { 2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10,11,11,12 };
        List<Integer> intList = Arrays.asList(intArray);
        Collections.shuffle(intList);
        i=0;
        for (Tile tile:
                tiles) {
            if(tile.getResource()!=ResourceType.Desert)
            {
                tile.setNumber(intList.get(i));
                i++;
            }
        }

    }
}
