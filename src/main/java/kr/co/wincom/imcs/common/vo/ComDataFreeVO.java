package kr.co.wincom.imcs.common.vo;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;


@SuppressWarnings("serial")
public class ComDataFreeVO implements Serializable {

	private String price = "";
    private String datafreePrice = "";
    private String approvalId = "";
    private String approvalPrice = "";
    private String datafreeApprovalId = "";
    private String datafreeApprovalPrice = "";
    private String ppvDatafreeApprovalId = "";
    private String ppvDatafreeApprovalPrice = "";
    private String approvalGb = "";
    
   

	public String getDatafreePrice() {
		return datafreePrice;
	}



	public void setDatafreePrice(String datafreePrice) {
		this.datafreePrice = datafreePrice;
	}



	public String getApprovalId() {
		return approvalId;
	}



	public void setApprovalId(String approvalId) {
		this.approvalId = approvalId;
	}



	public String getApprovalPrice() {
		return approvalPrice;
	}



	public void setApprovalPrice(String approvalPrice) {
		this.approvalPrice = approvalPrice;
	}



	public String getDatafreeApprovalId() {
		return datafreeApprovalId;
	}



	public void setDatafreeApprovalId(String datafreeApprovalId) {
		this.datafreeApprovalId = datafreeApprovalId;
	}



	public String getDatafreeApprovalPrice() {
		return datafreeApprovalPrice;
	}



	public void setDatafreeApprovalPrice(String datafreeApprovalPrice) {
		this.datafreeApprovalPrice = datafreeApprovalPrice;
	}

	
	public String getPpvDatafreeApprovalId() {
		return ppvDatafreeApprovalId;
	}



	public void setPpvDatafreeApprovalId(String ppvDatafreeApprovalId) {
		this.ppvDatafreeApprovalId = ppvDatafreeApprovalId;
	}



	public String getPpvDatafreeApprovalPrice() {
		return ppvDatafreeApprovalPrice;
	}



	public void setPpvDatafreeApprovalPrice(String ppvDatafreeApprovalPrice) {
		this.ppvDatafreeApprovalPrice = ppvDatafreeApprovalPrice;
	}



	public String getPrice() {
		return price;
	}



	public void setPrice(String price) {
		this.price = price;
	}


	public String getApprovalGb() {
		return approvalGb;
	}



	public void setApprovalGb(String approvalGb) {
		this.approvalGb = approvalGb;
	}



	public String toDatafreeString(){
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.nullToSpace(this.datafreePrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.approvalId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.approvalPrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.datafreeApprovalId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.datafreeApprovalPrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.ppvDatafreeApprovalId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.ppvDatafreeApprovalPrice)).append(ImcsConstants.COLSEP);
			
		return sb.toString();
	}
	    
}
