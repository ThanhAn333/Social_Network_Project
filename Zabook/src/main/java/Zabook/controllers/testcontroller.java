package Zabook.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import Zabook.models.Post;
import Zabook.models.test;

@Controller
@RequestMapping("/test")
public class testcontroller {
	@PostMapping("")
	public String createPost() {
		test tes = new test();
		tes.setContent("test");
		return "index";
	   
	}
}
