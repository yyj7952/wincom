package kr.co.wincom.imcs.api.setNSPassedTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.api.getNSWatchList.GetNSWatchListResponseVO;
import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComDupCHk;
import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.SvodPkgVO;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom2.imcs.api.setNSPassedTime.SetNSPassedTimeDao2;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class SetNSPassedTimeServiceImpl implements SetNSPassedTimeService {
	private Log imcsLogger = LogFactory.getLog("API_setNSPassedTime");
	
	@Autowired
	private SetNSPassedTimeDao setNSPassedTimeDao;
	
	@Autowired
	private SetNSPassedTimeDao2 setNSPassedTimeDao2;
	
	@Autowired
	private CommonService commonService;
	
//	public void setNSPassedTime(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public SetNSPassedTimeResultVO setNSPassedTime(SetNSPassedTimeRequestVO paramVO){
//		this.setNSPassedTime(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		SetNSPassedTimeResultVO resultVO = new SetNSPassedTimeResultVO();
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		String flag		= "";
		String errMsg	= "";
		String msg		= "";
		
	    String szProductType = "";
	    
	    int nMainCnt	= 0;
	    int nDataChk	= 0;
	    int resultSet	= 0;
	    int result		= 0;
	    int i_sub_chk   = 0;
	    
	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
		long tp2 = 0;
		long tp3 = 0;
	    
		try{
			
			if(paramVO.getAlbumId().equals("CH")) {
				switch (paramVO.getSvcType()) {
				case "K":
				case "B":
				case "G":
				case "P":
					if(this.insertSetWatchTime(paramVO) == 0) resultSet = -1;
					break;

				default:
					resultSet = -1;
					break;
				}
				
				
				if(resultSet == 0){
					resultVO.setFlag("0");
					resultVO.setErrMsg("이어보기 시간이 설정되었습니다.");
					return resultVO;
				}else{
					resultVO.setFlag("1");
					resultVO.setErrMsg("이어보기 시간설정이 실패하였습니다.");
					
					paramVO.setResultCode("20000001");		
					return resultVO;
				}
			}
			
			tp1	= System.currentTimeMillis();
			
			imcsLog.timeLog("KEY_LOG Length Check", String.valueOf((tp1 - tp_start)), methodName, methodLine);
			ContTypeVO	contTypeVO	= null;
			contTypeVO	= this.getContsType(paramVO);
			
			if(contTypeVO != null) {
				szProductType = contTypeVO.getContsType();
				resultVO.setContentsName(contTypeVO.getContsName());
				
				//2020.03.18 아이들나라 3.0
				paramVO.setMetaRunTime(contTypeVO.getMetaRunTime());
				paramVO.setAlbumName(contTypeVO.getContsName());
			}
			else
				resultSet = -1;
			
			/*if(szProductType.equals("0") && paramVO.getnWatchYn().equals("Y"))
			{
				String szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] msg[FVOD - 엔스크린 시청 불가능(fail)]";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
				resultSet = -1;
			}*/
			
			if(paramVO.getnWatchYn().equals("Y") && resultSet == 0) {
				String [] nscArray = contTypeVO.getNscreenYn().split(";");
				String nscYn = nscArray[0];
				
				if (nscYn.equals("N")) {
					if(szProductType.equals("0")){
						String szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] msg[FVOD - 시청 불가능(fail)]";
						imcsLog.serviceLog(szMsg, methodName, methodLine);
						resultSet = -1;
					}
				}
				//단방향이 M일경우 시청불가
				if (nscArray[2].equals("M")) {
					String szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] msg[Seamless 단방향 M - 시청 불가능(fail)]";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					resultSet = -1;
				}
			}
			
			
				
			// INSERT시 WATCH_DATE의 초 이하 단위가 틀려지는 현상으로 인해 미리 구한다
			tp1 = System.currentTimeMillis();
			String szSysDate	= commonService.getSysdate();
			paramVO.setSysdate(szSysDate);
			tp2 = System.currentTimeMillis();
			imcsLog.timeLog("SELETE sysdate", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			Integer nLinkChk = 0;
		    
			// 20200316 - 모바일 아이들나라 집계 로직 추가
			try {
				
				if (paramVO.getnWatchYn().equals("N") && !paramVO.getSvcType().equals("N") && resultSet == 0) {
					tp1 = System.currentTimeMillis();
					this.setkidsWatch(paramVO);
					tp2 = System.currentTimeMillis();
					imcsLog.timeLog("아이들 나라 setkidsWatch()", String.valueOf(tp2 - tp1), methodName, methodLine);
				} 
				
			} catch(Exception e) {
				String szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] msg[setkidsWatch-(fail)]";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}
			
			// 링크 유무 조회
			try {

				if (!paramVO.getnWatchYn().equals("N")) {
					nLinkChk = setNSPassedTimeDao.getLinkChk(paramVO);
				} else {
					if (paramVO.getNscnCustNo().equals("V")) {
						nLinkChk = setNSPassedTimeDao.getLinkChkNsc(paramVO);
					} else { // 2019.10.08 골프앱 고객체험단 변경 사항 - 골프앱에서 시청 혹은 비디오포탈에서 골프앱을 시청한 경우
						nLinkChk = setNSPassedTimeDao.getLinkChkNsc2(paramVO);
					}
				}

				if (nLinkChk == null)
					nLinkChk = 0;
			} catch (Exception e) {
				resultSet = -1;
				nLinkChk = 0;
			}

			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("SELETE PT_VO_SET_TIME_PTT_NSC", String.valueOf(tp1 - tp2), methodName, methodLine);
			
			// SET_TIME 테이블 이어보기 시간 설정		
			if(resultSet == 0 && !paramVO.getnWatchYn().equals("Y"))
			{
				if(nLinkChk > 0)
				{
					if(nLinkChk == 1 && "F".equals(paramVO.getNscnCustNo())) 
						paramVO.setiSubChk(1);//i_sub_chk = 1;
					
		            //update_PT_VO_SET_TIME_PTT() 함수에서 i_sub_cnk값으로 nscn_cust_no 값을 결정함(i_sub_chk=0->rd1.c_nscn_cust_no, i_sub_chk=1->rd1.c_nscn_cust_no_sub)
		            //최초 골프앱으로 시청한 경우(app_type=F@@@), authorzieNView, authorizeNSView에서 nscn_cust_no 값을 M으로 insert 하기 때문에,
		            //비디오 포탈과 이어보기 시간을 공유하기 위하여 NSCN_CUST_NO=M update					
					result = this.updateSetTime(paramVO);
					
					paramVO.setiSubChk(1);//i_sub_chk = 1;
					
					if( result > 0 )	resultSet = 0;
			    	else	    		resultSet = -1;
					
					msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] update [PT_VO_SET_TIME_PTT_NSC] table [" + result + "] records Success at";
		    		imcsLog.serviceLog(msg, methodName, methodLine);
		    		
		    		//i_link_chk=2 인 경우, 비디오포탈과 골프앱에 이어보기 시간이 존재 하기 때문에 위 UPDATE 구문에서 nscn_cust_no값을, 아래에서 nscn_cust_no_sub값을 update한다.
		    		if(nLinkChk == 2) {
						result = this.updateSetTime(paramVO);
						
						if( result > 0 )	resultSet = 0;
				    	else	    		resultSet = -1;
						
						msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] update [PT_VO_SET_TIME_PTT_NSC] table [" + result + "] records Success at";
			    		imcsLog.serviceLog(msg, methodName, methodLine);
		    			
		    		} //최초 골프앱으로 시청하는 경우, nscn_cust_no값이 M만 존재하기 때문에, F값을 insert 한다. 
		    		else if (nLinkChk == 1 && "F".equals(paramVO.getNscnCustNo())) {
		    			
						result = this.insertSetTime(paramVO);
						
						if( result > 0 )	resultSet = 0;
				    	else				resultSet = -1;
						
						msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] insert [PT_VO_SET_TIME_PTT_NSC] table [" + result + "] records Success at";
			    		imcsLog.serviceLog(msg, methodName, methodLine);		    			
		    		}
				}
				else
				{
					result = this.insertSetTime(paramVO);
					
					if( result > 0 )	resultSet = 0;
			    	else				resultSet = -1;
					
					msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] insert [PT_VO_SET_TIME_PTT_NSC] table [" + result + "] records Success at";
		    		imcsLog.serviceLog(msg, methodName, methodLine);
				}
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("이어보기 시간설정", String.valueOf((tp2 - tp1)), methodName, methodLine);
			}
			
			if(paramVO.getnWatchYn().equals("N") && resultSet == 0)
			{
				// SVOD가 아니면
				if( ( "0".equals(szProductType) || "1".equals(szProductType) || "2".equals(szProductType) ) 
						&& !"N".equals(paramVO.getBuyingDate()) )
				{
					result = this.updateWatchHis1(paramVO);
					
					if( result > 0 )  	resultSet = 0;
			    	else	    		resultSet = -1;
				}
				//SVOD가 맞으면
				else if( "3".equals(szProductType) || "N".equals(paramVO.getBuyingDate()) )
				{
					result = this.updateWatchHis2(paramVO);
					
					if( result > 0 )	resultSet = 0;
		    		else	    		resultSet = -1;
				}
			}
			else if(paramVO.getnWatchYn().equals("Y") && resultSet == 0)
			{
				if("N".equals(paramVO.getBuyingDate()) == false)
				{					
					//2019.03.08 권형도 수정 (tpacall setNSPassedTime 로직)
					if(nLinkChk == 1) {
						paramVO.setDmlGb("YU");
					} else {
						paramVO.setDmlGb("YI");						
					}
				
					try {
						int ret = this.setNSPassedTime2(paramVO);
						
						if (ret != 0) {
							msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] setNSPassedTimeDao2 [PT_VO_WATCH_HISTORY] table [" + ret + "] records Success at ";
						} else {
							msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] setNSPassedTimeDao2 [PT_VO_WATCH_HISTORY] table Failed at";
							resultSet	= -1;
						}
						
						imcsLog.serviceLog(msg, methodName, methodLine);
					} catch(Exception e) {
						msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] setNSPassedTimeDao2 [PT_VO_WATCH_HISTORY] table Failed at";
						imcsLog.serviceLog(msg, methodName, methodLine);

						resultSet	= -1;
					}
					
				}
				else if("N".equals(paramVO.getBuyingDate()))
				{					
					//2019.03.08 권형도 수정 (tpacall setNSPassedTime 로직)
					if(nLinkChk == 1) {
						paramVO.setDmlGb("NU");
					} else {
						paramVO.setDmlGb("NI");						
					}
				
					try {
						int ret = this.setNSPassedTime2(paramVO);
						
						if (ret != 0) {
							msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] setNSPassedTimeDao2 [PT_VO_WATCH_HISTORY] table [" + ret + "] records Success at ";
						} else {
							msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] setNSPassedTimeDao2 [PT_VO_WATCH_HISTORY] table Failed at";
							resultSet	= -1;
						}
						
						imcsLog.serviceLog(msg, methodName, methodLine);
					} catch(Exception e) {
						msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] setNSPassedTimeDao2 [PT_VO_WATCH_HISTORY] table Failed at";
						imcsLog.serviceLog(msg, methodName, methodLine);

						resultSet	= -1;
					}
				}
			}
			
			tp3	= System.currentTimeMillis();
	    	
			imcsLog.timeLog("시청시간, 키로그 설정", String.valueOf((tp3 - tp2)), methodName, methodLine);
			
			if(resultSet == 0){
				resultVO.setFlag("0");
				resultVO.setErrMsg("이어보기 시간이 설정되었습니다.");
			}else{
				resultVO.setFlag("1");
				resultVO.setErrMsg("이어보기 시간설정이 실패하였습니다.");
				
				paramVO.setResultCode("20000001");				
			}
			
			long tp4	= System.currentTimeMillis();
	    	
			if(resultSet == 0){
				imcsLog.timeLog("EXEC SQL COMMIT", String.valueOf((tp4 - tp3)), methodName, methodLine);
			}else{
				imcsLog.timeLog("EXEC SQL ROLLBACK", String.valueOf((tp4 - tp3)), methodName, methodLine);
				
				throw new ImcsException(resultVO.getFlag(), resultVO.getErrMsg(), "", resultVO);
			}
		}catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw ie;
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;

			resultVO.setFlag("1");
			resultVO.setErrMsg("이어보기 시간설정이 실패하였습니다.");
			
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException(flag, errMsg, "", resultVO);
		} finally{
			msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "]" + String.format("%-5s",  " sts[" + ImcsConstants.LOG_MSG3 + "]") 
					+ " snd[FLAG=" + resultVO.getFlag() + "|MESSAGE=" + resultVO.getErrMsg() + "|]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			resultVO.setResultCode(paramVO.getResultCode());
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultVO;
	}
	
	
	/**
	 * 컨텐츠 타입정보 조회
	 * @param	SetNSPassedTimeRequestVO
	 * @result	ContTypeVO
	 */
	public ContTypeVO getContsType(SetNSPassedTimeRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		
    	String sqlId = "lgvod189_001_20171214_001";
    	
		List<ContTypeVO> list = new ArrayList<ContTypeVO>();
		ContTypeVO conTypeVO = new ContTypeVO();
		
		
		try {
			try{
				list = setNSPassedTimeDao.getContsType(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list == null || list.isEmpty()){
				conTypeVO	= null;			
				
				//imcsLog.failLog(ImcsConstants.API_PRO_ID189, "", cache, "conts_type:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			} else {
				conTypeVO	= (ContTypeVO)list.get(0);
			}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null)	paramVO.setResultCode("4000" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("40000000");
//			imcsLog.failLog(ImcsConstants.API_PRO_ID189, sqlId, cache, "conts_type:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
    	return conTypeVO;
    }
	
	
	/**
	 *  SVOD, PKG 여부 조회
	 *  @param	SetNSPassedTimeRequestVO
	 *  @result	List<SvodPkgVO>
	 *  
	 */
	public List<SvodPkgVO> getSvodPkg(SetNSPassedTimeRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    	
		/*//UFLIX 미적용 이슈로 오라클로 처리하기로 함
		 * String sqlId = "lgvod189_002_20140610_003";

		NosqlResultCache cache = new NosqlResultCache();
		List<Object> binds = new ArrayList<Object>();
		RowKeyList rowKeys = new RowKeyList();
		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
		
		List<SvodPkgVO> list = new ArrayList<SvodPkgVO>();

		try {
			rowKeys.setSaId(paramVO.getSaId());
			rowKeys.setStbMac(paramVO.getStbMac());
			rowKeys.setSqlId(sqlId);
			rowKeys.addRowKeys(paramVO.getAlbumId());
			
			checkKey.addVersionTuple("PT_VO_CATEGORY");
			checkKey.addVersionTuple("PT_VO_CATEGORY_MAP", paramVO.getAlbumId());
			checkKey.addVersionTuple("PT_VO_WATCH_HISTORY", paramVO.getSaId());
			
			binds.add(paramVO);
						
			list = cache.getCachedResult(new CacheableExecutor<SvodPkgVO>() {
				@Override
				public List<SvodPkgVO> execute(List<Object> param) throws SQLException {
					try{
						SetNSPassedTimeRequestVO requestVO = (SetNSPassedTimeRequestVO)param.get(0);
						List<SvodPkgVO> rtnList = setNSPassedTimeDao.getSvodPkg(requestVO);
						
						return rtnList;
					}catch(DataAccessException e){
						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
					}
				}
				
				@Override
				public Class<SvodPkgVO> getReturnType() {
					return SvodPkgVO.class;
				}
			}, binds, rowKeys, checkKey);
			
			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
			if(cache.getLastException() != null)	paramVO.setResultCode("4000" + cache.getLastException().getErrorCode());
			else									paramVO.setResultCode("40000000");
			imcsLog.failLog(ImcsConstants.API_PRO_ID189, sqlId, cache, "cont_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}*/
    	
    	List<SvodPkgVO> list = new ArrayList<SvodPkgVO>();
    	
    	try {
    		list = setNSPassedTimeDao.getSvodPkg(paramVO);
    		paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
		} catch (Exception e) {
			String szMsg	 = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] sts[    0] msg[" + String.format("%-21s", "cont_info:" + ImcsConstants.RCV_MSG2) + "]";
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
    	
    	
		
    	return list;
    }
	
	
	/**
	 *  이어보기 정보 조회1
	 *  @param	SetNSPassedTimeRequestVO
	 *  @result	CommonDupCHk
	 */
	public ComDupCHk dataChk1(SetNSPassedTimeRequestVO paramVO) throws Exception{
	   	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
    	String sqlId =  "lgvod189_003_20171214_001";
		
		List<ComDupCHk> list   = new ArrayList<ComDupCHk>();
		ComDupCHk dupChk	= null;
		
		try {
			try{
				list = setNSPassedTimeDao.dataChk1(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				dupChk	= list.get(list.size() - 1);
			}else{
				dupChk = new ComDupCHk();
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
    	return dupChk;
    }
	
	
	/**
	 *  이어보기 정보 조회2
	 *  @param	SetNSPassedTimeRequestVO
	 *  @result	CommonDupCHk
	 */
	public ComDupCHk dataChk2(SetNSPassedTimeRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId =  "lgvod189_004_20171214_001";
		
		List<ComDupCHk> list   = new ArrayList<ComDupCHk>();
		ComDupCHk dupChk	= null;
		
		try {
			try{
				list = setNSPassedTimeDao.dataChk2(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				dupChk	= list.get(list.size() - 1);
			}else{
				dupChk = new ComDupCHk();
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
    	return dupChk;
    }
	
	
	/**
	 *  이어보기 정보 조회3
	 *  @param	SetNSPassedTimeRequestVO
	 *  @result	CommonDupCHk
	 */
	public ComDupCHk dataChk3(SetNSPassedTimeRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();

		String sqlId =  "lgvod189_005_20171214_001";
		
		List<ComDupCHk> list   = new ArrayList<ComDupCHk>();
		ComDupCHk dupChk	= null;
		
		try {
			try{
				list = setNSPassedTimeDao.dataChk3(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				dupChk	= list.get(list.size() - 1);
			}else{
				dupChk = new ComDupCHk();
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
    	return dupChk;
    }
	
	
	/**
	 *  이어보기 정보 조회4
	 *  @param	SetNSPassedTimeRequestVO
	 *  @result	CommonDupCHk
	 */
	public ComDupCHk dataChk4(SetNSPassedTimeRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();

		String sqlId =  "lgvod189_006_20171214_001";
		
		List<ComDupCHk> list   = new ArrayList<ComDupCHk>();
		ComDupCHk dupChk	= null;
		
		try {
			try{
				list = setNSPassedTimeDao.dataChk4(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if( list != null && !list.isEmpty()){
				dupChk	= list.get(list.size() - 1);
			}else{
				dupChk = new ComDupCHk();
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException(ImcsConstants.FAIL_CODE, e);
		}
		
    	return dupChk;
    }
	
	
	
	/**
	 *  이어보기 정보 조회5
	 *  @param	SetNSPassedTimeRequestVO
	 *  @result	CommonDupCHk
	 */
	public ComDupCHk dataChk5(SetNSPassedTimeRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId =  "lgvod189_007_20171214_001";
		
		List<ComDupCHk> list   = new ArrayList<ComDupCHk>();
		ComDupCHk dupChk	= null;
		
		try {
			try{
				list = setNSPassedTimeDao.dataChk5(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				dupChk	= list.get(list.size() - 1);
			}else{
				dupChk = new ComDupCHk();
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		}
		
    	return dupChk;
    }
		
	/**
	 *  이어보기 시간 갱신
	 *  @param	SetNSPassedTimeRequestVO
	 *  @result	int
	 */
	public int updateSetTime(SetNSPassedTimeRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId =  "lgvod189_uu2_20171214_001";
		
		int querySize = 0;
		
		String szMsg = "";
		    	
    	try {

    		try{
    			querySize =setNSPassedTimeDao.updateSetTime(paramVO);
    			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try{
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID189, sqlId, null, querySize, methodName, methodLine);

				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] update [PT_VO_SET_TIME_PTT_NSC] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
    	
		return querySize;
	}
	
	
	
	/** 
	 *  이어보기 시간 등록
	 *  @param	SetNSPassedTimeRequestVO
	 *  @result	int
	 */
	public int insertSetTime(SetNSPassedTimeRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId =  "lgvod189_ii2_20171214_001";
		
		int querySize = 0;
		
		
		String szMsg = "";
		    	
    	try {
			try{
				querySize = setNSPassedTimeDao.insertSetTime(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try{
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID189, sqlId, null, querySize, methodName, methodLine);

				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] insert [PT_VO_SET_TIME_PTT_NSC] table[" + querySize + "] records Success at";
				IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null)	paramVO.setResultCode("4000" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("40000000");
			//imcsLog.failLog(ImcsConstants.API_PRO_ID189, sqlId, null, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return querySize;
	}
	
	
	
	
	public int updateWatchHis1(SetNSPassedTimeRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId =  "lgvod189_u02_20171214_001";
		
		int querySize = 0;
		
		String szMsg = "";
		    	
    	try {
			
			try{
				querySize = setNSPassedTimeDao.updateWatchHis1(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
				if(querySize != 0){
					//imcsLog.dbLog(ImcsConstants.API_PRO_ID189, sqlId, null, querySize, methodName, methodLine);
					
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] update [PT_VO_WATCH_HISTORY_NSC] table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				}else{
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] update [PT_VO_WATCH_HISTORY_NSC] table Failed at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					
					//imcsLog.failLog(ImcsConstants.API_PRO_ID189, sqlId, null, "fail info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				}
			} catch (Exception e) {}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] update [PT_VO_WATCH_HISTORY_NSC] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//imcsLog.failLog(ImcsConstants.API_PRO_ID189, sqlId, null, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return querySize;
	}
	
	
	
	public int updateWatchHis2(SetNSPassedTimeRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId =  "lgvod189_u03_20171214_001";
		
		int querySize = 0;
		
		String szMsg = "";
		    	
    	try {

			try{
				querySize = setNSPassedTimeDao.updateWatchHis2(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
    		
			if(querySize != 0){
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID189, sqlId, null, querySize, methodName, methodLine);

				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] update [PT_VO_WATCH_HISTORY_NSC] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}else{
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] update [PT_VO_WATCH_HISTORY_NSC] table Failed at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
				//imcsLog.failLog(ImcsConstants.API_PRO_ID189, sqlId, null, "fail info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] update [PT_VO_WATCH_HISTORY_NSC] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//imcsLog.failLog(ImcsConstants.API_PRO_ID189, sqlId, null, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return querySize;
	}
			
	public int setNSPassedTime2(SetNSPassedTimeRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId =  "lgvod189_u02_20171214_001";
		
		int querySize = 0;
		String szMsg = "";
		String tablename = "";
		
    	try {
			
			try{
				
				if (paramVO.getDmlGb().substring(0, 1).equals("Y")) {
					querySize = setNSPassedTimeDao2.updateWatchHis3(paramVO);
					tablename = "update PT_VO_WATCH_HISTORY";
				} else if (paramVO.getDmlGb().substring(0, 1).equals("N")) {
					querySize = setNSPassedTimeDao2.updateWatchHis4(paramVO);
					tablename = "update PT_VO_WATCH_HISTORY";
				}
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				try {
					if(querySize != 0){
						//imcsLog.dbLog(ImcsConstants.API_PRO_ID189, sqlId, null, querySize, methodName, methodLine);
						
						szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] "+ tablename + " table[" + querySize + "] records Success at";
						imcsLog.serviceLog(szMsg, methodName, methodLine);
					}else{
						szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] "+ tablename + " table Failed at";
						imcsLog.serviceLog(szMsg, methodName, methodLine);
						
						return querySize;
						//imcsLog.failLog(ImcsConstants.API_PRO_ID189, sqlId, null, "fail info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
					}
				} catch (Exception e) {}
				
				if (paramVO.getDmlGb().substring(1, 2).equals("U")) {
					querySize = setNSPassedTimeDao2.updateSetTime(paramVO);
					tablename = "update PT_VO_SET_TIME_PTT";
				} else if (paramVO.getDmlGb().substring(1, 2).equals("I")) {
					querySize = setNSPassedTimeDao2.insertSetTime(paramVO);
					tablename = "insert PT_VO_SET_TIME_PTT";
				}
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
				if(querySize != 0){
					//imcsLog.dbLog(ImcsConstants.API_PRO_ID189, sqlId, null, querySize, methodName, methodLine);
					
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] "+ tablename + " table[" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				}else{
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] "+ tablename + " table Failed at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					
					//imcsLog.failLog(ImcsConstants.API_PRO_ID189, sqlId, null, "fail info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				}
			} catch (Exception e) {}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] "+ tablename + "  table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//imcsLog.failLog(ImcsConstants.API_PRO_ID189, sqlId, null, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return querySize;
	}
	
	// 2020.03.13 - 모바일 아이들나라 최근 시청 정보 저장
	public void setkidsWatch(SetNSPassedTimeRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		int querySize = 0;
		String szMsg = "";
		String function = "";
		int iCharCnt = 0;
		int iBookCnt = 0;
		String Char_Cat_Id = "";
		int iParentCnt = 0;
		Integer iLinkChkC = 0;
		Integer iLinkChkB = 0;
		Integer iLinkChkP = 0;
		
    	try {
			
    		if(paramVO.getSvcType().equals("K") || paramVO.getSvcType().equals("B")){
				try{
					
					HashMap<String, String> chkCharBook = setNSPassedTimeDao.chkCharBook(paramVO);
					function = "chkCharBook()";
					
					if(chkCharBook != null)
					{
						iCharCnt = Integer.parseInt(chkCharBook.get("char_cnt").toString());
						iBookCnt = Integer.parseInt(chkCharBook.get("book_cnt").toString());
						Char_Cat_Id = chkCharBook.get("char_cat_id");
						
						szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] "+ function + " function Success at";
						imcsLog.serviceLog(szMsg, methodName, methodLine);
					} else {
						szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] "+ function + " function Failed at";
						imcsLog.serviceLog(szMsg, methodName, methodLine);
					}
					
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
		
				}catch(DataAccessException e){
					//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
					throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
				}
    		} else if (paramVO.getSvcType().equals("P")) {
    			
				try{
					iParentCnt = setNSPassedTimeDao.chkParentCnt(paramVO);
					function = "chkParentCnt()";
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					
					if (iParentCnt > 0){
						
						
						szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] "+ function + " function Success at";
						imcsLog.serviceLog(szMsg, methodName, methodLine);
					} else {
						szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] "+ function + " function Failed at";
						imcsLog.serviceLog(szMsg, methodName, methodLine);
					}
					
				} catch (Exception e) {
					//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
					throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
				}
    		}
    		
    		
			if (iCharCnt > 0) {
				try {
					paramVO.setCatId(Char_Cat_Id);
					paramVO.setNscnCustNoKids("C");
					iLinkChkC = setNSPassedTimeDao.getLinkChkNscKids(paramVO);
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					
					if (iLinkChkC == null)
						iLinkChkC = 0;
				} catch (Exception e) {
					// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
					throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
				}
				
				if (iLinkChkC == 1) {
					this.updateSetTimeKids(paramVO);
				} else {
					this.insertSetTimeKids(paramVO);
				}
			}

			if (iBookCnt > 0) {
				try {
					paramVO.setNscnCustNoKids("B");
					iLinkChkB = setNSPassedTimeDao.getLinkChkNscKids(paramVO);
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					
					if (iLinkChkB == null)
						iLinkChkB = 0;
				} catch (Exception e) {
					// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
					throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
				}
				
				if (iLinkChkB == 1) {
					this.updateSetTimeKids(paramVO);
				} else {
					this.insertSetTimeKids(paramVO);
				}
			}

			if (iParentCnt > 0) {
				try {
					paramVO.setNscnCustNoKids("P");
					iLinkChkP = setNSPassedTimeDao.getLinkChkNscKids(paramVO);
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					
					if (iLinkChkP == null)
						iLinkChkP = 0;
				} catch (Exception e) {
					// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
					throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
				}
				
				if (iLinkChkP == 1) {
					this.updateSetTimeKids(paramVO);
				} else {
					this.insertSetTimeKids(paramVO);
				}
			}
			
			// 2020.03.20 아이들나라3.0 통계 테이블 INSERT
			if(!paramVO.getRunTime().equals("")) {
				this.insertSetWatchTime(paramVO);
			}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] "+ function + "  table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
		}
		
	}
	
	
	/** 
	 *  이어보기 시간 등록
	 *  @param	SetNSPassedTimeRequestVO
	 *  @result	int
	 */
	public int insertSetTimeKids(SetNSPassedTimeRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		
		int querySize = 0;
		
		
		String szMsg = "";
		    	
    	try {
			try{
				querySize = setNSPassedTimeDao.insertSetTimeKids(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try{
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID189, sqlId, null, querySize, methodName, methodLine);

				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] insert [PT_VO_SET_TIME_PTT_NSC] table[" + querySize + "] records Success at";
				IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null)	paramVO.setResultCode("4000" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("40000000");
			//imcsLog.failLog(ImcsConstants.API_PRO_ID189, sqlId, null, "fail info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return querySize;
	}
	
	/**
	 *  이어보기 시간 갱신
	 *  @param	SetNSPassedTimeRequestVO
	 *  @result	int
	 */
	public int updateSetTimeKids(SetNSPassedTimeRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
		
		int querySize = 0;
		
		String szMsg = "";
		    	
    	try {

    		try{
    			querySize =setNSPassedTimeDao.updateSetTimeKids(paramVO);
    			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try{
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID189, sqlId, null, querySize, methodName, methodLine);

				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] update [PT_VO_SET_TIME_PTT_NSC] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
    	
		return querySize;
	}
	
	/**
	 *  이어보기 시간 갱신
	 *  @param	SetNSPassedTimeRequestVO
	 *  @result	int
	 */ 
	public int insertSetWatchTime(SetNSPassedTimeRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
		
		int querySize = 0;
		
		String szMsg = "";
		    	
    	try {
    		
    		if(paramVO.getAlbumId().equals("CH")) {
    			paramVO.setChkRunTime("N");
    		} else {
    			checkRuntime(paramVO);
    		}

    		try{
    			querySize =setNSPassedTimeDao.insertSetWatchTime(paramVO);
    			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try{
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID189, sqlId, null, querySize, methodName, methodLine);

				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID189) + "] update [NPT_VO_WATCH_TIME] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
    	
		return querySize;
	}
	
	public void checkRuntime(SetNSPassedTimeRequestVO paramVO)
	{
	    String runtime = "";
	    String hh = "";
	    String mm = "";
	    String ss = "";
	
	    int iLinkTime = 0;
	    double iContTime;
	    double icheck;
	
	    hh = paramVO.getMetaRunTime().substring(0, 2);
	    mm = paramVO.getMetaRunTime().substring(2, 4);
	    ss = paramVO.getMetaRunTime().substring(4, 6);
	    
	    iLinkTime = Integer.parseInt(paramVO.getSecond());
	    paramVO.setChkRunTime("N");
	    
	    iContTime = Integer.parseInt(hh)*60*60 + Integer.parseInt(mm)*60 + Integer.parseInt(ss);
	    icheck = (iLinkTime/iContTime)*100;
	
	    if (icheck >= 98 || paramVO.getSecond().equals("0")) paramVO.setChkRunTime("Y");
	
	}
	
	
}
