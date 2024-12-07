package Zabook.services;

import org.bson.types.ObjectId;
import java.util.List;
import Zabook.models.FriendShip;
import Zabook.models.User;
public interface IFriendshipService {
	// Gửi lời mời kết bạn
	public FriendShip sendFriendRequest(ObjectId senderId, ObjectId receiverId);
	// Chấp nhận lời mời kết bạn
	public FriendShip acceptFriendRequest(ObjectId friendshipId);
	// Lấy danh sách bạn bè
	public List<User> getFriendList(ObjectId userId);
	// Lấy danh sách lời mời kết bạn đang chờ
	public List<FriendShip> getPendingRequests(ObjectId userId);
}
