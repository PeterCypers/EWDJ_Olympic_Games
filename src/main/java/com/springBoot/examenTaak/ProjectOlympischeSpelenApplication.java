package com.springBoot.examenTaak;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//zet ook in comment als je de session locale niet gebruikt -> programma kan klagen als je deze imports niet gebruikt
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import mockdata.SportMock;
//import mockdata.StadiumMock;
import mockdata.WedstrijdMock;
import perform.PerformRestExample;
import service.GebruikerDetailService;
import service.TicketService;
import service.WedstrijdRestService;
import service.WedstrijdRestServiceImpl;
import validator.DateValidator;
import validator.DisciplinesValidator;
import validator.OlympicNumberOneValidator;
import validator.OlympicNumberTwoValidator;
import validator.TicketValidator;
import validator.TimeValidator;

@SpringBootApplication
@EnableJpaRepositories("repository") //(packagenaam vd repo(s)) hierdoor geen @Bean annotation nodig -> gebeurt nu vanzelf dankzij die klasse(GuestRepository)
@EntityScan("domain")
public class ProjectOlympischeSpelenApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(ProjectOlympischeSpelenApplication.class, args);
		
		demoRestExample();
	}
	
	private static void demoRestExample() {
		try {
			new PerformRestExample(); //vervangt postman
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		    registry.addRedirectViewController("/", "/sports");
		    registry.addViewController("/wedstrijden");
		    registry.addViewController("/tickets");
		    registry.addViewController("/placeholder").setViewName("placeholder");
		    registry.addViewController("/403").setViewName("403");
	}
	//Repositories -> handled by annotation
	//Services:
	@Bean
	GebruikerDetailService gebruikerDetailService() {
		return new GebruikerDetailService();
	}
	
	@Bean
	TicketService ticketService() {
		return new TicketService();
	}
	
	//Rest service:
	@Bean
	WedstrijdRestService wedstrijdRestService() {
		return new WedstrijdRestServiceImpl();
	}
	
	
	//Validation:
	@Bean
	DateValidator dateValidator() {
		return new DateValidator();
	}
	
	@Bean
	OlympicNumberOneValidator olympicNumberOneValidator() {
		return new OlympicNumberOneValidator();
	}
	
	@Bean
	OlympicNumberTwoValidator olympicNumberTwoValidator() {
		return new OlympicNumberTwoValidator();
	}
	
	@Bean
	DisciplinesValidator disciplinesValidator() {
		return new DisciplinesValidator();
	}
	
	@Bean
	TimeValidator timeValidator() {
		return new TimeValidator();
	}
	
	@Bean
	TicketValidator ticketValidator() {
		return new TicketValidator();
	}
	
	//locales:
	@Bean
	LocaleResolver localeResolver() {
	    SessionLocaleResolver slr = new SessionLocaleResolver();
	    //slr.setDefaultLocale(Locale.ENGLISH); //now handled on request -> see LocaleController class
	    return slr;
	}
	
	//mockdata(outdated):
	@Bean
	SportMock sportMock() {
		return new SportMock();
	}
//	@Bean
//	StadiumMock stadiumMock() {
//		return new StadiumMock();
//	}
	@Bean
	WedstrijdMock wedstrijdMock() {
		return new WedstrijdMock();
	}

}
