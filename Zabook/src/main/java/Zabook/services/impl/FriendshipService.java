package Zabook.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import Zabook.models.FriendShip;
import Zabook.models.User;
import Zabook.repository.FriendshipRepository;
import Zabook.repository.UserRepository;
import Zabook.services.IFriendshipService;

@Service
public class FriendshipService implements IFriendshipService {

	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FriendshipRepository friendshipRepository;

	// Gửi lời mời kết bạn
	@Override
	public FriendShip sendFriendRequest(ObjectId senderId, ObjectId receiverId) {
		User sender = userRepository.findById(senderId).orElseThrow();
        User receiver = userRepository.findById(receiverId).orElseThrow();
        
        // Kiểm tra xem đã có lời mời kết bạn chưa
        if (friendshipRepository.findByUser1AndUser2(sender, receiver).isPresent() ||
            friendshipRepository.findByUser1AndUser2(receiver, sender).isPresent()) {
            throw new RuntimeException("Lời mời kết bạn đã tồn tại");
        }
        
        FriendShip friendship = new FriendShip(sender, receiver, "pending");
        return friendshipRepository.save(friendship);
	}
	// Chấp nhận lời mời kết bạn
	@Override
	public FriendShip acceptFriendRequest(ObjectId friendshipId) {
		FriendShip friendship = friendshipRepository.findById(friendshipId)
	            .orElseThrow(() -> new RuntimeException("Không tìm thấy lời mời kết bạn"));
	            
	        friendship.setStatus("accepted");
	        return friendshipRepository.save(friendship);
	}
	// Lấy danh sách bạn bè
	@Override
	public List<User> getFriendList(ObjectId userId) {
		User user = userRepository.findById(userId).orElseThrow();
        List<FriendShip> friendships = friendshipRepository.findByStatusAndUser1OrUser2(
            "accepted", user, user);
            
        return friendships.stream()
            .map(friendship -> friendship.getUser1().equals(user) 
                ? friendship.getUser2() 
                : friendship.getUser1())
            .collect(Collectors.toList());
	}
	// Lấy danh sách lời mời kết bạn đang chờ
	@Override
	public List<FriendShip> getPendingRequests(ObjectId userId) {
		 User user = userRepository.findById(userId).orElseThrow();
	        return friendshipRepository.findByStatusAndUser2("pending", user);
	}

}
