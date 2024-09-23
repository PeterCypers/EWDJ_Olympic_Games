package repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import domain.Discipline;

public interface DisciplineRepository extends CrudRepository<Discipline, Integer>{

	//TODO: confirm column name
	List<Discipline> findBySportSportId(int sportId);
}
