package kr.co.wincom.imcs.api.getNSSI;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;


@SuppressWarnings("serial")
public class GetNSSIRequestVO  extends NoSqlLoggingVO implements Serializable {

	/********************************************************************
	 *  GetNSVSI API 전문 칼럼(순서 일치)
	********************************************************************/	
    private String saId 				= "";	// 가입자 번호
    private String stbMac 				= "";	// 가입자 STB MAC Address
    private String nscType 				= "";	// N스크린 단말 타입
    private String baseGb 				= "";	// 기지국 코드 구분
    private String baseCd 				= "";	// 기지국 코드
    private String youthYn 				= "";	// 청소년 요금제 여부
    private String orderGb 				= "";	// 정렬 구분 (N:기본, H:인기순, A:제목순)
    
    /********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private String pid					= "";
    private String resultCode			= "";
    private String rBaseCode 			= "";
    private String cBaseCode 			= "";
    private String nodeCd 				= "";
    private String baseOneCd 			= "";
    private String baseCondi 			= "";
    private String pooqYnCom 			= "";
    private String youthYnCom 			= "";
    private String mProdId 				= "";
    private String testSbc 				= "";
    private String dongYn 				= "";
    private String serviceId 			= "";
    private String contentsId 			= "";
    private String stampToday 			= "";
    private String genre1				= "";

    
    private long tp_start = 0;
    
    public GetNSSIRequestVO(){}
    
    public GetNSSIRequestVO(String szParam){
    	String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("nsc_type") == -1 || szTemp.indexOf("base_gb") == -1 
				|| szTemp.indexOf("base_cd") == -1 )
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
				if(key.toLowerCase().equals("nsc_type"))		this.nscType = value;
				if(key.toLowerCase().equals("base_gb"))			this.baseGb = value;
				if(key.toLowerCase().equals("base_cd"))			this.baseCd = value;
				if(key.toLowerCase().equals("youth_yn"))		this.youthYn = value;
				if(key.toLowerCase().equals("order_gb"))		this.orderGb = value;

			}
		}
		
		//GetNSSIController.saId = paramMap.get("sa_id");
		//GetNSSIController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
				|| paramMap.get("nsc_type") == null || paramMap.get("base_gb") == null 
				|| paramMap.get("base_cd") == null )
		{
			throw new ImcsException();
		}
				
		/*if(this.saId.length() > 12 || this.saId.length() < 7 ){
			throw new ImcsException();
		}
		
		if(this.stbMac.length() > 39 || this.stbMac.length() < 14 ){
			throw new ImcsException();
		}*/
		
		this.baseGb = StringUtil.replaceNull(this.baseGb, "N");
		this.youthYn = StringUtil.replaceNull(this.youthYn, "N");
		this.orderGb = StringUtil.replaceNull(this.orderGb, "N");

		
		/*switch (youthYn) {
		case "Y":
		case "N":
			break;

		default:
			throw new ImcsException();
		}
		
		switch (orderGb) {
		case "N":
		case "H":
		case "A":
			break;

		default:
			throw new ImcsException();
		}*/
		
		
		if("Y".equals(this.youthYn))	this.youthYnCom = "1";
		else							this.youthYnCom = "0";
		
		if(this.baseCd != null && !"".equals(this.baseCd)){
	    	this.baseOneCd = this.baseCd.substring(0, 1);
	    }		
		
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDD");
		
		stampToday = sdf.format(today);
		
		/*if(( strcmp(apk_type1,"R")==0 && strcmp(apk_type4,"A")==0 )  && 	(( strcmp(stamp_min, "59")==0 && strncmp(stamp_sec, "5", 1)==0 ) || ( strcmp(stamp_min, "00")==0 && strncmp(stamp_sec, "0", 1)==0 ) || ( strcmp(stamp_min, "00")==0 && strncmp(stamp_sec, "1", 1)==0 ) ))
		    {
		        gettimeofday(&tp_end, NULL);
		        TIMEDIFF_tx(tp_start.tv_sec, tp_start.tv_usec, tp_end.tv_sec, tp_end.tv_usec, rd1.c_sa_id, rd1.c_stb_mac, "[getNSVSI] null return tx_time ", __FILE__, __LINE__);

		        stat_result_print();

		        tpreturn( TPSUCCESS, 0, NULL, 0, 0 );

		    }*/
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

	public String getNscType() {
		return nscType;
	}

	public void setNscType(String nscType) {
		this.nscType = nscType;
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

	public String getYouthYn() {
		return youthYn;
	}

	public void setYouthYn(String youthYn) {
		this.youthYn = youthYn;
	}

	public String getYouthYnCom() {
		return youthYnCom;
	}

	public void setYouthYnCom(String youthYnCom) {
		this.youthYnCom = youthYnCom;
	}

	public String getBaseOneCd() {
		return baseOneCd;
	}

	public void setBaseOneCd(String baseOneCd) {
		this.baseOneCd = baseOneCd;
	}

	public String getPooqYnCom() {
		return pooqYnCom;
	}

	public void setPooqYnCom(String pooqYnCom) {
		this.pooqYnCom = pooqYnCom;
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

	public String getOrderGb() {
		return orderGb;
	}

	public void setOrderGb(String orderGb) {
		this.orderGb = orderGb;
	}

	public long getTp_start() {
		return tp_start;
	}

	public void setTp_start(long tp_start) {
		this.tp_start = tp_start;
	}

	public String getGenre1() {
		return genre1;
	}

	public void setGenre1(String genre1) {
		this.genre1 = genre1;
	}

	public String getmProdId() {
		return mProdId;
	}

	public void setmProdId(String mProdId) {
		this.mProdId = mProdId;
	}

	public String getTestSbc() {
		return testSbc;
	}

	public void setTestSbc(String testSbc) {
		this.testSbc = testSbc;
	}

	public String getrBaseCode() {
		return rBaseCode;
	}

	public void setrBaseCode(String rBaseCode) {
		this.rBaseCode = rBaseCode;
	}

	public String getDongYn() {
		return dongYn;
	}

	public void setDongYn(String dongYn) {
		this.dongYn = dongYn;
	}

	public String getBaseCondi() {
		return baseCondi;
	}

	public void setBaseCondi(String baseCondi) {
		this.baseCondi = baseCondi;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getcBaseCode() {
		return cBaseCode;
	}

	public void setcBaseCode(String cBaseCode) {
		this.cBaseCode = cBaseCode;
	}

	public String getNodeCd() {
		return nodeCd;
	}

	public void setNodeCd(String nodeCd) {
		this.nodeCd = nodeCd;
	}

	public String getContentsId() {
		return contentsId;
	}

	public void setContentsId(String contentsId) {
		this.contentsId = contentsId;
	}

	public String getStampToday() {
		return stampToday;
	}

	public void setStampToday(String stampToday) {
		this.stampToday = stampToday;
	}

}
