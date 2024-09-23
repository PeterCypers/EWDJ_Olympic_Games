package com.springBoot.examenTaak;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import mockdata.SportMock;
import repository.GebruikerRepository;
import repository.SportRepository;

@Controller
@RequestMapping("/sports")
public class SportsController {
	//mockdata:
//	@Autowired
//	SportMock sportLookup;
//	
//	@GetMapping
//	public String showView(Model model) {
//		model.addAllAttributes(sportLookup.getSports());
//		return "sports";
//	}
	
    @ModelAttribute("username")
    public String populateUserName(Principal principal) {
    	return principal.getName();
    }
	
	@Autowired
	SportRepository sportRepository;
	
	@Autowired
	GebruikerRepository gebruikerRepository;
	
	@GetMapping
	public String showView(Model model, Authentication authentication) {
		//de ingelogde gebruiker + rollen (username + password zijn overal beschikbaar?)
        //model.addAttribute("username", authentication.getName());
		String role = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().get();
		int userId = gebruikerRepository.findByUsername(authentication.getName()).getId();
        int ticketTotal = gebruikerRepository.findByUsername(authentication.getName()).getTotalTickets();
		model.addAttribute("userId", userId);
		model.addAttribute("role", role);
		model.addAttribute("ticketTotal", ticketTotal);
		
		sportRepository.findAll().forEach((s) -> {
			model.addAttribute(String.format("sport%d", s.getSportId()),s);
		});
		
		//date today:
		LocalDate todayLocalDate = LocalDate.now();
		model.addAttribute("todayLocalDate", todayLocalDate);
		
		return "sports";
	}
	
	

}
