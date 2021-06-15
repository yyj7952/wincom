package kr.co.wincom.imcs.api.authorizeNSView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import kr.co.wincom.imcs.api.authorizeNSView.AuthorizeNSViewResultVO.authorizeSView;
import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComCpnVO;
import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.FmInfoVO;
import kr.co.wincom.imcs.common.vo.GenreInfoVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeNSViewServiceImpl implements AuthorizeNSViewService {
	
	private Log imcsLogger		= LogFactory.getLog("API_authorizeNSView");
//	private IMCSLog imcsLog		= null;
	
	
	@Autowired
	private AuthorizeNSViewDao authorizeNSViewDao;
	
	@Autowired
	private CommonService commonService;
	
	
	private final int SUCCESS			= 0;
	private final int FAILURE			= -1;

	private final int NORMAL_WIFI		= 0;
	private final int NORMAL_LTE		= 1;
	private final int NORMAL_5G_W		= 2;
	private final int NORMAL_5G_L		= 3;
	private final int NORMAL_4D_W		= 4;
	private final int NORMAL_4D_L		= 5;
	private final int NORMAL_AWS		= 6;
	private final int NORMAL_MP_MUSIC_W = 7;
	private final int NORMAL_MP_MUSIC_L = 8;
	private final int SVC_R				= 10;
	
	private int [] posArr = new int[100];
	
	private static String regexIPv4 = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$";
	private static String regexIPv6 = "^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$";
	private static String regexIPv4andIPv6 = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$";
	

//	public void authorizeNSView(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
	
	
	
	/**
	 * @author KIM
	 * @since 2019-05-09
	 */
	@Override
	public AuthorizeNSViewResultVO authorizeNSView(AuthorizeNSViewRequestVO paramVO) {
//		this.authorizeNSView(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodName = stackTraceElement.getMethodName();
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		
		imcsLog.debugLog(methodName + " service call");
		
		int nMainCnt		= 0;
		int nSubCnt			= 0;		
		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2
		String msg			= "";
		
		List<AuthorizeNSCateVO> lstCatInfoVO	= null;
		AuthorizeNSCateVO catInfoVO				= null;
		List<String> listCustomSubProd			= null;
		HashMap<String, String> mapSvodPkg		= null;
		
		GenreInfoVO genreInfoVO					= new GenreInfoVO();
		AuthorizeNSViewResultVO resultVO		= new AuthorizeNSViewResultVO();
		
		resultVO.setResultSet(SUCCESS);
		resultVO.setDataChk(0);
		resultVO.setViewType(paramVO.getViewType() );
		
		try {									
			tp1	= System.currentTimeMillis();
			// 2019.10.04 - IPv6듀얼스택지원 : IPv6PrefixIP 정보 조회
			List<String> IPv6Prefix = commonService.getIPv6PrefixIP(ImcsConstants.NSAPI_PRO_ID090.split("/")[1], "L", imcsLog);
			paramVO.setPrefixInternet(IPv6Prefix.get(0));
			paramVO.setPrefixUplushdtv(IPv6Prefix.get(1));
						
			listCustomSubProd = getCustomProduct(paramVO);
			
			lstCatInfoVO = getAlbumProduct(paramVO);
			if(lstCatInfoVO == null) throw new ImcsException();	
			else  nMainCnt	= lstCatInfoVO.size();

			mapSvodPkg = new HashMap<String, String>();
			for(int i = 0; i < nMainCnt; i++) {
				
				catInfoVO	= lstCatInfoVO.get(i);
				mapSvodPkg.put("productId", catInfoVO.getProdId() );
				mapSvodPkg.put("svodPkg", catInfoVO.getCatType() );
				mapSvodPkg.put("categoryLevel", catInfoVO.getCatLevel() );
				
				paramVO.setCategoryId(catInfoVO.getCatId() );
				paramVO.setCategoryName(catInfoVO.getCatName() );
				paramVO.setCategoryIdSvod(catInfoVO.getCatIdSvod() );
				paramVO.setCategoryNameSvod(catInfoVO.getCatNameSvod() );
			
				if( i == (nMainCnt - 1) ) {
					resultVO.setScatId(paramVO.getCategoryIdSvod() );		// 통계 로그 상위_카테고리 ID 추가
					resultVO.setScatName(paramVO.getCategoryNameSvod() );	// 통계 로그 상위_카테고리 명 추가
					resultVO.setCatId(paramVO.getCategoryId() );			// 통계 로그 시리즈 카테고리 ID 추가
					resultVO.setCatName(paramVO.getCategoryName() );		// 통계 로그 시리즈 카테고리 명 추가
				}

				paramVO.setPayYn("Y");
				
				List<ContTypeVO> listContTypeVO = getAlbumProductRelation(paramVO);
				if(null != listContTypeVO) {
					
					int nUdrProdChoice	= 0;
					ContTypeVO item		= null;
					nSubCnt = listContTypeVO.size();
					
					for( int j = 0; j < nSubCnt; j++ ) {
						
						item = listContTypeVO.get(j);
						paramVO.setProductType(item.getImcsProductType() );
						paramVO.setProductIdSub(item.getpProductId() );
						
						if("31030".equals(item.getProductCd()) ) {
							paramVO.setPayYn("N");
							paramVO.setPpmProdFlag("Y");
						}
						
						if("0".equals(paramVO.getProductType()) ) {
							
							paramVO.setPayYn("N");
							paramVO.setFreeProdFlag("Y");
							if(1 == nSubCnt) {
								nSubCnt--;
							}
						}
						
						for(int n = 0; n < listCustomSubProd.size(); n++ ) {
							
							if(paramVO.getProductIdSub().equals(listCustomSubProd.get(n))  ) {
								
								if( ("PPM".equals(mapSvodPkg.get("svodPkg").substring(0,  3))		&& 
									listCustomSubProd.get(n).equals(mapSvodPkg.get("productId")))	|| 
									"SVOD".equals(mapSvodPkg.get("svodPkg").length() == 3 ? mapSvodPkg.get("svodPkg").substring(0,  3) : mapSvodPkg.get("svodPkg").substring(0,  4)) ) {
									
									if(0 == nUdrProdChoice ) {
										
										resultVO.setDataChk(Integer.parseInt(item.getDataChk()) );
										paramVO.setProductId(item.getProductCd() );
										paramVO.setUflixFlag(item.getUflixFlag() );
										nUdrProdChoice = 1;
									}
								}
							}
						} // end of "for(int n = 0; i < listCustomSubProd.size(); n++ )"
					} // end of "for( int j = 0; j < nSubCnt; j++ )"
				} // end of "if(null != listContTypeVO)"
				 
				
				if(( 0 == resultVO.getDataChk()  || 0 == nSubCnt ) && "SVOD".equals(mapSvodPkg.get("svodPkg").length() == 3 ? mapSvodPkg.get("svodPkg").substring(0,  3) : mapSvodPkg.get("svodPkg").substring(0,  4))  ) {
					
					HashMap<String, String> mProductBuy = getProductBuy(paramVO); 
					if(null != mProductBuy) {
						
						resultVO.setDataChk(Integer.parseInt(mProductBuy.get("DATA_CHK")) );
						paramVO.setProductId(mProductBuy.get("PRODUCT_ID") );
					}
				}
				
				if("SVOD".equals(mapSvodPkg.get("svodPkg").length() == 3 ? mapSvodPkg.get("svodPkg").substring(0,  3) : mapSvodPkg.get("svodPkg").substring(0,  4)) )		{ paramVO.setProductType("3"); } // SVOD
				else if("PPM".equals(mapSvodPkg.get("svodPkg").substring(0,  3)))	{ paramVO.setProductType("7"); } // PPM(월정액)
			} // end of "for(int i = 0; i < nMainCnt; i++)"

			
			if("N".equals(paramVO.getFxType())  ) {
				
				if("Y".equals(paramVO.getUflixFlag()) ) {
					switch(paramVO.getReqType()) {
					case "N": paramVO.setFxType("H"); break;
					case "U":
					case "6":
						// 2017.10.31 - 정산개선 (비디오포털에서 UFLIX입점관 진입시 유플릭스APP에서 진입한 것과 구분하고 싶다는 요청으로 인해 수정)
						// 비디오포털 UFLIX는 H -> U로 수정
						paramVO.setFxType("U"); break;
					default: paramVO.setFxType("H"); break;
					}
				}
			}else if("H".equals(paramVO.getFxType()) ) {
				
				if("N".equals(paramVO.getUflixFlag()) ) {
					paramVO.setFxType("N");
				}
			}
			
			tp2 = System.currentTimeMillis();
			imcsLog.timeLog("가입 여부check", String.valueOf(tp2 - tp1), methodName, methodLine);  
			
			if( 1 == resultVO.getDataChk() ) {
				
				String waterMarkYn = getWaterMarkYn(paramVO);
				if(null != waterMarkYn && !waterMarkYn.isEmpty() ) {
					paramVO.setCatWatermark(waterMarkYn);
				}
				
				String capYn = "";
				nMainCnt = 0;
				HashMap<String, String> mapAlbumInfo = getAlbumInfo(paramVO);
				if(null == mapAlbumInfo) { throw new ImcsException(); }
				else  {
					
					resultVO.setAlbumInfo(mapAlbumInfo);
					paramVO.setFourdReplayYn(resultVO.getFourdReplayYn());
					paramVO.setCpId(resultVO.getCpId() );
					
					capYn = Integer.toString(resultVO.getCapYn() );
					
					// 4D 컨텐츠의 경우 다운로드 기능을 지원하지 않는다.
					if("D".equals(paramVO.getViewType()) && "Y".equals(resultVO.getFourdReplayYn()) ) {
						resultVO.setResultSet(FAILURE);
					}
					
					// 다국어 자막 관련 로직
					if("Y".equals(paramVO.getDecPosYn()) && resultVO.getCapFile2Name().length() != 0 && "Y".equals(resultVO.getCapFileEncryptYn())  ) {
						
						// 신 자막 제공
						// 자막 이름과 자막 사이즈를 신 자막으로 제공
						resultVO.setCapFileName(resultVO.getCapFile2Name() );
						resultVO.setCapFileSize(resultVO.getCapFile2Size() );
					} else {
						
						// 구 자막 제공
		            	// 자막 언어를 한국어와 영어에 한해서 제공하기 위하여 데이터 가공
						resultVO.setCapFileEncryptYn("N");
						
						String smiLanguage = "";
						if(resultVO.getSmiLanguage().indexOf("한국어") != -1)	{ smiLanguage = "한국어;"; }
						if(resultVO.getSmiLanguage().indexOf("영어") != -1)		{ smiLanguage = smiLanguage + "영어;"; }
						if(!smiLanguage.isEmpty()) 								{ resultVO.setSmiLanguage(smiLanguage); }
					}
				}
				
				tp2 = System.currentTimeMillis();
				imcsLog.timeLog("자막파일 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
				                                                                  
				if("Y".equals(resultVO.getContWatermark()) ||  "Y".equals(paramVO.getCatWatermark()) ) {
					resultVO.setWatermarkYn("Y");
				} else {
					resultVO.setWatermarkYn("N");
				}
				
				if("Y".equals(resultVO.getSmiYn()) && "Y".equals(resultVO.getSmiImpYn()) ) {
					
					// 자막파일 url정보 조회
					try {
						String capUrl = commonService.getIpInfo("cap_server", ImcsConstants.API_PRO_ID179.split("/")[1]);
						resultVO.setCapUrl(capUrl);
					} catch(Exception e) {
						
						imcsLog.failLog(ImcsConstants.API_PRO_ID179, "", null, "smi_svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
						paramVO.setResultCode("31000000");
						throw new ImcsException();
					}
					
					tp2 = System.currentTimeMillis();
					imcsLog.timeLog("자막파일 경로 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
				}
				else
				{
					//2019.07.22 - 암호화 자막 정보 및 다국어자막어학여부도 SMI_YN이 N일 경우에는 응답값으로 내려주지 않는다.
					resultVO.setCapUrl("");
					resultVO.setCapFileName("");
					resultVO.setCapFileSize("");
					resultVO.setSmiLanguage("");
					resultVO.setCapFileEncryptYn("N");
					resultVO.setCapFileLanguageYn("N");
				}
				
				tp1 = System.currentTimeMillis();
				
				// 이어보기 시간정보 조회 (LinkTime)
				String linkTime = authorizeNSViewDao.getLinkTime(paramVO);
				if(null == linkTime || linkTime.isEmpty()){
					resultVO.setLinkTime("0"); 
				} else {
					resultVO.setLinkTime(linkTime); 
					paramVO.setLinkChk(1);
				}
				
				tp2 = System.currentTimeMillis();
				imcsLog.timeLog("SELETE PT_VO_SET_TIME_PTT_NSC", String.valueOf(tp2 - tp1), methodName, methodLine);
				
				// 시청이력 등록
				if( SUCCESS == resultVO.getResultSet() ) {
					
					// 장르 정보 조회
					tp1 = System.currentTimeMillis();
					genreInfoVO = getGenreString(paramVO); 
					if(null != genreInfoVO  ) {
						
						resultVO.setGenreLarge(genreInfoVO.getGenreLarge() );
						resultVO.setGenreMid(genreInfoVO.getGenreMid() );
						paramVO.setGenreSmall(genreInfoVO.getGenreSmall() );
						paramVO.setSuggestedPrice(genreInfoVO.getSuggestedPrice() );
						paramVO.setTerrYn(genreInfoVO.getTerrYn() );
						paramVO.setTerrPeriod(genreInfoVO.getTerrPeriod() );
						paramVO.setTerrEddate(genreInfoVO.getTerrEdDate() );
						paramVO.setOnairDate(genreInfoVO.getOnairDate() );
						paramVO.setPreviewPeriod(genreInfoVO.getPreviewPeriod() );
						paramVO.setGenreInfo(genreInfoVO.getSzGenreInfo() );
						
						// 2019.11.01 - VOD 정산 프로세스 개선 : 메타 정보 저장 변수 Set
						paramVO.setAssetName(genreInfoVO.getAssetName());
						paramVO.setRatingCd(genreInfoVO.getRatingCd());
						paramVO.setRunTime(genreInfoVO.getRunTime());
						paramVO.setCpIdUflix(genreInfoVO.getCpIdUflix());
						paramVO.setSeriesNo(genreInfoVO.getSeriesNo());
						
						if(paramVO.getViewType().equals("D"))
						{
							paramVO.setContentFilesize(genreInfoVO.getContentFilesize());
						}
					} else {
						tp2 = System.currentTimeMillis();
						imcsLog.timeLog("장르 정보 조회 실패", String.valueOf(tp2 - tp1), methodName, methodLine);
					}
					
					// 시청이력 등록
					if(SUCCESS != setWatchHist(paramVO) ) {
						resultVO.setResultSet(FAILURE);
					} else {
						tp2 = System.currentTimeMillis();
						imcsLog.timeLog("시청이력 등록", String.valueOf(tp2 - tp1), methodName, methodLine);
					}
					
					// for test
					//Thread.sleep(1300);
					
				} // end of "if( SUCCESS == resultVO.getResultSet() )"
				
				// 구간점프 이미지 조회
				try {
					resultVO.setImgUrl(commonService.getIpInfo("vod_zip_server", ImcsConstants.API_PRO_ID179.split("/")[1]));
				} catch(Exception e) {
					
					imcsLog.failLog(ImcsConstants.API_PRO_ID179, "", null, "thumbnail_svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
					paramVO.setResultCode("31000000");
					throw new ImcsException();
				}
				
				List<HashMap<String, String>> listAlbumPoster = getAlbumPoster(paramVO);
				nMainCnt = listAlbumPoster.size();
				if(0 == nMainCnt) {
					imcsLog.failLog(ImcsConstants.API_PRO_ID179, "", null, "getAlbumPoster:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				} else {
					
					HashMap<String, String> mapItem = null;
					for( int i = 0; i < nMainCnt; i++ ) {
						
						mapItem = listAlbumPoster.get(i);
						String imgType = mapItem.get("POSTER_TYPE");
						if("Z".equals(imgType) ) {
							
							resultVO.setImgFileName(mapItem.get("CONTENT_VALUE") );
							resultVO.setTimeInfo(mapItem.get("SECOND_VALUE"));
							
						} else if("X".equals(imgType) ) {
							resultVO.setImgFileName6S(mapItem.get("CONTENT_VALUE") );
						}
					}
				}
				
				tp2 = System.currentTimeMillis();
				imcsLog.timeLog("구간점프 이미지 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
				
				
			} // end of "if( 1 == paramVO.getDataChk() )"
			else {
				imcsLog.failLog(ImcsConstants.API_PRO_ID179, "", "", "buy_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			}
			
			if(SUCCESS ==  resultVO.getResultSet() && 1 == resultVO.getDataChk() ) {
				
				resultVO.setTmpSndBuf(String.format("%s0|%s%s", resultVO.getTmpSndBuf(), paramVO.getSaId(), paramVO.getAlbumId()) );							
				
				if("D".equals(resultVO.getViewType()) ) {
					
					String liveHevcFileName = getLiveHevcFileName(paramVO);
					if(null != liveHevcFileName && !liveHevcFileName.isEmpty() ) {
						
						resultVO.setLiveHevcFileName(liveHevcFileName);
						paramVO.setBitRate("M9");
						
						// 2019.02.28 - 다운로드 M9 5G 또는 AWS 노드를 찾는다.
						if("Y".equals(resultVO.getMusicYn()) && "Y".equals(resultVO.getVrYn()) ) {
							
							// 5G 노드를 찾는다.
							searchCdnIp(0, "F", paramVO, resultVO );
							
							resultVO.setLiveHevcServer1(resultVO.listAuthorizeSView[0].vodServer1 );
							resultVO.setLiveHevcServer2(resultVO.listAuthorizeSView[0].vodServer2 );
							resultVO.setLiveHevcServer3(resultVO.listAuthorizeSView[0].vodServer3 );
							resultVO.setVodIpv6Node1(resultVO.listAuthorizeSView[0].vodIpv6Node1 );
							resultVO.setVodIpv6Node2(resultVO.listAuthorizeSView[0].vodIpv6Node2 );
							resultVO.setVodIpv6Node3(resultVO.listAuthorizeSView[0].vodIpv6Node3 );
							resultVO.setRealHdIpv6Server1(resultVO.listAuthorizeSView[0].vodIpv6Server1 );
							resultVO.setRealHdIpv6Server2(resultVO.listAuthorizeSView[0].vodIpv6Server2 );
							resultVO.setRealHdIpv6Server3(resultVO.listAuthorizeSView[0].vodIpv6Server3 );
							
						} else if("Y".equals(resultVO.getVrYn()) ) {
							
							// AWS(아마존) 노드를 찾는다
							searchCdnIp(0, "Z", paramVO, resultVO );
							
							resultVO.setLiveHevcServer1(resultVO.listAuthorizeSView[0].vodServer1 );
							resultVO.setLiveHevcServer2(resultVO.listAuthorizeSView[0].vodServer2 );
							resultVO.setLiveHevcServer3(resultVO.listAuthorizeSView[0].vodServer3 );
							resultVO.setVodIpv6Node1(resultVO.listAuthorizeSView[0].vodIpv6Node1 );
							resultVO.setVodIpv6Node2(resultVO.listAuthorizeSView[0].vodIpv6Node2 );
							resultVO.setVodIpv6Node3(resultVO.listAuthorizeSView[0].vodIpv6Node3 );
							resultVO.setRealHdIpv6Server1(resultVO.listAuthorizeSView[0].vodIpv6Server1 );
							resultVO.setRealHdIpv6Server2(resultVO.listAuthorizeSView[0].vodIpv6Server2 );
							resultVO.setRealHdIpv6Server3(resultVO.listAuthorizeSView[0].vodIpv6Server3 );
							
						}
					} // end of "if(null != liveHevcFileName && !liveHevcFileName.isEmpty() )"
				} // end of "if("D".equals(resultVO.getViewType()) )"
				
				//인앱의 경우 쿠폰 정보 조회 안함!!!
				if(!"I".equals(paramVO.getAppStr()) ) {
					
					//=========================
		            // 스탬프 쿠폰 정보 조회
		            //=========================
					tp1 = System.currentTimeMillis();
					if(SUCCESS != authorizeCpnCondIns(paramVO) ) {
						
						tp2 = System.currentTimeMillis();
						imcsLog.timeLog("스탬프/쿠폰 정보 조회 실패", String.valueOf(tp2 - tp1), methodName, methodLine);
						
					} else {
						
						//2018.12.22 - VR앱 : VR앱으로 조회 하는 경우 스탬프/쿠폰 정보를 단말에 주지 않는다.
						if(!"E".equals(paramVO.getAppType().substring(0,  1)) ) {
							
							ComCpnVO cpnVO = paramVO.getComCpnVO();
							
							String cpnInfo = cpnVO.getCpnInfo();
							if(!cpnInfo.isEmpty() ) {
								String tmpSndBufCpn = String.format("CPN01|%s|", cpnInfo );
								resultVO.setTmpSndBufCpn(tmpSndBufCpn);
							}
							
							String stempInfo = cpnVO.getStmInfo();
							if(!stempInfo.isEmpty() ) {
								String tmpSndBufStemp = String.format("STP00|%s|", stempInfo );
								resultVO.setTmpSndBufStemp(tmpSndBufStemp);
							}
							
							String useCpnInfo = cpnVO.getUseCpnInfo();
							if(!useCpnInfo.isEmpty() ) {
								String tmpSndBufCpnUse = String.format("CPN02|%s|", useCpnInfo );
								resultVO.setTmpSndBufCpnUse(tmpSndBufCpnUse);
							}
						}
						
						tp2 = System.currentTimeMillis();
						imcsLog.timeLog("스탬프/쿠폰 정보 조회 완료", String.valueOf(tp2 - tp1), methodName, methodLine);
					}
					
				} // end of "if("I".equals(paramVO.getAppStr()) )"
				
			} // end of "if(SUCCESS ==  resultVO.getResultSet() && 1 == resultVO.getDataChk() )"
			
			List<FmInfoVO> listFmInfo = getFmInfo(paramVO);
			if(null != listFmInfo && !listFmInfo.isEmpty() ) {
				
				FmInfoVO fmInfo = listFmInfo.get(0);
				resultVO.setFmYn(fmInfo.getFmYN() );
				resultVO.setFmAssetId(fmInfo.getAdiProdId() );
			}
			
			List<String> listAssetId = authorizeNSViewDao.getAssetId(paramVO );
			if(null != listAssetId && !listAssetId.isEmpty() ) {
				resultVO.setHevcYn("Y");
			}
			
			// 시즌 여부
			if(SUCCESS == getSeasonInfo(paramVO) ) {
				resultVO.setSeasonYn("Y");
			} else {
				resultVO.setSeasonYn("N"); 
			}
			
			//2018.10.30 - HEVC1 ~ 이후 화질은 PT_LA_M3U8_INFO 테이블에서 조회를 한다.
			if("S".equals(paramVO.getViewType()) && "N".equals(resultVO.getFourdReplayYn()) ) {
				
				tp1 = System.currentTimeMillis();
				vodM3u8Search(paramVO, resultVO );
				tp2 = System.currentTimeMillis();
				imcsLog.timeLog("M3U8 프로파일 및 CDN IP 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
				
			} else if("S".equals(paramVO.getViewType()) && "Y".equals(resultVO.getFourdReplayYn()) ) {
				
				tp1 = System.currentTimeMillis();
				try {
					// 2019.02.16 - 4D Replay CDN IP는 CDN구조체 배열의 마지막 항목으로 사용
					searchCdnIp(19, "D", paramVO, resultVO );
				} catch(Exception e) {}
				tp2 = System.currentTimeMillis();
				imcsLog.timeLog("4D Replay CDN IP 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
				
			} else if("D".equals(paramVO.getViewType()) ) {
				
				if("1".equals(paramVO.getDefinFlag()) ) {
					paramVO.setBitRate("M1");
				}
				
				if("2".equals(paramVO.getDefinFlag()) ) {
					paramVO.setBitRate("M2");
				}
				
				if(paramVO.getDefinFlag().isEmpty() ) {
					paramVO.setBitRate("M2");
				}
				
				resultVO.setListAuthorizeSView(new authorizeSView[20] );
				tp1 = System.currentTimeMillis();
				try {
					searchCdnIp(0, "N", paramVO, resultVO );
				} catch(Exception e) {}
				tp2 = System.currentTimeMillis();
				imcsLog.timeLog("DOWNLOAD CDN IP 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
				
				resultVO.setVodServer1(resultVO.listAuthorizeSView[0].vodServer1 );
				resultVO.setVodServer2(resultVO.listAuthorizeSView[0].vodServer2 );
				resultVO.setVodServer3(resultVO.listAuthorizeSView[0].vodServer3 );
				resultVO.setVodServer1Type(resultVO.listAuthorizeSView[0].vodServer1Type );
				resultVO.setVodServer2Type(resultVO.listAuthorizeSView[0].vodServer2Type );
				resultVO.setVodServer3Type(resultVO.listAuthorizeSView[0].vodServer3Type );
				resultVO.setVodIpv6Server1(resultVO.listAuthorizeSView[0].vodIpv6Server1 );
				resultVO.setVodIpv6Server2(resultVO.listAuthorizeSView[0].vodIpv6Server2 );
				resultVO.setVodIpv6Server3(resultVO.listAuthorizeSView[0].vodIpv6Server3 );
				resultVO.setVodIpv6Server1Type(resultVO.listAuthorizeSView[0].vodIpv6Server1Type );
				resultVO.setVodIpv6Server2Type(resultVO.listAuthorizeSView[0].vodIpv6Server2Type );
				resultVO.setVodIpv6Server3Type(resultVO.listAuthorizeSView[0].vodIpv6Server3Type );
				
				if( resultVO.getVodIpv6Node1().isEmpty() ) {
				
					resultVO.setVodIpv6Node1(resultVO.listAuthorizeSView[0].vodIpv6Node1 );
					resultVO.setVodIpv6Node2(resultVO.listAuthorizeSView[0].vodIpv6Node2 );
					resultVO.setVodIpv6Node3(resultVO.listAuthorizeSView[0].vodIpv6Node3 );
				}
				
				if(!resultVO.getLiveHevcFileName().equals("") && !"Y".equals(resultVO.getVrYn()) ) {
					
					tp1 = System.currentTimeMillis();
					try {
						// 2019.02.16 - 4D Replay CDN IP는 CDN구조체 배열의 마지막 항목으로 사용
						searchCdnIp(1, "H", paramVO, resultVO );
						resultVO.setLiveHevcServer1(resultVO.listAuthorizeSView[1].vodServer1);
						resultVO.setLiveHevcServer2(resultVO.listAuthorizeSView[1].vodServer2);
						resultVO.setLiveHevcServer3(resultVO.listAuthorizeSView[1].vodServer3);
						resultVO.setRealHdIpv6Server1(resultVO.listAuthorizeSView[1].vodIpv6Server1);
						resultVO.setRealHdIpv6Server2(resultVO.listAuthorizeSView[1].vodIpv6Server2);
						resultVO.setRealHdIpv6Server3(resultVO.listAuthorizeSView[1].vodIpv6Server3);						
					} catch(Exception e) {}
					tp2 = System.currentTimeMillis();
					imcsLog.timeLog("DOWNLOAD HEVC CDN IP 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
					
				}
			}
			
			// 2019.02.16 - 자막 서버 정보는 m3u8 및 cdn 정보 조회 후 설정하도록 순서 조정
			if("Y".equals(resultVO.getSmiYn()) && "Y".equals(resultVO.getSmiImpYn()) ) {
				
				if("S".equals(paramVO.getViewType()) && "N".equals(resultVO.getFourdReplayYn()) ) {
					resultVO.setCapServer(resultVO.listAuthorizeSView[0].vodServer1 );
				} else if("S".equals(paramVO.getViewType()) && "Y".equals(resultVO.getFourdReplayYn())) {
					resultVO.setCapServer(resultVO.listAuthorizeSView[19].vodServer1 );
				} else {
					resultVO.setCapServer(resultVO.getVodServer1() );
				}
			}
			
			// 2019.09.16 - IPv6듀얼스택 지원 : ipv6지원하는 단말이 아닐 경우에는 ipv6관련 정보를 전부 null로 전달한다.
	        if(paramVO.getIpv6Flag().equals("N"))
	        {
	        	resultVO.setVodIpv6Server1("");
	        	resultVO.setVodIpv6Server2("");
	        	resultVO.setVodIpv6Server3("");
	        	resultVO.setVodIpv6Server1Type("");
	        	resultVO.setVodIpv6Server2Type("");
	        	resultVO.setVodIpv6Server3Type("");
	        	resultVO.setRealHdIpv6Server1("");
	        	resultVO.setRealHdIpv6Server2("");
	        	resultVO.setRealHdIpv6Server3("");
	        	resultVO.listAuthorizeSView[19].vodIpv6Server1 = "";
	        	resultVO.listAuthorizeSView[19].vodIpv6Server2 = "";
	        	resultVO.listAuthorizeSView[19].vodIpv6Server3 = "";
	        	resultVO.listAuthorizeSView[19].vodIpv6Server1Type = "";
	        	resultVO.listAuthorizeSView[19].vodIpv6Server2Type = "";
	        	resultVO.listAuthorizeSView[19].vodIpv6Server3Type = "";
	        }
			
						
			
		} catch(ImcsException ie) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
			// TLO Log 를 Controller Layer 로 전달하기 위해 Setting 한다. (TLO Log 세부값은 finally 문에서 설정된다.)
			throw ie;
		} catch(Exception e) {
			isLastProcess	= ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			
			// TLO Log 를 Controller Layer 로 전달하기 위해 ImcsException 으로 변환한다. (TLO Log 세부값은 finally 문에서 설정된다.)
			throw new ImcsException();
		} finally{
			resultVO.setResultCode(paramVO.getResultCode());
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID179) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]";
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultVO;
	}
	

	

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 앨범 편성된 상품 정보 조회
	 * @param paramVO
	 * @return
	 */
	public List<AuthorizeNSCateVO> getAlbumProduct(AuthorizeNSViewRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		String sqlId = "lgvod179_001_20190102_001";
		List<AuthorizeNSCateVO> list	= new ArrayList<AuthorizeNSCateVO>();
		
		try {			
			try {
				list = authorizeNSViewDao.getAlbumProduct(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {

			paramVO.setResultCode("41000000");
			imcsLog.failLog(ImcsConstants.API_PRO_ID179, sqlId, "", "AlbumProduct:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return list;
	}	
    
    
	/**
	 * 가입자 가입상품 정보조회
	 * @param paramVO
	 * @return
	 */
	public List<String> getCustomProduct(AuthorizeNSViewRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		String sqlId = "lgvod179_026_20181213_001";
		List<String> list	= new ArrayList<String>();
		
		try {			
			try {
				list = authorizeNSViewDao.getCustomProduct(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
//			
			imcsLog.failLog(ImcsConstants.API_PRO_ID179, sqlId, "", "CustomProduct:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return list;
	}
	
	
	
	/**
	 * 가입자 가입상품 정보조회
	 * @param paramVO
	 * @return
	 */
	public List<ContTypeVO> getAlbumProductRelation(AuthorizeNSViewRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		String sqlId = "lgvod179_003_20190109_002";
		List<ContTypeVO> list	= null;
		
		try {			
			try {
				list = authorizeNSViewDao.getAlbumProductRelation(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
			imcsLog.failLog(ImcsConstants.API_PRO_ID179, sqlId, "", "AlbumProductRelation:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return list;
	}
	
	/**
	 * 월정액 상품 구매여부
	 * @param paramVO
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public HashMap<String, String> getProductBuy(AuthorizeNSViewRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		String sqlId	= "lgvod179_004_20190102_001";
		List<HashMap<String, String>> list	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String> mProductBuy	= null;
		
		try {
			try {
				
				for(HashMap item : authorizeNSViewDao.getProductBuy(paramVO) ) {
					list.add(item);
				}
			} catch (DataAccessException e) {				
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (null == list || list.isEmpty()) {
				imcsLog.failLog(ImcsConstants.API_PRO_ID179, sqlId, "", "ProductBuy:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			} else {
				mProductBuy	= list.get(0);
			}			
		} catch (Exception e) {
			imcsLog.failLog(ImcsConstants.API_PRO_ID179, sqlId, "", "ProductBuy:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return mProductBuy;
	}
	
	
	/**
	 * 컨텐츠 DRM 여부 조회
	 * @param paramVO
	 * @return
	 */
	public String getWaterMarkYn(AuthorizeNSViewRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "lgvod179_007_20190102_001";
		String retVal	= null;
		
		try {			
			try {
				retVal = authorizeNSViewDao.getWaterMarkYn(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
//			
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.failLog(ImcsConstants.API_PRO_ID179, sqlId, "", "WaterMarkYn:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return retVal;
	}
	
	
	/**
	 * 앨범 부가 정보 조회
	 * @param paramVO
	 * @return
	 */
	public HashMap<String, String> getAlbumInfo(AuthorizeNSViewRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "lgvod179_008_20190228_001";
		List<HashMap<String, String> > list	= null;
		HashMap<String, String> mapRetVal	= null;
					
		try {
			
			list = authorizeNSViewDao.getAlbumInfo(paramVO);
			if(null != list && !list.isEmpty() ) {
				mapRetVal = list.get(0);
			}
		} catch (DataAccessException e) {
			
			paramVO.setResultCode("41000000");
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.failLog(ImcsConstants.API_PRO_ID179, sqlId, "", "AlbumInfo:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			throw new Exception(e.getClass().getName() + ":" + e.getMessage());
		}

		return mapRetVal;
	}
	
	
	/**
	 * 장르정보 조회 (대장르, 중장르, 소장르)
	 * @param paramVO
	 * @return
	 */
	public GenreInfoVO getGenreString(AuthorizeNSViewRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
//		String sqlId		= "lgvod179_p01_20180808_001";
		String szMsg		= "";
		String szGenreInfo	= "";

		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		List<GenreInfoVO> list	= new ArrayList<GenreInfoVO>();
		GenreInfoVO genreVO		= null;
		
		try {
			try {
				list = authorizeNSViewDao.getGenreString(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list != null && !list.isEmpty()) {
				
				genreVO	= list.get(0);
				if(genreVO != null) {
					
					szGenreInfo	= StringUtil.nullToSpace(genreVO.getGenreLarge()) + "|" + StringUtil.nullToSpace(genreVO.getGenreMid()) + "|" + StringUtil.nullToSpace(genreVO.getGenreSmall());
					genreVO.setSzGenreInfo(szGenreInfo);
				}
			}
			
			try{
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID179) + "] select genre[" + szGenreInfo + "]";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			} catch(Exception e) {}
			
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}

		return genreVO;
	}

	/**
	 * 시청 이력 등록
	 * @param 
	 * @return
	 */
	public int setWatchHist(AuthorizeNSViewRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName	= oStackTrace.getMethodName();
		
		String sqlId	= "lgvod179_i01_20190208_001";
		String szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID179) + "] insert [PT_VO_WATCH_HISTORY_NSC]  table Start at ";
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		imcsLog.serviceLog(szMsg, methodName, methodLine);
		
		int querySize	= 0;
		int	nRetVal		= SUCCESS;
		try {
			// 2020.03.26 - 모바일 아이들나라 프로젝트로 아래 로직 추가
			if("A".equals(paramVO.getAppType().substring(0, 1)) )
			{
				String kidsGb = authorizeNSViewDao.getKidsGb(paramVO);
				if(null == kidsGb || kidsGb.isEmpty()){
					paramVO.setKidsGb(""); 
				} else {
					paramVO.setKidsGb(kidsGb);
				}
			}else {
				paramVO.setKidsGb("");
			}
			
			// 2018.08.03 - 시청UDR 유/무료 설정 프로젝트로 아래 로직 추가
        	HashMap<String, String> nSysdateinfo = authorizeNSViewDao.getSysdateInfo();
        	paramVO.setSysdateCurrent(nSysdateinfo.get("SYSDATE_CURRENT"));
        	paramVO.setSysdate1yearago(nSysdateinfo.get("SYSDATE_1YEARAGO"));
        	paramVO.setWatchDate(nSysdateinfo.get("WATCH_DATE"));
        	
        	if (paramVO.getTerrYn().equals("Y")) {
        		if (paramVO.getSysdateCurrent().equals(paramVO.getTerrEddate())) {
        			paramVO.setPayYn("N");
        		}
        	}
        	
        	if (paramVO.getPreviewPeriod().equals("Y")) {
        		if (!paramVO.getOnairDate().equals(paramVO.getSysdate1yearago())) {
        			HashMap<String, String> nNScreenWatchSubscriptionInfo = authorizeNSViewDao.getNScreenWatchSubscriptionInfo(paramVO);
        			
        			if(null == nNScreenWatchSubscriptionInfo) {
        				imcsLog.failLog(ImcsConstants.API_PRO_ID179, "", "", "free_period: " + ImcsConstants.RCV_MSG3, methodName, methodLine);
        			}else {
        				
        				paramVO.setTerrType(StringUtil.replaceNull(nNScreenWatchSubscriptionInfo.get("TERR_TYPE"), "") );
                    	paramVO.setSysdateYearago(StringUtil.replaceNull(nNScreenWatchSubscriptionInfo.get("AGO_DATE"), "") );
                    	
                    	if (paramVO.getTerrType().equals("1")) {
                    		if (!paramVO.getSysdateYearago().equals("")) {
                    			// 공중파쪽 VOD의 경우에는 방송일 1년 후에 유료 전환이 된다.
                    			if (paramVO.getSysdateYearago().equals(paramVO.getOnairDate())) {
                    				if (!paramVO.getSysdateYearago().equals(paramVO.getTerrEddate())) {
            							// 현재 FVOD가 걸려있다면, 그냥 무료로 판단한다. 왜냐하면 유료전환 프로시저에서는 31030상품을 삭제만하기 때문에 FVOD를 수동 삭제하지 않는 한, 존재하기 때문
            							if(!paramVO.getFreeProdFlag().equals("Y") && paramVO.getPpmProdFlag().equals("Y")) {
            								paramVO.setPayYn("Y");
            							}
                    				}
                    			}
                    		}
                    	} else if (paramVO.getTerrType().equals("2")) {
                    		if (!paramVO.getSysdateYearago().equals("")) {
                    			// CJ쪽 VOD의 경우에는 무료전환일 1년 후에 유료 전환이 된다.
                    			if (paramVO.getSysdateYearago().equals(paramVO.getTerrEddate())) {
            						// 현재 FVOD가 걸려있다면, 그냥 무료로 판단한다. 왜냐하면 유료전환 프로시저에서는 31030상품을 삭제만하기 때문에 FVOD를 수동 삭제하지 않는 한, 존재하기 때문
            						if(!paramVO.getFreeProdFlag().equals("Y") && paramVO.getPpmProdFlag().equals("Y"))
            						{
            							paramVO.setPayYn("Y");
            						}
                    			}
                    		}
                    	}
        			} // end of "if(null != nNScreenWatchSubscriptionInfo)"
        		} // end of "if (!paramVO.getOnairDate().equals(paramVO.getSysdate1yearago()))"
        	} // end of "if (paramVO.getPreviewPeriod().equals("Y"))"
        				
			try {
				
//				String assetId = String.format("|%s|%s|%s|%s|%s|%s|%s|%s|%s|"
//											   , paramVO.getCatId()
//						                       , paramVO.getAlbumId()
//						                       , paramVO.getSuggestedPrice()
//						                       , paramVO.getCpId()
//						                       , paramVO.getContentFilesize()
//						                       , paramVO.getPayYn()
//						                       , paramVO.getTerrYn()
//						                       , paramVO.getPreviewPeriod()
//						                       , paramVO.getTerrPeriod() );
//				paramVO.setAssetId(assetId);
				
				querySize = authorizeNSViewDao.setWatchHist(paramVO );
				if(querySize > 0 ) {
					if( 1 == paramVO.getLinkChk() ) {
						if( SUCCESS != updatePtVoSetTimePtt(paramVO)) {
							nRetVal = FAILURE;
						}
					} else {
						if(SUCCESS != insertPtVoSetTimePtt(paramVO) ) {
							nRetVal = FAILURE;
						}
					}
				}
				else
				{
					nRetVal = FAILURE;
				}
				
				imcsLog.dbLog(ImcsConstants.API_PRO_ID179, sqlId, "", querySize, methodName, methodLine);
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID179) + "] insert [PT_VO_WATCH_HISTORY_NSC] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
				if( nRetVal == SUCCESS )
				{
					querySize = authorizeNSViewDao.insWatchMeta(paramVO);
					
					if(querySize <= 0 ) {
						nRetVal = FAILURE;
					}
				}
				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID179) + "] insert [NPT_VO_WATCH_META] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해
				throw new SQLException(e.getClass().getName()+ ":" + e.getMessage());
			}						
			
		} catch (Exception e) {
			
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID179) + "] insert [PT_VO_WATCH_HISTORY_NSC] table [0] records Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			imcsLog.failLog(ImcsConstants.API_PRO_ID179, sqlId, "", "insert fail:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			paramVO.setResultCode("41000000");
		}

		return nRetVal;
	}

	
	/**
	 * SET_TIME 테이블 이어보기 시간 설정
	 * @param 
	 * @return
	 */
	public int updatePtVoSetTimePtt(AuthorizeNSViewRequestVO paramVO) throws Exception {
		
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName	= oStackTrace.getMethodName();
		
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		int resultSet = SUCCESS;		
		try {			
			
			int nRetVal = authorizeNSViewDao.updatePtVoSetTimePtt(paramVO);
			if(nRetVal > 0 ) {
				
				String szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID179) + "] update [PT_VO_SET_TIME_PTT_NSC] table[" + nRetVal + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			} else { resultSet = FAILURE;}
			
		} catch (Exception e) {
			resultSet = FAILURE;
		}
		
		return resultSet;
	}
	
	/**
	 * SET_TIME 테이블 이어보기 시간 설정
	 * @param 
	 * @return
	 */
	public int insertPtVoSetTimePtt(AuthorizeNSViewRequestVO paramVO) throws Exception {
		
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName	= oStackTrace.getMethodName();
		
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		int resultSet = SUCCESS;	
		String szMsg = "";
		try {			
			
			int nRetVal = authorizeNSViewDao.insertPtVoSetTimePtt(paramVO);
			if(nRetVal > 0 ) {
				
				szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID179) + "] insert [PT_VO_SET_TIME_PTT_NSC] table[" + nRetVal + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			} else { 
				
				resultSet = FAILURE;
				szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID179) + "] insert [PT_VO_SET_TIME_PTT_NSC] table Failed at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}
		} catch (Exception e) {
			
			resultSet = FAILURE;
			szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID179) + "] insert [PT_VO_SET_TIME_PTT_NSC] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
		return resultSet;
		
	}
	
	/**
	 * 앨범 포스터 정보 조회
	 * @param paramVO
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public List<HashMap<String, String> > getAlbumPoster(AuthorizeNSViewRequestVO paramVO) throws Exception {
		
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "lgvod179_019_20181001_001";
		List<HashMap<String, String>> list	= new ArrayList<HashMap<String, String>>();
		HashMap<String, String> mAlbumPoster	= null;
		
		try {
			try {
				
				List<HashMap> listItem = authorizeNSViewDao.getAlbumPoster(paramVO);
				for(HashMap item :  listItem) {
					list.add(item);
				}
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
		} catch (Exception e) {
			
			paramVO.setResultCode("41000000");
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.failLog(ImcsConstants.API_PRO_ID179, sqlId, "", "thumbnail_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return list;
	}
	
	/**
	 * Live Hevc 다운로드 컨텐츠 파일정보 조회
	 * @param paramVO
	 * @return
	 */
	public String getLiveHevcFileName(AuthorizeNSViewRequestVO paramVO) throws Exception {
		
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		List<String> list		= null;
		String liveHevcFileName	= "";
		try {
			
			try {
				list = authorizeNSViewDao.getLiveHevcFileName(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (null != list && !list.isEmpty()) {
				liveHevcFileName = StringUtil.nullToSpace(list.get(0));
			}
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}

		return liveHevcFileName;
	}


	/**
	 * CND IP 정보 조회
	 * @param paramVO
	 * @return
	 */
	public void searchCdnIp(int m3u8Cnt, String nodeGroup,  AuthorizeNSViewRequestVO paramVO, AuthorizeNSViewResultVO resultVO) throws Exception {
		
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		String tmpNodeGroup = null;
		String baseCondi	= null;
		int mainCount		= 0;

		// 2020.11.11 - 운영팀 테스트용으로 T BASE_CD 허용 (황영환 책임님 요청)
		// 				5G / 4D / HEVC / AWS 모두 다 T로 동작하도록 함		
		if (!"N".equals(nodeGroup) ) {
			if ("F".equals(nodeGroup)) {
				if ("W".equals(paramVO.getBaseCd().substring(0, 1)) ) {

					paramVO.setPosVar(this.NORMAL_5G_W);
					tmpNodeGroup = "F";
				} else if ("L".equals(paramVO.getBaseCd().substring(0, 1)) ) {

					paramVO.setPosVar(this.NORMAL_5G_L);
					tmpNodeGroup = "V";
				} else if ("T".equals(paramVO.getBaseCd().substring(0, 1)) ) {

					tmpNodeGroup = "T";
				}
			}else if ("D".equals(nodeGroup)) {

				if ("W".equals(paramVO.getBaseCd().substring(0, 1)) ) {

					paramVO.setPosVar(this.NORMAL_4D_W);
					tmpNodeGroup = "D";
				} else if ("L".equals(paramVO.getBaseCd().substring(0, 1)) ) {

					paramVO.setPosVar(this.NORMAL_4D_L);
					tmpNodeGroup = "R";
				} else if ("T".equals(paramVO.getBaseCd().substring(0, 1)) ) {

					tmpNodeGroup = "T";
				}
			} else if ("Z".equals(nodeGroup)) {

				if ("T".equals(paramVO.getBaseCd().substring(0, 1)) ) {

					tmpNodeGroup = "T";
				} else {
					// 아마존 IP는 WIFI / LTE 가 따로 없다.
					paramVO.setPosVar(this.NORMAL_AWS);
					tmpNodeGroup = "Z";	
				}				
			} else if ("H".equals(nodeGroup)) {
				
				// 2019.04.02 - 일반노드 배포인데, MP화질 O / VR X / 공연 O일 경우에는 온누리넷만 배포하므로 별도로 처리한다. (WIFI의 경우 H, LTE의 경우 E로 처리)
				if("W".equals(paramVO.getBaseCd().substring(0, 1))) {
					
					paramVO.setPosVar(NORMAL_MP_MUSIC_W);
					tmpNodeGroup = "H";
				} else if("L".equals(paramVO.getBaseCd().substring(0, 1))) {
					
					paramVO.setPosVar(NORMAL_MP_MUSIC_L);
					tmpNodeGroup = "E";
				} else if ("T".equals(paramVO.getBaseCd().substring(0, 1)) ) {

					tmpNodeGroup = "T";
				}
				
			}

		// end of "if ("F".equals(nodeGroup))"
		} else {
				
			if("W".equals(paramVO.getBaseCd().substring(0, 1)) ) {
				
				paramVO.setPosVar(this.NORMAL_WIFI);
				tmpNodeGroup = "W";
				
			} else if("T".equals(paramVO.getBaseCd().substring(0, 1)) ) {
								
				tmpNodeGroup = "T";
			} else {
				
				paramVO.setPosVar(this.NORMAL_LTE);
				tmpNodeGroup = "L";
			}
		}
			
		switch(paramVO.getSvcNode() ) {
			case "N": break;
			case "R":
				paramVO.setPosVar(paramVO.getPosVar() + this.SVC_R);
				tmpNodeGroup = String.format("A%c", tmpNodeGroup.charAt(0) );
				break;
			default: break;
		}
		
		if("S".equals(paramVO.getViewType()) ) {
			
			if("Y".equals(paramVO.getBaseGb()) ) {
				
				mainCount = 0;
				baseCondi = paramVO.getBaseCd();
				
				// WI-FI 사용자의 요청에 따라 SUB_NODE_CD값을 순차적으로 응답하기 위하여 모든 노드에 대하여
				// 현재 iPos값에 해당하는 순번의 노드값 반환(방송운영팀 요청에 의한 개발)
				// 20200825 - LTE도 로드밸런싱
				if(!"T".equals(tmpNodeGroup) && null != tmpNodeGroup && 1 == tmpNodeGroup.length() ) {
					
					// 하기코드와 함수명이 틀림 --> "getNodeCdForW1()"
					List<String> listNodeCd = authorizeNSViewDao.getNodeCd01(tmpNodeGroup);
					mainCount = listNodeCd.size();
					
					// 조회한 결과의 개수가 이번에 사용할 노드 순서보다 작은 경우 다시 첫번째 노드를 사용하기 위하여 변수 초기화
					// PT_LV_RANGE_IP_INFO 테이블에 WI-FI 사용자에게 응답할 노드의 수가 변동 되었을 경우
					// 아래 반복문으로 인한 잘못 된 데이터 저장하는 것을 방지
					synchronized(posArr) {
						
						if(posArr[paramVO.getPosVar()] >= mainCount ) {
							posArr[paramVO.getPosVar()] = 0;
						}
						
						if(mainCount > 0 ) {
							
							//  조회한 모든 row를 fetch하지 않고 이번에 반환할 순서에 해당하는 노드까지만 fetch 하여
							// 아래 반복문 종료시 이번에 반환할 순서에 해당하는 노드 값이 szTempNodeCd에 저장
							String szTempNodeCd = "";
							for(int i = 0; i <= posArr[paramVO.getPosVar()]; i++ ) {
								szTempNodeCd = listNodeCd.get(i);
							}
							
							paramVO.setNodeCd(szTempNodeCd);
							posArr[paramVO.getPosVar()]++;
							imcsLog.debugLog(String.format("Index[%d], posArrValue[%d]", paramVO.getPosVar(), posArr[paramVO.getPosVar()]) );
						}
					} // end of "synchronized(posArr)"
				} else if(!"T".equals(tmpNodeGroup) && null != tmpNodeGroup &&  2 == tmpNodeGroup.length() ) {
					
					// 상기코드와 함수명이 틀림 --> "getNodeCdForW2()"
					List<String> listNodeCd = authorizeNSViewDao.getNodeCd02(tmpNodeGroup);
					mainCount = listNodeCd.size();
					
					synchronized(posArr) {
						
						if(posArr[paramVO.getPosVar()] >= mainCount ) {
							posArr[paramVO.getPosVar()] = 0;
						}
						
						if(mainCount > 0 ) {
							
							//  조회한 모든 row를 fetch하지 않고 이번에 반환할 순서에 해당하는 노드까지만 fetch 하여
							// 아래 반복문 종료시 이번에 반환할 순서에 해당하는 노드 값이 szTempNodeCd에 저장
							String szTempNodeCd = "";
							for(int i = 0; i <= posArr[paramVO.getPosVar()]; i++ ) {
								szTempNodeCd = listNodeCd.get(i);
							}
							
							paramVO.setNodeCd(szTempNodeCd);
							posArr[paramVO.getPosVar()]++;
							imcsLog.debugLog(String.format("Index[%d], posArrValue[%d]", paramVO.getPosVar(), posArr[paramVO.getPosVar()]) );
						}
					} // end of "synchronized(posArr)"
				}
				
				if( 0 == mainCount ) {
					
					if("T".equals(tmpNodeGroup) ) {
						
						List<String> listNodeCd = authorizeNSViewDao.getNodeCd(baseCondi);
						mainCount = listNodeCd.size();
						if(mainCount > 0 ) {
							paramVO.setNodeCd(listNodeCd.get(0) );
						}
					}
					
					if( 0 == mainCount ) {
						
						baseCondi = null;
						baseCondi = tmpNodeGroup;
						
						List<String> listNodeCd = authorizeNSViewDao.getNodeCd(baseCondi);
						mainCount = listNodeCd.size();
						if(mainCount > 0 ) {
							paramVO.setNodeCd(listNodeCd.get(0) );
						}
						
						if( 0 == mainCount) {
							
							baseCondi = null;
							baseCondi = "1234567890";
							
							listNodeCd = authorizeNSViewDao.getNodeCd(baseCondi);
							mainCount = listNodeCd.size();
							if(mainCount > 0 ) {
								paramVO.setNodeCd(listNodeCd.get(0) );
							}
						}
					}
				}
					
				mainCount = 0;
				paramVO.setNodeGroup(nodeGroup);
				List<HashMap<String, String>> listCdnIp = authorizeNSViewDao.getCdnIp(paramVO);
				
				if(null == listCdnIp || listCdnIp.isEmpty() ) throw new ImcsException();
				
				mainCount = listCdnIp.size();
				HashMap<String, String> mapItem = listCdnIp.get(0);
				resultVO.listAuthorizeSView[m3u8Cnt].vodServer1			= StringUtil.nullToSpace(mapItem.get("VOD_SERVER1") ); 
				resultVO.listAuthorizeSView[m3u8Cnt].vodServer2 		= StringUtil.nullToSpace(mapItem.get("VOD_SERVER2") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodServer3 		= StringUtil.nullToSpace(mapItem.get("VOD_SERVER3") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodServer1Type 	= StringUtil.nullToSpace(mapItem.get("VOD_SERVER1_TYPE") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodServer2Type 	= StringUtil.nullToSpace(mapItem.get("VOD_SERVER2_TYPE") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodServer3Type 	= StringUtil.nullToSpace(mapItem.get("VOD_SERVER3_TYPE") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node1 		= StringUtil.nullToSpace(mapItem.get("VOD_IPV6_NODE1") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node2 		= StringUtil.nullToSpace(mapItem.get("VOD_IPV6_NODE2") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node3 		= StringUtil.nullToSpace(mapItem.get("VOD_IPV6_NODE3") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server1 	= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PLAY_IP1") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server2 	= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PLAY_IP2") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server3 	= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PLAY_IP3") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Port1 		= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PORT") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server1Type = StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_TYPE1") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server2Type = StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_TYPE2") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server3Type = StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_TYPE3") );
			} // if("Y".equals(paramVO.getBaseGb()) )
			else {
				
				mainCount = 0;
				paramVO.setNodeGroup(nodeGroup);
				List<HashMap<String, String>> listCdnIp02 = authorizeNSViewDao.getCdnIp02(paramVO);
				
				if(null == listCdnIp02 || listCdnIp02.isEmpty() ) throw new ImcsException();
				
				HashMap<String, String> mapItem = listCdnIp02.get(0);
				mainCount = listCdnIp02.size();
				resultVO.listAuthorizeSView[m3u8Cnt].vodServer1			= StringUtil.nullToSpace(mapItem.get("VOD_SERVER1") ); 
				resultVO.listAuthorizeSView[m3u8Cnt].vodServer2 		= StringUtil.nullToSpace(mapItem.get("VOD_SERVER2") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodServer3 		= StringUtil.nullToSpace(mapItem.get("VOD_SERVER3") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodServer1Type 	= StringUtil.nullToSpace(mapItem.get("VOD_SERVER1_TYPE") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodServer2Type 	= StringUtil.nullToSpace(mapItem.get("VOD_SERVER2_TYPE") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodServer3Type 	= StringUtil.nullToSpace(mapItem.get("VOD_SERVER3_TYPE") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node1 		= StringUtil.nullToSpace(mapItem.get("VOD_IPV6_NODE1") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node2 		= StringUtil.nullToSpace(mapItem.get("VOD_IPV6_NODE2") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node3 		= StringUtil.nullToSpace(mapItem.get("VOD_IPV6_NODE3") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server1 	= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PLAY_IP1") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server2 	= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PLAY_IP2") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server3 	= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PLAY_IP3") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Port1 		= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PORT") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server1Type = StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_TYPE1") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server2Type = StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_TYPE2") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server3Type = StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_TYPE3") );
			}
			
		// end of "if("S".equals(paramVO.getViewType()) )"
		} else if("D".equals(paramVO.getViewType()) ) {
			
			if("Y".equals(paramVO.getBaseGb()) ) {
				
				paramVO.setBaseOneCd(paramVO.getBaseCd().substring(0,  1) );
				mainCount = 0;
				baseCondi = paramVO.getBaseCd();
				
				// WI-FI 사용자의 요청에 따라 SUB_NODE_CD값을 순차적으로 응답하기 위하여 모든 노드에 대하여
				// 현재 iPos값에 해당하는 순번의 노드값 반환(방송운영팀 요청에 의한 개발)
				// 20200825 - LTE도 로드밸런싱
				if(!"T".equals(tmpNodeGroup) && null != tmpNodeGroup &&  1 == tmpNodeGroup.length() ) {
					
					List<String> listNodeCd = authorizeNSViewDao.getNodeCd01(tmpNodeGroup);
					mainCount = listNodeCd.size();
					
					// 조회한 결과의 개수가 이번에 사용할 노드 순서보다 작은 경우 다시 첫번째 노드를 사용하기 위하여 변수 초기화
					// PT_LV_RANGE_IP_INFO 테이블에 WI-FI 사용자에게 응답할 노드의 수가 변동 되었을 경우
					// 아래 반복문으로 인한 잘못 된 데이터 저장하는 것을 방지
					synchronized (posArr) {
						
						if(posArr[paramVO.getPosVar()] >= mainCount ) {
							posArr[paramVO.getPosVar()] = 0;
						}
						
						if(mainCount > 0 ) {
							//  조회한 모든 row를 fetch하지 않고 이번에 반환할 순서에 해당하는 노드까지만 fetch 하여
							// 아래 반복문 종료시 이번에 반환할 순서에 해당하는 노드 값이 szTempNodeCd에 저장
							String szTempNodeCd = "";
							for(int i = 0; i <= posArr[paramVO.getPosVar()]; i++ ) {
								szTempNodeCd = listNodeCd.get(i);
							}
							
							paramVO.setNodeCd(szTempNodeCd);
							posArr[paramVO.getPosVar()]++;		
							imcsLog.debugLog(String.format("Index[%d], posArrValue[%d]", paramVO.getPosVar(), posArr[paramVO.getPosVar()]) );
						}
					} // end of "synchronized (posArr)"
				} else if(!"T".equals(tmpNodeGroup) && null != tmpNodeGroup &&  2 == tmpNodeGroup.length()) {
					
					// WI-FI 사용자에게 응답 할 RANGE_IP_CD의 첫번째 자리는 'W'문자열, 두번째 자리는 숫자 1자리로 협의(방송운영팀)
					List<String> listNodeCd = authorizeNSViewDao.getNodeCd02(tmpNodeGroup);
					mainCount = listNodeCd.size();
					
					// 조회한 결과의 개수가 이번에 사용할 노드 순서보다 작은 경우 다시 첫번째 노드를 사용하기 위하여 변수 초기화
					// PT_LV_RANGE_IP_INFO 테이블에 WI-FI 사용자에게 응답할 노드의 수가 변동 되었을 경우
					// 아래 반복문으로 인한 잘못 된 데이터 저장하는 것을 방지
					synchronized (posArr) {
						
						if(posArr[paramVO.getPosVar()] >= mainCount ) {
							posArr[paramVO.getPosVar()] = 0;
						}
						
						if(mainCount > 0 ) {
							//  조회한 모든 row를 fetch하지 않고 이번에 반환할 순서에 해당하는 노드까지만 fetch 하여
							// 아래 반복문 종료시 이번에 반환할 순서에 해당하는 노드 값이 szTempNodeCd에 저장
							String szTempNodeCd = "";
							for(int i = 0; i <= posArr[paramVO.getPosVar()]; i++ ) {
								szTempNodeCd = listNodeCd.get(i);
							}
							
							paramVO.setNodeCd(szTempNodeCd);
							posArr[paramVO.getPosVar()]++;	
							imcsLog.debugLog(String.format("Index[%d], posArrValue[%d]", paramVO.getPosVar(), posArr[paramVO.getPosVar()]) );
						} 
					} // end of "synchronized (posArr)"
				} // end of "else if(!"L".equals(tmpNodeGroup) && 2 == tmpNodeGroup.length())"
				
				if(mainCount == 0 ) {
					
					// 20200825 - LTE도 로드밸런싱을 하기 위해 LTE만 위해 따로 조회하던 로직을 주석 처리
					if("T".equals(tmpNodeGroup) ) {
						
						List<String> listNodeCd1 = authorizeNSViewDao.getNodeCd(baseCondi);
						mainCount = listNodeCd1.size();
						if(mainCount > 0 ) {
							
							String nodeCd = listNodeCd1.get(0);
							paramVO.setNodeCd(nodeCd);
						}
					}
					
					if( 0 == mainCount ) {
						
						baseCondi = tmpNodeGroup;
						List<String> listNodeCd2 = authorizeNSViewDao.getNodeCd(baseCondi);
						
						mainCount = listNodeCd2.size();
						if(mainCount > 0 ) {
							
							String  nodeCd = listNodeCd2.get(0);
							paramVO.setNodeCd(nodeCd);
						}
						
						if(0 == mainCount) {
							
							baseCondi = "1234567890";
							List<String> listNodeCd3 = authorizeNSViewDao.getNodeCd(baseCondi);
							mainCount = listNodeCd3.size();
							if(mainCount > 0 ) {
								
								String nodeCd = listNodeCd3.get(0);
								paramVO.setNodeCd(nodeCd);
							}
						}
					}
					
				} // end of "if(mainCount == 0 )"
				
				mainCount = 0;
				paramVO.setNodeGroup(nodeGroup);
				List<HashMap<String, String>> listCdnIp03 = authorizeNSViewDao.getCdnIp03(paramVO);
				
				if(null == listCdnIp03 || listCdnIp03.isEmpty() ) throw new ImcsException();
				
				HashMap<String, String> mapItem = listCdnIp03.get(0);
				mainCount = listCdnIp03.size();
				resultVO.listAuthorizeSView[m3u8Cnt].vodServer1		= StringUtil.nullToSpace(mapItem.get("VOD_SERVER1") ); 
				resultVO.listAuthorizeSView[m3u8Cnt].vodServer2 		= StringUtil.nullToSpace(mapItem.get("VOD_SERVER2") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodServer3 		= StringUtil.nullToSpace(mapItem.get("VOD_SERVER3") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodServer1Type 	= StringUtil.nullToSpace(mapItem.get("VOD_SERVER1_TYPE") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodServer2Type 	= StringUtil.nullToSpace(mapItem.get("VOD_SERVER2_TYPE") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodServer3Type 	= StringUtil.nullToSpace(mapItem.get("VOD_SERVER3_TYPE") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node1 		= StringUtil.nullToSpace(mapItem.get("VOD_IPV6_NODE1") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node2 		= StringUtil.nullToSpace(mapItem.get("VOD_IPV6_NODE2") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node3 		= StringUtil.nullToSpace(mapItem.get("VOD_IPV6_NODE3") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server1 	= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PLAY_IP1") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server2 	= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PLAY_IP2") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server3 	= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PLAY_IP3") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Port1 		= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PORT") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server1Type = StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_TYPE1") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server2Type = StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_TYPE2") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server3Type = StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_TYPE3") );
				
				if(!"M9".equals(paramVO.getBitRate()) ) {
					resultVO.setVodFileName(StringUtil.nullToSpace(mapItem.get("VOD_FILE_NAME")) );
				}
			} // end of "if("Y".equals(paramVO.getBaseGb()) )"	
			else {
				
				List<HashMap<String, String>> listCdnIp04 = authorizeNSViewDao.getCdnIp04(paramVO);
				
				if(null == listCdnIp04 || listCdnIp04.isEmpty() ) throw new ImcsException();
				
				HashMap<String, String> mapItem = listCdnIp04.get(0);
				mainCount = listCdnIp04.size();
				resultVO.listAuthorizeSView[m3u8Cnt].vodServer1		= StringUtil.nullToSpace(mapItem.get("VOD_SERVER1") ); 
				resultVO.listAuthorizeSView[m3u8Cnt].vodServer2 		= StringUtil.nullToSpace(mapItem.get("VOD_SERVER2") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodServer3 		= StringUtil.nullToSpace(mapItem.get("VOD_SERVER3") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodServer1Type 	= StringUtil.nullToSpace(mapItem.get("VOD_SERVER1_TYPE") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodServer2Type 	= StringUtil.nullToSpace(mapItem.get("VOD_SERVER2_TYPE") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodServer3Type 	= StringUtil.nullToSpace(mapItem.get("VOD_SERVER3_TYPE") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node1 		= StringUtil.nullToSpace(mapItem.get("VOD_IPV6_NODE1") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node2 		= StringUtil.nullToSpace(mapItem.get("VOD_IPV6_NODE2") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node3 		= StringUtil.nullToSpace(mapItem.get("VOD_IPV6_NODE3") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server1 	= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PLAY_IP1") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server2 	= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PLAY_IP2") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server3 	= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PLAY_IP3") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Port1 		= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PORT") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server1Type = StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_TYPE1") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server2Type = StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_TYPE2") );
				resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server3Type = StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_TYPE3") );
				
				if(!"M9".equals(paramVO.getBitRate()) ) {
					resultVO.setVodFileName(StringUtil.nullToSpace(mapItem.get("VOD_FILE_NAME")) );
				}
			}	
		} // end of "else if("D".equals(paramVO.getViewType()) )"
			
		if( 0 == mainCount ) {
			if("Y".equals(paramVO.getBaseGb()) ) {
				if("S".equals(paramVO.getViewType()) ) {
					
					mainCount = 0;
					paramVO.setNodeGroup(nodeGroup);
					List<HashMap<String, String>> listCdnIp01 = authorizeNSViewDao.getStreamingCdnIp01(paramVO);
					HashMap<String, String> mapItem = listCdnIp01.get(0);
					mainCount = listCdnIp01.size();
					
					resultVO.listAuthorizeSView[m3u8Cnt].vodServer1		= StringUtil.nullToSpace(mapItem.get("VOD_SERVER1") ); 
					resultVO.listAuthorizeSView[m3u8Cnt].vodServer2 		= StringUtil.nullToSpace(mapItem.get("VOD_SERVER2") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodServer3 		= StringUtil.nullToSpace(mapItem.get("VOD_SERVER3") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodServer1Type 	= StringUtil.nullToSpace(mapItem.get("VOD_SERVER1_TYPE") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodServer2Type 	= StringUtil.nullToSpace(mapItem.get("VOD_SERVER2_TYPE") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodServer3Type 	= StringUtil.nullToSpace(mapItem.get("VOD_SERVER3_TYPE") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node1 		= StringUtil.nullToSpace(mapItem.get("VOD_IPV6_NODE1") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node2 		= StringUtil.nullToSpace(mapItem.get("VOD_IPV6_NODE2") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node3 		= StringUtil.nullToSpace(mapItem.get("VOD_IPV6_NODE3") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server1 	= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PLAY_IP1") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server2 	= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PLAY_IP2") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server3 	= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PLAY_IP3") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Port1 		= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PORT") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server1Type = StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_TYPE1") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server2Type = StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_TYPE2") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server3Type = StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_TYPE3") );
					
				} else if("D".equals(paramVO.getViewType()) ) {
					
					mainCount = 0;
					paramVO.setNodeGroup(nodeGroup);
					List<HashMap<String, String>> listCdnIp02 = authorizeNSViewDao.getStreamingCdnIp02(paramVO);
					HashMap<String, String> mapItem = listCdnIp02.get(0);
					mainCount = listCdnIp02.size();
					
					resultVO.listAuthorizeSView[m3u8Cnt].vodServer1		= StringUtil.nullToSpace(mapItem.get("VOD_SERVER1") ); 
					resultVO.listAuthorizeSView[m3u8Cnt].vodServer2 		= StringUtil.nullToSpace(mapItem.get("VOD_SERVER2") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodServer3 		= StringUtil.nullToSpace(mapItem.get("VOD_SERVER3") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodServer1Type 	= StringUtil.nullToSpace(mapItem.get("VOD_SERVER1_TYPE") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodServer2Type 	= StringUtil.nullToSpace(mapItem.get("VOD_SERVER2_TYPE") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodServer3Type 	= StringUtil.nullToSpace(mapItem.get("VOD_SERVER3_TYPE") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node1 		= StringUtil.nullToSpace(mapItem.get("VOD_IPV6_NODE1") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node2 		= StringUtil.nullToSpace(mapItem.get("VOD_IPV6_NODE2") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node3 		= StringUtil.nullToSpace(mapItem.get("VOD_IPV6_NODE3") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server1 	= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PLAY_IP1") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server2 	= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PLAY_IP2") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server3 	= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PLAY_IP3") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Port1 		= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PORT") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server1Type = StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_TYPE1") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server2Type = StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_TYPE2") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server3Type = StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_TYPE3") );
					
					if(!"M9".equals(paramVO.getBitRate()) ) {
						resultVO.setVodFileName(StringUtil.nullToSpace(mapItem.get("VOD_FILE_NAME")) );
					}
				}
			} // end of "if("Y".equals(paramVO.getBaseGb()) )"
			
			if("N".equals(paramVO.getBaseGb()) ) {
				if("S".equals(paramVO.getViewType()) ) {
					
					mainCount = 0;
					List<HashMap<String, String>> listCdnIp03 = authorizeNSViewDao.getStreamingCdnIp03(paramVO);
					HashMap<String, String> mapItem = listCdnIp03.get(0);
					mainCount = listCdnIp03.size();
					
					resultVO.listAuthorizeSView[m3u8Cnt].vodServer1		= StringUtil.nullToSpace(mapItem.get("VOD_SERVER1") ); 
					resultVO.listAuthorizeSView[m3u8Cnt].vodServer2 		= StringUtil.nullToSpace(mapItem.get("VOD_SERVER2") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodServer3 		= StringUtil.nullToSpace(mapItem.get("VOD_SERVER3") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodServer1Type 	= StringUtil.nullToSpace(mapItem.get("VOD_SERVER1_TYPE") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodServer2Type 	= StringUtil.nullToSpace(mapItem.get("VOD_SERVER2_TYPE") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodServer3Type 	= StringUtil.nullToSpace(mapItem.get("VOD_SERVER3_TYPE") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node1 		= StringUtil.nullToSpace(mapItem.get("VOD_IPV6_NODE1") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node2 		= StringUtil.nullToSpace(mapItem.get("VOD_IPV6_NODE2") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node3 		= StringUtil.nullToSpace(mapItem.get("VOD_IPV6_NODE3") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server1 	= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PLAY_IP1") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server2 	= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PLAY_IP2") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server3 	= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PLAY_IP3") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Port1 		= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PORT") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server1Type = StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_TYPE1") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server2Type = StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_TYPE2") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server3Type = StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_TYPE3") );
					
				// end of "if("S".equals(paramVO.getViewType()) )"
				} else if ("D".equals(paramVO.getViewType()) ) {
					
					mainCount = 0;
					List<HashMap<String, String>> listCdnIp04 = authorizeNSViewDao.getStreamingCdnIp04(paramVO);
					HashMap<String, String> mapItem = listCdnIp04.get(0);
					mainCount = listCdnIp04.size();
					
					resultVO.listAuthorizeSView[m3u8Cnt].vodServer1		= StringUtil.nullToSpace(mapItem.get("VOD_SERVER1") ); 
					resultVO.listAuthorizeSView[m3u8Cnt].vodServer2 		= StringUtil.nullToSpace(mapItem.get("VOD_SERVER2") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodServer3 		= StringUtil.nullToSpace(mapItem.get("VOD_SERVER3") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodServer1Type 	= StringUtil.nullToSpace(mapItem.get("VOD_SERVER1_TYPE") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodServer2Type 	= StringUtil.nullToSpace(mapItem.get("VOD_SERVER2_TYPE") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodServer3Type 	= StringUtil.nullToSpace(mapItem.get("VOD_SERVER3_TYPE") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node1 		= StringUtil.nullToSpace(mapItem.get("VOD_IPV6_NODE1") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node2 		= StringUtil.nullToSpace(mapItem.get("VOD_IPV6_NODE2") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node3 		= StringUtil.nullToSpace(mapItem.get("VOD_IPV6_NODE3") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server1 	= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PLAY_IP1") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server2 	= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PLAY_IP2") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server3 	= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PLAY_IP3") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Port1 		= StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_PORT") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server1Type = StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_TYPE1") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server2Type = StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_TYPE2") );
					resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server3Type = StringUtil.nullToSpace(mapItem.get("SERVER_IPV6_TYPE3") );
					
					if(!"M9".equals(paramVO.getBitRate()) ) {
						resultVO.setVodFileName(StringUtil.nullToSpace(mapItem.get("VOD_FILE_NAME")) );
					}
				}
			}
			
		} // end of "if( 0 == mainCount )"
		
		resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server1 = ipv6_change(paramVO, resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server1, resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Port1, resultVO.listAuthorizeSView[m3u8Cnt].vodServer1, resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node1, resultVO.getFourdReplayYn());
		resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server2 = ipv6_change(paramVO, resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server2, resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Port1, resultVO.listAuthorizeSView[m3u8Cnt].vodServer2, resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node2, resultVO.getFourdReplayYn());
		resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server3 = ipv6_change(paramVO, resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server3, resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Port1, resultVO.listAuthorizeSView[m3u8Cnt].vodServer3, resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Node3, resultVO.getFourdReplayYn());
		
		if(paramVO.getIpv6Flag().equals("Y"))
		{
			if(resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server1Type.equals("")) resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server1Type = resultVO.listAuthorizeSView[m3u8Cnt].vodServer1Type;
			if(resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server2Type.equals("")) resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server2Type = resultVO.listAuthorizeSView[m3u8Cnt].vodServer2Type;
			if(resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server3Type.equals("")) resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server3Type = resultVO.listAuthorizeSView[m3u8Cnt].vodServer3Type;
		}
		else
		{
			resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server1Type = "";	
			resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server2Type = "";
			resultVO.listAuthorizeSView[m3u8Cnt].vodIpv6Server3Type = "";
		}
	} // end of "public void searchCdnIp(int m3u8Cnt, String nodeGroup,  AuthorizeNSViewRequestVO paramVO, AuthorizeNSViewResultVO resultVO)"
	
	
	/**
	 * 스탬프 쿠폰 정보 조회(쿠폰 외부함수를 호출하여 쿠폰,스템프 정보를 취득)
	 * @param paramVO
	 * @return
	 */
	public int authorizeCpnCondIns(AuthorizeNSViewRequestVO paramVO) {
		
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName	= oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		int nRetVal = SUCCESS;
		String szMsg = "";
		long tp1 = 0L, tp2 = 0L;
		tp1 = System.currentTimeMillis();
		
		try {
			
			int cpnCnt		= 0;
			int stempCnt	= 0;
			ComCpnVO tmpCpnVO	= null;
			paramVO.setComCpnVO(new ComCpnVO() ); 
			ComCpnVO cpnVO = paramVO.getComCpnVO();
			
			try {
				
				tmpCpnVO = authorizeNSViewDao.getCpnPossibleList(paramVO);
				if(null == tmpCpnVO ) {
					imcsLog.failLog(ImcsConstants.API_PRO_ID179, "", "", "msg[coupon(mims)_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				} else {
					
					cpnVO.setCpnInfo(tmpCpnVO.getCpnInfo() );
					cpnVO.setCpnInsInfo(tmpCpnVO.getCpnInsInfo() );
					szMsg = " SELECT smartux.F_GET_CPN_COND_POSSIBLE_LIST =[" + tmpCpnVO.getCpnInfo() + "]";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				}
			} catch(DataAccessException e) {
				imcsLog.failLog(ImcsConstants.API_PRO_ID179, "", "", "msg[coupon(mims)_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
				nRetVal = FAILURE;
			}
			
			if(SUCCESS == nRetVal) {
				if(null != tmpCpnVO ) {				
					String cpnInfo = tmpCpnVO.getCpnInfo();
					String[] arCpnInfo = cpnInfo.split("\\|");
					for(int i1 = 0; i1 < arCpnInfo.length; i1 += 4 ) {
						
						paramVO.setCpevtId(arCpnInfo[1]);
						
						//쿠폰존재여부 체크
						try {
							cpnCnt = authorizeNSViewDao.getCpnChk(paramVO);
						} catch(DataAccessException e) {
							nRetVal = FAILURE;
							break;
						}
						
						//쿠폰이 존재할 경우 INSERT하지 않고 다음처리로...
						if(cpnCnt > 0 ) { continue; }
						
						try {
							
							int sqlFcnt = authorizeNSViewDao.insCpnInfo(paramVO);
							szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID179) + "] insert [SEQ_PT_CPM_COUPON_BOX_ACTION] COUPON_BOX_ACTION table [" + sqlFcnt + "] records Success at ";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
						} catch(DataAccessException e) {
							
							szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID179) + "] insert [SEQ_PT_CPM_COUPON_BOX_ACTION] COUPON_BOX_ACTION table Failed at  ";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
							nRetVal = FAILURE;
							break;
						}
						
						szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID179) + "] insert [SEQ_PT_CPM_COUPON_BOX_ACTION] table records Success : CPEVT_ID[" + paramVO.getCpevtId() + "] ";
						imcsLog.serviceLog(szMsg, methodName, methodLine);
					} // end of "for(i1 = 0; i1 < arCpnInfo.length; i1 += 4 )"
				}
				
			} // end of "if(SUCCESS == nRetVal)"
			
			tp2 = System.currentTimeMillis();
			imcsLog.timeLog("발급가능쿠폰(F_GET_CPN_COND_POSSIBLE_LIST)", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			//=====================
		    //STEMP INFO
		    //=====================
			tp1 = System.currentTimeMillis();
			try {
				
				tmpCpnVO = authorizeNSViewDao.getStmPossibleList(paramVO);
				if(null == tmpCpnVO ) {
					imcsLog.failLog(ImcsConstants.API_PRO_ID179, "", "", "msg[coupon(mims)_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				} else {
					
					cpnVO.setStmInfo(tmpCpnVO.getStmInfo() );
					cpnVO.setStmInsInfo(tmpCpnVO.getStmInsInfo() );
					szMsg = " SELECT smartux.F_GET_STM_COND_POSSIBLE =[" + tmpCpnVO.getStmInfo() + "]";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				}
			} catch(DataAccessException e) {
				imcsLog.failLog(ImcsConstants.API_PRO_ID179, "", "", "msg[coupon(mims)_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
				nRetVal = FAILURE;
			}
			
			if(SUCCESS == nRetVal) {
				if(null != tmpCpnVO ) {
					String stmInfo = tmpCpnVO.getStmInfo();
					String[] arStmInfo = stmInfo.split("\\|");
					for(int i2 = 0; i2 < arStmInfo.length; i2 += 4 ) {
						
						paramVO.setStmpId(arStmInfo[1] );
						
						//스템프 존재여부 체크
						try {
							stempCnt = authorizeNSViewDao.getStmChk(paramVO);
						} catch(DataAccessException e) {
							imcsLog.failLog(ImcsConstants.API_PRO_ID179, "", "", "msg[coupon(mims)_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
							nRetVal = FAILURE;
							break;
						}
						
						//쿠폰이 존재할 경우 INSERT하지 않고 다음처리로...
						if(stempCnt > 0 ) { continue; }
						
						try {
							
							int sqlFcnt = authorizeNSViewDao.insStmInfo(paramVO);
							szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID179) + "] insert [SEQ_PT_CPM_COUPON_BOX_ACTION] COUPON_BOX_ACTION table [" + sqlFcnt + "] records Success at ";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
						} catch(DataAccessException e) {
							
							szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID179) + "] insert [SEQ_PT_CPM_COUPON_BOX_ACTION] COUPON_BOX_ACTION table Failed at  ";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
							nRetVal = FAILURE;
							break;
						}
						
						szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID179) + "] insert [SEQ_PT_CPM_COUPON_BOX_ACTION] table records Success : STAMP_ID[" + paramVO.getStmpId() + "] ";
						imcsLog.serviceLog(szMsg, methodName, methodLine);
					} // end of "for(int i2 = 0; i2 < arStmInfo.length; i2 += 4 )"
				}
				
			} // end of "if(SUCCESS == nRetVal)"
			
			tp2 = System.currentTimeMillis();
			imcsLog.timeLog("발급가능스탬프(F_GET_STM_COND_POSSIBLE)", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			//2018.12.07 - VR앱 : VR앱으로 조회 하는 경우 사용 가능한 쿠폰 정보를 조회하지 않는다.
			if(!"E".equals(paramVO.getAppType().substring(0,  1)) ) {
				
				//=====================
				//USE CPN INFO
				//=====================
				tp1 = System.currentTimeMillis();
				try {
					
					tmpCpnVO = authorizeNSViewDao.getUseCpnPossibleList(paramVO);
					if(null == tmpCpnVO ) {
						imcsLog.failLog(ImcsConstants.API_PRO_ID179, "", "", "msg[coupon(mims)_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
					} else {
						
						cpnVO.setUseCpnInfo(tmpCpnVO.getUseCpnInfo() );
						szMsg = " SELECT smartux.F_GET_CPN_USE_POSSIBLE_LIST =[" + tmpCpnVO.getUseCpnInfo() + "]";
						imcsLog.serviceLog(szMsg, methodName, methodLine);
					}
				} catch(DataAccessException e) {
					imcsLog.failLog(ImcsConstants.API_PRO_ID179, "", "", "msg[coupon(mims)_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
					nRetVal = FAILURE;
				}
				
				tp2 = System.currentTimeMillis();
				imcsLog.timeLog("사용가능스쿠폰(F_GET_CPN_USE_POSSIBLE_LIST)", String.valueOf(tp2 - tp1), methodName, methodLine);
			}
			
		} catch (Exception e) {
			
			nRetVal = FAILURE;
			imcsLog.failLog(ImcsConstants.API_PRO_ID179, "", "", "msg[coupon(mims)_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
		}
		
		
		return nRetVal;
	}
	
	/**
	 * Face-Match 준비여부 조회 
	 * @param 
	 * @return
	 */
	public List<FmInfoVO> getFmInfo(AuthorizeNSViewRequestVO paramVO) throws Exception {
		
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "lgvod179_022_20150530_001";
		List<FmInfoVO> list	= null;
		
		try {
			try {
				list = authorizeNSViewDao.getFmInfo(paramVO);
			} catch (DataAccessException e) {
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.failLog(ImcsConstants.API_PRO_ID179, sqlId, "", "fm_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return list;
	}
	
	/**
	 * 시즌 여부 
	 * @param 
	 * @return
	 */
	public int getSeasonInfo(AuthorizeNSViewRequestVO paramVO) {
		
		int nRetVal = SUCCESS;
		switch(paramVO.getFxType().toUpperCase()) {
		
			// 비디오 포털
			case "N": 
			case "H":
			case "T":
			case "P":
			case "M": 
					try {
						List<String> listSeasonYn = authorizeNSViewDao.getSeasonInfo(paramVO);
						if( null == listSeasonYn || listSeasonYn.isEmpty() ){
							nRetVal = FAILURE;
							break;
						}
					} catch(DataAccessException e) {
						nRetVal = FAILURE;
						break;
					}
				break;
			
			default: nRetVal = FAILURE;
		}
		
		return nRetVal;
	}
	
	/**
	 *  m3u8 조회
	 * @param 
	 * @return
	 */
	public void vodM3u8Search(AuthorizeNSViewRequestVO paramVO, AuthorizeNSViewResultVO resultVO ) {
		
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		resultVO.setListAuthorizeSView(new authorizeSView[20] );
		int i_m3u8_cnt	= 0;
		int i_m3u8_tot	= 0;
		int i_dup_cdn	= 0;
		int i_cdn_chk	= 0;
		
		try {
			
			List<HashMap<String, String>> listVodProfile = authorizeNSViewDao.getVodProfile(paramVO);
			i_m3u8_tot = listVodProfile.size();
			
			HashMap<String, String> mapVodProfile = null;
			for(i_m3u8_cnt = 0; i_m3u8_cnt < i_m3u8_tot; i_m3u8_cnt++ ) {
				
				i_dup_cdn = 0;
				mapVodProfile = listVodProfile.get(i_m3u8_cnt);
				String nodeGroup	= mapVodProfile.get("NODE_GROUP");
				String m3u8Type		=  mapVodProfile.get("M3U8_TYPE");
				
				// 2019.09.09 - Ipv6 : Hevc화질인 L 프로파일일 때에도 온넷 CDN만 줄 수 있도록 node_group을 H로 조회한다.
				if("N".equals(nodeGroup) && "L".equals(m3u8Type)  ) {
					nodeGroup = "H";
				}
				
				// 2019.04.02 - 일반노드 배포인데, MP화질 O / VR X / 공연 O일 경우에는 온누리넷만 배포하므로 별도로 처리한다. (WIFI의 경우 H, LTE의 경우 E로 처리)
				if("N".equals(nodeGroup) && ("P".equals(m3u8Type) || "N".equals(m3u8Type)) && "N".equals(resultVO.getVrYn()) && "Y".equals(resultVO.getMusicYn() ) ) {
					nodeGroup = "H";
				}
				
				// 2019.04.30 - 일반노드 배포인데, MN화질일 경우에는 온누리넷만 배포하므로 별도로 처리한다. (WIFI의 경우 H, LTE의 경우 E로 처리) - 5G 2차 고음질 어셋
				if("N".equals(nodeGroup) && "N".equals(m3u8Type)  ) {
					nodeGroup = "H";
				}
				
				resultVO.listAuthorizeSView[i_m3u8_cnt].vodNodeGroup = nodeGroup; 
				
				for(i_cdn_chk = 0 ; i_cdn_chk < i_m3u8_cnt ; i_cdn_chk++) {
					
					String m3u8NodeGroup = resultVO.listAuthorizeSView[i_m3u8_cnt].vodNodeGroup;
					String cdnNodeGroup =  resultVO.listAuthorizeSView[i_cdn_chk].vodNodeGroup;
					if(m3u8NodeGroup.equals(cdnNodeGroup) ) {
						
						i_dup_cdn = 1; //이전에 구한 CDN IP 정보가 있으므로 다시 DB 조회하지 않는다.
						resultVO.listAuthorizeSView[i_m3u8_cnt] = Arrays.copyOfRange(resultVO.listAuthorizeSView, i_cdn_chk, (i_cdn_chk + 1))[0];
						break;
					}
				}
				
				if(1 == i_dup_cdn ) continue; 
				
				try {
					searchCdnIp(i_m3u8_cnt, resultVO.listAuthorizeSView[i_m3u8_cnt].vodNodeGroup, paramVO, resultVO );
				} catch (Exception e) {imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage()); }
				
				
			} // for(i_m3u8_cnt = 0; i_m3u8_cnt < i_m3u8_tot; i_m3u8_cnt++ )
			
			for(i_m3u8_cnt = 0 ; i_m3u8_cnt < i_m3u8_tot ; i_m3u8_cnt++) {
				
				mapVodProfile = listVodProfile.get(i_m3u8_cnt);
				if("H".equals(mapVodProfile.get("M3U8_TYPE")) || "L".equals(mapVodProfile.get("M3U8_TYPE")) ) {
					
					// 2019.02.16 - CastIs local IP Set
					if("1".equals(resultVO.listAuthorizeSView[i_m3u8_cnt].vodServer1Type) ) {
						
						if("H".equals(mapVodProfile.get("M3U8_TYPE")) ) {
							resultVO.setVodFileNamel(mapVodProfile.get("CASTIS_M3U8") );
						}else if("L".equals(mapVodProfile.get("M3U8_TYPE")) ) {
							resultVO.setVodFileNamel1(mapVodProfile.get("CASTIS_M3U8") );
						}
					}
					// 2019.02.16 - OnNet local IP Set
					else{
						
						if("H".equals(mapVodProfile.get("M3U8_TYPE")) ) {
							resultVO.setVodFileNamel(mapVodProfile.get("ONNURI_M3U8"));
						} else if("L".equals(mapVodProfile.get("M3U8_TYPE"))) {
							resultVO.setVodFileNamel1(mapVodProfile.get("ONNURI_M3U8") );
						}
					}
					
					// 2019.02.16 - CastIs Near IP Set
					if("1".equals(resultVO.listAuthorizeSView[i_m3u8_cnt].vodServer2Type)) {
						
						if("H".equals(mapVodProfile.get("M3U8_TYPE")) ) {
							resultVO.setVodFileNamen(mapVodProfile.get("CASTIS_M3U8") );
						} else if("L".equals(mapVodProfile.get("M3U8_TYPE"))) {
							resultVO.setVodFileNamen1(mapVodProfile.get("CASTIS_M3U8") );
						}
						
					}
					// 2019.02.16 - OnNet Near IP Set
					else {
						
						if("H".equals(mapVodProfile.get("M3U8_TYPE")) ) {
							resultVO.setVodFileNamen(mapVodProfile.get("ONNURI_M3U8") );
						} else if("L".equals(mapVodProfile.get("M3U8_TYPE"))) {
							resultVO.setVodFileNamen1(mapVodProfile.get("ONNURI_M3U8") );
						}
					}
					
					// 2019.02.16 - CastIs Center IP Set
					if("1".equals(resultVO.listAuthorizeSView[i_m3u8_cnt].vodServer3Type)) {
						
						if("H".equals(mapVodProfile.get("M3U8_TYPE")) ) {
							resultVO.setVodFileNamec(mapVodProfile.get("CASTIS_M3U8") );
						} else if("L".equals(mapVodProfile.get("M3U8_TYPE"))) {
							resultVO.setVodFileNamec1(mapVodProfile.get("CASTIS_M3U8") );
						}
					}
					// 2019.02.16 - OnNet Center IP Set
					else {
						if("H".equals(mapVodProfile.get("M3U8_TYPE")) ) {
							resultVO.setVodFileNamec(mapVodProfile.get("ONNURI_M3U8") );
						} else if("L".equals(mapVodProfile.get("M3U8_TYPE"))) {
							resultVO.setVodFileNamec1(mapVodProfile.get("ONNURI_M3U8") );
						}
					}
					
					if("H".equals(mapVodProfile.get("M3U8_TYPE")) ) {
						resultVO.setVodServer1(resultVO.listAuthorizeSView[i_m3u8_cnt].vodServer1 );
						resultVO.setVodServer2(resultVO.listAuthorizeSView[i_m3u8_cnt].vodServer2 );
						resultVO.setVodServer3(resultVO.listAuthorizeSView[i_m3u8_cnt].vodServer3 );
						resultVO.setVodServer1Type(resultVO.listAuthorizeSView[i_m3u8_cnt].vodServer1Type );
						resultVO.setVodServer2Type(resultVO.listAuthorizeSView[i_m3u8_cnt].vodServer2Type );
						resultVO.setVodServer3Type(resultVO.listAuthorizeSView[i_m3u8_cnt].vodServer3Type );
						resultVO.setVodIpv6Node1(resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Node1 );
						resultVO.setVodIpv6Node2(resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Node2 );
						resultVO.setVodIpv6Node3(resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Node3 );
						resultVO.setVodIpv6Server1(resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Server1 );
						resultVO.setVodIpv6Server2(resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Server2 );
						resultVO.setVodIpv6Server3(resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Server3 );
						resultVO.setVodIpv6Server1Type(resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Server1Type );
						resultVO.setVodIpv6Server2Type(resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Server2Type );
						resultVO.setVodIpv6Server3Type(resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Server3Type );						
					}
					else if("L".equals(mapVodProfile.get("M3U8_TYPE")) && ("S".equals(paramVO.getViewType()) || ("D".equals(paramVO.getViewType()) && "N".equals(resultVO.getVrYn()))) ) {
						resultVO.setLiveHevcServer1(resultVO.listAuthorizeSView[i_m3u8_cnt].vodServer1);
						resultVO.setLiveHevcServer2(resultVO.listAuthorizeSView[i_m3u8_cnt].vodServer2);
						resultVO.setLiveHevcServer3(resultVO.listAuthorizeSView[i_m3u8_cnt].vodServer3);
						resultVO.setRealHdIpv6Server1(resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Server1);
						resultVO.setRealHdIpv6Server2(resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Server2);
						resultVO.setRealHdIpv6Server3(resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Server3);
					}
					
					continue;
					
				} // if("H".equals(mapVodProfile.get("M3U8_TYPE")) || "L".equals(mapVodProfile.get("M3U8_TYPE")) )
				
				if(paramVO.getIpv6Flag().equals("N"))
				{
					resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Server1 = "";
					resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Server2 = "";
					resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Server3 = "";
					resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Server1Type = "";
					resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Server2Type = "";
					resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Server3Type = "";
				}
				
				StringBuilder sb = new StringBuilder();
				
				sb.append(resultVO.getTmpSndBufProfile() );
				sb.append("FILE");
				sb.append(ImcsConstants.COLSEP);
				sb.append(StringUtil.replaceNull(mapVodProfile.get("M3U8_TYPE"), "") );
				sb.append(ImcsConstants.COLSEP);
				sb.append(StringUtil.replaceNull(mapVodProfile.get("CASTIS_M3U8"), "") );
				sb.append(ImcsConstants.COLSEP);
				sb.append(StringUtil.replaceNull(mapVodProfile.get("ONNURI_M3U8"), "") );
				sb.append(ImcsConstants.COLSEP);
				sb.append(StringUtil.replaceNull(resultVO.listAuthorizeSView[i_m3u8_cnt].vodServer1, "") );
				sb.append(ImcsConstants.ARRSEP);
				sb.append(StringUtil.replaceNull(resultVO.listAuthorizeSView[i_m3u8_cnt].vodServer2, "") );
				sb.append(ImcsConstants.ARRSEP);
				sb.append(StringUtil.replaceNull(resultVO.listAuthorizeSView[i_m3u8_cnt].vodServer3, "") );
				sb.append(ImcsConstants.COLSEP);
				sb.append(StringUtil.replaceNull(resultVO.listAuthorizeSView[i_m3u8_cnt].vodServer1Type, "") );
				sb.append(ImcsConstants.ARRSEP);
				sb.append(StringUtil.replaceNull(resultVO.listAuthorizeSView[i_m3u8_cnt].vodServer2Type, "") );
				sb.append(ImcsConstants.ARRSEP);
				sb.append(StringUtil.replaceNull(resultVO.listAuthorizeSView[i_m3u8_cnt].vodServer3Type, "") );
				sb.append(ImcsConstants.COLSEP);
				sb.append(StringUtil.replaceNull(resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Node1, "") );
				sb.append(ImcsConstants.ARRSEP);
				sb.append(StringUtil.replaceNull(resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Node2, "") );
				sb.append(ImcsConstants.ARRSEP);
				sb.append(StringUtil.replaceNull(resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Node3, ""));
				sb.append(ImcsConstants.COLSEP);
				sb.append(StringUtil.replaceNull(resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Server1, "") );
				sb.append(ImcsConstants.ARRSEP);
				sb.append(StringUtil.replaceNull(resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Server2, "") );
				sb.append(ImcsConstants.ARRSEP);
				sb.append(StringUtil.replaceNull(resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Server3, ""));
				sb.append(ImcsConstants.COLSEP);
				sb.append(StringUtil.replaceNull(resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Server1Type, "") );
				sb.append(ImcsConstants.ARRSEP);
				sb.append(StringUtil.replaceNull(resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Server2Type, "") );
				sb.append(ImcsConstants.ARRSEP);
				sb.append(StringUtil.replaceNull(resultVO.listAuthorizeSView[i_m3u8_cnt].vodIpv6Server3Type, ""));
				sb.append(ImcsConstants.COLSEP);
				sb.append(ImcsConstants.ROWSEP);
				
				resultVO.setTmpSndBufProfile(sb.toString() );
				
			} // for(i_m3u8_cnt = 0 ; i_m3u8_cnt < i_m3u8_tot ; i_m3u8_cnt++)
			
			
		} catch(DataAccessException e) {
			imcsLog.failLog(ImcsConstants.API_PRO_ID179, "", "", "vod_m3u8_info " + ImcsConstants.RCV_MSG3, methodName, methodLine);
		}
		
	}
	
	/**
	 * IPv4 -> IPv6로 변환
	 * @param	AuthorizeNSViewRequestVO
	 * @result 	ipv6_change
	 */
	public String ipv6_change(AuthorizeNSViewRequestVO paramVO, String ipv6_ip, String ipv6_port, String ipv4_ip, String cdn_type, String fourd_replay) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		String szMsg = ""; 
		
		String result_ipv6 = "";
		String ipv4_protocol = "";
		String ipv4_port = "";
		String[] temp_ipv4_split = null;
		
		try{
			if(paramVO.getIpv6Flag().equals("Y"))
			{
				if(!ipv4_ip.equals("") && ipv6_ip.equals(""))
				{
					// 스트리밍 : case when :fourD_replay_yn = 'N' then 'http://'||B.stb_play_ip1||case when :node_group = 'Z' then '' else ':' || b.SUB_NODE_PORT1 end || '/' else 'rtsp://'||B.stb_play_ip1||':80/' end,
					// 다운로드 : 'http://'||B.stb_play_ip1||case when :node_group = 'Z' then '' else ':' || b.SUB_NODE_PORT1 end || '/',
					// AWS CDN은 도메인 방식이므로, 만약 IPv6컬럼에 값이 없을 경우에는 기존 IPv4의 도메인을 그대로 복사해준다.
					if(!cdn_type.equals("Z"))
					{
						temp_ipv4_split = ipv4_ip.split("/|:");
						for(int i = 0 ; i < temp_ipv4_split.length ; i++)
						{
							if(i == 0) ipv4_protocol = temp_ipv4_split[i];
							if(i == temp_ipv4_split.length - 1) ipv4_port = temp_ipv4_split[i];
								
							if(checkIp(temp_ipv4_split[i]) == 1)
							{
								String[] temp_ip = temp_ipv4_split[i].toString().split("\\.");
								for(int j = 0 ; j < temp_ip.length ; j++)
								{
									if(j != 0 && j % 2 == 0)
									{
										result_ipv6 = result_ipv6 + ":";
									}
									
									if(j == 0)
									{
//										result_ipv6 = Integer.toHexString(Integer.parseInt(temp_ip[j])).toUpperCase();
										result_ipv6 = String.format("%02X", Integer.parseInt(temp_ip[j]));
									}
									else
									{
//										result_ipv6 = result_ipv6 + Integer.toHexString(Integer.parseInt(temp_ip[j])).toUpperCase();
										result_ipv6 = result_ipv6 + String.format("%02X", Integer.parseInt(temp_ip[j]));
									}
								}
							}
						}
						
						if(cdn_type.equals("I"))
						{
							result_ipv6 = ipv4_protocol + "://[" + paramVO.getPrefixInternet() + result_ipv6 + "]:" + ipv4_port + "/"; 
						}
						else
						{
							result_ipv6 = ipv4_protocol + "://[" + paramVO.getPrefixUplushdtv() + result_ipv6 + "]:" + ipv4_port + "/";
						}
					}
					else
					{
						result_ipv6 = ipv4_ip;
					}
				}
				else if(!ipv6_ip.equals(""))
				{
					if(fourd_replay.equals("N") || cdn_type.equals("Z"))
					{
						result_ipv6 = "http://[" + ipv6_ip + "]:" + ipv6_port + "/";
					}
					else
					{
						result_ipv6 = "rtsp://[" + ipv6_ip + "]:80/";
					}
				}
			}
			else
			{
				result_ipv6 = "";
			}
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID178) + "] IPv4 to IPv6 convert fail (msg : " + e.getMessage() + ")";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}

		return result_ipv6;
	}
	
	public static int checkIp(String ipAddress) {
        Pattern pattern;
        int result = 0;
 
        pattern = Pattern.compile(regexIPv4andIPv6);
        if (ipAddress == null || pattern.matcher(ipAddress).matches() == false) {
//            System.out.println("유효하지 않은 IP 주소입니다.");
        	result = -1;
 
        } else {
            // IPv4
            pattern = Pattern.compile(regexIPv4);
            if (pattern.matcher(ipAddress).matches() == true) {
//                System.out.println("IPv4 주소입니다.");
            	result = 1;
            }
 
            // IPv6
            pattern = Pattern.compile(regexIPv6);
            if (pattern.matcher(ipAddress).matches() == true) {
//                System.out.println("IPv6 주소입니다.");
            	result = 2;
            }
        }
        return result;
    }
		
} // end of class "AuthorizeNSViewServiceImpl"