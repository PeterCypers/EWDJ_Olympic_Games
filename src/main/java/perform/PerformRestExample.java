package perform;

import org.springframework.web.reactive.function.client.WebClient;

import domain.Wedstrijd;
import reactor.core.publisher.Mono;

public class PerformRestExample {
	
	private final String SERVER_URI = "http://localhost:8080/rest";
	private WebClient webClient = WebClient.create();
	private final int WEDSTRIJDID = 1;
	private final int SPORTID = 3;
	
	public PerformRestExample() throws Exception {
		try {
			printOverzichtVrijePlaatsen(WEDSTRIJDID);
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		try {
			printLijstWedstrijdenOpSportId(SPORTID);
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void printOverzichtVrijePlaatsen(int wedstrijdId) {
		System.out.printf("----- Print overzicht vrije plaatsen voor Wedstrijd met ID = %d -----%n%n", wedstrijdId);
		webClient.get().uri(SERVER_URI + String.format("/wedstrijd/%d", wedstrijdId)).retrieve()
		.bodyToMono(Integer.class)
		.doOnSuccess(plaatsen -> System.out.println(plaatsen))
		.block();
	}
	
	private void printLijstWedstrijdenOpSportId(int sportId) {
		System.out.printf("----- Print Wedstrijden Per Sport voor Sport met ID = %d -----%n%n", sportId);
		webClient.get().uri(SERVER_URI + String.format("/sportWedstrijden/%d", sportId)).retrieve()
		.bodyToFlux(Wedstrijd.class)
		.flatMap(wedstrijd -> {
			System.out.println(wedstrijd.toDemoString());
			return Mono.empty();
		})
		.blockLast();
	}
	

}
