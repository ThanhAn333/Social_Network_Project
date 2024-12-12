package Zabook.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import Zabook.models.Image;

@Repository
public interface ImageRepository extends MongoRepository<Image, ObjectId>  {

}
