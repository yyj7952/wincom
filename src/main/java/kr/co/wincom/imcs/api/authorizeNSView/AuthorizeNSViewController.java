package kr.co.wincom.imcs.api.authorizeNSView;

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
public class AuthorizeNSViewController {
	private Log imcsLogger		= LogFactory.getLog("API_Common_authorizeNSView");
	private Log statLogger		= LogFactory.getLog("TLO_authorizeNSView");

	@Autowired
	private AuthorizeNSViewService	authorizeNSViewService;
	
	//protected static String saId	= "";
	//protected static String stbMac	= "";

	/**
	 * 시청 인증정보 및 파일명 리턴  (lgvod179.pc)
	 */
	public Object authorizeNSView(HttpServletRequest request, HttpServletResponse response
			, String szParam, String szStat) throws Exception {
		// 프로세스 정보
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String pName	= ManagementFactory.getRuntimeMXBean().getName();		// 프로세스네임 
		String pId		= String.valueOf(Thread.currentThread().getId());				// 프로세스ID
		String methodName	= oStackTrace.getMethodName();						// 메소드명
		String methodLine	= String.valueOf(oStackTrace.getLineNumber()-3);	// 메소드라인
		
		AuthorizeNSViewResultVO resultVO	= new AuthorizeNSViewResultVO();
		AuthorizeNSViewRequestVO requestVO	= null;
		String saId		= "";
		String stbMac	= "";
		
		// 변수 초기화
		try {
			requestVO	= new AuthorizeNSViewRequestVO(szParam);
			saId		= requestVO.getSaId();
			stbMac		= requestVO.getStbMac();
		} catch (Exception e) {
			resultVO.setResultCode("31000000");
			IMCSLog imcsLog = new IMCSLog(imcsLogger, saId, stbMac, pId);
			
			// 인입 로그
			String szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID179) + "] sts[start] rcv[" + szParam + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			String msg = " svc[" + ImcsConstants.API_PRO_ID179 + "] sts[error] msg["+ImcsConstants.RCV_MSG1 +"]";
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
			String szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID179) + "] sts[start] rcv[" + szParam + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			requestVO.setPid(pId);
			resultVO	= this.authorizeNSViewService.authorizeNSView(requestVO);
		} catch(ImcsException ie) {
			if( resultVO == null )		resultVO = new AuthorizeNSViewResultVO();
		} catch(Exception e) {
			if( resultVO == null )		resultVO = new AuthorizeNSViewResultVO();
		} finally {
			// 결과 로그 출력
			long tp_end = System.currentTimeMillis();
			imcsLog.timeLog("[" + methodName + "] tx_time", String.valueOf(tp_end - tp_start), methodName, methodLine);
			
			IMCSLog statLog = new IMCSLog(statLogger);
			
			String szGenre	= StringUtil.replaceNull(requestVO.getGenreInfo(), "||");
			statVO.setPrintType("2");
			statVO.setProductId(resultVO.getProductId());
			statVO.setProductName(resultVO.getProductName());
			statVO.setProductPrice(resultVO.getProductPrice());
			statVO.setContentsId(requestVO.getAlbumId());
			statVO.setContentsName(resultVO.getContentsName());
			statVO.setCatId(resultVO.getCatId());
			statVO.setCatName(resultVO.getCatName());
			statVO.setScatId(resultVO.getScatId());
			statVO.setScatName(resultVO.getScatName());
			statVO.setSvcName(requestVO.getAppType());
			statVO.setApiType("V");
			statVO.setViewType(requestVO.getViewType());
			statVO.setGenreLarge(szGenre.substring(0, szGenre.indexOf("|")));
			statVO.setGenreMid(szGenre.substring(szGenre.indexOf("|") + 1, szGenre.indexOf("|", szGenre.indexOf("|") + 1)));
			
			statVO.setResultCode(StringUtil.replaceNull(resultVO.getResultCode(), statVO.getResultCode()));
			statLog.statLog(statVO.toString());
		}
		
		return resultVO;
	}
}
