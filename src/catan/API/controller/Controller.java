package catan.API.controller;

import catan.API.request.ManagerRequest;
import catan.API.request.UserRequest;
import catan.API.response.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/Catan")
public class Controller {
    private static final String username = "catan";
    private static final String password = "catan";

    @RequestMapping (value = "/userRequest", method = RequestMethod.POST)
    public Response sendResponse(@RequestBody UserRequest request) {
            return request.run();
    }

    @RequestMapping (value = "/managerRequest", method = RequestMethod.POST)
    public Response sendResponse(@RequestBody ManagerRequest request) {
        if (request.getUsername().equals(username) && request.getPassword().equals(password)) {
            try {
                return request.run();
            } catch (JsonProcessingException exception) {
                exception.printStackTrace();
                return null;
            }
        }
        return new Response(HttpStatus.SC_ACCEPTED, "The credentials are wrong.","");
    }
}
