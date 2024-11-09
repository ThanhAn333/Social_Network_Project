package Zabook.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import Zabook.models.User;

@Repository
public interface UserRepository extends MongoRepository<User, Integer> {
	Optional<User> findByName(String name);
    Page<User> findByNameContaining(String name, Pageable pageable);

}
