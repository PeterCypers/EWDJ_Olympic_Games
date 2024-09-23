package validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import domain.Wedstrijd;
import repository.WedstrijdRepository;

public class OlympicNumberOneValidator implements Validator {
	
	@Autowired
	WedstrijdRepository wedstrijdRepository;

	@Override
	public boolean supports(Class<?> klasse) {
		return Wedstrijd.class.isAssignableFrom(klasse);
	}

	/**
	 * <strong>oNr1 rules:</strong> 
	 * <ul>
	 * <li>mag niet met 0 beginnen -> geen string -> conver to int + voorloop 0's worden afgekapt bij submit
	 * <li>moet uit 5 cijfers bestaan -> (number / 10_000 >= 1) && (number / 100_000 < 1) -> (number / 10_000 < 1) || (number / 100_000 >= 1)
	 * <li>1st getal != 5de getal -> ((number - number % 10_000) / 10_000) != (number % 10) -> x == y
	 * <li>mag niet reeds voorkomen in de databank. -> list Wedstrijd uit DB -> map to oNr1 -> contains
	 * </ul>
	 */
	@Override
	public void validate(Object target, Errors errors) {
		Wedstrijd wedstrijd = (Wedstrijd) target;
		int oNr1 = wedstrijd.getONr1();
		
		List<Wedstrijd> alleWedstrijden =  (List<Wedstrijd>) wedstrijdRepository.findAll();
		List<Integer> reedsInDatabankOnr1 = alleWedstrijden.stream().map(Wedstrijd::getONr1).toList();
		
		if((oNr1 / 10_000 < 1) || (oNr1 / 100_000 >= 1)) {
			errors.rejectValue("oNr1", "wedstrijd.oNr1.cijferlengte", "getal valt buiten range");
		}else if(((oNr1 - oNr1 % 10_000) / 10_000) == (oNr1 % 10)) {
			errors.rejectValue("oNr1", "wedstrijd.oNr1.eerstegelijklaatste", "eerste=laatste");
		}else if(reedsInDatabankOnr1.contains(oNr1)) {
			errors.rejectValue("oNr1", "wedstrijd.oNr1.reedsInDatabank", "zit al in de databank");
		}
		
	}

}
