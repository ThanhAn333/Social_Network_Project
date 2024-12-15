package Zabook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Zabook.services.IPostService;
import Zabook.services.IUserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private IUserService userService;
    

    @Autowired
    private IPostService postService;

    @GetMapping("/")
    public String admin(Model model) {
        model.addAttribute("users", userService.getAllUsersWithRoleUser());
        model.addAttribute("posts", postService.getAllPost());
        return "admin/index";
    }

     @PostMapping("/lockUser/{id}")
    public String lockUser(@PathVariable("id") String id) {
        userService.lockUser(id); // Gọi service để khóa người dùng
        return "redirect:/admin/"; // Điều hướng lại danh sách người dùng
    }

    
}
