document.getElementById("sendCommentBtn").addEventListener("click", function() {
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


function toggleLike(button) {
    const heart = button.querySelector('.love');  // Lấy phần tử trái tim bên trong nút bấm
    const likeText = button.querySelector('#like-text');  // Lấy văn bản "Thích" bên trong nút bấm

    // Kiểm tra trạng thái của trái tim và thay đổi màu sắc
    let reactionText = '';
    if (heart.classList.contains('liked')) {
        heart.classList.remove('liked');
        likeText.style.color = 'black';
        likeText.textContent = 'Thích';
        reactionText = 'unlike';  // Xác định hành động "Hủy thích"
    } else {
        heart.classList.add('liked');
        likeText.style.color = 'red';
        likeText.textContent = 'Đã thích';
        reactionText = 'like';  // Xác định hành động "Thích"
    }

    const postId = button.getAttribute('data-postId');
    const updateLikeSelector = button.getAttribute('data-updateLike');

    // Kiểm tra nếu không có giá trị `data-updateLike`
    if (!updateLikeSelector) {
        console.error('data-updateLike attribute is missing on button.');
        return;
    }

    // Gửi yêu cầu đến API
    fetch('/user/post/updateReaction', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `postId=${postId}&reaction=${reactionText}`  // Gửi cả postId và reaction (like/unlike)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();  // Trả về dữ liệu số lượng like mới
    })
    .then(updatedLikeCount => {
        console.log("Updated Like Count:", updatedLikeCount); // Debug giá trị trả về từ server

        // Tìm phần tử HTML chứa số lượng like của bài viết
        const likeCountElement = document.querySelector(updateLikeSelector);
        if (likeCountElement) {
            likeCountElement.textContent = updatedLikeCount;  // Cập nhật số lượng like
        } else {
            console.error(`Element with selector "${updateLikeSelector}" not found.`);
        }
    })
    .catch(error => console.error('Error:', error));
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


function toggleSharePopup(button) {
	const sharePopup = document.querySelector('.share-popup');

	const postId = button.getAttribute('data-postid');

	const sharePostElement = document.querySelector('#share-postid');
	sharePostElement.textContent = postId;

	var postIdValue = document.getElementById("share-postid").innerText;

	// Gán giá trị vào trường input có id="share-postid-input"
	document.getElementById("share-postid-input").value = postIdValue;

	if (sharePopup.style.display === 'none' || sharePopup.style.display === '') {
		sharePopup.style.display = 'block';
	} else {
		sharePopup.style.display = 'none';
	}
}




var darkButton = document.querySelector(".darkTheme");

darkButton.onclick = function() {
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
		reader.onload = function(e) {
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


document.addEventListener("DOMContentLoaded", function() {
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

        document.getElementById('modalAvatar').src = stories[currentStoryIndex].avatar;
        document.getElementById('modalUserName').textContent = story.userName;
        document.getElementById('modalTime').textContent = story.time;

		const modalImage = document.getElementById('modalImage');
		const modalVideo = document.getElementById('modalVideo');

        // Xử lý nội dung story
    if (story.textContent) {
        modalText.textContent = story.textContent;
        modalText.style.display = "block"; // Hiển thị văn bản
    } else {
        modalText.style.display = "none"; // Ẩn văn bản nếu không có
    }

    // Xử lý hình ảnh
    if (story.image) {
        modalImage.src = story.image;
        modalImage.style.display = "block"; // Hiển thị ảnh
    } else {
        modalImage.style.display = "none"; // Ẩn ảnh nếu không có
    }

    // Xử lý video
    if (story.video) {
        modalVideo.src = story.video;
        modalVideo.style.display = "block"; // Hiển thị video
        modalImage.style.display = "none"; // Ưu tiên video hơn ảnh
    } else {
        modalVideo.style.display = "none"; // Ẩn video nếu không có
        modalVideo.src = "";
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



// Hiển thị popup
function showPopupuser(popupId) {
	const popup = document.getElementById(popupId);
	popup.style.display = 'block';
}

// Đóng popup
function closePopupuser(popupId) {
	const popup = document.getElementById(popupId);
	popup.style.display = 'none';
}

// Hàm để thêm danh sách người thích vào popup
// Hàm gọi API và hiển thị danh sách người thích
async function loadLikeListFromController(button) {
	const postId = button.getAttribute('data-id'); // Lấy postId từ data-id của nút
	try {
		// Gửi request tới controller với postId làm tham số trong body
		const response = await fetch('/user/post/likeList', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/x-www-form-urlencoded'
			},
			body: `postId=${encodeURIComponent(postId)}`  // Gửi postId trong dạng x-www-form-urlencoded
		});

		if (!response.ok) {
			throw new Error(`HTTP error! status: ${response.status}`);
		}

		const users = await response.json();  // Phân tích response JSON
		console.log("Users who liked the post:", users);

		// Xóa danh sách cũ nếu có
		const likeListElement = document.getElementById('like-list');
		likeListElement.innerHTML = '';
		
		if(!users || users.length === 0){
			const thongbao = document.createElement('li');
			const thongbao2 =document.createElement('span');
			thongbao2.textContent = 'Chưa có lượt thích nào';
			thongbao.appendChild(thongbao2);
			likeListElement.appendChild(thongbao);
		}
		
		// Thêm từng người vào danh sách
		users.forEach(user => {
			const listItem = document.createElement('li');
			
			// Ảnh đại diện
			const avatar = document.createElement('img');
			avatar.src = user.avatar || '/images/default-avatar.png'; // URL ảnh đại diện
			avatar.alt = user.name;
			

			// Tên người dùng
			const name = document.createElement('span');
			name.textContent = user.firstName + ' ' + user.lastName;

			// Gắn các phần tử vào danh sách
			listItem.appendChild(avatar);
			listItem.appendChild(name);
			likeListElement.appendChild(listItem);
		});

		// Cập nhật số lượng người thích trong tiêu đề
		document.querySelector('#popup-like-list h3').innerHTML = `<img src="/images/love.png" alt="like"> ${users.length}`;
		
		// Hiển thị popup
		document.getElementById('popup-like-list').style.display = 'block';

	} catch (error) {
		console.error('Error loading like list:', error.message);
		alert(error.message);
	}
}




//modell hiển thị ảnh post
let mediaList1 = [];
let currentIndex1 = 0;

function openModalNew(element) {
    const modal = document.getElementById("mediaModal1");
    const image = document.getElementById("modalImage1");
    const video = document.getElementById("modalVideo1");

    const mediaType = element.getAttribute("data-type");
    const mediaSource = element.getAttribute("data-source");
    currentIndex1 = parseInt(element.getAttribute("data-index"), 10);

    if (mediaType === "img") {
        image.src = mediaSource;
        image.style.display = "block";
        video.style.display = "none";
    } else if (mediaType === "video") {
        video.src = mediaSource;
        video.style.display = "block";
        image.style.display = "none";
    }

    modal.style.display = "flex";
}

function closeModalNew() {
    const modal = document.getElementById("mediaModal1");
    const image = document.getElementById("modalImage1");
    const video = document.getElementById("modalVideo1");

    modal.style.display = "none";
    image.src = "";
    video.src = "";
}

function navigateModalNew(direction) {
    currentIndex1 = (currentIndex1 + direction + mediaList1.length) % mediaList1.length;

    const { type, source } = mediaList1[currentIndex1];
    openModalNew({ getAttribute: attr => attr === "data-type" ? type : source });
}

// Populate mediaList1 dynamically
document.addEventListener("DOMContentLoaded", () => {
    const mediaElements = document.querySelectorAll(".image-item1 img, .video-item1 video");
    mediaList1 = Array.from(mediaElements).map(media => ({
        type: media.tagName.toLowerCase() === "img" ? "img" : "video",
        source: media.getAttribute("data-source"),
        index: parseInt(media.getAttribute("data-index"), 10)
    }));
});



//menu bình luận
function toggleMenu1(button) {
    // Tìm phần tử menu chứa trong cùng cấp của button vừa bấm
    const menu1 = button.closest('.menu-container1').querySelector('.menu-options1');
    
    // Kiểm tra trạng thái hiển thị của menu và chuyển đổi
    if (menu1.style.display === "block") {
        menu1.style.display = "none";
    } else {
        menu1.style.display = "block";
    }

    // Đóng menu khi bấm ra ngoài
    document.addEventListener('click', function(event) {
        if (!button.closest('.menu-container1').contains(event.target)) {
            menu1.style.display = "none";  // Ẩn menu nếu bấm ra ngoài
        }
    });
}





function handleDelete1(button) {
    // Hiển thị hộp thoại xác nhận
    const confirmDelete = confirm("Bạn có chắc chắn muốn xóa không?");
    
    if (confirmDelete) {
        // Nếu người dùng chọn "OK", thực hiện hành động xóa
        const commentId = button.id;
        console.log("Đang xóa comment với id:", commentId);
        
        // Gửi yêu cầu DELETE đến server
        fetch(`/user/comments/${commentId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                // Thêm bất kỳ header nào cần thiết (ví dụ, Authorization)
            }
        })
        .then(response => {
            if (response.ok) {
                return response.text();  // Hoặc response.json() nếu trả về JSON
            } else {
                throw new Error('Failed to delete the comment');
            }
        })
        .then(message => {
            console.log(message);  // In ra thông báo thành công
            alert("Bình luận đã được xóa!");
            // Load lại trang
            window.location.reload();
        })
        .catch(error => {
            console.error("Error:", error);
            alert("Có lỗi xảy ra khi xóa bình luận!");
        });
    } else {
        // Nếu người dùng chọn "Cancel", không làm gì cả
        console.log("Hành động xóa bị hủy bỏ");
    }
}

function handleEdit1(button) {
    // Lấy thẻ cha gần nhất (comment-box) từ nút chỉnh sửa
    const commentBox = button.closest(".comment-form-container");

    // Lấy ID của bình luận cần chỉnh sửa
    const commentId = button.id;

    // Lấy nội dung của bình luận từ thẻ comment-content
    const commentContent = commentBox.querySelector(".comment-content p:nth-child(2)").textContent;

    // Đổ nội dung vào input
    const contentInput = commentBox.querySelector("#contentInput");
    contentInput.value = commentContent;

    // Gắn ID bình luận vào input ẩn
    const commentIdInput = commentBox.querySelector("#commentId");
    commentIdInput.value = commentId;
    
    const menu = button.closest(".menu-options1");
    if (menu) {
        menu.style.display = "none"; // Hoặc sử dụng class để ẩn
    }

    // Cuộn xuống và focus vào ô nhập liệu (nếu cần)
    contentInput.focus();
}

// Thay đổi hành động của form dựa trên việc chỉnh sửa
document.getElementById("comment-form").addEventListener("submit", function (event) {
    const commentId = document.getElementById("commentId").value;

    if (commentId) {
        // Nếu có ID bình luận, thực hiện chỉnh sửa
        event.preventDefault(); // Ngăn form gửi request mặc định

        const postId = document.getElementById("postId").value;
        const content = document.getElementById("contentInput").value;

        fetch(`/user/comments/${commentId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                // Thêm header Authorization nếu cần
            },
            body: `content=${content}`,
        })
        .then(response => {
            if (response.ok) {
                return response.text(); // Hoặc response.json()
            } else {
                throw new Error('Failed to update comment');
            }
        })
        .then(message => {
            console.log(message); // Log kết quả trả về
            alert("Bình luận đã được chỉnh sửa!");

            // Cập nhật nội dung bình luận trực tiếp trên giao diện mà không cần load lại trang
            const commentBox = document.querySelector(`button[id="${commentId}"]`).closest(".comment-box");
            commentBox.querySelector(".comment-content p:nth-child(2)").textContent = content;

            // Xóa giá trị input và reset input ẩn
            document.getElementById("contentInput").value = "";
            document.getElementById("commentId").value = "";
        })
        .catch(error => {
            console.error("Error:", error);
            alert("Có lỗi xảy ra khi chỉnh sửa bình luận!");
        });
    }
});
