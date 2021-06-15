package kr.co.wincom.imcs.api.getNSLinkTime;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;


@SuppressWarnings("serial")
public class GetNSLinkTimeResponseVO implements Serializable {
	/********************************************************************
	 * GetNSProdinfo API 전문 칼럼(순서 일치)
	********************************************************************/
	
	//Body
	private String linkTime = "";
    
    
    @Override
    public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.nullToSpace(this.linkTime)).append(ImcsConstants.COLSEP);
		
		return sb.toString();
    }
    
	public String getLinkTime() {
		return linkTime;
	}


	public void setLinkTime(String linkTime) {
		this.linkTime = linkTime;
	}


	
	
}
