package kr.co.wincom.imcs.api.getNSEncryptVal;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetNSEncryptValServiceImpl implements GetNSEncryptValService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSEncryptVal");
	
	@Autowired
	private GetNSEncryptValDao getNSEncryptValDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSContList(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	/**
	 * @author HONG
	 * @since 2016-07-12
	 * 컨텐츠 타입과 구매여부, 구매시간, 구매타입 값을 리턴 (lgvod997.pc)
	 */
	@Override
	public GetNSEncryptValResultVO getNSEncryptVal(GetNSEncryptValRequestVO paramVO)	{
//		this.getNSContList(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		List<GetNSEncryptValResponseVO> resultVO	= new ArrayList<GetNSEncryptValResponseVO>();
		GetNSEncryptValResponseVO tempVO			= new GetNSEncryptValResponseVO();
		GetNSEncryptValResultVO	resultListVO	= new GetNSEncryptValResultVO();
		
		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2
		
		String msg			= "";
		
				
		try {
			
			tp1 = System.currentTimeMillis();
			
			String pwd = "";
			
			pwd = this.getEnctyptKey(paramVO);
			
			if(!"".equals(pwd)){
				
				String strOneTimeKey =commonService.createOneTimeKey(paramVO.getSaId(), paramVO.getAlbumId(), pwd, paramVO.getEncryptFlag(), paramVO.getServiceId(), imcsLog);
				
				if(strOneTimeKey != null && !"".equals(strOneTimeKey)){
					tempVO.setEncryptValue(strOneTimeKey);
				}
							
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("OneTimeKey 생성", String.valueOf(tp2 - tp1), methodName, methodLine);
							
						
				resultVO.add(tempVO);			
				
				resultListVO.setList(resultVO);
			}
			
		} catch(ImcsException ie) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());

			// TLO Log 를 Controller Layer 로 전달하기 위해 Setting 한다. (TLO Log 세부값은 finally 문에서 설정된다.)
			throw ie;
		} catch(Exception e) {
			isLastProcess	= ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			
			// TLO Log 를 Controller Layer 로 전달하기 위해 ImcsException 으로 변환한다. (TLO Log 세부값은 finally 문에서 설정된다.)
			throw new ImcsException();
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID013) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	

	/**
	 * 암호키 조회
	 * @param paramVO
	 * @return
	 */
	public String getEnctyptKey(GetNSEncryptValRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "nsvod013_001_20171214_001";
		String enctyptKey	= "";
		
		List<String> list	= new ArrayList<String>();

		try {
			try {
				list = getNSEncryptValDao.getEnctyptKey();
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list != null && !list.isEmpty()) {
				enctyptKey	= StringUtil.nullToSpace(list.get(0));
			}else{
				paramVO.setResultCode("31000000");
				
//				imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID013, sqlId, cache, "get_encrypt_key:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			}

		} catch (Exception e) {
			paramVO.setResultCode("31000000");
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID013, sqlId, cache, "get_encrypt_key:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return enctyptKey;
	}

}
