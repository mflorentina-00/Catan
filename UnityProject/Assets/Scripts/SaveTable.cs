using UnityEngine;
using System.IO;
using System.Runtime.Serialization.Formatters.Binary;

public static class SaveTable
{
    public static void saveTable(BoardConnectivityJson board)
    {
        BinaryFormatter formatter = new BinaryFormatter();
        string path = Application.persistentDataPath + "/board.fun";
        FileStream stream = new FileStream(path, FileMode.Create);

        formatter.Serialize(stream, board);
        stream.Close();
    }

    public static BoardConnectivityJson LoadTable()
    {
        string path = Application.persistentDataPath + "/board.fun";
        if (File.Exists(path))
        {
            BinaryFormatter formatter = new BinaryFormatter();
            FileStream stream = new FileStream(path, FileMode.Open);

            BoardConnectivityJson board = formatter.Deserialize(stream) as BoardConnectivityJson;
            stream.Close();
            return board;
        }
        else
            Debug.Log("No file");
        return null;
    }
}
