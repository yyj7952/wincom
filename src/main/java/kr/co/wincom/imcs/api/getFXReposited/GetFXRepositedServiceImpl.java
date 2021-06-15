package kr.co.wincom.imcs.api.getFXReposited;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.DateUtil;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.AlbumInfoVO;
import kr.co.wincom.imcs.common.vo.CateInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetFXRepositedServiceImpl implements GetFXRepositedService {
	private Log imcsLogger		= LogFactory.getLog("API_getFXReposited");
	
	@Autowired
	private GetFXRepositedDao getFXRepositedDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getFXReposited(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	/**
	 * @author HONG
	 * @since 2016-07-12
	 * 찜목록 리스트 가져오기 (lgvod339.pc)
	 */
	@Override
	public GetFXRepositedResultVO getFXReposited(GetFXRepositedRequestVO paramVO) {
//		this.getFXReposited(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		List<GetFXRepositedResponseVO> resultVO	= new ArrayList<GetFXRepositedResponseVO>();
		GetFXRepositedResponseVO tempVO			= new GetFXRepositedResponseVO();
		GetFXRepositedResultVO	resultListVO	= new GetFXRepositedResultVO();
		
		int nMainCnt		= 0;
		
		long tp1			= 0;								// timePoint 1
		long tp2			= System.currentTimeMillis();		// timePoint 2
		
		String msg			= "";
		String szImgSvrIp	= "";
		
		try {
			// 서버 IP 조회
			try {
				szImgSvrIp	= commonService.getIpInfo("img_resize_server", ImcsConstants.FXAPI_PRO_ID100.split("/")[1]);
			} catch(Exception e) {
//				imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID100, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
				throw new ImcsException();
			}
			
			tp1 = System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp1 - tp2), methodName, methodLine);
						
			resultVO	= this.getRepositedList(paramVO);
			tp2 = System.currentTimeMillis();
			imcsLog.timeLog("시청목록 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			String szCurrentTime	= commonService.getSysdate();
			
			if(resultVO != null && resultVO.size() > 0)
				nMainCnt	= resultVO.size();
			
			for(int i = 0; i < nMainCnt; i++) {
				tempVO	= resultVO.get(i);
				
				// 구매일, 현재일에 대한 날짜 계산 로직 적용 예정 (변수 등 정리필요)
				DateUtil dateUtil = new DateUtil();
				Date dBuyEndDate = dateUtil.getStringToDate(tempVO.getExpiredYn(), "yyyyMMddHHmmss");
				Date dCurrentTime = dateUtil.getStringToDate(szCurrentTime, "yyyyMMddHHmmss");
				
				int n = dBuyEndDate.compareTo(dCurrentTime);
				
				if(n < 0)		tempVO.setExpiredYn("Y");		// 현재 시간이 만료일보다 크면 Y
				else			tempVO.setExpiredYn("N");		// 현재 시간이 만료일보다 크면 N
				
				tempVO.setBuyingPrice("");
				tempVO.setExpiredDate("");
				tempVO.setCpUseYn("");
				
				// 카테고리 정보 조회
				paramVO.setContsId(tempVO.getAlbumId());
				CateInfoVO cateInfoVO = new CateInfoVO();
				cateInfoVO	= this.getCateInfo(paramVO);
				
				
				if(cateInfoVO != null) {
					tempVO.setCatId(cateInfoVO.getCategoryId());
					//tempVO.getCatId();		// 안씀
					tempVO.setIsNew(cateInfoVO.getIsNew());
					tempVO.setChaNum(cateInfoVO.getChaNum());
					tempVO.setAuthYn(cateInfoVO.getAuthYn());
					tempVO.setSeriesYn(cateInfoVO.getSeriesYn());
					tempVO.setBelongingName(cateInfoVO.getBelongingName());
					
					String szSerInfo	 = cateInfoVO.getCateInfo();
					tempVO.setSerCatId(szSerInfo.substring(0, szSerInfo.indexOf("|")));
					tempVO.setSeriesNo(szSerInfo.substring(szSerInfo.indexOf("|") + 1, szSerInfo.length()));
				} else {
					cateInfoVO	= new CateInfoVO();
				}
				
				if("0".equals(StringUtil.nullToZero(cateInfoVO.getCount()))){
					msg = " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID100) + "] sts[" + ImcsConstants.LOG_MSG2 + "] msg[" + String.format("%-21s", "cate_info:no data found(" + paramVO.getContsId() + ")" + ImcsConstants.RCV_MSG1 + "]");
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
				
				// 앨범정보 조회
				paramVO.setSrcGb(tempVO.getSrcGb());
				AlbumInfoVO albumInfoVO = new AlbumInfoVO();
				albumInfoVO = this.getAlbumInfo(paramVO);
				
				if(albumInfoVO != null) {
					tempVO.setAlbumName(albumInfoVO.getContsName());
					tempVO.setOnairDate(albumInfoVO.getOnairDate());
					tempVO.setSeriesDesc(albumInfoVO.getSeriesDesc());
					tempVO.setRealHd(albumInfoVO.getRealHd());
					tempVO.setPoint(albumInfoVO.getPoint());
					tempVO.setPrInfo(albumInfoVO.getPrInfo());
					tempVO.setIsHd(albumInfoVO.getIsHd());
					tempVO.setRunTime(albumInfoVO.getRunTime());
					tempVO.setLicensingWindowEnd(albumInfoVO.getLicensingEnd());
					tempVO.setGenreGb(albumInfoVO.getGenreGb());
					tempVO.setImgFileName(albumInfoVO.getTempMinValue());
				}
				
				// 스틸이미지 정보 조회
				String szImgFileName	= "";
				szImgFileName	= this.getImgFileName(paramVO);
				tempVO.setThumbnailFileName(StringUtil.nullToSpace(szImgFileName));
				
				tempVO.setImgUrl(szImgSvrIp);
				
				resultVO.set(i, tempVO);
			}
			
			tp1 = System.currentTimeMillis();
			imcsLog.timeLog("시청목록 FETCH", String.valueOf(tp1 - tp2), methodName, methodLine);
			
			resultListVO.setList(resultVO);
		} catch(ImcsException ie) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
			throw ie;
		} catch(Exception e) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException();
		} finally{
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID100) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}

	
	
	
	
	
	/**
	 * 시청목록 조회
	 * @param 	GetFXRepositedRequestVO paramVO
	 * @return  String
	 **/
	public List<GetFXRepositedResponseVO> getRepositedList(GetFXRepositedRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod100_031_20171214_001";
	    
		List<GetFXRepositedResponseVO> list = new ArrayList<GetFXRepositedResponseVO>();
		
		int querySize	= 0;
		
		try {
			if (paramVO.getFxType().equals("NSC"))
				list = getFXRepositedDao.getRepositedList1(paramVO);
			else
				list = getFXRepositedDao.getRepositedList2(paramVO);
			
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			if (list == null || list.isEmpty()) 	querySize	= 0;
			else									querySize	= list.size();
			
			try{
//				imcsLog.dbLog2(ImcsConstants.FXAPI_PRO_ID100, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
			//paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
		}catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID100, sqlId, null, "buys_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);

		}
		
		return list;
	}

	
	
	/**
	 * 카테고리 정보 조회
	 * @param 	GetFXRepositedRequestVO paramVO
	 * @return  String
	 **/
	public CateInfoVO getCateInfo(GetFXRepositedRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String sqlId = "fxvod100_050_20171214_001";
		
		List<CateInfoVO> list = new ArrayList<CateInfoVO>();
		CateInfoVO resultVO = null;
		int querySize = 0;
		
		try {
			try {
				list = getFXRepositedDao.getCateInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list != null && !list.isEmpty()) {
				querySize = list.size();
				resultVO	= list.get(0);
			}

			//C에서 주석 처리된 로그
			try{
//				imcsLog.dbLog2(ImcsConstants.FXAPI_PRO_ID100, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
			//paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
		}catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID100, sqlId, null, "buys_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return resultVO;
	}
	
	
	
	
	/**
	 * 앨범정보 조회
	 * @param 	GetFXRepositedRequestVO paramVO
	 * @return  String
	 **/
	public AlbumInfoVO getAlbumInfo(GetFXRepositedRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod100_060_20171214_001";
		
		if("NSC".equals(paramVO.getFxType())) {
			if("10".equals(paramVO.getSrcGb()))		sqlId	= "fxvod100_060_20171214_001";
			else									sqlId	= "fxvod100_065_20171214_001";
		} else										sqlId	= "fxvod100_070_20171214_001";
		
		List<AlbumInfoVO> list = new ArrayList<AlbumInfoVO>();
		AlbumInfoVO resultVO = null;
		int querySize = 0;
		
		try {
				try {
					list = getFXRepositedDao.getAlbumInfo(paramVO);
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
					
				} catch (DataAccessException e) {
					// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
					throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
				}

			if (list != null && !list.isEmpty()) {
				querySize = list.size();
				resultVO	= list.get(0);
			}

			//C에서 주석 처리된 로그
			try{
//				imcsLog.dbLog2(ImcsConstants.FXAPI_PRO_ID100, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}

		}catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return resultVO;
	}
	
	
	/**
	 * 이미지 파일명 조회
	 * @param 	GetFXRepositedRequestVO paramVO
	 * @return  String
	 **/
	public String getImgFileName(GetFXRepositedRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String sqlId = "fxvod100_080_20171214_001";
		
		List<StillImageVO> list = new ArrayList<StillImageVO>();
		StillImageVO resultVO	= null;
		String szImgFileName	= "";
		int querySize = 0;
		try {
			try {
				list = getFXRepositedDao.getImgFileName(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}			
			
			if (list != null && !list.isEmpty()) {
				querySize = list.size();
				resultVO		= list.get(0);
				szImgFileName	= resultVO.getImgFileName();
			} 

			//C에서 주석 처리된 로그
			try{
//				imcsLog.dbLog2(ImcsConstants.FXAPI_PRO_ID100, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}

		}catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return szImgFileName;
	}
	
	
}
