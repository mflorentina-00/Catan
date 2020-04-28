using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;
using FullSerializer;
using Proyecto26;

/// <summary>
///  This class is used for serialization, for attaching a JSON to the Body of the POST request
/// </summary>
[Serializable]
public class LobbyConnectivityJson
{
    public String lobbyid;
    public String gameid;
}
