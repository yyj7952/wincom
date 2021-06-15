package kr.co.wincom.imcs.common.vo;

import java.io.Serializable;

public class FmInfoVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String fmYN			= "";
	private String adiProdId	= "";
	private String downCnt		= "";
	

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getFmYN() {
		return fmYN;
	}
	public void setFmYN(String fmYN) {
		this.fmYN = fmYN;
	}
	public String getAdiProdId() {
		return adiProdId;
	}
	public void setAdiProdId(String adiProdId) {
		this.adiProdId = adiProdId;
	}
	public String getDownCnt() {
		return downCnt;
	}
	public void setDownCnt(String downCnt) {
		this.downCnt = downCnt;
	}

}
