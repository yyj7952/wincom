package kr.co.wincom.imcs.api.getNSMusicList;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class GetNSMusicListResponseVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSMusicList API 전문 칼럼(순서 일치)
	********************************************************************/
	private String resultType			= "CON"; //결과값 구분
	private String scheType				= ""; //공연 스케줄 구분 (0:공연전, 1:공연중, 2:공연후)
	private String albumId				= ""; //앨범 ID
	private String qsheetId				= ""; //큐시트 ID
	private String concertName			= ""; //공연(방송)명  yyyymmddhhmm (201805201330)
	private String concertDate			= ""; //공연(방송)일시 yyyymmddhhmm (201805201330)
	private String place				= ""; //공연장소
	private String omniviewYn			= ""; //옴니뷰 제공 여부
	private String imageUrl				= ""; //공연이미지 URL
	private String posterUrl			= "";
	private String imageFileName		= ""; //공연이미지 파일명
	private String bannerFileName		= "";
	private String categoryId			= ""; //카테고리 ID (해당 큐시트 앨범이 편성된 카테고리 ID)
	private String serviceId			= ""; //서비스 채널 ID (공연예정 또는 공연중인 콘서트의 경우에만 제공)
	
	// 2020.05.06 - 아이돌 라이브 2020-1Q
	private String mainProperty			= ""; //아이돌라이브 주속성그룹
	private String subProperty			= ""; //아이돌라이브 부속성그룹
	
	
	
	
    @Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtils.defaultString(this.getResultType(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getScheType(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getAlbumId(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getQsheetId(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getConcertName(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getConcertDate(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getPlace(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getOmniviewYn(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getPosterUrl(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getImageFileName(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getCategoryId(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getServiceId(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getImageUrl(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getBannerFileName(), "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getMainProperty())).append(ImcsConstants.COLSEP);
		sb.append(StringUtils.defaultString(this.getSubProperty().replaceAll("\\\\b", ImcsConstants.ARRSEP))).append(ImcsConstants.COLSEP);
		return sb.toString();
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getScheType() {
		return scheType;
	}

	public void setScheType(String scheType) {
		this.scheType = scheType;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getQsheetId() {
		return qsheetId;
	}

	public void setQsheetId(String qsheetId) {
		this.qsheetId = qsheetId;
	}

	public String getConcertName() {
		return concertName;
	}

	public void setConcertName(String concertName) {
		this.concertName = concertName;
	}

	public String getConcertDate() {
		return concertDate;
	}

	public void setConcertDate(String concertDate) {
		this.concertDate = concertDate;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getOmniviewYn() {
		return omniviewYn;
	}

	public void setOmniviewYn(String omniviewYn) {
		this.omniviewYn = omniviewYn;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getPosterUrl() {
		return posterUrl;
	}

	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}

	public String getBannerFileName() {
		return bannerFileName;
	}

	public void setBannerFileName(String bannerFileName) {
		this.bannerFileName = bannerFileName;
	}

	public String getMainProperty() {
		return mainProperty;
	}

	public void setMainProperty(String mainProperty) {
		this.mainProperty = mainProperty;
	}

	public String getSubProperty() {
		return subProperty;
	}

	public void setSubProperty(String subProperty) {
		this.subProperty = subProperty;
	}
    
    

}
