package Zabook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import Zabook.models.User;
import Zabook.services.IUserService;
import Zabook.services.impl.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserService userService = new UserService();

    @Value("${upload.directory}")
    private String uploadDir;

    @GetMapping("/")
    public String getMethodName() {

        return "user/index";
    }

    //lâm
    @GetMapping("/profile")
    public String profile(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("currentuser", user);
        return "user/profile-page";
    }

    @PostMapping("/profile/save_avatar")
    public String changeAvatar(
            @RequestParam(value = "avatar", required = false) MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            Path path = Paths.get(uploadDir + "/" + fileName);
            Files.write(path, file.getBytes());

            User user = userService.getCurrentUser();
            user.setAvatar("/images/data/" + fileName);
            userService.updateUser(user);
        } catch (IOException e) {
            return "redirect:/user/profile";
        }

        return "redirect:/user/profile";
    }

    @PostMapping("/profile/update_bio")
    @ResponseBody
    public ResponseEntity<String> updateBio(@RequestBody Map<String, String> requestData) {
        String bio = requestData.get("bio");

        // Lấy người dùng hiện tại
        User currentUser = userService.getCurrentUser();
        currentUser.setBio(bio);

        // Cập nhật thông tin
        userService.updateUser(currentUser);

        return ResponseEntity.ok("Tiểu sử đã được cập nhật!");
    }

    @PostMapping("/profile/update_profile")
    @ResponseBody
    public ResponseEntity<String> updateProfile(@RequestBody Map<String, String> requestData) {
        String gender = requestData.get("gender");
        String address = requestData.get("address");

        // Lấy người dùng hiện tại
        User currentUser = userService.getCurrentUser();
        currentUser.setGender(gender);
        currentUser.setAddress(address);

        // Lưu thay đổi
        userService.updateUser(currentUser);
        return ResponseEntity.ok("Thông tin đã được cập nhật!");
    }
    //lâm
}
