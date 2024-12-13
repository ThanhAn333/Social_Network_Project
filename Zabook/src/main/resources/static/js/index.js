document.getElementById("sendCommentBtn").addEventListener("click", function () {
    const content = document.getElementById("contentInput").value;
    const postId = document.getElementById("postId").value; // Thay bằng ID bài viết thực tế
    const rate = 0; // Thay bằng giá trị đánh giá thực tế (nếu cần)

    if (!content.trim()) {
        alert("Vui lòng nhập nội dung bình luận!");
        return;
    }

    const url = `/user/comments/add`;

    // Dữ liệu cần gửi tới controller
    const data = {
        post: { id: postId }, // Gửi ID bài viết
        content: content,
        rate: rate
    };

    fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data) // Chuyển đối tượng thành JSON để gửi
    })
        .then(response => {
            if (response.ok) {
                alert("Bình luận đã được thêm thành công!");
            } else {
                throw new Error("Lỗi khi gửi yêu cầu");
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert("Có lỗi xảy ra khi thêm bình luận.");
        });
});

function toggleLike(event, button) {
			const hasSelectedReaction = button.classList.contains('selected');
			if (hasSelectedReaction) {
				// Nếu đã chọn cảm xúc, ấn thêm lần nữa để hủy thích
				button.classList.remove('selected', 'like-color', 'love-color', 'haha-color', 'wow-color', 'sad-color', 'angry-color');
				button.innerHTML = `
            <i class="fas fa-thumbs-up"></i> Thích
            <div class="emoji-icons">
                <i class="fas fa-thumbs-up like" onclick="selectReaction(event, this, 'Thích')"></i>
                <i class="fas fa-heart love" onclick="selectReaction(event, this, 'Yêu thích')"></i>
                <i class="fas fa-laugh-squint haha" onclick="selectReaction(event, this, 'Haha')"></i>
                <i class="fas fa-surprise wow" onclick="selectReaction(event, this, 'Wow')"></i>
                <i class="fas fa-sad-tear sad" onclick="selectReaction(event, this, 'Buồn')"></i>
                <i class="fas fa-angry angry" onclick="selectReaction(event, this, 'Phẫn nộ')"></i>
            </div>
        `;
			}
		}


		function selectReaction(event, icon, reactionText) {
			event.stopPropagation();

			// Tìm nút cha
			const button = icon.closest('.btn-like');

			// Xóa các class màu cũ
			button.classList.remove('like-color', 'love-color', 'haha-color', 'wow-color', 'sad-color', 'angry-color');

			// Thêm class màu dựa trên loại cảm xúc
			if (icon.classList.contains('like')) button.classList.add('like-color');
			else if (icon.classList.contains('love')) button.classList.add('love-color');
			else if (icon.classList.contains('haha')) button.classList.add('haha-color');
			else if (icon.classList.contains('wow')) button.classList.add('wow-color');
			else if (icon.classList.contains('sad')) button.classList.add('sad-color');
			else if (icon.classList.contains('angry')) button.classList.add('angry-color');

			// Thêm trạng thái "selected"
			button.classList.add('selected');
			button.innerHTML = `
        <i class="${icon.className}"></i> ${reactionText}
        <div class="emoji-icons">
            <i class="fas fa-thumbs-up like" onclick="selectReaction(event, this, 'Thích')"></i>
            <i class="fas fa-heart love" onclick="selectReaction(event, this, 'Yêu thích')"></i>
            <i class="fas fa-laugh-squint haha" onclick="selectReaction(event, this, 'Haha')"></i>
            <i class="fas fa-surprise wow" onclick="selectReaction(event, this, 'Wow')"></i>
            <i class="fas fa-sad-tear sad" onclick="selectReaction(event, this, 'Buồn')"></i>
            <i class="fas fa-angry angry" onclick="selectReaction(event, this, 'Phẫn nộ')"></i>
        </div>
    `;
    
    
    // Tìm bài viết cha chứa nút bấm
    const postElement = icon.closest('.post-actions');
    if (!postElement || !postElement.querySelector("input[name='postId1']")) {
        console.error("Không tìm thấy phần tử chứa postId");
        return;
    }

    // Lấy postId từ bài viết hiện tại
    const postId = postElement.querySelector("input[name='postId1']").value;

    // Gửi yêu cầu tới controller
    fetch('/user/post/like/', {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ postId: postId }) // Sửa thành JSON object
    })
    .then(response => {
        if (response.ok) {
            alert('Cảm xúc của bạn đã được gửi!');
        } else {
            throw new Error('Lỗi khi gửi yêu cầu');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert(error.message);
    });
		}
		
		
		
		
		//comment 
		function toggleCommentForm(button) {
			// Tìm phần tử form bình luận
			const commentFormContainer = button.closest('.fb-p1-main').querySelector('.comment-form-container');

			// Kiểm tra trạng thái hiển thị và thay đổi
			if (commentFormContainer.style.display === 'none' || commentFormContainer.style.display === '') {
				commentFormContainer.style.display = 'block'; // Hiển thị form
			} else {
				commentFormContainer.style.display = 'none'; // Ẩn form
			}
		}


		function toggleSharePopup() {
			const sharePopup = document.querySelector('.share-popup');
			// Đổi trạng thái hiển thị của popup
			if (sharePopup.style.display === 'none' || sharePopup.style.display === '') {
				sharePopup.style.display = 'block';
			} else {
				sharePopup.style.display = 'none';
			}
		}
		var darkButton = document.querySelector(".darkTheme");

		darkButton.onclick = function () {
			darkButton.classList.toggle("button-Active");
			document.body.classList.toggle("dark-color")
		}
		
		
		
		 function previewFiles(previewId, inputId, fileType) {
        const previewContainer = document.getElementById(previewId);
        const input = document.getElementById(inputId);
        const files = input.files;

        // Clear old previews
        previewContainer.innerHTML = "";

        // Duyệt qua danh sách file
        for (const file of files) {
            const reader = new FileReader();
            reader.onload = function (e) {
                // Tạo thẻ preview
                let previewElement;

                if (fileType === "image") {
                    previewElement = document.createElement("img");
                    previewElement.src = e.target.result;
                    previewElement.style.width = "100px";
                    previewElement.style.height = "100px";
                    previewElement.style.objectFit = "cover";
                } else if (fileType === "video") {
                    previewElement = document.createElement("video");
                    previewElement.src = e.target.result;
                    previewElement.controls = true;
                    previewElement.style.width = "150px";
                }

                previewContainer.appendChild(previewElement);
            };

            reader.readAsDataURL(file);
        }
    }
    
    
    
    const commentTime = new Date(document.getElementById("commentTime").textContent);
const now = new Date();
const diff = Math.abs(now - commentTime);
const hours = Math.floor(diff / (1000 * 60 * 60));
const days = Math.floor(hours / 24);

const elapsedTime = hours > 24 ? `${days} ngày trước` : `${hours} giờ trước`;
document.getElementById("elapsedTime").textContent = elapsedTime;


document.addEventListener("DOMContentLoaded", function () {
    const createdAtElement = document.getElementById("createdAt");
    const createdAtString = createdAtElement.getAttribute("data-time"); // Lấy thời gian từ attribute
    
    // Chuyển đổi thành đối tượng Date
    const createdAt = new Date(createdAtString);

    // Lấy các thành phần ngày, tháng, năm
    const year = createdAt.getFullYear();
    const month = String(createdAt.getMonth() + 1).padStart(2, "0"); // Tháng bắt đầu từ 0
    const day = String(createdAt.getDate()).padStart(2, "0");

    // Hiển thị định dạng yyyy-MM-dd
    createdAtElement.textContent = `${year}-${month}-${day}`;
});



// Lắng nghe sự kiện khi bấm vào số like
  document.getElementById("likeCount").addEventListener("click", function() {
    openLikesModal(); // Mở modal khi click vào số like
  });

  // Lắng nghe sự kiện đóng modal
  document.getElementById("closeModal").addEventListener("click", function() {
    closeLikesModal(); // Đóng modal khi click vào nút đóng
  });

  // Mở modal và lấy danh sách người đã like
  function openLikesModal() {
    fetchLikes(); // Gọi hàm fetch để lấy dữ liệu
    document.getElementById("likesModal").style.display = "block"; // Hiển thị modal
  }

  // Đóng modal
  function closeLikesModal() {
    document.getElementById("likesModal").style.display = "none"; // Ẩn modal
  }

  // Hàm lấy danh sách người đã like
  function fetchLikes() {
    const postId = document.getElementById("postId").value; // Giả sử bạn có postId trong HTML

    fetch(`/user/post/likeList?postId=${postId}`) // API của bạn
      .then(response => response.json())
      .then(data => {
        // Điền danh sách người vào modal
        const likesList = document.getElementById("likesList");
        likesList.innerHTML = ""; // Xóa dữ liệu cũ
        data.forEach(user => {
          const listItem = document.createElement("li");
          listItem.textContent = user.username; // Dữ liệu trả về chứa tên người dùng
          likesList.appendChild(listItem);
        });
      })
      .catch(error => console.error("Error fetching likes:", error));
  }

  function openStory(index) {
    if (index >= 0 && index < stories.length) {
        currentStoryIndex = index;
        const story = stories[index];

        const modal = document.getElementById('storyModal');
        modal.style.display = 'flex';

        document.getElementById('modalAvatar').src = story.avatar;
        document.getElementById('modalUserName').textContent = story.userName;
        document.getElementById('modalTime').textContent = story.time;

        const modalImage = document.getElementById('modalImage');
        const modalVideo = document.getElementById('modalVideo');

        if (story.image) {
            modalImage.src = story.image;
            modalImage.style.display = 'block';
            modalVideo.style.display = 'none';
        } else if (story.video) {
            modalVideo.src = story.video;
            modalVideo.style.display = 'block';
            modalImage.style.display = 'none';
        }

        document.getElementById('modalText').textContent = story.textContent || '';
    }
}

function prevStory() {
    if (currentStoryIndex > 0) {
        openStory(currentStoryIndex - 1);
    }
}

// Hàm chuyển đến câu chuyện tiếp theo
function nextStory() {
    if (currentStoryIndex < stories.length - 1) {
        openStory(currentStoryIndex + 1);
    }
}

// Hàm đóng modal khi người dùng nhấn vào nút đóng
function closeStory() {
    const modal = document.getElementById('storyModal');
    modal.style.display = 'none';

    // Reset lại nội dung trong modal
    document.getElementById('modalImage').src = '';
    document.getElementById('modalVideo').src = '';
}


function showPopup() {
    document.getElementById('popupOverlay').style.display = 'block';
}

function hidePopup() {
    document.getElementById('popupOverlay').style.display = 'none';
}

function toggleStoryType() {
    const textStory = document.getElementById('textStory');
    const mediaStory = document.getElementById('mediaStory');
    const storyType = document.querySelector('input[name="storyType"]:checked').value;

    if (storyType === 'text') {
        textStory.style.display = 'block';
        mediaStory.style.display = 'none';
    } else if (storyType === 'media') {
        textStory.style.display = 'none';
        mediaStory.style.display = 'block';
    }
}

function previewMedia(event) {
    const file = event.target.files[0];
    const previewContainer = document.getElementById('previewContainer');
    const imagePreview = document.getElementById('imagePreview');
    const videoPreview = document.getElementById('videoPreview');

    imagePreview.style.display = 'none';
    videoPreview.style.display = 'none';

    if (file) {
        const fileType = file.type;

        const fileURL = URL.createObjectURL(file);

        if (fileType.startsWith('image')) {
            imagePreview.src = fileURL;
            imagePreview.style.display = 'block';
        } else if (fileType.startsWith('video')) {
            videoPreview.src = fileURL;
            videoPreview.style.display = 'block';
        } else {
            alert('File không hợp lệ. Vui lòng chọn hình ảnh hoặc video.');
            event.target.value = '';
        }
    }
}

