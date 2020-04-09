public class YearOfPlenty extends DevelopmentCards {
    Bank bank;
    public YearOfPlenty(Player owner, Bank bank){
        this.owner = owner;
        this.bank = bank;
    }

    @Override
    public void Action() {
        owner.addResources(bank.getResources());
        owner.addResources(bank.getResources());
    }
}
