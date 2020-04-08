public class Tile : HexCell
{
    private Resource resource = Resource.Desert;
    private int number;
    private bool hasRobber;
    
    public Resource Resource { get => resource; set => resource = value; }

    public int Number { get => number; set => number = value; }

    public bool HasRobber { get => hasRobber; set => hasRobber = value; }
}