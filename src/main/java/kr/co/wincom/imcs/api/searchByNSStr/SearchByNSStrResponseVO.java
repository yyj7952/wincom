package kr.co.wincom.imcs.api.searchByNSStr;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;

import org.apache.commons.lang.StringUtils;


@SuppressWarnings("serial")
public class SearchByNSStrResponseVO extends NoSqlLoggingVO implements Serializable {

	private String resultType	= "";		// CAT/ALB
	private String contsId		= "";		// 앨범ID
    private String contsName	= "";		// 앨범이름
    private String catType		= "";		// 카테고리타입 (PKG:패키지, SER:시리즈)
    private String imgUrl		= "";		// 이미지URL
    private String imgFileName	= "";		// 이미지파일명
    private String parentCatId	= "";		// 상위 카테고리ID (parentCatId)
    private String authYn		= "";		// 카테고리 무조건 연령인증
    private String chaNum		= "";		// 채널번호
    private String catLevel		= "";		// 카테고리 레벨정보 (contsLevel)
    private String billFlag		= "";		// 유/무료 (price)
 // EXIST_SUB_CAT N
    private String prInfo		= "";		// 나이제한 (rating)
    private String runTime		= "";		// 상영시간
    private String is51ch		= "";		// 5.1ch 여부
    private String isNew		= "";		// 신규카테고리 등록 여부
    private String isUpdate		= "";		// 카테고리 등록업데이트 여부
    private String isHot		= "";		// HOT상품여부
    private String isCaption	= "";		// 자막여부 (captionYn)
    private String hdcontent	= "";		// HD영상여부
    private String isHd			= "";		// HD영상여부
    private String relayView	= "";		// 컨텐츠 연속재생가능여부 (IS_CONTINOUS_PLAY) A:역순재생, D:정순재생, N:연속재생없음
    private int subCnt;					// 하위카테고리 및 앨범 수
    private String point		= "";		// 평점
    private String closeYn		= "";		// 종영작여부
    private String svodYn		= "";		// SVOD여부
    private String is3d			= "";		// 3D여부
    private String serviceIcon	= "";		// 서비스 구분
    private String filterGb		= "";		// 필터구분
    private String ppsYn		= "";		// PPS여부
    private String categoryDesc	= "";		// 카테고리 설명
    private String isOrder		= "";		// 카테고리정렬
    private String fnsFileName	= "";		// 추가 카테고리 이미지
    private String terrCh		= "";		// 지상파채널
    private String overseeName	= "";		// 감독
    private String actor		= "";		// 출연
    private String releaseDate	= "";		// 개통일
    private String catGb		= "";		// 카테고리 구분
    
    
    private String catGb1		= "";		// 카테고리 구분
    private String catGb2		= "";		// 카테고리 구분
    private String catGb3		= "";		// 카테고리 구분
    
    
    private String albumBillFlag	= "";
    private String suggestedPrice	= "";
    private String productType		= "";
    private String eventValue		= "";
    
 /*   
    	// 
    
    private String categoryGb = "";
    private String imnCategoryType = "";
    private String imnRankYn = "";
    private String imnAuthYn = "";
    private String imnGenre = "";
    private String imnProductId = "";
    private String imnIsOrder = "";
    private String imnCategoryDesc = "";
    private String catGb1 = "";
    private String catGb2 = "";
    private String catGb3 = "";
    private String imageYn = "";
    private String cSubCnt = "";			//
    
    private String tempPrice = "";
    
    */
    
    
    @Override
   	public String toString() {
   		StringBuffer sb = new StringBuffer();
   		
   		sb.append(StringUtils.defaultString(this.getResultType(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getContsId(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getContsName(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getCatType(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getImgUrl(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getImgFileName(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getParentCatId(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getAuthYn(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getChaNum(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getCatLevel(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getAlbumBillFlag(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString("N", "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getPrInfo(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getRunTime(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getIs51ch(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getIsNew(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getIsUpdate(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getIsHot(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getIsCaption(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getIsHd(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getRelayView(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getPoint(), "")).append(ImcsConstants.COLSEP);
	    sb.append(this.getSubCnt()).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getCloseYn(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getSvodYn(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getIs3d(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getServiceIcon(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getFilterGb(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getPpsYn(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getCategoryDesc(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getIsOrder(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getFnsFileName(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getTerrCh(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getOverseeName(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getActor(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getReleaseDate(), "")).append(ImcsConstants.COLSEP);
	    sb.append(StringUtils.defaultString(this.getCatGb1(), "")).append(ImcsConstants.ARRSEP);
	    sb.append(StringUtils.defaultString(this.getCatGb2(), "")).append(ImcsConstants.ARRSEP);
	    sb.append(StringUtils.defaultString(this.getCatGb3(), "")).append(ImcsConstants.COLSEP);
	    sb.append(ImcsConstants.ROWSEP);
   		
   		return sb.toString();
   	}
    
    
    
	public String getResultType() {
		return resultType;
	}
	public void setResultType(String resultType) {
		this.resultType = resultType;
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
	public String getCatType() {
		return catType;
	}
	public void setCatType(String catType) {
		this.catType = catType;
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
	public String getParentCatId() {
		return parentCatId;
	}
	public void setParentCatId(String parentCatId) {
		this.parentCatId = parentCatId;
	}
	public String getAuthYn() {
		return authYn;
	}
	public void setAuthYn(String authYn) {
		this.authYn = authYn;
	}
	public String getChaNum() {
		return chaNum;
	}
	public void setChaNum(String chaNum) {
		this.chaNum = chaNum;
	}
	public String getCatLevel() {
		return catLevel;
	}
	public void setCatLevel(String catLevel) {
		this.catLevel = catLevel;
	}
	public String getBillFlag() {
		return billFlag;
	}
	public void setBillFlag(String billFlag) {
		this.billFlag = billFlag;
	}
	public String getPrInfo() {
		return prInfo;
	}
	public void setPrInfo(String prInfo) {
		this.prInfo = prInfo;
	}
	public String getRunTime() {
		return runTime;
	}
	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}
	public String getIs51ch() {
		return is51ch;
	}
	public void setIs51ch(String is51ch) {
		this.is51ch = is51ch;
	}
	public String getIsNew() {
		return isNew;
	}
	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}
	public String getIsUpdate() {
		return isUpdate;
	}
	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}
	public String getIsHot() {
		return isHot;
	}
	public void setIsHot(String isHot) {
		this.isHot = isHot;
	}
	public String getHdcontent() {
		return hdcontent;
	}
	public void setHdcontent(String hdcontent) {
		this.hdcontent = hdcontent;
	}
	public String getIsHd() {
		return isHd;
	}
	public void setIsHd(String isHd) {
		this.isHd = isHd;
	}
	public String getRelayView() {
		return relayView;
	}
	public void setRelayView(String relayView) {
		this.relayView = relayView;
	}
	public int getSubCnt() {
		return subCnt;
	}
	public void setSubCnt(int subCnt) {
		this.subCnt = subCnt;
	}
	public String getPoint() {
		return point;
	}
	public void setPoint(String point) {
		this.point = point;
	}
	public String getCloseYn() {
		return closeYn;
	}
	public void setCloseYn(String closeYn) {
		this.closeYn = closeYn;
	}
	public String getSvodYn() {
		return svodYn;
	}
	public void setSvodYn(String svodYn) {
		this.svodYn = svodYn;
	}
	public String getIs3d() {
		return is3d;
	}
	public void setIs3d(String is3d) {
		this.is3d = is3d;
	}
	public String getFilterGb() {
		return filterGb;
	}
	public void setFilterGb(String filterGb) {
		this.filterGb = filterGb;
	}
	public String getServiceIcon() {
		return serviceIcon;
	}
	public void setServiceIcon(String serviceIcon) {
		this.serviceIcon = serviceIcon;
	}
	public String getPpsYn() {
		return ppsYn;
	}
	public void setPpsYn(String ppsYn) {
		this.ppsYn = ppsYn;
	}
	public String getCategoryDesc() {
		return categoryDesc;
	}
	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}
	public String getIsOrder() {
		return isOrder;
	}
	public void setIsOrder(String isOrder) {
		this.isOrder = isOrder;
	}
	public String getFnsFileName() {
		return fnsFileName;
	}
	public void setFnsFileName(String fnsFileName) {
		this.fnsFileName = fnsFileName;
	}
	public String getTerrCh() {
		return terrCh;
	}
	public void setTerrCh(String terrCh) {
		this.terrCh = terrCh;
	}
	public String getOverseeName() {
		return overseeName;
	}
	public void setOverseeName(String overseeName) {
		this.overseeName = overseeName;
	}
	public String getActor() {
		return actor;
	}
	public void setActor(String actor) {
		this.actor = actor;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getCatGb() {
		return catGb;
	}
	public void setCatGb(String catGb) {
		this.catGb = catGb;
	}
	public String getCatGb1() {
		return catGb1;
	}



	public void setCatGb1(String catGb1) {
		this.catGb1 = catGb1;
	}



	public String getCatGb2() {
		return catGb2;
	}



	public void setCatGb2(String catGb2) {
		this.catGb2 = catGb2;
	}



	public String getCatGb3() {
		return catGb3;
	}



	public void setCatGb3(String catGb3) {
		this.catGb3 = catGb3;
	}



	public String getIsCaption() {
		return isCaption;
	}
	public void setIsCaption(String isCaption) {
		this.isCaption = isCaption;
	}



	public String getAlbumBillFlag() {
		return albumBillFlag;
	}



	public void setAlbumBillFlag(String albumBillFlag) {
		this.albumBillFlag = albumBillFlag;
	}



	public String getProductType() {
		return productType;
	}



	public void setProductType(String productType) {
		this.productType = productType;
	}



	public String getSuggestedPrice() {
		return suggestedPrice;
	}



	public void setSuggestedPrice(String suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}



	public String getEventValue() {
		return eventValue;
	}



	public void setEventValue(String eventValue) {
		this.eventValue = eventValue;
	}

}
