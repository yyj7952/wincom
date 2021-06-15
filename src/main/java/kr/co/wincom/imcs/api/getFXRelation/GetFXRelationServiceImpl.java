package kr.co.wincom.imcs.api.getFXRelation;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.FileUtil;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComWatchaVO;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetFXRelationServiceImpl implements GetFXRelationService {
	private Log imcsLogger		= LogFactory.getLog("API_getFXRelation");
	
	@Autowired
	private GetFXRelationDao getFXRelationDao;
	
	@Autowired
	private CommonService commonService;
	
	public void getFXRelation(String szSaId, String szStbMac, String szPid){
		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
	}
	
	private IMCSLog imcsLog = null;
	/**
	 * @author HONG
	 * @since 2016-07-12
	 * 찜목록 리스트 가져오기 (lgvod339.pc)
	 */
	@Override
	public GetFXRelationResultVO getFXRelation(GetFXRelationRequestVO paramVO)	{
		this.getFXRelation(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		List<GetFXRelationResponseVO> resultVO	= new ArrayList<GetFXRelationResponseVO>();
		List<GetFXRelationResponseVO> returnVO	= new ArrayList<GetFXRelationResponseVO>();
		List<ComWatchaVO> returnWatchaVO	= new ArrayList<ComWatchaVO>();
		
		List<GetFXRelationResponseVO> res_returnVO	= new ArrayList<GetFXRelationResponseVO>();
		List<ComWatchaVO> res_returnWatchaVO	= new ArrayList<ComWatchaVO>();
		
		GetFXRelationResponseVO tempVO			= new GetFXRelationResponseVO();
		GetFXRelationResultVO	resultListVO	= new GetFXRelationResultVO();
		
		ComWatchaVO watchaVO = new ComWatchaVO();
		
		int nMainCnt		= 0;
		int nWaitCnt		= 0;
		
		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2
		
		String msg			= "";
		String LOCKFILE		= "";
		String RESFILE		= "";
		String szImgSvrIp	= "";
		
		String LOCALPATH = "";
		
		LOCALPATH = commonService.getCachePath("LOCAL", ImcsConstants.FXAPI_PRO_ID040.split("/")[1], imcsLog);
		
		try {
			// 서버IP정보 조회
			tp1 = System.currentTimeMillis();
			tp2 = System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			
			// 검수 STB 여부 조회
			this.getTestSbc(paramVO);
			
			// 카테고리 ID 조회
			String szAdiCatId	= "";
			if("".equals(paramVO.getCatId())){
				tp1 = System.currentTimeMillis();
				szAdiCatId	= this.getCatId(paramVO);
				tp2 = System.currentTimeMillis();
				imcsLog.timeLog("연관컨텐츠정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			}				
			else
				szAdiCatId	= paramVO.getCatId();
			
			paramVO.setAdiCatId(szAdiCatId);
			
			tp2 = System.currentTimeMillis();
			
			
			LOCKFILE	= LOCALPATH + "/getFXRelation_c" + szAdiCatId + "_r" + paramVO.getPrInfo() + "_f" + paramVO.getFxType() + ".lock";
			RESFILE		= LOCALPATH + "/getFXRelation_c" + szAdiCatId + "_r" + paramVO.getPrInfo() + "_f" + paramVO.getFxType() + ".res";
			
			File res	= new File(RESFILE);
			
			// 파일이 존재하면 읽고 아니면 lock 파일 로직 실행
			if(res.exists()) {
				String result = FileUtil.fileRead(RESFILE, "UTF-8");
				
				if(!"".equals(result)) {
					msg = " File [" + RESFILE + "] rcvbuf... [" + res.length() + "] bytes ";
					imcsLog.serviceLog(msg, methodName, methodLine);
				} else { //캐시파일에서 불러올 데이터가 없는 경우
					return resultListVO;
				}
				
				String[] result_arr = result.split(ImcsConstants.ROWSEP);
				
				StringBuffer sb =  new StringBuffer();
				
				for(int i = 0; i < result_arr.length; i++){					
					String[] sub_arr = result_arr[i].split("[|]");
					if(!sub_arr[2].equals(paramVO.getAlbumId())){
						sb.append(result_arr[i]).append(ImcsConstants.ROWSEP);
					}
				}
				
				//resultListVO.setResult(result);
				resultListVO.setResult(sb.toString());
				//FileUtil.unlock(LOCKFILE, imcsLog);
				return resultListVO;		
			} else {
				msg = " File [" + RESFILE + "] open Failed";
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				while(FileUtil.lock(LOCKFILE, imcsLog)){
					Thread.sleep(1000);
					nWaitCnt++;
					
					msg = " queryWaitCheck Sleep [" + nWaitCnt + "] sec";
					imcsLog.serviceLog(msg, methodName, methodLine);
	
					if(nWaitCnt >= 5){
//						imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID040, "", null, "par_yn:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
						paramVO.setResultCode("21000000");

						throw new ImcsException();
					}
					
					if(res.exists()) {
						String result = FileUtil.fileRead(RESFILE, "UTF-8");
						
						if(!"".equals(result)) {
							msg = " File [" + RESFILE + "] rcvbuf... [" + res.length() + "] bytes ";
							imcsLog.serviceLog(msg, methodName, methodLine);
						}
						
						resultListVO.setResult(result);
						FileUtil.unlock(LOCKFILE, imcsLog);
						return resultListVO;
					}
				}
				
				try {
					szImgSvrIp	= commonService.getIpInfo("img_resize_server", ImcsConstants.FXAPI_PRO_ID040.split("/")[1]);
				} catch(Exception e) {
//					imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID040, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
					throw new ImcsException();
				}
				
				tp1 = System.currentTimeMillis();
				imcsLog.timeLog("서버IP값 조회", String.valueOf(tp1 - tp2), methodName, methodLine);
				
				
				resultVO	= this.getRelationList(paramVO);
				tp2 = System.currentTimeMillis();
				imcsLog.timeLog("연관 컨텐츠정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
				
				if(resultVO != null && resultVO.size() > 0)
					nMainCnt	= resultVO.size();
				
				imcsLog.timeLog("" + nMainCnt, String.valueOf(tp2 - tp1), methodName, methodLine);
				
				for(int i = 0; i < nMainCnt; i++) {
					tempVO	= resultVO.get(i);
				
					// 연관컨텐츠 상세 정보 조회
					//GetFXRelationSubVO contsInfoVO = new GetFXRelationSubVO();
					GetFXRelationSubVO contsInfoVO = null;
					paramVO.setContsId(tempVO.getContsId());
					paramVO.setTempCatGb(tempVO.getCatGb());
					
					if("M".equals(paramVO.getFxType()))
						contsInfoVO	= this.getContsInfo1(paramVO);
					else
						contsInfoVO	= this.getContsInfo2(paramVO);
					
					if(contsInfoVO != null) {
						tempVO.setCatId(contsInfoVO.getCatId());
						tempVO.setCatName(contsInfoVO.getCatName());
						tempVO.setAlbumId(contsInfoVO.getContsId());
						tempVO.setAlbumName(contsInfoVO.getContsName());
						tempVO.setChaNum(contsInfoVO.getChaNum());
						tempVO.setPrice(contsInfoVO.getSuggestedPrice());
						tempVO.setPrInfo(contsInfoVO.getPrInfo());
						tempVO.setRunTime(contsInfoVO.getRunTime());
						tempVO.setIs51Ch(contsInfoVO.getIs51CH());
						tempVO.setIsNew(contsInfoVO.getIsNew());
						tempVO.setIsCaption(contsInfoVO.getIsCaption());
						tempVO.setIsHd(contsInfoVO.getIsHd());
						tempVO.setIs3d(contsInfoVO.getIs3d());
						tempVO.setSeriesDesc(contsInfoVO.getSeriesDesc());
						tempVO.setRealHd(contsInfoVO.getRealHd());
						tempVO.setOnairDate(contsInfoVO.getOnairDate());
						tempVO.setTerrYn(contsInfoVO.getTerrYn());
						tempVO.setServiceIcon(contsInfoVO.getServiceIcon());
						
						paramVO.setAdiProdId(contsInfoVO.getAdiProdId());
					} else {
						contsInfoVO	= null;
						continue;
					}
					
					// 이미지 파일명 조회
					String szImgFileName	= "";
					
					//imcsLog.serviceLog(paramVO.getFxType() + "^" + paramVO.getAdiProdId() + "^" + paramVO.getPosterType(), methodName, methodLine);
					
					szImgFileName	= this.getImgFileName(paramVO);
					
					//imcsLog.serviceLog(">>>>>>>>>>>>>>>>>>>>>>>>> "+szImgFileName, methodName, methodLine);
					
					tempVO.setImgFileName(szImgFileName);
					tempVO.setImgUrl(szImgSvrIp);
					
					
					// 포인트 정보 지정
					String szDbType	= StringUtil.nullToSpace(paramVO.getDbType());
					if(szDbType.length() > 3)	szDbType	= szDbType.substring(0, 3);
					
					if(!tempVO.getPoint().equals(contsInfoVO.getPoint()) && "ORA".equals(szDbType))
						tempVO.setPoint(contsInfoVO.getPoint());
					
					// 5.1채널 여부 지정
					if("DOLBY 5.1".equals(tempVO.getIs51Ch()))
						tempVO.setIs51Ch("Y");
					else 
						tempVO.setIs51Ch("N");
					
					// HD 여부 지정
					if("Y".equals(tempVO.getIsHd()) || "S".equals(tempVO.getIsHd()))
						tempVO.setIsHd("Y");
					else if("N".equals(tempVO.getIsHd()))
						tempVO.setIsHd("N");
					
					// 상품타입 정보 조회 (FVOD면 가격정보 N으로 세팅)
					if("M".equals(paramVO.getFxType())){
						String szProdType	= "";
						szProdType = this.getProdType(paramVO);
						
						if("0".equals(szProdType))
							tempVO.setPrice("N");
					}
					
					
					tempVO.setSeriesYn("N");
					// 시리즈 카테고리 정보 조회
					String szSerCatId	= "";
					szSerCatId	= this.getSerCatId(paramVO);
					
					if(!"".equals(szSerCatId))
						tempVO.setSeriesYn("Y");
					
					tempVO.setSerCatId(szSerCatId);
					
					
					
					// 왓챠 정보 조회
					watchaVO = this.getWatchaInfo(paramVO);
					returnWatchaVO.add(watchaVO);
					
					returnVO.add(tempVO);
					
					if(!paramVO.getAlbumId().equals(tempVO.getAlbumId())){
						res_returnWatchaVO.add(watchaVO);
						res_returnVO.add(tempVO);
					}
					
					
				}
				
				tp1 = System.currentTimeMillis();
				imcsLog.timeLog("연관컨텐츠정보 FETCH", String.valueOf(tp1 - tp2), methodName, methodLine);
				
				//resultListVO.setWatchaVO(watchaVO);
				resultListVO.setWlist(returnWatchaVO);
				
				resultListVO.setList(returnVO);

				// 파일 쓰기
				int nRetVal = FileUtil.fileWrite(RESFILE, resultListVO.toString(), false);
				
				if(nRetVal == 1) {
					msg = " File [" + RESFILE + "] WRITE [" + res.length() + "] bytes Finished";
					imcsLog.serviceLog(msg, methodName, methodLine);
				} else {
					msg = " File [" + RESFILE + "] WRITE failed";
					imcsLog.serviceLog(msg, methodName, methodLine);		
				}
				
				FileUtil.unlock(LOCKFILE, imcsLog);
				
				
				resultListVO = new GetFXRelationResultVO();
				resultListVO.setWlist(res_returnWatchaVO);
				resultListVO.setList(res_returnVO);
			}
			
		} catch(ImcsException ie) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
			throw ie;
		} catch(Exception e) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException();
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID040) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}

	
	
	/**
	 * 왓챠 정보 조회
	 * @param 	GetFXRelationRequestVO paramVO
	 * @return  String
	 **/
	public ComWatchaVO getWatchaInfo(GetFXRelationRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String sqlId = "fxvod040_s01_20171214_001";
		
		List<ComWatchaVO> list = new ArrayList<ComWatchaVO>();
		//ComWatchaVO resultVO = null;
		ComWatchaVO resultVO = new ComWatchaVO();
		int querySize = 0;
		
		long tp1 = System.currentTimeMillis();
		long tp2 = System.currentTimeMillis();		
		
		try {
			try {
				list = getFXRelationDao.getWatchaInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list != null && !list.isEmpty()) {
				resultVO	= list.get(0);
			} else {
				querySize = list.size();
			}
			
			//C에서 주석 처리된 로그
			try{
//				imcsLog.dbLog2(ImcsConstants.FXAPI_PRO_ID040, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
			tp2 = System.currentTimeMillis();
			imcsLog.timeLog("왓챠 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
		}catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID040, sqlId, cache, "cate_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return resultVO;
	}
	
	
	
	
	/**
	 * 시리즈 카테고리 여부 조회
	 * @param 	GetFXRelationRequestVO paramVO
	 * @return  String
	 **/
	public String getSerCatId(GetFXRelationRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String sqlId = "fxvod040_050_20171214_001";
		String resultVO = "";
		
		List<String> list = new ArrayList<String>();
		int querySize = 0;
		
		try {
			String szContsId	= paramVO.getContsId();
			if(!"NSC".equals(paramVO.getCatGb()))
				szContsId	= szContsId	 + "_IPTV";
			
			try {
				list = getFXRelationDao.getSerCatId(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}			
			
			if (list != null && !list.isEmpty()) {
				resultVO	= StringUtil.nullToSpace(list.get(0));
			} else {
				querySize = list.size();
			}

			//C에서 주석 처리된 로그
			try{
//				imcsLog.dbLog2(ImcsConstants.FXAPI_PRO_ID040, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}

		}catch (Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return resultVO;
	}
	
	
	
	
	
	/**
	 * 상품타입정보 조회
	 * @param 	GetFXRelationRequestVO paramVO
	 * @return  String
	 **/
	public String getProdType(GetFXRelationRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String sqlId = "fxvod040_040_20171214_001";
		String resultVO = "";
		
		List<String> list = new ArrayList<String>();
		int querySize = 0;
		
		try {
			try {
				list = getFXRelationDao.getProdType(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list != null && !list.isEmpty()) {
				resultVO	= StringUtil.nullToSpace(list.get(0));
			} else {
				querySize = list.size();
			}
			
			//C에서 주석 처리된 로그
			try{
//				imcsLog.dbLog2(ImcsConstants.FXAPI_PRO_ID040, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}

		}catch (Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return resultVO;
	}
	
	
	
	
	/**
	 * 이미지 파일명 조회
	 * @param 	GetFXRelationRequestVO paramVO
	 * @return  String
	 **/
	public String getImgFileName(GetFXRelationRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod040_002_20171214_001";
		
		if("P".equals(paramVO.getFxType()) || "T".equals(paramVO.getFxType())) { 
			sqlId	= "fxvod040_002_20171214_001";
		} else {
			sqlId	= "fxvod040_003_20171214_001";
			//paramVO.setAdiProdId(paramVO.getContsId());			
		}
		
		List<String> list = new ArrayList<String>();
		String resultVO = "";
		
		int querySize	= 0;
		
		try {
			try {
				list = getFXRelationDao.getImgFileName(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list != null && !list.isEmpty()) {
				resultVO	= StringUtil.nullToSpace(list.get(0));

				querySize	= list.size();
			}
			
			try{
//				imcsLog.dbLog2(ImcsConstants.FXAPI_PRO_ID040, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		}catch (Exception e) {
//			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
//			
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID040, sqlId, cache, "poster_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return resultVO;
	}
	
	
	
	/**
	 * 연관컨텐츠 상세정보 조회2
	 * @param 	GetFXRelationRequestVO paramVO
	 * @return  String
	 **/
	public GetFXRelationSubVO getContsInfo2(GetFXRelationRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod040_032_20171214_001";
		
		List<GetFXRelationSubVO> list = new ArrayList<GetFXRelationSubVO>();
		GetFXRelationSubVO resultVO = null;
		int querySize = 0;
		
		try {
			try {
				list = getFXRelationDao.getContsInfo2(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list != null && !list.isEmpty()) {
				resultVO	= list.get(0);
			} else {
				querySize = list.size();
			}
			
			//C에서 주석 처리된 로그
			try{
//				imcsLog.dbLog2(ImcsConstants.FXAPI_PRO_ID040, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		}catch (Exception e) {
//			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
//			
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID040, sqlId, cache, "hdtvsbc_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return resultVO;
	}

	
	
	/**
	 * 연관컨텐츠 상세정보 조회1
	 * @param 	GetFXRelationRequestVO paramVO
	 * @return  String
	 **/
	public GetFXRelationSubVO getContsInfo1(GetFXRelationRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod040_031_20171214_001";
		
		List<GetFXRelationSubVO> list = new ArrayList<GetFXRelationSubVO>();
		GetFXRelationSubVO resultVO = null;
		int querySize = 0;
		
		try {
			try {
				list = getFXRelationDao.getContsInfo1(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list.get(0) != null && !list.isEmpty()) {
				resultVO	= list.get(0);
			} else {
				querySize = list.size();
			}
			
			//C에서 주석 처리된 로그
			try{
//				imcsLog.dbLog2(ImcsConstants.FXAPI_PRO_ID040, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){
				imcsLog.debugLog(methodName,"NosqlNoLogging");
			}
			
		}catch (Exception e) {
//			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
//			
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID040, sqlId, cache, "hdtvsbc_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return resultVO;
	}

	
	
	
	
	/**
	 * 연관컨텐츠 리스트 조회
	 * @param 	GetFXRelationRequestVO paramVO
	 * @return  String
	 **/
	public List<GetFXRelationResponseVO> getRelationList(GetFXRelationRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod040_020_20171214_001";

		List<GetFXRelationResponseVO> list = new ArrayList<GetFXRelationResponseVO>();
		
		int querySize	= 0;
		
		try {
			try {
				list = getFXRelationDao.getRelationList(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list == null || list.isEmpty()) {
				querySize	= 0;
			} else {
				querySize	= list.size();
			}
			
			try{
//				imcsLog.dbLog2(ImcsConstants.FXAPI_PRO_ID040, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		}catch (Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return list;
	}


	
	/**
	 * 카테고리ID 조회 (cat_id 미존재 시)
	 * @param 	GetFXRelationRequestVO paramVO
	 * @return  String
	 **/
	public String getCatId(GetFXRelationRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod040_011_20171214_001";

		List<String> list = new ArrayList<String>();

		String resultVO	= "";
		int querySize	= 0;
		
		try {
			String szAlbumId	= paramVO.getAlbumId();
			
			if(!"NSC".equals(paramVO.getCatGb()))
				szAlbumId	= szAlbumId	+ "_IPTV";
			
			try {
				list = getFXRelationDao.getCatId(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list == null || list.isEmpty()) {
				querySize	= 0;
			} else {
				querySize	= list.size();
				resultVO	= StringUtil.replaceNull(list.get(0), "");
			}
			
			try{
//				imcsLog.dbLog2(ImcsConstants.FXAPI_PRO_ID040, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
		
		
		}catch (Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return resultVO;
	}



	/**
	 * 검수 STB 여부 조회
	 * @param 	GetFXRelationRequestVO paramVO
	 * @return  String
	 **/
	public void getTestSbc(GetFXRelationRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod040_001_20171214_001";

		List<String> list = new ArrayList<String>();
		int querySize	= 0;
		
		try {			
			try {
				list = getFXRelationDao.getTestSbc(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list == null || list.isEmpty()) {
				querySize	= 0;
				paramVO.setViewFlag2("V");
			} else {
				paramVO.setViewFlag2(StringUtil.replaceNull(list.get(0), "V"));
				querySize	= list.size();
			}
			
			try{
//				imcsLog.dbLog2(ImcsConstants.FXAPI_PRO_ID040, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
			
		} catch (Exception e) {
			paramVO.setViewFlag2("V");
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
	}

	
	
}
