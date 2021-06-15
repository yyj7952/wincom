package kr.co.wincom.imcs.common.vo;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;


@SuppressWarnings("serial")
public class OstInfoVO  implements Serializable {
    private String resultType		= "OST";
    private String albumId			= "";		// 앨범ID
    private String reservedSeq		= "";		// 순번 001 - 999
    private String ostType			= "";		// OST구분 (O:OST곡정보, A:OST앨범정보)
    private String ostId			= "";		// OST_ID (OST ID또는 앨범ID)
    private String ostSinger		= "";		// 가수명
    private String ostTitle			= "";		// 앨범명/곡명 (OST_TYPE-O:곡명, A:앨범명)
    private String resultSet		= "";		// OST조회 결과
    
    

	
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(StringUtil.nullToSpace(this.resultType)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.albumId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.reservedSeq)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.ostType)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.ostId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.ostSinger)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.ostTitle)).append(ImcsConstants.COLSEP);
		sb.append(ImcsConstants.ROWSEP);
		
		return sb.toString();
	}
    
    
	public String getResultType() {
		return resultType;
	}
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	public String getAlbumId() {
		return albumId;
	}
	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}
	public String getReservedSeq() {
		return reservedSeq;
	}
	public void setReservedSeq(String reservedSeq) {
		this.reservedSeq = reservedSeq;
	}
	public String getOstType() {
		return ostType;
	}
	public void setOstType(String ostType) {
		this.ostType = ostType;
	}
	public String getOstId() {
		return ostId;
	}
	public void setOstId(String ostId) {
		this.ostId = ostId;
	}
	public String getOstSinger() {
		return ostSinger;
	}
	public void setOstSinger(String ostSinger) {
		this.ostSinger = ostSinger;
	}
	public String getOstTitle() {
		return ostTitle;
	}
	public void setOstTitle(String ostTitle) {
		this.ostTitle = ostTitle;
	}
	public String getResultSet() {
		return resultSet;
	}
	public void setResultSet(String resultSet) {
		this.resultSet = resultSet;
	}
    
}
