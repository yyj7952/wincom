package kr.co.wincom.imcs.api.getNSPSI;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.FileUtil;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetNSPSIServiceImpl implements GetNSPSIService {
	private Log imcsLogger = LogFactory.getLog("API_getNSPSI");
	
	@Autowired
	private GetNSPSIDao getNSPSIDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSPSI(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public GetNSPSIResultVO getNSPSI(GetNSPSIRequestVO paramVO){
//		this.getNSPSI(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		String msg	= "";
		
		GetNSPSIResultVO resultListVO = new GetNSPSIResultVO();
		GetNSPSIResponseVO tempVO = new GetNSPSIResponseVO();
		List<GetNSPSIResponseVO> resultVO = new ArrayList<GetNSPSIResponseVO>();
		
		String LOCKFILE = "";
		String RESFILE	= "";
		
		String LOCALPATH = "";
		
		LOCALPATH = commonService.getCachePath("LOCAL", ImcsConstants.API_PRO_ID001.split("/")[1], imcsLog);
		
		int nMainCnt = 0;
		int nWaitCnt = 0;
		
	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
		long tp2 = 0;
		long tp3 = 0;
	    
		List<StillImageVO> lstImageInfo	= null;		// ????????? ????????? ?????? VO
		StillImageVO imageVO = null;
		GetNSPSIResponseVO lstPrinfo	= null;		// ?????????????????? VO
		
		try{
						
			//????????? ????????? ?????? ??????
			this.getTestSbc(paramVO);
			
			tp1	= System.currentTimeMillis();
	    	
			imcsLog.timeLog("?????? STB?????? ??????", String.valueOf((tp1 - tp_start)), methodName, methodLine);
			
			String szThmImgSvrIp	= "";		// ????????? ????????? ?????? IP
			
		    		    		    
		    try {
		    	szThmImgSvrIp	= commonService.getIpInfo("snap_server", ImcsConstants.API_PRO_ID001.split("/")[1]);		// ????????? ????????? ??????
		    	
			} catch (Exception e) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID001, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
				paramVO.setResultCode("31000000");
				
				throw new ImcsException();
			}
			
			//stamp_today	// ????????????
		    //c_current		// ????????????
		    						
			LOCKFILE	= LOCALPATH + "/getNSPSI_t"+paramVO.getTestSbc()+"_n"+paramVO.getNscType()+"_y"+paramVO.getYouthYn()+"_t"+paramVO.getTcOut()+".lock";
			RESFILE		= LOCALPATH + "/getNSPSI_t"+paramVO.getTestSbc()+"_n"+paramVO.getNscType()+"_y"+paramVO.getYouthYn()+"_t"+paramVO.getTcOut()+".res";
			
			File res = new File(RESFILE);
			
			// ????????? ???????????? ?????? ????????? lock ?????? ?????? ??????
			if(res.exists()) {
				String result = FileUtil.fileRead(RESFILE, "UTF-8");
				
				if(!"".equals(result)) {
					msg = " File [" + RESFILE + "] rcvbuf... [" + res.length() + "] bytes ";
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
				
				resultListVO.setResult(result);
				FileUtil.unlock(LOCKFILE, imcsLog);
				return resultListVO;		
			} else {
				msg = " File [" + RESFILE + "] open Failed";
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				while(FileUtil.lock(LOCKFILE, imcsLog)){
					Thread.sleep(1000);
					nWaitCnt++;
					
					msg = " queryWaitCheck Sleep [" + nWaitCnt + "] sec";
					imcsLog.serviceLog(msg, methodName, methodLine);
	
					if(nWaitCnt >= 5){
						msg = " wait_count overload Failed svc2[" + ImcsConstants.API_PRO_ID001 + "] sts[    0] msg["+ 
								String.format("%-17s", "par_yn:" + ImcsConstants.RCV_MSG2 +"]");
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						paramVO.setResultCode("21000000");
						throw new ImcsException();
					}
					
					if(res.exists()) {
						String result = FileUtil.fileRead(RESFILE, "UTF-8");
						
						if(!"".equals(result)) {
							msg = " File [" + RESFILE + "] rcvbuf... [" + res.length() + "] bytes ";
							imcsLog.serviceLog(msg, methodName, methodLine);
						}
						
						resultListVO.setResult(result);
						FileUtil.unlock(LOCKFILE, imcsLog);
						return resultListVO;
					}
				}
			}
			
			//Nscreen EPG?????? ??????????????? ??????
			resultVO = this.getNSPSIList(paramVO);

			if(resultVO != null && !resultVO.isEmpty()){
				nMainCnt = resultVO.size();
			}
			
			tp2	= System.currentTimeMillis();
	    	
			imcsLog.timeLog("????????? ??????", String.valueOf((tp2 - tp1)), methodName, methodLine);
			
			for(int i = 0; i < nMainCnt; i++) {
				tempVO = resultVO.get(i);
				
				tempVO.setImgUrl(szThmImgSvrIp);
				
				
				if("N".equals(tempVO.getImageYn())){
					tempVO.setImgUrl("");
					tempVO.setImgFileName("");
				}
				
				resultVO.set(i, tempVO);
			}
			
			tp3	= System.currentTimeMillis();
	    	
			imcsLog.timeLog("????????? ?????? Fetch", String.valueOf((tp3 - tp2)), methodName, methodLine);
			resultListVO.setList(resultVO);

			
			// ?????? ??????
			int nRetVal = FileUtil.fileWrite(RESFILE, resultListVO.toString(), false);
			
			if(nRetVal == 1) {
				msg = "File [" + RESFILE + "] WRITE [" + res.length() + "] bytes Finished";
				imcsLog.serviceLog(msg, methodName, methodLine);
			} else {
				msg = "File [" + RESFILE + "] WRITE failed";
				imcsLog.serviceLog(msg, methodName, methodLine);		
			}
			
			FileUtil.unlock(LOCKFILE, imcsLog);
			
			res = new File(RESFILE);
			
			// ????????? ???????????? ?????? ????????? lock ?????? ?????? ??????
			if(res.exists()) {
				String result = FileUtil.fileRead(RESFILE, "UTF-8");
				
				if(!"".equals(result)) {
					msg = " File [" + RESFILE + "] rcvbuf... [" + res.length() + "] bytes ";
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
				
				resultListVO.setResult(result);
				FileUtil.unlock(LOCKFILE, imcsLog);
				return resultListVO;		
			} else {
				msg = " File [" + RESFILE + "] open Failed";
				imcsLog.serviceLog(msg, methodName, methodLine);
			}
			
		}catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog( methodName + "-E" , ie.getClass().getName() + ":" +  ie.getMessage());
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog( methodName + "-E" , e.getClass().getName() + ":" +  e.getMessage());
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID001) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
			
		}
		
		return resultListVO;
	}
	
	
	
	/**
	 * ????????? ????????? ?????? ??????
	 * @param paramVO
	 * @return
	 */
    public void getTestSbc(GetNSPSIRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
		List<String> list   = null;
				
		try {
						
			list = getNSPSIDao.getTestSbc(paramVO);
			
			if( list == null || list.isEmpty()) {
				paramVO.setTestSbc("N");				
			} else {
				paramVO.setTestSbc(list.get(0));
			}
						
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException();
		}
    }
    
    
    
    /**
	 * Nscreen ???????????? EPG?????? ??????????????? ??????
	 * @param paramVO
	 * @return
	 */
    public List<GetNSPSIResponseVO> getNSPSIList(GetNSPSIRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();

		List<GetNSPSIResponseVO> rtnList = null;
		try {
			
			if("A".equals(paramVO.getTcIn()) && "A".equals(paramVO.getTcOut())) {			// ??????????????? ??????/?????? ?????? (24??????)
				rtnList = getNSPSIDao.getNSPSIList1(paramVO);		// NO_PAGING (5????????? ???????????? ?????? ??????)
			} else {
				rtnList = getNSPSIDao.getNSPSIList2(paramVO);
			}
			
		} catch (Exception e) {
			//imcsLog.failLog(ImcsConstants.API_PRO_ID001, "", null, "favor_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
			paramVO.setResultCode("40000000");
			
			//imcsLog.errorLog(ImcsConstants.API_PRO_ID001, "ss", "aa", "favor_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
			
			throw new ImcsException();
		}
		
		if(rtnList == null || rtnList.isEmpty()){
			//imcsLog.failLog(ImcsConstants.API_PRO_ID001, "", null, "favor_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			paramVO.setResultCode("21000000");
		}
		
    	return rtnList;
    }
    
}
