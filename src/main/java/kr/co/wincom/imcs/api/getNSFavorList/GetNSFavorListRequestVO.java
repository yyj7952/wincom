package kr.co.wincom.imcs.api.getNSFavorList;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

public class GetNSFavorListRequestVO extends NoSqlLoggingVO implements Serializable {
	private static final long serialVersionUID = 820027358203692775L;
	
	
	/********************************************************************
	 * getFXFavorList API 전문 칼럼(순서 일치)
	********************************************************************/	
	private String saId			= "";	// 사용자 아이디
	private String stbMac		= "";	// MAC_ADDRESS
	private String contsGb		= "";	// 유플릭스 타입 (M:MOBILE, P:PC_WEB, T:TVG_APP, H:HDTV)
	private String pageNo		= "";	// 페이지 넘버
	private String pageCnt		= "";	// 페이지 개수
	private String orderGb		= "";	// 정렬 (N:최신, A:제목)
	private String quickDisYn	= "";	// 퀵배포 컨텐츠 포함 여부
	
	/********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
	private String pid			= "";
	private String resultCode	= "";
	private String chnlCd		= "";
	private String adiAlbumId	= "";
	private String parentCatId	= "";
	private String checkValue	= "";
	private String contsId		= "";
	private String currentDate	= "";
	
	public GetNSFavorListRequestVO(){}
	
	public GetNSFavorListRequestVO(String szParam){
		String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("conts_gb") == -1  )
		{
			throw new ImcsException();
		}*/
		
		for(int i = 0; i < arrStat.length; i++){
			nStr	= arrStat[i].indexOf("=");
			
			if(nStr > 0) {
				key = arrStat[i].substring(0, nStr).toLowerCase().trim();
				value = arrStat[i].substring(nStr + 1, arrStat[i].length()).trim();
				
				paramMap.put(key, value);
				
				/*if( !value.matches(ImcsConstants.N_SP_PTN) && key.toLowerCase().indexOf("name") == -1 ){
					//특수문자 있음
					throw new ImcsException();
				}*/
				
				if(key.toLowerCase().equals("sa_id"))		this.setSaId(value);
				if(key.toLowerCase().equals("stb_mac"))		this.setStbMac(value);
				if(key.toLowerCase().equals("conts_gb") || key.toLowerCase().equals("cont_gb") )	this.setContsGb(value);
				if(key.toLowerCase().equals("page_no"))		this.setPageNo(value);
				if(key.toLowerCase().equals("page_cnt"))	this.setPageCnt(value);
				if(key.toLowerCase().equals("order_gb"))	this.setOrderGb(value);
				if(key.toLowerCase().equals("quick_dis_yn"))	this.setQuickDisYn(value);
			}
		}
		
		//GetNSFavorListController.saId = paramMap.get("sa_id");
		
		//GetNSFavorListController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
				|| (paramMap.get("conts_gb") == null &&  paramMap.get("cont_gb") == null) )
		{
			throw new ImcsException();
		}
		
		/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
			throw new ImcsException();
		}*/
		
		this.contsGb	= StringUtil.replaceNull(this.getContsGb(), "NSC");
		this.pageNo		= StringUtil.replaceNull(this.getPageNo(), "A");
		this.pageCnt	= StringUtil.replaceNull(this.getPageCnt(), "A");
		this.orderGb	= StringUtil.replaceNull(this.getOrderGb(), "N");
		
		/*switch (contsGb) {
		case "NSC":
		case "PAD":
		case "I20":
		case "PCT":
			break;

		default:
			throw new ImcsException();
		}
		
		switch (orderGb) {
		case "A":
		case "N":
			break;

		default:
			throw new ImcsException();
		}*/
		
		if(this.contsGb.equals("PAD")) 			this.contsGb	= "NSC";

	}
	

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
		
	public String getSaId() {
		return saId;
	}

	public void setSaId(String saId) {
		this.saId = saId;
	}

	public String getStbMac() {
		return stbMac;
	}

	public void setStbMac(String stbMac) {
		this.stbMac = stbMac;
	}

	public String getContsGb() {
		return contsGb;
	}

	public void setContsGb(String contsGb) {
		this.contsGb = contsGb;
	}

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	public String getPageCnt() {
		return pageCnt;
	}

	public void setPageCnt(String pageCnt) {
		this.pageCnt = pageCnt;
	}

	public String getOrderGb() {
		return orderGb;
	}

	public void setOrderGb(String orderGb) {
		this.orderGb = orderGb;
	}
	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getQuickDisYn() {
		return quickDisYn;
	}

	public void setQuickDisYn(String quickDisYn) {
		this.quickDisYn = quickDisYn;
	}

	public String getChnlCd() {
		return chnlCd;
	}

	public void setChnlCd(String chnlCd) {
		this.chnlCd = chnlCd;
	}

	public String getAdiAlbumId() {
		return adiAlbumId;
	}

	public void setAdiAlbumId(String adiAlbumId) {
		this.adiAlbumId = adiAlbumId;
	}

	public String getCheckValue() {
		return checkValue;
	}

	public void setCheckValue(String checkValue) {
		this.checkValue = checkValue;
	}

	public String getContsId() {
		return contsId;
	}

	public void setContsId(String contsId) {
		this.contsId = contsId;
	}

	public String getParentCatId() {
		return parentCatId;
	}

	public void setParentCatId(String parentCatId) {
		this.parentCatId = parentCatId;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}
}
