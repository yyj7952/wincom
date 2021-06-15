package kr.co.wincom.imcs.api.getNSGuideVod;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;

public class GetNSGuideVodResponseVO implements Serializable {
	private static final long serialVersionUID = 170116096565178079L;
	//= 무중단 일경우 : SUBSCRIPTION_YN == ""
	
	/********************************************************************
	 * getNSGuideVod API 전문 칼럼(순서 일치)
	********************************************************************/
	private String idx			= "";	// Guide Vod 순위
	private String title		= "";	// 타이틀
	private String albumId		= "";	// 앨범ID
	private String isFlag		= "";	// 영상구분 (Y: 4M) - highlow
	private String vodServer1	= ""; 	// VOD서버1
	private String vodFileName	= "";	// VOD컨텐츠파일명1
	private String vodFileSize	= ""; 	// VOD파일사이즈1
	private String vodServer2	= "";	// VOD서버2
	private String vodServer3	= "";	// VOD서버3
	private String seriesYn		= "";	// 시리즈여부
	private String seriesNo		= "";	// 시리즈회차번호
	private String catId 		= "";	// 카테고리ID

	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String testSbc		= "";	// 검수STB 여부
	private String vodUseYn		= "";
	private String conType		= "";	// PRODUCT_TYPE
	private String conId		= "";	// 영화 ID
	private String albumName	= "";	// 영상의 제목
	//private String seriesNo		= "";	// 회차
	private String closeYn		= "";	// 종영작 여부(카테고리 테이블 속성) 
	private String conGb		= "";	// 컨텐츠 구분
	private String expiredDate	= "";	// 만료일자
	private String prodId		= "";	// PRODUCT_ID
	private String catName		= "";
	
	
	private String hdContent	= "";
	
	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIsFlag() {
		return isFlag;
	}

	public void setIsFlag(String isFlag) {
		this.isFlag = isFlag;
	}

	public String getVodServer1() {
		return vodServer1;
	}

	public void setVodServer1(String vodServer1) {
		this.vodServer1 = vodServer1;
	}

	public String getVodFileName() {
		return vodFileName;
	}

	public void setVodFileName(String vodFileName) {
		this.vodFileName = vodFileName;
	}

	public String getVodFileSize() {
		return vodFileSize;
	}

	public void setVodFileSize(String vodFileSize) {
		this.vodFileSize = vodFileSize;
	}

	public String getVodServer2() {
		return vodServer2;
	}

	public void setVodServer2(String vodServer2) {
		this.vodServer2 = vodServer2;
	}

	public String getVodServer3() {
		return vodServer3;
	}

	public void setVodServer3(String vodServer3) {
		this.vodServer3 = vodServer3;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getTestSbc() {
		return testSbc;
	}

	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}

	public String getConType() {
		return conType;
	}

	public void setConType(String conType) {
		this.conType = conType;
	}

	public String getConId() {
		return conId;
	}

	public void setConId(String conId) {
		this.conId = conId;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getSeriesNo() {
		return seriesNo;
	}

	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
	}

	public String getSeriesYn() {
		return seriesYn;
	}

	public void setSeriesYn(String seriesYn) {
		this.seriesYn = seriesYn;
	}

	public String getCloseYn() {
		return closeYn;
	}

	public void setCloseYn(String closeYn) {
		this.closeYn = closeYn;
	}

	public String getConGb() {
		return conGb;
	}

	public void setConGb(String conGb) {
		this.conGb = conGb;
	}

	public String getVodUseYn() {
		return vodUseYn;
	}

	public void setVodUseYn(String vodUseYn) {
		this.vodUseYn = vodUseYn;
	}

	public String getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.replaceNull(this.getIdx(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getTitle(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getAlbumId(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getIsFlag(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getVodServer1(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getVodFileName(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getVodFileSize(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getVodServer2(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getVodFileName(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getVodFileSize(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getVodServer3(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getVodFileName(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getVodFileSize(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getSeriesYn(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getSeriesNo(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.getCatId(), "")).append(ImcsConstants.COLSEP);
		
		return sb.toString();
	}

	public String getHdContent() {
		return hdContent;
	}

	public void setHdContent(String hdContent) {
		this.hdContent = hdContent;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}
}
