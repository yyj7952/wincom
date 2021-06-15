package kr.co.wincom.imcs.api.getNSSeriesList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.NosqlCacheType;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComDataFreeVO;
import kr.co.wincom.imcs.common.vo.ComSeriesInfoVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jets3t.service.security.AWSCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetNSSeriesListServiceImpl implements GetNSSeriesListService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSSeriesList");
	
	@Autowired
	private GetNSSeriesListDao getNSSeriesListDao;
	
	@Autowired
	private CommonService commonService;

//	public void getNSMusicList(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
	
//	private IMCSLog imcsLog = null;
	
	@SuppressWarnings("unused")
	@Override
	public GetNSSeriesListResultVO getNSSeriesList(GetNSSeriesListRequestVO paramVO){
//		this.getNSMusicList(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		
		// 2021.05.20 - 프로야구 모바일 웹 (배포속도 개선)
		// -------------------------------------------------------------------------------------------------------------------
		String AWS_chk_app = StringUtil.replaceNull(commonService.getServerProperties("AWS_chk_app"),"P");
		String AWS_chk_web = StringUtil.replaceNull(commonService.getServerProperties("AWS_chk_web"),"C");
		String AWS_app = "N";
		String AWS_web = "N";
		
		String[] AWS_app_arr = AWS_chk_app.split(";");
		for(String str_aws : AWS_app_arr)
		{
			if(paramVO.getAppType().length() == 4)
			{
				if(paramVO.getAppType().substring(0, 1).equals(str_aws))
				{
					AWS_app = "Y";
					break;
				}
			}
		}
		
		if(AWS_app.equals("Y"))
		{
			String[] AWS_web_arr = AWS_chk_web.split(";");
			for(String str_aws : AWS_web_arr)
			{
				if(paramVO.getAppType().length() == 4)
				{
					if(paramVO.getAppType().substring(2, 3).equals(str_aws))
					{
						AWS_web = "Y";
						break;
					}
				}
			}
		}
		// -------------------------------------------------------------------------------------------------------------------
		
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());		
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		List<GetNSSeriesListResponseVO> resultVO	= new ArrayList<GetNSSeriesListResponseVO>();
		GetNSSeriesListResultVO resultListVO	= new GetNSSeriesListResultVO();

		String msg	= "";
		
		int nMainCnt = 0;
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
			
			//가입자 정보 설정
			String c_idx_sa = paramVO.getSaId().substring(paramVO.getSaId().length()-2, paramVO.getSaId().length());
			
			int p_idx_sa = 0;
			
			try {
				p_idx_sa = Integer.parseInt(c_idx_sa) % 33;
			} catch (NumberFormatException e) {
				p_idx_sa = 0;
			}
			
			paramVO.setIdxSa(p_idx_sa);
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("검수 STB여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			//이미지 캐쉬 서버 정보 가져오기
			String img_poster_server = commonService.getImgReplaceUrl2("img_cachensc_server", "getNSSeriesList");
			String img_resize_server = commonService.getImgReplaceUrl2("img_still_server", "getNSSeriesList");
			//부가세 요율 정보 가져오기
			paramVO.setVatRate(this.getNSSeriesListDao.getVatRate());

			//가입자 정보 가져오기 => 검수 여부 (nsvod210_001_20180601)
			tp1 = System.currentTimeMillis();
			String szTestSbc	= "";
			szTestSbc	= this.getTestSbc(paramVO);
			if (szTestSbc == null || szTestSbc.equals("")) {
				paramVO.setTestSbc("N");
			}
			paramVO.setTestSbc(szTestSbc);
			
		    //노출여부 조회 조건 설정
		    //공연앱인 경우 비노출도 조회되도록 함...
		    if (paramVO.getRqsType().equals("M"))
		    	paramVO.setViewFlag1("N");
		    else
		    	paramVO.setViewFlag1("V");
		    	
		    if (paramVO.getTestSbc().equals("Y"))
		    	paramVO.setViewFlag2("T");
		    else
		    	paramVO.setViewFlag2("V");
		    
		    // ------------------------------------------------------------------------//
		    // SQL - 002 : 카테고리 정보 가져오기
		    //-------------------------------------------------------------------------//
		    // 시리즈 설정을 위한 시리즈 ID 가져오기
		    // 정렬 설정을 위한 종영 여부 가져오기
		    //-------------------------------------------------------------------------//
		    // 2021.02.24 - 모바일TV 기능개선 4차수 : 미편성 PPS의 경우 카테고리 정보를 조회하지 않는다. (PT_LA_GROUP_RELATION으로 정보 제공 필요)
		    HashMap<String, String> cateInfo = null;
		    if(paramVO.getRqsType().equals("V") && (paramVO.getCatId().startsWith("7") || paramVO.getCatId().startsWith("8")) && paramVO.getCatId().length() == 5)
		    {
		    	cateInfo = new HashMap<String, String>();
				cateInfo.put("SERIES_ID", "");
				cateInfo.put("CLOSE_YN", "Y");
				cateInfo.put("serno_disp_yn", "Y");
				cateInfo.put("pkg_yn", "N");
		    }
		    else
		    {
		    	cateInfo = this.getCateInfo(paramVO);
		    }
			
			if (cateInfo==null || cateInfo.isEmpty()) {
				//결과 헤더 구성
	            //iResult, 성공여부 코드값 (0:성공, 1:실패)
				String resultHeader  = String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|", 
						"1", "카테고리가 존재하지 않거나 접근 권한이 없습니다", "", 
						"", "", "",
						"", "", "",
						""); //currPage 구하다가 연산자 오버플로우 발생 발지
				resultListVO.setResultHeader(resultHeader);
				
				return resultListVO;
			}
			
			paramVO.setSeriesId(cateInfo.get("SERIES_ID"));
			if (paramVO.getSeriesId()==null) paramVO.setSeriesId("");
			paramVO.setCloseYn(cateInfo.get("CLOSE_YN"));
			if (paramVO.getCloseYn()==null) paramVO.setCloseYn("N");
			
			// 시리즈회차 표시 여부(Y/N)
			paramVO.setSerno_disp_yn(cateInfo.get("serno_disp_yn"));
			
			//IPTV UX 개편(2018)의 경우 단편패키지를 구분하기 위해 회차노출여부 항목을 사용함
			//패키지 카테고리이면서 시리즈 아이디가 존재하지 않을 경우 회차 노출 여부를 "N"으로 설정 (2018.11.14)
			//그외의 경우에는 DB 값 그대로 사용함...
			//(if ( strncmp((char*)headInfo.c_series_id.arr, "", 1) == 0 && strncmp((char*)headInfo.c_pkg_yn.arr, "Y", 1) == 0 )
			//strcpy((char*)headInfo.c_series_display.arr, "N");)
			if(StringUtils.isBlank(cateInfo.get("SERIES_ID")) && cateInfo.get("pkg_yn").equals("Y"))
				paramVO.setSerno_disp_yn("N");
			
			//----------------------------------------------
			// 페이지 설정 및 정렬 설정
			//----------------------------------------------
			paramVO = this.setCondition(paramVO, AWS_web);
			
		    // ------------------------------------------------------------------------//
		    // SQL - 010 : 컨텐츠 편성 정보 가져오기
		    //-------------------------------------------------------------------------//
			int currPage = 0;
			int totalCnt = 0;
			int iCount = 0;
			resultVO = this.getContScheduleList(paramVO, AWS_web);
			for (GetNSSeriesListResponseVO item : resultVO) {
				// 2021.02.24 - 모바일TV 기능개선 4차수 : 시리즈의 경우 미편성은 Z로 정상 편성은 V로 노출구분 정보를 제공 한다.
				if( (paramVO.getCatId().startsWith("7") || paramVO.getCatId().startsWith("8")) && paramVO.getCatId().length() == 5 )
				{
					item.setServiceFlag("Z");
				}
				else
				{
					item.setServiceFlag("V");
				}
				
				item.setImgUrl(img_poster_server);
				item.setStillUrl(img_resize_server);
				
//				if (item.getVodType().equals("D")) 
//					item.getVodType().equals("Y");
//				else 
//					item.getVodType().equals("N");
				
				iCount ++;
				if (iCount == 1) {
					int rowNo = Integer.parseInt(item.getRowNo());
					int pageCnt = Integer.parseInt(paramVO.getPageCnt());
					if (pageCnt != 0)
						currPage = (int) Math.ceil(rowNo / (double)pageCnt );
					else
						currPage =  1;
//					System.out.println("#######################:"+ rowNo + "|" + pageCnt + "|" + currPage);
					totalCnt = Integer.parseInt(item.getTotalCount());
				}
				
				if ( !paramVO.getRqsNo().equals("0") ) {   
					if ( paramVO.getRqsNo().equals(item.getSeriesNo()))
						paramVO.setFocusId(item.getAlbumId());
				}
				else
				{
					if ( iCount == 1 ) 
						paramVO.setFocusId(item.getAlbumId());
				}
			}
			
			if(resultVO != null){
				nMainCnt = resultVO.size();
			}else{
				paramVO.setResultCode("21000000");
				imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID210, "", null, "chnl_list:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			}
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("컨텐츠 편성 정보 가져오기", String.valueOf(tp2 - tp1), methodName, methodLine);
			
		    //-----------------------------------------------------------
		    // 시즌 시리즈 정보 가져오기 (공연 제외)
		    //-----------------------------------------------------------
		    if ( !paramVO.getRqsType().equals("M") ) 
		    {
		    	// 2021.02.24 - 모바일TV 기능개선 4차수 : 미편성 PPS의 경우 시즌 정보를 조회하지 않는다.
		    	if( !((paramVO.getCatId().startsWith("7") || paramVO.getCatId().startsWith("8")) && paramVO.getCatId().length() == 5) )
		    	{
		    		this.getSeriesInfo(paramVO);
		    	}
		    }
			
			//결과 헤더 구성
            //iResult, 성공여부 코드값 (0:성공, 1:실패)
			String resultHeader  = String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|", 
					iResult, "", totalCnt, 
					paramVO.getSeriesId(), paramVO.getSerCatId(), paramVO.getSerCatNm(),
					paramVO.getOrderGb(), paramVO.getVatRate(), paramVO.getFocusId(),
					paramVO.getPageCnt().equals("0")?"inf":currPage, paramVO.getSerno_disp_yn()); //currPage 구하다가 연산자 오버플로우 발생 발지
			resultListVO.setResultHeader(resultHeader);
			
			resultListVO.setList(resultVO);
			
			//######################################################
			// 로직구현 (끝)
			//######################################################
			
	    	
	    	tp1	= System.currentTimeMillis();
			imcsLog.timeLog("공연 뮤직 리스트 Fetch", String.valueOf(tp1 - tp2), methodName, methodLine); 
			
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

			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID210) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
					+ " [DB:" + nUserDB + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	
	/**
	 * 검수 STB여부 조회
	 * @param paramVO
	 * @return String		TEST_SBC	
	 */
	public String getTestSbc(GetNSSeriesListRequestVO paramVO) throws Exception {
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 		= "nsvod210_001_20180601";
		String szTestSbc	= "N";
		
		int querySize		= 0;

		try {			
			List<String> list = getNSSeriesListDao.getTestSbc(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;

			if (list != null && !list.isEmpty()) {
				querySize	= list.size();
				szTestSbc	= StringUtil.replaceNull(list.get(0), "N");
			}else{
				szTestSbc = "N";
			}
			
			try{
				imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID210, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return szTestSbc;
	}
	
	/**
	 * 카테고리 정보 조회
	 * @param 	GetNSWatchListRequestVO paramVO
	 * @return  HashMap<String, String>
	 **/
	public HashMap<String, String> getCateInfo(GetNSSeriesListRequestVO paramVO) throws Exception {
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "nsvod040_002_20180601";
		
		HashMap<String, String> catInfo = new HashMap<String, String>();
		int querySize = 0;

		try {
			//if ( paramVO.getTestSbc().equals("Y") ) {
				catInfo = getNSSeriesListDao.getCateInfo1(paramVO);
			//} else {
			//	catInfo = getNSSeriesListDao.getCateInfo2(paramVO);				
			//}
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;

			if (catInfo != null && !catInfo.isEmpty()) {
				querySize	= catInfo.size();
			}
			
			try{
				imcsLog.dbLog2(ImcsConstants.NSAPI_PRO_ID210, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID210, sqlId, null, "cate_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return catInfo;
	}
	
	/**
	 * 마지막 시청 회차 가져오기
	 * @param 	GetNSWatchListRequestVO paramVO
	 * @return  HashMap<String, String>
	 **/
	public GetNSSeriesListRequestVO setCondition(GetNSSeriesListRequestVO paramVO, String AWS_web) throws Exception {
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "nsvod040_003_20180601";
		
		HashMap<String, String> lastWInfo = new HashMap<String, String>();
		int querySize = 0;

		try {
			String watch_album_id  = "";
			String watch_no        = "";
			String watchCount      = "";
			int bgnNo = 0;
			int endNo = 0;
			// 2021.02.24 - 모바일TV 기능개선 4차수 : 미편성 PPS의 경우 카테고리 정보를 조회하지 않는다. (PT_LA_GROUP_RELATION으로 정보 제공 필요)
		    if(paramVO.getRqsType().equals("V") && (paramVO.getCatId().startsWith("7") || paramVO.getCatId().startsWith("8")) && paramVO.getCatId().length() == 5)
		    {
		    	lastWInfo = null;
		    }
		    else
		    {
		    	if(AWS_web.equals("N"))
		    	{
		    		lastWInfo = getNSSeriesListDao.getLastWatchNo(paramVO);
		    	}
		    	else
		    	{
		    		// 2021.05.20 - 프로야구 모바일 웹 (배포속도 개선)
		    		lastWInfo = getNSSeriesListDao.getLastWatchNoAws(paramVO);
		    	}
		    }
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			if (lastWInfo!=null) {
				watch_album_id = lastWInfo.get("CONTENTS_ID");
				watch_no = lastWInfo.get("SERIES_NO");
				watchCount = lastWInfo.get("WATCH_COUNT");
				if (watchCount == null || watchCount.equals(""))
					watchCount = "0";
			} else {
				watchCount = "0";
			}
			
			if (Integer.parseInt(watchCount)>0) {
				paramVO.setRqsNo(watch_no);
				paramVO.setFocusId(watch_album_id);
			}
			
			if (paramVO.getPageNo().equals("0")) {
				if (paramVO.getRqsNo().equals("0")) {
					bgnNo = 1;
					endNo = Integer.parseInt(paramVO.getPageCnt());
				}
			} else {
				bgnNo = Integer.parseInt(paramVO.getPageNo());
				endNo = Integer.parseInt(paramVO.getPageCnt());
				
				bgnNo = (bgnNo - 1) * endNo + 1;
				endNo = bgnNo + endNo - 1;
				
				paramVO.setRqsNo("0");
			}
			
			paramVO.setBgnNo(String.valueOf(bgnNo));
			paramVO.setEndNo(String.valueOf(endNo));
			//System.out.println("############################:"+ paramVO.getBgnNo() + "|" + paramVO.getEndNo());
			
			//정렬방식
			if (paramVO.getOrderGb().equals("A")) {
				if (paramVO.getCloseYn().equals("Y"))
					paramVO.setOrderGb("S");
				else
					paramVO.setOrderGb("N");
			}
			
			if (paramVO.getOrderGb().equals("S"))
				paramVO.setOrderType("DESC");
			else
				paramVO.setOrderType("");

			if (lastWInfo != null && !lastWInfo.isEmpty()) {
				querySize	= lastWInfo.size();
			}
			
			try{
				imcsLog.dbLog2(ImcsConstants.NSAPI_PRO_ID210, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID210, sqlId, null, "cate_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return paramVO;
	}
	
	/**
	 * 컨텐츠 편성 정보 가져오기
	 * @param 	GetNSWatchListRequestVO paramVO
	 * @return  GetNSSeriesListResponseVO
	 **/
	public List<GetNSSeriesListResponseVO> getContScheduleList (GetNSSeriesListRequestVO paramVO, String AWS_web) throws Exception {
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "nsvod040_010_20180601";
		String cur_date = commonService.getSysdate().substring(0, 12);
		List<GetNSSeriesListResponseVO> listContSched = new ArrayList<GetNSSeriesListResponseVO>();
		List<ComDataFreeVO> list   = new ArrayList<ComDataFreeVO>();
		ComDataFreeVO datafreeVO;
		List<String> main_img_file_name = new ArrayList<String>();
		int querySize = 0;

		try {
			if (paramVO.getRqsType().equals("M"))
			{
				listContSched = getNSSeriesListDao.getContSchedList1(paramVO);
			}
			else
			{
				// 2021.02.24 - 모바일TV 기능개선 4차수 : 미편성 PPS의 경우 카테고리 정보를 조회하지 않는다. (PT_LA_GROUP_RELATION으로 정보 제공 필요)
			    if((paramVO.getCatId().startsWith("7") || paramVO.getCatId().startsWith("8")) && paramVO.getCatId().length() == 5)
			    {
			    	listContSched = getNSSeriesListDao.getContSchedList3(paramVO);
			    }
			    else
			    {
			    	if(AWS_web.equals("N"))
			    	{
			    		listContSched = getNSSeriesListDao.getContSchedList2(paramVO);
			    	}
			    	else
			    	{
			    		listContSched = getNSSeriesListDao.getContSchedListAWS(paramVO);
			    	}
			    }
			}
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			int	iCount = 0;
			for (GetNSSeriesListResponseVO item : listContSched) {
				
				if (item.getVodType().equals("D")) {
					item.setVodType("Y");
				} else {
					item.setVodType("N");
				}
				
				if(item.getInappBuyYn().equals("Y"))
				{
					datafreeVO = null;
					datafreeVO = new ComDataFreeVO();
					datafreeVO.setPrice(item.getPrice());
					
					// 2020.12.22 - 모바일 아이들나라 인앱결제 (모바일TV와 모바일 아이들나라 인앱 분리)
					if(paramVO.getAppType().substring(0,1).equals("A"))
					{
						datafreeVO.setApprovalGb("A");
					}
					else if(paramVO.getAppType().substring(0,1).equals("E"))
					{
						datafreeVO.setApprovalGb("E");
					}					
					else if(paramVO.getAppType().substring(0,1).equals("L"))
					{
						datafreeVO.setApprovalGb("L");
					}
					else
					{
						datafreeVO.setApprovalGb("N");
					}
					list = getNSSeriesListDao.getDatafreeInfo(datafreeVO);
					
					if( list != null && !list.isEmpty()){
						item.setInappPrice(list.get(0).getApprovalPrice());
					}
				}
				
		        //----------------------------	
		        //스틸 이미지 정보 가져오기
		        //----------------------------	
		        main_img_file_name  = this.getNSSeriesListDao.getStillImage(item);
		        paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
		        try
		        {
		        	item.setStillFileName(main_img_file_name.get(0));
		        }catch(Exception e)
		        {}
		        
		        //----------------------------	
		        //다운로드 여부 가져오기
		        //----------------------------	
		        String down_flag  = this.getNSSeriesListDao.getDownFlag(item);
		        paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
		        item.setDownloadYn(down_flag);
		        
				if (item.getPayFlag().equals("1")) {
					
					String cstEndTime = "0";
					
					if(item.getPerformEndTime().length() == 4 && item.getPerformEndDate().length() == 8) {
						cstEndTime = item.getPerformEndDate() + item.getPerformEndTime();
					}
					
					if (Double.parseDouble(cur_date) < Double.parseDouble(cstEndTime) || cstEndTime.equals("0")) {
						item.setLivePpvYn("Y");
					} else {
						item.setLivePpvYn("N");
					}
				} else {
					item.setLivePpvYn("N");
				}
		        
			}
			
			if (listContSched != null && !listContSched.isEmpty()) {
				querySize	= listContSched.size();
			}
			
			try{
				imcsLog.dbLog2(ImcsConstants.NSAPI_PRO_ID210, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID210, sqlId, null, "cate_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return listContSched;
	}
	
	
	/*-----------------------------------------------------------------
    getSeriesInfo() : 시리즈 정보 가져오기
 	------------------------------------------------------------------
  	시리즈 정보 가져오기 시즌 정보 있는 경우 시즌 정보까지 가져온다.
	------------------------------------------------------------------*/
	public List<ComSeriesInfoVO> getSeriesInfo (GetNSSeriesListRequestVO paramVO) throws Exception {
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "nsvod040_010_20180601";
		int querySize = 0;
		
		List<ComSeriesInfoVO> listSeriesInfo = new ArrayList<ComSeriesInfoVO>();

		try {
			if (paramVO.getTestSbc().equals("Y")) {
				paramVO.setTestSbc("Y");
			} else {
				paramVO.setTestSbc("N");
			}
			
			listSeriesInfo = getNSSeriesListDao.getSeriesInfo(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			int iCount = 0;
			for (ComSeriesInfoVO item : listSeriesInfo) {
				try {
					if (paramVO.getSeriesId().equals(item.getSeriesId())) {
			            if ( !paramVO.getCatId().equals(item.getCategoryId()) )
			            {
			                item.setCategoryId(paramVO.getCatId());
			            }
					}
				}  catch (Exception e) {}
				
				iCount++;
				
				if (iCount == 1) {
					paramVO.setSerCatId(item.getCategoryId());
					paramVO.setSerCatNm(item.getSeriesDispName());
				} else {
					paramVO.setSerCatId(paramVO.getSerCatId() + "\b" + item.getCategoryId());
					paramVO.setSerCatNm(paramVO.getSerCatNm() + "\b" +item.getSeriesDispName());
				}
			}
			
			if (listSeriesInfo != null && !listSeriesInfo.isEmpty()) {
				querySize	= listSeriesInfo.size();
			}
			
			try{
				imcsLog.dbLog2(ImcsConstants.NSAPI_PRO_ID210, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID210, sqlId, null, "series_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return listSeriesInfo;
	}

}
