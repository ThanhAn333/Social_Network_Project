package Zabook.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import Zabook.models.Post;
import Zabook.models.User;

@Repository
public interface PostRepository extends MongoRepository<Post, ObjectId> {
    List<Post> findByUser(User user);
    //
    List<Post> findByOriginalPostId(String originalPostId);
    
    
}