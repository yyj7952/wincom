package kr.co.wincom.imcs.api.searchByNSStr;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class SearchByNSStrRequestVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSGuideVod API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			= "";	// 가입자정보
	private String stbMac		= "";	// 가입자 STB MAC Address
	private String keyWord		= "";	// 검색문자
    private String catId		= "";	// 카테고리 ID
    private String pageNo		= "";	// 페이지 넘버
    private String pageCnt		= "";	// 페이지 개수
    private String definFlag	= "";	// 사용자 화질가능 FLAG (1:HD, 2:SD)
    private String youthYn		= "";	// 청소년 요금제여부
	
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String pid		= "";
    private String resultCode	= "";
    
    private String youthYnCom = "";
    
    private long tp_start = 0;
    
    public SearchByNSStrRequestVO(){}
    
    public SearchByNSStrRequestVO(String szParam){
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("key_word") == -1 || szTemp.indexOf("cat_id") == -1 
				|| szTemp.indexOf("page_no") == -1 || szTemp.indexOf("page_cnt") == -1
				|| szTemp.indexOf("defin_flag") == -1 )
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
				if(key.toLowerCase().equals("key_word"))		this.keyWord = value;
				if(key.toLowerCase().equals("cat_id"))			this.catId = value;
				if(key.toLowerCase().equals("page_no"))			this.pageNo = value;
				if(key.toLowerCase().equals("page_cnt"))		this.pageCnt = value;
				if(key.toLowerCase().equals("defin_flag"))		this.definFlag = value;
				if(key.toLowerCase().equals("youth_yn"))		this.youthYn = value;
				
			}
		}
		
		//SearchByNSStrController.saId = paramMap.get("sa_id");
		//SearchByNSStrController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id")== null || paramMap.get("stb_mac") == null
				|| paramMap.get("key_word") == null || paramMap.get("cat_id") == null 
				|| paramMap.get("page_no") == null || paramMap.get("page_cnt") == null
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
		
		if( !"".equals(this.catId) && this.catId.length() > 0){
			if("%".equals(catId))	this.catId	= "VC";
		}
		
		this.youthYn	= StringUtil.replaceNull(this.youthYn, "N");
		
		/*switch (youthYn) {
		case "Y":
		case "N":
			break;

		default:
			throw new ImcsException();
		}*/
		
		if("Y".equals(this.youthYn))	this.youthYnCom = "1";
		else							this.youthYnCom = "0";
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

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
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

	public String getYouthYnCom() {
		return youthYnCom;
	}

	public void setYouthYnCom(String youthYnCom) {
		this.youthYnCom = youthYnCom;
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

	public long getTp_start() {
		return tp_start;
	}

	public void setTp_start(long tp_start) {
		this.tp_start = tp_start;
	}
}
