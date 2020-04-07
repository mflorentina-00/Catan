using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Road : MonoBehaviour
{
    private Player owner;

    public Player Owner
    {
        get { return owner;}
        set { owner = value; }
    }
    public bool isOwned()
    {
        if (owner == null)
            return false;
        return true;
    }
    

}
