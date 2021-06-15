package kr.co.wincom.imcs.api.buyNSDMConts;

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
public class BuyNSDMContsController {
	private Log imcsLogger		= LogFactory.getLog("API_Common_buyNSDMConts");
	private Log statLogger		= LogFactory.getLog("TLO_buyNSDMConts");

	@Autowired
	private BuyNSDMContsService		buyNSDMContsService;
	
	//protected static String saId	= "";
	//protected static String stbMac	= "";

	public Object buyNSDMConts(HttpServletRequest request, HttpServletResponse response, String szParam, String szStat) throws Exception {
		// 프로세스 정보
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String pName	= ManagementFactory.getRuntimeMXBean().getName();		// 프로세스네임 
		String pId		= String.valueOf(Thread.currentThread().getId());
		String methodName	= oStackTrace.getMethodName();						// 메소드명
		String methodLine	= String.valueOf(oStackTrace.getLineNumber()-3);	// 메소드라인
		
		BuyNSDMContsResultVO resultVO		= new BuyNSDMContsResultVO();
		BuyNSDMContsRequestVO requestVO	= null;
		String saId		= "";
		String stbMac	= "";
		
		// 변수 초기화
		try {
			requestVO	= new BuyNSDMContsRequestVO(szParam);
			saId		= requestVO.getSaId();
			stbMac		= requestVO.getStbMac();
		} catch (Exception e) {
			resultVO.setResultCode("31000000");
			IMCSLog imcsLog = new IMCSLog(imcsLogger, saId, stbMac, pId);
			
			// 인입 로그
			String szMsg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] sts[start] rcv[" + szParam + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			String msg = " svc[" + ImcsConstants.NSAPI_PRO_ID014 + "] sts[error] msg["+ImcsConstants.RCV_MSG1 +"]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			resultVO.setFlag("1");
			resultVO.setErrMsg("정의 되지 않은 구매 타입입니다."); 
			
			return resultVO;
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
			String szMsg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID014) + "] sts[start] rcv[" + szParam + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			requestVO.setPid(pId);
			resultVO	= buyNSDMContsService.buyNSDMConts(requestVO);
		} catch(ImcsException ie) {
			if( ie.getList() != null )	resultVO = (BuyNSDMContsResultVO) ie.getList();
			else						resultVO = new BuyNSDMContsResultVO();
			
			resultVO.setFlag(ie.getFlag());
			resultVO.setErrCode(ie.getErrorCode());
			resultVO.setErrMsg(ie.getMessage());
		} catch(Exception e) {
			if( resultVO == null )		resultVO = new BuyNSDMContsResultVO();
			resultVO.setFlag("1");
			resultVO.setErrCode("99");
			resultVO.setErrMsg("incorrect failed !!!|"); 
		} finally {
			// 결과 로그 출력
			long tp_end = System.currentTimeMillis();
			imcsLog.timeLog("[" + methodName + "] tx_time", String.valueOf(tp_end - tp_start), methodName, methodLine);

			IMCSLog statLog = new IMCSLog(statLogger);
			statVO.setResultCode(StringUtil.replaceNull(resultVO.getResultCode(), statVO.getResultCode()));
			statVO.setContentsId(requestVO.getAlbumId());
			statVO.setContentsName(requestVO.getAlbumName());
			statVO.setProductId(resultVO.getProductId());
			statVO.setProductName(resultVO.getProductName());
			statVO.setProductPrice(resultVO.getProductPrice());
			statVO.setBuyingType(requestVO.getBuyingType());
			statVO.setBuyAmt(requestVO.getSuggestedPrice());
			statVO.setAlwncecharge(requestVO.getAlwnceCharge());
			statVO.setBuyingGb(requestVO.getBuyingGb());
			statVO.setBalace(requestVO.getBuyingPrice());
			
			switch (requestVO.getBuyTypeFlag()) {
			case "2":
			case "3":
				statVO.setDfBuyYn("Y");
				statVO.setDfPrice(requestVO.getSuggestedDatafreePrice());
				statVO.setDfBalace(requestVO.getDatafreeBuyPrice());
				break;

			case "1":
			default:
				statVO.setDfBuyYn("N");
				break;
			}
			
			statLog.statLog(statVO.toString());
		}
		
		return resultVO;
	}
}
