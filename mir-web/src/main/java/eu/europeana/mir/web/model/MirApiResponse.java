package eu.europeana.mir.web.model;

import eu.europeana.mir.web.model.json.abstracts.ApiResponse;
import eu.europeana.mir.model.MirRecord;

public class MirApiResponse extends ApiResponse{
	
	MirRecord mir;
	String status;
	String stackTrace;

	public static String ERROR_NO_OBJECT_FOUND = "No Object Found!";
	public static String ERROR_VISIBILITY_CHECK = "This annotation object is marked as not visible!";
	public static String ERROR_RESOURCE_ID_DOES_NOT_MATCH = 
			"Passed 'collection' or 'object' parameter does not match to the ResourceId given in the JSON string!";	
	public static String ERROR_PROVIDER_DOES_NOT_MATCH = 
			"Passed 'provider' parameter does not match to the provider given in the JSON string!";	
	
	public static String ERROR_ANNOTATION_EXISTS_IN_DB = 
			"Cannot store object! An object with the given id already exists in the database: ";
	
	public static String ERROR_STATUS_TYPE_NOT_REGISTERED = 
			"Cannot set annotation status! A given status type is not registered: ";
	
	public static String ERROR_STATUS_ALREADY_SET = 
			"A given status type is already set: ";
	
	public static String ERROR_SUGGESTION_NOT_FOUND = "Suggestion not found!";
	public static String ERROR_ENTITY_NOT_FOUND = "Entity not found!";

	public MirApiResponse(String action){
		super(action);
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public MirRecord getMir() {
		return mir;
	}

	public void setMir(MirRecord mir) {
		this.mir = mir;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

}
