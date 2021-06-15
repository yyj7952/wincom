package kr.co.wincom.imcs.api.getNSSeriesStat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetNSSeriesStatServiceImpl implements GetNSSeriesStatService {
	private Log imcsLogger = LogFactory.getLog("API_getNSSeriesStat");
	
	@Autowired
	private GetNSSeriesStatDao getNSSeriesStatDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSSeriesStat(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public getNSSeriesStatResultVO getNSSeriesStat(GetNSSeriesStatRequestVO paramVO){
//		this.getNSSeriesStat(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		List<getNSSeriesStatResponseVO> tempVO = new ArrayList<getNSSeriesStatResponseVO>();
		List<getNSSeriesStatResponseVO> tempVO2 = new ArrayList<getNSSeriesStatResponseVO>();
		List<getNSSeriesStatResponseVO> AlbumInfoList = new ArrayList<getNSSeriesStatResponseVO>();
	    getNSSeriesStatResultVO resultListVO = new getNSSeriesStatResultVO();
	    
		String msg	= "";
		
		int nAlbumCnt = 0;
		Integer resultSet = 0;
		
		int album_divide = 5;

	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
		long tp2 = 0;
		
		String[] albumArr = null;
		String surtaxRate = "";
		
		try{
			
			if( resultSet == 0 && !"".equals(paramVO.getMulitAlbumId()) ){
				
				albumArr = paramVO.getMulitAlbumId().split(",");
				
				nAlbumCnt = albumArr.length;
				
				
				
				if( nAlbumCnt > 30 ) {
					resultListVO.setResultHeader("1|INPUT PARAM ERROR|||");	
					msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID230) + "] 정상적인 INPUT PARAM이 아닙니다.:"+paramVO.getSaId()+","+paramVO.getStbMac()+")"; 
					imcsLog.serviceLog(msg, methodName, methodLine);
					isLastProcess = ImcsConstants.RCV_MSG6;
					paramVO.setResultCode("31000000");
					paramVO.setResultSet(-1);
					return resultListVO;
				}
				
				for(int i = 0; i<nAlbumCnt; i++){
					
					if(!commonService.getValidParam(albumArr[i], 15, 15, 1))
					{
						resultListVO.setResultHeader("1|INPUT PARAM ERROR|||");	
						msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID230) + "] 정상적인 INPUT PARAM이 아닙니다.:"+paramVO.getSaId()+","+paramVO.getStbMac()+")"; 
						imcsLog.serviceLog(msg, methodName, methodLine);
						isLastProcess = ImcsConstants.RCV_MSG6;
						paramVO.setResultCode("31000000");
						paramVO.setResultSet(-1);
						return resultListVO;
					}
					
					getNSSeriesStatResponseVO infoVO = new getNSSeriesStatResponseVO();
					infoVO.setAlbumId(albumArr[i]);
					infoVO.setSubcriptionYn("1");
					infoVO.setIsSvodOnly("N");
					infoVO.setIsBuy("1");
					infoVO.setDataFreeBuy("1");
					infoVO.setWatchYn("N");
					infoVO.setNscreenYn("N");
					infoVO.setViewingLength("24");
					tempVO.add(infoVO);
				}
				if(nAlbumCnt <= 30) {
					tp1	= System.currentTimeMillis();
					if(getCustPairingChk(paramVO) != 0) {
						tp2	= System.currentTimeMillis();
						imcsLog.timeLog("가입자 정보 조회(no data found)", String.valueOf(tp2 - tp1), methodName, methodLine);
					} else {
						tp2	= System.currentTimeMillis();
						imcsLog.timeLog("가입자 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);

						int input_cnt = 0;
						int input_cnt_tmp = 0;
						int value_cnt = 0;
						int parent_album_cnt = nAlbumCnt % album_divide == 0 ? nAlbumCnt % album_divide + (nAlbumCnt / album_divide) : nAlbumCnt / album_divide + 1;
						List<String> arrAlbumList = new ArrayList<String>();
						List<String> isSubcription = new ArrayList<String>();
						List<String> isNscSubcription = new ArrayList<String>();
						List<String> subcriptionArr = new ArrayList<String>();
						List<String> arrBuyList = new ArrayList<String>();
						List<String> nsc_link_time = new ArrayList<String>();
						List<String> iptv_link_time = new ArrayList<String>();
						List<HashMap> arrBuyChk = new ArrayList<HashMap>();
						List<HashMap> arrNscBuyChk = new ArrayList<HashMap>();
						List<HashMap> arrLinkTime = new ArrayList<HashMap>();
						String[] productArray = null;
						String[] nscArray = null;

						for(int i = 0; i < parent_album_cnt; i++){
							value_cnt = 0;
							arrAlbumList = new ArrayList<String>();
							for(input_cnt = input_cnt_tmp; input_cnt < nAlbumCnt; input_cnt++) {
								if(value_cnt == album_divide) break;
								arrAlbumList.add(albumArr[input_cnt]);
								value_cnt++;
							}
							paramVO.setArrAlbumList(arrAlbumList);
							try{	
								AlbumInfoList = this.getAlbumInfo(paramVO);	
								if(AlbumInfoList != null) {
									for(int j = 0; j < AlbumInfoList.size(); j++){
										productArray = null;
										nscArray = null;
										
										AlbumInfoList.get(j).setSubcriptionYn("1");
										AlbumInfoList.get(j).setIsBuy("1");
										AlbumInfoList.get(j).setDataFreeBuy("1");
										AlbumInfoList.get(j).setWatchYn("N");
										AlbumInfoList.get(j).setNsubscribeYn("N");
										AlbumInfoList.get(j).setNbuyYn("N");
										
										nscArray = AlbumInfoList.get(j).getNscInfo().split(";");
										String nscYn = nscArray[0];
										if (nscYn.equals("Y")) {
											// 2020.02.26 - 엔스크린 지원
											AlbumInfoList.get(j).setNscreenYn(nscYn);
											AlbumInfoList.get(j).setNscreenType(nscArray[2]);
											productArray = nscArray[1].split("/");
											for(int nScreenProdCnt = 0 ; nScreenProdCnt < productArray.length ; nScreenProdCnt++)
											{
												if(nScreenProdCnt == 0)
												{
													AlbumInfoList.get(j).setNproductTypeMin(productArray[nScreenProdCnt]);													
												}
												
												// 2020.02.26 - 만약 IPTV쪽 상품이 PPV밖에 없다면, MAX에도 PPV 상품 타입을 넣어주기 위해 else if 안 하고 별도 if로 조건 확인
												if(nScreenProdCnt == productArray.length - 1)
												{
													AlbumInfoList.get(j).setNproductTypeMax(productArray[nScreenProdCnt]);
												}
											}
										}
										else
										{
											// 2020.02.26 - 엔스크린 미지원
											AlbumInfoList.get(j).setNscreenYn(nscYn);
										}
										
										if(AlbumInfoList.get(j).getProductTypeMin().equals("3") && AlbumInfoList.get(j).getProductTypeMax().equals("3")) {
											AlbumInfoList.get(j).setIsSvodOnly("Y");
										} else {
											AlbumInfoList.get(j).setIsSvodOnly("N");
										}
										
										// SVOD ONLY 여부 설정
										if(AlbumInfoList.get(j).getProductTypeMin().equals("3") || AlbumInfoList.get(j).getProductTypeMax().equals("3")) {
											subcriptionArr.add(AlbumInfoList.get(j).getAlbumId());
										}
										
										if(AlbumInfoList.get(j).getProductTypeMax().equals("3")) {
											AlbumInfoList.get(j).setSvodYn("Y");
										}
										
										tempVO2.add(AlbumInfoList.get(j));
									}
								}
								paramVO.setArrSubcriptionList(subcriptionArr);
							}catch(Exception e){
								imcsLog.errorLog(methodName + "-E", e.getClass().getName() + " getAlbumInfo :" + e.getMessage());
							}
							input_cnt_tmp = input_cnt;
						}
						tp2	= System.currentTimeMillis();
						imcsLog.timeLog("앨범 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
						
						if (paramVO.getArrSubcriptionList().size() > 0) {
							tp1	= System.currentTimeMillis();
							input_cnt = 0;
							input_cnt_tmp = 0;
							nAlbumCnt = paramVO.getArrSubcriptionList().size();
							parent_album_cnt = nAlbumCnt % album_divide == 0 ? nAlbumCnt % album_divide + (nAlbumCnt / album_divide) : nAlbumCnt / album_divide + 1;
							for(int i = 0; i < parent_album_cnt; i++){
								value_cnt = 0;
								List<String> subcriptionArr2 = new ArrayList<String>();
								for(input_cnt = input_cnt_tmp; input_cnt < nAlbumCnt; input_cnt++) {
									if(value_cnt == album_divide) break;
									subcriptionArr2.add(subcriptionArr.get(input_cnt));
									value_cnt++;
								}
								paramVO.setArrSubcriptionList(subcriptionArr2);
								try{	
									isSubcription = this.getSvodAlbum(paramVO);	
									
								}catch(Exception e){
									imcsLog.errorLog(methodName + "-E", e.getClass().getName() + " getSvodAlbum :" + e.getMessage());
								}
								if(isSubcription != null && isSubcription.size() > 0){
									// 가입된 SVOD가 존재하는 경우 가입여부를 '0'으로 바꿔주기 위해 사용
									for(int j = 0; j < tempVO2.size(); j++){
										for(int k = 0; k < isSubcription.size(); k++){
											if(!tempVO2.get(j).getSubcriptionYn().equals("0")) {
												if(tempVO2.get(j).getAlbumId().equals(isSubcription.get(k))){
													tempVO2.get(j).setSubcriptionYn("0");
												}
											}
										}
									}
								}
								input_cnt_tmp = input_cnt;
							}
							tp2	= System.currentTimeMillis();
							imcsLog.timeLog("SVOD 편성 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
						} else {
							for(int j = 0; j < tempVO2.size(); j++){
								tempVO2.get(j).setSubcriptionYn("1");
							}
						}
						for(int j = 0; j < tempVO2.size(); j++){
							if(tempVO2.get(j).getIsSvodOnly().equals("N") && tempVO2.get(j).getSubcriptionYn().equals("1") && !tempVO2.get(j).getProductTypeMin().equals("0")) {
								arrBuyList.add(tempVO2.get(j).getAlbumId());
								arrBuyList.add(tempVO2.get(j).getAlbumId()+"_D");
							}
						}
						if(arrBuyList.size() > 0) {
							tp1	= System.currentTimeMillis();
							paramVO.setArrBuyList(arrBuyList);
							arrBuyChk=this.getBuyChk(paramVO);
							if(arrBuyChk.size() == 0) {
								for(int j = 0; j < tempVO2.size(); j++){
									if(tempVO2.get(j).getIsSvodOnly().equals("N") && tempVO2.get(j).getSubcriptionYn().equals("1")) {
										tempVO2.get(j).setIsBuy("1");
										tempVO2.get(j).setDataFreeBuy("1");
										if(tempVO2.get(j).getProductTypeMin().equals("0")) {
											tempVO2.get(j).setIsBuy("0");
										}
									}
								}
							} else {
								for(int j = 0; j < tempVO2.size(); j++){
									for(int k = 0; k < arrBuyChk.size(); k++) {
										if(tempVO2.get(j).getIsSvodOnly().equals("N") && tempVO2.get(j).getSubcriptionYn().equals("1")) {
											if(tempVO2.get(j).getAlbumId().equals(arrBuyChk.get(k).get("DIFF_BUY_ALBUM").toString().substring(0, 15))) {
												if(arrBuyChk.get(k).get("DIFF_BUY_ALBUM").toString().indexOf("_D") != 0) {
													tempVO2.get(j).setIsBuy("0");
													tempVO2.get(j).setDataFreeBuy("0");
													tempVO2.get(j).setBuyDate(arrBuyChk.get(k).get("BUY_DATE").toString());
													tempVO2.get(j).setBuyExpired(arrBuyChk.get(k).get("EXPIRED_DATE").toString());
												} else {
													tempVO2.get(j).setIsBuy("0");
													if(!tempVO2.get(j).getDataFreeBuy().equals("0")) {
														tempVO2.get(j).setDataFreeBuy("1");
													}
													tempVO2.get(j).setBuyDate(arrBuyChk.get(k).get("BUY_DATE").toString());
													tempVO2.get(j).setBuyExpired(arrBuyChk.get(k).get("EXPIRED_DATE").toString());
												}
											} else if(!tempVO2.get(j).getIsBuy().equals("0")){
												tempVO2.get(j).setIsBuy("1");
												tempVO2.get(j).setDataFreeBuy("1");
												if(tempVO2.get(j).getProductTypeMin().equals("0")) {
													tempVO2.get(j).setIsBuy("0");
												}
											}
										}
									}
								}
							}
							tp2	= System.currentTimeMillis();
							imcsLog.timeLog("구매 여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
						}

						isSubcription = new ArrayList<String>();
						for(int j = 0; j < tempVO2.size(); j++){
							// 엔스크린 가입자의 경우 I30가입자의 가입 여부 체크를 위해 SVOD에 편성되어 있는지 확인 후 배열에 담는다.
							if(paramVO.getNsaId().length() != 0 && tempVO2.get(j).getNscreenYn().equals("Y") && (tempVO2.get(j).getNproductTypeMin().equals("3") || tempVO2.get(j).getNproductTypeMax().equals("3"))
									&& !tempVO2.get(j).getIsBuy().equals("0") && !tempVO2.get(j).getSubcriptionYn().equals("0")) {
								// Seamless 단방향
								if(tempVO2.get(j).getNscreenType().equals("T") || tempVO2.get(j).getNscreenType().equals("A")) {
									isSubcription.add(tempVO2.get(j).getAlbumId());
								}
							}
						}
						if(isSubcription.size() > 0) {
							// 엔스크린 가입자의 경우 I30가입자의 가입 여부 체크를 한다. (엔스크린 컨텐츠 Y && FVOD가 아닌 컨텐츠 대상)
							input_cnt = 0;
							input_cnt_tmp = 0;
							nAlbumCnt = isSubcription.size();
							parent_album_cnt = nAlbumCnt % album_divide == 0 ? nAlbumCnt % album_divide + (nAlbumCnt / album_divide) : nAlbumCnt / album_divide + 1;
							tp1	= System.currentTimeMillis();
							for(int i = 0; i < parent_album_cnt; i++){
								value_cnt = 0;
								List<String> isSubcription2 = new ArrayList<String>();
								for(input_cnt = input_cnt_tmp; input_cnt < nAlbumCnt; input_cnt++) {
									if(value_cnt == album_divide) break;
									isSubcription2.add(isSubcription.get(input_cnt));
									value_cnt++;
								}
								paramVO.setArrSubcriptionList(isSubcription2);
								try{	
									isNscSubcription = this.getNscSubscriptionInfo(paramVO);
									
								}catch(Exception e){
									imcsLog.errorLog(methodName + "-E", e.getClass().getName() + " getNscSubscriptionInfo :" + e.getMessage());
								}
								input_cnt_tmp = input_cnt;
								
								// 가입된 SVOD가 존재하는 경우 가입여부를 '0'으로 바꿔주기 위해 사용
								for(int j = 0; j < tempVO2.size(); j++){
									if(!tempVO2.get(j).getNsubscribeYn().equals("Y")) {
										for(int k = 0; k < isNscSubcription.size(); k++) {
											if(tempVO2.get(j).getAlbumId().equals(isNscSubcription.get(k))) {
												tempVO2.get(j).setNsubscribeYn("Y");
											}
										}
									}
								}
							}
							tp2	= System.currentTimeMillis();
							imcsLog.timeLog("엔스크린 SVOD 편성 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
						} else {//SVOD에 편성된 앨범이 없으므로 모두 미가입 처리
							for(int j = 0; j < tempVO2.size(); j++){
								tempVO2.get(j).setNsubscribeYn("N");
							}
						}
						arrBuyList = new ArrayList<String>();
						for(int j = 0; j < tempVO2.size(); j++){
							//기존 로직에서도 SVOD ONLY가 'N'이고 미가입된 앨범들만 구매여부를 체크 했기 때문에 사용
							if(paramVO.getNsaId().length() != 0 && tempVO2.get(j).getNscreenYn().equals("Y")
									&& !(tempVO2.get(j).getNproductTypeMin().equals("3") && tempVO2.get(j).getNproductTypeMax().equals("3")) && !tempVO2.get(j).getNproductTypeMin().equals("0")
									&& !tempVO2.get(j).getNsubscribeYn().equals("Y") && !tempVO2.get(j).getSubcriptionYn().equals("0")
									&& !tempVO2.get(j).getIsBuy().equals("0")) {
								// Seamless 단방향
								if(tempVO2.get(j).getNscreenType().equals("T") || tempVO2.get(j).getNscreenType().equals("A")) {
									arrBuyList.add(tempVO2.get(j).getAlbumId());
								}
							}
						}
						
						if(arrBuyList.size() > 0) {
							tp1	= System.currentTimeMillis();
							paramVO.setArrBuyList(arrBuyList);
							try{	
								arrNscBuyChk = this.getNscBuyChk(paramVO);
							} catch (Exception e){
								imcsLog.errorLog(methodName + "-E", e.getClass().getName() + " getBuyChk :" + e.getMessage());
							}
							if(arrNscBuyChk.size() == 0) {
								for(int j = 0; j < tempVO2.size(); j++){
									if(paramVO.getNsaId().length() != 0 && tempVO2.get(j).getNscreenYn().equals("Y")
											&& !(tempVO2.get(j).getNproductTypeMin().equals("3") && tempVO2.get(j).getNproductTypeMax().equals("3")) && !tempVO2.get(j).getNproductTypeMin().equals("0") 
											&& !tempVO2.get(j).getNsubscribeYn().equals("Y") && !tempVO2.get(j).getSubcriptionYn().equals("0")
											&& !tempVO2.get(j).getIsBuy().equals("0")) {
										tempVO2.get(j).setNbuyYn("N");
									}
								}
							} else {
								for(int j = 0; j < tempVO2.size(); j++){
									for(int k = 0; k < arrNscBuyChk.size(); k++) {
										if(paramVO.getNsaId().length() != 0 && tempVO2.get(j).getNscreenYn().equals("Y")
												&& !(tempVO2.get(j).getNproductTypeMin().equals("3") && tempVO2.get(j).getNproductTypeMax().equals("3")) && !tempVO2.get(j).getNproductTypeMin().equals("0") 
												&& !tempVO2.get(j).getNsubscribeYn().equals("Y") && !tempVO2.get(j).getSubcriptionYn().equals("0")
												&& !tempVO2.get(j).getIsBuy().equals("0") && !tempVO2.get(j).getNbuyYn().equals("Y")) {
											if(tempVO2.get(j).getAlbumId().equals(arrNscBuyChk.get(k).get("DIFF_BUY_ALBUM").toString().substring(0, 15))) {
												tempVO2.get(j).setNbuyYn("Y");
												tempVO2.get(j).setNbuyDate(arrNscBuyChk.get(k).get("BUY_DATE").toString());
												tempVO2.get(j).setNexpirdDate(arrNscBuyChk.get(k).get("EXPIRED_DATE").toString());
											} else {
												tempVO2.get(j).setNbuyYn("N");
											}
										}
									}
								}
							}
							tp2	= System.currentTimeMillis();
							imcsLog.timeLog("엔스크린 구매 여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
						}
						for(int j = 0; j < tempVO2.size(); j++){
							if(tempVO2.get(j).getNsubscribeYn().equals("0") || tempVO2.get(j).getIsBuy().equals("0")) {
								nsc_link_time.add(tempVO2.get(j).getAlbumId());
							}
							else if(tempVO2.get(j).getNsubscribeYn().equals("Y") || tempVO2.get(j).getNbuyYn().equals("Y")) {
								iptv_link_time.add(tempVO2.get(j).getAlbumId());
							}
							else {
								nsc_link_time.add(tempVO2.get(j).getAlbumId());
							}
							// 2020.11.09 - Seamless 단방향
							if (tempVO2.get(j).getNscreenYn().equals("Y") && tempVO2.get(j).getNsubscribeYn().equals("N") && tempVO2.get(j).getNbuyYn().equals("N")) {
								if(tempVO2.get(j).getNscreenType().equals("T")) {
									tempVO2.get(j).setNscreenYn("N");
								}
							}
						}
						if(nsc_link_time.size() > 0) {
							tp1	= System.currentTimeMillis();
							input_cnt = 0;
							input_cnt_tmp = 0;
							nAlbumCnt = nsc_link_time.size();
							parent_album_cnt = nAlbumCnt % album_divide == 0 ? nAlbumCnt % album_divide + (nAlbumCnt / album_divide) : nAlbumCnt / album_divide + 1;
							
							for(int f = 0; f < parent_album_cnt ; f++)
							{
								value_cnt = 0;
								List<String> nsc_link_time2 = new ArrayList<String>();
								for(input_cnt = input_cnt_tmp; input_cnt < nAlbumCnt; input_cnt++) {
									if(value_cnt == album_divide) break;
									nsc_link_time2.add(nsc_link_time.get(input_cnt));
									
									value_cnt++;
								}
								paramVO.setArrLinkTime(nsc_link_time2);
								try{
									arrLinkTime = this.getNscLinkTime(paramVO);
								} catch (Exception e){
									imcsLog.errorLog(methodName + "-E", e.getClass().getName() + " getLinkTime :" + e.getMessage());
								}
								// 가입자의 앨범 시청 여부 및 이어보기 시간 정보 저장
								for(int j = 0; j < tempVO2.size(); j++){
									for(int k = 0; k < arrLinkTime.size(); k++) {
										if(tempVO2.get(j).getAlbumId().equals(arrLinkTime.get(k).get("ADI_ALBUM_ID"))) {
											tempVO2.get(j).setWatchYn("Y");
											tempVO2.get(j).setLinkTime(arrLinkTime.get(k).get("LINK_TIME").toString());
										}
									}
								}
								input_cnt_tmp = input_cnt;
							}
							tp2	= System.currentTimeMillis();
							imcsLog.timeLog("NSC 이어보기 시간 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
						}
						
						if(iptv_link_time.size() > 0) {
							tp1	= System.currentTimeMillis();
							input_cnt = 0;
							input_cnt_tmp = 0;
							nAlbumCnt = iptv_link_time.size();
							parent_album_cnt = nAlbumCnt % album_divide == 0 ? nAlbumCnt % album_divide + (nAlbumCnt / album_divide) : nAlbumCnt / album_divide + 1;
							
							for(int f = 0; f < parent_album_cnt ; f++)
							{
								value_cnt = 0;
								List<String> iptv_link_time2 = new ArrayList<String>();
								for(input_cnt = input_cnt_tmp; input_cnt < nAlbumCnt; input_cnt++) {
									if(value_cnt == album_divide) break;
									iptv_link_time2.add(iptv_link_time.get(input_cnt));
									
									value_cnt++;
								}
								paramVO.setArrLinkTime(iptv_link_time2);
								try{
									arrLinkTime = this.getIptvLinkTime(paramVO);
								} catch (Exception e){
									imcsLog.errorLog(methodName + "-E", e.getClass().getName() + " getLinkTime :" + e.getMessage());
								}
								// 가입자의 앨범 시청 여부 및 이어보기 시간 정보 저장
								for(int j = 0; j < tempVO2.size(); j++){
									for(int k = 0; k < arrLinkTime.size(); k++) {
										if(tempVO2.get(j).getAlbumId().equals(arrLinkTime.get(k).get("ADI_ALBUM_ID"))) {
											tempVO2.get(j).setWatchYn("Y");
											tempVO2.get(j).setLinkTime(arrLinkTime.get(k).get("LINK_TIME").toString());
										}
									}
								}
								input_cnt_tmp = input_cnt;
							}
							tp2	= System.currentTimeMillis();
							imcsLog.timeLog("IPTV 이어보기 시간 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
						}
					}
				}
			}
			for(int l = 0; l < tempVO.size(); l++){
				for(int j = 0; j < tempVO2.size(); j++) {
					if(tempVO.get(l).getAlbumId().equals(tempVO2.get(j).getAlbumId())){
						tempVO.remove(l);
						tempVO.add(l, tempVO2.get(j));
					}
				}
			}
			resultListVO.setResultHeader("0||"+paramVO.getNsaId()+"|"+paramVO.getNstbMac()+"|");	
			resultListVO.setList(tempVO);
			
		}catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ie.getMessage());

			// TLO Log 를 Controller Layer 로 전달하기 위해 Setting 한다. (TLO Log 세부값은 finally 문에서 설정된다.)
			ie.setList(resultListVO);
			throw ie;
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + e.getMessage());
			
			// TLO Log 를 Controller Layer 로 전달하기 위해 CurationException 으로 변환한다. (TLO Log 세부값은 finally 문에서 설정된다.)
			throw new ImcsException();
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID230) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			msg	= "SurtaxRateCheck DB = ["+surtaxRate+"], File Read = ["+commonService.getSurtaxRate()+"]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);

		}
		
		return resultListVO;
	}
	
	/**
	 * 페어링 정보 조회
	 * @param paramVO
	 * @return
	 */
	public int getCustPairingChk(GetNSSeriesStatRequestVO paramVO) throws Exception {
    	IMCSLog imcsLog = new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String msg		= "";
		String sqlId	=  "";
		int result_set	= 0;
			
		List<HashMap> list = null;
		
		try {
			try{
				list  = getNSSeriesStatDao.getCustPairingChk(paramVO);
			}catch(DataAccessException e){
				result_set = 1;
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			
			if (list == null || list.size() == 0) {
				paramVO.setTestSbc("N");
				result_set = 1;
				msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID110) + "] PAIRING 정보가 없습니다.:"+paramVO.getSaId()+","+paramVO.getStbMac()+")"; 
				imcsLog.serviceLog(msg, methodName, methodLine);
				return result_set;
			} else {
				paramVO.setNsaId(list.get(0).get("N_SA_ID").toString());
				paramVO.setNstbMac(list.get(0).get("N_STB_MAC").toString());
				paramVO.setTestSbc(list.get(0).get("TEST_SBC").toString());
			}
			
		} catch (Exception e) {
			result_set = 1;
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			return result_set;
		}
		
		return result_set;
	}
    
    /**
     * 앨범 ID로 해당 앨범의 앨범 정보 및 svod only 여부 조회
     * @param paramVO
     * @return
     * @throws Exception
     */
    @SuppressWarnings("null")
	public List<getNSSeriesStatResponseVO> getAlbumInfo(GetNSSeriesStatRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    			
		String msg = "";
		
		List<getNSSeriesStatResponseVO> list   = null;
		List<getNSSeriesStatResponseVO> resultVO = null;
		
		try {
			try{
				list = getNSSeriesStatDao.getAlbumInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list == null || list.isEmpty()) {				
				paramVO.setResultSet(0);
			} else {
				resultVO = list;
				
			}
			
		} catch (Exception e) {
			paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID230, "", null, "getAlbumInfo:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			paramVO.setResultSet(-1);
		}
		
    	return resultVO;
    }
	
	/**
     * SVOD에 편성된 앨범의 경우 가입 여부를 체크
     * @param paramVO
     * @return
     * @throws Exception
     */
	public List<String> getSvodAlbum(GetNSSeriesStatRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    			
		String msg = "";
		
		List<String> list   = new ArrayList<String>();
		
		try {
			try{
				list = getNSSeriesStatDao.getSvodAlbum(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
			paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID230, "", null, "getSubcription:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			paramVO.setResultSet(-1);
		}
    	return list;
    }


	/**
     * 구매 여부 조회
     * @param paramVO
     * @return
     * @throws Exception
     */
	public List<HashMap> getBuyChk(GetNSSeriesStatRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    			
		String msg = "";
		
		List<HashMap> list   = new ArrayList<HashMap>();
		
		try {
			try{
				list = getNSSeriesStatDao.getBuyChk(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				if(list.size() == 0) {
					msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID230) + "] msg[buy_chk:구매한 컨텐츠가 없습니다.]"; 
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
		} catch (Exception e) {
			paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID230, "", null, "getSubcription:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			paramVO.setResultSet(-1);
		}
    	return list;
    }
	
	
	/**
     * 엔스크린 가입자의 경우 I30가입자의 가입 여부 체크.
     * @param paramVO
     * @return
     * @throws Exception
     */
	public List<String> getNscSubscriptionInfo(GetNSSeriesStatRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    			
		String msg = "";
		
		List<String> list   = new ArrayList<String>();
		
		try {
			try{
				list = getNSSeriesStatDao.getNscSubscriptionInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
			paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID230, "", null, "getSubcription:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			paramVO.setResultSet(-1);
		}
    	return list;
    }
	
	/**
     * 엔스크린 구매 여부 조회
     * @param paramVO
     * @return
     * @throws Exception
     */
	public List<HashMap> getNscBuyChk(GetNSSeriesStatRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    			
		String msg = "";
		
		List<HashMap> list   = new ArrayList<HashMap>();
		
		try {
			try{
				list = getNSSeriesStatDao.getNscBuyChk(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				if(list.size() == 0) {
					msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID230) + "] msg[nscreen_buy_chk:구매한 컨텐츠가 없습니다.]"; 
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
		} catch (Exception e) {
			paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID230, "", null, "getSubcription:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			paramVO.setResultSet(-1);
		}
    	return list;
    }
	
	
	/**
     * 시청 이력 및 이어보기 정보 조회
     * @param paramVO
     * @return
     * @throws Exception
     */
	public List<HashMap> getNscLinkTime(GetNSSeriesStatRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    			
		String msg = "";
		
		List<HashMap> list   = new ArrayList<HashMap>();
		
		try {
			try{
				list = getNSSeriesStatDao.getNscLinkTime(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				if(list.size() == 0) {
					imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID230, "", null, "watch_info:no data found", methodName, methodLine);
				}
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
			paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID230, "", null, "getSubcription:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			paramVO.setResultSet(-1);
		}
    	return list;
    }
  
	/**
     * 시청 이력 및 이어보기 정보 조회
     * @param paramVO
     * @return
     * @throws Exception
     */
	public List<HashMap> getIptvLinkTime(GetNSSeriesStatRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    			
		String msg = "";
		
		List<HashMap> list   = new ArrayList<HashMap>();
		
		try {
			try{
				list = getNSSeriesStatDao.getIptvLinkTime(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				if(list.size() == 0) {
					imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID230, "", null, "watch_info:no data found", methodName, methodLine);
				}
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
			paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID230, "", null, "getSubcription:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			paramVO.setResultSet(-1);
		}
    	return list;
    }
}
