package validator;

import java.time.LocalDate;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import domain.Wedstrijd;

public class DateValidator implements Validator {

	@Override
	public boolean supports(Class<?> klasse) {
		return Wedstrijd.class.isAssignableFrom(klasse);
	}

	/**
	 * datum moet liggen tussen:
	 * 26 juli 2024 en 11 augustus 2024
	 * 
	 */
	@Override
	public void validate(Object target, Errors errors) {
		Wedstrijd wedstrijd = (Wedstrijd) target;
		
		//controle datums:
		LocalDate dateMinimum = LocalDate.of(2024, 7, 26);
		LocalDate dateMaximum = LocalDate.of(2024, 8, 11);
				
		LocalDate wedstrijdDatum = wedstrijd.getStartDatum(); 
		
		if(wedstrijdDatum == null)
			return;
		
		if(wedstrijdDatum.isBefore(dateMinimum) || wedstrijdDatum.isAfter(dateMaximum)) {
			errors.rejectValue("startDatum", "datumValidatie.message", "ongeldige datum fallback");
		}
		
	}

}
