package Zabook.controllers;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import Zabook.services.IPostService;
import Zabook.services.IUserService;
import groovy.lang.Lazy;
import jakarta.ws.rs.Path;

import org.springframework.web.bind.annotation.RequestBody;


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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
     @PostMapping("/lockUser/{id}")
    public String lockUser(@PathVariable("id") String id) {
        userService.lockUser(id); 
        return "redirect:/admin/"; 
    }

    @PostMapping("/deletepost/{postId}")
    public String postMethodName(@PathVariable("postId") String postId) {
        postService.deletePost(new ObjectId(postId));
        return "redirect:/admin/";
    }
    

    
}
