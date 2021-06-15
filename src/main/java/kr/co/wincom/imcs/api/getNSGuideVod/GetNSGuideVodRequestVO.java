package kr.co.wincom.imcs.api.getNSGuideVod;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;


@SuppressWarnings("serial")
public class GetNSGuideVodRequestVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSGuideVod API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			= "";	// [12] 가입자정보
	private String stbMac		= "";	// [14] 가입자 STB MAC Address
	private String definFlag	= "";	// [ 2] 사용자 화질 가능 FLAG (1:4M, 2:2M)
	private String youthYn		= "";	// [ 1] 청소년 요금제 여부
	private String baseGb		= "";	// [ 1] 기지국 코드구분 (Y/N)
	private String baseCd		= "";	// [50] 기지국 코드 (ex. W123.456.789)
	
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String highLow		= "";	//
	private String youthYnCom	= "";	//
	private String baseOneCd	= "";	//
	private String baseCondi	= "";	//
	
	private String pid			= "";
	private String resultCode	= "";
	
	
	public GetNSGuideVodRequestVO(){}
	
	public GetNSGuideVodRequestVO(String szParam){
		String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("defin_flag") == -1  )
		{
			throw new ImcsException();
		}*/
		
		//단말에서 defin_flag를 두번 넣는 경우가 있음
		int definFlagCnt = 0;
		
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
				
				if(key.toLowerCase().equals("defin_flag") && definFlagCnt == 0) {
					definFlagCnt ++;
					if(key.toLowerCase().equals("defin_flag"))	this.setDefinFlag(value);
				} else {
					if(key.toLowerCase().equals("defin_flag"))	this.setBaseGb(value);
				}
				
				if(key.toLowerCase().equals("sa_id"))		this.setSaId(value);
				if(key.toLowerCase().equals("stb_mac"))		this.setStbMac(value);
				if(key.toLowerCase().equals("youth_yn"))	this.setYouthYn(value);
				//if(key.toLowerCase().equals("base_gb"))		this.setBaseGb(value);
				if(key.toLowerCase().equals("base_cd"))		this.setBaseCd(value);
			}
		}
		
		//GetNSGuideVodController.saId = paramMap.get("sa_id");
		//GetNSGuideVodController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null 
				|| paramMap.get("defin_flag") == null )
		{
			throw new ImcsException();
		}
		
		/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
			throw new ImcsException();
		}*/
		
		if(this.definFlag.length() >= 1) {
			if("1".equals(this.definFlag.subSequence(0, 1)))		this.highLow = "H";
			else if("2".equals(this.definFlag.subSequence(0, 1)))	this.highLow = "L";
		}
		
		this.youthYn = StringUtil.replaceNull(this.youthYn, "N");
		this.baseGb = StringUtil.replaceNull(this.baseGb, "N");
		
		/*switch (youthYn) {
		case "Y":
		case "N":
			break;

		default:
			throw new ImcsException();
		}
		
		switch (baseGb) {
		case "Y":
		case "N":
			break;

		default:
			throw new ImcsException();
		}*/
		
		if("Y".equals(this.youthYn))	this.youthYnCom = "1";
		else							this.youthYnCom	= "0";
		
		this.baseCd = StringUtil.replaceNull(this.baseCd, "?");
		this.baseOneCd = this.baseCd.substring(0, 1);
	}
	
	

	public String getSaId() {
		return saId;
	}

	public void setSaId(String saId) {
		if(saId == null || saId.equalsIgnoreCase("null")) saId = "";
		this.saId = saId;
	}

	public String getStbMac() {
		return stbMac;
	}

	public void setStbMac(String stbMac) {
		if(stbMac == null || stbMac.equalsIgnoreCase("null")) stbMac = "";
		this.stbMac = stbMac;
	}

	public String getDefinFlag() {
		return definFlag;
	}

	public void setDefinFlag(String definFlag) {
		this.definFlag = definFlag;
	}

	public String getYouthYn() {
		return youthYn;
	}

	public void setYouthYn(String youthYn) {
		this.youthYn = youthYn;
	}

	public String getBaseGb() {
		return baseGb;
	}

	public void setBaseGb(String baseGb) {
		this.baseGb = baseGb;
	}

	public String getBaseCd() {
		return baseCd;
	}

	public void setBaseCd(String baseCd) {
		this.baseCd = baseCd;
	}

	public String getHighLow() {
		return highLow;
	}

	public void setHighLow(String highLow) {
		this.highLow = highLow;
	}
	
	public String getYouthYnCom() {
		return youthYnCom;
	}

	public void setYouthYnCom(String youthYnCom) {
		this.youthYnCom = youthYnCom;
	}
	
	public String getBaseCondi() {
		return baseCondi;
	}

	public void setBaseCondi(String baseCondi) {
		this.baseCondi = baseCondi;
	}

	public String getBaseOneCd() {
		return baseOneCd;
	}

	public void setBaseOneCd(String baseOneCd) {
		this.baseOneCd = baseOneCd;
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
}
