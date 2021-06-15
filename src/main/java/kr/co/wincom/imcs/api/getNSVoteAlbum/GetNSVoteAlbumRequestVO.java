package kr.co.wincom.imcs.api.getNSVoteAlbum;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetNSVoteAlbumRequestVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSMusicList API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			  = "";	// 가입자정보
	private String stbMac		  = "";	// 가입자 STB MAC Address
	private String voteId		  = "";
	
	private String testSbc1		  = "";
	private String testSbc2		  = "";
   
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	
    private String pid			= "";
    private String resultCode	= "";
    private long tp_start 		= 0;
    
    public GetNSVoteAlbumRequestVO(){}
    
    public GetNSVoteAlbumRequestVO(String szParam){
    	
    	
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
				
				if(key.toLowerCase().equals("sa_id"))		this.saId    = value; 
				if(key.toLowerCase().equals("stb_mac"))		this.stbMac  = value;
				if(key.toLowerCase().equals("vote_id"))		this.voteId  = value;
			}
		}
		//가번이 없거나 길이가 맞지 않으면 안됨. 맥번호가 없거나 길이가 맞지 않으면 안됨.
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null || paramMap.get("vote_id") == null)
		{
			throw new ImcsException();
		}
		
		if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() != 14 ){
			throw new ImcsException();
		}
		
		if(this.voteId.length() > 20 || this.voteId.length() < 1){
			throw new ImcsException();
		}
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

	public String getVoteId() {
		return voteId;
	}

	public void setVoteId(String voteId) {
		this.voteId = voteId;
	}

	public String getTestSbc1() {
		return testSbc1;
	}

	public void setTestSbc1(String testSbc1) {
		this.testSbc1 = testSbc1;
	}

	public String getTestSbc2() {
		return testSbc2;
	}

	public void setTestSbc2(String testSbc2) {
		this.testSbc2 = testSbc2;
	}

    
}
