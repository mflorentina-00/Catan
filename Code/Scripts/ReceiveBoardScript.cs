using System.Collections;
using System.Collections.Generic;
using System.Threading;
using UnityEngine.UI;
using UnityEditor;
using UnityEngine;
using FullSerializer;
using Proyecto26;
using UnityEngine.SceneManagement;

public class ReceiveBoardScript : MonoBehaviour
{
    BoardConnectivityJson ReceivedBoard = new BoardConnectivityJson();

    public void getGameBoard(string ReceivedGameID)
    {
        GameIDConnectivityJson gameid = new GameIDConnectivityJson();
        gameid.gameid = ReceivedGameID;
        RestClient.Post<BoardConnectivityJson>("https://catan-connectivity.herokuapp.com/lobby/startgame", gameid).Then(board =>
        {
            ReceivedBoard.ports = board.ports;
            ReceivedBoard.board = board.board;
            Debug.Log(JsonUtility.ToJson(ReceivedBoard));
        }).Catch(err => { Debug.Log(err); });
    }

    public string sendSerializedBoardJson()
    {
        return JsonUtility.ToJson(ReceivedBoard);
    }

    public void RequestLobbyidAndGameid()
    {
        UnityConnectivityCommand command = new UnityConnectivityCommand();
        command.username = "abcdef";
        RestClient.Post<LobbyConnectivityJson>("https://catan-connectivity.herokuapp.com/lobby/add", command).Then(ReceivedLobby =>
        {
            getGameBoard(ReceivedLobby.gameid);
        }).Catch(err => { Debug.Log(err); });
    }

}
