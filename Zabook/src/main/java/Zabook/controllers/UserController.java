package Zabook.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import org.springframework.web.bind.annotation.GetMapping;




@Controller
@RequestMapping("/user/")
public class UserController {

	@GetMapping("/")
	public String getMethodName() {
		return "user/index";
	}
	
}
