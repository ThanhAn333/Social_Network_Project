package Zabook.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import Zabook.models.Story;
public interface StoryRepository extends MongoRepository<Story, ObjectId> {

    List<Story> findByUserId(ObjectId userId);
    
    List<Story> findByIsActiveTrue();
}
