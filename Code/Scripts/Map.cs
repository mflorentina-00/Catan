using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System.Threading;
using System.IO;
using UnityEngine.UI;
using System;
using FullSerializer;
using Proyecto26;

public class Map : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        ReceiveBoardScript recive = new ReceiveBoardScript();
        recive.RequestLobbyidAndGameid();
        /*
        string path = "board.json";
        byte[] byteArray = File.ReadAllBytes(path);
        string result = System.Text.Encoding.UTF8.GetString(byteArray);
        File.Delete(path);
        
        BoardConnectivityJson table = JsonUtility.FromJson<BoardConnectivityJson>(result);

        SaveTable.saveTable(table);

        BoardConnectivityJson boardd = SaveTable.LoadTable();
        */

    }


    // Update is called once per frame
    void Update()
    {
        foreach (string ports in ReceiveBoardScript.ReceivedBoard.ports)
        {
            if (ports == null)
            {
                Debug.Log("Misule");
            }
            else
            {
                Debug.Log(ports);
            }
        }
    }

}
