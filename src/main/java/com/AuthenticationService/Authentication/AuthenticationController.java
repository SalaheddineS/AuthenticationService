package com.AuthenticationService.Authentication;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/Authentication")
public class AuthenticationController {

   private final AuthenticationService _authenticationService;
    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {
        _authenticationService = authenticationService;
    }


    @PostMapping("/AddUser")
    public ResponseEntity AddUser(@RequestBody Authentication User){
        return _authenticationService.CreateUser(User);
    }

    @RequestMapping(value = "/GetUser", method = RequestMethod.GET)


    @PostMapping("/ConnectUser")
    public ResponseEntity<String> ConnectUser(@RequestBody Authentication User){
        try{
        return _authenticationService.ConnectUser(User);}
        catch (Exception e){
            throw e;
        }
    }
    @GetMapping("/logoutUser")
    public ResponseEntity<String> Logout(@RequestHeader HttpHeaders headers){
        try{
            return _authenticationService.Logout(headers);
        }
        catch (Exception e){
            throw e;
        }
    }

    @GetMapping("/ValidateToken")
    public boolean ValidateToken(@RequestHeader HttpHeaders headers){
        try{
            return _authenticationService.ValidateToken(headers);
        }
        catch (Exception e){
            throw e;
        }
    }
}
