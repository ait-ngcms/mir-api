package eu.europeana.mir.solr.model;

import org.apache.solr.client.solrj.beans.Field;

import eu.europeana.mir.model.BaseMirRecordImpl;
import eu.europeana.mir.model.MirRecord;
import eu.europeana.mir.vocabulary.MirSolrFields;
import eu.europeana.mir.web.model.view.MirRecordView;

public class SolrMirRecordImpl extends BaseMirRecordImpl implements MirRecord, MirRecordView {


	@Field(MirSolrFields.RECORD_ID)
	public void setRecordId(String recordId) {
		super.setRecordId(recordId);
	}

	@Override
	@Field(MirSolrFields.QDOC_ID)
	public void setQdocId(String qdocId) {
		super.setQdocId(qdocId);
	}

	@Override
	@Field(MirSolrFields.SDOC_ID)
	public void setSdocId(String sdocId) {
		super.setSdocId(sdocId);
	}
	
	@Override
	@Field(MirSolrFields.SDOC_SCORE)
	public void setSdocScore(float sdocScore) {
		super.setSdocScore(sdocScore);
	}
	
	@Override
	@Field(MirSolrFields.SDOC_TITLE)
	public void setSdocTitle(String sdocTitle) {
		super.setSdocTitle(sdocTitle);
	}
	
	@Override
	@Field(MirSolrFields.SDOC_LICENSE)
	public void setSdocLicense(String sdocLicense) {
		super.setSdocLicense(sdocLicense);
	}
	
	@Override
	@Field(MirSolrFields.SDOC_LICENSE_GROUP)
	public void setSdocLicenseGroup(String sdocLicenseGroup) {
		super.setSdocLicenseGroup(sdocLicenseGroup);
	}
	
	public String getRecordId() {
		return super.getRecordId();
	}

	public String getQdocId() {
		return super.getQdocId();
	}

	public String getSdocId() {
		return super.getSdocId();
	}

	public float getSdocScore() {
		return super.getSdocScore();
	}

	public String getSdocTitle() {
		return super.getSdocTitle();
	}

	public String getSdocLicense() {
		return super.getSdocLicense();
	}

	public String getSdocLicenseGroup() {
		return super.getSdocLicenseGroup();
	}
	
}
