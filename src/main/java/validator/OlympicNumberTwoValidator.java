package validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import domain.Wedstrijd;

public class OlympicNumberTwoValidator implements Validator{

	@Override
	public boolean supports(Class<?> klasse) {
		return Wedstrijd.class.isAssignableFrom(klasse);
	}
	
	/**
	 * <strong>oNr2 rules:</strong>
	 * <ul>
	 * <li>oNr2 >= oNr1 -1000  ->  oNr2 < oNr1 - 1000
	 * <li>oNr2 <= oNr +1000  ->  oNr2 > oNr1 + 1000
	 * </ul>
	 */
	@Override
	public void validate(Object target, Errors errors) {
		Wedstrijd wedstrijd = (Wedstrijd) target;
		int oNr1 = wedstrijd.getONr1();
		int oNr2 = wedstrijd.getONr2();
		
		if(oNr2 < oNr1 - 1000) {
			errors.rejectValue("oNr2", "wedstrijd.oNr2.teklein", "nummer is te klein");
		}else if(oNr2 > oNr1 + 1000) {
			errors.rejectValue("oNr2", "wedstrijd.oNr2.tegroot", "nummer is te groot");
		}
		
	}

}
