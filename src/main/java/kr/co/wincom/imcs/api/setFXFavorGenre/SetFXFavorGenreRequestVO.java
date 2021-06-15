package kr.co.wincom.imcs.api.setFXFavorGenre;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class SetFXFavorGenreRequestVO  extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * SetFXFavorGenre API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			= "";	// 가입자정보
	private String stbMac		= "";	// 가입자 STB MAC Address
	private String catId		= "";
	private String fxType		= "";
	
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private ArrayList<String> catList	= new ArrayList<String>();
	private String catInputId	= "";
	private String pid			= "";
	private String favCnt		= "";
    private Integer resultSet = 0;
    
    private String api          = "fxvod010";
    private String dml_gb       = "";
    private String binding1     = "";
    private String binding2     = "";
    
    public SetFXFavorGenreRequestVO(){}
    
    public SetFXFavorGenreRequestVO(String szParam){
    	
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("cat_id") == -1 )
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
				
				if(key.toLowerCase().equals("sa_id"))		this.saId = value;
				if(key.toLowerCase().equals("stb_mac"))		this.stbMac = value;
				if(key.toLowerCase().equals("cat_id"))		this.catId = value;
				if(key.toLowerCase().equals("fx_type"))		this.fxType = value;
			}
		}
		
		//SetFXFavorGenreController.saId = paramMap.get("sa_id");
		//SetFXFavorGenreController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null 
				|| paramMap.get("cat_id") == null )
		{
			throw new ImcsException();
		}
		
		/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
			throw new ImcsException();
		}*/
		
		if(this.catId.indexOf(ImcsConstants.ARRSEP) != -1){
						
			StringTokenizer tokens = new StringTokenizer( this.catId, ImcsConstants.ARRSEP );
			
			while(tokens.hasMoreElements()){
							
				String temp = tokens.nextToken();
				
				//System.out.println("temp1 : "+temp);
				
				catList.add(temp);
			}
			
		}else{
			
			//인코딩 해서 테스트 \b -> %08
//			StringTokenizer tokens = new StringTokenizer( this.catId, "%08" );
//			
//			while(tokens.hasMoreElements()){
//							
//				String temp = tokens.nextToken();
//				
//				System.out.println("#############temp2 : "+temp);
//				
//				catList.add(temp);
//			}
			
			String [] arrCatid = this.catId.split("%08");
			
			for (int i=0; i<arrCatid.length; i++) {
				String temp = arrCatid[i]; 
				//System.out.println("#############temp2 : "+temp);
				catList.add(temp);
			}
			
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

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
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

	public ArrayList<String> getCatList() {
		return catList;
	}

	public void setCatList(ArrayList<String> catList) {
		this.catList = catList;
	}

	public Integer getResultSet() {
		return resultSet;
	}

	public void setResultSet(Integer resultSet) {
		this.resultSet = resultSet;
	}

	public String getFavCnt() {
		return favCnt;
	}

	public void setFavCnt(String favCnt) {
		this.favCnt = favCnt;
	}

	public String getCatInputId() {
		return catInputId;
	}

	public void setCatInputId(String catInputId) {
		this.catInputId = catInputId;
	}

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public String getDml_gb() {
		return dml_gb;
	}

	public void setDml_gb(String dml_gb) {
		this.dml_gb = dml_gb;
	}

	public String getBinding1() {
		return binding1;
	}

	public void setBinding1(String binding1) {
		this.binding1 = binding1;
	}

	public String getBinding2() {
		return binding2;
	}

	public void setBinding2(String binding2) {
		this.binding2 = binding2;
	}

}
