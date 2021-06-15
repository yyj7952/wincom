package kr.co.wincom.imcs.api.getNSFavorList;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.AlbumInfoVO;
import kr.co.wincom.imcs.common.vo.CateInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetNSFavorListServiceImpl implements GetNSFavorListService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSFavorList");
	
	@Autowired
	private GetNSFavorListDao getNSFavorListDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSFavorList(String szSaId, String szStbMac, String szPid){
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
	public GetNSFavorListResultVO getNSFavorList(GetNSFavorListRequestVO paramVO)	{
//		this.getNSFavorList(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		List<GetNSFavorListResponseVO> resultVO		= new ArrayList<GetNSFavorListResponseVO>();
		GetNSFavorListResponseVO tempVO				= new GetNSFavorListResponseVO();
		GetNSFavorListResultVO	resultListVO		= new GetNSFavorListResultVO();
		
		int nMainCnt	= 0;
		
		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2
		
		String szImgSvrIp	= "";		// 이미지 서버 IP
		String szImgSvrUrl	= "";		// 이미지 서버 URL
		String msg			= "";
		
		AlbumInfoVO albumInfoVO		= null;
		List<StillImageVO> imageVO	= null;
		
		
		if(paramVO.getPageNo().equals("0") || paramVO.getPageNo().equals("") || paramVO.getPageNo() == null || paramVO.getPageNo().substring(0,1).equals("0")){
			paramVO.setPageNo("A");
		}
		
		if(paramVO.getPageCnt().equals("0") || paramVO.getPageCnt().equals("") || paramVO.getPageCnt() == null || paramVO.getPageCnt().substring(0,1).equals("0")){
			paramVO.setPageCnt("A");
		}
		
		try {
			// 서버IP정보 조회
			tp1 = System.currentTimeMillis();
			
			try {
				szImgSvrIp	= commonService.getIpInfo("img_server", ImcsConstants.API_PRO_ID339.split("/")[1]);			// 이미지서버 IP 조회
				szImgSvrUrl	= commonService.getIpInfo("img_resize_server", ImcsConstants.API_PRO_ID339.split("/")[1]);		// 이미지서버 URL 조회
			} catch(Exception e) {
//				imcsLog.failLog(ImcsConstants.API_PRO_ID339, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
				paramVO.setResultCode("31000000");

				throw new ImcsException();
			}
			this.getSysDate(paramVO);
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			
			
			// 채널코드정보 조회
			this.getChnlCode(paramVO);
			
			// 찜목록 조회
			resultVO	= this.getFavorInfoList(paramVO);
			
			tp1 = System.currentTimeMillis();
			imcsLog.timeLog("찜목록 조회" , String.valueOf(tp1 - tp2), methodName, methodLine);
			
			if(resultVO != null)			nMainCnt	= resultVO.size();
		    			
			for(int i = 0; i < nMainCnt; i++) {
				tempVO	= resultVO.get(i);
				
				// 통계용 데이터 설정
				if(i == nMainCnt - 1 && !tempVO.getContentsName().equals("")) {	
					resultListVO.setContentsId(tempVO.getContsId());
					resultListVO.setContentsName(tempVO.getContentsName());
				}
				
				// 카테고리정보 조회
				CateInfoVO cateInfoVO = new CateInfoVO();
				paramVO.setAdiAlbumId(tempVO.getAdiAlbumId());
				cateInfoVO = this.getCateInfo(paramVO);
				
				//int iCheckCount	= 0;
				
				if(cateInfoVO != null && !cateInfoVO.getCount().equals("")){
					String szSerInfo	= cateInfoVO.getCateInfo();
					
					tempVO.setParentCatId(cateInfoVO.getParentCatId());
					resultListVO.setCatId(cateInfoVO.getParentCatId());					
					tempVO.setIsUpdate(cateInfoVO.getIsUpdate());
					tempVO.setContsLevel(cateInfoVO.getContsLevel());
					tempVO.setChaNum(cateInfoVO.getChaNum());
					tempVO.setSerCatId(szSerInfo.substring(0, szSerInfo.indexOf("|")));
					tempVO.setSeriesNo(szSerInfo.substring(szSerInfo.indexOf("|") + 1, szSerInfo.length()));
					tempVO.setBelongingName(cateInfoVO.getBelongingName());
				} 
				
				// 앨범정보 조회
				
				
				paramVO.setCheckValue(tempVO.getCheckValue());
				paramVO.setContsId(tempVO.getContsId());
				
				
				// 부모카테고리의 제일 앞글자로만 비교
				String szTempParentCatId	= "";
				if(tempVO.getParentCatId() != null & tempVO.getParentCatId().length() > 1)
					szTempParentCatId	= tempVO.getParentCatId().substring(0, 1);
				
				
				if(!"X".equals(szTempParentCatId))	paramVO.setParentCatId("A");
				else								paramVO.setParentCatId("X");
				
								
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
					tempVO.setIsHd(albumInfoVO.getHdContent());
					tempVO.setIs3D(albumInfoVO.getIs3D());
					tempVO.setIsCaption(albumInfoVO.getCaptionYn());
					tempVO.setServiceIcon(albumInfoVO.getServiceIcon());
					tempVO.setImgFileName(albumInfoVO.getImgFileName());
					tempVO.setTerrCh(albumInfoVO.getTerrCh());
					tempVO.setVisitFlag(albumInfoVO.getVisitFlag());
					
					tempVO.setImgUrl("");
					tempVO.setIsHot("");
					tempVO.setSubCnt("0");
				} 
				
				
				// 상품타입정보 조회
				String szProductType	= this.getProductType(paramVO);
				
				if(szProductType == null || szProductType.equals("") || szProductType.equals("X"))
					szProductType	= "";
				
				
				tempVO.setImgUrl(szImgSvrIp);
				
				// SuggestedPrice (제안가)
				if(!szProductType.equals("0") && Integer.parseInt(StringUtil.nullToZero(tempVO.getPrice())) > 0)
					tempVO.setSuggestedPrice("Y");
				else
					tempVO.setSuggestedPrice("N");
				
				
				// 5.1ch 여부 
				if(tempVO.getIs51Ch().equals("DOLBY 5.1"))
					tempVO.setIs51Ch("Y");
				else
					tempVO.setIs51Ch("N");
				
				
				// HD 여부
				if(tempVO.getHdContent().equals("Y") || tempVO.getHdContent().equals("S"))
					tempVO.setIsHd("Y");
				else
					tempVO.setIsHd("N");
				
				
				// 이미지파일명 조회
				String szImageFileName	= "";
				
				
				if(tempVO.getGenreGb().equals("T")) {
					imageVO	= this.getImageFileName(paramVO);
					
					if(imageVO.size() > 0) {
						tempVO.setThumbnailFileUrl(StringUtil.nullToSpace(imageVO.get(0).getImgUrl()));
						tempVO.setThumbnailFileName(StringUtil.nullToSpace(imageVO.get(0).getImgFileName()));
					}
					
					//tempVO.setImgFileName(szImageFileName);
				} else {
					szImageFileName	= this.getStillFileName(paramVO);
					tempVO.setThumbnailFileName(szImageFileName);
				}
				
				tempVO.setThumbnailFileUrl(szImgSvrUrl);
				
				tempVO.setCatGb("\b\b");
				
				
				resultVO.set(i, tempVO);
			}
			
			tp2 = System.currentTimeMillis();
			imcsLog.timeLog("찜목록 정보 Fetch", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			resultListVO.setCatId(StringUtil.nullToSpace(tempVO.getParentCatId()));
			resultListVO.setList(resultVO);
		} catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw ie;
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID339) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}

	
	
	
	
	/**
	 * 스틸이미지 파일명 조회
	 * @param 	GetNSFavorListRequestVO paramVO
	 * @return  String
	 **/
	public String getStillFileName(GetNSFavorListRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "lgvod339_005_20171214_001";
		int querySize = 0;

		List<String> list = new ArrayList<String>();

		String szImageFileName	= "";
		
		try {
			try {
				list = getNSFavorListDao.getStillFileName(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list != null && !list.isEmpty()) {
				szImageFileName = StringUtil.nullToSpace(list.get(0));

				querySize = list.size();
			}
			
			//C에서 주석처리된 로그
			try{
//				imcsLog.dbLog2(ImcsConstants.API_PRO_ID339, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
		return szImageFileName;
	}



	/**
	 * 이미지 파일명 조회
	 * @param 	GetNSFavorListRequestVO paramVO
	 * @return  String
	 **/
	public List<StillImageVO> getImageFileName(GetNSFavorListRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "lgvod339_004_20171214_001";
		int querySize = 0;

		List<StillImageVO> list = new ArrayList<StillImageVO>();
		
		try {
			try {
				list = getNSFavorListDao.getImageFileName(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			//C에서 주석처리된 로그
			try{
				querySize = list.size();
//				imcsLog.dbLog2(ImcsConstants.API_PRO_ID339, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID339, sqlId, cache, ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return list;
	}



	/**
	 * 상품타입 정보 조회
	 * @param 	GetNSFavorListRequestVO paramVO
	 * @return  String
	 **/
	public String getProductType(GetNSFavorListRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "lgvod339_p01_20171214_001";
		
		if(paramVO.getCheckValue().equals("20"))		sqlId = "lgvod339_p01_20171214_001";
		else											sqlId = "lgvod339_p02_20171214_001";
		
		String szProductType	= "";
		int querySize			= 0;
		
		List<String> list = new ArrayList<String>();

		try {
			try {
				list = getNSFavorListDao.getProductType(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list != null && !list.isEmpty()) {
				szProductType	= StringUtil.nullToSpace(list.get(0));

				querySize = list.size();
			}
			
			//C에서 주석처리된 로그
			try{
//				imcsLog.dbLog2(ImcsConstants.API_PRO_ID339, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
		
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
		return szProductType;
	}



	/**
	 * 앨범 정보 조회
	 * @param 	GetNSFavorListRequestVO paramVO
	 * @return  List<GetNSPurchasedResultVO>
	 **/
	public AlbumInfoVO getAlbumInfo(GetNSFavorListRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
	
		String sqlId = "lgvod339_s01_20171214_001";
		int querySize = 0;
		
		if(paramVO.getCheckValue().equals("20"))		sqlId = "lgvod339_s01_20171214_001";
		else											sqlId = "lgvod339_s02_20171214_001";
		
		List<AlbumInfoVO> list	= new ArrayList<AlbumInfoVO>();
		AlbumInfoVO resultVO		= new AlbumInfoVO();

		try {
			try {
				if(paramVO.getCheckValue().equals("20"))
					list = getNSFavorListDao.getAlbumInfo1(paramVO);
				else
					list = getNSFavorListDao.getAlbumInfo2(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list != null && !list.isEmpty()) {
				resultVO	= list.get(0);

				querySize = list.size();
			}
			
			//C에서 주석처리된 로그
			try{
//				imcsLog.dbLog2(ImcsConstants.API_PRO_ID339, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}

		return resultVO;
	}
	
	

	/**
	 * 카테고리 정보 조회
	 * @param 	GetNSFavorListRequestVO paramVO
	 * @return  GetNSCateInfoVO
	 **/
	public CateInfoVO getCateInfo(GetNSFavorListRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "lgvod339_002_20171214_001";
		int querySize	= 0;
		
		List<CateInfoVO> list = new ArrayList<CateInfoVO>();
		CateInfoVO resultVO	= new CateInfoVO();

		try {
			try {
				list = getNSFavorListDao.getCateInfo(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list != null && !list.isEmpty()) {
				querySize	= list.size();
				resultVO	= list.get(0);
			}
			
			//C에서 주석처리된 로그
			try{
//				imcsLog.dbLog2(ImcsConstants.API_PRO_ID339, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID339, sqlId, cache, "cate_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return resultVO;
	}



	/**
	 * 찜목록 정보 조회
	 * @param 	GetNSFavorListRequestVO paramVO
	 * @return  List<GetNSFavorListResultVO>
	 **/
	public List<GetNSFavorListResponseVO> getFavorInfoList(GetNSFavorListRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "";
		
		if(paramVO.getChnlCd().equals("")) {
			if(paramVO.getPageNo().equals("A") || paramVO.getPageCnt().equals("A"))		sqlId = "lgvod339_m01_20171214_001";
			else																		sqlId = "lgvod339_m03_20171214_001";
		} else {
			if(paramVO.getPageNo().equals("A") || paramVO.getPageCnt().equals("A"))		sqlId = "lgvod339_m02_20171214_001";
			else																		sqlId = "lgvod339_m04_20171214_001";
		}
		
		int querySize	= 0;
		
		List<GetNSFavorListResponseVO> list = new ArrayList<GetNSFavorListResponseVO>();

		try {
			try {
				if(paramVO.getChnlCd().equals(""))
					list = getNSFavorListDao.getFavorInfoList1(paramVO);
				else
					list = getNSFavorListDao.getFavorInfoList2(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list == null || list.isEmpty()) {
//				imcsLog.failLog(ImcsConstants.API_PRO_ID339, "", cache, "favor_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				paramVO.setResultCode("21000000");
			} else {
				querySize = list.size();
			}
			
			try{
//				imcsLog.dbLog2(ImcsConstants.API_PRO_ID339, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){
				
			}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.API_PRO_ID339, sqlId, cache, "favor_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			throw new ImcsException();
		}

		return list;
	}
	

	
	/**
	 * 채널코드 정보 조회
	 * @param 	GetNSFavorListRequestVO paramVO
	 * @return  void
	 **/
	public void getChnlCode(GetNSFavorListRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId		= "lgvod339_001_20171214_001";
		int querySize		= 0;
		
		List<String> list = new ArrayList<String>();

		try {
			try {
				list = getNSFavorListDao.getChnlCode(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list != null && !list.isEmpty()) {
				querySize	= list.size();
				paramVO.setChnlCd(StringUtil.nullToSpace(list.get(0)));
			}
			
			try{
//				imcsLog.dbLog2(ImcsConstants.API_PRO_ID339, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {}
	}
	
	/**
	 * 날짜 정보 조회
	 * @param 	GetNSFavorListRequestVO paramVO
	 * @return  void
	 **/
	public void getSysDate(GetNSFavorListRequestVO paramVO){
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId		= "";
		int querySize		= 0;
		
		List<String> list = new ArrayList<String>();

		try {
			try {
				list = getNSFavorListDao.getSysDate(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list != null && !list.isEmpty()) {
				querySize	= list.size();
				paramVO.setCurrentDate(StringUtil.nullToSpace(list.get(0)));
			}
			
			try{
//				imcsLog.dbLog2(ImcsConstants.API_PRO_ID339, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {}
	}
	
	
}
