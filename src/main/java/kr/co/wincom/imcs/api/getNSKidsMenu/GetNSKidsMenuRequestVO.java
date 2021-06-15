package kr.co.wincom.imcs.api.getNSKidsMenu;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

public class GetNSKidsMenuRequestVO extends NoSqlLoggingVO implements Serializable
{
	private static final long serialVersionUID = 5696612191266165970L;
	
	private String saId = "";
	private String stbMac = "";
	private String catId = "";
	private String categoryType = "";
	private String requestType = "";
	private String requestCode = "";
	private String levelGb = "";
	private String pageNo = "";
	private String pageCnt = "";
	private String orderType = "";
	private String freeYn = "";
	private String topMenuType = "";
	
	private int p_idx_sa = 0;
	private int p_stb_idx_sa = 0;
	private int category_level = 0;
	
	private String pid		= "";
	private String resultCode = "20000000";
	private String testSbc = "";
	private String viewFlag = "";
	private String stbSaId = "";
	private String stbMacAddr = "";
	private String stbPairing = "";
	
	private int iBgnNo = 0;
	private int iEndNo = 0;
	
	private String poster_url = "";
	private String still_url = "";
	private String caption_url = "";
	private String category_url = "";
	
	private int iLevel = 0;
	private String sugg_cat_id = "";
	private int iLastLevel = 0;
	private String catInfo_catId = "";
	private String temp_category_id = "";
	
	private String ppm_prod_id = "";
	private String cust_eng_level = "";
	private String guide_cat_id = "";
	private String guide_image_file = "";
	
	private String info_text = "";
	private String cat_msg_ppm1 = ""; // 에그스쿨 가입 메시지
	private String cat_msg_ppm2 = ""; // 에그스쿨 미가입 메시지
	private String cat_msg_free = ""; // 맛보기 진입 메시지
	
	public GetNSKidsMenuRequestVO(String szParam)
	{
		CommonService commonService = new CommonService();
		
		String[] arrStat = szParam.split("\\|");
		
		String key = "";
		String value = "";
		int nStr = 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		for(int i = 0; i < arrStat.length; i++)
		{
			nStr = arrStat[i].indexOf("=");
			
			if(nStr > 0)
			{
				key = arrStat[i].substring(0, nStr).toLowerCase();
				value = arrStat[i].substring(nStr + 1, arrStat[i].length());
				
				paramMap.put(key, value);
				
				if(key.toLowerCase().equals("sa_id"))			this.setSaId(value);
				if(key.toLowerCase().equals("stb_mac"))			this.setStbMac(value);
				if(key.toLowerCase().equals("cat_id"))			this.setCatId(value);
				if(key.toLowerCase().equals("category_type"))	this.setCategoryType(value);
				if(key.toLowerCase().equals("request_type"))	this.setRequestType(value);
				if(key.toLowerCase().equals("request_code"))	this.setRequestCode(value);
				if(key.toLowerCase().equals("level_gb"))		this.setLevelGb(value);
				if(key.toLowerCase().equals("page_no"))			this.setPageNo(value);
				if(key.toLowerCase().equals("page_cnt"))		this.setPageCnt(value);
				if(key.toLowerCase().equals("order_type"))		this.setOrderType(value);
				if(key.toLowerCase().equals("free_yn"))			this.setFreeYn(value);
				if(key.toLowerCase().equals("top_menu_type"))	this.setTopMenuType(value);
			}
		}
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null 
				|| paramMap.get("cat_id") == null || paramMap.get("category_type") == null
				|| paramMap.get("request_type") == null || paramMap.get("level_gb") == null
				|| paramMap.get("page_no") == null || paramMap.get("page_cnt") == null
				|| paramMap.get("order_type") == null)
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.saId, 7, 12, 1))
			throw new ImcsException();
		
		if(!commonService.getValidParam(this.stbMac, 14, 14, 1))
			throw new ImcsException();
		
		if(!commonService.getValidParam(this.catId, 3, 5, 1))
			throw new ImcsException();
		
		if(!commonService.getValidParam(this.categoryType, 1, 1, 1))
			throw new ImcsException();
		
		// 허용된 요청값 이외에는 default 값으로 처리
		if(!this.categoryType.equals("C") && !this.categoryType.equals("B")
				&& !this.categoryType.equals("T") && !this.categoryType.equals("G")
				&& !this.categoryType.equals("P") && !this.categoryType.equals("E")
				&& !this.categoryType.equals("R") && !this.categoryType.equals("H"))
		{
			this.setCategoryType("M");
		}
		
		if(!commonService.getValidParam(this.requestType, 1, 1, 1))
			throw new ImcsException();
		
		// 허용된 요청값 이외에는 default 값으로 처리
		if(!this.requestType.equals("A") && !this.requestType.equals("C") && !this.requestType.equals("L"))
			this.setRequestType("N");
		
		if(!commonService.getValidParam(this.requestCode, 0, 1, 3))
			throw new ImcsException();
		
		if(!commonService.getValidParam(this.levelGb, 1, 1, 1))
			throw new ImcsException();
		
		// 허용된 요청값 이외에는 default 값으로 처리
		if(!this.levelGb.equals("A") && !this.levelGb.equals("B"))
			this.setLevelGb("B");
		
		if(!commonService.getValidParam(this.pageNo, 1, 5, 3))
			throw new ImcsException();
		
		if(!commonService.getValidParam(this.pageCnt, 1, 5, 3))
			throw new ImcsException();
		
		if(!commonService.getValidParam(this.orderType, 1, 1, 1))
			throw new ImcsException();
		
		// 허용된 요청값 이외에는 default 값으로 처리
		if(!this.orderType.equals("N") && !this.orderType.equals("C") && 
				!this.orderType.equals("A") && !this.orderType.equals("F"))
			this.setOrderType("N");
		
		if(StringUtils.isNotBlank(this.getFreeYn()) &&
				!commonService.getValidParam(this.freeYn, 1, 1, 2))
		{
			throw new ImcsException();
		}
		
		// 허용된 요청값 이외에는 default 값으로 처리
		if(!"Y".equals(this.freeYn))
			this.setFreeYn("N");
		
		// 허용된 요청값 이외에는 default 값으로 처리
		if(this.getRequestType().equals("A"))
		{
			if(!this.topMenuType.equals("C") && !this.topMenuType.equals("B") && !this.topMenuType.equals("N"))
				this.setTopMenuType("N");
		}
		else
		{
			this.setTopMenuType("N");
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
	public String getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getRequestCode() {
		return requestCode;
	}
	public void setRequestCode(String requestCode) {
		this.requestCode = requestCode;
	}
	public String getLevelGb() {
		return levelGb;
	}
	public void setLevelGb(String levelGb) {
		this.levelGb = levelGb;
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
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
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
	public String getTestSbc() {
		return testSbc;
	}
	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}
	public String getViewFlag() {
		return viewFlag;
	}
	public void setViewFlag(String viewFlag) {
		this.viewFlag = viewFlag;
	}
	public int getP_idx_sa() {
		return p_idx_sa;
	}
	public void setP_idx_sa(int p_idx_sa) {
		this.p_idx_sa = p_idx_sa;
	}
	public int getCategory_level() {
		return category_level;
	}
	public void setCategory_level(int category_level) {
		this.category_level = category_level;
	}
	public int getiBgnNo() {
		return iBgnNo;
	}
	public void setiBgnNo(int iBgnNo) {
		this.iBgnNo = iBgnNo;
	}
	public int getiEndNo() {
		return iEndNo;
	}
	public void setiEndNo(int iEndNo) {
		this.iEndNo = iEndNo;
	}
	public String getPoster_url() {
		return poster_url;
	}
	public void setPoster_url(String poster_url) {
		this.poster_url = poster_url;
	}
	public String getStill_url() {
		return still_url;
	}
	public void setStill_url(String still_url) {
		this.still_url = still_url;
	}
	public String getCaption_url() {
		return caption_url;
	}
	public void setCaption_url(String caption_url) {
		this.caption_url = caption_url;
	}
	public String getCategory_url() {
		return category_url;
	}
	public void setCategory_url(String category_url) {
		this.category_url = category_url;
	}
	public int getiLevel() {
		return iLevel;
	}
	public void setiLevel(int iLevel) {
		this.iLevel = iLevel;
	}
	public String getSugg_cat_id() {
		return sugg_cat_id;
	}
	public void setSugg_cat_id(String sugg_cat_id) {
		this.sugg_cat_id = sugg_cat_id;
	}
	public int getiLastLevel() {
		return iLastLevel;
	}
	public void setiLastLevel(int iLastLevel) {
		this.iLastLevel = iLastLevel;
	}
	public String getCatInfo_catId() {
		return catInfo_catId;
	}
	public void setCatInfo_catId(String catInfo_catId) {
		this.catInfo_catId = catInfo_catId;
	}
	public String getTemp_category_id() {
		return temp_category_id;
	}
	public void setTemp_category_id(String temp_category_id) {
		this.temp_category_id = temp_category_id;
	}
	public String getStbSaId() {
		return stbSaId;
	}
	public void setStbSaId(String stbSaId) {
		this.stbSaId = stbSaId;
	}
	public String getStbMacAddr() {
		return stbMacAddr;
	}
	public void setStbMacAddr(String stbMacAddr) {
		this.stbMacAddr = stbMacAddr;
	}
	public String getStbPairing() {
		return stbPairing;
	}
	public void setStbPairing(String stbPairing) {
		this.stbPairing = stbPairing;
	}
	public int getP_stb_idx_sa() {
		return p_stb_idx_sa;
	}
	public void setP_stb_idx_sa(int p_stb_idx_sa) {
		this.p_stb_idx_sa = p_stb_idx_sa;
	}
	public String getPpm_prod_id() {
		return ppm_prod_id;
	}
	public void setPpm_prod_id(String ppm_prod_id) {
		this.ppm_prod_id = ppm_prod_id;
	}
	public String getFreeYn() {
		return freeYn;
	}
	public void setFreeYn(String freeYn) {
		this.freeYn = freeYn;
	}
	public String getTopMenuType() {
		return topMenuType;
	}
	public void setTopMenuType(String topMenuType) {
		this.topMenuType = topMenuType;
	}
	public String getCust_eng_level() {
		return cust_eng_level;
	}
	public void setCust_eng_level(String cust_eng_level) {
		this.cust_eng_level = cust_eng_level;
	}
	public String getGuide_cat_id() {
		return guide_cat_id;
	}
	public void setGuide_cat_id(String guide_cat_id) {
		this.guide_cat_id = guide_cat_id;
	}
	public String getGuide_image_file() {
		return guide_image_file;
	}
	public void setGuide_image_file(String guide_image_file) {
		this.guide_image_file = guide_image_file;
	}
	public String getInfo_text() {
		return info_text;
	}
	public void setInfo_text(String info_text) {
		this.info_text = info_text;
	}
	public String getCat_msg_ppm1() {
		return cat_msg_ppm1;
	}
	public void setCat_msg_ppm1(String cat_msg_ppm1) {
		this.cat_msg_ppm1 = cat_msg_ppm1;
	}
	public String getCat_msg_ppm2() {
		return cat_msg_ppm2;
	}
	public void setCat_msg_ppm2(String cat_msg_ppm2) {
		this.cat_msg_ppm2 = cat_msg_ppm2;
	}
	public String getCat_msg_free() {
		return cat_msg_free;
	}
	public void setCat_msg_free(String cat_msg_free) {
		this.cat_msg_free = cat_msg_free;
	}
	
}

























