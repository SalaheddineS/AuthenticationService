package com.AuthenticationService.Authentication;


import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/ConnectUser")
    public ResponseEntity<String> ConnectUser(@RequestBody Authentication User){
        try{
        return _authenticationService.ConnectUser(User);}
        catch (Exception e){
            throw e;
        }
    }

}
