package kr.co.wincom.imcs.api.getNSLists;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.NosqlCacheType;
import kr.co.wincom.imcs.common.util.FileUtil;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.common.vo.UrlListVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Async
public class GetNSMakeListsServiceImpl implements GetNSMakeListsService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSMakeLists");
	private Log imcsComLogger		= LogFactory.getLog("API_Common_getNSMakeLists");
	
	@Autowired
	private GetNSListsDao getNSListsDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSMakeLists(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//		this.imcsMkLog	= new IMCSLog(imcsComLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
//	private IMCSLog imcsMkLog = null;
	private String apiInfo	= "";
	
	
	@Override
	public GetNSListsResultVO getNSMakeLists(GetNSListsRequestVO paramVO)	{
//		this.getNSMakeLists(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		GetNSListsResultVO	resultListVO	= new GetNSListsResultVO();
		this.apiInfo	= ImcsConstants.API_PRO_ID829;
		
		resultListVO	= this.getMakeList(paramVO, apiInfo);
		return resultListVO;
	}
	
	
	// getNSLists에서도 사용하기 위하여 모듈화
	public GetNSListsResultVO getMakeList(GetNSListsRequestVO paramVO, String apiInfo)	{
		long start_total = System.currentTimeMillis();
		
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		IMCSLog imcsMkLog	= new IMCSLog(imcsComLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		
		imcsLog.debugLog(methodName + " service call");

		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		//System.out.println("############################## getMakeList(시작) #################################");

		List<GetNSListsResponseVO> resultVO		= new ArrayList<GetNSListsResponseVO>();
		GetNSListsResponseVO tempVO				= new GetNSListsResponseVO();
		GetNSListsResultVO	resultListVO		= new GetNSListsResultVO();
		
		HashMap<String, String> mLastInfo		= null;		// 앨범 정보 MAP
		UrlListVO vodUrlVO 						= null;		// VOD URL 리스트 VO
		HashMap<String, String> mContentInfo	= null;		// 컨텐츠 정보 MAP
		//System.out.println("############################## getMakeList(2) #################################");
		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2
		
		String szImgSvrIp	= "img_server";		// 이미지 서버 IP
		String szImgSvrUrl	= "img_resize_server";		// 이미지 서버 URL
		String szCatImgSvrUrl	= "img_cat_server";	// 카테고리 이미지 서버 URL
		String msg			= "";
		
		long tp_start = System.currentTimeMillis();
		
		msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID829) + "] sts[start] rcv[" + paramVO.getParam()+ "]";
		imcsMkLog.serviceLog(msg, methodName, methodLine);
		//System.out.println("############################## getMakeList(3) #################################");
		try {
			// 이미지 서버IP조회
			tp1 = System.currentTimeMillis();
		
			// 검수 STB여부 조회		
			// getNSLists 에서 해당 로직 진행하므로 makelist에서는 할 필요가 없음
			/*String szTestSbc	= "";
			szTestSbc	= this.getTestSbc(paramVO);
			paramVO.setTestSbc(szTestSbc);
			
			// 카테고리 상세 정보 조회
			this.getCateInfo(paramVO);*/
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("검수 STB여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			//System.out.println("############################## getMakeList(4) #################################");
			// 파일명 관련 파라미터
			String RESFILE		= "";
			String NASFILE		= "";
			String LOCALLOCKFILE		= "";
			String LOCKFILE		= "";
			
			String LOCALPATH = "";
			
			LOCALPATH = commonService.getCachePath("LOCAL", ImcsConstants.API_PRO_ID994.split("/")[1], imcsLog);
			//foldering 처리
			LOCALPATH = String.format("%s/%s", LOCALPATH, paramVO.getCatId().substring(0 ,2));
			String NASPATH = "";
			
			NASPATH = commonService.getCachePath("NAS", ImcsConstants.API_PRO_ID994.split("/")[1], imcsLog);
			//foldering 처리
			NASPATH = String.format("%s/%s", NASPATH, paramVO.getCatId().substring(0 ,2));
			
			//System.out.println("############################## getMakeList(5) #################################");
			File NAS_DIR = new File(NASPATH);
			File LOCAL_DIR = new File(LOCALPATH);
			
			//String NASPATH			= "/home/jeus/file/getNSLists";		// Config에서 가져올듯?
			String szMsg			= "";
			
			int nMainCnt	= 0;
			int nSubCnt		= 0;
			
			int nTrailerChk	= 0;
			
			File fNASFILE	= null;
			File fRESFILE	= null;
			
			File fLOCKFILE	= null;
			File fLOCALLOCKFILE	= null;
			
			
			String szTempCatId	= "";
			if(paramVO.getCatId().length() > 1)
				szTempCatId = paramVO.getCatId().substring(0, 2);
			//System.out.println("############################## getMakeList(6) #################################");
			// 카테고리ID가 VC나 CA로 시작하지 않는 케이스 시작
			if(!"VC".equals(szTempCatId) && !"CA".equals(szTempCatId)){
				// strcpy(rd1.c_sub_version, rd1.c_version); 어차피 GetNSLists에서 올때 version이 sub_version임
				// paramVO.setSubVersion(paramVO.getVersion());
				//System.out.println("############################## getMakeList (1단계)  #################################");
				if(paramVO.getNscGb().equals("UFX") && paramVO.getFxType().equals("H")) {
					RESFILE		= LOCALPATH + "/Ver1-G" + paramVO.getCatGb() + "_" + paramVO.getCatId() + "-" + paramVO.getSubVersion() + "-T" + 
						paramVO.getTestSbc() + "-D" + paramVO.getDefinFlag() + "-N" + paramVO.getNscType() + "-R" + paramVO.getRating() + "-O" + 
						paramVO.getOrderGb() + "-Y" + paramVO.getYouthYn() + "-B" + paramVO.getBaseOneCd() + "-Q" + paramVO.getQuickDisYn() +"-UY-P" + paramVO.getPurchasable() + ".res";
					LOCALLOCKFILE	= LOCALPATH + "/Ver1-G" + paramVO.getCatGb() + "_id" + paramVO.getCatId() + "-T" + 
						paramVO.getTestSbc() + "-D" + paramVO.getDefinFlag() + "-N" + paramVO.getNscType() + "-R" + paramVO.getRating() + "-O" + 
						paramVO.getOrderGb() + "-Y" + paramVO.getYouthYn() + "-B" + paramVO.getBaseOneCd() + "-Q" + paramVO.getQuickDisYn() +"-UY-P" + paramVO.getPurchasable() + ".lock";
					NASFILE		= NASPATH + "/Ver1-G" + paramVO.getCatGb() + "_" + paramVO.getCatId() + "-" + paramVO.getSubVersion() + "-T" + 
						paramVO.getTestSbc() + "-D" + paramVO.getDefinFlag() + "-N" + paramVO.getNscType() + "-R" + paramVO.getRating() + "-O" + 
						paramVO.getOrderGb() + "-Y" + paramVO.getYouthYn() + "-B" + paramVO.getBaseOneCd() + "-Q" + paramVO.getQuickDisYn() +"-UY-P" + paramVO.getPurchasable() + ".res";
					LOCKFILE	= NASPATH + "/Ver1-G" + paramVO.getCatGb() + "_id" + paramVO.getCatId() + "-T" + 
						paramVO.getTestSbc() + "-D" + paramVO.getDefinFlag() + "-N" + paramVO.getNscType() + "-R" + paramVO.getRating() + "-O" + 
						paramVO.getOrderGb() + "-Y" + paramVO.getYouthYn() + "-B" + paramVO.getBaseOneCd() + "-Q" + paramVO.getQuickDisYn() +"-UY-P" + paramVO.getPurchasable() + ".lock";	
				} else {
					RESFILE		= LOCALPATH + "/Ver1-G" + paramVO.getCatGb() + "_" + paramVO.getCatId() + "-" + paramVO.getSubVersion() + "-T" + 
						paramVO.getTestSbc() + "-D" + paramVO.getDefinFlag() + "-N" + paramVO.getNscType() + "-R" + paramVO.getRating() + "-O" + 
						paramVO.getOrderGb() + "-Y" + paramVO.getYouthYn() + "-B" + paramVO.getBaseOneCd() + "-Q" + paramVO.getQuickDisYn() +"-UN-P" + paramVO.getPurchasable() + ".res";
					LOCALLOCKFILE	= LOCALPATH + "/Ver1-G" + paramVO.getCatGb() + "_id" + paramVO.getCatId() + "-T" + 
							paramVO.getTestSbc() + "-D" + paramVO.getDefinFlag() + "-N" + paramVO.getNscType() + "-R" + paramVO.getRating() + "-O" + 
							paramVO.getOrderGb() + "-Y" + paramVO.getYouthYn() + "-B" + paramVO.getBaseOneCd() + "-Q" + paramVO.getQuickDisYn() +"-UN-P" + paramVO.getPurchasable() + ".lock";
					NASFILE		= NASPATH + "/Ver1-G" + paramVO.getCatGb() + "_" + paramVO.getCatId() + "-" + paramVO.getSubVersion() + "-T" + 
						paramVO.getTestSbc() + "-D" + paramVO.getDefinFlag() + "-N" + paramVO.getNscType() + "-R" + paramVO.getRating() + "-O" + 
						paramVO.getOrderGb() + "-Y" + paramVO.getYouthYn() + "-B" + paramVO.getBaseOneCd() + "-Q" + paramVO.getQuickDisYn() +"-UN-P" + paramVO.getPurchasable() + ".res";
					LOCKFILE	= NASPATH + "/Ver1-G" + paramVO.getCatGb() + "_id" + paramVO.getCatId() + "-T" + 
						paramVO.getTestSbc() + "-D" + paramVO.getDefinFlag() + "-N" + paramVO.getNscType() + "-R" + paramVO.getRating() + "-O" + 
						paramVO.getOrderGb() + "-Y" + paramVO.getYouthYn() + "-B" + paramVO.getBaseOneCd() + "-Q" + paramVO.getQuickDisYn() +"-UN-P" + paramVO.getPurchasable() + ".lock";
				}
				
				//System.out.println("RESFILE:"+ RESFILE);
				//System.out.println("LOCALLOCKFILE:"+ LOCALLOCKFILE);
				//System.out.println("NASFILE:"+ NASFILE);
				//System.out.println("LOCKFILE:"+ LOCKFILE);
				
				//System.out.println("############################## getMakeList(8) #################################");
				if(!LOCAL_DIR.exists()){
					LOCAL_DIR.mkdirs();
				}
				
				if(!NAS_DIR.exists()){
					NAS_DIR.mkdirs();
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
							//System.out.println("############################## getMakeList(9) #################################");
				            return resultListVO;
						} catch(Exception e) {
							szMsg = " cp cache error!!! ";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
						}
					} else if(fLOCKFILE.exists()){
						int nWaitCnt	= 0;
						
						//while(FileUtil.lock(LOCKFILE, imcsLog)){
						while(fLOCKFILE.exists()){
							nWaitCnt++;
							
							if(nWaitCnt >= 5) {
								szMsg = " wait_count overload Failed svc2[" + ImcsConstants.API_PRO_ID829 + "]";
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
							String[] szCommand = {"/bin/sh", "-c", "cp " + RESFILE + " " + NASFILE};
							Process p = Runtime.getRuntime().exec(szCommand);
							
							szMsg = " File [" + RESFILE + "] copy [" + NASFILE + "]";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
							
							String result = FileUtil.fileRead(RESFILE, "UTF-8");
							resultListVO.setResult(result);
							
							FileUtil.unlock(LOCALLOCKFILE, imcsLog);
							
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
								szMsg = " wait_count overload Failed svc2[" + ImcsConstants.API_PRO_ID829 + "]";
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
								szMsg = " wait_count overload Failed svc2[" + ImcsConstants.API_PRO_ID829 + "]";
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
					
					//System.out.println("############################## getMakeList(11) #################################");
					
					
				}
				
				
	
				szMsg	= " category fetch started ";
				imcsLog.serviceLog(szMsg, methodName, methodLine);
				
				/*try {
					szImgSvrIp	= commonService.getIpInfo("img_server", ImcsConstants.API_PRO_ID829.split("/")[1]);			// 이미지서버 IP 조회
					szImgSvrUrl	= commonService.getIpInfo("img_resize_server", ImcsConstants.API_PRO_ID829.split("/")[1]);	// 이미지서버 URL 조회
					szCatImgSvrUrl	= commonService.getIpInfo("img_cat_server", ImcsConstants.API_PRO_ID829.split("/")[1]);	// 카테고리 이미지서버 URL 조회
				} catch(Exception e) {							// 이미지 서버 IP 조회 실패 시 에러
					imcsLog.failLog(apiInfo, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
					paramVO.setResultCode("31000000");
					
					throw new ImcsException();
				}
				
				tp1	= System.currentTimeMillis();
				imcsLog.timeLog("서버IP값 조회", String.valueOf(tp1 - tp2), methodName, methodLine);
				*/
				
				if( "UFX".equals(paramVO.getNscGb()) && "H".equals(paramVO.getFxType()))
					paramVO.setMultiMappingFlag("1");
				else
					paramVO.setMultiMappingFlag("0");
				
				//############################### DB 조회 ############################
				// 구매가능 목록 조회
				System.out.println("##1단계:getPurchableList##");
				resultVO = this.getPurchableList(paramVO);
				
				tp2 = System.currentTimeMillis();
				imcsLog.timeLog("서브카테고리정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
				
				if(resultVO != null) 	nMainCnt = resultVO.size();
				else					nMainCnt	= 0;
				
				String tempCatId	= "";
				String rRating		= "";
				
				for(int i = 0; i < nMainCnt; i++) {
					tempVO	= resultVO.get(i);
					rRating	= "";
				//	tempVO.setImgUrl(szImgSvrUrl);
					
					paramVO.setCategoryId(tempVO.getCatId());
					
					// 타입이 ALBUM일 경우
					if(tempVO.getResultType().equals("ALB")) {
						
						if(!tempVO.getParentCatId().equals(tempCatId)){
							tempCatId	= tempVO.getParentCatId();
						}
						
						tempVO.setSubCnt("0");
						
						// 5.1ch 여부
						if(tempVO.getIs51ch().equals("DOLBY 5.1"))		tempVO.setIs51ch("Y");
						else											tempVO.setIs51ch("N");
						
						
						if(tempVO.getPrInfo().equals("06"))				rRating = "Y";
						if(rRating.equals("Y"))							tempVO.setPrInfo("06");
						nSubCnt++;
						
						// ALB일때는 ALBUM_BILL_FLAG, CAT일떄는 BILL_FLAG 동일한 리턴위치이어서 둘다 price로 세팅
						if(tempVO.getPrice().equals("Y"))					tempVO.setPrice("Y");

						nSubCnt++;
						
						//tempVO.setSubCnt("0");
						//tempVO.setSubCnt(Integer.toString(nSubCnt));
						// sprintf((char *)c_album_bill_flag.arr, (char*)lst_Lists.c_bill_flag.arr);
						
						// VOD 서버 URL 조회
						if(tempVO.getSampleYn().equals("Y")){
							if(nTrailerChk == 0){
								
								if(!paramVO.getBaseOneCd().equals("")){
									vodUrlVO	= this.getVodUrlList(paramVO);
									
									if(vodUrlVO != null) {
										paramVO.setVodServer1(vodUrlVO.getUrl1());
										paramVO.setVodServer2(vodUrlVO.getUrl2());
										paramVO.setVodServer3(vodUrlVO.getUrl3());
									}
								}
								
								if(vodUrlVO == null || paramVO.getBaseOneCd().equals("") || "".equals(paramVO.getVodServer1())) {
									paramVO.setBaseOneCd("1234567890");
									vodUrlVO	= this.getVodUrlList(paramVO);
									
									if(vodUrlVO != null) {
										paramVO.setVodServer1(vodUrlVO.getUrl1());
										paramVO.setVodServer2(vodUrlVO.getUrl2());
										paramVO.setVodServer3(vodUrlVO.getUrl3());
									}
								}
								
								nTrailerChk++;
							}
							
							
							mContentInfo = this.getContentInfo(paramVO);
							
							if(mContentInfo.size() > 0) {
								tempVO.setVodFileName1(mContentInfo.get("VOD_FILE_NAME1"));
								tempVO.setVodFileName2(mContentInfo.get("VOD_FILE_NAME1"));
								tempVO.setVodFileName3(mContentInfo.get("VOD_FILE_NAME1"));
								// 그외 VOD_FILE_SIZE, CONTENTS_ID, CONTENTS_NAME을 가져오지만 사용하지 않음
							}
						}
						
						// 이미지 파일명 조회
						String szStillFileName	= "";
						paramVO.setId(paramVO.getCategoryId());
						szStillFileName	= this.getImageFileName(paramVO);
						tempVO.setThumbnailFileName(szStillFileName);
						tempVO.setImgUrl(szImgSvrIp);
						tempVO.setImgUrl2(szImgSvrUrl);
						
						
						// 시리즈 정보 조회
						String szSerInfo	= "";
						szSerInfo	= this.getCatSerInfo(paramVO);
						
						if (szSerInfo == null || szSerInfo.equals("")) 
							szSerInfo	= "|";
						
						tempVO.setSerCatId(szSerInfo.substring(0, szSerInfo.indexOf("|")));
						tempVO.setSeriesNo(szSerInfo.substring(szSerInfo.indexOf("|") + 1, szSerInfo.length()));
						
						tempVO.setIsOrder("0");
						tempVO.setNoCache("N");
						tempVO.setParYn("N");
						tempVO.setPpsYn("");
						tempVO.setCatDesc("");
						tempVO.setFocusFileName("");
						tempVO.setNormalFileName("");
						tempVO.setSelectFileName("");
						
						tempVO.setVodServer1(paramVO.getVodServer1());
						tempVO.setVodServer2(paramVO.getVodServer2());
						tempVO.setVodServer3(paramVO.getVodServer3());
						
						
						// VC, CA아닌 ALB 상태 케이스 끝 2105
					}
					
					// 타입이 CATEGORY 일 경우
					else if(tempVO.getResultType().equals("CAT")) {
						// LEVEL_GB는 어디서 났나?	- LEVEL_GB 선언도 가져오는 로직도 없음
						//if(paramVO.getLevelGb[0].equals("T")){
						//if(!tempVO.getCatLevel().equals("*") && !tempVO.getCatLevel().equals("1") && !tempVO.getCatLevel().equals("2")){
						//	continue;
						//}
						
						paramVO.setLastAlbumId(tempVO.getLastAlbumId());
						
						// 마지막 앨범아이디가 비어있지 않으면 TERR_CH(지상파여부) 조회
						if(!tempVO.getLastAlbumId().equals("")) {
							mLastInfo	= this.getLastInfo(paramVO);
							
							if(mLastInfo != null) {
								//tempVO.setActor(mLastInfo.get("ACTOR"));
								//tempVO.setPrInfo(mLastInfo.get("PR_INFO"));
								//tempVO.setOverseerName(mLastInfo.get("OVERSEER_NAME"));
								tempVO.setTerrCh(mLastInfo.get("TERR_CH"));
							}
						}
						
						
						// 카테고리 타입이 시리즈면 이미지 정보 조회
						if(tempVO.getCatType().equals("SER")) {
							String szStillFileName	= "";
							paramVO.setId(paramVO.getLastAlbumId());
							szStillFileName	= this.getImageFileName(paramVO);
							tempVO.setThumbnailFileName(szStillFileName);
						}
						
						tempVO.setImgUrl2(szImgSvrUrl);
						
						nSubCnt = 0;
						
						// 서브카테고리 유무 체크
						nSubCnt = this.getExistSubCat(paramVO);
						System.out.println("#############nSubCnt###########:" + nSubCnt);
						if(nSubCnt > 0) {
							tempVO.setParYn("Y");
						} else {
							tempVO.setParYn("N");
						}
						
						tempVO.setSubCnt(String.valueOf(nSubCnt));
						
						
						// 상세 정보 조회
						String szOnairDate		= "";
						String szSuggestedPrice	= "";
						String szProductType	= "";
						if(tempVO.getParYn().equals("N")) {
							szOnairDate		= this.getOnairDate(paramVO);		// 방영일 정보 조회
							tempVO.setOnairDate(szOnairDate);
							
							mLastInfo = this.getLastInfo2(paramVO);		// 마지막 앨범정보조회2
							
							if(mLastInfo != null) {
								tempVO.setPrInfo(mLastInfo.get("PR_INFO"));
								tempVO.setActor(mLastInfo.get("ACTOR"));
								tempVO.setOverseerName(mLastInfo.get("OVERSEER_NAME"));
								szSuggestedPrice	= StringUtil.nullToSpace(mLastInfo.get("SUGGESTED_PRICE"));
							}
							
							szProductType	= this.getProductType(paramVO);		// 상품타입 정보조회
							
							// 유무료 정보 적용
							if(!szSuggestedPrice.equals("0") && !szProductType.equals("0"))		tempVO.setPrice("Y");
							else 																tempVO.setPrice("N");
						} else {
							tempVO.setPrice("N");
						}
						
						if(tempVO.getParYn().equals("N") && !tempVO.getCatType().equals("SER"))
							tempVO.setSubCnt(tempVO.getConCnt());
						else tempVO.setSubCnt(String.valueOf(nSubCnt));
	
						
						tempVO.setSerCatId("");
						tempVO.setSeriesNo("");
						
						tempVO.setImgUrl(szCatImgSvrUrl);
						
						//2019.03.21
						if (tempVO.getCatType().equals("SER") && tempVO.getImgFileName().equals("")) {
							tempVO.setImgUrl("img_server");
							tempVO.setImgFileName(getNSListsDao.getPosterImgFileName(tempVO));
						}
	
						tempVO.setIs3D("N");
						tempVO.setFilterGb("N");
						tempVO.setReleaseDate("");
						tempVO.setOnairDate("");
						tempVO.setSeriesDesc("");
						tempVO.setPointWatcha("");
						tempVO.setDatafreeBillFlag("");
						
						tempVO.setVodServer1(paramVO.getVodServer1());
						tempVO.setVodServer2(paramVO.getVodServer2());
						tempVO.setVodServer3(paramVO.getVodServer3());
						// VC, CA아닌 CAT 상태 케이스 끝 2601
					}
					
					// Object 초기화
					if(mContentInfo != null) {	mContentInfo.clear();	mContentInfo	= null;	}
					if(mLastInfo != null) {	mLastInfo.clear();	mLastInfo	= null;	}
					if(vodUrlVO != null) { vodUrlVO = null; }
					
					
					resultVO.set(i, tempVO);
				}
				
				tp1	= System.currentTimeMillis();
				imcsLog.timeLog("서브카테고리정보 FETCH", String.valueOf(tp1 - tp2), methodName, methodLine);
			}
			
			// 카테고리ID가 VC나 CA로 시작하지 않는 케이스 END
			
			
			
			// 카테고리ID가 VC나 CA로 시작하는 케이스 시작
			else {
				//System.out.println("############################## getMakeList (2단계)  #################################");
				paramVO.setVersion(paramVO.getCatId());		// 여기서는 i20버전
				
				paramVO.setSubVersion(paramVO.getCatId());
				paramVO.setSubPVersion(paramVO.getCatId());
				paramVO.setSubPPVersion(paramVO.getCatId());
				//System.out.println("############################## getMakeList(12) #################################");
			
				if(szTempCatId.equals("VC")) {
					RESFILE		= LOCALPATH + "/Ver1-G" + paramVO.getCatGb() + "_" + paramVO.getCatId() + "-V" + paramVO.getI20Version() + "_" + paramVO.getSubVersion()  +
						"-T" + paramVO.getTestSbc() + "-D" + paramVO.getDefinFlag() + "-N" + paramVO.getNscType() + "-Y" + paramVO.getYouthYn() + 
						"-B" + paramVO.getBaseOneCd() + "-Q" + paramVO.getQuickDisYn() +"-P" + paramVO.getPurchasable() + ".res";
					LOCALLOCKFILE	= LOCALPATH + "/Ver1-G" + paramVO.getCatGb() + "-" + paramVO.getCatId() + "-T" + paramVO.getTestSbc() + "-D" + paramVO.getDefinFlag() + 
							"-N" + paramVO.getNscType() + "-Y" + paramVO.getYouthYn() + "-B" + paramVO.getBaseOneCd() + "-Q" + paramVO.getQuickDisYn() + "-P" + paramVO.getPurchasable() + ".lock";
					NASFILE		= NASPATH + "/Ver1-G" + paramVO.getCatGb() + "_" + paramVO.getCatId() + "-V" + paramVO.getI20Version() + "_" + paramVO.getSubVersion() + 
						"-T" + paramVO.getTestSbc() + "-D" + paramVO.getDefinFlag() + "-N" + paramVO.getNscType() + "-Y" + paramVO.getYouthYn() + 
						"-B" + paramVO.getBaseOneCd() + "-Q" + paramVO.getQuickDisYn() +"-P" + paramVO.getPurchasable() + ".res";
					LOCKFILE	= NASPATH + "/Ver1-G" + paramVO.getCatGb() + "-" + paramVO.getCatId() + "-T" + paramVO.getTestSbc() + "-D" + paramVO.getDefinFlag() + 
						"-N" + paramVO.getNscType() + "-Y" + paramVO.getYouthYn() + "-B" + paramVO.getBaseOneCd() + "-Q" + paramVO.getQuickDisYn() + "-P" + paramVO.getPurchasable() + ".lock";	
				} else {
					RESFILE		= LOCALPATH + "/Ver1-G" + paramVO.getCatGb() + "_" + paramVO.getCatId() + "-V" + paramVO.getI20Version() + "_" + paramVO.getSubVersion()  +
						"-T" + paramVO.getTestSbc() + "-D" + paramVO.getDefinFlag() + "-N" + paramVO.getNscType() + "-Y" + paramVO.getYouthYn() +  
						"-Q" + paramVO.getQuickDisYn() +"-P" + paramVO.getPurchasable() + ".res";
					LOCALLOCKFILE	= LOCALPATH + "/Ver1-G" + paramVO.getCatGb() + "-" + paramVO.getCatId() + "-T" + paramVO.getTestSbc() + "-D" + paramVO.getDefinFlag() + 
							"-N" + paramVO.getNscType() + "-Y" + paramVO.getYouthYn() + "-Q" + paramVO.getQuickDisYn() + "-P" + paramVO.getPurchasable() + ".lock";
					NASFILE		= NASPATH + "/Ver1-G" + paramVO.getCatGb() + "_" + paramVO.getCatId() + "-V" + paramVO.getI20Version() + "_" + paramVO.getSubVersion() + 
						"-T" + paramVO.getTestSbc() + "-D" + paramVO.getDefinFlag() + "-N" + paramVO.getNscType() + "-Y" + paramVO.getYouthYn() + 
						"-Q" + paramVO.getQuickDisYn() +"-P" + paramVO.getPurchasable() + ".res";
					LOCKFILE	= NASPATH + "/Ver1-G" + paramVO.getCatGb() + "-" + paramVO.getCatId() + "-T" + paramVO.getTestSbc() + "-D" + paramVO.getDefinFlag() + 
						"-N" + paramVO.getNscType() + "-Y" + paramVO.getYouthYn() + "-Q" + paramVO.getQuickDisYn() + "-P" + paramVO.getPurchasable() + ".lock";
				}
				//System.out.println("############################## getMakeList(13) #################################");
//				System.out.println("RESFILE:"+ RESFILE);
//				System.out.println("LOCALLOCKFILE:"+ LOCALLOCKFILE);
//				System.out.println("NASFILE:"+ NASFILE);
//				System.out.println("LOCKFILE:"+ LOCKFILE);
				
				
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
							
				            return resultListVO;
						} catch(Exception e) {
							szMsg = " cp cache error!!! ";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
						}
					} else if(fLOCKFILE.exists()){
						int nWaitCnt	= 0;
						
						//while(FileUtil.lock(LOCKFILE, imcsLog)){
						while(fLOCKFILE.exists()){
							nWaitCnt++;
							
							if(nWaitCnt >= 5) {
								szMsg = " wait_count overload Failed svc2[" + ImcsConstants.API_PRO_ID829 + "]";
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
					//System.out.println("############################## getMakeList(14) #################################");
					if(fRESFILE.exists() && fRESFILE.length() == 0){
						fRESFILE.delete();
					}
					//System.out.println("############################## getMakeList(15) #################################");
					if(fRESFILE.exists() && !fLOCALLOCKFILE.exists()){
						//System.out.println("<<<11111111111111111111111111111111fRESFILE.exists() && !fLOCALLOCKFILE.exists()1111111111111111111111111111111111111>>>");
						
						try {
							String[] szCommand = {"/bin/sh", "-c", "cp " + RESFILE + " " + NASFILE};
							Process p = Runtime.getRuntime().exec(szCommand);
							//System.out.println("############################## getMakeList(16) #################################");
							szMsg = " File [" + RESFILE + "] copy [" + NASFILE + "]";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
							
							String result = FileUtil.fileRead(RESFILE, "UTF-8");
							resultListVO.setResult(result);
							
							FileUtil.unlock(LOCALLOCKFILE, imcsLog);
							
				            return resultListVO;
						} catch(Exception e) {
							szMsg = " cp cache error!!! ";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
						}
					}else if(!fRESFILE.exists() && !fLOCALLOCKFILE.exists()){
						int nWaitCnt	= 0;
						//System.out.println("############################## getMakeList(17) #################################");
						//System.out.println("<<<222222222222222222222222222222!fRESFILE.exists() && !fLOCALLOCKFILE.exists()222222222222222222222222222222222>>>");
						while(FileUtil.lock(LOCALLOCKFILE, imcsLog)){
							Thread.sleep(1000);		// 1초 대기
							//System.out.println("<<<33333333333333333333333333333FileUtil.lock(LOCALLOCKFILE, imcsLog)33333333333333333333333333333333>>>");
							nWaitCnt++;
							//System.out.println("############################## getMakeList(17-1) #################################");
							if(nWaitCnt >= 5) {
								szMsg = " wait_count overload Failed svc2[" + ImcsConstants.API_PRO_ID829 + "]";
								imcsLog.serviceLog(szMsg, methodName, methodLine);
								//System.out.println("############################## getMakeList(17-2) #################################");
					            return resultListVO;
							}
							
							if(fRESFILE.exists()) {
								//System.out.println("<<<4444444444444444444444444444444444444444444444444444>>>");
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
					}else if(!fRESFILE.exists() && fLOCALLOCKFILE.exists()){
						int nWaitCnt	= 0;
						//System.out.println("############################## getMakeList(17) #################################");
						//System.out.println("<<<5555555555555555555555555!fRESFILE.exists() && fLOCALLOCKFILE.exists()55555555555555555555555555>>>");
						//while(FileUtil.lock(LOCALLOCKFILE, imcsLog)){
						while(true){
							//Thread.sleep(1000);		// 1초 대기
							//System.out.println("<<<666666666666666666666666666666666666666666666666666666>>>");
							nWaitCnt++;
							//System.out.println("############################## getMakeList(17-1) #################################");
							if(nWaitCnt >= 5) {
								szMsg = " wait_count overload Failed svc2[" + ImcsConstants.API_PRO_ID829 + "]";
								imcsLog.serviceLog(szMsg, methodName, methodLine);
								//System.out.println("############################## getMakeList(17-2) #################################");
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
					} else {
						//System.out.println("<<<7777777777777777777777777777777777777777777777>>>");
						//System.out.println("############################## getMakeList(18) 그냥빠져나가기!!!!!!!!!!!! #################################");
						return resultListVO;
					}
				}
				//System.out.println("############################## getMakeList(18) #################################");
				
				/*try {
					szImgSvrIp	= commonService.getIpInfo("img_server", ImcsConstants.API_PRO_ID829.split("/")[1]);			// 이미지서버 IP 조회
					szImgSvrUrl	= commonService.getIpInfo("img_resize_server", ImcsConstants.API_PRO_ID829.split("/")[1]);		// 이미지서버 URL 조회
					szCatImgSvrUrl	= commonService.getIpInfo("img_cat_server", ImcsConstants.API_PRO_ID829.split("/")[1]);	// 카테고리 이미지서버 URL 조회
				} catch(Exception e) {							// 이미지 서버 IP 조회 실패 시 에러
					imcsLog.failLog(apiInfo, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
					paramVO.setResultCode("31000000");

					throw new ImcsException();
				}

				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("서버IP값 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
				*/
				
				//System.out.println("############################## getMakeList(18-1) #################################");
				//System.out.println("<<<<<<<<<<<<<<<<<<<<<<<< @@@@@ getVcPurchableList 수행시간(시작) @@@@@ >>>>>>>>>>>>>>>>>>>>>: ");
				long start = System.currentTimeMillis();
				// VC, CA 구매가능 목록 조회
				if(szTempCatId.equals("VC"))
					resultVO = this.getVcPurchableList(paramVO);
				else if(szTempCatId.equals("CA"))
					resultVO = this.getCaPurchableList(paramVO);
				long end = System.currentTimeMillis();
				System.out.println("<<<<<<<<<<<<<<<<<<<<<<<< @@@@@ getVcPurchableList 수행시간(끝) @@@@@ >>>>>>>>>>>>>>>>>>>>>: " + ( end - start )/1000.0  );
				
				//System.out.println("############################## getMakeList(19) #################################");
				tp1	= System.currentTimeMillis();
				imcsLog.timeLog("카테고리정보 조회", String.valueOf(tp1 - tp2), methodName, methodLine);
				
				if(resultVO != null)
					nMainCnt = resultVO.size();
				
				for(int i = 0; i < nMainCnt; i++) {
					tempVO	= resultVO.get(i);
					//System.out.println("############################## getMakeList(20) #################################");
					paramVO.setCategoryId(tempVO.getCatId());
					paramVO.setLastAlbumId(tempVO.getLastAlbumId());
					
					if(tempVO.getResultType().equals("CAT")) {
						
						tempVO.setImgUrl(szCatImgSvrUrl);
					
						if(!tempVO.getLastAlbumId().equals("")) {
							mLastInfo	= this.getLastInfo(paramVO);
							
							if(mLastInfo != null) {
								tempVO.setActor(mLastInfo.get("ACTOR"));
								tempVO.setPrInfo(mLastInfo.get("PR_INFO"));
								tempVO.setOverseerName(mLastInfo.get("OVERSEER_NAME"));
								tempVO.setTerrCh(mLastInfo.get("TERR_CH"));
							}
						}
						
						// 카테고리 타입이 SER이면
						if(tempVO.getCatType().equals("SER")) {
							String szStillFileName	= "";
							paramVO.setId(tempVO.getLastAlbumId());
							szStillFileName	= this.getImageFileName(paramVO);
							tempVO.setThumbnailFileName(szStillFileName);
						}
						
						tempVO.setImgUrl2(szImgSvrUrl);
						
						// 서브카테고리 유무 체크
						nSubCnt = this.getExistSubCat(paramVO);
						if(nSubCnt > 0) {
							tempVO.setParYn("Y");
							tempVO.setSubCnt(String.valueOf(nSubCnt));
						} else {
							tempVO.setParYn("N");
							tempVO.setSubCnt(tempVO.getConCnt());
						}
						
					} else if(tempVO.getResultType().equals("ALB")){
						tempVO.setImgUrl(szImgSvrIp);
						tempVO.setImgUrl2(szImgSvrUrl);
						
						nSubCnt	= 0;
						
						if(tempVO.getSampleYn().equals("Y")) {
							if(nTrailerChk == 0) {

								if(!paramVO.getBaseOneCd().equals("")){
									vodUrlVO	= this.getVodUrlList(paramVO);
									
									if(vodUrlVO != null) {
										paramVO.setVodServer1(vodUrlVO.getUrl1());
										paramVO.setVodServer2(vodUrlVO.getUrl2());
										paramVO.setVodServer3(vodUrlVO.getUrl3());
									}
								}
								
								if(vodUrlVO == null || paramVO.getBaseOneCd().equals("") || "".equals(paramVO.getVodServer1())) {
									paramVO.setBaseOneCd("1234567890");
									vodUrlVO	= this.getVodUrlList(paramVO);
									
									if(vodUrlVO != null) {
										paramVO.setVodServer1(vodUrlVO.getUrl1());
										paramVO.setVodServer2(vodUrlVO.getUrl2());
										paramVO.setVodServer3(vodUrlVO.getUrl3());
									}
								}
								
								nTrailerChk++;
							}
							
							// 컨텐츠 정보 조회
							mContentInfo = this.getContentInfo(paramVO);
							
							if(mContentInfo.size() > 0) {
								tempVO.setVodFileName1(mContentInfo.get("VOD_FILE_NAME1"));
								tempVO.setVodFileName2(mContentInfo.get("VOD_FILE_NAME1"));
								tempVO.setVodFileName3(mContentInfo.get("VOD_FILE_NAME1"));
								// 그외 VOD_FILE_SIZE, CONTENTS_ID, CONTENTS_NAME을 가져오지만 사용하지 않음
							}
						}
						
						String szStillFileName	= "";
						paramVO.setId(paramVO.getCategoryId());
						szStillFileName	= this.getImageFileName(paramVO);
						tempVO.setThumbnailFileName(szStillFileName);
						
						tempVO.setParYn("N");
							
						if(tempVO.getParYn().equals("N") && !tempVO.getCatType().equals("SER"))
							tempVO.setSubCnt(tempVO.getConCnt());
						else 
							tempVO.setSubCnt(String.valueOf(nSubCnt));
							
						
						
						// 시리즈 정보 조회
						if(szTempCatId.equals("VC")) {
							tempVO.setSeriesNo(tempVO.getSerCatId().substring(tempVO.getSerCatId().indexOf("|") + 1, tempVO.getSerCatId().length()));
							tempVO.setSerCatId(tempVO.getSerCatId().substring(0, tempVO.getSerCatId().indexOf("|")));
						} else if(szTempCatId.equals("CA")) 
							tempVO.setSeriesNo("");
						
						//2019.03.21
						if (tempVO.getCatType().equals("SER") && tempVO.getImgFileName().equals("")) {
							tempVO.setImgUrl("img_server");
							tempVO.setImgFileName(getNSListsDao.getPosterImgFileName(tempVO));
						}
						
					}
					

					if("|".equals(tempVO.getSerCatId()))
						tempVO.setSerCatId("");
							
					// VC, CA가 아닌 카테고리에서는 catGb를 사용하지 않음
					tempVO.setTempCatGb(tempVO.getCatGb());
					
					tempVO.setVodServer1(paramVO.getVodServer1());
					tempVO.setVodServer2(paramVO.getVodServer2());
					tempVO.setVodServer3(paramVO.getVodServer3());
					
					// Object 초기화
					if(mContentInfo !=null) { mContentInfo.clear();	mContentInfo	= null; }
					if(mLastInfo != null) {	mLastInfo.clear();	mLastInfo	= null;	}
					if(vodUrlVO != null) { vodUrlVO = null; }
					
					resultVO.set(i, tempVO);
				}
			
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("카테고리정보 FETCH", String.valueOf(tp2 - tp1), methodName, methodLine);
				
			}
			//System.out.println("□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□ getMakeList(21) 캐시파일생성끝 □□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□");
			resultListVO.setList(resultVO);
			resultListVO.setEndStr("END_STR");
			
			/*if(NAS_DIR.exists()){
			
				tp1	= System.currentTimeMillis();
				// 파일 쓰기
				int nRetVal = FileUtil.fileWrite(NASFILE, resultListVO.toString(), false);
				
				if(nRetVal == 1) {
					msg = " File [" + NASFILE + "] WRITE [" + fNASFILE.length() + "] bytes Finished";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
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
					
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog(" [getNSMakeLists]cache_time("+paramVO.getCatId()+")", String.valueOf(tp2 - tp1), methodName, methodLine);
					
				} else {
					msg = " File [" + NASFILE + "] WRITE failed";
					imcsLog.serviceLog(msg, methodName, methodLine);		
				}
				
				FileUtil.unlock(LOCKFILE, imcsLog);
			}else{*/
				tp1	= System.currentTimeMillis();
				// 파일 쓰기
				int nRetVal = FileUtil.fileWrite(RESFILE, resultListVO.toString(), false);
				long end2 = System.currentTimeMillis();
				System.out.println("□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□ getMakeList(21) 캐시파일생성끝 □□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□□" + ( end2 - start_total )/1000.0  );

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
				
				FileUtil.unlock(LOCALLOCKFILE, imcsLog);
			//}
			
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
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID829) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
					+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			long tp_end = System.currentTimeMillis();
			imcsMkLog.timeLog("[" + methodName + "] tx_time", String.valueOf(tp_end - tp_start), methodName, methodLine);
			
		}
		long end_total = System.currentTimeMillis();
		//System.out.println("####################getMakeList (끝)####################: " + ( end_total - start_total )/1000.0  );
		
		return resultListVO;
	}
	
	
	
	/**
	 * 상품타입 정보조회
	 * @param paramVO
	 * @return
	 */
	public String getProductType(GetNSListsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		String sqlId 			= "lgvod829_140_20171214_001";
		String szProductType	= "";
		
		List<String> list = new ArrayList<String>();

		try {
			
			list = getNSListsDao.getProductType(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;

			if (list != null && !list.isEmpty()) {
				szProductType	= StringUtil.nullToSpace(list.get(0));
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return szProductType;
	}
	
	
	
	/**
	 * 앨범 정보 조회2
	 * @param paramVO
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap<String, String> getLastInfo2(GetNSListsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		String sqlId	= "lgvod829_130_20171214_001";
		
		HashMap<String, String> mLastInfo	 = new HashMap<String, String>();
		List<HashMap> list = null;
		
		try {
			
			list = getNSListsDao.getLastInfo2(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			if (list != null && !list.isEmpty()) {
				mLastInfo = (HashMap) list.get(0);
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}

		return mLastInfo;
	}



	/**
	 * 서브 카테고리 유무 조회
	 * @param paramVO
	 * @return
	 */
	public int getExistSubCat(GetNSListsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 	= "lgvod829_110_20171214_001";
		int nSubCnt		= 0;
		
		List<Integer> list = new ArrayList<Integer>();

		try {
			
			list = getNSListsDao.getExistSubCat(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					
			if (list == null || list.isEmpty()) {
				nSubCnt = 0;
//				imcsLog.failLog(apiInfo, "", cache, "conts_type:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			} else {
				nSubCnt		= list.get(0);
			}
			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
//			imcsLog.failLog(apiInfo, sqlId, cache, "conts_type:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return nSubCnt;
	}

	
	
	
	/**
	 * 방영일 정보 조회
	 * @param paramVO
	 * @return
	 */
	public String getOnairDate(GetNSListsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		String sqlId 		= "lgvod829_120_20171214_001";
		String szOnairDate	= "";
		
		List<String> list = new ArrayList<String>();

		try {

			list = getNSListsDao.getOnairDate(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			if (list != null && !list.isEmpty()) {
				szOnairDate	= StringUtil.nullToSpace(list.get(0));
				if(szOnairDate.equals("NULL"))	szOnairDate = "";
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return szOnairDate;
	}



	/**
	 * 마지막 앨범 정보 조회
	 * @param paramVO
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap<String, String> getLastInfo(GetNSListsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "lgvod829_f01_20171214_001";
		
		HashMap<String, String> mLastInfo	 = new HashMap<String, String>();
		List<HashMap> list = null;
		
		try {

			list = getNSListsDao.getLastInfo(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			if (list == null || list.isEmpty()) {
//				imcsLog.failLog(apiInfo, sqlId, cache, "conts_type:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			} else {
				mLastInfo = (HashMap) list.get(0);
			}
			
		} catch (Exception e) {
//			imcsLog.failLog(apiInfo, sqlId, cache, "conts_type:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return mLastInfo;
	}




	/**
	 * 시리즈 정보 조회
	 * @param paramVO
	 * @return
	 */
	public String getCatSerInfo(GetNSListsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		String sqlId 		= "lgvod829_080_20171214_001";
		String szSerInfo	= "";


		List<String> list = new ArrayList<String>();

		try {
			
			list = getNSListsDao.getSerInfo(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			if (list != null && !list.isEmpty()) {
				szSerInfo	= StringUtil.nullToSpace(list.get(0));
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
		return szSerInfo;
	}





	/**
	 * 이미지파일명 조회
	 * @param paramVO
	 * @return
	 */
	public String getImageFileName(GetNSListsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "lgvod829_070_20171214_001";
		String szMsg = "";
		
		List<StillImageVO> list	= new ArrayList<StillImageVO>();
		StillImageVO tempVO		= new StillImageVO();
		String szImageFileName	= "";
		
		try {
			
			list = getNSListsDao.getImageFileName(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			

			if (list != null && !list.isEmpty()) {
				tempVO = list.get(0);
				szImageFileName = StringUtil.nullToSpace(tempVO.getImgFileName());
				
			}
			
		} catch (Exception e) {
			szMsg	 = " svc[" + String.format("%-20s", apiInfo) + "] SQLID[" + sqlId + "]" + 
					String.format("%-21s", "msg[" + ":]");
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}
		
		return szImageFileName;
	}
	






	/**
	 * 컨텐츠 정보 조회
	 * @param GetNSListsRequestVO	paramVO
	 * @return HashMap<String, String>
	 **/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap<String, String> getContentInfo(GetNSListsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "lgvod829_060_20171214_001";
		String szMsg	= "";
		
		List<HashMap> list = new ArrayList<HashMap>();
		HashMap<String, String> mConInfo	 = new HashMap<String, String>();
		
		try {
			
			list = getNSListsDao.getContentInfo(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			if (list != null && !list.isEmpty()) {
				mConInfo = (HashMap) list.get(0);
			}
		} catch (Exception e) {
			szMsg	 = " svc[" + String.format("%-20s", apiInfo) + "] SQLID[" + sqlId + "]" + 
					String.format("%-21s", "msg[" + "]");
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.serviceLog(szMsg, methodName, methodLine);
		}

		return mConInfo;
	}

	

	/**
	 * 지역노드 리스트를 조회한다 1
	 * @param GetNSListsRequestVO	paramVO
	 * @return HashMap<String, String>
	 **/
	public UrlListVO getVodUrlList(GetNSListsRequestVO paramVO) throws Exception {
		IMCSLog imcsLog = new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();

		String sqlId = "lgvod829_040_20171214_001";
		
		List<UrlListVO> list = new ArrayList<UrlListVO>();
		UrlListVO urlListVO	 = null;
		
		try {
			
			list = getNSListsDao.getVodUrlList(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			if (list != null && !list.isEmpty()) {
				urlListVO	= list.get(0);
			}

		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}

		return urlListVO;
	}


	
	/**
	 * 구매가능 목록 조회
	 * @param 	paramVO
	 * @return	List<GetNSListsResponseVO>
	 */
	public List<GetNSListsResponseVO> getPurchableList(GetNSListsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId = "lgvod829_031_20171214_001";
		String szMsg = "";
		
		if(paramVO.getPurchasable().equals("Y"))	sqlId	= "lgvod829_031_20171214_001";
		else										sqlId	= "lgvod829_032_20171214_001";
	
		int querySize = 0;
		
		List<GetNSListsResponseVO> list = new ArrayList<GetNSListsResponseVO>();
		
		try {
			
			try {
				list = getNSListsDao.getPurchableList(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			try{
				if (list == null || list.isEmpty()) {
					querySize = 0;
					
					szMsg	 = " svc4[" + String.format("%-20s", apiInfo) + "] sts[    0]" + String.format("%-21s", "msg[categ_info:" + ImcsConstants.RCV_MSG3 + "]");
					imcsLog.serviceLog(szMsg, methodName, methodLine);
				} else {
					querySize = list.size();
					
					imcsLog.dbLog(apiInfo, sqlId, null, querySize, methodName, methodLine);
				} 
			}catch(Exception e){}
		} catch (Exception e) {
			szMsg	 = " svc[" + String.format("%-20s", apiInfo) + "] SQLID[" + sqlId + "] msg[" + e.getMessage() + "]";
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			
			throw new ImcsException();
		}
					
		return list;
	}
	
	
	

	/**
	 * 구매가능 목록 조회 (카테고리 ID가 VC로 시작하는 경우)
	 * @param paramVO
	 * @return
	 */
	public List<GetNSListsResponseVO> getVcPurchableList(GetNSListsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId = "lgvod829_151_20171214_001";
		
		if(paramVO.getPurchasable().equals("Y"))		sqlId	= "lgvod829_151_20171214_001";
		else											sqlId	= "lgvod829_152_20171214_001";
	
		int querySize = 0;
		
		List<GetNSListsResponseVO> list = new ArrayList<GetNSListsResponseVO>();

		try {
			
			list = getNSListsDao.getVcPurchableList(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			try{
				if (list == null || list.isEmpty()) {
					imcsLog.failLog(apiInfo, "", null, "categ_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				} else {
					querySize = list.size();
					imcsLog.dbLog(apiInfo, sqlId, null, querySize, methodName, methodLine);
				}
			}catch(Exception e){}
		} catch (Exception e) {
			imcsLog.failLog(apiInfo, sqlId, null, "categ_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			throw new ImcsException();
		}
					
		return list;
	}
	
	

	/**
	 * 구매가능 목록 조회 (카테고리 ID가 CA로 시작하는 경우)
	 * @param paramVO
	 * @return
	 * @throws Exception
	 */
	public List<GetNSListsResponseVO> getCaPurchableList(GetNSListsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId = "lgvod829_160_20171214_001";
		int querySize = 0;
		
		List<GetNSListsResponseVO> list = new ArrayList<GetNSListsResponseVO>();

		try {
			
			list = getNSListsDao.getCaPurchableList(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;

			try{
				if (list == null || list.isEmpty()) {
					imcsLog.failLog(apiInfo, "", null, "categ_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				} else {
					querySize = list.size();
					imcsLog.dbLog(apiInfo, sqlId, null, querySize, methodName, methodLine);
				}
			}catch(Exception e){}
		} catch (Exception e) {
			imcsLog.failLog(apiInfo, sqlId, null, "categ_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			throw new ImcsException();
		}
					
		return list;
	}
	
	
	
	/**
	 * 카테고리 상세정보 조회
	 * @param GetNSListsRequestVO	paramVO
	 * @return HashMap<String, String>
	 **/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap<String, String> getCateInfo(GetNSListsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();

		String sqlId = "lgvod829_020_20171214_001";
		
		HashMap<String, String> mCateInfo	 = new HashMap<String, String>();
		
		List<HashMap> list = null;
		
		try {
			
			list = getNSListsDao.getCateInfo(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			if (list == null || list.isEmpty()) {
				paramVO.setCateLevel("0");
				paramVO.setNscGb("LTE");
				paramVO.setCateName("");
			} else {
				mCateInfo = (HashMap<String, String>) list.get(0);
				
				paramVO.setCateLevel(StringUtil.nullToSpace(mCateInfo.get("CAT_LEVEL")));
				paramVO.setNscGb(StringUtil.nullToSpace(mCateInfo.get("NSC_GB")));
				paramVO.setCateName(StringUtil.nullToSpace(mCateInfo.get("CATE_NAME")));
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}

		return mCateInfo;
	}
	
	
	
	
	
	/**
	 * 검수 STB여부 조회
	 * @param paramVO
	 * @return String		TEST_SBC	
	 */
	public String getTestSbc(GetNSListsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId 		= "lgvod829_010_20171214_001";
		String szTestSbc	= "N";
		String szViewFlag2	= "";
		
		int querySize		= 0;
		
		List<String> list = new ArrayList<String>();

		try {
			
			list = getNSListsDao.getTestSbc(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;

			if (list != null && !list.isEmpty()) {
				querySize	= list.size();
				szTestSbc	= StringUtil.replaceNull(list.get(0), "N");
				
				if(szTestSbc.equals("Y"))	szViewFlag2 = "T";
				else						szViewFlag2 = "V";
				
				paramVO.setViewFlag2(szViewFlag2);
			}
			
			try{
				imcsLog.dbLog(apiInfo, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
		return szTestSbc;
	}
}
