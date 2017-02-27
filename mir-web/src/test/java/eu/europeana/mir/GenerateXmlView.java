package eu.europeana.mir;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import eu.europeana.mir.model.BaseMirRecordImpl;


/**
 * This class supports and tests all operations required for 
 * creation of MIR documents in XML format.
 * 
 * These operations are:
 *     extracting CSV from GZ package
 *     reading CSV distance data
 *     reading JSON metadata
 *     sorting of results by similarity score
 *     creation of XML documents.
 *     
 * @author GrafR
 *
 */
public class GenerateXmlView extends BaseMirTest {

	
	@Test
	public void fullMirXmlGeneration() throws IOException {
		
		final File rootFolder = new File(REMOTE_GENERATED_PATH + DISTANCES_GZ_FOLDER);
		List<String> collectionDirs = getMirUtils().listDirsForFolder(rootFolder);

		for (String collectionDirPath : collectionDirs) {
			
			final File inputCollectionFolder = new File(collectionDirPath);
			List<String> collectionFiles = getMirUtils().listFilesForFolder(inputCollectionFolder);
	
			log.info(collectionFiles.get(0));
			
			assertTrue(collectionFiles.size() > 0);
			
			if (getMirUtils().isFolderEmpty(collectionDirPath, DISTANCES_GZ_FOLDER, DISTANCES_CSV_FOLDER)) {
			
				int cnt = 0;
				
				for (String gzFilePath : collectionFiles) {
					
					String csvFilePath = getMirUtils().extractItemFromPackage(
							gzFilePath, DISTANCES_GZ_FOLDER, DISTANCES_CSV_FOLDER);
					
					assertTrue(csvFilePath.contains(CSV_EXT));			
					cnt++;
				}
		
				log.info("Successfully extracted " + cnt + " MIR-API CSV files for collection: " + collectionDirPath);				
			}
			
			if (getMirUtils().isFolderEmpty(collectionDirPath, DISTANCES_GZ_FOLDER, MIR_XML_FOLDER)) {
			
				final File analysisCollectionFolder = new File(
						getMirUtils().getNextFolder(collectionDirPath, DISTANCES_GZ_FOLDER, DISTANCES_CSV_FOLDER));
				List<String> analysisCollectionFiles = getMirUtils().listFilesForFolder(analysisCollectionFolder);
		
				log.info(analysisCollectionFiles.get(0));
				
				assertTrue(analysisCollectionFiles.size() > 0);
				
				int cntAnalysis = 0;
				
				for (String csvFilePath : analysisCollectionFiles) {
					
					List<BaseMirRecordImpl> mirEntityList = getMirUtils().readMirImplWithScoresFromCsv(
							csvFilePath, REMOTE_GENERATED_PATH + EUROPEANA_ID_INDEXES_FILE
							, DISTANCES_CSV_FOLDER, REMOTE_METADATA_FOLDER);
					
					String xmlFilePath = getMirUtils().getNextFolder(
							csvFilePath, DISTANCES_CSV_FOLDER, MIR_XML_FOLDER);
					xmlFilePath = xmlFilePath.replace(CSV_EXT, XML_EXT);
					
					boolean storeRes = getMirUtils().storeMirImplInXml(
							mirEntityList, xmlFilePath);
					
					assertTrue(storeRes == true);
					
					cntAnalysis++;
				}
		
				log.info("Successfully generated " + cntAnalysis + " MIR-API items for collection: " + collectionDirPath);
			}
		}
	}
	
	
}
