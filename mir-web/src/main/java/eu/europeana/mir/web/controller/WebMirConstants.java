package eu.europeana.mir.web.controller;

import eu.europeana.mir.vocabulary.WebMirFields;

public interface WebMirConstants extends WebMirFields{

	String PARAM_WSKEY = "wskey";
	String PATH_PARAM_TYPE = "type";
	String PARAM_DEFAULT_ROWS = "10";
	String QUERY_PARAM_QDOC_ID = "qdoc_id";
	String QUERY_PARAM_ROWS = "rows";
	String QUERY_PARAM_START = "start";
		
	/**
	 * Search API response
	 */
	public static final String SEARCH_RESP_FACETS = "facets";
	public static final String SEARCH_RESP_FACETS_FIELD = "field";
	public static final String SEARCH_RESP_FACETS_VALUES = "values";
	public static final String SEARCH_RESP_FACETS_COUNT = "count";
	public static final String TOTAL_ITEMS = "totalItems";
	
	/**
	 * PARAMS
	 */
	public static final String PARAM_INCLUDE_ERROR_STACK = "includeErrorStack";
	
}
