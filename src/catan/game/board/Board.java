package catan.game.board;

import catan.game.enumeration.PortType;
import catan.game.enumeration.ResourceType;
import catan.game.property.Intersection;
import catan.game.rule.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.util.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Board {
    private List<Tile> tiles = new ArrayList<>();
    private List<List<Tile>> numberedTiles = new ArrayList<>();
    private List<Intersection> buildings = new ArrayList<>();
    private TileGraph tileGraph = new TileGraph();
    private IntersectionGraph intersectionGraph = new IntersectionGraph();
    private ArrayList<ArrayList<Integer>> tileBuildingAdjacency = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> buildingTileAdjacency = new ArrayList<>();
    private List<PortType> ports = new ArrayList<>();
    private Tile robberPosition = null;

    public Board() {
        for (int i = 0; i < Component.TILES; i++) {
            tiles.add(new Tile(i));
        }
        for (int i = 0; i < Component.INTERSECTIONS; i++) {
            buildings.add(new Intersection(i));
        }
        for (int index = 0; index < Component.INTERSECTIONS; ++index) {
            ports.add(PortType.None);
        }
        generateRandomTiles();
        createMapping();
        setPorts();
        printTileBuildingAdjacency();
        printBuildingTileAdjacency();
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public List<List<Tile>> getNumberedTiles() {
        return numberedTiles;
    }

    public List<Tile> getTilesFromNumber(int i) {
        return numberedTiles.get(i);
    }

    public List<Intersection> getBuildings() {
        return buildings;
    }

    public void addBuilding(Intersection building) {
        buildings.add(building);
    }

    public TileGraph getTileGraph() {
        return tileGraph;
    }

    public IntersectionGraph getIntersectionGraph() {
        return intersectionGraph;
    }

    public ArrayList<ArrayList<Integer>> getTileBuildingAdjacency() {
        return tileBuildingAdjacency;
    }

    public ArrayList<ArrayList<Integer>> getBuildingTileAdjacency() {
        return buildingTileAdjacency;
    }

    public List<PortType> getPorts() {
        return ports;
    }

    public Tile getRobberPosition() {
        return robberPosition;
    }

    public void setRobberPosition(Tile robberPosition) {
        this.robberPosition = robberPosition;
    }

    public List<Intersection> getIntersectionsFromTile(Tile tile) {
        List<Intersection> intersections = new ArrayList<>();
        List<Integer> intersectionsId = tileBuildingAdjacency.get(tile.getId());
        for (Integer id : intersectionsId) {
            intersections.add(buildings.get(id));
        }
        return intersections;
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
            if (tile.getResource() == ResourceType.Desert) {
                setRobberPosition(tile);
            }
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
                else {
                    tile.setNumber(0);
                }
            }
            for (Tile tile : tiles) {
                if (tile.getNumber() == 6 || tile.getNumber() == 8) {
                    List<Integer> neighbors = tileGraph.getNeighborTiles(tile.getId());
                    for (Integer neighbor : neighbors) {
                        if (tile.getNumber() + tiles.get(neighbor).getNumber() == 14) {
                            sixNearEight = true;
                            break;
                        }
                    }
                }
            }
        }

        for (int i = 0; i <= 12; i++) {
            numberedTiles.add(new ArrayList<>());
        }
        for (Tile tile : tiles) {
            numberedTiles.get(tile.getNumber()).add(tile);
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

    public void setPorts() {
        int[] frequency = {4, 1, 1, 1, 1, 1};
        int counter = 0;
        int max = 5;
        int min = 0;
        int random;
        int sum;
        int addValue = 0;
        int index = 26;
        while (index < 54) {
            sum = 0;
            counter++;
            if (counter == 1 || counter == 3 || counter == 5 || counter == 6 || counter == 7 || counter == 8)
                addValue = 3;
            if (counter == 2 || counter == 4)
                addValue = 4;
            random = (int) (Math.random() * ((max - min) + 1)) + min;
            for (int value : frequency) sum += value;
            if (sum != 0) {
                while (frequency[random] <= 0) {
                    random = (int) (Math.random() * ((max - min) + 1)) + min;
                }
                ports.add(index, PortType.values()[random]);
                int nextIndex = index + 1;
                ports.add(nextIndex, PortType.values()[random]);
                frequency[random]--;
            }
            index += addValue;
        }
    }

    public String getBoardJson() {
        List<Pair<ResourceType, Integer>> tilesInformation = new ArrayList<>();
        for (Tile tile : tiles) {
            tilesInformation.add(new Pair<>(tile.getResource(), tile.getNumber()));
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String boardJSON = objectMapper.writeValueAsString(tilesInformation);
            return boardJSON.replaceAll("key", "resource")
                    .replaceAll("value", "number");
        } catch (JsonProcessingException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public String getPortsJson() {
        try {
            return new ObjectMapper().writeValueAsString(ports);
        } catch (IOException exception) {
            exception.printStackTrace();
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
}
