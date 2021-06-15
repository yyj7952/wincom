package kr.co.wincom.imcs.api.getNSLists;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.NosqlCacheType;
import kr.co.wincom.imcs.common.util.FileUtil;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.util.SystemOutLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetNSListsServiceImpl implements GetNSListsService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSLists");
		
	@Autowired
	private GetNSListsDao getNSListsDao;
	
	@Autowired
	private GetNSMakeListsService getNSMakeListsService;
	
	@Autowired
	private CommonService commonService;

//	public void getNSLists(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);		
//	}
//	
//	private IMCSLog imcsLog = null;	
	
	@Override
	public GetNSListsResultVO getNSLists(GetNSListsRequestVO paramVO)	{
		
//		long start = System.currentTimeMillis();
//		this.getNSLists(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 5);
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);

		List<GetNSListsResponseVO> resultVO	= null;
		GetNSListsResultVO	resultListVO	= new GetNSListsResultVO();
		
		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2

		String msg			= "";
		String RESFILE		= "";
		String ORIFILE		= "";
		
		try {
			int nPageNo			= Integer.parseInt(StringUtil.nullToZero(paramVO.getPageNo()));
			int nPageCnt		= Integer.parseInt(StringUtil.nullToZero(paramVO.getPageCnt()));
			int nPageIdx		= Integer.parseInt(StringUtil.nullToZero(paramVO.getPageIdx()));
			int nStartNo		= 0;
			int nEndNo			= 0;
			
			if(nPageNo != 0 && nPageCnt != 0) {
				nStartNo = (nPageNo * nPageCnt) - (nPageCnt - 1);
				nEndNo   = (nPageNo * nPageCnt);
	
	            if (nPageIdx > 0) {
	            	nStartNo	= nPageIdx;
	            	nEndNo		= nStartNo + (nPageCnt - 1);
	            }
			}
			
			String img_server = commonService.getImgReplaceUrl2("img_server", "getNSLists");
			String img_resize_server = commonService.getImgReplaceUrl2("img_resize_server", "getNSLists");
			String img_cat_server = commonService.getImgReplaceUrl2("img_cat_server", "getNSLists");
            
			// 검수 STB여부 조회
			tp1 = System.currentTimeMillis();
			String szTestSbc	= "";
			szTestSbc	= this.getTestSbc(paramVO);
			paramVO.setTestSbc(szTestSbc);
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("검수 STB여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			System.out.println("검수 STB여부 조회: " + String.valueOf(tp2 - tp1));
			
			// 카테고리 상세 정보 조회
			String szTempCatId	= "";
			if(paramVO.getCatId().length() > 1)
				szTempCatId = paramVO.getCatId().substring(0, 2);
			
			//System.out.println("szTempCatId:"+szTempCatId);
			
			if(!szTempCatId.equals("VC") && !szTempCatId.equals("CA")){		// VC, CA가 아닐 경우 VERSION 정보 조회
				this.getCateInfo(paramVO);
				
				// 파일명 생성을 위해 버정정보 등을 조회
				if(Integer.parseInt(StringUtil.nullToZero(paramVO.getCateLevel())) - 1 > 2) {
					String szParentCatId	= "";
					szParentCatId = this.getParentCatId(paramVO);
					paramVO.setParentCatId(szParentCatId);
					
					if(szParentCatId != null && !szParentCatId.equals("")) {
						this.getI20VerInfo(paramVO);
					}
				} else {
					paramVO.setParentCatId(paramVO.getCatId());				// 위의 getVerInfo와 다를 바가 없어 같은 메소드로 묶음 
					this.getI20VerInfo(paramVO);
				}
			} else {														// VC, CA일 경우 VERSION 정보 조회
				this.getVerInfo(paramVO);
			}
			// 파일명 맞추기 위한 로직 끝
			
			
			if(!szTempCatId.equals("VC") && !szTempCatId.equals("CA")) {
				RESFILE	= this.getFileName(paramVO, paramVO.getSubVersion(), "");
				ORIFILE	= RESFILE;
			} else {
				RESFILE	= this.getFileName(paramVO, paramVO.getSubVersion(), paramVO.getI20Version());
				ORIFILE	= RESFILE;
			}
				
			
			File res = new File(RESFILE);
			//System.out.println("RESFILE:" + RESFILE);
			if(res.exists()) {		// 파일이 존재할 경우 파일 읽기
				resultListVO	= this.getFileList(RESFILE, nStartNo, nEndNo, img_server, img_resize_server, img_cat_server, paramVO);
				
				long end5 = System.currentTimeMillis();
				//System.out.println("####################getNSLists 수행시간(6)####################: " + ( end5 - start )/1000.0  );
				//System.out.println(resultListVO.toString());
				//System.out.println("★★★★★현재버전 파일명으로 리턴하고 빠져나감!!★★★★★:" + RESFILE);
				
				return resultListVO;
				
			} else {		// 파일이 미존재할 경우 MAKELIST(lgvod829) 호출

				long end7 = System.currentTimeMillis();
				//System.out.println("####################getNSLists 수행시간(7)####################: " + ( end7 - start )/1000.0  );
				
				msg	= " File [" + RESFILE + "] open failed ";
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				getNSMakeListsService.getNSMakeLists(paramVO);
				
				/*if(resultListVO != null) {
					String result	= "";
					String[] arrResult	= null;
					
					arrResult	= resultListVO.toString().split(ImcsConstants.ROWSEP);
					
					for(int i = 0; i < arrResult.length; i++) {
						if(nStartNo != 0 && nEndNo != 0) {
							if(i + 1 >= nStartNo && i + 1 <= nEndNo)
								result	= result + arrResult[i] + ImcsConstants.ROWSEP;
						}else{
							result	= result + arrResult[i] + ImcsConstants.ROWSEP;
						}
						
					}
					
					result	= StringUtil.nullToSpace(result);
					resultListVO.setResult(result);
					
					return resultListVO;
				}*/
			}
			
			long end1 = System.currentTimeMillis();
			//System.out.println("####################getNSLists 수행시간(1)####################: " + ( end1 - start )/1000.0  );
		
			// 이전 버전 캐쉬 파일 읽기
			if(!szTempCatId.equals("VC") && !szTempCatId.equals("CA"))
				RESFILE		= this.getFileName(paramVO, paramVO.getSubPVersion(), "");
			else
				RESFILE		= this.getFileName(paramVO, paramVO.getSubPVersion(), paramVO.getI20PVersion());
			//System.out.println("★★★★★이전버전 파일명★★★★★:" + RESFILE);
			
			res	= new File(RESFILE);
			imcsLog.serviceLog("[CACHE] Previous version cache Read : " + RESFILE, methodName, methodLine);
			long end2 = System.currentTimeMillis();
			//System.out.println("####################getNSLists 수행시간(2)####################: " + ( end2 - start )/1000.0  );
			// 반복
			if(res.exists()) {		// 파일이 존재할 경우 파일 읽기 - MAKELIST 호출 이후 반복
				resultListVO	= this.getFileList(RESFILE, nStartNo, nEndNo, img_server, img_resize_server, img_cat_server, paramVO);
				long end3 = System.currentTimeMillis();
				//System.out.println("####################★★★★★이전버전 읽어서 리턴하고 끝남!!★★★★★####################: ");
				return resultListVO;
			} else {						// 파일 미존 재 시 이이전 버전의 파일 조회
				imcsLog.serviceLog(" File [" + RESFILE + "] open failed ", methodName, methodLine);
				
				// 이이전 버전 캐쉬 파일 읽기
				if(!szTempCatId.equals("VC") && !szTempCatId.equals("CA"))
					RESFILE		= this.getFileName(paramVO, paramVO.getSubPPVersion(), "");
				else
					RESFILE		= this.getFileName(paramVO, paramVO.getSubPPVersion(), paramVO.getI20PPVersion());
				//System.out.println("★★★★★이이전버전 파일명★★★★★:" + RESFILE);
				res	 = new File(RESFILE);
				
				if(res.exists()) {		// 파일이 존재할 경우 파일 읽기 - MAKELIST 호출 이후 반복
					resultListVO	= this.getFileList(RESFILE, nStartNo, nEndNo, img_server, img_resize_server, img_cat_server, paramVO);
					//System.out.println("####################★★★★★이이전버전 읽어서 리턴하고 끝남!!★★★★★####################: ");
					long end3 = System.currentTimeMillis();
					//System.out.println("####################getNSLists 수행시간(4)####################: " + ( end3 - start )/1000.0  );
					
					return resultListVO;
				} else {
					imcsLog.serviceLog(" File [" + RESFILE + "] open failed ", methodName, methodLine);
					
					// 5초 대기 로직 들어감
					int nWaitCnt	= 0;
					
					File fFileNameOri	= new File(ORIFILE);
					while(true) {
						Thread.sleep(1000);		// 1초 대기
						nWaitCnt ++;
						imcsLog.serviceLog(" queryWaitCheck Sleep [" + nWaitCnt + "] sec", methodName, methodLine);
						
						long end8 = System.currentTimeMillis();
						//System.out.println("####################getNSLists 수행시간(8-0)####################: " + ( end8 - start )/1000.0  );
						//System.out.println("####################getNSLists 수행시간(8-1)####################: " + fFileNameOri  );
						
						if(nWaitCnt == 5)
							break;
						
						if(fFileNameOri.exists()){
							//System.out.println("####################getNSLists 수행시간(8-2)####################: " + fFileNameOri  );
							imcsLog.serviceLog(" File [" + ORIFILE+ "] open failed ", methodName, methodLine);
							break;
						}
						
					}
					
					if(fFileNameOri.exists()) {
						resultListVO	= this.getFileList(ORIFILE, nStartNo, nEndNo, img_server, img_resize_server, img_cat_server, paramVO);
						return resultListVO;
					}
					
					if(nWaitCnt	>= 5){
						msg = " wait_count overload Failed svc2[" + ImcsConstants.API_PRO_ID994 + "]";
						imcsLog.serviceLog(msg, methodName, methodLine);

						paramVO.setResultCode("21000000");
						//throw new ImcsException();
					}
					
					long end3 = System.currentTimeMillis();
					//System.out.println("####################getNSLists 수행시간(5)####################: " + ( end3 - start )/1000.0  );
				}
			}
			
			
		} catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			if(resultVO != null && !resultVO.isEmpty()) {
				resultListVO.setList(resultVO);
			}

			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID994) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
					+ " [DB:" + nUserDB + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
//		long end = System.currentTimeMillis();
		//System.out.println("####################getNSLists 수행시간(끝)####################: " + ( end - start )/1000.0  );


		return resultListVO;
	}
	
	
	
	/**
	 * GetNSList 내 파일 읽기 공통 모듈 (너무 많아서 만듬)
	 * @param RESFILE
	 * @param nStartNo
	 * @param nEndNo
	 * @return
	 */
	public GetNSListsResultVO getFileList(String RESFILE, int nStartNo, int nEndNo, String img_server, String img_resize_server, String img_cat_server, GetNSListsRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		GetNSListsResultVO resultListVO = new GetNSListsResultVO();
		String result		= FileUtil.fileRead(RESFILE, "UTF-8");
		
		if(nStartNo != 0 && nEndNo != 0) {
			String[] arrResult	= result.split(ImcsConstants.ROWSEP);
			
			result	= "";
			
			for(int i = 0; i < arrResult.length; i++) {
				if(i + 1 >= nStartNo && i + 1 <= nEndNo){
					arrResult[i] = arrResult[i].replaceAll("img_server", img_server);
					arrResult[i] = arrResult[i].replaceAll("img_resize_server", img_resize_server);
					arrResult[i] = arrResult[i].replaceAll("img_cat_server", img_cat_server);			
					result	= result + arrResult[i] + ImcsConstants.ROWSEP;
				}
			}
			
			if (Integer.parseInt(paramVO.getPageCnt()) > arrResult.length) { //TMAX VTS하고 똑 같이 맞춤
				result	= result + ImcsConstants.ROWSEP;
			}
			
		}else{
			String[] arrResult	= result.split(ImcsConstants.ROWSEP);
			
			result	= "";
			
			for(int i = 0; i < arrResult.length; i++) {
				arrResult[i] = arrResult[i].replaceAll("img_server", img_server);
				arrResult[i] = arrResult[i].replaceAll("img_resize_server", img_resize_server);
				arrResult[i] = arrResult[i].replaceAll("img_cat_server", img_cat_server);
				
				if (!arrResult[i].equals("END_STR")) //TMAX VTS하고 똑 같이 맞춤
					result	= result + arrResult[i] + ImcsConstants.ROWSEP;
			}
			
			if (Integer.parseInt(paramVO.getPageCnt()) >= arrResult.length) { //TMAX VTS하고 똑 같이 맞춤
				result	= result + ImcsConstants.ROWSEP;
			}
		}
		
		//result	= StringUtil.nullToSpace(result);
		
        String msg	= " File [" + RESFILE + "] rcvbud... [" + result.length() + "] bytes ";
        IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
        imcsLog.serviceLog(msg, methodName, methodLine);

        resultListVO.setResult(result);
		return resultListVO;
	}



	
	/**
	 * 파일명 생성 (파일명이 너무 길어 별도 메소드 생성
	 * @param GetNSListsRequestVO	paramVO
	 * @return String
	 **/
	public String getFileName(GetNSListsRequestVO paramVO, String szVerInfo, String szI20VerInfo) throws Exception {
		String szFileName	= "";
		String szFileType	= "N";
		String szTempCatId	= "";
		
		String LOCALPATH = "";
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		LOCALPATH = commonService.getCachePath("LOCAL", ImcsConstants.API_PRO_ID994.split("/")[1], imcsLog);
		
		//foldering 처리
		LOCALPATH = String.format("%s/%s", LOCALPATH, paramVO.getCatId().substring(0 ,2));
		
		if(paramVO.getCatId().length() > 1)
			szTempCatId	= paramVO.getCatId().substring(0, 2);
		
		if(!szTempCatId.equals("VC") && !szTempCatId.equals("CA")) {
			
			// VC, CA가 아닐 경우
			if(paramVO.getNscGb().equals("UFX") && paramVO.getFxType().equals("H"))
				szFileType	= "Y";
			else
				szFileType	= "N";
			
			
			szFileName	= LOCALPATH + "/Ver1-G" + paramVO.getCatGb() + "_" + paramVO.getCatId() + "-" + szVerInfo + "-T" + paramVO.getTestSbc() +  
					"-D" + paramVO.getDefinFlag()+ "-N" + paramVO.getNscType() + "-R" + paramVO.getRating() + "-O" + paramVO.getOrderGb() + "-Y" + paramVO.getYouthYn() +  
					"-B" + paramVO.getBaseOneCd() + "-Q" + paramVO.getQuickDisYn() + "-U" + szFileType + "-P" + paramVO.getPurchasable() + ".res";
		} else {
			if(szTempCatId.equals("VC"))
				szFileName	= LOCALPATH + "/Ver1-G" + paramVO.getCatGb() + "_" + paramVO.getCatId() + "-V" + szI20VerInfo + "_" + szVerInfo + 
					"-T" + paramVO.getTestSbc()+ "-D" + paramVO.getDefinFlag() + "-N" + paramVO.getNscType() +"-Y" + paramVO.getYouthYn() + 
					"-B" + paramVO.getBaseOneCd()+ "-Q" + paramVO.getQuickDisYn() +"-P" + paramVO.getPurchasable() +".res";
			else
				szFileName	= LOCALPATH + "/Ver1-G" + paramVO.getCatGb() + "_" + paramVO.getCatId() + "-V" + szI20VerInfo + "_" + szVerInfo + 
				"-T" + paramVO.getTestSbc()+ "-D" + paramVO.getDefinFlag() + "-N" + paramVO.getNscType() +"-Y" + paramVO.getYouthYn() + 
				"-Q" + paramVO.getQuickDisYn() +"-P" + paramVO.getPurchasable() +".res";
		}
		
		return szFileName;
	}

	
	
	
	/**
	 * VC인 경우의 VERSION LIST를 조회한다
	 * @param GetNSListsRequestVO	paramVO
	 * @return HashMap<String, String>
	 **/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap<String, String> getVerInfo(GetNSListsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();

		String sqlId = "lgvod994_006_20171214_001";
		
		HashMap<String, String> mVerInfo	 = new HashMap<String, String>();
		List<HashMap> list = null;
		
		try {
			try {
				list = getNSListsDao.getVerInfo();
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			
			if (list != null && !list.isEmpty()) {
				mVerInfo = (HashMap) list.get(0);

				paramVO.setI20Version(StringUtil.nullToSpace(mVerInfo.get("VOD_VERSION")));
				paramVO.setI20PVersion(StringUtil.nullToSpace(mVerInfo.get("P_VOD_VERSION")));
				paramVO.setI20PPVersion(StringUtil.nullToSpace(mVerInfo.get("PP_VOD_VERSION")));
			}
			
			paramVO.setSubVersion(paramVO.getCatId());
			paramVO.setSubPVersion(paramVO.getCatId());
			paramVO.setSubPPVersion(paramVO.getCatId());
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		
		//##############################
		//list.clear();
		//##############################

		return mVerInfo;
	}
	
	
	
	/**
	 * GUIDE VOD 리스트를 조회한다
	 * @param GetNSListsRequestVO	paramVO
	 * @return HashMap<String, String>
	 **/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap<String, String> getI20VerInfo(GetNSListsRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();

		String sqlId = "lgvod994_004_20171214_001";
		
		HashMap<String, String> mVerInfo	 = new HashMap<String, String>();
		List<HashMap> list = new ArrayList<HashMap>();
		
		try {
			try {
				list = getNSListsDao.getI20VerInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			
			if (list == null || list.isEmpty()) {
				paramVO.setSubVersion(paramVO.getCatId());
				paramVO.setSubPVersion(paramVO.getCatId());
				paramVO.setSubPPVersion(paramVO.getCatId());
			} else {
				mVerInfo = (HashMap) list.get(0);

				paramVO.setSubVersion(StringUtil.nullToSpace(mVerInfo.get("VOD_VERSION")));
				paramVO.setSubPVersion(StringUtil.nullToSpace(mVerInfo.get("P_VOD_VERSION")));
				paramVO.setSubPPVersion(StringUtil.nullToSpace(mVerInfo.get("PP_VOD_VERSION")));
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		

		return mVerInfo;
	}
	
	
	
	
	
	
	/**
	 * 부모 카테고리 ID 조회
	 * 카테고리 레벨 3부터는 업데이트 버전정보가 없으므로 버전이 입력된 최종 레벨인 2레벨의 버전정보를 사용 한다.
	 * @param paramVO
	 * @return String		TEST_SBC	
	 */
	public String getParentCatId(GetNSListsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 		= "lgvod994_003_20171214_001";
		String szParentCatId	= "";
		
		List<String> list = new ArrayList<String>();

		try {
			try {
				list = getNSListsDao.getParentCatId(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list == null || list.isEmpty()) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID994, sqlId, cache, "ver_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				
				paramVO.setSubVersion(paramVO.getCatId());
				paramVO.setSubPVersion(paramVO.getCatId());
				paramVO.setSubPPVersion(paramVO.getCatId());
			} else {
				szParentCatId	= StringUtil.replaceNull(list.get(0), "");
			}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
//			imcsLog.failLog(ImcsConstants.API_PRO_ID994, sqlId, cache, "ver_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			throw new ImcsException();	
		}
		
		
		return szParentCatId;
	}
	
	
	
	
	
	/**
	 * 카테고리 상세정보 조회
	 * @param GetNSListsRequestVO	paramVO
	 * @return HashMap<String, String>
	 **/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getCateInfo(GetNSListsRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();

		String sqlId = "lgvod994_002_20171214_001";
		String szCatLevel = "";
		
		HashMap<String, String> mCateInfo	 = new HashMap<String, String>();
		List<HashMap> list = null;
		
		try {
			try {
				list = getNSListsDao.getCateInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list != null && !list.isEmpty()) {
				mCateInfo = (HashMap) list.get(0);
				
				paramVO.setCateLevel(StringUtil.nullToSpace(mCateInfo.get("CAT_LEVEL")));
				paramVO.setNscGb(StringUtil.nullToSpace(mCateInfo.get("NSC_GB")));
				paramVO.setCateName(StringUtil.nullToSpace(mCateInfo.get("CATE_NAME")));
				
				szCatLevel	= paramVO.getCateLevel();
			}
			
			if(list == null || list.isEmpty() || "".equals(szCatLevel)) {
				paramVO.setCateLevel("0");
				paramVO.setNscGb("LTE");
				paramVO.setCateName("");
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
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
		
		String sqlId 		= "lgvod994_001_20171214_001";
		String szTestSbc	= "N";
		String szViewFlag2	= "";
		
		int querySize		= 0;
		
		List<String> list = new ArrayList<String>();

		try {		
			try {
				list = getNSListsDao.getTestSbc(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list != null && !list.isEmpty()) {
				querySize	= list.size();
				szTestSbc	= StringUtil.replaceNull(list.get(0), "N");
				
				if(szTestSbc.equals("Y"))	szViewFlag2 = "T";
				else						szViewFlag2 = "V";
				
				paramVO.setViewFlag2(szViewFlag2);
			}else{
				szTestSbc = "N";
				//paramVO.setViewFlag2("N");
				paramVO.setViewFlag2("V");
			}
			
			try{
				imcsLog.dbLog(ImcsConstants.API_PRO_ID994, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
			//paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return szTestSbc;
	}
}
