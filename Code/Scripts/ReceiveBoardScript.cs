using System.Collections;
using System.Collections.Generic;
using UnityEngine.UI;
using UnityEditor;
using UnityEngine;
using FullSerializer;
using Proyecto26;
using UnityEngine.SceneManagement;

public class ReceiveBoardScript : MonoBehaviour
{
    public Hexagon[] RequestBoard(int lobbyid)
    {
        string URL = "http://localhost:5000/board";
        Lobby lobby = new Lobby(lobbyid);
        RestClient.PostArray<Hexagon>(URL,lobby).Then(board => {
            foreach(Hexagon hexagon in board)
            {
                Debug.Log(hexagon.resource + " " + hexagon.number);
            }
            return board;
        }).Catch(err => { Debug.Log(err); });
       return null;
    }

}
