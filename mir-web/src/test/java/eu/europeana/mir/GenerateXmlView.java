package eu.europeana.mir;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import eu.europeana.mir.model.BaseMirRecordImpl;


public class GenerateXmlView extends BaseMirTest {


	protected String TEST_COLLECTION = "2059206";
	final String EUROPEANA_ID_INDEXES_FILE = "indexes_for_distance_resultfiles.csv";

	String ANALYSIS_FOLDER = "analysis";
	String METADATA_FOLDER = "metadata";
	String MIR_OUTPUT = "mir-output.xml"; 
	
	int EUROPEANA_ID_COL_POS    = 0;

	protected Logger log = Logger.getLogger(getClass());	
	
	
//	@Test
	public void extractMirCsvFiles() throws IOException {
		
		final File inputCollectionFolder = new File(GENERATED_PATH + TEST_COLLECTION);
		List<String> collectionFiles = getMirUtils().listFilesForFolder(inputCollectionFolder);

		log.info(collectionFiles.get(0));
		
		assertTrue(collectionFiles.size() > 0);
		
		int cnt = 0;
		
		for (String gzFilePath : collectionFiles) {
			
			String csvFilePath = getMirUtils().extractItemFromPackage(
					gzFilePath, ANALYSIS_FOLDER + "\\"+ CSV_EXT + "\\");
			
			assertTrue(csvFilePath.contains(CSV_EXT));			
			cnt++;
		}

		log.info("Successfully extracted " + cnt + " MIR-API CSV files.");
	}

	
//	@Test
	public void readMirMetadata() throws IOException {
		
		final File metadataCollectionFolder = new File(
				GENERATED_PATH + TEST_COLLECTION + "/" + METADATA_FOLDER);
		List<String> collectionFiles = getMirUtils().listFilesForFolder(metadataCollectionFolder);

		log.info(collectionFiles.get(0));
		
		assertTrue(collectionFiles.size() > 0);
		
		List<String> titleList = getMirUtils().readMetadataFieldFromJson(
				collectionFiles, TITLE);
		
		List<String> licenseList = getMirUtils().readMetadataFieldFromJson(
				collectionFiles, LICENSE);
						
		log.info("Successfully loaded MIR-API metadata.");
		
		assertTrue(collectionFiles.size() == titleList.size());
		assertTrue(collectionFiles.size() == licenseList.size());
		
	}
	
	
//	@Test
	public void generateMirXmlView() throws IOException {
		
		final File inputCollectionFolder = new File(
				GENERATED_PATH + TEST_COLLECTION + "/" + ANALYSIS_FOLDER + "/" + CSV_EXT);
		List<String> collectionFiles = getMirUtils().listFilesForFolder(inputCollectionFolder);

		log.info(collectionFiles.get(0));
		
		assertTrue(collectionFiles.size() > 0);
		
		int cnt = 0;
		
		for (String csvFilePath : collectionFiles) {
			
			List<BaseMirRecordImpl> mirEntityList = getMirUtils().readMirImplWithScoresFromCsv(
					csvFilePath, BASE_FILE_PATH + EUROPEANA_ID_INDEXES_FILE
					, ANALYSIS_FOLDER + "\\" + CSV_EXT, METADATA_FOLDER);
			
			boolean storeRes = getMirUtils().storeMirImplInXml(
					mirEntityList, csvFilePath.replace(CSV_EXT, XML_EXT));
			
			assertTrue(storeRes == true);
			
			cnt++;
		}

		log.info("Successfully generated " + cnt + " MIR-API items.");
	}

	
	@Test
	public void fullMirXmlGeneration() throws IOException {
		
		final File rootFolder = new File(GENERATED_PATH);
		List<String> collectionDirs = getMirUtils().listDirsForFolder(rootFolder);

		for (String collectionDirPath : collectionDirs) {
			
			final File inputCollectionFolder = new File(collectionDirPath);
			List<String> collectionFiles = getMirUtils().listFilesForFolder(inputCollectionFolder);
	
			log.info(collectionFiles.get(0));
			
			assertTrue(collectionFiles.size() > 0);
			
			int cnt = 0;
			
			for (String gzFilePath : collectionFiles) {
				
				String csvFilePath = getMirUtils().extractItemFromPackage(
						gzFilePath, ANALYSIS_FOLDER + "\\"+ CSV_EXT + "\\");
				
				assertTrue(csvFilePath.contains(CSV_EXT));			
				cnt++;
			}
	
			log.info("Successfully extracted " + cnt + " MIR-API CSV files for collection: " + collectionDirPath);
	
			
			
			final File analysisCollectionFolder = new File(
					collectionDirPath + "/" + ANALYSIS_FOLDER + "/" + CSV_EXT);
			List<String> analysisCollectionFiles = getMirUtils().listFilesForFolder(analysisCollectionFolder);
	
			log.info(analysisCollectionFiles.get(0));
			
			assertTrue(analysisCollectionFiles.size() > 0);
			
			int cntAnalysis = 0;
			
			for (String csvFilePath : analysisCollectionFiles) {
				
				List<BaseMirRecordImpl> mirEntityList = getMirUtils().readMirImplWithScoresFromCsv(
						csvFilePath, BASE_FILE_PATH + EUROPEANA_ID_INDEXES_FILE
						, ANALYSIS_FOLDER + "\\" + CSV_EXT, METADATA_FOLDER);
				
				boolean storeRes = getMirUtils().storeMirImplInXml(
						mirEntityList, csvFilePath.replace(CSV_EXT, XML_EXT));
				
				assertTrue(storeRes == true);
				
				cntAnalysis++;
			}
	
			log.info("Successfully generated " + cntAnalysis + " MIR-API items for collection: " + collectionDirPath);
		}
	}
	
	
}
