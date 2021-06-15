package kr.co.wincom.imcs.api.getNSSuggestVOD;

import java.lang.management.ManagementFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.StatVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class GetNSSuggestVODController {
	private Log imcsLogger = LogFactory.getLog("API_Common_getNSSuggestVOD");
	private Log statLogger = LogFactory.getLog("TLO_getNSSuggestVOD");
	
	@Autowired
	private GetNSSuggestVODService GetNSSuggestVODService;
	
	//protected static String saId	= "";
	//protected static String stbMac	= "";
	
	public Object getNSSuggestVOD(HttpServletRequest request, HttpServletResponse response, String szParam, String szStat) throws Exception	{
		// 프로세스 정보
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String pName	= ManagementFactory.getRuntimeMXBean().getName();		// 프로세스네임 
		String pId		= String.valueOf(Thread.currentThread().getId());				// 프로세스ID
		String methodName	= oStackTrace.getMethodName();						// 메소드명
		String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 3);	// 메소드라인
		
		GetNSSuggestVODResultVO resultVO	= new GetNSSuggestVODResultVO();
		GetNSSuggestVODRequestVO requestVO	= null;
		String saId		= "";
		String stbMac	= "";
		long tp_start = System.currentTimeMillis();
		
		
		// 변수 초기화
		try {
			requestVO	= new GetNSSuggestVODRequestVO(szParam);
			saId		= requestVO.getSaId();
			stbMac		= requestVO.getStbMac();
		} catch (Exception e) {
			resultVO.setResultCode("31000000");
			IMCSLog imcsLog = new IMCSLog(imcsLogger, saId, stbMac, pId);
			String szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID899) + "] sts[start] rcv[" + szParam + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			String msg = " svc[" + ImcsConstants.API_PRO_ID899 + "] sts[error] msg["+ImcsConstants.RCV_MSG1 +"]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			return "";
			//throw new ImcsException();
		}
		
		// 서비스로그 초기화
		IMCSLog imcsLog = new IMCSLog(imcsLogger, saId, stbMac, pId);			
		requestVO.setTp_start(tp_start);
		
		// 통계로그 정보
		StatVO statVO	= null;
		try {
			statVO	= new StatVO(szStat);
			statVO.setApiNm(methodName);
			statVO.setSaId(saId);
			statVO.setStbMac(stbMac);
		} catch (Exception e) {
			resultVO.setResultCode("31000000");
			throw new ImcsException();
		}
		
		
				
		try {
			// 입수로그
			String szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID899) + "] sts[start] rcv[" + szParam + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			requestVO.setPid(pId);
			resultVO = this.GetNSSuggestVODService.getNSSuggestVOD(requestVO);
		}  catch(ImcsException ie) {
			if( resultVO == null )		resultVO = new GetNSSuggestVODResultVO();
		} catch(Exception e) {
			if( resultVO == null )		resultVO = new GetNSSuggestVODResultVO();
		} finally {
			// 결과 로그 출력
			long tp_end = System.currentTimeMillis();
			imcsLog.timeLog("[" + methodName + "] tx_time", String.valueOf(tp_end - tp_start), methodName, methodLine);
			
			IMCSLog statLog = new IMCSLog(statLogger);
			statVO.setContentsId(resultVO.getContentsId());
			statVO.setContentsName(resultVO.getContentsName());
			statVO.setCatId(resultVO.getCatId());
			statVO.setCatName(resultVO.getCatName());
			statVO.setResultCode(StringUtil.replaceNull(resultVO.getResultCode(), statVO.getResultCode()));
			statLog.statLog(statVO.toString());			
		}
		
	    	    
		return resultVO;
	}
	
	
}
