﻿using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using SocketIO;
using Proyecto26;

[System.Serializable]
public class Lobby
{
    public string extension;
    public string first;
    public string second;
    public string third;
    public string master;
    public string lobbyid;
    public string gameid;

    public Lobby(string extension, string first, string second, string third, string master, string gameid, string lobbyid)
    {
        this.extension = extension;
        this.first = first;
        this.second = second;
        this.third = third;
        this.master = master;
        this.gameid = gameid;
        this.lobbyid = lobbyid;
    }
}

[System.Serializable]
public class AllLobbies
{
    public Lobby[] lobbies;
}

public class JoinLobby
{
    public string username;
    public string lobbyid;
    public JoinLobby(string username, string lobbyid)
    {
        this.username = username;
        this.lobbyid = lobbyid;
    }
}

public class LobbyHandler : MonoBehaviour
{
    public SocketIOComponent socket;                                                               // BETTER ASK Datco Maxim what he have done here. Only God and him know
    public AllLobbies allLobbies = new AllLobbies();        // deserialized json
    public List<Lobby> list_lobbies = new List<Lobby>();       // an list in order to operate lobbies
    public static bool started = false;                                                       // Start is called before the first frame update
    public bool pressed_start_game = false;
    GameObject startbutton;
    void Start()
    {
        GameObject go = GameObject.Find("SocketIO");
        socket = go.GetComponent<SocketIOComponent>();
        setupEvents();
        // the script is being called twice idk why and I dont want to investigate bcs I spent too much time on it
        if (!started)
        {
            started = true;
            startbutton = GameObject.Find("Start");
            startbutton.SetActive(false);

            RestClient.Get<AllLobbies>("https://catan-connectivity.herokuapp.com/lobby/all").Then(ReceivedLobby =>                          // find a lobby with a user extension if found and it has a place join else add a lobby
            {
                allLobbies.lobbies = ReceivedLobby.lobbies;
                if (allLobbies.lobbies.Length != 0)
                {
                    bool found_free_lobby = false;
                    for (int i = 0; i < allLobbies.lobbies.Length; i++)
                    {
                        if (allLobbies.lobbies[i].extension == LoginScript.CurrentUserExtension && (allLobbies.lobbies[i].first == "-" || allLobbies.lobbies[i].second == "-" || allLobbies.lobbies[i].third == "-"))
                        {
                            JoinLobby jl = new JoinLobby(LoginScript.CurrentUser, allLobbies.lobbies[i].lobbyid);
                            found_free_lobby = true;
                           // JoinLobby jl = new JoinLobby("mmoruz", allLobbies.lobbies[i].lobbyid);
                            RestClient.Post("https://catan-connectivity.herokuapp.com/lobby/join", jl).Then(joined_lobby =>
                            {
                                LoginScript.CurrentUserGameId = allLobbies.lobbies[i].gameid;
                                LoginScript.CurrentUserLobbyId = allLobbies.lobbies[i].lobbyid;
                            }).Catch(err => { Debug.Log(err); });
                            break;
                        }
                    }
                    if (!found_free_lobby)
                    {
                        UnityConnectivityCommand command = new UnityConnectivityCommand();
                        //command.username = "mmoruz";
                        command.username = LoginScript.CurrentUser;
                        RestClient.Post<LobbyConnectivityJson>("https://catan-connectivity.herokuapp.com/lobby/add", command).Then(added_Lobby =>
                        {
                            LoginScript.CurrentUserGameId = added_Lobby.gameid;
                            LoginScript.CurrentUserLobbyId = added_Lobby.lobbyid;
                        }).Catch(err => { Debug.Log(err); });
                    }
                }
                else
                {
                    UnityConnectivityCommand command = new UnityConnectivityCommand();
                    //command.username = "mmoruz";
                    command.username = LoginScript.CurrentUser;
                    RestClient.Post<LobbyConnectivityJson>("https://catan-connectivity.herokuapp.com/lobby/add", command).Then(added_Lobby =>
                        {
                            LoginScript.CurrentUserGameId = added_Lobby.gameid;
                            LoginScript.CurrentUserLobbyId = added_Lobby.lobbyid;
                        }).Catch(err => { Debug.Log(err); });
                }
            }).Catch(err => { Debug.Log(err); });


        }

    }

    public void setupEvents()
    {
        socket.On("open", (E) =>
        {
            Debug.Log("Open");
        });
        socket.On("gamestart", EmittedStartGame);

        socket.On("changed", (E) =>                       // updating lobbies list
        {
            Lobby lobby = new Lobby(
                 E.data.GetField("lobby").GetField("extension").str,
                 E.data.GetField("lobby").GetField("first").str,
                 E.data.GetField("lobby").GetField("second").str,
                 E.data.GetField("lobby").GetField("third").str,
                 E.data.GetField("lobby").GetField("master").str,
                 E.data.GetField("lobby").GetField("gameid").str,
                 E.data.GetField("lobby").GetField("lobbyid").str
                 );

            for (int i = 0; i < list_lobbies.Count; i++)
                if (lobby.gameid == list_lobbies[i].gameid)
                {
                    list_lobbies[i] = lobby;
                }
        });

        socket.On("added", (E) =>                 // updating lobbies list
        {
            Lobby lobby = new Lobby(
                E.data.GetField("lobby").GetField("extension").str,
                E.data.GetField("lobby").GetField("first").str,
                E.data.GetField("lobby").GetField("second").str,
                E.data.GetField("lobby").GetField("third").str,
                E.data.GetField("lobby").GetField("master").str,
                E.data.GetField("lobby").GetField("gameid").str,
                E.data.GetField("lobby").GetField("lobbyid").str
                );
            bool found = false;
            for (int i = 0; i < list_lobbies.Count; i++)
                if (lobby.gameid == list_lobbies[i].gameid)
                    found = true;
            if (!found)
            {
                list_lobbies.Add(lobby);
            }
        });
    }

    public void EmitStartGame()
    {

        GameIDConnectivityJson gameid = new GameIDConnectivityJson();
        gameid.gameid = LoginScript.CurrentUserGameId;
        RestClient.Post<BoardConnectivityJson>("https://catan-connectivity.herokuapp.com/lobby/startgame", gameid).Then(board =>
        {
            ReceiveBoardScript.ReceivedBoard.ports = board.ports;
            ReceiveBoardScript.ReceivedBoard.board = board.board;

            JSONObject json_message = new JSONObject();
            //json_message.AddField("lobbyid", "123");
            json_message.AddField("lobbyid", LoginScript.CurrentUserLobbyId);
            socket.Emit("gamestart", json_message);

            SceneChanger n = new SceneChanger();
            n.startGame();

        }).Catch(err => { Debug.Log(err); });

    }

    public void EmittedStartGame(SocketIOEvent e)
    {
        string lobbyid = e.data.GetField("lobbyid").str;
        if (lobbyid == LoginScript.CurrentUserLobbyId) 
        {
            ReceiveBoardScript recive = new ReceiveBoardScript();
            recive.getGameBoardNotMaster();

            SceneChanger n = new SceneChanger();
            n.startGame();
        }
    }

    // Update is called once per frame
    void Update()
    {                                                                                                       // display the names of the members of the lobby. If not master then cannot start game
        GameObject name1 = GameObject.Find("Text1");
        Text line1 = name1.GetComponent<Text>();
        GameObject name2 = GameObject.Find("Text2");
        Text line2 = name2.GetComponent<Text>();
        GameObject name3 = GameObject.Find("Text3");
        Text line3 = name3.GetComponent<Text>();
        GameObject name4 = GameObject.Find("Text4");
        Text line4 = name4.GetComponent<Text>();
       
        for (int i = 0; i < list_lobbies.Count; i++)
            if (list_lobbies[i].lobbyid == LoginScript.CurrentUserLobbyId)
            {
                if (list_lobbies[i].first == LoginScript.CurrentUser)
                {
                    startbutton.SetActive(false);
                    Debug.Log(list_lobbies[i].master);
                    line1.text = list_lobbies[i].master;                
                    line2.text = LoginScript.CurrentUser;                
                    line3.text = list_lobbies[i].second;                 
                    line4.text = list_lobbies[i].third;

                }
                else if(list_lobbies[i].second == LoginScript.CurrentUser)
                {
                    startbutton.SetActive(false);
                    line1.text = list_lobbies[i].master;
                    line2.text = list_lobbies[i].first;
                    line3.text = LoginScript.CurrentUser;
                    line4.text = list_lobbies[i].third;
                    
                }
                else if(list_lobbies[i].third == LoginScript.CurrentUser)
                {
                    startbutton.SetActive(false);
                    line1.text = list_lobbies[i].master;
                    line2.text = list_lobbies[i].first;
                    line3.text = list_lobbies[i].second;
                    line4.text = LoginScript.CurrentUser;
                    
                }
                else
                {
                    startbutton.SetActive(false);
                    line1.text = LoginScript.CurrentUser; 
                    line2.text = list_lobbies[i].first;
                    line3.text = list_lobbies[i].second;
                    line4.text = list_lobbies[i].third;
                    if (list_lobbies[i].first != "-" && list_lobbies[i].second != "-" && list_lobbies[i].third != "-")
                        startbutton.SetActive(true);

                }
            }
    }
}
