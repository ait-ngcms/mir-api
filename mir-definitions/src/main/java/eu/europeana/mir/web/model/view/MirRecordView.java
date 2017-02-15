package eu.europeana.mir.web.model.view;

import eu.europeana.mir.vocabulary.MirTypes;

public interface MirRecordView {

	//functional fields
//	public void setMirId(String mirId);
	
	public void setQdocId(String qdocId);
	
	public String getQdocId();
	
	void setSdocId(String sdocId);

	public String getSdocId();
	
//	void setMatchedTerm(String matchedTerm);
//
//	String getMatchedTerm();
//
//	void setSearchedTerm(String searchedTerm);
//
//	String getSearchedTerm();

//	void setMirType(MirTypes mirType);
//
//	MirTypes getMirType();
	
}
