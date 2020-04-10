package com.game.Catan.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.Catan.API.Requests.GameRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.HashMap;


public class HttpClientPost {
    public static BaseResponse userPostTo(String game,GameRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInputString = objectMapper.writeValueAsString(request);
        URL url = new URL("http://localhost:8080/Catan/userRequest?key="+game);
        return postTo(url,jsonInputString);
    }
    public static BaseResponse managerPostTo(GameRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInputString = objectMapper.writeValueAsString(request);
        URL url = new URL("http://localhost:8080/Catan/managerRequest");
        return postTo(url,jsonInputString);
    }
    public static BaseResponse postTo(URL url,String jsonInputString) throws IOException {
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            String json = "{\"name\":\"mkyong\", \"age\":\"37\"}";
            HashMap<String,Object> result =
                    new ObjectMapper().readValue(response.toString(), HashMap.class);
            System.out.println(result);
            return new BaseResponse(Integer.valueOf(result.get("code").toString()),result.get("status").toString());
        }
    }
}
