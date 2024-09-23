package domain;

import java.io.Serializable;
import java.time.LocalDate;
//import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.LocalDateDeserializer;
import utils.LocalDateSerializer;
import utils.LocalTimeDeserializer;
import utils.LocalTimeSerializer;

@Entity
@NamedQueries({
	@NamedQuery(name = "Wedstrijd.gesorteerdeWedstrijden",
	query = "SELECT w FROM Wedstrijd w WHERE w.sport.sportId = :sportId ORDER BY w.startDatum, w.startTijd")
})
@Builder
@Getter @Setter
@NoArgsConstructor//(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode(exclude = "wedstrijdId")
@Table(name = "wedstrijd")
@JsonPropertyOrder({"wedstrijd_id","stadium_naam","start_datum","start_tijd"})
public class Wedstrijd implements Serializable  {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wedstrijd_id")
	@JsonProperty("wedstrijd_id")
	private int wedstrijdId;
	
	@ManyToOne
	@JoinColumn(name = "sport_id")
	@JsonIgnore //exclude uit de api-request
	private Sport sport;
	
	@OneToMany(mappedBy = "wedstrijd")
	@JsonIgnore //exclude uit de api-request
	private final List<Ticket> tickets = new ArrayList<>();
	
	//~ final
	//@Builder.Default
//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(name = "wedstrijd_discipline",
//            joinColumns = @JoinColumn(name = "wedstrijd_id"),
//            inverseJoinColumns = @JoinColumn(name = "discipline_id"))
//    private final Set<Discipline> disciplines = new HashSet<>();


	@Column(name = "start_datum")
	@NotNull
	@JsonProperty("start_datum")
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate startDatum;
	
	@Column(name = "start_tijd")
	@NotNull
	@JsonProperty("start_tijd")
	@JsonSerialize(using = LocalTimeSerializer.class)
	@JsonDeserialize(using = LocalTimeDeserializer.class)
	private LocalTime startTijd;
	
	@Column(name = "o_nr1")
	@JsonProperty("o_nr1")
	private int oNr1;
	
	@Column(name = "o_nr2")
	@JsonProperty("o_nr2")
	private int oNr2;
	
	@Column(name = "ticket_prijs")
	@DecimalMin(value = "1.0", message="{wedstrijd.ticketPrijs.DecimalMin.message}")
	@DecimalMax(value = "149.99", message="{wedstrijd.ticketPrijs.DecimalMax.message}")
	@JsonProperty("ticket_prijs")
	private double ticketPrijs;
	
	//custom validation d1 != d2
	@JsonProperty("discipline1")
	private String discipline1;
	
	@JsonProperty("discipline2")
	private String discipline2;
	
	@NotNull
	@JsonProperty("stadium_naam")
	private String stadium_naam;

	@NotNull
	@Range(min= 0, max=50, message="{wedstrijd.aantal_plaatsen.Range.message}")
	@JsonProperty("aantal_plaatsen")
	private int aantal_plaatsen;
	//private int vrije_plaatsen;
	
    public void addTicket(Ticket ticket) {
    	ticket.setWedstrijd(this);
    	
        tickets.add(ticket);
    }
    
    public void vulPlaatsen(int aantalGekochteTickets) {
    	if (this.aantal_plaatsen - aantalGekochteTickets < 0)
    		throw new IllegalArgumentException("Wedstrijd - aantal gekochte tickets is meer dan aantal vrije plaatsen");
    	this.aantal_plaatsen -= aantalGekochteTickets;
    }
    
    @Override
    public String toString() {
    	return String.format("id: %d, Sport: %s, Tickets: %s, StartDatum: %s, StartTijd: %s, oNr1: %d, oNr2: %d, prijs: %f, disci1: %s, disci2: %s, stadium: %s, aantalplaatsen: %d",
    			wedstrijdId, sport, tickets, startDatum, startTijd, oNr1, oNr2, ticketPrijs, discipline1, discipline2,
    			stadium_naam, aantal_plaatsen);
    }
    
    public String toDemoString() {
        return String.format("wedstrijd_id: %d, start_datum: %s, start_tijd: %s, o_nr1: %d, o_nr2: %d, ticket_prijs: %.2f,"
        		+ " discipline1: %s, discipline2: %s, stadium_naam: %s, aantal_plaatsen: %d",
            wedstrijdId, startDatum, startTijd, oNr1, oNr2, ticketPrijs, discipline1, discipline2, stadium_naam, aantal_plaatsen);
    }
	
	
}
