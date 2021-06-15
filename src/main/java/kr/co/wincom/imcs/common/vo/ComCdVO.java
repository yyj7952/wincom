package kr.co.wincom.imcs.common.vo;

import java.io.Serializable;


@SuppressWarnings("serial")
public class ComCdVO implements Serializable {

    private int rownum;
    private String comCd = "";
    private String comName = "";
    private String comNodeType = "";
    
	public int getRownum() {
		return rownum;
	}
	public void setRownum(int rownum) {
		this.rownum = rownum;
	}
	public String getComCd() {
		return comCd;
	}
	public void setComCd(String comCd) {
		this.comCd = comCd;
	}
	public String getComName() {
		return comName;
	}
	public void setComName(String comName) {
		this.comName = comName;
	}
	public String getComNodeType() {
		return comNodeType;
	}
	public void setComNodeType(String comNodeType) {
		this.comNodeType = comNodeType;
	}
	
}
