package kr.co.wincom.imcs.api.getFXProdInfo;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetFXProdInfoRequestVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getFXFavorList API 전문 칼럼(순서 일치)
	********************************************************************/	
	private String saId			= "";	// 사용자 아이디
	private String stbMac		= "";	// MAC_ADDRESS
	private String youthYn		= "";	// MAC_ADDRESS
	private String fxType		= "";	// 유플릭스 타입 (M:MOBILE, P:PC_WEB, T:TVG_APP, H:HDTV)
	private String orderGb		= "";	// 정렬 (N:최신, A:제목)
	
	private String uflixYn		= "";	// 연동정의서는 5번째가 orderGb이나 VTS소스에는 uflixYn임 
	
	/********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
	private String pid			= "";
	private String chnlDvCd	 	= "";
	private String nscProdKind	= "";
	private String sortYn		= "";
	private String sortGb		= "";
	private String appGb		= "";
	private String ppmType		= "";
	private String subProdId	= "";
	
	//2018.12.06 - 신규 유플릭스 상품 추가
	private String customUflix	= "";	//가입자 유플릭스 가입 FLAG ( 0 : 미가입 / 1 : 구 유플릭스 가입 / 2 : 신규 유플릭스 가입 )
	
	private long tp_start		= 0; 
	
	public GetFXProdInfoRequestVO(){}
	
	public GetFXProdInfoRequestVO(String szParam){
		CommonService commonService = new CommonService();
		
		String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1 )
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
				if(key.toLowerCase().equals("youth_yn"))		this.youthYn	= value;
				if(key.toLowerCase().equals("fx_type"))			this.fxType		= value;
				if(key.toLowerCase().equals("order_gb"))		this.orderGb	= value;
			}
		}
		
		//GetFXProdInfoController.saId = paramMap.get("sa_id");
		
		//GetFXProdInfoController.stbMac = paramMap.get("stb_mac");
		
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null )
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
		
		this.youthYn	= StringUtil.replaceNull(this.youthYn, "N");
		this.fxType		= StringUtil.replaceNull(this.fxType, "N");
		this.orderGb	= StringUtil.replaceNull(this.orderGb, "Y");
		
		/*switch (youthYn) {
		case "Y":
		case "N":
			break;

		default:
			throw new ImcsException();
		}
		
		switch (fxType) {
		case "N":
		case "M":
		case "P":
		case "T":
		case "H":
			break;

		default:
			throw new ImcsException();
		}
		*/
		switch (this.orderGb) {
		case "Y":
		case "U":
		case "H":
			break;

		default:
			throw new ImcsException();
		}
		
		this.sortYn			= "N";
		
		if("N".equals(this.fxType)) {
			this.chnlDvCd		= "";
		} else if("M".equals(this.fxType)) {
			this.chnlDvCd		= "MOBILE";
			this.nscProdKind	= "1";
			this.sortYn			= "Y";
		} else if("P".equals(this.fxType)) {
			this.chnlDvCd		= "HDTV";
			this.nscProdKind	= "1";
		} else if("T".equals(this.fxType)) {
			this.chnlDvCd		= "IPTV";
			this.nscProdKind	= "0";
		} else if("H".equals(this.fxType)) {
			this.chnlDvCd		= "MOBILE";
			this.nscProdKind	= "1";
			this.sortYn			= "Y";
		}
		
		if("Y".equals(this.orderGb))	this.sortGb	= "U";
		else							this.sortGb	= "H";
		
		this.appGb		= "NSC";
		this.ppmType	= "NN";
		
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

	public String getYouthYn() {
		return youthYn;
	}

	public void setYouthYn(String youthYn) {
		this.youthYn = youthYn;
	}

	public String getFxType() {
		return fxType;
	}

	public void setFxType(String fxType) {
		this.fxType = fxType;
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

	public String getUflixYn() {
		return uflixYn;
	}

	public void setUflixYn(String uflixYn) {
		this.uflixYn = uflixYn;
	}

	public String getChnlDvCd() {
		return chnlDvCd;
	}

	public void setChnlDvCd(String chnlDvCd) {
		this.chnlDvCd = chnlDvCd;
	}

	public String getNscProdKind() {
		return nscProdKind;
	}

	public void setNscProdKind(String nscProdKind) {
		this.nscProdKind = nscProdKind;
	}

	public String getSortYn() {
		return sortYn;
	}

	public void setSortYn(String sortYn) {
		this.sortYn = sortYn;
	}

	public String getSortGb() {
		return sortGb;
	}

	public void setSortGb(String sortGb) {
		this.sortGb = sortGb;
	}

	public String getAppGb() {
		return appGb;
	}

	public void setAppGb(String appGb) {
		this.appGb = appGb;
	}

	public String getPpmType() {
		return ppmType;
	}

	public void setPpmType(String ppmType) {
		this.ppmType = ppmType;
	}

	public String getSubProdId() {
		return subProdId;
	}

	public void setSubProdId(String subProdId) {
		this.subProdId = subProdId;
	}

	public long getTp_start() {
		return tp_start;
	}

	public void setTp_start(long tp_start) {
		this.tp_start = tp_start;
	}

	public String getCustomUflix() {
		return customUflix;
	}

	public void setCustomUflix(String customUflix) {
		this.customUflix = customUflix;
	}
		
}
