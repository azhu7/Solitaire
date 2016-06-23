/*
 * Created June 22, 2016
 * Author: Alexander Zhu
 * Coauthor: Jason Zhu
 */

public class InvalidMoveException extends Exception{

	/**
	 * Autogenerated serialVersionUID (thanks Eclipse?)
	 */
	private static final long serialVersionUID = 2006919217943530036L;

	public InvalidMoveException() {
	}
	
	public InvalidMoveException(String message) {
		super(message);
	}
	
	public InvalidMoveException(Throwable cause) {
		super(cause);
	}
	
	public InvalidMoveException(String message, Throwable cause) {
		super(message, cause);
	}
}
