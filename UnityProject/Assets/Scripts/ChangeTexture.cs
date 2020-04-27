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
        
        string str = "";
        str = getNumber(nr);
        text.text = str;
        string resource=getResource(nr);
       
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
             
                break;
            case "Desert":
             
                break;
            case "Brick":
                Instantiate(objBrick, SpawnHills.position, SpawnHills.rotation);
                break;

        }
        
    }

    public string getResource(int index)
    {
        string st = File.ReadAllText("board.json");
        st = trim(st);
        int k=0;
        string[] words = st.Split(' ');
        for (int i = 0; i < words.Length; i++)
        {
            if (words[i] == "resource")
            {
               
                if(index==k)
                return(words[i + 1]);
                k++;
            }
            
        }

        return "No tile";

    }

    public string getNumber(int index)
    {
        string st = File.ReadAllText("board.json");
        st = trim(st);
        int k = 0;
        string[] words = st.Split(' ');
        for (int i = 0; i < words.Length; i++)
        {
            if (words[i] == "number")
            {
               
                if (index == k)
                    return (words[i + 1]); 
                k++;
            }

        }
        return "No tile";
    }

    public string trim(string st)
    {
        var sb = "";
        foreach (char c in st)
        {
            if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')|| c == ' '||c=='\'')
            {
                sb = sb + c;
            }
        }
        return (sb.ToString());
    }


    // Update is called once per frame
    void Update()
    {
        
    }
}