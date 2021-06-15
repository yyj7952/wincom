package kr.co.wincom.imcs.api.getNSContReview;

import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetNSContReviewServiceImpl implements GetNSContReviewService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSContReview");
	
	@Autowired
	private GetNSContReviewDao getNSContReviewDao;
	
	@Autowired
	private CommonService commonService;

//	public void getNSContReview(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@SuppressWarnings("unused")
	@Override
	public GetNSContReviewResultVO getNSContReview(GetNSContReviewRequestVO paramVO){
//		this.getNSContReview(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		List<GetNSContReviewResponseVO> resultVO	= new ArrayList<GetNSContReviewResponseVO>();
		GetNSContReviewResultVO resultListVO	= new GetNSContReviewResultVO();

		String msg	= "";
		String resultHeader = "";
		int nMainCnt = 0;
        // header계산
        int iCntDtl  = 0;
        int iCntPre  = 0;
        int iCntOn   = 0;
        int iCntPost = 0;
        int iResult  = 0;	// 성공여부 코드값. 0:성공, 1:오류

	    long tp_start = paramVO.getTp_start();
		long tp1, tp2 = 0;

		try{
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp1 - tp_start), methodName, methodLine); 
			
			//######################################################
			// 로직구현 (시작)
			//######################################################
			int nPageNo			= Integer.parseInt(StringUtil.nullToZero(paramVO.getPageNo()));
			int nPageCnt		= Integer.parseInt(StringUtil.nullToZero(paramVO.getPageCnt()));
			int nStartNo		= 0;
			int nEndNo			= 0;
			
			int nRqs_Type       = 0;
			int iTypeEnd        = 0;
			
			if(nPageNo != 0 && nPageCnt != 0) {
				nStartNo = (nPageNo * nPageCnt) - (nPageCnt - 1);
				nEndNo   = (nPageNo * nPageCnt);
			}
			
			paramVO.setStartNum(String.valueOf(nStartNo));
			paramVO.setEndNum(String.valueOf(nEndNo));
			
			for ( int i = nRqs_Type; i <= iTypeEnd; i++) {
				
				List<GetNSContReviewResponseVO> lists = new ArrayList<GetNSContReviewResponseVO>();
				
				paramVO.setnRqsType(String.valueOf(i));
				if(paramVO.getReviewFlag().equals("W") || paramVO.getReviewFlag().equals("A")) {
					tp1 = System.currentTimeMillis();
					lists = this.getWatchaReviewList(paramVO);
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("와챠 리뷰 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
				
					if(lists.size() == 0 && paramVO.getReviewFlag().equals("W")) {
						//결과 헤더 구성
						//iResult, 성공여부 코드값 (0:성공, 1:실패)
						//headermsg, 결과메시지
						iResult = 1;
						resultHeader  = String.format("%d|%s|", iResult, "WATCHA REVIEW가 없습니다.");
						resultListVO.setResultHeader(resultHeader);
					}
					
					for (GetNSContReviewResponseVO item : lists) {
			            resultVO.add(item);
					}
				}
				
				if(paramVO.getReviewFlag().equals("C") || paramVO.getReviewFlag().equals("A")) {
					lists = new ArrayList<GetNSContReviewResponseVO>();
					tp1 = System.currentTimeMillis();
					lists = this.getWatchaReviewList2(paramVO);
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("씨네21 리뷰 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
					
					if(lists.size() == 0 && paramVO.getReviewFlag().equals("C")) {
						//결과 헤더 구성
						//iResult, 성공여부 코드값 (0:성공, 1:실패)
						//headermsg, 결과메시지
						iResult = 1;
						resultHeader  = String.format("%d|%s|", iResult, "씨네21 REVIEW가 없습니다.");
						resultListVO.setResultHeader(resultHeader);
					}
					
					for (GetNSContReviewResponseVO item : lists) {
			            resultVO.add(item);
					}
				}
			}
			
			if(resultVO.size() > 0) {
				resultHeader  = String.format("%d|%s|", iResult, "");
				resultListVO.setResultHeader(resultHeader);
			} else {
				iResult = 1;
				resultHeader  = String.format("%d|%s|", iResult, "REVIEW가 없습니다.");
				resultListVO.setResultHeader(resultHeader);
			}
			
			
			if(resultVO != null){
				nMainCnt = resultVO.size();
			}else{
				paramVO.setResultCode("21000000");
//				imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID410, "", null, "chnl_list:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			}
			
			resultListVO.setList(resultVO);
			
		}catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw ie;
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID410) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	
	
	/**
	 * 컨텐츠 왓챠 리뷰 조회
	 * @param paramVO
	 * @return GetNSContReviewResponseVO		list	
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<GetNSContReviewResponseVO> getWatchaReviewList(GetNSContReviewRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		
		int querySize		= 0;
		List<GetNSContReviewResponseVO> list = new ArrayList<GetNSContReviewResponseVO>();

		try {			
			list = getNSContReviewDao.getWatchaReviewList(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			if (list != null && !list.isEmpty()) {
				querySize	= list.size();
			} else {
				list = new ArrayList<GetNSContReviewResponseVO>();
			}
			
			try{
				String szMsg = " watcha_Review_Count["+ querySize +"]"; 
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return list;
	}
	
	/**
	 * 컨텐츠 시네21 리뷰 조회
	 * @param paramVO
	 * @return GetNSContReviewResponseVO		list	
	 */
	public List<GetNSContReviewResponseVO> getWatchaReviewList2(GetNSContReviewRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		
		int querySize		= 0;
		List<GetNSContReviewResponseVO> list = new ArrayList<GetNSContReviewResponseVO>();

		try {			
			list = getNSContReviewDao.getWatchaReviewList2(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			if (list != null && !list.isEmpty()) {
				querySize	= list.size();
			} else {
				list = new ArrayList<GetNSContReviewResponseVO>();
			}
			
			try{
				String szMsg = " cine21_Review_Count["+ querySize +"]"; 
				imcsLog.serviceLog(szMsg, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return list;
	}

}
