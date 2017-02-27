package eu.europeana.mir;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import eu.europeana.mir.model.BaseMirRecordImpl;

public class GenerateXmlDocumentFromCsv extends BaseMirTest {

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
		mirEntityList.add(mirImpl);
		
		boolean storeRes = getMirUtils().storeMirImplInXml(
				mirEntityList, xmlFilePath);
		
		assertTrue(storeRes == true);
		
	}
}
