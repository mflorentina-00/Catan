using System.Runtime.CompilerServices;
using UnityEngine;

[System.Serializable]
public struct HexCoordinates
{
    [SerializeField]
    private int x, z;


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

    public HexCoordinates(int x, int z)
    {
        this.x = x;
        this.z = z;
    }
    public int Y
    {
        get
        {
            return -X - Z;
        }
    }

    public static HexCoordinates FromOffsetCoordinates(int x, int z)
    {
        return new HexCoordinates(x - z / 2, z);
    }
    public Vector2 FromAxialToOffset()
    {
        return new Vector2(x+z/2,z);
    }
    public override string ToString()
    {
        return "(" + X.ToString() + ", " + Z.ToString() + ")";
    }
    public string ToStringCube()
    {
       
        return "(" + X.ToString() + ", " + Y.ToString() + ", " + Z.ToString() + ")";
    }

    public string ToStringOnSeparateLines()
    {
        return X.ToString() + "\n" + Z.ToString();
    }
    public string ToStringCubeOnSeparateLines()
    {
        return X.ToString() + "\n" + Y.ToString() + "\n" + Z.ToString();
    }

    public Vector3 FromCordsToPosition()
    {
        int x =(int) FromAxialToOffset().x;
        int z = (int) FromAxialToOffset().y;
        float X1 = (x + z * 0.5f - z / 2) * (HexMetrics.innerRadius * 2f);
        float Z1 = z * (HexMetrics.outerRadius * 1.5f);
        Vector3 position = new Vector3(X1, 0, Z1);
        return position;
    }

    public static HexCoordinates FromPosition(Vector3 position)
    {
        float x = position.x / (HexMetrics.innerRadius * 2f);
        float y = -x;
        float offset = position.z / (HexMetrics.outerRadius * 3f);
        x -= offset;
        y -= offset;
        int iX = Mathf.RoundToInt(x);
        int iY = Mathf.RoundToInt(y);
        int iZ = Mathf.RoundToInt(-x - y);
        if (iX + iY + iZ != 0)
        {
            float dX = Mathf.Abs(x - iX);
            float dY = Mathf.Abs(y - iY);
            float dZ = Mathf.Abs(-x - y - iZ);

            if (dX > dY && dX > dZ)
            {
                iX = -iY - iZ;
            }
            else if (dZ > dY)
            {
                iZ = -iX - iY;
            }
        }
        return new HexCoordinates(iX, iZ);
    }
}