using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class IntersectionCoordinates : MonoBehaviour
{
    private int x, z;
    private CardinalDirection direction;
    
    public int X
    {
        get
        {
            return x;
        }
    }
    public int Z
    {
        get
        {
            return z;
        }
    }

    public CardinalDirection Direction
    {
        get { return direction; }
    }
    
    public IntersectionCoordinates(int x, int z,CardinalDirection direction)
    {
        this.x = x;
        this.z = z;
        this.direction = direction;
    }
    
    public static bool operator==(IntersectionCoordinates obj1,IntersectionCoordinates obj2)
    {
        return ((obj1.X == obj2.X) && (obj1.Z == obj2.Z) && (obj1.Direction == obj2.Direction));
    }
    
    public static bool operator!= (IntersectionCoordinates obj1,IntersectionCoordinates obj2)
    {
        return !(obj1 == obj2);
    }
    
    public override bool Equals(object obj)
    {
        return this.GetHashCode() == obj.GetHashCode();
    }
    
    public override int GetHashCode()
    {
        return this.X.GetHashCode() + this.Z.GetHashCode() + (int)this.Direction;
    }
}