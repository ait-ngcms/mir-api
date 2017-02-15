package eu.europeana.mir.web.exception;

import org.springframework.http.HttpStatus;


public class ParamValidationException extends HttpException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3664126076494279093L;
	public static final String MESSAGE_BLANK_PARAMETER_VALUE = "Invalid request. Parameter value must not be null or empty!";
	
	String parameterName;
	String parameterValue;
	
	
	public ParamValidationException(String message, String parameterName, String parameterValue){
		this(message, parameterName, parameterValue, null);
	}
	public ParamValidationException(String message, String parameterName, String parameterValue, Throwable th){
		this(message, parameterName, parameterValue, HttpStatus.BAD_REQUEST, th);
	}
	public ParamValidationException(String message, String parameterName, String parameterValue, HttpStatus status, Throwable th){
		super(message + " " + parameterName + ":" + parameterValue, status, th);
		this.parameterName = parameterName;
		this.parameterValue = parameterValue;
	}
}
