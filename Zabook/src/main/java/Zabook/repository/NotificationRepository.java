package Zabook.repository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import Zabook.models.Notification;


@Repository
public interface NotificationRepository extends MongoRepository<Notification, ObjectId> {
}