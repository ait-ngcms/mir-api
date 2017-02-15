package eu.europeana.mir.solr.exception;


public class MirSuggestionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -167260566275881316L;

	public MirSuggestionException(String message, Throwable th) {
		super(message, th);
	}

	public MirSuggestionException(String message) {
		super(message);
	}
	
	
}
