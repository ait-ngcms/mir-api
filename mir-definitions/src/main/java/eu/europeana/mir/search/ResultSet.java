/*
 * Copyright 2007-2012 The Europeana Foundation
 *
 *  Licenced under the EUPL, Version 1.1 (the "Licence") and subsequent versions as approved
 *  by the European Commission;
 *  You may not use this work except in compliance with the Licence.
 * 
 *  You may obtain a copy of the Licence at:
 *  http://joinup.ec.europa.eu/software/page/eupl
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under
 *  the Licence is distributed on an "AS IS" basis, without warranties or conditions of
 *  any kind, either express or implied.
 *  See the Licence for the specific language governing permissions and limitations under
 *  the Licence.
 */

package eu.europeana.mir.search;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import eu.europeana.mir.web.model.view.MirRecordView;

/**
 * taken from the corelib-definitions/corelib-search and eliminated explicit solr dependencies
 * @author Sergiu Gordea @ait
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({ "searchTime"})
@JsonPropertyOrder({ "qdocId", "resultSize", "results"})
public class ResultSet<T extends MirRecordView> {

	/**
	 * The request query object
	 */
	private Query query;

	private String qdocId;

	/**
	 * The language to be used by search handler
	 */
	private String language;

	/**
	 * The list of result objects
	 */
	private List<T> results;

	/**
	 * The list of facets
	 */
	private List<FacetFieldView> facetFields;

	/**
	 * The query facets response
	 */
	private Map<String, Integer> queryFacets;

	// statistics

	/**
	 * The total number of results
	 */
	private Long resultSize;

	/**
	 * The time in millisecond how long the search has been taken
	 */
	private Long searchTime = -1L;

	/**
	 * GETTERS & SETTTERS
	 */

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> list) {
		this.results = list;
	}

	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;
	}

	public List<FacetFieldView> getFacetFields() {
		return facetFields;
	}

	public void setFacetFields(List<FacetFieldView> facetFields) {
		this.facetFields = facetFields;
	}

	/**
	 * Gets the total number of results
	 * @return
	 */
	public Long getResultSize() {
		return resultSize;
	}

	public void setResultSize(long resultSize) {
		this.resultSize = resultSize;
	}

	public long getSearchTime() {
		return searchTime;
	}

	public void setSearchTime(long l) {
		this.searchTime = l;
	}

	public Map<String, Integer> getQueryFacets() {
		return queryFacets;
	}

	public void setQueryFacets(Map<String, Integer> queryFacets) {
		this.queryFacets = queryFacets;
	}

	@Override
	public String toString() {
		return "ResultSet [query=" + query + ", results=" + results
				+ ", facetFields=" + facetFields 
				+ ", resultSize=" + resultSize + ", searchTime=" + searchTime
				+ "]";
	}

	@JsonIgnore
	public boolean isEmpty(){
		return getResults() == null || getResults().isEmpty();
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getQdocId() {
		return qdocId;
	}

	public void setQdocId(String qdocId) {
		this.qdocId = qdocId;
	}
}
