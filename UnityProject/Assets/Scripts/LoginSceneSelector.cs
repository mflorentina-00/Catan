using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class LoginSceneSelector : MonoBehaviour
{
    // Start is called before the first frame update
    public void selectScene()
    {
        switch (this.gameObject.name)
        {
            case "NoAccountClick":
                SceneManager.LoadScene("RegisterScene");
                break;
           
        }
    }
}
