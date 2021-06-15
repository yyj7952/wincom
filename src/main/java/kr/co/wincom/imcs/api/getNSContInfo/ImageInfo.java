package kr.co.wincom.imcs.api.getNSContInfo;

import java.io.Serializable;

import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;

public class ImageInfo extends NoSqlLoggingVO implements Serializable
{
	private static final long serialVersionUID = 820027358203692775L;
	
    private String stillImage;
    private String mainImage;
    private String logoImage;
    private String posterP;
    private String posterT;
    
	public String getStillImage() {
		return stillImage;
	}
	public void setStillImage(String stillImage) {
		this.stillImage = stillImage;
	}
	public String getMainImage() {
		return mainImage;
	}
	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}
	public String getLogoImage() {
		return logoImage;
	}
	public void setLogoImage(String logoImage) {
		this.logoImage = logoImage;
	}
	public String getPosterP() {
		return posterP;
	}
	public void setPosterP(String posterP) {
		this.posterP = posterP;
	}
	public String getPosterT() {
		return posterT;
	}
	public void setPosterT(String posterT) {
		this.posterT = posterT;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
