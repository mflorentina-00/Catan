package catan.game.card.development;

import catan.game.card.Bank;
import catan.game.property.Intersection;
import catan.game.property.Road;

//TODO
public class RoadBuilding extends Development {
    private Bank bank;
    private Intersection start;
    private Intersection end;

    public RoadBuilding() {
        super();
        bank = null;
        start = null;
        end = null;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Intersection getStart() {
        return start;
    }

    public void setStart(Intersection start) {
        this.start = start;
    }

    public Intersection getEnd() {
        return end;
    }

    public void setEnd(Intersection end) {
        this.end = end;
    }

    // folosim metoda "use" de doua ori atunci cand jucam cartea
    @Override
    public boolean use() {
        if (owner == null || bank == null || start == null || end == null) {
            return false;
        }
        Road road = bank.getRoad(owner);
        if (road == null) {
            return false;
        }
        road.setCoordinates(start, end);
        owner.buildRoad(road);
        return true;
    }
}
