package kr.co.wincom.imcs.api.getNSPageList;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.NosqlCacheType;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComDataFreeVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetNSPageListServiceImpl implements GetNSPageListService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSPageList");
	
	@Autowired
	private GetNSPageListDao getNSPageListDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSContList(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	/**
	 * @author HONG
	 * @since 2016-07-12
	 * 컨텐츠 타입과 구매여부, 구매시간, 구매타입 값을 리턴 (lgvod997.pc)
	 */
	@Override
	public GetNSPageListResultVO getNSPageList(GetNSPageListRequestVO paramVO)	{
//		this.getNSContList(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		List<GetNSPageListResponseVO> pageListVO	= new ArrayList<GetNSPageListResponseVO>();
		List<GetNSPageListResponseVO> resultVO	= new ArrayList<GetNSPageListResponseVO>();
		GetNSPageListResponseVO tempVO			= new GetNSPageListResponseVO();
		GetNSPageListResponseVO contDescVO		= null; //new GetNSPageListResponseVO();
		GetNSPageListResultVO	resultListVO	= new GetNSPageListResultVO();
		
		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2
		
		String msg			= "";
		
		int nStartNo		= 0;
		int nEndNo			= 0;
		int nPageNo			= 0;
		int nPageCnt		= 0;
		int nPageIdx		= 0;
		
		int MAX_RES_CNT     = 600;
		
		String szSelectAll	= "";
		
		String szImgCasheSvrIp	= "";		// 이미지 캐쉬서버 IP
		String szImgSvrUrl		= "";		// 이미지 서버 URL

		
		int nMainCnt		= 0;		// 메인쿼리 Count
		int totCnt			= 0;
		
		try {
			tp1 = System.currentTimeMillis();
			
			// 시작, 끝 번호 저장
			nPageNo		= Integer.parseInt(StringUtil.nullToZero(paramVO.getPageNo()));
			nPageCnt	= Integer.parseInt(StringUtil.nullToZero(paramVO.getPageCnt()));
			nPageIdx	= Integer.parseInt(StringUtil.nullToZero(paramVO.getPageIdx()));

			if(nPageNo != 0 && nPageCnt != 0) {
				nStartNo	= (nPageNo * nPageCnt) - (nPageCnt - 1);
				nEndNo		= nPageNo * nPageCnt;
				
				if(nPageIdx > 0) {
					nStartNo	= nPageIdx;
					nEndNo		= nStartNo + (nPageCnt - 1);
				}
				
				szSelectAll	= "N";
			} else {
				szSelectAll	= "Y";
			}
			
			paramVO.setSelectAll(szSelectAll);
			paramVO.setStartNo(String.valueOf(nStartNo));
			paramVO.setEndNo(String.valueOf(nEndNo));
			
			// 서버IP정보 조회
			try {
				szImgCasheSvrIp		= commonService.getIpInfo("img_cachensc_server", ImcsConstants.NSAPI_PRO_ID997.split("/")[1]);		// 이미지 캐쉬서버 IP 조회
				szImgSvrUrl			= commonService.getIpInfo("img_resize_server", ImcsConstants.NSAPI_PRO_ID997.split("/")[1]);			// 이미지서버 URL 조회
				
			} catch(Exception e) {
//				imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID995, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
				paramVO.setResultCode("31000000");
				
				throw new ImcsException();
			}

			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp2 - tp1), methodName, methodLine);
	
			// TEST계정 유무 조회
		    String test_sbcYN = this.testSbc(paramVO);
		    paramVO.setTestSbc(test_sbcYN);
			
			
			// 컨텐츠 리스트 조회
		    pageListVO = this.getContList(paramVO);
			
			if(pageListVO != null)	nMainCnt = pageListVO.size();
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("컨텐츠정보 조회", String.valueOf(tp2 - tp1) ,methodName, methodLine);
			
			
			for(int i = 0; i < nMainCnt; i++){
				
		    	if(i == MAX_RES_CNT)
		    	{
		    		break;
		    	}
				
				tempVO = pageListVO.get(i);
			
				tempVO.setPosterImgUrl(szImgCasheSvrIp);
				tempVO.setThumbnailImgUrl(szImgSvrUrl);
				
				if( i == 0 ){
					totCnt = tempVO.getTotCnt();
				}
				
				
				if((i == nMainCnt-1) && !tempVO.getCatName().equals("")) {
					resultListVO.setScatName(tempVO.getCatName());
				}
			
				paramVO.setContsId(tempVO.getAlbumId());
				
				
				// 컨텐츠 상세 정보 조회
				contDescVO = this.getAlbumInfo(paramVO);
				
				if(contDescVO != null) {
					
					tempVO.setAlbumName(contDescVO.getAlbumName());
					tempVO.setOnairDate(contDescVO.getOnairDate());
					tempVO.setSeriesDesc(contDescVO.getSeriesDesc());
					tempVO.setPrInfo(contDescVO.getPrInfo());
					tempVO.setContsType(contDescVO.getContsType());
					tempVO.setImgFileName(contDescVO.getImgFileName());
					tempVO.setTerrCh(contDescVO.getTerrCh());
					tempVO.setRuntime(contDescVO.getRuntime());
					tempVO.setSynopsis(contDescVO.getSynopsis());
					
				}else{
					totCnt--;
					continue;
				}
				
				
				// 썸네일이미지명 조회
				String szThumbnailFileName	= "";	// 썸네일이미지 파일명
				
				szThumbnailFileName	= this.getThumbnailImage(paramVO);

				tempVO.setThumbnailImgFileName(szThumbnailFileName);
				    			
    			resultVO.add(tempVO);
			}			
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("컨텐츠정보 FETCH", String.valueOf(tp1 - tp2), methodName, methodLine);
			
			resultListVO.setTotCnt(totCnt);
			resultListVO.setList(resultVO);
			
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
			resultListVO.setResultCode(paramVO.getResultCode());
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID997) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
					+ " [DB:" + nUserDB + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
    
	/**
	 * 스틸 이미지명 조회
	 * @param paramVO
	 * @return
	 */
	public String getThumbnailImage(GetNSPageListRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	=  "nsvod995_003_20171214_001";
					
		List<StillImageVO> list	= new ArrayList<StillImageVO>();
		String result = "";
		
		try {
			try{
				list  = getNSPageListDao.getThumbnailImage(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				result =list.get(0).getImgFileName();
			}			
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			//imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID995, sqlId, cache, "ALBUM_THUMBNAIL:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return result;
	}

	/**
	 * 컨텐츠 상세정보 조회(앨범정보 조회)
	 * @param paramVO
	 * @return
	 */
	public GetNSPageListResponseVO getAlbumInfo(GetNSPageListRequestVO paramVO) throws Exception {
    	IMCSLog imcsLog = new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	=  "nsvod995_002_20180713_002";
			
		List<GetNSPageListResponseVO> list	= new ArrayList<GetNSPageListResponseVO>();
		GetNSPageListResponseVO contDescVO	= null;
		
		try {
			try{
				list  = getNSPageListDao.getContDesc(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list != null && !list.isEmpty()) {
				contDescVO	= list.get(0);
			}
					
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			//imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID995, sqlId, cache, "ALBUM_INFO:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return contDescVO;
	}



	/**
	 * 컨텐츠 리스트 조회 
	 * @param paramVO
	 * @return
	 */
	public List<GetNSPageListResponseVO> getContList(GetNSPageListRequestVO paramVO) throws Exception {
    	IMCSLog imcsLog = new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	=  "";
		String szMsg	= "";
		
		int querySize	= 0;
		
		if( "Y".equals(paramVO.getCloseYn())) {
			if("Y".equals(paramVO.getSelectAll()))	sqlId	= "nsvod995_m01_20171214_001";
			else									sqlId	= "nsvod995_m02_20171214_001";
		} else {
			if("Y".equals(paramVO.getSelectAll()))	sqlId	= "nsvod995_m03_20171214_001";
			else									sqlId	= "nsvod995_m04_20171214_001";
		}
		
		List<GetNSPageListResponseVO> list	= new ArrayList<GetNSPageListResponseVO>();
		
		try {
			try{
				list  = getNSPageListDao.getContList(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list != null && !list.isEmpty()) {
				querySize	= list.size();
			}
			
			try{
//				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID995, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
					
		} catch (Exception e) {
			//if(cache.getLastException() != null){
			//	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
			//	
			//	szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID995) + "] SQLID[" + sqlId + "] sts[" + cache.getLastException().getErrorCode() + "] "
			//			+ String.format("%-21s", "msg[conts_info:" + cache.getLastException().getErrorMessage() + "]");
			//	imcsLog.serviceLog(szMsg, methodName, methodLine);
			//} else {
				paramVO.setResultCode("41000000");
				
				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID997) + "] SQLID[" + sqlId + "] sts[    0] "
						+ String.format("%-21s", "msg[conts_info:" + ImcsConstants.RCV_MSG6 + "]");
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			//}
			
		}
		
		return list;
	}

	/**
	 * 검수 STB 여부 조회
     * @param
     * @result 
     */
    public String testSbc(GetNSPageListRequestVO paramVO) throws Exception{
    	IMCSLog imcsLog = new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		String sqlId =  "nsvod995_001_20171214_001";
		String szTestSbc	= "N";
		
		List<String> list   = null;
		
		try {
			try{
				list  = getNSPageListDao.testSbc(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			}catch(DataAccessException e){
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				szTestSbc = StringUtil.replaceNull((String)list.get(0), "N");
				
				if(szTestSbc.equals("Y"))	paramVO.setViewFlag2("T");
				else						paramVO.setViewFlag2("V");
			}else{
				szTestSbc = "N";
				paramVO.setViewFlag2("V");
			}
		
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
    	return szTestSbc;
    }
		
}
