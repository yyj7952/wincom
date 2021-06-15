package kr.co.wincom.imcs.api.getFXContStat;

import java.lang.management.ManagementFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class GetFXContStatController {
	private Log imcsLogger		= LogFactory.getLog("API_Common_getFXContStat");

	@Autowired
	private GetFXContStatService getFXContStatService;
	
	//protected static String saId	= "";
	//protected static String stbMac	= "";

	public Object getFXContStat(HttpServletRequest request, HttpServletResponse response, String szParam, String szStat) throws Exception {
		// 프로세스 정보
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String pName	= ManagementFactory.getRuntimeMXBean().getName();		// 프로세스네임 
		String pId		= String.valueOf(Thread.currentThread().getId());				// 프로세스ID
		String methodName	= oStackTrace.getMethodName();						// 메소드명
		String methodLine	= String.valueOf(oStackTrace.getLineNumber()-3);	// 메소드라인
		
		GetFXContStatResultVO resultVO		= new GetFXContStatResultVO();
		GetFXContStatRequestVO requestVO	= null;
		String saId		= "";
		String stbMac	= "";
		
		// 변수 초기화
		try {
			requestVO	= new GetFXContStatRequestVO(szParam);
			saId		= requestVO.getSaId();
			stbMac		= requestVO.getStbMac();
		} catch (Exception e) {
			IMCSLog imcsLog = new IMCSLog(imcsLogger, saId, stbMac, pId);

			// 인입 로그
			String szMsg = " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID151) + "] sts[start] rcv[" + szParam + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
						
			String msg = " svc[" + ImcsConstants.FXAPI_PRO_ID151 + "] sts[error] msg["+ImcsConstants.RCV_MSG1 +"]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			return "";
//			throw new ImcsException();
		}
		
		// 서비스로그 초기화
		IMCSLog imcsLog = new IMCSLog(imcsLogger, saId, stbMac, pId);
		long tp_start = System.currentTimeMillis();
		requestVO.setTp_start(tp_start);
		try {
			// 인입 로그
			String szMsg = " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID151) + "] sts[start] rcv[" + szParam + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			requestVO.setPid(pId);
			resultVO	= this.getFXContStatService.getFXContStat(requestVO);
		} catch(ImcsException ie) {
			if(resultVO == null)		resultVO = new GetFXContStatResultVO();
		} catch(Exception e) {
			if(resultVO == null)		resultVO = new GetFXContStatResultVO();
		} finally {
			// 결과 로그 출력
			long tp_end = System.currentTimeMillis();
			imcsLog.timeLog("[" + methodName + "] tx_time", String.valueOf(tp_end - tp_start), methodName, methodLine);
		}
		
		return resultVO;
	}
}
