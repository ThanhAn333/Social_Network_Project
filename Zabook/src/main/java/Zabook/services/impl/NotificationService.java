package Zabook.services.impl;


import java.time.LocalDateTime;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import Zabook.models.Notification;
import Zabook.repository.NotificationRepository;
import Zabook.services.INotificationService;

@Service
public class NotificationService implements INotificationService {

     @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendNotification(String userId, String type, String message) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setRead(false);
        notification.setTimestamp(LocalDateTime.now());

        // Lưu thông báo vào MongoDB
        notificationRepository.save(notification);

        // Đẩy thông báo thời gian thực đến client qua WebSocket
        messagingTemplate.convertAndSend("/topic/notifications/" + userId, notification);
        
    }

   
    
}
