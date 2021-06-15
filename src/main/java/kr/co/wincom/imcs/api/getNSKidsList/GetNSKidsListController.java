package kr.co.wincom.imcs.api.getNSKidsList;

import java.lang.management.ManagementFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import kr.co.wincom.imcs.api.getNSKidsEStudy.GetNSKidsEStudyResultVO;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.StatVO;
import kr.co.wincom.imcs.handler.ImcsException;

@Controller
public class GetNSKidsListController
{
	private final static String API_LOG_NAME = "000/getNSKidsList";
	
	private Log imcsLogger		= LogFactory.getLog("API_Common_getNSKidsList");
	private Log statLogger		= LogFactory.getLog("TLO_getNSKidsList");
	
	@Autowired
	private GetNSKidsListService getNSKidsListService;
	
	public Object getNSKidsList(HttpServletRequest request, HttpServletResponse response , String szParam, String szStat) throws Exception
	{
		// 프로세스 정보
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String pName		= ManagementFactory.getRuntimeMXBean().getName();		// 프로세스네임 
		String pId			= String.valueOf(Thread.currentThread().getId());		// 프로세스ID
		String methodName	= oStackTrace.getMethodName();							// 메소드명
		String methodLine	= String.valueOf(oStackTrace.getLineNumber()-3);		// 메소드라인
		
		GetNSKidsListResultVO resultVO = new GetNSKidsListResultVO();
		GetNSKidsListRequestVO requestVO = null;
		
		String saId		= "";
		String stbMac	= "";
		String catId	= "";
		
		try
		{
			requestVO	= new GetNSKidsListRequestVO(szParam);
			saId		= requestVO.getSaId();
			stbMac		= requestVO.getStbMac();
			catId		= requestVO.getCatId();
		}
		catch(Exception e)
		{
			resultVO.setResultCode("31000000");
			IMCSLog imcsLog = new IMCSLog(imcsLogger, saId, stbMac, pId);
			
			// 인입 로그
			String szMsg = " svc[" + String.format("%-20s", API_LOG_NAME) + "] sts[start] rcv[" + szParam + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			String msg = " svc[" + API_LOG_NAME + "] sts[error] msg["+ImcsConstants.RCV_MSG1 +"]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			return "1|INPUT PARAM ERROR||||||||\f";
		}
		
		// 통계로그 정보
		StatVO statVO	= null;
		try
		{
			statVO	= new StatVO(szStat);
			statVO.setApiNm(methodName);
			statVO.setSaId(saId);
			statVO.setStbMac(stbMac);
			statVO.setCatId(catId);
		}
		catch (Exception e)
		{
			resultVO.setResultCode("31000000");
			throw new ImcsException();
		}
		
		// 서비스로그 초기화
		IMCSLog imcsLog = new IMCSLog(imcsLogger, saId, stbMac, pId);
		long tp_start = System.currentTimeMillis();
		
		try
		{
			// 인입 로그
			String szMsg = " svc[" + String.format("%-20s", API_LOG_NAME) + "] sts[start] rcv[" + szParam + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			requestVO.setPid(pId);
			resultVO = this.getNSKidsListService.getNSKidsList(requestVO);
		}
		catch(ImcsException ie)
		{
			if(resultVO == null)		resultVO = new GetNSKidsListResultVO();
		}
		catch(Exception e)
		{
			if(resultVO == null)		resultVO = new GetNSKidsListResultVO();
		}
		finally
		{
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

















