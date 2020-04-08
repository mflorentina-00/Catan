using System.Collections;
using System.Collections.Generic;
using UnityEditor;
using UnityEngine;
using UnityEngine.UI;
using Proyecto26;


[System.Serializable]
public class Extension
{
    public string avlb;
    public string color;

}


public class MenuLobby : MonoBehaviour
{
    public Image[] Extension = new Image[5];
    public int[] boolArray = new int[5];
    private const string projectId = "catan-f1c9a";
    private static readonly string databaseURL = $"https://{projectId}.firebaseio.com/";
    public Image CheckImage1;
    public Image CheckImage2;
    public Image[] SelectedImages = new Image[5];

    public void GetExtension(int i)
    {
        RestClient.Get($"{databaseURL}extensions/ext{i}.json").Then(response =>
        {
            Extension[i - 1] = GameObject.Find($"ButtonExp{i}").GetComponent<Image>();
            if (JsonUtility.FromJson<Extension>(response.Text).avlb.Equals("true"))
            {
                Extension[i - 1].enabled = true;
                boolArray[i - 1] = 1;
            }
            else
            {
                Extension[i - 1].enabled = false;
                boolArray[i - 1] = 0;
            };
        });
    }

    public void GetExtensions()
    {
        for (int i = 1; i <= 5; i++)
            GetExtension(i);
    }

    void Start()
    {
        //StartCoroutine(GetExtensions());
        CheckImage1 = GameObject.Find("Image1").GetComponent<Image>();
        CheckImage1.enabled = false;

        CheckImage2 = GameObject.Find("Image2").GetComponent<Image>();
        CheckImage2.enabled = false;

        for (int i = 0; i < 5; i++)
        {
            SelectedImages[i] = GameObject.Find($"ImageSelectExp{i + 1}").GetComponent<Image>();
            SelectedImages[i].enabled = false;
        }
        GetExtensions();
    }

    public void apasaCheck(Image customImage)
    {

        CheckImage2.enabled = false;
        CheckImage1.enabled = false;
        customImage.enabled = true;

    }

    public void UpdateUserExtension()
    {
        UserCredentials user = new UserCredentials("gigi", "balta");
        string currentextension = null;
        user.useremail = "Marius@gimail.com";
        user.userId = "fced3a1d-cc62-4760-972d-de4acc614864";
        for(int i = 0; i < 5; i++)
        {
            if(SelectedImages[i].enabled == true)
            {
                currentextension = string.Concat("ext" + (i+1));
            }
        }
        Debug.Log(currentextension + " was added to current User");
        user.userextension = currentextension;
        RestClient.Put($"{DatabaseHandler.databaseURL}users/{user.username}.json", user);
    }

    public void apasaExansion(Image customImage)
    {
        for (int i = 0; i < 5; i++)
            SelectedImages[i].enabled = false;

        customImage.enabled = true;

        for (int i = 0; i < 5; i++)
        {
            if(boolArray[i] == 0)
                SelectedImages[i].enabled = false;
        }
    }
}

