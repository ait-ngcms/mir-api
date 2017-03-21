package eu.europeana.mir.utils;

public class MirConst {

	/**
	 * JSON helper constants
	 */
	protected String TITLE = "title";
	protected String LICENSE = "rights";

	
	/**
	 * This is a limit for similarity score numbers. 
	 * We take lowest values starting from 0.0.
	 */
	protected final int MAX_SCORES_NUMBER = 200;

	
	/**
	 * Formats
	 */
	protected String GZ_EXT = "gz";
	protected String CSV_EXT = "csv";
	protected String XML_EXT = "xml";
	protected String JSON_EXT = "json";
	
	
	/**
	 * Parsing constants.
	 */
	protected final String LINE_BREAK = "\n"; 
	protected final String TAB = "\t"; 
	protected final String PATH_PARSING_DELIMITER = "\\\\";
	protected final String PATH_ID_DELIMETER = "/";
	protected final String BACK_SLASH = "\\";	
	
	
	/**
	 * Folders
	 */
	protected String REMOTE_BASE_FILE_PATH  = "F:\\";
	protected String REMOTE_GENERATED_PATH  = REMOTE_BASE_FILE_PATH + "generated/solr/";
	protected String REMOTE_METADATA_FOLDER = REMOTE_GENERATED_PATH + "metadata";

}
