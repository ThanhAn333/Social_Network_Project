package Zabook.services;

import java.util.List;

import Zabook.models.Notification;
import Zabook.models.NotificationType;

public interface INotificationService {
    public void sendNotification(String userId, NotificationType type, String senderName, String targetId, String receiverId);

    public List<Notification> getNotifications(String userId);

}
