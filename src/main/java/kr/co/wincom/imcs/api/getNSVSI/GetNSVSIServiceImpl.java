package kr.co.wincom.imcs.api.getNSVSI;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.FileUtil;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComCdVO;
import kr.co.wincom.imcs.common.vo.ComNodeVO;
import kr.co.wincom.imcs.common.vo.ComProdInfoVO;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

@Service
public class GetNSVSIServiceImpl implements GetNSVSIService {
	private Log imcsLogger = LogFactory.getLog("API_getNSVSI");
	
	@Autowired
	private GetNSVSIDao getNSVSIDao;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private GetNSMakeVSIService getNSMakeVSIService;
	
//	@Autowired
//	private NosqlResultCache cache;
//	@Resource
//	public void setNosqlResultCache (NosqlResultCache nosqlResultCache) {
//		this.cache = nosqlResultCache;
//	}
	
//	public void getNSVSI(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	int iPos = 1;
	int iMaxPos = 1;
	int iPos_AW = 1;
	int iMaxPos_AW = 1;	/* 해외 노드도 와이파이 분산 로직과 동일하게 구현하기 위해 */
	int iPos_AL = 1;
	int iMaxPos_AL = 1;	/* 해외 노드도 LTE 분산 로직과 동일하게 구현하기 위해 */
	int	iPos_TW = 1;
	int iMaxPos_TW = 1;	/* TVApp 노드도 와이파이 분산 로직과 동일하게 구현하기 위해 */
	int	iPos_TL = 1;
	int	iMaxPos_TL = 1;	/* TVApp 노드도 와이파이 분산 로직과 동일하게 구현하기 위해 */
	
	@Override
	public GetNSVSIResultVO getNSVSI(GetNSVSIRequestVO paramVO){
//		this.getNSVSI(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		String msg	= "";
		
		GetNSVSIResultVO resultListVO = new GetNSVSIResultVO();
		GetNSVSIResponseVO tempVO = new GetNSVSIResponseVO();
		List<GetNSVSIResponseVO> resultVO = new ArrayList<GetNSVSIResponseVO>();
		List<GetNSVSIResponseVO> returnVO = new ArrayList<GetNSVSIResponseVO>();

		String LOCKFILE = "";
		String RESFILE	= "";
		
		String LOCALPATH = "";
		
		LOCALPATH = commonService.getCachePath("LOCAL", ImcsConstants.API_PRO_ID032.split("/")[1], imcsLog);
		
		//2018.08.23 폴더로 나누는 방법으로 변경
		String foldering_dir = String.format("y%so%sp%sv%sf%ssn%s", paramVO.getYouthYn(), paramVO.getOrderGb(), paramVO.getPooqYn(), 
				paramVO.getHdtvViewGb(), paramVO.getFiveChYn(), paramVO.getSvcNode());
		LOCALPATH = String.format("%s/%s", LOCALPATH, foldering_dir);

		
		msg = "LOCALPATH : " + LOCALPATH;
		imcsLog.serviceLog(msg, methodName, methodLine);
		
//		int nMainCnt = 0;
//		int nSubCnt = 0;
//		int nSbdelay_cnt = 0;

	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
		long tp2 = 0;
		long tp3 = 0;
	    
//		List<ComProdInfoVO> lstProdInfo = null;
//		List<ComNodeVO> lstNodeInfo = null;
//		List<ComCdVO> lstLiveTimeSvrInfo = null;
//		
//		ComProdInfoVO prodInfoVO = null;
//		ComNodeVO nodeVO = null;
//		ComCdVO liveTimeSvrVO = null;
		
		String szChnlImgSvrip = "";
		String szCacheNodeCd = "";
		
		try {
			szChnlImgSvrip	= commonService.getImgReplaceUrl2("img_chnl_server", "getNSVSI");
		} catch(Exception e) {
			//imcsLog.failLog(ImcsConstants.API_PRO_ID032, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
			throw new ImcsException();
		}
		
		szCacheNodeCd = paramVO.getBaseCd();
		if(szCacheNodeCd.length() >= 2){
			if (szCacheNodeCd.substring(0, 2).equals("AW") || szCacheNodeCd.substring(0, 2).equals("AL") ||
					szCacheNodeCd.substring(0, 2).equals("TW") || szCacheNodeCd.substring(0, 2).equals("TL")) //20191112 버그 수정
			{
				szCacheNodeCd = szCacheNodeCd.substring(0, 2);
			}
			
			if("Y".equals(paramVO.getBaseGb())){
				if( "AW".equals( szCacheNodeCd ) ){
					if(iMaxPos_AW != 0){
						if(iPos_AW > iMaxPos_AW){
							iPos_AW = 1;
						}
						szCacheNodeCd = szCacheNodeCd + iPos_AW;
					}
				}
				else if( "AL".equals( szCacheNodeCd ) ){
					if(iMaxPos_AL != 0){
						if(iPos_AL > iMaxPos_AL){
							iPos_AL = 1;
						}
						szCacheNodeCd = szCacheNodeCd + iPos_AL;
					}
				}
				else if( "TW".equals( szCacheNodeCd ) ){
					if(iMaxPos_TW != 0){
						if(iPos_TW > iMaxPos_TW){
							iPos_TW = 1;
						}
						szCacheNodeCd = szCacheNodeCd + iPos_TW;
					}
				}
				else if( "TL".equals( szCacheNodeCd ) ){
					if(iMaxPos_TL != 0){
						if(iPos_TL > iMaxPos_TL){
							iPos_TL = 1;
						}
						szCacheNodeCd = szCacheNodeCd + iPos_TL;
					}
				}
					
			}			
		}
		
		if(iMaxPos != 0){
			if("Y".equals(paramVO.getBaseGb())){
				if( "W".equals( paramVO.getBaseOneCd().toUpperCase() ) ){
					if(iPos > iMaxPos){
						iPos = 1;
					}
					szCacheNodeCd = paramVO.getBaseOneCd()+iPos;
				}
			}
		}
		
		msg = "iMaxPos : " + iMaxPos + " - iPos : " + iPos + ", iMaxPos_AW : " + iMaxPos_AW + " - iPos_AW : " + iPos_AW + ", iMaxPos_AL : " + iMaxPos_AL + " - iPos_AL : " + iPos_AL  ;
		imcsLog.serviceLog(msg, methodName, methodLine);
		
		try{
			//Thread.sleep(100);
			// 테스트 가입자 여부 조회
			//this.getTestSbc(paramVO);
			String szTestSbc	= "";
			szTestSbc	= this.getTestSbc(paramVO);
			paramVO.setTestSbc(szTestSbc);
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("검수 STB여부 조회", String.valueOf((tp1 - tp_start)), methodName, methodLine);
			
			//Thread.sleep(100);
			//System.out.println("##############: 검수 STB여부 조회!!: " + String.valueOf((tp1 - tp_start)));
			// 가입 상품 조회
			String szProdId	= "";
			szProdId = this.getmProdId(paramVO);
			paramVO.setmProdId(szProdId);
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("가입 상품 조회", String.valueOf((tp2 - tp1)), methodName, methodLine);
			
			//Thread.sleep(100);
			//System.out.println("##############: 가입 상품 조회!!: " + String.valueOf((tp2 - tp1)));
			String compFileName = "_n" + paramVO.getNscType()+"_y"+paramVO.getYouthYn()+"_o"+paramVO.getOrderGb()+"_t"+paramVO.getTestSbc()
					+"_p"+paramVO.getPooqYn()+"_m"+paramVO.getmProdId()+"_B"+paramVO.getBaseGb()+"_"+szCacheNodeCd+"_v"+paramVO.getHdtvViewGb()+"_f"+paramVO.getFiveChYn() +".res";
			
			msg = "compFileName : " + compFileName;
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			
			File fLOCALPATH = new File(LOCALPATH);
			
			if(!fLOCALPATH.exists()){
				fLOCALPATH.mkdir();
			}
			
			String chkFileName = "";
			
			File[] files = fLOCALPATH.listFiles();
			
			List<String> list = new ArrayList<String>();
			
			
			try{
				if(files.length > 0){			
					for(int i = 0; i < files.length; i++){
						if(files[i].getName().toString().indexOf(compFileName) > -1){					
							msg = files[i].getName();
							
							imcsLog.serviceLog(msg, methodName, methodLine);
							
							list.add(files[i].getName());
						}
					}
					if(list != null && list.size() > 0){
	//					List<String> list2 = new ArrayList<String>(list);
	//					Collections.reverse(list2);
						Collections.sort(list, new Comparator<String>(){
							public int compare(String obj1, String obj2){
								return obj1.compareToIgnoreCase(obj2);
							}
						});
						
						Collections.reverse(list);
						
						chkFileName = list.get(0);
					}else{
						chkFileName = "1";
					}
					
					
				}else{				
					chkFileName = "1";
				}
			}catch(NullPointerException e)
			{
				msg = " getNSVSI Cache File Empty";
				imcsLog.serviceLog(msg, methodName, methodLine);
			}
			
			msg = "chkFileName : " + chkFileName;
			
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			//List<String> list = Arrays.asList(files);
			
			
			
			if(list.size() > 4){
				msg = " [WARN] getNSVSI res file count [" + list.size() + "]";
				imcsLog.serviceLog(msg, methodName, methodLine);
			}
			
			
			if(commonService.chkCacheFile(chkFileName, imcsLog) || list.size() == 0){
//				Thread.sleep(1000);
				long tt1 = System.currentTimeMillis();
				
				// 노드정보 조회
				/* 20161206 - CDN IP 노드 분산(와이파이일 때) */
				String szNodeCd = "";
				
				if("Y".equals(paramVO.getBaseGb())){
					if( "W".equals( paramVO.getBaseOneCd().toUpperCase() ) ){
										
						szNodeCd = this.getNodeCdW(paramVO);
											
					}
					else if( "AW".equals( paramVO.getBaseCd().substring(0, 2).toUpperCase()) ){
						
						szNodeCd = this.getNodeCdAW(paramVO);
											
					}
					else if( "AL".equals( paramVO.getBaseCd().substring(0, 2).toUpperCase()) ){
						
						szNodeCd = this.getNodeCdAL(paramVO);
											
					}
					else if( "TW".equals( paramVO.getBaseCd().substring(0, 2).toUpperCase()) ){
						
						szNodeCd = this.getNodeCdTW(paramVO);
											
					}
					else if( "TL".equals( paramVO.getBaseCd().substring(0, 2).toUpperCase()) ){
						
						szNodeCd = this.getNodeCdTL(paramVO);										
					}
				}
				
//				if( "".equals(szNodeCd) ){
//					
//					if("Y".equals(paramVO.getBaseGb())){
//						if( "W".equals( paramVO.getBaseOneCd().toUpperCase() ) ){
//							iMaxPos = 0;
//						}
//						else if( "AW".equals( paramVO.getBaseCd().substring(0, 2).toUpperCase() ) ){
//							iMaxPos_AW = 0;
//						}
//						else if( "AL".equals( paramVO.getBaseCd().substring(0, 2).toUpperCase() ) ){
//							iMaxPos_AL = 0;
//						}
//						else if( "TW".equals( paramVO.getBaseCd().substring(0, 2).toUpperCase()) ){		
//							iMaxPos_TW = 0;
//						}
//						else if( "TL".equals( paramVO.getBaseCd().substring(0, 2).toUpperCase()) ){	
//							iMaxPos_TL = 0;
//						}
//					}
//				}
				
//				paramVO.setNodeCd(szCacheNodeCd);
				
				msg = "getNSMakeVSIs excute";
				imcsLog.serviceLog(msg, methodName, methodLine);
				//Thread.sleep(800);
				
				getNSMakeVSIService.getNSMakeVSIs(paramVO);
				imcsLog.timeLog("getNSMakeVSIs excute", String.valueOf((tp1 - tp_start)), methodName, methodLine);
				
				//System.out.println("### getNSMakeVSIs excute ###");
				
//				list.add("");

				msg = " File [" + RESFILE + "] 리턴케이스0 ";
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				long tt2 = System.currentTimeMillis();
				imcsLog.timeLog("리턴케이스1:", String.valueOf((tt2 - tp_start)), methodName, methodLine);
				
				if(list.size() == 0)
				{
					String result	= "";
					resultListVO.setResult(result);
					return resultListVO;
				}
				
			}
			
			//if(!list.get(0).equals("")){
			if(!chkFileName.equals("1")){
				
				String lockFileName = "";
				String[] arrlockFileName = list.get(0).split("\\.");
				
				for(int i = 0; i < arrlockFileName.length -1; i++){
					lockFileName += arrlockFileName[i];
				}
				
				RESFILE = LOCALPATH + "/" + list.get(0);
				
				LOCKFILE = LOCALPATH + "/" + lockFileName + ".lock";
				
				File lock = new File(LOCKFILE);
				
				if(lock.exists()){
					msg = " [WARN] File [" + list.get(1) + "] 이전 cache return ";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					RESFILE = LOCALPATH + "/" + list.get(1);
					
				}
				
				File res = new File(RESFILE);
				
				if(res.exists()) {
					String result = FileUtil.fileRead(RESFILE, "UTF-8");
					
					
					
					if(!"".equals(result)) {
						String[] arrResult	= result.split(ImcsConstants.ROWSEP);
						
						result	= "";
						
						long tt1 = System.currentTimeMillis();
						for(int i = 0; i < arrResult.length; i++) {
							arrResult[i] = arrResult[i].replaceAll("img_chnl_server", szChnlImgSvrip);
							
							result	= result + arrResult[i] + ImcsConstants.ROWSEP;
						}
						long tt2 = System.currentTimeMillis();
						imcsLog.timeLog("파일치환:", String.valueOf((tt2 - tt1)), methodName, methodLine);
						
						
						msg = " File [" + RESFILE + "] rcvbuf... [" + res.length() + "] bytes ";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						/* 20161207 - 와이파이일 때, 노드 분리 캐시를 정상적으로 읽었다면 노드순번(?)을 늘려주자 */
						if("Y".equals(paramVO.getBaseGb())){
							if( "W".equals( paramVO.getBaseOneCd().toUpperCase() ) ){
								iPos++;
							}
							else if( "AW".equals( paramVO.getBaseCd().substring(0, 2).toUpperCase() ) ){
								iPos_AW++;
							}
							else if( "AL".equals( paramVO.getBaseCd().substring(0, 2).toUpperCase() ) ){
								iPos_AL++;
							}
							else if( "TW".equals( paramVO.getBaseCd().substring(0, 2).toUpperCase() ) ){
								iPos_TW++;
							}
							else if( "TL".equals( paramVO.getBaseCd().substring(0, 2).toUpperCase() ) ){
								iPos_TL++;
							}
						}
						
						//result.replaceAll("img_chnl_server", szChnlImgSvrip);
						long tt3 = System.currentTimeMillis();
						imcsLog.timeLog("파일리턴:", String.valueOf((tt3 - tp_start)), methodName, methodLine);
						//System.out.println("##############: 파일에서 리턴한 경우!!: " + String.valueOf((tt3 - tp_start)));
						
						msg = " File [" + RESFILE + "] 리턴케이스1 ";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						resultListVO.setResult(result);
//						FileUtil.unlock(LOCKFILE, imcsLog);
						return resultListVO;
						
					}else{
						msg = " File [" + RESFILE + "] checkResFile Failed ";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						msg = " File [" + RESFILE + "] 리턴케이스2 ";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						res.delete();
					}	
				}else{
					
					msg = " File [" + RESFILE + "] 리턴케이스3 ";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					msg = " File [" + RESFILE + "] open Failed";
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
			}

			
			
					    						
//			LOCKFILE = LOCALPATH + "/getNSVSI_n"+paramVO.getNscType()+"_y"+paramVO.getYouthYn()+"_o"+paramVO.getOrderGb()+"_t"+paramVO.getTestSbc()
//					+"_p"+paramVO.getPooqYn()+"_m"+paramVO.getmProdId()+"_B"+paramVO.getBaseGb()+"_"+szCacheNodeCd+"_v"+paramVO.getHdtvViewGb()+"_f"+paramVO.getFiveChYn()+".lock";
//			RESFILE  = LOCALPATH + "/getNSVSI_n"+paramVO.getNscType()+"_y"+paramVO.getYouthYn()+"_o"+paramVO.getOrderGb()+"_t"+paramVO.getTestSbc()
//					+"_p"+paramVO.getPooqYn()+"_m"+paramVO.getmProdId()+"_B"+paramVO.getBaseGb()+"_"+szCacheNodeCd+"_v"+paramVO.getHdtvViewGb()+"_f"+paramVO.getFiveChYn()+".res";
//			
//			File res = new File(RESFILE);
//			
//			int nWaitCnt = 0;
//			
//			
//			// 파일이 존재하면 읽고 아니면 lock 파일 로직 실행
//			if(res.exists()) {
//				String result = FileUtil.fileRead(RESFILE, "UTF-8");
//				
//				if(!"".equals(result)) {
//					msg = " File [" + RESFILE + "] rcvbuf... [" + res.length() + "] bytes ";
//					imcsLog.serviceLog(msg, methodName, methodLine);
//				}
//				
//				/* 20161207 - 와이파이일 때, 노드 분리 캐시를 정상적으로 읽었다면 노드순번(?)을 늘려주자 */
//				if("Y".equals(paramVO.getBaseGb())){
//					if( "W".equals( paramVO.getBaseOneCd().toUpperCase() ) ){
//						iPos++;
//					}
//				}
//				
//				resultListVO.setResult(result);
//				FileUtil.unlock(LOCKFILE, imcsLog);
//				return resultListVO;		
//			} else {
//				msg = " File [" + RESFILE + "] read Failed";
//				imcsLog.serviceLog(msg, methodName, methodLine);
//				
//				while(FileUtil.lock(LOCKFILE, imcsLog)){
//					Thread.sleep(1000);
//					nWaitCnt++;
//					
//					msg = " queryWaitCheck Sleep [" + nWaitCnt + "] sec";
//					imcsLog.serviceLog(msg, methodName, methodLine);
//	
//					if(nWaitCnt >= 5){
//						msg = " wait_count overload Failed svc2[" + ImcsConstants.API_PRO_ID032 + "] sts[API_PRO_ID032] msg["+ 
//								String.format("%-17s", "par_yn:" + ImcsConstants.RCV_MSG2 +"]");
//						imcsLog.serviceLog(msg, methodName, methodLine);
//						
//						paramVO.setResultCode("21000000");
//						
//						throw new ImcsException();
//					}
//					
//					if(res.exists()) {
//						String result = FileUtil.fileRead(RESFILE, "UTF-8");
//						
//						if(!"".equals(result)) {
//							msg = " File [" + RESFILE + "] rcvbuf... [" + res.length() + "] bytes ";
//							imcsLog.serviceLog(msg, methodName, methodLine);
//						}
//						
//						/* 20161207 - 와이파이일 때, 노드 분리 캐시를 정상적으로 읽었다면 노드순번(?)을 늘려주자 */
//						if("Y".equals(paramVO.getBaseGb())){
//							if( "W".equals( paramVO.getBaseOneCd().toUpperCase() ) ){
//								iPos++;
//							}
//						}
//						
//						resultListVO.setResult(result);
//						FileUtil.unlock(LOCKFILE, imcsLog);
//						return resultListVO;
//					}
//				}
//			}
//			
//			
//			String live_time_server1  ="";
//			String live_time_server2 = "";
//			String live_time_server3 = "";
//			
//			// 실시간 서버 조회
//			lstLiveTimeSvrInfo = this.getLiveTimeServer(paramVO);
//
//			if(lstLiveTimeSvrInfo != null && lstLiveTimeSvrInfo.size() > 0){
//				nMainCnt = lstLiveTimeSvrInfo.size();
//			}
//			
//			for(int i = 0; i < nMainCnt; i++){
//				liveTimeSvrVO = lstLiveTimeSvrInfo.get(i);
//				
//				if("01".equals(liveTimeSvrVO.getComCd())){
//					live_time_server1 = liveTimeSvrVO.getComName();
//				}else if("02".equals(liveTimeSvrVO.getComCd())){
//					live_time_server2 = liveTimeSvrVO.getComName();
//				}else if("03".equals(liveTimeSvrVO.getComCd())){
//					live_time_server3 = liveTimeSvrVO.getComName();
//				}
//			}
//			
//			
//			// 노드정보 조회
//			/* 20161206 - CDN IP 노드 분산(와이파이일 때) */
//			String szNodeCd = "";
//			
//			if("Y".equals(paramVO.getBaseGb())){
//				if( "W".equals( paramVO.getBaseOneCd().toUpperCase() ) ){
//									
//					szNodeCd = this.getNodeCdW(paramVO);
//					
//					paramVO.setNodeCd(szNodeCd);
//										
//				}
//			}
//			
//			if( "".equals(szNodeCd) ){
//				
//				if("Y".equals(paramVO.getBaseGb())){
//					if( "W".equals( paramVO.getBaseOneCd().toUpperCase() ) ){
//						iMaxPos = 0;
//					}
//				}
//				
//				paramVO.setBaseCondi(paramVO.getBaseCd());
//				lstNodeInfo = this.getNode(paramVO);
//				
//				if(lstNodeInfo != null && !lstNodeInfo.isEmpty()){
//					nodeVO = lstNodeInfo.get(0);
//					
//					paramVO.setNodeCd(nodeVO.getNodeCd());
//					paramVO.setrBaseCode(nodeVO.getrBaseCode());
//				}
//				
//				if(lstNodeInfo == null || lstNodeInfo.isEmpty()){
//					paramVO.setBaseCondi(paramVO.getBaseOneCd());
//					
//					lstNodeInfo = this.getNode(paramVO);
//					
//					if(lstNodeInfo != null && !lstNodeInfo.isEmpty()){
//						nodeVO = lstNodeInfo.get(0);
//						
//						paramVO.setNodeCd(nodeVO.getNodeCd());
//						paramVO.setrBaseCode(nodeVO.getrBaseCode());
//					}
//				}
//					
//				if(lstNodeInfo == null || lstNodeInfo.isEmpty()){
//					paramVO.setBaseCondi("1234567890");
//
//					lstNodeInfo = this.getNode(paramVO);
//					
//					if(lstNodeInfo != null && !lstNodeInfo.isEmpty()){
//						nodeVO = lstNodeInfo.get(0);
//						
//						paramVO.setNodeCd(nodeVO.getNodeCd());
//						paramVO.setrBaseCode(nodeVO.getrBaseCode());
//					}
//				}
//				
//			}
//			
//			
//			// 동 여부 조회
//			String szDongYn = this.getDongYn(paramVO);
//			
//			if(szDongYn != null && !"".equals(szDongYn))	paramVO.setDongYn("Y");
//			else											paramVO.setDongYn("N");
//			
//			msg = " [파라미터확인]  c_order_gb["+paramVO.getOrderGb()+"] c_base_gb["+paramVO.getBaseGb()+"] c_dong_yn["+paramVO.getDongYn()+"] c_test_sbc["+paramVO.getTestSbc()+"] "
//					+ "c_nsc_type["+paramVO.getNscType()+"] c_base_code["+paramVO.getBaseCondi()+"] c_node_cd["+paramVO.getNodeCd()+"]";
//			imcsLog.serviceLog(msg, methodName, methodLine);
//			
//			msg = " [파라미터확인]  c_hdtv_view_gb["+paramVO.getHdtvViewGb()+"] c_youth_yn_com["+paramVO.getYouthYnCom()+"] c_pooq_yn_com["+paramVO.getPooqYnCom()+"]";
//			imcsLog.serviceLog(msg, methodName, methodLine);
//					
//		
//			// EPG 전체 채널정보 조회
//			nMainCnt = 0;
//			
//			List<GetNSVSIResponseVO> sbdelayList = new ArrayList<GetNSVSIResponseVO>();
//			GetNSVSIResponseVO sbdelayVO = new GetNSVSIResponseVO();
//			nSbdelay_cnt = 0;
//			
//			try {
//				if("H".equals(paramVO.getOrderGb())) {					// 인기순으로  Nscreen 가상채널 EPG전체 채널정보 조회
//					resultVO = getNSVSIDao.getNSVSIListH(paramVO);				
//				} else if("N".equals(paramVO.getOrderGb())) {			// 기본으로  Nscreen 가상채널 EPG전체 채널정보 조회				
//					resultVO = getNSVSIDao.getNSVSIListN(paramVO);				
//				}else if("A".equals(paramVO.getOrderGb())){				// 제목순으로  Nscreen 가상채널 EPG전체 채널정보 조회
//					resultVO = getNSVSIDao.getNSVSIListA(paramVO);
//				}
//				
//				if( "N".equals(paramVO.getBaseGb()) && "Y".equals(paramVO.getDongYn()) ){
//					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.HBASE_DB.ordinal()]++;
//				}else if( "N".equals(paramVO.getBaseGb()) && "N".equals(paramVO.getDongYn()) ){
//					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.REDIS_DB.ordinal()]++;
//				}else if( "Y".equals(paramVO.getBaseGb()) ){
//					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.HBASE_DB.ordinal()]++;
//				}else{
//					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.REDIS_DB.ordinal()]++;
//				}
//				
//				if(resultVO != null)	nMainCnt = resultVO.size();
//			} catch (Exception e) {
//				imcsLog.failLog(ImcsConstants.API_PRO_ID032, "", null, "favor_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
//				paramVO.setResultCode("40000000");
//
//				throw new ImcsException();
//			}
//			
//			if(resultVO == null || resultVO.isEmpty()){
//				imcsLog.failLog(ImcsConstants.API_PRO_ID032, "", null, "favor_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
//				paramVO.setResultCode("21000000");
//			}
//			
//			tp2	= System.currentTimeMillis();
//			imcsLog.timeLog("채널 조회", String.valueOf((tp2 - tp1)), methodName, methodLine);
//			
//			
//			for(int i = 0; i < nMainCnt; i++){
//				tempVO = resultVO.get(i);
//				
//				if( "PAD".equals(paramVO.getNscType()) ){
//					if( "1".equals(tempVO.getFilteringCode()) || "3".equals(tempVO.getFilteringCode()) || "5".equals(tempVO.getFilteringCode()) || "7".equals(tempVO.getFilteringCode())  ){
//						continue;
//					}
//				}else{
//					if( "8".equals(tempVO.getFilteringCode()) || "10".equals(tempVO.getFilteringCode())
//							|| "12".equals(tempVO.getFilteringCode()) || "13".equals(tempVO.getFilteringCode()) || "14".equals(tempVO.getFilteringCode()) ){
//						continue;
//					}
//				}
//				
//				
//				if("8".equals(tempVO.getHdtvViewGb())){
//					
//					sbdelayVO = tempVO;
//					
//					sbdelayVO.setServiceId(tempVO.getServiceRefId());
//					
//					sbdelayList.add(sbdelayVO);					
//					
//					nSbdelay_cnt++;
//					
//					continue;
//				}
//				
//				if( "N".equals(paramVO.getTestSbc()) && "999".equals(tempVO.getSortNo()) ){
//					continue;
//				}
//				
//				for(int j=0; j<nSbdelay_cnt; j++){
//					
//					sbdelayVO = sbdelayList.get(j);
//					
//					if( tempVO.getServiceId().equals( sbdelayVO.getServiceId() ) ){
//						tempVO.setTsFileName1(sbdelayVO.getLiveFileName1());
//						tempVO.setTsFileName2(sbdelayVO.getLiveFileName2());
//						tempVO.setTsFileName3(sbdelayVO.getLiveFileName3());
//						
//						tempVO.setTsLowFileName1(sbdelayVO.getLiveFileName4());
//						tempVO.setTsLowFileName2(sbdelayVO.getLiveFileName5());
//						tempVO.setTsLowFileName3(sbdelayVO.getLiveFileName6());
//					}
//					
//				}	
//					
//				tp1	= System.currentTimeMillis();
//				
//				if(!"http:".equals(tempVO.getImgUrl())){
//					tempVO.setImgUrl(szChnlImgSvrip);
//				}
//				
//				// 채널구분이 0이 아니면(1:CJ채널이면) NULL을 내린다
//				if( !"0".equals(tempVO.getChnlGrp()) ){
//					tempVO.setChnlGrp("");
//				}
//				
//				
//				
//				// 장르명 조회
//				String szComName = "";
//				paramVO.setGenre1(tempVO.getGenre1());
//				
//				szComName = this.getComName(paramVO);
//				tempVO.setComName(szComName);
//				
//				
//				
//				// 앨범이 속한 상품 조회
//				// 기존 API는 무조건 아래 SQL을 실행하나 가상채널의 경우 c_contents_id 가 없으므로 동작할 필요 없어서 체크 후 실행
//				// SYSDATE 사용하나 년월일까지만 사용하므로 일자를 조건으로 추가하여 NOSQL 적용		
//				if( !"".equals(tempVO.getContentsId()) ){
//					String szProdId		= "";
//					String szProdName	= "";
//					
//					nSubCnt = 0;
//					
//					paramVO.setContentsId(tempVO.getContentsId());
//					lstProdInfo = this.getProdInfo(paramVO);
//				
//					if(lstProdInfo != null){
//						nSubCnt = lstProdInfo.size();
//					}
//					
//					for(int j = 0; j < nSubCnt; j++){
//						prodInfoVO = lstProdInfo.get(j);
//						
//						if(j == 0){
//							szProdId	= prodInfoVO.getProdId();
//							szProdName	= prodInfoVO.getProdName();
//						}else{
//							szProdId	= szProdId + ImcsConstants.ARRSEP + prodInfoVO.getProdId();
//							szProdName	= szProdName + ImcsConstants.ARRSEP +  prodInfoVO.getProdName();
//						}
//						
//					}
//					
//					tempVO.setProductId(szProdId);
//					tempVO.setProductName(szProdName);
//				}
//				
//
//				
//				// 선호채널 여부 조회
//				String szFavorYn = "";
//				szFavorYn = this.getFavorYn(paramVO);
//				if(szFavorYn != null && !"".equals(szFavorYn))
//					tempVO.setFavorYn(szFavorYn);
//				
//				tempVO.setLiveServer1("http://" + tempVO.getLiveIp1() + ":" + tempVO.getLivePort() + "/");
//				tempVO.setLiveServer2("http://" + tempVO.getLiveIp2() + ":" + tempVO.getLivePort() + "/");
//				tempVO.setLiveServer3("http://" + tempVO.getLiveIp3() + ":" + tempVO.getLivePort() + "/");
//				tempVO.setLiveServer4("http://" + tempVO.getLiveIp4() + ":" + tempVO.getLivePort() + "/");
//				tempVO.setLiveServer5("http://" + tempVO.getLiveIp5() + ":" + tempVO.getLivePort() + "/");
//				tempVO.setLiveServer6("http://" + tempVO.getLiveIp6() + ":" + tempVO.getLivePort() + "/");
//				
//				if("Y".equals(tempVO.getTimeAppYn())){
//					tempVO.setLiveTimeServer1(live_time_server1);
//					tempVO.setLiveTimeServer2(live_time_server2);
//					tempVO.setLiveTimeServer3(live_time_server3);
//					
//					// 최소 SAVE_TIME 조회
//					paramVO.setServiceId(tempVO.getServiceId());
//					String szSaveTime = "";
//					szSaveTime = this.getSaveTime(paramVO);
//					tempVO.setSaveTime(szSaveTime);
//					
//				}else{
//					tempVO.setLiveTimeServer1("");
//					tempVO.setLiveTimeServer2("");
//					tempVO.setLiveTimeServer3("");
//				}
//								
//				if("P".equals(paramVO.getPooqYn()) && "N".equals(tempVO.getPooqYn()) ){
//					
//				}else{
//					returnVO.add(tempVO);
//				}
//				
//			}
//			
//			resultListVO.setList(returnVO);
//			
//			tp3	= System.currentTimeMillis();
//			imcsLog.timeLog("채널 정보 Fetch", String.valueOf((tp3 - tp2)), methodName, methodLine);
//			
//			
//			
//			// 파일 쓰기
//			int nRetVal = FileUtil.fileWrite(RESFILE, resultListVO.toString(), false);
//			
//			if(nRetVal == -1) {
//				msg = "File [" + RESFILE + "] WRITE failed";
//				imcsLog.serviceLog(msg, methodName, methodLine);
//			}
//			
//			FileUtil.unlock(LOCKFILE, imcsLog);
//			
//			// 파일이 존재하면 읽고 아니면 lock 파일 로직 실행
//			if(res.exists()) {
//				String result = FileUtil.fileRead(RESFILE, "UTF-8");
//				
//				if(!"".equals(result)) {
//					msg = " File [" + RESFILE + "] rcvbuf... [" + res.length() + "] bytes ";
//					imcsLog.serviceLog(msg, methodName, methodLine);
//				}
//				
//				/* 20161207 - 와이파이일 때, 노드 분리 캐시를 정상적으로 읽었다면 노드순번(?)을 늘려주자 */
//				if("Y".equals(paramVO.getBaseGb())){
//					if( "W".equals( paramVO.getBaseOneCd().toUpperCase() ) ){
//						iPos++;
//					}
//				}
//				
//				resultListVO.setResult(result);
//				FileUtil.unlock(LOCKFILE, imcsLog);
//				return resultListVO;		
//			} else {
//				msg = " File [" + RESFILE + "] open Failed";
//				imcsLog.serviceLog(msg, methodName, methodLine);
//			}
			
		} catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		} finally{
			// NosqlCacheType.HBASE_WR.ordinal(), NosqlCacheType.USERDB.ordinal() 이거는?
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID032) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID032) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	
	
	
	/**
	 * 검수 STB 여부 조회
	 * @param paramVO
	 * @return
	 */
    public String getTestSbc(GetNSVSIRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
		//String sqlId = "lgvod032_001_20140510_001";
		String sqlId = "lgvod032_001_20181019_001";
		
		List<String> list   = null;
		String testSbc = "";
		try {
			
			try{
				list = getNSVSIDao.getTestSbc(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}			
			
			if( list == null || list.isEmpty()){
				//paramVO.setTestSbc("N");	
				testSbc = "N";
			} else {
				//paramVO.setTestSbc(StringUtil.nullToSpace(list.get(0)));
				//return StringUtil.nullToSpace(list.get(0));
				testSbc = StringUtil.nullToSpace(list.get(0));
			}
						
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			//paramVO.setTestSbc("N");
			return "N";
		}
		
		return testSbc;
    }
    
    
    
    /**
	 * 가입 상품 조회
	 * @param paramVO
	 * @return
	 */
    public String getmProdId(GetNSVSIRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	//String sqlId = "lgvod032_002_20140610_001";
    	String sqlId = "lgvod032_002_20180912_001";
				
		List<String> list   = null;
		String prodId = "";
		
		try {
			
			try{
				list = getNSVSIDao.getmProdId(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			if( list != null && !list.isEmpty()){
				//paramVO.setmProdId(StringUtil.nullToSpace(list.get(0)));
				prodId = StringUtil.nullToSpace(list.get(0));
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
		return prodId;
    }
    
    
    
//    /**
//	 * 실시간 서버 조회
//	 * @param paramVO
//	 * @return
//	 */
//    public List<ComCdVO> getLiveTimeServer(GetNSVSIRequestVO paramVO) throws Exception{
//    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
//    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
//		String methodName = oStackTrace.getMethodName();
//		
//    	//String sqlId = "lgvod032_003_20171214_001";
//    	String sqlId = "lgvod032_003_20180911_001";
//    	
////		NosqlResultCache cache = new NosqlResultCache();
////		List<Object> binds = new ArrayList<Object>();
////		RowKeyList rowKeys = new RowKeyList();
////		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
//		
//		List<ComCdVO> list   = new ArrayList<ComCdVO>();
//		
//		try {
//			
//			list = getNSVSIDao.getLiveTimeServer();
//			
////			rowKeys.setSqlId(sqlId);
////			rowKeys.addRowKeys("TIMECDNIP");
////			checkKey.addVersionTuple("PT_CD_COM_CD", "TIMECDNIP");
////									
////			list = cache.getCachedResult(new CacheableExecutor<ComCdVO>() {
////				@Override
////				public List<ComCdVO> execute(List<Object> param) throws SQLException {
////					try{
////						List<ComCdVO> rtnList = getNSVSIDao.getLiveTimeServer();
////						return rtnList;
////					}catch(DataAccessException e){
////						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
////						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
////					}
////				}
////				
////				@Override
////				public Class<ComCdVO> getReturnType() {
////					return ComCdVO.class;
////				}
////			}, binds, rowKeys, checkKey);
////			
////			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
//			
//		} catch (Exception e) {
////			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
////			else									paramVO.setResultCode("41000000");
////			imcsLog.failLog(ImcsConstants.API_PRO_ID032, "", cache, "cont_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
//		}
//		
//    	return list;
//    }
//    
//    
//    
//    /**
//	 * 노드 정보 조회
//	 * @param paramVO
//	 * @return
//	 */
//    public List<ComNodeVO> getNode(GetNSVSIRequestVO paramVO) throws Exception{
//    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
//		String methodName = oStackTrace.getMethodName();
//    	
//    	//String sqlId = "lgvod032_004_20180302_002";
//		String sqlId = "lgvod032_004_20180911_002";
//
////		NosqlResultCache cache = new NosqlResultCache();
////		List<Object> binds = new ArrayList<Object>();
////		RowKeyList rowKeys = new RowKeyList();
////		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
//		
//		List<ComNodeVO> list   = new ArrayList<ComNodeVO>();
//		
//		try {
//
//			list = getNSVSIDao.getNode(paramVO);
//			
////			rowKeys.setSqlId(sqlId);
////			rowKeys.addRowKeys(paramVO.getBaseCondi());
////			checkKey.addVersionTuple("PT_LV_RANGE_IP_INFO");
////			binds.add(paramVO);
////			
////			list = cache.getCachedResult(new CacheableExecutor<ComNodeVO>() {
////				@Override
////				public List<ComNodeVO> execute(List<Object> param) throws SQLException {
////					try{
////						GetNSVSIRequestVO requestVO = (GetNSVSIRequestVO)param.get(0);
////						List<ComNodeVO> rtnList = getNSVSIDao.getNode(requestVO);
////						
////						return rtnList;
////					}catch(DataAccessException e){
////						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
////						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
////					}
////				}
////				
////				@Override
////				public Class<ComNodeVO> getReturnType() {
////					return ComNodeVO.class;
////				}
////			}, binds, rowKeys, checkKey);
////			
////			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
//			
//		} catch (Exception e) {
//			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
//		}
//		
//    	return list;
//    }
//    
//    
//    
//    /**
//	 * 동 여부 조회
//	 * @param paramVO
//	 * @return
//	 */
//    public String getDongYn(GetNSVSIRequestVO paramVO) throws Exception{
//    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
//    	String methodName = oStackTrace.getMethodName();
//    	
//    	//String sqlId = "lgvod032_005_20180302_002";
//    	String sqlId = "lgvod032_005_20180911_002";
//    	String szDongYn	= "";
//    	
////		NosqlResultCache cache = new NosqlResultCache();
////		List<Object> binds = new ArrayList<Object>();
////		RowKeyList rowKeys = new RowKeyList();
////		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
//		
//		List<String> list   = new ArrayList<String>();
//		
//		try {
//
//			list = getNSVSIDao.getDongYn(paramVO);
//			
////			rowKeys.setSaId(paramVO.getSaId());
////			rowKeys.setSqlId(sqlId);
////			rowKeys.setStbMac(paramVO.getStbMac());
////			checkKey.addVersionTuple("PT_VO_CUSTOM_ID", paramVO.getSaId());
////			checkKey.addVersionTuple("PT_LV_NODE_INFO");
////			checkKey.addVersionTuple("PT_LV_DONG_INFO");
////			binds.add(paramVO);
////						
////			list = cache.getCachedResult(new CacheableExecutor<String>() {
////				@Override
////				public List<String> execute(List<Object> param) throws SQLException {
////					try{
////						GetNSVSIRequestVO requestVO = (GetNSVSIRequestVO)param.get(0);
////						List<String> rtnList = getNSVSIDao.getDongYn(requestVO);
////						
////						return rtnList;
////					}catch(DataAccessException e){
////						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
////						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
////					}
////				}
////				
////				@Override
////				public Class<String> getReturnType() {
////					return String.class;
////				}
////			}, binds, rowKeys, checkKey);
//			
//			if( list != null && !list.isEmpty()){
//				szDongYn = StringUtil.nullToSpace(list.get(0));
//			}
//		
////			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
//			
//		} catch (Exception e) {
//			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
//		}
//		
//    	return szDongYn;
//    }
//    
//    
//	
//	/**
//	 * 장르명 조회
//	 * @param paramVO
//	 * @return
//	 */
//	public String getComName(GetNSVSIRequestVO paramVO) throws Exception{
//    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
//		String methodName = oStackTrace.getMethodName();
//    	
//    	//String sqlId = "lgvod032_014_20180302_002";
//    	String sqlId = "lgvod032_014_20180911_002";
//		String szComName	= "";
//		
////		NosqlResultCache cache = new NosqlResultCache();
////		List<Object> binds = new ArrayList<Object>();
////		RowKeyList rowKeys = new RowKeyList();
////		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
//		
//		List<String> list   = null;
//		
//		try {
//			
//			list = getNSVSIDao.getComName(paramVO);
//			
////			rowKeys.setSqlId(sqlId);
////			rowKeys.addRowKeys("HSVCKIND");
////			rowKeys.addRowKeys(paramVO.getGenre1());
////			checkKey.addVersionTuple("PT_CD_COM_CD", paramVO.getGenre1());
////			binds.add(paramVO);
////						
////			list = cache.getCachedResult(new CacheableExecutor<String>() {
////				@Override
////				public List<String> execute(List<Object> param) throws SQLException {
////					try{
////						GetNSVSIRequestVO requestVO = (GetNSVSIRequestVO)param.get(0);
////						List<String> rtnList = getNSVSIDao.getComName(requestVO);
////						
////						return rtnList;
////					}catch(DataAccessException e){
////						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
////						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
////					}
////				}
////				
////				@Override
////				public Class<String> getReturnType() {
////					return String.class;
////				}
////			}, binds, rowKeys, checkKey);
//			
//			if( list != null && !list.isEmpty()){
//				szComName = StringUtil.nullToSpace(list.get(0));
//			}
//			
////			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
//			
//		} catch (Exception e) {
//			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
//		}
//		
//		return szComName;
//    }
//	
//	
//	
//	/**
//	 * 앨범이 속한 상품 조회
//	 * @param paramVO
//	 * @return
//	 */
//	public List<ComProdInfoVO> getProdInfo(GetNSVSIRequestVO paramVO) throws Exception{
//    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
//    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
//		String methodName = oStackTrace.getMethodName();
//		
//    	//String sqlId = "lgvod032_015_20180302_002";
//    	String sqlId = "lgvod032_015_20180911_002";
//    	
////		NosqlResultCache cache = new NosqlResultCache();
////		List<Object> binds = new ArrayList<Object>();
////		RowKeyList rowKeys = new RowKeyList();
////		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
//		
//		List<ComProdInfoVO> list   = new ArrayList<ComProdInfoVO>();
//		
//		try {
//
//			list = getNSVSIDao.getProdInfo(paramVO);
//			
////			rowKeys.setSqlId(sqlId);
////			rowKeys.addRowKeys(paramVO.getStampToday());
////			rowKeys.addRowKeys(paramVO.getmProdId());
////			rowKeys.addRowKeys(paramVO.getContentsId());
////			rowKeys.addRowKeys(paramVO.getYouthYn());
////			checkKey.addVersionTuple("PT_PD_PACKAGE");
////			checkKey.addVersionTuple("PT_PD_PACKAGE_DETAIL", paramVO.getContentsId());
////			checkKey.addVersionTuple("PT_PD_PACKAGE_RELATION");
////			checkKey.addVersionTuple("PV_PROD_PRODUCT_TBL");
////			checkKey.addVersionTuple("PT_CD_COM_CD", "HDTVPROD");
////			
////			binds.add(paramVO);
////						
////			list = cache.getCachedResult(new CacheableExecutor<ComProdInfoVO>() {
////				@Override
////				public List<ComProdInfoVO> execute(List<Object> param) throws SQLException {
////					try{
////						GetNSVSIRequestVO requestVO = (GetNSVSIRequestVO)param.get(0);
////						List<ComProdInfoVO> rtnList = getNSVSIDao.getProdInfo(requestVO);
////						
////						return rtnList;
////					}catch(DataAccessException e){
////						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
////						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
////					}
////				}
////				
////				@Override
////				public Class<ComProdInfoVO> getReturnType() {
////					return ComProdInfoVO.class;
////				}
////			}, binds, rowKeys, checkKey);
////			
////			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
//			
//		} catch (Exception e) {
////			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
////			else									paramVO.setResultCode("41000000");
////			imcsLog.failLog(ImcsConstants.API_PRO_ID032, "", cache, "prod_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
//			
//			throw new ImcsException();
//		}
//		
//    	return list;
//    }
//	
//	
//	
//	/**
//	 * 선호채널 여부 조회
//	 * @param paramVO
//	 * @return
//	 */
//	public String getFavorYn(GetNSVSIRequestVO paramVO) throws Exception{
//    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
//		String methodName = oStackTrace.getMethodName();
//    	
//    	//String sqlId = "lgvod032_016_20180302_002";
//		String sqlId = "lgvod032_016_20180911_002";
//    	String szFavorYn = "";
//		
////		NosqlResultCache cache = new NosqlResultCache();
////		List<Object> binds = new ArrayList<Object>();
////		RowKeyList rowKeys = new RowKeyList();
////		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
//		
//		List<String> list   = new ArrayList<String>();
//		
//		try {
//			
//			list = getNSVSIDao.getFavorYn(paramVO);
//			
////			rowKeys.setSaId(paramVO.getSaId());			
////			rowKeys.setStbMac(paramVO.getStbMac());
////			rowKeys.setSqlId(sqlId);
////			rowKeys.addRowKeys(paramVO.getServiceId());
////			
////			checkKey.addVersionTuple("PT_VO_FAVORITE_CH",paramVO.getSaId());
////			
////			binds.add(paramVO);
////						
////			list = cache.getCachedResult(new CacheableExecutor<String>() {
////				@Override
////				public List<String> execute(List<Object> param) throws SQLException {
////					try{
////						GetNSVSIRequestVO requestVO = (GetNSVSIRequestVO)param.get(0);
////						List<String> rtnList = getNSVSIDao.getFavorYn(requestVO);
////						
////						return rtnList;
////					}catch(DataAccessException e){
////						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
////						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
////					}
////				}
////				
////				@Override
////				public Class<String> getReturnType() {
////					return String.class;
////				}
////			}, binds, rowKeys, checkKey);
//			
//			if( list == null ||list.isEmpty()){
//				szFavorYn = "";
//			} else {
//				szFavorYn = StringUtil.nullToSpace(list.get(0));
//			}
//			
////			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
//			
//		} catch (Exception e) {
//			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
//		}
//		
//    	return szFavorYn;
//    }
//	
//	
//	
//	/**
//	 * 최소 SAVE_TIME 조회
//	 * @param paramVO
//	 * @return
//	 */
//	public String getSaveTime(GetNSVSIRequestVO paramVO) throws Exception{
//    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
//		String methodName = oStackTrace.getMethodName();
//    	
//    	//String sqlId = "lgvod032_017_20180302_002";
//    	String sqlId = "lgvod032_017_20180911_002";
//		String szSaveTime = "";
//    	
////		NosqlResultCache cache = new NosqlResultCache();
////		List<Object> binds = new ArrayList<Object>();
////		RowKeyList rowKeys = new RowKeyList();
////		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
//		
//		List<String> list   = null;
//		
//		try {
//			
//			list = getNSVSIDao.getSaveTime(paramVO);
//			
////			rowKeys.setSqlId(sqlId);
////			rowKeys.addRowKeys(paramVO.getServiceId());
////			checkKey.addVersionTuple("PT_CD_NSC_CHNL", paramVO.getServiceId());
////			binds.add(paramVO);
////			
////			list = cache.getCachedResult(new CacheableExecutor<String>() {
////				@Override
////				public List<String> execute(List<Object> param) throws SQLException {
////					try{
////						GetNSVSIRequestVO requestVO = (GetNSVSIRequestVO)param.get(0);
////						List<String> rtnList = getNSVSIDao.getSaveTime(requestVO);
////						
////						return rtnList;
////					}catch(DataAccessException e){
////						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
////						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
////					}
////				}
////				
////				@Override
////				public Class<String> getReturnType() {
////					return String.class;
////				}
////			}, binds, rowKeys, checkKey);
//			
//			if( list == null || list.isEmpty()){
//				szSaveTime = "";
//			} else {
//				szSaveTime = StringUtil.nullToSpace(list.get(0));
//			}
//			
////			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
//			
//		} catch (Exception e) {
//			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
//		}
//		
//    	return szSaveTime;
//    }
//	
	/**
	 * 노드 정보 조회(와이파이)
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public String getNodeCdW(GetNSVSIRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId	=  "lgvod032_s18_20180911_002";
    	String szNodeCd	= "";
    	
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
		
		List<String> list   = new ArrayList<String>();
		
		try {

			list = getNSVSIDao.getNodeCdW(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
//			rowKeys.setSqlId(sqlId);
//			checkKey.addVersionTuple("PT_LV_RANGE_IP_INFO");
//			binds.add(paramVO);
//			
//			list = cache.getCachedResult(new CacheableExecutor<String>() {
//				@Override
//				public List<String> execute(List<Object> param) throws SQLException {
//					try{
//						GetNSVSIRequestVO requestVO = (GetNSVSIRequestVO)param.get(0);
//						
//						List<String> rtnList = null;
//						rtnList = getNSVSIDao.getNodeCdW(requestVO);
//												
//						return rtnList;
//					}catch(DataAccessException e){
//						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//				}
//				
//				@Override
//				public Class<String> getReturnType() {
//					return String.class;
//				}
//			}, binds, rowKeys, checkKey);
			
			/* 조회한 결과의 개수가 이번에 사용할 노드 순서보다 작은 경우 다시 첫번째 노드를 사용하기 위하여 변수 초기화
			 * PT_LV_RANGE_IP_INFO 테이블에 WI-FI 사용자에게 응답할 노드의 수가 변동 되었을 경우
			 * 아래 반복문으로 인한 잘못 된 데이터 저장하는 것을 방지
			 */
			if(list == null || list.isEmpty()){
				iMaxPos = 0;
			}else{
				iMaxPos = list.size();
			}
			
			if(iPos > iMaxPos){
				iPos = 1;
			}
			
//			if( list != null && !list.isEmpty()){
//				
//				/* 조회한 모든 row를 fetch하지 않고 이번에 반환할 순서에 해당하는 노드까지만 fetch 하여
//				 * 아래 반복문 종료시 이번에 반환할 순서에 해당하는 노드 값이 szNodeCd에 저장		 */
//				for(int i=0; i<iPos; i++){
//					szNodeCd	= list.get(i);
//				}
//				iPos++;
//			}
			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
    	return szNodeCd;
    }
	
	/**
	 * 노드 정보 조회(해외노드 와이파이)
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public String getNodeCdAW(GetNSVSIRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	//String sqlId	=  "lgvod032_s19_20180723_001";
    	String sqlId	=  "lgvod032_s19_20180911_001";
    	String szNodeCd	= "";
    	
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
		
		List<String> list   = new ArrayList<String>();
		
		try {

			list = getNSVSIDao.getNodeCdAW(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
//			rowKeys.setSqlId(sqlId);
//			checkKey.addVersionTuple("PT_LV_RANGE_IP_INFO");
//			binds.add(paramVO);
//			
//			list = cache.getCachedResult(new CacheableExecutor<String>() {
//				@Override
//				public List<String> execute(List<Object> param) throws SQLException {
//					try{
//						GetNSVSIRequestVO requestVO = (GetNSVSIRequestVO)param.get(0);
//						
//						List<String> rtnList = null;
//						rtnList = getNSVSIDao.getNodeCdAW(requestVO);
//												
//						return rtnList;
//					}catch(DataAccessException e){
//						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//				}
//				
//				@Override
//				public Class<String> getReturnType() {
//					return String.class;
//				}
//			}, binds, rowKeys, checkKey);
			
			/* 조회한 결과의 개수가 이번에 사용할 노드 순서보다 작은 경우 다시 첫번째 노드를 사용하기 위하여 변수 초기화
			 * PT_LV_RANGE_IP_INFO 테이블에 WI-FI 사용자에게 응답할 노드의 수가 변동 되었을 경우
			 * 아래 반복문으로 인한 잘못 된 데이터 저장하는 것을 방지
			 */
			if(list == null || list.isEmpty()){
				iMaxPos_AW = 0;
			}else{
				iMaxPos_AW = list.size();
			}
			
			if(iPos_AW > iMaxPos_AW){
				iPos_AW = 1;
			}
			
//			if( list != null && !list.isEmpty()){
//				
//				/* 조회한 모든 row를 fetch하지 않고 이번에 반환할 순서에 해당하는 노드까지만 fetch 하여
//				 * 아래 반복문 종료시 이번에 반환할 순서에 해당하는 노드 값이 szNodeCd에 저장		 */
//				for(int i=0; i<iPos_AW; i++){
//					szNodeCd	= list.get(i);
//				}
//				iPos_AW++;
//			}
			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
    	return szNodeCd;
    }
	
	/**
	 * 노드 정보 조회(해외노드 LTE)
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public String getNodeCdAL(GetNSVSIRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	//String sqlId	=  "lgvod032_s19_20180723_001";
		String sqlId	=  "lgvod032_s19_20180911_001";
    	String szNodeCd	= "";
    	
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
		
		List<String> list   = new ArrayList<String>();
		
		try {

			list = getNSVSIDao.getNodeCdAL(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
//			rowKeys.setSqlId(sqlId);
//			checkKey.addVersionTuple("PT_LV_RANGE_IP_INFO");
//			binds.add(paramVO);
//			
//			list = cache.getCachedResult(new CacheableExecutor<String>() {
//				@Override
//				public List<String> execute(List<Object> param) throws SQLException {
//					try{
//						GetNSVSIRequestVO requestVO = (GetNSVSIRequestVO)param.get(0);
//						
//						List<String> rtnList = null;
//						rtnList = getNSVSIDao.getNodeCdAL(requestVO);
//												
//						return rtnList;
//					}catch(DataAccessException e){
//						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//				}
//				
//				@Override
//				public Class<String> getReturnType() {
//					return String.class;
//				}
//			}, binds, rowKeys, checkKey);
			
			/* 조회한 결과의 개수가 이번에 사용할 노드 순서보다 작은 경우 다시 첫번째 노드를 사용하기 위하여 변수 초기화
			 * PT_LV_RANGE_IP_INFO 테이블에 WI-FI 사용자에게 응답할 노드의 수가 변동 되었을 경우
			 * 아래 반복문으로 인한 잘못 된 데이터 저장하는 것을 방지
			 */
			if(list == null || list.isEmpty()){
				iMaxPos_AL = 0;
			}else{
				iMaxPos_AL = list.size();
			}
			
			if(iPos_AL > iMaxPos_AL){
				iPos_AL = 1;
			}
			
//			if( list != null && !list.isEmpty()){
//				
//				/* 조회한 모든 row를 fetch하지 않고 이번에 반환할 순서에 해당하는 노드까지만 fetch 하여
//				 * 아래 반복문 종료시 이번에 반환할 순서에 해당하는 노드 값이 szNodeCd에 저장		 */
//				for(int i=0; i<iPos_AL; i++){
//					szNodeCd	= list.get(i);
//				}
//				iPos_AL++;
//			}
			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
    	return szNodeCd;
    }

	/**
	 * 노드 정보 조회(TW)
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public String getNodeCdTW(GetNSVSIRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	//String sqlId	=  "lgvod032_s19_20180723_001";
    	String sqlId	=  "lgvod032_s19_20180911_001";
    	String szNodeCd	= "";
    	
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
		
		List<String> list   = new ArrayList<String>();
		
		try {

			list = getNSVSIDao.getNodeCdTW(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
//			rowKeys.setSqlId(sqlId);
//			checkKey.addVersionTuple("PT_LV_RANGE_IP_INFO");
//			binds.add(paramVO);
//			
//			list = cache.getCachedResult(new CacheableExecutor<String>() {
//				@Override
//				public List<String> execute(List<Object> param) throws SQLException {
//					try{
//						GetNSVSIRequestVO requestVO = (GetNSVSIRequestVO)param.get(0);
//						
//						List<String> rtnList = null;
//						rtnList = getNSVSIDao.getNodeCdTW(requestVO);
//												
//						return rtnList;
//					}catch(DataAccessException e){
//						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//				}
//				
//				@Override
//				public Class<String> getReturnType() {
//					return String.class;
//				}
//			}, binds, rowKeys, checkKey);
			
			/* 조회한 결과의 개수가 이번에 사용할 노드 순서보다 작은 경우 다시 첫번째 노드를 사용하기 위하여 변수 초기화
			 * PT_LV_RANGE_IP_INFO 테이블에 WI-FI 사용자에게 응답할 노드의 수가 변동 되었을 경우
			 * 아래 반복문으로 인한 잘못 된 데이터 저장하는 것을 방지
			 */
			if(list == null || list.isEmpty()){
				iMaxPos_TW = 0;
			}else{
				iMaxPos_TW = list.size();
			}
			
			if(iPos_TW > iMaxPos_TW){
				iPos_TW = 1;
			}
			
//			if( list != null && !list.isEmpty()){
//				
//				/* 조회한 모든 row를 fetch하지 않고 이번에 반환할 순서에 해당하는 노드까지만 fetch 하여
//				 * 아래 반복문 종료시 이번에 반환할 순서에 해당하는 노드 값이 szNodeCd에 저장		 */
//				for(int i=0; i<iPos_TW; i++){
//					szNodeCd	= list.get(i);
//				}
//				iPos_TW++;
//			}
			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
    	return szNodeCd;
    }
	
	/**
	 * 노드 정보 조회(TL)
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public String getNodeCdTL(GetNSVSIRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	//String sqlId	=  "lgvod032_s19_20180723_001";
    	String sqlId	=  "lgvod032_s19_20180911_001";
    	String szNodeCd	= "";
    	
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
		
		List<String> list   = new ArrayList<String>();
		
		try {

			list = getNSVSIDao.getNodeCdTL(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
//			rowKeys.setSqlId(sqlId);
//			checkKey.addVersionTuple("PT_LV_RANGE_IP_INFO");
//			binds.add(paramVO);
//			
//			list = cache.getCachedResult(new CacheableExecutor<String>() {
//				@Override
//				public List<String> execute(List<Object> param) throws SQLException {
//					try{
//						GetNSVSIRequestVO requestVO = (GetNSVSIRequestVO)param.get(0);
//						
//						List<String> rtnList = null;
//						rtnList = getNSVSIDao.getNodeCdTL(requestVO);
//												
//						return rtnList;
//					}catch(DataAccessException e){
//						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//				}
//				
//				@Override
//				public Class<String> getReturnType() {
//					return String.class;
//				}
//			}, binds, rowKeys, checkKey);
			
			/* 조회한 결과의 개수가 이번에 사용할 노드 순서보다 작은 경우 다시 첫번째 노드를 사용하기 위하여 변수 초기화
			 * PT_LV_RANGE_IP_INFO 테이블에 WI-FI 사용자에게 응답할 노드의 수가 변동 되었을 경우
			 * 아래 반복문으로 인한 잘못 된 데이터 저장하는 것을 방지
			 */
			if(list == null || list.isEmpty()){
				iMaxPos_TL = 0;
			}else{
				iMaxPos_TL = list.size();
			}
			
			if(iPos_TL > iMaxPos_TL){
				iPos_TL = 1;
			}
			
//			if( list != null && !list.isEmpty()){
//				
//				/* 조회한 모든 row를 fetch하지 않고 이번에 반환할 순서에 해당하는 노드까지만 fetch 하여
//				 * 아래 반복문 종료시 이번에 반환할 순서에 해당하는 노드 값이 szNodeCd에 저장		 */
//				for(int i=0; i<iPos_TL; i++){
//					szNodeCd	= list.get(i);
//				}
//				iPos_TL++;
//			}
			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
    	return szNodeCd;
    }

}
