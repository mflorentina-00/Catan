package catan.game.board;

import catan.game.enumeration.ResourceType;
import catan.game.property.Intersection;
import catan.game.rule.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import javafx.util.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Board {
    private List<Intersection> buildings;
    private List<Tile> tiles;
    private TileGraph tileGraph = new TileGraph();
    private IntersectionGraph intersectionGraph = new IntersectionGraph();
    private ArrayList<ArrayList<Integer>> tileBuildingAdjacency;
    private ArrayList<ArrayList<Integer>> buildingTileAdjacency;

    public Board() {
        buildings = new ArrayList<>();
        tiles = new ArrayList<>();
        for (int i = 0; i < Component.TILES; i++)
            tiles.add(new Tile(i));
        tileBuildingAdjacency = new ArrayList<>(Component.TILES);
        buildingTileAdjacency = new ArrayList<>(Component.INTERSECTIONS);
        generateRandomTiles();
        createMapping();
        printTileBuildingAdjacency();
        printBuildingTileAdjacency();
        printBoardJSON();
    }

    public List<Intersection> getBuildings() {
        return buildings;
    }

    public void setBuildings(List<Intersection> buildings) {
        this.buildings = buildings;
    }

    public void addBuilding(Intersection building) {
        buildings.add(building);
    }

    public void generateRandomTiles() {
        generateResources();
        generateNumbers();
    }

    private void generateResources() {
        List<ResourceType> resources = new ArrayList<>();

        for (int i = 0; i < Component.FIELD_TILES; i++)
            resources.add(ResourceType.Grain);
        for (int i = 0; i < Component.FOREST_TILES; i++)
            resources.add(ResourceType.Lumber);
        for (int i = 0; i < Component.PASTURE_TILES; i++)
            resources.add(ResourceType.Wool);
        for (int i = 0; i < Component.MOUNTAINS_TILES; i++)
            resources.add(ResourceType.Ore);
        for (int i = 0; i < Component.HILLS_TILES; i++)
            resources.add(ResourceType.Brick);
        for (int i = 0; i < Component.DESERT_TILES; i++)
            resources.add(ResourceType.Desert);
        Collections.shuffle(resources);

        int i = 0;
        for (Tile tile : tiles) {
            tile.setResource(resources.get(i++));
        }
    }

    private void generateNumbers() {
        boolean sixNearEight = true;
        while (sixNearEight) {
            sixNearEight = false;
            Integer[] numberArray = {2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12};
            List<Integer> numberList = Arrays.asList(numberArray);
            Collections.shuffle(numberList);
            int i = 0;
            for (Tile tile : tiles) {
                if (tile.getResource() != ResourceType.Desert) {
                    tile.setNumber(numberList.get(i));
                    i++;
                }
            }
            for (Tile tile : tiles) {
                if (tile.getNumber() == 6 || tile.getNumber() == 8) {
                    List<Integer> neighbors = tileGraph.getNeighborTiles(tile.getID());
                    for (Integer neighbor : neighbors) {
                        if (tile.getNumber() + tiles.get(neighbor).getNumber() == 14) {
                            sixNearEight = true;
                            break;
                        }
                    }
                }
            }
        }
    }

    private void createMapping() {
        for (int i = 0; i < Component.TILES; i++)
            tileBuildingAdjacency.add(new ArrayList<>(6));

        for (int i = 0; i < Component.INTERSECTIONS; i++)
            buildingTileAdjacency.add(new ArrayList<>(3));

        for (int ring = 0; ring < 3; ring++) {
            List<Integer> tileRing = tileGraph.getRing(ring);
            int tileRingSize = tileRing.size();

            List<Integer> intersectionRing = intersectionGraph.getRing(ring);
            int intersectionRingSize = intersectionRing.size();

            List<Integer> beforeIntersectionRing;
            int previousIntersectionRingSize;
            if (ring > 0) {
                beforeIntersectionRing = intersectionGraph.getRing(ring - 1);
                previousIntersectionRingSize = beforeIntersectionRing.size();
            } else {
                beforeIntersectionRing = null;
                previousIntersectionRingSize = 0;
            }

            int iIndex1 = 0;
            int iIndex2 = 1;
            for (int tile = 0; tile < tileRingSize; tile++) {
                int iIndex3 = 3;
                int iIndex4 = iIndex3;
                if (ring != 0) {
                    boolean corner = ((tile % ring) == 0);
                    if (corner) {
                        iIndex3++;
                        iIndex4--;
                    }
                }

                for (int k = 0; k < iIndex3; k++) {
                    tileBuildingAdjacency.get(tileRing.get(tile)).add(intersectionRing.get(iIndex1));
                    buildingTileAdjacency.get(intersectionRing.get(iIndex1)).add(tileRing.get(tile));
                    if (k + 1 < iIndex3) {
                        iIndex1 = (iIndex1 + 1) % intersectionRingSize;
                    }
                }

                if (beforeIntersectionRing != null) {
                    for (int k = 0; k < iIndex4; k++) {
                        tileBuildingAdjacency.get(tileRing.get(tile)).add(beforeIntersectionRing.get(iIndex2));
                        buildingTileAdjacency.get(beforeIntersectionRing.get(iIndex2)).add(tileRing.get(tile));
                        if (k + 1 < iIndex4) {
                            iIndex2 = (iIndex2 + 1) % previousIntersectionRingSize;
                        }
                    }
                }
            }
        }
        tileBuildingAdjacency.get(0).add(3);
        tileBuildingAdjacency.get(0).add(4);
        tileBuildingAdjacency.get(0).add(5);
        buildingTileAdjacency.get(3).add(0);
        Collections.sort(buildingTileAdjacency.get(3));
        buildingTileAdjacency.get(4).add(0);
        Collections.sort(buildingTileAdjacency.get(4));
        buildingTileAdjacency.get(5).add(0);
        Collections.sort(buildingTileAdjacency.get(5));
    }

    public String getBoardJSON() {
        List<Pair<ResourceType, Integer>> tilesInformation = new ArrayList<>();
        for (Tile tile : tiles) {
            tilesInformation.add(new Pair<>(tile.getResource(), tile.getNumber()));
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy();
            String boardJSON = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tilesInformation);
            return boardJSON.replaceAll("key", "resource")
                    .replaceAll("value", "number");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void printTileBuildingAdjacency() {
        try {
            FileWriter fileWriter = new FileWriter("resources/TileBuildingAdjacency.txt");
            for (int i = 0; i < Component.TILES; i++) {
                fileWriter.write(i + " : ");
                for (int j = 0; j < tileBuildingAdjacency.get(i).size(); j++)
                    fileWriter.write(tileBuildingAdjacency.get(i).get(j) + " ");
                fileWriter.write('\n');
            }
            fileWriter.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void printBuildingTileAdjacency() {
        try {
            FileWriter fileWriter = new FileWriter("resources/BuildingTileAdjacency.txt");
            for (int i = 0; i < Component.INTERSECTIONS; i++) {
                fileWriter.write(i + " : ");
                for (int j = 0; j < buildingTileAdjacency.get(i).size(); j++)
                    fileWriter.write(buildingTileAdjacency.get(i).get(j) + " ");
                fileWriter.write('\n');
            }
            fileWriter.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void printBoardJSON() {
        try {
            FileWriter fileWriter = new FileWriter("resources/Board.json");
            fileWriter.write(getBoardJSON());
            fileWriter.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
