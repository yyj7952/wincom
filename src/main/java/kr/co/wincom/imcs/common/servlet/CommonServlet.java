//package kr.co.wincom.imcs.common.servlet;
//
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.io.PrintWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Properties;
//
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import kr.co.wincom.imcs.api.addFXFavorite.AddFXFavoriteController;
//import kr.co.wincom.imcs.api.addNSAlert.AddNSAlertController;
//import kr.co.wincom.imcs.api.addNSCHFavor.AddNSCHFavorController;
//import kr.co.wincom.imcs.api.addNSFavorite.AddNSFavoriteController;
//import kr.co.wincom.imcs.api.authorizeNSView.AuthorizeNSViewController;
//import kr.co.wincom.imcs.api.authorizeNView.AuthorizeNViewController;
//import kr.co.wincom.imcs.api.authorizePView.AuthorizePViewController;
//import kr.co.wincom.imcs.api.authorizeVView.AuthorizeVViewController;
//import kr.co.wincom.imcs.api.buyContsCp.BuyContsCpController;
//import kr.co.wincom.imcs.api.buyNSContent.BuyNSContentController;
//import kr.co.wincom.imcs.api.buyNSConts.BuyNSContsController;
//import kr.co.wincom.imcs.api.buyNSDMConts.BuyNSDMContsController;
//import kr.co.wincom.imcs.api.buyNSPresent.BuyNSPresentController;
//import kr.co.wincom.imcs.api.buyNSProduct.BuyNSProductController;
//import kr.co.wincom.imcs.api.chkBuyNSConts.ChkBuyNSContsController;
//import kr.co.wincom.imcs.api.chkBuyNSPG.ChkBuyNSPGController;
//import kr.co.wincom.imcs.api.chkSubscribeProd.ChkSubscribeProdController;
//import kr.co.wincom.imcs.api.getCopyCache.GetCopyCacheController;
//import kr.co.wincom.imcs.api.getFXContStat.GetFXContStatController;
//import kr.co.wincom.imcs.api.getFXFavorGenre.GetFXFavorGenreController;
//import kr.co.wincom.imcs.api.getFXFavorList.GetFXFavorListController;
//import kr.co.wincom.imcs.api.getFXProdInfo.GetFXProdInfoController;
//import kr.co.wincom.imcs.api.getFXRelation.GetFXRelationController;
//import kr.co.wincom.imcs.api.getFXReposited.GetFXRepositedController;
//import kr.co.wincom.imcs.api.getNSAlbumList.GetNSAlbumListController;
//import kr.co.wincom.imcs.api.getNSAlbumStat.GetNSAlbumStatController;
//import kr.co.wincom.imcs.api.getNSAlertList.GetNSAlertListController;
//import kr.co.wincom.imcs.api.getNSBuyList.GetNSBuyListController;
//import kr.co.wincom.imcs.api.getNSCHLists.GetNSCHListsController;
//import kr.co.wincom.imcs.api.getNSCHRank.GetNSCHRankController;
//import kr.co.wincom.imcs.api.getNSCHRatings.GetNSCHRatingsController;
//import kr.co.wincom.imcs.api.getNSCatBillInfo.GetNSCatBillInfoController;
//import kr.co.wincom.imcs.api.getNSChList.GetNSChListController;
//import kr.co.wincom.imcs.api.getNSChList2.GetNSChList2Controller;
//import kr.co.wincom.imcs.api.getNSChPGM.GetNSChPGMController;
//import kr.co.wincom.imcs.api.getNSChPGM2.GetNSChPGM2Controller;
//import kr.co.wincom.imcs.api.getNSChnlPlayIP.GetNSChnlPlayIPController;
//import kr.co.wincom.imcs.api.getNSComBotList.GetNSComBotListController;
//import kr.co.wincom.imcs.api.getNSContDtl.GetNSContDtlController;
//import kr.co.wincom.imcs.api.getNSContInfo.GetNSContInfoController;
//import kr.co.wincom.imcs.api.getNSContList.GetNSContListController;
//import kr.co.wincom.imcs.api.getNSContReview.GetNSContReviewController;
//import kr.co.wincom.imcs.api.getNSContStat.GetNSContStatController;
//import kr.co.wincom.imcs.api.getNSCustInfo.GetNSCustInfoController;
//import kr.co.wincom.imcs.api.getNSDMPurDtl.GetNSDMPurDtlController;
//import kr.co.wincom.imcs.api.getNSEncryptVal.GetNSEncryptValController;
//import kr.co.wincom.imcs.api.getNSFavorList.GetNSFavorListController;
//import kr.co.wincom.imcs.api.getNSGoodsList.GetNSGoodsListController;
//import kr.co.wincom.imcs.api.getNSGuideVod.GetNSGuideVodController;
//import kr.co.wincom.imcs.api.getNSHighLight.GetNSHighLightController;
//import kr.co.wincom.imcs.api.getNSKidsEStudy.GetNSKidsEStudyController;
//import kr.co.wincom.imcs.api.getNSKidsGuide.GetNSKidsGuideController;
//import kr.co.wincom.imcs.api.getNSKidsHome.GetNSKidsHomeController;
//import kr.co.wincom.imcs.api.getNSKidsList.GetNSKidsListController;
//import kr.co.wincom.imcs.api.getNSKidsMenu.GetNSKidsMenuController;
//import kr.co.wincom.imcs.api.getNSKidsWatch.GetNSKidsWatchController;
//import kr.co.wincom.imcs.api.getNSLastWatch.GetNSLastWatchController;
//import kr.co.wincom.imcs.api.getNSLinkTime.GetNSLinkTimeController;
//import kr.co.wincom.imcs.api.getNSLists.GetNSListsController;
//import kr.co.wincom.imcs.api.getNSMainPage.GetNSMainPageController;
//import kr.co.wincom.imcs.api.getNSMainPromo.GetNSMainPromoController;
//import kr.co.wincom.imcs.api.getNSMakeNodeList.GetNSMakeNodeListController;
//import kr.co.wincom.imcs.api.getNSMakePrefIP.GetNSMakePrefIPController;
//import kr.co.wincom.imcs.api.getNSMapInfo.GetNSMapInfoController;
//import kr.co.wincom.imcs.api.getNSMnuList.GetNSMnuListController;
//import kr.co.wincom.imcs.api.getNSMnuList2.GetNSMnuList2Controller;
//import kr.co.wincom.imcs.api.getNSMnuListDtl.GetNSMnuListDtlController;
//import kr.co.wincom.imcs.api.getNSMnuListDtl2.GetNSMnuListDtl2Controller;
//import kr.co.wincom.imcs.api.getNSMultiConts.GetNSMultiContsController;
//import kr.co.wincom.imcs.api.getNSMultiView.GetNSMultiViewController;
//import kr.co.wincom.imcs.api.getNSMusicCue.GetNSMusicCueController;
//import kr.co.wincom.imcs.api.getNSMusicList.GetNSMusicListController;
//import kr.co.wincom.imcs.api.getNSNodeList.GetNSNodeListController;
//import kr.co.wincom.imcs.api.getNSPSI.GetNSPSIController;
//import kr.co.wincom.imcs.api.getNSPageList.GetNSPageListController;
//import kr.co.wincom.imcs.api.getNSPeriod.GetNSPeriodController;
//import kr.co.wincom.imcs.api.getNSPresent.GetNSPresentController;
//import kr.co.wincom.imcs.api.getNSProdCpInfo.GetNSProdCpInfoController;
//import kr.co.wincom.imcs.api.getNSProdinfo.GetNSProdinfoController;
//import kr.co.wincom.imcs.api.getNSPropertyList.GetNSPropertyListController;
//import kr.co.wincom.imcs.api.getNSPurchased.GetNSPurchasedController;
//import kr.co.wincom.imcs.api.getNSReposited.GetNSRepositedController;
//import kr.co.wincom.imcs.api.getNSSI.GetNSSIController;
//import kr.co.wincom.imcs.api.getNSSVODInfo.GetNSSVODInfoController;
//import kr.co.wincom.imcs.api.getNSSeriesList.GetNSSeriesListController;
//import kr.co.wincom.imcs.api.getNSSeriesStat.GetNSSeriesStatController;
//import kr.co.wincom.imcs.api.getNSSimilarList.GetNSSimilarListController;
//import kr.co.wincom.imcs.api.getNSSubCount.GetNSSubCountController;
//import kr.co.wincom.imcs.api.getNSSuggestVOD.GetNSSuggestVODController;
//import kr.co.wincom.imcs.api.getNSVODRank.GetNSVODRankController;
//import kr.co.wincom.imcs.api.getNSVPSI.GetNSVPSIController;
//import kr.co.wincom.imcs.api.getNSVSI.GetNSVSIController;
//import kr.co.wincom.imcs.api.getNSViewInfo.GetNSViewInfoController;
//import kr.co.wincom.imcs.api.getNSVisitDtl.GetNSVisitDtlController;
//import kr.co.wincom.imcs.api.getNSVodPlayIP.GetNSVodPlayIPController;
//import kr.co.wincom.imcs.api.getNSVoteAlbum.GetNSVoteAlbumController;
//import kr.co.wincom.imcs.api.getNSWatchList.GetNSWatchListController;
//import kr.co.wincom.imcs.api.moveNSFavorIdx.MoveNSFavorIdxController;
//import kr.co.wincom.imcs.api.rmFXAllFavor.RmFXAllFavorController;
//import kr.co.wincom.imcs.api.rmFXAllWatchHis.RmFXAllWatchHisController;
//import kr.co.wincom.imcs.api.rmFXFavorite.RmFXFavoriteController;
//import kr.co.wincom.imcs.api.rmFXWatchHis.RmFXWatchHisController;
//import kr.co.wincom.imcs.api.rmNSAlert.RmNSAlertController;
//import kr.co.wincom.imcs.api.rmNSAllAlert.RmNSAllAlertController;
//import kr.co.wincom.imcs.api.rmNSAllCHFavor.RmNSAllCHFavorController;
//import kr.co.wincom.imcs.api.rmNSAllFavor.RmNSAllFavorController;
//import kr.co.wincom.imcs.api.rmNSAllWatchHis.RmNSAllWatchHisController;
//import kr.co.wincom.imcs.api.rmNSCHFavor.RmNSCHFavorController;
//import kr.co.wincom.imcs.api.rmNSFavorite.RmNSFavoriteController;
//import kr.co.wincom.imcs.api.rmNSPresent.RmNSPresentController;
//import kr.co.wincom.imcs.api.rmNSWatchHis.RmNSWatchHisController;
//import kr.co.wincom.imcs.api.searchByNSStr.SearchByNSStrController;
//import kr.co.wincom.imcs.api.setFXFavorGenre.SetFXFavorGenreController;
//import kr.co.wincom.imcs.api.setNSPassedTime.SetNSPassedTimeController;
//import kr.co.wincom.imcs.api.setNSPoint.SetNSPointController;
//import kr.co.wincom.imcs.api.setNSRating.SetNSRatingController;
//import kr.co.wincom.imcs.api.useNSPresent.UseNSPresentController;
//import kr.co.wincom.imcs.common.property.ImcsProperties;
//import kr.co.wincom.imcs.common.util.GlobalCom;
//import kr.co.wincom.imcs.common.util.StringUtil;
//
//@SuppressWarnings("serial")
//@Controller
//public class CommonServlet extends HttpServlet {
//	@Autowired
//	GetNSProdinfoController getNSProdinfoController;
//	@Autowired
//	BuyNSProductController buyNSProductController;
//	@Autowired
//	GetNSSVODInfoController getNSSVODInfoController;
//	@Autowired
//	GetNSViewInfoController getNSViewInfoController;
//	@Autowired
//	GetNSProdCpInfoController getNSProdCpInfoController;
//	@Autowired
//	GetNSVPSIController getNSVPSIController;
//	@Autowired
//	GetNSVSIController getNSVSIController;
//	@Autowired
//	GetNSMultiViewController getNSMultiViewController;
//	@Autowired
//	GetNSCHListsController getNSCHListsController;
//	@Autowired
//	BuyContsCpController buyContsCpController;
//	@Autowired
//	BuyNSContentController buyNSContentController;
//	@Autowired
//	GetNSPurchasedController getNSPurchasedController;
//	@Autowired
//	GetNSRepositedController getNSRepositedController;
//	@Autowired
//	AuthorizePViewController authorizePViewController;
//	@Autowired
//	AuthorizeVViewController authorizeVViewController;
//	@Autowired
//	AuthorizeNViewController authorizeNViewController;
//	@Autowired
//	AuthorizeNSViewController authorizeNSViewController;
//	@Autowired
//	SetNSPassedTimeController setNSPassedTimeController;
//	@Autowired
//	ChkBuyNSPGController chkBuyNSPGController;
//	@Autowired
//	ChkBuyNSContsController chkBuyNSContsController;
//	@Autowired
//	BuyNSPresentController buyNSPresentController;
//	@Autowired
//	GetNSPresentController getNSPresentController;
//	@Autowired
//	RmNSPresentController rmNSPresentController;
//	@Autowired
//	UseNSPresentController useNSPresentController;
//	@Autowired
//	GetNSFavorListController getNSFavorListController;
//	@Autowired
//	RmNSCHFavorController rmNSCHFavorController;
//	@Autowired
//	RmNSWatchHisController rmNSWatchHisController;
//	@Autowired
//	RmNSFavoriteController rmNSFavoriteController;
//	@Autowired
//	RmNSAllCHFavorController rmNSAllCHFavorController;
//	@Autowired
//	RmNSAllWatchHisController rmNSAllWatchHisController;
//	@Autowired
//	RmNSAllFavorController rmNSAllFavorController;
//	@Autowired
//	AddNSCHFavorController addNSCHFavorController;
//	@Autowired
//	AddNSFavoriteController addNSFavoriteController;
//	@Autowired
//	GetNSMainPageController getNSMainPageController;
//	@Autowired
//	GetNSGuideVodController getNSGuideVodController;
//	@Autowired
//	SetNSPointController setNSPointController;
//	@Autowired
//	GetNSCHRankController getNSCHRankController;
//	@Autowired
//	AddNSAlertController addNSAlertController;
//	@Autowired
//	RmNSAlertController rmNSAlertController;
//	@Autowired
//	RmNSAllAlertController rmNSAllAlertController;
//	@Autowired
//	GetNSAlertListController getNSAlertListController;
//	@Autowired
//	GetNSSuggestVODController getNSSuggestVODController;
//	@Autowired
//	SearchByNSStrController searchByNSStrController;
//	@Autowired
//	GetNSAlbumListController getNSAlbumListController;
//	@Autowired
//	GetNSCHRatingsController getNSCHRatingsController;
//	@Autowired
//	GetNSVisitDtlController getNSVisitDtlController;
//	@Autowired
//	GetNSSimilarListController getNSSimilarListController;
//	@Autowired
//	GetNSPeriodController getNSPeriodController;
//	@Autowired
//	GetNSVODRankController getNSVODRankController;
//	@Autowired
//	GetNSListsController getNSListsController;
//	@Autowired
//	GetNSContListController getNSContListController;
//	@Autowired
//	GetNSContDtlController getNSContDtlController;
//	@Autowired
//	GetNSContStatController getNSContStatController;
//	@Autowired
//	SetNSRatingController setNSRatingController;
//	@Autowired
//	BuyNSContsController buyNSContsController;
//	@Autowired
//	GetNSCatBillInfoController getNSCatBillInfoController;
//	@Autowired
//	GetNSSeriesStatController getNSSeriesStatController;
//	@Autowired
//	GetNSMnuListDtlController getNSMnuListDtlController;
//	@Autowired
//	GetNSMnuListDtl2Controller getNSMnuListDtl2Controller;
//	@Autowired
//	GetNSMultiContsController getNSMultiContsController;
//	@Autowired
//	GetNSEncryptValController getNSEncryptValController;
//	@Autowired
//	BuyNSDMContsController buyNSDMContsController;
//	@Autowired
//	GetNSDMPurDtlController getNSDMPurDtlController;
//
//	@Autowired
//	SetFXFavorGenreController setFXFavorGenreController;
//	@Autowired
//	GetFXFavorGenreController getFXFavorGenreController;
//	@Autowired
//	GetFXProdInfoController getFXProdInfoController;
//	@Autowired
//	GetFXRelationController getFXRelationController;
//	@Autowired
//	GetFXContStatController getFXContStatController;
//	@Autowired
//	GetFXFavorListController getFXFavorListController;
//	@Autowired
//	RmFXFavoriteController rmFXFavoriteController; 
//	@Autowired
//	RmFXAllFavorController rmFXAllFavorController; 
//	@Autowired
//	AddFXFavoriteController addFXFavoriteController; 
//	@Autowired
//	GetFXRepositedController getFXRepositedController;
//	@Autowired
//	RmFXWatchHisController rmFXWatchHisController;
//	@Autowired
//	RmFXAllWatchHisController rmFXAllWatchHisController;
//	@Autowired
//	GetNSPageListController getNSPageListController;
//	@Autowired
//	GetNSPSIController getNSPSIController;
//	@Autowired
//	GetNSSIController getNSSIController;
//	@Autowired
//	MoveNSFavorIdxController moveNSFavorIdxController;
//	@Autowired
//	GetNSMainPromoController getNSMainPromoController;
//	@Autowired
//	GetNSBuyListController getNSBuyListController;
//	@Autowired
//	GetNSMusicListController getNSMusicListController;
//	@Autowired
//	GetNSMusicCueController getNSMusicCueController;
//	@Autowired
//	GetNSSeriesListController getNSSeriesListController;
//	@Autowired
//	GetNSContInfoController getNSContInfoController;
//	@Autowired
//	GetNSChListController getNSChListController;
//	@Autowired
//	GetNSChList2Controller getNSChList2Controller;
//	@Autowired
//	GetNSMnuListController getNSMnuListController;
//	@Autowired
//	GetNSMnuList2Controller getNSMnuList2Controller;
//	@Autowired
//	GetNSChPGMController getNSChPGMController;
//	@Autowired
//	GetNSChPGM2Controller getNSChPGM2Controller;
//	@Autowired
//	GetNSMakeNodeListController getNSMakeNodeListController;
//	@Autowired
//	GetNSNodeListController getNSNodeListController;
//	@Autowired
//	GetNSChnlPlayIPController getNSChnlPlayIPController;
//	@Autowired
//	GetNSVodPlayIPController getNSVodPlayIPController;
//	@Autowired
//	GetNSWatchListController getNSWatchListController;
//	@Autowired
//	GetNSCustInfoController getNSCustInfoController;
//	@Autowired
//	GetNSContReviewController getNSContReviewController;
//	@Autowired
//	GetCopyCacheController getCopyCacheController;
//	@Autowired
//	GetNSMakePrefIPController getNSMakePrefIPController;
//	@Autowired
//	GetNSVoteAlbumController getNSVoteAlbumController;
//	
//	/* 2020.01 ????????? ??????????????? */
//	@Autowired
//	GetNSLastWatchController getNSLastWatchController;
//	@Autowired
//	GetNSKidsWatchController getNSKidsWatchController;
//	@Autowired
//	GetNSKidsEStudyController getNSKidsEStudyController;
//	@Autowired
//	GetNSMapInfoController getNSMapInfoController;
//	@Autowired
//	GetNSAlbumStatController getNSAlbumStatController;
//	@Autowired
//	GetNSKidsListController getNSKidsListController;
//	/* 2020.01 ????????? ??????????????? */
//	
//	@Autowired
//	GetNSHighLightController getNSHighLightController;
//	@Autowired
//	GetNSPropertyListController getNSPropertyListController;
//	@Autowired
//	ChkSubscribeProdController chkSubscribeProdController;
//	
//	/* 2020.07 ????????? ??????????????? 4.0 */
//	@Autowired
//	GetNSKidsHomeController getNSKidsHomeController;	
//	@Autowired
//	GetNSSubCountController getNSSubCountController;	
//	@Autowired
//	GetNSKidsMenuController getNSKidsMenuController;	
//	@Autowired
//	GetNSGoodsListController getNSGoodsListController;
//	/* 2020.07 ????????? ??????????????? 4.0 */
//	
//	@Autowired
//	GetNSComBotListController getNSComBotListController;
//	
//	@Autowired
//	GetNSLinkTimeController getNSLinkTimeController;
//	
//	@Autowired
//	GetNSKidsGuideController getNSKidsGuideController;
//	
//	
//    String SERVER_CONF = ImcsProperties.getProperty("filepath.server");
//    Properties serverManager = GlobalCom.getPropertyFile(SERVER_CONF);
//    private String mjdServer = StringUtil.replaceNull(serverManager.getProperty("mjd"),"");
//    private String vts_serverUrl = StringUtil.replaceNull(serverManager.getProperty("bypass_tamx_url"),"http://123.140.17.253"); //Bypass VTS ???????????? URL
//    
//    String SERVER_CONF2 = ImcsProperties.getProperty("filepath.blockapi");
//    Properties serverManager2 = GlobalCom.getPropertyFile(SERVER_CONF2);
//    private String blockApis = StringUtil.replaceNull(serverManager2.getProperty("BLOCK_APIS"),"");
//	
//	String mode = "N";
//	
//	//@ResponseBody
//	//@RequestMapping(value = "/servlets/CommSvl", method = RequestMethod.GET, produces="text/html;charset=UTF-8")
//	@RequestMapping(value = "/servlets/CommSvl", method = RequestMethod.GET)
//	@ResponseBody
//	public void doGet(
//	//public String doGet(
//			@RequestParam(value = "CMD", required=false)		String szCmd,
//			@RequestParam(value = "PARAM", required=false)		String szParam,
//			@RequestParam(value = "STATS", required=false)		String szStat,
//			HttpServletRequest request, HttpServletResponse response) throws Exception
//	{
//		
//		Object obj = ""; 
//		
//		if(request.getRequestURI().equals("/servlets/CommSvl")){
//			//request.setCharacterEncoding("UTF-8");
//		    response.setContentType("text/html;charset=KSC5601");
//		}else if(request.getRequestURI().equals("/servlets/CommSvl_MMI")){
//			//request.setCharacterEncoding("KSC5601");
//			response.setContentType("text/html;charset=KSC5601");
//		}
//		
//		PrintWriter out = null;		
//
//		//blockApi ??????
//		if(blockApis.indexOf(szCmd) == -1){
//			
////			// Regacy ?????? ?????? ??????
////			CommonServletCmd commonServletCmd = new CommonServletCmd(request, response, mode);
////			szParam = commonServletCmd.getServletCmd();
////			//if("".equals(szParam))	return "";
////			
////			this.mode = commonServletCmd.getMode();
////			
////			if("M".equals(this.mode)){
////				
////				Log imcsLogger		= LogFactory.getLog("API_COMMON");
////				
////				if (mode.equals("M")) {
////					if(szCmd.startsWith("n_") || szCmd.startsWith("t_")){
////					}else{
////						szCmd = "m_" + szCmd;
////					}
////				}
////				/* API?????? 15???????? ???????????? */
////				if(szCmd.length() > 15 ){
////					szCmd = szCmd.substring(0, 15);
////				}
////				
////				try{
////	        		// ?????? ????????? ?????? URL ??????
////	                URL url = new URL("http://" + mjdServer + "/servlets/CommSvl?CMD="+ szCmd + "&PARAM=" + szParam);
////	                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
////	                conn.setRequestMethod("POST");
////	                
////	                imcsLogger.info(" [CommSvl] getURL() : " + conn.getURL());	// URL ????????????                
////	                
////	                if(conn.getResponseCode() == 200){
////	                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
////	                    StringBuffer buffer = new StringBuffer();
////	                    String line = null;
////	                    while ((line = reader.readLine()) != null) {
////	                        buffer.append(line).append("\r\n");
////	                    }
////	                    
////	                    reader.close();
////	                    
////	                    imcsLogger.debug(" [CommSvl] responseMessage =" + buffer.toString());	// ?????? ?????? ??????
////	                    String resultValue = buffer.toString();
////	            
////	                    obj = resultValue;
////
////	                }else{ // SA?????? Timeout ????????? ?????? ??????
////	                	imcsLogger.info(" [CommSvl] ????????? ?????? ?????? ??????");
////	                }
////	            }catch(SocketTimeoutException e){
////	                imcsLogger.info(" [CommSvl] ????????? ?????? ?????? ??????");
////	            }
////				
////				
////			}else{
//				
//				if( ("".equals(szStat) || szStat == null)  && (szParam != null && !"".equals(szParam))){
//					if(szParam.indexOf("|STATS=") != -1){
//						
//						String[] strArr = null;
//						
//						strArr = szParam.split("STATS=");
//						
//						if(strArr != null && strArr[1] != null && !"".equals(strArr[1])){
//							szStat = strArr[1];
//							szParam = strArr[0];
//						}						
//					}
//				}
//				 
//				if(szCmd.equals("getNSProdinfo"))		// lgvod004.pc	- ????????? ???????????? ??????
//					obj = getNSProdinfoController.getNSProdinfo(request, response, szParam, szStat);
//				
//				if(szCmd.equals("buyNSProduct"))		// lgvod005.pc	- VOD ??????
//					obj = buyNSProductController.buyNSProduct(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSSVODInfo"))		// lgvod008.pc	- SVOD ???????????? ?????? 
//					obj = getNSSVODInfoController.getNSSVODInfo(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSViewInfo"))		// ???????????? VOD???????????? ?????? ??????
//					obj = getNSViewInfoController.getNSViewInfo(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSProdCpInfo"))		// lgvod014.pc	- ????????? ???????????? ??????
//					obj = getNSProdCpInfoController.getNSProdCpInfo(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSVPSI"))			// lgvod031.pc	- ???????????? EPG?????? ??????????????? ??????
//					obj = getNSVPSIController.getNSVPSI(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSVSI"))			// lgvod032.pc	- ???????????? EPG?????? ???????????? ??????
//					obj = getNSVSIController.getNSVSI(request, response, szParam, szStat);
//				
//				// rmCacheFile
//				
//				if(szCmd.equals("getNSMultiView"))		// lgvod034.pc	- ????????? ???????????? ??????
//					obj = getNSMultiViewController.getNSMultiView(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSCHLists"))		// lgvod035.pc	- ??????????????? ???????????? ?????? 
//					obj = getNSCHListsController.getNSCHLists(request, response, szParam, szStat);
//				
//				if(szCmd.equals("buyContsCp"))			// lgvod108.pc	- VOD ???????????? 
//					obj = buyContsCpController.buyContsCp(request, response, szParam, szStat);
//				
//				if(szCmd.equals("buyNSContent"))		// lgvod109.pc	- VOD ??????
////					obj = buyNSContentController.buyNSContent(request, response, szParam, szStat);
//					// 2019.12.05 - buyNSContent??? VOD?????? ???????????? ?????? ???????????? ???????????? ?????? ??????(buyNSConts ?????? ??????????????? ??? API?????? ??????) ??? buyNSConts??? ??????????????? ??????.
//					obj = buyNSContsController.buyNSConts(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSPurchased"))		// lgvod119.pc	- ?????? ?????? ??????
//					obj = getNSPurchasedController.getNSPurchased(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSReposited"))		// lgvod129.pc	- ???????????? ??????
//					obj = getNSRepositedController.getNSReposited(request, response, szParam, szStat);
//				
//				if(szCmd.equals("authorizePView"))		// lgvod175.pc	- VOD, VOD???????????? 3??? ???????????? ????????? ??????
//					obj = authorizePViewController.authorizePView(request, response, szParam, szStat);
//				
//				if(szCmd.equals("authorizeVView"))		// lgvod177.pc	- ???????????? VOD?????? ???????????? ??? ????????? ??????
//					obj = authorizeVViewController.authorizeVView(request, response, szParam, szStat);
//				
//				if(szCmd.equals("authorizeNView"))		// lgvod178.pc	- ?????? ???????????? ??? ????????? ??????
//					obj = authorizeNViewController.authorizeNView(request, response, szParam, szStat);
//				
//				if(szCmd.equals("authorizeNSView"))		// lgvod179.pc	- SVOD ?????? ???????????? ??? ????????? ??????
//					obj = authorizeNSViewController.authorizeNSView(request, response, szParam, szStat);
//				
//				if(szCmd.equals("setNSPassedTime"))		// lgvod189.pc	- ???????????? ?????? ??????
//					obj = setNSPassedTimeController.setNSPassedTime(request, response, szParam, szStat);
//				
//				if(szCmd.equals("chkBuyNSPG"))			// lgvod258.pc	- VOD ????????? ?????? ??????
//					obj = chkBuyNSPGController.chkBuyNSPG(request, response, szParam, szStat);
//				
//				if(szCmd.equals("chkBuyNSConts"))		// lgvod259.pc	- ???????????? ???????????? ?????? ??????(CA/DRM)
//					obj = chkBuyNSContsController.chkBuyNSConts(request, response, szParam, szStat);
//				
//				if(szCmd.equals("buyNSPresent"))		// lgvod311.pc	- VOD ???????????? 
//					obj = buyNSPresentController.buyNSPresent(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSPresent"))		// lgvod312.pc	- VOD ???????????? ??????
//					obj = getNSPresentController.getNSPresent(request, response, szParam, szStat);
//				
//				if(szCmd.equals("rmNSPresent"))			// lgvod313.pc	- VOD ????????????
//					obj = rmNSPresentController.rmNSPresent(request, response, szParam, szStat);
//				
//				if(szCmd.equals("useNSPresent"))		// lgvod314.pc	- VOD ????????????
//					obj = useNSPresentController.useNSPresent(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSFavorList"))		// lgvod339.pc	- ????????? ????????? ??????
//					obj = getNSFavorListController.getNSFavorList(request, response, szParam, szStat);
//				
//				if(szCmd.equals("rmNSCHFavor"))			// lgvod347.pc	- ?????????????????? ????????? ?????? ????????????
//					obj = rmNSCHFavorController.rmNSCHFavor(request, response, szParam, szStat);
//				
//				if(szCmd.equals("rmNSWatchHis"))		// lgvod348.pc	- ???????????? ????????? ??????
//					obj = rmNSWatchHisController.rmNSWatchHis(request, response, szParam, szStat);
//				
//				if(szCmd.equals("rmNSFavorite"))		// lgvod349.pc	- ????????? ????????? ??????
//					obj = rmNSFavoriteController.rmNSFavorite(request, response, szParam, szStat);
//				
//				if(szCmd.equals("rmNSAllCHFavor"))		// lgvod357.pc	- ???????????? ?????? ??????
//					obj = rmNSAllCHFavorController.rmNSAllCHFavor(request, response, szParam, szStat);
//				
//				if(szCmd.equals("rmNSAllWatchHis"))		// lgvod358.pc	- ???????????? ?????? ????????? ??????
//					obj = rmNSAllWatchHisController.rmNSAllWatchHis(request, response, szParam, szStat);
//				
//				if(szCmd.equals("rmNSAllFavor"))		// lgvod359.pc	- ????????? ?????? ????????? ??????
//					obj = rmNSAllFavorController.rmNSAllFavor(request, response, szParam, szStat);
//				
//				if(szCmd.equals("addNSCHFavor"))		// lgvod377.pc	- ???????????? ??????
//					obj = addNSCHFavorController.addNSCHFavor(request, response, szParam, szStat);
//				
//				if(szCmd.equals("addNSFavorite"))		// lgvod379.pc	- ????????? ????????? ??????
//					obj = addNSFavoriteController.addNSFavorite(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSMainPage"))		// lgvod509.pc	- Phase3 ????????????
//					obj = getNSMainPageController.getNSMainPage(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSGuideVod"))		// lgvod569.pc	- GUIDE VOD ?????? ??????
//					obj = getNSGuideVodController.getNSGuideVod(request, response, szParam, szStat);
//				
//				if(szCmd.equals("setNSPoint"))			// lgvod609.pc	- ?????? ??????
//					obj = setNSPointController.setNSPoint(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSCHRank"))			// lgvod649.pc	- ????????? ???????????? ??????
//					obj = getNSCHRankController.getNSCHRank(request, response, szParam, szStat);
//				
//				if(szCmd.equals("addNSAlert"))			// lgvod661.pc	- ???????????? ?????? 
//					obj = addNSAlertController.addNSAlert(request, response, szParam, szStat);
//
//				if(szCmd.equals("rmNSAlert"))			// lgvod662.pc	- ???????????? ??????
//					obj = rmNSAlertController.rmNSAlert(request, response, szParam, szStat);
//				
//				if(szCmd.equals("rmNSAllAlert"))		// lgvod663.pc	- ???????????? ?????? ??????
//					obj = rmNSAllAlertController.rmNSAllAlert(request, response, szParam, szStat);
//
//				if(szCmd.equals("getNSAlertList"))		// lgvod664.pc	- ???????????? ?????? ?????? 
//					obj = getNSAlertListController.getNSAlertList(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSSuggestVOD"))		// lgvod899.pc	- ?????? VOD??????
//					obj = getNSSuggestVODController.getNSSuggestVOD(request, response, szParam, szStat);
//				
//				if(szCmd.equals("searchByNSStr"))		// lgvod939.pc	- VOD ??? ????????? ?????? ??????
//					obj = searchByNSStrController.searchByNSStr(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSAlbumList"))		// lgvod949.pc	- ???????????? ??? ??????????????? ??????
//					obj = getNSAlbumListController.getNSAlbumList(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSCHRatings")) {		// lgvod982.pc	- ????????? ????????????
//					obj = getNSCHRatingsController.getNSCHRatings(request, response, szParam, szStat);
//				}
//					
//				
//				if(szCmd.equals("getNSVisitDtl"))		// lgvod984.pc	- ??????/????????? ?????? ?????? ??????
//					obj = getNSVisitDtlController.getNSVisitDtl(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSSimilarList") || szCmd.equals("getNSSimilarLis"))	// lgvod985.pc	- ????????? ??????(??????) ?????? ?????? 
//					obj = getNSSimilarListController.getNSSimilarList(request, response, szParam, szStat);
//				
//				// getNSContVod
//				
//				if(szCmd.equals("getNSPeriod"))			// lgvod992.pc	- ????????? ???????????? ?????? ??????
//					obj = getNSPeriodController.getNSPeriod(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSVODRank"))		// lgvod993.pc	- ?????? VOD ?????? (BEST VOD)
//					obj = getNSVODRankController.getNSVODRank(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSLists") || szCmd.equals("getNSMakeLists"))			// lgvod994.pc	- VOD ?????? ????????????/????????? ??????
//					obj = getNSListsController.getNSLists(request, response, szCmd, szParam, szStat);
//				
//				if(szCmd.equals("getNSContList"))		// lgvod995.pc	- VOD ???????????? ?????? ????????? ????????? ??????
//					obj = getNSContListController.getNSContList(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSContDtl"))		// lgvod996.pc	- VOD ????????? ?????? ?????? ??????
//					obj = getNSContDtlController.getNSContDtl(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSContStat"))		// lgvod997.pc	- VOD ???????????? ?????? ??????
//					obj = getNSContStatController.getNSContStat(request, response, szParam, szStat);
//
//				// getNSInfoByDCA
//				
//				if(szCmd.equals("setNSRating"))			// lgvod999.pc	- ?????? ??????
//					obj = setNSRatingController.setNSRating(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSCustInfo"))			// nsvod001.pc	- ?????? ????????? ?????? ??????
//					obj = getNSCustInfoController.getNSCustInfo(request, response, szParam, szStat);
//				
//				if(szCmd.equals("buyNSConts"))			// nsvod010.pc	- VOD ??????
//					obj = buyNSContsController.buyNSConts(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSCatBillInf"))			// nsvod011.pc	- ??????????????? ???????????? ?????? 
//					obj = getNSCatBillInfoController.getNSCatBillInfo(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSSeriesStat"))			// nsvod230.pc	- ??????????????? ?????? ??? ???????????? ??????
//					obj = getNSSeriesStatController.getNSSeriesStat(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSMultiConts"))			// nsvod012.pc	- ???????????? ??????
//					obj = getNSMultiContsController.getNSMultiConts(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSEncryptVal"))			// nsvod013.pc	- ????????? ?????? ??????
//					obj = getNSEncryptValController.getNSEncryptVal(request, response, szParam, szStat);
//				
//				if(szCmd.equals("buyNSDMConts"))			// nsvod014.pc	- ?????? ??????
//					obj = buyNSDMContsController.buyNSDMConts(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSDMPurDtl"))			// nsvod015.pc	- ?????? ?????? ?????? ??????
//					obj = getNSDMPurDtlController.getNSDMPurDtl(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSPageList"))			// nsvod995.pc	- ????????? ????????? ??????
//					obj = getNSPageListController.getNSPageList(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSPSI"))			// lgvod001.pc	- EPG?????? ??????????????? ??????
//					obj = getNSPSIController.getNSPSI(request, response, szParam, szStat);
//				if(szCmd.equals("getNSSI"))			// lgvod002.pc	- EPG?????? ???????????? ??????
//					obj = getNSSIController.getNSSI(request, response, szParam, szStat);
//				
//				if(szCmd.equals("moveNSFavorIdx"))			// lgvod396.pc	- ????????? ?????? ??????
//					obj = moveNSFavorIdxController.moveNSFavorIdx(request, response, szParam, szStat);
//				if(szCmd.equals("getNSMainPromo"))			// lgvod296.pc	- ?????????????????? ??????
//					obj = getNSMainPromoController.getNSMainPromo(request, response, szParam, szStat);
//				
//				
//				if(szCmd.equals("setFXFavorGenre"))			// fxvod010.pc	- ???????????? ??????
//					obj = setFXFavorGenreController.setFXFavorGenre(request, response, szParam, szStat);
//				if(szCmd.equals("getFXFavorGenre"))			// fxvod020.pc	- ???????????? ????????? ??????
//					obj = getFXFavorGenreController.getFXFavorGenre(request, response, szParam, szStat);
//				if(szCmd.equals("getFXProdInfo"))			// fxvod020.pc	- ???????????? ????????? ??????
//					obj = getFXProdInfoController.getFXProdInfo(request, response, szParam, szStat);
//				if(szCmd.equals("getFXRelation"))			// fxvod040.pc	- ??????????????? ????????? ??????
//					obj = getFXRelationController.getFXRelation(request, response, szParam, szStat);
//				if(szCmd.equals("getFXContStat"))			// fxvod040.pc	- ??????????????? ????????? ??????
//					obj = getFXContStatController.getFXContStat(request, response, szParam, szStat);
//				if(szCmd.equals("getFXFavorList"))			// fxvod060.pc	- ????????? ??????
//					obj = getFXFavorListController.getFXFavorList(request, response, szParam, szStat);
//				if(szCmd.equals("rmFXFavorite"))			// fxvod070.pc	- ????????? ??????
//					obj = rmFXFavoriteController.rmFXFavorite(request, response, szParam, szStat);
//				if(szCmd.equals("rmFXAllFavor"))			// fxvod080.pc	- ????????? ????????????
//					obj = rmFXAllFavorController.rmFXAllFavor(request, response, szParam, szStat);
//				if(szCmd.equals("addFXFavorite"))			// fxvod090.pc	- ????????? ??????
//					obj = addFXFavoriteController.addFXFavorite(request, response, szParam, szStat);
//				if(szCmd.equals("getFXReposited"))			// fxvod100.pc	- ???????????? ??????
//					obj = getFXRepositedController.getFXReposited(request, response, szParam, szStat);
//				if(szCmd.equals("rmFXWatchHis"))			// fxvod110.pc	- ???????????? ??????
//					obj = rmFXWatchHisController.rmFXWatchHis(request, response, szParam, szStat);
//				if(szCmd.equals("rmFXAllWatchHis"))			// fxvod120.pc	- ???????????? ????????????
//					obj = rmFXAllWatchHisController.rmFXAllWatchHis(request, response, szParam, szStat);
//				
//				//2018.08.22 ?????? API
//				if(szCmd.equals("getNSBuyList"))			// nsvod110.pc	- ??????????????? ??????
//					obj = getNSBuyListController.getNSBuyList(request, response, szParam, szStat);
//				if(szCmd.equals("getNSMusicList"))			// nsvod030.pc	- ??????????????? ??????
//					obj = getNSMusicListController.getNSMusicList(request, response, szParam, szStat);
//				if(szCmd.equals("getNSMusicCue"))			// nsvod040.pc	- ??????????????? ??????
//					obj = getNSMusicCueController.getNSMusicCue(request, response, szParam, szStat);
//				if(szCmd.equals("getNSSeriesList"))			// nsvod210.pc	- ????????? ????????? ??????
//					obj = getNSSeriesListController.getNSSeriesList(request, response, szParam, szStat);
//				if(szCmd.equals("getNSContInfo"))			// nsvod220.pc	- ????????? ?????? ??????
//					obj = getNSContInfoController.getNSContInfo(request, response, szParam, szStat);
//				if(szCmd.equals("getNSChList"))	{		// nsvod020.pc	- ?????? ????????? ??????
//					obj = getNSChListController.getNSChList(request, response, szParam, szStat);
//					//obj = getNSChList2Controller.getNSChList2(request, response, szParam, szStat);
//				}
//				if(szCmd.equals("getNSChPGM")) {			// nsvod021.pc	- ???????????? ????????? ??????
//					obj = getNSChPGMController.getNSChPGM(request, response, szParam, szStat);
//					//obj = getNSChPGM2Controller.getNSChPGM2(request, response, szParam, szStat);
//				}
//				if(szCmd.equals("getNSNodeList"))			// nsvod050.pc	- CDN ?????? ?????? ????????? ??????
//					obj = getNSNodeListController.getNSNodeList(request, response, szParam, szStat);
//				if(szCmd.equals("getNSMakeNodeLi"))			// nsvod050.pc	- CDN ?????? ?????? ????????? ??????
//					obj = getNSMakeNodeListController.getNSMakeNodeList(request, response, szParam, szStat);
//				if(szCmd.equals("getNSChnlPlayIP"))			// nsvod060.pc	- CDN ?????? ?????? ?????? ??????
//					obj = getNSChnlPlayIPController.getNSChnlPlayIP(request, response, szParam, szStat);
//				if(szCmd.equals("getNSVodPlayIP"))			// nsvod070.pc	- CDN ?????? VOD ?????? ??????
//					obj = getNSVodPlayIPController.getNSVodPlayIP(request, response, szParam, szStat);
//				if(szCmd.equals("getNSWatchList"))			// nsvod710.pc	- ???????????? ??????
//					obj = getNSWatchListController.getNSWatchList(request, response, szParam, szStat);
//				if(szCmd.equals("getNSContReview"))			// nsvod410.pc	- ????????????
//					obj = getNSContReviewController.getNSContReview(request, response, szParam, szStat);
//				if(szCmd.equals("getNSMnuList")) {			// nsvod900.pc	- ?????? ????????? ??????
//					obj = getNSMnuListController.getNSMnuList(request, response, szParam, szStat);
//					//obj = getNSMnuList2Controller.getNSMnuList2(request, response, szParam, szStat);
//				}
//				if(szCmd.equals("getNSMnuListDtl")) {			// nsvod910.pc	-  category list ??????
//					obj = getNSMnuListDtlController.getNSMnuListDtl(request, response, szParam, szStat);
//					//obj = getNSMnuListDtl2Controller.getNSMnuListDtl2(request, response, szParam, szStat);
//				}
//				if(szCmd.equals("getCopyCache"))			// cache001	- ???????????? ??????
//					obj = getCopyCacheController.getCopyCache(request, response, szParam, szStat);
//				if(szCmd.equals("getNSMakePrefIP"))			// nsvod090_w	- IPv6 ?????? PrefixIP ?????? ?????? ??????
//					obj = getNSMakePrefIPController.getNSMakePrefIP(request, response, szParam);
//				if(szCmd.equals("getNSVoteAlbum"))			// nsvod045.pc - ?????????????????? ????????????
//					obj = getNSVoteAlbumController.getNSVoteAlbum(request, response, szParam, szStat);
//				
//				/* 2020.01 ????????? ??????????????? */
//				if(szCmd.equals("getNSLastWatch"))			// ?????? ???????????? ??? ????????? ????????? ???????????? ??????
//					obj = getNSLastWatchController.getNSLastWatch(request, response, szParam, szStat);
//				if(szCmd.equals("getNSKidsWatch"))			// ??????????????? ?????? ??? ?????? ?????? ?????? ??????
//					obj = getNSKidsWatchController.getNSKidsWatch(request, response, szParam, szStat);
//				if(szCmd.equals("getNSKidsEStudy"))			// ??????????????? ???????????? ?????? ?????? ??????
//					obj = getNSKidsEStudyController.getNSKidsEStudy(request, response, szParam, szStat);
//				if(szCmd.equals("getNSMapInfo"))			// ???????????? ?????? ?????? ID??? ???????????? ?????? ?????? ?????? ??? ????????? ?????? ??????
//					obj = getNSMapInfoController.getNSMapInfo(request, response, szParam, szStat);
//				if(szCmd.equals("getNSAlbumStat"))			// ??????????????? ????????? ??????/??????/???????????? ?????? ??????
//					obj = getNSAlbumStatController.getNSAlbumStat(request, response, szParam, szStat);
//				if(szCmd.equals("getNSKidsList"))			// ??????????????? ???????????? ?????? ?????? ??????
//					obj = getNSKidsListController.getNSKidsList(request, response, szParam, szStat);
//				/* 2020.01 ????????? ??????????????? */
//				
//				if(szCmd.equals("getNSHighLight")) {
//					obj = getNSHighLightController.getNSHighLight(request, response, szParam, szStat);
//				}
//				 /*2020.04 ??????????????????*/ 
//				if(szCmd.equals("getNSPropertyList")) {
//					obj = getNSPropertyListController.getNSPropertyList(request, response, szParam, szStat);
//				}
//				
//				if(szCmd.equals("chkSubscribeProd")) {
//					obj = chkSubscribeProdController.chkSubscribeProd(request, response, szParam, szStat);
//				}
//				
//				/* 2020.07 ????????? ??????????????? 4.0 */
//				if(szCmd.equals("getNSKidsHome"))			// KIDS Home ?????? ?????? ?????? (?????? ?????? ??????)
//					obj = getNSKidsHomeController.getNSKidsHome(request, response, szParam, szStat);				
//				if(szCmd.equals("getNSSubCount"))			// ????????? ??????????????? ?????? ???????????? ????????? ??? ????????????
//					obj = getNSSubCountController.getNSSubCount(request, response, szParam, szStat);
//				if(szCmd.equals("getNSKidsMenu"))			// KIDS Home ???????????? ?????? ?????? ??????
//					obj = getNSKidsMenuController.getNSKidsMenu(request, response, szParam, szStat);
//				if(szCmd.equals("getNSGoodsList"))			// KIDS Home ???????????? ?????? ?????? ??????
//					obj = getNSGoodsListController.getNSGoodsList(request, response, szParam, szStat);
//				/* 2020.07 ????????? ??????????????? 4.0 */
//				
//				if(szCmd.equals("getNSComBotList"))			// ??????????????? 4.0 ???????????? ??? ?????? ????????? ??????
//					obj = getNSComBotListController.getNSComBotList(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSLinkTime"))			// ???????????? ?????? ??????
//					obj = getNSLinkTimeController.getNSLinkTime(request, response, szParam, szStat);
//				
//				if(szCmd.equals("getNSKidsGuide"))			// ???????????????
//					obj = getNSKidsGuideController.getNSKidsGuide(request, response, szParam, szStat);
//				
////			}	
//			out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "KSC5601"));
//			out.println(obj.toString());
//			out.close();
//
//		} else {
//			System.out.println("vts bypath: " + szCmd);
//			obj = this.getDataFromVts(request);
//			
//			out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "KSC5601"));
//			out.println(obj.toString());
//			out.close();
//		}
//		//return "";
//	}
//
//	
//
//	//@ResponseBody
//	//@RequestMapping(value = "/servlets/CommSvl", method = RequestMethod.POST, produces="text/plain;charset=UTF-8")
//	@RequestMapping(value = "/servlets/CommSvl", method = RequestMethod.POST)
//	public void doPost(
//			@RequestParam(value = "CMD", required=false)		String szCmd,
//			@RequestParam(value = "PARAM", required=false)		String szParam,
//			@RequestParam(value = "STATS", required=false)		String szStat,
//			HttpServletRequest request, HttpServletResponse response) throws Exception
//	{
//		//Object obj = doGet(szCmd, szParam, szStat, request, response);
//		//return obj.toString();
//		
//		doGet(szCmd, szParam, szStat, request, response);
//	}
//	
//	//@ResponseBody
//	//@RequestMapping(value = "/servlets/CommSvl_MMI", method = RequestMethod.GET, produces="text/html;charset=UTF-8")
//	@RequestMapping(value = "/servlets/CommSvl_MMI", method = RequestMethod.GET)
//	public void doGetMMI(
//			@RequestParam(value = "CMD", required=false)		String szCmd,
//			@RequestParam(value = "PARAM", required=false)		String szParam,
//			@RequestParam(value = "STATS", required=false)		String szStat,
//			HttpServletRequest request, HttpServletResponse response) throws Exception
//	{
//		//Object obj = doGet(szCmd, szParam, szStat, request, response);
//		//return obj.toString();
//		
//		doGet(szCmd, szParam, szStat, request, response);
//	}
//
//	
//
//	//@ResponseBody
//	@RequestMapping(value = "/servlets/CommSvl_MMI", method = RequestMethod.POST, produces="text/plain;charset=UTF-8")
//	public void doPostMMI(
//			@RequestParam(value = "CMD", required=false)		String szCmd,
//			@RequestParam(value = "PARAM", required=false)		String szParam,
//			@RequestParam(value = "STATS", required=false)		String szStat,
//			HttpServletRequest request, HttpServletResponse response) throws Exception
//	{
//		//Object obj = doGet(szCmd, szParam, szStat, request, response);
//		//return obj.toString();
//		
//		doGet(szCmd, szParam, szStat, request, response);
//	}
//	
//	private Object getDataFromVts (HttpServletRequest request) throws IOException {
//
//		//System.out.println("request url = " + request.getRequestURI());
//		//System.out.println("request = " + request.getQueryString());
//		//TMAX ????????????
//		String vts_request = "";
//		URL objurl;
//		HttpURLConnection conn = null;
//		String inputLine;
//		StringBuffer vts_response = null;
//		BufferedReader in = null;
//
//		Object obj = "";
//		
//		try {
//			vts_request = vts_serverUrl + request.getRequestURI() + "?" + request.getQueryString();
//			objurl = new URL(vts_request);
//			//System.out.println("vts_request = " + vts_request); //?????? ?????? ???//		//System.out.println("getHeaderNames = " + request.getHeaderNames());
//			
//			conn = (HttpURLConnection) objurl.openConnection();
//			conn.setRequestProperty("Content-Type", "text/html");
//			conn.setDoOutput(true);
//			conn.setRequestMethod("GET");
//			
//			in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"EUC-KR"));
//			
//			vts_response = new StringBuffer();
//			while ((inputLine = in.readLine()) != null) {
//				vts_response.append(inputLine);
//			}
//			
//		} catch (Exception e) {
//			System.out.println("#################################:" + e.getMessage());
//		} finally {
//			in.close();
//			conn.disconnect();
//		}
//        //conn.disconnect();
//
//        //System.out.println(vts_response.toString()); //??????
//        
//		try {
//			obj = vts_response.toString();
//		} catch (Exception e) {
//			System.out.println("#################################2:" + e.getMessage());
//		} finally {
//			if (vts_response != null)
//				vts_response = null;
//		}
// 
//		
//		//???????????? ?????? ????????????
////		RestTemplate restTemplate = new RestTemplate();
////		String result = "";
////		
////		try {
////			System.out.println(URLDecoder.decode(vts_request, java.nio.charset.StandardCharsets.UTF_8.toString()));
////			result  = restTemplate.getForObject(URLDecoder.decode(vts_request, java.nio.charset.StandardCharsets.UTF_8.toString()), String.class);
////		} catch (Exception e) {
////		}
////		obj = result;
//		
//        return obj;
//	}
//	
//	
//}
