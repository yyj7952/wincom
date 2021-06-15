package kr.co.wincom.imcs.api.getNSPeriod;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Transactional(readOnly = true)
public class GetNSPeriodController {
	private Log imcsLogger		= LogFactory.getLog("API_Common_getNSPeriod");
	private Log statLogger		= LogFactory.getLog("TLO_getNSPeriod");

	@Autowired
	private GetNSPeriodService		GetNSPeriodService;
	
	//protected static String saId	= "";
	//protected static String stbMac	= "";

	public Object getNSPeriod(HttpServletRequest request, HttpServletResponse response, String szParam, String szStat) throws Exception {
		// 프로세스 정보
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String pName	= ManagementFactory.getRuntimeMXBean().getName();		// 프로세스네임 
		String pId		= String.valueOf(Thread.currentThread().getId());				// 프로세스ID
		String methodName	= oStackTrace.getMethodName();						// 메소드명
		String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 3);	// 메소드라인
		
		GetNSPeriodResultVO resultVO	= new GetNSPeriodResultVO();
		GetNSPeriodRequestVO requestVO	= null;
		String saId		= "";
		String stbMac	= "";
		long tp_start = System.currentTimeMillis();
		
		// 변수 초기화
		try {
			requestVO	= new GetNSPeriodRequestVO(szParam);
			saId		= requestVO.getSaId();
			stbMac		= requestVO.getStbMac();
		} catch (Exception e) {
			resultVO.setResultCode("31000000");
			IMCSLog imcsLog = new IMCSLog(imcsLogger, saId, stbMac, pId);
			
			// 인입 로그
			String szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID992) + "] sts[start] rcv[" + szParam + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);			
			
			String msg = " svc[" + ImcsConstants.API_PRO_ID992 + "] sts[error] msg["+ImcsConstants.RCV_MSG1 +"]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			return "";
			//throw new ImcsException();
		}
		
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
		
		// 서비스로그 초기화
		IMCSLog imcsLog = new IMCSLog(imcsLogger, saId, stbMac, pId);
		try {
			// 인입 로그
			String szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID992) + "] sts[start] rcv[" + szParam + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			requestVO.setPid(pId);
			resultVO	= this.GetNSPeriodService.getNSPeriod(requestVO);
		} catch(ImcsException ie) {
			if(resultVO == null)	resultVO = new GetNSPeriodResultVO();
		} catch(Exception e) {
			if(resultVO	== null)	resultVO = new GetNSPeriodResultVO();
		} finally {
			// 결과 로그 출력
			long tp_end = System.currentTimeMillis();
			imcsLog.timeLog("[" + methodName + "] tx_time", String.valueOf(tp_end - tp_start), methodName, methodLine);

			// 통합통계 로그 출력
			IMCSLog statLog = new IMCSLog(statLogger);
			statVO.setResultCode(StringUtil.replaceNull(resultVO.getResultCode(), statVO.getResultCode()));
			statLog.statLog(statVO.toString());
		}
		
		return resultVO;
	}
	
	
	//CommonServlet 타지 않고, 컨트롤러 직접호출 방식 (권형도)
//	@RequestMapping(value = "/servlets/CommSvl3", method=RequestMethod.GET, params="CMD=getNSPeriod")
//	@ResponseBody
//	public String getNSPeriod2 (
//			@RequestParam(value = "CMD", required=false)		String szCmd,
//			@RequestParam(value = "PARAM", required=false)		String szParam,
//			@RequestParam(value = "STATS", required=false)		String szStat, 
//			HttpServletRequest request, HttpServletResponse response)  throws Exception {
//		// 프로세스 정보
//		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
//		String pName	= ManagementFactory.getRuntimeMXBean().getName();		// 프로세스네임 
//		String pId		= String.valueOf(Thread.currentThread().getId());				// 프로세스ID
//		String methodName	= oStackTrace.getMethodName();						// 메소드명
//		String methodLine	= String.valueOf(oStackTrace.getLineNumber() - 3);	// 메소드라인
//		
//		GetNSPeriodResultVO resultVO	= new GetNSPeriodResultVO();
//		GetNSPeriodRequestVO requestVO	= null;
//		String saId		= "";
//		String stbMac	= "";
//		long tp_start = System.currentTimeMillis();
//		
//		// 변수 초기화
//		try {
//			requestVO	= new GetNSPeriodRequestVO(szParam);
//			saId		= requestVO.getSaId();
//			stbMac		= requestVO.getStbMac();
//		} catch (Exception e) {
//			resultVO.setResultCode("31000000");
//			IMCSLog imcsLog = new IMCSLog(imcsLogger, saId, stbMac, pId);
//			
//			// 인입 로그
//			String szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID992) + "] sts[start] rcv[" + szParam + "]";
//			imcsLog.serviceLog(szMsg, methodName, methodLine);			
//			
//			String msg = " svc[" + ImcsConstants.API_PRO_ID992 + "] sts[error] msg["+ImcsConstants.RCV_MSG1 +"]";
//			imcsLog.serviceLog(msg, methodName, methodLine);
//			
//			return "";
//			//throw new ImcsException();
//		}
//		
//		// 통계로그 정보
//		StatVO statVO	= null;
//		try {
//			statVO	= new StatVO(szStat);
//			statVO.setApiNm(methodName);
//			statVO.setSaId(saId);
//			statVO.setStbMac(stbMac);
//		} catch (Exception e) {
//			resultVO.setResultCode("31000000");
//			throw new ImcsException();
//		}
//		
//		// 서비스로그 초기화
//		IMCSLog imcsLog = new IMCSLog(imcsLogger, saId, stbMac, pId);
//		try {
//			// 인입 로그
//			String szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID992) + "] sts[start] rcv[" + szParam + "]";
//			imcsLog.serviceLog(szMsg, methodName, methodLine);
//			
//			requestVO.setPid(pId);
//			resultVO	= this.GetNSPeriodService.getNSPeriod(requestVO);
//		} catch(ImcsException ie) {
//			if(resultVO == null)	resultVO = new GetNSPeriodResultVO();
//		} catch(Exception e) {
//			if(resultVO	== null)	resultVO = new GetNSPeriodResultVO();
//		} finally {
//			// 결과 로그 출력
//			long tp_end = System.currentTimeMillis();
//			imcsLog.timeLog("[" + methodName + "] tx_time", String.valueOf(tp_end - tp_start), methodName, methodLine);
//
//			// 통합통계 로그 출력
//			IMCSLog statLog = new IMCSLog(statLogger);
//			statVO.setResultCode(StringUtil.replaceNull(resultVO.getResultCode(), statVO.getResultCode()));
//			statLog.statLog(statVO.toString());
//		}
//		
//		//return resultVO;
//		return resultVO.toString();
//	}
}
