using System.Collections;
using System.Collections.Generic;
using System.Diagnostics;
using System.Net.NetworkInformation;
using System.Runtime.InteropServices;
using UnityEngine;
using UnityEngine.UI;


public class addPiece : MonoBehaviour
{

    public GameObject piece;
    public bool activeme;

   // Update is called once per frame
    void Update()
    {

        if (Input.GetMouseButtonDown(0))
        {
             
            RaycastHit hit;
            Ray ray = Camera.main.ScreenPointToRay(Input.mousePosition);

            if (Physics.Raycast(ray,out hit, 100.0f))
            {
                UnityEngine.Debug.Log("asd");
                if (hit.transform.gameObject != null);
                {
                    PrintName(hit.transform.gameObject);
                }
            }    
        }
    }
    private void PrintName(GameObject go)
    {
        UnityEngine.Debug.Log("asd");
    }
    

}
