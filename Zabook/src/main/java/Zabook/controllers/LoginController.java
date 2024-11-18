package Zabook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.RequestParam;
=======
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
>>>>>>> 6db718dbe2cbc58b0c99306c4ce445e94c84f9e4

import Zabook.models.User;
import Zabook.services.IUserService;

@Controller
<<<<<<< HEAD
public class LoginController {

    @Autowired
    private IUserService userService;

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,@RequestParam String pass, Model model) {
        

        User user = userService.getUserByEmail(email);

        if (user != null && user.getPassword().equals(pass)) {
            return "redirect:/home";
        } else {
            model.addAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không đúng!");
            return "login";
        }
    }
=======
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private IUserService userService;
	
	@GetMapping
    public String showLoginPage() {
        return "login";
    }
	
	@PostMapping	
    public ModelAndView login(@RequestBody User loginRequest, Model model) {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        User user = userService.getUserByEmail(username);

        if (user != null && user.getPassword().equals(password)) {
            
            return new ModelAndView("home");
        } else {
            
            model.addAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không đúng!");
            return new ModelAndView("login");  
        }
    }

>>>>>>> 6db718dbe2cbc58b0c99306c4ce445e94c84f9e4
}
