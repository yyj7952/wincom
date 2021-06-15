package kr.co.wincom.imcs.api.getNSMnuList;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;





import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.FileUtil;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.handler.ImcsException;

@Service
@Async
public class GetNSMakeMnuListServiceImpl implements GetNSMakeMnuListService {

	private Log imcsLogger		= LogFactory.getLog("API_getNSMnuList");
	private Log imcsComLogger		= LogFactory.getLog("API_Common_getNSMnuList");
	
	@Autowired
	private GetNSMnuListDao getNSMnuListDao;
	
	@Autowired
	private CommonService commonService;

//	public void getNSMakeMnuList(String szSaId, String szStbMac, String szPid){
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
	public GetNSMnuListResultVO getNSMakeMnuList(GetNSMnuListRequestVO paramVO)	{
//		this.getNSMakeMnuList(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		GetNSMnuListResultVO	resultListVO	= new GetNSMnuListResultVO();
		this.apiInfo	= ImcsConstants.NSAPI_PRO_ID900_w;
		
		resultListVO	= this.getNSMakeMnuList(paramVO, apiInfo);
		return resultListVO;
	}
	
	public GetNSMnuListResultVO getNSMakeMnuList(GetNSMnuListRequestVO paramVO, String apiInfo)	{
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		IMCSLog imcsMkLog	= new IMCSLog(imcsComLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		
		imcsLog.debugLog(methodName + " service call");
		
		int nMainCnt = 0;
		int nSubCnt = 0;
		int nSbdelay_cnt = 0;
		
		GetNSMnuListResultVO resultListVO = new GetNSMnuListResultVO();
		GetNSMnuListDetailResultVO resultListDetailVO = new GetNSMnuListDetailResultVO(); 
		//GetNSMnuListResponseVO tempVO = new GetNSMnuListResponseVO();
		List<GetNSMnuListResponseVO> resultVO = new ArrayList<GetNSMnuListResponseVO>();
		//List<GetNSMnuListResponseVO> returnVO = new ArrayList<GetNSMnuListResponseVO>();
		List<GetNSMnuDtlVO> listDtlVO = new ArrayList<GetNSMnuDtlVO>();
		
		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2
		long tp3			= 0;		// timePoint 3


		String msg			= "";
		
		long tp_start = System.currentTimeMillis();
		
		msg = " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID900_w) + "] sts[start] rcv[" + paramVO.getParam()+ "]";
		imcsLog.serviceLog(msg, methodName, methodLine);
		
		try {
			
			//검수사용자 조회
			this.getTestSbc(paramVO);
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("검수 STB여부 조회", String.valueOf((tp1 - tp_start)), methodName, methodLine);
			
			//카테고리ID의 카테고리 level 확인
			this.getCateLevel(paramVO);
			
			//파일생성규칙은 pid, IPTV2.0전체버전, 서브카테고리 변경버전, 검수여부, defin_flag , NSC_TYPE, 연령정보, 정렬기준
			//YOUth_YN 적용후 파일생성 이름
			//비디오 포털 상시 4차
			//cache 파일 생성 API 반영시 cache 파일 버전 이슈로 인하여 cache 파일 제일 앞에 버전 값 추가
			//API 반영시 버전값도 수정하여 반영
			
			// 파일명 관련 파라미터
			String RESFILE		= "";
			String NASFILE		= "";
			String LOCKFILE		= "";
			String LOCALLOCKFILE= "";
			String RESFILE_DTL  = "";
			String NASFILE_DTL  = "";
			
			
			File fNASFILE	= null;
			File fRESFILE	= null;
			
			File fLOCKFILE	= null;
			File fLOCALLOCKFILE	= null;
			
			// 2020.01.13 - 시청률순 정렬 캐시 파일 생성 여부 (Y : 생성 / N : 미생성) - 미생성이라는 건 최신 캐시파일이 있다는 것..
			String Watch_Cahce_file_Write_YN = "N";
			
			String szMsg			= "";
			//String current_time = commonService.getSysdate();
			
			//LOCAL 경로
			String LOCALPATH = "";
			LOCALPATH = commonService.getCachePath("LOCAL", ImcsConstants.NSAPI_PRO_ID900.split("/")[1], imcsLog);
			LOCALPATH = String.format("%s/%s", LOCALPATH, paramVO.getCatId().substring(0,2));
			
			String LOCALPATH_DTL = "";
			LOCALPATH_DTL = commonService.getCachePath("LOCAL", ImcsConstants.NSAPI_PRO_ID910.split("/")[1], imcsLog);
			LOCALPATH_DTL = String.format("%s/%s", LOCALPATH_DTL, paramVO.getCatId().substring(0,2));

			
			szMsg = " LOCALPATH : " + LOCALPATH;
			imcsLog.serviceLog(szMsg, methodName, methodLine);

			File LOCAL_DIR = new File(LOCALPATH);
			if(!LOCAL_DIR.exists()){
				LOCAL_DIR.mkdirs();
			}
			
			File LOCAL_DTL_DIR = new File(LOCALPATH_DTL);
			if(!LOCAL_DTL_DIR.exists()){
				LOCAL_DTL_DIR.mkdirs();
			}

			
			//NAS 경로
			String NASPATH = "";
			NASPATH = commonService.getCachePath("NAS", ImcsConstants.NSAPI_PRO_ID900.split("/")[1], imcsLog);
			NASPATH = String.format("%s/%s", NASPATH, paramVO.getCatId().substring(0,2));
			
			String NASPATH_DTL = "";
			NASPATH_DTL = commonService.getCachePath("NAS", ImcsConstants.NSAPI_PRO_ID910.split("/")[1], imcsLog);
			NASPATH_DTL = String.format("%s/%s", NASPATH_DTL, paramVO.getCatId().substring(0,2));
			
			szMsg = " NASPATH : " + NASPATH;
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			File NAS_DIR = new File(NASPATH);
			if(!NAS_DIR.exists()){
				NAS_DIR.mkdirs();
			}
			
			File NAS_DIR_DTL = new File(NASPATH_DTL);
			if(!NAS_DIR_DTL.exists()){
				NAS_DIR_DTL.mkdirs();
			}
			
			if (paramVO.getNscGb().equals("UFX") && paramVO.getFxType().equals("H")) {
				RESFILE = String.format("%s/Ver1_%s-%s-T%s-R%s-O%s-UY.res", LOCALPATH, paramVO.getCatId(), paramVO.getSubVersion(), paramVO.getTestSbc(), paramVO.getRating(), paramVO.getOrderGb());
				msg = "RESFILE : " + RESFILE;
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				NASFILE = String.format("%s/Ver1_%s-%s-T%s-R%s-O%s-UY.res", NASPATH, paramVO.getCatId(), paramVO.getSubVersion(), paramVO.getTestSbc(), paramVO.getRating(), paramVO.getOrderGb());
				msg = "NASFILE : " + NASFILE;
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				LOCKFILE = String.format("%s/Ver1_id%s-T%s-R%s-O%s-UY.lock", NASPATH, paramVO.getCatId(), paramVO.getTestSbc(), paramVO.getRating(), paramVO.getOrderGb());
				msg = "LOCKFILE : " + LOCKFILE;
				imcsLog.serviceLog(msg, methodName, methodLine);

				LOCALLOCKFILE = String.format("%s/Ver1_id%s-T%s-R%s-O%s-UY.lock", LOCALPATH, paramVO.getCatId(), paramVO.getTestSbc(), paramVO.getRating(), paramVO.getOrderGb());
				msg = "LOCALLOCKFILE : " + LOCALLOCKFILE;
				imcsLog.serviceLog(msg, methodName, methodLine);

				// getNSMnuListDtl API의 캐시도 getNSMakeMnuLis API에서 만들 것이기 때문에 캐시파일이 NAS에 존재시 같이 복사해 준다.
				RESFILE_DTL = String.format("%s/Ver1_%s-%s-T%s-R%s-O%s-UY.res", LOCALPATH_DTL, paramVO.getCatId(), paramVO.getSubVersion(), paramVO.getTestSbc(), paramVO.getRating(), paramVO.getOrderGb());
				msg = "RESFILE_DTL : " + RESFILE_DTL;
				imcsLog.serviceLog(msg, methodName, methodLine);

				NASFILE_DTL = String.format("%s/Ver1_%s-%s-T%s-R%s-O%s-UY.res", NASPATH_DTL, paramVO.getCatId(), paramVO.getSubVersion(), paramVO.getTestSbc(), paramVO.getRating(), paramVO.getOrderGb());
				msg = "NASFILE_DTL : " + NASFILE_DTL;
				imcsLog.serviceLog(msg, methodName, methodLine);
				
			} else {
				RESFILE = String.format("%s/Ver1_%s-%s-T%s-R%s-O%s-UN.res", LOCALPATH, paramVO.getCatId(), paramVO.getSubVersion(), paramVO.getTestSbc(), paramVO.getRating(), paramVO.getOrderGb());
				msg = "RESFILE : " + RESFILE;
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				NASFILE = String.format("%s/Ver1_%s-%s-T%s-R%s-O%s-UN.res", NASPATH, paramVO.getCatId(), paramVO.getSubVersion(), paramVO.getTestSbc(), paramVO.getRating(), paramVO.getOrderGb());
				msg = "NASFILE : " + NASFILE;
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				LOCKFILE = String.format("%s/Ver1_id%s-T%s-R%s-O%s-UN.lock", NASPATH, paramVO.getCatId(), paramVO.getTestSbc(), paramVO.getRating(), paramVO.getOrderGb());
				msg = "LOCKFILE : " + LOCKFILE;
				imcsLog.serviceLog(msg, methodName, methodLine);

				LOCALLOCKFILE = String.format("%s/Ver1_id%s-T%s-R%s-O%s-UN.lock", LOCALPATH, paramVO.getCatId(), paramVO.getTestSbc(), paramVO.getRating(), paramVO.getOrderGb());
				msg = "LOCALLOCKFILE : " + LOCALLOCKFILE;
				imcsLog.serviceLog(msg, methodName, methodLine);

				// getNSMnuListDtl API의 캐시도 getNSMakeMnuLis API에서 만들 것이기 때문에 캐시파일이 NAS에 존재시 같이 복사해 준다.
				RESFILE_DTL = String.format("%s/Ver1_%s-%s-T%s-R%s-O%s-UN.res", LOCALPATH_DTL, paramVO.getCatId(), paramVO.getSubVersion(), paramVO.getTestSbc(), paramVO.getRating(), paramVO.getOrderGb());
				msg = "RESFILE_DTL : " + RESFILE_DTL;
				imcsLog.serviceLog(msg, methodName, methodLine);

				NASFILE_DTL = String.format("%s/Ver1_%s-%s-T%s-R%s-O%s-UN.res", NASPATH_DTL, paramVO.getCatId(), paramVO.getSubVersion(), paramVO.getTestSbc(), paramVO.getRating(), paramVO.getOrderGb());
				msg = "NASFILE_DTL : " + NASFILE_DTL;
				imcsLog.serviceLog(msg, methodName, methodLine);				
				
			}
			
			if(NAS_DIR.exists()){
				fRESFILE	= new File(RESFILE);
				fLOCALLOCKFILE   = new File(LOCALLOCKFILE);
				fNASFILE	= new File(NASFILE);
				fLOCKFILE   = new File(LOCKFILE);
				
				if(fNASFILE.exists()) {	
					try {
						String result = FileUtil.fileRead(NASFILE, "UTF-8");
						
						if(paramVO.getOrderGb().equals("W"))
						{
							String[] arrResult	= result.split(ImcsConstants.ROWSEP);
							if(!arrResult[0].equals(paramVO.getCurrent_date()))
							{
								Watch_Cahce_file_Write_YN = "Y";
							}
						}
						
						if( "N".equals(Watch_Cahce_file_Write_YN) )
						{
							String[] szCommand = {"/bin/sh", "-c", "cp " + NASFILE + " " + RESFILE};
							Process p = Runtime.getRuntime().exec(szCommand);
							
							szMsg = " File [" + NASFILE + "] copy [" + RESFILE + "]";
							imcsLog.serviceLog(szMsg, methodName, methodLine);						
							
							resultListVO.setResult(result);
							
							FileUtil.unlock(LOCKFILE, imcsLog);
							//System.out.println("############################## getMakeList(9) #################################");
				            return resultListVO;
						}
						
						// 2020.01.13 - Local 캐시 파일도 체크하기 위해 초기값으로 세팅
						Watch_Cahce_file_Write_YN = "N";
						
					} catch(Exception e) {
						szMsg = " cp cache error!!! ";
						imcsLog.serviceLog(szMsg, methodName, methodLine);
					}
				} else if(fLOCKFILE.exists()){
					int nWaitCnt	= 0;
					
					//while(FileUtil.lock(LOCKFILE, imcsLog)){
					while(fLOCKFILE.exists()){
						
						//1초 대기하고   RES 파일 생성여부 판단
						Thread.sleep(1000);
						
						nWaitCnt++;
						if(nWaitCnt >= 5) {
							szMsg = " wait_count overload Failed svc2[" + ImcsConstants.NSAPI_PRO_ID900 + "]";
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
							
				            return resultListVO;
						} else {
							imcsLog.serviceLog("File [" + NASFILE + "] not exist", methodName, methodLine);
						}
					}
				}
				//System.out.println("############################## getMakeList(10) #################################");
				if(fRESFILE.exists() && fRESFILE.length() == 0){
					fRESFILE.delete();
				}
				
				if(fRESFILE.exists() && !fLOCALLOCKFILE.exists()){
					try {
						String result = FileUtil.fileRead(RESFILE, "UTF-8");
						
						if(paramVO.getOrderGb().equals("W"))
						{
							String[] arrResult	= result.split(ImcsConstants.ROWSEP);
							if(!arrResult[0].equals(paramVO.getCurrent_date()))
							{
								Watch_Cahce_file_Write_YN = "Y";
							}
						}
						
						if( "N".equals(Watch_Cahce_file_Write_YN) )
						{
							String[] szCommand = {"/bin/sh", "-c", "cp " + RESFILE + " " + NASFILE};
							Process p = Runtime.getRuntime().exec(szCommand);
							
							szMsg = " File [" + RESFILE + "] copy [" + NASFILE + "]";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
														
							resultListVO.setResult(result);
							
							FileUtil.unlock(LOCALLOCKFILE, imcsLog);
							
				            return resultListVO;
						}
						else
						{
							// 2020.01.14 - 시청률순 캐시 파일이 최근에 만들어진게 아니면... 생성하는데, lock파일 생성 시도 후 lock파일 생성을 정상적으로 했으면..
							//				해당 쓰레드가 캐시파일을 생성하고, lock파일 생성시 다른 쓰레드가 생성했으면 5초 동안 파일 생성됐는지 확인한다.
							int nWaitCnt	= 0;
							while(FileUtil.lock(LOCALLOCKFILE, imcsLog)){
								nWaitCnt++;
								if(nWaitCnt >= 5) {
									szMsg = " wait_count overload Failed svc2[" + ImcsConstants.NSAPI_PRO_ID900 + "]";
									imcsLog.serviceLog(szMsg, methodName, methodLine);

						            return resultListVO;
								}
								
								if(fRESFILE.exists()) {
									imcsLog.serviceLog(" File [" + RESFILE + "] exist ", methodName, methodLine);
									
									result = FileUtil.fileRead(RESFILE, "UTF-8");
									String[] arrResult	= result.split(ImcsConstants.ROWSEP);
									if(!arrResult[0].equals(paramVO.getCurrent_date()))
									{
										continue;
									}
									
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
									
									
									resultListVO.setResult(result);
									
									FileUtil.unlock(LOCALLOCKFILE, imcsLog);
									
						            return resultListVO;
								} else {
									imcsLog.serviceLog("File [" + RESFILE + "] not exist", methodName, methodLine);
								}
							}
						}
						
					} catch(Exception e) {
						szMsg = " cp cache error!!! ";
						imcsLog.serviceLog(szMsg, methodName, methodLine);
					}
				}else if(!fRESFILE.exists() && !fLOCALLOCKFILE.exists()){
					int nWaitCnt	= 0;
					while(FileUtil.lock(LOCALLOCKFILE, imcsLog)){
						nWaitCnt++;
						if(nWaitCnt >= 5) {
							szMsg = " wait_count overload Failed svc2[" + ImcsConstants.NSAPI_PRO_ID900 + "]";
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
							
				            return resultListVO;
						} else {
							imcsLog.serviceLog("File [" + RESFILE + "] not exist", methodName, methodLine);
						}
					}
				} else if(!fRESFILE.exists() && fLOCALLOCKFILE.exists()){ //추가로직 2018.07.20 (권형도)
					//#############
					int nWaitCnt	= 0;
					while(FileUtil.lock(LOCALLOCKFILE, imcsLog)){
						nWaitCnt++;
						if(nWaitCnt >= 5) {
							szMsg = " wait_count overload Failed svc2[" + ImcsConstants.NSAPI_PRO_ID900 + "]";
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
							
				            return resultListVO;
						} else {
							imcsLog.serviceLog("File [" + RESFILE + "] not exist", methodName, methodLine);
						}
					}
					//#############
				}
				
				if(paramVO.getNscGb().equals("UFX") && paramVO.getFxType().equals("H")) {
					paramVO.setMultiMappingFlag("1");
				} else {
					paramVO.setMultiMappingFlag("0");
				}
				
				
				//BODY 세팅
				resultVO = this.getNSMnuList(paramVO);
				//dtl list 생성
				for (GetNSMnuListResponseVO rItem : resultVO) {
					
					if (rItem.getResultType().equals("ALB")) {
						GetNSMnuDtlVO dtlVO = new GetNSMnuDtlVO();
						dtlVO.setResultType(rItem.getResultType());
						dtlVO.setCategoryId(rItem.getCategoryId());
						dtlVO.setCategoryName(rItem.getCategoryName());
						dtlVO.setRunTime(rItem.getRunTime());
						dtlVO.setStillFileName(rItem.getStillFileName());
						dtlVO.setFpAlbumYn(rItem.getFpAlbumYn());
						dtlVO.setFpAlbumId(rItem.getFpAlbumId());
						dtlVO.setSynopsis(rItem.getSynopsis());
						dtlVO.setCastisM3u8File(rItem.getCastisM3u8File());
						dtlVO.setOnnetM3u8File(rItem.getOnnetM3u8File());
						listDtlVO.add(dtlVO);
					}
				}
				resultListVO.setList(resultVO);
				resultListVO.setEndStr("END_STR");
				
				//HEADER 세팅
				// 2020.01.13 - 시청률순의 경우 헤더 위에 날짜값(YYYYMMDD)을 넣어서  최신 파일인지 확인용으로 사용한다. 
				if( !"W".equals(paramVO.getOrderGb()) )
				{
					String resultHeader  = String.format("%s|%s|%d|%d|%d|%s|%s|", 
							"0", "", resultVO.size(), paramVO.getCatCnt(), paramVO.getAlbCnt(), "img_still_server", paramVO.getCatId());
					resultListVO.setResultHeader(resultHeader);
				}
				else
				{					
					String resultHeader  = String.format("%s\f%s|%s|%d|%d|%d|%s|%s|", 
							paramVO.getCurrent_date(),
							"0", "", resultVO.size(), paramVO.getCatCnt(), paramVO.getAlbCnt(), "img_still_server", paramVO.getCatId());
					resultListVO.setResultHeader(resultHeader);					
				}
				int nRetVal = FileUtil.fileWrite(RESFILE, resultListVO.toString(), false);
				
				String resultHeaderDetail  = String.format("%s|%s|%d|%d|%d|%s|%s|", 
						"0", "", listDtlVO.size(), 0, listDtlVO.size(), "img_still_server", paramVO.getCatId());
				resultListDetailVO.setResultHeader(resultHeaderDetail);
				resultListDetailVO.setListDtl(listDtlVO);
				resultListDetailVO.setEndStr("END_STR");
				int nRetVal2 = FileUtil.fileWrite(RESFILE_DTL, resultListDetailVO.toString(), false);
				
				//################################### 캐시 파일쓰기 ###################################
				
				if(nRetVal == 1) {
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
							
						} catch (Exception e) {
							szMsg = " cp cache error!!!";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
						}
					}
					
										
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog(" [getNSMakeLists]cache_time("+paramVO.getCatId()+")", String.valueOf(tp2 - tp1), methodName, methodLine);
					
				} else {
					msg = " File [" + RESFILE + "] WRITE failed";
					imcsLog.serviceLog(msg, methodName, methodLine);		
				}
				
				// 최초 생성시 NAS의 getNSMnuListDtl에도 cache file을 write 한다. 
				File fRESFILE_DTL = new File(RESFILE_DTL);
				if(nRetVal2 == 1) {
					msg = " File [" + RESFILE_DTL + "] WRITE [" + fRESFILE_DTL.length() + "] bytes Finished";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					try {
						String[] szCommand = {"/bin/sh", "-c", "chmod 666 " + RESFILE_DTL};
						Process p = Runtime.getRuntime().exec(szCommand);
						
						szMsg = " File [" + RESFILE_DTL + "] chmod 666";
						imcsLog.serviceLog(szMsg, methodName, methodLine);
						
					} catch (Exception e) {
						szMsg = " cache chmod 666 error!!!";
						imcsLog.serviceLog(szMsg, methodName, methodLine);
					}
					
					if(NAS_DIR.exists()){
						try {
							String[] szCommand2 = {"/bin/sh", "-c", "cp " + RESFILE_DTL + " " + NASFILE_DTL};
							Process p = Runtime.getRuntime().exec(szCommand2);
						
							szMsg = " File [" + RESFILE_DTL + "] copy [" + NASFILE_DTL + "]";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
							
						} catch (Exception e) {
							szMsg = " cp cache error!!!";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
						}
					}
										
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog(" [getNSMakeLists]cache_time("+paramVO.getCatId()+")", String.valueOf(tp2 - tp1), methodName, methodLine);
					
				} else {
					msg = " File [" + NASFILE_DTL + "] WRITE failed";
					imcsLog.serviceLog(msg, methodName, methodLine);		
				}
				
				FileUtil.unlock(LOCALLOCKFILE, imcsLog);				
				
				//System.out.println("############################## getNSMakeMnuList(11) #################################");
				
				
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
    public void getTestSbc(GetNSMnuListRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
    	
		List<HashMap<String, String>> item   = new ArrayList<HashMap<String,String>>(); 
		
		try {
			item = getNSMnuListDao.getTestSbcMake(paramVO);
			
			if (item!=null) {
				paramVO.setTestSbc(item.get(0).get("TEST_SBC"));
				paramVO.setViewFlag2(item.get(0).get("VIEW_FLAG2"));				
				paramVO.setViewFlag1("P");
				paramVO.setCurrent_date(item.get(0).get("CURRENT_DATE"));
			} else {
				paramVO.setTestSbc("N");
				paramVO.setViewFlag2("V");
				paramVO.setViewFlag1("P");
				paramVO.setCurrent_date(commonService.getSysdateYMD());
			}
						
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			paramVO.setTestSbc("N");
			paramVO.setViewFlag2("V");
			paramVO.setViewFlag1("P");
			paramVO.setCurrent_date(commonService.getSysdateYMD());
		}
    }
    
	/**
	 * 카테고리 레벨 조회
	 * @param paramVO
	 * @return
	 */
/*    public void getCateLevel(GetNSMnuListRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		HashMap<String, String> mCat   = new HashMap<String, String>();
		
		try {
			mCat = getNSMnuListDao.getCateLevelMake(paramVO);
			
			if (mCat != null) {
				paramVO.setCategoryLevel(mCat.get("CATEGORY_LEVEL"));
				paramVO.setNscGb(mCat.get("NSC_GB"));
			} else {
				paramVO.setCategoryLevel("0");
				paramVO.setNscGb("LTE");
			}
			
		} catch (Exception e) {
			paramVO.setCategoryLevel("0");
			paramVO.setNscGb("LTE");
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
    }*/
    
	/**
	 * 카테고리 레벨 조회
	 * @param paramVO
	 * @return
	 */
    public void getCateLevel(GetNSMnuListRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		HashMap<String, String> mCat   = new HashMap<String, String>();
		
		try {
			mCat = getNSMnuListDao.getCateLevel(paramVO);
			
			if (mCat != null) {
				paramVO.setCategoryLevel(mCat.get("CATEGORY_LEVEL"));
				paramVO.setNscGb(mCat.get("NSC_GB"));
				paramVO.setCategoryName(mCat.get("CATEGORY_NAME"));
			}
			
			//카테고리 레벨 3부터는 업데이트 버전정보가 없으므로 버전이 입력된 최종 레벨인 2레벨의 버전정보를 사용 한다.
			if (Integer.parseInt(paramVO.getCategoryLevel())-1 > 2) {
				//하위카테고리부터 순차적으로 연결된 상위 카테고리중에서 2레벨의 카테고리 정보 SELECT
				String category_id = getNSMnuListDao.getCateId(paramVO);
				
				if (category_id==null) {
					paramVO.setSubVersion(category_id);
					paramVO.setSubPVersion(category_id);
					paramVO.setSubPPVersion(category_id);
				} else {
					paramVO.setParentCategory(category_id);
				}
				
				HashMap<String, String> parentVersion = getNSMnuListDao.getParentVersion(paramVO);
				if (parentVersion==null) {
					paramVO.setSubVersion(paramVO.getCatId());
					paramVO.setSubPVersion(paramVO.getCatId());
					paramVO.setSubPPVersion(paramVO.getCatId());
				} else {
					paramVO.setSubVersion(parentVersion.get("VOD_VERSION"));
					paramVO.setSubPVersion(parentVersion.get("P_VOD_VERSION"));
					paramVO.setSubPPVersion(parentVersion.get("PP_VOD_VERSION"));
				}
			} else {
				HashMap<String, String> catVersion = getNSMnuListDao.getParentVersion2(paramVO);
				
				//2레벨 카테고리 정보 버전 입력
				if (catVersion == null) {
					paramVO.setSubVersion(paramVO.getCatId());
					paramVO.setSubPVersion(paramVO.getCatId());
					paramVO.setSubPPVersion(paramVO.getCatId());
				} else {
					paramVO.setSubVersion(catVersion.get("VOD_VERSION"));
					paramVO.setSubPVersion(catVersion.get("P_VOD_VERSION"));
					paramVO.setSubPPVersion(catVersion.get("PP_VOD_VERSION"));
				}
				
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
    }     
    
	/**
	 * 구매가능 목록 조회
	 * @param 	paramVO
	 * @return	List<GetNSMnuListResponseVO>
	 */
	public List<GetNSMnuListResponseVO> getNSMnuList(GetNSMnuListRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId = "lgvod829_031_20171214_001";
		String szMsg = "";
	
		int querySize = 0;
		List<GetNSMnuListResponseVO> list = new ArrayList<GetNSMnuListResponseVO>();
	    int cat_cnt = 0;
	    int alb_cnt = 0;
	    int i_still_cnt = 0;
	    String parent_cat_id = "";
	    String temp_cat_id   = "";
	    String r_rating      = ""; 
	    String album_bill_flag      = "";
	    
		try {
			
			try {
				list = getNSMnuListDao.getNSMnuList(paramVO);
				
				for (GetNSMnuListResponseVO item : list) {
					if (StringUtil.isEmpty(item.getVodType())) {
						item.setVodType("N");
					}
					if (item.getVodType().equals("D")) {
						item.setVodType("Y");
					} else {
						item.setVodType("N");
					}
					
					if (item.getSortNo() !=null && !item.getSortNo().equals("")) {
						item.setLsortNo(item.getSortNo());
					}
					
					//ALB 루틴
					if (item.getResultType().equals("ALB")) {
						
						if(cat_cnt == 0 && alb_cnt == 0)
						{
							parent_cat_id = item.getParentCategoryId();
						}
						
						if (!item.getParentCategoryId().equals(temp_cat_id)) {
							temp_cat_id = item.getParentCategoryId();
							item.setAlbumSeq(1);
						}
						
						// Send할 데이타를 Buffer에 저장
						if (item.getRating().equals("06")) {
							r_rating = "Y";
						}
						
						item.setSubCnt(item.getSubCnt()+1);
						item.setAlbumSeq(item.getAlbumSeq()+1);
						
						album_bill_flag = item.getBillFlag();
						
						if (r_rating.equals("Y")) {
							item.setRating("06");
						}
						
						i_still_cnt = 0;
						
						List<HashMap<String, String>> st_img = getNSMnuListDao.getStillImageFileName(item.getCategoryId());
						if (st_img.size() > 0) {
							item.setStillFileName(st_img.get(0).get("MAIN_IMG_FILE_NAME").toString());
						}
						//String main_img_file_name = st_img.get("MAIN_IMG_FILE_NAME").toString();
						//String img_flag = st_img.get("IMG_FLAG").toString();
						
						//series info 가져오기
						paramVO.setCategoryId(item.getCategoryId());
						String series_info = getNSMnuListDao.getSeriesInfo(paramVO);
						//System.out.println(series_info);
						
						if (StringUtil.isEmpty(series_info)) {
							item.setSerCatId("");
							item.setSeriesNo("");
						} else {
							item.setSerInfo(series_info);
							String[] mSerInfo = series_info.split("\\|");
							item.setSerCatId(mSerInfo[0]);
							item.setSeriesNo(mSerInfo[1]);
						}
						
						// getNSMnuListDtl 응답값에서 예고편에 대한 본편 앨범ID를 주기 위해 조회
						String adi_album_id = getNSMnuListDao.getFpAlbumInfo(item);
						if (StringUtil.isEmpty(adi_album_id)) {
							item.setFpAlbumId("");
							item.setFpAlbumYn("N");
						} else {
							item.setFpAlbumId(adi_album_id);
							item.setFpAlbumYn("Y");
						}
						
						//앨범 m3u8파일 조회
						int i_m3u8_cnt = 0;
						int j = 0;
						int sort_cnt = 1;
						List<HashMap<String, String>> mM3u8Info = getNSMnuListDao.getChnlM3u8Search(item);
						if (mM3u8Info.size() > 0) {
							for (HashMap<String, String> m3u8item : mM3u8Info) {
								int m3u8_order = Integer.parseInt(m3u8item.get("M3U8_ORDER").toString());
								String m3u8_file_1 = m3u8item.get("M3U8_FILE_1").toString();
								String m3u8_file_2 = m3u8item.get("M3U8_FILE_2").toString();
								
								if (m3u8_file_1 == null)  m3u8_file_1 = "";
								if (m3u8_file_2 == null)  m3u8_file_2 = "";
								
								//System.out.println(String.format("%s, %s, %s", m3u8_order, m3u8_file_1, m3u8_file_2));
								
								for( ; sort_cnt < m3u8_order ; sort_cnt++)
								{
									item.setCastisM3u8File(item.getCastisM3u8File() + "\b");
									item.setOnnetM3u8File(item.getOnnetM3u8File() + "\b");
								}
								//System.out.println("###########("+ item.getCastisM3u8File() + ")");

								item.setCastisM3u8File(item.getCastisM3u8File() + m3u8_file_1);
								item.setOnnetM3u8File(item.getOnnetM3u8File() + m3u8_file_2);
								//System.out.println("###########("+ item.getCastisM3u8File() + ")");

								sort_cnt = m3u8_order;								
								
							}
						}
						//System.out.println(String.format("### %s, %s ###", item.getCastisM3u8File(), item.getOnnetM3u8File()));

						item.setImgUrl("img_server");
						
						alb_cnt++;
						
					} else if (item.getResultType().equals("CAT")) {
						item.setImgUrl("img_cat_server");
						item.setAnimaitonUrl("img_cat_server");
						
						if (cat_cnt == 0)
							parent_cat_id = item.getParentCategoryId();
						
//						if (item.getLastAlbumId().length() > 0) {
//							HashMap<String, String> mLastInfo = getNSMnuListDao.getListInfo(item);
//							if (mLastInfo!=null) {
//								//item.setActor(mLastInfo.get("ACTORS_DISPLAY").toString());
//								//item.setRating(mLastInfo.get("RATING_CD").toString());
//								//item.setProducer(mLastInfo.get("PRODUCER").toString());
//								//item.setComCd(mLastInfo.get("COM_CD").toString());
//								item.setTerrCh(mLastInfo.get("COM_CD").toString());
//							}
//						}
						
						if (item.getCategoryType().equals("SER")) {
							i_still_cnt = 0;
							List<HashMap<String, String>> st_img = getNSMnuListDao.getStillImageFileName(item.getLastAlbumId());
							if (st_img.size() > 0) {
								item.setStillFileName(st_img.get(0).get("MAIN_IMG_FILE_NAME").toString());
							}
						}
						
						System.out.println("###################1:(" + item.getImageFileName() + ") " + item.getImageFileName().length());
						
						/*if (item.getImageFileName()==null) {
							System.out.println("###################2:" + item.getImageFileName() + ") " + item.getImageFileName().length());
							String poster_img_file_name = getNSMnuListDao.getPosterImgFileName(item.getLastAlbumId());
							item.setImgUrl("img_server");
							item.setImageFileName(poster_img_file_name);
						}*/
						if (item.getImageFileName().length() == 0) {
							System.out.println("###################3:" + item.getImageFileName() + ") " + item.getImageFileName().length());
							String poster_img_file_name = getNSMnuListDao.getPosterImgFileName(item.getLastAlbumId());
							item.setImgUrl("img_server");
							item.setImageFileName(poster_img_file_name);
						}
						
						item.setSubCnt(0);
						
						//하위레벨 여부 가져오기
						GetNSMnuListRequestVO tempParamVO = new GetNSMnuListRequestVO();
						tempParamVO  = paramVO;
						tempParamVO.setCategoryId(item.getCategoryId());
						HashMap<String, String> mSubLevel = getNSMnuListDao.getSubLevel(tempParamVO);
						item.setParYn(mSubLevel.get("PAR_YN").toString());
						item.setSubCnt(Integer.parseInt(mSubLevel.get("SUB_CNT").toString()));
						
						if (item.getParYn().equals("N")) {
							HashMap<String, String> mAlbumInfo = getNSMnuListDao.getAlbumInfo(item.getLastAlbumId());
							if (mAlbumInfo!=null) {
								item.setOnairDate(mAlbumInfo.get("ONAIR_DATE"));
								
								if ("NULL".equals(item.getOnairDate())) {
									item.setOnairDate("");
								}
								
								item.setRating(mAlbumInfo.get("RATING_CD"));
								item.setActor(mAlbumInfo.get("ACTORS_DISPLAY"));
								item.setOverseeName(mAlbumInfo.get("PRODUCER"));
								item.setSuggestedPrice(mAlbumInfo.get("SUGGESTED_PRICE"));
							}
							
							String productType = getNSMnuListDao.getProductType(item.getLastAlbumId());
							item.setProductType(productType);
							//if (!item.getSuggestedPrice().equals("0") && !item.getProductType().equals("0")) {
							if (!"0".equals(item.getSuggestedPrice()) && !"0".equals(item.getProductType())) {
								item.setBillFlag("Y");
							} else {
								item.setBillFlag("N");
							}
						} else {
							item.setBillFlag("N");
						}
						item.setSerCatId("");
						item.setSeriesNo("");
						
						cat_cnt++;
					}
				}
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				//System.out.println("########Exception########DB Access Exception:" + e.getMessage());
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			
			try{
				if (list == null || list.isEmpty()) {
					querySize = 0;
					paramVO.setAlbCnt(0);
					paramVO.setCatCnt(0);
					
					szMsg	 = " svc4[" + String.format("%-20s", apiInfo) + "] sts[    0]" + String.format("%-21s", "msg[getNSMnuList info:" + ImcsConstants.RCV_MSG3 + "]");
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				} else {
					querySize = list.size();
					
					paramVO.setAlbCnt(alb_cnt);
					paramVO.setCatCnt(cat_cnt);
					
					//imcsLog.dbLog(apiInfo, sqlId, null, querySize, methodName, methodLine);
				} 
			}catch(Exception e){}
		} catch (Exception e) {
			szMsg	 = " svc[" + String.format("%-20s", apiInfo) + "] SQLID[" + sqlId + "] msg[" + String.format("%-21s", ":" + "]");
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			System.out.println("########Exception########:" + e.getMessage());
			throw new ImcsException();
		}
					
		return list;
	}


}
