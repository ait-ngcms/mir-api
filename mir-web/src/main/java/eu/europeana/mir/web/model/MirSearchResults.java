package eu.europeana.mir.web.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_EMPTY)
public class MirSearchResults<T> extends AbstractSearchResults<T> {

	public MirSearchResults(String action){
		super(action);
	}
}
