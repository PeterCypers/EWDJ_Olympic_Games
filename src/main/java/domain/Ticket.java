package domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NamedQueries({
    @NamedQuery(
        name = "Ticket.findTicketsByUserIdWithDetails",
        query = "SELECT t, g, w, s " +
                "FROM Ticket t " +
                "JOIN t.gebruiker g " +
                "JOIN t.wedstrijd w " +
                "JOIN w.sport s " +
                "WHERE g.id = :userId " +
                "ORDER BY s.naam, w.startDatum"
    ),
    @NamedQuery(
    	    name = "Ticket.findTicketCountByUserIdAndWedstrijdId",
    	    query = "SELECT t.aantalTickets FROM Ticket t WHERE t.gebruiker.id = :userId AND t.wedstrijd.id = :wedstrijdId"
    	),
    @NamedQuery(
    		name = "Ticket.findTicketByGebruikerIdWedstrijdId",
    		query = "SELECT t FROM Ticket t WHERE t.gebruiker.id = :userId AND t.wedstrijd.id = :wedstrijdId")
})
@Table(name = "tickets")
@Data
@NoArgsConstructor//(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
public class Ticket implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ticket_id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "wedstrijd_id"/*, referencedColumnName = "wedstrijd_id"*/)
	private Wedstrijd wedstrijd;
	
	@ManyToOne
	@JoinColumn(name = "gebruiker_id"/*, referencedColumnName = "id"*/)
	private Gebruiker gebruiker;
	
	@Column(name = "ticket_quantity")
	private int aantalTickets;
	
	//nieuw veld toegevoegd om het leven makkelijker te maken
	@Min(value = 0, message="{ticket.ticketsInAankoop.min.message}")
	private transient int ticketsInAankoop;
	
    public Ticket(/*Wedstrijd wedstrijd, Gebruiker gebruiker, */int aantalTickets) {
//        this.wedstrijd = wedstrijd;
//        this.gebruiker = gebruiker;
        this.aantalTickets = aantalTickets;
    }
    
    public void increaseQuantity(int amount) {
    	if(aantalTickets + amount > 20)
    		throw new IllegalArgumentException("Meer dan 20 tickets voor 1 wedstrijd...");
    	this.aantalTickets += amount;
    }

}
