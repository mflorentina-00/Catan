using System.Collections;
using System.Collections.Generic;
using System.Threading;
using UnityEngine.UI;
using UnityEditor;
using UnityEngine;
using FullSerializer;
using Proyecto26;
using UnityEngine.SceneManagement;
using System.IO;
using System.Text;

[System.Serializable]
public class ReceiveBoardScript
{
    public static BoardConnectivityJson ReceivedBoard = new BoardConnectivityJson();

    public static string stringBoard;

    public void GetString(string user)
    {
        ReceiveBoardScript.stringBoard = user;
    }

    public void getGameBoard(string ReceivedGameID)
    {
        string board2 = "";
        GameIDConnectivityJson gameid = new GameIDConnectivityJson();
        gameid.gameid = ReceivedGameID;

        RestClient.Post<BoardConnectivityJson>("https://catan-connectivity.herokuapp.com/lobby/startgame", gameid).Then(board =>
        {
            ReceiveBoardScript.ReceivedBoard.ports = board.ports;
            ReceiveBoardScript.ReceivedBoard.board = board.board;
        }).Catch(err => { Debug.Log(err); });
    }


    public void RequestLobbyidAndGameid()
    {
        UnityConnectivityCommand command = new UnityConnectivityCommand();
        string board = "";
        command.username = "abcdef";
        RestClient.Post<LobbyConnectivityJson>("https://catan-connectivity.herokuapp.com/lobby/add", command).Then(ReceivedLobby =>
        {
            getGameBoard(ReceivedLobby.gameid);
        }).Catch(err => { Debug.Log(err); });
    }

}
