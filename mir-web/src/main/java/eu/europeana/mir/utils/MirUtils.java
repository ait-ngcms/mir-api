package eu.europeana.mir.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.europeana.mir.model.BaseMirRecordImpl;


/**
 * This is a helper class for MIR-API.
 * 
 * @author GrafR
 *
 */
public class MirUtils extends MirConst {


	protected Logger log = Logger.getLogger(getClass());	

	
	
	/**
	 * Read collection directories.
	 * @param folder
	 * @return
	 */
	public List<String> listDirsForFolder(final File folder) {
		
		List<String> collectionDirs = new ArrayList<String>();
		
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	if (!fileEntry.getPath().startsWith(".git"))
	        		collectionDirs.add(fileEntry.getPath());
	        }
	    }
	    
	    return collectionDirs;
	}
	
	
	/**
	 * Read collection files.
	 * @param folder
	 * @return
	 */
	public List<String> listFilesForFolder(final File folder) {
		
		List<String> collectionFiles = new ArrayList<String>();
		
		if (folder != null && new File(folder.getAbsolutePath()).exists()) {
		    for (final File fileEntry : folder.listFiles()) {
		        if (fileEntry.isDirectory()) {
		            listFilesForFolder(fileEntry);
		        } else {
		        	collectionFiles.add(fileEntry.getPath());
		        }
		    }
		}	    
	    return collectionFiles;
	}
	
	
    /**
     * This is a help method from: 
     * http://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values-java
     * @param input map 
     * @return map sorted by value
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> 
	    sortByValue( Map<K, V> map )
	{
	    List<Map.Entry<K, V>> list =
	        new LinkedList<Map.Entry<K, V>>( map.entrySet() );
	    Collections.sort( list, new Comparator<Map.Entry<K, V>>()
	    {
	        public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
	        {
	            return (o1.getValue()).compareTo( o2.getValue() );
	        }
	    } );
	
	    Map<K, V> result = new LinkedHashMap<K, V>();
	    for (Map.Entry<K, V> entry : list)
	    {
	        result.put( entry.getKey(), entry.getValue() );
	    }
	    return result;
	}	
	
    
    /**
     * @param indexFilePath
     * @return
     */
    public List<String> readEuropeanaIdIndexForDistanceFile(String indexFilePath) {
    	
    	List<String> europeanaIdList = new ArrayList<String>();
	
		File csvFile = FileUtils.getFile(indexFilePath);
		LineIterator iterator;
		try {
			iterator = FileUtils.lineIterator(csvFile);
			
			while (iterator.hasNext()) {				
				String line = (String) iterator.next();
				europeanaIdList.add(line);
			}
		} catch (IOException e) {
			log.error("Error reading Europeana IDs from index for distance CSV file. " + e.getMessage());
		}		
		
		return europeanaIdList;
    }
    
    
	/**
	 * This method reads scores from extracted CSV file, metadata from related JSON file 
	 * and creates Mir entities.
	 * 
	 * @param csvFilePath
	 * @param indexFilePath
	 * @param csvFolder
	 * @param metadataFolder
	 * @return list of Mir entities
	 */
	public List<BaseMirRecordImpl> readMirImplWithScoresFromCsv(
			String csvFilePath, String indexFilePath, String csvFolder, String metadataFolder) {
		
		final int NUMBERS_AFTER_COMMA = 4;
		
		List<BaseMirRecordImpl> mirImplList = new ArrayList<BaseMirRecordImpl>();

		/**
		 * This is a mapping between Europeana IDs from index file
		 * and scores from input CSV files.
		 */
		Map<String, Float> scoreMap = new HashMap<String, Float>();
		
		List<String> europeanaIdList = readEuropeanaIdIndexForDistanceFile(indexFilePath);
		
		File csvFile = FileUtils.getFile(csvFilePath);
		LineIterator iterator;
		int idx = 0;
		try {
			iterator = FileUtils.lineIterator(csvFile);
			
			while (iterator.hasNext()) {
				
				String line = (String) iterator.next();
				int pointPos = line.indexOf(".");
				String floatPartStr = line.substring(pointPos + 1, line.length());
				int lastIndex = pointPos + NUMBERS_AFTER_COMMA;
				
				/**
				 * Check case when float number has fewer numbers after comma 
				 * then specified in NUMBERS_AFTER_COMMA.
				 */
				if (lastIndex > floatPartStr.length()) {
					lastIndex = floatPartStr.length();
				}
				String score = line.substring(0, lastIndex);
				float floatScore = Float.parseFloat(score);
				scoreMap.put(europeanaIdList.get(idx), floatScore);
				idx++;
			}
		} catch (IOException e) {
			log.error("Error reading scores from CSV file. " + e.getMessage());
		}		
		
		scoreMap = sortByValue(scoreMap);

		int cnt = 0;
		for(Map.Entry<String, Float> entry : scoreMap.entrySet()) {		
			
			String sdocId = PATH_ID_DELIMETER + entry.getKey();
			Float score = entry.getValue();
			
			BaseMirRecordImpl mirImpl = new BaseMirRecordImpl();
			
			String metadataFilePath = metadataFolder + sdocId + "." + JSON_EXT;
			
			String sdocTitle = getJsonFieldValueFromFile(metadataFilePath, TITLE);
			String sdocLicense = getJsonFieldValueFromFile(metadataFilePath, LICENSE);
			
			int csvFolderPos = csvFilePath.indexOf(csvFolder);
			String qdocId = csvFilePath.substring(csvFolderPos + csvFolder.length())
					.replace(".csv", "").replace(BACK_SLASH, PATH_ID_DELIMETER);

			mirImpl.setQdocId(qdocId);
			mirImpl.setSdocId(sdocId);
			String recordId = qdocId + sdocId;
			mirImpl.setRecordId(recordId);
			mirImpl.setSdocScore(score);
			mirImpl.setSdocTitle(sdocTitle);
			mirImpl.setSdocLicense(sdocLicense);
			mirImplList.add(mirImpl);
			log.info("added " + cnt + " Mir entity: " + mirImpl);
			cnt++;
			if (cnt > MAX_SCORES_NUMBER) break;
		}
		
	    return mirImplList;
	}
	
	
	/**
	 * This method stores MIR entities according to the template in MirImpl object.
	 * @param mirImpl
	 * @param xmlFile
	 * @return
	 */
	public boolean storeMirImplInXml(List<BaseMirRecordImpl> mirImplList, String xmlFile) {
	
		boolean res = false;
		
		File recordFile = FileUtils.getFile(xmlFile);

		StringBuilder row = new StringBuilder();
		row.append("<add>").append(LINE_BREAK);
		for (BaseMirRecordImpl mirImpl: mirImplList) {
			row.append(TAB).append("<doc>").append(LINE_BREAK);
			row.append(TAB).append(TAB).append("<field name=\"record_id\">")
				.append(mirImpl.getRecordId()).append("</field>").append(LINE_BREAK);
			row.append(TAB).append(TAB).append("<field name=\"qdoc_id\">")
				.append(mirImpl.getQdocId()).append("</field>").append(LINE_BREAK);
			row.append(TAB).append(TAB).append("<field name=\"sdoc_id\">")
				.append(mirImpl.getSdocId()).append("</field>").append(LINE_BREAK);
			row.append(TAB).append(TAB).append("<field name=\"sdoc_score\">")
				.append(Float.toString(mirImpl.getSdocScore())).append("</field>").append(LINE_BREAK);
			
			String escapedXml = StringEscapeUtils.escapeXml(mirImpl.getSdocTitle());
			row.append(TAB).append(TAB).append("<field name=\"sdoc_title\">")
				.append(escapedXml).append("</field>").append(LINE_BREAK);
			row.append(TAB).append(TAB).append("<field name=\"sdoc_license\">")
				.append(mirImpl.getSdocLicense()).append("</field>").append(LINE_BREAK);
			row.append(TAB).append(TAB).append("<field name=\"sdoc_license_group\">")
				.append("").append("</field>").append(LINE_BREAK);
			row.append(TAB).append("</doc>").append(LINE_BREAK);
		}
		row.append("</add>").append(LINE_BREAK);
		
		try {
			String doc = row.toString();
			FileUtils.writeStringToFile(recordFile, doc, "UTF-8");
			res = true;
		} catch (IOException e) {
			log.error("Mir entity could not be stored in XML file. " + e.getMessage());
		}
		
		return res;
	}

	
	/**
	 * This method converts input path in similar output path by
	 * replacing current analysis folder.
	 * @param filePath
	 * @param inputFolder
	 * @param outputFolder
	 * @return analysis path
	 */
	public String getNextFolder(String filePath, String inputFolder, String outputFolder) {
		if (filePath.contains("." + GZ_EXT))
			filePath = filePath.replace("." + GZ_EXT, "");
		return filePath.replace(inputFolder, outputFolder);		
	}
	
	
	/**
	 * This method extracts data from GZ package and stores it separately.
	 * @param filePath
	 * @param gzFolder
	 * @param csvFolder
	 * @return
	 */
	public String extractItemFromPackage(String filePath, String gzFolder, String csvFolder) {

		String csvFilePath = "";
		
	    byte[] buffer = new byte[1024];

	    try{

	        GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(filePath));
			csvFilePath = getNextFolder(filePath, gzFolder, csvFolder);
	        
	        File csvFile = new File(csvFilePath);
	        csvFile.getParentFile().mkdirs(); // create directories if not exists
	        csvFile.createNewFile(); // if file already exists will be ignored
	        FileOutputStream out = new FileOutputStream(csvFile, false); 

	        int len;
	        while ((len = gzis.read(buffer)) > 0) {
	            out.write(buffer, 0, len);
	        }

	        gzis.close();
	        out.close();

	        log.info("Extracted " + filePath);

	    } catch(IOException ex){
	        ex.printStackTrace();
	    }
	    
	    return csvFilePath;
	}
	
	
	/**
	 * This method extracts given field from a JSON file.
	 * @param inputPathCollection
	 * @param fieldName
	 * @return list of field values
	 */
	public List<String> readMetadataFieldFromJson(List<String> inputPathCollection, String fieldName) {

		List<String> res = new ArrayList<String>();
		
		for (String jsonFilePath : inputPathCollection) {
			String value = getJsonFieldValueFromFile(jsonFilePath, fieldName);
			res.add(value);
		}
		
		return res;
	}

	
	/**
	 * This method reads a given field from JSON file to String.
	 * @param fileName
	 * @param fieldName The name of a field in JSON object
	 * @return The value of the given field
	 * @throws Throwable
	 */
	public String getJsonFieldValueFromFile(String fileName, String fieldName) {
		
		String res = "";
	
		try {
			ObjectMapper mapper = new ObjectMapper();
		    JsonNode rootNode = mapper.readTree(new File(fileName));
		    JsonNode fieldArray = rootNode.get(fieldName);	
		    Iterator<JsonNode> itr = fieldArray.elements();
		    while (itr.hasNext()) {
		    	res = itr.next().textValue();
		    	break;
		    }		
		} catch (Exception ex) {
			log.info("File: " + fileName + " has no value for field: " + fieldName + ". " + ex.getMessage());
		}
		return res;
	}


	/**
	 * This method validates whether data in folder already exist 
	 * based on input path and folder in this path.
	 * @param collectionDirPath
	 * @param inputFolder
	 * @param outputFolder
	 * @return false if folder is not empty
	 */
	public boolean isFolderEmpty(String collectionDirPath, String inputFolder, String outputFolder) {
		
		boolean res = true;
		
		String nextFolder = getNextFolder(collectionDirPath, inputFolder, outputFolder);
        File nextFolderFile = new File(nextFolder);
		List<String> collectionFiles = listFilesForFolder(nextFolderFile);
		if (collectionFiles.size() > 0)
			res = false;
        
		return res;
	}
	
	
	/**
	 * This method extracts string from content between passed prefix and ending.
	 * @param content string
	 * @param prefix
	 * @param ending
	 * @return resulting string
	 */
	public static String extractStringBetweenPrefixAndEnding(String content, String prefix, String ending) {
		String res = "";
		if (StringUtils.isNotEmpty(content) && content.contains(prefix) && content.contains(ending)) {
			Pattern pattern = Pattern.compile(prefix + "(.*?)" + ending);
			Matcher matcher = pattern.matcher(content);
			if (matcher.find()) {
			    res = matcher.group(1);
			}		
		}
		return res;
	}
	
	
	public String getParsedXmlFieldValue(String filePath, String fieldName) {
		
		String res = "";
		
	    File file = new File(filePath);
		try {
			String content = FileUtils.readFileToString(file);
		    res = extractStringBetweenPrefixAndEnding(content, fieldName + "\">" , "</field");
		} catch (IOException e) {
	    	log.error("Error by parsing XML document. " + e.getMessage());
		}
	    
	    return res;
	}
}
