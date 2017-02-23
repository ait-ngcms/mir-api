package eu.europeana.mir.solr.service;

import java.util.List;

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
	public ResultSet<? extends MirRecordView> search(
			String qdocId
			, String start
			, String rows
			) throws MirRetrievalException;
	
	
	/**
	 * This method retrieves available MIR documents by searching the given MIR qdoc ID, 
	 * text and license.
	 * @param qdocId
	 * @param text
	 * @param licenseList
	 * @param start
	 * @param rows
	 * @return
	 * @throws MirRetrievalException
	 */
	public ResultSet<? extends MirRecordView> searchByTextAndLicense(
			String qdocId
			, String text
			, List<String> licenseList
			, String start
			, String rows
			)  throws MirRetrievalException;
	
}
