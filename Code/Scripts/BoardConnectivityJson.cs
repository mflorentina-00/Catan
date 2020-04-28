using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;
using FullSerializer;
using Proyecto26;

[Serializable]
public class BoardConnectivityJson
{
    public string[] ports = new string[256];
    public HexagonConnectivityJson[] board = new HexagonConnectivityJson[256];
}
