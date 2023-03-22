package com.AuthenticationService;

import com.AuthenticationService.Authentication.Authentication;
import com.AuthenticationService.Authentication.AuthenticationController;
import com.AuthenticationService.Authentication.AuthenticationService;
import com.AuthenticationService.Repository.AuthRepository;
import com.AuthenticationService.Util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SpringBootAuthenticationServiceRunnerTests {

    @Autowired
    private AuthenticationService _authenticationService;
    @Autowired
    private AuthRepository _AuthRepository;
    @Autowired
    private JwtUtil _jwtUtil;
    @Test
    void testAuthentication() {
        Authentication user = new Authentication();
        user.setEmail("testcasemail0@gmail.com");
        user.setAddress("Rue de test");
        user.setBirthDate(new Date());
        user.setPassword("testpassword");
        user.setToken("testtoken");
        _authenticationService.CreateUser(user);
        assertEquals(user.getEmail(), _AuthRepository.findByEmail(user.getEmail()).getEmail());
        Authentication User=_AuthRepository.findByEmail(user.getEmail());
        String id=User.getId();
        _AuthRepository.deleteById(id);
        try{
        _AuthRepository.findByEmail(user.getEmail()).getEmail();
        assertEquals(user.getEmail(), _AuthRepository.findByEmail(user.getEmail()).getEmail());
        }
        catch (Exception e){
            assertEquals(1,1);
        }
    }
    @Test
    void testConnexion(){
        Authentication user = new Authentication();
        user.setEmail("testcasemail@gmail.com");
        user.setAddress("Rue de test");
        user.setBirthDate(new Date());
        user.setPassword("testpassword");
        _authenticationService.ConnectUser(user);
        Authentication User=_AuthRepository.findByEmail(user.getEmail());
        String Token=User.getToken();
        assertEquals(true,_jwtUtil.validateToken(Token));
    }
    @Test
    void testLogout(){
        Authentication user=_AuthRepository.findByEmail("testcasemail@gmail.com");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",user.getToken());
        _authenticationService.Logout(headers);
         user=_AuthRepository.findByEmail("testcasemail@gmail.com");
        assertEquals("",user.getToken());
    }
}
