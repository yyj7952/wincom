package kr.co.wincom.imcs.api.getNSContList;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

public class GetNSContListRequestVO extends NoSqlLoggingVO implements Serializable
{
	private static final long serialVersionUID = 820027358203692775L;

	/********************************************************************
	 * getNSPurchased API 전문 칼럼(순서 일치)
	********************************************************************/
    private String saId			= "";
    private String stbMac		= "";
    private String catId		= "";
    private String definFlag	= "";
    private String nscType		= "";
    private String pageNo		= "";
    private String pageCnt		= "";
    private String pageIdx		= "";
    private String closeYn		= "";
    private String fxType		= "";
    private String quickDisYn	= "";
    private String decPosYn	= "";
	
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String pid			= "";
	private String resultCode	= "";
	
	private String posterType	= "";
	private String testSbc		= "";
	private String viewFlag1	= "";
	private String viewFlag2	= "";
	private String pkgYn		= "";
	private String svodYn		= "";
	private String rangeIpCd	= "";
	private String selectAll	= "";
	private String startNo		= "";
	private String endNo		= "";
	private String nscGb		= "";	// 컨텐츠 조회 시 조회됨 
	private String adiProdId	= "";
	private String contsId		= "";
	private String downYn		= "";
	private String setPointYn	= "";
	private String tempId		= "";	// 패키지나 앨범 상품 중복 조회시 사용될 id
	
	private int resultSet		= 0;
	
	public GetNSContListRequestVO(){}
	
	public GetNSContListRequestVO(String szParam){
		String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( paramMap.get("sa_id") == -1 || paramMap.get("stb_mac") == -1
				|| paramMap.get("cat_id") == -1 || paramMap.get("defin_flag") == -1 )
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
				
				if(key.toLowerCase().equals("sa_id"))			this.saId = value;
				if(key.toLowerCase().equals("stb_mac"))			this.stbMac = value;
				if(key.toLowerCase().equals("cat_id"))			this.catId = value;
				if(key.toLowerCase().equals("defin_flag"))		this.definFlag = value;
				if(key.toLowerCase().equals("nsc_type"))		this.nscType = value;
				if(key.toLowerCase().equals("page_no"))			this.pageNo = value;
				if(key.toLowerCase().equals("page_cnt"))		this.pageCnt = value;
				if(key.toLowerCase().equals("page_idx"))		this.pageIdx = value;
				if(key.toLowerCase().equals("close_yn"))		this.closeYn = value;
				if(key.toLowerCase().equals("fx_type"))			this.fxType = value;
				if(key.toLowerCase().equals("quick_dis_yn"))	this.quickDisYn = value;
				if(key.toLowerCase().equals("dec_pos_yn"))		this.decPosYn = value;
			}
		}
		
		//GetNSContListController.saId = paramMap.get("sa_id");
		
		//GetNSContListController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
				|| paramMap.get("cat_id") == null || paramMap.get("defin_flag") == null )
		{
			throw new ImcsException();
		}
		
		/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
			throw new ImcsException();
		}*/
		
		this.nscType	= StringUtil.replaceNull(this.nscType, "LTE");
		this.pageNo		= StringUtil.replaceNull(this.pageNo, "0");
		this.pageCnt	= StringUtil.replaceNull(this.pageCnt, "0");
		this.pageIdx	= StringUtil.replaceNull(this.pageIdx, "0");
		this.closeYn	= StringUtil.replaceNull(this.closeYn, "Y");
		this.fxType		= StringUtil.replaceNull(this.fxType, "X");
		this.quickDisYn	= StringUtil.replaceNull(this.quickDisYn, "N");
		this.decPosYn	= StringUtil.replaceNull(this.decPosYn, "N");
		
		/*switch (nscType) {
		case "LTE":
		case "PAD":
		case "PAH":
		case "PAW":
			break;

		default:
			throw new ImcsException();
		}
		
		switch (closeYn) {
		case "Y":
		case "N":
			break;

		default:
			throw new ImcsException();
		}
		
		switch (fxType) {
		case "X":
		case "M":
		case "P":
		case "T":
		case "H":
			break;

		default:
			throw new ImcsException();
		}
		
		switch (quickDisYn) {
		case "Y":
		case "N":
			break;

		default:
			throw new ImcsException();
		}
		
		switch (decPosYn) {
		case "Y":
		case "N":
			break;

		default:
			throw new ImcsException();
		}*/
		
		if( "LTE".equals(this.nscType))			this.posterType	= "P";
		else if ( "PAD".equals(this.nscType))	this.posterType	= "D";
		else if ( "PAH".equals(this.nscType))	this.posterType = "W";
	    		    
	    if( "Y".equals(this.quickDisYn))  		this.viewFlag1 = "P";
	    else							    	this.viewFlag1 = "V";
	    
		if( ("".equals(this.saId) && "".equals(this.stbMac)) || this.catId.indexOf("undef") != -1 )
			throw new ImcsException();
		
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

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getDefinFlag() {
		return definFlag;
	}

	public void setDefinFlag(String definFlag) {
		this.definFlag = definFlag;
	}

	public String getNscType() {
		return nscType;
	}

	public void setNscType(String nscType) {
		this.nscType = nscType;
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

	public String getPageIdx() {
		return pageIdx;
	}

	public void setPageIdx(String pageIdx) {
		this.pageIdx = pageIdx;
	}

	public String getCloseYn() {
		return closeYn;
	}

	public void setCloseYn(String closeYn) {
		this.closeYn = closeYn;
	}

	public String getFxType() {
		return fxType;
	}

	public void setFxType(String fxType) {
		this.fxType = fxType;
	}

	public String getQuickDisYn() {
		return quickDisYn;
	}

	public void setQuickDisYn(String quickDisYn) {
		this.quickDisYn = quickDisYn;
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

	public String getPosterType() {
		return posterType;
	}

	public void setPosterType(String posterType) {
		this.posterType = posterType;
	}

	public String getViewFlag1() {
		return viewFlag1;
	}

	public void setViewFlag1(String viewFlag1) {
		this.viewFlag1 = viewFlag1;
	}

	public String getTestSbc() {
		return testSbc;
	}

	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}

	public String getViewFlag2() {
		return viewFlag2;
	}

	public void setViewFlag2(String viewFlag2) {
		this.viewFlag2 = viewFlag2;
	}

	public String getPkgYn() {
		return pkgYn;
	}

	public void setPkgYn(String pkgYn) {
		this.pkgYn = pkgYn;
	}

	public String getSvodYn() {
		return svodYn;
	}

	public void setSvodYn(String svodYn) {
		this.svodYn = svodYn;
	}

	public String getRangeIpCd() {
		return rangeIpCd;
	}

	public void setRangeIpCd(String rangeIpCd) {
		this.rangeIpCd = rangeIpCd;
	}

	public String getSelectAll() {
		return selectAll;
	}

	public void setSelectAll(String selectAll) {
		this.selectAll = selectAll;
	}

	public String getStartNo() {
		return startNo;
	}

	public void setStartNo(String startNo) {
		this.startNo = startNo;
	}

	public String getEndNo() {
		return endNo;
	}

	public void setEndNo(String endNo) {
		this.endNo = endNo;
	}

	public String getNscGb() {
		return nscGb;
	}

	public void setNscGb(String nscGb) {
		this.nscGb = nscGb;
	}

	public String getAdiProdId() {
		return adiProdId;
	}

	public void setAdiProdId(String adiProdId) {
		this.adiProdId = adiProdId;
	}

	public String getContsId() {
		return contsId;
	}

	public void setContsId(String contsId) {
		this.contsId = contsId;
	}

	public String getDownYn() {
		return downYn;
	}

	public void setDownYn(String downYn) {
		this.downYn = downYn;
	}

	public int getResultSet() {
		return resultSet;
	}

	public void setResultSet(int resultSet) {
		this.resultSet = resultSet;
	}

	public String getSetPointYn() {
		return setPointYn;
	}

	public void setSetPointYn(String setPointYn) {
		this.setPointYn = setPointYn;
	}

	public String getTempId() {
		return tempId;
	}

	public void setTempId(String tempId) {
		this.tempId = tempId;
	}

	public String getDecPosYn() {
		return decPosYn;
	}

	public void setDecPosYn(String decPosYn) {
		this.decPosYn = decPosYn;
	}


	
}
