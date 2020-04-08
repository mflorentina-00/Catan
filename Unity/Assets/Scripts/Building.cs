using System.Collections;
using System.Collections.Generic;
using UnityEngine;

    public class Building : MonoBehaviour
    {
        private Player owner = null;
        private Tile firstTile = null;
        private Tile secondTile = null;
        private Tile thirdTile = null;

        public Player Owner { get => owner; set => owner = value; }
        
        public Tile FirstTile { get => firstTile; set => firstTile = value; }
    
        public Tile SecondTile { get => secondTile; set => secondTile = value; }

        public Tile ThirdTile { get => thirdTile; set => thirdTile = value; }        
        
        public bool isOwned()
        {
            if (owner == null)
                return false;
            return true;
        }
    }