using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.IO;
using UnityEngine.UI;
using System;
using FullSerializer;
using Proyecto26;

[System.Serializable]
public class chestii
{
    public List<string> ports = new List<string>();
    public List<hexagon> board= new List<hexagon>();

}

public class hexagon
{
    public string resource;
    public int number;
}

public class Map : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        
        ReceiveBoardScript recive = new ReceiveBoardScript();
        recive.RequestLobbyidAndGameid();

        string path = "board.json";
        byte[] byteArray = File.ReadAllBytes(path);
        string result = System.Text.Encoding.UTF8.GetString(byteArray);
        File.Delete(path);
        BoardConnectivityJson table = JsonUtility.FromJson<BoardConnectivityJson>(result);

       /* foreach (var x in table.board) {
            Debug.Log(x.number);
            Debug.Log(x.resource);
        }
        foreach (var x in table.ports)
        {
            Debug.Log(x);
        }
        //Read the text from directly from the test.txt file

    */
    }




    public void getData()
    {
       // ReceiveBoardScript recive = new ReceiveBoardScript();
       // recive.RequestLobbyidAndGameid();
        //string st = recive.stringBoard;
       // File.WriteAllText("Board.json", st);


    }
    // Update is called once per frame
    void Update()
    {

    }

}
