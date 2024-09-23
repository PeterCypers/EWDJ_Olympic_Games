package com.springBoot.examenTaak;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import domain.Gebruiker;
import domain.Ticket;
import domain.Wedstrijd;
import global.GlobalVariables;
import jakarta.validation.Valid;
import repository.GebruikerRepository;
import repository.SportRepository;
import repository.TicketRepository;
import repository.WedstrijdRepository;
import service.TicketService;
import validator.TicketValidator;

@Controller
@RequestMapping("/koopTickets")
public class TicketKopenController {
	
	@Autowired
	GebruikerRepository gebruikerRepository;
	@Autowired
	SportRepository sportRepository;
	@Autowired
	WedstrijdRepository wedstrijdRepository;
	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
    private TicketService ticketService;
	
	@Autowired
	private TicketValidator ticketValidator;
	
    @ModelAttribute("username")
    public String populateUserName(Principal principal) {
    	return principal.getName();
    }
	
	@GetMapping(value = "/{wedsId}")
	public String showView(@PathVariable("wedsId") int wedsId, Model model, Authentication authentication) {
		int userId = gebruikerRepository.findByUsername(authentication.getName()).getId();
		hulpMethode(wedsId, model, authentication);
				
		int aantalTickets = ticketService.getTicketsGebruikerIdWedstrijdId(userId, wedsId);
		model.addAttribute("ticket", new Ticket(aantalTickets));
		
		
		return "ticketskopen";
	}
	
	@PostMapping(value = "/{wedsId}")
	public String onSubmit(@Valid Ticket ticket, BindingResult result,
			Model model, @PathVariable("wedsId") int wedsId, Authentication authentication, RedirectAttributes ra) {
		
		Gebruiker loggedInUser = gebruikerRepository.findByUsername(authentication.getName());
		int userId = loggedInUser.getId();
		hulpMethode(wedsId, model, authentication);
		
		//work-around -> geen verbinding mogelijk met ongebonden ticket + wedstrijd
		GlobalVariables.vrijePlaatsen = wedstrijdRepository.findById(wedsId).get().getAantal_plaatsen();
		
		ticketValidator.validate(ticket, result);
		
		if(result.hasErrors()) {
			//ticket vergeet zijn aantal telkens bij reloading
			int aantalTickets = ticketService.getTicketsGebruikerIdWedstrijdId(userId, wedsId);			
			ticket.setAantalTickets(aantalTickets);
			
			return "ticketskopen";
		}
		
		
		//bestaat ticket(Ticket != null)? -> update in DB, bestaat geen ticket? -> ticket obj behouden, binden + persisteren
		Ticket databaseTicket = ticketService.findTicketByGebruikerIdWedstrijdId(userId, wedsId);
		Wedstrijd wedstrijdWaarTicketBijHoort = wedstrijdRepository.findById(wedsId).get();
		int aantalGekocht = ticket.getTicketsInAankoop();
		
		
		if(databaseTicket != null) {
			//update database ticket met nieuwe waarden: (+ticketsInAankoop resetten)
			//case ticket bestaat al: aantal aangekocht bij totaal voegen voor Gebruiker
			int newTicketTotal = loggedInUser.getTotalTickets() + aantalGekocht;
			loggedInUser.setTotalTickets(newTicketTotal);
			
			databaseTicket.increaseQuantity(aantalGekocht);
			databaseTicket.setTicketsInAankoop(0);
			ticketRepository.save(databaseTicket); //pas de DB-ticket aan
		}else {
			//case: geen ticket in database -> bind + persisteer de form-ticket
			//bij het binden worden Gebruiker's totaal tickets automatisch geupdatet
			//aantalTickets vermeerderen met ticketsInAankoop + ticketsInAankoop resetten
			ticket.setAantalTickets(aantalGekocht);
			ticket.setTicketsInAankoop(0);
			wedstrijdWaarTicketBijHoort.addTicket(ticket);
			loggedInUser.addTicket(ticket);
			ticketRepository.save(ticket); //persisteer de form-ticket
		}
		
		//gebruiker zal altijd veranderd zijn
		gebruikerRepository.save(loggedInUser);
		//wedstrijd zal mogelijk ook veranderd zijn: aantal_plaatsen -= tickets gekocht (niet veranderd case: 0tickets)
		wedstrijdWaarTicketBijHoort.vulPlaatsen(aantalGekocht);
		wedstrijdRepository.save(wedstrijdWaarTicketBijHoort);
		
		//bericht terugsturen:
		String sportNaam = sportRepository.findSportByWedstrijdId(wedsId).getNaam();
		ra.addFlashAttribute("aantalGekocht", aantalGekocht);
		
		return String.format("redirect:/wedstrijden/%s", sportNaam);
	}
	
	/**
	 * 
	 * deze hulpmethode zet de variabelen in de modelview
	 * 
	 * @param wedsId
	 * @param model
	 * @param authentication
	 */
	private void hulpMethode(int wedsId, Model model, Authentication authentication) {
		String role = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().get();
		int userId = gebruikerRepository.findByUsername(authentication.getName()).getId();
        int ticketTotal = gebruikerRepository.findByUsername(authentication.getName()).getTotalTickets();
        String sportNaam = sportRepository.findSportByWedstrijdId(wedsId).getNaam();
        String stadiumNaam = wedstrijdRepository.findById(wedsId).get().getStadium_naam();
		model.addAttribute("userId", userId);
		model.addAttribute("role", role);
		model.addAttribute("ticketTotal", ticketTotal);
		model.addAttribute("sportName",sportNaam);
		model.addAttribute("stadiumNaam", stadiumNaam);
	}
	

}
