package kr.co.wincom.imcs.api.getNSSeriesStat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetNSSeriesStatRequestVO  extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSMultiView API 전문 칼럼(순서 일치)
	********************************************************************/
    private String saId		= "";	// 가입자 정보
    private String stbMac	= "";	// 가입자 MAC ADDRESS
    private String multiAlbumId	= "";	// 앨범 ID들
    private String rqsType = "";

    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String pid			= "";
    private String resultCode	= "";
    private long tp_start = 0;
    private String albumId	= "";
    private String price = "";
    private Integer resultSet = 0;
    private String[] albumList;
    private List<String> arrAlbumList;
    private List<String> arrSubcriptionList;
    private List<String> arrBuyList;
    private List<String> arrLinkTime;
    
    private String subcriptionList = "";
    private String buyList	= "";	
    
    private String nsaId		= "";
    private String nstbMac		= "";
    private String testSbc 		= "";
    
    public GetNSSeriesStatRequestVO(){}
    
    public GetNSSeriesStatRequestVO(String szParam){
    	CommonService commonService = new CommonService();
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
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
				if(key.toLowerCase().equals("multi_album_id"))		this.multiAlbumId = value;
				if(key.toLowerCase().equals("rqs_type"))		this.rqsType = value;
			}
		}
		
		if(  paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null || paramMap.get("multi_album_id") == null )
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

		if(!commonService.getValidParam(this.multiAlbumId, 15, 479, 1))
		{
			throw new ImcsException();
		}
		
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

	public String getMulitAlbumId() {
		return multiAlbumId;
	}

	public void setMulitAlbumId(String multiAlbumId) {
		this.multiAlbumId = multiAlbumId;
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

	public Integer getResultSet() {
		return resultSet;
	}

	public void setResultSet(Integer resultSet) {
		this.resultSet = resultSet;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public List<String> getArrAlbumList() {
		return arrAlbumList;
	}

	public void setArrAlbumList(List<String> arrAlbumList) {
		this.arrAlbumList = arrAlbumList;
	}

	public String[] getAlbumList() {
		return albumList;
	}

	public void setAlbumList(String[] albumList) {
		this.albumList = albumList;
	}

	public String getRqsType() {
		return rqsType;
	}

	public void setRqsType(String rqsType) {
		this.rqsType = rqsType;
	}

	public String getTestSbc() {
		return testSbc;
	}

	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}

	public String getNsaId() {
		return nsaId;
	}

	public void setNsaId(String nsaId) {
		this.nsaId = nsaId;
	}

	public String getNstbMac() {
		return nstbMac;
	}

	public void setNstbMac(String nstbMac) {
		this.nstbMac = nstbMac;
	}

	public List<String> getArrSubcriptionList() {
		return arrSubcriptionList;
	}

	public void setArrSubcriptionList(List<String> arrSubcriptionList) {
		this.arrSubcriptionList = arrSubcriptionList;
	}

	public List<String> getArrBuyList() {
		return arrBuyList;
	}

	public void setArrBuyList(List<String> arrBuyList) {
		this.arrBuyList = arrBuyList;
	}

	public String getSubcriptionList() {
		return subcriptionList;
	}

	public void setSubcriptionList(String subcriptionList) {
		this.subcriptionList = subcriptionList;
	}

	public String getBuyList() {
		return buyList;
	}

	public void setBuyList(String buyList) {
		this.buyList = buyList;
	}

	public List<String> getArrLinkTime() {
		return arrLinkTime;
	}

	public void setArrLinkTime(List<String> arrLinkTime) {
		this.arrLinkTime = arrLinkTime;
	}


	
	
}
