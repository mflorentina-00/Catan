using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Main : MonoBehaviour
{   
    [RuntimeInitializeOnLoadMethod(RuntimeInitializeLoadType.AfterSceneLoad)]
    private static void OnAppStart()
    {
        Debug.Log("Aplicatia a inceput");
        Debug.Log("API Endpoint:" + DatabaseHandler.databaseURL);
        UserCredentials test2 = new UserCredentials("test2", "test2");
        DatabaseHandler.PostUser(test2, () =>
        {
            Debug.Log("Utilizatorul a fost creat cu success");
        });
        
    }

}
