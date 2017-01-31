import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
	 * This method reads scores from extracted CSV file and creates
	 * Mir entities.
	 * 
	 * @param csvFilePath
	 * @return list of Mir entities
	 */
	public List<MirEntity> readMirEntityWithScoresFromCsv(String csvFilePath) {
		
		final int NUMBERS_AFTER_COMMA = 4;
		
		final int RECORD_ID_POS = 4;
		final int QDOC_ID_POS = 1;
		
		List<MirEntity> mirEntityList = new ArrayList<MirEntity>();

		List<Float> scoreList = new ArrayList<Float>();
		File csvFile = FileUtils.getFile(csvFilePath);
		LineIterator iterator;
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
				scoreList.add((Float) floatScore);
			}
		} catch (IOException e) {
			log.error("Error reading scores from CSV file. " + e.getMessage());
		}		
		
		Collections.sort(scoreList);
		
		int i = 0;
		for (Float elem: scoreList) {
			log.info("sorted elem i: " + elem);
			i++;
			if (i > 9) break;
		}

		int cnt = 0;
		for (Float elem: scoreList) {

			MirEntity mirEntity = new MirEntity();
			
			String[] items = csvFilePath.split(pathSeparator);
			String recordId = items[items.length - RECORD_ID_POS];
			mirEntity.setRecordId(recordId);
			String qdocId = items[items.length - QDOC_ID_POS].replace(".csv", "");
			mirEntity.setQdocId(qdocId);
			mirEntity.setSdocId(cnt);
			mirEntity.setSdocScore(elem);
			mirEntityList.add(mirEntity);
			log.info("added " + cnt + " Mir entity: " + mirEntity);
			cnt++;
			if (cnt > MAX_SCORES_NUMBER) break;
		}
		
	    return mirEntityList;
	}
	
	
	/**
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
				.append(Integer.toString(mirEntity.getSdocId())).append("</field>").append(lineBreak);
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
