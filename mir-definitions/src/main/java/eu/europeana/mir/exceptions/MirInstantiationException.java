package eu.europeana.mir.exceptions;

/**
 * This class is used represent validation errors for the annotation class hierarchy 
 * @author Sergiu Gordea 
 *
 */
public class MirInstantiationException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6895963160368630124L;
	public static final String DEFAULT_MESSAGE = "Cannot instantiate Mir attribute: ";
	
	public MirInstantiationException(String attributeName){
		super(DEFAULT_MESSAGE + attributeName);
	}
	
	public MirInstantiationException(String attributeName , Throwable th){
		super(DEFAULT_MESSAGE + attributeName, th);
	}
	
}
