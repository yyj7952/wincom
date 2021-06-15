package kr.co.wincom.imcs.api.getNSChPGM2;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.NosqlCacheType;
import kr.co.wincom.imcs.common.util.FileUtil;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GetNSChPGM2ServiceImpl implements GetNSChPGM2Service {
	private Log imcsLogger = LogFactory.getLog("API_getNSChPGM2");
	
	@Autowired
	private GetNSChPGM2Dao getNSChPGM2Dao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSChPGM(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public GetNSChPGM2ResultVO getNSChPGM2(GetNSChPGM2RequestVO paramVO){
//		this.getNSChPGM(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		String msg	= "";
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		GetNSChPGM2ResultVO resultListVO = new GetNSChPGM2ResultVO();
		GetNSChPGM2ResponseVO tempVO = new GetNSChPGM2ResponseVO();
		List<GetNSChPGM2ResponseVO> tempListVO = new ArrayList<GetNSChPGM2ResponseVO>();
		
		String RESFILE	= "";
		
		String LOCALPATH = "";
		
		LOCALPATH = commonService.getCachePath("COPY_LOCAL", ImcsConstants.NSAPI_PRO_ID021.split("/")[1], imcsLog);
		
		int nMainCnt = 0;
		int nWaitCnt = 0;
		
	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
		long tp2 = 0;
		long tp3 = 0;
		
		String szThmImgSvrIp	= "";		// 썸네일 이미지 서버 IP
		String szImgSvrUrl		= "";		// 이미지 서버 URL
		String szStilImgSvrUrl	= "";		// 스틸 이미지 서버 URL
		String VirtualChFlag	= "";		// 가상채널 여부
		
		try {
			VirtualChFlag	= commonService.getVCFlag(ImcsConstants.NSAPI_PRO_ID021.split("/")[1]);	// 가상채널 여부
			paramVO.setVirtualChFlag(VirtualChFlag);
		} catch (Exception e) {
			System.out.println("GetConfigInfo fail");
		}
		
		try {
			szThmImgSvrIp	= commonService.getImgReplaceUrl2("snap_server","getNSChPGM");
			szImgSvrUrl	= commonService.getImgReplaceUrl2("img_resize_server","getNSChPGM");
			szStilImgSvrUrl	= commonService.getImgReplaceUrl2("img_still_server","getNSChPGM");
		} catch(Exception e) {
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID021, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
			throw new ImcsException();
		}

		
		try{
						
			//테스트 가입자 여부 조회
			this.getTestSbc(paramVO);
			
			tp1	= System.currentTimeMillis();
	    	
			imcsLog.timeLog("검수 STB여부 조회", String.valueOf((tp1 - tp_start)), methodName, methodLine);
			
			String compFileName = "-3"+paramVO.getRequestTime()+"-4"+paramVO.getHdtvViewGb()+"-5"+paramVO.getPooqYn()+
			                      "-6"+paramVO.getCallFlag()+"-T"+paramVO.getTestSbc() + ".res";
			
			//하위 폴더 경로 생성
			String dirName = String.format("%s/4%s5%s6%sT%s", LOCALPATH, 
					paramVO.getHdtvViewGb(), paramVO.getPooqYn(), paramVO.getCallFlag(), paramVO.getTestSbc());
			LOCALPATH = dirName;
			File fLOCALPATH = new File(LOCALPATH);
			//System.out.println("####### dirName ########: " + LOCALPATH);
			
			String chkFileName = "";
			
			File[] files = fLOCALPATH.listFiles();
			
			try
			{
				if(files.length > 0){			
					for(int i = 0; i < files.length; i++){
						if(files[i].getName().toString().indexOf(compFileName) > -1){
							msg = files[i].getName();						
							imcsLog.serviceLog(msg, methodName, methodLine);
							
							chkFileName = files[i].getName();
						}
					}
				} else {
					
					chkFileName = "1";
				}
			}catch(NullPointerException e)
			{
				msg = " getNSChPGM Cache File Empty";
				imcsLog.serviceLog(msg, methodName, methodLine);
			}
			
			if(!chkFileName.equals("1")){
				RESFILE = LOCALPATH + "/" + chkFileName;
				File res = new File(RESFILE);
				
				if(res.exists()) {
					String result = FileUtil.fileRead(RESFILE, "UTF-8");
					
					if(!"".equals(result)) {
						String[] arrResult	= result.split(ImcsConstants.ROWSEP);
						
						result	= "";
						String[] arrRowResult = null;
						
						for(int i = 0; i < arrResult.length; i++) {
							if(i == 0) continue;	// 첫번째는 헤더정보이기 때문에, 넘어간다.
							tempVO = new GetNSChPGM2ResponseVO();
							
							
							arrResult[i] = arrResult[i].replaceAll("snap_server", szThmImgSvrIp);	
							arrResult[i] = arrResult[i].replaceAll("img_resize_server", szImgSvrUrl);		
							arrResult[i] = arrResult[i].replaceAll("img_still_server", szStilImgSvrUrl);	
							
							arrRowResult = arrResult[i].split(ImcsConstants.COLSEP_SPLIT);
							tempVO.dataParcing(arrRowResult);
							
							if(!VirtualChFlag.equals("0")) {
								if(tempVO.getEpgType().equals("2")) {
									continue;
								}
							}
							
							tempListVO.add(tempVO);
						}
						
						msg = " File [" + RESFILE + "] rcvbuf... [" + res.length() + "] bytes ";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						String resultHeader	= "0||";
						resultListVO.setResultHeader(resultHeader);
						resultListVO.setList(tempListVO);
//						FileUtil.unlock(LOCKFILE, imcsLog);
						return resultListVO;
						
					}else{
						msg = " File [" + RESFILE + "] ResFile Read Failed";
						imcsLog.serviceLog(msg, methodName, methodLine);
						String resultHeader  = String.format("%s|%s|", "1", "ChPGM is not SCHEDULE");
						resultListVO.setResultHeader(resultHeader);
					}	
				}else{
					msg = " File [" + RESFILE + "] Have No ResFile";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					//결과 헤더 붙이기
					String resultHeader  = String.format("%s|%s|", "1", "ChPGM is not SCHEDULE");
					resultListVO.setResultHeader(resultHeader);
				}
			} else {
				msg = " File [" + RESFILE + "] Open Failed";
				imcsLog.serviceLog(msg, methodName, methodLine);
				//결과 헤더 붙이기
				String resultHeader  = String.format("%s|%s|", "1", "CACHE FILE OPEN FAIL");
				resultListVO.setResultHeader(resultHeader);
			}
			
		}catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog( methodName + "-E" , ie.getClass().getName() + ":" +  ie.getMessage());
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog( methodName + "-E" , e.getClass().getName() + ":" +  e.getMessage());
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			

			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID021) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
					+ " [DB:" + nUserDB + "]";
			
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	
	
	
	/**
	 * 테스트 가입자 여부 조회
	 * @param paramVO
	 * @return
	 */
    public void getTestSbc(GetNSChPGM2RequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		List<String> list   = null;
				
		try {
			
			list = getNSChPGM2Dao.getTestSbc(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			if( list == null || list.isEmpty()) {
				paramVO.setTestSbc("N");				
			} else {
				paramVO.setTestSbc(list.get(0));
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			paramVO.setTestSbc("N");
		}
    }
    
}
