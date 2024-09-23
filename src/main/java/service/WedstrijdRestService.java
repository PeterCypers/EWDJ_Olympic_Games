package service;

import java.util.List;

import domain.Wedstrijd;

public interface WedstrijdRestService {
	
	//(buiten scope -> mogelijke uitbreiding)
	//public Wedstrijd getWedstrijdById(int id);
	//public List<Wedstrijd> getAllWedstrijden();
	//public Wedstrijd createDummyWedstrijd();
	//public Wedstrijd deleteWedstrijd(int id);
	
	
	public Integer getOverzichtBeschikbarePlaatsen(int id);
	
	public List<Wedstrijd> getWedstrijdBySportId(int sportId);
}
