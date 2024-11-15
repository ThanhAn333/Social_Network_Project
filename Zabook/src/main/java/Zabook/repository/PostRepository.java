package Zabook.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import Zabook.models.Post;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByUserId(ObjectId userId);
}