package kr.co.wincom.imcs.api.getNSPresent;

import java.io.Serializable;


@SuppressWarnings("serial")
public class GetNSPresentDtlVO implements Serializable {

    private String hdcontent	= "";
    private String isHd			= "";
    private String prInfo		= "";
    private String seriesNo		= "";
    private String presentYn	= "";
    private String presentRate	= "";
    private String presentPrice	= "";
    private String suggestedPrice	= "";
    
	public String getHdcontent() {
		return hdcontent;
	}
	public void setHdcontent(String hdcontent) {
		this.hdcontent = hdcontent;
	}
	public String getIsHd() {
		return isHd;
	}
	public void setIsHd(String isHd) {
		this.isHd = isHd;
	}
	public String getPrInfo() {
		return prInfo;
	}
	public void setPrInfo(String prInfo) {
		this.prInfo = prInfo;
	}
	public String getSeriesNo() {
		return seriesNo;
	}
	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
	}
	public String getPresentYn() {
		return presentYn;
	}
	public void setPresentYn(String presentYn) {
		this.presentYn = presentYn;
	}
	public String getPresentRate() {
		return presentRate;
	}
	public void setPresentRate(String presentRate) {
		this.presentRate = presentRate;
	}
	public String getPresentPrice() {
		return presentPrice;
	}
	public void setPresentPrice(String presentPrice) {
		this.presentPrice = presentPrice;
	}
	public String getSuggestedPrice() {
		return suggestedPrice;
	}
	public void setSuggestedPrice(String suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}
    
}
