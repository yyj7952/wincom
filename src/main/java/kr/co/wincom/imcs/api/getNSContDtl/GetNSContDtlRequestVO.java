package kr.co.wincom.imcs.api.getNSContDtl;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;


@SuppressWarnings("serial")
public class GetNSContDtlRequestVO  extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * GetNSContDtl API 전문 칼럼(순서 일치)
	********************************************************************/
    private String saId			= "";
    private String stbMac		= "";
    private String albumId		= "";
    private String catId		= "";
    private String definFlag	= "";
    private String nscType		= "";
    private String fxType		= "";
    private String quickDisYn	= "";
    private String decPosYn		= "";
    
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String testSbc		= "";
    private String posterType	= "";
    private String viewFlag1	= "";
    private String viewFlag2	= "";
    private String setPointYn	= "";
    private String hdGb			= "";
    private String contsId		= "";
    private String prodId		= "";
    
    private String suggestedPrice	= "";
    private String adiProdId	= "";		// 패키지정보 및 가격정보 조회시 사용
    
    private String pid			= "";
    private String resultCode	= "";
    private Integer resultSet	= 0;
    
    public GetNSContDtlRequestVO(){}
	
	public GetNSContDtlRequestVO(String szParam){
		
		String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("cat_id") == -1 || szTemp.indexOf("album_id") == -1 
				|| szTemp.indexOf("defin_flag") == -1  )
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
				if(key.toLowerCase().equals("album_id"))		this.albumId = value;
				if(key.toLowerCase().equals("defin_flag"))		this.definFlag = value;
				if(key.toLowerCase().equals("nsc_type"))		this.nscType = value;
				if(key.toLowerCase().equals("fx_type"))			this.fxType = value;
				if(key.toLowerCase().equals("quick_dis_yn"))	this.quickDisYn = value;
				if(key.toLowerCase().equals("dec_pos_yn") || key.toLowerCase().equals("des_pos_yn"))		this.decPosYn = value;
			}
		}
		
		//GetNSContDtlController.saId = paramMap.get("sa_id");
		
		//GetNSContDtlController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
				|| paramMap.get("cat_id") == null || paramMap.get("album_id") == null 
				|| paramMap.get("defin_flag") == null  )
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
		}*/
		
		
		if( "LTE".equals(this.nscType))			this.posterType	= "P";
		else if ( "PAD".equals(this.nscType))	this.posterType	= "D";
		else if ( "PAH".equals(this.nscType))	this.posterType = "W";
	    		    
	    if( "Y".equals(this.getQuickDisYn()))   this.viewFlag1 = "P";
	    else							    	this.viewFlag1 = "V";
	    
	    if( "1".equals(definFlag))		    	this.hdGb = "Y";
	    else if( "3".equals(definFlag))			this.hdGb = "N";
	    
	    this.setPointYn = "N";
		
	    if( ("".equals(this.saId) && "".equals(this.stbMac)) || this.catId.indexOf("undef") != -1)
	    	throw new ImcsException();
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

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
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

	public String getViewFlag2() {
		return viewFlag2;
	}

	public void setViewFlag2(String viewFlag2) {
		this.viewFlag2 = viewFlag2;
	}

	public String getSetPointYn() {
		return setPointYn;
	}

	public void setSetPointYn(String setPointYn) {
		this.setPointYn = setPointYn;
	}

	public String getHdGb() {
		return hdGb;
	}

	public void setHdGb(String hdGb) {
		this.hdGb = hdGb;
	}

	public String getSuggestedPrice() {
		return suggestedPrice;
	}

	public void setSuggestedPrice(String suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}

	public String getContsId() {
		return contsId;
	}

	public void setContsId(String contsId) {
		this.contsId = contsId;
	}

	public String getTestSbc() {
		return testSbc;
	}

	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}

	public String getAdiProdId() {
		return adiProdId;
	}

	public void setAdiProdId(String adiProdId) {
		this.adiProdId = adiProdId;
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

	public String getProdId() {
		return prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public Integer getResultSet() {
		return resultSet;
	}

	public void setResultSet(Integer resultSet) {
		this.resultSet = resultSet;
	}

	public String getDecPosYn() {
		return decPosYn;
	}

	public void setDecPosYn(String decPosYn) {
		this.decPosYn = decPosYn;
	}   
    
    
}
