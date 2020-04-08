using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;
using FullSerializer;
using Proyecto26;

[Serializable]
public class UserCredentials
{
    public string username;
    public string userpassword;
    public string useremail;
    public string userId;
    public string userextension;

    public UserCredentials(string username,string userpassword)
    {
        this.username = username;
        this.userpassword = userpassword;
        userId = System.Guid.NewGuid().ToString();
    }
    public UserCredentials(string username, string userpassword,string useremail)
    {
        this.username = username;
        this.userpassword = userpassword;
        this.useremail = useremail;
        userId = System.Guid.NewGuid().ToString();
    }
        
}
