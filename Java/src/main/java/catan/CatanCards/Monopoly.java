public class Monopoly extends DevelopmentCards {
    // Lobby lobby
    public Monopoly(Player owner/*, lobby pentru ceilalti jucatori*/)
    {
        this.owner = owner;
    }

    @Override
    public void Action() {
        Resources res;
        res = new Resources(/*tip*/);
        // foreach player in lista de playeri din lobby
        // daca player-ul nu este player-ul curent
        // scadem toate resursele de tipul res
        // adaugam numarul respectiv de resurse la player-ul curent
    }
}
