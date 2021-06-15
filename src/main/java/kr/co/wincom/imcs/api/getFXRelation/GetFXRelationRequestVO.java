package kr.co.wincom.imcs.api.getFXRelation;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetFXRelationRequestVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getFXRelation API 전문 칼럼(순서 일치)
	********************************************************************/	
	private String saId			= "";	// 사용자 아이디
	private String stbMac		= "";	// MAC_ADDRESS
	private String catId		= "";
	private String albumId		= "";
	private String prInfo		= "";	// RATING
	private String fxType		= "";
	private String quickDisYn	= "";	// 퀵배포 컨텐츠 포함 여부
	
	/********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
	private String pid			= "";
	private String resultCode	= "";

	private String posterType	= "";
	private String catGb		= "";
	private String tempCatGb	= "";	// 컨텐츠 상세 정보 조회 시에는 조회된 catGb로 조회하므로 위와는 별도
	private String viewFlag1	= "";
	private String viewFlag2	= "";
	private String adiCatId		= "";
	private String adiProdId	= "";
	private String contsId		= "";
	private String dbType		= "";
	
	public GetFXRelationRequestVO(){}
	
	public GetFXRelationRequestVO(String szParam){
		String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("cat_id") == -1 || szTemp.indexOf("album_id") == -1 
				|| szTemp.indexOf("rating") == -1 || szTemp.indexOf("fx_type") == -1 )
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
				if(key.toLowerCase().equals("cat_id"))			this.setCatId(value);
				if(key.toLowerCase().equals("album_id"))		this.setAlbumId(value);
				if(key.toLowerCase().equals("rating"))			this.setPrInfo(value);
				if(key.toLowerCase().equals("fx_type"))			this.setFxType(value);
				if(key.toLowerCase().equals("quick_dis_yn"))	this.setQuickDisYn(value);
			}
		}
		
		//GetFXRelationController.saId = paramMap.get("sa_id");
		
		//GetFXRelationController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
				|| paramMap.get("cat_id") == null || paramMap.get("album_id") == null 
				|| paramMap.get("rating") == null || paramMap.get("fx_type") == null )
		{
			throw new ImcsException();
		}
		
		/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
			throw new ImcsException();
		}	*/	
		
		this.prInfo			= StringUtil.replaceNull(this.prInfo, "07");
		if("01".equals(this.prInfo))	this.prInfo = "07";
		
		/*switch (prInfo) {
		case "01":
		case "02":
		case "03":
		case "04":
		case "05":
		case "06":
		case "07":
			break;

		default:
			throw new ImcsException();
		}*/
		
		this.quickDisYn		= StringUtil.replaceNull(this.quickDisYn, "N");
		
		
		/*switch (quickDisYn) {
		case "Y":
		case "N":
			break;

		default:
			throw new ImcsException();
		}*/
		
		if("P".equals(this.fxType) || "T".equals(this.fxType) || "M".equals(this.fxType))
				this.posterType	= "P";
		else	this.posterType	= "V";
		
		if("M".equals(this.fxType))			this.catGb	= "NSC";
		else if("H".equals(this.fxType))	this.catGb	= "NSC";
		else if("P".equals(this.fxType))	this.catGb	= "I20";
		else if("T".equals(this.fxType))	this.catGb	= "I20";
		
		if("Y".equals(this.quickDisYn))		this.viewFlag1	= "P";
		else								this.viewFlag1	= "V";
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

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getPrInfo() {
		return prInfo;
	}

	public void setPrInfo(String prInfo) {
		this.prInfo = prInfo;
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

	public String getCatGb() {
		return catGb;
	}

	public void setCatGb(String catGb) {
		this.catGb = catGb;
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

	public String getAdiCatId() {
		return adiCatId;
	}

	public void setAdiCatId(String adiCatId) {
		this.adiCatId = adiCatId;
	}

	public String getContsId() {
		return contsId;
	}

	public void setContsId(String contsId) {
		this.contsId = contsId;
	}

	public String getAdiProdId() {
		return adiProdId;
	}

	public void setAdiProdId(String adiProdId) {
		this.adiProdId = adiProdId;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getTempCatGb() {
		return tempCatGb;
	}

	public void setTempCatGb(String tempCatGb) {
		this.tempCatGb = tempCatGb;
	}
	

}
