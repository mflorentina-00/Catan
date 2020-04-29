using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.IO;
using UnityEngine.UI;

public class ChangeTexture : MonoBehaviour
{
    public Transform SpawnForestOre;
    public Transform SpawnHills;
    public Transform SpawnPasture;
    public Transform SpawnDesertField;
    public GameObject objForest;
    public GameObject objPasture;
    public GameObject objBrick;
    public GameObject objOre;
    public GameObject objGrain;
    public GameObject objDesert;
    public TextMesh text;
    public int nr;
   
    // Start is called before the first frame update
    void Start()
    {
        BoardConnectivityJson boardd = SaveTable.LoadTable();
        
        string str = "";
        str = boardd.board[nr].number.ToString();
        text.text = str;
        string resource = boardd.board[nr].resource;
       
        switch (resource)
        {
            case "Lumber":
               Instantiate(objForest, SpawnForestOre.position, SpawnForestOre.rotation);
                break;
            case "Wool":

                Instantiate(objPasture, SpawnPasture.position, SpawnPasture.rotation);
                break;
            case "Ore":
                Instantiate(objOre, SpawnForestOre.position, SpawnForestOre.rotation);
                break;
            case "Grain":
                Instantiate(objGrain, SpawnPasture.position, SpawnPasture.rotation);
                break;
            case "Desert":
                Instantiate(objDesert, SpawnDesertField.position, SpawnDesertField.rotation);
                break;
            case "Brick":
                Instantiate(objBrick, SpawnHills.position, SpawnHills.rotation);
                break;

        }
        
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}