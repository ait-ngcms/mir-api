package eu.europeana.mir.web.controller;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import eu.europeana.mir.config.MirConfiguration;
import eu.europeana.mir.model.MirRecord;
import eu.europeana.mir.web.exception.authentication.MirAuthenticationException;
import eu.europeana.mir.web.model.MirSearchResults;

public class BaseRest{

	@Resource
	MirConfiguration configuration;
	
	public BaseRest() {
		super();
	}

	protected MirConfiguration getConfiguration() {
		return configuration;
	}
	
	protected void validateApiKey(String wsKey) throws MirAuthenticationException {
		// throws exception if the wskey is not found
	}

	public MirSearchResults<MirRecord> buildSearchErrorResponse(String action,
			Throwable th) {

		MirSearchResults<MirRecord> response = new MirSearchResults<MirRecord>(action);
		response.success = false;
		response.error = th.getMessage();
		return response;
	}

	
	/**
	 * Get licenses list from comma separated licenses string.
	 * @param commaSepLicenses Comma separated licenses string
	 * @return Licenses list
	 */
	public List<String> getLicensesFromString(String commaSepLicenses) {
		
		if (StringUtils.isEmpty(commaSepLicenses)) {
			return null;
		}
		
		String[] licensesArr = commaSepLicenses.split(",");
		List<String> licensesList = Arrays.asList(licensesArr);
		return licensesList;
	}
	
}