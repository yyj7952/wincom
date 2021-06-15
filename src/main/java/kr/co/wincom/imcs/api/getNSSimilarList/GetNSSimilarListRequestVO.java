package kr.co.wincom.imcs.api.getNSSimilarList;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;


@SuppressWarnings("serial")
public class GetNSSimilarListRequestVO  extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSSimilarList API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			= "";		// 가입자 정보
    private String stbMac		= "";		// 가입자 MAC_ADDRESS
    private String catId		= "";		// 카테고리 ID
    private String definFlag	= "";		// 사용자화질설정 FLAG
    private String nscType		= "";		// N스크린 단말타입
    private String albumId		= "";		// 앨범 ID
    
    
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String pid			= "";
    private String resultCode	= "";
    
    private long tp_start = 0;
    private Integer resultSet	= 0;
    
    private String quickDisYn	= "";
    private String viewFlag1	= "";
    private String viewFlag2	= "";
    private String posterType	= "";
    private String testSbc		= "";
    private String similarAlbumId	= "";	// 비슷한 영화의 앨범ID

    
    public GetNSSimilarListRequestVO(){}
    
    public GetNSSimilarListRequestVO(String szParam){
		String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("cat_id") == -1 || szTemp.indexOf("defin_flag") == -1 
				|| szTemp.indexOf("nsc_type") == -1 || szTemp.indexOf("album_id") == -1
				|| szTemp.indexOf("buying_price") == -1 || szTemp.indexOf("buying_type") == -1 )
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
				if(key.toLowerCase().equals("cat_id"))			this.catId		= value;
				if(key.toLowerCase().equals("defin_flag"))		this.definFlag	= value;
				if(key.toLowerCase().equals("nsc_type"))		this.nscType	= value;
				if(key.toLowerCase().equals("album_id"))		this.albumId	= value;
			}
		}
		
		//GetNSSimilarListController.saId = paramMap.get("sa_id");
		//GetNSSimilarListController.stbMac = paramMap.get("stb_mac");
		
		/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
			throw new ImcsException();
		}*/
		
		this.nscType	= StringUtil.replaceNull(this.nscType, "LTE");
		
		/*switch (nscType) {
		case "LTE":
		case "PAD":
		case "PAW":
		case "PAH":
			break;

		default:
			throw new ImcsException();
		}*/
		
		
	    if("LTE".equals(this.nscType))   		this.setPosterType("P");
	    else if("PAD".equals(this.nscType))   	this.setPosterType("D");
	    else if("PAH".equals(this.nscType))		this.setPosterType("W");
	    
	    if("Y".equals(this.getQuickDisYn()))   	this.setViewFlag1("P");
	    else							    	this.setViewFlag1("V");
	    
	    if(StringUtil.nullToSpace(this.saId).equals("") || StringUtil.nullToSpace(this.stbMac).equals("") 
	    		|| this.catId.indexOf("undef") != -1 ){
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

	public String getNscType() {
		return nscType;
	}

	public void setNscType(String nscType) {
		this.nscType = nscType;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
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

	public String getPosterType() {
		return posterType;
	}

	public void setPosterType(String posterType) {
		this.posterType = posterType;
	}

	public String getTestSbc() {
		return testSbc;
	}

	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}

	public Integer getResultSet() {
		return resultSet;
	}

	public void setResultSet(Integer resultSet) {
		this.resultSet = resultSet;
	}

	public String getSimilarAlbumId() {
		return similarAlbumId;
	}

	public void setSimilarAlbumId(String similarAlbumId) {
		this.similarAlbumId = similarAlbumId;
	}

}
