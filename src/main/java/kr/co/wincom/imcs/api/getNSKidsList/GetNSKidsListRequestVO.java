package kr.co.wincom.imcs.api.getNSKidsList;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

public class GetNSKidsListRequestVO extends NoSqlLoggingVO implements Serializable
{
	private static final long serialVersionUID = 7718159818157369821L;
	
	private String saId		= "";
	private String stbMac	= "";
	private String catId	= "";
	private String categoryType	= "";
	private String pageNo	= "";
	private String pageCnt	= "";
	
	private String pid		= "";
	private String resultCode = "20000000";
	private String testSbc = "";
	private String viewFlag2 = "";
	
	private int c_idx_sa = 0;
	private String last_album_id = "";
	
	private String kids_category_type = null;
	private String kids_category_level = null;
	private String kids_parent_category_id = null;
	private String kids_category_month = null;
	
	private String c_cat_id_cache = "";
	private String c_parent_category = "";
	
	private String c_sub_version = "";
	private String c_sub_P_version = "";
	private String c_sub_PP_version = "";
	
	private String param = "";
	
	private String c_cont_id = "";
	// 2020.03.13 - 모바일 아이들나라 페어링된 IPTV 가입의 최근 시청 정보 조회하는 용도로 사용
	private String ncnSaId = "";
	private String ncnStbMac = "";
	
	public GetNSKidsListRequestVO(String szParam)
	{
		CommonService commonService = new CommonService();  
		
		this.setParam(szParam);
		
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
				
				if(key.toLowerCase().equals("sa_id"))	this.setSaId(value);
				if(key.toLowerCase().equals("stb_mac"))	this.setStbMac(value);
				if(key.toLowerCase().equals("cat_id"))
				{
					this.setCatId(value);
					this.setC_cat_id_cache(value);
				}
				if(key.toLowerCase().equals("category_type"))	this.setCategoryType(value);
				if(key.toLowerCase().equals("page_no"))	this.setPageNo(value);
				if(key.toLowerCase().equals("page_cnt"))	this.setPageCnt(value);
			}
		}
		
		this.pageNo = StringUtil.replaceNull(this.getPageNo(), "1");
		this.pageCnt = StringUtil.replaceNull(this.getPageCnt(), "1");
		
		if(StringUtils.isBlank(this.getCategoryType()))
			this.setCategoryType("N");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null 
				|| paramMap.get("cat_id") == null)
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
		
		if(!commonService.getValidParam(this.catId, 4, 5, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.categoryType, 1, 1, 2))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.pageNo, 1, 5, 3))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.pageCnt, 1, 5, 3))
		{
			throw new ImcsException();
		}
		
		if(this.pageNo.equals("0") || this.pageCnt.equals("0"))
		{
			throw new ImcsException();
		}
		
		switch(this.categoryType)
		{
			case "N":
			case "C":
			case "B":
			case "T":
			case "G":
			case "P":
				break;				
			default:
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
	public String getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
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
	public String getViewFlag2() {
		return viewFlag2;
	}
	public void setViewFlag2(String viewFlag2) {
		this.viewFlag2 = viewFlag2;
	}
	public String getKids_category_type() {
		return kids_category_type;
	}
	public void setKids_category_type(String kids_category_type) {
		this.kids_category_type = kids_category_type;
	}
	public String getKids_category_level() {
		return kids_category_level;
	}
	public void setKids_category_level(String kids_category_level) {
		this.kids_category_level = kids_category_level;
	}
	public String getKids_parent_category_id() {
		return kids_parent_category_id;
	}
	public void setKids_parent_category_id(String kids_parent_category_id) {
		this.kids_parent_category_id = kids_parent_category_id;
	}
	public String getKids_category_month() {
		return kids_category_month;
	}
	public void setKids_category_month(String kids_category_month) {
		this.kids_category_month = kids_category_month;
	}
	public int getC_idx_sa() {
		return c_idx_sa;
	}
	public void setC_idx_sa(int c_idx_sa) {
		this.c_idx_sa = c_idx_sa;
	}
	public String getLast_album_id() {
		return last_album_id;
	}
	public void setLast_album_id(String last_album_id) {
		this.last_album_id = last_album_id;
	}
	public String getC_cat_id_cache() {
		return c_cat_id_cache;
	}
	public void setC_cat_id_cache(String c_cat_id_cache) {
		this.c_cat_id_cache = c_cat_id_cache;
	}
	public String getC_parent_category() {
		return c_parent_category;
	}
	public void setC_parent_category(String c_parent_category) {
		this.c_parent_category = c_parent_category;
	}
	public String getC_sub_version() {
		return c_sub_version;
	}
	public void setC_sub_version(String c_sub_version) {
		this.c_sub_version = c_sub_version;
	}
	public String getC_sub_P_version() {
		return c_sub_P_version;
	}
	public void setC_sub_P_version(String c_sub_P_version) {
		this.c_sub_P_version = c_sub_P_version;
	}
	public String getC_sub_PP_version() {
		return c_sub_PP_version;
	}
	public void setC_sub_PP_version(String c_sub_PP_version) {
		this.c_sub_PP_version = c_sub_PP_version;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	public String getC_cont_id() {
		return c_cont_id;
	}
	public void setC_cont_id(String c_cont_id) {
		this.c_cont_id = c_cont_id;
	}

	public String getNcnSaId() {
		return ncnSaId;
	}

	public void setNcnSaId(String ncnSaId) {
		this.ncnSaId = ncnSaId;
	}

	public String getNcnStbMac() {
		return ncnStbMac;
	}

	public void setNcnStbMac(String ncnStbMac) {
		this.ncnStbMac = ncnStbMac;
	}

}
