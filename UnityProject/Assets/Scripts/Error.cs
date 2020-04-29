using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class Error : MonoBehaviour
{
    public GameObject error;
    public int UserStatus;
    public Text errortext;
    public int UserStatus2;
    public void ChangeError(string err)
    {
        errortext.text = err;
    }

    void Update()
    {

        UserStatus = LoginScript.UserAccepted;
        
        if (UserStatus == -2) return;
        if (LoginScript.ButtonPressed == 0) return;
        
        UserStatus2 = UserStatus;
        if (UserStatus == 0)
        {
            ChangeError("Ooops! Invalid Password");
        }
        else if (UserStatus == 1)
        {
            ChangeError("Congratulations! Logged In");
        }
        else if (UserStatus == -1)
        {
            ChangeError("Ooops! Invalid Username");
        }
        else if(UserStatus==-3)
        {
            ChangeError("Username field is EMPTY ! ");
        }
        else if (UserStatus == -4)
        {
            ChangeError("Password field is EMPTY ! ");
        }
        LoginScript.ButtonPressed = 0;
        Show();
        
    }
    public void Show()
    {
        error.SetActive(true);
    }
    public void Hide()
    {
        error.SetActive(false);
    }
}
