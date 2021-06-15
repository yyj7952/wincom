package kr.co.wincom.imcs.common.vo;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;


public class ComCpnVO {

	private String cpnInfo			= "";
	private String cpnInsInfo		= "";
	private String stmInfo			= "";
	private String stmInsInfo		= "";
	private String useCpnInfo		= "";
	private String useCpnInsInfo	= "";
	private String gblCpnInfo		= "";
	private String gblCpnInsInfo	= "";
	private String systemGb         = "";
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.nullToSpace(this.stmInfo)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.cpnInfo)).append(ImcsConstants.COLSEP);			
		sb.append(StringUtil.nullToSpace(this.useCpnInfo)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.gblCpnInfo)).append(ImcsConstants.COLSEP);
		
		
		return sb.toString();
	}
	
	public String getCpnInfo() {
		return cpnInfo;
	}
	public void setCpnInfo(String cpnInfo) {
		this.cpnInfo = cpnInfo;
	}
	public String getCpnInsInfo() {
		return cpnInsInfo;
	}
	public void setCpnInsInfo(String cpnInsInfo) {
		this.cpnInsInfo = cpnInsInfo;
	}
	public String getStmInfo() {
		return stmInfo;
	}
	public void setStmInfo(String stmInfo) {
		this.stmInfo = stmInfo;
	}
	public String getStmInsInfo() {
		return stmInsInfo;
	}
	public void setStmInsInfo(String stmInsInfo) {
		this.stmInsInfo = stmInsInfo;
	}
	public String getUseCpnInfo() {
		return useCpnInfo;
	}
	public void setUseCpnInfo(String useCpnInfo) {
		this.useCpnInfo = useCpnInfo;
	}
	public String getUseCpnInsInfo() {
		return useCpnInsInfo;
	}
	public void setUseCpnInsInfo(String useCpnInsInfo) {
		this.useCpnInsInfo = useCpnInsInfo;
	}
	public String getGblCpnInfo() {
		return gblCpnInfo;
	}
	public void setGblCpnInfo(String gblCpnInfo) {
		this.gblCpnInfo = gblCpnInfo;
	}
	public String getGblCpnInsInfo() {
		return gblCpnInsInfo;
	}
	public void setGblCpnInsInfo(String gblCpnInsInfo) {
		this.gblCpnInsInfo = gblCpnInsInfo;
	}

	public String getSystemGb() {
		return systemGb;
	}

	public void setSystemGb(String systemGb) {
		this.systemGb = systemGb;
	}
	
}

