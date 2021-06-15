package kr.co.wincom.imcs.api.getFXContStat;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetFXContStatRequestVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getFXContStat API 전문 칼럼(순서 일치)
	********************************************************************/	
	private String saId			= "";	// 사용자 아이디
	private String stbMac		= "";	// MAC_ADDRESS
	private String albumId		= "";
	private String appType		= "";
	private String isUhd		= "";
	private String catId		= "";
	
	/********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
	private String pid			= "";
	private String catGb		= "I20";
	private int pIdxSa			= 0;
	private String cIdxSa		= "";
	
	private String premiumYn	= "N";
	private String svodProdBuyYn	= "1";	// 일반상품 구매여부
	
	private String testSbc		= "";
	private String contsId		= "";
	private String contsType	= "";
	
	private int resultSet		= 0;
	private long tp_start		= 0;
	
	private String shortYn		= "";
	private String pkgYn		= "";
	private String svodYn		= "";
	private String svodOnly		= "";
	private String ppmYn		= "";
	private String ppmSubYn		= "";
	
	private int dataSubChk		= 0;
	
	private String svodProdId	= "";
	private String ppmProdId	= "";
	private String currentDate	= "";
	
	private String customProperty = "";
	private String concurrentCnt = "";

	public GetFXContStatRequestVO(){}
	
	public GetFXContStatRequestVO(String szParam){
		CommonService commonService = new CommonService();
		
		String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("album_id") == -1  )
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
				if(key.toLowerCase().equals("album_id"))		this.albumId	= value;
				if(key.toLowerCase().equals("app_type"))		this.appType	= value;
				if(key.toLowerCase().equals("is_uhd"))			this.isUhd		= value;
				if(key.toLowerCase().equals("cat_id"))			this.catId		= value;
			}
		}
		
		//GetFXContStatController.saId = paramMap.get("sa_id");
		//GetFXContStatController.stbMac = paramMap.get("stb_mac");	
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
				|| paramMap.get("album_id") == null  )
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.saId, 7, 12, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.stbMac, 14, 14, 1))
		{
			throw new ImcsException();
		}
		
		if( !"".equals(this.saId) ){
			this.cIdxSa = this.saId.substring(this.saId.length()-2, this.saId.length());
			try {
				this.pIdxSa = Integer.parseInt(this.cIdxSa) % 33;
			} catch (Exception e) {
				this.pIdxSa = 0;
			}
			
		}
		
		this.appType	= StringUtil.replaceNull(this.appType, "RUTL");
		this.isUhd		= StringUtil.replaceNull(this.isUhd, "N");
		
		if("".equals(this.saId) && "".equals(this.stbMac))
			new ImcsException();
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

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getIsUhd() {
		return isUhd;
	}

	public void setIsUhd(String isUhd) {
		this.isUhd = isUhd;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getCatGb() {
		return catGb;
	}

	public void setCatGb(String catGb) {
		this.catGb = catGb;
	}

	public int getpIdxSa() {
		return pIdxSa;
	}

	public void setpIdxSa(int pIdxSa) {
		this.pIdxSa = pIdxSa;
	}

	public String getcIdxSa() {
		return cIdxSa;
	}

	public void setcIdxSa(String cIdxSa) {
		this.cIdxSa = cIdxSa;
	}

	public long getTp_start() {
		return tp_start;
	}

	public void setTp_start(long tp_start) {
		this.tp_start = tp_start;
	}

	public int getResultSet() {
		return resultSet;
	}

	public void setResultSet(int resultSet) {
		this.resultSet = resultSet;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getPremiumYn() {
		return premiumYn;
	}

	public void setPremiumYn(String premiumYn) {
		this.premiumYn = premiumYn;
	}

	public String getSvodProdBuyYn() {
		return svodProdBuyYn;
	}

	public void setSvodProdBuyYn(String svodProdBuyYn) {
		this.svodProdBuyYn = svodProdBuyYn;
	}

	public String getTestSbc() {
		return testSbc;
	}

	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}

	public String getPpmYn() {
		return ppmYn;
	}

	public void setPpmYn(String ppmYn) {
		this.ppmYn = ppmYn;
	}

	public String getShortYn() {
		return shortYn;
	}

	public void setShortYn(String shortYn) {
		this.shortYn = shortYn;
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

	public String getContsId() {
		return contsId;
	}

	public void setContsId(String contsId) {
		this.contsId = contsId;
	}

	public String getContsType() {
		return contsType;
	}

	public void setContsType(String contsType) {
		this.contsType = contsType;
	}

	public int getDataSubChk() {
		return dataSubChk;
	}

	public void setDataSubChk(int dataSubChk) {
		this.dataSubChk = dataSubChk;
	}

	public String getSvodProdId() {
		return svodProdId;
	}

	public void setSvodProdId(String svodProdId) {
		this.svodProdId = svodProdId;
	}

	public String getPpmProdId() {
		return ppmProdId;
	}

	public void setPpmProdId(String ppmProdId) {
		this.ppmProdId = ppmProdId;
	}

	public String getSvodOnly() {
		return svodOnly;
	}

	public void setSvodOnly(String svodOnly) {
		this.svodOnly = svodOnly;
	}

	public String getPpmSubYn() {
		return ppmSubYn;
	}

	public void setPpmSubYn(String ppmSubYn) {
		this.ppmSubYn = ppmSubYn;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public String getCustomProperty() {
		return customProperty;
	}

	public void setCustomProperty(String customProperty) {
		this.customProperty = customProperty;
	}

	public String getConcurrentCnt() {
		return concurrentCnt;
	}

	public void setConcurrentCnt(String concurrentCnt) {
		this.concurrentCnt = concurrentCnt;
	}

	
}
