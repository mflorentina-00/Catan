using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public static class Token 
{
    private static string ApiToken;
    public static void SetToken(string Token)
    {
        ApiToken = Token;
    }
    public static string GetToken()
    {
        return ApiToken;
    }
}
