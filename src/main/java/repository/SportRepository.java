package repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import domain.Sport;

public interface SportRepository extends CrudRepository<Sport, Integer>{

	Sport findByNaam(String naam);
	
	@Query(name = "Sport.findSportByWedstrijdId")
	Sport findSportByWedstrijdId(@Param("wedstrijdId") int wedstrijdId);
}
