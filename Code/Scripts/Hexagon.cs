using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;
using FullSerializer;
using Proyecto26;

/// <summary>
/// This class is used in the deserialization of the received JSON through the POST request
/// </summary>

[Serializable]
public class Hexagon
{
    public string resource;
    public int number;
}

