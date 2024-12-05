package Zabook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import Zabook.models.User;
import Zabook.services.IUserService;
import Zabook.services.impl.UserService;





@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	IUserService userService = new UserService();


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

	
	//lâm
}
