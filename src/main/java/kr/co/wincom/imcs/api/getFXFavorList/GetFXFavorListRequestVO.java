package kr.co.wincom.imcs.api.getFXFavorList;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetFXFavorListRequestVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getFXFavorList API 전문 칼럼(순서 일치)
	********************************************************************/	
	private String saId			= "";	// 사용자 아이디
	private String stbMac		= "";	// MAC_ADDRESS
	private String fxType		= "";	// 유플릭스 타입 (M:MOBILE, P:PC_WEB, T:TVG_APP, H:HDTV)
	private String pageNo		= "";	// 페이지 넘버
	private String pageCnt		= "";	// 페이지 개수
	private String orderGb		= "";	// 정렬 (N:최신, A:제목)
	private String quickDisYn	= "";	// 퀵배포 컨텐츠 포함 여부
	
	/********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
	private String pid			= "";
	private String catGb		= "";
	private String posterType	= ""; 
	private String contsId		= "";
	private String albumId		= "";
	private String catId1		= "";
	
	public GetFXFavorListRequestVO(){}
	
	public GetFXFavorListRequestVO(String szParam){
		String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("fx_type") == -1  )
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
				
				if(key.toLowerCase().equals("sa_id"))			this.saId		= value;
				if(key.toLowerCase().equals("stb_mac"))			this.stbMac		= value;
				if(key.toLowerCase().equals("fx_type"))			this.fxType		= value;
				if(key.toLowerCase().equals("page_no"))			this.pageNo		= value;
				if(key.toLowerCase().equals("page_cnt"))		this.pageCnt	= value;
				if(key.toLowerCase().equals("order_gb"))		this.orderGb	= value;
				if(key.toLowerCase().equals("quick_dis_yn"))	this.quickDisYn	= value;
			}
		}
		
		//GetFXFavorListController.saId = paramMap.get("sa_id");
		
		//GetFXFavorListController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null 
				|| paramMap.get("fx_type") == null )
		{
			throw new ImcsException();
		}
		
		/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
			throw new ImcsException();
		}*/
		
		this.pageNo		= StringUtil.replaceNull(this.pageNo, "A");
		this.pageCnt	= StringUtil.replaceNull(this.pageCnt, "A");
		this.orderGb	= StringUtil.replaceNull(this.orderGb, "N");
		
		/*switch (orderGb) {
		case "A":
		case "N":
			break;

		default:
			throw new ImcsException();
		}*/
		
		if("M".equals(this.fxType) || "H".equals(this.fxType)) {
			this.catGb		= "NSC";
			this.posterType	= "P";
		} else if("P".equals(this.fxType)) {
			this.catGb		= "I20";
			this.posterType	= "P";
		} else {
			this.catGb		= "I20";
			this.posterType	= "V";
		}
		
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

	public String getFxType() {
		return fxType;
	}

	public void setFxType(String fxType) {
		this.fxType = fxType;
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

	public String getQuickDisYn() {
		return quickDisYn;
	}

	public void setQuickDisYn(String quickDisYn) {
		this.quickDisYn = quickDisYn;
	}

	public String getCatGb() {
		return catGb;
	}

	public void setCatGb(String catGb) {
		this.catGb = catGb;
	}

	public String getPosterType() {
		return posterType;
	}

	public void setPosterType(String posterType) {
		this.posterType = posterType;
	}

	public String getContsId() {
		return contsId;
	}

	public void setContsId(String contsId) {
		this.contsId = contsId;
	}

	public String getCatId1() {
		return catId1;
	}

	public void setCatId1(String catId1) {
		this.catId1 = catId1;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

}
