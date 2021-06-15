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
import org.apache.poi.util.SystemOutLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.NosqlCacheType;
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
public class GetNSMakeVSIServiceImpl implements GetNSMakeVSIService {
//	private Log imcsLogger		= LogFactory.getLog("API_getNSMakeVSI");
//	private Log imcsComLogger		= LogFactory.getLog("API_Common_getNSMakeVSI");
	private Log imcsLogger		= LogFactory.getLog("API_getNSVSI");
	private Log imcsComLogger		= LogFactory.getLog("API_Common_getNSVSI");
	
	@Autowired
	private GetNSVSIDao getNSVSIDao;
	
	@Autowired
	private CommonService commonService;
	
//	@Autowired
//	private NosqlResultCache cache;
//	@Resource
//	public void setNosqlResultCache (NosqlResultCache nosqlResultCache) {
//		this.cache = nosqlResultCache;
//	}
	
//	public void getNSMakeVSIs(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//		this.imcsMkLog	= new IMCSLog(imcsComLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
//	private IMCSLog imcsMkLog = null;
	private String apiInfo	= "";
	
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
	
	//2018.06.08 - TV앱의 경우 메인채널과 , MPTS 채널의 경우 3번째 m3u8파일명을 4번째에 동일하게 넣어달라는 요청으로 인한 처리
	//	   조건 : TV앱이고, 4번째 m3u8파일이 없어야 하며, 3번째 m3u8이 존재하면, 3번째 m3u8파일명을 4번째 자리에 동일하게 채워준다.
	//위와 같은 조건을 하기 위해 만든 변수
	int tvapp_flag = 0;
	String tvapp_castis_m3u8 = "";
	String tvapp_onnuri_m3u8 = "";
	String tvapp_m3u8_info = "";
	
	@Override
	public GetNSVSIResultVO getNSMakeVSIs(GetNSVSIRequestVO paramVO)	{
//		this.getNSMakeVSIs(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		GetNSVSIResultVO	resultListVO	= new GetNSVSIResultVO();
		this.apiInfo	= ImcsConstants.API_PRO_ID032_w;
		
		resultListVO	= this.getNSMakeVSI(paramVO, apiInfo);
		return resultListVO;
	}
	
	
	// getNSLists에서도 사용하기 위하여 모듈화
	
	public GetNSVSIResultVO getNSMakeVSI(GetNSVSIRequestVO paramVO, String apiInfo)	{
		
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		IMCSLog imcsMkLog	= new IMCSLog(imcsComLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		imcsLog.debugLog(methodName + " service call");
		
		//System.out.println("##############: getNSMakeVSI 로 진입한 경우!!");

		int nMainCnt = 0;
		int nSubCnt = 0;
		int nSbdelay_cnt = 0;

//		List<GetNSVSIResponseVO> resultVO		= new ArrayList<GetNSVSIResponseVO>();
//		GetNSVSIResponseVO tempVO				= new GetNSVSIResponseVO();
//		GetNSVSIResultVO	resultListVO		= new GetNSVSIResultVO();
		
		GetNSVSIResultVO resultListVO = new GetNSVSIResultVO();
		GetNSVSIResponseVO tempVO = new GetNSVSIResponseVO();
		List<GetNSVSIResponseVO> resultVO = new ArrayList<GetNSVSIResponseVO>();
		List<GetNSVSIResponseVO> returnVO = new ArrayList<GetNSVSIResponseVO>();
		
		
		List<ComProdInfoVO> lstProdInfo = null;
		List<ComNodeVO> lstNodeInfo = null;
		List<ComCdVO> lstLiveTimeSvrInfo = null;
		
		ComProdInfoVO prodInfoVO = null;
		ComNodeVO nodeVO = null;
		ComCdVO liveTimeSvrVO = null;
		
		String szChnlImgSvrip = "img_chnl_server";
		String VirtualChFlag	= "";		// 가상채널 여부
		
		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2
		long tp3			= 0;		// timePoint 3


		String msg			= "";
		
		long tp_start = System.currentTimeMillis();
		
		msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID032_w) + "] sts[start] rcv[" + paramVO.getParam()+ "]";
		imcsLog.serviceLog(msg, methodName, methodLine);
		
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
			String foldering_dir = String.format("y%so%sp%sv%sf%ssn%s", paramVO.getYouthYn(), paramVO.getOrderGb(), paramVO.getPooqYn(), 
					paramVO.getHdtvViewGb(), paramVO.getFiveChYn(), paramVO.getSvcNode());
			
			LOCALPATH = commonService.getCachePath("LOCAL", ImcsConstants.API_PRO_ID032.split("/")[1], imcsLog);
			LOCALPATH = String.format("%s/%s", LOCALPATH, foldering_dir);
			
			szMsg = " LOCALPATH : " + LOCALPATH;
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			String NASPATH = "";
			
			NASPATH = commonService.getCachePath("NAS", ImcsConstants.API_PRO_ID032.split("/")[1], imcsLog);
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
			
			
			// 노드정보 조회
			/* 20161206 - CDN IP 노드 분산(와이파이일 때) */
			String szNodeCd = "";
			String szCacheNodeCd = "";
			
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
			
			if( "".equals(szNodeCd) ){
				
				if("Y".equals(paramVO.getBaseGb())){
					if( "W".equals( paramVO.getBaseOneCd().toUpperCase() ) ){
						iMaxPos = 0;
					}
					else if( "AW".equals( paramVO.getBaseCd().substring(0, 2).toUpperCase() ) ){
						iMaxPos_AW = 0;
					}
					else if( "AL".equals( paramVO.getBaseCd().substring(0, 2).toUpperCase() ) ){
						iMaxPos_AL = 0;
					}
					else if( "TW".equals( paramVO.getBaseCd().substring(0, 2).toUpperCase()) ){		
						iMaxPos_TW = 0;
					}
					else if( "TL".equals( paramVO.getBaseCd().substring(0, 2).toUpperCase()) ){	
						iMaxPos_TL = 0;
					}
				}
			}
			
			paramVO.setNodeCd(szCacheNodeCd);
			
			//String compFileName = paramVO.getNscType()+"_y"+paramVO.getYouthYn()+"_o"+paramVO.getOrderGb()+"_t"+paramVO.getTestSbc()
			//		+"_p"+paramVO.getPooqYn()+"_m"+paramVO.getmProdId()+"_B"+paramVO.getBaseGb()+"_"+paramVO.getNodeCd()+"_v"+paramVO.getHdtvViewGb()+"_f"+paramVO.getFiveChYn();
			//String compFileName = "_n" + paramVO.getNscType()+"_y"+paramVO.getYouthYn()+"_o"+paramVO.getOrderGb()+"_t"+paramVO.getTestSbc()
			//+"_p"+paramVO.getPooqYn()+"_m"+paramVO.getmProdId()+"_B"+paramVO.getBaseGb()+"_"+ paramVO.getNodeCd()+"_v"+paramVO.getHdtvViewGb()+"_f"+paramVO.getFiveChYn();
			String compFileName = "_n" + paramVO.getNscType()+"_y"+paramVO.getYouthYn()+"_o"+paramVO.getOrderGb()+"_t"+paramVO.getTestSbc()
			+"_p"+paramVO.getPooqYn()+"_m"+paramVO.getmProdId()+"_B"+paramVO.getBaseGb()+"_"+szCacheNodeCd+"_v"+paramVO.getHdtvViewGb()+"_f"+paramVO.getFiveChYn();
			
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
				szMsg = " getNSMakeVSI Cache File Empty";
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
					}/* else if(fLOCKFILE.exists()){
						int nWaitCnt	= 0;
						
						//while(FileUtil.lock(LOCKFILE, imcsLog)){
						while(fLOCKFILE.exists()){
							nWaitCnt++;
							
							if(nWaitCnt >= 5) {
								szMsg = " wait_count overload Failed svc2[" + ImcsConstants.API_PRO_ID032_w + "]";
								imcsLog.serviceLog(szMsg, methodName, methodLine);

					            return resultListVO;
							}
							
							if(fNASFILE.exists()) {
								imcsLog.serviceLog(" File [" + NASFILE + "] exist ", methodName, methodLine);
								
								try {
									String[] szCommand = {"/bin/sh", "-c", "chmod 666 " + NASFILE};
									Process p = Runtime.getRuntime().exec(szCommand);
									
									szMsg = " File [" + NASFILE + "] chmod 666";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
									
								} catch (Exception e) {
									szMsg = " cache chmod 666 error!!!";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
								}
								
								try {
									String[] szCommand2 = {"/bin/sh", "-c", "cp " + NASFILE + " " + RESFILE};
									Process p = Runtime.getRuntime().exec(szCommand2);
								
									szMsg = " File [" + NASFILE + "] copy [" + RESFILE + "]";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
									
								} catch (Exception e) {
									szMsg = " cp cache error!!!";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
								}
								
								
								String result = FileUtil.fileRead(NASFILE, "UTF-8");
								resultListVO.setResult(result);
								
								FileUtil.unlock(LOCKFILE, imcsLog);
								
								delCacheFile(NASPATH, compFileName);	
								
								delCacheFile(LOCALPATH, compFileName);	
								
					            return resultListVO;
							} else {
								imcsLog.serviceLog("File [" + NASFILE + "] not exist", methodName, methodLine);
							}
						}
					}
					
					if(fRESFILE.exists() && fRESFILE.length() == 0){
						fRESFILE.delete();
					}
					
					if(fRESFILE.exists() && !fLOCALLOCKFILE.exists()){
						try {
							String[] szCommand = {"/bin/sh", "-c", "cp " + RESFILE + " " + NASFILE};
							Process p = Runtime.getRuntime().exec(szCommand);
							
							szMsg = " File [" + RESFILE + "] copy [" + NASFILE + "]";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
							
							String result = FileUtil.fileRead(RESFILE, "UTF-8");
							resultListVO.setResult(result);
							
							FileUtil.unlock(LOCALLOCKFILE, imcsLog);
							
							delCacheFile(NASPATH, compFileName);							
							
				            return resultListVO;
						} catch(Exception e) {
							szMsg = " cp cache error!!! ";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
						}
					}else if(!fRESFILE.exists() && !fLOCALLOCKFILE.exists()){
						int nWaitCnt	= 0;
						
						while(FileUtil.lock(LOCALLOCKFILE, imcsLog)){
							nWaitCnt++;
							
							if(nWaitCnt >= 5) {
								szMsg = " wait_count overload Failed svc2[" + ImcsConstants.API_PRO_ID032_w + "]";
								imcsLog.serviceLog(szMsg, methodName, methodLine);

					            return resultListVO;
							}
							
							if(fRESFILE.exists()) {
								imcsLog.serviceLog(" File [" + RESFILE + "] exist ", methodName, methodLine);
								
								try {
									String[] szCommand = {"/bin/sh", "-c", "chmod 666 " + RESFILE};
									Process p = Runtime.getRuntime().exec(szCommand);
									
									szMsg = " File [" + RESFILE + "] chmod 666";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
									
								} catch (Exception e) {
									szMsg = " cache chmod 666 error!!!";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
								}
								
								try {
									String[] szCommand2 = {"/bin/sh", "-c", "cp " + RESFILE + " " + NASFILE};
									Process p = Runtime.getRuntime().exec(szCommand2);
								
									szMsg = " File [" + RESFILE + "] copy [" + NASFILE + "]";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
									
								} catch (Exception e) {
									szMsg = " cp cache error!!!";
									imcsLog.serviceLog(szMsg, methodName, methodLine);
								}
								
								
								String result = FileUtil.fileRead(RESFILE, "UTF-8");
								resultListVO.setResult(result);
								
								FileUtil.unlock(LOCALLOCKFILE, imcsLog);
								
								delCacheFile(LOCALPATH, compFileName);	
								
								delCacheFile(NASPATH, compFileName);	
								
					            return resultListVO;
							} else {
								imcsLog.serviceLog("File [" + RESFILE + "] not exist", methodName, methodLine);
							}
						}
					}*/
					
					
				}
			}else{
				szMsg = " NAS File : NO FILE";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
				
				LOCKFILE = NASPATH + "/getNSVSI_" + current_time + compFileName + ".lock";				
				LOCALLOCKFILE = LOCALPATH + "/getNSVSI_" + current_time + compFileName + ".lock";				
				NASFILE = NASPATH + "/getNSVSI_" + current_time + compFileName + ".res";
				RESFILE = LOCALPATH + "/getNSVSI_" + current_time + compFileName + ".res";
			}
			
			fRESFILE	= new File(RESFILE);
			fLOCALLOCKFILE   = new File(LOCALLOCKFILE);
			fNASFILE	= new File(NASFILE);
			fLOCKFILE   = new File(LOCKFILE);
			
			boolean procStatus = false;
			
			if(NAS_DIR.exists()){
				if (!fNASFILE.exists() && !fLOCKFILE.exists()) {
					
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
				}else {
					procStatus = false;
				}
			}else{
				szMsg = " queryLock Fail, [NAS Directory Not Exist] " + NASPATH + " query execution itself";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
				procStatus = false;
			}
			
			if (!fRESFILE.exists() && !fLOCALLOCKFILE.exists()) {
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
				
			} else {
				procStatus = false;
			}

			if(procStatus){	
				long tt2 = System.currentTimeMillis();
				System.out.println("#################패치 로직으로 들어감!!");
				imcsLog.serviceLog("#################패치 로직으로 들어감!!", methodName, methodLine);
				
				szMsg = " LOCALLOCKFILE";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
				try {
					VirtualChFlag	= commonService.getVCFlag(ImcsConstants.API_PRO_ID031.split("/")[1]);	// 가상채널 여부
				} catch (Exception e) {
					System.out.println("GetConfigInfo fail");
				}
				
				paramVO.setVirtualChFlag(VirtualChFlag);
				
				String live_time_server1  ="";
				String live_time_server2 = "";
				String live_time_server3 = "";
				
				// 실시간 서버 조회
				lstLiveTimeSvrInfo = this.getLiveTimeServer(paramVO);
	
				if(lstLiveTimeSvrInfo != null && lstLiveTimeSvrInfo.size() > 0){
					nMainCnt = lstLiveTimeSvrInfo.size();
				}
				long tt3 = System.currentTimeMillis();
				
				for(int i = 0; i < nMainCnt; i++){
					liveTimeSvrVO = lstLiveTimeSvrInfo.get(i);
					
					
					if(paramVO.getSvcNode().equals("R")){
						if("04".equals(liveTimeSvrVO.getComCd())){
							live_time_server1 = liveTimeSvrVO.getComName();
						}else if("05".equals(liveTimeSvrVO.getComCd())){
							live_time_server2 = liveTimeSvrVO.getComName();
						}else if("06".equals(liveTimeSvrVO.getComCd())){
							live_time_server3 = liveTimeSvrVO.getComName();
						}
					} else if(paramVO.getSvcNode().equals("T")){
						if("07".equals(liveTimeSvrVO.getComCd())){
							live_time_server1 = liveTimeSvrVO.getComName();
						}else if("08".equals(liveTimeSvrVO.getComCd())){
							live_time_server2 = liveTimeSvrVO.getComName();
						}else if("09".equals(liveTimeSvrVO.getComCd())){
							live_time_server3 = liveTimeSvrVO.getComName();
						}
					}else{
						if("01".equals(liveTimeSvrVO.getComCd())){
							live_time_server1 = liveTimeSvrVO.getComName();
						}else if("02".equals(liveTimeSvrVO.getComCd())){
							live_time_server2 = liveTimeSvrVO.getComName();
						}else if("03".equals(liveTimeSvrVO.getComCd())){
							live_time_server3 = liveTimeSvrVO.getComName();
						}
					}
				}
				
				paramVO.setBaseCondi(paramVO.getNodeCd());
				
				lstNodeInfo = this.getNode(paramVO);
				
				if(lstNodeInfo != null && !lstNodeInfo.isEmpty()){
					nodeVO = lstNodeInfo.get(0);
					paramVO.setNodeCd(nodeVO.getNodeCd());
					paramVO.setrBaseCode(nodeVO.getrBaseCode());
				}
				
				
				if(lstNodeInfo == null || lstNodeInfo.isEmpty()){
					paramVO.setBaseCondi(paramVO.getBaseOneCd());
					
					//2018.08.01 권형도 (lgvod032_w.c line:1271)
					switch(paramVO.getSvcNode())
					{
					case "N":
						//strcpy(c_base_condi, rd1.c_base_one_cd);
						paramVO.setBaseCondi(paramVO.getBaseOneCd());
						break;
					case "R":
					case "T":
						//strncpy(c_base_condi, szCacheNodeCd, 2);
						paramVO.setBaseCondi(paramVO.getBaseCd());
						break;
					default:
						break;
					}
					
					
					if(!paramVO.getBaseOneCd().equals("A")){
						lstNodeInfo = this.getNode(paramVO);
						
						if(lstNodeInfo != null && !lstNodeInfo.isEmpty()){
							nodeVO = lstNodeInfo.get(0);
							
							paramVO.setNodeCd(nodeVO.getNodeCd());
							paramVO.setrBaseCode(nodeVO.getrBaseCode());
						}
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
				
				// 노드정보 조회
				/* 20161206 - CDN IP 노드 분산(와이파이일 때) */
				/*String szNodeCd = "";
				
				if("Y".equals(paramVO.getBaseGb())){
					if( "W".equals( paramVO.getBaseOneCd().toUpperCase() ) ){
										
						szNodeCd = this.getNodeCdW(paramVO);
						
						paramVO.setNodeCd(szNodeCd);
											
					}
				}
				
				if( "".equals(szNodeCd) ){
					
					if("Y".equals(paramVO.getBaseGb())){
						if( "W".equals( paramVO.getBaseOneCd().toUpperCase() ) ){
							iMaxPos = 0;
						}
					}
					
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
					
				}*/
				
				
				// 동 여부 조회
				String szDongYn = this.getDongYn(paramVO);
				
				if(szDongYn != null && !"".equals(szDongYn))	paramVO.setDongYn("Y");
				else											paramVO.setDongYn("N");
				
				msg = " [파라미터확인]  c_order_gb["+paramVO.getOrderGb()+"] c_base_gb["+paramVO.getBaseGb()+"] c_dong_yn["+paramVO.getDongYn()+"] c_test_sbc["+paramVO.getTestSbc()+"] "
						+ "c_nsc_type["+paramVO.getNscType()+"] c_base_code["+paramVO.getBaseCondi()+"] c_node_cd["+paramVO.getNodeCd()+"]";
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				msg = " [파라미터확인]  c_hdtv_view_gb["+paramVO.getHdtvViewGb()+"] c_youth_yn_com["+paramVO.getYouthYnCom()+"] c_pooq_yn_com["+paramVO.getPooqYnCom()+"]";
				imcsLog.serviceLog(msg, methodName, methodLine);
						
			
				// EPG 전체 채널정보 조회
				nMainCnt = 0;
				
				List<GetNSVSIResponseVO> sbdelayList = new ArrayList<GetNSVSIResponseVO>();
				GetNSVSIResponseVO sbdelayVO = new GetNSVSIResponseVO();
				nSbdelay_cnt = 0;
				
				try {
					if("H".equals(paramVO.getOrderGb())) {					// 인기순으로  Nscreen 가상채널 EPG전체 채널정보 조회
						resultVO = getNSVSIDao.getNSVSIListH(paramVO);
					} else if("N".equals(paramVO.getOrderGb())) {			// 기본으로  Nscreen 가상채널 EPG전체 채널정보 조회				
						resultVO = getNSVSIDao.getNSVSIListN(paramVO);				
					}else if("A".equals(paramVO.getOrderGb())){				// 제목순으로  Nscreen 가상채널 EPG전체 채널정보 조회
						resultVO = getNSVSIDao.getNSVSIListA(paramVO);
					}
					
					if( "N".equals(paramVO.getBaseGb()) && "Y".equals(paramVO.getDongYn()) ){
						//paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.HBASE_DB.ordinal()]++;
					}else if( "N".equals(paramVO.getBaseGb()) && "N".equals(paramVO.getDongYn()) ){
						//paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.REDIS_DB.ordinal()]++;
					}else if( "Y".equals(paramVO.getBaseGb()) ){
						//paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.HBASE_DB.ordinal()]++;
					}else{
						//paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.REDIS_DB.ordinal()]++;
					}
					
					if(resultVO != null)	nMainCnt = resultVO.size();
					
					//System.out.println("nMainCnt: "+nMainCnt);
					
				} catch (Exception e) {
					//imcsLog.failLog(ImcsConstants.API_PRO_ID032, "", null, "favor_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
					paramVO.setResultCode("40000000");
	
					throw new ImcsException();
				}
				
				if(resultVO == null || resultVO.isEmpty()){
					//imcsLog.failLog(ImcsConstants.API_PRO_ID032, "", null, "favor_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
					paramVO.setResultCode("21000000");
				}
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("채널 조회", String.valueOf((tp2 - tp1)), methodName, methodLine);
				
				long tp_in1 = 0;
				long tp_in2 = 0;
				
				int sort_cnt = 0;
				int sbdelay_cnt = 0;
				//2018.06.11 - TV앱일 때 3번째 m3u8파일명을 4번째에 동일하게 복사하기 위한 변수
				String c_tvapp_castis_m3u8 = "";
				String c_tvapp_onnuri_m3u8 = "";
				String c_tvapp_m3u8_info   = "";
				
				//region #for
				for(int i = 0; i < nMainCnt; i++){
					tp_in1	= System.currentTimeMillis();
					tempVO = resultVO.get(i);
					
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
					
					List<M3u8ProfileVO> lst_ChnlProfile = this.Chnl_m3u8_search(resultVO.get(i), paramVO);
					int i_m3u8_cnt = lst_ChnlProfile.size();
					
					int m3u8_order_chk = 1; // 2018.04.26 - m3u8파일 order(순서)가 중간에 비는지 확인하기 위한 변수 (비어있을 경우 빈공백 배열값을 넣는다.)

					// 시보딜레이 처리 (메인 채널에 필드 채워넣기 위해)
					if("8".equals(tempVO.getHdtvViewGb())){
						sbdelayList = new ArrayList<GetNSVSIResponseVO>();
						
						sbdelayVO = tempVO;
						sbdelayVO.setServiceId(tempVO.getmsvcId());
						
						sort_cnt = 3;	// high,low는 기존 응답값에 내리므로 3<hevc>부터 신규 배열 응답값에 내리기 위해 사용
						
						for (int j=0; j<i_m3u8_cnt; j++) {
			        		/***************************************************************************************************
			        		 * 2018.01.16 - 프로야구2.0
			        		 *              ord_num --> 1 : high, 2 : low, 3 : hevc ...
			        		 *              ord_num이 1,2일 땐 기존 응답값에 그대로 전달하고, 그 이상일 때에는 신규 응답값에 배열로 내린다.
			        		 ***************************************************************************************************/
							if (lst_ChnlProfile.get(j).getOrderNum() == 1) {
								switch(tempVO.getVodServer1Type())
								{
									case "1":
										sbdelayVO.setLiveFileName1(lst_ChnlProfile.get(j).getCastisM3u8());
										break;
									case "2":
										sbdelayVO.setLiveFileName1(lst_ChnlProfile.get(j).getOnnuriM3u8());
										break;
									default:
										break;
								}

								switch(tempVO.getVodServer2Type())
								{
									case "1":
										sbdelayVO.setLiveFileName2(lst_ChnlProfile.get(j).getCastisM3u8());
										break;
									case "2":
										sbdelayVO.setLiveFileName2(lst_ChnlProfile.get(j).getOnnuriM3u8());
										break;
									default:
										break;
								}

								switch(tempVO.getVodServer3Type())
								{
									case "1":
										sbdelayVO.setLiveFileName3(lst_ChnlProfile.get(j).getCastisM3u8());
										break;
									case "2":
										sbdelayVO.setLiveFileName3(lst_ChnlProfile.get(j).getOnnuriM3u8());
										break;
									default:
										break;
								}								
							} else if (lst_ChnlProfile.get(j).getOrderNum() == 2) {
								switch(tempVO.getVodServer1Type())
								{
									case "1":
										sbdelayVO.setLiveFileName4(lst_ChnlProfile.get(j).getCastisM3u8());
										break;
									case "2":
										sbdelayVO.setLiveFileName4(lst_ChnlProfile.get(j).getOnnuriM3u8());
										break;
									default:
										break;
								}

								switch(tempVO.getVodServer2Type())
								{
									case "1":
										sbdelayVO.setLiveFileName5(lst_ChnlProfile.get(j).getCastisM3u8());
										break;
									case "2":
										sbdelayVO.setLiveFileName5(lst_ChnlProfile.get(j).getOnnuriM3u8());
										break;
									default:
										break;
								}

								switch(tempVO.getVodServer3Type())
								{
									case "1":
										sbdelayVO.setLiveFileName6(lst_ChnlProfile.get(j).getCastisM3u8());
										break;
									case "2":
										sbdelayVO.setLiveFileName6(lst_ChnlProfile.get(j).getOnnuriM3u8());
										break;
									default:
										break;
								}								
							} else {
								//2018.06.08 - TV앱의 경우 메인채널과 , MPTS 채널의 경우 3번째 m3u8파일명을 4번째에 동일하게 넣어달라는 요청으로 인한 처리
								//			   조건 : TV앱이고, 4번째 m3u8파일이 없어야 하며, 3번째 m3u8이 존재하면, 3번째 m3u8파일명을 4번째 자리에 동일하게 채워준다.
								if (paramVO.getSvcNode().equals("T")) {
									if (lst_ChnlProfile.get(j).getOrderNum() == 5) {
										tvapp_flag = 1;
										c_tvapp_castis_m3u8 = lst_ChnlProfile.get(j).getCastisM3u8();
										c_tvapp_onnuri_m3u8 = lst_ChnlProfile.get(j).getOnnuriM3u8();
									} else if (lst_ChnlProfile.get(j).getOrderNum() == 6) {
										tvapp_flag = 0;
									} else if (lst_ChnlProfile.get(j).getOrderNum() > 6) {
										if(tvapp_flag == 1)
										{
											sbdelayVO.setLiveFileNameL(sbdelayVO.getLiveFileNameL() + "\b");
											sbdelayVO.setLiveFileNameN(sbdelayVO.getLiveFileNameN() + "\b");
											sbdelayVO.setLiveFileNameC(sbdelayVO.getLiveFileNameC() + "\b");

											switch(tempVO.getVodServer1Type())
											{
												case "1":
													sbdelayVO.setLiveFileNameL(sbdelayVO.getLiveFileNameL() + c_tvapp_castis_m3u8);
													break;
												case "2":
													sbdelayVO.setLiveFileNameL(sbdelayVO.getLiveFileNameL() + c_tvapp_onnuri_m3u8);
													break;
												default:
													break;
											}

											switch(tempVO.getVodServer2Type())
											{
												case "1":
													sbdelayVO.setLiveFileNameN(sbdelayVO.getLiveFileNameN() + c_tvapp_castis_m3u8);
													break;
												case "2":
													sbdelayVO.setLiveFileNameN(sbdelayVO.getLiveFileNameN() + c_tvapp_onnuri_m3u8);
													break;
												default:
													break;
											}

											switch(tempVO.getVodServer3Type())
											{
												case "1":
													sbdelayVO.setLiveFileNameC(sbdelayVO.getLiveFileNameC() + c_tvapp_castis_m3u8);
													break;
												case "2":
													sbdelayVO.setLiveFileNameC(sbdelayVO.getLiveFileNameC() + c_tvapp_onnuri_m3u8);
													break;													
												default:
													break;
											}

											sort_cnt = 6;
										}
										tvapp_flag = 0;	
									}
								}
								
								for(  ; sort_cnt < lst_ChnlProfile.get(j).getOrderNum() ; sort_cnt++)
								{
									sbdelayVO.setLiveFileNameL(sbdelayVO.getLiveFileNameL() + "\b");
									sbdelayVO.setLiveFileNameN(sbdelayVO.getLiveFileNameN() + "\b");
									sbdelayVO.setLiveFileNameC(sbdelayVO.getLiveFileNameC() + "\b");
								}
								
								switch(tempVO.getVodServer1Type())
								{
									case "1":
										sbdelayVO.setLiveFileNameL(sbdelayVO.getLiveFileNameL() + lst_ChnlProfile.get(j).getCastisM3u8());
										break;
									case "2":
										sbdelayVO.setLiveFileNameL(sbdelayVO.getLiveFileNameL() + lst_ChnlProfile.get(j).getOnnuriM3u8());
										break;
									default:
										break;
								}

								switch(tempVO.getVodServer2Type())
								{
									case "1":
										sbdelayVO.setLiveFileNameN(sbdelayVO.getLiveFileNameN() + lst_ChnlProfile.get(j).getCastisM3u8());
										break;
									case "2":
										sbdelayVO.setLiveFileNameN(sbdelayVO.getLiveFileNameN() + lst_ChnlProfile.get(j).getOnnuriM3u8());										
										break;
									default:
										break;
								}

								switch(tempVO.getVodServer3Type())
								{
									case "1":
										sbdelayVO.setLiveFileNameC(sbdelayVO.getLiveFileNameC() + lst_ChnlProfile.get(j).getCastisM3u8());
										break;
									case "2":
										sbdelayVO.setLiveFileNameC(sbdelayVO.getLiveFileNameC() + lst_ChnlProfile.get(j).getOnnuriM3u8());
										break;
									default:
										break;
								}
								sort_cnt = lst_ChnlProfile.get(j).getOrderNum();
							}
						}
						
			        	if(paramVO.getSvcNode().equals("T"))
			        	{
			        		if(tvapp_flag == 1)
							{
								sbdelayVO.setLiveFileNameL(sbdelayVO.getLiveFileNameL() + "\b");
								sbdelayVO.setLiveFileNameN(sbdelayVO.getLiveFileNameN() + "\b");
								sbdelayVO.setLiveFileNameC(sbdelayVO.getLiveFileNameC() + "\b");

								switch(tempVO.getVodServer1Type())
								{
									case "1":
										sbdelayVO.setLiveFileNameL(sbdelayVO.getLiveFileNameL() + c_tvapp_castis_m3u8);
										break;
									case "2":
										sbdelayVO.setLiveFileNameL(sbdelayVO.getLiveFileNameL() + c_tvapp_onnuri_m3u8);
										break;
									default:
										break;
								}

								switch(tempVO.getVodServer2Type())
								{
								case "1":
									sbdelayVO.setLiveFileNameN(sbdelayVO.getLiveFileNameN() + c_tvapp_castis_m3u8);
									break;
								case "2":
									sbdelayVO.setLiveFileNameN(sbdelayVO.getLiveFileNameN() + c_tvapp_onnuri_m3u8);
									break;
								default:
									break;
								}

								switch(tempVO.getVodServer3Type())
								{
								case "1":
									sbdelayVO.setLiveFileNameC(sbdelayVO.getLiveFileNameC() + c_tvapp_castis_m3u8);
									break;
								case "2":
									sbdelayVO.setLiveFileNameC(sbdelayVO.getLiveFileNameC() + c_tvapp_onnuri_m3u8);
									break;
								default:
									break;
								}
							}
			        		tvapp_flag = 0;
			        	}
			        	
			        	sbdelayList.add(sbdelayVO);
						sbdelay_cnt++;
						continue;
						
					}
					
					if( "N".equals(paramVO.getTestSbc()) && "999".equals(tempVO.getSortNo()) ){
						continue;
					}
					
					//System.out.println("nSbdelay_cnt: " + nSbdelay_cnt);
					//System.out.println("sbdelayList.size(): " + sbdelayList.size());
					for(int ii=0; ii<sbdelayList.size(); ii++){
						//System.out.println("zzzz:" + tempVO.getServiceId() + " " + sbdelayList.get(ii).getServiceId() + " " + sbdelayList.get(ii).getServiceRefId());
						if( tempVO.getServiceId().equals( sbdelayList.get(ii).getServiceId() ) ){
							tempVO.setTsFileName1(sbdelayList.get(ii).getLiveFileName1());
							tempVO.setTsFileName2(sbdelayList.get(ii).getLiveFileName2());
							tempVO.setTsFileName3(sbdelayList.get(ii).getLiveFileName3());
							tempVO.setTsFileName4(sbdelayList.get(ii).getLiveFileName4());
							tempVO.setTsFileName5(sbdelayList.get(ii).getLiveFileName5());
							tempVO.setTsFileName6(sbdelayList.get(ii).getLiveFileName6());
							
							tempVO.setTsLowFileName1(sbdelayList.get(ii).getLiveFileName4());
							tempVO.setTsLowFileName2(sbdelayList.get(ii).getLiveFileName5());
							tempVO.setTsLowFileName3(sbdelayList.get(ii).getLiveFileName6());
							
							//2018.01.16 - 프로야구2.0 hevc화질 이후로는 배열로 응답값을 내림
							tempVO.setTsFileNameL(sbdelayList.get(ii).getLiveFileNameL());
							tempVO.setTsFileNameN(sbdelayList.get(ii).getLiveFileNameN());
							tempVO.setTsFileNameC(sbdelayList.get(ii).getLiveFileNameC());
							
						}
					}
					
					tvapp_castis_m3u8 = "";
					tvapp_onnuri_m3u8 = "";
					sort_cnt = 3; // high,low는 기존 응답값에 내리므로 3<hevc>부터 신규 배열 응답값에 내리기 위해 사용
					tvapp_flag = 0;
					
					for(int jj = 0 ; jj < i_m3u8_cnt ; jj++)
					{
						/***************************************************************************************************
						 * 2018.01.16 - 프로야구2.0
						 *              ord_num --> 1 : high, 2 : low, 3 : hevc ...
						 *              ord_num이 1,2일 땐 기존 응답값에 그대로 전달하고, 그 이상일 때에는 신규 응답값에 배열로 내린다.
						 ***************************************************************************************************/
						if(lst_ChnlProfile.get(jj).getOrderNum() == 1)
						{
							switch(tempVO.getVodServer1Type())
							{
								case "1":
									tempVO.setLiveFileName1(lst_ChnlProfile.get(jj).getCastisM3u8());
									break;
								case "2":
									tempVO.setLiveFileName1(lst_ChnlProfile.get(jj).getOnnuriM3u8());
									break;
								default:
									break;
							}

							switch(tempVO.getVodServer2Type())
							{
								case "1":
									tempVO.setLiveFileName2(lst_ChnlProfile.get(jj).getCastisM3u8());
									break;
								case "2":
									tempVO.setLiveFileName2(lst_ChnlProfile.get(jj).getOnnuriM3u8());
									break;
								default:
									break;
							}

							switch(tempVO.getVodServer3Type())
							{
								case "1":
									tempVO.setLiveFileName3(lst_ChnlProfile.get(jj).getCastisM3u8());
									break;
								case "2":
									tempVO.setLiveFileName3(lst_ChnlProfile.get(jj).getOnnuriM3u8());
									break;
								default:
									break;
							}
						}
						else if(lst_ChnlProfile.get(jj).getOrderNum() == 2)
						{
							switch(tempVO.getVodServer1Type())
							{
								case "1":
									tempVO.setLiveFileName4(lst_ChnlProfile.get(jj).getCastisM3u8());
									break;
								case "2":
									tempVO.setLiveFileName4(lst_ChnlProfile.get(jj).getOnnuriM3u8());
									break;
								default:
									break;
							}

							switch(tempVO.getVodServer2Type())
							{
								case "1":
									tempVO.setLiveFileName5(lst_ChnlProfile.get(jj).getCastisM3u8());
									break;
								case "2":
									tempVO.setLiveFileName5(lst_ChnlProfile.get(jj).getOnnuriM3u8());
									break;
								default:
									break;
							}

							switch(tempVO.getVodServer3Type())
							{
								case "1":
									tempVO.setLiveFileName6(lst_ChnlProfile.get(jj).getCastisM3u8());
									break;
								case "2":
									tempVO.setLiveFileName6(lst_ChnlProfile.get(jj).getOnnuriM3u8());
									break;
								default:
									break;
							}
						}
						else
						{
							//2018.06.08 - TV앱의 경우 메인채널과 , MPTS 채널의 경우 3번째 m3u8파일명을 4번째에 동일하게 넣어달라는 요청으로 인한 처리
							//			   조건 : TV앱이고, 4번째 m3u8파일이 없어야 하며, 3번째 m3u8이 존재하면, 3번째 m3u8파일명을 4번째 자리에 동일하게 채워준다.
							if(paramVO.getSvcNode().equals("T"))
							{
								if(lst_ChnlProfile.get(jj).getOrderNum() == 5)
								{
									tvapp_flag = 1;
									c_tvapp_castis_m3u8 = lst_ChnlProfile.get(jj).getCastisM3u8();
									c_tvapp_onnuri_m3u8 = lst_ChnlProfile.get(jj).getOnnuriM3u8();
								}
								else if(lst_ChnlProfile.get(jj).getOrderNum() == 6)
								{
									tvapp_flag = 0;
								}
								else if(lst_ChnlProfile.get(jj).getOrderNum() > 6)
								{
									if(tvapp_flag == 1)
									{
										c_tvapp_m3u8_info = tempVO.getM3u8Info();
										//if(c_tvapp_m3u8_info[2] == '1')
										//{
										//	c_tvapp_m3u8_info[3] = '1';
										//	sprintf((char*)lst_ChnlList.c_m3u8_info.arr, "%s", c_tvapp_m3u8_info);
										//	tempVO.setM3u8Info(c_tvapp_m3u8_info);
										//}

										tempVO.setLiveFileNameL(tempVO.getLiveFileNameL() + "\b");
										tempVO.setLiveFileNameN(tempVO.getLiveFileNameN() + "\b");
										tempVO.setLiveFileNameC(tempVO.getLiveFileNameC() + "\b");

										switch(tempVO.getVodServer1Type())
										{
											case "1":
												tempVO.setLiveFileNameL(tempVO.getLiveFileNameL() + c_tvapp_castis_m3u8);
												break;
											case "2":
												tempVO.setLiveFileNameL(tempVO.getLiveFileNameL() + c_tvapp_onnuri_m3u8);
												break;
											default:
												break;
										}

										switch(tempVO.getVodServer2Type())
										{
											case "1":
												tempVO.setLiveFileNameN(tempVO.getLiveFileNameN() + c_tvapp_castis_m3u8);
												break;
											case "2":
												tempVO.setLiveFileNameN(tempVO.getLiveFileNameN() + c_tvapp_onnuri_m3u8);
												break;
											default:
												break;
										}

										switch(tempVO.getVodServer3Type())
										{
											case "1":
												tempVO.setLiveFileNameC(tempVO.getLiveFileNameC() + c_tvapp_castis_m3u8);
												break;
											case "2":
												tempVO.setLiveFileNameC(tempVO.getLiveFileNameC() + c_tvapp_onnuri_m3u8);
												break;
											default:
												break;
										}
										sort_cnt = 6;
									}
									tvapp_flag = 0;
								}
							}

							//여기문제임
							for(  ; sort_cnt < lst_ChnlProfile.get(jj).getOrderNum() ; sort_cnt++)
							{
								System.out.println("sort_cnt:" + sort_cnt);
								System.out.println("order_num:" + lst_ChnlProfile.get(jj).getOrderNum());
								tempVO.setLiveFileNameL(tempVO.getLiveFileNameL() + "\b");
								tempVO.setLiveFileNameN(tempVO.getLiveFileNameN() + "\b");
								tempVO.setLiveFileNameC(tempVO.getLiveFileNameC() + "\b");
							}

							switch(tempVO.getVodServer1Type())
							{
								case "1":
									tempVO.setLiveFileNameL(tempVO.getLiveFileNameL() + lst_ChnlProfile.get(jj).getCastisM3u8());
									break;
								case "2":
									tempVO.setLiveFileNameL(tempVO.getLiveFileNameL() + lst_ChnlProfile.get(jj).getOnnuriM3u8());
									break;
								default:
									break;
							}

							switch(tempVO.getVodServer2Type())
							{
								case "1":
									tempVO.setLiveFileNameN(tempVO.getLiveFileNameN() + lst_ChnlProfile.get(jj).getCastisM3u8());
									break;
								case "2":
									tempVO.setLiveFileNameN(tempVO.getLiveFileNameN() + lst_ChnlProfile.get(jj).getOnnuriM3u8());
									break;
								default:
									break;
							}

							switch(tempVO.getVodServer3Type())
							{
								case "1":
									tempVO.setLiveFileNameC(tempVO.getLiveFileNameC() + lst_ChnlProfile.get(jj).getCastisM3u8());
									break;
								case "2":
									tempVO.setLiveFileNameC(tempVO.getLiveFileNameC() + lst_ChnlProfile.get(jj).getOnnuriM3u8());
									break;
								default:
									break;
							}
							sort_cnt = lst_ChnlProfile.get(jj).getOrderNum();
						}
					}
					
					if(paramVO.getSvcNode().equals("T"))
					{
						if(tvapp_flag == 1)
						{
							c_tvapp_m3u8_info = tempVO.getM3u8Info();
							//if(c_tvapp_m3u8_info[2] == '1')
							//{
							//	c_tvapp_m3u8_info[3] = '1';
							//	sprintf((char*)lst_ChnlList.c_m3u8_info.arr, "%s", c_tvapp_m3u8_info);
							//}

							tempVO.setLiveFileNameL(tempVO.getLiveFileNameL() + "\b");
							tempVO.setLiveFileNameN(tempVO.getLiveFileNameN() + "\b");
							tempVO.setLiveFileNameC(tempVO.getLiveFileNameC() + "\b");

							switch(tempVO.getVodServer1Type())
							{
								case "1":
									tempVO.setLiveFileNameL(tempVO.getLiveFileNameL() + c_tvapp_castis_m3u8);
									break;
								case "2":
									tempVO.setLiveFileNameL(tempVO.getLiveFileNameL() + c_tvapp_onnuri_m3u8);
									break;
								default:
									break;
							}

							switch(tempVO.getVodServer2Type())
							{
								case "1":
									tempVO.setLiveFileNameN(tempVO.getLiveFileNameN() + c_tvapp_castis_m3u8);
									break;
								case "2":
									tempVO.setLiveFileNameN(tempVO.getLiveFileNameN() + c_tvapp_onnuri_m3u8);
									break;
								default:
									break;
							}

							switch(tempVO.getVodServer3Type())
							{
								case "1":
									tempVO.setLiveFileNameC(tempVO.getLiveFileNameC() + c_tvapp_castis_m3u8);
									break;
								case "2":
									tempVO.setLiveFileNameC(tempVO.getLiveFileNameC() + c_tvapp_onnuri_m3u8);
									break;
								default:
									break;
							}
						}
						tvapp_flag = 0;
					}
					
					tp1	= System.currentTimeMillis();
					
					if(!"http:".equals(tempVO.getImgUrl())){
						tempVO.setImgUrl(szChnlImgSvrip);
					}
					
					// 채널구분이 0이 아니면(1:CJ채널이면) NULL을 내린다
					//if( !"0".equals(tempVO.getChnlGrp()) ){
					//	tempVO.setChnlGrp("");
					//}
					// 채널구분이 0이면(1:CJ채널이면) NULL을 내린다
					if( "0".equals(tempVO.getChnlGrp()) ){
						tempVO.setChnlGrp("");
					}
					
					
					
					// 장르명 조회
					String szComName = "";
					paramVO.setGenre1(tempVO.getGenre1());
					
					//long tp_sub21 = System.currentTimeMillis();
//					if (paramVO.getGenre1()==null) paramVO.setGenre1("");
//					if (!paramVO.getGenre1().equals(""))
					szComName = this.getComName(paramVO);
						
					//long tp_sub22 = System.currentTimeMillis();
					//System.out.println("getComName: " + String.valueOf(tp_sub22-tp_sub21));
					tempVO.setComName(szComName);
					
					
					
					// 앨범이 속한 상품 조회
					// 기존 API는 무조건 아래 SQL을 실행하나 가상채널의 경우 c_contents_id 가 없으므로 동작할 필요 없어서 체크 후 실행
					// SYSDATE 사용하나 년월일까지만 사용하므로 일자를 조건으로 추가하여 NOSQL 적용		
					if( !"".equals(tempVO.getContentsId()) ){
						String szProdId		= "";
						String szProdName	= "";
						
						nSubCnt = 0;
						
						paramVO.setContentsId(tempVO.getContentsId());
						//long tp_sub1 = System.currentTimeMillis();
						lstProdInfo = this.getProdInfo(paramVO);
						//long tp_sub2 = System.currentTimeMillis();
						//System.out.println("getProdInfo: " + String.valueOf(tp_sub2-tp_sub1));
					
						if(lstProdInfo != null){
							nSubCnt = lstProdInfo.size();
						}
						
						for(int j = 0; j < nSubCnt; j++){
							prodInfoVO = lstProdInfo.get(j);
							
							if(j == 0){
								szProdId	= prodInfoVO.getProdId();
								szProdName	= prodInfoVO.getProdName();
							}else{
								szProdId	= szProdId + ImcsConstants.ARRSEP + prodInfoVO.getProdId();
								szProdName	= szProdName + ImcsConstants.ARRSEP +  prodInfoVO.getProdName();
							}
							
						}
						
						tempVO.setProductId(szProdId);
						tempVO.setProductName(szProdName);
					}
					
	
					
					// 선호채널 여부 조회
					String szFavorYn = "";
					szFavorYn = this.getFavorYn(paramVO);
					if(szFavorYn != null && !"".equals(szFavorYn))
						tempVO.setFavorYn(szFavorYn);
					
					// 2018.02.28 - 프로야구2.0, 골프APP 서버IP 정보는 server1,2,3과 같으므로 동일하게 Set
					tempVO.setLiveServer1("http://" + tempVO.getLiveIp1() + ":" + tempVO.getLivePort() + "/");
					tempVO.setLiveServer2("http://" + tempVO.getLiveIp2() + ":" + tempVO.getLivePort() + "/");
					tempVO.setLiveServer3("http://" + tempVO.getLiveIp3() + ":" + tempVO.getLivePort() + "/");
					tempVO.setLiveServer4("http://" + tempVO.getLiveIp4() + ":" + tempVO.getLivePort() + "/");
					tempVO.setLiveServer5("http://" + tempVO.getLiveIp5() + ":" + tempVO.getLivePort() + "/");
					tempVO.setLiveServer6("http://" + tempVO.getLiveIp6() + ":" + tempVO.getLivePort() + "/");
					tempVO.setLiveServerL("http://" + tempVO.getLiveIp1() + ":" + tempVO.getLivePort() + "/");
					tempVO.setLiveServerN("http://" + tempVO.getLiveIp2() + ":" + tempVO.getLivePort() + "/");
					tempVO.setLiveServerC("http://" + tempVO.getLiveIp3() + ":" + tempVO.getLivePort() + "/");
					
					if("Y".equals(tempVO.getTimeAppYn())){
						tempVO.setLiveTimeServer1(live_time_server1);
						tempVO.setLiveTimeServer2(live_time_server2);
						tempVO.setLiveTimeServer3(live_time_server3);
						
						// 최소 SAVE_TIME 조회
						paramVO.setServiceId(tempVO.getServiceId());
						String szSaveTime = "";
						szSaveTime = this.getSaveTime(paramVO);
						tempVO.setSaveTime(szSaveTime);
						
					}else{
						tempVO.setLiveTimeServer1("");
						tempVO.setLiveTimeServer2("");
						tempVO.setLiveTimeServer3("");
					}
					
//					if (tempVO.getServiceId().equals("080")) {
//						System.out.println("tempVO.getLiveFileNameL():" + tempVO.getLiveFileNameL() + "  " + tempVO.getLiveServerL());
//						System.out.println("tempVO.getLiveFileNameC():" + tempVO.getLiveFileNameC() + "  " + tempVO.getLiveServerC());
//						System.out.println("tempVO.getLiveFileNameN():" + tempVO.getLiveFileNameN() + "  " + tempVO.getLiveServerN());
//					}
					
					//2018.02.02 - 프로야구2.0 HEVC이후 화질이 없을 경우 데이터 사이즈를 줄이기 위해 서버IP정보도 넘기지 않는다.
					if(StringUtil.isEmpty(tempVO.getLiveFileNameL()))
					{
						tempVO.setLiveServerL("");
					}
					if(StringUtil.isEmpty(tempVO.getLiveFileNameN()))
					{
						tempVO.setLiveServerN("");
					}
					if(StringUtil.isEmpty(tempVO.getLiveFileNameC()))
					{
						tempVO.setLiveServerC("");
					}
					
					if("P".equals(paramVO.getPooqYn()) && "N".equals(tempVO.getPooqYn()) ){
						
					}else{
						returnVO.add(tempVO);
					}
					
					tp_in2	= System.currentTimeMillis();
					
					//System.out.println("채널 정보 Fetch 1회소요:" + String.valueOf((tp_in2 - tp_in1)));
				}
				//endregion #for
				tp3	= System.currentTimeMillis();
				System.out.println("Fetch완료:" + String.valueOf((tp3 - tp_start)));
				resultListVO.setList(returnVO);
				
				msg = " File [" + RESFILE + "] 리턴케이스5 ";
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				tp3	= System.currentTimeMillis();
				imcsLog.timeLog("채널 정보 Fetch" + "(" + nMainCnt + ")", String.valueOf((tp3 - tp2)), methodName, methodLine);
				//System.out.println("채널 정보 Fetch:" + String.valueOf((tp3 - tp2)));
				
				
				// 파일 쓰기
				int nRetVal = FileUtil.fileWrite(RESFILE, resultListVO.toString(), false);
				tp3	= System.currentTimeMillis();
				System.out.println("파일쓰기완료:" + String.valueOf((tp3 - tp_start)));
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
					imcsLog.timeLog(" [getNSMakeVSI]cache_time", String.valueOf(tp2 - tp1), methodName, methodLine);
					
				} else {
					msg = " File [" + RESFILE + "] WRITE failed";
					imcsLog.serviceLog(msg, methodName, methodLine);		
				}
				
				FileUtil.unlock(LOCALLOCKFILE, imcsLog);
				
				FileUtil.unlock(LOCKFILE, imcsLog);
				
				delCacheFile(LOCALPATH, compFileName, paramVO);	
				
				long tp4	= System.currentTimeMillis();
				//System.out.println("파일생성수행완료:" + String.valueOf(tp2 - tp_start));
				imcsLog.timeLog(" [getNSMakeVSI]cache_time2", String.valueOf(tp4 - tp2), methodName, methodLine);
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
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID032_w) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
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
    public void getTestSbc(GetNSVSIRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "lgvod031_001_20180911_002";
		
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
		
		List<String> list   = new ArrayList<String>();
		
		try {
			
			list = getNSVSIDao.getTestSbc(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
//			rowKeys.setSaId(paramVO.getSaId());
//			rowKeys.setStbMac(paramVO.getStbMac());
//			rowKeys.setSqlId(sqlId);
//			checkKey.addVersionTuple("PT_VO_CUSTOM_ID", paramVO.getSaId());
//			
//			binds.add(paramVO);
//			
//			list = cache.getCachedResult(new CacheableExecutor<String>() {
//				@Override
//				public List<String> execute(List<Object> param) throws SQLException {
//					try{
//						GetNSVSIRequestVO requestVO = (GetNSVSIRequestVO)param.get(0);
//						List<String> rtnList = getNSVSIDao.getTestSbc(requestVO);
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
			
			if( list == null || list.isEmpty()){
				paramVO.setTestSbc("N");				
			} else {
				paramVO.setTestSbc(StringUtil.nullToSpace(list.get(0)));
			}
						
			//paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			paramVO.setTestSbc("N");
		} 
    }
    
    
    
    /**
	 * 가입 상품 조회
	 * @param paramVO
	 * @return
	 */
//    public void getmProdId(GetNSVSIRequestVO paramVO) throws Exception{
//    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
//		String methodName = oStackTrace.getMethodName();
//    	
//    	String sqlId = "lgvod032_002_20180911_002";
//				
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
//		
//		List<String> list   = new ArrayList<String>();
//		
//		try {
//			rowKeys.setSaId(paramVO.getSaId());
//			rowKeys.setStbMac(paramVO.getStbMac());
//			rowKeys.setSqlId(sqlId);
//			checkKey.addVersionTuple("PT_VO_CUSTOM_PRODUCT", paramVO.getSaId());			
//			checkKey.addVersionTuple("PT_PD_PACKAGE");
//			binds.add(paramVO);
//			
//			list = cache.getCachedResult(new CacheableExecutor<String>() {
//				@Override
//				public List<String> execute(List<Object> param) throws SQLException {
//					try{
//						GetNSVSIRequestVO requestVO = (GetNSVSIRequestVO)param.get(0);
//						List<String> rtnList = getNSVSIDao.getmProdId(requestVO);
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
//			
//			if( list != null && !list.isEmpty()){
//				paramVO.setmProdId(StringUtil.nullToSpace(list.get(0)));
//			}
//			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
//			
//		} catch (Exception e) {
//			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
//		}
//    }
    
    
    
    /**
	 * 실시간 서버 조회
	 * @param paramVO
	 * @return
	 */
    
    public List<ComCdVO> getLiveTimeServer(GetNSVSIRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		
    	String sqlId = "lgvod032_003_20180911_001";
    	
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
		
		List<ComCdVO> list   = new ArrayList<ComCdVO>();
		
		try {
			
			list = getNSVSIDao.getLiveTimeServer();
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
//			rowKeys.setSqlId(sqlId);
//			rowKeys.addRowKeys("TIMECDNIP");
//			checkKey.addVersionTuple("PT_CD_COM_CD", "TIMECDNIP");
//									
//			list = cache.getCachedResult(new CacheableExecutor<ComCdVO>() {
//				@Override
//				public List<ComCdVO> execute(List<Object> param) throws SQLException {
//					try{
//						List<ComCdVO> rtnList = getNSVSIDao.getLiveTimeServer();
//						return rtnList;
//					}catch(DataAccessException e){
//						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//				}
//				
//				@Override
//				public Class<ComCdVO> getReturnType() {
//					return ComCdVO.class;
//				}
//			}, binds, rowKeys, checkKey);
//			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
//			
		} catch (Exception e) {
//			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
//			imcsLog.failLog(ImcsConstants.API_PRO_ID032, "", cache, "cont_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			throw new ImcsException();
		}
		
    	return list;
    }
    
    
    
    /**
	 * 노드 정보 조회
	 * @param paramVO
	 * @return
	 */
    
    public List<ComNodeVO> getNode(GetNSVSIRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "lgvod032_004_20180911_002";

//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
		
		List<ComNodeVO> list   = new ArrayList<ComNodeVO>();
		
		try {
			
			list = getNSVSIDao.getNode(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
//			rowKeys.setSqlId(sqlId);
//			rowKeys.addRowKeys(paramVO.getBaseCondi());
//			checkKey.addVersionTuple("PT_LV_RANGE_IP_INFO");
//			binds.add(paramVO);
//			
//			list = cache.getCachedResult(new CacheableExecutor<ComNodeVO>() {
//				@Override
//				public List<ComNodeVO> execute(List<Object> param) throws SQLException {
//					try{
//						GetNSVSIRequestVO requestVO = (GetNSVSIRequestVO)param.get(0);
//						List<ComNodeVO> rtnList = getNSVSIDao.getNode(requestVO);
//						
//						return rtnList;
//					}catch(DataAccessException e){
//						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//				}
//				
//				@Override
//				public Class<ComNodeVO> getReturnType() {
//					return ComNodeVO.class;
//				}
//			}, binds, rowKeys, checkKey);
//			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
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
    
    public String getDongYn(GetNSVSIRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "lgvod032_005_20180913_002";
    	String szDongYn	= "";
    	
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
		
		List<String> list   = new ArrayList<String>();
		
		try {

			list = getNSVSIDao.getDongYn(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
//			rowKeys.setSaId(paramVO.getSaId());
//			rowKeys.setSqlId(sqlId);
//			rowKeys.setStbMac(paramVO.getStbMac());
//			checkKey.addVersionTuple("PT_VO_CUSTOM_ID", paramVO.getSaId());
//			checkKey.addVersionTuple("PT_LV_NODE_INFO");
//			checkKey.addVersionTuple("PT_LV_DONG_INFO");
//			binds.add(paramVO);
//						
//			list = cache.getCachedResult(new CacheableExecutor<String>() {
//				@Override
//				public List<String> execute(List<Object> param) throws SQLException {
//					try{
//						GetNSVSIRequestVO requestVO = (GetNSVSIRequestVO)param.get(0);
//						List<String> rtnList = getNSVSIDao.getDongYn(requestVO);
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
			
			if( list != null && !list.isEmpty()){
				szDongYn = StringUtil.nullToSpace(list.get(0));
			}
		
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
    	return szDongYn;
    }
    
    
	
	/**
	 * 장르명 조회
	 * @param paramVO
	 * @return
	 */
    
	public String getComName(GetNSVSIRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "lgvod032_014_20180913_002";
		String szComName	= "";
		
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
		
		List<String> list   = null;
		
		try {
			
			list = getNSVSIDao.getComName(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
//			rowKeys.setSqlId(sqlId);
//			rowKeys.addRowKeys("HSVCKIND");
//			rowKeys.addRowKeys(paramVO.getGenre1());
//			checkKey.addVersionTuple("PT_CD_COM_CD", paramVO.getGenre1());
//			binds.add(paramVO);
//						
//			list = cache.getCachedResult(new CacheableExecutor<String>() {
//				@Override
//				public List<String> execute(List<Object> param) throws SQLException {
//					try{
//						GetNSVSIRequestVO requestVO = (GetNSVSIRequestVO)param.get(0);
//						List<String> rtnList = getNSVSIDao.getComName(requestVO);
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
			
			if( list != null && !list.isEmpty()){
				szComName = StringUtil.nullToSpace(list.get(0));
			}
			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return szComName;
    }
	
	
	
	/**
	 * 앨범이 속한 상품 조회
	 * @param paramVO
	 * @return
	 */
    
	public List<ComProdInfoVO> getProdInfo(GetNSVSIRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		
    	//String sqlId = "lgvod032_015_20180302_002";
		String sqlId = "lgvod032_015_20180911_002";
    	
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
		
		List<ComProdInfoVO> list   = new ArrayList<ComProdInfoVO>();
		
		try {
			
			list = getNSVSIDao.getProdInfo(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
//			rowKeys.setSqlId(sqlId);
//			rowKeys.addRowKeys(paramVO.getStampToday());
//			rowKeys.addRowKeys(paramVO.getmProdId());
//			rowKeys.addRowKeys(paramVO.getContentsId());
//			rowKeys.addRowKeys(paramVO.getYouthYn());
//			checkKey.addVersionTuple("PT_PD_PACKAGE");
//			checkKey.addVersionTuple("PT_PD_PACKAGE_DETAIL", paramVO.getContentsId());
//			checkKey.addVersionTuple("PT_PD_PACKAGE_RELATION");
//			checkKey.addVersionTuple("PV_PROD_PRODUCT_TBL");
//			checkKey.addVersionTuple("PT_CD_COM_CD", "HDTVPROD");
//			
//			binds.add(paramVO);
//						
//			list = cache.getCachedResult(new CacheableExecutor<ComProdInfoVO>() {
//				@Override
//				public List<ComProdInfoVO> execute(List<Object> param) throws SQLException {
//					try{
//						GetNSVSIRequestVO requestVO = (GetNSVSIRequestVO)param.get(0);
//						List<ComProdInfoVO> rtnList = getNSVSIDao.getProdInfo(requestVO);
//						
//						return rtnList;
//					}catch(DataAccessException e){
//						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//				}
//				
//				@Override
//				public Class<ComProdInfoVO> getReturnType() {
//					return ComProdInfoVO.class;
//				}
//			}, binds, rowKeys, checkKey);
//			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
//			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
//			imcsLog.failLog(ImcsConstants.API_PRO_ID032, "", cache, "prod_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			throw new ImcsException();
		}
		
    	return list;
    }
	
	
	
	/**
	 * 선호채널 여부 조회
	 * @param paramVO
	 * @return
	 */
    
	public String getFavorYn(GetNSVSIRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	//String sqlId = "lgvod032_016_20180302_002";
		String sqlId = "lgvod032_016_20180913_002";
    	String szFavorYn = "";
		
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
		
		List<String> list   = new ArrayList<String>();
		
		try {

			list = getNSVSIDao.getFavorYn(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
//			rowKeys.setSaId(paramVO.getSaId());			
//			rowKeys.setSqlId(sqlId);	
//			rowKeys.setStbMac(paramVO.getStbMac());
//			rowKeys.addRowKeys(paramVO.getServiceId());
//			
//			checkKey.addVersionTuple("PT_VO_FAVORITE_CH",paramVO.getSaId());
//			
//			binds.add(paramVO);
//						
//			list = cache.getCachedResult(new CacheableExecutor<String>() {
//				@Override
//				public List<String> execute(List<Object> param) throws SQLException {
//					try{
//						GetNSVSIRequestVO requestVO = (GetNSVSIRequestVO)param.get(0);
//						List<String> rtnList = getNSVSIDao.getFavorYn(requestVO);
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
//			
			if( list == null ||list.isEmpty()){
				szFavorYn = "";
			} else {
				szFavorYn = StringUtil.nullToSpace(list.get(0));
			}
			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
    	return szFavorYn;
    }
	
	
	
	/**
	 * 최소 SAVE_TIME 조회
	 * @param paramVO
	 * @return
	 */
    
	public String getSaveTime(GetNSVSIRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
    	String sqlId = "lgvod032_017_20180911_002";
		String szSaveTime = "";
    	
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
		
		List<String> list   = null;
		
		try {
			
			list = getNSVSIDao.getSaveTime(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
//			rowKeys.setSqlId(sqlId);
//			rowKeys.addRowKeys(paramVO.getServiceId());
//			checkKey.addVersionTuple("PT_CD_NSC_CHNL", paramVO.getServiceId());
//			binds.add(paramVO);
//			
//			list = cache.getCachedResult(new CacheableExecutor<String>() {
//				@Override
//				public List<String> execute(List<Object> param) throws SQLException {
//					try{
//						GetNSVSIRequestVO requestVO = (GetNSVSIRequestVO)param.get(0);
//						List<String> rtnList = getNSVSIDao.getSaveTime(requestVO);
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
			
			if( list == null || list.isEmpty()){
				szSaveTime = "";
			} else {
				szSaveTime = StringUtil.nullToSpace(list.get(0));
			}
			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
    	return szSaveTime;
    }
	
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
			
			if( list != null && !list.isEmpty()){
				
				/* 조회한 모든 row를 fetch하지 않고 이번에 반환할 순서에 해당하는 노드까지만 fetch 하여
				 * 아래 반복문 종료시 이번에 반환할 순서에 해당하는 노드 값이 szNodeCd에 저장		 */
				for(int i=0; i<iPos; i++){
					szNodeCd	= list.get(i);
				}
				iPos++;
			}
			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
    	return szNodeCd;
    }
	
	// 2018.02.23 - 프로야구2.0, 모바일 야구APP m3u8 신규 테이블 조회
    
	public List<M3u8ProfileVO> Chnl_m3u8_search(GetNSVSIResponseVO resultVO, GetNSVSIRequestVO paramVO){
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();

		List<M3u8ProfileVO> list   = new ArrayList<M3u8ProfileVO>();
		List<M3u8ProfileVO> list_order   = new ArrayList<M3u8ProfileVO>();
		
		try {
			try{
				list = getNSVSIDao.getChnlm3u8(resultVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
//				if (list.size()>0) {
//					int max_order_num = list.get(list.size()-1).getOrderNum();
//					
//					for (int i=1 ; i <= max_order_num; i++) {
//						
//						boolean orderExist = false;
//						
//						//list에 order_num이 와 같은 것을 찾는다.
//						for (M3u8ProfileVO item : list) {
//							if (item.getOrderNum() == i) {
//								list_order.add(item);
//								orderExist = true;
//								break;
//							}
//						}
//						
//						if (orderExist == false) {
//							M3u8ProfileVO orderTemp = new M3u8ProfileVO();
//							orderTemp.setOrderNum(i);
//							list_order.add(orderTemp);
//						}
//						
//					}
//				}
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
		//return (List<M3u8ProfileVO>) list;
		return list;
		//return list_order;
	}
	
    
	public void delCacheFile(String file_path, String compValue, GetNSVSIRequestVO paramVO){
		
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
			
			if( list != null && !list.isEmpty()){
				
				/* 조회한 모든 row를 fetch하지 않고 이번에 반환할 순서에 해당하는 노드까지만 fetch 하여
				 * 아래 반복문 종료시 이번에 반환할 순서에 해당하는 노드 값이 szNodeCd에 저장		 */
				for(int i=0; i<iPos_AW; i++){
					szNodeCd	= list.get(i);
				}
				iPos_AW++;
			}
			
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
			
			if( list != null && !list.isEmpty()){
				
				/* 조회한 모든 row를 fetch하지 않고 이번에 반환할 순서에 해당하는 노드까지만 fetch 하여
				 * 아래 반복문 종료시 이번에 반환할 순서에 해당하는 노드 값이 szNodeCd에 저장		 */
				for(int i=0; i<iPos_AL; i++){
					szNodeCd	= list.get(i);
				}
				iPos_AL++;
			}
			
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
			
			if( list != null && !list.isEmpty()){
				
				/* 조회한 모든 row를 fetch하지 않고 이번에 반환할 순서에 해당하는 노드까지만 fetch 하여
				 * 아래 반복문 종료시 이번에 반환할 순서에 해당하는 노드 값이 szNodeCd에 저장		 */
				for(int i=0; i<iPos_TW; i++){
					szNodeCd	= list.get(i);
				}
				iPos_TW++;
			}
			
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
			
			if( list != null && !list.isEmpty()){
				
				/* 조회한 모든 row를 fetch하지 않고 이번에 반환할 순서에 해당하는 노드까지만 fetch 하여
				 * 아래 반복문 종료시 이번에 반환할 순서에 해당하는 노드 값이 szNodeCd에 저장		 */
				for(int i=0; i<iPos_TL; i++){
					szNodeCd	= list.get(i);
				}
				iPos_TL++;
			}
			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
    	return szNodeCd;
    }
}
