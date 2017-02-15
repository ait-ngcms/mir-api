package eu.europeana.mir.web.controller;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.europeana.api.common.config.swagger.SwaggerSelect;
import eu.europeana.mir.search.ResultSet;
import eu.europeana.mir.web.exception.HttpException;
import eu.europeana.mir.web.exception.InternalServerException;
import eu.europeana.mir.web.http.HttpHeaders;
import eu.europeana.mir.web.model.view.MirRecordView;
import eu.europeana.mir.web.service.MirService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(tags = "Discovery API")
@SwaggerSelect
public class SearchController extends BaseRest {

	@Resource 
	MirService mirService;
	
//	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Search MIR documents for the given text query.", nickname = "searchSimilar", response = java.lang.Void.class)
	@RequestMapping(value = {"/mir/search"}, method = RequestMethod.GET, 
		produces = {HttpHeaders.CONTENT_TYPE_JSON_UTF8})
	public ResponseEntity<String> searchSimilar(
			@RequestParam(value = WebMirConstants.QUERY_PARAM_QDOC_ID) String qDocId,
			@RequestParam(value = WebMirConstants.QUERY_PARAM_START, defaultValue = "0") String start,
			@RequestParam(value = WebMirConstants.QUERY_PARAM_ROWS, defaultValue = WebMirConstants.PARAM_DEFAULT_ROWS) String rows
			) throws HttpException  {

		try {			
			
			ResultSet<? extends MirRecordView> results = mirService.search(qDocId, start, rows);
			String serialized = new ObjectMapper().writeValueAsString(results);
			
			return new ResponseEntity<>(serialized, HttpStatus.OK);

		} catch (RuntimeException e) {
			//not found .. 
			throw new InternalServerException(e);
		} catch (HttpException e) {
			//avoid wrapping http exception
			throw e;
		} catch (Exception e) {
			throw new InternalServerException(e);
		}
			
	}
	
	/**
	 * Get entity type string list from comma separated entities string.
	 * @param commaSepMirTypes Comma separated entities string
	 * @return Mir types string list
	 * @throws ParamValidationException 
	 */
//	public MirTypes[] getMirTypesFromString(String commaSepMirTypes) throws ParamValidationException {
//		
//			String[] splittedMirTypes = commaSepMirTypes.split(",");
//			MirTypes[] entityTypes = new MirTypes[splittedMirTypes.length];
//			
//			MirTypes entityType = null;
//			String typeAsString;
//			
//			for (int i = 0; i < splittedMirTypes.length; i++) {
//				typeAsString = splittedMirTypes[i].trim();
//				entityType = MirTypes.getByInternalType(typeAsString);
//				
//				if(entityType == null)
//					throw new ParamValidationException("Invalid request parameter value! ", WebMirConstants.QUERY_PARAM_TYPE, typeAsString);
//				
//				entityTypes[i] = entityType;
//				
//			}
//			
//			return entityTypes;
//	}
//		
}
