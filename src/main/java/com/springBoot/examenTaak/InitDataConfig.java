package com.springBoot.examenTaak;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import domain.Discipline;
import domain.Gebruiker;
import domain.Rol;
import domain.Sport;
//import domain.Stadium;
import domain.Ticket;
import domain.Wedstrijd;
import repository.DisciplineRepository;
import repository.GebruikerRepository;
import repository.SportRepository;
import repository.TicketRepository;
import repository.WedstrijdRepository;

@Component
public class InitDataConfig  implements CommandLineRunner {
	
	private PasswordEncoder encoder = new BCryptPasswordEncoder();
	
	//TODO : repositories van elke entity die al in de databank moet zitten
	@Autowired 
	private DisciplineRepository disciplineRepository;
	
	@Autowired
	private GebruikerRepository gebruikerRepository;
	
	@Autowired
	private SportRepository sportRepository;
	
//	@Autowired
//	private StadiumRepository stadiumRepository;
//	
	@Autowired
	private TicketRepository ticketRepository;
//	
	@Autowired
	private WedstrijdRepository wedstrijdRepository;

	/**
	 * Entity creation order: 
	 * 1) Sport -> has List<Wedstrijden> & List<Discipline> -> these are added post-construction
	 * 2) Discipline -> requires Sport to exist
	 * 3) Wedstrijd -> has List<Discipline> -> important !!! link [ Sport  & Stadium ] through method !!! & add disciplines
	 * 4) Gebruiker
	 * 5) Tickets -> requires Gebruiker & Wedstrijd to exist
	 * 
	 */
	@Override
	public void run(String... args) {
		
		int STADIONPLAATSEN = 50;
		
		//Sport creation
		String[] sporten = {"Voetbal","Netsport","Sprinten","Atletiek Werpen","Zwemmen"};
		String[] images = {"/img/footballEwdjP.png", "/img/tennisEwdjP.png", "/img/runningEwdjP.png", "/img/athleticsThrowingEwdjP.png", "/img/swimmingEwdjP.png"};
		List<Sport> sportenToSave = new ArrayList<>();
		
		for (int i = 0; i < sporten.length; i++) {
			Sport s = new Sport(i+1, sporten[i], images[i]);
			sportenToSave.add(s);
		}
		
		//Discipline creation
		//indexen = 1: Net, 2: Sprint, 3: Atl.Werpen, 4: Zwemmen
		Discipline netsportDiscipline1 = new Discipline(sportenToSave.get(1),"Tennis");
		Discipline netsportDiscipline2 = new Discipline(sportenToSave.get(1),"Volleybal");
		Discipline netsportDiscipline3 = new Discipline(sportenToSave.get(1),"Badminton");
		
		Discipline loopDiscipline1 = new Discipline(sportenToSave.get(2), "Mannen 100m");
		Discipline loopDiscipline2 = new Discipline(sportenToSave.get(2), "Mannen 400m");
		Discipline loopDiscipline3 = new Discipline(sportenToSave.get(2), "Vrouwen 100m");
		Discipline loopDiscipline4 = new Discipline(sportenToSave.get(2), "Vrouwen 400m");
		Discipline loopDiscipline5 = new Discipline(sportenToSave.get(2), "Mannen 4x100m Relay");
		Discipline loopDiscipline6 = new Discipline(sportenToSave.get(2), "Vrouwen 4x100m Relay");
		
		Discipline werpDiscipline1 = new Discipline(sportenToSave.get(3), "Discus werpen");
		Discipline werpDiscipline2 = new Discipline(sportenToSave.get(3), "Speer werpen");
		Discipline werpDiscipline3 = new Discipline(sportenToSave.get(3), "Shot put");
		
		List<Discipline> disciplinesToSave = new ArrayList<>(List.of(
				netsportDiscipline1, netsportDiscipline2, netsportDiscipline3,
				loopDiscipline1, loopDiscipline2, loopDiscipline3, loopDiscipline4, loopDiscipline5, loopDiscipline6,
				werpDiscipline1, werpDiscipline2, werpDiscipline3));

		//Linken van sporten aan disciplines
		sportenToSave.get(1).addDiscipline(netsportDiscipline1);
		sportenToSave.get(1).addDiscipline(netsportDiscipline2);
		sportenToSave.get(1).addDiscipline(netsportDiscipline3);
		
		sportenToSave.get(2).addDiscipline(loopDiscipline1);
		sportenToSave.get(2).addDiscipline(loopDiscipline2);
		sportenToSave.get(2).addDiscipline(loopDiscipline3);
		sportenToSave.get(2).addDiscipline(loopDiscipline4);
		sportenToSave.get(2).addDiscipline(loopDiscipline5);
		sportenToSave.get(2).addDiscipline(loopDiscipline6);
		
		sportenToSave.get(3).addDiscipline(werpDiscipline1);
		sportenToSave.get(3).addDiscipline(werpDiscipline2);
		sportenToSave.get(3).addDiscipline(werpDiscipline3);

		
		//Olympia / Bird’s Nest / Yoyogi National Gymnasium / Aquatics Centre / Colloseum / Santiago Bernabéa / Panathenean Stadium
		//datums tussen: 26 jul 2024 & 11 aug 2024
		// 12345, 54321, 15432, 23451, 34512, 45123, 51234, 11122, 12312 , 21345
		
		//Sport1: voetbal
		Wedstrijd ws1 = Wedstrijd.builder()
				.startDatum(LocalDate.of(2024, 7, 27))
				.startTijd(LocalTime.of(17, 30))
				.oNr1(12345)
				.oNr2(22345)
				.ticketPrijs(50.0)
				.discipline1("")
				.discipline2("")
				.stadium_naam("Santiago Bernabéa")
				.aantal_plaatsen(STADIONPLAATSEN)
				//.vrije_plaatsen(40)
				.build();
		//Sport2:(netsport: zelfde datum, tijd aflopend)
		Wedstrijd ws2 = Wedstrijd.builder()
				.startDatum(LocalDate.of(2024, 7, 30))
				.startTijd(LocalTime.of(16, 45))
				.oNr1(54321)
				.oNr2(64321)
				.ticketPrijs(100.0)
				.discipline1(netsportDiscipline2.getNaam()) //"Volleybal"
				.discipline2("")
				.stadium_naam("Bird's Nest")
				.aantal_plaatsen(0)
				//.vrije_plaatsen(0)
				.build();
		
		Wedstrijd ws3 = Wedstrijd.builder()
				.startDatum(LocalDate.of(2024, 7, 30))
				.startTijd(LocalTime.of(10, 30))
				.oNr1(15432)
				.oNr2(25432)
				.ticketPrijs(149.0)
				.discipline1(netsportDiscipline1.getNaam()) //"Tennis"
				.discipline2(netsportDiscipline3.getNaam()) //"Badminton"
				.stadium_naam("Yoyogi National Gymnasium")
				.aantal_plaatsen(0)
				//.vrije_plaatsen(1)
				.build();
		//Sport3(lopen - 2 zelfde datums oplopend, tijd aflopend):
		Wedstrijd ws4 = Wedstrijd.builder()
				.startDatum(LocalDate.of(2024, 8, 7))
				.startTijd(LocalTime.of(17, 30))
				.oNr1(23451)
				.oNr2(33451)
				.ticketPrijs(85.50)
				.discipline1(loopDiscipline1.getNaam())
				.discipline2(loopDiscipline2.getNaam())
				.stadium_naam("Olympia")
				.aantal_plaatsen(10)
				//.vrije_plaatsen(15)
				.build();
		
		Wedstrijd ws5 = Wedstrijd.builder()
				.startDatum(LocalDate.of(2024, 8, 7))
				.startTijd(LocalTime.of(16, 30))
				.oNr1(34512)
				.oNr2(44512)
				.ticketPrijs(85.50)
				.discipline1(loopDiscipline3.getNaam())
				.discipline2(loopDiscipline4.getNaam())
				.stadium_naam("Olympia")
				.aantal_plaatsen(5)
				//.vrije_plaatsen(35)
				.build();
		
		Wedstrijd ws6 = Wedstrijd.builder()
				.startDatum(LocalDate.of(2024, 8, 10))
				.startTijd(LocalTime.of(13, 30))
				.oNr1(45123)
				.oNr2(55123)
				.ticketPrijs(125.25)
				.discipline1(loopDiscipline5.getNaam())
				.discipline2(loopDiscipline6.getNaam())
				.stadium_naam("Colloseum")
				.aantal_plaatsen(STADIONPLAATSEN)
				//.vrije_plaatsen(49)
				.build();
		//Sport4(werpen - allen zelfde datum):
		Wedstrijd ws7 = Wedstrijd.builder()
				.startDatum(LocalDate.of(2024, 8, 10))
				.startTijd(LocalTime.of(9, 30))
				.oNr1(51234)
				.oNr2(61234)
				.ticketPrijs(99.95)
				.discipline1(werpDiscipline1.getNaam())
				.discipline2(werpDiscipline3.getNaam())
				.stadium_naam("Panathenean Stadium")
				.aantal_plaatsen(STADIONPLAATSEN)
				//.vrije_plaatsen(10)
				.build();
		
		Wedstrijd ws8 = Wedstrijd.builder()
				.startDatum(LocalDate.of(2024, 8, 10))
				.startTijd(LocalTime.of(11, 0))
				.oNr1(11122)
				.oNr2(21122)
				.ticketPrijs(99.95)
				.discipline1(werpDiscipline2.getNaam())
				.discipline2("")
				.stadium_naam("Panathenean Stadium")
				.aantal_plaatsen(STADIONPLAATSEN)
				//.vrije_plaatsen(20)
				.build();
		
		Wedstrijd ws9 = Wedstrijd.builder()
				.startDatum(LocalDate.of(2024, 8, 10))
				.startTijd(LocalTime.of(12, 30))
				.oNr1(12312)
				.oNr2(22312)
				.ticketPrijs(75)
				.discipline1("")
				.discipline2("")
				.stadium_naam("Olympia")
				.aantal_plaatsen(STADIONPLAATSEN)
				//.vrije_plaatsen(30)
				.build();
		
		//Sport5:
		Wedstrijd ws10 = Wedstrijd.builder()
				.startDatum(LocalDate.of(2024, 8, 10))
				.startTijd(LocalTime.of(9, 45))
				.oNr1(21345)
				.oNr2(31345)
				.ticketPrijs(75)
				.discipline1("")
				.discipline2("")
				.stadium_naam("Aquatics Centre")
				.aantal_plaatsen(STADIONPLAATSEN)
				//.vrije_plaatsen(49)
				.build();
		
		List<Wedstrijd> wedstrijdenToSave = List.of(ws1, ws2, ws3, ws4, ws5, ws6, ws7, ws8, ws9, ws10);
		
		
		//linken van sporten aan wedstrijden
		//voetbal
		sportenToSave.get(0).addWedstrijd(ws1);
		//netsport
		sportenToSave.get(1).addWedstrijd(ws2);
		sportenToSave.get(1).addWedstrijd(ws3);
		//lopen
		sportenToSave.get(2).addWedstrijd(ws4);
		sportenToSave.get(2).addWedstrijd(ws5);
		sportenToSave.get(2).addWedstrijd(ws6);
		//werpen
		sportenToSave.get(3).addWedstrijd(ws7);
		sportenToSave.get(3).addWedstrijd(ws8);
		sportenToSave.get(3).addWedstrijd(ws9);
		//zwemmen
		sportenToSave.get(4).addWedstrijd(ws10);
	
		//Persisting
		sportRepository.saveAll(sportenToSave);
		disciplineRepository.saveAll(disciplinesToSave);
		wedstrijdRepository.saveAll(wedstrijdenToSave);
		//TODO: add more users to showcase various use-cases or to run tests
	
		var user1 = Gebruiker.builder()
				.username("Mark")
				.rol(Rol.USER)
				.password(encoder.encode("1234"))
				.build();
		
		var admin = Gebruiker.builder()
				.username("admin")
				.rol(Rol.ADMIN)
				.password(encoder.encode("admin"))
				.build();
		
		//max aantal tickets (& technisch gezien 0 tickets gekocht per wedstrijd)
		var user2 = Gebruiker.builder()
				.username("Ticketman")
				.rol(Rol.USER)
				.totalTickets(100)
				.password(encoder.encode("1234"))
				.build();
		
		//0 tickets
		var user3 = Gebruiker.builder()
				.username("zero")
				.rol(Rol.USER)
				.totalTickets(0)
				.password(encoder.encode("1234"))
				.build();
		
		List<Gebruiker> userList = Arrays.asList(user1, admin, user2, user3);
		
		//Tickets
		Ticket ticket1 = new Ticket(20);
		Ticket ticket2 = new Ticket(10);
		Ticket ticket3 = new Ticket(15);
		Ticket ticket4 = new Ticket(10);
		List<Ticket> ticketsToSave = List.of(ticket1, ticket2, ticket3, ticket4);
		

		//Linking ticket + gebruiker + wedstrijd
		ws1.addTicket(ticket1);
		user1.addTicket(ticket1);
		ws2.addTicket(ticket2);
		user1.addTicket(ticket2);
		ws3.addTicket(ticket3);
		user1.addTicket(ticket3);
		ws7.addTicket(ticket4);
		user1.addTicket(ticket4);
		
		//Persisting
		gebruikerRepository.saveAll(userList);
		ticketRepository.saveAll(ticketsToSave);
		
	}

}
