package dto;

import domain.Gebruiker;
import domain.Sport;
import domain.Ticket;
import domain.Wedstrijd;

public class TicketDetailsDTO {
    private Ticket ticket;
    private Gebruiker gebruiker;
    private Wedstrijd wedstrijd;
    private Sport sport;

    public TicketDetailsDTO(Ticket ticket, Gebruiker gebruiker, Wedstrijd wedstrijd, Sport sport) {
        this.ticket = ticket;
        this.gebruiker = gebruiker;
        this.wedstrijd = wedstrijd;
        this.sport = sport;
    }

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public Gebruiker getGebruiker() {
		return gebruiker;
	}

	public void setGebruiker(Gebruiker gebruiker) {
		this.gebruiker = gebruiker;
	}

	public Wedstrijd getWedstrijd() {
		return wedstrijd;
	}

	public void setWedstrijd(Wedstrijd wedstrijd) {
		this.wedstrijd = wedstrijd;
	}

	public Sport getSport() {
		return sport;
	}

	public void setSport(Sport sport) {
		this.sport = sport;
	}

    
}
