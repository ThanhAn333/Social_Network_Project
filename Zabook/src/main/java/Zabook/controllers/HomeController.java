package Zabook.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {
<<<<<<< HEAD

    public HomeController() {
    }

    @GetMapping("")
    public String home() {
        return "index";
    }
=======
	@GetMapping("")
	public String index() {
		return "index";
	}
>>>>>>> 6db718dbe2cbc58b0c99306c4ce445e94c84f9e4
}
