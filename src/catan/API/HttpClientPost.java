package catan.API;

import catan.API.request.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import catan.API.request.GameRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class HttpClientPost {
    public static Response userPostTo(GameRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInputString = objectMapper.writeValueAsString(((UserRequest)request));
        URL url = new URL("http://localhost:8080/Catan/userRequest");
        return postTo(url, jsonInputString);
    }

    public static Response managerPostTo(GameRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInputString = objectMapper.writeValueAsString(request);
        URL url = new URL("http://localhost:8080/Catan/managerRequest");
        return postTo(url, jsonInputString);
    }

    public static Response postTo(URL url, String jsonInputString) throws IOException {
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        try(BufferedReader br = new BufferedReader
                (new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            HashMap<?, ?> result = new ObjectMapper().readValue(response.toString(), HashMap.class);
            System.out.println(result);
            return new Response(Integer.valueOf(result.get("code").toString()), result.get("status").toString(), result.get("data").toString());
        }
    }
}
