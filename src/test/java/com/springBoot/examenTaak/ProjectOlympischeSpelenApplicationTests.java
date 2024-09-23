package com.springBoot.examenTaak;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import domain.Discipline;
import domain.Gebruiker;
import domain.Rol;
import domain.Sport;
import domain.Ticket;
import domain.Wedstrijd;
import repository.DisciplineRepository;
import repository.GebruikerRepository;
import repository.SportRepository;
import repository.TicketRepository;
import repository.WedstrijdRepository;


@Import(SecurityConfig.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProjectOlympischeSpelenApplicationTests {

	@Autowired
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
    	
    	Sport sport = new Sport(1, "Netsport", "image1");
    	
		Discipline netsportDiscipline1 = new Discipline(sport, "Tennis");
		Discipline netsportDiscipline2 = new Discipline(sport, "Volleybal");
		
		sport.addDiscipline(netsportDiscipline1);
		sport.addDiscipline(netsportDiscipline2);
		
		Wedstrijd ws1 = Wedstrijd.builder()
				.wedstrijdId(1) //TODO testing
				.startDatum(LocalDate.of(2024, 7, 30))
				.startTijd(LocalTime.of(16, 45))
				.oNr1(54321)
				.oNr2(64321)
				.ticketPrijs(100.0)
				.discipline1(netsportDiscipline2.getNaam()) //"Volleybal"
				.discipline2("")
				.stadium_naam("Bird's Nest")
				.aantal_plaatsen(1)
				//.vrije_plaatsen(0)
				.build();
		
		sport.addWedstrijd(ws1);
		
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
    }
    
	@Test
	public void loginGet() throws Exception {
	    	mockMvc.perform(get("/login"))
	        .andExpect(status().isOk())
	    	.andExpect(view().name("login"));
	}
	
	@Test
	public void accessDeniedPageGet() throws Exception {
	    	mockMvc.perform(get("/403"))
	        .andExpect(status().isOk())
	    	.andExpect(view().name("403"));
	}
	
	/**
	 * note: om een executable Jar file te bouwen heb je allemaal slagende tests nodig
	 * zet deze test in comment als je het project opnieuw wilt bouwen,
	 * maar de jar is nu al gemaakt en zit in de target folder
	 * 
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username = "user")
	public void testAccessWithUserRole() throws Exception {
		 mockMvc.perform(get("/koopTickets/1"))
	        .andExpect(status().isOk())
			.andExpect(view().name("ticketskopen"))
			.andExpect(model().attributeExists("username"))
			.andExpect(model().attributeExists("userId"))
			.andExpect(model().attributeExists("ticketTotal"))
			.andExpect(model().attributeExists("sportName"))
			.andExpect(model().attributeExists("stadiumNaam"))
			.andExpect(model().attribute("username", "user"));
    }
	
	@WithMockUser(username = "admin", roles = {"ADMIN"})
	@Test
    public void testNoAccessTickets() throws Exception {
        mockMvc.perform(get("/tickets/1"))
            .andExpect(status().isForbidden());
	}
	
	@WithMockUser(username = "user1", roles = {"USER"})
	@Test
    public void testNoAccessWedstrijdToevoegen() throws Exception {
        mockMvc.perform(get("/toevoegenWedstrijd/Netsport"))
            .andExpect(status().isForbidden());
	}
	
	@WithAnonymousUser
	@Test
	public void testNoAccessAnonymous() throws Exception {
	    mockMvc.perform(get("/sports"))
	            .andExpect(redirectedUrlPattern("**/login"));
	}

}
