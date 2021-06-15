package kr.co.wincom.imcs.api.getNSReposited;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.AlbumInfoVO;
import kr.co.wincom.imcs.common.vo.CateInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.handler.ImcsException;

@Service
public class GetNSRepositedServiceImpl implements GetNSRepositedService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSReposited");
	
	@Autowired
	private GetNSRepositedDao getNSRepositedDao;
	
	@Autowired
	private CommonService commonService;

//	public void getNSReposited(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	private static final int MAX_RES_CNT  = 150;
	
	/**
	 * @author HONG
	 * @since 2016-07-12
	 * 4.4.18  시청 목록 가져오기 (lgvod129.pc)
	 */
	@Override
	public GetNSRepositedResultVO getNSReposited(GetNSRepositedRequestVO paramVO)	{
//		this.getNSReposited(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		GetNSRepositedResultVO	resultListVO		= new GetNSRepositedResultVO();
		GetNSRepositedResponseVO tempVO				= new GetNSRepositedResponseVO();
		List<GetNSRepositedResponseVO> resultVO		= new ArrayList<GetNSRepositedResponseVO>();
		List<GetNSRepositedResponseVO> returnVO		= new ArrayList<GetNSRepositedResponseVO>();
		
		int nMainCnt	= 0;
		int resultSet	= 0;
		int iTotalCount = 0;
		int data_chk 	= 0;
		
		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2
		
		String szImgSvrIp	= "";		// 이미지 서버 IP
		String szImgSvrUrl	= "";		// 이미지 서버 URL
		String msg			= "";
		
		String[] nscArray = null;
		
		try {
			// 서버IP정보 조회
			tp1 = System.currentTimeMillis();
			
			try {
				szImgSvrIp	= commonService.getIpInfo("img_server", ImcsConstants.API_PRO_ID129.split("/")[1]);			// 이미지서버 IP 조회
				szImgSvrUrl	= commonService.getIpInfo("img_resize_server", ImcsConstants.API_PRO_ID129.split("/")[1]);	// 이미지서버 URL 조회
			} catch(Exception e) {
				msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID129) + "] sts[" + ImcsConstants.LOG_MSG2 + "] msg[" + String.format("%-21s", "svr_ip:" + ImcsConstants.RCV_MSG1 + "]");
				imcsLog.serviceLog(msg, methodName, methodLine);
				paramVO.setResultCode("31000000");
				
				throw new ImcsException();
			}
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			//DB시간 가져오기
			String szSysDate	= "";
			
			try {
				szSysDate = commonService.getSysdate();
				paramVO.setSysDate(szSysDate);
			} catch (Exception e) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID996, "", null, "sysdate:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
			}
			
			//20180724 권형도 (가입자의 검수 여부 조회 초기화)
			//String szTestSbc	= "Y";
			//paramVO.setTestSbc(szTestSbc);
			
			String szTestSbc	= "N";
			
			if(paramVO.getNscListYn().equals("Y")) {
				// 엔스크린 구매연동 여부 및 테스트 계정 확인
				// getCustPairingChk 메소드 안에서 N_SA_ID, N_STB_MAC, TEST_SBC 및 nIdxSa 값 세팅 함.
				resultSet = this.getCustPairingChk(paramVO);
				
				// nScreen(STB) 목록 조회 요청을 했는데 페이링이 되어 있지 않으면 바로 리턴
				if(resultSet != 0)
				{
					imcsLog.timeLog("페어링 정보 없음", String.valueOf(System.currentTimeMillis() - tp2), methodName, methodLine);
					
					if(paramVO.getNscListYn().equals("Y"))
					{
						tempVO.setContsType("N");
						returnVO.add(tempVO);
						resultListVO.setList(returnVO);
						
						paramVO.setResultCode("31000000");
						
						return resultListVO;
					}
				}
			} else { //2018.07.24 권형도
		    	//2018.06.27 - 엔스크린2차 nsc_list_yn이 N이라도 가입자의 검수여부를 조회
		    	//             일반사용자의 경우 양쪽 스크린 모두 검수 카테고리에만 편성된 것이 없어야하기 때문!
				szTestSbc	= this.getTestSbc(paramVO);
				paramVO.setTestSbc(szTestSbc);
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("검수 STB여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			}
			
			// 2. 시청목록 리스트 조회
			resultVO = this.getWatchingList(paramVO);
			tp1 = System.currentTimeMillis();
			imcsLog.timeLog("시청목록 조회(SELETE PT_VO_SET_TIME_PTT)", String.valueOf(tp1 - tp2), methodName, methodLine);
			
			
//			// 날짜 검색
//			String szSysDate	= "";
//			
//			try {
//				szSysDate = commonService.getSysdate();
//			} catch (Exception e) {
//				imcsLog.failLog(ImcsConstants.API_PRO_ID996, "", null, "sysdate:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
//			}
			
			if(resultVO != null)
				nMainCnt = resultVO.size();
			
			int iResultCount = 0;
			
			for(int i = 0; i < nMainCnt; i++) {
				if(iResultCount > 59)			break;
				
				tempVO	= resultVO.get(i);
				tempVO.setImgUrl(szImgSvrUrl);
				
				paramVO.setContsId(tempVO.getContsId());			// 이후 쿼리를 위해 paramVO에 set
				paramVO.setTempCheck(tempVO.getTempCheck().trim());	// 이후 쿼리를 위해 paramVO에 set
				paramVO.setSysDate(szSysDate);
				
				if(Long.parseLong(tempVO.getBuyEndDate()) > Long.parseLong(szSysDate)){
					tempVO.setExpiredYn("N");
				}else{
					tempVO.setExpiredYn("Y");
				}		
				
				if (i==0) {
					iTotalCount = Integer.parseInt(tempVO.getTotalCnt());
					if (iTotalCount > MAX_RES_CNT)
						iTotalCount = MAX_RES_CNT;
				}
				
				if(tempVO.getnScreenYn().length() > 0) {
					nscArray = tempVO.getnScreenYn().split(";");
					tempVO.setnScreenYn(nscArray[0]);
					
					//2021.05.11 Seamless 단방향 서비스 2차
					if(paramVO.getNscListYn().equals("Y")) {
						if(nscArray[0].equals("Y")) {
							if(nscArray[2].equals("M")) {
								iTotalCount--;
								continue;
							}
						}
					}
				}
				
				
				// 카테고리 정보조회
				CateInfoVO cateInfoVO = new CateInfoVO();
				cateInfoVO = this.getCateInfo(paramVO);
				
				if(!cateInfoVO.getCount().equals("0")) {
					String szSerInfo	= cateInfoVO.getCateInfo();
					
					// iCateCnt	= Integer.parseInt(cateInfoVO.getCount());		// NullToZero 처리 해줘야 함 - nosql 내부에서 처리하여 불필요
					tempVO.setCatId(cateInfoVO.getCateList());
					// tempVO.setCatId(cateInfoVO.getCategoryId());		// 위와 같이 CateId 로 사용되나 싶은데 이후 사용되는 곳은 없음
					tempVO.setIsNew(cateInfoVO.getIsNew());
					tempVO.setChaNum(cateInfoVO.getChaNum());
					tempVO.setAuthYn(cateInfoVO.getAuthYn());
					tempVO.setSerCatId(szSerInfo.substring(0, szSerInfo.indexOf("|")));
					tempVO.setSeriesNo(szSerInfo.substring(szSerInfo.indexOf("|") + 1, szSerInfo.length()));
					tempVO.setSeriesYn(cateInfoVO.getSeriesYn());
					tempVO.setCatGb(cateInfoVO.getCateGb1() + "\b" + cateInfoVO.getCateGb2() + "\b" + cateInfoVO.getCateGb3());
					tempVO.setBelongingName(cateInfoVO.getBelongingName());
					tempVO.setCateGb4(cateInfoVO.getCateGb4());
					tempVO.setIptvTestSbc(cateInfoVO.getIptvTestSbc());
					tempVO.setNscTestSbc(cateInfoVO.getNscTestSbc());
				} else {
					iTotalCount--;
					continue;
				}
				
				// 유플릭스 체크 값 세팅 - 아래의 로직 전 진행해야 함
				if(tempVO.getUflixYn().equals("M") || tempVO.getUflixYn().equals("P") || tempVO.getUflixYn().equals("T") || tempVO.getUflixYn().equals("H"))
					paramVO.setUflixCheck("Y");
				else
					paramVO.setUflixCheck("N");
				
				paramVO.setUflixYn(tempVO.getUflixYn());				
							
				// 앨범 정보조회 - 분기만 많을 뿐 간단함 - 추후 정리
				AlbumInfoVO albumInfoVO = new AlbumInfoVO();
				albumInfoVO = this.getAlbumInfo(paramVO);
				
				// 앨범 정보가 존재할 경우
				if(albumInfoVO != null){
					tempVO.setContsName(albumInfoVO.getContsName());
					tempVO.setOnairDate(albumInfoVO.getOnairDate());
					tempVO.setSeriesDesc(albumInfoVO.getSeriesDesc());
					tempVO.setRealHd(albumInfoVO.getRealHd());
					tempVO.setPoint(albumInfoVO.getPoint());
					tempVO.setPrInfo(albumInfoVO.getPrInfo());
					tempVO.setIsHd(albumInfoVO.getIsHd());
					tempVO.setRuntime(albumInfoVO.getRunTime());
					tempVO.setLicensingWindowEnd(albumInfoVO.getLicensingEnd());
					tempVO.setGenreGb(albumInfoVO.getGenreGb());
					tempVO.setServiceIcon(albumInfoVO.getServiceIcon());
					tempVO.setFmYn(albumInfoVO.getFmInfo());
					// 2017.11.30 엔스크린 추가
					tempVO.setnScreenYn(albumInfoVO.getnScreenYn());
					
					// 2018.06.03 (2018.07.25 권형도) - 엔스크린2차 : 양쪽 스크린에 모두 편성되어 있는지와 엔스크린 여부를 체크하는 로직 추가
					if(paramVO.getNscListYn().equals("Y") && 
							(!tempVO.getCateGb4().equals("I30") || 
							tempVO.getnScreenYn().equals("N") ||
							(paramVO.getTestSbc().equals("N") && ( tempVO.getIptvTestSbc().equals("Y")||tempVO.getNscTestSbc().equals("Y") )))) {
						iTotalCount--;
					} else if (paramVO.getNscListYn().equals("N") &&
							(!tempVO.getCateGb4().equals("I30") || 
							(paramVO.getTestSbc().equals("N") && ( tempVO.getIptvTestSbc().equals("Y")||tempVO.getNscTestSbc().equals("Y") )))) {
						tempVO.setnScreenYn("N");
					}
					
					if(paramVO.getTempCheck().equals("20") || paramVO.getTempCheck().equals("30")) {
						tempVO.setImgFileName("X");
					} else {
						tempVO.setImgFileName(albumInfoVO.getTempMinValue());
					}
					
					// 이미지 스틸컷 정보 조회
					String stillFileName	= "";
					if(tempVO.getImgFileName().equals("X")) {
						// NullToSpace 넣어주면 좋을 듯
						stillFileName = this.getStillFileName(paramVO);
						tempVO.setImgFileName(stillFileName);
					}
					
					// 시청이력의 PRODUCT_ID와 앨범정보의 CONTS_ID 가 같을 경우
					String szContsType	= "";
					String szScreenType = "";
					paramVO.setProductId(tempVO.getProductId());
					paramVO.setContsId(tempVO.getContsId());
					
					// 2018.06.05 (2018.07.25 권형도) - 엔스크린2차 : 엔스크린 여부가 Y인데 IPTV의 상품이 FVOD이면 목록에서 제외한다.
					List<GetNSRepositedResponseVO> resultProTypeVO = this.getProductType(paramVO);
					
					data_chk = 0;
					for ( int j = 0 ; j < resultProTypeVO.size() ; j++ ) {
						szContsType = resultProTypeVO.get(j).getProductType();
						System.out.println("szContsType:" + szContsType);
						szScreenType = resultProTypeVO.get(j).getScreenGubun();
						tempVO.setContsType(szContsType);
						
						if (szScreenType.equals("IPT")) {
							if (paramVO.getNscListYn().equals("Y")) {
								if (szContsType.equals("0")) {
									data_chk = 1;
									break;
								}
								continue;
							} else {
								if (szContsType.equals("0")) {
									tempVO.setnScreenYn("N");
									continue;
								}
							}
						} else {
							if (paramVO.getNscListYn().equals("Y")) {
								if (szContsType.equals("0")) {
									data_chk = 1;
									break;
								}
							} else {
								if (szContsType.equals("0")) {
									tempVO.setnScreenYn("N");
									break;
								}
							}						
						}
						
						if(szContsType.equals("X"))	tempVO.setContsType("");
						
						// 2017.10.10 - 엔스크린(NSCREEN) 온디맨드류는 4로 나올 수 있는데,
						// 연동규격서 상에 4에 대한 정의는 없으므로 3으로 치환
		                if(tempVO.getContsType().equals("4"))
		                	tempVO.setContsType("3");

		                // 2017.10.10 - 엔스크린(NSCREEN) IPTV에서 PPS 구매한 건은 PPV앨범 단위로 노출하기 때문에
		                // conts_type이 2가 나올 경우 1로 치환
		                if(paramVO.getNscListYn().equals("Y"))
		                {
		                	 if(tempVO.getContsType().equals("2"))
		                		 tempVO.setContsType("1");
		                }
						
					}
					if(data_chk == 1)
		            {
		            	iTotalCount--;
		            	continue;
		            }
					
					data_chk = 0;
					
					//2018.06.05 - 엔스크린2차 : IPTV/NSC 편성 체크 추가
//					if (resultProTypeVO.size() > 0) {
//						szContsType = resultProTypeVO.get(0).getProductType();
//						System.out.println("szContsType:" + szContsType);
//						szScreenType = resultProTypeVO.get(0).getScreenGubun();
//					}
					

					
//					if(szContsType != null && !szContsType.equals("")) {
//						if(szContsType.equals("X"))	szContsType = "";
//						
//						tempVO.setContsType(szContsType);
//						
//						// 2017.10.10 - 엔스크린(NSCREEN) 온디맨드류는 4로 나올 수 있는데,
//						// 연동규격서 상에 4에 대한 정의는 없으므로 3으로 치환
//		                if(tempVO.getContsType().equals("4"))
//		                	tempVO.setContsType("3");
//
//		                // 2017.10.10 - 엔스크린(NSCREEN) IPTV에서 PPS 구매한 건은 PPV앨범 단위로 노출하기 때문에
//		                // conts_type이 2가 나올 경우 1로 치환
//		                if(paramVO.getNscListYn().equals("Y"))
//		                {
//		                	 if(tempVO.getContsType().equals("2"))
//		                		 tempVO.setContsType("1");
//		                }
//					} else {
//						tempVO.setContsType("");
//					}
					
		            // 2017.10.10 - 엔스크린(NSCREEN) IPTV사용자 구매 여부 DEFAULT 값 설정
		            tempVO.setnBuyYn("N");
					
					// 구매정보 조회 (CP_USE_YN, BUYING_PRICE, EXPIRED_DATE)
					HashMap<String, String> mBuyInfo = new HashMap<String, String>();
				
					mBuyInfo = this.getBuyInfo(paramVO, tp1, tempVO);
					
					if(mBuyInfo != null && mBuyInfo.size() != 0){
						tempVO.setCpUseYn(mBuyInfo.get("CP_USE_YN"));
						tempVO.setBuyingPrice(mBuyInfo.get("BUYING_PRICE"));
						tempVO.setExpiredDate(mBuyInfo.get("EXPIRED_DATE"));
					} else {
						tempVO.setCpUseYn("");
						tempVO.setBuyingPrice("");
						tempVO.setExpiredDate("");
					}
					
					// 서버 IP 
					tempVO.setImgUrl(szImgSvrIp);
					tempVO.setImgUrl2(szImgSvrUrl);					
					
					// 이미지파일명 조회
					String szStillFileName	= "";
					szStillFileName	= this.getImageFileName(paramVO);
					tempVO.setThumbnailFileName(szStillFileName);
					
					// 이어보기 정보조회
					String szLinkTime	= this.getLinkTime(paramVO, tempVO);
					tempVO.setLinkTime(szLinkTime);
					
				} else
					continue;
				
				returnVO.add(tempVO);
				iResultCount++;
				
				
			}
			// 시청목록 리스트가 없고 nScreen(STB) 목록 조회 요청일 때 바로 리턴
			if(nMainCnt == 0 && paramVO.getNscListYn().equals("Y"))
			{
				tempVO.setContsType("Y");
				returnVO.add(tempVO);
				resultListVO.setList(returnVO);
				
				return resultListVO;
			}
			else
			{
				for (int i = 0 ; i < returnVO.size(); i++)
				{
					returnVO.get(i).setTotalCnt(Integer.toString(iTotalCount));
				}
			}
			tp2 = System.currentTimeMillis();
			imcsLog.timeLog("시청목록 FETCH", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			resultListVO.setList(returnVO);
		} catch(ImcsException ie) {
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw ie;
		} catch(Exception e) {
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		} finally{
			resultListVO.setResultCode(StringUtil.replaceNull(paramVO.getResultCode(), "20000000"));
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID129) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	
	
	/**
	 * 구매정보 조회
	 * @param  GetNSRepositedRequestVO paramVO
	 * @return HashMap<String, String>
	 */
	public HashMap<String, String> getBuyInfo(GetNSRepositedRequestVO paramVO, long tp1, GetNSRepositedResponseVO tempVO) {
		HashMap<String, String> mBuyInfo	= new HashMap<String, String>();
		List<String> mDataFreeInfo	= new ArrayList<String>();
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		long tp2			= 0;		// timePoint 2
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		String szMsg = "";
		
		// 엔스크린 관련 변수 - 2017.11.30
		int authChk = 0;
		int dataChk = 0;
		
		if(paramVO.getNscListYn().equals("N"))
			authChk = 1; // 모바일 구매/가입 여부 부터 체크 (모바일 -> IPTV)
		else
			authChk = 2; // IPTV 구매/가입 여부 부터 체크 (IPTV -> 모바일)
		
		String strExpiredDate = "0";
		String strNExpiredDate = "0";
		
		for(int iii = 0; iii < 2; iii++)
		{
			if(Long.parseLong(strExpiredDate) < Long.parseLong(paramVO.getSysDate())
					&& Long.parseLong(strNExpiredDate) < Long.parseLong(paramVO.getSysDate()))
			{
				dataChk = 0;
			}
			
			if((authChk == 1 || authChk == 3) && dataChk == 0)
			{
				if(tempVO.getContsType().equals("1")) // PPV일 경우 구매정보 조회 - contsId 로 조회
				{
					paramVO.setId(paramVO.getContsId());
					paramVO.setBuyingDate(tempVO.getBuyingDate());
					
					try
					{
						/* 해당 시청 컨텐츠의 데이터 프리 구매여부 조회	*/
						mDataFreeInfo = getNSRepositedDao.getBuyDataFreeInfo(paramVO);
						
						if(mDataFreeInfo == null || mDataFreeInfo.isEmpty())
							tempVO.setDatafreeBuyYn("N");
						else
							tempVO.setDatafreeBuyYn("Y");
						
						szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID129) + "] chkPaymentId [SELECT DATAFREE_BUY_YN PT_VO_BUY] table[" + mDataFreeInfo.size() + "] records Success at";
						imcsLog.serviceLog(szMsg, methodName, methodLine);
						
					} catch (Exception e) {				
						//imcsLog.failLog(ImcsConstants.API_PRO_ID129, "", null, ImcsConstants.RCV_MSG6, methodName, methodLine);
					}
					
					// 구매 정보 조회
					if(paramVO.getNscListYn().equals("N"))
						mBuyInfo = getNSRepositedDao.getBuyInfo(paramVO);
					else
						mBuyInfo = getNSRepositedDao.getBuyInfoNScreen(paramVO);
					
					boolean isPresentQuery = false;
					
					// 구매 데이타가 아닌 경우 받은선물 을 확인
					// getBuyInfoNScreen 쿼리는 데이터가 무조건 1건이 있으므로 CP_USE_YN 값을 체크함.
					if(mBuyInfo == null || StringUtils.isBlank(mBuyInfo.get("CP_USE_YN")))
						isPresentQuery = true;
					
					if(isPresentQuery == true)
					{
						if(paramVO.getNscListYn().equals("N"))
							mBuyInfo = getNSRepositedDao.getPresentInfo(paramVO);
						else
							mBuyInfo = getNSRepositedDao.getPresentInfoNScreen(paramVO);
						
					}
					
					if(mBuyInfo != null && mBuyInfo.size() > 0
							&& StringUtils.isNotEmpty(mBuyInfo.get("EXPIRED_DATE")))
					{
						strExpiredDate = mBuyInfo.get("EXPIRED_DATE");
						
						if(Long.parseLong(strExpiredDate) > Long.parseLong(paramVO.getSysDate()))
						{
							dataChk = 1;
						}
					}
				}
				else if(tempVO.getContsType().equals("2")) // PVOD일 경우 구매정보 조회 - productID로 조회
				{
					paramVO.setId(paramVO.getProductId());
					paramVO.setBuyingDate(tempVO.getBuyingDate());
					tempVO.setDatafreeBuyYn("N");
					
					mBuyInfo = getNSRepositedDao.getBuyInfo(paramVO);
					
					if(mBuyInfo != null && mBuyInfo.size() > 0
							&& StringUtils.isNotEmpty(mBuyInfo.get("EXPIRED_DATE")))
					{
						strExpiredDate = mBuyInfo.get("EXPIRED_DATE");
						
						if(Long.parseLong(strExpiredDate) > Long.parseLong(paramVO.getSysDate()))
						{
							dataChk = 1;
						}
					}
				}
				else
				{
					// 2018.06.04 - 구매연동하면서 FVOD에 대한 데이터프리 구매 여부를 빼먹어서 추가....;;
					/* 해당 시청 컨텐츠의 데이터 프리 구매여부 조회	*/
					//mBuyInfo = new HashMap<String, String>();
					
					/* 해당 시청 컨텐츠의 데이터 프리 구매여부 조회	*/
					mDataFreeInfo = getNSRepositedDao.getBuyDataFreeInfo(paramVO);
					
					if(mDataFreeInfo == null || mDataFreeInfo.isEmpty())
						tempVO.setDatafreeBuyYn("N");
					else
						tempVO.setDatafreeBuyYn("Y");
				}
			}
			else if(authChk == 2 && dataChk == 0 && tempVO.getnScreenYn().equals("Y"))
			{
				HashMap<String, String> mBuyInfoNScreen = null;
				
				paramVO.setId(paramVO.getContsId());
				paramVO.setBuyingDate(tempVO.getBuyingDate());
				
				try
				{
					// 엔스크린 구매만료 여부check
					mBuyInfoNScreen = getNSRepositedDao.getNScreenBuyChk(paramVO);
					
					szMsg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID129) + "] SQLID[lgvod129_s05_20171207_001]";
					imcsLog.serviceLog(szMsg, methodName, methodLine);
					
					if(mBuyInfoNScreen != null && mBuyInfoNScreen.size() > 0)
					{
						dataChk = mBuyInfoNScreen.size();
						
						if(Integer.parseInt(mBuyInfoNScreen.get("DATA_CHK")) > 0)
							tempVO.setnBuyYn("Y");
						
						if(StringUtils.isNotBlank(mBuyInfoNScreen.get("N_BUY_DATE")))
							tempVO.setnBuyDate(mBuyInfoNScreen.get("N_BUY_DATE"));
						
						if(StringUtils.isNotBlank(mBuyInfoNScreen.get("N_EXPIRED_DATE")))
						{
							strNExpiredDate = mBuyInfoNScreen.get("N_EXPIRED_DATE");
							tempVO.setnExpiredDate(mBuyInfoNScreen.get("N_EXPIRED_DATE"));
						}
					}
				}
				catch(Exception e)
				{
					//imcsLog.failLog(ImcsConstants.API_PRO_ID129, "", null, ImcsConstants.RCV_MSG6, methodName, methodLine);
				}
			}
			
			authChk++;
		}
		
		return mBuyInfo;
	}


	/**
	 * 이미지 파일명 조회
	 * @param  GetNSRepositedRequestVO paramVO
	 * @return String
	 */
	public String getImageFileName(GetNSRepositedRequestVO paramVO) {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId	= "lgvod129_004_20171214_001";
		
		List<StillImageVO> list	= new ArrayList<StillImageVO>();
		StillImageVO tempVO		= new StillImageVO();
		String szImageFileName	= "";
		
		try {
			
			try {
				list = getNSRepositedDao.getImageFileName(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list != null && !list.isEmpty()) {
			//	querySize	= list.size();
				
				int i_still_cnt = 0;
				for(int i = 0;  i < list.size(); i++){
					tempVO		= list.get(0);
					
					if(tempVO.getImgFlag().equals("N")){
						i_still_cnt ++;
						if(i_still_cnt == 1){
							szImageFileName = tempVO.getImgFileName();
						}
					}
				}
			}
			
			try{
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID129, sqlId, cache, 0, methodName, methodLine);
			}catch(Exception e){}
			
			//paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
		} catch (Exception e) {
			//if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
			//else									paramVO.setResultCode("41000000");
			
			//imcsLog.failLog(ImcsConstants.API_PRO_ID129, sqlId, null, "still_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return szImageFileName;
	}
	

	
	/**
	 * 상품 타입 조회 1
	 * @param 	GetNSRepositedRequestVO
	 * @return  String
	 **/
	public List<GetNSRepositedResponseVO> getProductType(GetNSRepositedRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String szTempContsId	= "";
		Boolean bCompareId		= false;
		
		if(paramVO.getContsId().length() > 15)		szTempContsId = paramVO.getContsId().substring(0, 15);
		else										szTempContsId = paramVO.getContsId();
		
		if(paramVO.getProductId().equals(szTempContsId))	bCompareId	= true;
		paramVO.setbCompareId(bCompareId);		// ProductId와 ContsId가 같은지 확인
		
		String sqlId	= "";
		
		if(bCompareId)		sqlId	= "lgvod129_p01_20180611_001";
		else 				sqlId	= "lgvod129_p02_20180605_001";
		
		int querySize	= 0;
		
		List<GetNSRepositedResponseVO> list = new ArrayList<GetNSRepositedResponseVO>();

		try {
			try {
				if(paramVO.getbCompareId())
					list = getNSRepositedDao.getProductTypeInfo1(paramVO);
				else
					list = getNSRepositedDao.getProductTypeInfo2(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list != null && !list.isEmpty()) {
				querySize		= list.size();
			}
			
			try{
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID129, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
			//paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
		} catch (Exception e) {
			//if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
			//else									paramVO.setResultCode("41000000");
			
			//imcsLog.failLog(ImcsConstants.API_PRO_ID129, sqlId, null, "prod_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
		}

		return list;
	}
	
	
	/**
	 * 스틸 이미지 정보 조회
	 * @param 	GetNSRepositedRequestVO paramVO
	 * @return  String
	 **/
	public String getStillFileName(GetNSRepositedRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		String sqlId = "lgvod129_p00_20171214_001";
		
		List<String> list		= new ArrayList<String>();
		String szStillFileName	= "";
		
		try {
			try {
				list = getNSRepositedDao.getStillFileName(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list != null && !list.isEmpty()) {
				szStillFileName	= list.get(0);		
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}

		return szStillFileName;
	}
	
	
	/**
	 * 앨범 정보 조회
	 * @param 	GetNSRepositedRequestVO paramVO
	 * @return  AlbumInfoVO
	 **/
	public AlbumInfoVO getAlbumInfo(GetNSRepositedRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);

		String sqlId	= "";
		int querySize = 0;
		
		if(paramVO.getTempCheck().equals("10")) {		// FX_TYPE이 N인 케이스
			if(paramVO.getrGrade().equals("Y"))			sqlId = "lgvod129_s81_20180603_001";	// 19금 컨텐츠만  조회
			else if(paramVO.getrGrade().equals("N"))	sqlId = "lgvod129_s91_20180603_001";	// 19금 컨텐츠를 제외한 조회
			else										sqlId = "lgvod129_s01_20180603_001";
		}	

		else if(paramVO.getTempCheck().equals("20")) {	// FX_TYPE이 N이 아닌 것중 
			if(paramVO.getrGrade().equals("Y"))			sqlId = "lgvod129_s82_20180603_001";	// 19금 컨텐츠만  조회
			else if(paramVO.getrGrade().equals("N"))	sqlId = "lgvod129_s92_20180603_001";	// 19금 컨텐츠를 제외한 조회
			else										sqlId = "lgvod129_s02_20180603_001";
		}
		else {
			if(paramVO.getrGrade().equals("Y"))			sqlId = "lgvod129_s83_20180603_001";	// 19금 컨텐츠만  조회
			else if(paramVO.getrGrade().equals("N"))	sqlId = "lgvod129_s93_20180603_001";	// 19금 컨텐츠를 제외한 조회
			else										sqlId = "lgvod129_s03_20180603_001";
		}
		
		List<AlbumInfoVO> list = new ArrayList<AlbumInfoVO>();
		AlbumInfoVO resultVO = null;
		
		try {
			
			list = getNSRepositedDao.getAlbumInfo(paramVO);

			if (list != null && !list.isEmpty()) {
				querySize	= list.size();
				resultVO	= list.get(0);
			}
			
			try{
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID129, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}

		return resultVO;
	}
	


	/**
	 * 카테고리 정보 조회
	 * @param GetNSRepositedRequestVO paramVO
	 * @return CateInfoVO
	 * @throws Exception
	 */
	public CateInfoVO getCateInfo(GetNSRepositedRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);

		String sqlId = "";
		
		if(paramVO.getTestSbc().equals("N"))
			sqlId = "lgvod129_003_20180614_002";
		else
			sqlId = "lgvod129_002_20180614_002";
		
		List<CateInfoVO> list	= new ArrayList<CateInfoVO>();
		CateInfoVO cateInfoVO	= null;

		try {
			
			try {
				list = getNSRepositedDao.getCateInfo(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list == null || list.isEmpty()) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID129, "", null, "cate_info:no data found(" + paramVO.getContsId() + ")", methodName, methodLine);
				paramVO.setResultCode("21000000");
			} else {
				cateInfoVO	= list.get(0);
			}
			
			//paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
		} catch (Exception e) {
			//imcsLog.failLog(ImcsConstants.API_PRO_ID129, sqlId, null, "cate_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			paramVO.setResultCode("41000000");
		}

		return cateInfoVO;
	}


	
	/**
	 * 구매내역 리스트 조회
	 * @param 	GetNSRepositedRequestVO paramVO
	 * @return  List<GetNSRepositedResponseVO>
	 **/
	public List<GetNSRepositedResponseVO> getWatchingList(GetNSRepositedRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "";
		
		int querySize 	= 0;
		
		List<GetNSRepositedResponseVO> list = new ArrayList<GetNSRepositedResponseVO>();

		try {
			try {
				if(paramVO.getNscListYn().equals("Y") == false)
				{
					if(paramVO.getFxType().equals("N"))
						list = getNSRepositedDao.getWatchingListTypeN(paramVO);
					else
						list = getNSRepositedDao.getWatchingListTypeE(paramVO);
				}
				else // 엔스크린 인 경우 시청이력 리스트
				{
					list = getNSRepositedDao.getWatchingListTypeNScreen(paramVO);
				}
	
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list == null || list.isEmpty()) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID129, "", cache, "buys_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				paramVO.setResultCode("21000000");
			} else 	{									
				querySize = list.size();
			}
			try{
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID129, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){
				//imcsLog.debugLog(methodName,"NosqlNoLogging");
			}
			
			//paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
		} catch (Exception e) {
//			if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			
			//imcsLog.failLog(ImcsConstants.API_PRO_ID129, sqlId, null, "buys_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			throw new ImcsException();
		}

		return list;
	}


	
	
	/**
	 *  이어보기 시간 조회
	 * @param 	GetNSRepositedRequestVO paramVO
	 * @return  String		이어보기 시간
	 **/
	public String getLinkTime(GetNSRepositedRequestVO paramVO, GetNSRepositedResponseVO tempVO) throws Exception {
		String sqlId = "lgvod129_s04_20171214_001";
		
		List<String> list = new ArrayList<String>();

		String szLinkTime	= "";
		String strNExpiredDate = "0";
		
		try {
			if(StringUtils.isNotBlank(tempVO.getnExpiredDate()))
				strNExpiredDate = tempVO.getnExpiredDate();
			
			if(Long.parseLong(strNExpiredDate) > Long.parseLong(paramVO.getSysDate()))
			{
				paramVO.setLinkTimeNScreenYn("Y");
				
			}
			else
			{
				paramVO.setLinkTimeNScreenYn("N");
				
			}
			
			try {
				list = getNSRepositedDao.getLinkTime(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list != null && !list.isEmpty())		szLinkTime  = list.get(0);
		} catch (Exception e) {
			throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
		}

		return szLinkTime;
	}
	
	/**
	 * 엔스크린 구매연동 여부 및 테스트 계정 확인
	 * 엔스크린 SA_ID, 엔스크린 STB_MAC, 테스트 계정 여부 및 nIdxSa 값 세팅
	 * 
	 * @param paramVO
	 * @throws Exception
	 */
	public int getCustPairingChk(GetNSRepositedRequestVO paramVO) throws Exception
	{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);

		// 구매내역 리스트 와 쿼리가 같음.
		String sqlId	= "lgvod129_s07_20180829_001";
		
		List<HashMap> list	= new ArrayList<HashMap>();
		
		int resultVal = -1;
		
		try
		{			
			try {
				// 구매내역 리스트 와 쿼리가 같음.
				list = getNSRepositedDao.getCustPairingChk(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if(list == null || list.isEmpty())
			{
				//imcsLog.failLog(ImcsConstants.API_PRO_ID129, "", null, "msg[PAIRING 정보가 없습니다.]", methodName, methodLine);
				paramVO.setResultCode("21000000");
				
				resultVal = -1;
			}
			else
			{
				paramVO.setnSaId((String)list.get(0).get("STB_SA_ID")); // 엔스크린 SA_ID
				paramVO.setnStbMac((String)list.get(0).get("STB_MAC")); // 엔스크린 STB_MAC
				paramVO.setTestSbc((String)list.get(0).get("TEST_SBC")); // 테스트 계정 여부
				
				if(StringUtils.isNotBlank(paramVO.getnSaId()))
				{
					String tempNSaId = paramVO.getnSaId();
					String tempIdxSa = tempNSaId.substring(tempNSaId.length() - 2, tempNSaId.length());
					
					try {
						paramVO.setnIdxSa(Integer.parseInt(tempIdxSa) % 33);
					} catch(Exception e) {
						paramVO.setnIdxSa(0);
					}
				}
				
				resultVal = 0;
			}
		}
		catch(Exception e)
		{
			//imcsLog.failLog(ImcsConstants.API_PRO_ID129, sqlId, null, "msg[PAIRING_INFO:" + ImcsConstants.RCV_MSG6 + "]", methodName, methodLine);
			paramVO.setResultCode("41000000");
			
			resultVal = -1;
			
			throw new ImcsException();
		}
		
		return resultVal;
	}
	
	/**
	 * 검수 STB여부 조회
	 * @param paramVO
	 * @return String		TEST_SBC	
	 */
	public String getTestSbc(GetNSRepositedRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 		= "lgvod129_s07_20180628_001";
		String szTestSbc	= "N";
		String szViewFlag2	= "";
		
		int querySize		= 0;
		
		List<String> list = new ArrayList<String>();

		try {			
			try {
				list = getNSRepositedDao.getTestSbc(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list != null && !list.isEmpty()) {
				querySize	= list.size();
				szTestSbc	= StringUtil.replaceNull(list.get(0), "N");
				
				if(szTestSbc.equals("Y"))	szViewFlag2 = "T";
				else						szViewFlag2 = "V";
				
			}else{
				szTestSbc = "N";
			}
			
			try{
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID994, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return szTestSbc;
	}
}
