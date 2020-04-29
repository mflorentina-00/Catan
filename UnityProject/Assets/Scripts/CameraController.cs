using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CameraController : MonoBehaviour
{
   


    public float movementSpeed;
    public float movementTime;
    public float rotation;
   

    public Vector3 newPos;
    public Quaternion newRotation;
    

    // Start is called before the first frame update
    void Start()
    {
        newPos = gameObject.transform.position;
        newRotation = gameObject.transform.rotation;
       
    }

    // Update is called once per frame
    void Update()
    {
        HandleMovementInp();
    }
    void HandleMovementInp()
    {
        if (Input.GetKey(KeyCode.W) || Input.GetKey(KeyCode.UpArrow))
        {
            newPos += (transform.forward * movementSpeed);

        }
        if (Input.GetKey(KeyCode.S) || Input.GetKey(KeyCode.DownArrow))
        {
            newPos += (transform.forward * -movementSpeed);

        }
        if (Input.GetKey(KeyCode.D) || Input.GetKey(KeyCode.RightArrow))
        {
            newPos += (transform.right * movementSpeed);

        }
        if (Input.GetKey(KeyCode.A) || Input.GetKey(KeyCode.LeftArrow))
        {
            newPos += (transform.right * -movementSpeed);

        }
        if (Input.GetKey(KeyCode.Q))
        {
            newRotation *= Quaternion.Euler(Vector3.up * rotation);

        }
        if (Input.GetKey(KeyCode.E))
        {
            newRotation *= Quaternion.Euler(Vector3.up * -rotation);

        }
       
        transform.position = Vector3.Lerp(transform.position, newPos, Time.deltaTime * movementTime);
        transform.rotation = Quaternion.Lerp(transform.rotation, newRotation, Time.deltaTime * movementTime);
        
    }
}
