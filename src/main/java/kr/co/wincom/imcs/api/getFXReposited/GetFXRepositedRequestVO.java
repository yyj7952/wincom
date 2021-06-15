package kr.co.wincom.imcs.api.getFXReposited;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetFXRepositedRequestVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * GetFXReposited API 전문 칼럼(순서 일치)
	********************************************************************/	
	private String saId			= "";	// 사용자 아이디
	private String stbMac		= "";	// MAC_ADDRESS
	private String fxType		= "";
	private String pageNo		= "";
	private String pageCnt		= "";
	private String orderGb		= "";
	private String quickDisYn	= "";	// 퀵배포 컨텐츠 포함 여부
	private String rGrade		= "";	
	
	//2019.01.18 - 영화 월정액 개선 : 분리되어 있는 쿼리 합치기 + 19세 이상 컨텐츠 미제공 로직 구현
	private String ratingN01    = ""; // 전체 컨텐츠 제공 여부
	private String ratingN02    = ""; // R등급 컨텐츠 제공 여부
	private String ratingN03    = ""; // 19세 이상 컨텐츠 제공 여부
	
	/********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
	private String pid			= "";
	private String nscGb		= "";
	private String posterType	= "";
	private String contsId		= "";
	private String srcGb		= "";
	private Integer pIdxSa		= 0;
	
	public GetFXRepositedRequestVO(){}
	
	public GetFXRepositedRequestVO(String szParam){
		String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		//String szTemp = szParam.toLowerCase();
		
		/*if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("fx_type") == -1 )
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
				if(key.toLowerCase().equals("r_grade"))			this.rGrade		= value;
			}
		}
		
		//GetFXRepositedController.saId = paramMap.get("sa_id");
		
		//GetFXRepositedController.stbMac = paramMap.get("stb_mac");
		
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
		
		this.pageNo			= StringUtil.replaceNull(this.pageNo, "A");
		this.pageCnt		= StringUtil.replaceNull(this.pageCnt, "A");
		
		if(this.orderGb == null){
			this.orderGb		= "N";
		}else if(this.orderGb.length() == 0){
			this.orderGb 		= "X";
		}
		
		this.rGrade			= StringUtil.replaceNull(this.rGrade, "A");
		this.quickDisYn		= StringUtil.replaceNull(this.quickDisYn, "N");
		
		switch (this.rGrade) {
			case "A":
				this.ratingN01 = "99"; //99문자열 밑으로는 다 노출 - 즉, 전체 노출
				this.ratingN02 = "06"; //R등급 컨텐츠 노출
				this.ratingN03 = "05"; //19세 이상 컨텐츠 노출
				break;
			case "Y":
				this.ratingN01 = "00"; //다 비노출
				this.ratingN02 = "06"; //허나, R등급만 노출
				this.ratingN03 = "99"; //19세는 비노출
				break;
			case "N":
				this.ratingN01 = "06"; //R등급 밑으로만 노출
				this.ratingN02 = "99"; //R등급 비노출
				this.ratingN03 = "99"; //19세 비노출
				break;
			case "B":
				this.ratingN01 = "05"; //19세 밑으로만 노출
				this.ratingN02 = "99"; //R등급 비노출
				this.ratingN03 = "99"; //19세 비노출
				break;
	
			default:
				throw new ImcsException();
		}
		
		
		/*switch (orderGb) {
		case "N":
		case "A":
		case "P":
		case "W":
		case "R":
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
		
		if("P".equals(this.fxType)) {
			this.fxType	= "I20";
			this.nscGb	= "UFX";
			this.posterType	= "P";
		} else if("T".equals(this.fxType)){
			this.fxType	= "I20";
			this.nscGb	= "UFX";
			this.posterType	= "V";
		} else if("M".equals(this.fxType) || "H".equals(this.fxType)){
			this.fxType	= "NSC";
			this.nscGb	= "UFX";
			this.posterType	= "P";
		}
		
		if( !"".equals(this.saId) ) {
			try {
				this.pIdxSa = Integer.parseInt(this.getSaId().substring(this.getSaId().length() - 2, this.getSaId().length()));
				//System.out.println(this.getSaId() + "|" + this.pIdxSa);
			} catch (Exception e) {
				this.pIdxSa = 0;
			}
		}
		
		/*if( FAILURE == nReturn ) {
	        strcpy(rd1.c_order_gb, "N");
	    } else  {
	        strcpy(rd1.c_order_gb, strReturn);
	        if ( strlen(rd1.c_order_gb) == 0 ) strcpy(rd1.c_order_gb, "X");
	    }*/
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

	public String getrGrade() {
		return rGrade;
	}

	public void setrGrade(String rGrade) {
		this.rGrade = rGrade;
	}

	public String getNscGb() {
		return nscGb;
	}

	public void setNscGb(String nscGb) {
		this.nscGb = nscGb;
	}

	public String getPosterType() {
		return posterType;
	}

	public void setPosterType(String posterType) {
		this.posterType = posterType;
	}

	public Integer getpIdxSa() {
		return pIdxSa;
	}

	public void setpIdxSa(Integer pIdxSa) {
		this.pIdxSa = pIdxSa;
	}

	public String getContsId() {
		return contsId;
	}

	public void setContsId(String contsId) {
		this.contsId = contsId;
	}

	public String getSrcGb() {
		return srcGb;
	}

	public void setSrcGb(String srcGb) {
		this.srcGb = srcGb;
	}

	public String getRatingN01() {
		return ratingN01;
	}

	public void setRatingN01(String ratingN01) {
		this.ratingN01 = ratingN01;
	}

	public String getRatingN02() {
		return ratingN02;
	}

	public void setRatingN02(String ratingN02) {
		this.ratingN02 = ratingN02;
	}

	public String getRatingN03() {
		return ratingN03;
	}

	public void setRatingN03(String ratingN03) {
		this.ratingN03 = ratingN03;
	}
	
	
}
