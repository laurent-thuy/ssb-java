package ssb;

@SuppressWarnings("serial")
public class SSBDatasetException extends Exception {
	
	/**
	 * Instantiates a new SSB dataset exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public SSBDatasetException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Instantiates a new SSB dataset exception.
	 *
	 * @param message the message
	 */
	public SSBDatasetException(String message) {
		super(message);
	}
}
