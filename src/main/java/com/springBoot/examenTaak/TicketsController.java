package com.springBoot.examenTaak;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dto.TicketDetailsDTO;
import repository.GebruikerRepository;
import repository.SportRepository;
import service.TicketService;

@Controller
@RequestMapping("/tickets")
public class TicketsController {
	
	
    @ModelAttribute("username")
    public String populateUserName(Principal principal) {
    	return principal.getName();
    }
    
    @Autowired
    TicketService ticketService;
	
	@Autowired
	SportRepository sportRepository;
	
	@Autowired
	GebruikerRepository gebruikerRepository;
		
	//postmapping ipv getmapping: values in req-body ipv url
	@PostMapping
	public String showView(Model model, Authentication authentication, @RequestParam("userId") int userId) {
		List<TicketDetailsDTO> ticketDetails = ticketService.findTicketsByUserIdWithDetails(userId);
		model.addAttribute("userId", userId);
		model.addAttribute("totalTickets", gebruikerRepository.findById(userId).get().getTotalTickets());
		model.addAttribute("tickDetDTOList", ticketDetails);
		return "tickets";
	}

}
