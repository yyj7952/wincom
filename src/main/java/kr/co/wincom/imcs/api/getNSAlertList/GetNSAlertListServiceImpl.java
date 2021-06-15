package kr.co.wincom.imcs.api.getNSAlertList;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
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
public class GetNSAlertListServiceImpl implements GetNSAlertListService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSAlertList");
	
	@Autowired
	private GetNSAlertListDao getNSAlertListDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSAlertList(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	/**
	 * @author HONG
	 * @since 2016-07-12
	 * 알람목록 리스트 가져오기 (lgvod339.pc)
	 */
	@Override
	public GetNSAlertListResultVO getNSAlertList(GetNSAlertListRequestVO paramVO)	{
//		this.getNSAlertList(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		List<GetNSAlertListResponseVO> resultVO		= new ArrayList<GetNSAlertListResponseVO>();
		GetNSAlertListResponseVO tempVO				= new GetNSAlertListResponseVO();
		GetNSAlertListResultVO	resultListVO	= new GetNSAlertListResultVO();
		
		int nMainCnt		= 0;
		
		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2
		
		String szImgSvrIp	= "";		// 이미지 서버 IP
		String szImgSvrUrl	= "";		// 이미지 서버 URL
		String msg			= "";
		
		try {
			// 이미지 서버IP조회
			tp1 = System.currentTimeMillis();
			
			try {
				szImgSvrIp	= commonService.getIpInfo("img_server", ImcsConstants.API_PRO_ID664.split("/")[1]);			// 이미지서버 IP 조회
				szImgSvrUrl	= commonService.getIpInfo("img_resize_server", ImcsConstants.API_PRO_ID664.split("/")[1]);	// 이미지서버 URL 조회
			} catch(Exception e) {							// 이미지 서버 IP 조회 실패 시 에러
//				imcsLog.failLog(ImcsConstants.API_PRO_ID664, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
				paramVO.setResultCode("31000000");
				
				throw new ImcsException();
			}

			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			
			// 채널코드정보 조회
			this.getChnlCode(paramVO);
			
			// 알림받기 조회
			resultVO	= this.getAlertListInfo(paramVO);
			
			tp1 = System.currentTimeMillis();
			imcsLog.timeLog("알림받기 조회", String.valueOf(tp1 - tp2), methodName, methodLine);

			
			if(resultVO == null || resultVO.size() == 0) {
//				imcsLog.failLog(ImcsConstants.API_PRO_ID664, "", null, "alert_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				paramVO.setResultCode("21000000");
			} else{
				nMainCnt	= resultVO.size();
			}
		    
			for(int i = 0; i < nMainCnt; i++) {
				tempVO	= resultVO.get(i);
				
				tempVO.setImgUrl(szImgSvrUrl);
				
				// 통계용 데이터 설정
				if((i == nMainCnt - 1) && (!tempVO.getContentsName().equals(""))) {	
					resultListVO.setContentsId(tempVO.getContsId());
					resultListVO.setContentsName(tempVO.getContentsName());
				}

				// 카테고리정보 조회
				CateInfoVO cateInfoVO = new CateInfoVO();
				paramVO.setAdiAlbumId(tempVO.getAdiAlbumId());
				cateInfoVO = this.getCateInfo(paramVO);
				
				if(cateInfoVO != null && !cateInfoVO.getCount().equals("")){
					String szSerInfo	= cateInfoVO.getCateInfo();				// SerCatId와 SeriseNo 분리
					
					tempVO.setParentCatId(cateInfoVO.getParentCatId());
					tempVO.setIsUpdate(cateInfoVO.getIsUpdate());
					tempVO.setContsLevel(cateInfoVO.getContsLevel());
					tempVO.setChaNum(cateInfoVO.getChaNum());
					tempVO.setSerCatId(szSerInfo.substring(0, szSerInfo.indexOf("|")));
					tempVO.setSeriesNo(szSerInfo.substring(szSerInfo.indexOf("|") + 1, szSerInfo.length()));
					tempVO.setBelongingName(cateInfoVO.getBelongingName());
				} 
				
				int cnt = 0;
				
				try {
					cnt = Integer.parseInt(cateInfoVO.getCount());
				} catch (Exception e) {
					cnt = 0;
				}
				
				if(cnt == 0){
					continue;
				}
			
				
				// 앨범정보 조회
				AlbumInfoVO albumInfoVO = new AlbumInfoVO();
				
				paramVO.setCheckValue(tempVO.getCheckValue());
				paramVO.setContsId(tempVO.getContsId());
				if(!tempVO.getParentCatId().substring(0, 1).equals("X"))	paramVO.setParentCatId("A");
				
				albumInfoVO = this.getAlbumInfo(paramVO);
				
				if(albumInfoVO != null){
					tempVO.setOnairDate(albumInfoVO.getOnairDate());
					tempVO.setReleaseDate(albumInfoVO.getReleaseDate());
					tempVO.setSeriesDesc(albumInfoVO.getSeriesDesc());
					tempVO.setRealHd(albumInfoVO.getRealHd());
					tempVO.setPoint(albumInfoVO.getPoint());
					tempVO.setPrInfo(albumInfoVO.getPrInfo());
					tempVO.setRunTime(albumInfoVO.getRunTime());
					tempVO.setPrice(albumInfoVO.getPrice());
					tempVO.setOverseerName(albumInfoVO.getOverseerName());
					tempVO.setActor(albumInfoVO.getActor());
					tempVO.setLicensingWindowEnd(albumInfoVO.getLicensingEnd());
					tempVO.setGenreGb(albumInfoVO.getGenreGb());
					tempVO.setFilterGb(albumInfoVO.getFilterGb());
					tempVO.setIsNew(albumInfoVO.getIsNew());
					tempVO.setIs51Ch(albumInfoVO.getIs51ch());
					tempVO.setHdContent(albumInfoVO.getHdContent());
					tempVO.setIs3D(albumInfoVO.getIs3D());
					tempVO.setIsCaption(albumInfoVO.getCaptionYn());
					tempVO.setServiceIcon(albumInfoVO.getServiceIcon());
					tempVO.setImgFileName(albumInfoVO.getImgFileName());
					tempVO.setTerrCh(albumInfoVO.getTerrCh());
					tempVO.setReservedType(albumInfoVO.getReservedType());
					tempVO.setSuggestedPrice(albumInfoVO.getSuggestedPrice());
					tempVO.setReservedPrice(albumInfoVO.getReservedPrice());
					tempVO.setReservedDate(albumInfoVO.getReservedDate());
					tempVO.setImgUrl("");
					tempVO.setIsHot("");
					tempVO.setSubCnt("");
				} 			
				
				
				// 상품타입정보 조회
				String szProductType	= this.getProductType(paramVO);
				
				if(szProductType == null || szProductType.equals("") || szProductType.equals("X"))
					szProductType	= "";
				
				
				tempVO.setImgUrl(szImgSvrIp);
				
				
				// (제안가)
				if(!szProductType.equals("0") && Integer.parseInt(StringUtil.nullToZero(tempVO.getPrice())) > 0)
					tempVO.setPrice("Y");
				else
					tempVO.setPrice("N");
					
				
				tempVO.setImgUrl2(szImgSvrUrl);

				// 5.1ch 여부 
				if(tempVO.getIs51Ch().equals("DOLBY 5.1"))
					tempVO.setIs51Ch("Y");
				else
					tempVO.setIs51Ch("N");
				
				// HD 여부
				if(tempVO.getHdContent().equals("Y") || tempVO.getHdContent().equals("S"))
					tempVO.setIsHd("Y");
				else if( tempVO.getHdContent().equals("N") )
					tempVO.setIsHd("N");
				
				
				
				// 이미지파일명 조회
				String szStillFileName	= "";
				if(tempVO.getGenreGb().equals("T")) {
					szStillFileName	= this.getImageFileName(paramVO);
					tempVO.setThumbnailFileName(szStillFileName);
				} else {
					szStillFileName	= this.getStillFileName(paramVO);
					tempVO.setThumbnailFileName(szStillFileName);
				}
				
				resultVO.set(i, tempVO);
			}
			
			resultListVO.setCatId(StringUtil.nullToSpace(tempVO.getParentCatId()));
			resultListVO.setList(resultVO);
			
			tp2 = System.currentTimeMillis();
			imcsLog.timeLog("알림받기 정보 Fetch", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			
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
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID664) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}

	
	
	
	/**
	 * 장르정보가 T가 아닌 경우의 이미지파일명 조회 
	 * @param paramVO
	 * @return String 	이미지파일명
	 */
	public String getStillFileName(GetNSAlertListRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId = "lgvod664_005_20171214_001";

		List<String> list = new ArrayList<String>();

		String szImageFileName	= "";
		long tp1 = System.currentTimeMillis();
		try {
			try {
				list = getNSAlertListDao.getStillFileName(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			long tp2 = System.currentTimeMillis();
	        imcsLog.timeLog("이미지파일명 (!T) 정보 조회", String.valueOf(tp2 - tp1), methodName, "");

			if (list != null && !list.isEmpty()) {
				szImageFileName	= StringUtil.nullToSpace(list.get(0));
			}
			
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			
			throw new ImcsException();
		}
		
		return szImageFileName;
	}


	/**
	 * 장르정보가 T일 경우의 이미지파일명 조회 
	 * @param paramVO
	 * @return String 	이미지파일명
	 */
	public String getImageFileName(GetNSAlertListRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId = "lgvod664_004_20171214_001";

		List<StillImageVO> list = new ArrayList<StillImageVO>();

		String szImageFileName	= "";
		String szMsg			= "";
		
		int querySize			= 0;
		long tp1 = System.currentTimeMillis();
		try {
			try {
				list = getNSAlertListDao.getImageFileName(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			long tp2 = System.currentTimeMillis();
	        imcsLog.timeLog("이미지파일명(T)  정보 조회", String.valueOf(tp2 - tp1), methodName, "");

			if (list != null && !list.isEmpty()) {
				querySize = list.size();
				
				if(querySize > 0)
					szImageFileName = StringUtil.nullToSpace(list.get(0).getImgFileName());
			}
		
		} catch (Exception e) {
//			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			
//			szMsg	 = " svc[" + String.format("%-27s", ImcsConstants.API_PRO_ID664) + "] SQLID[" + sqlId + "]" + String.format("%-21s", "msg[" + cache.getLastException().getErrorCode() + ":" + cache.getLastException().getErrorMessage() + "]");
			imcsLog.serviceLog(szMsg, methodName, methodLine);
			throw new ImcsException();
		}
		
		return szImageFileName;
	}
	
	
	/**
	 * 상품타입 정보 조회
	 * @param paramVO
	 * @return String		상품타입정보	
	 */
	public String getProductType(GetNSAlertListRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId = "";
		
		if(!paramVO.getCheckValue().equals("20"))		sqlId = "lgvod339_p02_20171214_001";
		else											sqlId = "lgvod339_p01_20171214_001";
		
		String szProductType	= "";
		
		List<String> list = new ArrayList<String>();
		long tp1 = System.currentTimeMillis();
		try {
			
			try {
				list = getNSAlertListDao.getProductType(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			long tp2 = System.currentTimeMillis();
	        imcsLog.timeLog("상품 정보 조회", String.valueOf(tp2 - tp1), methodName, "");

			if (list != null && !list.isEmpty()) {
				szProductType	= list.get(0);
			}
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException();
		}
		
		return szProductType;
	}
	
	
	/**
	 * 앨범정보 조회
	 * @param	paramVO
	 * @return	GetNSAlbumInfoVO	앨범정보
	 */
	public AlbumInfoVO getAlbumInfo(GetNSAlertListRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId = "lgvod664_s01_20171214_001";
		
		if(paramVO.getCheckValue().equals("20"))
			sqlId	= "lgvod664_s01_20171214_001";
		else
			sqlId	= "lgvod664_s02_20171214_001";
		
		List<AlbumInfoVO> list	= new ArrayList<AlbumInfoVO>();
		AlbumInfoVO resultVO	= new AlbumInfoVO();
		long tp1 = System.currentTimeMillis();
		
		try {
			try {
				if(paramVO.getCheckValue().equals("20"))
					list = getNSAlertListDao.getAlbumInfo1(paramVO);
				else
					list = getNSAlertListDao.getAlbumInfo2(paramVO);
				
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			long tp2 = System.currentTimeMillis();
	        imcsLog.timeLog("앨범 정보 조회", String.valueOf(tp2 - tp1), methodName, "");

			if (list != null && !list.isEmpty()) {
				resultVO	= list.get(0);
			}
			
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException();
		}

		return resultVO;
	}
	
	

	/**
	 * 카테고리 조회
	 * @param	GetNSAlertListRequestVO	paramVO
	 * @return	GetNSCateInfoVO		카테고리정보
	 */
	public CateInfoVO getCateInfo(GetNSAlertListRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId = "lgvod664_002_20171214_001";
		
		List<CateInfoVO> list = new ArrayList<CateInfoVO>();
		CateInfoVO resultVO	= new CateInfoVO();
		
		try {
			long tp1 = System.currentTimeMillis();
			try {
				list = getNSAlertListDao.getCateInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			long tp2 = System.currentTimeMillis();
	        imcsLog.timeLog("카테고리 정보 조회", String.valueOf(tp2 - tp1), methodName, "");

			if (list != null && !list.isEmpty()) {
				resultVO	= list.get(0);
			}
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID664, sqlId, cache, "cate_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			throw new ImcsException();
		}

		return resultVO;
	}

	
	/**
	 * 알람받기 리스트 조회
	 * @param	paramVO
	 * @return	List<GetNSAlertListResultVO>		// 알람받기 리스트
	 */
	public List<GetNSAlertListResponseVO> getAlertListInfo(GetNSAlertListRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId = "lgvod664_m01_20171214_001";
		
		if(paramVO.getPageNo().equals("A") || paramVO.getPageCnt().equals("A")) {
			if(paramVO.getChnlCd().equals(""))		sqlId	= "lgvod664_m01_20171214_001";
			else									sqlId	= "lgvod664_m02_20171214_001";
		} else {
			if(paramVO.getChnlCd().equals(""))		sqlId	= "lgvod664_m03_20171214_001";
			else									sqlId	= "lgvod664_m04_20171214_001";
		}
		
		int querySize = 0;
		
		List<GetNSAlertListResponseVO> list = new ArrayList<GetNSAlertListResponseVO>();
		
		try {
			long tp1 = System.currentTimeMillis();
			
			try {
				if(paramVO.getChnlCd().equals(""))
					list = getNSAlertListDao.getAlertListInfo1(paramVO);		// m01, m03 케이스 조회쿼리
				else
					list = getNSAlertListDao.getAlertListInfo2(paramVO);		// m02, m04 케이스 조회 쿼리
				
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			long tp2 = System.currentTimeMillis();
	        imcsLog.timeLog("알람받기 정보 조회", String.valueOf(tp2 - tp1), methodName, "");

			if (list != null && !list.isEmpty()) {
				querySize = list.size();
			}
			
			int szRetCode = 0;
			
			try{
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID664, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID664, sqlId, cache, "alert_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			throw new ImcsException();
		}
					
		return list;
	}




	/**
	 * 채널코드정보 조회
	 * @param	GetNSAlertListRequestVO
	 * @return	void
	 */
	public void getChnlCode(GetNSAlertListRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId		= "lgvod664_001_20171214_001";
		int querySize		= 0;
		
		List<String> list = new ArrayList<String>();

		try {
			long tp1 = System.currentTimeMillis();
			
			try {
				list = getNSAlertListDao.getChnlCode(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			long tp2 = System.currentTimeMillis();
	        imcsLog.timeLog("채널코드 정보 조회", String.valueOf(tp2 - tp1), methodName, "");

			if (list != null && !list.isEmpty()) {
				querySize	= list.size();
				paramVO.setChnlCd(StringUtil.nullToSpace(list.get(0)));
			}
			
			try{
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID664, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {}
	}
	
}
