package com.rodtech.qideasauthapi.repository;

import com.rodtech.qideasauthapi.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findFirstByEmail(String email);

}
