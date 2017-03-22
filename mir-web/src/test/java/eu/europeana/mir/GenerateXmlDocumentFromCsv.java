package eu.europeana.mir;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import eu.europeana.mir.model.BaseMirRecordImpl;
//import eu.europeana.mir.utils.MirConst;
//import eu.europeana.mir.vocabulary.WebMirFields;

public class GenerateXmlDocumentFromCsv extends BaseMirTest {

	String sdocId = "data_sounds_http___epth_sfm_gr_card_aspx_mid_100_";
	String testFieldName = "sdoc_title";
	String expectedEncodedXmlResult = "&#922;&#945;&#955;&#945;&#956;&#945;&#964;&#953;&#945;&#957;&#972;&#962;";
	
	/**
	 * MirRecord [recordId=/2059206/data_sounds_http
	 * ___epth_sfm_gr_card_aspx_mid_100_/2059206/data_sounds_http___epth_sfm_gr_card_aspx_mid_100_, 
	 * qdocId=/2059206/data_sounds_http___epth_sfm_gr_card_aspx_mid_100_, 
	 * sdocId=/2059206/data_sounds_http___epth_sfm_gr_card_aspx_mid_100_, 
	 * sdocScore=0.0, 
	 * sdocTitle=Καλαματιανός, 
	 * sdocLicense=http://creativecommons.org/licenses/by-nc/4.0/, 
	 * sdocLicenseGroup=null]
	 * @throws IOException
	 */
	@Test
	public void fullMirXmlGeneration() throws IOException {
		
		List<BaseMirRecordImpl> mirEntityList = new ArrayList<BaseMirRecordImpl>();
		
		BaseMirRecordImpl mirImpl = new BaseMirRecordImpl();
		
		String metadataFilePath = REMOTE_METADATA_FOLDER + "/" + TEST_COLLECTION + "/" + sdocId + "." + JSON_EXT;
		String csvFilePath = REMOTE_GENERATED_PATH + DISTANCES_CSV_FOLDER + "/" + TEST_COLLECTION + "/" + sdocId + "." + CSV_EXT;
		String xmlFilePath = REMOTE_GENERATED_PATH + MIR_XML_TEST_FOLDER + "/" + TEST_COLLECTION + "/" + sdocId + "." + XML_EXT;
		
		String sdocTitle = getMirUtils().getJsonFieldValueFromFile(metadataFilePath, TITLE);
		String sdocLicense = getMirUtils().getJsonFieldValueFromFile(metadataFilePath, LICENSE);
		
		int csvFolderPos = csvFilePath.indexOf(DISTANCES_CSV_FOLDER);
		String qdocId = csvFilePath.substring(csvFolderPos + DISTANCES_CSV_FOLDER.length())
				.replace(".csv", "").replace(BACK_SLASH, PATH_ID_DELIMETER);

		sdocId = "/" + TEST_COLLECTION + "/" + sdocId;
		float score = 0;
		mirImpl.setQdocId(qdocId);
		mirImpl.setSdocId(sdocId);
		String recordId = qdocId + sdocId;
		mirImpl.setRecordId(recordId);
		mirImpl.setSdocScore(score);
		mirImpl.setSdocTitle(sdocTitle);
		mirImpl.setSdocLicense(sdocLicense);
		mirEntityList.add(mirImpl);
		
		boolean storeRes = getMirUtils().storeMirImplInXml(
				mirEntityList, xmlFilePath);
		
		assertTrue(storeRes == true);
		
		String resTitleValue = getMirUtils().getParsedXmlFieldValue(
				xmlFilePath, testFieldName);
		assertTrue(expectedEncodedXmlResult.equals(resTitleValue));
		
	}
}
