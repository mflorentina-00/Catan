using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class WelcomePage : MonoBehaviour
{
    // Start is called before the first frame update
    public void selectScene()
    {
        switch (this.gameObject.name)
        {
            case "SignInButton":
                SceneManager.LoadScene("LoginScene");
                break;
            case "RegisterButton":
                SceneManager.LoadScene("RegisterScene");
                break;
        }
    }
    
}
