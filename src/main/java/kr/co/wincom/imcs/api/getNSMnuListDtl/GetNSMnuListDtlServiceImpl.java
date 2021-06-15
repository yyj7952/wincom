package kr.co.wincom.imcs.api.getNSMnuListDtl;

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
public class GetNSMnuListDtlServiceImpl implements GetNSMnuListDtlService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSMnuListDtl");
	
	@Autowired
	private GetNSMnuListDtlDao getNSMnuListDtlDao;
	
	@Autowired
	private CommonService commonService;
	
//	private IMCSLog imcsLog = null;
//	
//	public void getNSMnuListDtl(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
	
	@Override
	public GetNSMnuListDtlResultVO getNSMnuListDtl(GetNSMnuListDtlRequestVO paramVO){
		
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
		GetNSMnuListDtlResultVO resultListVO = new GetNSMnuListDtlResultVO();

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
		    // 카테고리ID의 카테고리 level 확인
		    //
		    try {
		    	confirmCatLevelByCatId(paramVO);
		    } catch(Exception e) {
		    	paramVO.setCategoryLevel("0");
		    	paramVO.setNscGb("LTE");
		    	imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID910, "", null, "category_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		    }
		    
		    //
		    //카테고리 레벨 3부터는 업데이트 버전정보가 없으므로 버전이 입력된 최종 레벨인 2레벨의 버전정보를 사용 한다.
		    //
	    	if(((Integer.parseInt(paramVO.getCategoryLevel())) - 1) > 2 ) {
	    		
	    		try {
	    			
	    			List<String> list = getParentCategory(paramVO);
	    			if(null != list && !list.isEmpty() ) {
	    				
	    				try { getParentVersion(paramVO); }
	    				catch(Exception e) { throw new Exception(e.getMessage() ); }
	    			} else {
	    				
	    				paramVO.setSubVersion(paramVO.getCatId() );
		    			paramVO.setSubPVersion(paramVO.getCatId() );
		    			paramVO.setSubPpVersion(paramVO.getCatId() );
		    			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID910, "", null, "category_2Depth_parent:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
	    			}
	    		} catch(Exception e) {
	    			
	    			paramVO.setSubVersion(paramVO.getCatId() );
	    			paramVO.setSubPVersion(paramVO.getCatId() );
	    			paramVO.setSubPpVersion(paramVO.getCatId() );
	    			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID910, "", null, "ver_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
	    		}
	    	}
	    	else {
	    		
	    		try {
	    			getVersion(paramVO);
	    		} catch(Exception e) {
	    			paramVO.setSubVersion(paramVO.getCatId() );
	    			paramVO.setSubPVersion(paramVO.getCatId() );
	    			paramVO.setSubPpVersion(paramVO.getCatId() );
	    			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID910, "", null, "ver_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
	    		}
	    	}
		    
	    	//
	    	// Cache 파일의 경로를 얻어온다.
	    	// 
	    	// LOCAL 경로
	    	String LOCAL_PATH = "";
	    	LOCAL_PATH = commonService.getCachePath("LOCAL", ImcsConstants.NSAPI_PRO_ID910.split("/")[1], imcsLog);
	    	LOCAL_PATH = String.format("%s/%s", LOCAL_PATH, paramVO.getCatId().substring(0,2));

	    	msg = " LOCAL_PATH : " + LOCAL_PATH;
	    	imcsLog.serviceLog(msg, methodName, methodLine);

	    	File LOCAL_DIR = new File(LOCAL_PATH);
	    	if(!LOCAL_DIR.exists()){
	    		LOCAL_DIR.mkdir();
	    	}
	    		    	
	    	//NAS 경로
	    	String NAS_PATH = "";
	    	NAS_PATH = commonService.getCachePath("NAS", ImcsConstants.NSAPI_PRO_ID910.split("/")[1], imcsLog);
	    	NAS_PATH = String.format("%s/%s", NAS_PATH, paramVO.getCatId().substring(0,2));

	    	msg = " NAS_PATH : " + NAS_PATH;
	    	imcsLog.serviceLog(msg, methodName, methodLine);

	    	File NAS_DIR = new File(NAS_PATH);
	    	if(!NAS_DIR.exists()){
	    		NAS_DIR.mkdir();
	    	}
	    	
	    	String FILE_PATH = NAS_PATH;
	    	//String FILE_PATH = LOCAL_PATH;
	    	
		    //
	    	// 파일생성규칙은 pid, IPTV2.0전체버전, 서브카테고리 변경버전, 검수여부, defin_flag , NSC_TYPE, 연령정보, 정렬기준 YOUTH_YN 적용후 파일생성 이름
		    //
		    String fname		= "";
//		    String fnameOrg		= "";
		    if("UFX".equals(paramVO.getNscGb()) && "H".equals(paramVO.getFxType()) ) {
		    	fname = String.format("%s/Ver1_%s-%s-T%s-R%s-O%s-UY.res", FILE_PATH, paramVO.getCatId(), paramVO.getSubVersion(), paramVO.getTestSbc(), paramVO.getRating(), paramVO.getOrderGb() );
//		    	fnameOrg = String.format("%s/%s/Ver1_%s-%s-T%s-R%s-O%s-UY.res", FILE_PATH, forderingDir, paramVO.getCatId(), paramVO.getSubVersion(), paramVO.getTestSbc(), paramVO.getRating(), paramVO.getOrderGb() ); 	
		    } else {
		    	fname = String.format("%s/Ver1_%s-%s-T%s-R%s-O%s-UN.res", FILE_PATH, paramVO.getCatId(), paramVO.getSubVersion(), paramVO.getTestSbc(), paramVO.getRating(), paramVO.getOrderGb() );
//		    	fnameOrg = String.format("%s/%s/Ver1_%s-%s-T%s-R%s-O%s-UN.res", FILE_PATH, forderingDir, paramVO.getCatId(), paramVO.getSubVersion(), paramVO.getTestSbc(), paramVO.getRating(), paramVO.getOrderGb() );
		    }
		    
		    File res = new File(fname);
		    
		    String rcvBuf	= "";
		    String fBuf		= "";
		    if(res.exists() ) {
		    	
		    	fBuf = FileUtil.fileRead(fname, "UTF-8");
		    	if(fBuf.isEmpty()) {
		    		
		    		msg = String.format( "File [%s] read Failed", fname);
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
										
					msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID910) + "] result[" + resultListVO.toString() + "]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					resultListVO.setResultCode(paramVO.getResultCode());
					return resultListVO;
		    	}	
		    } else {
		    	msg = String.format( "File [%s] open failed", fname );
				imcsLog.serviceLog(msg, methodName, methodLine);
		    }

		    //
		    // 20120217 : 5번 이상 에러 발생 시에 이전 버전 캐시 파일 내용 리턴
		    // 이전 버전 검색 후 리턴 시작
		    //
		    if("UFX".equals(paramVO.getNscGb()) && "H".equals(paramVO.getFxType()) ) {
		    	fname = String.format("%s/Ver1_%s-%s-T%s-R%s-O%s-UY.res", FILE_PATH, paramVO.getCatId(), paramVO.getSubPVersion(), paramVO.getTestSbc(), paramVO.getRating(), paramVO.getOrderGb() );	
		    } else {
		    	fname = String.format("%s/Ver1_%s-%s-T%s-R%s-O%s-UN.res", FILE_PATH, paramVO.getCatId(), paramVO.getSubPVersion(), paramVO.getTestSbc(), paramVO.getRating(), paramVO.getOrderGb() );
		    }
		    
		    msg = String.format("[CACHE] Previous version cache Read : %s", fname );
		    imcsLog.serviceLog(msg, methodName, methodLine);
		      
		    res = new File(fname);
		    rcvBuf	= "";
		    fBuf	= "";
		    if(res.exists() ) {
		    	
		    	fBuf = FileUtil.fileRead(fname, "UTF-8");
		    	if(fBuf.isEmpty()) {
		    		
		    		msg = String.format( "File [%s] read Failed", fname);
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
					
					msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID910) + "] result[" + resultListVO.toString() + "]";
					imcsLog.serviceLog(msg, methodName, methodLine);
					resultListVO.setResultCode(paramVO.getResultCode());
					
					return resultListVO;
		    	}	
		    } else {
		    	msg = String.format( "File [%s] open failed ", fname );
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				//
				// 이전 버전이 없을 경우 이전 버전의 이전 버전 리딩 시작
				// 카테고리 검색일때는 전체 버전은 제외 했다 2012022
				// YOUTH_YN 적용후
				//
				if("UFX".equals(paramVO.getNscGb()) && "H".equals(paramVO.getFxType()) ) {
					fname = String.format("%s/Ver1_%s-%s-T%s-R%s-O%s-UY.res", FILE_PATH, paramVO.getCatId(), paramVO.getSubPpVersion(), paramVO.getTestSbc(), paramVO.getRating(), paramVO.getOrderGb() );	
			    } else {
			    	fname = String.format("%s/Ver1_%s-%s-T%s-R%s-O%s-UN.res", FILE_PATH, paramVO.getCatId(), paramVO.getSubPpVersion(), paramVO.getTestSbc(), paramVO.getRating(), paramVO.getOrderGb() );
			    }
			    
			    msg = String.format("[CACHE] Previous-Previous version cache Read : %s", fname );
			    imcsLog.serviceLog(msg, methodName, methodLine);
			      
			    res = new File(fname);
			    rcvBuf	= "";
			    fBuf	= "";
			    if(res.exists() ) {
			    	
			    	fBuf = FileUtil.fileRead(fname, "UTF-8");
			    	if(fBuf.isEmpty()) {
			    		
			    		msg = String.format( "File [%s] read Failed", fname);
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
						
						msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID910) + "] result[" + resultListVO.toString() + "]";
						imcsLog.serviceLog(msg, methodName, methodLine);
						resultListVO.setResultCode(paramVO.getResultCode());
						
						return resultListVO;					
			    	}	
			    } else {
			    	
			    	// getNSMnuListDtl API의 경우 해당 API가 캐시를 만들지 않으므로, 캐시 파일 만드는 것을 기다리지 않는다.
			    	msg = String.format( "File [%s] open failed ", fname );
					imcsLog.serviceLog(msg, methodName, methodLine);
			    }
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
    public String testSbc(GetNSMnuListDtlRequestVO paramVO) throws Exception{
		
		String szTestSbc	= "";
		List<String> list   = new ArrayList<String>();
		try {
			
			list  = getNSMnuListDtlDao.testSbc(paramVO);
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
    
    /**
     * 카테고리ID의 카테고리 level 확인
     * @param
     * @result 
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<HashMap<String, String>> confirmCatLevelByCatId(GetNSMnuListDtlRequestVO paramVO) throws Exception{
		
    	List<HashMap<String, String>> list   = new ArrayList<HashMap<String, String>>(); 
		try{
			
			for(HashMap item : getNSMnuListDtlDao.confirmCatLevelByCatId(paramVO)) {
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				list.add(item);
			}
			
			paramVO.setCategoryLevel(list.get(0).get("CATEGORY_LEVEL"));
			paramVO.setNscGb(list.get(0).get("NSC_GB") );
			paramVO.setCategoryName(list.get(0).get("CATEGORY_NAME") );					
			
		}catch(DataAccessException e) {
						
			throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
		}
		
		return list;
    }

    /**
     * 하위카테고리부터 순차적으로 연결된 상위 카테고리중에서 2레벨의 카테고리 정보 SELECT
     * @param
     * @result 
     */
    public List<String> getParentCategory(GetNSMnuListDtlRequestVO paramVO) throws Exception{

		List<String> list = null;
		try{
			
			list = getNSMnuListDtlDao.getParentCategory(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			paramVO.setParentCategory(list.get(0) );
		}catch(Exception e) {
						
			throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
		}
		
		return list;
    }
    
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<HashMap<String, String>> getParentVersion(GetNSMnuListDtlRequestVO paramVO) throws Exception{
		
    	List<HashMap<String, String>> list   = new ArrayList<HashMap<String, String>>(); 
		try{
			
			for(HashMap item : getNSMnuListDtlDao.getParentVersion(paramVO)) {
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				list.add(item);
			}
			
			paramVO.setParentVersion(list.get(0).get("PARENT_VERSION"));
			paramVO.setParentPVersion(list.get(0).get("PARENT_P_VERSION") );
			paramVO.setParentPpVersion(list.get(0).get("PARENT_PP_VERSION") );
			
			//2레벨 카테고리 정보 버전 입력
			paramVO.setSubVersion(paramVO.getParentVersion() );
			paramVO.setSubPVersion(paramVO.getParentPVersion() );
			paramVO.setSubPpVersion(paramVO.getParentPpVersion() );
			
		}catch(DataAccessException e) {
						
			throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
		}
		
		return list;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<HashMap<String, String>> getVersion(GetNSMnuListDtlRequestVO paramVO) throws Exception{
		
    	List<HashMap<String, String>> list   = new ArrayList<HashMap<String, String>>(); 
		try{
			
			for(HashMap item : getNSMnuListDtlDao.getVersion(paramVO)) {
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				list.add(item);
			}
			
			paramVO.setVersion(list.get(0).get("VERSION"));
			paramVO.setPVersion(list.get(0).get("P_VERSION") );
			paramVO.setPpVersion(list.get(0).get("PP_VERSION") );
			
			//2레벨 카테고리 정보 버전 입력
			paramVO.setSubVersion(paramVO.getVersion() );
			paramVO.setSubPVersion(paramVO.getPVersion() );
			paramVO.setSubPpVersion(paramVO.getPpVersion() );
			
		}catch(DataAccessException e) {
						
			throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
		}
		
		return list;
    }
    
    
    
} // end of "GetNSMnuListDtlServiceImpl" class
