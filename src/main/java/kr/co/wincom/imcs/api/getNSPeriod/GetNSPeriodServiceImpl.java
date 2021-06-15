package kr.co.wincom.imcs.api.getNSPeriod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComVersionVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetNSPeriodServiceImpl implements GetNSPeriodService {
	private Log imcsLogger = LogFactory.getLog("API_getNSPeriod");
	
	@Autowired
	private GetNSPeriodDao getNSPeriodDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSPeriod(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public GetNSPeriodResultVO getNSPeriod(GetNSPeriodRequestVO paramVO){
//		this.getNSPeriod(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess	= ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		
		List<GetNSPeriodResponseVO> resultVO	= new ArrayList<GetNSPeriodResponseVO>();
		GetNSPeriodResponseVO tempVO			= new GetNSPeriodResponseVO();
		GetNSPeriodResponseVO tempCacheVO		= new GetNSPeriodResponseVO();
		GetNSPeriodResultVO resultListVO 		= new GetNSPeriodResultVO();
		
		String msg	= "";
		String LOCKFILE	= "";
		String RESFILE	= "";
		String szPrInfo	= "";
		
		String LOCALPATH = "";
		
		LOCALPATH = commonService.getCachePath("LOCAL", ImcsConstants.API_PRO_ID992.split("/")[1], imcsLog);
		
		int nMainCnt = 0;

		long tp1 = 0;
		long tp2 = 0;
		long tp3 = 0;	
		
		try{
			// 가입자 해피콜 완료 여부 조회
			tp1	= System.currentTimeMillis();
			String szVodUseYn	= "N";
			szVodUseYn	= this.getVodUseYn(paramVO);
			
			tp2	= System.currentTimeMillis();	    	
			imcsLog.timeLog("가입자 해피콜완료 여부 조회", String.valueOf((tp2 - tp1)), methodName, methodLine);
						
			LOCKFILE	= LOCALPATH + "/getNSPeriod.lock";
			RESFILE		= LOCALPATH + "/getNSPerio.res";
			String sql_id = "";
			
			
			File res = new File(RESFILE);
			
			if(res.exists()){
				try {
					tempVO = this.getFileRead(RESFILE);
					
					if("N".equals(szVodUseYn)) {
						tempVO.setGuideCnt("1");
					}
					
					// 가입자 연령정보 조회
					tp1	= System.currentTimeMillis();
					String sqlId	= "lgvod992_002_20171214_001";
					szPrInfo	= this.getPrInfo(paramVO, sqlId);
					szPrInfo	= StringUtil.replaceNull(szPrInfo, "01");
					tempVO.setPrInfo(szPrInfo);
					tp2	= System.currentTimeMillis();
					
					imcsLog.timeLog("가입자 연령정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
					
					msg = "File [" + RESFILE + "] rcvbuf... [" + res.length() + "] bytes ";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
				} catch (Exception e) {
				} finally {
					resultVO.add(tempVO);
					resultListVO.setList(resultVO);
				}
				
				return resultListVO;
			}else{
				msg = "File ["+ RESFILE +"] read Failed";
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				res.delete();
			}
			
			int nWaitCnt = 0;
			
			while(queryLock(LOCKFILE, paramVO)){
				Thread.sleep(1000);
				nWaitCnt++;
				
				msg = "queryWaitCheck Sleep [" + nWaitCnt + "] sec";
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				if(nWaitCnt >= 5){
					break;
				}
				
				res = new File(RESFILE);
				
				if(res.exists()){
					try {
						tempVO = this.getFileRead(RESFILE);
					} catch (Exception e) {
					} finally{
						resultVO.add(tempVO);
						resultListVO.setList(resultVO);
					}
					
					return resultListVO;
				}
			}
			
			if(nWaitCnt >= 5){
				msg = "wait_count overload Failed svc2["+ImcsConstants.API_PRO_ID992+"] sts[API_PRO_ID992] msg["+ String.format("%-17s", "par_yn:" + ImcsConstants.RCV_MSG2 +"]");
				imcsLog.serviceLog(msg, methodName, methodLine);
				paramVO.setResultCode("21000000");
				
				throw new ImcsException();
			}
			
			
			
			// 카테고리 리스트 업데이트 주기 Fetch 
			List<String> lCateUpdPeriode = new ArrayList<String>();
			String szCateUpdPeriode = "";
			lCateUpdPeriode = this.getCateUpdPeriode(paramVO);
			
			nMainCnt = 0;
		
			if(lCateUpdPeriode != null){
				nMainCnt = lCateUpdPeriode.size();
			}
			
			for(int i = 0; i < nMainCnt; i++){
				szCateUpdPeriode = lCateUpdPeriode.get(i);
				
				if(i == 0){
					tempVO.setLstUPeriode(szCateUpdPeriode);
					tempCacheVO.setLstUPeriode("LST_U_PERIODE=" + szCateUpdPeriode);
				}else{
					tempVO.setLstUPeriode(tempVO.getLstUPeriode() + ImcsConstants.ARRSEP + szCateUpdPeriode);
					tempCacheVO.setLstUPeriode(tempCacheVO.getLstUPeriode() + ImcsConstants.ARRSEP + szCateUpdPeriode);
				}
			}
			tp3	= System.currentTimeMillis();
			imcsLog.timeLog("카테고리 업데이트 주기 Fetch", String.valueOf(tp3 - tp2), methodName, methodLine);
			
			
			// 버전정보 조회 (Guide VOD, 카테고리 리스트)
			List<ComVersionVO> lVodInfo = new ArrayList<ComVersionVO>();
			ComVersionVO vodVersion = new ComVersionVO();
			
			nMainCnt = 0;
			lVodInfo = this.getVodVersion(paramVO);
			
			if(lVodInfo != null){
				nMainCnt = lVodInfo.size();
			}
			
			for(int i = 0; i < nMainCnt; i++){
				vodVersion = lVodInfo.get(i);
				
				if("N".equals(vodVersion.getVersionType())){			// NScreen : N(전체 버전)
					tempVO.setLstVer(vodVersion.getVersion());
					tempCacheVO.setLstVer("LST_VER=" + vodVersion.getVersion());
				}else if("G".equals(vodVersion.getVersionType())){
					tempVO.setGuideVer(vodVersion.getVersion());
					tempVO.setGuideCnt(String.valueOf(vodVersion.getDownloadCnt()));
				}else if("Z".equals(vodVersion.getVersionType())){
					tempVO.setSiVer(vodVersion.getVersion());
					tempCacheVO.setSiVer("SI_VER=" + vodVersion.getVersion());
				}else if("E".equals(vodVersion.getVersionType())){
					tempVO.setPsiVer(vodVersion.getVersion());
					tempCacheVO.setPsiVer("PSI_VER=" + vodVersion.getVersion());
				}
			}
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("Guide VOD, 카테고리 Version 조회", String.valueOf(tp2 - tp3), methodName, methodLine);
			

			
			// 서브 카테고리 Version 조회
			nMainCnt = 0;
			lVodInfo = this.getSubVersion(paramVO);			
			
			if(lVodInfo != null){
				nMainCnt = lVodInfo.size();
			}
			
			for(int i = 0; i < nMainCnt; i++){
				vodVersion = lVodInfo.get(i);
				
				if(i == 0){
					tempVO.setSubVer(vodVersion.getCatId() + ":" + vodVersion.getVersion());
					tempCacheVO.setSubVer("SUB_VER=" + vodVersion.getCatId() + ":" + vodVersion.getVersion());
				}else{
					tempVO.setSubVer(tempVO.getSubVer() + ImcsConstants.ARRSEP + vodVersion.getCatId() + ":" + vodVersion.getVersion());
					tempCacheVO.setSubVer(tempCacheVO.getSubVer() + ImcsConstants.ARRSEP + vodVersion.getCatId() + ":" + vodVersion.getVersion());
				}

				if(i == nMainCnt - 1) {
					resultListVO.setCatId(vodVersion.getCatId());			// 통합통계
					resultListVO.setCatName(vodVersion.getCatName());		// 통합통계
				}
			}
			tp3	= System.currentTimeMillis();
			imcsLog.timeLog("서브 카테고리 Version 조회", String.valueOf(tp3 - tp2), methodName, methodLine);
			
			
			if("N".equals(szVodUseYn)){
				tempVO.setGuideVer(tempVO.getGuideVer());
				tempVO.setGuideCnt("1");
			}else{
				tempVO.setGuideVer(tempVO.getGuideVer());
				tempVO.setGuideCnt(tempVO.getGuideCnt());
			}
			
			tempCacheVO.setGuideVer("GUIDE_VER=" + tempVO.getGuideVer());
			tempCacheVO.setGuideCnt("GUIDE_CNT=" + tempVO.getGuideCnt());
			
			
			// 컨텐츠 업데이트 주기 Fetch
			List<String> lConUpdPeriod = new ArrayList<String>();
			String szConPeriod = "";
			
			nMainCnt = 0;
			lConUpdPeriod = this.getConUpdPeriod(paramVO);
			
			if(lConUpdPeriod != null){
				nMainCnt = lConUpdPeriod.size();
			}
			
			for(int i = 0; i < nMainCnt; i++){
				szConPeriod = lConUpdPeriod.get(i);
				
				if(i == 0){
					tempVO.setSubUPeriode(szConPeriod);
					tempCacheVO.setSubUPeriode("SUB_U_PERIODE=" + szConPeriod);
				}else{
					tempVO.setSubUPeriode(tempVO.getSubUPeriode() + ImcsConstants.ARRSEP + szConPeriod);
					tempCacheVO.setSubUPeriode(tempCacheVO.getSubUPeriode() + ImcsConstants.ARRSEP + szConPeriod);
				}
			}
						
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("컨텐츠 업데이트 주기 Fetch", String.valueOf(tp2 - tp3), methodName, methodLine);

			
			
			
			// 가입자 연령정보 조회
			nMainCnt = 0;
			
			sql_id	= "lgvod992_007_20170817_001";
			szPrInfo = this.getPrInfo(paramVO, sql_id);
			szPrInfo = StringUtil.replaceNull(szPrInfo, "01");
			
			tempVO.setPrInfo(szPrInfo);
			tp3	= System.currentTimeMillis();
			imcsLog.timeLog("가입자 연령정보 조회", String.valueOf(tp3 - tp2), methodName, methodLine);
			
			
			// EPG 스케줄 업데이트 주기 조회
			List<String> lPSIUpdPeriod = new ArrayList<String>();
			String szPSIPeriod	= "";
						
			nMainCnt = 0;
			
			lPSIUpdPeriod = this.getPSIUpdPeriod(paramVO);
			
			if(lPSIUpdPeriod != null){
				nMainCnt = lPSIUpdPeriod.size();
			}
			
			for(int i = 0; i < nMainCnt; i++){
				szPSIPeriod = lPSIUpdPeriod.get(i);
				
				if(i == 0){
					tempVO.setPsiLstUPeriod(szPSIPeriod);
					tempCacheVO.setPsiLstUPeriod("PSI_LST_U_PERIODE=" + szPSIPeriod);
				}else{
					tempVO.setPsiLstUPeriod(tempVO.getPsiLstUPeriod() + ImcsConstants.ARRSEP + szPSIPeriod);
					tempCacheVO.setPsiLstUPeriod(tempCacheVO.getPsiLstUPeriod() + ImcsConstants.ARRSEP + szPSIPeriod);
				}
			}
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("EPG스케줄 업데이트 주기 Fetch", String.valueOf(tp2 - tp3), methodName, methodLine);

			resultVO.add(tempVO);
		    resultListVO.setList(resultVO);
		    
		    FileWriter fw	= null;
		    try {
		    	fw = new FileWriter(res);
		    	
				fw.write(tempCacheVO.toString());
				fw.flush();
				
				msg = "File [" + RESFILE + "] Fsize [" + res.length() + "] WRITE [" + res.length() + "] bytes Finished";
				imcsLog.serviceLog(msg, methodName, methodLine);				
				
			} catch (Exception e) {
				msg = "File [" + RESFILE + "] WRITE failed";
				imcsLog.serviceLog(msg, methodName, methodLine);
			}finally{
				if(fw != null) fw.close();
			}
		   
		} catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
			
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			queryUnLock(LOCKFILE, paramVO);
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID992) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	
	
	/**
	 * 파일 읽기 공통모듈
	 * @param fname
	 * @return
	 */
	private GetNSPeriodResponseVO getFileRead(String fname) {
		GetNSPeriodResponseVO resultVO = new GetNSPeriodResponseVO();
		FileReader fr		= null;
		BufferedReader br	= null;
		
		try {
			fr = new FileReader(fname);
			br = new BufferedReader(fr);
			
			String s = "";
			String temp = "";
			
			while((s = br.readLine())!=null){
				temp += s;
			}
			
			resultVO.setLstUPeriode(getData(temp, "LST_U_PERIODE="));
			resultVO.setGuideVer(getData(temp, "GUIDE_VER="));
			resultVO.setGuideCnt(getData(temp,"GUIDE_CNT="));
			resultVO.setSubUPeriode(getData(temp, "SUB_U_PERIODE="));
			resultVO.setSubVer(getData(temp, "SUB_VER="));
			resultVO.setLstVer(getData(temp, "LST_VER="));
			resultVO.setSiVer(getData(temp, "SI_VER="));
			resultVO.setPsiLstUPeriod(getData(temp, "PSI_LST_U_PERIODE="));
			resultVO.setPsiVer(getData(temp, "PSI_VER="));
		} catch (Exception e) {
		} finally {
			if(br != null) try {br.close();} catch (IOException e) {}
			if(fr != null) try {fr.close();} catch (IOException e) {}
			
		}
		return resultVO;					
	}



	/**
	 * 가입자 해피콜완료 여부 조회
	 * @param  GetNSPeriodRequestVO
	 */
    public String getVodUseYn(GetNSPeriodRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId =  "lgvod992_001_20171214_001";
		String szVodUseYn	= "";
		
		List<String> list   = new ArrayList<String>();
		
		try {
			
			try{
				list = getNSPeriodDao.getVodUseYn(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null ||list.isEmpty()){
				szVodUseYn	= "N";			
			} else {
				szVodUseYn	= StringUtil.replaceNull(list.get(0), "N");
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return szVodUseYn;
    }
    
    
    
    
    
    /**
     * 가입자 연령정보 조회
     * @param GetNSPeriodRequestVO paramVO
     * @param String sqlId	
     * @throws Exception
     */
    public String getPrInfo(GetNSPeriodRequestVO paramVO, String sqlId) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    	
		List<String> list   = new ArrayList<String>();
		String szPrInfo	= "";
		
		try {
			try{
				list = getNSPeriodDao.getPrInfo(paramVO);
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null ||list.isEmpty()){
				szPrInfo = "01";
				//imcsLog.failLog(ImcsConstants.API_PRO_ID992, "", null, "svr_ip:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			} else {
				szPrInfo = StringUtil.replaceNull(list.get(0), "01");
			}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID992, sqlId, null, "rating_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return szPrInfo;
    }
    
    
    
    
    /** 카테고리 업데이트 주기 조회
     * @param	GetNSPeriodRequestVO
     * @return	String
     */
    public List<String> getCateUpdPeriode(GetNSPeriodRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName	= oStackTrace.getMethodName();
    	
    	String sqlId =  "lgvod992_003_20171214_001";
		
		int querySize = 0;
		
		List<String> list   = new ArrayList<String>();
		
		String szMsg = "";
		
		try {
			
			try{
				list = getNSPeriodDao.getCateUpdPeriod();
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null ||list.isEmpty()){
				querySize = 0;
				
				szMsg = "svc["+ String.format("%-20s", ImcsConstants.API_PRO_ID992) +"] SQLID[" + sqlId + "] sts[" + querySize + "] msg["+ 
						String.format("%-19s", "periode_info:" + ImcsConstants.RCV_MSG3 +"]");
				IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			} else {
				querySize = list.size();
			}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID992, sqlId, null, "periode_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
    	return list;
    }
    
    
    
    /**
     *	버전정보 조회 (Guide VOD, 카테고리 리스트)
     *	@param 	GetNSPeriodRequestVO
     *	@result	List<GetNSPeriodVodVerVO>
     */
    public List<ComVersionVO> getVodVersion(GetNSPeriodRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "lgvod992_004_20171214_001";
		
		List<ComVersionVO> list   = null;
		
		try {
			try{
				list = getNSPeriodDao.getVodVersion();
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null || list.isEmpty()){
				//imcsLog.failLog(ImcsConstants.API_PRO_ID992, sqlId, null, "ver_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);	
			}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID992, sqlId, null, "ver_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
    	return list;
    }
    
    
    /** 
     * 	서브 카테고리 Version 조회
     * 	@param	GetNSPeriodRequestVO
     * 	@result	List<GetNSPeriodVodVerVO>
     */
    public List<ComVersionVO> getSubVersion(GetNSPeriodRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId =  "lgvod992_005_20171214_001";
		
		List<ComVersionVO> list   = null;
		
		try {
			
			try{
				list = getNSPeriodDao.getSubVersion();
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null || list.isEmpty()){
//				imcsLog.failLog(ImcsConstants.API_PRO_ID992, sqlId, null, "ver_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID992, sqlId, null, "ver_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			throw new ImcsException();
		}
		
    	return list;
    }
    
    
    
    /**
     * 컨텐츠 업데이트 주기 조회
     * @param	GetNSPeriodRequestVO
     * @result	List<GetNSPeriodUpdPerVO>
     */
    public List<String> getConUpdPeriod(GetNSPeriodRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId =  "lgvod992_006_20171214_001";
		
		List<String> list   = new ArrayList<String>();
		
		try {
			try{
				list = getNSPeriodDao.getConUpdPeriod();
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if( list == null ||list.isEmpty()){
//				imcsLog.failLog(ImcsConstants.API_PRO_ID992, sqlId, null, "periode_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID992, sqlId, null, "periode_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			throw new ImcsException();
		}
		
    	return list;
    }
    
    
    
    
    /**
     * 	EPG 스케줄 업데이트 주기 조회
     *	@param	GetNSPeriodRequestVO
     * 	@result	List<GetNSPeriodUpdPerVO>
     */
    public List<String> getPSIUpdPeriod(GetNSPeriodRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId =  "lgvod992_008_20171214_001";

		
		List<String> list   = new ArrayList<String>();
		
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDD");
		String stamp_today = sdf.format(today);
		
		try {

			try{
				list = getNSPeriodDao.getPSIUpdPeriod();
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if( list == null ||list.isEmpty()){
//				imcsLog.failLog(ImcsConstants.API_PRO_ID992, sqlId, null, "periode_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID992, sqlId, null, "periode_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			throw new ImcsException();
		}
		
    	return list;
    }
    
    /**
     *  getNSPeriod 파일 파싱
     *  szOriStr	대상 문자열
     *  szSearchStr	검색하고자 하는 문자열
     */
	public String getData(String szOriStr, String szSearchStr){
		String szRtnVal = "";
		
		try {
			szOriStr	= szOriStr.substring(szOriStr.indexOf(szSearchStr) + szSearchStr.length(), szOriStr.length());
			szRtnVal = szOriStr.substring(0, szOriStr.indexOf("|"));
		} catch(Exception e) {}
		
		System.out.println(szSearchStr + "/" + szRtnVal);
		
		return szRtnVal;
	}
	
	
	
	public boolean queryLock(String LOCKFILE, GetNSPeriodRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String msg;
		File lock = new File(LOCKFILE);
		
		if(lock.exists()){
			return true;
		}else{
			try {
				lock.createNewFile();
				
				msg = "queryLock Success, query execution itself ";
				imcsLog.serviceLog(msg, methodName, methodLine);
			} catch (Exception e) {
			}
			
			return false;
		}
		
	}
	
	public void queryUnLock(String LOCKFILE, GetNSPeriodRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String msg = "";
		File lock = new File(LOCKFILE);
		
		if(lock.exists()){
			if(lock.delete()){
				msg = "queryUnLock Success, unlink ["+LOCKFILE+"] ";
				imcsLog.serviceLog(msg, methodName, methodLine);
				
			}else{
				msg = "lockfile ["+LOCKFILE+"] unlink Failed ";
				imcsLog.serviceLog(msg, methodName, methodLine);
			}
		}
		
	}

}
