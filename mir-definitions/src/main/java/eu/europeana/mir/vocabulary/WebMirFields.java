package eu.europeana.mir.vocabulary;

public interface WebMirFields {


	// common fields
	public static final String TEXT = "text";
	public static final String ROWS = "rows";
	public static final String QDOC_ID = "qdoc_id";
	public static final String START_ON = "start_on";
	public static final String LIMIT = "limit";

	// REST API query
	public static final String MIR = "mir";
	public static final String SUGGEST = "suggest";
	public static final String SLASH = "/";
	public static final String PAR_CHAR = "?";
	public static final String AND = "&";
	public static final String EQUALS = "=";
	public static final String PARAM_WSKEY = "wskey";
	
	// defaults
	public static final String DEFAULT_ROWS = "10";
	public static final String DEFAULT_LIMIT = "10";
	public static final String DEFAULT_START_ON = "0";

}
