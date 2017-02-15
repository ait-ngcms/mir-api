package eu.europeana.mir.solr.model.vocabulary;

import eu.europeana.mir.vocabulary.WebMirFields;

public interface SuggestionFields {

	public static final String SUGGEST = "suggest";
	public static final String SUGGESTIONS = "suggestions";
	public static final String SUFFIX_EXACT = "_exact";
	public static final String SUFFIX_FUZZY = "_fuzzy";
	public static final String PREFIX_SUGGEST_ENTITY = "suggestEntity_";
	
	public static final String FILTER_IN_EUROPEANA = "in_europeana";
	public static final String PARAM_EUROPEANA = "europeana";
	
	
	public static final String TERM = "term";
	public static final String PAYLOAD = "payload";
	
	//TODO: update to correct values from specs
	public static final String TIME_SPAN_START = "lifespanStart";
	public static final String TIME_SPAN_END = "lifespanEnd";
	public static final String IN_SCHEME = "inScheme";
		
}
