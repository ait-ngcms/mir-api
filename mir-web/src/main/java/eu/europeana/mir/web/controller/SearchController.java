package eu.europeana.mir.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.europeana.api.common.config.swagger.SwaggerSelect;
import eu.europeana.mir.search.ResultSet;
import eu.europeana.mir.utils.MirConst;
import eu.europeana.mir.web.exception.HttpException;
import eu.europeana.mir.web.exception.InternalServerException;
import eu.europeana.mir.web.exception.ParamValidationException;
import eu.europeana.mir.web.http.HttpHeaders;
import eu.europeana.mir.web.model.view.MirRecordView;
import eu.europeana.mir.web.service.MirService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * This is a MIR API.
 * 
 * @author GrafR
 *
 */
@Controller
@Api(tags = "Discovery API")
@SwaggerSelect
public class SearchController extends BaseRest {

	@Resource 
	MirService mirService;
	
	@ApiOperation(value = "Search MIR documents for the given qdoc ID.", nickname = "searchById", response = java.lang.Void.class)
	@RequestMapping(value = {"/mir/searchById"}, method = RequestMethod.GET, 
		produces = {HttpHeaders.CONTENT_TYPE_JSON_UTF8})
	public ResponseEntity<String> searchSimilar(
			@RequestParam(value = WebMirConstants.QUERY_PARAM_QDOC_ID) String qDocId,
			@RequestParam(value = WebMirConstants.QUERY_PARAM_START, defaultValue = "0") String start,
			@RequestParam(value = WebMirConstants.QUERY_PARAM_ROWS, defaultValue = WebMirConstants.PARAM_DEFAULT_ROWS) String rows
			) throws HttpException  {

		try {			
			
			//validate qdoc ID
			if(StringUtils.isEmpty(qDocId))
				throw new ParamValidationException(
						"Invalid request parameter value! ", WebMirConstants.QUERY_PARAM_QDOC_ID, qDocId);
			
			ResultSet<? extends MirRecordView> results = mirService.search(qDocId, start, rows);
			ObjectMapper objectMapper = new ObjectMapper();
			String serialized = objectMapper.writeValueAsString(results);
			
			
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
	

	@ApiOperation(value = "Search MIR documents for the given qdoc ID, text and license query.", nickname = "search", response = java.lang.Void.class)
	@RequestMapping(value = {"/mir/search"}, method = RequestMethod.GET, 
		produces = {HttpHeaders.CONTENT_TYPE_JSON_UTF8})
	public ResponseEntity<String> searchByTextAndLicense(
			@RequestParam(value = WebMirConstants.QUERY_PARAM_TEXT) String text,
			@RequestParam(value = WebMirConstants.QUERY_PARAM_QDOC_ID, required = false) String qDocId,
			@RequestParam(value = WebMirConstants.QUERY_PARAM_LICENSE, required = false) String license,
			@RequestParam(value = WebMirConstants.QUERY_PARAM_PROFILE, defaultValue = "MINIMAL", required = false) WebMirConstants.Profiles profile,
			@RequestParam(value = WebMirConstants.QUERY_PARAM_START, defaultValue = "0") String start,
			@RequestParam(value = WebMirConstants.QUERY_PARAM_ROWS, defaultValue = WebMirConstants.PARAM_DEFAULT_ROWS) String rows
			) throws HttpException  {

		try {			
			
			//validate qdoc ID
			if(StringUtils.isEmpty(qDocId))
				qDocId = "";
//				throw new ParamValidationException(
//						"Invalid request parameter value! ", WebMirConstants.QUERY_PARAM_QDOC_ID, qDocId);
			
			//validate and convert text
			if(StringUtils.isEmpty(text))
				throw new ParamValidationException(
						"Invalid request parameter value! ", WebMirConstants.QUERY_PARAM_TEXT, text);
			
			//validate and convert licenses
//			if(StringUtils.isEmpty(license))
//				throw new ParamValidationException("Invalid request parameter value! "
//						, WebMirConstants.QUERY_PARAM_LICENSE, license);
			
			//validate and convert profile
			if(profile == null)
				profile = WebMirConstants.Profiles.MINIMAL;

			List<String> licenseList = new ArrayList<String>();
			if(StringUtils.isNotEmpty(license))
				licenseList = getLicensesFromString(license);
			
			ResultSet<? extends MirRecordView> results = mirService.searchByTextAndLicense(
					qDocId, text, licenseList, start, rows);
			
			if (profile == WebMirConstants.Profiles.FULL) {
				String dataUrl = getConfiguration().getDataUrl() + MirConst.REMOTE_METADATA_FOLDER;
				List<? extends MirRecordView> resultList = results.getResults();
				String metadata;
				for (MirRecordView result : resultList) {
					metadata = mirService.getMetadataJsonContent(result.getSdocId(), dataUrl);
					result.setMetadata(metadata);
				}
			}
			
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
	
}
