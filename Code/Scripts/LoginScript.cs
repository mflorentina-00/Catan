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
    public static int UserAccepted = -2;
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

    public static int GetUserStatus()
    {
        return LoginScript.UserAccepted;
    }

    public void LoginRequest()
    {

        Debug.Log("Username:" + Username.text);
        DatabaseHandler.GetUserByUsername(Username.text, CheckCredentials);
    }

}
