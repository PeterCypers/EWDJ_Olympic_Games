package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "username")
@Table(name = "gebruikers")
public class Gebruiker implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false, unique = true)
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private Rol rol;
	
	@OneToMany(mappedBy = "gebruiker")
	private final List<Ticket> tickets = new ArrayList<>();
	
	private int totalTickets;
	
	//private transient Map<String, Integer> ticketmap;
    public void addTicket(Ticket ticket) {
    	ticket.setGebruiker(this);
    	
    	increaseTickets(ticket.getAantalTickets());
    	
        tickets.add(ticket);
    }
    
    private void increaseTickets(int amount) {
    	if(totalTickets + amount > 100) {
    		throw new IllegalArgumentException("passed maximum tickets per user");
    	}
    	totalTickets += amount;
    	
    }
	

}
