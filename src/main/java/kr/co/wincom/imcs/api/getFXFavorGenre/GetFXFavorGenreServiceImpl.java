package kr.co.wincom.imcs.api.getFXFavorGenre;

import java.sql.SQLException;
import java.util.ArrayList;
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
public class GetFXFavorGenreServiceImpl implements GetFXFavorGenreService {
	private Log imcsLogger		= LogFactory.getLog("API_getFXFavorGenre");
	
	@Autowired
	private GetFXFavorGenreDao getFXFavorGenreDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getFXFavorGenre(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;

	/**
	 * @author HONG
	 */
	@Override
	public GetFXFavorGenreResultVO getFXFavorGenre(GetFXFavorGenreRequestVO paramVO)	{
//		this.getFXFavorGenre(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		List<GetFXFavorGenreResponseVO> resultVO	= new ArrayList<GetFXFavorGenreResponseVO>();
		GetFXFavorGenreResultVO	resultListVO		= new GetFXFavorGenreResultVO();
		
		long tp1			= System.currentTimeMillis();		// timePoint 1
		long tp2			= 0;		// timePoint 2
		
		String msg			= "";
		
		try {
			// 서버IP정보 조회
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			// 관림목록 조회
			resultVO	= this.getFavorGenre(paramVO);
			
			tp1 = System.currentTimeMillis();
			imcsLog.timeLog("관심장르 조회" , String.valueOf(tp1 - tp2), methodName, methodLine);
			
			resultListVO.setList(resultVO);
		   
			tp2 = System.currentTimeMillis();
			imcsLog.timeLog("관심장르 정보 Fetch", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			
		} catch(ImcsException ie) {
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
			throw ie;
		} catch(Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException();
		} finally{
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID020) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}


	/**
	 * 관심장르 정보 조회
	 * @param 	GetFXFavorGenreRequestVO paramVO
	 * @return  List<GetFXFavorGenreResultVO>
	 **/
	public List<GetFXFavorGenreResponseVO> getFavorGenre(GetFXFavorGenreRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "fxvod020_001_20171214_001";
		int querySize	= 0;
		
		List<GetFXFavorGenreResponseVO> list = new ArrayList<GetFXFavorGenreResponseVO>();

		try {
			try {
				list = getFXFavorGenreDao.getFavorGenre(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list != null && !list.isEmpty()) {
				querySize = list.size();
			}
			
			try{
//				imcsLog.dbLog(ImcsConstants.FXAPI_PRO_ID020, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){
//				IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
//				imcsLog.debugLog(methodName,"NosqlNoLogging");
			}
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID020, sqlId, cache, "cate_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			throw new ImcsException();
		}

		return list;
	}
	

	
	
	
	
}
