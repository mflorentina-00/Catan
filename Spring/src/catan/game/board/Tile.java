package catan.game.board;

import catan.game.enumeration.ResourceType;

import java.util.List;

public class Tile {
    private int ID;
    private ResourceType resource;
    private int number;
    private List<Tile> neighbours;
    
    Tile(int ID){
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public List<Tile> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(List<Tile> neighbours) {
        this.neighbours = neighbours;
    }
    
    public void addNeighbour(Tile neighbour){
        this.neighbours.add(neighbour);
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public ResourceType getResource() {
        return resource;
    }

    public void setResource(ResourceType resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "ID=" + ID +
                ", resource=" + resource +
                ", number=" + number +
                ", neighbours=" + neighbours +
                '}';
    }
}
