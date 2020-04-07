using UnityEngine;

public class HexCell : MonoBehaviour
{
    public HexCoordinates coordinates;
    [SerializeField]
    HexCell[] neighbors;

    public HexCell[] GetAllNeighbors()
    {
        return this.neighbors;
    }

    public HexCell GetNeighbor(HexDirection direction)
    {
        return neighbors[(int)direction];
    }

    public void SetNeighbor(HexDirection direction, HexCell cell)
    {
        neighbors[(int)direction] = cell;
        //We set the neighbor for the opposite tile too
        cell.neighbors[(int)direction.Opposite()] = this;
    }
}
