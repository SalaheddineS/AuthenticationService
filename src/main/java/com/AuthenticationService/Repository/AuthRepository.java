package com.AuthenticationService.Repository;

import com.AuthenticationService.Authentication.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends MongoRepository<Authentication, String> {

    Authentication findByEmail(String email);

    Authentication findUserById(String id);

    boolean existsByEmail(String email);






}
