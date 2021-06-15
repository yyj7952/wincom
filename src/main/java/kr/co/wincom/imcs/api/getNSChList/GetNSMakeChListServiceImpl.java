package kr.co.wincom.imcs.api.getNSChList;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.FileUtil;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComCdVO;
import kr.co.wincom.imcs.common.vo.ComNodeVO;
import kr.co.wincom.imcs.common.vo.ComProdInfoVO;
import kr.co.wincom.imcs.common.vo.M3u8ProfileVO;
import kr.co.wincom.imcs.handler.ImcsException;

@Service
@Async
public class GetNSMakeChListServiceImpl implements GetNSMakeChListService {
//	private Log imcsLogger		= LogFactory.getLog("API_getNSMakeChList");
//	private Log imcsComLogger		= LogFactory.getLog("API_Common_getNSMakeChList");
	private Log imcsLogger		= LogFactory.getLog("API_getNSChList");
	private Log imcsComLogger		= LogFactory.getLog("API_Common_getNSChList");
	
	@Autowired
	private GetNSChListDao getNSChListDao;
	
	@Autowired
	private CommonService commonService;
	
//	@Autowired
//	private NosqlResultCache cache;
//	@Resource
//	public void setNosqlResultCache (NosqlResultCache nosqlResultCache) {
//		this.cache = nosqlResultCache;
//	}
	
//	public void getNSMakeChLists(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//		this.imcsMkLog	= new IMCSLog(imcsComLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
//	private IMCSLog imcsMkLog = null;
	
	private String apiInfo	= "";
	
	int iPos = 1;
	int iMaxPos = 1;
	//2018.06.08 - TV앱의 경우 메인채널과 , MPTS 채널의 경우 3번째 m3u8파일명을 4번째에 동일하게 넣어달라는 요청으로 인한 처리
	//	   조건 : TV앱이고, 4번째 m3u8파일이 없어야 하며, 3번째 m3u8이 존재하면, 3번째 m3u8파일명을 4번째 자리에 동일하게 채워준다.
	//위와 같은 조건을 하기 위해 만든 변수
	int tvapp_flag = 0;
	String tvapp_castis_m3u8 = "";
	String tvapp_onnuri_m3u8 = "";
	String tvapp_m3u8_info = "";
	
	@Override
	public GetNSChListResultVO getNSMakeChLists(GetNSChListRequestVO paramVO)	{
//		this.getNSMakeChLists(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
			
		
		GetNSChListResultVO	resultListVO	= new GetNSChListResultVO();
		this.apiInfo	= ImcsConstants.NSAPI_PRO_ID020_w;
		
		resultListVO	= this.getNSMakeChList(paramVO, apiInfo);
		return resultListVO;
	}
	
	
	// getNSLists에서도 사용하기 위하여 모듈화
	public GetNSChListResultVO getNSMakeChList(GetNSChListRequestVO paramVO, String apiInfo)	{
		
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		IMCSLog imcsMkLog	= new IMCSLog(imcsComLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		
		imcsLog.debugLog(methodName + " service call");
		
		int nMainCnt = 0;
		int nSubCnt = 0;
		int nSbdelay_cnt = 0;
		
		GetNSChListResultVO resultListVO = new GetNSChListResultVO();
		GetNSChListResponseVO tempVO = new GetNSChListResponseVO();
		List<GetNSChListResponseVO> resultVO = new ArrayList<GetNSChListResponseVO>();
		List<GetNSChListResponseVO> returnVO = new ArrayList<GetNSChListResponseVO>();
		HashMap<String, String> cjchnlurl = new HashMap<String, String>();
		
		List<ComProdInfoVO> lstProdInfo = null;
		List<ComNodeVO> lstNodeInfo = null;
		List<ComCdVO> lstLiveTimeSvrInfo = null;
		List<ConcertInfoVO> lstConsertInfo = new ArrayList<ConcertInfoVO>();
		
		ComProdInfoVO prodInfoVO = null;
		ComNodeVO nodeVO = null;
		ComCdVO liveTimeSvrVO = null;
		ConcertInfoVO consertVO = null;
		
		
		
		String szChnlImgSvrip = "img_chnl_server";
		String VirtualChFlag  = "";		// 가상채널 여부
		VirtualChFlag	= commonService.getVCFlag(ImcsConstants.NSAPI_PRO_ID020.split("/")[1]);	// 가상채널 여부
		
		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2
		long tp3			= 0;		// timePoint 3


		String msg			= "";
		
		long tp_start = System.currentTimeMillis();
		
		msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID020_w) + "] sts[start] rcv[" + paramVO.getParam()+ "]";
		imcsLog.serviceLog(msg, methodName, methodLine);
		
		if(paramVO.getmProdId().equals("") || paramVO.getmProdId().isEmpty())
		{
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID020_w) + "] msg[정상적인 INPUT PARAM이 아닙니다.]";
			imcsLog.serviceLog(msg, methodName, methodLine);
			return resultListVO;
		}
		
		String vch_sql = "";
		if (!paramVO.getHdtvViewGb().equals("R") && !paramVO.getHdtvViewGb().equals("M") && !paramVO.getHdtvViewGb().equals("G")) {
			vch_sql = "AND 1=2";
		} else {
			if (VirtualChFlag.equals("0")) {
				if (paramVO.getHdtvViewGb().equals("R")) {
					vch_sql = "AND A.VIRTUAL_TYPE = 'VOD'";
				} else if (paramVO.getHdtvViewGb().equals("G")) {
					vch_sql = "AND A.VIRTUAL_TYPE = 'GLF'";
				} else {
					vch_sql = "AND A.VIRTUAL_TYPE = 'MSC'";
				}
			}
		}
		
		try {
			// 파일명 관련 파라미터
			String RESFILE		= "";
			String NASFILE		= "";
			String LOCKFILE		= "";
			String LOCALLOCKFILE		= "";
			
			
			File fNASFILE	= null;
			File fRESFILE	= null;
			
			File fLOCKFILE	= null;
			File fLOCALLOCKFILE	= null;
			
			String szMsg			= "";
			
			String current_time = commonService.getSysdate();
			
			String LOCALPATH = "";
			
			//2018.08.23 폴더로 나누는 방법으로 변경
			String foldering_dir = String.format("p%sv%sf%sn%s", paramVO.getPooqYn(), paramVO.getHdtvViewGb(), paramVO.getFiveChYn(), paramVO.getSvcNode());
			
			LOCALPATH = commonService.getCachePath("LOCAL", ImcsConstants.NSAPI_PRO_ID020.split("/")[1], imcsLog);
			LOCALPATH = String.format("%s/%s", LOCALPATH, foldering_dir);
			
			szMsg = " LOCALPATH : " + LOCALPATH;
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			String NASPATH = "";
			
			NASPATH = commonService.getCachePath("NAS", ImcsConstants.NSAPI_PRO_ID020.split("/")[1], imcsLog);
			NASPATH = String.format("%s/%s", NASPATH, foldering_dir);
			
			szMsg = " NASPATH : " + NASPATH;
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			File NAS_DIR = new File(NASPATH);
			if(!NAS_DIR.exists()){
				NAS_DIR.mkdir();
			}
			
			if(paramVO.getBaseCd().length() > 1){
				paramVO.setBaseOneCd(paramVO.getBaseCd().substring(0, 1));
			}
			
			// 이미지 서버IP조회
			tp1 = System.currentTimeMillis();
		
			// 검수 STB여부 조회		
			// getNSLists 에서 해당 로직 진행하므로 makelist에서는 할 필요가 없음
			/*String szTestSbc	= "";
			szTestSbc	= this.getTestSbc(paramVO);
			paramVO.setTestSbc(szTestSbc);
			
			// 카테고리 상세 정보 조회
			this.getCateInfo(paramVO);*/
			
			//tp2	= System.currentTimeMillis();
			//imcsLog.timeLog("검수 STB여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			
			String compFileName = "_t" + paramVO.getTestSbc()    + "_p"+paramVO.getPooqYn()   + "_m" + paramVO.getmProdId()
            +"_v" + paramVO.getHdtvViewGb() + "_f"+paramVO.getFiveChYn();
			
			szMsg = " compFileName : " + compFileName;
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			String chkFileName = "";
			
			File[] files = NAS_DIR.listFiles();
			//File[] files = new File(NASPATH).listFiles();
			
			List<String> list = new ArrayList<String>();
			
			try{
				if(files.length > 0){	
					
					for(int i = 0; i < files.length; i++){
						if(files[i].getName().toString().indexOf(compFileName) > -1){
							list.add(files[i].getName());
						}
					}
					
					if(list != null && list.size() > 0){
//						List<String> list2 = new ArrayList<String>(list);
//						Collections.reverse(list2);
//						
//						chkFileName = list2.get(0);
						
						Collections.sort(list, new Comparator<String>(){
							public int compare(String obj1, String obj2){
								return obj1.compareToIgnoreCase(obj2);
							}
						});
						
						Collections.reverse(list);
						
						chkFileName = list.get(0);
					}else{
						chkFileName = "2";
					}
				}else{
					
					chkFileName = "2";
				}
			}catch(NullPointerException e)
			{
				szMsg = " getNSMakeChList Cache File Empty";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}
			
			
//			if(list.size() > 0 && list.size() <= 4){
//				chkFileName = list.get(0);
//			}
			
			
			if(!commonService.chkCacheFile(chkFileName, imcsLog)){
				szMsg = " POINT";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
				String cacheFileName = "";
				String[] arrlockFileName = list.get(0).split("\\.");
				
				for(int i = 0; i < arrlockFileName.length -1; i++){
					cacheFileName += arrlockFileName[i];
				}
				
				//String[] cacheFileName = list.get(0).split("\\.");
				if(list.get(0).indexOf(".lock") > -1){					
					LOCKFILE = NASPATH + "/" + list.get(0);
					LOCALLOCKFILE = LOCALPATH + "/" + list.get(0);
					NASFILE = NASPATH + "/" + cacheFileName + ".res";
					RESFILE = LOCALPATH + "/" + cacheFileName + ".res";
				}else{					
					LOCKFILE = NASPATH + "/" + cacheFileName + ".lock";
					LOCALLOCKFILE = LOCALPATH + "/" + cacheFileName + ".lock";
					NASFILE = NASPATH + "/" + list.get(0);
					RESFILE = LOCALPATH + "/" + list.get(0);
				}
				
				
				
				if(NAS_DIR.exists()){
					fRESFILE	= new File(RESFILE);
					fLOCALLOCKFILE   = new File(LOCALLOCKFILE);
					fNASFILE	= new File(NASFILE);
					fLOCKFILE   = new File(LOCKFILE);
					
					if(fNASFILE.exists()) {	
						try {
							String[] szCommand = {"/bin/sh", "-c", "cp " + NASFILE + " " + RESFILE};
							Process p = Runtime.getRuntime().exec(szCommand);
							
							szMsg = " File [" + NASFILE + "] copy [" + RESFILE + "]";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
							
							String result = FileUtil.fileRead(NASFILE, "UTF-8");
							resultListVO.setResult(result);
							
							FileUtil.unlock(LOCKFILE, imcsLog);
							
							delCacheFile(LOCALPATH, compFileName, paramVO);
							
							msg = " File [" + RESFILE + "] 리턴케이스4 ";
							imcsLog.serviceLog(msg, methodName, methodLine);

				            return resultListVO;
						} catch(Exception e) {
							szMsg = " cp cache error!!! ";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
						}
					}
					
					
				}
			}else{
				szMsg = " NAS File : NO FILE";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
				
				LOCKFILE = NASPATH + "/getNSChList_" + current_time + compFileName + ".lock";				
				LOCALLOCKFILE = LOCALPATH + "/getNSChList_" + current_time + compFileName + ".lock";				
				NASFILE = NASPATH + "/getNSChList_" + current_time + compFileName + ".res";
				RESFILE = LOCALPATH + "/getNSChList_" + current_time + compFileName + ".res";
			}
			
			fRESFILE	= new File(RESFILE);
			fLOCALLOCKFILE   = new File(LOCALLOCKFILE);
			fNASFILE	= new File(NASFILE);
			fLOCKFILE   = new File(LOCKFILE);
			
			boolean procStatus = false;
			
			if(NAS_DIR.exists()){
				//if (!fNASFILE.exists() && !fLOCKFILE.exists()) {
					
					if(!FileUtil.lock(LOCKFILE, imcsLog)){ //lock 파일이 없는 경우.락 파일 생성
						if(!fLOCKFILE.exists()){
							szMsg = " queryLock Fail, [NAS lock file make fail] " + LOCKFILE + " query execution itself";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
							
							szMsg = " lock File write fail [" + LOCKFILE + "]";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
							
							procStatus = false;
						}
					}else{
						szMsg = " lock File exist [" + LOCKFILE + "]";
						imcsLog.serviceLog(szMsg, methodName, methodLine);	
						procStatus = true;
					}
				//}else {
				//	procStatus = false;
				//}
			}else{
				szMsg = " queryLock Fail, [NAS Directory Not Exist] " + NASPATH + " query execution itself";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
				procStatus = false;
			}
			
			//if (!fRESFILE.exists() && !fLOCALLOCKFILE.exists()) {
				if(!FileUtil.lock(LOCALLOCKFILE, imcsLog)){ //lock 파일이 없는 경우.락 파일 생성
					if(!fLOCALLOCKFILE.exists()){
						szMsg = " lock File write fail [" + LOCALLOCKFILE + "]";
						imcsLog.serviceLog(szMsg, methodName, methodLine);
						
						procStatus = false;
					}else{
						szMsg = " lock File exist [" + LOCALLOCKFILE + "]";
						imcsLog.serviceLog(szMsg, methodName, methodLine);	
						
						procStatus = true;
					}
				}else{
					szMsg = " lock File exist [" + LOCALLOCKFILE + "]";
					imcsLog.serviceLog(szMsg, methodName, methodLine);	
					
					procStatus = false;
				}
				
			//} else {
			//	procStatus = false;
			//}

				if(procStatus){	
					//System.out.println("#################패치 로직으로 들어감!!");
					imcsLog.serviceLog("#################패치 로직으로 들어감!!", methodName, methodLine);

					szMsg = " LOCALLOCKFILE";
					imcsLog.serviceLog(szMsg, methodName, methodLine);

					try {
						VirtualChFlag	= commonService.getVCFlag(ImcsConstants.NSAPI_PRO_ID020.split("/")[1]);	// 가상채널 여부
						paramVO.setVirtualChFlag(VirtualChFlag);
					} catch (Exception e) {
						System.out.println("GetConfigInfo fail");
					}

					String live_time_server1  ="";
					String live_time_server2 = "";
					String live_time_server3 = "";
					String live_time_node1  ="";
					String live_time_node2 = "";
					String live_time_node3 = "";
					String live_time_ipv6_server1  ="";
					String live_time_ipv6_server2 = "";
					String live_time_ipv6_server3 = "";

					// 실시간 서버 조회
					lstLiveTimeSvrInfo = this.getLiveTimeServer(paramVO);

					if(lstLiveTimeSvrInfo != null && lstLiveTimeSvrInfo.size() > 0){
						nMainCnt = lstLiveTimeSvrInfo.size();
					}

					for(int i = 0; i < nMainCnt; i++){
						liveTimeSvrVO = lstLiveTimeSvrInfo.get(i);


						if(paramVO.getSvcNode().equals("R")){
							if("04".equals(liveTimeSvrVO.getComCd())){
								live_time_server1 = liveTimeSvrVO.getComName();
								live_time_node1 = liveTimeSvrVO.getComNodeType();
							}else if("05".equals(liveTimeSvrVO.getComCd())){
								live_time_server2 = liveTimeSvrVO.getComName();
								live_time_node2 = liveTimeSvrVO.getComNodeType();
							}else if("06".equals(liveTimeSvrVO.getComCd())){
								live_time_server3 = liveTimeSvrVO.getComName();
								live_time_node3 = liveTimeSvrVO.getComNodeType();
							}
							
							if("104".equals(liveTimeSvrVO.getComCd())){
								live_time_ipv6_server1 = liveTimeSvrVO.getComName();
							}else if("105".equals(liveTimeSvrVO.getComCd())){
								live_time_ipv6_server2 = liveTimeSvrVO.getComName();
							}else if("106".equals(liveTimeSvrVO.getComCd())){
								live_time_ipv6_server3 = liveTimeSvrVO.getComName();
							}
						} else if(paramVO.getSvcNode().equals("T")){
							if("07".equals(liveTimeSvrVO.getComCd())){
								live_time_server1 = liveTimeSvrVO.getComName();
								live_time_node1 = liveTimeSvrVO.getComNodeType();
							}else if("08".equals(liveTimeSvrVO.getComCd())){
								live_time_server2 = liveTimeSvrVO.getComName();
								live_time_node2 = liveTimeSvrVO.getComNodeType();
							}else if("09".equals(liveTimeSvrVO.getComCd())){
								live_time_server3 = liveTimeSvrVO.getComName();
								live_time_node3 = liveTimeSvrVO.getComNodeType();
							}
							
							if("107".equals(liveTimeSvrVO.getComCd())){
								live_time_ipv6_server1 = liveTimeSvrVO.getComName();
							}else if("108".equals(liveTimeSvrVO.getComCd())){
								live_time_ipv6_server2 = liveTimeSvrVO.getComName();
							}else if("109".equals(liveTimeSvrVO.getComCd())){
								live_time_ipv6_server3 = liveTimeSvrVO.getComName();
							}
						} else if(paramVO.getSvcNode().equals("M")){
							if("10".equals(liveTimeSvrVO.getComCd())){
								live_time_server1 = liveTimeSvrVO.getComName();
								live_time_node1 = liveTimeSvrVO.getComNodeType();
							}else if("11".equals(liveTimeSvrVO.getComCd())){
								live_time_server2 = liveTimeSvrVO.getComName();
								live_time_node2 = liveTimeSvrVO.getComNodeType();
							}else if("12".equals(liveTimeSvrVO.getComCd())){
								live_time_server3 = liveTimeSvrVO.getComName();
								live_time_node3 = liveTimeSvrVO.getComNodeType();
							}
							
							if("110".equals(liveTimeSvrVO.getComCd())){
								live_time_ipv6_server1 = liveTimeSvrVO.getComName();
							}else if("111".equals(liveTimeSvrVO.getComCd())){
								live_time_ipv6_server2 = liveTimeSvrVO.getComName();
							}else if("112".equals(liveTimeSvrVO.getComCd())){
								live_time_ipv6_server3 = liveTimeSvrVO.getComName();
							}
						}else{
							if("01".equals(liveTimeSvrVO.getComCd())){
								live_time_server1 = liveTimeSvrVO.getComName();
								live_time_node1 = liveTimeSvrVO.getComNodeType();
							}else if("02".equals(liveTimeSvrVO.getComCd())){
								live_time_server2 = liveTimeSvrVO.getComName();
								live_time_node2 = liveTimeSvrVO.getComNodeType();
							}else if("03".equals(liveTimeSvrVO.getComCd())){
								live_time_server3 = liveTimeSvrVO.getComName();
								live_time_node3 = liveTimeSvrVO.getComNodeType();
							}
							
							if("101".equals(liveTimeSvrVO.getComCd())){
								live_time_ipv6_server1 = liveTimeSvrVO.getComName();
							}else if("102".equals(liveTimeSvrVO.getComCd())){
								live_time_ipv6_server2 = liveTimeSvrVO.getComName();
							}else if("103".equals(liveTimeSvrVO.getComCd())){
								live_time_ipv6_server3 = liveTimeSvrVO.getComName();
							}
						}
					}		

					// EPG 전체 채널정보 조회
					nMainCnt = 0;

					List<GetNSChListResponseVO> sbdelayList = new ArrayList<GetNSChListResponseVO>();
					GetNSChListResponseVO sbdelayVO = new GetNSChListResponseVO();
					nSbdelay_cnt = 0;

					try {
						resultVO = getNSChListDao.getNSChList(paramVO);

						if(resultVO != null)	nMainCnt = resultVO.size();

						//System.out.println("nMainCnt: "+nMainCnt);

					} catch (Exception e) {
//						imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID020, "", null, "ChList_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
						paramVO.setResultCode("40000000");

						throw new ImcsException();
					}

					if(resultVO == null || resultVO.isEmpty()){
//						imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID020, "", null, "ChList_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
						paramVO.setResultCode("21000000");
					}

					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("채널 조회", String.valueOf((tp2 - tp1)), methodName, methodLine);

					long tp_in1 = 0;
					long tp_in2 = 0;
					int sort_cnt = 0;
					int m3u8_cnt = 0;
					String c_tvapp_castis_m3u8 = "";
					String c_tvapp_onnuri_m3u8 = "";

					for(int i = 0; i < nMainCnt; i++){
						tp_in1	= System.currentTimeMillis();
						tempVO = resultVO.get(i);
						
						if (tempVO.getServiceName().equals("프로야구 카메라#1")) {
							System.out.println("");
						}
						

				        if(paramVO.getHdtvViewGb().equals("P")) {
				        	if(tempVO.getPayYn().equals("D"))
				        		tempVO.setReplay4DYn("Y");
				            else if(tempVO.getPayYn().equals("E"))
				            	tempVO.setReplay4DYn("E");
				            else
				            	tempVO.setReplay4DYn("");
				        } else { 
				        	tempVO.setReplay4DYn("");
				        }
				        
				        if (tempVO.getPayYn().equals("C")) {
				        	
				        	cjchnlurl = getNSChListDao.getCjUrl(tempVO);
				        	tempVO.setCjChnlYn("Y");
				        	tempVO.setCjChnlUrl(cjchnlurl.get("CJ_CHNL_URL") == null ? "" : cjchnlurl.get("CJ_CHNL_URL").toString());
				        	tempVO.setCjReslType("vplive5000.smil\bvplive2000.smil\bvplive1000.smil");
				        	
				        	tempVO.setCjHevcChnlUrl(cjchnlurl.get("CJ_HEVC_CHNL_URL") == null ? "" : cjchnlurl.get("CJ_HEVC_CHNL_URL").toString());				        	
				        	if(tempVO.getCjHevcChnlYn().equals("Y"))
				        	{
				        		tempVO.setCjHevcReslType("hv_vplive4000.smil\bhv_vplive2000.smil\bhv_vplive1000.smil");
				        	}
				        } else {
				        	tempVO.setCjChnlYn("N");
				        	tempVO.setCjChnlUrl("");
				        	tempVO.setCjReslType("");
				        	tempVO.setCjHevcChnlUrl("");
				        	tempVO.setCjHevcReslType("");
				        }

						List<M3u8ProfileVO> lst_ChnlProfile = this.getChnlm3u8(resultVO.get(i), paramVO);
						m3u8_cnt = lst_ChnlProfile.size();

						int m3u8_order_chk = 1; // 2018.04.26 - m3u8파일 order(순서)가 중간에 비는지 확인하기 위한 변수 (비어있을 경우 빈공백 배열값을 넣는다.)

						//################################################################
						// 시보딜레이 처리 (메인 채널에 필드 채워넣기 위해)
						if(tempVO.getHdtvViewGb().equals("8")){

							sbdelayVO = tempVO;
							sbdelayVO.setServiceId(tempVO.getmSvcId());
							sort_cnt = 1;

							for(int j = 0; j < m3u8_cnt; j++)
							{
								/***************************************************************************************************
								 * 2018.07.04 - m3u8파일을 캐스트이즈와 온누리넷으로 구분하여 배열형태로 전달한다.
								 *              ord_num --> 1 : high, 2 : low, 3 : hevc ...
								 ***************************************************************************************************/
								//2018.06.08 - TV앱의 경우 메인채널과 , MPTS 채널의 경우 5번째 m3u8파일명을 6번째에 동일하게 넣어달라는 요청으로 인한 처리
								//			   조건 : TV앱이고, 6번째 m3u8파일이 없어야 하며, 5번째 m3u8이 존재하면, 5번째 m3u8파일명을 5번째 자리에 동일하게 채워준다.
								if(paramVO.getSvcNode().equals("T"))
								{
									if(lst_ChnlProfile.get(j).getOrderNum() == 5)
									{
										tvapp_flag = 1;
										c_tvapp_castis_m3u8 = lst_ChnlProfile.get(j).getCastisM3u8();
										c_tvapp_onnuri_m3u8 = lst_ChnlProfile.get(j).getOnnuriM3u8();
									}
									else if(lst_ChnlProfile.get(j).getOrderNum() == 6)
									{
										tvapp_flag = 0;
									}
									else if(lst_ChnlProfile.get(j).getOrderNum() > 6)
									{
										if(tvapp_flag == 1)
										{
											sbdelayVO.setCastisM3u8File(sbdelayVO.getCastisM3u8File() + "\b" + c_tvapp_castis_m3u8);
											sbdelayVO.setOnnuriM3u8File(sbdelayVO.getOnnuriM3u8File() + "\b" + c_tvapp_onnuri_m3u8);

											sort_cnt = 6;
										}
										tvapp_flag = 0;
									}
								}

								for(  ; sort_cnt < lst_ChnlProfile.get(j).getOrderNum() ; sort_cnt++)
								{
									sbdelayVO.setCastisM3u8File(sbdelayVO.getCastisM3u8File() + "\b");
									sbdelayVO.setOnnuriM3u8File(sbdelayVO.getOnnuriM3u8File() + "\b");
								}

								sbdelayVO.setCastisM3u8File(sbdelayVO.getCastisM3u8File() + lst_ChnlProfile.get(j).getCastisM3u8());
								sbdelayVO.setOnnuriM3u8File(sbdelayVO.getOnnuriM3u8File() + lst_ChnlProfile.get(j).getOnnuriM3u8());

								sort_cnt = lst_ChnlProfile.get(j).getOrderNum();
							}

							if(paramVO.getSvcNode().equals("T")) {
								{
									if(tvapp_flag == 1)
									{
										sbdelayVO.setCastisM3u8File(sbdelayVO.getCastisM3u8File() + "\b" + c_tvapp_castis_m3u8);
										sbdelayVO.setOnnuriM3u8File(sbdelayVO.getOnnuriM3u8File() + "\b" + c_tvapp_onnuri_m3u8);
									}
									tvapp_flag = 0;
								}

							}
							sbdelayList.add(sbdelayVO);					
							nSbdelay_cnt++;
							continue;
							//################################################################
						}
						if( "N".equals(paramVO.getTestSbc()) && "999".equals(tempVO.getSortNo()) ){
							continue;
						}

						//lst_ChnlList  --> tempVO
						//chnlList      --> sbdelayVO
						//lst_ChnlProfile --> this.getChnlm3u8

						for (int k = 0; k < nSbdelay_cnt; k++){
							if (tempVO.getServiceId().equals(sbdelayList.get(k).getServiceId())) {
								//2018.07.04 - m3u8파일은 캐스트이즈/온누리넷 따로 배열형태로 전달한다.
								tempVO.setTscM3u8FileName(sbdelayList.get(k).getCastisM3u8File());
								tempVO.setTsoM3u8FileName(sbdelayList.get(k).getOnnuriM3u8File());
							}
						}

						//###################################################################################
						tvapp_castis_m3u8 = "";
						tvapp_onnuri_m3u8 = "";
						sort_cnt  = 1; // m3u8파일을 내려줄 때, 중간 중간 이빨 빠진 부분을 배열로 채우기 위해 사용하는 변수
						tvapp_flag = 0;

						//lst_ChnlList  --> tempVO
						//chnlList      --> sbdelayVO
						//lst_ChnlProfile --> this.getChnlm3u8

						// 2018.07.27 - M3U8 파일 중 HIGH/LOW도 포함하여 M3U8정보를 주게끔 수정하면서 DB에 m3u8_info(서비스 여부)가 10자리인데 앞에 두자리(high,low 서비스여부)를 더 붙여줘야한다.
						tempVO.setM3u8Info("00" + tempVO.getM3u8Info());
						char[] c_tvapp_m3u8_info = { '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'}; 
						char[] charArray = tempVO.getM3u8Info().toCharArray();
						//System.out.println("###:"+ tempVO.getM3u8Info());
						for (int kk=0; kk < charArray.length; kk++) {
							c_tvapp_m3u8_info[kk] = charArray[kk];
						}
						//System.out.println("###:"+ String.copyValueOf(c_tvapp_m3u8_info));

						for(int j = 0 ; j < m3u8_cnt ; j++)
						{
							if(lst_ChnlProfile.get(j).getOrderNum() == 1)
							{
								c_tvapp_m3u8_info[0] = '1';
								tempVO.setM3u8Info(String.copyValueOf(c_tvapp_m3u8_info));
							}
							else if(lst_ChnlProfile.get(j).getOrderNum() == 2)
							{
								c_tvapp_m3u8_info[1] = '1';
								tempVO.setM3u8Info(String.copyValueOf(c_tvapp_m3u8_info));
							}
							/***************************************************************************************************
							 * 2018.07.04 - m3u8파일을 캐스트이즈와 온누리넷으로 구분하여 배열형태로 전달한다.
							 *              ord_num --> 1 : high, 2 : low, 3 : hevc ...
							 ***************************************************************************************************/
							//2018.06.08 - TV앱의 경우 메인채널과 , MPTS 채널의 경우 5번째 m3u8파일명을 6번째에 동일하게 넣어달라는 요청으로 인한 처리
							//			   조건 : TV앱이고, 6번째 m3u8파일이 없어야 하며, 5번째 m3u8이 존재하면, 5번째 m3u8파일명을 6번째 자리에 동일하게 채워준다.
							if(paramVO.getSvcNode().equals("T"))
							{
								if(lst_ChnlProfile.get(j).getOrderNum() == 5)
								{
									tvapp_flag = 1;
									c_tvapp_castis_m3u8 = lst_ChnlProfile.get(j).getCastisM3u8();
									c_tvapp_onnuri_m3u8 = lst_ChnlProfile.get(j).getOnnuriM3u8();
								}
								else if(lst_ChnlProfile.get(j).getOrderNum() == 6)
								{
									tvapp_flag = 0;
								}
								else if(lst_ChnlProfile.get(j).getOrderNum() > 6)
								{
									if(tvapp_flag == 1)
									{
										if(c_tvapp_m3u8_info[4] == '1')
										{
											c_tvapp_m3u8_info[5] = '1';
											tempVO.setM3u8Info(String.copyValueOf(c_tvapp_m3u8_info));
										}

										tempVO.setCastisM3u8File(tempVO.getCastisM3u8File() + "\b" + c_tvapp_castis_m3u8);
										tempVO.setOnnuriM3u8File(tempVO.getOnnuriM3u8File() + "\b" + c_tvapp_onnuri_m3u8);

										sort_cnt = 6;
									}
									tvapp_flag = 0;
								}
							}

							for(  ; sort_cnt < lst_ChnlProfile.get(j).getOrderNum() ; sort_cnt++)
							{
								tempVO.setCastisM3u8File(tempVO.getCastisM3u8File() + "\b");
								tempVO.setOnnuriM3u8File(tempVO.getOnnuriM3u8File() + "\b");
							}

							tempVO.setCastisM3u8File(tempVO.getCastisM3u8File() + lst_ChnlProfile.get(j).getCastisM3u8());
							tempVO.setOnnuriM3u8File(tempVO.getOnnuriM3u8File() + lst_ChnlProfile.get(j).getOnnuriM3u8());

							sort_cnt = lst_ChnlProfile.get(j).getOrderNum();
						}

						if(paramVO.getSvcNode().equals("T"))
						{
							if(tvapp_flag == 1)
							{
								if(c_tvapp_m3u8_info[4] == '1')
								{
									c_tvapp_m3u8_info[5] = '1';
									tempVO.setM3u8Info(String.copyValueOf(c_tvapp_m3u8_info));
								}

								tempVO.setCastisM3u8File(tempVO.getCastisM3u8File() + "\b" + c_tvapp_castis_m3u8);
								tempVO.setOnnuriM3u8File(tempVO.getCastisM3u8File() + "\b" + c_tvapp_onnuri_m3u8);
							}
							tvapp_flag = 0;
						}

						//###################################################################################

						tp1	= System.currentTimeMillis();

						if(!"http:".equals(tempVO.getImgUrl())){
							tempVO.setImgUrl(szChnlImgSvrip);
						}

				        //CJ채널구분이 0이면 NULL을 내린다
				        if (tempVO.getChnlGrp().equals("0"))
				        {
				            tempVO.setChnlGrp("");
				        }
				        
						// 앨범이 속한 상품 조회
						// 기존 API는 무조건 아래 SQL을 실행하나 가상채널의 경우 c_contents_id 가 없으므로 동작할 필요 없어서 체크 후 실행
						// SYSDATE 사용하나 년월일까지만 사용하므로 일자를 조건으로 추가하여 NOSQL 적용		
						if( !"".equals(tempVO.getContentsId()) ){
							String szProdId		= "";
							String szProdName	= "";
							String[] orderFlag = new String[4];
							for(int j = 0; j < orderFlag.length; j++) {
								orderFlag[j] = "";
							}
							
							String tempFlag = "";
							nSubCnt = 0;
							
							paramVO.setContentsId(tempVO.getContentsId());
							lstProdInfo = this.getProdInfo(paramVO);
						
							if(lstProdInfo != null){
								nSubCnt = lstProdInfo.size();
							}
							
							for(int j = 0; j < nSubCnt; j++){
								prodInfoVO = lstProdInfo.get(j);
								
								if(prodInfoVO.getProdGb().equals("50502")) {
									orderFlag[3] = "무료";
								} else if (prodInfoVO.getProdGb().equals("50506")) {
									orderFlag[2] = "라이트";
								} else if (prodInfoVO.getProdGb().equals("50500")) {
									orderFlag[1] = "기본월정액";
								} else {
									orderFlag[0] = "유료";
								}
								
								if(!prodInfoVO.getProdId().equals("XXXXX"))
								{
									if(j == 0){
										szProdId	= prodInfoVO.getProdId();
										szProdName	= prodInfoVO.getProdName();
									}else{
										szProdId	= szProdId + ImcsConstants.ARRSEP + prodInfoVO.getProdId();
										szProdName	= szProdName + ImcsConstants.ARRSEP +  prodInfoVO.getProdName();
									}
								}
								
							}
							
							for(int j = 0; j < 4; j++) {
								if(orderFlag[j].length() > 0 || !orderFlag[j].equals("")) {
									tempFlag = orderFlag[j];
								} 
							}
							
							tempVO.setSubscribeProdInfo(tempFlag);
							tempVO.setProductId(szProdId);
							tempVO.setProductName(szProdName);
						}
				        
						if("Y".equals(tempVO.getTimeAppYn())){
							tempVO.setLiveTimeServer1(live_time_server1);
							tempVO.setLiveTimeServer2(live_time_server2);
							tempVO.setLiveTimeServer3(live_time_server3);
							
							tempVO.setLiveVodIpv6Server1(live_time_ipv6_server1);
							tempVO.setLiveVodIpv6Server2(live_time_ipv6_server2);
							tempVO.setLiveVodIpv6Server3(live_time_ipv6_server3);
							
							tempVO.setLiveServerNode1(live_time_node1);
							tempVO.setLiveServerNode2(live_time_node2);
							tempVO.setLiveServerNode3(live_time_node3);							
							
							// 최소 SAVE_TIME 조회
							paramVO.setServiceId(tempVO.getServiceId());
							String szSaveTime = "";
							szSaveTime = this.getSaveTime(paramVO);
							tempVO.setSaveTime(szSaveTime);
							
						}else{
							tempVO.setLiveTimeServer1("");
							tempVO.setLiveTimeServer2("");
							tempVO.setLiveTimeServer3("");
							tempVO.setLiveVodIpv6Server1("");
							tempVO.setLiveVodIpv6Server2("");
							tempVO.setLiveVodIpv6Server3("");							
							tempVO.setLiveServerNode1("");
							tempVO.setLiveServerNode2("");
							tempVO.setLiveServerNode3("");		
						}
				        
						// 장르명 조회
						String szComName = "";
						paramVO.setGenre1(tempVO.getGenre1());
						szComName = this.getComName(paramVO);
						tempVO.setComName(szComName);
						
						
						if(!tempVO.getCjChnlCd().equals(""))
						{
							consertVO = new ConcertInfoVO();
							consertVO = this.getConsertInfo(tempVO, paramVO);
							
							if(consertVO != null) {
								//유툐채널정보 붙이기
								if(consertVO.getPayFlag().equals("1")) {
									tempVO.setPpvChnlYn("Y");
									tempVO.setCjReslType(tempVO.getCjChnlCd() + "/cmaf-dash/dash.mpd\b" + tempVO.getCjChnlCd() + "/cmaf-dash/dash.mpd\b" + tempVO.getCjChnlCd() + "/cmaf-hls/master.m3u8");
									consertVO.setConcertBgnTime(consertVO.getPerformDate() + consertVO.getPerformTime());
									consertVO.setConcertEndTime(consertVO.getPerformEndDate() + consertVO.getPerformEndTime());
									
									lstConsertInfo.add(consertVO);
								}
							}
						}
						
						
						if ((paramVO.getPooqYn().equals("P") && (tempVO.getPooqYn().equals("N")))) {
							System.out.println("############################:" + tempVO.getServiceName());
						} else {
							returnVO.add(tempVO);
						}

						tp_in2	= System.currentTimeMillis();

						//System.out.println("채널 정보 Fetch 1회소요:" + String.valueOf((tp_in2 - tp_in1)));
						
						
					}
					//endregion #for
					
					//유툐채널정보 붙이기
				    if(lstConsertInfo.size() > 0) {
				    	 resultListVO.setCstList(lstConsertInfo);
				    }
				    
					String resultHeader  = String.format("%s|%s|", "0", "");
					resultListVO.setResultHeader(resultHeader);
					resultListVO.setList(returnVO);

					msg = " File [" + RESFILE + "] 리턴케이스5 ";
					imcsLog.serviceLog(msg, methodName, methodLine);

					tp3	= System.currentTimeMillis();
					imcsLog.timeLog("채널 정보 Fetch", String.valueOf((tp3 - tp2)), methodName, methodLine);
					//System.out.println("채널 정보 Fetch:" + String.valueOf((tp3 - tp2)));


					// 파일 쓰기
					int nRetVal = FileUtil.fileWrite(RESFILE, resultListVO.toString(), false);

					szMsg = " nRetVal : " + nRetVal;
					imcsLog.serviceLog(szMsg, methodName, methodLine);

					if(nRetVal == 1) {
						fRESFILE = new File(RESFILE);

						if(fRESFILE.length() != 0 ){

							msg = " File [" + RESFILE + "] WRITE [" + fRESFILE.length() + "] bytes Finished";
							imcsLog.serviceLog(msg, methodName, methodLine);

							try {
								String[] szCommand = {"/bin/sh", "-c", "chmod 666 " + RESFILE};
								Process p = Runtime.getRuntime().exec(szCommand);

								szMsg = " File [" + RESFILE + "] chmod 666";
								imcsLog.serviceLog(szMsg, methodName, methodLine);

							} catch (Exception e) {
								szMsg = " cache chmod 666 error!!!";
								imcsLog.serviceLog(szMsg, methodName, methodLine);
							}

							if(NAS_DIR.exists()){
								try {
									String[] szCommand2 = {"/bin/sh", "-c", "cp " + RESFILE + " " + NASFILE};
									Process p = Runtime.getRuntime().exec(szCommand2);

									szMsg = " File [" + RESFILE + "] copy [" + NASFILE + "]";
									imcsLog.serviceLog(szMsg, methodName, methodLine);


									delCacheFile(NASPATH, compFileName, paramVO);

								} catch (Exception e) {
									szMsg = " cp cache error!!!";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
								}
							}
						}else{
							szMsg = " cache's size 0, cache delete and return";
							imcsLog.serviceLog(szMsg, methodName, methodLine);	

							fRESFILE.delete();
						}


						tp2	= System.currentTimeMillis();
						//System.out.println("파일생성수행완료:" + String.valueOf(tp2 - tp_start));
						imcsLog.timeLog(" [getNSMakeChList]cache_time", String.valueOf(tp2 - tp1), methodName, methodLine);

					} else {
						msg = " File [" + RESFILE + "] WRITE failed";
						imcsLog.serviceLog(msg, methodName, methodLine);		
					}

					FileUtil.unlock(LOCALLOCKFILE, imcsLog);

					FileUtil.unlock(LOCKFILE, imcsLog);

					delCacheFile(LOCALPATH, compFileName, paramVO);	

					long tp4	= System.currentTimeMillis();
					//System.out.println("파일생성수행완료:" + String.valueOf(tp2 - tp_start));
					imcsLog.timeLog(" [getNSMakeChList]cache_time2", String.valueOf(tp4 - tp2), methodName, methodLine);
				}


		} catch(ImcsException ce) {
			isLastProcess = ImcsConstants.RCV_MSG6;
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			isLastProcess	= ImcsConstants.RCV_MSG5;
			msg	= " svc[" + String.format("%-20s", apiInfo) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);			
			
			long tp_end = System.currentTimeMillis();
			imcsMkLog.timeLog("[" + methodName + "] tx_time", String.valueOf(tp_end - tp_start), methodName, methodLine);
			
		}
				
		return resultListVO;
	}
	
	
	/**
	 * 검수 STB 여부 조회
	 * @param paramVO
	 * @return
	 */
    public void getTestSbc(GetNSChListRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
		List<String> list   = new ArrayList<String>();
		
		try {
			
			list = getNSChListDao.getTestSbc(paramVO);
			
			if( list == null || list.isEmpty()){
				paramVO.setTestSbc("N");				
			} else {
				paramVO.setTestSbc(StringUtil.nullToSpace(list.get(0)));
			}
						
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			paramVO.setTestSbc("N");
		}
    }
    
    /**
	 * 실시간 서버 조회
	 * @param paramVO
	 * @return
	 */
    public List<ComCdVO> getLiveTimeServer(GetNSChListRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		
		List<ComCdVO> list   = new ArrayList<ComCdVO>();
		
		try {
			
			list = getNSChListDao.getLiveTimeServer();
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
    	return list;
    }
    
    
    
    /**
	 * 노드 정보 조회
	 * @param paramVO
	 * @return
	 */
    public List<ComNodeVO> getNode(GetNSChListRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		List<ComNodeVO> list   = new ArrayList<ComNodeVO>();
		
		try {
			list = getNSChListDao.getNode(paramVO);
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
    	return list;
    }
    
	
	/**
	 * 장르명 조회
	 * @param paramVO
	 * @return
	 */
	public String getComName(GetNSChListRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
		String szComName	= "";
		
		List<String> list   = null;
		
		try {
			list = getNSChListDao.getComName(paramVO);
			
			if( list != null && !list.isEmpty()){
				szComName = StringUtil.nullToSpace(list.get(0));
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException();
		}
		
		return szComName;
    }
	
	
	
	/**
	 * 앨범이 속한 상품 조회
	 * @param paramVO
	 * @return
	 */
	public List<ComProdInfoVO> getProdInfo(GetNSChListRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		
		List<ComProdInfoVO> list   = new ArrayList<ComProdInfoVO>();
		
		try {
			
			list = getNSChListDao.getProdInfo(paramVO);
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException();
		}
		
    	return list;
    }
	
	/**
	 * 최소 SAVE_TIME 조회
	 * @param paramVO
	 * @return
	 */
	public String getSaveTime(GetNSChListRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
		String szSaveTime = "";
		
		List<String> list   = null;
		
		try {
			
			list = getNSChListDao.getSaveTime(paramVO);
			
			if( list == null || list.isEmpty()){
				szSaveTime = "";
			} else {
				szSaveTime = StringUtil.nullToSpace(list.get(0));
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException();
		}
		
    	return szSaveTime;
    }
	
	// 2018.02.23 - 프로야구2.0, 모바일 야구APP m3u8 신규 테이블 조회
	public List<M3u8ProfileVO> getChnlm3u8(GetNSChListResponseVO resultVO, GetNSChListRequestVO paramVO){
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		List<M3u8ProfileVO> list   = new ArrayList<M3u8ProfileVO>();
		
		try {
			
			list = getNSChListDao.getChnlm3u8(resultVO);
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		}
		
		return (List<M3u8ProfileVO>) list;
	}
	
	public void delCacheFile(String file_path, String compValue, GetNSChListRequestVO paramVO){
		
		String szMsg = "";
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		try{
		
			File dirFile = new File(file_path);
			
			File[] files = dirFile.listFiles();
			
			List<String> list = new ArrayList<String>();
			
			if(files.length > 0){
				for(int i = 0; i < files.length; i++){
					if(files[i].getName().toString().indexOf(compValue) > -1){						
						list.add(files[i].getName());
					}
				}
				if(list != null){
					Collections.sort(list, new Comparator<String>(){
						public int compare(String obj1, String obj2){
							return obj1.compareToIgnoreCase(obj2);
						}
					});
					
					Collections.reverse(list);
					
					if(list.size() > 3){
						String del_file_path = file_path + "/" + list.get(list.size() -1);
						
						File del_file = new File(del_file_path);
						if(del_file.delete()){
							szMsg = " File delete [rm " + file_path + "/" + list.get(list.size() -1) + "]";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
							
						}else{	
							imcsLog.errorLog(methodName + "-E","delCacheFile : File delete ERROR [rm " + file_path + "/" + list.get(list.size() -1) + "]");
						}
					}
				}
			}
		}catch(Exception e){
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
	}
	
	//아이돌라이브 유료결제
	public ConcertInfoVO getConsertInfo(GetNSChListResponseVO resultVO, GetNSChListRequestVO paramVO) {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();

		ConcertInfoVO reultVO = new ConcertInfoVO();

		try {

			reultVO = getNSChListDao.getConsertInfo(resultVO);

		} catch (Exception e) {
			IMCSLog imcsLog = new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException();
		}

		return reultVO;
	}
	
}
