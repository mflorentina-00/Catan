using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class RegisterSceneSelector : MonoBehaviour
{
    // Start is called before the first frame update
    public void selectScene()
    {
        switch (this.gameObject.name)
        {
            case "AlreadyHasAccount":
                SceneManager.LoadScene("LoginScene");
                break;
        }
    }
}
