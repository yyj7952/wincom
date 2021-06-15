package kr.co.wincom.imcs.api.getNSPresent;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;


@SuppressWarnings("serial")
public class GetNSPresentResponseVO extends NoSqlLoggingVO implements Serializable {

	private String contsType		= "";	// 컨텐츠타입 (0:FVOd, 1:PPV, 2:PVOD, 3:SVOD)
	private String catIdArr			= "";	// 카테고리ID (CAT_ID)
	private String imgUrl 			= "";	// 포스터 URL
	private String imgFileName		= "";	// 포스터 파일명
	private String albumId			= "";	// 영화 ID(CONTS_ID)
	private String contsName		= "";	// 영화 제목
	private String presentDate		= "";	// 구매 날짜
	private String buyingPrice		= "";	// 구매 금액
	private String presentPrice		= "";	// 선물 금액
	private String expiredDate		= "";	// 상품 만료시간
	private String prInfo			= "";	// 나이 제한
	private String isHd 			= "";	// HD영상 구분
	private String seriesNo			= "";	// 회차 번호
	private String totalCnt			= "";	// 목록 총 갯수
	private String sendSaId			= "";	// 발송자 가입자 번호
	private String sendStbMac		= "";	// 발송자 가입자 MAC_ADDRESS
	private String sendCtnNo		= "";	// 발송자 CTN번호
	private String rcvSaId			= "";	// 수신자 가입자 번호
	private String rcvStbMac		= "";	// 수신자 가입자 MAC_ADDRESS
	private String rcvCtnNo			= "";	// 수신자 CTN번호
    private String presentGb		= "";	// 선물구분
    private String useYn			= "";	// 사용유무
    private String useDate			= "";	// 사용일
    private String hdcontent		= "";	// HD컨텐츠?
    private String presentYn		= "";	// 선물여부
    private String presentRate		= "";	// 선물 할인율
    private String realPresentPrice	= "";	// 선물금액(부가세포함)
    private int ordnum;
    
    
	
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append(StringUtil.replaceNull(this.contsType, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.catIdArr, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgUrl, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.imgFileName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.albumId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.contsName, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.presentDate, "")).append(ImcsConstants.COLSEP);

		sb.append(StringUtil.replaceNull(this.buyingPrice, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.presentPrice, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.expiredDate, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.prInfo, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.isHd, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.seriesNo, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.totalCnt, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.sendSaId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.sendStbMac, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.sendCtnNo, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.rcvSaId, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.rcvStbMac, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.rcvCtnNo, "")).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.replaceNull(this.realPresentPrice, "")).append(ImcsConstants.COLSEP);
		
		return sb.toString();
	}
	
			    
	public String getContsType() {
		return contsType;
	}
	public void setContsType(String contsType) {
		this.contsType = contsType;
	}
	public String getAlbumId() {
		return albumId;
	}
	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}
	public String getContsName() {
		return contsName;
	}
	public void setContsName(String contsName) {
		this.contsName = contsName;
	}
	public String getPresentDate() {
		return presentDate;
	}
	public void setPresentDate(String presentDate) {
		this.presentDate = presentDate;
	}
	public String getBuyingPrice() {
		return buyingPrice;
	}
	public void setBuyingPrice(String buyingPrice) {
		this.buyingPrice = buyingPrice;
	}
	public String getPresentPrice() {
		return presentPrice;
	}
	public void setPresentPrice(String presentPrice) {
		this.presentPrice = presentPrice;
	}	
	public String getExpiredDate() {
		return expiredDate;
	}
	public void setExpiredDate(String expiredDate) {
		this.expiredDate = expiredDate;
	}
	public String getPresentGb() {
		return presentGb;
	}
	public void setPresentGb(String presentGb) {
		this.presentGb = presentGb;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getUseDate() {
		return useDate;
	}
	public void setUseDate(String useDate) {
		this.useDate = useDate;
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
	public String getCatIdArr() {
		return catIdArr;
	}
	public void setCatIdArr(String catIdArr) {
		this.catIdArr = catIdArr;
	}
	public String getPrInfo() {
		return prInfo;
	}
	public void setPrInfo(String prInfo) {
		this.prInfo = prInfo;
	}
	public String getSeriesNo() {
		return seriesNo;
	}
	public void setSeriesNo(String seriesNo) {
		this.seriesNo = seriesNo;
	}
	public String getSendSaId() {
		return sendSaId;
	}
	public void setSendSaId(String sendSaId) {
		this.sendSaId = sendSaId;
	}
	public String getSendStbMac() {
		return sendStbMac;
	}
	public void setSendStbMac(String sendStbMac) {
		this.sendStbMac = sendStbMac;
	}
	public String getSendCtnNo() {
		return sendCtnNo;
	}
	public void setSendCtnNo(String sendCtnNo) {
		this.sendCtnNo = sendCtnNo;
	}
	public String getRcvSaId() {
		return rcvSaId;
	}
	public void setRcvSaId(String rcvSaId) {
		this.rcvSaId = rcvSaId;
	}
	public String getRcvStbMac() {
		return rcvStbMac;
	}
	public void setRcvStbMac(String rcvStbMac) {
		this.rcvStbMac = rcvStbMac;
	}
	public String getRcvCtnNo() {
		return rcvCtnNo;
	}
	public void setRcvCtnNo(String rcvCtnNo) {
		this.rcvCtnNo = rcvCtnNo;
	}
	public String getPresentYn() {
		return presentYn;
	}
	public void setPresentYn(String presentYn) {
		this.presentYn = presentYn;
	}
	public String getPresentRate() {
		return presentRate;
	}
	public void setPresentRate(String presentRate) {
		this.presentRate = presentRate;
	}
	public int getOrdnum() {
		return ordnum;
	}
	public void setOrdnum(int ordnum) {
		this.ordnum = ordnum;
	}
	public String getTotalCnt() {
		return totalCnt;
	}
	public void setTotalCnt(String totalCnt) {
		this.totalCnt = totalCnt;
	}


	public String getRealPresentPrice() {
		return realPresentPrice;
	}


	public void setRealPresentPrice(String realPresentPrice) {
		this.realPresentPrice = realPresentPrice;
	}
    
}
