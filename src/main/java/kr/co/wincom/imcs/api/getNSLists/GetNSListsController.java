package kr.co.wincom.imcs.api.getNSLists;

import java.lang.management.ManagementFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.StatVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Controller
public class GetNSListsController {
	private Log imcsLogger		= LogFactory.getLog("API_Common_getNSLists");
	private Log statLogger		= LogFactory.getLog("TLO_getNSLists");
	
	@Autowired
	private GetNSListsService	getNSListsService;

	@Autowired
	private GetNSMakeListsService	getNSMakeListsService;
	
	//protected static String saId	= "";
	//protected static String stbMac	= "";

	
	/**
	 * HDTV 리스트 정보 가져오기 (lgvod829, 994.pc)
	 */
	public Object getNSLists(HttpServletRequest request, HttpServletResponse response, String szCmd, String szParam, String szStat) throws Exception {
		
//		long start = System.currentTimeMillis();
		
		// 프로세스 정보
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String pName	= ManagementFactory.getRuntimeMXBean().getName();		// 프로세스네임 
		String pId		= String.valueOf(Thread.currentThread().getId());				// 프로세스ID
		String methodName	= oStackTrace.getMethodName();						// 메소드명
		String methodLine	= String.valueOf(oStackTrace.getLineNumber()-3);	// 메소드라인
		String apiId	= "";
		
		GetNSListsResultVO resultVO		= new GetNSListsResultVO();
		GetNSListsRequestVO requestVO	= null;
		String saId		= "";
		String stbMac	= "";
		
		if("getNSLists".equals(szCmd)){
			this.imcsLogger	= LogFactory.getLog("API_Common_getNSLists");
			this.statLogger	= LogFactory.getLog("TLO_getNSLists");
			apiId	= ImcsConstants.API_PRO_ID994;
		} else if("getNSMakeLists".equals(szCmd)){
			this.imcsLogger	= LogFactory.getLog("API_Common_getNSMakeLists");
			this.statLogger	= null; //LogFactory.getLog("TLO_getNSMakeLists");	// MakeLists는 Stat로그를 남기지 않는다
			apiId	= ImcsConstants.API_PRO_ID829;
		}
		
		// 변수 초기화
		try {
			requestVO	= new GetNSListsRequestVO(szParam);
			saId		= requestVO.getSaId();
			stbMac		= requestVO.getStbMac();
		} catch (Exception e) {
			resultVO.setResultCode("31000000");
			IMCSLog imcsLog = new IMCSLog(imcsLogger, saId, stbMac, pId);
			
			// 인입 로그
			String szMsg = " svc[" + String.format("%-20s", apiId) + "] sts[start] rcv[" + szParam + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			String msg = " svc[" + ImcsConstants.API_PRO_ID994 + "] sts[error] msg["+ImcsConstants.RCV_MSG1 +"]";
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
		long tp_start = System.currentTimeMillis();
		
		try {
			// 인입 로그
			String szMsg = " svc[" + String.format("%-20s", apiId) + "] sts[start] rcv[" + szParam + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			requestVO.setPid(pId);
			if("getNSLists".equals(szCmd)){
				resultVO	= this.getNSListsService.getNSLists(requestVO);
			} else if("getNSMakeLists".equals(szCmd)){
				resultVO	= this.getNSMakeListsService.getNSMakeLists(requestVO);
			}
			
		} catch(ImcsException ce) {
			if(resultVO == null)		resultVO = new GetNSListsResultVO();
		} catch(Exception e) {
			if(resultVO == null)		resultVO = new GetNSListsResultVO();
		} finally {
			long tp_end = System.currentTimeMillis();
			imcsLog.timeLog("[" + methodName + "] tx_time", String.valueOf(tp_end - tp_start), methodName, methodLine);

			// 통합통계 로그 출력
			if("getNSLists".equals(szCmd)){
				IMCSLog statLog = new IMCSLog(statLogger);
				statVO.setCatId(requestVO.getCatId());
				statVO.setCatName(requestVO.getCateName());
				statVO.setResultCode(StringUtil.replaceNull(resultVO.getResultCode(), statVO.getResultCode()));
				statLog.statLog(statVO.toString());
			}
		}
		
//		long end = System.currentTimeMillis();
		//System.out.println("■■■■■■■■■■■■■■■■■■■■■■ getNSLists 수행시간(끝) ■■■■■■■■■■■■■■■■■■■■■■: " + ( end - start )/1000.0  );
		
		return resultVO;
	}
}
