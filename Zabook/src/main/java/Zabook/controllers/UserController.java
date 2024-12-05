package Zabook.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;




@Controller
@RequestMapping("/user")
public class UserController {

	@GetMapping("/")
	public String getMethodName() {
		return "user/index";
	}

	//lâm
	@GetMapping("/header")
	public String header() {
		return "user/header";
	}
}
