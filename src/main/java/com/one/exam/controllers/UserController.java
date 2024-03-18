package com.one.exam.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.one.exam.models.User;
import com.one.exam.services.UserService;
import com.one.exam.validator.UserValidator;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class UserController {
	
	private UserService userService;
    private UserValidator userValidator;
    
    public UserController(UserService userService, UserValidator userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }
        
    @RequestMapping(value="/registration", method=RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, HttpSession session, RedirectAttributes redirect) {
        userValidator.validate(user, result);
        if(userService.findByEmail(user.getEmail())!= null) {
        	redirect.addFlashAttribute("correoDup", "This email has already been registered");
        	return "redirect:/";
        } if(result.hasErrors()) {
            return "reglogPage";
        } 
        User u = userService.registerUser(user);
        session.setAttribute("userId", u.getId());
        return "redirect:/home";
    }
    
    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password, Model model, HttpSession session, RedirectAttributes redirect) {
    	if(userService.findByEmail(email) == null) {
    		redirect.addFlashAttribute("errorL", "Email not yet registered");
    		return "redirect:/";
    	}
    	if(!userService.authenticateUser(email, password)) {
    		redirect.addFlashAttribute("errorL", "Incorrect email or password");
    		return "redirect:/";
    	} else {
    		User user = userService.findByEmail(email);
    		Long id = user.getId();
    		session.setAttribute("userId", id);
    		return "redirect:/home";
    	}
    }
    
    @RequestMapping("/logout")
	 public String logout(HttpSession session) {
		 session.invalidate();
		 return "redirect:/";
	 }
}
