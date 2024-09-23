package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domain.Gebruiker;
import domain.Sport;
import domain.Ticket;
import domain.Wedstrijd;
import dto.TicketDetailsDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import repository.TicketRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {
	
	// (A)
	@Autowired
	private TicketRepository ticketRepository;
	
	// (B)
	@PersistenceContext
    private EntityManager entityManager;


    public List<TicketDetailsDTO> findTicketsByUserIdWithDetails(int userId) {
    	//(B)
        /*List<Object[]> resultList = entityManager
            .createNamedQuery("Ticket.findTicketsByUserIdWithDetails", Object[].class)
            .setParameter("userId", userId)
            .getResultList();*/
        
    	//(A)
        List<Object[]> resultList = ticketRepository.findTicketsByUserIdWithDetails(userId);

        List<TicketDetailsDTO> ticketDetailsList = new ArrayList<>();
        for (Object[] objects : resultList) {
            Ticket ticket = (Ticket) objects[0];
            Gebruiker gebruiker = (Gebruiker) objects[1];
            Wedstrijd wedstrijd = (Wedstrijd) objects[2];
            Sport sport = (Sport) objects[3];
            ticketDetailsList.add(new TicketDetailsDTO(ticket, gebruiker, wedstrijd, sport));
        }
        return ticketDetailsList;
    }
    
    public int getTicketsGebruikerIdWedstrijdId(int userId, int wedstrijdId) {
    	try {
    	Integer ticketCount = (Integer) entityManager.createNamedQuery("Ticket.findTicketCountByUserIdAndWedstrijdId")
    			.setParameter("userId", userId)
    			.setParameter("wedstrijdId", wedstrijdId)
    			.getSingleResult();
    	
    	return ticketCount != null ? ticketCount.intValue() : 0;
    	
    	//relevante case: er bestaat in de database geen Ticket voor deze gebruiker + wedstrijd
    	}catch(NoResultException e) {
    		return 0;
    	}
    }
    
    /**
     * voor ticket kopen: als een ticket al bestaat voor een wedstrijd/gebruiker -> update
     * als null -> maak maar een nieuw ticket object aan
     * 
     * @param userId
     * @param wedstrijdId
     * @return
     */
    public Ticket findTicketByGebruikerIdWedstrijdId(int userId, int wedstrijdId) {
    	try {
    		return (Ticket) entityManager.createNamedQuery("Ticket.findTicketByGebruikerIdWedstrijdId")
    		.setParameter("userId", userId)
			.setParameter("wedstrijdId", wedstrijdId).getSingleResult();
    		
    	}catch(NoResultException e) {
    		return null;
    	}
    }

}

