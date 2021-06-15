package kr.co.wincom.imcs.api.getNSContStat;

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
public class GetNSContStatController {
	private Log imcsLogger		= LogFactory.getLog("API_Common_getNSContStat");
	private Log statLogger		= LogFactory.getLog("TLO_getNSContStat");

	@Autowired
	private GetNSContStatService		getNSContStatService;
	
	//protected static String saId	= "";
	//protected static String stbMac	= "";

	public Object getNSContStat(HttpServletRequest request, HttpServletResponse response, String szParam, String szStat) throws Exception {
		// 프로세스 정보
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String pName	= ManagementFactory.getRuntimeMXBean().getName();		// 프로세스네임 
		// 2018.03.20 - 프로세스ID로 하니까 로그 구분하기가 어려워 쓰레드ID? 로 구분!
//		String pId		= pName.substring(0, pName.indexOf("@"));
		String pId		= String.valueOf(Thread.currentThread().getId());
		String methodName	= oStackTrace.getMethodName();						// 메소드명
		String methodLine	= String.valueOf(oStackTrace.getLineNumber()-3);	// 메소드라인
		
		GetNSContStatResultVO resultVO		= new GetNSContStatResultVO();
		GetNSContStatRequestVO requestVO	= null;
		String saId		= "";
		String stbMac	= "";
		
		// 변수 초기화
		try {
			requestVO	= new GetNSContStatRequestVO(szParam);
			saId		= requestVO.getSaId();
			stbMac		= requestVO.getStbMac();
		} catch (Exception e) {
			resultVO.setResultCode("31000000");
			IMCSLog imcsLog = new IMCSLog(imcsLogger, saId, stbMac, pId);	
			
			// 인입 로그
			String szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID997) + "] sts[start] rcv[" + szParam + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			String msg = " svc[" + ImcsConstants.API_PRO_ID997 + "] sts[error] msg["+ImcsConstants.RCV_MSG1 +"]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			return "1|INPUT PARAM ERROR|||" + ImcsConstants.ROWSEP;
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
			String szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID997) + "] sts[start] rcv[" + szParam + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			requestVO.setPid(pId);
			resultVO	= this.getNSContStatService.getNSContStat(requestVO);
		} catch(ImcsException ie) {
			if( resultVO == null )		resultVO = new GetNSContStatResultVO();
		} catch(Exception e) {
			if( resultVO == null )		resultVO = new GetNSContStatResultVO();
		} finally {
			// 결과 로그 출력
			long tp_end = System.currentTimeMillis();
			imcsLog.timeLog("[" + methodName + "] tx_time", String.valueOf(tp_end - tp_start), methodName, methodLine);

			IMCSLog statLog = new IMCSLog(statLogger);
			
			statVO.setProductId(resultVO.getProductId());
			statVO.setProductName(resultVO.getProductName());
			statVO.setProductPrice(resultVO.getProductPrice());			
			statVO.setContentsName(resultVO.getContentsName());
			statVO.setBuyingType(resultVO.getBuyingType());
			
			statVO.setCatId(requestVO.getCatId());
			statVO.setContentsId(requestVO.getAlbumId());
			
			statVO.setResultCode(StringUtil.replaceNull(resultVO.getResultCode(), statVO.getResultCode()));
			statLog.statLog(statVO.toString());
		}
		
		return resultVO;
	}
}
