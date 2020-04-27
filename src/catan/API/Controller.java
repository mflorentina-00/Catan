package catan.API;

import catan.API.request.ManagerRequest;
import catan.API.request.Status;
import catan.API.request.UserRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/Catan")
public class Controller {
    private static final String ERROR_STATUS = "error";

    @RequestMapping (value = "/userRequest", method = RequestMethod.POST)
    public Response sendResponse(@RequestBody UserRequest request) {
            return request.run();
    }
    @RequestMapping (value = "/managerRequest", method = RequestMethod.POST)
    public Response sendResponse(@RequestBody ManagerRequest request) {
        // Check if the username and password are good.
        if (request.getUsername().equals("catan") && request.getPassword().equals("catan")) {
            try {
                return request.run();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }
        else {
            return new Response(Status.ERROR, "Wrong credentials","");
        }
    }
}
