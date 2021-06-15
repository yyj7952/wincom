package kr.co.wincom.imcs.api.getNSDMPurDtl;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.vo.NoSqlLoggingVO;
import kr.co.wincom.imcs.handler.ImcsException;

public class GetNSDMPurDtlRequestVO extends NoSqlLoggingVO implements Serializable
{
	private static final long serialVersionUID = 820027358203692775L;

	/********************************************************************
	 * GetNSDMConts API 전문 칼럼(순서 일치)
	********************************************************************/
	private String saId							= "";
	private String stbMac						= "";
	private String contsId						= "";
	private String buyingDate					= "";
	private String suggestedPrice				= "";
	private String buyingPrice					= "";
	private String datafreePrice				= "";
	private String datafreeBuyPrice				= "";
	private String realBuyingPrice				= "";
	private String realDatafreeBuyPrice			= "";
	private String nBuyYn						= "";
	private String nSaId						= "";
	private String nStbMac						= "";

	
	/********************************************************************
	 * 추가 사용 파라미터
	********************************************************************/
    private Integer pIdxSa		= 0;
    
    private String pid			= "";
    private String resultCode	= "";
    private String cIdxSa		= "";
	
	private Integer resultSet	= 0;
	private Integer messageSet = 0;
	
	private int surtaxRate = 0;
		
	public GetNSDMPurDtlRequestVO(){}
	
	public GetNSDMPurDtlRequestVO(String szParam){
		CommonService commonService = new CommonService();
		
		String[] arrStat = szParam.split("\\|");
		
		String key		= "";
		String value	= "";
		int    nStr		= 0;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		
		/*String szTemp = szParam.toLowerCase();
		
		if( szTemp.indexOf("sa_id") == -1 || szTemp.indexOf("stb_mac") == -1
				|| szTemp.indexOf("conts_id") == -1 || szTemp.indexOf("buying_date") == -1 
				|| szTemp.indexOf("suggested_price") == -1 || szTemp.indexOf("buying_price") == -1
				|| szTemp.indexOf("datafree_price") == -1 || szTemp.indexOf("datafree_buy_price") == -1
				|| szTemp.indexOf("real_buying_price") == -1 || szTemp.indexOf("real_datafree_buy_price") == -1 )
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
				if(key.toLowerCase().equals("conts_id"))		this.setContsId(value);
				if(key.toLowerCase().equals("buying_date"))		this.buyingDate = value;
				if(key.toLowerCase().equals("suggested_price"))	this.suggestedPrice = value;
				if(key.toLowerCase().equals("buying_price"))	this.buyingPrice = value;
				if(key.toLowerCase().equals("datafree_price"))		this.datafreePrice = value;
				if(key.toLowerCase().equals("datafree_buy_price"))		this.datafreeBuyPrice = value;
				if(key.toLowerCase().equals("real_buying_price"))		this.realBuyingPrice = value;
				if(key.toLowerCase().equals("real_datafree_buy_price"))		this.realDatafreeBuyPrice = value;
				if(key.toLowerCase().equals("n_buy_yn"))		this.nBuyYn = value;
				if(key.toLowerCase().equals("n_sa_id"))			this.nSaId = value;
				if(key.toLowerCase().equals("n_stb_mac"))		this.nStbMac = value;
			}
		}
		
		//GetNSDMPurDtlController.saId = paramMap.get("sa_id");
		
		//GetNSDMPurDtlController.stbMac = paramMap.get("stb_mac");
		
		if( paramMap.get("sa_id") == null || paramMap.get("stb_mac") == null
				|| paramMap.get("conts_id") == null || paramMap.get("buying_date") == null 
				|| paramMap.get("suggested_price") == null || paramMap.get("buying_price") == null
				|| paramMap.get("datafree_price") == null || paramMap.get("datafree_buy_price") == null
				|| paramMap.get("real_buying_price") == null || paramMap.get("real_datafree_buy_price") == null )
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
		
		if(!commonService.getValidParam(this.contsId, 5, 15, 1))
		{
			throw new ImcsException();
		}
		
		if(!commonService.getValidParam(this.buyingDate, 14, 14, 3))
		{
			throw new ImcsException();
		}
	    
		if((this.saId.equals("") && this.stbMac.equals("")))
			throw new ImcsException();
		
		if( !"".equals(this.saId) ){
			if(this.getnBuyYn().equals("N")) // 엔스크린인 경우 N_SA_ID 사용
				this.cIdxSa = this.saId.substring(this.saId.length()-2, this.saId.length());
			else
				this.cIdxSa = this.getnSaId().substring(this.getnSaId().length()-2, this.getnSaId().length());
			
			try {
				this.pIdxSa = Integer.parseInt(this.cIdxSa);
			} catch (Exception e) {
				this.pIdxSa = 0;
			}
			
		}
		
	}
	

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public String getContsId() {
		return contsId;
	}

	public void setContsId(String contsId) {
		this.contsId = contsId;
	}

	public int getResultSet() {
		return resultSet;
	}

	public void setResultSet(int resultSet) {
		this.resultSet = resultSet;
	}

	public Integer getpIdxSa() {
		return pIdxSa;
	}

	public void setpIdxSa(Integer pIdxSa) {
		this.pIdxSa = pIdxSa;
	}

	public String getcIdxSa() {
		return cIdxSa;
	}

	public void setcIdxSa(String cIdxSa) {
		this.cIdxSa = cIdxSa;
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

	public Integer getMessageSet() {
		return messageSet;
	}

	public void setMessageSet(Integer messageSet) {
		this.messageSet = messageSet;
	}

	public String getSuggestedPrice() {
		return suggestedPrice;
	}

	public void setSuggestedPrice(String suggestedPrice) {
		this.suggestedPrice = suggestedPrice;
	}

	public String getBuyingPrice() {
		return buyingPrice;
	}

	public void setBuyingPrice(String buyingPrice) {
		this.buyingPrice = buyingPrice;
	}

	public String getDatafreePrice() {
		return datafreePrice;
	}

	public void setDatafreePrice(String datafreePrice) {
		this.datafreePrice = datafreePrice;
	}

	public String getDatafreeBuyPrice() {
		return datafreeBuyPrice;
	}

	public void setDatafreeBuyPrice(String datafreeBuyPrice) {
		this.datafreeBuyPrice = datafreeBuyPrice;
	}

	public String getRealBuyingPrice() {
		return realBuyingPrice;
	}

	public void setRealBuyingPrice(String realBuyingPrice) {
		this.realBuyingPrice = realBuyingPrice;
	}

	public String getRealDatafreeBuyPrice() {
		return realDatafreeBuyPrice;
	}

	public void setRealDatafreeBuyPrice(String realDatafreeBuyPrice) {
		this.realDatafreeBuyPrice = realDatafreeBuyPrice;
	}

	public void setResultSet(Integer resultSet) {
		this.resultSet = resultSet;
	}

	public int getSurtaxRate() {
		return surtaxRate;
	}

	public void setSurtaxRate(int surtaxRate) {
		this.surtaxRate = surtaxRate;
	}

	public String getBuyingDate() {
		return buyingDate;
	}

	public void setBuyingDate(String buyingDate) {
		this.buyingDate = buyingDate;
	}

	public String getnBuyYn() {
		if(StringUtils.isBlank(this.nBuyYn))
			return "N";
		
		return nBuyYn;
	}

	public void setnBuyYn(String nBuyYn) {
		this.nBuyYn = nBuyYn;
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

}
