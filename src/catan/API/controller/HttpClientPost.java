package catan.API.controller;

import catan.API.request.GameRequest;
import catan.API.response.Response;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpClientPost {
    private static final String localhostManagerUrl = "http://localhost:8080/Catan/managerRequest/";
    private static final String localhostUserUrl = "http://localhost:8080/Catan/userRequest/";

    private static final String herokuManagerUrl = "https://catan-engine.herokuapp.com/Catan/managerRequest/";
    private static final String herokuUserUrl = "https://catan-engine.herokuapp.com/Catan/userRequest/";

    public static Response managerPost(GameRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString((request)));
        String requestJson = objectMapper.writeValueAsString(request);
        URL url = new URL(localhostManagerUrl);
        return postTo(url, requestJson);
    }

    public static Response userPost(GameRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString((request)));
        String jsonInputString = objectMapper.writeValueAsString((request));
        URL url = new URL(localhostUserUrl);
        return postTo(url, jsonInputString);
    }

    public static Response postTo(URL url, String json) throws IOException {
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] input = json.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
        }
        try (BufferedReader bufferedReader = new BufferedReader
                (new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = bufferedReader.readLine();
            while (responseLine != null) {
                response.append(responseLine.trim());
                responseLine = bufferedReader.readLine();
            }
            Map<String, Object> result = new ObjectMapper().readValue(response.toString(),
                    new TypeReference<HashMap<String, Object>>(){});
            System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString((result)));
            return new Response((Integer)result.get("code"), (String)result.get("status"), (Map<String, Object>)result.get("arguments"));
        }
    }
}
