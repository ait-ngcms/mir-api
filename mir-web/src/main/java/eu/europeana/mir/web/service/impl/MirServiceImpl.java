package eu.europeana.mir.web.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;

import eu.europeana.mir.search.ResultSet;
import eu.europeana.mir.solr.exception.MirRetrievalException;
import eu.europeana.mir.solr.service.SolrMirService;
import eu.europeana.mir.web.exception.HttpException;
import eu.europeana.mir.web.model.view.MirRecordView;
import eu.europeana.mir.web.service.MirService;

public class MirServiceImpl implements MirService {

	@Resource 
	SolrMirService solrMirService;
	
	@Override
	public ResultSet<? extends MirRecordView> search(
			String qdocId
			, String start
			, String rows
			)  throws HttpException {
		
		ResultSet<? extends MirRecordView> result;
		try {
			result = solrMirService.search(qdocId, start, rows);
		} catch (MirRetrievalException e) {
			throw new HttpException("Cannot retrieve MIR document by QDOC ID", HttpStatus.INTERNAL_SERVER_ERROR, e);
		}
		
		//if not found send appropriate error message
		if(result == null)
			throw new HttpException("No MIR document found for QDOC ID: " + qdocId, HttpStatus.NOT_FOUND);
		
		return result;
	}

	
	@Override
	public ResultSet<? extends MirRecordView> searchByTextAndLicense(
			String qdocId
			, String text
			, List<String> licenseList
			, String start
			, String rows
			) throws HttpException {
		
		ResultSet<? extends MirRecordView> result;
		try {
			result = solrMirService.searchByTextAndLicense(qdocId, text, licenseList, start, rows);
		} catch (MirRetrievalException e) {
			throw new HttpException("Cannot retrieve MIR document by QDOC ID", HttpStatus.INTERNAL_SERVER_ERROR, e);
		}
		
		//if not found send appropriate error message
		if(result == null)
			throw new HttpException("No MIR document found for QDOC ID: " + qdocId, HttpStatus.NOT_FOUND);
		
		return result;
	}
	
}
