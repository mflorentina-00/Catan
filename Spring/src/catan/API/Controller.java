package catan.API;

import catan.API.request.ManagerRequest;
import catan.API.request.UserRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/Catan")
public class Controller {
    private final String sharedKey = "SHARED_KEY";
    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STATUS = "error";
    private static final int CODE_SUCCESS = 100;
    private static final int AUTH_FAILURE = 102;

    @RequestMapping (value = "/userRequest", method = RequestMethod.POST)
    public Response sendResponse(@RequestParam (value = "key") String key, @RequestBody UserRequest request) {
        if(sharedKey.equalsIgnoreCase(key)) {
            //TODO Process the request and return response to the client.
            return request.run();
        }
        else {
            return new Response(AUTH_FAILURE,ERROR_STATUS);
        }
    }
    @RequestMapping (value = "/managerRequest", method = RequestMethod.POST)
    public Response sendResponse(@RequestBody ManagerRequest request) {
        //TODO Check if the username and password are good.
        if (request.getManagerId().equals("silviu") && request.getManagerPass().equals("1234")) {
            return request.run();
        }
        else {
            return new Response(102, "Wrong credentials");
        }
    }
}
