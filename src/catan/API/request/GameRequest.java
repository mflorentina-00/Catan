package catan.API.request;

import catan.API.Response;
import catan.API.util.RandomString;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public interface GameRequest {
    RandomString randString = new RandomString();
    Response run() throws JsonProcessingException;
    static HashMap<String,String> getMapFromData(String data){
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, String> args=null;
        try {
            if(!data.equals(""))
                args = (HashMap<String, String>) mapper.readValue(data, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return args;
    }
}
