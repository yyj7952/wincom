package kr.co.wincom.imcs.api.getNSAlbumList;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetNSAlbumListRequestVO extends NoSqlLoggingVO implements Serializable {

	/********************************************************************
	 * GetNSAlbumList API 전문 칼럼(순서 일치)
	********************************************************************/	
	private String saId			= "";
	private String stbMac		= "";
	private String catId		= "";
	private String definFlag	= "";
	private String orderGb		= "";
	private String pageNo		= "";
	private String pageCnt		= "";
	private String pageIdx		= "";
	private String genreGb		= "";
	private String nscType		= "";
	private String quickDisYn	= "";
	private String fxType		= "";
	
	/********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
	private String pid			= "";
	private String resultCode	= "20000000";
	
	private String viewFlag1	= "";
	private String viewFlag2	= null;
	private String rankingStart	= "";
	private String rankingEnd	= "";
	private String posterType	= "";
	private String catType		= "";
	private String contsId		= "";			// 앨범정보 조회 시 사용
	
	
	/*private String parentCatId		= "";		// 앨범정보 조회 시 분기를 위해 사용
	private String adiAlbumId	= "";			// 카테고리정보 조회 시 사용
	
	private String checkValue	= "";			// 상품타입 조회 시 분기를 위해 사용
	*/
	
	public GetNSAlbumListRequestVO(){}
	
	public GetNSAlbumListRequestVO(String szParam){
		String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("cat_id") == -1 || szTemp.indexOf("defin_flag") == -1 
				|| szTemp.indexOf("order_gb") == -1 )
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
				if(key.toLowerCase().equals("defin_flag"))		this.setDefinFlag(value);
				if(key.toLowerCase().equals("order_gb"))		this.setOrderGb(value);
				if(key.toLowerCase().equals("page_no"))			this.setPageNo(value);
				if(key.toLowerCase().equals("page_cnt"))		this.setPageCnt(value);
				if(key.toLowerCase().equals("page_idx"))		this.setPageIdx(value);
				if(key.toLowerCase().equals("genre_gb"))		this.setGenreGb(value);
				if(key.toLowerCase().equals("nsc_type"))		this.setNscType(value);
				if(key.toLowerCase().equals("quick_dis_yn"))	this.setQuickDisYn(value);
				if(key.toLowerCase().equals("fx_type"))			this.setFxType(value);
			}
		}
		
		//GetNSAlbumListController.saId = paramMap.get("sa_id");
		//GetNSAlbumListController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null 
				|| paramMap.get("cat_id") == null || paramMap.get("defin_flag") == null
				|| paramMap.get("order_gb") == null )
		{
			throw new ImcsException();
		}
		
		/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
			throw new ImcsException();
		}*/
	
		
		if(this.definFlag == null || this.definFlag.equals(""))			this.definFlag	= "1";
		if(this.orderGb == null || this.orderGb.equals(""))				this.orderGb	= "N";
		if(this.pageNo == null || this.pageNo.equals(""))				this.pageNo		= "0";
		if(this.pageCnt == null || this.pageCnt.equals(""))				this.pageCnt	= "0";
		if(this.pageIdx == null || this.pageIdx.equals(""))				this.pageIdx	= "0";
		if(this.genreGb == null || this.genreGb.equals(""))				this.genreGb	= "N";
		
		/*switch (orderGb) {
		case "N":
		case "H":
		case "D":
		case "S":
		case "A":
		case "P":
		case "W":
		case "R":
		case "B":
			break;

		default:
			throw new ImcsException();
		}
		
		switch (genreGb) {
		case "T":
		case "V":
		case "N":
			break;

		default:
			throw new ImcsException();
		}*/
		
		if(this.genreGb.substring(0, 1).equals("T")) {
			this.rankingStart	= "32001";
			this.rankingEnd		= "32100";
		} else {
			this.rankingStart	= "32101";
			this.rankingEnd		= "32200";
		}
			
		if(this.nscType == null || this.nscType.equals(""))				this.nscType	= "LTE";
		if(this.quickDisYn == null || this.quickDisYn.equals(""))		this.quickDisYn	= "N";
		
		
		/*switch (nscType) {
		case "LTE":
		case "PAD":
		case "PAW":
		case "PAH":
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
		
	
		// if-else 로 되어야 할 것 같은데 원VTS 버그로 보임
		if(this.nscType.equals("LTE"))									this.posterType	= "P";
		if(this.nscType.equals("PAD"))									this.posterType	= "D";
		if(this.nscType.equals("PAH"))									this.posterType	= "W";
		else															this.posterType	= "P";
		
		if(this.quickDisYn.equals("Y"))									this.viewFlag1	= "P";
		else															this.viewFlag1	= "V";
			
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

	public String getOrderGb() {
		return orderGb;
	}

	public void setOrderGb(String orderGb) {
		this.orderGb = orderGb;
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

	public String getGenreGb() {
		return genreGb;
	}

	public void setGenreGb(String genreGb) {
		this.genreGb = genreGb;
	}

	public String getNscType() {
		return nscType;
	}

	public void setNscType(String nscType) {
		this.nscType = nscType;
	}

	public String getQuickDisYn() {
		return quickDisYn;
	}

	public void setQuickDisYn(String quickDisYn) {
		this.quickDisYn = quickDisYn;
	}

	public String getFxType() {
		return fxType;
	}

	public void setFxType(String fxType) {
		this.fxType = fxType;
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

	public String getRankingStart() {
		return rankingStart;
	}

	public void setRankingStart(String rankingStart) {
		this.rankingStart = rankingStart;
	}

	public String getRankingEnd() {
		return rankingEnd;
	}

	public void setRankingEnd(String rankingEnd) {
		this.rankingEnd = rankingEnd;
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

	public String getCatType() {
		return catType;
	}

	public void setCatType(String catType) {
		this.catType = catType;
	}

	public String getContsId() {
		return contsId;
	}

	public void setContsId(String contsId) {
		this.contsId = contsId;
	}
	
	
	


}
