package kr.co.wincom.imcs.api.getFXFavorList;

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

import kr.co.wincom.imcs.common.NosqlCacheType;

@Service
public class GetFXFavorListServiceImpl implements GetFXFavorListService {
	private Log imcsLogger		= LogFactory.getLog("API_getFXFavorList");
	
	@Autowired
	private GetFXFavorListDao getFXFavorListDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getFXFavorList(String szSaId, String szStbMac, String szPid){
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
	public GetFXFavorListResultVO getFXFavorList(GetFXFavorListRequestVO paramVO)	{
//		this.getFXFavorList(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");

		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		List<GetFXFavorListResponseVO> resultVO		= new ArrayList<GetFXFavorListResponseVO>();
		List<GetFXFavorListResponseVO> returnVO		= new ArrayList<GetFXFavorListResponseVO>();
		GetFXFavorListResponseVO tempVO				= new GetFXFavorListResponseVO();
		GetFXFavorListResultVO	resultListVO		= new GetFXFavorListResultVO();
		
		int nMainCnt	= 0;
		int nSetCnt		= 0;
		int nTotCnt		= 0;
		
		int nStartNo	= 0;
		int nEndNo		= 0;
		int nPageNo		= 0;
		int nPageCnt	= 0;
		
		long tp1		= 0;		// timePoint 1
		long tp2		= 0;		// timePoint 2
		
		String szImgSvrUrl	= "";		// 이미지 서버 URL
		String msg			= "";
		
		try {
			if(paramVO.getPageNo().equals("0")){ 
				paramVO.setPageNo("A");
			}
			
			if(paramVO.getPageCnt().equals("0")){
				paramVO.setPageCnt("A");
			}
				
			
			if(paramVO.getPageNo().equals("") || paramVO.getPageNo()== null){
				paramVO.setPageNo("1");
			}				
		
			if(paramVO.getPageCnt().equals("") || paramVO.getPageCnt()== null){
				paramVO.setPageCnt("1");
			}

			
			if("A".equals(paramVO.getPageNo()) || "A".equals(paramVO.getPageCnt())) {
				nStartNo	= 1;
				nEndNo		= 1000;
			} else {
				nPageNo		= Integer.parseInt(StringUtil.nullToZero(paramVO.getPageNo()));
				nPageCnt	= Integer.parseInt(StringUtil.nullToZero(paramVO.getPageCnt()));
				
				nStartNo	= nPageNo * nPageCnt - nPageCnt;				
				nEndNo		= nPageNo * nPageCnt;
			}
			
			tp1 = System.currentTimeMillis();
			
			try {
				szImgSvrUrl	= commonService.getIpInfo("img_server", ImcsConstants.FXAPI_PRO_ID060.split("/")[1]);		// 이미지서버 URL 조회
			} catch(Exception e) {
//				imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID060, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
				throw new ImcsException();
			}

			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			
			
			resultVO	= this.getFavorList(paramVO);
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("찜목록 조회", String.valueOf(tp1 - tp2), methodName, methodLine);
			
			if(resultVO != null)	nMainCnt	= resultVO.size();
			
			for(int i = 0; i < nMainCnt; i++) {
				tempVO	= resultVO.get(i);
												
				// 카테고리 정보 조회
				paramVO.setContsId(tempVO.getContsId());
				paramVO.setAlbumId(tempVO.getAlbumId());
				
				if("X".equals(tempVO.getAlbumId())) 	continue;
				
				CateInfoVO cateInfoVO = new CateInfoVO();
				cateInfoVO = this.getCateInfo(paramVO);
				
				int nSubCnt	= 0;
				
				if(cateInfoVO != null) {
					nSubCnt	= Integer.parseInt(StringUtil.nullToZero(cateInfoVO.getCount()));
					tempVO.setParentCatId(cateInfoVO.getCategoryId());
					tempVO.setIsUpdate(cateInfoVO.getIsUpdate());
					tempVO.setCatLevel(cateInfoVO.getContsLevel());
					tempVO.setChaNum(cateInfoVO.getChaNum());
					tempVO.setAuthYn(cateInfoVO.getAuthYn());
					tempVO.setBelongingName(cateInfoVO.getBelongingName());
					
					String szSerInfo	 = cateInfoVO.getCateInfo();
					tempVO.setSerCatId(szSerInfo.substring(0, szSerInfo.indexOf("|")));
					tempVO.setSeriesNo(szSerInfo.substring(szSerInfo.indexOf("|") + 1, szSerInfo.length()));
				}
				
				if(cateInfoVO == null || nSubCnt == 0)	continue;
				
				
				try {
					if("X".equals(tempVO.getParentCatId().substring(0,1)))	paramVO.setCatId1("X");
					else									paramVO.setCatId1("A");
				} catch (Exception e) {
					paramVO.setCatId1("A");
				}
				
				
				// 앨범정보 조회
				String szGenreGb	= "";
				
				//AlbumInfoVO albumInfoVO = new AlbumInfoVO();
				AlbumInfoVO albumInfoVO = null;
				albumInfoVO	= this.getAlbumInfo(paramVO);
				
				
				if(albumInfoVO == null)	continue;
				else {
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
					tempVO.setFilterGb(albumInfoVO.getFilterGb());
					tempVO.setIsNew(albumInfoVO.getIsNew());
					tempVO.setIs51Ch(albumInfoVO.getIs51ch());
					tempVO.setIsHd(albumInfoVO.getIsHd());
					tempVO.setIs3D(albumInfoVO.getIs3D());
					tempVO.setIsCaption(albumInfoVO.getCaptionYn());
					tempVO.setServiceGb(albumInfoVO.getServiceIcon());
					tempVO.setImgFileName(albumInfoVO.getImgFileName());
					tempVO.setTerrCh(albumInfoVO.getTerrCh());
					
					tempVO.setIsHot("");
					tempVO.setSubCnt("");
					
					szGenreGb	= albumInfoVO.getGenreGb();
				}
				
				nTotCnt++;
				
				if(!"A".equals(paramVO.getPageNo()) && !"A".equals(paramVO.getPageCnt())) {
					if ( nTotCnt <= nStartNo || nTotCnt > nEndNo)	continue;
				}
				
				
				// 상품정보 조회
				String szProdType	= "";
				szProdType	= this.getProdType(paramVO);
				if("X".equals(szProdType))	szProdType	= "";
				
				
				// FVOD가 아니고 가격이 0원이 아니면 유료 그 외 무료
				int tempPrice	= Integer.parseInt(StringUtil.nullToZero(tempVO.getPrice()));
				if(!"0".equals(szProdType) && tempPrice > 0)	tempVO.setPrice("Y");
				else											tempVO.setPrice("N");
				
				
				// 5.1채널 여부
				if("DOLBY 5.1".equals(tempVO.getIs51Ch()))
						tempVO.setIs51Ch("Y");
				else	tempVO.setIs51Ch("N");
				
				
				// HD여부
				if("Y".equals(tempVO.getIsHd()) || "S".equals(tempVO.getIsHd()))
					tempVO.setIsHd("Y");
				else if("N".equals(tempVO.getIsHd()))
					tempVO.setIsHd("N");
				
				// i_sub_cnt를 가져오는 로직 미존재 함
				tempVO.setSubCnt("0");
				
				
				// 썸네일 파일명 조회
				String szStillFileName	= "";
				if("T".equals(szGenreGb))
					szStillFileName	= this.getImageFileName(paramVO);
				else 
					szStillFileName	= this.getStillFileName(paramVO);
				
				tempVO.setThumbnailFileName(StringUtil.nullToSpace(szStillFileName));
				tempVO.setImgUrl(szImgSvrUrl);
				
				//nSetCnt++;
				returnVO.add(tempVO);
			}
						
			tp2 = System.currentTimeMillis();
			imcsLog.timeLog("찜목록 정보 Fetch", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			resultListVO.setTotalCnt(String.valueOf(nTotCnt));
			resultListVO.setList(returnVO);
		} catch(ImcsException ie) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw ie;
		} catch(Exception e) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		} finally{
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID060) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}

	
	
	/**
	 * 스틸이미지 파일명 조회
	 * @param 	GetFXFavorListRequestVO paramVO
	 * @return  String
	 **/
	public String getStillFileName(GetFXFavorListRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod060_005_20171214_001";
		
		List<String> list = new ArrayList<String>();
		String szImgFileName	= "";
		int querySize = 0;
		
		try {
			try {
				list = getFXFavorListDao.getStillFileName(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list != null && !list.isEmpty()) {
				szImgFileName = StringUtil.nullToSpace(list.get(0));
			} else {
				querySize = list.size();
			}
			
			// C에서 주석 처리된 로그
			try{
//				imcsLog.dbLog2(ImcsConstants.FXAPI_PRO_ID060, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return szImgFileName;
	}



	/**
	 * 이미지 파일명 조회
	 * @param 	GetFXFavorListRequestVO paramVO
	 * @return  String
	 **/
	public String getImageFileName(GetFXFavorListRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod060_004_20171214_001";
		
		List<StillImageVO> list = new ArrayList<StillImageVO>();
		StillImageVO resultVO = null;
		int querySize = 0;
		
		String szImgFileName	= "";
		
		try {
			try {
				list = getFXFavorListDao.getImageFileName(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list != null && !list.isEmpty()) {
				resultVO		= list.get(0);
				szImgFileName	= resultVO.getImgFileName();
			} else {
				querySize = list.size();
			}
			
			// C에서 주석 처리된 로그
			try{
//				imcsLog.dbLog2(ImcsConstants.FXAPI_PRO_ID060, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID060, sqlId, cache, "svr_ip:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return szImgFileName;
	}
	


	/**
	 * 상품타입 정보 조회
	 * @param 	GetFXFavorListRequestVO paramVO
	 * @return  String
	 **/
	public String getProdType(GetFXFavorListRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod060_p01_20171214_001";
		String szProductType = "";
		
		if(paramVO.getAlbumId().equals(paramVO.getContsId()))
					sqlId = "fxvod060_p01_20171214_001";
		else		sqlId = "fxvod060_p02_20171214_001";
		
		List<String> list = new ArrayList<String>();
		int querySize = 0;
		
		try {
			String szContsId	= paramVO.getAlbumId();
			if(!"NSC".equals(paramVO.getCatGb()))
				szContsId	= szContsId	 + "_IPTV";
			
			if(!paramVO.getAlbumId().equals(paramVO.getContsId()))
				szContsId	= paramVO.getContsId();

			try {
				if(paramVO.getAlbumId().equals(paramVO.getContsId()))
					list = getFXFavorListDao.getProdType1(paramVO);
				else
					list = getFXFavorListDao.getProdType2(paramVO);
				
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list != null && !list.isEmpty()) {
				szProductType	= StringUtil.nullToSpace(list.get(0));
			} else {
				querySize = list.size();
			}
			
			// C에서 주석 처리된 로그
			try{
//				imcsLog.dbLog2(ImcsConstants.FXAPI_PRO_ID060, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
		
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return szProductType;
	}

	
	
	



	/**
	 * 앨범 정보 조회
	 * @param 	GetFXFavorListRequestVO paramVO
	 * @return  List<GetNSPurchasedResultVO>
	 **/
	public AlbumInfoVO getAlbumInfo(GetFXFavorListRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
	
		String sqlId = "fxvod060_s01_20171214_001";
		
		if("M".equals(paramVO.getFxType()) || "H".equals(paramVO.getFxType()))	
				sqlId = "fxvod060_s01_20171214_001";
		else	sqlId = "fxvod060_s02_20171214_001";
		
		List<AlbumInfoVO> list	= new ArrayList<AlbumInfoVO>();
		//AlbumInfoVO resultVO		= new AlbumInfoVO();
		AlbumInfoVO resultVO		= null;
		int querySize = 0;

		try {
			String szAlbumId = paramVO.getAlbumId();
			if(!"M".equals(paramVO.getFxType()) && !"H".equals(paramVO.getFxType()))	
				szAlbumId	= szAlbumId + "_IPTV";
			
			try {
				list = getFXFavorListDao.getAlbumInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list != null && !list.isEmpty()) {
				resultVO	= list.get(0);
			} else {
				querySize = list.size();
			}
			
			// C에서 주석 처리된 로그
			try{
//				imcsLog.dbLog2(ImcsConstants.FXAPI_PRO_ID060, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}

		return resultVO;
	}
	

	
	
	
	

	/**
	 * 카테고리 정보 조회
	 * @param 	GetFXFavorListRequestVO paramVO
	 * @return  GetNSCateInfoVO
	 **/
	public CateInfoVO getCateInfo(GetFXFavorListRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "fxvod060_002_20171214_001";
		
		List<CateInfoVO> list = new ArrayList<CateInfoVO>();
		CateInfoVO resultVO	= new CateInfoVO();
		int querySize = 0;

		try {
			String szContsId	= paramVO.getAlbumId();
			if(!"NSC".equals(paramVO.getCatGb()))	szContsId	= szContsId + "_IPTV";
			try {
				list = getFXFavorListDao.getCateInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list != null && !list.isEmpty()) {
				resultVO	= list.get(0);
			} else {
				querySize = list.size();
			}
			
			try{// C에서 주석 처리된 로그
//				imcsLog.dbLog2(ImcsConstants.FXAPI_PRO_ID060, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID060, sqlId, cache, "cate_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return resultVO;
	}


	
	


	/**
	 * 찜목록 정보 조회
	 * @param 	GetFXFavorListRequestVO paramVO
	 * @return  List<GetFXFavorListResultVO>
	 **/
	public List<GetFXFavorListResponseVO> getFavorList(GetFXFavorListRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "fxvod060_001_20171214_001";
		int querySize	= 0;
		
		List<GetFXFavorListResponseVO> list = new ArrayList<GetFXFavorListResponseVO>();

		try {
			try {
				list = getFXFavorListDao.getFavorList(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list == null || list.isEmpty()) {
//				imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID060, sqlId, cache, "favor_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			} else {	
				querySize = list.size();
			}
			
			try{// C에서 주석 처리된 로그
//				imcsLog.dbLog2(ImcsConstants.FXAPI_PRO_ID060, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID060, sqlId, cache, "favor_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
			throw new ImcsException();
		}

		return list;
	}
	
	
	
}
