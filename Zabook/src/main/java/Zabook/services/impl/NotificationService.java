package Zabook.services.impl;



import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import Zabook.models.Notification;
import Zabook.models.NotificationType;
import Zabook.repository.NotificationRepository;
import Zabook.services.INotificationService;

@Service
public class NotificationService implements INotificationService {

     @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendNotification(String userId, NotificationType type, String senderName, String targetId) {
        String content = "";

        // Xử lý nội dung thông báo theo từng loại
        switch (type) {
            case LIKE:
                content = "đã thích bài viết của bạn.";
                break;
            case COMMENT:
                content = "đã bình luận về bài viết của bạn.";
                break;
            case SHARE:
                content = "đã chia sẻ bài viết của bạn.";
                break;
            case FRIEND_REQUEST:
                content = "đã gửi cho bạn một lời mời kết bạn.";
                break;
        }

        // Tạo đối tượng Notification
        Notification notification = new Notification(type, senderName, content, targetId);
        notificationRepository.save(notification);

        // Gửi thông báo đến user
        messagingTemplate.convertAndSendToUser(userId, "/queue/notifications", notification);
    }

   
    
}
