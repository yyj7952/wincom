package kr.co.wincom.imcs.common.vo;

public class ComSbcVO {
	private String statusFlag	= "";
	private String ynVodOpen	= "";
	private Long cpCnt;
	private String comName		= "";
	
	private String chnlDvCd		= "";
	private String custNo		= "";
	
	private String pvsCtnNo	= "";
    private String pvsAtrtChnlDvCd = "";
    private String blockFlag = "";
    
    //2021.02.24 - 검수 사용자 여부 조회
    private String testSbc = "";
	
	public String getBlockFlag() {
		return blockFlag;
	}
	public void setBlockFlag(String blockFlag) {
		this.blockFlag = blockFlag;
	}
	public String getStatusFlag() {
		return statusFlag;
	}
	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
	}
	public String getYnVodOpen() {
		return ynVodOpen;
	}
	public void setYnVodOpen(String ynVodOpen) {
		this.ynVodOpen = ynVodOpen;
	}
	public Long getCpCnt() {
		return cpCnt;
	}
	public String getComName() {
		return comName;
	}
	public void setComName(String comName) {
		this.comName = comName;
	}
	public void setCpCnt(Long cpCnt) {
		this.cpCnt = cpCnt;
	}
	public String getChnlDvCd() {
		return chnlDvCd;
	}
	public void setChnlDvCd(String chnlDvCd) {
		this.chnlDvCd = chnlDvCd;
	}
	public String getCustNo() {
		return custNo;
	}
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}
	public String getPvsCtnNo() {
		return pvsCtnNo;
	}
	public void setPvsCtnNo(String pvsCtnNo) {
		this.pvsCtnNo = pvsCtnNo;
	}
	public String getPvsAtrtChnlDvCd() {
		return pvsAtrtChnlDvCd;
	}
	public void setPvsAtrtChnlDvCd(String pvsAtrtChnlDvCd) {
		this.pvsAtrtChnlDvCd = pvsAtrtChnlDvCd;
	}
	public String getTestSbc() {
		return testSbc;
	}
	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}

}
