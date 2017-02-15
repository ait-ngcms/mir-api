package eu.europeana.mir.web.service;

import java.io.IOException;

import eu.europeana.mir.search.ResultSet;
import eu.europeana.mir.web.exception.HttpException;
import eu.europeana.mir.web.model.view.MirRecordView;


public interface MirService {

	/**
	 * This method retrieves Mir object from REST interface.
	 * Example HTTP request for MIR object: 
	 *     http://localhost:8080/mir?gdoc_id=""&start_on=0&rows=10
	 * @param qdoc_id
	 * @param start_on
	 * @param rows
	 * @return response Mir that comprises response body, headers and status code.
	 * @throws IOException
	 */
	public ResultSet<? extends MirRecordView> search(
			String qdocId
			, String start
			, String rows
			)  throws HttpException;
				
}
