package catan.game.board;

import catan.game.enumeration.ResourceType;

import java.util.List;

public class Tile {
    private int id;
    private ResourceType resource;
    private int number;
    private List<Tile> neighbours;
    
    Tile(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
                "ID=" + id +
                ", resource=" + resource +
                ", number=" + number +
                ", neighbours=" + neighbours +
                '}';
    }
}
