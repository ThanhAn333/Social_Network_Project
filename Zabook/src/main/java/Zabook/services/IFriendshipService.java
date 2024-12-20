package Zabook.services;

import org.bson.types.ObjectId;
import java.util.List;

import Zabook.dto.UserDTO;
import Zabook.models.FriendShip;
import Zabook.models.User;
public interface IFriendshipService {
	// Gửi lời mời kết bạn
	public FriendShip sendFriendRequest(ObjectId senderId, ObjectId receiverId);
	// Chấp nhận lời mời kết bạn
	public FriendShip acceptFriendRequest(ObjectId friendship);
	// Lấy danh sách bạn bè
	public List<User> getFriendList(ObjectId userId);
	// Lấy danh sách lời mời kết bạn đang chờ
	public List<FriendShip> getPendingRequests(ObjectId userId);
	// Từ chối lời mời kết bạn
	public FriendShip rejectFriendRequest(ObjectId friendshipId);
	// Tìm kiếm người dùng theo tên hoặc họ
	public List<User> searchUsers(String firstName, String lastName);
	// Thêm method mới
	public List<FriendShip> getPendingRequestsSorted(ObjectId userId);
	//Đồng bộ UserDTO vs Friendship
	public UserDTO convertToUserDTO(User user, FriendShip friendship);

	public boolean accept(ObjectId friendshipId) ;


	
	public List<FriendShip> getFriendships(ObjectId id) ;
}
