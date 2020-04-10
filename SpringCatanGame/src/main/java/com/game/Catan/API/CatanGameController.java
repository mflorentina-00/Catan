package com.game.Catan.API;
import com.game.Catan.API.Requests.ManagerRequest;
import com.game.Catan.API.Requests.UserRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/Catan")
public class CatanGameController {
    private final String sharedKey = "SHARED_KEY";
    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STATUS = "error";
    private static final int CODE_SUCCESS = 100;
    private static final int AUTH_FAILURE = 102;
    @RequestMapping(value = "/userRequest", method = RequestMethod.POST)
    public BaseResponse sendResponse(@RequestParam(value = "key") String key,@RequestBody UserRequest request) {
        if(sharedKey.equalsIgnoreCase(key)) {
            // Process the request
            // ....
            // Return response to the client.
            return request.run();
        }
        else
        {
            return new BaseResponse(AUTH_FAILURE,ERROR_STATUS);
        }
    }
    @RequestMapping(value = "/managerRequest", method = RequestMethod.POST)
    public BaseResponse sendResponse(@RequestBody ManagerRequest request){
        if(request.getManagerId().equals("silviu") && request.getManagerPass().equals("1234"))//TODO Check if the username and password are good
            {
                return request.run();
            }
        else
            return new BaseResponse(102,"Wrong Credentials");
    }
}
