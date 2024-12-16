package Zabook.repository;

import java.util.Optional;
import java.util.List;
import org.bson.types.ObjectId;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import Zabook.dto.FriendshipStatus;
import Zabook.models.FriendShip;
import Zabook.models.User;

@Repository
public interface FriendshipRepository extends MongoRepository<FriendShip, ObjectId> {
    Optional<FriendShip> findByUser1AndUser2(User user1, User user2);
    List<FriendShip> findByStatusAndUser1OrUser2(String status, User user1, User user2);
    List<FriendShip> findByStatusAndUser2(String status, User user2);
    
   List<FriendShip> findByUser2AndStatus(ObjectId user2, FriendshipStatus status);

}