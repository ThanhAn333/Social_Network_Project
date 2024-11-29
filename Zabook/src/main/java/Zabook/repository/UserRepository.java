package Zabook.repository;

import java.util.Optional;

import org.bson.types.ObjectId;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import Zabook.models.User;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
	Optional<User> findByFirstNameAndLastName(String firstname, String lastname);
	Optional<User> findByEmail(String email);

	Optional<User> findById(ObjectId userId);
}
