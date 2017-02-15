package eu.europeana.mir.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import eu.europeana.mir.model.MirRecord;
import eu.europeana.mir.web.model.MirApiResponse;

@Deprecated
public class ApiResponseBuilder {

	Logger logger = Logger.getLogger(getClass());
	private static ObjectMapper objectMapper = new ObjectMapper();
	

	public Logger getLogger() {
		return logger;
	}

	public MirApiResponse buildErrorResponse(String errorMessage, String action) {
		MirApiResponse response;
		response = new MirApiResponse(action);

		response.success = false;
		response.error = errorMessage;
		return response;
	}

	public MirApiResponse buildResponse(MirRecord mir, String action) {
		MirApiResponse response;
		response = new MirApiResponse(action);

		response.success = true;
		response.setMir(mir);
		//response. setStatus(message);
		return response;
	}

	protected MirApiResponse getErrorReport(String action, String errorMessage,
			Throwable th, boolean includeErrorStack) {

		MirApiResponse response = new MirApiResponse(action);

		String message = "";

		if (errorMessage != null)
			message += " " + errorMessage;
		if (th != null)
			message += th.toString();
		if (th != null && th.getCause() != null && th != th.getCause())
			message += " " + th.getCause().toString();

		response = buildErrorResponse(message, response.action);

		if(includeErrorStack && th != null)
			response.setStackTrace(getStackTraceAsString(th));
		
		return response;
	}

	
	String getStackTraceAsString(Throwable th) {
		StringWriter out = new StringWriter();
		th.printStackTrace(new PrintWriter(out));
		return out.toString();
	}

	
	protected String serializeResponse(MirApiResponse res) {
		String errorMessage;
		try {
			//correct serialization
			return objectMapper.writeValueAsString(res);
		} catch (JsonGenerationException e) {
			getLogger().error("Json Generation Exception: " + e.getMessage(),e);
			errorMessage = "Json Generation Exception: " + e.getMessage() + " See error logs!";
		} catch (JsonMappingException e) {
			getLogger().error("Json Mapping Exception: " + e.getMessage(),e);
			errorMessage = "Json Generation Exception: " + e.getMessage() + " See error logs!";
		} catch (IOException e) {
			getLogger().error("I/O Exception: " + e.getMessage(),e);
			errorMessage = "I/O Exception: " + e.getMessage() + " See error logs!";
		}
		return errorMessage;
	}
}
