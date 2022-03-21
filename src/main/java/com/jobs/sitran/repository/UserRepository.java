package com.jobs.sitran.repository;

import com.jobs.sitran.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findOneByEmailIgnoreCase(String email);

    Boolean existsByEmail(String email);

    Optional<User> findOneByActivationKey(String token);

    Optional<User> findUserById(String id);

    User findByEmail(String email);
}
