package kr.co.wincom.imcs.api.getNSLists;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class GetNSListsRequestVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * getNSLists API 전문 칼럼(순서 일치)
	 ********************************************************************/
	private String saId = "";
	private String stbMac = "";
	private String catId = "";
	private String definFlag = "";
	private String nscType = "";
	private String rating = "";
	private String orderGb = ""; // orderType??
	private String pageNo = ""; // [ 5] 페이지 넘버
	private String pageCnt = ""; // [ 5] 페이지 개수 (페이지 당 N개)
	private String youthYn = "";
	private String pageIdx = "";
	private String baseGb = "";
	private String baseCd = "";
	private String quickDisYn = ""; // [ 1] 퀵배포 컨텐츠 포함 여부
	private String fxType = ""; // [ 1] 유플릭스 타입 (M : MobileWeb, P : PcWeb, T : TvgApp, H : HDTV)
	private String purchasable = "";
	private String imgServer = "";
	private String imgResizeServer = "";
	private String imgCat_server = "";
	private String folderingCat = "";

	/********************************************************************
	 * 추가 사용 파라미터
	 ********************************************************************/
	private String pid = ""; // 프로세스ID - 로그용
	private String resultCode = "20000000";

	private String baseOneCd = "";
	private String youthYnCom = "";
	private String posterType = "";
	private String viewFlag1 = "";
	private String viewFlag2 = "";
	private String catGb = "NSC";

	private String testSbc = "";
	private String cateLevel = ""; // mCateInfo
	private String nscGb = ""; // mCateInfo
	private String cateName = ""; // mCateInfo
	private String parentCatId = ""; // getParentCatId()
	private String subVersion = ""; // getParentCatId()
	private String subPVersion = ""; // getParentCatId()
	private String subPPVersion = ""; // getParentCatId()
	private String i20Version = ""; // VC 인경우에 대한 버전
	private String i20PVersion = ""; // VC 인경우에 대한 버전
	private String i20PPVersion = ""; // VC 인경우에 대한 버전

	private String categoryId = ""; // makeList에서 사용 이름은 catID이나 contentID임

	private String catString1 = ""; // makeList에서 사용
	private String catString2 = ""; // makeList에서 사용
	private String version = ""; // Lists에서 makeList로 version정보 넘길 때 사용 (sub_version이나 i20_version 둘 중 하나)
	private String lastAlbumId = ""; // makeList에서 사용
	private String id = ""; // 이미지파일명 조회 시 사용될 id

	private String vodServer1 = "";
	private String vodServer2 = "";
	private String vodServer3 = "";

	private String multiMappingFlag = "";

	private String param = "";

	public GetNSListsRequestVO(String szParam) {
		String[] arrStat = szParam.split("\\|");

		String key = "";
		String value = "";
		int nStr = 0;

		this.param = szParam; // getNSList에서 MakeList 호출 시 로그 용

		HashMap<String, String> paramMap = new HashMap<String, String>();

		/*
		 * String szTemp = szParam.toLowerCase();
		 * 
		 * if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null ||
		 * paramMap.get("cat_id") == null || paramMap.get("defin_flag") == null ||
		 * paramMap.get("nsc_type") == null ) { throw new ImcsException(); }
		 */

		for (int i = 0; i < arrStat.length; i++) {
			nStr = arrStat[i].indexOf("=");

			if (nStr > 0) {
				key = arrStat[i].substring(0, nStr).toLowerCase().trim();
				value = arrStat[i].substring(nStr + 1, arrStat[i].length()).trim();

				paramMap.put(key, value);

				/*
				 * if( !value.matches(ImcsConstants.N_SP_PTN) &&
				 * key.toLowerCase().indexOf("name") == null ){ //특수문자 있음 throw new
				 * ImcsException(); }
				 */

				if (key.toLowerCase().equals("version"))
					this.setVersion(value);
				if (key.toLowerCase().equals("sa_id"))
					this.setSaId(value);
				if (key.toLowerCase().equals("stb_mac"))
					this.setStbMac(value);
				if (key.toLowerCase().equals("cat_id"))
					this.setCatId(value);
				if (key.toLowerCase().equals("defin_flag"))
					this.setDefinFlag(value);
				if (key.toLowerCase().equals("nsc_type"))
					this.setNscType(value);
				if (key.toLowerCase().equals("rating"))
					this.setRating(value);
				if (key.toLowerCase().equals("order_gb"))
					this.setOrderGb(value);
				if (key.toLowerCase().equals("page_no"))
					this.setPageNo(value);
				if (key.toLowerCase().equals("page_cnt"))
					this.setPageCnt(value);
				if (key.toLowerCase().equals("youth_yn"))
					this.setYouthYn(value);
				if (key.toLowerCase().equals("page_idx"))
					this.setPageIdx(value);
				if (key.toLowerCase().equals("base_gb"))
					this.setBaseGb(value);
				if (key.toLowerCase().equals("base_cd"))
					this.setBaseCd(value);
				if (key.toLowerCase().equals("quick_dis_yn"))
					this.setQuickDisYn(value);
				if (key.toLowerCase().equals("fx_type"))
					this.setFxType(value);
				if (key.toLowerCase().equals("purchasable"))
					this.setPurchasable(value);
			}
		}

		// GetNSListsController.saId = paramMap.get("sa_id");

		// GetNSListsController.stbMac = paramMap.get("stb_mac");

		if (paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null || paramMap.get("cat_id") == null
				|| paramMap.get("defin_flag") == null || paramMap.get("nsc_type") == null) {
			throw new ImcsException();
		}

		/*
		 * if(this.saId.length() > 12 || this.saId.length() < 7 ){ throw new
		 * ImcsException(); }
		 * 
		 * if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){ throw new
		 * ImcsException(); }
		 */

		/**
		 * 1. Parameter 존재 체크(값은 없어도 파라미터는 모두 존재해야 한다.). Controller 에서 파라미터가 없으면 호출되지 않음
		 * -> 체크 필요 없음 /** 2. 필수 Parameter의 값 존재 체크
		 */

		// 파라미터 null 체크 및 null일때 Default값 지정
		this.rating = StringUtil.replaceNull(this.getRating(), "07");
		if (this.rating.equals("01"))
			this.rating = "07";
		this.orderGb = StringUtil.replaceNull(this.getOrderGb(), "D");
		this.pageNo = StringUtil.replaceNull(this.getPageNo(), "0");
		this.pageCnt = StringUtil.replaceNull(this.getPageCnt(), "0");
		this.youthYn = StringUtil.replaceNull(this.getYouthYn(), "N");
		this.pageIdx = StringUtil.replaceNull(this.getPageIdx(), "0");
		this.baseGb = StringUtil.replaceNull(this.getBaseGb(), "Y");
		this.quickDisYn = StringUtil.replaceNull(this.getQuickDisYn(), "N");
		this.fxType = StringUtil.replaceNull(this.getFxType(), "X");
		this.purchasable = StringUtil.replaceNull(this.getPurchasable(), "N");

		/*
		 * switch (rating) { case "01": case "02": case "03": case "04": case "05": case
		 * "06": case "07": break;
		 * 
		 * default: throw new ImcsException(); }
		 * 
		 * switch (orderGb) { case "N": case "H": case "D": case "A": case "P": case
		 * "W": case "R": break;
		 * 
		 * default: throw new ImcsException(); }
		 * 
		 * switch (youthYn) { case "Y": case "N": break;
		 * 
		 * default: throw new ImcsException(); }
		 * 
		 * switch (baseGb) { case "Y": case "N": break;
		 * 
		 * default: throw new ImcsException(); }
		 * 
		 * switch (quickDisYn) { case "Y": case "N": break;
		 * 
		 * default: throw new ImcsException(); }
		 * 
		 * 
		 * switch (fxType) { case "X": case "M": case "P": case "T": case "H": break;
		 * 
		 * default: throw new ImcsException(); }
		 */

		if (this.baseCd != null && !this.baseCd.equals(""))
			this.baseOneCd = this.baseCd.substring(0, 1);

		if (this.youthYn.equals("Y"))
			this.youthYnCom = "1";
		else
			this.youthYnCom = "0";

		if (this.nscType.equals("PAD")) {
			this.posterType = "P";
			this.catString1 = "PAD";
			this.catString2 = "PAD";
		} else if (this.nscType.equals("LTE")) {
			this.posterType = "P";
			this.catString1 = "LTE";
			this.catString2 = "UFX";
		} else if (this.nscType.equals("PAW")) {
			this.posterType = "W";
		}

		if (this.quickDisYn.equals("Y"))
			this.viewFlag1 = "P";
		else
			this.viewFlag1 = "V";

		this.catGb = "NSC";

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

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getOrderGb() {
		return orderGb;
	}

	public void setOrderGb(String orderGb) {
		this.orderGb = orderGb;
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

	public String getYouthYn() {
		return youthYn;
	}

	public void setYouthYn(String youthYn) {
		this.youthYn = youthYn;
	}

	public String getPageIdx() {
		return pageIdx;
	}

	public void setPageIdx(String pageIdx) {
		this.pageIdx = pageIdx;
	}

	public String getBaseGb() {
		return baseGb;
	}

	public void setBaseGb(String baseGb) {
		this.baseGb = baseGb;
	}

	public String getBaseCd() {
		return baseCd;
	}

	public void setBaseCd(String baseCd) {
		this.baseCd = baseCd;
	}

	public String getQuickDisYn() {
		return quickDisYn;
	}

	public void setQuickDisYn(String quickDisYn) {
		this.quickDisYn = quickDisYn;
	}

	public String getFxType() {
		return fxType;
	}

	public void setFxType(String fxType) {
		this.fxType = fxType;
	}

	public String getPurchasable() {
		return purchasable;
	}

	public void setPurchasable(String purchasable) {
		this.purchasable = purchasable;
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

	public String getBaseOneCd() {
		return baseOneCd;
	}

	public void setBaseOneCd(String baseOneCd) {
		this.baseOneCd = baseOneCd;
	}

	public String getYouthYnCom() {
		return youthYnCom;
	}

	public void setYouthYnCom(String youthYnCom) {
		this.youthYnCom = youthYnCom;
	}

	public String getPosterType() {
		return posterType;
	}

	public void setPosterType(String posterType) {
		this.posterType = posterType;
	}

	public String getViewFlag1() {
		return viewFlag1;
	}

	public void setViewFlag1(String viewFlag1) {
		this.viewFlag1 = viewFlag1;
	}

	public String getCatGb() {
		return catGb;
	}

	public void setCatGb(String catGb) {
		this.catGb = catGb;
	}

	public String getViewFlag2() {
		return viewFlag2;
	}

	public void setViewFlag2(String viewFlag2) {
		this.viewFlag2 = viewFlag2;
	}

	public String getCateLevel() {
		return cateLevel;
	}

	public void setCateLevel(String cateLevel) {
		this.cateLevel = cateLevel;
	}

	public String getNscGb() {
		return nscGb;
	}

	public void setNscGb(String nscGb) {
		this.nscGb = nscGb;
	}

	public String getCateName() {
		return cateName;
	}

	public void setCateName(String cateName) {
		this.cateName = cateName;
	}

	public String getParentCatId() {
		return parentCatId;
	}

	public void setParentCatId(String parentCatId) {
		this.parentCatId = parentCatId;
	}

	public String getSubVersion() {
		return subVersion;
	}

	public void setSubVersion(String subVersion) {
		this.subVersion = subVersion;
	}

	public String getSubPVersion() {
		return subPVersion;
	}

	public void setSubPVersion(String subPVersion) {
		this.subPVersion = subPVersion;
	}

	public String getSubPPVersion() {
		return subPPVersion;
	}

	public void setSubPPVersion(String subPPVersion) {
		this.subPPVersion = subPPVersion;
	}

	public String getTestSbc() {
		return testSbc;
	}

	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}

	public String getMultiMappingFlag() {
		return multiMappingFlag;
	}

	public void setMultiMappingFlag(String multiMappingFlag) {
		this.multiMappingFlag = multiMappingFlag;
	}

	public String getCatString1() {
		return catString1;
	}

	public void setCatString1(String catString1) {
		this.catString1 = catString1;
	}

	public String getCatString2() {
		return catString2;
	}

	public void setCatString2(String catString2) {
		this.catString2 = catString2;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getLastAlbumId() {
		return lastAlbumId;
	}

	public void setLastAlbumId(String lastAlbumId) {
		this.lastAlbumId = lastAlbumId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getI20Version() {
		return i20Version;
	}

	public void setI20Version(String i20Version) {
		this.i20Version = i20Version;
	}

	public String getI20PVersion() {
		return i20PVersion;
	}

	public void setI20PVersion(String i20pVersion) {
		i20PVersion = i20pVersion;
	}

	public String getI20PPVersion() {
		return i20PPVersion;
	}

	public void setI20PPVersion(String i20ppVersion) {
		i20PPVersion = i20ppVersion;
	}

	public String getVodServer1() {
		return vodServer1;
	}

	public void setVodServer1(String vodServer1) {
		this.vodServer1 = vodServer1;
	}

	public String getVodServer2() {
		return vodServer2;
	}

	public void setVodServer2(String vodServer2) {
		this.vodServer2 = vodServer2;
	}

	public String getVodServer3() {
		return vodServer3;
	}

	public void setVodServer3(String vodServer3) {
		this.vodServer3 = vodServer3;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getImgServer() {
		return imgServer;
	}

	public void setImgServer(String imgServer) {
		this.imgServer = imgServer;
	}

	public String getImgResizeServer() {
		return imgResizeServer;
	}

	public void setImgResizeServer(String imgResizeServer) {
		this.imgResizeServer = imgResizeServer;
	}

	public String getImgCat_server() {
		return imgCat_server;
	}

	public void setImgCat_server(String imgCat_server) {
		this.imgCat_server = imgCat_server;
	}

	public String getFolderingCat() {
		return folderingCat;
	}

	public void setFolderingCat(String folderingCat) {
		this.folderingCat = folderingCat;
	}

}