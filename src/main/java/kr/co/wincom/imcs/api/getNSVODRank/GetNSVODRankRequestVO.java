package kr.co.wincom.imcs.api.getNSVODRank;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;


@SuppressWarnings("serial")
public class GetNSVODRankRequestVO  extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * GetNSVODRank API 전문 칼럼(순서 일치)
	********************************************************************/
    private String saId			= "";	// 가입자 정보
    private String stbMac		= "";	// 가입자 MAC ADDRESS
    private String genreCd		= "";	// 장르코드
    private String seriesId		= "";	// 시리즈 Id
    private String definFlag	= "";	// 사용자화질가능 FLAG
    private String contsGb		= "";	// 컨텐츠구분 (PAD, LTE, PAT)
    private String pageNo		= "";	// 페이지 넘버
    private String pageCnt		= "";	// 페이지 개수
    private String youthYn		= "";	// 청소년요금제 여부
    private String quickDisYn	= "";	// 퀵배포 컨텐츠 포함여부
    private String genreOne		= "";	// 큐빅의 대장르
    private String genreTwo		= "";	// 큐빅의 중장르
    
    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String pid			= "";
    private String resultCode	= "";

    private String viewFlag1	= "";
    private String viewFlag2	= "";
    private String nscGb		= "";
    private String imgUrl		= "";
    private String contsId		= "";
    
    private long tp_start = 0;
    
    
    
    public GetNSVODRankRequestVO(){}
    
    public GetNSVODRankRequestVO(String szParam){
    	
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("genre_cd") == -1 || szTemp.indexOf("series_id") == -1 
				|| szTemp.indexOf("defin_flag") == -1 || szTemp.indexOf("conts_gb") == -1 )
		{
			throw new ImcsException();
		}*/
		
		for(int i = 0; i < arrStat.length; i++){
			nStr	= arrStat[i].indexOf("=");
			
			if(nStr > 0) {
				key = arrStat[i].substring(0, nStr).toLowerCase().trim();
				value = arrStat[i].substring(nStr + 1, arrStat[i].length()).trim();
				
				paramMap.put(key, value);
				
//				if( !value.matches(ImcsConstants.N_SP_PTN) && key.toLowerCase().indexOf("name") == -1 ){
//					//특수문자 있음
//					throw new ImcsException();
//				}
				
				if(key.toLowerCase().equals("sa_id"))			this.saId = value;
				if(key.toLowerCase().equals("stb_mac"))			this.stbMac = value;
				if(key.toLowerCase().equals("genre_cd"))		this.genreCd = value;
				if(key.toLowerCase().equals("series_id"))		this.seriesId = value;
				if(key.toLowerCase().equals("defin_flag"))		this.definFlag = value;
				if(key.toLowerCase().equals("conts_gb"))		this.contsGb = value;
				if(key.toLowerCase().equals("page_no"))			this.pageNo = value;
				if(key.toLowerCase().equals("page_cnt"))		this.pageCnt = value;
				if(key.toLowerCase().equals("youth_yn"))		this.youthYn = value;
				if(key.toLowerCase().equals("quick_dis_yn"))	this.quickDisYn = value;
				if(key.toLowerCase().equals("genre_one"))		this.genreOne = value;
				if(key.toLowerCase().equals("genre_two"))		this.genreTwo = value;
			}
		}
		
		//GetNSVODRankController.saId = paramMap.get("sa_id");
		
		//GetNSVODRankController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null 
				|| paramMap.get("genre_cd") == null || paramMap.get("series_id") == null
				|| paramMap.get("defin_flag") == null || paramMap.get("conts_gb") == null)
		{
			throw new ImcsException();
		}
		
		/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
			throw new ImcsException();
		}*/
		
		this.pageNo = StringUtil.replaceNull(this.pageNo, "");
		this.pageCnt = StringUtil.replaceNull(this.pageCnt, "");
		this.youthYn = StringUtil.replaceNull(this.youthYn, "N");
		this.quickDisYn = StringUtil.replaceNull(this.quickDisYn, "N");
		
		/*switch (youthYn) {
		case "Y":
		case "N":
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
		
		
		if("PAD".equals(this.contsGb))	this.nscGb = "W";
		else if("LTE".equals(this.contsGb))	this.nscGb = "P";
		else if("PAH".equals(this.contsGb))	this.nscGb = "D";
		else if("PAT".equals(this.contsGb))	this.nscGb = "T";

		
		if("Y".equals(this.quickDisYn))	this.viewFlag1="P";
		else							this.viewFlag1="V";
		
		this.definFlag = StringUtil.replaceNull(this.definFlag, "3");
		this.genreOne = StringUtil.replaceNull(this.genreOne, "A");
		this.genreTwo = StringUtil.replaceNull(this.genreTwo, "A");
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

	public String getGenreCd() {
		return genreCd;
	}

	public void setGenreCd(String genreCd) {
		this.genreCd = genreCd;
	}

	public String getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(String seriesId) {
		this.seriesId = seriesId;
	}

	public String getDefinFlag() {
		return definFlag;
	}

	public void setDefinFlag(String definFlag) {
		this.definFlag = definFlag;
	}

	public String getContsGb() {
		return contsGb;
	}

	public void setContsGb(String contsGb) {
		this.contsGb = contsGb;
	}

	public String getNscGb() {
		return nscGb;
	}

	public void setNscGb(String nscGb) {
		this.nscGb = nscGb;
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

	public String getYouthYn() {
		return youthYn;
	}

	public void setYouthYn(String youthYn) {
		this.youthYn = youthYn;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getQuickDisYn() {
		return quickDisYn;
	}

	public void setQuickDisYn(String quickDisYn) {
		this.quickDisYn = quickDisYn;
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

	public String getGenreOne() {
		return genreOne;
	}

	public void setGenreOne(String genreOne) {
		this.genreOne = genreOne;
	}

	public String getGenreTwo() {
		return genreTwo;
	}

	public void setGenreTwo(String genreTwo) {
		this.genreTwo = genreTwo;
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

	public String getContsId() {
		return contsId;
	}

	public void setContsId(String contsId) {
		this.contsId = contsId;
	}
}
