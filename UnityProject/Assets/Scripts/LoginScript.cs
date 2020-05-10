using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using FullSerializer;
using Proyecto26;
using UnityEngine.SceneManagement;

public class LoginScript : MonoBehaviour
{

    public InputField Username;
    public InputField Password;
    public string LoginEndpoint = "http://catan-connectivity.herokuapp.com/auth/login";
    public static int UserAccepted = -2;
    public static string CurrentUser = null;
    public static string CurrentUserPassword = null;
    public static string CurrentUserEmail = null;
    public static string CurrentUserId = null;
    public static string CurrentUserExtension = null;
    public static int ButtonPressed = 0;
    private void CheckCredentials(UserCredentials Checker)
    {
        ButtonPressed = 1;

        if (Checker == null)
        {

            LoginScript.UserAccepted = -1;

        }
        else
        {
            if (Checker.username == Username.text && Checker.userpassword == Password.text)
            {
                Debug.Log("Congratulations you succesfully logged on!" + Checker.username);
                LoginScript.UserAccepted = 1;
                CurrentUser = Checker.username;
                CurrentUserPassword = Checker.userpassword;
                CurrentUserEmail = Checker.useremail;
                CurrentUserId = Checker.userId;
                CurrentUserExtension = Checker.userextension;
                SceneManager.LoadScene("MenuScene");

            }
            else if (Checker.username == null)
            {
                LoginScript.UserAccepted = -3;
            }
            else if (Password.text == "")
            {
                LoginScript.UserAccepted = -4;
            }
            else
            {
                LoginScript.UserAccepted = 0;

            }
        }

    }
    private void CheckLogin(Login login)
    {
        if (login.username == "")
        {
            LoginScript.UserAccepted = -3;
        }
        else if (login.password == "")
        {
            LoginScript.UserAccepted = -4;

        }
        else
        {
            ButtonPressed = 1;
            RestClient.Post(LoginEndpoint, login)
           .Then(response =>
           {
                Debug.Log(response.StatusCode.ToString());
                LoginScript.UserAccepted = 1;
                LoginConnectivityJson jsonResponse = JsonUtility.FromJson<LoginConnectivityJson>(response.Text);
                Token.SetToken(jsonResponse.access_token);
                SceneManager.LoadScene("MenuScene");
        })
           .Catch(err =>
           {
               string[] HeaderInfo = err.Message.Split(' ');
               if (HeaderInfo[1] == "404")
               {
                   LoginScript.UserAccepted = -1;
               }
               else if (HeaderInfo[1] == "400")
               {
                   LoginScript.UserAccepted = 0;
               }
           });
        }
        LoginScript.UserAccepted = -2;
    }

    public static int GetUserStatus()
    {
        return LoginScript.UserAccepted;
    }

    public void LoginRequest()
    {

        //Debug.Log("Username:" + Username.text);
        //DatabaseHandler.GetUserByUsername(Username.text, CheckCredentials);
        CheckLogin(new Login(Username.text, Password.text));
        
    }

}
