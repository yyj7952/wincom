package kr.co.wincom.imcs.api.getNSLiveStat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsCacheService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.property.ImcsProperties;
import kr.co.wincom.imcs.common.util.GlobalCom;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.StatVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class GetNSLiveStatController {
	
	private String SERVER_CONF4 = ImcsProperties.getProperty("filepath.cachepath");
	private Properties serverManager4 = GlobalCom.getPropertyFile(SERVER_CONF4);
	
	private Log imcsCommonLogger = LogFactory.getLog("API_Common_getNSLiveStat");
	private Log imcsLogger = LogFactory.getLog("API_getNSLiveStat");
	private Log statLogger = LogFactory.getLog("TLO_getNSLiveStat");

	@Autowired
	private GetNSLiveStatService getNSLiveStatService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ImcsCacheService imcsCacheService;

	public Object getNSLiveStat(HttpServletRequest request, HttpServletResponse response, String szParam, String szStat) throws Exception {
		// 프로세스 정보
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName	= oStackTrace.getMethodName();						// 메소드명
		String methodLine	= String.valueOf(oStackTrace.getLineNumber()-3);	// 메소드라인
		String pId		= String.valueOf(Thread.currentThread().getId());
		
		GetNSLiveStatRequestVO requestVO = null;
		GetNSLiveStatResponseVO responseVO = new GetNSLiveStatResponseVO();
		GetNSLiveStatResultVO resultVO   = new GetNSLiveStatResultVO();
		String saId   = "";
		String stbMac = "";
		
		try {
			requestVO =  new GetNSLiveStatRequestVO(szParam);
			saId		= requestVO.getSaId();
			stbMac		= requestVO.getStbMac();
		} catch(Exception e) {
			resultVO.setResultCode("31000000");
			IMCSLog imcsCommonLog = new IMCSLog(imcsCommonLogger, saId, stbMac, pId);
			IMCSLog imcsLog = new IMCSLog(imcsLogger, saId, stbMac, pId);
			
			// 인입 로그
			String szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID989) + "] sts[start] rcv[" + szParam + "]";
			imcsCommonLog.serviceLog(szMsg, methodName, methodLine);
			
			String msg = " svc[" + ImcsConstants.API_PRO_ID989 + "] sts[error] msg["+ImcsConstants.RCV_MSG1 +"]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			return "1|INPUT PARAM ERROR|||||||||" + ImcsConstants.ROWSEP; //파라미터오류리턴
		}
		
		// 통계로그 정보
		StatVO statVO = null;
		try {
			statVO	= new StatVO(szStat);
			statVO.setApiNm(methodName);
			statVO.setSaId(saId);
			statVO.setStbMac(stbMac);
		} catch(Exception e) {
			resultVO.setResultCode("31000000");
			throw new ImcsException();
		}
		
		// 서비스로그 초기화
		IMCSLog imcsCommonLog = new IMCSLog(imcsCommonLogger, saId, stbMac, pId);
		IMCSLog imcsLog = new IMCSLog(imcsLogger, saId, stbMac, pId);
		long tp_start = System.currentTimeMillis();

		try {
			// 인입 로그
			String szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID989) + "] sts[start] rcv[" + szParam + "]";
			imcsCommonLog.serviceLog(szMsg, methodName, methodLine);
			
			// 메타 데이터 캐시 존재 확인 및 생성 시간 확인
			requestVO.setCacheDataFlag(this.imcsCacheService.isEhcacheValue(ImcsCacheService.LIVE_STAT_DATA_CACHE, requestVO.getAlbumId()));
			requestVO.setCacheBuyFlag(this.imcsCacheService.isEhcacheValue(ImcsCacheService.IDOL_LIVE_BUY_CACHE, requestVO.getSaId() + "-" + requestVO.getStbMac() + "-" + requestVO.getAlbumId()));
			if(requestVO.getCacheFlag().equals("M")) {
				this.imcsCacheService.removeCache(ImcsCacheService.LIVE_STAT_DATA_CACHE, requestVO.getAlbumId());
				
				return "메타 캐시 삭제 완료";
			} else if(requestVO.getCacheFlag().equals("B")) {
				this.imcsCacheService.removeCache(ImcsCacheService.IDOL_LIVE_BUY_CACHE, requestVO.getSaId() + "-" + requestVO.getStbMac() + "-" + requestVO.getAlbumId());
				
				return "구매 캐시 삭제 완료";
			} else if(!requestVO.getCacheDataFlag() || !this.checkCacheCreateTime(requestVO)) {
				try {										                
					requestVO.setPid(pId);
					resultVO	= this.getNSLiveStatService.getNSLiveStat(requestVO);
				} catch(ImcsException ie) {
					if( resultVO == null )		resultVO = new GetNSLiveStatResultVO();
				} catch(Exception e) {
					if( resultVO == null )		resultVO = new GetNSLiveStatResultVO();
				} finally {
					IMCSLog statLog = new IMCSLog(statLogger);
					
					statVO.setProductId(resultVO.getProductId());
					statVO.setProductName(resultVO.getProductName());
					statVO.setProductPrice(resultVO.getProductPrice());			
					statVO.setContentsName(resultVO.getContentsName());
					statVO.setBuyingType(resultVO.getBuyingType());
					statVO.setContentsId(requestVO.getAlbumId());
					
					statVO.setResultCode(StringUtil.replaceNull(resultVO.getResultCode(), statVO.getResultCode()));
					statLog.statLog(statVO.toString());
				}
			}
			
			resultVO = this.getCacheData(requestVO, responseVO, resultVO);
		} catch(ImcsException ie) {
			resultVO = new GetNSLiveStatResultVO();
			resultVO.setResultHeader(ie.getFlag() + "|" + ie.getMessage() + "|||||||||");
		} catch(Exception e) {
			resultVO = new GetNSLiveStatResultVO();
		} finally {
			// 결과 로그 출력
			long tp_end = System.currentTimeMillis();
			imcsCommonLog.timeLog("[" + methodName + "] tx_time", String.valueOf(tp_end - tp_start), methodName, methodLine);
		}
		
		return resultVO;
	}
	
	public GetNSLiveStatResultVO getCacheData(GetNSLiveStatRequestVO requestVO, GetNSLiveStatResponseVO responseVO, GetNSLiveStatResultVO resultVO) {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, requestVO.getSaId(), requestVO.getStbMac(), requestVO.getPid());
		
		String msg = "";
		
		try {
			// 메모리 캐시에 구매 여부 체크
			if(requestVO.getCacheBuyFlag()) {
				String cacheBuyYn = (String) this.imcsCacheService.getEhcacheValue(ImcsCacheService.IDOL_LIVE_BUY_CACHE, requestVO.getSaId() + "-" + requestVO.getStbMac() + "-" + requestVO.getAlbumId());
				
				resultVO = this.setCachetoResultVO(cacheBuyYn, true, requestVO, responseVO, resultVO);
				resultVO.setResultHeader("0||" + resultVO.getResultHeader());
			} else {
				imcsLog.serviceLog(" No cache in memory", methodName, methodLine);
				// 메모리에 캐시가 존재하지 않아 로컬에서 구매 여부 캐시 파일 체크
				String strLocalRootPath = this.commonService.getCachePath("LOCAL", "idollive-cache", imcsLog);
				String commonFilePath = "/" + requestVO.getAlbumId() + "/" + requestVO.getSaId().substring(requestVO.getSaId().length() - 4) + "/";
				String cacheFileName = requestVO.getSaId() + "-" + requestVO.getStbMac() + "-" + requestVO.getAlbumId() + ".dat";
				
				File localCache = new File(strLocalRootPath + commonFilePath + cacheFileName);
				if(localCache.exists()) {
					try {
						FileReader fr = new FileReader(strLocalRootPath + commonFilePath + cacheFileName);
						BufferedReader br = new BufferedReader(fr);
						
						String cacheBuyYn = "";
						String temp = "";
						while((temp = br.readLine()) != null) {
							cacheBuyYn = cacheBuyYn + temp;
						}
						
						resultVO = this.setCachetoResultVO(cacheBuyYn, true, requestVO, responseVO, resultVO);
						resultVO.setResultHeader("0||" + resultVO.getResultHeader());
						
						// 구매 여부 메모리에 저장
						this.imcsCacheService.putEhcacheValue(ImcsCacheService.IDOL_LIVE_BUY_CACHE, requestVO.getSaId() + "-" + requestVO.getStbMac() + "-" + requestVO.getAlbumId(), cacheBuyYn);
						imcsLog.serviceLog(" save cache to memory", methodName, methodLine);
					} catch(ImcsException ie) { 
						imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
						throw new ImcsException(ImcsConstants.FAIL_CODE, ie.getMessage() == null ? "로컬 캐시를 읽는 중 오류 발생" : ie.getMessage());
					} catch(Exception e) {
						imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
						throw new ImcsException(ImcsConstants.FAIL_CODE, "로컬 캐시를 읽는 중 오류 발생");
					}
				} else {
					imcsLog.serviceLog(" No cache in local", methodName, methodLine);
					
					String strNasRootPath = this.commonService.getCachePath("NAS", "idollive-cache", imcsLog);
					
					File nasCache = new File(strNasRootPath + commonFilePath + cacheFileName);
					if(nasCache.exists()) {
						try {
							FileReader fr = new FileReader(strNasRootPath + commonFilePath + cacheFileName);
							BufferedReader br = new BufferedReader(fr);
							
							String cacheBuyYn = "";
							String temp = "";
							while((temp = br.readLine()) != null) {
								cacheBuyYn = cacheBuyYn + temp;
							}
							
							resultVO = this.setCachetoResultVO(cacheBuyYn, true, requestVO, responseVO, resultVO);
							resultVO.setResultHeader("0||" + resultVO.getResultHeader());
							
							// 구매 여부 메모리에 저장
							this.imcsCacheService.putEhcacheValue(ImcsCacheService.IDOL_LIVE_BUY_CACHE, requestVO.getSaId() + "-" + requestVO.getStbMac() + "-" + requestVO.getAlbumId(), cacheBuyYn);
							imcsLog.serviceLog(" save cache to memory", methodName, methodLine);
							// 구매 여부 로컬에 복사
							this.copyCache(commonFilePath, cacheFileName, requestVO);
							imcsLog.serviceLog(" copy cache to local", methodName, methodLine);
						} catch(ImcsException ie) { 
							imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
							throw new ImcsException(ImcsConstants.FAIL_CODE, ie.getMessage() == null ? "나스 캐시를 읽는 중 오류 발생" : ie.getMessage());
						} catch(Exception e) {
							imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
							throw new ImcsException(ImcsConstants.FAIL_CODE, "나스 캐시를 읽는 중 오류 발생");
						}
					} else {
						imcsLog.serviceLog(" No cache in nas", methodName, methodLine);
						
						resultVO = this.setCachetoResultVO(null, false, requestVO, responseVO, resultVO);
						resultVO.setResultHeader("0|구매내역이 존재하지 않습니다.|" + resultVO.getResultHeader());
					}
				}
			}
		} catch(ImcsException ie) { 
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw new ImcsException(ImcsConstants.FAIL_CODE, ie.getMessage() == null ? "캐시 데이터를 조회하던 중 오류 발생" : ie.getMessage());
		} catch(Exception e) {
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException(ImcsConstants.FAIL_CODE, "캐시 데이터를 조회하던 중 오류 발생");
		} finally {
			resultVO.setResultCode(requestVO.getResultCode());
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID989) + "] result[" + resultVO.toString() + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultVO;
	}
	
	public GetNSLiveStatResultVO setCachetoResultVO(String cache, Boolean isCache, GetNSLiveStatRequestVO requestVO, GetNSLiveStatResponseVO responseVO, GetNSLiveStatResultVO resultVO) {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, requestVO.getSaId(), requestVO.getStbMac(), requestVO.getPid());
		
		try {
			if(requestVO.getCacheDataFlag() && this.checkCacheCreateTime(requestVO)) {
				String metaCache = (String) this.imcsCacheService.getEhcacheValue(ImcsCacheService.LIVE_STAT_DATA_CACHE, requestVO.getAlbumId());
				String[] meta = metaCache.split(ImcsConstants.ROWSEP);
				// 공통 데이터 캐시에서 헤더 분리 후 응답값 세팅
				resultVO.setResultHeader(meta[0]);
				// 공통 데이터 캐시에서 바디 분리 후 응답값 세팅
				String[] metaArray = meta[1].split("\\|");
				for(int i = 0; i < metaArray.length; i++) {
					if(i == 0) responseVO.setResultType(metaArray[i]);
					else if(i == 1) responseVO.setContsId(metaArray[i]);
					else if(i == 2) responseVO.setContsType(metaArray[i]);
					else if(i == 3) responseVO.setBuyYn(metaArray[i]);
					else if(i == 4) responseVO.setBuyingDate(metaArray[i]);
					else if(i == 5) responseVO.setExpireDate(metaArray[i]);
					else if(i == 6) responseVO.setPrice(metaArray[i]);
					else if(i == 7) responseVO.setPvodProdId(metaArray[i]);
					else if(i == 8) responseVO.setPvodProdName(metaArray[i]);
					else if(i == 9) responseVO.setInappProdId(metaArray[i]);
					else if(i == 10) responseVO.setInappPrice(metaArray[i]);
					else if(i == 11) responseVO.setnScreenYn(metaArray[i]);
					else if(i == 12) responseVO.setnBuyYn(metaArray[i]);
					else if(i == 13) responseVO.setnBuyDate(metaArray[i]);
					else if(i == 14) responseVO.setnExpiredDate(metaArray[i]);
					else if(i == 15) responseVO.setnSaId(metaArray[i]);
					else if(i == 16) responseVO.setnStbMac(metaArray[i]);
					else if(i == 17) responseVO.setWatchDate(metaArray[i]);
				}
				
				List<GetNSLiveStatResponseVO> responseVOList = new ArrayList<GetNSLiveStatResponseVO>();
				responseVOList.add(responseVO);
				resultVO.setList(responseVOList);
			}
			
			String[] contsType = resultVO.getList().get(0).getContsType().split("\b");
			
			String resultBuyYn = "";
			String resultBuyingDate = "";
			
			if(isCache) {
				String[] cacheData = cache.split("\\|");

				for(int i = 0; i < contsType.length; i++) {
					if(contsType[i].equals(cacheData[0])) {
						resultBuyYn = resultBuyYn + "\b0";
						resultBuyingDate = resultBuyingDate + "\b" + cacheData[1];
					} else {
						resultBuyYn = resultBuyYn + "\b1";
						resultBuyingDate = resultBuyingDate + "\b";
					}
				}
			} else {
				for(int i = 0; i < contsType.length; i++) {
					resultBuyYn = resultBuyYn + "\b1";
					resultBuyingDate = resultBuyingDate + "\b";
				}
			}
			
			resultVO.getList().get(0).setBuyYn(resultBuyYn);
			resultVO.getList().get(0).setBuyingDate(resultBuyingDate);
		} catch(ImcsException ie) {
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw new ImcsException(ImcsConstants.FAIL_CODE, ie.getMessage() == null ? "응답값을 세팅하던 중 오류 발생" : ie.getMessage());
		} catch(Exception e) {
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException(ImcsConstants.FAIL_CODE, "응답값을 세팅하던 중 오류 발생");
		}
		
		return resultVO;
	}
	
	public void copyCache(String path, String name, GetNSLiveStatRequestVO requestVO) {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber()-3);
		
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, requestVO.getSaId(), requestVO.getStbMac(), requestVO.getPid());
		
		FileInputStream fis = null;
		FileOutputStream fos = null;
		
		try {
			String strLocalRootPath = this.commonService.getCachePath("LOCAL", "idollive-cache", imcsLog);
			String strNasRootPath = this.commonService.getCachePath("NAS", "idollive-cache", imcsLog);
			
			File localDir = new File(strLocalRootPath + path);
			if(!localDir.exists()) {
				localDir.mkdir();
			}
			
			File localCache = new File(strLocalRootPath + path + name);
			File nasCache = new File(strNasRootPath + path + name);
			if(nasCache.exists()) {
				fis = new FileInputStream(nasCache);
				fos = new FileOutputStream(localCache);
				
				int fileByte = 0;
				while((fileByte = fis.read()) != -1) {
					fos.write(fileByte);
				}
			}
		} catch(FileNotFoundException fnfe) {
			imcsLog.errorLog(methodName + "-E",fnfe.getClass().getName()+":"+fnfe.getMessage());
			throw new ImcsException(ImcsConstants.FAIL_CODE, "캐시 파일을 복사하던 중 오류 발생");
		} catch(Exception e) {
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException(ImcsConstants.FAIL_CODE, "캐시 파일을 복사하던 중 오류 발생");
		} finally {
			try {
		         if(fis != null) fis.close();
		    } catch (IOException ioe) {
		    	imcsLog.errorLog(methodName + "-E",ioe.getClass().getName()+":"+ioe.getMessage());
				throw new ImcsException(ImcsConstants.FAIL_CODE, "파일 입출력 객체 삭제 중 오류 발생");
		    }
		    try {
		    	if(fos != null) fos.close();
		    } catch (IOException ioe) {
		    	imcsLog.errorLog(methodName + "-E",ioe.getClass().getName()+":"+ioe.getMessage());
				throw new ImcsException(ImcsConstants.FAIL_CODE, "파일 입출력 객체 삭제 중 오류 발생");
		    }
		}
	}
	
	public Boolean checkCacheCreateTime(GetNSLiveStatRequestVO requestVO) {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber()-3);
		
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, requestVO.getSaId(), requestVO.getStbMac(), requestVO.getPid());
		
		boolean reuslt = true;
		
		SimpleDateFormat fm = new SimpleDateFormat("yyyyMMddHHmmss");

		String cache = (String) this.imcsCacheService.getEhcacheValue(ImcsCacheService.LIVE_STAT_DATA_CACHE, requestVO.getAlbumId());
		String createTimeStr = cache.split("&")[1];
		
		Date createTime = null;
		Date currentTime = null;
		
		long diffSec = 0;
		try {
			createTime = fm.parse(createTimeStr);
			currentTime = new Date(System.currentTimeMillis());

			diffSec = (currentTime.getTime() / 1000) - (createTime.getTime() / 1000);
			
			if(diffSec <= Long.parseLong(serverManager4.getProperty("getNSLiveStat"))) {
				reuslt = true;
			} else {
				reuslt = false;
			}
		} catch (ParseException pe) {
			imcsLog.errorLog(methodName + "-E",pe.getClass().getName()+":"+pe.getMessage());
			throw new ImcsException(ImcsConstants.FAIL_CODE, "메타 데이터 캐시 생성 시간 확인 중 오류 발생");
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException(ImcsConstants.FAIL_CODE, "메타 데이터 캐시 생성 시간 확인 중 오류 발생");
		}
		
		return reuslt;
	}
}
