package kr.co.wincom.imcs.api.getNSMnuListDtl2;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.FileUtil;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.NosqlCacheType;
import kr.co.wincom.imcs.common.vo.ComDataFreeVO;
import kr.co.wincom.imcs.common.vo.ComDupCHk;
import kr.co.wincom.imcs.common.vo.ComTrailerVO;
import kr.co.wincom.imcs.common.vo.ComWatchaVO;
import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.FmInfoVO;
import kr.co.wincom.imcs.common.vo.OstInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.common.vo.SvodPkgVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetNSMnuListDtl2ServiceImpl implements GetNSMnuListDtl2Service {
	private Log imcsLogger		= LogFactory.getLog("API_getNSMnuListDtl2");
	
	@Autowired
	private GetNSMnuListDtl2Dao getNSMnuListDtl2Dao;
	
	@Autowired
	private CommonService commonService;

	
	@Override
	public GetNSMnuListDtl2ResultVO getNSMnuListDtl2(GetNSMnuListDtl2RequestVO paramVO){
		
		// imcsLog 객체 생성
//		getNSMnuListDtl(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	

		// 프로세스 정보
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		GetNSMnuListDtl2ResultVO resultListVO = new GetNSMnuListDtl2ResultVO();

	    String imgStillSvrIp	= "";	// 이미지 스틸 서버 IP
	    String msg				= "";
	    
	    long tp1, tp2;
		try{
			//
			// 서버IP정보 조회
			//
			tp1 = System.currentTimeMillis();
			try {
				
				imgStillSvrIp	= commonService.getIpInfo("img_still_server", ImcsConstants.NSAPI_PRO_ID910.split("/")[1]);	// 이미지 스틸 서버 IP
			} catch(Exception e) {
				
//				imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID910, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
				paramVO.setResultCode("31000000");
				throw new ImcsException();
			}

			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("서버IP값 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			
		    //
		    // 검수사용자 여부 조회
			//
			try {
				testSbc(paramVO);
			}catch(Exception e) {
				paramVO.setTestSbc("N");
				imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID910, "", null, "custom_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			}
		    
	    	//
	    	// Cache 파일의 경로를 얻어온다.
	    	// 
	    	// LOCAL 경로
	    	String LOCAL_PATH = "";
	    	LOCAL_PATH = commonService.getCachePath("COPY_LOCAL", ImcsConstants.NSAPI_PRO_ID910.split("/")[1], imcsLog);
	    	
	    	//그냥 폴더인지, bak 폴더인지 판단
			File SUBFOLDER = new File(LOCAL_PATH);
			if (!SUBFOLDER.exists()) {
				LOCAL_PATH = LOCAL_PATH + "_bak";
			}
			
	    	LOCAL_PATH = String.format("%s/%s", LOCAL_PATH, paramVO.getCatId().substring(0,2));
	    	msg = " LOCAL_PATH : " + LOCAL_PATH;
	    	imcsLog.serviceLog(msg, methodName, methodLine);		
	    	
	    	String FILE_PATH = LOCAL_PATH;
	    	//String FILE_PATH = LOCAL_PATH;
	    	
		    //
	    	// 파일생성규칙은 pid, IPTV2.0전체버전, 서브카테고리 변경버전, 검수여부, defin_flag , NSC_TYPE, 연령정보, 정렬기준 YOUTH_YN 적용후 파일생성 이름
		    //
		    String fname		= "";
//		    String fnameOrg		= "";
		    if("UFX".equals(paramVO.getNscGb()) && "H".equals(paramVO.getFxType()) ) {
		    	fname = String.format("%s/getNSMnuListDtl-%s-T%s-R%s-O%s-UY.res", FILE_PATH, paramVO.getCatId(), paramVO.getTestSbc(), paramVO.getRating(), paramVO.getOrderGb());
//		    	fnameOrg = String.format("%s/%s/Ver1_%s-%s-T%s-R%s-O%s-UY.res", FILE_PATH, forderingDir, paramVO.getCatId(), paramVO.getSubVersion(), paramVO.getTestSbc(), paramVO.getRating(), paramVO.getOrderGb() ); 	
		    } else {
		    	fname = String.format("%s/getNSMnuListDtl-%s-T%s-R%s-O%s-UN.res", FILE_PATH, paramVO.getCatId(), paramVO.getTestSbc(), paramVO.getRating(), paramVO.getOrderGb());
//		    	fnameOrg = String.format("%s/%s/Ver1_%s-%s-T%s-R%s-O%s-UN.res", FILE_PATH, forderingDir, paramVO.getCatId(), paramVO.getSubVersion(), paramVO.getTestSbc(), paramVO.getRating(), paramVO.getOrderGb() );
		    }
		    
		    File res = new File(fname);
		    
		    String rcvBuf	= "";
		    String fBuf		= "";
		    if(res.exists() ) {
		    	
		    	fBuf = FileUtil.fileRead(fname, "UTF-8");
		    	if(fBuf.isEmpty()) {
		    		
		    		msg = " File [" + fname + "] Resfile Read Failed";
					imcsLog.serviceLog(msg, methodName, methodLine);
		    	} else {
		    		
		    		//여기서부터 캐쉬파일 페이징 처리
		    		if(!"0".equals(paramVO.getPageNo()) && !"".equals(paramVO.getPageCnt()) ) {
		    			
		    			int nPageNo		= Integer.parseInt(paramVO.getPageNo() );
		    			int nPageCnt	= Integer.parseInt(paramVO.getPageCnt() );
		    			int nStartNo	= (nPageNo * nPageCnt) - (nPageCnt - 1);
		    			int nEndNo		= nPageNo * nPageCnt;
		    			
		    			String[] arrTokens = fBuf.split(ImcsConstants.ROWSEP);
		    			fBuf = "";
		    			for(int i = 0; i < arrTokens.length; i++) {
		    				if(0 == i) {fBuf = String.format("%s%s%s", fBuf, arrTokens[i],  ImcsConstants.ROWSEP); continue; } 
							if(i >= nStartNo && i <= nEndNo) {
								fBuf = String.format("%s%s%s", fBuf, arrTokens[i],  ImcsConstants.ROWSEP);
							}	
						}
		    			
						if (Integer.parseInt(paramVO.getPageCnt()) >= arrTokens.length) {
							fBuf	= fBuf + ImcsConstants.ROWSEP;
						}
		    			
		    			rcvBuf = fBuf.replaceAll("img_still_server", imgStillSvrIp);
		    		//여기까지 캐쉬파일 페이징 처리	
		    		} else {
		    			rcvBuf = fBuf.replaceAll("img_still_server", imgStillSvrIp);
		    		}
		    		
		    		resultListVO.setRcvBuf(rcvBuf);
		    		
		    		msg = String.format( "File [%s] rcvbuf...", fname);
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("[getNSMnuListDtl] tx_time", String.valueOf(tp2 - tp1), methodName, methodLine);  
										
//					msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID910) + "] result[" + resultListVO.toString() + "]";
//					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultListVO.setResultCode(paramVO.getResultCode());
					return resultListVO;
		    	}	
		    } else {
				msg = " File [" + fname + "] Have No ResFile";
				imcsLog.serviceLog(msg, methodName, methodLine);
				resultListVO.setRcvBuf("0||0|0|0|" + imgStillSvrIp + "|" + paramVO.getCatId() + "|\fEND_STR\f\f");
				return resultListVO;
		    }
		   
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
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID910) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}    
	
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * 검수사용자 여부 조회(TEST계정 유무 조회)
     * @param
     * @result 
     */
    public String testSbc(GetNSMnuListDtl2RequestVO paramVO) throws Exception{
		
		String szTestSbc	= "";
		List<String> list   = new ArrayList<String>();
		try {
			
			list  = getNSMnuListDtl2Dao.testSbc(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			if(list != null && !list.isEmpty()){
				szTestSbc = StringUtil.replaceNull((String)list.get(0), "N");
				paramVO.setTestSbc(szTestSbc);
			} else {
				szTestSbc = "N";
				paramVO.setTestSbc(szTestSbc);
			}
		} catch (Exception e) {
						
			throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
		}
		
    	return szTestSbc;
    }
    
} // end of "GetNSMnuListDtlServiceImpl" class
