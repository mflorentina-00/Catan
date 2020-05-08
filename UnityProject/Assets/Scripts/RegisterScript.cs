using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using FullSerializer;
using Proyecto26;
using UnityEngine.SceneManagement;


public class RegisterScript : MonoBehaviour
{
    public InputField usernameField;
    public InputField emailField;
    public InputField passwordField;
    public InputField passwordConfirmField;
    public string RegisterEndpoint = "https://catan-connectivity.herokuapp.com/auth/register";

    public GameObject error;
    public Text errorBoxMsg;

    public void RegisterNewUser() // poate fi transforata in bool prin schimbare return si decomentare
    {
        bool valid = false;

        string username = usernameField.text;
        string email = emailField.text;
        string password = passwordField.text;
        string confirmpassword = passwordConfirmField.text;

        if (username.Equals(""))
        {
            errorBoxMsg.text = "Username field is empty!";
            Debug.Log("Username field is empty!");
            ShowErrorBox();
        }
        else if (email.Equals(""))
        {
            errorBoxMsg.text = "Email field is empty!";
            Debug.Log("Email field is empty!");
            ShowErrorBox();
        }
        else if (IsValidEmail(email) == false)
        {
            errorBoxMsg.text = "Email is not valid";
            Debug.Log("Email is not valid");
            ShowErrorBox();
        }
        else if (password.Equals(""))
        {
            errorBoxMsg.text = "Password field is empty!";
            Debug.Log("Password field is empty!");
            ShowErrorBox();
        }
        else if (password.Length < 6)
        {
            errorBoxMsg.text = "The password is too short!";
            Debug.Log("The password is too short!");
            ShowErrorBox();
        }
        else if (confirmpassword.Equals(""))
        {
            errorBoxMsg.text = "Confirm Password field is empty!";
            Debug.Log("Confirm Password field is empty!");
            ShowErrorBox();
        }
        else if (!(password.Equals(confirmpassword)))
        {
            errorBoxMsg.text = "The passwords don't match!";
            Debug.Log("The passwords don't match!");
            ShowErrorBox();
        }
        else
        {

            RegisterConnectivityJson new_user = new RegisterConnectivityJson(email, username, password, confirmpassword);

            RestClient.Post(RegisterEndpoint, new_user)
            .Then(response => {
                Debug.Log("Json:" + response.Text);
                Debug.Log(response.StatusCode.ToString());

                SceneManager.LoadScene("LoginScene");
            })
            .Catch(err => {
                string[] HeaderInfo = err.Message.Split(' ');

                //Debug.Log(err.Message);

                if (HeaderInfo[1] == "400")
                {
                    errorBoxMsg.text = "The username is already in use. Use another!";
                    ShowErrorBox();
                    Debug.Log($"The username is already in use. Use another!");
                }
                else {
                    errorBoxMsg.text = "An unknown error happened. Please try again.";
                    ShowErrorBox();
                    Debug.Log("Http response diferit de 200 sau 400 !! ");
                }
            });

        }

    }

    public bool IsValidEmail(string email)
    {
        try
        {
            var addr = new System.Net.Mail.MailAddress(email);
            return addr.Address == email;
        }
        catch
        {
            return false;
        }
    }

    public void ShowErrorBox()
    {
        error.SetActive(true);
    }
    public void HideErrorBox()
    {
        error.SetActive(false);
    }
}
