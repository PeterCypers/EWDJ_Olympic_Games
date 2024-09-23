package repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import domain.Gebruiker;

@Component
public interface GebruikerRepository extends CrudRepository<Gebruiker, Integer>{
	
	Gebruiker findByUsername(String name);
}
