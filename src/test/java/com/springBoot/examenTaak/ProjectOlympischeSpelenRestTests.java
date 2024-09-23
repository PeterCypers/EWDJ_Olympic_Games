package com.springBoot.examenTaak;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import org.springframework.test.web.servlet.MvcResult;

import domain.Discipline;
import domain.Gebruiker;
import domain.Rol;
import domain.Sport;
import domain.Ticket;
import domain.Wedstrijd;
import exceptions.WedstrijdNotFoundException;
import repository.DisciplineRepository;
import repository.GebruikerRepository;
import repository.SportRepository;
import repository.TicketRepository;
import repository.WedstrijdRepository;
import service.WedstrijdRestService;
import static utils.InitFormatter.*;

@SpringBootTest
//@AutoConfigureMockMvc
class ProjectOlympischeSpelenRestTests {
	
	private final int GELDIGE_WEDSTRIJD_ID = 1;
	private final int GELDIGE_SPORT_ID = 1;
	
	private final int ONBESTAAND_WEDSTRIJD_ID = 2;
	private final int ONBESTAAND_SPORT_ID = 2;
	
	private final int AANTAL_VRIJE_PLAATSEN = 50;
	
	private String EXPECTED_DATE_FORMATTED; //utils.InitFormatter : "dd-MM-yyyy"
	private String EXPECTED_TIME_FORMATTED; //utils.InitFormatter : "HH:mm:ss"
	
	private Wedstrijd mockWedstrijd;
	
	@Mock
	private WedstrijdRestService mock;
	
	private WedstrijdRestController controller;
	private MockMvc mockMvc;
	
    @MockBean
    private UserDetailsService userService;
    @MockBean
    private GebruikerRepository userRepository;
    @MockBean
    private TicketRepository ticketRepository;
    @MockBean
    private SportRepository sportRepository;
    @MockBean
    private DisciplineRepository disciplineRepository;
    @MockBean
    private WedstrijdRepository wedstrijdRepository;
    
  
    @BeforeEach
    public void setup() {
    	
    	//mock van de ganse DB-constructie met minimale entities
    	
    	Sport sport = new Sport(GELDIGE_SPORT_ID, "Netsport", "image1");
    	
		Discipline netsportDiscipline1 = new Discipline(sport, "Tennis");
		Discipline netsportDiscipline2 = new Discipline(sport, "Volleybal");
		
		sport.addDiscipline(netsportDiscipline1);
		sport.addDiscipline(netsportDiscipline2);
		
		Wedstrijd ws1 = Wedstrijd.builder()
				.wedstrijdId(GELDIGE_WEDSTRIJD_ID) //TODO testing
				.startDatum(LocalDate.of(2024, 7, 30))
				.startTijd(LocalTime.of(16, 45))
				.oNr1(54321)
				.oNr2(64321)
				.ticketPrijs(100.0)
				.discipline1(netsportDiscipline2.getNaam()) //"Volleybal"
				.discipline2("")
				.stadium_naam("Bird's Nest")
				.aantal_plaatsen(AANTAL_VRIJE_PLAATSEN)
				.build();
		
		EXPECTED_DATE_FORMATTED = ws1.getStartDatum().format(DATE_FORMATTER);
		EXPECTED_TIME_FORMATTED = ws1.getStartTijd().format(TIME_FORMATTER);
		
		sport.addWedstrijd(ws1);
		
		mockWedstrijd = ws1;
		
		sportRepository.save(sport);
		disciplineRepository.saveAll(List.of(netsportDiscipline1, netsportDiscipline2));
		wedstrijdRepository.save(ws1);

        Gebruiker normalUser = Gebruiker.builder()
                .username("user")
                .password("password")
                .rol(Rol.USER)
                .id(1)
                .totalTickets(15)
                .build();
        
        Ticket ticket = new Ticket(5);
        
        ws1.addTicket(ticket);
        normalUser.addTicket(ticket);
        
        userRepository.save(normalUser);
        ticketRepository.save(ticket);
        
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
	    User user= new User(normalUser.getUsername(), normalUser.getPassword(), Collections.singletonList(authority));
       
	    when(userService.loadUserByUsername("user")).thenReturn(user);
        when(userRepository.findByUsername("user")).thenReturn(normalUser);
        
        MockitoAnnotations.openMocks(this);
        controller = new WedstrijdRestController();
        mockMvc = standaloneSetup(controller).build();
        ReflectionTestUtils.setField(controller, "wedstrijdRestService", mock);
    }
    
    @Test
    void testAantalVrijePLaatsen_notFound() throws Exception {
    	//mock trainen
    	/*Mockito.*/when(mock.getOverzichtBeschikbarePlaatsen(ONBESTAAND_WEDSTRIJD_ID)).thenThrow(new WedstrijdNotFoundException(ONBESTAAND_WEDSTRIJD_ID));
    	Exception exception = assertThrows(Exception.class, () -> {
    		mockMvc.perform(get("/rest/wedstrijd/" + ONBESTAAND_WEDSTRIJD_ID)).andReturn();
    	});
    	assertTrue(exception.getCause() instanceof WedstrijdNotFoundException);
    	
    	Mockito.verify(mock).getOverzichtBeschikbarePlaatsen(ONBESTAAND_WEDSTRIJD_ID);
    }
    
    @Test
    void testAantalVrijePlaatsen() throws Exception {
    	//mock trainen
    	when(mock.getOverzichtBeschikbarePlaatsen(GELDIGE_WEDSTRIJD_ID)).thenReturn(AANTAL_VRIJE_PLAATSEN);
    	/*MvcResult result = */mockMvc.perform(get("/rest/wedstrijd/" + GELDIGE_WEDSTRIJD_ID))
    	.andExpect(status().isOk())//.andReturn(); (B)
    	.andExpect(content().string(String.valueOf(AANTAL_VRIJE_PLAATSEN)));
    	
    	//(B)
    	//String content = result.getResponse().getContentAsString();
    	//assertEquals(AANTAL_VRIJE_PLAATSEN, Integer.parseInt(content));
    }
    
    @Test
    void testGetWedstrijdenOpSportId_notFound() throws Exception {
    	when(mock.getWedstrijdBySportId(ONBESTAAND_SPORT_ID)).thenThrow(new WedstrijdNotFoundException(ONBESTAAND_SPORT_ID));
    	
    	Exception exception = assertThrows(Exception.class, () -> {
    		mockMvc.perform(get("/rest/sportWedstrijden/" + ONBESTAAND_SPORT_ID)).andReturn();
    	});
    	
    	assertTrue(exception.getCause() instanceof WedstrijdNotFoundException);
    	
    	Mockito.verify(mock).getWedstrijdBySportId(ONBESTAAND_SPORT_ID);
    }
    
    @Test
    void testGetWedstrijdenOpSportId_emptyList() throws Exception {
    	when(mock.getWedstrijdBySportId(GELDIGE_SPORT_ID)).thenReturn(new ArrayList<>());
    	
    	mockMvc.perform(get("/rest/sportWedstrijden/" + GELDIGE_SPORT_ID))
		    	.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$").isEmpty());
    	
    	Mockito.verify(mock).getWedstrijdBySportId(GELDIGE_SPORT_ID);
    }
    
    /** expected:
     *  "wedstrijd_id": 1, (GELDIGE_WEDSTRIJD_ID)
        "stadium_naam": "Bird's Nest",
        "start_datum": "30-07-2024",
        "start_tijd": "16:45:00",
        "o_nr1": 54321,
        "o_nr2": 64321,
        "ticket_prijs": 100.0,
        "discipline1": "Volleybal",
        "discipline2": "",
        "aantal_plaatsen": 50 (AANTAL_VRIJE_PLAATSEN)
     */
    @Test
    void testGetWedstrijdenOpSportId() throws Exception {
    	when(mock.getWedstrijdBySportId(GELDIGE_SPORT_ID)).thenReturn(List.of(mockWedstrijd));
    	mockMvc.perform(get("/rest/sportWedstrijden/" + GELDIGE_SPORT_ID))
    			.andExpect(status().isOk())
    			.andExpect(jsonPath("$").isNotEmpty())
    			.andExpect(jsonPath("$[0].wedstrijd_id").value(GELDIGE_WEDSTRIJD_ID))
    			.andExpect(jsonPath("$[0].stadium_naam").value("Bird's Nest"))
    			.andExpect(jsonPath("$[0].o_nr1").value(54321))
    			.andExpect(jsonPath("$[0].o_nr2").value(64321))
    			.andExpect(jsonPath("$[0].ticket_prijs").value(100.0))
    			.andExpect(jsonPath("$[0].discipline1").value("Volleybal"))
    			.andExpect(jsonPath("$[0].discipline2").value(""))
    			.andExpect(jsonPath("$[0].aantal_plaatsen").value(AANTAL_VRIJE_PLAATSEN));
    	
    	Mockito.verify(mock).getWedstrijdBySportId(GELDIGE_SPORT_ID);
    }
    
    @Test
    void testGetWedstrijdenOpSportId_formatCorrect() throws Exception {
    	when(mock.getWedstrijdBySportId(GELDIGE_SPORT_ID)).thenReturn(List.of(mockWedstrijd));
    	mockMvc.perform(get("/rest/sportWedstrijden/" + GELDIGE_SPORT_ID))
    			.andExpect(status().isOk())
    			.andExpect(jsonPath("$").isNotEmpty())
    			.andExpect(jsonPath("$[0].start_datum").value("30-07-2024"))
    			.andExpect(jsonPath("$[0].start_tijd").value("16:45:00"));
    	
    	assertTrue(EXPECTED_DATE_FORMATTED.equals("30-07-2024"));
    	assertTrue(EXPECTED_TIME_FORMATTED.equals("16:45:00"));
    }
    
    /**
     * alle training in tests gehouden --> leesbaarheid
     */
    private void trainMock() {
    	
    }
    

}
