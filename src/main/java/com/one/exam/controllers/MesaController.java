package com.one.exam.controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.one.exam.models.Mesa;
import com.one.exam.models.User;
import com.one.exam.services.MesaService;
import com.one.exam.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class MesaController {
	
	private UserService userService;
	private MesaService mesaService;
	
	public MesaController(MesaService mesaService, UserService userService){
		this.mesaService = mesaService;
		this.userService = userService;
	}
	
	@GetMapping("/tables/new")
	public String create(@ModelAttribute("mesa") Mesa mesa, HttpSession session, RedirectAttributes redirect) {
		if(session.getAttribute("userId") == null) {
    		redirect.addFlashAttribute("sessionInv", "You haven't logged in yet");
    		return "redirect:/";
    	} return "NewTable";
	}
	
	@PostMapping("/tables/new")
	public String createV(@Valid @ModelAttribute("mesa") Mesa mesa, BindingResult result,  HttpSession session, RedirectAttributes redirect) {
		if(session.getAttribute("userId") == null) {
    		redirect.addFlashAttribute("sessionInv", "You haven't logged in yet");
    		return "redirect:/";
    	} if(result.hasErrors()) {
    		return "NewTable";
    	} Object idU = session.getAttribute("userId");
		 User user = userService.findById((Long) idU);
    	mesa.setUser(user);
    	mesaService.save(mesa);
    	return "redirect:/home";
	}
	
	@GetMapping("/tables/{id}/edit")
	 public String edit(@PathVariable Long id, HttpSession session, Model model, @ModelAttribute("mesa") Mesa mesa, RedirectAttributes redirect) {
		if(session.getAttribute("userId") == null) {
			redirect.addFlashAttribute("sessionInv", "You haven't logged in yet");
			return "redirect:/";
		}  if(mesaService.findById(id) == null) {
    		redirect.addFlashAttribute("withoutUser", "That table does not exist");
			return "redirect:/home";
    	} if(mesaService.findById(id).getUser() == null || userService.findById((Long) session.getAttribute("userId")).getId() != mesaService.findById(id).getUser().getId()) {
			redirect.addFlashAttribute("credentials", "Esa mesa no la diriges tú");
			return "redirect:/home";
		} Mesa mesaCreada = mesaService.findById(id);
		model.addAttribute("mesaCreada", mesaCreada);
		return "EditTable";
	 }
	
	@PostMapping("/tables/{id}/edit")
	public String editV(@PathVariable Long id, @Valid @ModelAttribute("mesa") Mesa mesa, BindingResult result, Model model, HttpSession session, RedirectAttributes redirect) {
		if(session.getAttribute("userId") == null) {
    		redirect.addFlashAttribute("sessionInv", "You haven't logged in yet");
    		return "redirect:/";
    	}  if(mesaService.findById(id) == null) {
    		redirect.addFlashAttribute("withoutUser", "That table does not exist");
			return "redirect:/home";
    	} if(mesaService.findById(id).getUser() == null || userService.findById((Long) session.getAttribute("userId")).getId() != mesaService.findById(id).getUser().getId()) {
			redirect.addFlashAttribute("credentials", "You don't run that table");
			return "redirect:/home";
		} Mesa mesaCreada = mesaService.findById(id);
    	  if(result.hasErrors()) {
    		model.addAttribute("mesaCreada", mesaCreada);
    		return "EditTable";
    	} mesaCreada.setGuestName(mesa.getGuestName());
    	mesaCreada.setGuests(mesa.getGuests());
    	mesaCreada.setNotes(mesa.getNotes());
    	mesaService.save(mesaCreada);
		return "redirect:/home";
	}
	
	@GetMapping("/tables/{id}/delete")
	public String validation(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes redirect) {
		if(session.getAttribute("userId") == null) {
    		redirect.addFlashAttribute("sessionInv", "You haven't logged in yet");
    		return "redirect:/";
    	}  if(mesaService.findById(id) == null) {
    		redirect.addFlashAttribute("withoutUser", "That table does not exist");
			return "redirect:/home";
    	} if(mesaService.findById(id).getUser() == null || userService.findById((Long) session.getAttribute("userId")).getId() != mesaService.findById(id).getUser().getId()) {
			redirect.addFlashAttribute("credentials", "You don't run that table");
			return "redirect:/home";
		} Mesa mesa = mesaService.findById(id);
    	model.addAttribute("mesa", mesa);
    	return "Validation";
	}
	
	@GetMapping("/tables/{id}/delete/confirm")
	public String validation(@PathVariable Long id, HttpSession session, RedirectAttributes redirect) {
		if(session.getAttribute("userId") == null) {
    		redirect.addFlashAttribute("sessionInv", "You haven't logged in yet");
    		return "redirect:/";
    	}  if(mesaService.findById(id) == null) {
    		redirect.addFlashAttribute("withoutUser", "That table does not exist");
			return "redirect:/home";
    	} if(mesaService.findById(id).getUser() == null || userService.findById((Long) session.getAttribute("userId")).getId() != mesaService.findById(id).getUser().getId()) {
			redirect.addFlashAttribute("credentials", "You don't run that table");
			return "redirect:/home";
		} mesaService.delete(id);
		return "redirect:/home";
	}
	
	@GetMapping("/tables")
	public String tables(HttpSession session, Model model, RedirectAttributes redirect) {
		if(session.getAttribute("userId") == null) {
    		redirect.addFlashAttribute("sessionInv", "You haven't logged in yet");
    		return "redirect:/";
    	} List<Mesa> mesas = mesaService.findAll();
    	List<Mesa> mesasWithout = new ArrayList<>();
    	for(int i = 0; i < mesas.size(); i++) {
    		if(mesas.get(i).getUser() == null) {
    			mesasWithout.add(mesas.get(i));
    		}
    	} if(!mesasWithout.isEmpty()) {
    		model.addAttribute("mesasWithout", mesasWithout);
    	}
		return "TablesWithout";
	}
	
	@GetMapping("/tables/{id}/give")
	public String give(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes redirect) {
		if(session.getAttribute("userId") == null) {
    		redirect.addFlashAttribute("sessionInv", "You haven't logged in yet");
    		return "redirect:/";
    	}  if(mesaService.findById(id) == null) {
    		redirect.addFlashAttribute("withoutUser", "That table does not exist");
			return "redirect:/home";
    	} if(mesaService.findById(id).getUser() == null) {
    		redirect.addFlashAttribute("withoutUser", "That table already has an owner");
			return "redirect:/home";
    	} if(userService.findById((Long) session.getAttribute("userId")).getId() != mesaService.findById(id).getUser().getId()) {
			redirect.addFlashAttribute("credentials", "You don't run that table");
			return "redirect:/home";
		} Mesa mesa = mesaService.findById(id);
    	mesa.setUser(null);
    	mesaService.update(mesa);
    	return "redirect:/home";
	}
	
	@GetMapping("/tables/{id}/pick")
	public String pick(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes redirect) {
		if(session.getAttribute("userId") == null) {
    		redirect.addFlashAttribute("sessionInv", "You haven't logged in yet");
    		return "redirect:/";
    	} if(mesaService.findById(id) == null) {
    		redirect.addFlashAttribute("withoutUser", "That table does not exist");
			return "redirect:/home";
    	} if(mesaService.findById(id).getUser() != null) {
    		redirect.addFlashAttribute("withoutUser", "That table already has an owner");
			return "redirect:/home";
    	} Mesa mesa = mesaService.findById(id);
    	mesa.setUser(userService.findById((Long) session.getAttribute("userId")));
    	mesaService.update(mesa);
    	return "redirect:/home";
	}
}
