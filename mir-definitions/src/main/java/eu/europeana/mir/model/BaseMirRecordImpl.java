/**
 * This is a container for MIR-API objects.
 * 
 * An example in XML format:
 * 
 *	<doc>
 *		<field name="record_id">/2059206/data_sounds_http___epth_sfm_gr_card_aspx_mid_1_/2059206/data_sounds_http___epth_sfm_gr_card_aspx_mid_14_</field>
 *		<field name="qdoc_id">/2059206/data_sounds_http___epth_sfm_gr_card_aspx_mid_1_</field>
 *		<field name="sdoc_id">/2059206/data_sounds_http___epth_sfm_gr_card_aspx_mid_14_</field>
 *		<field name="sdoc_score">1.231</field>
 *      <field name="sdoc_title">Test Document 1</field>
 *      <field name="sdoc_license">http://creativecommons.org/licenses/by-nc/4.0/</field>
 *      <field name="sdoc_licence_group">Open Reuse</field>*
 *	</doc>
 *    
 * @author GrafR
 *
 */
package eu.europeana.mir.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRawValue;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({ "recordId", "qdocId" })
public class BaseMirRecordImpl  implements Comparable<BaseMirRecordImpl>, MirRecord {
	
	private String recordId;
	private String qdocId;
	private String sdocId;
	private Float sdocScore;
	private String sdocTitle;
	private String sdocLicense;
	private String sdocLicenseGroup;
	private String metadata;

	/* (non-Javadoc)
	 * @see eu.europeana.mir.Mir#getRecordId()
	 */
	@Override
	public String getRecordId() {
		return recordId;
	}
	/* (non-Javadoc)
	 * @see eu.europeana.mir.Mir#setRecordId(java.lang.String)
	 */
	@Override
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	/* (non-Javadoc)
	 * @see eu.europeana.mir.Mir#getQdocId()
	 */
	@Override
	public String getQdocId() {
		return qdocId;
	}
	/* (non-Javadoc)
	 * @see eu.europeana.mir.Mir#setQdocId(java.lang.String)
	 */
	@Override
	public void setQdocId(String qdocId) {
		this.qdocId = qdocId;
	}
	/* (non-Javadoc)
	 * @see eu.europeana.mir.Mir#getSdocId()
	 */
	@Override
	public String getSdocId() {
		return sdocId;
	}
	/* (non-Javadoc)
	 * @see eu.europeana.mir.Mir#setSdocId(java.lang.String)
	 */
	@Override
	public void setSdocId(String sdocId) {
		this.sdocId = sdocId;
	}
	/* (non-Javadoc)
	 * @see eu.europeana.mir.Mir#getSdocScore()
	 */
	@Override
	public float getSdocScore() {
		return sdocScore;
	}
	/* (non-Javadoc)
	 * @see eu.europeana.mir.Mir#setSdocScore(float)
	 */
	@Override
	public void setSdocScore(float sdocScore) {
		this.sdocScore = sdocScore;
	}
	/* (non-Javadoc)
	 * @see eu.europeana.mir.Mir#getSdocTitle()
	 */
	@Override
	public String getSdocTitle() {
		return sdocTitle;
	}
	/* (non-Javadoc)
	 * @see eu.europeana.mir.Mir#setSdocTitle(java.lang.String)
	 */
	@Override
	public void setSdocTitle(String sdocTitle) {
		this.sdocTitle = sdocTitle;
	}
	/* (non-Javadoc)
	 * @see eu.europeana.mir.Mir#getSdocLicense()
	 */
	@Override
	public String getSdocLicense() {
		return sdocLicense;
	}
	/* (non-Javadoc)
	 * @see eu.europeana.mir.Mir#setSdocLicense(java.lang.String)
	 */
	@Override
	public void setSdocLicense(String sdocLicense) {
		this.sdocLicense = sdocLicense;
	}
	/* (non-Javadoc)
	 * @see eu.europeana.mir.Mir#getSdocLicenseGroup()
	 */
	@Override
	public String getSdocLicenseGroup() {
		return sdocLicenseGroup;
	}
	/* (non-Javadoc)
	 * @see eu.europeana.mir.Mir#setSdocLicenseGroup(java.lang.String)
	 */
	@Override
	public void setSdocLicenseGroup(String sdocLicenseGroup) {
		this.sdocLicenseGroup = sdocLicenseGroup;
	}
	
	 @JsonRawValue
	public String getMetadata() {
		return metadata;
	}
	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(sdocScore);
		return result;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseMirRecordImpl other = (BaseMirRecordImpl) obj;
		if (Float.floatToIntBits(sdocScore) != Float.floatToIntBits(other.sdocScore))
			return false;
		return true;
	}
	
	
    @Override
    public int compareTo(BaseMirRecordImpl another) {
        return this.sdocScore.compareTo(another.sdocScore);
    }
    
	
	@Override
	public String toString() {
		return "MirRecord [recordId=" + recordId + ", qdocId=" + qdocId + ", sdocId=" + sdocId + ", sdocScore="
				+ sdocScore + ", sdocTitle=" + sdocTitle + ", sdocLicense=" + sdocLicense + ", sdocLicenseGroup="
				+ sdocLicenseGroup + "]";
	}
	

}
