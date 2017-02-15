package eu.europeana.mir.solr.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.europeana.mir.search.ResultSet;
import eu.europeana.mir.solr.exception.MirRetrievalException;
import eu.europeana.mir.web.model.view.MirRecordView;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/entity-solr-test.xml" })
public class SolrMirServiceTest {

    private final String TEST_MIR_QDOC_ID = "/2059201/data_sounds_9";

	@Resource
	SolrMirService solrMirService;

	private final Logger log = Logger.getLogger(getClass());
	
	@Test
	public void testSearchByQdocId() throws MirRetrievalException {

		ResultSet<? extends MirRecordView> mirList = solrMirService.search(TEST_MIR_QDOC_ID, null, null);
		assertNotNull(mirList);
		assertTrue(TEST_MIR_QDOC_ID.equals(mirList.getResults().get(0).getQdocId()));
	}
	
	public Logger getLog() {
		return log;
	}
	
}
