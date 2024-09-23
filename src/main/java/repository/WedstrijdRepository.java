package repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import domain.Wedstrijd;

public interface WedstrijdRepository extends CrudRepository<Wedstrijd, Integer> {

	//
	/**
	 * SportSportId == Sport.sport_id
	 * 
	 * @param sportId
	 * @return een List<Wedstrijd> op sportId
	 */
	List<Wedstrijd> findBySportSportId(int sportId);
	
	/**met <code>NamedQuery</code>
	 *
	 * @param sportId
	 * @return List<Wedstrijd> get op sportId gesorteerd op startDatum, dan startTijd
	 */
	List<Wedstrijd> gesorteerdeWedstrijden(@Param("sportId") int sportId);
	
	
    @Query("SELECT w FROM Wedstrijd w WHERE w.wedstrijdId = (SELECT MAX(w.wedstrijdId) FROM Wedstrijd w)")
    Wedstrijd findLast();
    
    //void deleteByWedstrijdId(int wedstrijdId);

}
