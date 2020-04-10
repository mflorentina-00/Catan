package buildings;

public class Main {
    public static void main(String[] args) {
        Player player1 = new Player("Gigel");
        Player player2 = new Player("Costel");
        Player player3 = new Player("Dorel");

        Intersection int1 = new Intersection(1);
        Intersection int2 = new Intersection(2);
        Intersection int3 = new Intersection(3);
        Intersection int4 = new Intersection(4);

        Road road1 = new Road(int1, int2);
        Road road2 = new Road(int1, int3);
        Road road3 = new Road(int3, int4);

        road1.setOwner(player1);
        road2.setOwner(player2);
        road3.setOwner(player3);

        System.out.println(road1.commonIntersection(road2).getId());
        System.out.println(road2.commonIntersection(road3).getId());
        System.out.println(road3.hasCommonIntersection(road1));
    }
}
