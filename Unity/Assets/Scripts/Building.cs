using System.Collections;
using System.Collections.Generic;
using UnityEngine;

    public class Building : MonoBehaviour
    {
        private Player owner;

        public Player Owner { get { return owner; } set { this.owner = value; } }
        public bool isOwned()
        {
            if (owner == null)
                return false;
            return true;
        }

    }

