package kr.co.wincom.imcs.api.getFXProdInfo;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.DateUtil;
import kr.co.wincom.imcs.common.util.FileUtil;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComCdVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetFXProdInfoServiceImpl implements GetFXProdInfoService {
	private Log imcsLogger		= LogFactory.getLog("API_getFXProdInfo");
	
	@Autowired
	private GetFXProdInfoDao getFXProdInfoDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getFXProdInfo(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	/**
	 * @author HONG
	 */
	@Override
	public GetFXProdInfoResultVO getFXProdInfo(GetFXProdInfoRequestVO paramVO)	{
//		this.getFXProdInfo(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		List<GetFXProdInfoResponseVO> resultVO		= new ArrayList<GetFXProdInfoResponseVO>();
		List<GetFXProdInfoResponseVO> returnVO		= new ArrayList<GetFXProdInfoResponseVO>();
		GetFXProdInfoResponseVO tempVO				= new GetFXProdInfoResponseVO();
		GetFXProdInfoResultVO	resultListVO		= new GetFXProdInfoResultVO();
		
		int nMainCnt	= 0;
		int nWaitCnt	= 0;
		int nSubCnt 	= 0;
		
		long tp_start	= paramVO.getTp_start();
		long tp1		= System.currentTimeMillis();		// timePoint 1
		long tp2		= System.currentTimeMillis();		// timePoint 2
		
				
		
		String msg			= "";
		String LOCKFILE	= "";
		String RESFILE	= "";
		String result	= "";
		String LOCALPATH = "";
		
		LOCALPATH = commonService.getCachePath("LOCAL", ImcsConstants.FXAPI_PRO_ID030.split("/")[1], imcsLog);
		
		LOCKFILE = LOCALPATH + "/getFXProdInfoSbc.lock";
		RESFILE = LOCALPATH +"/getFXProdInfoSbc.res";
		
		
		String szPpmImgSvrip = "";
		
		try {
			szPpmImgSvrip	= commonService.getIpInfo("img_ppm_server", ImcsConstants.FXAPI_PRO_ID030.split("/")[1]);
		} catch(Exception e) {
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID030, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
			throw new ImcsException();
		}
		
		try {
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp1 - tp_start), methodName, methodLine);
			
			int nComCnt	= 10;	// HDTV PROD SBC 갯수
			
			String arrComName[] = new String[nComCnt];
			File res = new File(RESFILE);
			
			// 파일이 존재하면 읽고 아니면 lock 파일 로직 실행
			if(res.exists()) {
				result = FileUtil.fileRead(RESFILE, "UTF-8");
				
				if(!"".equals(result)) {
					msg = " File [" + RESFILE + "] rcvbuf... [" + res.length() + "] bytes ";
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
				
				//FileUtil.unlock(LOCKFILE, imcsLog);
			} else {
				msg = " File [" + RESFILE + "] open Failed";
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				while(FileUtil.lock(LOCKFILE, imcsLog)){
					Thread.sleep(1000);
					nWaitCnt++;
					
					msg = " queryWaitCheck Sleep [" + nWaitCnt + "] sec";
					imcsLog.serviceLog(msg, methodName, methodLine);
	
					if(nWaitCnt >= 5){
						msg = " wait_count overload Failed svc2[" + ImcsConstants.FXAPI_PRO_ID030 + "] sts[    0] msg["+ String.format("%-17s", "par_yn:" + ImcsConstants.RCV_MSG2 +"]");
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						break;
					}
					
					if(res.exists()) {
						result = FileUtil.fileRead(RESFILE, "UTF-8");
						
						if(!"".equals(result)) {
							msg = " File [" + RESFILE + "] rcvbuf... [" + res.length() + "] bytes ";
							imcsLog.serviceLog(msg, methodName, methodLine);
						}
						
						FileUtil.unlock(LOCKFILE, imcsLog);
						break;
					}
				}
				
				// HDTV PROD SBC 조회
				List<ComCdVO> lstComCdVO = new ArrayList<ComCdVO>();
				lstComCdVO	= this.getProdSbc(paramVO);
				
				for(int i = 0; i < lstComCdVO.size(); i++) {
					int nComCd	= Integer.parseInt(lstComCdVO.get(i).getComCd()) - 1;
					arrComName[nComCd]	= lstComCdVO.get(i).getComName();
				}
				
				for(int i = 0; i < nComCnt; i++) {
					result	= result + arrComName[i] + "|";
				}
				
				// 파일 쓰기
				int nRetVal = FileUtil.fileWrite(RESFILE, result, false);
				
				if(nRetVal == 1) {
					msg = " File [" + RESFILE + "] WRITE [" + res.length() + "] bytes Finished";
					imcsLog.serviceLog(msg, methodName, methodLine);
				} else {
					msg = " File [" + RESFILE + "] WRITE failed";
					imcsLog.serviceLog(msg, methodName, methodLine);		
				}
				FileUtil.unlock(LOCKFILE, imcsLog);
				
				tp1	= System.currentTimeMillis();
				imcsLog.timeLog("HDTV PROD SBC 조회 (캐쉬생성)", String.valueOf(tp1 - tp2), methodName, methodLine);
			}
			
			/*
			if(!"".equals(result)) {
				arrComName	= result.split("\\|");
				
				tp1	= System.currentTimeMillis();
				imcsLog.timeLog("HDTV PROD SBC 조회 (파일캐쉬)", String.valueOf(tp1 - tp2), methodName, methodLine);
			} else {
				// HDTV PROD SBC 조회
				List<ComCdVO> lstComCdVO = new ArrayList<ComCdVO>();
				lstComCdVO	= this.getProdSbc(paramVO);
				
				for(int i = 0; i < lstComCdVO.size(); i++) {
					int nComCd	= Integer.parseInt(lstComCdVO.get(i).getComCd()) - 1;
					arrComName[nComCd]	= lstComCdVO.get(i).getComName();
				}
				
				for(int i = 0; i < nComCnt; i++) {
					result	= result + arrComName[i] + "|";
				}
				
				// 파일 쓰기
				int nRetVal = FileUtil.fileWrite(RESFILE, result, false);
				
				if(nRetVal == 1) {
					msg = " File [" + RESFILE + "] WRITE [" + res.length() + "] bytes Finished";
					imcsLog.serviceLog(msg, methodName, methodLine);
				} else {
					msg = " File [" + RESFILE + "] WRITE failed";
					imcsLog.serviceLog(msg, methodName, methodLine);		
				}
				FileUtil.unlock(LOCKFILE, imcsLog);
				
				tp1	= System.currentTimeMillis();
				imcsLog.timeLog("HDTV PROD SBC 조회 (캐쉬생성)", String.valueOf(tp1 - tp2), methodName, methodLine);
			}*/
			
			arrComName	= result.split("\\|");
			
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("HDTV PROD SBC 조회 (파일캐쉬)", String.valueOf(tp1 - tp2), methodName, methodLine);
			
			
			// 가입자 정보 조회 (검수 STB여부 조회)
			String szTestSbc	= this.getTestSbc(paramVO);

			// 가입상품 조회
			String szProdId		= this.getProdId(paramVO);
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("가입자정보/가입상품 조회", String.valueOf(tp2 - tp1), methodName, methodLine);

			String szCurrentDate = commonService.getSysdate();
			szCurrentDate	= szCurrentDate.substring(0, 8);
			
			
			// 가입자의 상품 조회
			List<HashMap<String, String>> lstCustomerProd = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> mCustomerProd = new HashMap<String, String>();
			
			List<String> arrProdId	= new ArrayList<String>();
			lstCustomerProd	= this.getCutsomProdInfo(paramVO);
			
			if(lstCustomerProd != null && lstCustomerProd.size() > 0) {
				nSubCnt	= lstCustomerProd.size();
			}
			
			for(int i = 0; i < nSubCnt; i++) {
				mCustomerProd	= lstCustomerProd.get(i);
				arrProdId.add(mCustomerProd.get("PROD_CD"));
			}
			
			
			
			// 요금제 가입상품정보 조회
			resultVO	= this.getProdInfo(paramVO);
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("요금제 가입 상품정보 조회", String.valueOf(tp1 - tp2), methodName, methodLine);
			
			if(resultVO != null)	nMainCnt	= resultVO.size();
			
			for(int i = 0; i < nMainCnt; i++) {
				tempVO	= resultVO.get(i);
				
				String szChkProd	= "N";
				
				// 구매일, 현재일에 대한 날짜 계산 로직 적용 예정 (변수 등 정리필요)
				DateUtil dateUtil = new DateUtil();
				Date dBgnDate = dateUtil.getStringToDate(tempVO.getBgnDate(), "yyyyMMdd");
				Date dEndDate = dateUtil.getStringToDate(tempVO.getEndDate(), "yyyyMMdd");
				Date dCurrentTime = dateUtil.getStringToDate(szCurrentDate, "yyyyMMdd");
				
				// 현재일이 종료일보다 크거나 시작일보다 작으면 패스
				if(dEndDate.compareTo(dCurrentTime) < 0 || dBgnDate.compareTo(dCurrentTime) > 0) continue;
				
				
				if("1".equals(tempVO.getSubYn())) {
					for(int j = 0; j < nSubCnt; j++) {
						if(tempVO.getSubProdId().equals(arrProdId.get(j))) {
							szChkProd	= "Y";
							break;
						}
					}
					
					if("Y".equals(szChkProd))	continue;
				}
				
				
				int nScreen	= Integer.parseInt(StringUtil.nullToZero(tempVO.getScreen()));
				String szScreenType	= "";
				
				// 파일 만드는 로직 생성 - 기VTS 파일과 다름
				String szTempProdId	= "";
				String szTempSubProdId	= "";
				if(szProdId.length() > 4)				szTempProdId	= szProdId.substring(0, 4);
				if(tempVO.getSubProdId().length() > 4)	szTempSubProdId	= tempVO.getSubProdId().substring(0, 4);
				
				if("31200".equals(szTempProdId) && "31211".equals(szTempSubProdId)) {}
				else if("N".equals(szTestSbc) && tempVO.getSubProdId().equals(arrComName[0])){}
				else if("N".equals(szTestSbc) && tempVO.getSubProdId().equals(arrComName[1])){}
				else if("N".equals(szTestSbc) && tempVO.getSubProdId().equals(arrComName[2])){}
				else if("N".equals(szTestSbc) && tempVO.getSubProdId().equals(arrComName[3])){}
				else if("N".equals(szTestSbc) && tempVO.getSubProdId().equals(arrComName[4])){}
				else if("N".equals(szTestSbc) && tempVO.getSubProdId().equals(arrComName[5])){}
				else if("N".equals(szTestSbc) && tempVO.getSubProdId().equals(arrComName[6])){}
				else if("N".equals(szTestSbc) && tempVO.getSubProdId().equals(arrComName[7])){}
				else if("N".equals(szTestSbc) && tempVO.getSubProdId().equals(arrComName[8])){}
				else if("N".equals(szTestSbc) && tempVO.getSubProdId().equals(arrComName[9])){}
				else {
					if(nScreen >= 1 && nScreen <= 8)	szScreenType	= "M";
					if(nScreen % 7 >= 2 && nScreen % 7 <= 5) {
						if(szScreenType != null && !"".equals(szScreenType))	
							szScreenType	= szScreenType + "\b"; 
						szScreenType	= szScreenType + "P";
					}
					
					if(nScreen % 7 == 3 || nScreen % 7 == 4 || nScreen % 7 == 6 || nScreen == 8 || nScreen == 14) {
						if(szScreenType != null && !"".equals(szScreenType))	
							szScreenType	= szScreenType + "\b"; 
						szScreenType	= szScreenType + "T";
					}
					
					if(nScreen % 7 == 4 || nScreen % 7 == 5 || nScreen % 8 >= 6) {
						if(szScreenType != null && !"".equals(szScreenType))	
							szScreenType	= szScreenType + "\b"; 
						szScreenType	= szScreenType + "H";
					}
					
					tempVO.setScreen(szScreenType);
				}
				
				
				// FVOD가 아니면 요금제 이미지 가져오기
				if(!"".equals(tempVO.getProdType())) {
					paramVO.setSubProdId(tempVO.getSubProdId());
					StillImageVO imageVO = new StillImageVO();
					imageVO	= this.getImageFileName(paramVO);
					
					if(imageVO != null) {
						
						if(!"".equals(imageVO.getImgUrl())){
							tempVO.setImgUrl(szPpmImgSvrip);
						}
						//tempVO.setImgUrl(StringUtil.nullToSpace(imageVO.getImgUrl()));
						tempVO.setImgFileName(StringUtil.nullToSpace(imageVO.getImgFileName()));
					}
				}
				
				// 가입자 가입상품 정보 로그 기록
				if("0".equals(tempVO.getSubYn())) {
					msg	 = " 가입자 가입상품 [custom_product] [" + tempVO.getSubProdId() + "|" + tempVO.getSubProdName() + "]";
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
				
				returnVO.add(tempVO);
			}
						
			tp2 = System.currentTimeMillis();
			imcsLog.timeLog("요금제 가입 상품정보 Fetch", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			resultListVO.setList(returnVO);
		} catch(ImcsException ie) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw ie;
		} catch(Exception e) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		} finally{
			msg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID030) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID030) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);			
		}
		
		return resultListVO;
	}

	

	/**
	 * HDTV PROD SBC 조회
	 * @param GetFXProdInfoRequestVO
	 * @return
	 */
	public List<ComCdVO> getProdSbc(GetFXProdInfoRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod030_001_20171214_001";
		
		List<ComCdVO> list	= new ArrayList<ComCdVO>();
		int querySize = 0;
		
		try {
			try {
				list = getFXProdInfoDao.getProdSbc(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list == null || list.isEmpty()) {
//				imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID030, sqlId, cache, "hdtvsbc_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			} else {
				querySize = list.size();
			}
			
			//C에서 주석 처리된 로그
			try{
//				imcsLog.dbLog2(ImcsConstants.FXAPI_PRO_ID030, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){
				IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
				imcsLog.debugLog(methodName,"NosqlNoLogging");
			}
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID030, sqlId, cache, "hdtvsbc_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return list;
	}











	/**
	 * 이미지 파일명 조회
	 * @param 	GetFXProdInfoRequestVO paramVO
	 * @return  String
	 **/
	public StillImageVO getImageFileName(GetFXProdInfoRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod030_040_20171214_001";
				
		List<StillImageVO> list = new ArrayList<StillImageVO>();
		StillImageVO resultVO = null;
		int querySize = 0;
		
		try {
			try {
				list = getFXProdInfoDao.getImageFileName(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list != null && !list.isEmpty()) {
				resultVO		= list.get(list.size() - 1);
			} else {
				querySize = list.size();
			}
			
			//C에서 주석 처리된 로그
			try{
//				imcsLog.dbLog2(ImcsConstants.FXAPI_PRO_ID030, sqlId, cache, querySize, methodName, methodLine);
			}catch (Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return resultVO;
	}
	
	
	
	@SuppressWarnings("rawtypes")
	public List<HashMap<String, String>> getCutsomProdInfo(GetFXProdInfoRequestVO paramVO) throws Exception {
	   	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		paramVO.setCustomUflix("0");
		String sqlId = "fxvod030_030_20171214_001";
		
		List<HashMap<String, String>> list	= new ArrayList<HashMap<String, String>>();
		int querySize = 0;
		
		try {	
			try {
				list = getFXProdInfoDao.getCutsomProdInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				for(int i = 0; i < list.size(); i++) {
					if("Y".equals(list.get(i).get("UFLIX_PROD_YN"))){
						if("0".equals(paramVO.getCustomUflix())) {
							if(!"0".equals(list.get(i).get("CONCURRENT_COUNT"))) {
								paramVO.setCustomUflix("2");
							} else {
								paramVO.setCustomUflix("1");
							}
						}
					}
				}
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			//C에서 주석 처리된 로그 추후 체크 필요
			try{
				querySize = list.size();
//				imcsLog.dbLog2(ImcsConstants.FXAPI_PRO_ID030, sqlId, cache, querySize, methodName, methodLine);
			} catch (Exception e) {}
				
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}

		return list;
}


	/**
	 * 요금제 가입상품정보 조회
	 * @param 	GetFXProdInfoRequestVO paramVO
	 * @return  List<GetFXProdInfoResponseVO>
	 **/
	public List<GetFXProdInfoResponseVO> getProdInfo(GetFXProdInfoRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod030_031_20171214_001";
		
		if("Y".equals(paramVO.getSortYn()))		sqlId = "fxvod030_031_20171214_001";
		else									sqlId = "fxvod030_032_20171214_001";
		
		List<GetFXProdInfoResponseVO> list	= new ArrayList<GetFXProdInfoResponseVO>();
		int querySize = 0;
		
		try {
			try {
				if("Y".equals(paramVO.getSortYn()))	
					list = getFXProdInfoDao.getProdInfo1(paramVO);
				else 
					list = getFXProdInfoDao.getProdInfo2(paramVO);
				
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list == null || list.isEmpty()) {
//				imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID030, sqlId, cache, "prod_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			} else {
				querySize	= list.size();
			}
			
			for(int i = 0; i < list.size(); i++) {
				if("Y".equals(list.get(i).getUflixProdYn()) && "1".equals(list.get(i).getSubYn())) {
					if("0".equals(list.get(i).getConcurrentCount())) {
						switch (paramVO.getCustomUflix()) {
						case "0":
							list.get(i).setUflixPop("2");
							list.get(i).setUflixPopMsg("구 요금제는 더이상 가입이 불가합니다.");
							break;
						case "1":
							list.get(i).setUflixPop("0");
							list.get(i).setUflixPopMsg("");
							break;
						case "2":
							list.get(i).setUflixPop("9");
							list.get(i).setUflixPopMsg("");
							break;
						default:
							break;
						}
					} else {
						switch (paramVO.getCustomUflix()) {
						case "0":
							list.get(i).setUflixPop("0");
							list.get(i).setUflixPopMsg("");
							break;
						case "1":
							list.get(i).setUflixPop("1");
							list.get(i).setUflixPopMsg("기존 상품 해지후 가입이 가능합니다.");
							break;
						case "2":
							list.get(i).setUflixPop("1");
							list.get(i).setUflixPopMsg("기존 상품 해지후 가입이 가능합니다.");
							break;
						default:
							break;
						}
					}
					if("9".equals(list.get(i).getUflixPop())){
//						continue;
						list.remove(i);
						i--;
					}
				}
			}
		
			try{
//				imcsLog.dbLog2(ImcsConstants.FXAPI_PRO_ID030, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){
				IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
				imcsLog.debugLog(methodName,"NosqlNoLogging");
			}
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID030, sqlId, cache, "prod_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return list;
	}
	


	/**
	 * 가입상품 조회
	 * @param 	GetFXProdInfoRequestVO paramVO
	 * @return  String
	 **/
	public String getProdId(GetFXProdInfoRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId	= "fxvod030_020_20171214_001";
		String szProdId	= "";
		
		List<String> list = new ArrayList<String>();
		int querySize = 0;

		try {
			try {
				list = getFXProdInfoDao.getProdId(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			if (list != null && !list.isEmpty()) {
				szProdId	= StringUtil.nullToSpace(list.get(0));
			} else {
				querySize = list.size();
			}
			
			//C에서 주석 처리된 로그
			try{
//				imcsLog.dbLog2(ImcsConstants.FXAPI_PRO_ID030, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){
				imcsLog.debugLog(methodName,"NosqlNoLogging");
			}
			
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}

		return szProdId;
	}


	
	/**
	 * 검수 STB 여부 조회
	 * @param 	GetFXProdInfoRequestVO paramVO
	 * @return  String
	 **/
	public String getTestSbc(GetFXProdInfoRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId	= "fxvod030_010_20171214_001";
		String szTestSbc	= "";
		
		List<String> list = new ArrayList<String>();
		int querySize = 0;

		try {
			try {
				list = getFXProdInfoDao.getTestSbc(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if(list != null && list.size() > 0) {
				szTestSbc	= StringUtil.replaceNull(list.get(0), "N");
			} else {
				querySize = list.size();
			}
			
			//C에서 주석 처리된 로그
			try{
//				imcsLog.dbLog2(ImcsConstants.FXAPI_PRO_ID030, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){
				imcsLog.debugLog(methodName,"NosqlNoLogging");
			}

		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}

		return szTestSbc;
	}
	
	
}
