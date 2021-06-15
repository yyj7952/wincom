package kr.co.wincom.imcs.api.getNSMnuListDtl;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;


@SuppressWarnings("serial")
public class GetNSMnuListDtlRequestVO extends NoSqlLoggingVO implements Serializable {
	
	/********************************************************************
	 * GetNSMnuListDtl API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId		= ""; 
	private String stbMac	= "";
	private String catId	= "";
	private String rating	= "";
	private String orderGb	= "";
	private String pageNo	= "";
	private String pageCnt	= "";
	private String fxType	= "";
    
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String	pid				= "";
	private String	resultCode		= "";
    private Integer	resultSet		= 0;
    private String	testSbc			= "";
    private String	categoryLevel	= "";
    private String	nscGb			= "";
    private String	categoryName	= "";
    private String 	parentCategory	= "";
    private String	subVersion		= "";
    private String	subPVersion		= "";
    private String 	subPpVersion	= "";
    private String	parentVersion	= "";
    private String 	parentPVersion	= "";
    private String	parentPpVersion	= "";
    private String	version			= "";
    private String	pVersion		= "";
    private String	ppVersion		= "";
    
	//////////////////////////////////////////////////////////////////////////////
	// Constructor
	//////////////////////////////////////////////////////////////////////////////
    public GetNSMnuListDtlRequestVO(){}
	public GetNSMnuListDtlRequestVO(String szParam){
		CommonService commonService = new CommonService();
		
		String[] arrStat 	= szParam.split("\\|");
		//String key			= "";
		String key			= null;
		String value		= "";
		int nStr			= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		for(int i = 0; i < arrStat.length; i++){
			
			nStr	= arrStat[i].indexOf("=");
			if(nStr > 0) {
				key = arrStat[i].substring(0, nStr).toLowerCase().trim();
				value = arrStat[i].substring(nStr + 1, arrStat[i].length()).trim();
				
				paramMap.put(key, value);
				
				if(key.toLowerCase().equals("sa_id"))		this.saId		= value;
				if(key.toLowerCase().equals("stb_mac"))		this.stbMac		= value;
				if(key.toLowerCase().equals("cat_id"))		this.catId		= value;
				if(key.toLowerCase().equals("rating"))		this.rating		= value;
				if(key.toLowerCase().equals("order_gb"))	this.orderGb	= value;
				if(key.toLowerCase().equals("page_no"))		this.pageNo		= value;
				if(key.toLowerCase().equals("page_cnt"))	this.pageCnt	= value;
				if(key.toLowerCase().equals("fx_type"))		this.fxType		= value;
			}
		}
		
		if(!commonService.getValidParam(this.saId, 7, 12, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.stbMac, 14, 14, 1))
		{
			throw new ImcsException();
		}
		
		value = paramMap.get("cat_id");
		if(null == value )								{ throw new ImcsException(); }
		if(value.length() < 4 || value.length() > 5)	{ throw new ImcsException(); }
		
		value = paramMap.get("rating");
		if(null == value ) 								{ value = this.rating = "01"; } // rating "key"가 없으면 "01"로 설정 
		else if(value.length() > 2) 					{ throw new ImcsException(); }
		else {
			
			if( value.length() == 0) {
				value = this.rating = "01";
			}
			
			if(    !"01".equals(value) 
			    && !"02".equals(value)
			    && !"03".equals(value)
			    && !"04".equals(value)
			    && !"05".equals(value)
			    && !"06".equals(value)) { throw new ImcsException(); }
			if("01".equals(value) ) { this.rating = "07"; }
		}
		
		value = paramMap.get("order_gb");
		if(null == value) { throw new ImcsException(); }
		else if(value.length() > 1) { throw new ImcsException(); }
		else {
			
			if( value.length() == 0 ) {
				value = this.orderGb = "N";
			}
			
			switch(value ) {
				case "N":
				case "A": 
				case "P":
				case "W":
					break;
				default: throw new ImcsException();
			}
		}
		
		value = paramMap.get("page_no");
		if(null == value ) { throw new ImcsException(); }
		else if(value.length() > 5) { throw new ImcsException(); }
		else {
			if(value.length() == 0 ) { this.pageNo = "0"; }
		}
		
		value = paramMap.get("page_cnt");
		if(null == value ) { throw new ImcsException(); }
		else if (value.length() > 5) { throw new ImcsException(); }
		else {
			if(value.length() == 0 ) { this.pageCnt = "0"; }
		}
		
		value = paramMap.get("fx_type");
		if(null == value ) { throw new ImcsException(); }
		else if(value.length() > 1) { throw new ImcsException(); }
		else {
			
			if(value.length() == 0 ) {
				value = this.fxType = "H";
			}
			switch(value) {
				case "M":
				case "P":
				case "T":
				case "H": break;
				default: throw new ImcsException();
			}
		}
	    
	}// End of custom constructor.

	
	//////////////////////////////////////////////////////////////////////////////
	// Getter, Setter Methods
	//////////////////////////////////////////////////////////////////////////////
	public String getSaId() { return saId; }
	public void setSaId(String saId) { this.saId = saId; }

	public String getStbMac() { return stbMac; }
	public void setStbMac(String stbMac) { this.stbMac = stbMac; }
	
	public String getCatId() { return catId; }
	public void setCatId(String catId) { this.catId = catId; }

	public String getRating() { return rating; }
	public void setRating(String rating) { this.rating = rating; }
	
	public String getOrderGb() { return orderGb; }
	public void setOrderGb(String orderGb) { this.orderGb = orderGb; }
	
	public String getPageNo() { return pageNo; }
	public void setPageNo(String pageNo) { this.pageNo = pageNo; }
	
	public String getPageCnt() { return pageCnt; }
	public void setPageCnt(String pageCnt) { this.pageCnt = pageCnt; }
	
	public String getFxType() { return fxType; }
	public void setFxType(String fxType) { this.fxType = fxType; }
	
	
	public String getPid() { return pid; }
	public void setPid(String pid) { this.pid = pid; }
	
	public String getResultCode() { return resultCode; }
	public void setResultCode(String resultCode) { this.resultCode = resultCode; }
	
	public Integer getResultSet() { return resultSet; }
	public void setPid(Integer resultSet) { this.resultSet = resultSet; }
	
	public String getTestSbc() { return testSbc; }
	public void setTestSbc(String testSbc) { this.testSbc = testSbc; }
	
	public String getCategoryLevel() { return categoryLevel; }
	public void setCategoryLevel(String categoryLevel) { this.categoryLevel = categoryLevel; }
	
	public String getNscGb() { return nscGb; }
	public void setNscGb(String nscGb) { this.nscGb = nscGb; }
	
	public String getCategoryName() { return categoryName; }
	public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
	
	public String getParentCategory() { return parentCategory; }
	public void setParentCategory(String parentCategory) { this.parentCategory = parentCategory; }
			
	public String getSubVersion() { return subVersion; }
	public void setSubVersion(String subVersion) { this.subVersion = subVersion; }
	
	public String getSubPVersion() { return subPVersion; }
	public void setSubPVersion(String subPVersion) { this.subPVersion = subPVersion; }
	
	public String getSubPpVersion() { return subPpVersion; }
	public void setSubPpVersion(String subPpVersion) { this.subPpVersion = subPpVersion; }
	
	public String getParentVersion() { return parentVersion; }
	public void setParentVersion(String parentVersion) { this.parentVersion = parentVersion; }
	
	public String getParentPVersion() { return parentPVersion; }
	public void setParentPVersion(String parentPVersion) { this.parentPVersion = parentPVersion; }
	
	public String getParentPpVersion() { return parentPpVersion; }
	public void setParentPpVersion(String parentPpVersion) { this.parentPpVersion = parentPpVersion; }
	
	public String getVersion() { return version; }
	public void setVersion(String version) { this.version = version; }
	
	public String getPVersion() { return pVersion; }
	public void setPVersion(String pVersion) { this.pVersion = pVersion; }
	
	public String getPpVersion() { return ppVersion; }
	public void setPpVersion(String ppVersion) { this.ppVersion = ppVersion; }
	
	
//	public String get() { return ; }
//	public void set(String ) { this. = ; }
	
    
} // End of class "GetNSMnuListDtlRequestVO"
