

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;


public class GenerateXmlView extends BaseMirTest {


	protected String TEST_COLLECTION = "2059206";

	String ANALYSIS_FOLDER = "analysis";
	String CSV_FOLDER = "csv";
	String XML_FOLDER = "xml";

	String MIR_OUTPUT = "mir-output.xml"; 
	
	int EUROPEANA_ID_COL_POS    = 0;

	protected Logger log = Logger.getLogger(getClass());	
	
	
//	@Test
	public void extractMirCsvFiles() throws IOException {
		
		final File inputCollectionFolder = new File(BASE_FILE_PATH + TEST_COLLECTION);
		List<String> collectionFiles = getMirUtils().listFilesForFolder(inputCollectionFolder);

		log.info(collectionFiles.get(0));
		
		assertTrue(collectionFiles.size() > 0);
		
		int cnt = 0;
		
		for (String gzFilePath : collectionFiles) {
			
			String csvFilePath = getMirUtils().extractItemFromPackage(
					gzFilePath, ANALYSIS_FOLDER + "\\"+ CSV_FOLDER + "\\");
			
			assertTrue(csvFilePath.contains(CSV_FOLDER));			
			cnt++;
		}

		log.info("Successfully extracted " + cnt + " MIR-API CSV files.");
	}

	
	@Test
	public void generateMirXmlView() throws IOException {
		
		final File inputCollectionFolder = new File(
				BASE_FILE_PATH + TEST_COLLECTION + "/" + ANALYSIS_FOLDER + "/" + CSV_FOLDER);
		List<String> collectionFiles = getMirUtils().listFilesForFolder(inputCollectionFolder);

		log.info(collectionFiles.get(0));
		
		assertTrue(collectionFiles.size() > 0);
		
		int cnt = 0;
		
		for (String csvFilePath : collectionFiles) {
			
			List<MirEntity> mirEntityList = getMirUtils().readMirEntityWithScoresFromCsv(csvFilePath);
			
			boolean storeRes = getMirUtils().storeMirEntityInXml(
					mirEntityList, csvFilePath.replace(CSV_FOLDER, XML_FOLDER));
			
			assertTrue(storeRes == true);
			
			cnt++;
		}

		log.info("Successfully generated " + cnt + " MIR-API items.");
	}
	
}
