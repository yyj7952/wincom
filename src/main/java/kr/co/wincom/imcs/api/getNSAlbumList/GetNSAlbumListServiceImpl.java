package kr.co.wincom.imcs.api.getNSAlbumList;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.FileUtil;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.AlbumInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.common.vo.ComTrailerVO;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetNSAlbumListServiceImpl implements GetNSAlbumListService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSAlbumList");

	@Autowired
	private GetNSAlbumListDao getNSAlbumListDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSAlbumList(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	/**
	 * @author HONG
	 * @since 2016-07-30
	 * @see 
	 */
	@Override
	public GetNSAlbumListResultVO getNSAlbumList(GetNSAlbumListRequestVO paramVO)	{
//		this.getNSAlbumList(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodName = stackTraceElement.getMethodName();
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		
		imcsLog.debugLog(methodName + "service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);

		GetNSAlbumListResponseVO tempVO			= new GetNSAlbumListResponseVO();
		GetNSAlbumListResultVO	returnListVO	= new GetNSAlbumListResultVO();
		List<GetNSAlbumListResponseVO> resultVO	= new ArrayList<GetNSAlbumListResponseVO>();	// 파일 쓰기용 VO
		List<GetNSAlbumListResponseVO> returnVO	= new ArrayList<GetNSAlbumListResponseVO>();	// 결과 리턴용 VO
		
		int nMainCnt		= 0;
		String szImgSvrIp	= "";		// 이미지 서버 IP
		String szImgSvrUrl	= "";		// 이미지 서버 URL
		String msg			= "";
		
		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2
		
		long tp5			= 0;		// timePoint 5
		long tp6			= 0;		// timePoint 6
		
		int nPageNo			= 0;
		int nPageCnt		= 0;
		int nPageIdx		= 0;
		int nStartNo		= 0;
		int nEndNo			= 0;
		int nWaitCnt		= 0;
		
		String LOCKFILE	= "";
		String RESFILE	= "";
		
		String LOCALPATH = "";
		
		LOCALPATH = commonService.getCachePath("LOCAL", ImcsConstants.API_PRO_ID949.split("/")[1], imcsLog);
		
		try {
			// 페이징 관련 파라미터 정리
			nPageNo		= Integer.parseInt(StringUtil.nullToZero(paramVO.getPageNo()));
			nPageCnt	= Integer.parseInt(StringUtil.nullToZero(paramVO.getPageCnt()));
			nPageIdx	= Integer.parseInt(StringUtil.nullToZero(paramVO.getPageIdx()));
			
			if (nPageNo != 0 && nPageCnt != 0) {
							
				nStartNo = (nPageNo * nPageCnt) - (nPageCnt - 1) - 1;
				nEndNo   = (nPageNo * nPageCnt);

				if (nPageIdx > 0) {
					nStartNo = nPageIdx - 1;
					nEndNo   = nStartNo + nPageCnt;
				}
			}
			
			tp1 = System.currentTimeMillis();
			
			LOCKFILE	= LOCALPATH + "/getNSAlbumList_g" + paramVO.getGenreGb() + "_n" + paramVO.getNscType() + "_o" + paramVO.getOrderGb() + "_q" + paramVO.getQuickDisYn() + ".lock";
			RESFILE		= LOCALPATH + "/getNSAlbumList_g" + paramVO.getGenreGb() + "_n" + paramVO.getNscType() + "_o" + paramVO.getOrderGb() + "_q" + paramVO.getQuickDisYn() + ".res";
			
			tp5 = System.currentTimeMillis();
			
			File res	= new File(RESFILE);

			tp6 = System.currentTimeMillis();
			imcsLog.timeLog("파일객체 생성", String.valueOf(tp6 - tp5), methodName, "");
			
			// 장르가 T, V 일경우에만 FILE READ
			if(paramVO.getGenreGb().equals("T") || paramVO.getGenreGb().equals("V")) {
				
				tp5 = System.currentTimeMillis();
				
				if(res.exists()) {
					String result = FileUtil.fileRead(RESFILE, "UTF-8");					
					
					if(nPageNo != 0 && nPageCnt != 0) {				// 페이징 처리
						String[] arrResult	= result.split(ImcsConstants.ROWSEP);
						result = "";
						
						for(int i = 0; i < arrResult.length; i++) {
							if(i >= nStartNo && i < nEndNo)
								result	= result + arrResult[i] + ImcsConstants.ROWSEP;
						}
					}
					
					if(!"".equals(StringUtil.nullToSpace(result))) {
						returnListVO.setResult(result);
						//FileUtil.unlock(LOCKFILE, imcsLog);
						
						msg = " File [" + RESFILE + "] rcvbuf... [" + res.length() + "] bytes ";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						returnListVO.setResultCode(paramVO.getResultCode());
						
						return returnListVO;
					}
				} else {
					msg = " File [" + RESFILE + "] open Failed";
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					while(FileUtil.lock(LOCKFILE, imcsLog)){
						Thread.sleep(1000);
						nWaitCnt++;
						
						msg = " queryWaitCheck Sleep [" + nWaitCnt + "] sec";
						imcsLog.serviceLog(msg, methodName, methodLine);
		
						if(nWaitCnt >= 5){
							msg = " wait_count overload Failed svc2[" + ImcsConstants.API_PRO_ID949 + "] sts[    0] msg["+ 
									String.format("%-17s", "par_yn:" + ImcsConstants.RCV_MSG2 +"]");
							imcsLog.serviceLog(msg, methodName, methodLine);
							
							paramVO.setResultCode("21000000");
							
							throw new ImcsException();
						}
						
						if(res.exists()) {
							String result = FileUtil.fileRead(RESFILE, "UTF-8");
							
							if(nPageNo != 0 && nPageCnt != 0) {				// 페이징 처리
								String[] arrResult	= result.split(ImcsConstants.ROWSEP);
								result = "";
								
								for(int i = 0; i < arrResult.length; i++) {
									if(i >= nStartNo && i < nEndNo)
										result	= result + ImcsConstants.ROWSEP;
								}
							}
							
							if(!"".equals(StringUtil.nullToSpace(result))) {
								returnListVO.setResult(result);
								FileUtil.unlock(LOCKFILE, imcsLog);
								
								returnListVO.setResultCode(paramVO.getResultCode());
								
								return returnListVO;
							}
						}
					}
				}
				
				tp6 = System.currentTimeMillis();
				imcsLog.timeLog("파일 읽기", String.valueOf(tp6 - tp5), methodName, "");
			}
			
			
			// viewFlag 정보조회
			this.getViewFlag2(paramVO);
			
			// 이미지 서버 IP조회
			try {
				
				tp5 = System.currentTimeMillis();
				
				szImgSvrIp	= commonService.getIpInfo("img_server", ImcsConstants.API_PRO_ID949.split("/")[1]);			// 이미지서버 IP 조회
				szImgSvrUrl	= commonService.getIpInfo("img_resize_server", ImcsConstants.API_PRO_ID949.split("/")[1]);		// 이미지서버 URL 조회
				
				tp6 = System.currentTimeMillis();
				imcsLog.timeLog("이미지서버 정보 조회", String.valueOf(tp6 - tp5), methodName, "");
				
				tempVO.setImgUrl(szImgSvrUrl);
			} catch(Exception e) {								// 이미지 서버 IP 조회 실패 시 에러
				paramVO.setResultCode("31000000");
//				imcsLog.failLog(ImcsConstants.API_PRO_ID949, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
				
				throw new ImcsException();
			}
			
			tp2 = System.currentTimeMillis();
			imcsLog.timeLog("서버IP값 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			
	
			// 서브카테고리정보 조회
			resultVO = this.getSubCateInfo(paramVO);
			tp1 = System.currentTimeMillis();
			imcsLog.timeLog("서브카테고리정보 조회", String.valueOf(tp1 - tp2), methodName, methodLine);
		
			
			if(resultVO != null)
				nMainCnt	= resultVO.size();
			
			
			for(int i = 0; i < nMainCnt; i++) {
				tempVO	= resultVO.get(i);
				
				/**************************버그인지 아닌지 확인 필요********************************/				
				// Parameter에서 지정한 범위의 컨텐츠만 FETCH 한다
				if ("N".equals(paramVO.getGenreGb()) && i < nStartNo) 
					continue;
				
				// Parameter에서 지정한 범위의 컨텐츠만 FETCH 한다
				if ("N".equals(paramVO.getGenreGb()) && i >= nEndNo ) 
					break;
				
				paramVO.setCatType(tempVO.getCatType());
				paramVO.setContsId(tempVO.getContsId());
				paramVO.setCatId(tempVO.getCatId());
				AlbumInfoVO albumListVO	= new AlbumInfoVO();
				albumListVO = this.getAlbumListInfo(paramVO);
				
				if(albumListVO != null) {
					tempVO.setContsName(albumListVO.getContsName());
					tempVO.setReleaseDate(albumListVO.getReleaseDate());
					tempVO.setSeriesDesc(albumListVO.getSeriesDesc());
					tempVO.setRealHd(albumListVO.getRealHd());
					tempVO.setPoint(albumListVO.getPoint());
					tempVO.setServiceGb(albumListVO.getServiceIcon());
					tempVO.setSuggestedPrice(albumListVO.getSuggestedPrice());
					tempVO.setPrInfo(albumListVO.getPrInfo());
					tempVO.setRuntime(albumListVO.getRunTime());
					//tempVO.setEventValue(albumListVO.getEventValue());
					tempVO.setOverseerName(albumListVO.getOverseerName());
					tempVO.setActor(albumListVO.getActor());
					tempVO.setFilterGb(albumListVO.getFilterGb());
					tempVO.setGenreGb(albumListVO.getGenreGb());
					tempVO.setIs51Ch(albumListVO.getIs51ch());
					tempVO.setIsCaption(albumListVO.getCaptionYn());
					tempVO.setOverseerName(albumListVO.getOverseerName());
					tempVO.setHdContent(albumListVO.getHdContent());
					tempVO.setIs3d(albumListVO.getIs3D());
					tempVO.setTerrCh(albumListVO.getTerrCh());
					tempVO.setImgFileName(albumListVO.getImgFileName());
					tempVO.setProductType(albumListVO.getProdType());
				}
				
				// 가격정보 설정
				if(!tempVO.getProductType().equals("0") && Integer.parseInt(StringUtil.nullToZero(tempVO.getSuggestedPrice())) > 0)
					tempVO.setSuggestedPrice("Y");
				else 
					tempVO.setSuggestedPrice("N");
				
				// 이미지URL설정
				tempVO.setImgUrl(szImgSvrIp);
				tempVO.setImgSvcUrl(szImgSvrUrl);
				
				// 51채널여부 설정
				if(tempVO.getIs51Ch().equals("DOLBY 5.1"))	tempVO.setIs51Ch("Y");
				else										tempVO.setIs51Ch("N");
				
				// HD여부 설정
				if(tempVO.getHdContent().equals("Y") || tempVO.getHdContent().equals("S"))
					tempVO.setIsHd("Y");
				else if(tempVO.getHdContent().equals("N"))
					tempVO.setIsHd("N");
				
				// Sub Count설정??? strcpy((char*)lst_SubList.c_sub_cnt.arr, "0"); < 원소스
				tempVO.setSubCnt("0");
				
				// Content정보 조회
				if(tempVO.getSampleYn().equals("Y")) {
					ComTrailerVO AlbumContentVO = new ComTrailerVO();
					
					AlbumContentVO = this.getSampleInfo(paramVO);
					
					if(AlbumContentVO != null) {
						tempVO.setVodServer1(AlbumContentVO.getTrailerUrl1());
						tempVO.setVodServer2(AlbumContentVO.getTrailerUrl2());
						tempVO.setVodServer3(AlbumContentVO.getTrailerUrl3());
						tempVO.setVodFileName1(AlbumContentVO.getTrailerFileName1());
						tempVO.setVodFileName2(AlbumContentVO.getTrailerFileName1());
						tempVO.setVodFileName3(AlbumContentVO.getTrailerFileName1());
					}
				}
				
				
				String szStillImageName	= "";
				szStillImageName = this.getStillFileName(paramVO);
				tempVO.setThumbnailFileName(szStillImageName);
				
				
				
				resultVO.set(i, tempVO);
				
				/*if(nPageNo != 0 && nPageCnt != 0) {
					if(i >= nStartNo && i <= nEndNo)
						returnVO.add(tempVO);
				}*/
				
				returnVO.add(tempVO);				
				
				
			}
			
			tp2 = System.currentTimeMillis();
			imcsLog.timeLog("서브카테고리정보 Fetch", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			
			
			
			// 파일 쓰기
			if(paramVO.getGenreGb().equals("T") || paramVO.getGenreGb().equals("V")) {
				
				tp5 = System.currentTimeMillis();
				
				GetNSAlbumListResultVO resultListVO = new GetNSAlbumListResultVO();
				resultListVO.setList(resultVO);
				int nRetVal = FileUtil.fileWrite(RESFILE, resultListVO.toString(), false);
				
				if(nRetVal == 1) {
					msg = " File [" + RESFILE + "] WRITE [" + res.length() + "] bytes Finished";
					imcsLog.serviceLog(msg, methodName, methodLine);
				} else {
					msg = " File [" + RESFILE + "] WRITE failed";
					imcsLog.serviceLog(msg, methodName, methodLine);		
				}
				
				FileUtil.unlock(LOCKFILE, imcsLog);
				
				
				if(res.exists()) {
					String result = FileUtil.fileRead(RESFILE, "UTF-8");					
					
					if(nPageNo != 0 && nPageCnt != 0) {				// 페이징 처리
						String[] arrResult	= result.split(ImcsConstants.ROWSEP);
						result = "";
						
						for(int i = 0; i < arrResult.length; i++) {
							if(i >= nStartNo && i < nEndNo)
								result	= result + arrResult[i] + ImcsConstants.ROWSEP;
						}
					}
					
					if(!"".equals(StringUtil.nullToSpace(result))) {
						returnListVO.setResult(result);
						//FileUtil.unlock(LOCKFILE, imcsLog);
						
						msg = " File [" + RESFILE + "] rcvbuf... [" + res.length() + "] bytes ";
						imcsLog.serviceLog(msg, methodName, methodLine);
						
						returnListVO.setResultCode(paramVO.getResultCode());
						
						return returnListVO;
					}					
					
				} else {
					msg = " File [" + RESFILE + "] open Failed";
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
				
				tp6 = System.currentTimeMillis();
				imcsLog.timeLog("파일 쓰기", String.valueOf(tp6 - tp5), methodName, "");
				
			}else{
				
				if(nStartNo == 0 && nEndNo == 0 ){
					returnListVO.setList(resultVO);
				}else{
					returnListVO.setList(returnVO);
				}
				
			}
			
		} catch(ImcsException ie) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
			throw ie;
		} catch(Exception e) {
			isLastProcess	= ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException();
		} finally{
			returnListVO.setResultCode(paramVO.getResultCode());
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID949) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return returnListVO;
	}

	
	

	/**
	 * 이미지파일명 조회 
	 * @param paramVO
	 * @return String 	이미지파일명
	 */
	public String getStillFileName(GetNSAlbumListRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId = "lgvod949_004_20171214_001";
		String szImageFileName	= "";
		
		List<StillImageVO> list = new ArrayList<StillImageVO>();
		
		try {
			long tp1 = System.currentTimeMillis();
			try {
				list = getNSAlbumListDao.getStillFileName(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			long tp2 = System.currentTimeMillis();
	        imcsLog.timeLog("이미지파일명 정보 조회", String.valueOf(tp2 - tp1), methodName, "");

			if (list != null && !list.isEmpty()) {
				StillImageVO resultVO	= new StillImageVO();
				resultVO	= list.get(0);
				
				if(resultVO != null) {
					szImageFileName = resultVO.getImgFileName();
				}
			}
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID949, sqlId, cache, "still_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			throw new ImcsException();
		}
		
		return szImageFileName;
	}

	


	/**
	 * 샘플컨텐츠 정보 조회
	 * @param	String	컨텐츠ID
	 * @return	GetNSAlbumInfoVO	앨범정보
	 */
	public ComTrailerVO getSampleInfo(GetNSAlbumListRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId = "lgvod949_003_20171214_001";
		
		List<ComTrailerVO> list	= new ArrayList<ComTrailerVO>();
		ComTrailerVO resultVO	= new ComTrailerVO();
		
		try {
			long tp1 = System.currentTimeMillis();
			try {
				list = getNSAlbumListDao.getSampleInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			long tp2 = System.currentTimeMillis();
	        imcsLog.timeLog("샘플컨텐츠 정보 조회", String.valueOf(tp2 - tp1), methodName, "");

			if (list != null && !list.isEmpty()) {
				resultVO	= list.get(0);
			}
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID949, sqlId, cache, "triler_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			throw new ImcsException();
		}

		return resultVO;
	}
	
	

	/**
	 * 앨범 조회
	 * @param	paramVO
	 * @return	GetNSAlbumListResultVO		앨범정보
	 */
	public AlbumInfoVO getAlbumListInfo(GetNSAlbumListRequestVO paramVO) throws Exception{
		String sqlId = "lgvod949_s01_20171214_001";
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		if(paramVO.getGenreGb().equals("N"))	sqlId = "lgvod949_s01_20171214_001";
		else									sqlId = "lgvod949_s02_20171214_001";
		
		List<AlbumInfoVO> list	= new ArrayList<AlbumInfoVO>();
		AlbumInfoVO resultVO	= new AlbumInfoVO();

		try {
			long tp1 = System.currentTimeMillis();
			try {
				list = getNSAlbumListDao.getAlbumInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			long tp2 = System.currentTimeMillis();
	        imcsLog.timeLog("앨범 정보 조회", String.valueOf(tp2 - tp1), "getAlbumListInfo", "");

			if (list != null && !list.isEmpty()) {
				resultVO	= list.get(0);
			}
			
		} catch (Exception e) {
			throw new ImcsException();
		}

		return resultVO;
	}

	
	/**
	 * 서브카테고리정보 조회
	 * @param	paramVO
	 * @return	List<GetNSAlbumListResultVO>		// 알람받기 리스트
	 */
	public List<GetNSAlbumListResponseVO> getSubCateInfo(GetNSAlbumListRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId = "lgvod949_m13_20171214_001";
	
//		if(paramVO.getGenreGb().equals("N")) {
//			if(paramVO.getOrderGb().equals("N"))		sqlId = "lgvod949_m01_20171214_001";		// 최신순(방영일) 기준 정렬
//			else if(paramVO.getOrderGb().equals("H"))	sqlId = "lgvod949_m02_20171214_001";		// 인기순(평점순) 기준 정렬
//			else if(paramVO.getOrderGb().equals("D"))	sqlId = "lgvod949_m03_20171214_001";		// 추천순(편성순) 기준 정렬
//			else if(paramVO.getOrderGb().equals("A"))	sqlId = "lgvod949_m04_20171214_001";		// 가나다순 기준 정렬
//			else if(paramVO.getOrderGb().equals("S"))	sqlId = "lgvod949_m05_20171214_001";		// 인기순(매출순) 기준 정렬
//			else if(paramVO.getOrderGb().equals("B"))	sqlId = "lgvod949_m05_20171214_001";		// 구매순 기준 정렬
//			else if(paramVO.getOrderGb().equals("P"))	sqlId = "lgvod949_m07_20171214_001";		// 별점순(구+신) 기준 정렬
//			else if(paramVO.getOrderGb().equals("W"))	sqlId = "lgvod949_m08_20171214_001";		// 별점순(신) 기준 정렬
//		} else {
//			if(paramVO.getOrderGb().equals("N"))		sqlId = "lgvod949_m11_20171214_001";		// 최신순(방영일) 기준 정렬
//			else if(paramVO.getOrderGb().equals("A"))	sqlId = "lgvod949_m12_20171214_001";		// 가나다순 기준 정렬
//			else if(paramVO.getOrderGb().equals("P"))	sqlId = "lgvod949_m17_20171214_001";		// 별점순(구+신) 기준 정렬
//			else if(paramVO.getOrderGb().equals("W"))	sqlId = "lgvod949_m18_20171214_001";		// 별점순(신) 기준 정렬
//			else 										sqlId = "lgvod949_m13_20171214_001";		// 인기순(매출순) 기준 정렬
//			
//		}
		
		int querySize = 0;
		
		List<GetNSAlbumListResponseVO> list = new ArrayList<GetNSAlbumListResponseVO>();

		try {
			long tp1 = System.currentTimeMillis();
			try {
				if(paramVO.getGenreGb().equals("N")) {
					if(!paramVO.getOrderGb().equals("S") && !paramVO.getOrderGb().equals("B"))
						list = getNSAlbumListDao.getSubCateInfoN1(paramVO);
					else	
						list = getNSAlbumListDao.getSubCateInfoN2(paramVO);
				} else {
					list = getNSAlbumListDao.getSubCateInfoE(paramVO);
				}
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			long tp2 = System.currentTimeMillis();
	        imcsLog.timeLog("서브카테고리 정보 조회", String.valueOf(tp2 - tp1), methodName, "");

			if (list != null && !list.isEmpty()) {
				querySize = list.size();
			}
			
			try{
//				imcsLog.dbLog(ImcsConstants.API_PRO_ID949, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
						
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.API_PRO_ID949, sqlId, cache, "sub_list:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			throw new ImcsException();
		}
					
		return list;
	}




	/**
	 * ViewFlag정보 조회
	 * @param	paramVO
	 * @return	void
	 */
	public void getViewFlag2(GetNSAlbumListRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String sqlId = "lgvod949_010_20171214_001";
		
		List<String> list = new ArrayList<String>();
		String viewFlag2	= "";
		
		try {
			long tp1 = System.currentTimeMillis();
			try {
				list = getNSAlbumListDao.getViewFlag2(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			long tp2 = System.currentTimeMillis();
	        imcsLog.timeLog("ViewFlag 정보 조회", String.valueOf(tp2 - tp1), methodName, "");

			if (list != null && !list.isEmpty()) {
				viewFlag2	= list.get(0);
			}
			
			paramVO.setViewFlag2(viewFlag2);
		} catch (Exception e) {
			paramVO.setViewFlag2("");
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		}
	}
}
