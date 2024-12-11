document.getElementById("sendCommentBtn").addEventListener("click", function () {
    const content = document.getElementById("contentInput").value;
    const postId = "6732221ed711a16b44025e36"; // Thay bằng ID bài viết thực tế
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
