package kr.co.wincom.imcs.common;

public class ImcsConstants {
	// 분리자
	public static final String ROWSEP		= "\f";		// 행 분리자
	public static final String COLSEP		= "|";		// 열 분리자
	public static final String COLSEP_SPLIT	= "\\|";		// 열 분리자 (캐시파일 내 '|' 열 분리자 Split하기 위해)
	public static final String KEY_VALUE_SPLIT	= "\\=";		// Key, Value Split하기 위해
	public static final String ARRSEP		= "\b";		// 배열 분리자
	public static final String RSSEP		= "!@";		// 레코드셋 분리자
	public static final String REP_ROWSEP	= "\\f";	// 로깅시 치환시킬 행 분리자
	public static final String REP_ARRSEP	= "\\b";	// 로깅시 치환시킬 배열 분리자
	
	// 서비스 연동 결과 메세지
	public static final String SUCCESS_CODE = "0";
	public static final String FAIL_CODE	= "1";
	public static final String BEFORE_BUY	= "2";
	
	// LOG 메세지
	public static final String LOG_MSG1	= "start";
	public static final String LOG_MSG2	= "error";
	public static final String LOG_MSG3	= "end  ";
	
	// 응답 메세지
	public static final String RCV_MSG1	= "request message data error";
	public static final String RCV_MSG2	= "sql error";
	public static final String RCV_MSG3	= "no data found";
	public static final String RCV_MSG4	= "memory allocation error";
	public static final String RCV_MSG5	= "success";
	public static final String RCV_MSG6	= "failure";
	public static final String RCV_MSG7	= "tmax service reboot";
	
	// API_ID
	public static final String API_PRO_ID001	= "001/getNSPSI";
	public static final String API_PRO_ID002	= "002/getNSSI";
	public static final String API_PRO_ID004	= "004/getNSProdInfo";
	public static final String API_PRO_ID005	= "005/buyNSProduct";
	public static final String API_PRO_ID008	= "008/getNSSVODInfo";
	public static final String API_PRO_ID014	= "014/getNSProdCpInfo";
	public static final String API_PRO_ID031	= "031/getNSVPSI";
	public static final String API_PRO_ID031_w	= "031_w/getNSMakeVPSI";
	public static final String API_PRO_ID032	= "032/getNSVSI";
	public static final String API_PRO_ID032_w	= "032_w/getNSMakeVSI";
	public static final String API_PRO_ID033	= "033/rmCacheFile";
	public static final String API_PRO_ID034	= "034/getNSMultiView";
	public static final String API_PRO_ID035	= "035/getNSCHLists";
	public static final String API_PRO_ID108	= "108/buyContsCp";
	public static final String API_PRO_ID109	= "109/buyNSContent";
	public static final String API_PRO_ID119	= "119/getNSPurchased";
	public static final String API_PRO_ID129	= "129/getNSReposited";
	public static final String API_PRO_ID175	= "175/authorizePView";
	public static final String API_PRO_ID177	= "177/authorizeVView";
	public static final String API_PRO_ID178	= "178/authorizeNView";
	public static final String API_PRO_ID179	= "179/authorizeNSView";
	public static final String API_PRO_ID189	= "189/setNSPassedTime";
	public static final String API_PRO_ID258	= "258/chkBuyNSPG";
	public static final String API_PRO_ID259	= "259/chkBuyNSConts";
	public static final String API_PRO_ID269	= "269/getNSMainPromos";
	public static final String API_PRO_ID311	= "311/buyNSPresent";
	public static final String API_PRO_ID312	= "312/getNSPresent";
	public static final String API_PRO_ID313	= "313/rmNSPresent";
	public static final String API_PRO_ID314	= "314/useNSPresent";
	public static final String API_PRO_ID339	= "339/getNSFavorList";
	public static final String API_PRO_ID347	= "347/rmNSCHFavor";
	public static final String API_PRO_ID348	= "348/rmNSWatchHis";
	public static final String API_PRO_ID349	= "349/rmNSFavorite";
	public static final String API_PRO_ID357	= "357/rmNSAllCHFavor";
	public static final String API_PRO_ID358	= "358/rmNSAllWatchHis";
	public static final String API_PRO_ID359	= "359/rmNSAllFavor";
	public static final String API_PRO_ID369	= "369/moveNSFavorIdx";
	public static final String API_PRO_ID377	= "377/addNSCHFavor";
	public static final String API_PRO_ID379	= "379/addNSFavorite";
	public static final String API_PRO_ID509	= "509/getNSMainPage";
	public static final String API_PRO_ID569	= "569/getNSGuideVod";
	public static final String API_PRO_ID609	= "609/setNSPoint";
	public static final String API_PRO_ID649	= "649/getNSCHRank";
	public static final String API_PRO_ID661	= "661/addNSAlert";
	public static final String API_PRO_ID662	= "662/rmNSAlert";
	public static final String API_PRO_ID663	= "663/rmNSAllAlert";
	public static final String API_PRO_ID664	= "664/getNSAlertList";
	public static final String API_PRO_ID829	= "829/getNSMakeLists";
	public static final String API_PRO_ID899	= "899/getNSSuggestVOD";
	public static final String API_PRO_ID939	= "939/searchByNSStr";
	public static final String API_PRO_ID949	= "949/getNSAlbumList";
	public static final String API_PRO_ID982	= "982/getNSCHRatings";
	public static final String API_PRO_ID984	= "984/getNSVisitDtl";
	public static final String API_PRO_ID985	= "985/getNSSimilarList";
	public static final String API_PRO_ID991	= "991/getNSContVod";
	public static final String API_PRO_ID992	= "992/getNSPeriod";
	public static final String API_PRO_ID993	= "993/getNSVODRank";
	public static final String API_PRO_ID994	= "994/getNSLists";
	public static final String API_PRO_ID995	= "995/getNSContList";
	public static final String API_PRO_ID996	= "996/getNSContDtl";
	public static final String API_PRO_ID997	= "997/getNSContStat";
	public static final String API_PRO_ID998	= "998/getNSInfoByDCA";
	public static final String API_PRO_ID999	= "999/setNSRating";
	public static final String API_PRO_ID990	= "990/getNSHighLight";
	public static final String API_PRO_ID989	= "989/getNSLiveStat";
	
	public static final String NSAPI_PRO_ID001	= "001/getNSCustInfo";
	public static final String NSAPI_PRO_ID010	= "010/buyNSConts";
	public static final String NSAPI_PRO_ID011	= "011/getNSCatBillInf";
	public static final String NSAPI_PRO_ID012	= "012/getNSMultiConts";
	public static final String NSAPI_PRO_ID013	= "013/getNSEncryptVal";
	public static final String NSAPI_PRO_ID014	= "014/buyNSDMConts";
	public static final String NSAPI_PRO_ID015	= "015/getNSDMPurDtl";
	public static final String NSAPI_PRO_ID020	= "020/getNSChList";
	public static final String NSAPI_PRO_ID020_w= "020_w/getNSMakeChList";
	public static final String NSAPI_PRO_ID021	= "021/getNSChPGM";
	public static final String NSAPI_PRO_ID021_w= "021_w/getNSMakeChPGM";
	public static final String NSAPI_PRO_ID030	= "030/getNSMusicList";
	public static final String NSAPI_PRO_ID040	= "040/getNSMusicCue";
	public static final String NSAPI_PRO_ID050	= "050/getNSNodeList";
	public static final String NSAPI_PRO_ID050_w= "050_w/getNSMakeNodeList";
	public static final String NSAPI_PRO_ID060  = "060/getNSChnlPlayIP";
	public static final String NSAPI_PRO_ID070  = "070/getNSVodPlayIP";
	public static final String NSAPI_PRO_ID110  = "110/getNSBuyList";
	public static final String NSAPI_PRO_ID210	= "210/getNSSeriesList";
	public static final String NSAPI_PRO_ID220	= "220/getNSContInfo";
	public static final String NSAPI_PRO_ID230	= "230/getNSSeriesStat";
	public static final String NSAPI_PRO_ID410	= "410/getNSContReview";
	public static final String NSAPI_PRO_ID710	= "710/getNSWatchList";
	public static final String NSAPI_PRO_ID900	= "900/getNSMnuList";
	public static final String NSAPI_PRO_ID900_w= "900_w/getNSMnuList";
	public static final String NSAPI_PRO_ID910	= "910/getNSMnuListDtl";
	public static final String NSAPI_PRO_ID995	= "995/getNSMakeMnuList";
	public static final String NSAPI_PRO_ID997	= "997/getNSPageList";
	public static final String NSAPI_PRO_ID090	= "090/getNSPrefixIP";
	public static final String NSAPI_PRO_ID045	= "045/getNSVoteAlbum";
	
	public static final String NSAPI_GETNSVIEWINFO 		= "getNSViewInfo";
	public static final String NSAPI_GETPROPERTYLIST	= "getNSPropertyList";
	public static final String NSAPI_SUBSCRIBEPROD 		= "chkSubscribeProd";
	public static final String NSAPI_GETNSCOMBOTLIST	= "getNSComBotList";
	public static final String NSAPI_GETNSLINKTIME		= "getNSLinkTime";
	public static final String NSAPI_SetNSCHWatchTime	= "SetNSCHWatchTime";
	
	public static final String FXAPI_PRO_ID010	= "01/setFXFavorGenre";
	public static final String FXAPI_PRO_ID020	= "02/getFXFavorGenre";
	public static final String FXAPI_PRO_ID030	= "03/getFXProdInfo";
	public static final String FXAPI_PRO_ID040	= "04/getFXRelation";
	public static final String FXAPI_PRO_ID050	= "05/getFFContStat";		// 안씀
	public static final String FXAPI_PRO_ID060	= "06/getFXFavorList";
	public static final String FXAPI_PRO_ID070	= "07/rmFXFavorite";
	public static final String FXAPI_PRO_ID080	= "08/rmFXAllFavor";
	public static final String FXAPI_PRO_ID090	= "09/addFXFavorite";
	public static final String FXAPI_PRO_ID100	= "10/getFXReposited";
	public static final String FXAPI_PRO_ID110	= "11/rmFXWatchHis";
	public static final String FXAPI_PRO_ID120	= "12/rmFXAllWatchHis";
	public static final String FXAPI_PRO_ID151	= "05/getFXContStat";
	
	
	//결제 수단
	public static final String CP_USE_YN_NORMAL = "N";				// 일반 구매
	public static final String CP_USE_YN_PAYNOW = "W";				// 페이나우 구매
	public static final String CP_USE_YN_CREDITCARD = "T";			// 신용카드 구매
	public static final String CP_USE_YN_OCULUS = "O";				// 오큘러스 구매
	public static final String CP_USE_YN_INAPP = "A";				// 인앱 결제
	public static final String CP_USE_YN_NAVER = "B";				// 네이버pay 결제
	public static final String CP_USE_YN_KAKAO = "F";				// 카카오pay 결제
	
	//할인 수단
	public static final String CP_USE_YN_COUPON = "C";				// IPTV 구 쿠폰 할인
	public static final String CP_USE_YN_PREPAY = "S";
	public static final String CP_USE_YN_TVPOINT = "Q";				// TV포인트	
	public static final String CP_USE_YN_INNERCOUPON = "K";			// 자사 쿠폰
	public static final String CP_USE_YN_OTHERCOUPON = "I";			// 타사 쿠폰
	public static final String CP_USE_YN_MEMBERSHIP = "H";			// 멤버십 할인	
	public static final String CP_USE_YN_KLUPOINT = "L";			// KLU포인트 
	
	
	//할인 가능 개수
	public static final int DISCOUNT_CNT = 4;
	
	//DISCOUNT_DIV 순서별
	public static final int DISCOUNT_DIV_COUPON = 0;
	public static final int DISCOUNT_DIV_MEMBERSHIP = 1;
	public static final int DISCOUNT_DIV_TVPOINT = 2;
	public static final int DISCOUNT_DIV_KLUPOINT = 3;
	
	public static final String API_COPYCACHE_ID001	= "001/getCopyCache";
	public static final String NSAPI_PRO_ID9002	= "900/getNSMnuList";
	
	public static final String N_SP_PTN = "[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝|\\.|\\,]*";
	
}
