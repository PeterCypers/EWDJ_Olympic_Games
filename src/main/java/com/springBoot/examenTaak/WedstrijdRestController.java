package com.springBoot.examenTaak;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import domain.Wedstrijd;
import service.WedstrijdRestService;

@RestController
@RequestMapping(value = "/rest")
public class WedstrijdRestController {
	
	@Autowired
	private WedstrijdRestService wedstrijdRestService;
	
	@GetMapping(value = "/wedstrijd/{id}")
	public Integer getOverzichtVrijePlaatsen(@PathVariable("id") int wedstrijdId) {
		return wedstrijdRestService.getOverzichtBeschikbarePlaatsen(wedstrijdId);
	}
	
	@GetMapping(value = "/sportWedstrijden/{id}")
	public List<Wedstrijd> getWedstrijdenPerSport(@PathVariable("id") int sportId) {
		return wedstrijdRestService.getWedstrijdBySportId(sportId);
	}

	
}
