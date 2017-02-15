package eu.europeana.mir.solr.service;

import eu.europeana.mir.search.ResultSet;
import eu.europeana.mir.solr.exception.MirRetrievalException;
import eu.europeana.mir.web.model.view.MirRecordView;

public interface SolrMirService {

	/**
	 * This method retrieves available MIR documents by searching the given MIR qdoc ID.
	 * @param qdocId
	 * @param start
	 * @param rows
	 * @return
	 * @throws MirRetrievalException
	 */
	public ResultSet<? extends MirRecordView> search(String qdocId, String start, String rows) 
			throws MirRetrievalException;
	
}
