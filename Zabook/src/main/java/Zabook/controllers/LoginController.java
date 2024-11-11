package Zabook.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import Zabook.models.User;
import Zabook.services.IUserService;

@Controller
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

}
