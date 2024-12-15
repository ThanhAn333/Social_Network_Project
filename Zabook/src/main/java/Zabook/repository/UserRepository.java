package Zabook.repository;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import Zabook.models.User;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

	Optional<User> findByFirstNameAndLastName(String firstname, String lastname);

	public boolean existsByEmail(String emai);

    public User findByEmail(String email);
    
    public User findByVerificationCode(String verificationCode);


	Optional<User> findById(ObjectId userId);

	// Tìm kiếm người dùng theo tên hoặc họ
	List<User> findByFirstNameContainingOrLastNameContainingIgnoreCase(String firstName, String lastName);

	List<User> findByRole(String role);
}
