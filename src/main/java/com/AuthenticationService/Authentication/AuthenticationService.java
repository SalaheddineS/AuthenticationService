package com.AuthenticationService.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {

    private final PasswordEncoder _passwordEncoder;
    private final MongoTemplate _mongoTemplate;



    @Autowired
    public AuthenticationService(PasswordEncoder passwordEncoder, MongoTemplate mongoTemplate) {
        _passwordEncoder = passwordEncoder;
        _mongoTemplate = mongoTemplate;
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
       return ResponseEntity.status(HttpStatus.ACCEPTED).body("lol");

    }

}
