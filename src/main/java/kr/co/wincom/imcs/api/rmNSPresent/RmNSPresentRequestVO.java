package kr.co.wincom.imcs.api.rmNSPresent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;


@SuppressWarnings("serial")
public class RmNSPresentRequestVO  extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSGuideVod API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			= "";	// 가입자정보
	private String stbMac		= "";	// 가입자 STB MAC Address
	private String albumId		= "";	// 앨범ID
	private String presentDate	= "";	// 선물받은날짜 (recv_date)
	private String presentGb	= "";	// 삭제할 선물함 구분 (G:받은선물함, R:보낸선물함)
	
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String pid		= "";
    private String resultCode = "";
	
    private Integer nResultSet = 0;
    private Integer nMessageset = 0;
    
    
	private ArrayList<String> albumList	= new ArrayList<String>();
	private String albumStr		= "";	// 파싱한 개별 앨범ID
	private ArrayList<String> presentList	= new ArrayList<String>();
	private String preDateStr	= "";	// 파싱한 개별 날짜


    private long tp_start = 0;
    
    public RmNSPresentRequestVO(){}
    
    public RmNSPresentRequestVO(String szParam){
    	
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("album_id") == -1 || szTemp.indexOf("recv_date") == -1 )
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
				if(key.toLowerCase().equals("album_id"))		this.albumId = value;
				if(key.toLowerCase().equals("recv_date"))		this.presentDate = value;
				if(key.toLowerCase().equals("present_gb"))		this.presentGb = value;
			}
		}

		//RmNSPresentController.saId = paramMap.get("sa_id");
		
		//RmNSPresentController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
				|| paramMap.get("album_id") == null || paramMap.get("recv_date") == null )
		{
			throw new ImcsException();
		}
		
		/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
			throw new ImcsException();
		}*/
		
		if(this.albumId.indexOf("\\b") != -1){
			
			StringTokenizer tokens = new StringTokenizer( this.albumId, "\\b" );
			
			while(tokens.hasMoreElements()){
							
				String temp = tokens.nextToken();
				
				//System.out.println("temp1 : "+temp);
				
				albumList.add(temp);
			}
			
		}else{
			
			//인코딩 해서 테스트 \b -> %08
			StringTokenizer tokens = new StringTokenizer( this.albumId, "\b" );
			
			while(tokens.hasMoreElements()){
							
				String temp = tokens.nextToken();
				
				//System.out.println("temp2 : "+temp);
				
				albumList.add(temp);
			}
		}
		
		if(this.presentDate.indexOf("\\b") != -1){
			
			StringTokenizer tokens = new StringTokenizer( this.presentDate, "\\b" );
			
			while(tokens.hasMoreElements()){
							
				String temp = tokens.nextToken();
				
				//System.out.println("temp1 : "+temp);
				
				presentList.add(temp);
			}
			
		}else{
			
			//인코딩 해서 테스트 \b -> %08
			StringTokenizer tokens = new StringTokenizer( this.presentDate, "\b" );
			
			while(tokens.hasMoreElements()){
							
				String temp = tokens.nextToken();
				
				//System.out.println("temp2 : "+temp);
				
				presentList.add(temp);
			}
		}		
		
		this.presentGb	= StringUtil.replaceNull(this.presentGb, "G");	// 받은선물함을 기본으로 한다
		
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

	public String getPresentDate() {
		return presentDate;
	}

	public void setPresentDate(String presentDate) {
		this.presentDate = presentDate;
	}

	public String getPresentGb() {
		return presentGb;
	}

	public void setPresentGb(String presentGb) {
		this.presentGb = presentGb;
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

	public Integer getnResultSet() {
		return nResultSet;
	}

	public void setnResultSet(Integer nResultSet) {
		this.nResultSet = nResultSet;
	}

	public Integer getnMessageset() {
		return nMessageset;
	}

	public void setnMessageset(Integer nMessageset) {
		this.nMessageset = nMessageset;
	}

	public ArrayList<String> getAlbumList() {
		return albumList;
	}

	public void setAlbumList(ArrayList<String> albumList) {
		this.albumList = albumList;
	}

	public ArrayList<String> getPresentList() {
		return presentList;
	}

	public void setPresentList(ArrayList<String> presentList) {
		this.presentList = presentList;
	}

	public String getAlbumStr() {
		return albumStr;
	}

	public void setAlbumStr(String albumStr) {
		this.albumStr = albumStr;
	}

	public String getPreDateStr() {
		return preDateStr;
	}

	public void setPreDateStr(String preDateStr) {
		this.preDateStr = preDateStr;
	}

}
