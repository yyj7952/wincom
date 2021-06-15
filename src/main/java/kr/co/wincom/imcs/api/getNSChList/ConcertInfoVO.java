package kr.co.wincom.imcs.api.getNSChList;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;

public class ConcertInfoVO extends NoSqlLoggingVO implements Serializable
{
	private static final long serialVersionUID = 820027358203692775L;
	
	private String resultType 		= "PPVCH";
    private String serviceId		= "";
    private String albumId			= "";
  	private String concertBgnTime 	= "";
  	private String concertEndTime 	= "";
  	private String ppvBgnTime		= "";
  	private String ppvEndTime		= "";
    
  	private String performDate		= "";
  	private String performTime		= "";
  	private String performEndTime	= "";
  	private String performEndDate	= "";
  	private String payFlag			= "";
  	
    @Override
    public String toString() {
		StringBuffer sb = new StringBuffer();
		// 몇몇 row의 adult_yn이 " "로 나타나는 경우가 있는데, 모르겠음
		sb.append(StringUtil.nullToSpace(this.getResultType())).append(ImcsConstants.COLSEP);	
		sb.append(StringUtil.nullToSpace(this.getServiceId())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getAlbumId())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getConcertBgnTime())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getConcertEndTime())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getPpvBgnTime())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getPpvEndTime())).append(ImcsConstants.COLSEP);

		return sb.toString();
	}
    
    public void dataParcing2(String[] arrRowResult)
    {
    	for(int j = 0; j < arrRowResult.length; j++)
		{
			switch(j)
			{
				case 0:
					this.setResultType(arrRowResult[j]);
					break;
				case 1:
					this.setServiceId(arrRowResult[j]);
					break;
				case 2:
					this.setAlbumId(arrRowResult[j]);
					break;
				case 3:
					this.setConcertBgnTime(arrRowResult[j]);
					break;
				case 4:
					this.setConcertEndTime(arrRowResult[j]);
					break;
				case 5:
					this.setPpvBgnTime(arrRowResult[j]);
					break;
				case 6:
					this.setPpvEndTime(arrRowResult[j]);
					break;
				default:
					break;
			}
		}
    }
    
	public String getResultType() {
		return resultType;
	}



	public void setResultType(String resultType) {
		this.resultType = resultType;
	}



	public String getServiceId() {
		return serviceId;
	}



	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}



	public String getAlbumId() {
		return albumId;
	}



	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}
	

	public String getConcertBgnTime() {
		return concertBgnTime;
	}



	public void setConcertBgnTime(String concertBgnTime) {
		this.concertBgnTime = concertBgnTime;
	}



	public String getConcertEndTime() {
		return concertEndTime;
	}



	public void setConcertEndTime(String concertEndTime) {
		this.concertEndTime = concertEndTime;
	}



	public String getPpvBgnTime() {
		return ppvBgnTime;
	}



	public void setPpvBgnTime(String ppvBgnTime) {
		this.ppvBgnTime = ppvBgnTime;
	}



	public String getPpvEndTime() {
		return ppvEndTime;
	}



	public void setPpvEndTime(String ppvEndTime) {
		this.ppvEndTime = ppvEndTime;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPerformDate() {
		return performDate;
	}

	public void setPerformDate(String performDate) {
		this.performDate = performDate;
	}

	public String getPerformTime() {
		return performTime;
	}

	public void setPerformTime(String performTime) {
		this.performTime = performTime;
	}

	public String getPerformEndTime() {
		return performEndTime;
	}

	public void setPerformEndTime(String performEndTime) {
		this.performEndTime = performEndTime;
	}

	public String getPayFlag() {
		return payFlag;
	}

	public void setPayFlag(String payFlag) {
		this.payFlag = payFlag;
	}

	public String getPerformEndDate() {
		return performEndDate;
	}

	public void setPerformEndDate(String performEndDate) {
		this.performEndDate = performEndDate;
	}
}
