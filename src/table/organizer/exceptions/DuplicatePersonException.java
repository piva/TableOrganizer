package table.organizer.exceptions;

public class DuplicatePersonException extends Exception {

	private static final long serialVersionUID = -8260200326456927845L;

	public DuplicatePersonException(String description){
		super(description);
	}
}
