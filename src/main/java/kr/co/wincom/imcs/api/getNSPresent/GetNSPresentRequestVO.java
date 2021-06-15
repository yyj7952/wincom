package kr.co.wincom.imcs.api.getNSPresent;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetNSPresentRequestVO  extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSPresent API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			= "";	// 가입자정보
	private String stbMac		= "";	// 가입자 STB MAC Address
	private String pageNo		= "";	// 페이지 넘버
    private String pageCnt		= "";	// 페이지 개수
    private String presentGb	= "";	// 선물함 구분 (G:받은 선물함, R:보낸 선물함)
	
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String pid			= "";
    private String resultCode	= "";
    
    private String contsGb		= "";
    private String cateGb		= "";
    private String imgUrl		= "";
    private String currentDate	= "";
    private String albumId		= "";
    
    
    
    private long tp_start = 0;
    
    public GetNSPresentRequestVO(){}
    
    public GetNSPresentRequestVO(String szParam){
    	
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
				
				if(key.toLowerCase().equals("sa_id"))			this.saId = value;
				if(key.toLowerCase().equals("stb_mac"))			this.stbMac = value;
				if(key.toLowerCase().equals("page_no"))			this.pageNo = value;
				if(key.toLowerCase().equals("page_cnt"))		this.pageCnt = value;
				if(key.toLowerCase().equals("present_gb"))		this.presentGb = value;
				
			}
		}
		
		//GetNSPresentController.saId = paramMap.get("sa_id");
		
		//GetNSPresentController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null )
		{
			throw new ImcsException();
		}
		
		/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
			throw new ImcsException();
		}*/
		
		
		if(this.pageNo == null){
			this.setPageNo("A");
		}else{
			this.setPageNo(this.pageNo);
			if("0".equals(this.pageNo) || "".equals(this.pageNo)){
				this.setPageNo("A");
			}
		}
		
		if(this.pageCnt == null){
			this.setPageCnt("A");
		}else{
			this.setPageCnt(this.pageCnt);
			if("0".equals(this.pageCnt) || "".equals(this.pageCnt)){
				this.setPageCnt("A");
			}
		}
		
		if(this.presentGb == null){
			this.setPresentGb("G");
		}else{
			this.setPresentGb(this.presentGb);
			if("".equals(this.presentGb)){
				this.setPresentGb("G");
			}
		}
		
		/*switch (presentGb) {
		case "G":
		case "R":
			break;

		default:
			throw new ImcsException();
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
	public String getContsGb() {
		return contsGb;
	}

	public void setContsGb(String contsGb) {
		this.contsGb = contsGb;
	}

	public String getCateGb() {
		return cateGb;
	}

	public void setCateGb(String cateGb) {
		this.cateGb = cateGb;
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

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getPresentGb() {
		return presentGb;
	}

	public void setPresentGb(String presentGb) {
		this.presentGb = presentGb;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
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

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}
}
