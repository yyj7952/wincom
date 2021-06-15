package kr.co.wincom.imcs.api.getNSMnuList;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;


@SuppressWarnings("serial")
public class GetNSMnuDtlVO implements Serializable {
	/********************************************************************
	 * GetNSMnuList API 전문 칼럼(순서 일치)
	********************************************************************/
	private String resultType           = "";
	private String categoryId           = "";
	private String categoryName         = "";
	private String categoryType         = "";
	private String runTime              = "";
	private String stillFileName        = "";
	private String fpAlbumYn            = "";
	private String fpAlbumId            = "";
	private String synopsis             = "";
	private String castisM3u8File       = "";
	private String onnetM3u8File        = "";
    
    @Override
    public String toString() {
		StringBuffer sb = new StringBuffer();
		// 몇몇 row의 adult_yn이 " "로 나타나는 경우가 있는데, 모르겠음
		sb.append(StringUtil.nullToSpace(this.getResultType())).append(ImcsConstants.COLSEP);	
		sb.append(StringUtil.nullToSpace(this.getCategoryId())).append(ImcsConstants.COLSEP);	
		sb.append(StringUtil.nullToSpace(this.getCategoryName())).append(ImcsConstants.COLSEP);
		//sb.append(StringUtil.nullToSpace(this.getCategoryType())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getRunTime())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getStillFileName())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getFpAlbumYn())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getFpAlbumId())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getSynopsis())).append(ImcsConstants.COLSEP);
		//sb.append(StringUtil.nullToSpace(this.getCastisM3u8File())).append(ImcsConstants.COLSEP);
		//sb.append(StringUtil.nullToSpace(this.getOnnetM3u8File())).append(ImcsConstants.COLSEP);
		sb.append((this.getCastisM3u8File())).append(ImcsConstants.COLSEP);
		sb.append((this.getOnnetM3u8File())).append(ImcsConstants.COLSEP);
		
		return sb.toString();
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

	public String getRunTime() {
		return runTime;
	}

	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}

	public String getStillFileName() {
		return stillFileName;
	}

	public void setStillFileName(String stillFileName) {
		this.stillFileName = stillFileName;
	}

	public String getFpAlbumYn() {
		return fpAlbumYn;
	}

	public void setFpAlbumYn(String fpAlbumYn) {
		this.fpAlbumYn = fpAlbumYn;
	}

	public String getFpAlbumId() {
		return fpAlbumId;
	}

	public void setFpAlbumId(String fpAlbumId) {
		this.fpAlbumId = fpAlbumId;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getCastisM3u8File() {
		return castisM3u8File;
	}

	public void setCastisM3u8File(String castisM3u8File) {
		this.castisM3u8File = castisM3u8File;
	}

	public String getOnnetM3u8File() {
		return onnetM3u8File;
	}

	public void setOnnetM3u8File(String onnetM3u8File) {
		this.onnetM3u8File = onnetM3u8File;
	}
	
}
