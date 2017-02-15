package eu.europeana.mir.web.controller;

import org.apache.commons.lang.StringUtils;

import eu.europeana.mir.model.MirRecord;
//import eu.europeana.entity.definitions.model.Concept;
import eu.europeana.mir.web.controller.exception.GlobalExceptionHandling;
import eu.europeana.mir.web.exception.authentication.MirAuthenticationException;
import eu.europeana.mir.web.model.MirSearchResults;

public class BaseRest{

	public BaseRest() {
		super();
	}

	protected void validateApiKey(String wsKey) throws MirAuthenticationException {
		// throws exception if the wskey is not found
	}

	public MirSearchResults<MirRecord> buildSearchErrorResponse(String action,
			Throwable th) {

		MirSearchResults<MirRecord> response = new MirSearchResults<MirRecord>(action);
		response.success = false;
		response.error = th.getMessage();
		// response.requestNumber = 0L;

		return response;
	}

	
}