package Zabook.services.impl;

import java.util.ArrayList;
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
	// Lấy danh sách lời mời kết bạn đang chờ
	@Override
	public List<FriendShip> getPendingRequests(ObjectId userId) {
		User user = userRepository.findById(userId).orElseThrow();
		return friendshipRepository.findByStatusAndUser2("pending", user);
	}
	public List<FriendShip> getPendingRequestsSorted(ObjectId userId) {
		User user = userRepository.findById(userId).orElseThrow();
		List<FriendShip> pendingRequests = friendshipRepository.findByStatusAndUser2("pending", user);
		
		// Sắp xếp theo thời gian gửi, mới nhất lên đầu
		pendingRequests.sort((f1, f2) -> f2.getCreatedAt().compareTo(f1.getCreatedAt()));
		
		return pendingRequests;
	}
	// Chấp nhận lời mời kết bạn
	@Override
	public FriendShip acceptFriendRequest(ObjectId friendshipId) {
		FriendShip friendship = friendshipRepository.findById(friendshipId)
	            .orElseThrow(() -> new RuntimeException("Không tìm thấy lời mời kết bạn"));
	            
	        if (!friendship.getStatus().equals("pending")) {
	            throw new RuntimeException("Lời mời kết bạn không ở trạng thái chờ");
	        }
	        
	        friendship.setStatus("accepted");
	        return friendshipRepository.save(friendship);
	}
		// Thêm phương thức từ chối lời mời kết bạn
		@Override
		public FriendShip rejectFriendRequest(ObjectId friendshipId) {
			FriendShip friendship = friendshipRepository.findById(friendshipId)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy lời mời kết bạn"));
			
			if (!friendship.getStatus().equals("pending")) {
				throw new RuntimeException("Lời mời kết bạn không ở trạng thái chờ");
			}
			
			friendship.setStatus("rejected");
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

	@Override
	public List<User> searchUsers(String firstName, String lastName) {
		// Nếu cả hai tham số đều null hoặc rỗng, trả về list rỗng
		if ((firstName == null || firstName.trim().isEmpty()) && 
			(lastName == null || lastName.trim().isEmpty())) {
			return new ArrayList<>();
		}

		// Chuẩn hóa các tham số tìm kiếm
		firstName = (firstName != null) ? firstName.trim() : "";
		lastName = (lastName != null) ? lastName.trim() : "";

		// Nếu chỉ có một trong hai tham số, sử dụng tham số đó cho cả hai điều kiện
		if (firstName.isEmpty()) {
			firstName = lastName;
		} else if (lastName.isEmpty()) {
			lastName = firstName;
		}
		
		return userRepository.findByFirstNameContainingOrLastNameContainingIgnoreCase(
			firstName, lastName);
	}

	// Thêm method để lấy thời gian gửi lời mời





}
