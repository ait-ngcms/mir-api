package eu.europeana.mir.web.service;

import java.io.IOException;
import java.util.List;

import eu.europeana.mir.search.ResultSet;
import eu.europeana.mir.web.exception.HttpException;
import eu.europeana.mir.web.model.view.MirRecordView;


public interface MirService {

	/**
	 * This method retrieves Mir object from REST interface.
	 * Example HTTP request for MIR object: 
	 *     http://localhost:8080/mir/search?qdoc_id=2023601%2Foai_eu_dismarc_CHARM_DISC01SIDE02METSEE29&start=0&rows=10
	 * @param qdoc_id
	 * @param start
	 * @param rows
	 * @return response Mir that comprises response body, headers and status code.
	 * @throws IOException
	 */
	public ResultSet<? extends MirRecordView> search(
			String qdocId
			, String start
			, String rows
			)  throws HttpException;

	
	/**
	 * This method retrieves Mir object from REST interface.
	 * Example HTTP request for MIR object: 
	 *     http://localhost:8080/mir/searchByTextAndLicense?qdoc_id=2023601%2Foai_eu_dismarc_CHARM_DISC01SIDE02METSEE29&start=0&rows=10
	 * @param qdoc_id
	 * @param license list
	 * @param text
	 * @param start
	 * @param rows
	 * @return response Mir that comprises response body, headers and status code.
	 * @throws IOException
	 */
	public ResultSet<? extends MirRecordView> searchByTextAndLicense(
			String qdocId
			, String text
			, List<String> licenseList
			, String start
			, String rows
			)  throws HttpException;

	
	/**
	 * This method retrieves Mir objects from REST interface usind CDVS library.
	 * Example HTTP request for MIR object: 
	 *     http://localhost:8080/mir/searchCdvsByText?text=green&start=0&rows=10
	 * In background python script is executed e.g.
	 * C:\cdvs>python C:\git\ait-ngcms\scoregraph\cdvs.py E:\app-test\testcollection -u all-test -q O_446.jpg
	 * @param text
	 * @param start
	 * @param rows
	 * @return response that comprises response body, headers and status code.
	 * @throws IOException
	 */
	public String searchCdvsByText(
			String text
			, String start
			, String rows
			)  throws HttpException;

	
	/**
	 * This method retrieves metadata JSON content.
	 * @param sdocId
	 * @param dataUrl
	 * @return JSON string
	 */
	public String getMetadataJsonContent(String sdocId, String dataUrl);
	
}
