using System;
using UnityEngine;

public class Road : MonoBehaviour
{
    private Player owner = null;
    private Tile firstTile = null;
    private Tile secondTile = null;
    private Tuple<IntersectionCoordinates, IntersectionCoordinates> intersectionPair;
    
    public Player Owner { get => owner; set => owner = value; }
        
    public Tile FirstTile { get => firstTile; set => firstTile = value; }
    
    public Tile SecondTile { get => secondTile; set => secondTile = value; }
    
    public bool isOwned()
    {
        if (owner == null)
            return false;
        return true;
    }

    public Tuple<IntersectionCoordinates, IntersectionCoordinates> IntersectionPair
    {
        get { return intersectionPair;}
        set { intersectionPair = value; }
    }
    
    public IntersectionCoordinates getStart()
    {
        return intersectionPair.Item1;
    }
    
    public IntersectionCoordinates getEnd()
    {
        return intersectionPair.Item2;
    }

    public IntersectionCoordinates commonIntersection(Road road)
    {
        if (road.getStart() == this.getStart() && road.getEnd() == this.getEnd())
        {
            return null;
        }
        
        if (road.getStart() == this.getStart() || road.getEnd() == this.getStart()) 
        {
            return this.getStart();
        }

        if (road.getStart() == this.getEnd() || road.getEnd() == this.getEnd())
        {
            return this.getEnd();
        }

        return null;
    }
    
    public bool hasCommonIntersection(Road road)
    {
        if (commonIntersection(road) == null)
            return false;
        return true;
    }
}