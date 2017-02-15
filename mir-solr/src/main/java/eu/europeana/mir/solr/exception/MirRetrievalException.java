package eu.europeana.mir.solr.exception;


public class MirRetrievalException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -167560566275381316L;

	public MirRetrievalException(String message, Throwable th) {
		super(message, th);
	}

	public MirRetrievalException(String message) {
		super(message);
	}
	
	
}
