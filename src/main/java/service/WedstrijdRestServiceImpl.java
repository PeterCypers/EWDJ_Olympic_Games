package service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import domain.Wedstrijd;
import exceptions.WedstrijdNotFoundException;
import repository.WedstrijdRepository;

public class WedstrijdRestServiceImpl implements WedstrijdRestService {
	
	@Autowired
	WedstrijdRepository wedstrijdRepository;


	@Override
	public Integer getOverzichtBeschikbarePlaatsen(int id) {
		if(!wedstrijdRepository.existsById(id)) {
			throw new WedstrijdNotFoundException(id);
		}
		Wedstrijd wedstrijd = wedstrijdRepository.findById(id).get();
		
		return wedstrijd.getAantal_plaatsen();
	}

	@Override
	public List<Wedstrijd> getWedstrijdBySportId(int sportId) {
		List<Wedstrijd> wedstrijden = wedstrijdRepository.findBySportSportId(sportId);
		if(wedstrijden.size() == 0)
			throw new WedstrijdNotFoundException(sportId);
		return wedstrijden.stream().collect(Collectors.toUnmodifiableList());
	}

}
