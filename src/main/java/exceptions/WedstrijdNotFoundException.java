package exceptions;

public class WedstrijdNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public WedstrijdNotFoundException(Integer id) {
		super(String.format("Wedstrijd met id %s niet gevonden.", id));
	}

}
