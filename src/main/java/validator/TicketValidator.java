package validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import domain.Gebruiker;
import domain.Ticket;
import repository.GebruikerRepository;
import global.GlobalVariables;

public class TicketValidator implements Validator {
	
	@Autowired
	private GebruikerRepository gebruikerRepository;
	

	@Override
	public boolean supports(Class<?> klasse) {
		return Ticket.class.isAssignableFrom(klasse);
	}

	/**
	 * mogelijk geen link tussen Ticket <-> Gebruiker -> security authentication context nodig
	 * alsook een global variable die tijdelijk de aantal vrije plaatsen in wedstrijd bijhoud<br>
	 * <strong>rules:</strong>
	 * <ul>
	 * <li>gebruiker mag niet meer dan 100 tickets in totaal hebben
	 * <li>gebruiker mag niet meer dan 20 tickets per wedstrijd kopen
	 * <li>gebruiker kan niet meer tickets kopen dan vrije plaatsen beschikbaar voor die wedstrijd
	 * </ul>
	 */
	@Override
	public void validate(Object target, Errors errors) {
		Ticket ticket = (Ticket) target;
		int aantalTickets = ticket.getAantalTickets();
		int ticketsInAankoop = ticket.getTicketsInAankoop();
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Gebruiker ingelogdeGebruiker = gebruikerRepository.findByUsername(authentication.getName());
		int vrijePlaatsen = GlobalVariables.vrijePlaatsen;
		
		if(ingelogdeGebruiker.getTotalTickets() + ticketsInAankoop > 100) {
			errors.rejectValue("ticketsInAankoop", "ticket.meerdanHonderd", "meer dan 100 tikets");
		}else if(ticketsInAankoop > vrijePlaatsen) {
			errors.rejectValue("ticketsInAankoop", "ticket.meerticketsDanVrijePlaatsen", "geen plaatsen over in wedstrijd");
		}else if(aantalTickets + ticketsInAankoop > 20) {
			errors.rejectValue("ticketsInAankoop", "ticket.wedstrijdOverschreden", "te veel tickets van 1 wedstrijd");
		}
		
		
	}
	
}
