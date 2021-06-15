package kr.co.wincom.imcs.common.vo;

import java.util.Random;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.DateUtil;
import kr.co.wincom.imcs.common.util.StringUtil;

public class StatVO{
	DateUtil dateUtil = new DateUtil();
	
	
	private String seqId			= "";
	private String logTime			= "";
	private String logType			= "SVC";
	private String sid				= "";
	private String resultCode		= "20000000";
	
	private String reqTime			= "";
	private String rspTime			= "";
	private String clientIp			= "";
	private String devInfo			= "";
	private String osInfo			= "";
	private String nwInfo 			= "";
	private String svcName			= "";
   	private String devModel			= "";
	private String carrierType		= "";
	private String apiId			= "";
	private String apiSid			= "";
	private String apiAkey			= "";
	private String apiNm			= "";
	private String saId				= "";
	private String stbMac			= "";
	private String uiVer			= "";
	private String nowPage			= "";
	private String prePage			= "";
	private String menuNm			= "";
	private String contentsId		= "";
	private String contentsName		= "";
	private String scatId			= "";
	private String scatName			= "";
	private String productId		= "";
	private String productName		= "";
	private String productPrice		= "";
	private String apiType			= "";
	private String genreLarge		= "";	
	private String genreMid			= "";
	private String catId			= "";
	private String catName			= "";
	private String buyingDate		= "";
	private String contsGb			= "";
	private String runTime			= "";
	private String continueType		= "";
	private String viewType			= "";
	private String continueTime		= "";
	private String downSize			= "";
	private String createDate		= "";
	private String termBinver		= "";
	private String buyingType		= "";
	private String buyAmt			= "";
	private String alwncecharge		= "";
	private String balace			= "";
	private String inappYn			= "";
	private String inappProdId  	= "";
	private String inappAmt			= "";
	private String cpnEventId		= "";
	private String buyingGb			= "";
	private String parentCatId		= "";
	private String dfBuyYn			= "";
	private String dfBalace			= "";
	private String dfPrice			= "";
	private String dfWatchYn		= "";
	
	
	private String printType		= "1";
	private String cpnEventSeq		= "";
	private String dfAlwnceChargee	= "";

	public StatVO() {}
	
	public StatVO(String szStat) {
		
		
		
		
		// SEQ_ID - 현재시간 (YYYYMMDDHHmmSSsss) + 랜덤8자리
		String szSeqId	= dateUtil.getCustomTodayFormat("yyyyMMddHHmmssSSS");
		szSeqId			= szSeqId + String.valueOf(10000000 + new Random().nextInt(90000000));
		
		// 통합통계 초기값 세팅
		this.setSeqId(szSeqId);
		this.setReqTime(dateUtil.getCustomTodayFormat("yyyyMMddHHmmssSSS"));
		
					
		if(szStat != null && !szStat.trim().equals("")){
			
			
			
			String[] arrStat = szStat.split("\\|");
			
			String key		= "";
			String value	= "";
			int    nStr		= 0;
			
			for(int i = 0; i < arrStat.length; i++){
				nStr	= arrStat[i].indexOf("=");
				
				if(nStr > 0) {
					key = arrStat[i].substring(0, nStr).toLowerCase().trim();
					value = arrStat[i].substring(nStr + 1, arrStat[i].length()).trim();
					
					if(key.toLowerCase().equals("sid"))						this.setSid(value);	//this.sid = value;
					if(key.toLowerCase().equals("client_ip"))				this.setClientIp(value); //this.clientIp	= value;
					if(key.toLowerCase().equals("os_info"))					this.setOsInfo(value); //this.osInfo	= value;
					if(key.toLowerCase().equals("nw_info"))					this.setNwInfo(value); //this.nwInfo	= value;
					if(key.toLowerCase().equals("app_type"))				this.setSvcName(value); //this.svcName	= value;
					if(key.toLowerCase().equals("dev_model"))				this.setDevModel(value); //this.devModel	= value;
					if(key.toLowerCase().equals("carrier_type"))			this.setCarrierType(value); //this.carrierType	= value;
					if(key.toLowerCase().equals("ui_ver"))					this.setUiVer(value); //this.uiVer	= value;
					if(key.toLowerCase().equals("now_page"))				this.setNowPage(value); //this.nowPage	= value;
					if(key.toLowerCase().equals("pre_page"))				this.setPrePage(value);//this.prePage	= value;
					if(key.toLowerCase().equals("menu_nm"))					this.setMenuNm(value);//this.menuNm	= value;
					if(key.toLowerCase().equals("conts_gb"))				this.setContsGb(value);//this.contsGb	= value;
					if(key.toLowerCase().equals("term_binver"))				this.setTermBinver(value);//this.termBinver	= value;
					if(key.toLowerCase().equals("cpn_event_id"))			this.setCpnEventId(value);//this.cpnEventId	= value;
					if(key.toLowerCase().equals("buying_gb"))				this.setBuyingGb(value);//this.buyingGb	= value;
					if(key.toLowerCase().equals("parent_cat_id"))			this.setParentCatId(value);//this.parentCatId	= value;
					if(key.toLowerCase().equals("df_price"))				this.setDfPrice(value);//this.dfPrice	= value;
					if(key.toLowerCase().equals("df_balace"))				this.setDfBalace(value);//this.dfBalace	= value;
					if(key.toLowerCase().equals("df_buy_yn"))				this.setDfBuyYn(value);//this.dfBuyYn	= value;
					if(key.toLowerCase().equals("df_watch_yn"))				this.setDfWatchYn(value);//this.dfWatchYn	= value;
					
					// OPENAPI
					if(key.toLowerCase().equals("api_id"))					this.setApiId(value);//this.apiId	= value;
					if(key.toLowerCase().equals("api_sid"))					this.setApiSid(value);//this.apiSid	= value;
					if(key.toLowerCase().equals("api_akey"))				this.setApiAkey(key);        //this.apiAkey	= value;
	
					
					
					/*if(key.toLowerCase().equals("seq_id"))					this.seq_id	= value;
					if(key.toLowerCase().equals("log_time"))				this.log_time	= value;
					if(key.toLowerCase().equals("log_type"))				this.log_type	= value;
					if(key.toLowerCase().equals("result_code"))				this.result_code	= value;
					if(key.toLowerCase().equals("req_time"))				this.req_time	= value;
					if(key.toLowerCase().equals("rsp_time"))				this.rsp_time	= value;
					if(key.toLowerCase().equals("dev_info"))				this.dev_info	= value;
					if(key.toLowerCase().equals("api_nm"))					this.api_nm	= value;
					if(key.toLowerCase().equals("sa_id"))					this.sa_id	= value;
					if(key.toLowerCase().equals("stb_mac"))					this.stb_mac	= value;
					if(key.toLowerCase().equals("contents_id"))				this.contents_id	= value;
					if(key.toLowerCase().equals("contents_name"))			this.contents_name	= value;
					if(key.toLowerCase().equals("scat_id"))					this.scat_id	= value;
					if(key.toLowerCase().equals("scat_name"))				this.scat_name	= value;
					if(key.toLowerCase().equals("product_id"))				this.product_id	= value;
					if(key.toLowerCase().equals("product_name"))			this.product_name	= value;
					if(key.toLowerCase().equals("product_price"))			this.product_price	= value;
					if(key.toLowerCase().equals("api_type"))				this.api_type	= value;
					if(key.toLowerCase().equals("genre_large"))				this.genre_large	= value;
					if(key.toLowerCase().equals("genre_mid"))				this.genre_mid	= value;
					if(key.toLowerCase().equals("cat_id"))					this.cat_id	= value;
					if(key.toLowerCase().equals("cat_name"))				this.cat_name	= value;
					if(key.toLowerCase().equals("buying_date"))				this.buying_date	= value;
					if(key.toLowerCase().equals("run_time"))				this.run_time	= value;
					if(key.toLowerCase().equals("continue_type"))			this.continue_type	= value;
					if(key.toLowerCase().equals("view_type"))				this.view_type	= value;
					if(key.toLowerCase().equals("continue_time"))			this.continue_time	= value;
					if(key.toLowerCase().equals("down_size"))				this.down_size	= value;
					if(key.toLowerCase().equals("create_date"))				this.create_date	= value;
					if(key.toLowerCase().equals("buying_type"))				this.buying_type	= value;
					if(key.toLowerCase().equals("buy_amt"))					this.buy_amt	= value;
					if(key.toLowerCase().equals("alwnce_charge"))			this.alwnce_charge	= value;
					if(key.toLowerCase().equals("balace"))					this.balace	= value;
					if(key.toLowerCase().equals("inapp_yn"))				this.inapp_yn	= value;
					if(key.toLowerCase().equals("inapp_prod_id"))			this.inapp_prod_id	= value;
					if(key.toLowerCase().equals("inapp_amt"))				this.inapp_amt	= value;*/
				}
			}
			
			if(this.sid.equals(""))
				this.resultCode	= "30000000";

		} else {
			this.resultCode	= "30000000";
		}
	}
	
	
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		this.setRspTime(dateUtil.getCustomTodayFormat("yyyyMMddHHmmssSSS"));
		this.setLogTime(dateUtil.getCustomTodayFormat("yyyyMMddHHmmss"));
		
		sb.append("SEQ_ID=").append( this.seqId ).append(ImcsConstants.COLSEP);
		
		
		sb.append("LOG_TIME=").append(StringUtil.replaceNull(this.logTime, "")).append(ImcsConstants.COLSEP);
		sb.append("LOG_TYPE=").append(StringUtil.replaceNull(this.logType, "")).append(ImcsConstants.COLSEP);		
		sb.append("SID=").append(StringUtil.replaceNull(this.sid, "")).append(ImcsConstants.COLSEP);		
		sb.append("RESULT_CODE=").append(StringUtil.replaceNull(this.resultCode, "")).append(ImcsConstants.COLSEP);
		sb.append("REQ_TIME=").append(StringUtil.replaceNull(this.reqTime, "")).append(ImcsConstants.COLSEP);
		sb.append("RSP_TIME=").append(StringUtil.replaceNull(this.rspTime, "")).append(ImcsConstants.COLSEP);
		sb.append("CLIENT_IP=").append(StringUtil.replaceNull(this.clientIp, "")).append(ImcsConstants.COLSEP);
		sb.append("DEV_INFO=").append(StringUtil.replaceNull(this.devInfo, "")).append(ImcsConstants.COLSEP);
		sb.append("OS_INFO=").append(StringUtil.replaceNull(this.osInfo, "")).append(ImcsConstants.COLSEP);
		sb.append("NW_INFO=").append(StringUtil.replaceNull(this.nwInfo, "")).append(ImcsConstants.COLSEP);
		sb.append("SVC_NAME=").append(StringUtil.replaceNull(this.svcName, "")).append(ImcsConstants.COLSEP);
		sb.append("DEV_MODEL=").append(StringUtil.replaceNull(this.devModel, "")).append(ImcsConstants.COLSEP);
		sb.append("CARRIER_TYPE=").append(StringUtil.replaceNull(this.carrierType, "")).append(ImcsConstants.COLSEP);
		sb.append("API_ID=").append(StringUtil.replaceNull(this.apiId, "")).append(ImcsConstants.COLSEP);
		sb.append("API_SID=").append(StringUtil.replaceNull(this.apiSid, "")).append(ImcsConstants.COLSEP);
		sb.append("API_AKEY=").append(StringUtil.replaceNull(this.apiAkey, "")).append(ImcsConstants.COLSEP);
		sb.append("API_NM=").append(StringUtil.replaceNull(this.apiNm, "")).append(ImcsConstants.COLSEP);
		sb.append("SA_ID=").append(StringUtil.replaceNull(this.saId, "")).append(ImcsConstants.COLSEP);
		sb.append("STB_MAC=").append(StringUtil.replaceNull(this.stbMac, "")).append(ImcsConstants.COLSEP);
		sb.append("UI_VER=").append(StringUtil.replaceNull(this.uiVer, "")).append(ImcsConstants.COLSEP);
		sb.append("NOW_PAGE=").append(StringUtil.replaceNull(this.nowPage, "")).append(ImcsConstants.COLSEP);
		sb.append("PRE_PAGE=").append(StringUtil.replaceNull(this.prePage, "")).append(ImcsConstants.COLSEP);
		sb.append("MENU_NM=").append(StringUtil.replaceNull(this.menuNm, "")).append(ImcsConstants.COLSEP);
		sb.append("CONTENTS_ID=").append(StringUtil.replaceNull(this.contentsId, "")).append(ImcsConstants.COLSEP);
		sb.append("CONTENTS_NAME=").append(StringUtil.replaceNull(this.contentsName, "")).append(ImcsConstants.COLSEP);
		sb.append("SCAT_ID=").append(StringUtil.replaceNull(this.scatId, "")).append(ImcsConstants.COLSEP);
		sb.append("SCAT_NAME=").append(StringUtil.replaceNull(this.scatName, "")).append(ImcsConstants.COLSEP);
		sb.append("PRODUCT_ID=").append(StringUtil.replaceNull(this.productId, "")).append(ImcsConstants.COLSEP);
		sb.append("PRODUCT_NAME=").append(StringUtil.replaceNull(this.productName, "")).append(ImcsConstants.COLSEP);
		sb.append("PRODUCT_PRICE=").append(StringUtil.replaceNull(this.productPrice, "")).append(ImcsConstants.COLSEP);
		sb.append("API_TYPE=").append(StringUtil.replaceNull(this.apiType, "")).append(ImcsConstants.COLSEP);
		sb.append("GENRE_LARGE=").append(StringUtil.replaceNull(this.genreLarge, "")).append(ImcsConstants.COLSEP);
		sb.append("GENRE_MID=").append(StringUtil.replaceNull(this.genreMid, "")).append(ImcsConstants.COLSEP);
		sb.append("CAT_ID=").append(StringUtil.replaceNull(this.catId, "")).append(ImcsConstants.COLSEP);
		sb.append("CAT_NAME=").append(StringUtil.replaceNull(this.catName, "")).append(ImcsConstants.COLSEP);
		sb.append("BUYING_DATE=").append(StringUtil.replaceNull(this.buyingDate, "")).append(ImcsConstants.COLSEP);
		sb.append("CONTS_GB=").append(StringUtil.replaceNull(this.contsGb, "")).append(ImcsConstants.COLSEP);
		sb.append("RUN_TIME=").append(StringUtil.replaceNull(this.runTime, "")).append(ImcsConstants.COLSEP);
		sb.append("CONTINUE_TYPE=").append(StringUtil.replaceNull(this.continueType, "")).append(ImcsConstants.COLSEP);
		sb.append("VIEW_TYPE=").append(StringUtil.replaceNull(this.viewType, "")).append(ImcsConstants.COLSEP);
		sb.append("CONTINUE_TIME=").append(StringUtil.replaceNull(this.continueTime, "")).append(ImcsConstants.COLSEP);
		sb.append("DOWN_SIZE=").append(StringUtil.replaceNull(this.downSize, "")).append(ImcsConstants.COLSEP);
		sb.append("CREATE_DATE=").append(StringUtil.replaceNull(this.createDate, "")).append(ImcsConstants.COLSEP);
		sb.append("TERM_BINVER=").append(StringUtil.replaceNull(this.termBinver, "")).append(ImcsConstants.COLSEP);
		sb.append("BUYING_TYPE=").append(StringUtil.replaceNull(this.buyingType, "")).append(ImcsConstants.COLSEP);
		sb.append("BUY_AMT=").append(StringUtil.replaceNull(this.buyAmt, "")).append(ImcsConstants.COLSEP);
		sb.append("ALWNCE_CHARGE=").append(StringUtil.replaceNull(this.alwncecharge, "")).append(ImcsConstants.COLSEP);
		sb.append("BALACE=").append(StringUtil.replaceNull(this.balace, "")).append(ImcsConstants.COLSEP);
		sb.append("INAPP_YN=").append(StringUtil.replaceNull(this.inappYn, "")).append(ImcsConstants.COLSEP);
		sb.append("INAPP_PROD_ID=").append(StringUtil.replaceNull(this.inappProdId, "")).append(ImcsConstants.COLSEP);
		sb.append("INAPP_AMT=").append(StringUtil.replaceNull(this.inappAmt, "")).append(ImcsConstants.COLSEP);
		sb.append("CPN_EVENT_ID=").append(StringUtil.replaceNull(this.cpnEventId, "")).append(ImcsConstants.COLSEP);
		
		if("2".equals(this.printType)){
			sb.append("PARENT_CAT_ID=").append(StringUtil.replaceNull(this.parentCatId, "")).append(ImcsConstants.COLSEP);
		}
		
		if("3".equals(this.printType)){
			sb.append("BUYING_GB=").append(StringUtil.replaceNull(this.buyingGb, "")).append(ImcsConstants.COLSEP);
			sb.append("PARENT_CAT_ID=").append(StringUtil.replaceNull(this.parentCatId, "")).append(ImcsConstants.COLSEP);			
		}
		
		if("4".equals(this.printType)){//현재 buyContsCp 에서만 사용
			sb.append("PARENT_CAT_ID=").append(StringUtil.replaceNull(this.parentCatId, "")).append(ImcsConstants.COLSEP);
			sb.append("CPN_EVENT_SEQ=").append(StringUtil.replaceNull(this.cpnEventSeq, "")).append(ImcsConstants.COLSEP);
			
			
			sb.append("DF_PRICE=").append(StringUtil.replaceNull(this.dfPrice, "")).append(ImcsConstants.COLSEP);
			sb.append("DF_BALACE=").append(StringUtil.replaceNull(this.dfBalace, "")).append(ImcsConstants.COLSEP);
			sb.append("DFALWNCE_CHARGEE=").append(StringUtil.replaceNull(this.dfPrice, "")).append(ImcsConstants.COLSEP);
			sb.append("DF_BUY_YN=").append(StringUtil.replaceNull(this.dfBuyYn, "")).append(ImcsConstants.COLSEP);			
		}
		
		if("5".equals(this.printType)){ //현제 getNSContList, getNSContDtl, getNSPageList 에서만 사용
			sb.append("PARENT_CAT_ID=").append(StringUtil.replaceNull(this.parentCatId, "")).append(ImcsConstants.COLSEP);
			sb.append("DF_PRICE=").append(StringUtil.replaceNull(this.dfPrice, "")).append(ImcsConstants.COLSEP);
		}
		
		if("6".equals(this.printType)){ //현재 authorizeNView 에서만 사용
			sb.append("PARENT_CAT_ID=").append(StringUtil.replaceNull(this.parentCatId, "")).append(ImcsConstants.COLSEP);
			sb.append("DF_PRICE=").append(StringUtil.replaceNull(this.dfPrice, "")).append(ImcsConstants.COLSEP);
			sb.append("DF_BUY_YN=").append(StringUtil.replaceNull(this.dfBuyYn, "")).append(ImcsConstants.COLSEP);
			sb.append("DF_WATCH_YN=").append(StringUtil.replaceNull(this.dfWatchYn, "")).append(ImcsConstants.COLSEP);
		}
		
		if("7".equals(this.printType)){//현재 buyNSConts, buyNSDMConts 에서만 사용
			sb.append("BUYING_GB=").append(StringUtil.replaceNull(this.buyingGb, "")).append(ImcsConstants.COLSEP);
			sb.append("PARENT_CAT_ID=").append(StringUtil.replaceNull(this.parentCatId, "")).append(ImcsConstants.COLSEP);
			sb.append("DF_PRICE=").append(StringUtil.replaceNull(this.dfPrice, "")).append(ImcsConstants.COLSEP);
			sb.append("DF_BALACE=").append(StringUtil.replaceNull(this.dfBalace, "")).append(ImcsConstants.COLSEP);
			sb.append("DF_BUY_YN=").append(StringUtil.replaceNull(this.dfBuyYn, "")).append(ImcsConstants.COLSEP);
		}
				
		return sb.toString();
	}
	
	

	

	
	public DateUtil getDateUtil() {
		return dateUtil;
	}

	public void setDateUtil(DateUtil dateUtil) {
		this.dateUtil = dateUtil;
	}

	public String getSeqId() {
		return seqId;
	}

	public void setSeqId(String seqId) {
		this.seqId = seqId;
	}

	public String getLogTime() {
		return logTime;
	}

	public void setLogTime(String logTime) {
		this.logTime = logTime;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getReqTime() {
		return reqTime;
	}

	public void setReqTime(String reqTime) {
		this.reqTime = reqTime;
	}

	public String getRspTime() {
		return rspTime;
	}

	public void setRspTime(String rspTime) {
		this.rspTime = rspTime;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getDevInfo() {
		return devInfo;
	}

	public void setDevInfo(String devInfo) {
		this.devInfo = devInfo;
	}

	public String getOsInfo() {
		return osInfo;
	}

	public void setOsInfo(String osInfo) {
		this.osInfo = osInfo;
	}

	public String getNwInfo() {
		return nwInfo;
	}

	public void setNwInfo(String nwInfo) {
		this.nwInfo = nwInfo;
	}

	public String getSvcName() {
		return svcName;
	}

	public void setSvcName(String svcName) {
		this.svcName = svcName;
	}

	public String getDevModel() {
		return devModel;
	}

	public void setDevModel(String devModel) {
		this.devModel = devModel;
	}

	public String getCarrierType() {
		return carrierType;
	}

	public void setCarrierType(String carrierType) {
		this.carrierType = carrierType;
	}

	public String getApiId() {
		return apiId;
	}

	public void setApiId(String apiId) {
		this.apiId = apiId;
	}

	public String getApiSid() {
		return apiSid;
	}

	public void setApiSid(String apiSid) {
		this.apiSid = apiSid;
	}

	public String getApiAkey() {
		return apiAkey;
	}

	public void setApiAkey(String apiAkey) {
		this.apiAkey = apiAkey;
	}

	public String getApiNm() {
		return apiNm;
	}

	public void setApiNm(String apiNm) {
		this.apiNm = apiNm;
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

	public String getUiVer() {
		return uiVer;
	}

	public void setUiVer(String uiVer) {
		this.uiVer = uiVer;
	}

	public String getNowPage() {
		return nowPage;
	}

	public void setNowPage(String nowPage) {
		this.nowPage = nowPage;
	}

	public String getPrePage() {
		return prePage;
	}

	public void setPrePage(String prePage) {
		this.prePage = prePage;
	}

	public String getMenuNm() {
		return menuNm;
	}

	public void setMenuNm(String menuNm) {
		this.menuNm = menuNm;
	}

	public String getContentsId() {
		return contentsId;
	}

	public void setContentsId(String contentsId) {
		this.contentsId = contentsId;
	}

	public String getContentsName() {
		return contentsName;
	}

	public void setContentsName(String contentsName) {
		this.contentsName = contentsName;
	}

	public String getScatId() {
		return scatId;
	}

	public void setScatId(String scatId) {
		this.scatId = scatId;
	}

	public String getScatName() {
		return scatName;
	}

	public void setScatName(String scatName) {
		this.scatName = scatName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}

	public String getApiType() {
		return apiType;
	}

	public void setApiType(String apiType) {
		this.apiType = apiType;
	}

	public String getGenreLarge() {
		return genreLarge;
	}

	public void setGenreLarge(String genreLarge) {
		this.genreLarge = genreLarge;
	}

	public String getGenreMid() {
		return genreMid;
	}

	public void setGenreMid(String genreMid) {
		this.genreMid = genreMid;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getBuyingDate() {
		return buyingDate;
	}

	public void setBuyingDate(String buyingDate) {
		this.buyingDate = buyingDate;
	}

	public String getContsGb() {
		return contsGb;
	}

	public void setContsGb(String contsGb) {
		this.contsGb = contsGb;
	}

	public String getRunTime() {
		return runTime;
	}

	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}

	public String getContinueType() {
		return continueType;
	}

	public void setContinueType(String continueType) {
		this.continueType = continueType;
	}

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public String getContinueTime() {
		return continueTime;
	}

	public void setContinueTime(String continueTime) {
		this.continueTime = continueTime;
	}

	public String getDownSize() {
		return downSize;
	}

	public void setDownSize(String downSize) {
		this.downSize = downSize;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getTermBinver() {
		return termBinver;
	}

	public void setTermBinver(String termBinver) {
		this.termBinver = termBinver;
	}

	public String getBuyingType() {
		return buyingType;
	}

	public void setBuyingType(String buyingType) {
		this.buyingType = buyingType;
	}

	public String getBuyAmt() {
		return buyAmt;
	}

	public void setBuyAmt(String buyAmt) {
		this.buyAmt = buyAmt;
	}

	public String getAlwncecharge() {
		return alwncecharge;
	}

	public void setAlwncecharge(String alwncecharge) {
		this.alwncecharge = alwncecharge;
	}

	public String getBalace() {
		return balace;
	}

	public void setBalace(String balace) {
		this.balace = balace;
	}

	public String getInappYn() {
		return inappYn;
	}

	public void setInappYn(String inappYn) {
		this.inappYn = inappYn;
	}

	public String getInappProdId() {
		return inappProdId;
	}

	public void setInappProdId(String inappProdId) {
		this.inappProdId = inappProdId;
	}

	public String getInappAmt() {
		return inappAmt;
	}

	public void setInappAmt(String inappAmt) {
		this.inappAmt = inappAmt;
	}

	public String getCpnEventId() {
		return cpnEventId;
	}

	public void setCpnEventId(String cpnEventId) {
		this.cpnEventId = cpnEventId;
	}

	public String getBuyingGb() {
		return buyingGb;
	}

	public void setBuyingGb(String buyingGb) {
		this.buyingGb = buyingGb;
	}

	public String getParentCatId() {
		return parentCatId;
	}

	public void setParentCatId(String parentCatId) {
		this.parentCatId = parentCatId;
	}

	public String getDfBuyYn() {
		return dfBuyYn;
	}

	public void setDfBuyYn(String dfBuyYn) {
		this.dfBuyYn = dfBuyYn;
	}

	public String getDfBalace() {
		return dfBalace;
	}

	public void setDfBalace(String dfBalace) {
		this.dfBalace = dfBalace;
	}

	public String getDfPrice() {
		return dfPrice;
	}

	public void setDfPrice(String dfPrice) {
		this.dfPrice = dfPrice;
	}

	public String getDfWatchYn() {
		return dfWatchYn;
	}

	public void setDfWatchYn(String dfWatchYn) {
		this.dfWatchYn = dfWatchYn;
	}

	public String getPrintType() {
		return printType;
	}

	public void setPrintType(String printType) {
		this.printType = printType;
	}

	public String getCpnEventSeq() {
		return cpnEventSeq;
	}

	public void setCpnEventSeq(String cpnEventSeq) {
		this.cpnEventSeq = cpnEventSeq;
	}

	public String getDfAlwnceChargee() {
		return dfAlwnceChargee;
	}

	public void setDfAlwnceChargee(String dfAlwnceChargee) {
		this.dfAlwnceChargee = dfAlwnceChargee;
	}

	
}
