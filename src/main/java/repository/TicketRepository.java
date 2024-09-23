package repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import domain.Ticket;

public interface TicketRepository extends CrudRepository<Ticket, Integer>{
	
	@Query(name = "Ticket.findTicketsByUserIdWithDetails")  
	List<Object[]> findTicketsByUserIdWithDetails(@Param("userId") int userId);
	
	List<Ticket> findByGebruikerId(int userId);
}
