package com.one.exam.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.one.exam.models.Mesa;
import com.one.exam.models.User;
import com.one.exam.services.MesaService;
import com.one.exam.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	private UserService userService;
	private MesaService mesaService;
	
	public HomeController(UserService userService, MesaService mesaService) {
		this.userService = userService;
		this.mesaService = mesaService;
	}
	
	@GetMapping("/")
	 public String registerForm(@ModelAttribute("user") User user) {
	     return "reglogPage";
	 }
	
    @GetMapping("/home")
    public String home(HttpSession session, Model model, RedirectAttributes redirect) {
    	if(session.getAttribute("userId") == null) {
    		redirect.addFlashAttribute("sessionInv", "You haven't logged in yet");
    		return "redirect:/";
    	}
    	Object idU = session.getAttribute("userId");
		User user = userService.findById((Long) idU);
		model.addAttribute("user", user);
		List<Mesa> mesas = mesaService.findAll();
		List<Mesa> mesasPropias = new ArrayList<>();
		for(int i = 0; i < mesas.size(); i++) {
			if(mesas.get(i).getUser() != null && mesas.get(i).getUser().getId() == user.getId()) {
				mesasPropias.add(mesas.get(i));
			}
		} if(!mesasPropias.isEmpty()) {
			model.addAttribute("mesasPropias", mesasPropias);
		}
		return "AllS";
    }
    
    @GetMapping("/error")
    public String handleError() {
        return "error"; 
    }
    
    @PostMapping("/error")
    public String handleErrors() {
        return "error"; 
    }
    
}
