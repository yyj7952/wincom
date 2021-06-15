package kr.co.wincom.imcs.api.getNSAlbumList;

import java.lang.management.ManagementFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.vo.StatVO;
import kr.co.wincom.imcs.handler.ImcsException;

@Controller
public class GetNSAlbumListController {
	private Log imcsLogger		= LogFactory.getLog("API_Common_getNSAlbumList");
	private Log statLogger		= LogFactory.getLog("TLO_getNSAlbumList");

	@Autowired
	private GetNSAlbumListService getNSAlbumListService;
	
	//protected static String saId	= "";
	//protected static String stbMac	= "";

	/**
	 * 앨범받기목록 가져오기 (lgvod949.pc)
	 * URL 형태 - servlets/CommSvl_MMI 
	 */
	public Object getNSAlbumList(HttpServletRequest request, HttpServletResponse response, String szParam, String szStat) throws Exception {
		// 프로세스 정보
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String pName	= ManagementFactory.getRuntimeMXBean().getName();		// 프로세스네임 
		String pId		= String.valueOf(Thread.currentThread().getId());	
		String methodName	= oStackTrace.getMethodName();						// 메소드명
		String methodLine	= String.valueOf(oStackTrace.getLineNumber()-3);	// 메소드라인
		
		GetNSAlbumListResultVO resultListVO = new GetNSAlbumListResultVO();
		GetNSAlbumListRequestVO requestVO	= null;
		String saId		= "";
		String stbMac	= "";
		
		long tp_start = System.currentTimeMillis();
		
		// 변수 초기화
		try {
			requestVO	= new GetNSAlbumListRequestVO(szParam);
			saId		= requestVO.getSaId();
			stbMac		= requestVO.getStbMac();
		} catch (Exception e) {
			resultListVO.setResultCode("31000000");
			IMCSLog imcsLog = new IMCSLog(imcsLogger, saId, stbMac, pId);
			
			// 인입 로그
			String szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID949) + "] sts[start] rcv[" + szParam + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
						
			String msg = " svc[" + ImcsConstants.API_PRO_ID949 + "] sts[error] msg["+ImcsConstants.RCV_MSG1 +"]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			return "";
			//throw new ImcsException();
		}
		
		// 통계로그 정보
		StatVO statVO	= null;
		try {
			statVO	= new StatVO(szStat);
			
			if(szStat == null || "".equals(szStat)){
				requestVO.setResultCode("30000000");
			}
			
			statVO.setApiNm(methodName);
			statVO.setSaId(saId);
			statVO.setStbMac(stbMac);
			statVO.setCatId(requestVO.getCatId());
		} catch (Exception e) {
			resultListVO.setResultCode("31000000");
			throw new ImcsException();
		}
		
		// 서비스로그 초기화
		IMCSLog imcsLog = new IMCSLog(imcsLogger, saId, stbMac, pId);
		
		
		try {
			// 인입 로그
			String szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID949) + "] sts[start] rcv[" + szParam + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			requestVO.setPid(pId);
			resultListVO	= this.getNSAlbumListService.getNSAlbumList(requestVO);

		} catch(ImcsException ie) {
			if( resultListVO == null ){
				resultListVO = new GetNSAlbumListResultVO();
			}
		} catch(Exception e) {
			if( resultListVO == null ){
				resultListVO = new GetNSAlbumListResultVO();
			}
		} finally {
			// 결과 로그 출력
			long tp_end = System.currentTimeMillis();
			imcsLog.timeLog("[" + methodName + "] tx_time", String.valueOf(tp_end - tp_start), methodName, methodLine);

			IMCSLog statLog = new IMCSLog(statLogger);
			statVO.setResultCode(resultListVO.getResultCode());
			//statVO.setContentsId(resultListVO.getContentsIdSt());
			//statVO.setContentsName(resultListVO.getContentsNameSt());
						
			statLog.statLog(statVO.toString());
		}
		
		return resultListVO;
	}
}
