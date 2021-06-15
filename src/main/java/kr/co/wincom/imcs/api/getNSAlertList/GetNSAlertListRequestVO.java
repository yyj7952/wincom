package kr.co.wincom.imcs.api.getNSAlertList;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

public class GetNSAlertListRequestVO extends NoSqlLoggingVO implements Serializable {
	private static final long serialVersionUID = 820027358203692775L;
	
	/********************************************************************
	 * getNSFavorList API 전문 칼럼(순서 일치)
	********************************************************************/	
	private String saId			= "";
	private String stbMac		= "";
	private String contsGb		= "";
	private String pageNo		= "";
	private String pageCnt		= "";
	private String orderGb		= "";
	private String quickDisYn	= "";
	
	/********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
	private String pid			= "";
	private String resultCode	= "";
	private String chnlCd		= "";			// 채널코드 분기를 위해 사용
	private String parentCatId		= "";		// 앨범정보 조회 시 분기를 위해 사용
	private String adiAlbumId	= "";			// 카테고리정보 조회 시 사용
	private String contsId		= "";			// 앨범정보 조회 시 사용
	private String checkValue	= "";			// 상품타입 조회 시 분기를 위해 사용
	
	
	public GetNSAlertListRequestVO(){}
	
	public GetNSAlertListRequestVO(String szParam){
		String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("conts_gb") == -1 )
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
				
				if(key.toLowerCase().equals("sa_id"))			this.setSaId(value);
				if(key.toLowerCase().equals("stb_mac"))			this.setStbMac(value);
				if(key.toLowerCase().equals("conts_gb"))		this.setContsGb(value);
				if(key.toLowerCase().equals("page_no"))			this.setPageNo(value);
				if(key.toLowerCase().equals("page_cnt"))		this.setPageCnt(value);
				if(key.toLowerCase().equals("order_gb"))		this.setOrderGb(value);
				if(key.toLowerCase().equals("quick_dis_yn"))	this.setQuickDisYn(value);
			}
		}
		
		//GetNSAlertListController.saId = paramMap.get("sa_id");
		//GetNSAlertListController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null 
				|| paramMap.get("conts_gb") == null )
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
		
		if("0".equals(this.pageNo)){
			this.pageNo		= "A";
		}
		
		if("0".equals(this.pageCnt)){
			this.pageCnt		= "A";
		}
		
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
		case "N":
		case "A":
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

	public String getChnlCd() {
		return chnlCd;
	}

	public void setChnlCd(String chnlCd) {
		this.chnlCd = chnlCd;
	}

}
