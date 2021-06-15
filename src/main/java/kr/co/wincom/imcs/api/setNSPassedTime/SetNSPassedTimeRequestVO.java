package kr.co.wincom.imcs.api.setNSPassedTime;

import java.io.Serializable;
import java.util.HashMap;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

@SuppressWarnings("serial")
public class SetNSPassedTimeRequestVO extends NoSqlLoggingVO implements Serializable {
	/********************************************************************
	 * setNSPassedTime API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId			= "";	// 가입자정보
	private String stbMac		= "";	// 가입자 STB MAC Address
	private String albumId		= "";	// 앨범 ID
    private String second		= "";	// 이어보기 시간
    private String buyingDate	= "";	// 이어보기 날짜/시간
    private String runTime		= "";	// 총재생시간
    private String keyLog		= "";	// 재생 중 입력 KEY
    private String assetId		= "";	// 대기영상 ASSET_ID
    private String downloadByte	= "";	// 다운로드 BYTE
    private String applType     = "";
    private String buyingGb		= "";	// 구매구분
    private String nWatchYn		= "";	// nScreen(STB) 권한으로 시청 여부
    private String nSaId		= "";	// nScreen(STB) 가입자 번호
    private String nStbMac		= "";	// nScreen(STB) 가입자 맥주소

    
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String pid			= "";
    private String resultCode	= "";
    private String cIdxSa 		= "";
    private String productId	= "";
    private String sysdate		= "";
    
    private String dmlGb       = "";
    

	//2018.12.06 - VR앱 (VR앱일 경우 PT_VO_SET_TIME_PTT 테이블 NSCN_CUST_NO 컬럼에 'V' 아닌경우 'M')
    private String nscnCustNo   = "";
    private String nscnCustNoSub   = "";
    
    private int pIdxSa;
    
    // 2017.08.29 엔스크린(NSCREEN) 기존 모바일과 공용으로 쓸 가번/맥주소
    private String shareSaId	= "";
    private String shareStbMac	= "";
    
    private int iSubChk = 0;
    
    private long tp_start = 0;
    
    //2020.03.13 - 모바일 아이들나라 - 캐릭터관,책읽어주는TV 시청순 정렬을 위해 변수 추가
    private String svcType			= "";
    private String nscnCustNoKids	= "";
    private String catId			= "";
    
    private String albumName		= "";
    private String chkRunTime		= "";
    private String metaRunTime		= "";
    
    public SetNSPassedTimeRequestVO(){}
    
    public SetNSPassedTimeRequestVO(String szParam){
    	CommonService commonService = new CommonService();
    	
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("album_id") == -1 || szTemp.indexOf("second") == -1 
				|| szTemp.indexOf("buying_date") == -1 )
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
				
				if(key.toLowerCase().equals("sa_id"))			this.saId = value;
				if(key.toLowerCase().equals("stb_mac"))			this.stbMac = value;
				if(key.toLowerCase().equals("album_id"))		this.albumId = value;
				if(key.toLowerCase().equals("second"))			this.second = value;
				if(key.toLowerCase().equals("buying_date"))		this.buyingDate = value;
				if(key.toLowerCase().equals("run_time"))		this.runTime = value;
				if(key.toLowerCase().equals("key_log"))			this.keyLog = value;
				if(key.toLowerCase().equals("asset_id"))		this.assetId = value;
				if(key.toLowerCase().equals("download_byte"))	this.downloadByte = value;
				if(key.toLowerCase().equals("buying_gb"))		this.buyingGb = value;
				if(key.toLowerCase().equals("n_watch_yn"))		this.nWatchYn = value;
				if(key.toLowerCase().equals("n_sa_id"))			this.nSaId = value;
				if(key.toLowerCase().equals("n_stb_mac"))		this.nStbMac = value;
				if(key.toLowerCase().equals("app_type"))		this.applType = value;
				if(key.toLowerCase().equals("nscn_cust_no"))	this.nscnCustNo = value;
				if(key.toLowerCase().equals("svc_type"))		this.svcType = value;
				
			}
		}	
		
		//SetNSPassedTimeController.saId = paramMap.get("sa_id");
		
		//SetNSPassedTimeController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
				|| paramMap.get("album_id") == null || paramMap.get("second") == null 
				|| paramMap.get("buying_date") == null)
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
		
		if(!(commonService.getValidParam(this.albumId, 15, 15, 1) || this.albumId.equals("CH")))
		{
			throw new ImcsException();
		}
		
		this.runTime	= StringUtil.nullToSpace(this.runTime);
		this.keyLog	= StringUtil.nullToSpace(this.keyLog);
		this.assetId	= StringUtil.nullToSpace(this.assetId);
		this.downloadByte	= StringUtil.nullToSpace(this.downloadByte);
		this.buyingGb	= StringUtil.nullToSpace(this.buyingGb);
		this.buyingDate = StringUtil.nullToSpace(this.buyingDate);
		
		this.buyingDate = StringUtil.replaceNull(this.buyingDate, "N");
		this.nWatchYn = StringUtil.replaceNull(this.nWatchYn, "N");
		this.applType = StringUtil.replaceNull(this.applType, "RUSA");
		this.svcType = StringUtil.replaceNull(this.svcType, "N");
		
		if(!commonService.getValidParam(this.svcType, 1, 1, 2))
		{
			throw new ImcsException();
		}
		
		if(this.nWatchYn.equals("N"))
		{
			this.shareSaId = this.saId;
			this.shareStbMac = this.stbMac;
		}
		else
		{
			this.shareSaId = this.nSaId;
			this.shareStbMac = this.nStbMac;
		}
		
		if( !"".equals(this.shareSaId) ){
			this.cIdxSa = this.shareSaId.substring(this.shareSaId.length()-2, this.shareSaId.length());
			try {
				this.pIdxSa = Integer.parseInt(this.cIdxSa);
			} catch (Exception e) {
				this.pIdxSa = 0;
			}
		}
		
		if (this.applType == null || this.applType.equals("")) {
			this.applType = "RUSA";
		}
		
		//2018.12.06 VR앱 - APP_TYPE 첫번째가 E인 경우 VR앱 PT_VO_SET_TIME_PTT 테이블 NSCN_CUST_NO 테이블에 'V'로 저장 나머지는 'M'
		if(this.applType.substring(0, 1).equals("E")) {
			this.nscnCustNo = "V";
			this.nscnCustNoSub = "";
		} else if(this.applType.substring(0, 1).equals("F")) {
			this.nscnCustNo = "F";
			this.nscnCustNoSub = "M";
		} else {
			this.nscnCustNo = "M";
			this.nscnCustNoSub = "F";
		}

	    //2018.12.06 VR앱 사용자는 엔스크린 시청 불가능
	    if("E".equals(this.applType.substring(0, 1)) && "Y".equals(this.nWatchYn))
	    {
	    	ImcsException ie = new ImcsException();
	    	String message = String.format("VR앱 사용자는 엔스크린 보기가 지원되지 않습니다.:%s:%s:%s", this.saId, this.stbMac, ImcsConstants.API_PRO_ID189);
	    	ie.setMessage(message);
	    	throw new ImcsException();
			//gf_Logging_New(gst_ss_info.pro_id, ":%s:%s] svc[%-18s] msg[VR앱 사용자는 엔스크린 보기가 지원되지 않습니다.] [%s:%d]\n", rd1.c_sa_id, rd1.c_stb_mac, API_PRO_ID189, __FILE__, __LINE__);
	    }	
	    
	    switch(this.svcType)
	    {
	    	case "N":
	    	case "K":
	    	case "B":
	    	case "G":
	    	case "P":
	    		break;
	    	default:
	    		this.svcType = "N";
	    		break;
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

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public String getSecond() {
		return second;
	}

	public void setSecond(String second) {
		this.second = second;
	}

	public String getBuyingDate() {
		return buyingDate;
	}

	public void setBuyingDate(String buyingDate) {
		this.buyingDate = buyingDate;
	}

	public String getRunTime() {
		return runTime;
	}

	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}

	public String getKeyLog() {
		return keyLog;
	}

	public void setKeyLog(String keyLog) {
		this.keyLog = keyLog;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

	public String getDownloadByte() {
		return downloadByte;
	}

	public void setDownloadByte(String downloadByte) {
		this.downloadByte = downloadByte;
	}

	public String getBuyingGb() {
		return buyingGb;
	}

	public void setBuyingGb(String buyingGb) {
		this.buyingGb = buyingGb;
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

	public String getcIdxSa() {
		return cIdxSa;
	}

	public void setcIdxSa(String cIdxSa) {
		this.cIdxSa = cIdxSa;
	}

	public int getpIdxSa() {
		return pIdxSa;
	}

	public void setpIdxSa(int pIdxSa) {
		this.pIdxSa = pIdxSa;
	}

	public long getTp_start() {
		return tp_start;
	}

	public void setTp_start(long tp_start) {
		this.tp_start = tp_start;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getSysdate() {
		return sysdate;
	}

	public void setSysdate(String sysdate) {
		this.sysdate = sysdate;
	}

	public String getnWatchYn() {
		return nWatchYn;
	}

	public void setnWatchYn(String nWatchYn) {
		this.nWatchYn = nWatchYn;
	}

	public String getnSaId() {
		return nSaId;
	}

	public void setnSaId(String nSaId) {
		this.nSaId = nSaId;
	}

	public String getnStbMac() {
		return nStbMac;
	}

	public void setnStbMac(String nStbMac) {
		this.nStbMac = nStbMac;
	}

	public String getShareSaId() {
		return shareSaId;
	}

	public void setShareSaId(String shareSaId) {
		this.shareSaId = shareSaId;
	}

	public String getShareStbMac() {
		return shareStbMac;
	}

	public void setShareStbMac(String shareStbMac) {
		this.shareStbMac = shareStbMac;
	}


	public String getDmlGb() {
		return dmlGb;
	}

	public void setDmlGb(String dmlGb) {
		this.dmlGb = dmlGb;
	}

	public String getApplType() {
		return applType;
	}

	public void setApplType(String applType) {
		this.applType = applType;
	}

	public String getNscnCustNo() {
		return nscnCustNo;
	}

	public void setNscnCustNo(String nscnCustNo) {
		this.nscnCustNo = nscnCustNo;
	}

	public String getNscnCustNoSub() {
		return nscnCustNoSub;
	}

	public void setNscnCustNoSub(String nscnCustNoSub) {
		this.nscnCustNoSub = nscnCustNoSub;
	}

	public int getiSubChk() {
		return iSubChk;
	}

	public void setiSubChk(int iSubChk) {
		this.iSubChk = iSubChk;
	}

	public String getSvcType() {
		return svcType;
	}

	public void setSvcType(String svcType) {
		this.svcType = svcType;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getMetaRunTime() {
		return metaRunTime;
	}

	public void setMetaRunTime(String metaRunTime) {
		this.metaRunTime = metaRunTime;
	}

	public String getChkRunTime() {
		return chkRunTime;
	}

	public void setChkRunTime(String chkRunTime) {
		this.chkRunTime = chkRunTime;
	}

	public String getNscnCustNoKids() {
		return nscnCustNoKids;
	}

	public void setNscnCustNoKids(String nscnCustNoKids) {
		this.nscnCustNoKids = nscnCustNoKids;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

}
