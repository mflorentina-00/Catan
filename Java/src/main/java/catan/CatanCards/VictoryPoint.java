public class VictoryPoint extends DevelopmentCards {
    public VictoryPoint(Player owner){
        this.owner = owner;
    }

    @Override
    public void Action() {
        owner.addVP(1);
    }
}
