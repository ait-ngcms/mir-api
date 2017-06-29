package eu.europeana.mir.web.service.impl;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;

import eu.europeana.mir.search.ResultSet;
import eu.europeana.mir.solr.exception.MirRetrievalException;
import eu.europeana.mir.solr.service.SolrMirService;
import eu.europeana.mir.utils.MirUtils;
import eu.europeana.mir.web.exception.HttpException;
import eu.europeana.mir.web.model.view.MirRecordView;
import eu.europeana.mir.web.service.MirService;
//import org.python.util.PythonInterpreter; 


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


	@Override
	public String searchCdvsByText(
			String text
			, String start
			, String rows
			) throws HttpException {
		
//		ResultSet<? extends MirRecordView> result = null;
		String result = null;
		try {
			//C:\git\ait-ngcms\scoregraph\cdvs.py E:\app-test\testcollection -u all-test -q 
			
			/*PythonInterpreter interpreter = new PythonInterpreter();
			interpreter.exec("import sys\nsys.path.append('pathToModiles if they're not there by default')\nimport yourModule");
			// execute a function that takes a string and returns a string
			PyObject someFunc = interpreter.get("funcName");
			PyObject result = someFunc.__call__(new PyString("Test!"));
			String realResult = (String) result.__tojava__(String.class);*/
			
			String[] args = {
					  "python", 
					  "C:\\git\\ait-ngcms\\scoregraph\\cdvs.py", 
					  "E:\\app-test\\testcollection",
					  "-u",
					  "search-collection",
					  "-q",
					  text
					};
			ProcessBuilder pb = new ProcessBuilder(args);		
			pb.redirectErrorStream(true);
			Process p = pb.start();
			IOUtils.closeQuietly(p.getOutputStream());
			IOUtils.copy(p.getInputStream(), System.out);
			String output = IOUtils.toString(pb.start().getInputStream());
			IOUtils.closeQuietly(p.getInputStream());
			int returnVal = p.waitFor();
			result = output.split("#search result view#")[1];
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MirRetrievalException e) {
			throw new HttpException("Cannot retrieve similar images", HttpStatus.INTERNAL_SERVER_ERROR, e);
		}
		
		//if not found send appropriate error message
		if(result == null)
			throw new HttpException("No similar images found for query: " + text, HttpStatus.NOT_FOUND);
		
		return result;
	}


	@Override
	public String getMetadataJsonContent(String sdocId, String dataUrl) {
		
		return (new MirUtils()).getMetadataJsonContent(sdocId, dataUrl);
	}
	
}
