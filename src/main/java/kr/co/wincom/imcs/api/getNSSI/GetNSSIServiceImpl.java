package kr.co.wincom.imcs.api.getNSSI;

import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComNodeVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetNSSIServiceImpl implements GetNSSIService {
	private Log imcsLogger = LogFactory.getLog("API_getNSSI");
	
	@Autowired
	private GetNSSIDao getNSSIDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSSI(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	int iPos = 1;
	int iMaxPos = 1;
	
	@Override
	public GetNSSIResultVO getNSSI(GetNSSIRequestVO paramVO){
//		this.getNSSI(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		String msg	= "";
		
		GetNSSIResultVO resultListVO = new GetNSSIResultVO();
		GetNSSIResponseVO tempVO = new GetNSSIResponseVO();
		List<GetNSSIResponseVO> resultVO = new ArrayList<GetNSSIResponseVO>();
		List<GetNSSIResponseVO> returnVO = new ArrayList<GetNSSIResponseVO>();
		
		int nMainCnt = 0;

	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
		long tp2 = 0;
		long tp3 = 0;
	    
		List<ComNodeVO> lstNodeInfo = null;		
		ComNodeVO nodeVO = null;
		
		String szChnlImgSvrip = "";
		
		try {
			szChnlImgSvrip	= commonService.getIpInfo("img_chnl_server", ImcsConstants.API_PRO_ID002.split("/")[1]);
		} catch(Exception e) {
			//imcsLog.failLog(ImcsConstants.API_PRO_ID002, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
			throw new ImcsException();
		}
		
		
		try{
			
			// 노드정보 조회
			paramVO.setBaseCondi(paramVO.getBaseCd());
			lstNodeInfo = this.getNode(paramVO);
			
			if(lstNodeInfo != null && !lstNodeInfo.isEmpty()){
				nodeVO = lstNodeInfo.get(0);
				
				paramVO.setNodeCd(nodeVO.getNodeCd());
				paramVO.setrBaseCode(nodeVO.getrBaseCode());
			}
			
			if(lstNodeInfo == null || lstNodeInfo.isEmpty()){
				paramVO.setBaseCondi(paramVO.getBaseOneCd());
				
				lstNodeInfo = this.getNode(paramVO);
				
				if(lstNodeInfo != null && !lstNodeInfo.isEmpty()){
					nodeVO = lstNodeInfo.get(0);
					
					paramVO.setNodeCd(nodeVO.getNodeCd());
					paramVO.setrBaseCode(nodeVO.getrBaseCode());
				}
			}
				
			if(lstNodeInfo == null || lstNodeInfo.isEmpty()){
				paramVO.setBaseCondi("1234567890");

				lstNodeInfo = this.getNode(paramVO);
				
				if(lstNodeInfo != null && !lstNodeInfo.isEmpty()){
					nodeVO = lstNodeInfo.get(0);
					
					paramVO.setNodeCd(nodeVO.getNodeCd());
					paramVO.setrBaseCode(nodeVO.getrBaseCode());
				}
			}
			
			// 동 여부 조회
			String szDongYn = this.getDongYn(paramVO);
			
			if(szDongYn != null && !"".equals(szDongYn))	paramVO.setDongYn("Y");
			else											paramVO.setDongYn("N");
			
			// 테스트 가입자 여부 조회
			this.getTestSbc(paramVO);
			
			tp1	= System.currentTimeMillis();	    	
			imcsLog.timeLog("검수 STB여부 조회", String.valueOf((tp1 - tp_start)), methodName, methodLine);
			
			msg = " [파라미터확인] c_test_sbc["+paramVO.getTestSbc()+"] c_nsc_type["+paramVO.getNscType()+"] c_base_gb["+paramVO.getBaseGb()+"] c_dong_yn["+paramVO.getDongYn()+"]  "
					+ " c_base_code["+paramVO.getBaseCondi()+"] c_node_cd["+paramVO.getNodeCd()+"]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			// EPG 전체 채널정보 조회
			nMainCnt = 0;
			
			
			try {
				if("H".equals(paramVO.getOrderGb())) {					// 인기순으로  Nscreen EPG전체 채널정보 조회
					resultVO = getNSSIDao.getNSSIListH(paramVO);				
				}else{			// 기본으로  Nscreen EPG전체 채널정보 조회				
					resultVO = getNSSIDao.getNSSIListN(paramVO);				
				}
				
				if(resultVO != null)	nMainCnt = resultVO.size();
			} catch (Exception e) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID002, "", null, "favor_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
				paramVO.setResultCode("40000000");

				throw new ImcsException();
			}
			
			if(resultVO == null || resultVO.isEmpty()){
				//imcsLog.failLog(ImcsConstants.API_PRO_ID002, "", null, "favor_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				paramVO.setResultCode("40000000");
			}
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("채널 조회", String.valueOf((tp2 - tp1)), methodName, methodLine);
			
			
			for(int i = 0; i < nMainCnt; i++){
				tempVO = resultVO.get(i);
				
				if(!"http:".equals(tempVO.getImgUrl())){
					tempVO.setImgUrl(szChnlImgSvrip);
				}
				
				if( "N".equals(paramVO.getTestSbc()) && "999".equals(tempVO.getSortNo()) ){
					continue;
				}
				
				if( "PAD".equals(paramVO.getNscType()) ){
					if( "1".equals(tempVO.getFilteringCode()) || "3".equals(tempVO.getFilteringCode()) || "5".equals(tempVO.getFilteringCode()) || "7".equals(tempVO.getFilteringCode())  ){
						continue;
					}
				}else{
					if( "8".equals(tempVO.getFilteringCode()) || "10".equals(tempVO.getFilteringCode())
							|| "12".equals(tempVO.getFilteringCode()) || "13".equals(tempVO.getFilteringCode()) || "14".equals(tempVO.getFilteringCode()) ){
						continue;
					}
				}			
				
				tempVO.setLiveServer1("http://" + tempVO.getLiveIp1() + ":" + tempVO.getLivePort() + "/");
				tempVO.setLiveServer2("http://" + tempVO.getLiveIp2() + ":" + tempVO.getLivePort() + "/");
				tempVO.setLiveServer3("http://" + tempVO.getLiveIp3() + ":" + tempVO.getLivePort() + "/");
				tempVO.setLiveServer4("http://" + tempVO.getLiveIp4() + ":" + tempVO.getLivePort() + "/");
				tempVO.setLiveServer5("http://" + tempVO.getLiveIp5() + ":" + tempVO.getLivePort() + "/");
				tempVO.setLiveServer6("http://" + tempVO.getLiveIp6() + ":" + tempVO.getLivePort() + "/");
				

				returnVO.add(tempVO);
				
			}
			
			resultListVO.setList(returnVO);
			
			tp3	= System.currentTimeMillis();
			imcsLog.timeLog("채널 정보 Fetch", String.valueOf((tp3 - tp2)), methodName, methodLine);

		} catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID002) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
			
		}
		
		return resultListVO;
	}
	
	
	
	/**
	 * 검수 STB 여부 조회
	 * @param paramVO
	 * @return
	 */
    public void getTestSbc(GetNSSIRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    			
		List<String> list   = new ArrayList<String>();
		
		try {

			list = getNSSIDao.getTestSbc(paramVO);
			
			if( list == null || list.isEmpty()){
				//paramVO.setTestSbc("");				
			} else {
				paramVO.setTestSbc(StringUtil.nullToSpace(list.get(0)));
			}
									
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
    }
        
    
    /**
	 * 노드 정보 조회
	 * @param paramVO
	 * @return
	 */
    public List<ComNodeVO> getNode(GetNSSIRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
		List<ComNodeVO> list   = new ArrayList<ComNodeVO>();
		
		try {
			
			list = getNSSIDao.getNode(paramVO);
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
    	return list;
    }
    
    
    
    /**
	 * 동 여부 조회
	 * @param paramVO
	 * @return
	 */
    public String getDongYn(GetNSSIRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodName = oStackTrace.getMethodName();
    	
    	String szDongYn	= "";
    			
		List<String> list   = new ArrayList<String>();
		
		try {

			list = getNSSIDao.getDongYn(paramVO);
			
			if( list != null && !list.isEmpty()){
				szDongYn = StringUtil.nullToSpace(list.get(0));
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
    	return szDongYn;
    }
    

}
