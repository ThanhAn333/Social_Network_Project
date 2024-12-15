package Zabook.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import Zabook.dto.FriendshipStatus;
import Zabook.dto.UserDTO;
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
	@Autowired
	private UserService userService;
	

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
        FriendShip friendship = new FriendShip(sender, receiver, FriendshipStatus.PENDING);
        friendshipRepository.save(friendship);
        
        if (sender == null || receiver == null || friendship == null) {
            throw new IllegalArgumentException("Sender, Receiver hoặc Friendship không được null");
        }
        UserDTO senderDTO = convertToUserDTO(sender, friendship);
        UserDTO receiverDTO = convertToUserDTO(receiver, friendship);
     // Đồng bộ trạng thái vào DTO


        // Nếu cần, trả về DTO
        return friendship;
	}
	// Chấp nhận lời mời kết bạn
		@Override
		public FriendShip acceptFriendRequest(ObjectId friendshipId) {

		     // Tìm mối quan hệ trong cơ sở dữ liệu
		        FriendShip friendship = friendshipRepository.findById(friendshipId)
		                .orElseThrow(() -> new RuntimeException("Không tìm thấy lời mời kết bạn"));

		        // Kiểm tra trạng thái mối quan hệ
		        if (!friendship.getStatus().equals(FriendshipStatus.PENDING)) {
		            throw new RuntimeException("Lời mời kết bạn không ở trạng thái chờ");
		        }

		        // Cập nhật trạng thái mối quan hệ
		        friendship.setStatus(FriendshipStatus.ACCEPTED);
		        
		        // Lưu lại mối quan hệ đã thay đổi
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
	
		// Thêm phương thức từ chối lời mời kết bạn
		@Override
		public FriendShip rejectFriendRequest(ObjectId friendshipId) {
			FriendShip friendship = friendshipRepository.findById(friendshipId)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy lời mời kết bạn"));
			
			if (!friendship.getStatus().equals("pending")) {
				throw new RuntimeException("Lời mời kết bạn không ở trạng thái chờ");
			}
			
			friendship.setStatus(FriendshipStatus.REJECTED);
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
	@Override
	public UserDTO convertToUserDTO(User user, FriendShip friendship) {
		// Khai báo các giá trị cần thiết
	    FriendshipStatus status = FriendshipStatus.NONE; // Mặc định trạng thái là NONE
	    boolean isSender = false; // Mặc định không phải là người gửi
	    String friendshipId = null; // Mặc định không có ID mối quan hệ
	    String fullName = user.getFirstName() + " " + user.getLastName(); // Ghép tên đầy đủ

	    if (friendship != null) { // Nếu có mối quan hệ
	        status = friendship.getStatus(); // Lấy trạng thái từ mối quan hệ
	        friendshipId = friendship.getFriendshipID().toString(); // Lấy ID của mối quan hệ

	        // Kiểm tra người dùng hiện tại có phải là người gửi không
	        isSender = friendship.getUser1().equals(userService.getCurrentUser());
	    }

	    // Trả về đối tượng UserDTO với các thông tin đã xử lý
	    return new UserDTO(
	        user.getUserID(),
	        fullName,
	        user.getAvatar(),
	        calculateMutualFriends(user), // Tính số lượng bạn chung
	        status,
	        friendshipId,
	        isSender
	    );
	    
	}
	private int calculateMutualFriends(User user) {
        // Logic để tính số lượng bạn chung
        return 0; // Thay thế bằng logic thực tế
    }
	 public FriendshipStatus getFriendshipStatus(ObjectId userId1, ObjectId userId2) {
	        // Kiểm tra trạng thái quan hệ

		 	User sender = userRepository.findById(userId1).orElseThrow();
	        User receiver = userRepository.findById(userId2).orElseThrow();
	        FriendShip friendship = friendshipRepository.findByUser1AndUser2(sender, receiver)
	                .orElse(friendshipRepository.findByUser1AndUser2(receiver, sender).orElse(null));
	        if (friendship == null) {
	            return FriendshipStatus.NONE;
	        }

	        return friendship.getStatus();
	    }
	public FriendShip getFriendshipBetweenUsers(ObjectId userId1, ObjectId userId2) {
		User sender = userRepository.findById(userId1).orElseThrow();
        User receiver = userRepository.findById(userId2).orElseThrow();    
		 return friendshipRepository.findByUser1AndUser2(sender, receiver)
			        .orElse(friendshipRepository.findByUser1AndUser2(receiver, sender).orElse(null));

	}

	// Thêm method để lấy thời gian gửi lời mời





}
