package kr.co.wincom.imcs.api.getNSVoteAlbum;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.api.getNSCatBillInfo.GetNSCatBillInfoRequestVO;
import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetNSVoteAlbumServiceImpl implements GetNSVoteAlbumService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSVoteAlbum");
	
	@Autowired
	private GetNSVoteAlbumDao getNSVoteAlbumDao;
	
	@Autowired
	private CommonService commonService;

//	public void getNSVoteAlbum(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@SuppressWarnings("unused")
	@Override
	public GetNSVoteAlbumResultVO getNSVoteAlbum(GetNSVoteAlbumRequestVO paramVO){
//		this.getNSVoteAlbum(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		GetNSVoteAlbumResponseVO tempVO = new GetNSVoteAlbumResponseVO();
		List<GetNSVoteAlbumResponseVO> tempVOList	= new ArrayList<GetNSVoteAlbumResponseVO>();
		List<GetNSVoteAlbumResponseVO> resultVO	= new ArrayList<GetNSVoteAlbumResponseVO>();
		GetNSVoteAlbumResultVO resultListVO	= new GetNSVoteAlbumResultVO();

		String msg	= "";
		String resultHeader = "0";
		int nMainCnt = 0;
        String iResult  = "";	// 성공여부 코드값. 0:성공, 1:오류

	    long tp_start = paramVO.getTp_start();
		long tp1, tp2 = 0;

		try{
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp1 - tp_start), methodName, methodLine); 
			
			
			String poster_url   = commonService.getImgReplaceUrl2("img_server", "getNSVoteAlbum");
			String still_url    = commonService.getImgReplaceUrl2("img_still_server", "getNSVoteAlbum");
			
			
			
			//testSbc1, testSbc2 의 값을 paramVO 넣어줌
			this.getTestSbc(paramVO);
			
			tempVOList = this.getVoteAlbumDtlList(paramVO);
			
			for(int i = 0; i < tempVOList.size(); i++) {
				tempVO = new GetNSVoteAlbumResponseVO();
				tempVO =  tempVOList.get(i);
				tempVO.setPosterUrl(poster_url);
				tempVO.setStillUrl(still_url);
				this.getStillImage(paramVO, tempVO);
				
				this.getCueSheetInfo(paramVO, tempVO);
				//ONAIR DATE 설정 (큐시트 정보가 있으면 큐시트 정보의 PERFROM_DATE|PERFORM_TIME을 주고 큐시트 정보가 없을 경우에는 메타의 onair_date_display를 준다.
				if(tempVO.getPerformDate().length() >= 8) {
					tempVO.setOnairDate(tempVO.getPerformDate() + tempVO.getPerformTime());
				}
				
				
				
				resultVO.add(tempVO);
				
				if(resultVO != null){
					nMainCnt = resultVO.size();
				}else{
					paramVO.setResultCode("21000000");
//					imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID045, "", null, "chnl_list:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				}
				
				resultListVO.setList(resultVO);
			}
			resultHeader = String.format("%s|%s|%d|", "0" , msg, nMainCnt);
			resultListVO.setResultHeader(resultHeader);
			
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
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID045) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	

	
	/**
     * TEST_SBC1, TEST_SBC2 값 조회
     * @param	GetNSVoteAlbumRequestVO
    **/
    public void getTestSbc(GetNSVoteAlbumRequestVO paramVO) throws Exception{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		HashMap<String, String> testSbcMap = new HashMap<String, String>();
		
		try {
			
			testSbcMap  = getNSVoteAlbumDao.getTestSbc(paramVO);
			paramVO.setTestSbc1(testSbcMap.get("TEST_SBC1"));
			paramVO.setTestSbc2(testSbcMap.get("TEST_SBC2"));
			
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
    }
    
    /**
     * TEST_SBC1, TEST_SBC2 값 조회
     * @param	GetNSVoteAlbumResponseVO
    **/
    public List<GetNSVoteAlbumResponseVO> getVoteAlbumDtlList(GetNSVoteAlbumRequestVO paramVO) throws Exception{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		List<GetNSVoteAlbumResponseVO> list = new ArrayList<GetNSVoteAlbumResponseVO>();
		
		try {
			list  = getNSVoteAlbumDao.getVoteAlbumDtlList(paramVO);
			
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return list;
    }
    
    
    /**
     * TEST_SBC1, TEST_SBC2 값 조회
     * @param	GetNSVoteAlbumRequestVO
    **/
    public void getStillImage(GetNSVoteAlbumRequestVO paramVO, GetNSVoteAlbumResponseVO tempVO) throws Exception{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		ArrayList<String> stillImageList = new ArrayList<String>();
		
		try {
			
			stillImageList  = getNSVoteAlbumDao.getStillImage(tempVO);
			tempVO.setMainImgFileName(stillImageList.get(0));
			
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
    }
    
    /**
     * TEST_SBC1, TEST_SBC2 값 조회
     * @param	GetNSVoteAlbumRequestVO
    **/
    public void getCueSheetInfo(GetNSVoteAlbumRequestVO paramVO, GetNSVoteAlbumResponseVO tempVO) throws Exception{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		GetNSVoteAlbumResponseVO cueSheetMap = new GetNSVoteAlbumResponseVO();
		
		try {
			
			if("D".equals(tempVO.getMusicType()) || "T".equals(tempVO.getMusicType())) {
				cueSheetMap  = getNSVoteAlbumDao.getCueSheetInfo1(tempVO);
			} else {
				cueSheetMap  = getNSVoteAlbumDao.getCueSheetInfo2(tempVO);
			}
			
			if(cueSheetMap != null) {
				tempVO.setPerformDate(cueSheetMap.getPerformDate());
				tempVO.setPerformTime(cueSheetMap.getPerformTime());
			}
			
			
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
    }

}
