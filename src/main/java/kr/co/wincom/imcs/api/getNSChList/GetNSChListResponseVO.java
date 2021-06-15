package kr.co.wincom.imcs.api.getNSChList;

import java.io.Serializable;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;


@SuppressWarnings("serial")
public class GetNSChListResponseVO implements Serializable {
	/********************************************************************
	 * GetNSVSI API 전문 칼럼(순서 일치)
	********************************************************************/
	private String resultType           = "CHNL";
    private String serviceId 			= "";
    private String serviceName 			= "";
    private String serviceEngName 		= "";    
    private String imgUrl 				= "";
    private String imgFileName 			= "";
    private String serviceType 			= "";
    private String pvrYn 				= "";
    private String localAreaCode 		= "";
    private String runningStatus 		= "";
    private String channelNo 			= "";
    private String barkerChannel 		= "";
    private String description 			= "";
    private String sortNo 				= "";
    private String filteringCode 		= "";
    private String maxBitrate 			= "";
    private String favorYn 				= "";    
    private String genre1 				= "";
    private String prodDescUrl 			= "";
    private String virtualType 			= "";
    private String timeAppYn 			= "";
    private String liveTimeServer1 		= "";
    private String liveTimeServer2 		= "";
    private String liveTimeServer3 		= "";
    private String pooqYn 				= "";
    private String pooqGenreName 		= "";
    private String productId 			= "";
    private String productName 			= "";
    private String saveTime 			= "";
    private String startChunk 			= "";
    private String isFhd 				= "";
    private String adultYn	 			= "";
    private String ratings	 			= "";
    private String chnlGrp	 			= "";
    private String isUhd 				= "";
    private String comName 				= "";	// GenreName
    private String chatYn 				= "";
    private String chatId 				= "";
    private String chatName 			= "";
    private String hdtvViewGb 			= "";
    private String serviceRefId 		= "";
    private String adminChFlag  		= "";
    private String subscribeProdId      = "";
    private String subscribeProdNm      = "";
    private String mainServiceId        = "";
    private String cM3u8FileName        = "";
    private String oM3u8FileName        = "";
    private String tscM3u8FileName      = "";
    private String tsoM3u8FileName      = "";
    
	// 2019.02.11 - 4D replay, CJ CHNL
    private String payYn               = "";
    private String Replay4DYn           = "";
    private String cjChnlYn             = "";
    private String cjChnlUrl            = "";
    private String cjChnlCd             = "";
    private String cjReslType           = "";
    // 2019.04.15 - CJ CHNL HEVC 	
 	private String cjHevcChnlYn         = "";
 	private String cjHevcChnlUrl        = "";
 	private String cjHevcReslType       = "";
    
    /********************************************************************
	 * 추가 사용되는 속성값  start
	 ********************************************************************/
    private String liveTimeGb 			= "";
    private String liveTimeServer 		= "";
    private String contentsId 			= "";
    private String castisM3u8File 		= "";
    private String onnuriM3u8File 		= "";
    
    //2018.02.23 - 프로야구2.0/골프APP 변수 추가
    private String mSvcId				= ""; 	// 2018.01.16 - 프로야구2.0 에서 메인/서브채널 개념이 생기면서 시보딜레이ID 변수를 메인서비스ID로 변경
    private String chnlType				= ""; 	// 프로야구에서 서브채널에서 대한 포지션 정보
    private String m3u8Info				= ""; 	// 채널 hevc화질 서비스 여부
//    private String liveServerL 			= "";	// 화질에 대한 CDN 서버 타입 제공
//    private String liveFileNameL		= "";	// 화질 추가에 따른 배열로 m3u8파일명 전달 (local)
//    private String liveServerN 			= "";	// 화질에 대한 CDN 서버 타입 제공
//    private String liveFileNameN		= "";	// 화질 추가에 따른 배열로 m3u8파일명 전달 (near)
//    private String liveServerC 			= "";	// 화질에 대한 CDN 서버 타입 제공
//    private String liveFileNameC		= "";	// 화질 추가에 따른 배열로 m3u8파일명 전달 (center)
//    private String tsFileNameL			= "";	// 화질 추가에 따른 배열로 시보딜레이m3u8파일명 전달 (local)
//    private String tsFileNameN			= "";	// 화질 추가에 따른 배열로 시보딜레이m3u8파일명 전달 (near)
//    private String tsFileNameC			= "";	// 화질 추가에 따른 배열로 시보딜레이m3u8파일명 전달 (center)
    
    //2019.10.11 - IPv6듀얼스택 지원 변수 추가
    private String liveServerNode1   	  = "";
	private String liveServerNode2   	  = "";
	private String liveServerNode3     	  = "";
  	private String liveVodIpv6Server1		  = "";		// VOD IPv6 서버 1
  	private String liveVodIpv6Server2		  = "";		// VOD IPv6 서버 2
  	private String liveVodIpv6Server3		  = "";		// VOD IPv6 서버 3
  	
  	//2020.06.08 - seamless 추가 개발
  	private String iptvCastingYn     	  = "";
    
  	private String subscribeProdInfo	  = "";		//월정액 상품정보
    
  	private String ppvChnlYn			  = "N";
  	
    @Override
    public String toString() {
		StringBuffer sb = new StringBuffer();
		// 몇몇 row의 adult_yn이 " "로 나타나는 경우가 있는데, 모르겠음
		sb.append(StringUtil.nullToSpace(this.getResultType())).append(ImcsConstants.COLSEP);	
		sb.append(StringUtil.nullToSpace(this.getServiceId())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getServiceName())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getServiceEngName())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getImgUrl())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getImgFileName())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getServiceType())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getAdminChFlag())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getSortNo())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getMaxBitrate())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getVirtualType())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getTimeAppYn())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getLiveTimeServer1())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getLiveTimeServer2())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getLiveTimeServer3())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getPooqYn())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getPooqGenreName())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getProductId())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getProductName())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getSaveTime())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getStartChunk())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getIsUhd())).append(ImcsConstants.COLSEP);
		sb.append(this.getAdultYn()).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getRatings())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getChnlGrp())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getIsFhd())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getComName())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getChatYn())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getChatId())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getChatName())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getmSvcId())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getChnlType())).append(ImcsConstants.COLSEP);
		sb.append(StringUtil.nullToSpace(this.getM3u8Info())).append(ImcsConstants.COLSEP);
		sb.append(this.getCastisM3u8File()).append(ImcsConstants.COLSEP);
		sb.append(this.getOnnuriM3u8File()).append(ImcsConstants.COLSEP);
		sb.append(this.getTscM3u8FileName()).append(ImcsConstants.COLSEP);
		sb.append(this.getTsoM3u8FileName()).append(ImcsConstants.COLSEP);
		sb.append(this.getReplay4DYn()).append(ImcsConstants.COLSEP);
		
		// CJ HEVC 이전 버전
//		sb.append(this.getCjChnlYn()).append(ImcsConstants.COLSEP);
//		sb.append(this.getCjChnlUrl()).append(ImcsConstants.COLSEP);
//		sb.append(this.getCjChnlCd()).append(ImcsConstants.COLSEP);
//		sb.append(this.getCjReslType()).append(ImcsConstants.COLSEP);
		
		// CJ HEVC 이후 버전
		sb.append(this.getCjChnlYn()).append(ImcsConstants.COLSEP);
		sb.append(this.getCjChnlUrl()).append(ImcsConstants.COLSEP);
		sb.append(this.getCjHevcChnlUrl()).append(ImcsConstants.COLSEP);
		sb.append(this.getCjReslType()).append(ImcsConstants.COLSEP);
		sb.append(this.getCjHevcReslType()).append(ImcsConstants.COLSEP);
		sb.append(this.getLiveServerNode1()).append(ImcsConstants.COLSEP);
		sb.append(this.getLiveServerNode2()).append(ImcsConstants.COLSEP);
		sb.append(this.getLiveServerNode3()).append(ImcsConstants.COLSEP);
		sb.append(this.getLiveVodIpv6Server1()).append(ImcsConstants.COLSEP);
		sb.append(this.getLiveVodIpv6Server2()).append(ImcsConstants.COLSEP);
		sb.append(this.getLiveVodIpv6Server3()).append(ImcsConstants.COLSEP);
		sb.append(this.getIptvCastingYn()).append(ImcsConstants.COLSEP);
		sb.append(this.getSubscribeProdInfo()).append(ImcsConstants.COLSEP);
		sb.append(this.getPpvChnlYn()).append(ImcsConstants.COLSEP);
		return sb.toString();
	}
    
    public void dataParcing(String[] arrRowResult)
    {
    	for(int j = 0; j < arrRowResult.length; j++)
		{
			switch(j)
			{
				case 0:
					this.setResultType(arrRowResult[j]);
					break;
				case 1:
					this.setServiceId(arrRowResult[j]);
					break;
				case 2:
					this.setServiceName(arrRowResult[j]);
					break;
				case 3:
					this.setServiceEngName(arrRowResult[j]);
					break;
				case 4:
					this.setImgUrl(arrRowResult[j]);
					break;
				case 5:
					this.setImgFileName(arrRowResult[j]);
					break;
				case 6:
					this.setServiceType(arrRowResult[j]);
					break;
				case 7:
					this.setAdminChFlag(arrRowResult[j]);
					break;
				case 8:
					this.setSortNo(arrRowResult[j]);
					break;
				case 9:
					this.setMaxBitrate(arrRowResult[j]);
					break;
				case 10:
					this.setVirtualType(arrRowResult[j]);
					break;
				case 11:
					this.setTimeAppYn(arrRowResult[j]);
					break;
				case 12:
					this.setLiveTimeServer1(arrRowResult[j]);
					break;
				case 13:
					this.setLiveTimeServer2(arrRowResult[j]);
					break;
				case 14:
					this.setLiveTimeServer3(arrRowResult[j]);
					break;
				case 15:
					this.setPooqYn(arrRowResult[j]);
					break;
				case 16:
					this.setPooqGenreName(arrRowResult[j]);
					break;
				case 17:
					this.setProductId(arrRowResult[j]);
					break;
				case 18:
					this.setProductName(arrRowResult[j]);
					break;
				case 19:
					this.setSaveTime(arrRowResult[j]);
					break;
				case 20:
					this.setStartChunk(arrRowResult[j]);
					break;
				case 21:
					this.setIsUhd(arrRowResult[j]);
					break;
				case 22:
					this.setAdultYn(arrRowResult[j]);
					break;
				case 23:
					this.setRatings(arrRowResult[j]);
					break;
				case 24:
					this.setChnlGrp(arrRowResult[j]);
					break;
				case 25:
					this.setIsFhd(arrRowResult[j]);
					break;
				case 26:
					this.setComName(arrRowResult[j]);
					break;
				case 27:
					this.setChatYn(arrRowResult[j]);
					break;
				case 28:
					this.setChatId(arrRowResult[j]);
					break;
				case 29:
					this.setChatName(arrRowResult[j]);
					break;
				case 30:
					this.setmSvcId(arrRowResult[j]);
					break;
				case 31:
					this.setChnlType(arrRowResult[j]);
					break;
				case 32:
					this.setM3u8Info(arrRowResult[j]);
					break;
				case 33:
					this.setCastisM3u8File(arrRowResult[j]);
					break;
				case 34:
					this.setOnnuriM3u8File(arrRowResult[j]);
					break;
				case 35:
					this.setTscM3u8FileName(arrRowResult[j]);
					break;
				case 36:
					this.setTsoM3u8FileName(arrRowResult[j]);
					break;
				case 37:
					this.setReplay4DYn(arrRowResult[j]);
					break;
				case 38:
					this.setCjChnlYn(arrRowResult[j]);
					break;
				case 39:
					this.setCjChnlUrl(arrRowResult[j]);
					break;
				case 40:
					this.setCjHevcChnlUrl(arrRowResult[j]);
					break;
				case 41:
					this.setCjReslType(arrRowResult[j]);
					break;
				case 42:
					this.setCjHevcReslType(arrRowResult[j]);
					break;
				case 43:
					this.setLiveServerNode1(arrRowResult[j]);
					break;
				case 44:
					this.setLiveServerNode2(arrRowResult[j]);
					break;
				case 45:
					this.setLiveServerNode3(arrRowResult[j]);
					break;
				case 46:
					this.setLiveVodIpv6Server1(arrRowResult[j]);
					break;
				case 47:
					this.setLiveVodIpv6Server2(arrRowResult[j]);
					break;
				case 48:
					this.setLiveVodIpv6Server3(arrRowResult[j]);
					break;
				case 49:
					this.setIptvCastingYn(arrRowResult[j]);
					break;
				case 50:
					this.setSubscribeProdInfo(arrRowResult[j]);
					break;
				case 51:
					this.setPpvChnlYn(arrRowResult[j]);
					break;
				default:
					break;
			}
		}
    }
    
	public String getResultType() {
		return resultType;
	}
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServiceEngName() {
		return serviceEngName;
	}
	public void setServiceEngName(String serviceEngName) {
		this.serviceEngName = serviceEngName;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getImgFileName() {
		return imgFileName;
	}
	public void setImgFileName(String imgFileName) {
		this.imgFileName = imgFileName;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getPvrYn() {
		return pvrYn;
	}
	public void setPvrYn(String pvrYn) {
		this.pvrYn = pvrYn;
	}
	public String getLocalAreaCode() {
		return localAreaCode;
	}
	public void setLocalAreaCode(String localAreaCode) {
		this.localAreaCode = localAreaCode;
	}
	public String getRunningStatus() {
		return runningStatus;
	}
	public void setRunningStatus(String runningStatus) {
		this.runningStatus = runningStatus;
	}
	public String getChannelNo() {
		return channelNo;
	}
	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}
	public String getBarkerChannel() {
		return barkerChannel;
	}
	public void setBarkerChannel(String barkerChannel) {
		this.barkerChannel = barkerChannel;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSortNo() {
		return sortNo;
	}
	public void setSortNo(String sortNo) {
		this.sortNo = sortNo;
	}
	public String getFilteringCode() {
		return filteringCode;
	}
	public void setFilteringCode(String filteringCode) {
		this.filteringCode = filteringCode;
	}
	public String getMaxBitrate() {
		return maxBitrate;
	}
	public void setMaxBitrate(String maxBitrate) {
		this.maxBitrate = maxBitrate;
	}
	public String getFavorYn() {
		return favorYn;
	}
	public void setFavorYn(String favorYn) {
		this.favorYn = favorYn;
	}
	public String getGenre1() {
		return genre1;
	}
	public void setGenre1(String genre1) {
		this.genre1 = genre1;
	}
	public String getProdDescUrl() {
		return prodDescUrl;
	}
	public void setProdDescUrl(String prodDescUrl) {
		this.prodDescUrl = prodDescUrl;
	}
	public String getVirtualType() {
		return virtualType;
	}
	public void setVirtualType(String virtualType) {
		this.virtualType = virtualType;
	}
	public String getTimeAppYn() {
		return timeAppYn;
	}
	public void setTimeAppYn(String timeAppYn) {
		this.timeAppYn = timeAppYn;
	}
	public String getLiveTimeServer1() {
		return liveTimeServer1;
	}
	public void setLiveTimeServer1(String liveTimeServer1) {
		this.liveTimeServer1 = liveTimeServer1;
	}
	public String getLiveTimeServer2() {
		return liveTimeServer2;
	}
	public void setLiveTimeServer2(String liveTimeServer2) {
		this.liveTimeServer2 = liveTimeServer2;
	}
	public String getLiveTimeServer3() {
		return liveTimeServer3;
	}
	public void setLiveTimeServer3(String liveTimeServer3) {
		this.liveTimeServer3 = liveTimeServer3;
	}
	public String getPooqYn() {
		return pooqYn;
	}
	public void setPooqYn(String pooqYn) {
		this.pooqYn = pooqYn;
	}
	public String getPooqGenreName() {
		return pooqGenreName;
	}
	public void setPooqGenreName(String pooqGenreName) {
		this.pooqGenreName = pooqGenreName;
	}
	public String getContentsId() {
		return contentsId;
	}
	public void setContentsId(String contentsId) {
		this.contentsId = contentsId;
	}
	public String getSaveTime() {
		return saveTime;
	}
	public void setSaveTime(String saveTime) {
		this.saveTime = saveTime;
	}
	public String getStartChunk() {
		return startChunk;
	}
	public void setStartChunk(String startChunk) {
		this.startChunk = startChunk;
	}
	public String getLiveTimeGb() {
		return liveTimeGb;
	}
	public void setLiveTimeGb(String liveTimeGb) {
		this.liveTimeGb = liveTimeGb;
	}
	public String getLiveTimeServer() {
		return liveTimeServer;
	}
	public void setLiveTimeServer(String liveTimeServer) {
		this.liveTimeServer = liveTimeServer;
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
	public String getIsUhd() {
		return isUhd;
	}
	public void setIsUhd(String isUhd) {
		this.isUhd = isUhd;
	}
	public String getIsFhd() {
		return isFhd;
	}
	public void setIsFhd(String isFhd) {
		this.isFhd = isFhd;
	}
	public String getAdultYn() {
		return adultYn;
	}
	public void setAdultYn(String adultYn) {
		this.adultYn = adultYn;
	}
	public String getRatings() {
		return ratings;
	}
	public void setRatings(String ratings) {
		this.ratings = ratings;
	}
	public String getChnlGrp() {
		return chnlGrp;
	}
	public void setChnlGrp(String chnlGrp) {
		this.chnlGrp = chnlGrp;
	}
	public String getComName() {
		return comName;
	}
	public void setComName(String comName) {
		this.comName = comName;
	}
	public String getChatYn() {
		return chatYn;
	}
	public void setChatYn(String chatYn) {
		this.chatYn = chatYn;
	}
	public String getChatId() {
		return chatId;
	}
	public void setChatId(String chatId) {
		this.chatId = chatId;
	}
	public String getChatName() {
		return chatName;
	}
	public void setChatName(String chatName) {
		this.chatName = chatName;
	}
	public String getHdtvViewGb() {
		return hdtvViewGb;
	}
	public void setHdtvViewGb(String hdtvViewGb) {
		this.hdtvViewGb = hdtvViewGb;
	}
	public String getServiceRefId() {
		return serviceRefId;
	}
	public void setServiceRefId(String serviceRefId) {
		this.serviceRefId = serviceRefId;
	}
	public String getChnlType() {
		return chnlType;
	}
	public void setChnlType(String chnlType) {
		this.chnlType = chnlType;
	}
	public String getM3u8Info() {
		return m3u8Info;
	}
	public void setM3u8Info(String m3u8Info) {
		this.m3u8Info = m3u8Info;
	}

	public String getAdminChFlag() {
		return adminChFlag;
	}

	public void setAdminChFlag(String adminChFlag) {
		this.adminChFlag = adminChFlag;
	}

	public String getSubscribeProdId() {
		return subscribeProdId;
	}

	public void setSubscribeProdId(String subscribeProdId) {
		this.subscribeProdId = subscribeProdId;
	}

	public String getSubscribeProdNm() {
		return subscribeProdNm;
	}

	public void setSubscribeProdNm(String subscribeProdNm) {
		this.subscribeProdNm = subscribeProdNm;
	}

	public String getMainServiceId() {
		return mainServiceId;
	}

	public void setMainServiceId(String mainServiceId) {
		this.mainServiceId = mainServiceId;
	}

	public String getcM3u8FileName() {
		return cM3u8FileName;
	}

	public void setcM3u8FileName(String cM3u8FileName) {
		this.cM3u8FileName = cM3u8FileName;
	}

	public String getoM3u8FileName() {
		return oM3u8FileName;
	}

	public void setoM3u8FileName(String oM3u8FileName) {
		this.oM3u8FileName = oM3u8FileName;
	}

	public String getTscM3u8FileName() {
		return tscM3u8FileName;
	}

	public void setTscM3u8FileName(String tscM3u8FileName) {
		this.tscM3u8FileName = tscM3u8FileName;
	}

	public String getTsoM3u8FileName() {
		return tsoM3u8FileName;
	}

	public void setTsoM3u8FileName(String tsoM3u8FileName) {
		this.tsoM3u8FileName = tsoM3u8FileName;
	}

	public String getmSvcId() {
		return mSvcId;
	}

	public void setmSvcId(String mSvcId) {
		this.mSvcId = mSvcId;
	}

	public String getCastisM3u8File() {
		return castisM3u8File;
	}

	public void setCastisM3u8File(String castisM3u8File) {
		this.castisM3u8File = castisM3u8File;
	}

	public String getOnnuriM3u8File() {
		return onnuriM3u8File;
	}

	public void setOnnuriM3u8File(String onnuriM3u8File) {
		this.onnuriM3u8File = onnuriM3u8File;
	}

	public String getPayYn() {
		return payYn;
	}

	public void setPayYn(String payYn) {
		this.payYn = payYn;
	}

	public String getReplay4DYn() {
		return Replay4DYn;
	}

	public void setReplay4DYn(String replay4dYn) {
		Replay4DYn = replay4dYn;
	}

	public String getCjChnlYn() {
		return cjChnlYn;
	}

	public void setCjChnlYn(String cjChnlYn) {
		this.cjChnlYn = cjChnlYn;
	}

	public String getCjChnlUrl() {
		return cjChnlUrl;
	}

	public void setCjChnlUrl(String cjChnlUrl) {
		this.cjChnlUrl = cjChnlUrl;
	}

	public String getCjChnlCd() {
		return cjChnlCd;
	}

	public void setCjChnlCd(String cjChnlCd) {
		this.cjChnlCd = cjChnlCd;
	}

	public String getCjReslType() {
		return cjReslType;
	}

	public void setCjReslType(String cjReslType) {
		this.cjReslType = cjReslType;
	}

	public String getCjHevcChnlYn() {
		return cjHevcChnlYn;
	}

	public void setCjHevcChnlYn(String cjHevcChnlYn) {
		this.cjHevcChnlYn = cjHevcChnlYn;
	}

	public String getCjHevcChnlUrl() {
		return cjHevcChnlUrl;
	}

	public void setCjHevcChnlUrl(String cjHevcChnlUrl) {
		this.cjHevcChnlUrl = cjHevcChnlUrl;
	}

	public String getCjHevcReslType() {
		return cjHevcReslType;
	}

	public void setCjHevcReslType(String cjHevcReslType) {
		this.cjHevcReslType = cjHevcReslType;
	}

	public String getLiveServerNode1() {
		return liveServerNode1;
	}

	public void setLiveServerNode1(String liveServerNode1) {
		this.liveServerNode1 = liveServerNode1;
	}

	public String getLiveServerNode2() {
		return liveServerNode2;
	}

	public void setLiveServerNode2(String liveServerNode2) {
		this.liveServerNode2 = liveServerNode2;
	}

	public String getLiveServerNode3() {
		return liveServerNode3;
	}

	public void setLiveServerNode3(String liveServerNode3) {
		this.liveServerNode3 = liveServerNode3;
	}

	public String getLiveVodIpv6Server1() {
		return liveVodIpv6Server1;
	}

	public void setLiveVodIpv6Server1(String liveVodIpv6Server1) {
		this.liveVodIpv6Server1 = liveVodIpv6Server1;
	}

	public String getLiveVodIpv6Server2() {
		return liveVodIpv6Server2;
	}

	public void setLiveVodIpv6Server2(String liveVodIpv6Server2) {
		this.liveVodIpv6Server2 = liveVodIpv6Server2;
	}

	public String getLiveVodIpv6Server3() {
		return liveVodIpv6Server3;
	}

	public void setLiveVodIpv6Server3(String liveVodIpv6Server3) {
		this.liveVodIpv6Server3 = liveVodIpv6Server3;
	}

	public String getIptvCastingYn() {
		return iptvCastingYn;
	}

	public void setIptvCastingYn(String iptvCastingYn) {
		this.iptvCastingYn = iptvCastingYn;
	}

	public String getSubscribeProdInfo() {
		return subscribeProdInfo;
	}

	public void setSubscribeProdInfo(String subscribeProdInfo) {
		this.subscribeProdInfo = subscribeProdInfo;
	}

	public String getPpvChnlYn() {
		return ppvChnlYn;
	}

	public void setPpvChnlYn(String ppvChnlYn) {
		this.ppvChnlYn = ppvChnlYn;
	}
	
}
