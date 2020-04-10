package com.company;


import java.util.ArrayList;
import java.util.List;

public class Main {
    public static List<List<Integer>> Positions=new ArrayList<>(3);
    public static boolean[][] adjacency;

    public static void main(String[] args) {




       buildPositions();
        System.out.println(Positions);
      adjacentIntersections();
        for(int i=0;i<54;i++){
            for(int j=0;j<54;j++)
                System.out.print(adjacency[i][j] + " ");
            System.out.println(" ");
        }






    };

    public static  void buildPositions(){

        List<Integer> l1=new ArrayList<>(6);
        List<Integer> l2=new ArrayList<>(18);
        List<Integer> l3=new ArrayList<>(30);
        for(int i=0;i<6;i++)
         l1.add(new Integer(i));

        for(int i=6;i<24;i++)
            l2.add(new Integer(i));
        for(int i=24;i<54;i++)
            l3.add(new Integer(i));

        List<List<Integer>> lista=new ArrayList<>();
        lista.add(l1);
        lista.add(l2);
        lista.add(l3);


      Positions=lista;



    };


    public static void adjacentIntersections()
    { int numIntersections=54;
        //matrice pentru stabilirea adiacentei dintre pozitii
        adjacency=new boolean[numIntersections][numIntersections];

        //o initializam pe toata cu false mai intai
        for(int i=0;i<numIntersections;i++)
            for (int j=0;j<numIntersections;j++)
                adjacency[i][j]=false;

        //Forul este pentru iterarea prin fiecare inel
        for(int i=0;i<3;i++)
        {
          //extragem fiecare inel din lista
         List<Integer> ring=Positions.get(i);
         int ringSize=ring.size();

         int difference=-1;
         int wait=0;
         //variabila care indica daca inelul curent mai are un inel ca vecin
         boolean linkToNext=true;

         //parcurgem inelul actual
            for(int j=1;j<=ringSize;j++)
            {

                List<Integer> followingRing;
                int followingRingSize;

                //ultimul inel se invecineaza cu marea
                if(i+1<3)
                {
                    followingRing=Positions.get(i+1);
                    followingRingSize=followingRing.size();
                }
                else{
                    followingRing=null;
                    followingRingSize=0;
                }

                int index=ring.get(j%ringSize);
                int neighbourIndex=ring.get((j+1)%ringSize);

                //ne ocupam de adiacenta celor de pe acelasi inel
                adjacency[index][neighbourIndex]=true;
                adjacency[neighbourIndex][index]=true;

                //urmeaza adiacenta cu intersectia de pe celalalt inel
                if(linkToNext==true)
                {int index1=0;
                   if(j+difference<followingRingSize)
                    {index1=followingRing.get(j+difference);
                    /*
                    //pentru debug
                    System.out.println("Current index "  +index);
                    System.out.println(index1);
                    System.out.println("j:" +j);
                    System.out.println("Diferenta "+difference);

                    */
                    adjacency[index][index1]=true;

                    adjacency[index1][index]=true;}
                    if(wait==0)
                    {
                        wait=i;
                        difference=difference+2;

                    }
                    else {
                        wait=wait-1;
                        linkToNext=false;


                    }
                }
                else {
                    linkToNext=true;
                }


            }



        }







    }





}
