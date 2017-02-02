import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * This is a helper class for MIR-API.
 * 
 * @author GrafR
 *
 */
public class MirUtils extends MirConst {


	protected Logger log = Logger.getLogger(getClass());	

	
	
	/**
	 * Read collection.
	 */
	public List<String> listFilesForFolder(final File folder) {
		
		List<String> collectionFiles = new ArrayList<String>();
		
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	        	collectionFiles.add(fileEntry.getPath());
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
	public List<MirEntity> readMirEntityWithScoresFromCsv(
			String csvFilePath, String indexFilePath, String csvFolder, String metadataFolder) {
		
		final int NUMBERS_AFTER_COMMA = 4;
		
		final int COLLECTION_ID_POS = 4;
		final int QDOC_ID_POS = 1;
		
		List<MirEntity> mirEntityList = new ArrayList<MirEntity>();

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
			
			MirEntity mirEntity = new MirEntity();
			
			String metadataFilePath = csvFilePath.
					replace("." + CSV_EXT, "." + JSON_EXT).
					replace(csvFolder, metadataFolder);
			
			String sdocTitle = getJsonFieldValueFromFile(metadataFilePath, TITLE);
			String sdocLicense = getJsonFieldValueFromFile(metadataFilePath, LICENSE);
			
			String[] items = csvFilePath.split(PATH_PARSING_DELIMITER);
			String collectionId = items[items.length - COLLECTION_ID_POS];
			String docId = items[items.length - QDOC_ID_POS].replace(".csv", "");
			String qdocId = PATH_ID_DELIMETER + collectionId + PATH_ID_DELIMETER + docId;
			mirEntity.setQdocId(qdocId);
			mirEntity.setSdocId(sdocId);
			String recordId = qdocId + sdocId;
			mirEntity.setRecordId(recordId);
			mirEntity.setSdocScore(score);
			mirEntity.setSdocTitle(sdocTitle);
			mirEntity.setSdocLicense(sdocLicense);
			mirEntityList.add(mirEntity);
			log.info("added " + cnt + " Mir entity: " + mirEntity);
			cnt++;
			if (cnt > MAX_SCORES_NUMBER) break;
		}
		
	    return mirEntityList;
	}
	
	
	/**
	 * This method stores MIR entities according to the template in MirEntity object.
	 * @param mirEntity
	 * @param xmlPath
	 * @return
	 */
	public boolean storeMirEntityInXml(List<MirEntity> mirEntityList, String xmlPath) {
	
		boolean res = false;
		
		File recordFile = FileUtils.getFile(xmlPath);

		StringBuilder row = new StringBuilder();
		row.append("<add>").append(LINE_BREAK);
		for (MirEntity mirEntity: mirEntityList) {
			row.append(TAB).append("<doc>").append(LINE_BREAK);
			row.append(TAB).append(TAB).append("<field name=\"record_id\">")
				.append(mirEntity.getRecordId()).append("</field>").append(LINE_BREAK);
			row.append(TAB).append(TAB).append("<field name=\"qdoc_id\">")
				.append(mirEntity.getQdocId()).append("</field>").append(LINE_BREAK);
			row.append(TAB).append(TAB).append("<field name=\"sdoc_id\">")
				.append(mirEntity.getSdocId()).append("</field>").append(LINE_BREAK);
			row.append(TAB).append(TAB).append("<field name=\"sdoc_score\">")
				.append(Float.toString(mirEntity.getSdocScore())).append("</field>").append(LINE_BREAK);
			row.append(TAB).append(TAB).append("<field name=\"sdoc_title\">")
				.append(mirEntity.getSdocTitle()).append("</field>").append(LINE_BREAK);
			row.append(TAB).append(TAB).append("<field name=\"sdoc_license\">")
				.append(mirEntity.getSdocLicense()).append("</field>").append(LINE_BREAK);
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
	 * This method extracts data from GZ package and stores it separately.
	 * @param filePath
	 * @param csvPath
	 * @return
	 */
	public String extractItemFromPackage(String filePath, String csvPath) {

		String csvFilePath = "";
		
	    byte[] buffer = new byte[1024];

	    try{

	        GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(filePath));
	        csvFilePath = filePath.replace(".gz", "");
			String[] pathElems = csvFilePath.split(PATH_PARSING_DELIMITER);
			String recordName = pathElems[pathElems.length - 1];
			csvFilePath = csvFilePath.replace(recordName, csvPath + recordName);

	        
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
		
        JsonObject jsonObject = new JsonObject();
        
        try {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader(fileName));
            jsonObject = jsonElement.getAsJsonObject();
            res = jsonObject.get(fieldName).getAsString();
		} catch (Exception ex) {
			log.info("File: " + fileName + " has no value for field: " + fieldName + ". " + ex.getMessage());
		}
		return res;
	}

	
}
