using System.Collections;
using System.Collections.Generic;
using UnityEditor;
using UnityEngine;
using UnityEngine.UI;
using FullSerializer;
using Proyecto26;

public static class DatabaseHandler
{
    private const string projectId = "catan-f1c9a";
    public static readonly string databaseURL = $"https://{projectId}.firebaseio.com/";

    private static fsSerializer serializer = new fsSerializer();

    public delegate void PostUserCallback();
    public delegate void HeadUserCallback();
    public delegate void GetUserCallback(UserCredentials user);
    public delegate void GetUsersCallback(Dictionary<string, UserCredentials> users);

    public static void PostUser(UserCredentials user, PostUserCallback callback)
    {

        RestClient.Put<UserCredentials>($"{databaseURL}users/{user.username}.json", user)
         .Then(response => { callback(); });

    }

    public static void GetUserById(string userId, GetUserCallback callback)
    {
        RestClient.Get<UserCredentials>($"{databaseURL}users/{userId}.json")
        .Then(user => { callback(user); })
        .Catch(err => { Debug.Log("GET couldn't fetch the object from database!"); callback(null); });

    }

    public static void GetUserByUsername(string username, GetUserCallback callback)
    {
        RestClient.Get<UserCredentials>($"{databaseURL}users/{username}.json")
        .Then(user => { callback(user); })
        .Catch(err => { Debug.Log("GET couldn't fetch the object from database!"); callback(null); });
    }

    /*
    public static void GetUserByEmail(string useremail, GetUserCallback callback)
    {
        RestClient.Get<UserCredentials>($"{databaseURL}users/{username}.json")
        .Then(user => { callback(user); })
        .Catch(err => { Debug.Log("GET couldn't fetch the object from database!"); callback(null); });
    }
    */

    public static void GetUsers(GetUsersCallback callback)
    {
        RestClient.Get($"{databaseURL}users.json").Then(response =>
        {
            var responseJson = response.Text;

            var data = fsJsonParser.Parse(responseJson);
            object deserialized = null;
            serializer.TryDeserialize(data, typeof(Dictionary<string, UserCredentials>), ref deserialized);

            var users = deserialized as Dictionary<string, UserCredentials>;
            callback(users);
        });
    }
}
