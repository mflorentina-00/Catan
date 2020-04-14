using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;


public class InviteButton : MonoBehaviour
{
    public GameObject invite;
  
    public void Show()
    {
        invite.SetActive(true);
    }
    public void Hide()
    {
        invite.SetActive(false);
    }
}
