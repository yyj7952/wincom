package kr.co.wincom.imcs.api.getNSSeriesList;

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
public class GetNSSeriesListController {
	private Log imcsLogger	= LogFactory.getLog("API_Common_getNSSeriesList");
	private Log statLogger	= LogFactory.getLog("TLO_getNSSeriesList");
	
	@Autowired
	private GetNSSeriesListService GetNSSeriesListService;
	
	//protected static String saId	= "";
	//protected static String stbMac	= "";

	public Object getNSSeriesList(HttpServletRequest request, HttpServletResponse response
			, String szParam, String szStat) throws Exception
	{
		// 프로세스 정보
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String pName	= ManagementFactory.getRuntimeMXBean().getName();		// 프로세스네임 
		String pId		= String.valueOf(Thread.currentThread().getId());				// 프로세스ID
		String methodName	= oStackTrace.getMethodName();						// 메소드명
		String methodLine	= String.valueOf(oStackTrace.getLineNumber()-3);	// 메소드라인
		
		GetNSSeriesListResultVO resultVO	= new GetNSSeriesListResultVO();
		GetNSSeriesListRequestVO requestVO	= null;
		String saId		= "";
		String stbMac	= "";
		long tp_start = System.currentTimeMillis();
		
		
		// 변수 초기화
		try {
			requestVO	= new GetNSSeriesListRequestVO(szParam);
			saId		= requestVO.getSaId();
			stbMac		= requestVO.getStbMac();
		} catch (Exception e) {
			resultVO.setResultCode("31000000");
			IMCSLog imcsLog = new IMCSLog(imcsLogger, saId, stbMac, pId);
			
			// 인입로그
			String szMsg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID210) + "] sts[start] rcv[" + szParam + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			String msg = " svc[" + ImcsConstants.NSAPI_PRO_ID210 + "] sts[error] msg["+ImcsConstants.RCV_MSG1 +"]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			return "1|INPUT PARAM ERROR||||||||||" + ImcsConstants.ROWSEP; //파라미터오류리턴
			//throw new ImcsException();
		}
		
		// 통계로그 정보
		StatVO statVO	= new StatVO();
		try{
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
		requestVO.setTp_start(tp_start);
		
		try {
			// 인입로그
			String szMsg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID210) + "] sts[start] rcv[" + szParam + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			requestVO.setPid(pId);
		    resultVO = this.GetNSSeriesListService.getNSSeriesList(requestVO);
		}  catch(ImcsException ie) {
			if( resultVO == null )		resultVO = new GetNSSeriesListResultVO();
		} catch(Exception e) {
			if( resultVO == null )		resultVO = new GetNSSeriesListResultVO();
		} finally {
			// 결과 로그 출력
			long tp_end = System.currentTimeMillis();
			imcsLog.timeLog("[" + methodName + "] tx_time", String.valueOf(tp_end - tp_start), methodName, methodLine);
		 
			IMCSLog statLog = new IMCSLog(statLogger);
			statVO.setResultCode(StringUtil.replaceNull(resultVO.getResultCode(), statVO.getResultCode()));
			statLog.statLog(statVO.toString());
		}
		
		return resultVO;
	}
	
}
