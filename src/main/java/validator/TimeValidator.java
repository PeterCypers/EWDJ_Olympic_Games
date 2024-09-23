package validator;

import java.time.LocalTime;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import domain.Wedstrijd;

public class TimeValidator implements Validator {

	@Override
	public boolean supports(Class<?> klasse) {
		return Wedstrijd.class.isAssignableFrom(klasse);
	}

	/**
	 * starttijd moet vanaf 8uur zijn
	 */
	@Override
	public void validate(Object target, Errors errors) {
		Wedstrijd wedstrijd = (Wedstrijd) target;
		
		LocalTime wedstrijdStart = wedstrijd.getStartTijd();
		
		if(wedstrijdStart == null) {
			return;
		}
		
		if(wedstrijdStart.isBefore(LocalTime.of(8, 0))) {
			errors.rejectValue("startTijd", "tijdValidatie.message", "startTijd verkeerd");
		}
		
	}

}
