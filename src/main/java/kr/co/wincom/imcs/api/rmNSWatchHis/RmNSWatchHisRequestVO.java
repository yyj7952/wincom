package kr.co.wincom.imcs.api.rmNSWatchHis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;


@SuppressWarnings("serial")
public class RmNSWatchHisRequestVO  extends NoSqlLoggingVO implements Serializable {
	
	/********************************************************************
	 * rmNSWatchHis API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			= "";	// 가입자정보
	private String stbMac		= "";	// 가입자 STB MAC Address
	private String contsId		= "";	// 
	
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String pIdxSa		= "";
    private String pid			= "";
    private String resultCode	= "";
	
    private ArrayList<String> contentsList	= new ArrayList<String>();
    private String contentId	= "";

    /*private Integer result_set = 0;*/
    private long tp_start = 0;    
    
    public RmNSWatchHisRequestVO(){}
    
    public RmNSWatchHisRequestVO(String szParam){
    	
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("cont_id") == -1 )
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
				
				if(key.toLowerCase().equals("sa_id"))		this.saId		= value;
				if(key.toLowerCase().equals("stb_mac"))		this.stbMac		= value;
				if(key.toLowerCase().equals("cont_id"))		this.contsId	= value;
			}
		}
		
		//RmNSWatchHisController.saId = paramMap.get("sa_id");
		
		//RmNSWatchHisController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
				|| paramMap.get("cont_id") == null )
		{
			throw new ImcsException();
		}
		
		/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
			throw new ImcsException();
		}*/
		
		if(this.contsId.indexOf("\\b") != -1){
			
			StringTokenizer tokens = new StringTokenizer( this.contsId, "\\b" );
			
			while(tokens.hasMoreElements()){
							
				String temp = tokens.nextToken();
				
				//System.out.println("temp1 : "+temp);
				
				contentsList.add(temp);
			}
			
		}else{
			
			//인코딩 해서 테스트 \b -> %08
			StringTokenizer tokens = new StringTokenizer( this.contsId, "\b" );
			
			while(tokens.hasMoreElements()){
							
				String temp = tokens.nextToken();
				
				//System.out.println("temp2 : "+temp);
				
				contentsList.add(temp);
			}
		}		
		
		if( !"".equals(this.saId) ) {
			this.setpIdxSa(this.getSaId().substring(this.getSaId().length() - 2, this.getSaId().length()));
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

	public String getContsId() {
		return contsId;
	}

	public void setContsId(String contsId) {
		this.contsId = contsId;
	}

	public String getpIdxSa() {
		return pIdxSa;
	}

	public void setpIdxSa(String pIdxSa) {
		this.pIdxSa = pIdxSa;
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

	public ArrayList<String> getContentsList() {
		return contentsList;
	}

	public void setContentsList(ArrayList<String> contentsList) {
		this.contentsList = contentsList;
	}

	public long getTp_start() {
		return tp_start;
	}

	public void setTp_start(long tp_start) {
		this.tp_start = tp_start;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	
}
