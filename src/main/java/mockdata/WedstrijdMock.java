package mockdata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

//import domain.Stadium;
import domain.Wedstrijd;

public class WedstrijdMock {
	
//	private final StadiumMock stadiumMock = new StadiumMock();
//	private Map<String, Stadium> stadiums;
	
	private Map<String, Wedstrijd> wedstrijden = new HashMap<>();
	
	public WedstrijdMock() {
//		stadiums = stadiumMock.getStadiums();
//		int count = 0;
//		stadiums.values().forEach(stadium -> {
//			int numberValue = Integer.parseInt(stadium.getNaam().charAt(7)+"");
//			wedstrijden.put(String.format("wedstrijd%d", numberValue),
//					new Wedstrijd(numberValue, LocalDateTime.now().plusDays(numberValue), stadium, numberValue,numberValue,numberValue * 100.0));
//		});
	}
	
	public Map<String, Wedstrijd> getWedstrijden() {
		return wedstrijden;
	}

}
