package kr.co.wincom.imcs.api.getNSMnuList2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;


@SuppressWarnings("serial")
public class GetNSMnuList2RequestVO  extends NoSqlLoggingVO implements Serializable {

	/********************************************************************
	 *  GetNSMnuList API 전문 칼럼(순서 일치)
	********************************************************************/	
	private String saId   			= "";
	private String stbMac 			= "";
	private String catId            = "";
	private String definFlag        = "";
	private String nscGb            = "";
	private String testSbc          = "";
	private String orderGb          = "";
	private String rating           = "";
	private String catSeq           = "";
	private String categoryLevel    = "";
	private String parentCategory   = "";
	private String pageNo           = "";
	private String pageCnt          = "";
	private String fxType           = "";
	private String categoryName     = "";
    
    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
	private String resultCode	    = "";
	private String param            = "";
	private String pid			    = "";
	
	// 2020.03.04 - 모바일 아이들나라
	private String levelGb 		     	= "";
	private List<String> levelGbCatId	= null;
	private int tokCnt;

	private int endCnt;
	// 2020.03.04 - 모바일 아이들나라 - level_gb가 B일 때 헤더는 별도 세팅한다.
	private int headerTotCnt;
	private int headerCatCnt;
	private int headerVodCnt;
	private String headerStillImgUrl = "";
	private String headerCatId = "";
	
	private int pagingFlag;
	
	private int albCnt = 0;
	private int catCnt = 0;
	
    private long tp_start = 0;
    
    public GetNSMnuList2RequestVO(){}
    
    public GetNSMnuList2RequestVO(String szParam){
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
				
				if(key.toLowerCase().equals("sa_id"))				this.saId             = value;
				if(key.toLowerCase().equals("stb_mac"))				this.stbMac           = value;
				if(key.toLowerCase().equals("cat_id"))				this.catId            = value;
				if(key.toLowerCase().equals("order_gb"))			this.orderGb          = value;
				if(key.toLowerCase().equals("rating"))				this.rating           = value;
				if(key.toLowerCase().equals("page_no"))				this.pageNo           = value;
				if(key.toLowerCase().equals("page_cnt"))			this.pageCnt          = value;
				if(key.toLowerCase().equals("fx_type"))				this.fxType           = value;
				if(key.toLowerCase().equals("level_gb"))			this.levelGb     	  = value;
				
			}
		}
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null )
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
		
		if(this.catId.length() > 5 || this.catId.length() < 4 ){
			throw new ImcsException();
		}
		
		// 2020.03.04 - 모바일 아이들나라
		this.levelGb = StringUtil.replaceNull(this.getLevelGb(), "N");
		if(!commonService.getValidParam(this.levelGb, 1, 1, 2))
		{
			throw new ImcsException();
		}
		
		this.rating = StringUtil.replaceNull(this.getRating(), "01");
		if (!this.rating.equals("01") && !this.rating.equals("02") && !this.rating.equals("03") 
				&& !this.rating.equals("04") && !this.rating.equals("05")) {
			throw new ImcsException();
		}
		if (this.rating.equals("01")) {
			this.rating = "07";
		}
		
		this.orderGb = StringUtil.replaceNull(this.getOrderGb(), "N");
		switch (this.orderGb) {
		case "N":
			break;
		case "A":
			break;
		case "P":
			break;
		case "W":
			break;
		default:
			throw new ImcsException();
		}
		
		this.pageNo = StringUtil.replaceNull(this.getPageNo(), "0");
		this.pageCnt = StringUtil.replaceNull(this.getPageCnt(), "0");
		this.fxType = StringUtil.replaceNull(this.getFxType(), "H");
		
		if (this.fxType.equals("M")) {
			this.fxType = "H";
		}
		
		/*switch (this.fxType) {
		case "M": 
		case "P":
		case "T":
		case "H":
			break;
		default:
			throw new ImcsException();
		}*/
		
		switch (this.levelGb) {
		case "N":
		case "B":
			break;
		default:
			this.levelGb = "N";
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

	public String getNscGb() {
		return nscGb;
	}

	public void setNscGb(String nscGb) {
		this.nscGb = nscGb;
	}

	public String getTestSbc() {
		return testSbc;
	}

	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}

	public String getOrderGb() {
		return orderGb;
	}

	public void setOrderGb(String orderGb) {
		this.orderGb = orderGb;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getCatSeq() {
		return catSeq;
	}

	public void setCatSeq(String catSeq) {
		this.catSeq = catSeq;
	}

	public String getCategoryLevel() {
		return categoryLevel;
	}

	public void setCategoryLevel(String categoryLevel) {
		this.categoryLevel = categoryLevel;
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

	public String getFxType() {
		return fxType;
	}

	public void setFxType(String fxType) {
		this.fxType = fxType;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public long getTp_start() {
		return tp_start;
	}

	public void setTp_start(long tp_start) {
		this.tp_start = tp_start;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public int getAlbCnt() {
		return albCnt;
	}

	public void setAlbCnt(int albCnt) {
		this.albCnt = albCnt;
	}

	public int getCatCnt() {
		return catCnt;
	}

	public void setCatCnt(int catCnt) {
		this.catCnt = catCnt;
	}

	public String getLevelGb() {
		return levelGb;
	}

	public void setLevelGb(String levelGb) {
		this.levelGb = levelGb;
	}
	
	public List<String> getLevelGbCatId() {
		return levelGbCatId;
	}

	public void setLevelGbCatId(List<String> levelGbCatId) {
		this.levelGbCatId = levelGbCatId;
	}

	public int getTokCnt() {
		return tokCnt;
	}

	public void setTokCnt(int tokCnt) {
		this.tokCnt = tokCnt;
	}

	public int getEndCnt() {
		return endCnt;
	}

	public void setEndCnt(int endCnt) {
		this.endCnt = endCnt;
	}

	public int getHeaderTotCnt() {
		return headerTotCnt;
	}

	public void setHeaderTotCnt(int headerTotCnt) {
		this.headerTotCnt = headerTotCnt;
	}

	public int getHeaderCatCnt() {
		return headerCatCnt;
	}

	public void setHeaderCatCnt(int headerCatCnt) {
		this.headerCatCnt = headerCatCnt;
	}

	public int getHeaderVodCnt() {
		return headerVodCnt;
	}

	public void setHeaderVodCnt(int headerVodCnt) {
		this.headerVodCnt = headerVodCnt;
	}

	public String getHeaderStillImgUrl() {
		return headerStillImgUrl;
	}

	public void setHeaderStillImgUrl(String headerStillImgUrl) {
		this.headerStillImgUrl = headerStillImgUrl;
	}

	public String getHeaderCatId() {
		return headerCatId;
	}

	public void setHeaderCatId(String headerCatId) {
		this.headerCatId = headerCatId;
	}

	public String getParentCategory() {
		return parentCategory;
	}

	public void setParentCategory(String parentCategory) {
		this.parentCategory = parentCategory;
	}

	public int getPagingFlag() {
		return pagingFlag;
	}

	public void setPagingFlag(int pagingFlag) {
		this.pagingFlag = pagingFlag;
	}
}
