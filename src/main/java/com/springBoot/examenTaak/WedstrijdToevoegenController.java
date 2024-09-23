package com.springBoot.examenTaak;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import domain.Sport;
import domain.Wedstrijd;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import repository.DisciplineRepository;
import repository.SportRepository;
import repository.WedstrijdRepository;
import validator.DateValidator;
import validator.DisciplinesValidator;
import validator.OlympicNumberOneValidator;
import validator.OlympicNumberTwoValidator;
import validator.TimeValidator;

@Controller
@RequestMapping("/toevoegenWedstrijd")
public class WedstrijdToevoegenController {
	
    @ModelAttribute("username")
    public String populateUserName(Principal principal) {
    	return principal.getName();
    }
	
	@Autowired
	WedstrijdRepository wedstrijdRepository;
	@Autowired
	SportRepository sportRepository;
	@Autowired
	DisciplineRepository disciplineRepository;
	
	@Autowired
	private TimeValidator timeValidator;
	@Autowired
	private DateValidator dateValidator;
	@Autowired
	private DisciplinesValidator disciplinesValidator;
	@Autowired
	private OlympicNumberOneValidator olympicNumberOneValidator;
	@Autowired
	private OlympicNumberTwoValidator olympicNumberTwoValidator;

	
	
    @GetMapping(value = "/{sportName}")
    public String showAddView(@PathVariable("sportName") String sportName, Model model, Authentication authentication) {
    	String role = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().get();
    	model.addAttribute("role", role);
    	int sportId = sportRepository.findByNaam(sportName).getSportId();
    	model.addAttribute("sportId", sportId);
    	model.addAttribute("sport", sportName);
    	Sport wedstrijdSport = sportRepository.findByNaam(sportName);
    	
    	//lijst van stadiumnamen:
    	List<String> stadiumNamen = wedstrijdRepository.findBySportSportId(sportId).stream().map(Wedstrijd::getStadium_naam)
    			.collect(Collectors.toUnmodifiableList());
    	model.addAttribute("stadiumList", stadiumNamen);
    	
    	//voetbal?(0 diciplines) -> lege lijst meegeven 
    	model.addAttribute("disciplineList", sportId == 1? new ArrayList<>() : disciplineRepository.findBySportSportId(sportId));
    	
    	//(A) if addAttribute zonder key-name -> keyname == obj name toLowercase
    	Wedstrijd geldigeWedstrijd = new Wedstrijd();
    	wedstrijdSport.addWedstrijd(geldigeWedstrijd);
    	model.addAttribute(geldigeWedstrijd);
    	
    	
    	/*(B) -> lange work-around o.a. dankzij @NoArgsConstructor(access = AccessLevel.PROTECTED)
    	  - problematiek: omdat ik niet via normaal constr. kan aanmaken moet ik de builder gebruiken, maar hier heeft het een probleem met
    	  - de auto-indexed id field -> die moet ik initializeren door dit obj even in de db te zetten en opnieuw verwijderen nadat id is ingesteld*/
//    	Wedstrijd geldigeWedstrijd = Wedstrijd.builder()
//				.startDatum(LocalDate.of(2024, 7, 30))
//				.startTijd(LocalTime.of(8, 0))
//				.oNr1(88889)
//				.oNr2(98889)
//				.ticketPrijs(150.0)
//				.discipline1("") //"Tennis"
//				.discipline2("") //"Badminton"
//				.stadium_naam("Dummy Stadium")
//				.aantal_plaatsen(49)
//				//.vrije_plaatsen(49)
//				.build();
    	//wedstrijd+sport verbinden met elkaar + persisteren voor auto-increment id-binding + verwijderen
//    	wedstrijdSport.addWedstrijd(geldigeWedstrijd);
//    	wedstrijdRepository.save(geldigeWedstrijd);
//    	model.addAttribute("wedstrijd", wedstrijdRepository.findLast());
//    	wedstrijdRepository.delete(wedstrijdRepository.findLast());
    	
    	
    	//System.out.println(geldigeWedstrijd.toString());
    	
    	return "wedstrijdtoevoegen";
    }
	
	
	
	@PostMapping(value = "/{sportName}")
	public String onSubmit(@Valid
			Wedstrijd wedstrijd, BindingResult result, Model model, @PathVariable("sportName") String sportName, Authentication authentication) {
		
		timeValidator.validate(wedstrijd, result);
		dateValidator.validate(wedstrijd, result);
		disciplinesValidator.validate(wedstrijd, result);
		olympicNumberTwoValidator.validate(wedstrijd, result);
		olympicNumberOneValidator.validate(wedstrijd, result);
		
		if(result.hasErrors()) {
			//extra testing component in comment: zie html
			model.addAttribute("validationErrors", result.getAllErrors());
			
			//objects verdwijnen uit de modelmap bij herladen -> oplossing: opnieuw toevoegen:
			//disciplines:
			int sportId = sportRepository.findByNaam(sportName).getSportId();
			model.addAttribute("disciplineList", sportId == 1? new ArrayList<>() : disciplineRepository.findBySportSportId(sportId));
	    	//lijst van stadiumnamen:
	    	List<String> stadiumNamen = wedstrijdRepository.findBySportSportId(sportId).stream().map(Wedstrijd::getStadium_naam)
	    			.collect(Collectors.toUnmodifiableList());
	    	model.addAttribute("stadiumList", stadiumNamen);
			
			return "wedstrijdtoevoegen";
		}
		
    	String role = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().get();
    	model.addAttribute("role", role);
		
		//bij het passeren van de Form verliest Wedstrijd zijn Sport -> opnieuw toevoegen
		sportRepository.findByNaam(sportName).addWedstrijd(wedstrijd);
		wedstrijdRepository.save(wedstrijd);
		

		
		
		model.addAttribute("sportName", sportName);
		return "successpage";
		
	}

}
