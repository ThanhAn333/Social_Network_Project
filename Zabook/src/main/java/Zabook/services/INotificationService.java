package Zabook.services;

import Zabook.models.NotificationType;

public interface INotificationService {
    public void sendNotification(String userId, NotificationType type, String senderName, String targetId);


}
