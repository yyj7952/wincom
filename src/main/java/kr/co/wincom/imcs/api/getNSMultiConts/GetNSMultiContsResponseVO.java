package kr.co.wincom.imcs.api.getNSMultiConts;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;

public class GetNSMultiContsResponseVO implements Serializable
{
	private static final long serialVersionUID = 170116096565178079L;
	//= 무중단 일경우 : SUBSCRIPTION_YN == ""
	
	private String albumId			= "";
	private String runtime			= "";
	private String watchYn			= "";
	private String linkTime			= "";
	
	/********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
	private String resultCode		= "";
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.nullToSpace(this.albumId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.runtime)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.watchYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.linkTime)).append(ImcsConstants.COLSEP);
				
		return sb.toString();
	}



	public String getAlbumId() {
		return albumId;
	}
	
	
	
	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}



	public String getRuntime() {
		return runtime;
	}



	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}



	public String getWatchYn() {
		return watchYn;
	}



	public void setWatchYn(String watchYn) {
		this.watchYn = watchYn;
	}



	public String getLinkTime() {
		return linkTime;
	}



	public void setLinkTime(String linkTime) {
		this.linkTime = linkTime;
	}



	public String getResultCode() {
		return resultCode;
	}



	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	
	
}
