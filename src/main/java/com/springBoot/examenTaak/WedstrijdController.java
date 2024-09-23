package com.springBoot.examenTaak;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import domain.Sport;
import domain.Wedstrijd;
import repository.GebruikerRepository;
//import mockdata.WedstrijdMock;
import repository.SportRepository;
import repository.TicketRepository;
import repository.WedstrijdRepository;
import service.TicketService;

@Controller
@RequestMapping("/wedstrijden")
public class WedstrijdController {
	
    @ModelAttribute("username")
    public String populateUserName(Principal principal) {
    	return principal.getName();
    }
    
//    @Autowired
//    WedstrijdMock wedstrijdLookup;
    @Autowired
    WedstrijdRepository wedstrijdRepository;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    SportRepository sportRepository;
	@Autowired
	GebruikerRepository gebruikerRepository;
	@Autowired
    private TicketService ticketService;
    
    @GetMapping(value = "/{sportName}")
    public String showView(@PathVariable("sportName") String sportName, Model model, Authentication authentication) {
    	
    	String role = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().get();
    	int userId = gebruikerRepository.findByUsername(authentication.getName()).getId();
    	int sportId = sportRepository.findByNaam(sportName).getSportId();
    	
    	model.addAttribute("role", role);
    	model.addAttribute("sportId", sportId);
    	model.addAttribute("sportName", sportName);
    	
    	List<Wedstrijd> wedstrijdList = wedstrijdRepository.gesorteerdeWedstrijden(sportId);
    	Map<Integer, Integer> ticketsPerWedstrijdId = new HashMap<>();
    	
    	for (Wedstrijd wedstrijd : wedstrijdList) {
    		int aantalTickets = ticketService.getTicketsGebruikerIdWedstrijdId(userId, wedstrijd.getWedstrijdId());
    		ticketsPerWedstrijdId.put(wedstrijd.getWedstrijdId(), aantalTickets);
    	}
    	
    	model.addAttribute("wedstrijdList", wedstrijdList);
    	model.addAttribute("ticketsPerWedstrijdId", ticketsPerWedstrijdId);
    	return "wedstrijden";
    }
    
}
