package eu.europeana.mir.solr.exception;


public class MirRuntimeException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -167560526275881316L;

	public MirRuntimeException(String message, Throwable th) {
		super(message, th);
	}

	public MirRuntimeException(String message) {
		super(message);
	}
	
	
}
