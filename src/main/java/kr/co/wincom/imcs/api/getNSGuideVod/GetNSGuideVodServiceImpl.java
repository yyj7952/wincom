package kr.co.wincom.imcs.api.getNSGuideVod;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.vo.UrlListVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetNSGuideVodServiceImpl implements GetNSGuideVodService {
	private Log imcsLogger	= LogFactory.getLog("API_getNSGuideVod");
	
	@Autowired
	private GetNSGuideVodDao getNSGuideVodDao;

//	public void getNSGuideVod(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	/**
	 * @author HONG
	 * @since 2016-06-21
	 * GUIDE VOD 정보 가져오기 (lgvod569.pc)
	 */
	@Override
	public GetNSGuideVodResultVO getNSGuideVod(GetNSGuideVodRequestVO paramVO)	{
//		this.getNSGuideVod(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog("GetNSGuideVod service call");
		
		String isLastProcess = ImcsConstants.RCV_MSG5;

		List<GetNSGuideVodResponseVO> resultVO	= new ArrayList<GetNSGuideVodResponseVO>();
		GetNSGuideVodResponseVO tempVO			= new GetNSGuideVodResponseVO();
		GetNSGuideVodResultVO	resultListVO	= new GetNSGuideVodResultVO();
		
		String szVodServer1 = "";
		String szVodServer2 = "";
		String szVodServer3 = "";
		
		String msg			= "";
		
		long tp1 = 0, tp2 = 0;
		
		try {
			// 지역노드 정보 조회
			UrlListVO urlListVO	= new UrlListVO();
			tp1 = System.currentTimeMillis();
			
			urlListVO = this.getServerIpList(paramVO);
			szVodServer1 = urlListVO.getUrl1();
			szVodServer2 = urlListVO.getUrl2();
			szVodServer3 = urlListVO.getUrl3();

			tp2	= System.currentTimeMillis();
			
			imcsLog.timeLog("지역노드 정보 조회", String.valueOf(tp2 - tp1), methodLine, methodLine);
			
			// 3. GuideVod 정보리스트 조회

			resultVO			= this.getGuideVodInfo(paramVO);
			
			tp1	= System.currentTimeMillis();			
			imcsLog.timeLog("Guide VOD 정보 조회", String.valueOf(tp1 - tp2), methodLine, methodLine);

			for(int i = 0; i < resultVO.size(); i ++) {
				tempVO = resultVO.get(i);

				tempVO.setVodServer1(szVodServer1);
				tempVO.setVodServer2(szVodServer2);
				tempVO.setVodServer3(szVodServer3);

				/*
				사용안해서 주석처리
				if("Y".equals(tempVO.getHdContent() || "S".equals(tempVO.getHdContent()))) 
					tempVO.setIdHd("Y");
				else
					tempVO.setIdHd("N");*/
				
				//통계 로그용
				if(resultVO.size() - 1 == i) {
					resultListVO.setContentsId(tempVO.getAlbumId());
					resultListVO.setContentsName(tempVO.getTitle());
					resultListVO.setCatId(tempVO.getCatId());
					resultListVO.setCatName(tempVO.getCatName());
				}
				
				resultVO.set(i, tempVO);
			}
			
			tp2	= System.currentTimeMillis();			
			imcsLog.timeLog("Guide VOD 정보 Fetch", String.valueOf(tp2 - tp1), methodLine, methodLine);
			
			resultListVO.setList(resultVO);
		} catch(ImcsException ce) {
			isLastProcess = ImcsConstants.RCV_MSG6;
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID569) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
				
		return resultListVO;
	}
	
	
	
	/**
	 * 서버 IP 리스트 조회
	 * @param GetNSGuideVodRequestVO paramVO
	 * @return GetNSGuidVodResultVO - VODSERVER1, 2, 3의 정보만 보유
	 **/
	public UrlListVO getServerIpList(GetNSGuideVodRequestVO paramVO) throws Exception {
		UrlListVO guideVodResultVO	= new UrlListVO();
		
		if(paramVO.getBaseGb().equals("Y")) {
			paramVO.setBaseCondi(paramVO.getBaseCd());

			// 1. 해당 가입자의 가장 가까운 위치 SERVER IP 3개 조회 - BASE_CD를 이용하여 조회
			guideVodResultVO = this.getServerIpByBaseCd(paramVO);

			if(guideVodResultVO == null){
				paramVO.setBaseCondi(paramVO.getBaseOneCd());
				guideVodResultVO = this.getServerIpByBaseCd(paramVO);
			}
			
			if(guideVodResultVO == null){
				paramVO.setBaseCondi("1234567890");
				guideVodResultVO = this.getServerIpByBaseCd(paramVO);
			}
		} else {
			// 2. 해당 가입자의 가장 가까운 위치 SERVER IP 3개 조회 - SA_ID, STB_MAC을 이용하여 조회
			guideVodResultVO = this.getServerIpBySaMac(paramVO);

			if(guideVodResultVO == null){
				// 동 정보를 고정으로 하여 다시 가장 가까운 위치 SERVER IP 3개 재조회
				paramVO.setBaseCondi("1234567890");
				guideVodResultVO = this.getServerIpBySaMacDong(paramVO);
			}
		}
		
		return guideVodResultVO;
	}
	
	/**
	 * BASE_CD를 기반으로 서버IP 리스트 조회
	 * @param GetNSGuideVodRequestVO paramVO
	 * @return UrlListVO - VODSERVER1, 2, 3의 정보만 보유
	 **/
	public UrlListVO getServerIpByBaseCd(GetNSGuideVodRequestVO paramVO) throws Exception {
		String sqlId = "lgvod569_001_20171214_001";
		
		List<UrlListVO> list = new ArrayList<UrlListVO>();
		UrlListVO resultVO = null;

		try {
			try {
				list = getNSGuideVodDao.getServerIpbyBaseCd(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list != null && !list.isEmpty()) {
				resultVO = (UrlListVO) list.get(0);
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog("-E",e.getClass().getName()+":"+e.getMessage());
		}

		return resultVO;
	}
	
	
	/**
	 * SA_ID, STB_MAC 을 기반으로 서버IP 리스트 조회
	 * @param GetNSListsVO paramVO
	 * @return GetNSGuidVodResultVO - VODSERVER1, 2, 3의 정보만 보유
	 **/
	public UrlListVO getServerIpBySaMac(GetNSGuideVodRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodName = oStackTrace.getMethodName();
    	
		String sqlId = "lgvod569_002_20171214_001";
		
		List<UrlListVO> list	= new ArrayList<UrlListVO>();
		UrlListVO resultVO	= null;

		try {
			try {
				list = getNSGuideVodDao.getServerIpBySaMac(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list != null && !list.isEmpty()) {
				resultVO = (UrlListVO) list.get(0);
			}

		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}

		return resultVO;
	}
	
	
	/**
	 * SA_ID, STB_MAC 을 기반으로 서버IP 리스트 조회
	 * DONG_CD 은 '1234567890' 쿼리 내 하드코딩 상태로 조회
	 * @param GetNSListsVO paramVO
	 * @return GetNSGuidVodResultVO - VODSERVER1, 2, 3의 정보만 보유
	 **/
	public UrlListVO getServerIpBySaMacDong(GetNSGuideVodRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodName = oStackTrace.getMethodName();
    	
 		String sqlId = "lgvod569_003_20171214_001";
		

		List<UrlListVO> list = null;
		UrlListVO resultVO = null;

		try {
			try {
				list = getNSGuideVodDao.getServerIpBySaMacDong(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list != null && !list.isEmpty()) {
				resultVO = (UrlListVO) list.get(0);
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}

		return resultVO;
	}
	
	
	
	/**
	 * GUIDE VOD 리스트를 조회한다
	 * @param GetNSListsVO paramVO
	 * @return GetNSGuidVodResultVO - VODSERVER1, 2, 3의 정보만 보유
	 **/
	public List<GetNSGuideVodResponseVO> getGuideVodInfo(GetNSGuideVodRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodName = oStackTrace.getMethodName();
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
		String sqlId = "lgvod569_004_20171214_001";
		
		List<GetNSGuideVodResponseVO> list = new ArrayList<GetNSGuideVodResponseVO>();
		
		int querySize = 0;

		try {
			try {
				list = getNSGuideVodDao.getGuideVodInfo(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list != null && !list.isEmpty()) {
				querySize = list.size();
			}
			
//			imcsLog.dbLog(ImcsConstants.API_PRO_ID569, sqlId, cache, querySize, methodName, methodLine);
			
		} catch (Exception e) {
//			if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID569, sqlId, cache, "file_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return list;
	}
	
	
}
