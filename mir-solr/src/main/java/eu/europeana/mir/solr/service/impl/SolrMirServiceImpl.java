package eu.europeana.mir.solr.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

import eu.europeana.mir.search.ResultSet;
import eu.europeana.mir.solr.exception.MirRetrievalException;
import eu.europeana.mir.solr.service.SolrMirService;
import eu.europeana.mir.vocabulary.MirSolrFields;
import eu.europeana.mir.web.model.view.MirRecordView;


public class SolrMirServiceImpl extends BaseMirService implements SolrMirService {

	@Resource
	SolrServer solrServer;

	private final Logger log = Logger.getLogger(getClass());

	public void setSolrServer(SolrServer solrServer) {
		this.solrServer = solrServer;
	}

	public ResultSet<? extends MirRecordView> search(String qdocId, String start, String rows) {
 
		ResultSet<? extends MirRecordView> res = null;

		String msg = qdocId + "' and start: '" + start + "' and rows: '" + rows + "'.";
		getLogger().debug("search by qdoc ID: '" + msg);
		
		/**
		 * Validate QDOC ID
		 */
		String prefix = "";
		String ending = "";
		if (!qdocId.startsWith(MirSolrFields.QUOTE)) prefix = MirSolrFields.QUOTE;
		if (!qdocId.endsWith(MirSolrFields.QUOTE)) ending = MirSolrFields.QUOTE;
		String mirQuery = MirSolrFields.QDOC_ID + ":" + prefix + qdocId + ending;

		/**
	     * Construct a SolrQuery 
	     */
	    SolrQuery query = new SolrQuery(mirQuery);
	    try {
	    	if (StringUtils.isNotEmpty(start)) 
	    		query.setStart(Integer.parseInt(start));
	    	if (StringUtils.isNotEmpty(rows))
	    		query.setRows(Integer.parseInt(rows));
		} catch (Exception e) {
			throw new MirRetrievalException(
					"Unexpected exception occured when searching MIR documents for qdoc ID: " + qdocId, e);
        }
		log.info("limited query: " + query.toString());	    
	    
	    /**
	     * Query the server 
	     */
	    try {
	    	QueryResponse rsp =  solrServer.query( query );
			log.info("query response: " + rsp.toString());
			res = buildResultSet(qdocId, rsp);
			getLogger().debug("search obj res size: " + res.getResultSize());
		} catch (SolrServerException e) {
			throw new MirRetrievalException(
					"Unexpected exception occured when searching MIR documents for solr query: "
							+ query.toString(),
					e);
        }
	    
	    return res;
	}

	public Logger getLogger() {
		return log;
	}

	
	@Override
	public ResultSet<? extends MirRecordView> searchByTextAndLicense(
			String qdocId
			, String text
			, List<String> licenseList
			, String start
			, String rows
			) throws MirRetrievalException {
		
		ResultSet<? extends MirRecordView> res = null;

		String msg = qdocId + "' and text: '" + text + 
				"' and license: '" + licenseList.toString() + 
				"' and start: '" + start + "' and rows: '" + rows + "'.";
		
		getLogger().debug("search by qdoc ID: '" + msg);
		
		/**
		 * Validate QDOC ID
		 */
		String prefix = "";
		String ending = "";
		if (!qdocId.startsWith(MirSolrFields.QUOTE)) prefix = MirSolrFields.QUOTE;
		if (!qdocId.endsWith(MirSolrFields.QUOTE)) ending = MirSolrFields.QUOTE;

    	qdocId = prefix + qdocId + ending;
    	String mirQuery = text;

		/**
	     * Construct a SolrQuery 
	     */
	    SolrQuery query = new SolrQuery(mirQuery);
    	if (StringUtils.isNotEmpty(qdocId)) {
    		query.addFilterQuery(MirSolrFields.QDOC_ID + ":" + 
    				qdocId);
    	}
    	
	    if (licenseList.size() > 0) {
		    for (String license : licenseList) {
		    	query.addFilterQuery(MirSolrFields.SDOC_LICENSE + ":" + 
		    			license);
		    }
	    }
	    try {
	    	if (StringUtils.isNotEmpty(start)) 
	    		query.setStart(Integer.parseInt(start));
	    	if (StringUtils.isNotEmpty(rows))
	    		query.setRows(Integer.parseInt(rows));
		} catch (Exception e) {
			throw new MirRetrievalException(
					"Unexpected exception occured when searching MIR documents for qdoc ID: " + qdocId, e);
        }
		log.info("limited query: " + query.toString());	    
	    
	    /**
	     * Query the server 
	     */
	    try {
	    	QueryResponse rsp =  solrServer.query( query );
			log.info("query response: " + rsp.toString());
			res = buildResultSet(qdocId, rsp);
			getLogger().debug("search obj res size: " + res.getResultSize());
		} catch (SolrServerException e) {
			throw new MirRetrievalException(
					"Unexpected exception occured when searching MIR documents for solr query: "
							+ query.toString(),
					e);
        }
	    
	    return res;
	}

}
