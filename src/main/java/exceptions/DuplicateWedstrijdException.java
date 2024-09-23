package exceptions;

public class DuplicateWedstrijdException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public DuplicateWedstrijdException(Integer id) {
		super(String.format("Kon de wedstrijd op id %s niet vinden", id));
	}

}
