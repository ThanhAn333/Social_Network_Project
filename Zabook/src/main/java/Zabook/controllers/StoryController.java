package Zabook.controllers;

import java.io.File;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import Zabook.models.Story;
import Zabook.models.User;
import Zabook.services.IStoryService;
import Zabook.services.IUserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
@RequestMapping("/user/story")
public class StoryController {
    @Autowired
    private IStoryService storyService;
    
    @Autowired
    private IUserService userService;
    
    @GetMapping("")
	public String index() {
		return "index";
	}

    @GetMapping("/user/")
	public String getUserStorys(Principal principal, Model model) {

        List<Story> list = storyService.getUserStories(userService.getCurrentBuyerId(principal));
		model.addAttribute("stories", list);
		return "index";
	}
    


    @PostMapping("/createStory")
    public String postMethodName(@RequestParam(value = "file", required = false) MultipartFile file,
                          @RequestParam(value = "textContent", required = false) String textContent,
                          Model model, Principal principal) {
        
        String staticDir = new File("src/main/resources/static").getAbsolutePath();
		String imageDir = staticDir + "/uploads/images/";
		String videoDir = staticDir + "/uploads/videos/";
        try {
        Story story = new Story();
        story.setTimestamp(LocalDateTime.now().toString());
        story.setActive(true);
        ObjectId userid = userService.getCurrentBuyerId(principal);
		User user = new User();
		user.setUserID(userid);
        story.setUser(user);
        story.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        if (textContent != null && !textContent.isEmpty()) {
            story.setTextContent(textContent);
        } else if (file != null && !file.isEmpty()) {
            String contentType = file.getContentType();
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            if (contentType != null && contentType.startsWith("image")) {
                // Lưu ảnh vào thư mục images
                File destinationFile = new File(imageDir + fileName);
                file.transferTo(destinationFile);

                String imageUrl = "/uploads/images/" + fileName;
                story.setImage(imageUrl);
            } else if (contentType != null && contentType.startsWith("video")) {
                // Lưu video vào thư mục videos
                File destinationFile = new File(videoDir + fileName);
                file.transferTo(destinationFile);

                String videoUrl = "/uploads/videos/" + fileName;
                story.setVideo(videoUrl);
            } else {
                model.addAttribute("error", "File không hợp lệ! Vui lòng chọn hình ảnh hoặc video.");
                return "error";
            }
        } else {
            model.addAttribute("error", "Vui lòng chọn một nội dung để đăng story!");
            return "error";
        }


        // Lưu vào database
        storyService.createStory(story);
        System.out.println("Story created successfully!");
        return "redirect:/user/";
       
    } catch (Exception e) {
        model.addAttribute("error", "Đã xảy ra lỗi khi tạo story!");
        return "error";
    }
    }
    
}
