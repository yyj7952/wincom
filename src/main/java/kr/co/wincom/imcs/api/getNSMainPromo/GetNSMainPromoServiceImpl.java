package kr.co.wincom.imcs.api.getNSMainPromo;

import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetNSMainPromoServiceImpl implements GetNSMainPromoService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSMainPromo");
	
	@Autowired
	private GetNSMainPromoDao getNSMainPromoDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSMainPromo(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	/**
	 * @author HONG
	 * @since 2016-07-12
	 * 컨텐츠 타입과 구매여부, 구매시간, 구매타입 값을 리턴 (lgvod997.pc)
	 */
	@Override
	public GetNSMainPromoResultVO getNSMainPromo(GetNSMainPromoRequestVO paramVO)	{
//		this.getNSMainPromo(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		List<GetNSMainPromoResponseVO> resultVO	= new ArrayList<GetNSMainPromoResponseVO>();		
		GetNSMainPromoResponseVO tempVO			= new GetNSMainPromoResponseVO();
		GetNSMainPromoResultVO	resultListVO	= new GetNSMainPromoResultVO();
		
		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2
		
		String msg			= "";
		
				
		try {
			
			tp1 = System.currentTimeMillis();
			
			try {
				resultVO = getNSMainPromoDao.getMainPromo(paramVO);
			} catch (Exception e) {
				msg = " svc3[" + String.format("%-20s", ImcsConstants.API_PRO_ID269) + "] sts[0] msg[" + String.format("%-21s", "cate_info:" + ImcsConstants.RCV_MSG2 + "]");
				imcsLog.serviceLog(msg, methodName, methodLine);
			}
			
			
			if(resultVO.size() == 0 ){
				msg = " svc4[" + String.format("%-20s", ImcsConstants.API_PRO_ID269) + "] sts[0] msg[" + String.format("%-21s", "cate_info:" + ImcsConstants.RCV_MSG3 + "]");
				imcsLog.serviceLog(msg, methodName, methodLine);
			}
			
			List<String> notiVO	= new ArrayList<String>();
			String noti = "";
			
			try {
				notiVO = getNSMainPromoDao.getMessage(paramVO);
			} catch (Exception e) {
				msg = " svc3[" + String.format("%-20s", ImcsConstants.API_PRO_ID269) + "] sts[0] msg[" + String.format("%-21s", "noti_info:" + ImcsConstants.RCV_MSG2 + "]");
				imcsLog.serviceLog(msg, methodName, methodLine);
			}
			
			for(int i=0; i<notiVO.size(); i++){
				
				if(i == 0){
					noti = notiVO.get(i);
				}else{
					noti = noti + "\b" + notiVO.get(i);
				}
				
			}					
			
			resultListVO.setList(resultVO);
			resultListVO.setNotiMsg(noti);
			
			
		} catch(ImcsException ie) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());

			// TLO Log 를 Controller Layer 로 전달하기 위해 Setting 한다. (TLO Log 세부값은 finally 문에서 설정된다.)
			throw ie;
		} catch(Exception e) {
			isLastProcess	= ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			
			// TLO Log 를 Controller Layer 로 전달하기 위해 ImcsException 으로 변환한다. (TLO Log 세부값은 finally 문에서 설정된다.)
			throw new ImcsException();
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
		}
		
		return resultListVO;
	}
	}
