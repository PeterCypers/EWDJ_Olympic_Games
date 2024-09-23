package mockdata;

import java.util.HashMap;
import java.util.Map;

import domain.Sport;

public class SportMock {
	
	private Map<String, Sport> sporten = new HashMap<>();
	
	public SportMock() {
//		sporten.put("sport1", new Sport(1, "Voetbal", "/img/footballEwdjP.png"));
//		sporten.put("sport2", new Sport(2, "Tennis", "/img/tennisEwdjP"));
//		sporten.put("sport3", new Sport(3, "Sprinten", "/img/runningEwdjP"));
	}
	
	public Map<String, Sport> getSports() {
		return sporten;
	}

}
