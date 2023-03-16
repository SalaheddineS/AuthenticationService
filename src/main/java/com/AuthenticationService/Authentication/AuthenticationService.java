package com.AuthenticationService.Authentication;


import com.AuthenticationService.Util.JwtUtil;
import io.jsonwebtoken.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {

    private final PasswordEncoder _passwordEncoder;
    private final MongoTemplate _mongoTemplate;
    private final JwtUtil _jwtUtil;


    @Autowired
    public AuthenticationService(PasswordEncoder passwordEncoder, MongoTemplate mongoTemplate, JwtUtil jwtUtil) {
        _passwordEncoder = passwordEncoder;
        _mongoTemplate = mongoTemplate;
        _jwtUtil = jwtUtil;
    }

    public String HashPassword(String nonHashedPassword) {
        return _passwordEncoder.encode(nonHashedPassword);
    }

    public ResponseEntity<String> CreateUser(Authentication User) {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("email").is(User.getEmail()));
            boolean userExists = _mongoTemplate.exists(query, Authentication.class, "authentication");
            if (userExists) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email Already Exists");
            }
            if (User.getEmail() != null
                    && User.getEmail() != ""
                    && User.getAddress() != null
                    && User.getAddress() != ""
                    && User.getBirthDate() != null
                    && User.getPassword() != null
                    && User.getPassword() != "") {
                User.setPassword(HashPassword(User.getPassword()));
                _mongoTemplate.save(User, "authentication");
                return ResponseEntity.status(HttpStatus.CREATED).body("User Created");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Request");
        } catch (Exception e) {
            throw new RuntimeException("Error whilst creating new user , for more info check:" + e);
        }
    }

    public ResponseEntity<String> ConnectUser(Authentication User) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(User.getEmail()));
        Authentication user = _mongoTemplate.findOne(query, Authentication.class, "authentication");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Account Inexistant");
        }
        boolean passwordMatches = _passwordEncoder.matches(User.getPassword(), user.getPassword());
        if (!passwordMatches) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Password Dont Match");
        }
        // Generate token
        try {
            String token = _jwtUtil.generateToken(user.getEmail(), user.getId());
            Update update = new Update();
            update.set("token", token);
            _mongoTemplate.updateFirst(query, update, Authentication.class, "authentication");
            // Send token
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Correctly Authenticated, heres your token" + token);
        } catch (Exception e) {
            throw new RuntimeException("Error whilst generating token , for more info check:" + e);
        }

    }

    public ResponseEntity<String> Logout(HttpHeaders headers) {
        String token = headers.getFirst("Authorization");
        if (token == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No token provided" + token);
        String id = _jwtUtil.getIdFromToken(token);
        if (id == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No token provided");
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("token", "");
        try {
            _mongoTemplate.updateFirst(query, update, Authentication.class, "authentication");
        } catch (Exception e) {
            throw new RuntimeException("Error whilst logging out , for more info check:" + e);
        }
        headers.remove("Authorization"); //Remove token from LocalStorage
        return ResponseEntity.ok().body("Logged out successfully");
    }

    public boolean ValidateToken(HttpHeaders headers){
         String token = headers.getFirst("Authorization");
        if (token == null) return false;
        return _jwtUtil.validateToken(token);
    }

}
