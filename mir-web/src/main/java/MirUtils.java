import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

/**
 * This is a helper class for MIR-API.
 * 
 * @author GrafR
 *
 */
public class MirUtils {


	/**
	 * This is a limit for similarity score numbers. 
	 * We take lowest values starting from 0.0.
	 */
	final int MAX_SCORES_NUMBER = 200;

	final String lineBreak = "\n"; 
	final String tab = "\t"; 
	final String pathSeparator = "\\\\";
	final String PATH_ID_DELIMETER = "/";
	
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
	 * This method reads scores from extracted CSV file and creates
	 * Mir entities.
	 * 
	 * @param csvFilePath
	 * @param indexFilePath
	 * @return list of Mir entities
	 */
	public List<MirEntity> readMirEntityWithScoresFromCsv(String csvFilePath, String indexFilePath) {
		
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
			
			String[] items = csvFilePath.split(pathSeparator);
			String collectionId = items[items.length - COLLECTION_ID_POS];
			String docId = items[items.length - QDOC_ID_POS].replace(".csv", "");
			String qdocId = PATH_ID_DELIMETER + collectionId + PATH_ID_DELIMETER + docId;
			mirEntity.setQdocId(qdocId);
			mirEntity.setSdocId(sdocId);
			String recordId = qdocId + sdocId;
			mirEntity.setRecordId(recordId);
			mirEntity.setSdocScore(score);
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
		row.append("<add>").append(lineBreak);
		for (MirEntity mirEntity: mirEntityList) {
			row.append(tab).append("<doc>").append(lineBreak);
			row.append(tab).append(tab).append("<field name=\"record_id\">")
				.append(mirEntity.getRecordId()).append("</field>").append(lineBreak);
			row.append(tab).append(tab).append("<field name=\"qdoc_id\">")
				.append(mirEntity.getQdocId()).append("</field>").append(lineBreak);
			row.append(tab).append(tab).append("<field name=\"sdoc_id\">")
				.append(mirEntity.getSdocId()).append("</field>").append(lineBreak);
			row.append(tab).append(tab).append("<field name=\"sdoc_score\">")
				.append(Float.toString(mirEntity.getSdocScore())).append("</field>").append(lineBreak);
			row.append(tab).append("</doc>").append(lineBreak);
		}
		row.append("</add>").append(lineBreak);
		
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
			String[] pathElems = csvFilePath.split(pathSeparator);
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
	

}
