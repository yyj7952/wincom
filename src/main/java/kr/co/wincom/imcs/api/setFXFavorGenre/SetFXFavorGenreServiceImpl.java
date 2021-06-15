package kr.co.wincom.imcs.api.setFXFavorGenre;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.nosql.NoSQLRedisDao;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom2.imcs.api.setFXFavorGenre.SetFXFavorGenreDao2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.common.NosqlCacheType;

@Service
public class SetFXFavorGenreServiceImpl implements SetFXFavorGenreService {
	private Log imcsLogger = LogFactory.getLog("API_setFXFavorGenre");
	
	@Autowired
	private SetFXFavorGenreDao setFXFavorGenreDao;
	
	@Autowired
	private SetFXFavorGenreDao2 setFXFavorGenreDao2;
	
//	@Autowired
//	private NoSQLRedisDao noSQLRedisDao;
	
//	public void setFxFavorGenre(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public SetFXFavorGenreResultVO setFxFavorGenre(SetFXFavorGenreRequestVO paramVO){
//		this.setFxFavorGenre(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		SetFXFavorGenreResultVO resultVO = new SetFXFavorGenreResultVO();
		
		//DB#2 접속테스트
		//System.out.println("### DB#2접속 시간:" + setFXFavorGenreDao2.getDB2Time() + " ###");
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		String msg		= "";
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		Integer nResultSet	= 0;
		
	    int nDataCnt = 0;
	    long tp1, tp2 = 0;
	    		
		try{
			// 선호장르 전체삭제
			tp1 = System.currentTimeMillis();
			
			if("".equals(paramVO.getCatId())) {
				this.deleteFavorGenre(paramVO);
				nResultSet	= paramVO.getResultSet();
				
				//2019.03.06 권형도 수정
				if (nResultSet==0) { //tpacall setFXFavorGenre 로직
					
					String binding_str = String.format("\b%s\b%s\b", paramVO.getSaId(), paramVO.getStbMac());
					paramVO.setBinding1(binding_str);
					paramVO.setDml_gb("D");
					
					try {
						int ret = this.setFxFavorGenre2(paramVO);
						
						if (ret == 0) {
							msg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID010) + "] setFXFavorGenreDao2 [PT_VO_FAVORITE_GENRE] table [" + nDataCnt + "] records Success at ";
						} else {
							msg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID010) + "] setFXFavorGenreDao2 [PT_VO_FAVORITE_GENRE] table Failed at";
							nResultSet	= -1;
						}
						
						imcsLog.serviceLog(msg, methodName, methodLine);
					} catch(Exception e) {
						msg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID010) + "] setFXFavorGenreDao2 [PT_VO_FAVORITE_GENRE] table Failed at";
						imcsLog.serviceLog(msg, methodName, methodLine);

						nResultSet	= -1;
					}
				}
			} else {
				try {
					nDataCnt = setFXFavorGenreDao.deleteFavorGenre(paramVO);
					nDataCnt = paramVO.getCatList().size();
					System.out.println("############################################:" + nDataCnt);
					msg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID010) + "] delete [PT_VO_FAVORITE_GENRE] table [" + nDataCnt + "] records Success at ";
					imcsLog.serviceLog(msg, methodName, methodLine);
				} catch(Exception e) {
					msg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID010) + "] delete [PT_VO_FAVORITE_GENRE] table Failed at";
					imcsLog.serviceLog(msg, methodName, methodLine);

					nResultSet	= -1;
				}
			}
			
			
			System.out.println("####################:" + nDataCnt);
			for(int i = 0; i < nDataCnt; i++) {
				paramVO.setFavCnt(String.valueOf(i));
				paramVO.setCatInputId(paramVO.getCatList().get(i));
				
				try {
					this.insertFavorGenre(paramVO);
					nResultSet	= paramVO.getResultSet();
				}catch(Exception e) {}
			}
			
			if(!"".equals(paramVO.getCatId())) { //카테고리 정보가 있는 경우임
				//2019.03.06 권형도 수정 (DB분리처리)
				if (nResultSet==0) { //tpacall setFXFavorGenre 로직
					
					String binding_str = String.format("\b%s\b%s\b", paramVO.getSaId(), paramVO.getStbMac());
					paramVO.setBinding1(binding_str);
					paramVO.setDml_gb("I");
					
					try {
						int ret = this.setFxFavorGenre2(paramVO);
						
						if (ret == 0) {
							msg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID010) + "] setFXFavorGenreDao2 [PT_VO_FAVORITE_GENRE] table [" + nDataCnt + "] records Success at ";
						} else {
							msg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID010) + "] setFXFavorGenreDao2 [PT_VO_FAVORITE_GENRE] table Failed at";
							nResultSet	= -1;
						}
						
						imcsLog.serviceLog(msg, methodName, methodLine);
					} catch(Exception e) {
						msg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID010) + "] setFXFavorGenreDao2 [PT_VO_FAVORITE_GENRE] table Failed at";
						imcsLog.serviceLog(msg, methodName, methodLine);

						nResultSet	= -1;
					}
				}
			}
			
			if (paramVO.getFavCnt()==null || "".equals(paramVO.getFavCnt()) ) paramVO.setFavCnt("0");
			nDataCnt = Integer.parseInt(paramVO.getFavCnt());
			
			tp2 = System.currentTimeMillis();
			imcsLog.timeLog("관심장르 등록", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			if(nResultSet == 0){
				resultVO.setFlag("0");
				resultVO.setErrMsg("관심장르가 등록되었습니다.");
			} else if(nResultSet != 0 && nDataCnt > 5) {
				resultVO.setFlag("1");
				resultVO.setErrMsg("등록 가능한 관심장르 개수를 초과하였습니다.");
				resultVO.setErrCode("01");
				
				throw new ImcsException(resultVO.getFlag(), resultVO.getErrMsg(), resultVO.getErrCode());
			} else {
				resultVO.setFlag("1");
				resultVO.setErrMsg("관심장르 등록이 실패하였습니다.");
				resultVO.setErrCode("02");
				
				throw new ImcsException(resultVO.getFlag(), resultVO.getErrMsg(), resultVO.getErrCode());
			}
		}catch(ImcsException ie) {
			resultVO.setFlag(ie.getFlag());
			resultVO.setErrMsg(ie.getMessage());
			resultVO.setErrCode(ie.getErrorCode());
			
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			
			throw ie;
		} catch(Exception e) {
			resultVO.setFlag("1");
			resultVO.setErrMsg("관심장르 등록이 실패하였습니다.");
			resultVO.setErrCode("02");
			
			isLastProcess	= ImcsConstants.RCV_MSG6; 

			throw new ImcsException(resultVO.getFlag(), resultVO.getErrMsg(), resultVO.getErrCode());
		} finally{
			msg = " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID010) + "] sts[" + ImcsConstants.LOG_MSG3 + "]"
					+ " snd[FLAG=" + resultVO.getFlag() + "|MESSAGE=" + resultVO.getErrMsg() + "|]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID010) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);			
		}
		
		return resultVO;
	}
    
	
	
    /**
     * 장르정보 전체 삭제
     * @param paramVO
     * @return
     * @throws Exception
     */
    public Integer deleteFavorGenre(SetFXFavorGenreRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId = "fxvod010_d01_20171214_001";
		String szMsg = "";
		
		int querySize = 0;
		
    	try {
			try{
				querySize = setFXFavorGenreDao.deleteFavorGenre(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				System.out.println("deleteFavorGenre:" + e.getMessage());
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
				if(querySize == 0) {
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID010) + "] delete [PT_VO_FAVORITE_GENRE] table Failed at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					
					//imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID010, "", cache, "update fail:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
					paramVO.setResultSet(-1);
				} else {
					//imcsLog.dbLog(ImcsConstants.FXAPI_PRO_ID010, sqlId, cache, querySize, methodName, methodLine);

					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID010) + "] delete [PT_VO_FAVORITE_GENRE] table [" + querySize + "] records Success at";
					imcsLog.serviceLog(szMsg, methodName, methodLine);	
				}
			} catch (Exception e) {}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID010) + "] delete [PT_VO_FAVORITE_GENRE] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID010, "", cache, "update fail:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			paramVO.setResultSet(-1);
			
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
		return querySize;
	}
    
    
    
    /**
     * 관심장르 등록
     * @param paramVO
     * @return
     * @throws Exception
     */
    public Integer insertFavorGenre(SetFXFavorGenreRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String sqlId =  "fxvod010_i01_20171214_001";
		
		int querySize = 0;
		
		String szMsg = "";
		    	
    	try {
			try{
				querySize = setFXFavorGenreDao.insertFavorGenre(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try {
				//imcsLog.dbLog(ImcsConstants.FXAPI_PRO_ID010, sqlId, cache, querySize, methodName, methodLine);

				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID010) + "] insert [PT_VO_FAVORITE_GENRE] table[" + querySize + "] records Success at";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			} catch (Exception e) {}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID010) + "] insert [PT_VO_FAVORITE_GENRE] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID010, sqlId, cache, "update fail:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			paramVO.setResultSet(-1);
		}
		
		return querySize;
	}
    
    /**
     * DB분리에 따른 tpacall setFXFavorGenre 구현
     * @param paramVO
     * @return
     * @throws Exception
     */
    public Integer setFxFavorGenre2(SetFXFavorGenreRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId =  "fxvod010_d01_20190307_001";
    	
		int ret = 0;
		int querySize = 0;
		
		String szMsg = "";
		
		try {

			if (paramVO.getDml_gb().equals("D")) {
				
				querySize = setFXFavorGenreDao2.deleteFavorGenre(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				//imcsLog.dbLog(ImcsConstants.FXAPI_PRO_ID010, sqlId, null, querySize, methodName, methodLine);
				if (querySize >= 0) {	
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID010) + "] setFXFavorGenreDao2 delete [PT_VO_FAVORITE_GENRE] table[" + querySize + "] records Success at";
				} else {
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID010) + "] setFXFavorGenreDao2 delete [PT_VO_FAVORITE_GENRE] table Failed at";
				}
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
			} else if(paramVO.getDml_gb().equals("I")) {
				
				querySize = setFXFavorGenreDao2.deleteFavorGenre(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				//imcsLog.dbLog(ImcsConstants.FXAPI_PRO_ID010, sqlId, null, querySize, methodName, methodLine);
				if (querySize >= 0) {	
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID010) + "] setFXFavorGenreDao2 delete [PT_VO_FAVORITE_GENRE] table[" + querySize + "] records Success at";
				} else {
					szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID010) + "] setFXFavorGenreDao2 delete [PT_VO_FAVORITE_GENRE] table Failed at";
				}
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
				if (paramVO.getCatList().size() > 0) {
					
					int favCnt = 0;
					for (String cate_id : paramVO.getCatList()) {
						favCnt ++;
						paramVO.setCatInputId(cate_id);
						paramVO.setFavCnt(String.valueOf(favCnt));
						querySize = setFXFavorGenreDao2.insertFavorGenre(paramVO);
						paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
						
						//imcsLog.dbLog(ImcsConstants.FXAPI_PRO_ID010, sqlId, null, querySize, methodName, methodLine);
						if (querySize >= 0) {	
							szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID010) + "] setFXFavorGenreDao2 insert [PT_VO_FAVORITE_GENRE] table[" + querySize + "] records Success at";
						} else {
							szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID010) + "] setFXFavorGenreDao2 insert [PT_VO_FAVORITE_GENRE] table Failed at";
						}
						imcsLog.serviceLog(szMsg, methodName, methodLine);
						
					}
				}
			}
			
		} catch (Exception e) {
			szMsg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID010) + "] setFXFavorGenreDao2 [PT_VO_FAVORITE_GENRE] table Failed at";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			//imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID010, sqlId, null, "update fail:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			paramVO.setResultSet(-1);
		}
		
		return ret;
	}
}
