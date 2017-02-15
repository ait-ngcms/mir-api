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

import org.junit.After;
import org.junit.Before;

import eu.europeana.mir.utils.MirConst;
import eu.europeana.mir.utils.MirUtils;

/**
 * This class implements base methods for MIR-API testing.
 *
 * @author GrafR
 *
 */
public abstract class BaseMirTest extends MirConst {

	protected String BASE_FILE_PATH  = "./src/test/resources/";
	protected String GENERATED_PATH  = BASE_FILE_PATH + "generated/solr/";

	
	private MirUtils mirUtils = null;

    
	@Before
    public void setUp() throws Exception {
		setMirUtils(new MirUtils());
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
