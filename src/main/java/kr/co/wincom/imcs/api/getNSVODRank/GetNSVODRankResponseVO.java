package kr.co.wincom.imcs.api.getNSVODRank;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class GetNSVODRankResponseVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * GetNSVODRank API 전문 칼럼(순서 일치)
	********************************************************************/
	private String seriesYn		= "";	// 시리즈 여부
    private String relationYn	= "";	// 동일장르 여부
    private String recommendYn	= "";	// 전체(동일장르제외) 여부
    private String catId		= "";	// 카테고리 ID
    private String catName		= "";	// 카테고리명
    private String contsId		= "";	// 컨텐츠 ID
    private String contsName	= "";	// 컨텐츠명
    private String genreName	= "";	// 장르명
    private String rankNo		= "";	// 순위정보
    private String imgUrl		= "";	// 포스터 URL
    private String imgFileName	= "";	// 포스터명
    private String runtime		= "";	// 상영시간
    private String prInfo		= "";	// 나이제한
    private String is51ch		= "";	// 5.1ch
    private String isHd			= "";	// HD 여부
    private String albumId		= "";	// 앨범 ID
    private String point		= "";	// 평점
    private String cnt			= "";
    private String wideImageUrl	= "";	// 포스터 URL
    private String realHDYn		= "";	// Real HD 여부
    private String suggestedPrice	= "";	// 제공 가격
    
    private String serCatId		= "";	// 시리즈 카테고리ID (연동규격서 상에는 없는데 소스에는 있음)
    
    /********************************************************************
   	 * 추가 사용 파라미터
   	********************************************************************/
	private int iRankNo;
    private String audioType	= "";
    private String hdcontent	= "";
    private int iOrdnum;
    private int rownum;
    
    @Override
    public String toString(){
		StringBuffer sb = new StringBuffer();
			
		sb.append(StringUtil.nullToSpace(this.seriesYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.relationYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.recommendYn)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.catId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.catName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.contsId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.contsName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.genreName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.rankNo)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.imgUrl)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.imgFileName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.runtime)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.prInfo)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.is51ch)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.isHd)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.albumId)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.point)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.cnt)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.wideImageUrl)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.realHDYn)).append(ImcsConstants.COLSEP);
		//sb.append(StringUtil.nullToSpace(this.imgFileName)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.suggestedPrice)).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.serCatId)).append(ImcsConstants.COLSEP);
			
		return sb.toString();
	}
   
	public String getSeriesYn() {
		return seriesYn;
	}
	public void setSeriesYn(String seriesYn) {
		this.seriesYn = seriesYn;
	}
	public String getRelationYn() {
		return relationYn;
	}
	public void setRelationYn(String relationYn) {
		this.relationYn = relationYn;
	}
	public String getRecommendYn() {
		return recommendYn;
	}
	public void setRecommendYn(String recommendYn) {
		this.recommendYn = recommendYn;
	}
	public String getCatId() {
		return catId;
	}
	public void setCatId(String catId) {
		this.catId = catId;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	public String getContsId() {
		return contsId;
	}
	public void setContsId(String contsId) {
		this.contsId = contsId;
	}
	public String getContsName() {
		return contsName;
	}
	public void setContsName(String contsName) {
		this.contsName = contsName;
	}
	public String getRuntime() {
		return runtime;
	}
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getImgFileName() {
		return imgFileName;
	}
	public void setImgFileName(String imgFileName) {
		this.imgFileName = imgFileName;
	}
	public String getGenreName() {
		return genreName;
	}
	public void setGenreName(String genreName) {
		this.genreName = genreName;
	}
	public String getPrInfo() {
		return prInfo;
	}
	public void setPrInfo(String prInfo) {
		this.prInfo = prInfo;
	}
	public String getIs51ch() {
		return is51ch;
	}
	public void setIs51ch(String is51ch) {
		this.is51ch = is51ch;
	}
	public String getIsHd() {
		return isHd;
	}
	public void setIsHd(String isHd) {
		this.isHd = isHd;
	}
	public String getAlbumId() {
		return albumId;
	}
	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}
	public String getWideImageUrl() {
		return wideImageUrl;
	}
	public void setWideImageUrl(String wideImageUrl) {
		this.wideImageUrl = wideImageUrl;
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public String getCnt() {
		return cnt;
	}
	public void setCnt(String cnt) {
		this.cnt = cnt;
	}
	public String getRealHDYn() {
		return realHDYn;
	}
	public void setRealHDYn(String realHDYn) {
		this.realHDYn = realHDYn;
	}
	public String getSuggestedPrice() {
		return suggestedPrice;
	}
	public void setSuggestedPrice(String suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}
	public String getSerCatId() {
		return serCatId;
	}
	public void setSerCatId(String serCatId) {
		this.serCatId = serCatId;
	}

	public int getiRankNo() {
		return iRankNo;
	}

	public void setiRankNo(int iRankNo) {
		this.iRankNo = iRankNo;
	}

	public String getAudioType() {
		return audioType;
	}

	public void setAudioType(String audioType) {
		this.audioType = audioType;
	}

	public String getHdcontent() {
		return hdcontent;
	}

	public void setHdcontent(String hdcontent) {
		this.hdcontent = hdcontent;
	}

	public int getiOrdnum() {
		return iOrdnum;
	}

	public void setiOrdnum(int iOrdnum) {
		this.iOrdnum = iOrdnum;
	}

	public int getRownum() {
		return rownum;
	}

	public void setRownum(int rownum) {
		this.rownum = rownum;
	}

	public String getRankNo() {
		return rankNo;
	}

	public void setRankNo(String rankNo) {
		this.rankNo = rankNo;
	}
    
    
}
