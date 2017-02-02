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
public class MirEntity  implements Comparable<MirEntity> {
	
	private String recordId;
	private String qdocId;
	private String sdocId;
	private Float sdocScore;
	private String sdocTitle;
	private String sdocLicense;
	private String sdocLicenseGroup;

	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getQdocId() {
		return qdocId;
	}
	public void setQdocId(String qdocId) {
		this.qdocId = qdocId;
	}
	public String getSdocId() {
		return sdocId;
	}
	public void setSdocId(String sdocId) {
		this.sdocId = sdocId;
	}
	public float getSdocScore() {
		return sdocScore;
	}
	public void setSdocScore(float sdocScore) {
		this.sdocScore = sdocScore;
	}
	public String getSdocTitle() {
		return sdocTitle;
	}
	public void setSdocTitle(String sdocTitle) {
		this.sdocTitle = sdocTitle;
	}
	public String getSdocLicense() {
		return sdocLicense;
	}
	public void setSdocLicense(String sdocLicense) {
		this.sdocLicense = sdocLicense;
	}
	public String getSdocLicenseGroup() {
		return sdocLicenseGroup;
	}
	public void setSdocLicenseGroup(String sdocLicenseGroup) {
		this.sdocLicenseGroup = sdocLicenseGroup;
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
		MirEntity other = (MirEntity) obj;
		if (Float.floatToIntBits(sdocScore) != Float.floatToIntBits(other.sdocScore))
			return false;
		return true;
	}
	
	
    @Override
    public int compareTo(MirEntity another) {
        return this.sdocScore.compareTo(another.sdocScore);
    }
    
	
	@Override
	public String toString() {
		return "MirEntity [recordId=" + recordId + ", qdocId=" + qdocId + ", sdocId=" + sdocId + ", sdocScore="
				+ sdocScore + ", sdocTitle=" + sdocTitle + ", sdocLicense=" + sdocLicense + ", sdocLicenseGroup="
				+ sdocLicenseGroup + "]";
	}
	

}
