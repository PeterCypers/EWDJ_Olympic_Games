package validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import domain.Wedstrijd;

public class DisciplinesValidator implements Validator {

	@Override
	public boolean supports(Class<?> klasse) {
		return Wedstrijd.class.isAssignableFrom(klasse);
	}

	/**
	 * <string>discipline regels:</strong>
	 * <ul>
	 * <li>0-2 disciplines zijn ingevuld
	 * <li>indien 2 ingevuld -> d1 != d2
	 * </ul>
	 */
	@Override
	public void validate(Object target, Errors errors) {
		Wedstrijd wedstrijd = (Wedstrijd) target;
		String d1 = wedstrijd.getDiscipline1();
		String d2 = wedstrijd.getDiscipline2();
		
		if(d1 == null || d2 == null) {
			return;
		}
		
		if(!d1.isEmpty() && !d2.isEmpty() && d1.equals(d2)) {
			errors.rejectValue("discipline2", "wedstrijd.discipline2.equalsdiscipline1", "duplikaat disciplines");
		}
		
		
	}

}
