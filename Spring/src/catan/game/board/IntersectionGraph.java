package catan.game.board;

import java.util.ArrayList;
import java.util.List;

public class IntersectionGraph {
    private static List<List<Integer>> positions = new ArrayList<>(3);
    private static boolean[][] adjacency;

    IntersectionGraph() {
        setPositions();
        markAdjacentIntersections();
        // printAdjacency();
    }

    public static boolean areAdjacent(int source, int target) {
        return adjacency[source][target];
    }

    public static void printAdjacency() {
        for (int i = 0; i < 54; i++) {
            System.out.print(i + " : ");
            for (int j = 0; j < 54; j++)
                if (adjacency[i][j])
                    System.out.print(j + " ");
            System.out.println();
        }
    }

    private static void setPositions() {
        List<Integer> list1 = new ArrayList<>(6);
        List<Integer> list2 = new ArrayList<>(18);
        List<Integer> list3 = new ArrayList<>(30);
        for (int i = 0; i < 6; i++)
            list1.add(i);
        for (int i = 6; i < 24; i++)
            list2.add(i);
        for (int i = 24; i < 54; i++)
            list3.add(i);
        positions.add(list1);
        positions.add(list2);
        positions.add(list3);
    }

    private static void markAdjacentIntersections() {
        int noIntersections = 54;
        // matrice pentru stabilirea adiacentei dintre pozitii
        adjacency = new boolean[noIntersections][noIntersections];
        // o initializam pe toata cu false mai intai
        for (int i = 0; i < noIntersections; i++)
            for (int j = 0; j < noIntersections; j++)
                adjacency[i][j] = false;
        // for-ul este pentru iterarea prin fiecare inel
        for (int i = 0; i < 3; i++) {
            // extragem fiecare inel din lista
            List<Integer> ring = positions.get(i);
            int ringSize = ring.size();
            int difference = -1;
            int wait = 0;
            // variabila care indica daca inelul curent mai are un inel ca vecin
            boolean hasNextLink = true;
            // parcurgem inelul actual
            for (int j = 1; j <= ringSize; j++) {
                List<Integer> followingRing;
                int followingRingSize;
                // ultimul inel se invecineaza cu marea
                if (i + 1 < 3) {
                    followingRing = positions.get(i+1);
                    followingRingSize = followingRing.size();
                }
                else {
                    followingRing = null;
                    followingRingSize = 0;
                }
                int index = ring.get(j % ringSize);
                int neighbourIndex = ring.get((j + 1) % ringSize);
                // ne ocupam de adiacenta celor de pe acelasi inel
                adjacency[index][neighbourIndex] = true;
                adjacency[neighbourIndex][index] = true;
                // urmeaza adiacenta cu intersectia de pe celalalt inel
                if (hasNextLink) {
                    int index1;
                    if (j + difference < followingRingSize) {
                        assert followingRing != null;
                        index1 = followingRing.get(j + difference);
                        adjacency[index][index1] = true;
                        adjacency[index1][index] = true;
                    }
                    if (wait == 0) {
                        wait = i;
                        difference = difference + 2;
                    }
                    else {
                        wait = wait - 1;
                        hasNextLink = false;
                    }
                }
                else {
                    hasNextLink = true;
                }
            }
        }
    }

    public static List<Integer> getPositions(Integer index) {
        return positions.get(index);
    }
}
