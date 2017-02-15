package eu.europeana.mir.solr.service.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;

import eu.europeana.mir.search.Query;
import eu.europeana.mir.search.ResultSet;
import eu.europeana.mir.solr.model.SolrMirRecordImpl;
import eu.europeana.mir.web.model.view.MirRecordView;


public abstract class BaseMirService {
	
	private static final int DEFAULT_FACET_LIMIT = 50;
	
	private final Logger log = Logger.getLogger(getClass());

	protected SolrQuery toSolrQuery(Query searchQuery) {

		SolrQuery solrQuery = new SolrQuery();

		solrQuery.setQuery(searchQuery.getQuery());

		solrQuery.setRows(searchQuery.getRows());
		solrQuery.setStart(searchQuery.getStart());

		if (searchQuery.getFilters() != null)
			solrQuery.addFilterQuery(searchQuery.getFilters());

		if (searchQuery.getFacetFields() != null) {
			solrQuery.setFacet(true);
			solrQuery.addFacetField(searchQuery.getFacetFields());
			solrQuery.setFacetLimit(DEFAULT_FACET_LIMIT);
		}

		if (searchQuery.getSort() != null) {
			solrQuery.setSort(searchQuery.getSort(), SolrQuery.ORDER.valueOf(searchQuery.getSortOrder()));
		}

		solrQuery.setFields(searchQuery.getViewFields());

		return solrQuery;
	}

	protected <T extends MirRecordView> ResultSet<T> buildResultSet(String qdocId, QueryResponse rsp) {

		ResultSet<T> resultSet = new ResultSet<>();
		@SuppressWarnings("unchecked")
		List<T> beans = (List<T>) rsp.getBeans(SolrMirRecordImpl.class);
		resultSet.setResults(beans);

		resultSet.setResultSize(rsp.getResults().getNumFound());
		resultSet.setQdocId(qdocId);
		return resultSet;
	}

	
	public Logger getLog() {
		return log;
	}

}
