package kr.co.wincom.imcs.api.getNSViewInfo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.api.authorizeNView.AuthorizeNViewRequestVO;
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
public class GetNSViewInfoServiceImpl implements GetNSViewInfoService {
	private Log imcsLogger = LogFactory.getLog("API_getNSViewInfo");
	
	@Autowired
	private GetNSViewInfoDao getNSViewInfoDao;

//	public void getNSViewInfo(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public GetNSViewInfoResultVO getNSViewInfo(GetNSViewInfoRequestVO paramVO){
//		this.getNSViewInfo(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		GetNSViewInfoResultVO resultListVO = new GetNSViewInfoResultVO();
    	GetNSViewInfoResponseVO tempVO = new GetNSViewInfoResponseVO();
    	
    	String resultHeader = "";
		String msg	= "";
		
		long tp1 = 0;
		long tp2 = 0;
		
		try{
			try {
				
				//페어링 정보  조회
				tp1	= System.currentTimeMillis();
				this.getChkpairing(paramVO);
				
				if(paramVO.getTestSbc().length() == 0) {
					tp2	= System.currentTimeMillis();
					resultHeader = String.format("%s|%s|", "1", "IMCS004");
				    resultListVO.setResultHeader(resultHeader);			
					msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_GETNSVIEWINFO) + "] msg[페어링 정보 없음.]"; 
		    		imcsLog.serviceLog(msg, methodName, methodLine);	
				} else {
					
					//파티션분리로 추가
					if(paramVO.getLinkFlag().equals("M")) {
						paramVO.setIdxSa(paramVO.getSaId().substring(paramVO.getSaId().length()-2, paramVO.getSaId().length()));
					} else {
						paramVO.setIdxSa(paramVO.getConSaId().substring(paramVO.getConSaId().length()-2, paramVO.getConSaId().length()));
					}
					
					tp1	= System.currentTimeMillis();
					tempVO = this.getViewInfoList(paramVO);
					
					if(tempVO != null){
			    		tp2	= System.currentTimeMillis();
			    		imcsLog.timeLog("앨범 정보 및 편성 조회", String.valueOf(tp2 - tp1), methodName, methodLine);	
			    		
			    		String fvodFlag = "";
			    		
			    		if(paramVO.getLinkFlag().equals("S")) {
			    			String [] nscArray = tempVO.getNscreenYn().split(";");
							String nscYn = nscArray[0];
							
							if (nscYn.equals("Y") && paramVO.getLinkFlag().equals("S")) {
								String [] productArray = nscArray[1].split("/");
								
								if(productArray[0].equals("0")) {
									String sysDate = getNSViewInfoDao.getSysdate();
									
									fvodFlag = "0";
									tempVO.setBuyFlag("0");
									tempVO.setBuyingDate(sysDate);
									
									msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_GETNSVIEWINFO) + "] msg[FVOD 컨텐츠 입니다.]"; 
						    		imcsLog.serviceLog(msg, methodName, methodLine);		
								}
								
							}
			    		}
			    		
			    		boolean nScreen = true;
			    		
			    		if(tempVO.getContsId().length() > 0 && !fvodFlag.equals("0")) {
							
			    			tp1	= System.currentTimeMillis();
			    			
							// 엔스크린 불가 컨텐츠 체크
							nScreen = this.getNscreenStatus(paramVO, tempVO);
							
							tp2	= System.currentTimeMillis();
							imcsLog.timeLog("엔스크린 가능 콘텐츠 여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine);

							if(!nScreen) {
								resultHeader = String.format("%s|%s|", "1", "IMCS002");
						    	msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_GETNSVIEWINFO) + "] msg[엔스크린 불가 콘텐츠. (" + paramVO.getAlbumId() + ")]"; 
					    		imcsLog.serviceLog(msg, methodName, methodLine);		
						    	resultListVO.setResultHeader(resultHeader);
							}
							
							if(nScreen) {	
								
								tp1	= System.currentTimeMillis();
								this.getChkBuyInfo(paramVO, tempVO);
								
								if(tempVO.getBuyFlag().equals("0")) {
									tp2	= System.currentTimeMillis();
						    		imcsLog.timeLog("구매 여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
						    		
								} else {
									tp1	= System.currentTimeMillis();
									this.getChkSubscriptionInfo(paramVO, tempVO);
									tp2	= System.currentTimeMillis();
									imcsLog.timeLog("가입 여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
									
								    if(!tempVO.getBuyFlag().equals("0")) {
								    	resultHeader = String.format("%s|%s|", "1", "IMCS002");
								    	msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_GETNSVIEWINFO) + "] msg[시청권한 없음. (" + paramVO.getAlbumId() + ")]"; 
							    		imcsLog.serviceLog(msg, methodName, methodLine);		
								    	resultListVO.setResultHeader(resultHeader);
								    }
								}
							}
						}
						
						if(nScreen && tempVO.getBuyFlag().equals("0")) {
							tp1	= System.currentTimeMillis();
							this.getSetTimeInfo(paramVO, tempVO);
							tp2	= System.currentTimeMillis();
							imcsLog.timeLog("이어보기 시간 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
						}
						
						if(nScreen && tempVO.getBuyFlag().equals("0")) {
							if(paramVO.getLinkFlag().equals("S")) {
								tempVO.setNscSaId(paramVO.getConSaId());
								tempVO.setNscMacAddr(paramVO.getConMac());
							}
							
							resultListVO.setList(tempVO);
						    resultHeader = String.format("%s||", "0");
						    resultListVO.setResultHeader(resultHeader);
						}
			    		
			    	} else {
			    		resultHeader = String.format("%s|%s|", "1", "IMCS001");
					    resultListVO.setResultHeader(resultHeader);
			    		msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_GETNSVIEWINFO) + "] msg[존재하지 않는 앨범ID 입니다.(" + paramVO.getAlbumId() + ")]"; 
			    		imcsLog.serviceLog(msg, methodName, methodLine);			
			    		resultListVO.setResultCode("21000000");
			    	}
				}
				
				
	    		
			} catch (Exception e) {
				resultListVO.setResultCode("40000000");

				throw new ImcsException();		
			}
	    	
			
		    
		}catch(ImcsException ie) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw ie;
		} catch(Exception e) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_GETNSVIEWINFO) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	
	
	/**
	 * 페어링 여부 체크
	 * @param paramVO
	 * @return paramVO	
	 */
	public void getChkpairing(GetNSViewInfoRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		
		try {
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			if(paramVO.getLinkFlag().equals("M")) {// 권한이 모바일 경우에는 검수 사용자인지만 확인 한다.
				String testSbc = getNSViewInfoDao.getTestSbc(paramVO);
				if(testSbc == null) {
					paramVO.setTestSbc("");
				} else {
					paramVO.setTestSbc(testSbc);
				}
				
			} else {
				HashMap<String, String> mTestSbcPairing = getNSViewInfoDao.getTestSbcPairing(paramVO);
				
				if(mTestSbcPairing != null) {
					String conSaId = mTestSbcPairing.get("CON_SA_ID") == null ? "" : mTestSbcPairing.get("CON_SA_ID").toString();
					String conStbMac = mTestSbcPairing.get("CON_STB_MAC") == null ? "" : mTestSbcPairing.get("CON_STB_MAC").toString();
					String testSbc = mTestSbcPairing.get("TEST_SBC") == null ? "" : mTestSbcPairing.get("TEST_SBC").toString();
					
					if(conSaId.length() > 0 && conStbMac.length() > 0 && testSbc.length() > 0 ) {
						paramVO.setConSaId(conSaId);
						paramVO.setConMac(conStbMac);
						paramVO.setTestSbc(testSbc);
					}
				}
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}

	}
	
	/**
	 * VOD정보 조회
	 * @param paramVO
	 * @return paramVO	
	 */
	public GetNSViewInfoResponseVO getViewInfoList (GetNSViewInfoRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		GetNSViewInfoResponseVO tempVO = new GetNSViewInfoResponseVO();
		
		try {
			tempVO = getNSViewInfoDao.getNSViewInfoList(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return tempVO;

	}
	
	/**
	 * 구매 여부 조회
	 * @param paramVO
	 * @return paramVO	
	 */
	public void getChkBuyInfo(GetNSViewInfoRequestVO paramVO, GetNSViewInfoResponseVO tempVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		try {
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			if(paramVO.getLinkFlag().equals("S")) {
				HashMap<String, String> mChkBuyInfo = getNSViewInfoDao.chkBuyInfo(paramVO);
				int duplicChk = Integer.parseInt(mChkBuyInfo.get("DUPLIC_CHK"));
				String buyDate = mChkBuyInfo.get("BUY_DATE");

				if(duplicChk > 0) {
					tempVO.setBuyingDate(buyDate);
					tempVO.setBuyFlag("0");
				} else {
					tempVO.setBuyFlag("1");
				}
			} else {
				HashMap<String, String> mChkBuyInfo = getNSViewInfoDao.chkBuyInfoNsc(paramVO);
				int duplicChk = Integer.parseInt(mChkBuyInfo.get("DUPLIC_CHK"));
				String buyDate = mChkBuyInfo.get("BUY_DATE");
				
				if(duplicChk > 0) {
					tempVO.setBuyingDate(buyDate);
					tempVO.setBuyFlag("0");
				} else {
					tempVO.setBuyFlag("1");
				}
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}

	}
	
	
	/**
	 * 가입 여부 조회
	 * @param paramVO
	 * @return paramVO	
	 */
	public void getChkSubscriptionInfo(GetNSViewInfoRequestVO paramVO, GetNSViewInfoResponseVO tempVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		try {
			int kidProd = 0;
			
			
			if(paramVO.getLinkFlag().equals("S")) {
				
				if(tempVO.getBookYn().equals("Y")) {
					kidProd = this.kidProductCd(paramVO);
					
					if(kidProd == 1) {
						tempVO.setBuyingDate("N");
					}
					
					if(tempVO.getBuyingDate().equals("N")) {
						tempVO.setBuyFlag("0");
					} else {
						tempVO.setBuyFlag("1");
					}
					
				} else {
					String[] productcd = getNSViewInfoDao.getProductCd(paramVO);
					
					String[] productInfo = getNSViewInfoDao.getProductInfo(paramVO);
					
					if(productcd.length > 0 && productInfo.length > 0) {
						for(int i = 0; i < productcd.length; i++) {
							for(int j = 0; j < productInfo.length; j++) {
								if(productcd[i].equals(productInfo[j])) {
									tempVO.setBuyingDate("N");
									break;
								}
							}
							if (tempVO.getBuyingDate().equals("N")) {
								break;
							}
						}

						if(tempVO.getBuyingDate().equals("N")) {
							tempVO.setBuyFlag("0");
						} else {
							tempVO.setBuyFlag("1");
						}
					} else {
						tempVO.setBuyFlag("1");
					}
				}
			} else {
				String[] productcd = getNSViewInfoDao.getProductCdNsc(paramVO);
				
				String[] productInfo = getNSViewInfoDao.getProductInfoNsc(paramVO);
				
				if(productcd.length > 0 && productInfo.length > 0) {
					for(int i = 0; i < productcd.length; i++) {
						for(int j = 0; j < productInfo.length; j++) {
							if(productcd[i].equals(productInfo[j])) {
								tempVO.setBuyingDate("N");
								break;
							}
						}
						if (tempVO.getBuyingDate().equals("N")) {
							break;
						}
					}
					
					if(tempVO.getBuyingDate().equals("N")) {
						tempVO.setBuyFlag("0");
					} else {
						tempVO.setBuyFlag("1");
					}
				} else {
					tempVO.setBuyFlag("1");
				}
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}

	}
	
	/**
	 * 이어보기 시간 정보 조회
	 * @param paramVO
	 * @return paramVO	
	 */
	public void getSetTimeInfo(GetNSViewInfoRequestVO paramVO, GetNSViewInfoResponseVO tempVO) {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		try {
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			if(paramVO.getLinkFlag().equals("S")) {
				String mSetTimeInfo = getNSViewInfoDao.getSetTimeInfo(paramVO);
				
				if (mSetTimeInfo != null && mSetTimeInfo.length() > 0) {
					tempVO.setLinkTime(mSetTimeInfo);
				} 
			} else {
				String mSetTimeInfo = getNSViewInfoDao.getSetTimeInfoNsc(paramVO);
				
				if (mSetTimeInfo !=null && mSetTimeInfo.length() > 0) {
					tempVO.setLinkTime(mSetTimeInfo);
				}
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
	}
	

	
	/**
	 * 엔스크린(NSCREEN) 키즈 가입 여부 체크
	 * @param paramVO
	 * @throws Exception
	 */
	private int kidProductCd(GetNSViewInfoRequestVO paramVO) throws Exception
	{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		int kidProduct = 0;
		String szMsg = "";
		
		try
		{
			kidProduct = getNSViewInfoDao.kidProductCd(paramVO);
		}
		catch(Exception e)
		{
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			paramVO.setResultCode("21000000");
		}
		
		return kidProduct;
	}
	
	// 엔스크린 불가 콘텐츠 체크
	public boolean getNscreenStatus(GetNSViewInfoRequestVO paramVO, GetNSViewInfoResponseVO tempVO) {
		
		boolean result = true;
		
		if(paramVO.getLinkFlag().equals("S")) {	
			// 엔스크린 정보
			String[] nScreenInfo = tempVO.getNscreenYn().split(";");
			// 엔스크린 불가 콘텐츠
			if(nScreenInfo[0].equalsIgnoreCase("N")) {
				result = false;
			}else {
				// 모바일->IPTV 단방향 콘텐츠(엔스크린 불가 콘텐츠)
				if(nScreenInfo[2].equalsIgnoreCase("M")) {
					result = false;
				}
			}
		}
		
		return result;
	}
	

}
