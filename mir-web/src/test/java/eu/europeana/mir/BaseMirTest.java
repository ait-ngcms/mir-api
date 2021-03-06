/*
* contributor license agreements.  See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/


package eu.europeana.mir;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import eu.europeana.mir.config.MirConfigurationImpl;
import eu.europeana.mir.utils.MirConst;
import eu.europeana.mir.utils.MirUtils;
import eu.europeana.mir.web.service.MirService;
import eu.europeana.mir.web.service.impl.MirServiceImpl;


/**
 * This class implements base methods for MIR-API testing.
 *
 * @author GrafR
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/mir-web-context.xml", "/mir-common-context.xml" })
public abstract class BaseMirTest extends MirConst {

	protected String BASE_FILE_PATH        = "./src/test/resources/";
	protected String GENERATED_PATH        = BASE_FILE_PATH + "generated/solr/";
	protected String METADATA_FOLDER       = GENERATED_PATH + "metadata";
	protected String DISTANCES_GZ_FOLDER   = "distances-gz";
	protected String DISTANCES_CSV_FOLDER  = "distances-csv";
	protected String MIR_XML_FOLDER        = "mir-xml";
	protected String MIR_XML_TEST_FOLDER   = "mir-xml-test";

	protected String REMOTE_BASE_FILE_PATH  = "E:\\";
	protected String REMOTE_GENERATED_PATH  = REMOTE_BASE_FILE_PATH + "generated/solr/";
	protected String REMOTE_METADATA_FOLDER = REMOTE_GENERATED_PATH + "metadata";
	
	protected String TEST_COLLECTION        = "2059206";
		
	final String EUROPEANA_ID_INDEXES_FILE = "indexes_for_distance_resultfiles.csv";
	
	protected Logger log = Logger.getLogger(getClass());	
		
	private MirUtils mirUtils = null;

//	@Resource 
//	MirService webMirService;
	
	
	@Before
    public void setUp() throws Exception {
		setMirUtils(new MirUtils());
		
/*		MirConfigurationImpl config = (MirConfigurationImpl) ((MirServiceImpl) webMirService).getConfiguration();
//		MirConfigurationImpl config = new MirConfigurationImpl();
		String dataUrl = config.getMirProperties().getProperty(MirConfigurationImpl.DATA_URL);
		
//		REMOTE_BASE_FILE_PATH = (new MirConfigurationImpl()).getDataUrl(); //MirConfigurationImpl.DATA_URL;
		REMOTE_BASE_FILE_PATH = dataUrl; //MirConfigurationImpl.DATA_URL;
		REMOTE_GENERATED_PATH  = REMOTE_BASE_FILE_PATH + "generated/solr/";
		REMOTE_METADATA_FOLDER = REMOTE_GENERATED_PATH + "metadata";
		*/
    }

    @After
    public void tearDown() throws Exception {
    }

        	    
	public MirUtils getMirUtils() {
		return mirUtils;
	}

	public void setMirUtils(MirUtils mirUtils) {
		this.mirUtils = mirUtils;
	}
	

}
