package eu.europeana.mir.model;

public interface MirRecord {

	String getRecordId();

	void setRecordId(String recordId);

	String getQdocId();

	void setQdocId(String qdocId);

	String getSdocId();

	void setSdocId(String sdocId);

	float getSdocScore();

	void setSdocScore(float sdocScore);

	String getSdocTitle();

	void setSdocTitle(String sdocTitle);

	String getSdocLicense();

	void setSdocLicense(String sdocLicense);

	String getSdocLicenseGroup();

	void setSdocLicenseGroup(String sdocLicenseGroup);

}