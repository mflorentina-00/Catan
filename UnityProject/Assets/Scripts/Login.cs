using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;
using FullSerializer;
using Proyecto26;
[Serializable]
public class Login
{
    public string username;
    public string password;

    public Login(string username,string password)
    {
        this.username=username;
        this.password = password;
    }
}
