package kr.co.wincom.imcs.api.getNSPurchased;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.api.getNSLists.GetNSListsRequestVO;
import kr.co.wincom.imcs.api.getNSReposited.GetNSRepositedDao;
import kr.co.wincom.imcs.api.getNSReposited.GetNSRepositedRequestVO;
import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.CateInfoVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetNSPurchasedServiceImpl implements GetNSPurchasedService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSPurchased");
	
	@Autowired
	private GetNSPurchasedDao getNSPurchasedDao;
	
	// 엔스크린 구매연동 여부 및 테스트 계정 확인 쿼리가 시청목록 API 쿼리를 사용함.
	@Autowired
	private GetNSRepositedDao getNSRepositedDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSPurchased(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	/**
	 * @author HONG
	 * @since 2016-06-21
	 * 구매내역 리스트 가져오기 (lgvod119.pc)
	 */
	@Override
	public GetNSPurchasedResultVO getNSPurchased(GetNSPurchasedRequestVO paramVO)	{
//		this.getNSPurchased(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		imcsLog.debugLog(methodName + " service call");

		GetNSPurchasedResultVO	resultListVO	= new GetNSPurchasedResultVO();
		GetNSPurchasedResponseVO tempVO			= new GetNSPurchasedResponseVO();
		List<GetNSPurchasedResponseVO> tempListVO	= new ArrayList<GetNSPurchasedResponseVO>();
		List<GetNSPurchasedResponseVO> resultList	= new ArrayList<GetNSPurchasedResponseVO>();
	
		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2
		
		String szImgSvrIp	= "";		// 이미지 서버 IP
		String szImgSvrUrl	= "";		// 이미지 서버 URL
		String msg			= "";
		
		String nSurtaxRate	= "";
		int resultSet		= 0;
		int iMainCount		= 0;
		
		int MAX_RES_CNT = 300;
		
		String[] nscArray = null;
		
		try {
			
			try {
				szImgSvrIp	= commonService.getIpInfo("img_server", ImcsConstants.API_PRO_ID119.split("/")[1]);			// 이미지서버 IP 조회
				szImgSvrUrl	= commonService.getIpInfo("img_resize_server", ImcsConstants.API_PRO_ID119.split("/")[1]);		// 이미지서버 URL 조회
			} catch(Exception e) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID119, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
				paramVO.setResultCode("31000000");
				
				throw new ImcsException();
			}
			
			tp1	= System.currentTimeMillis();
			//20180724 권형도 (가입자의 검수 여부 조회 초기화)
			String szTestSbc	= "Y";
			paramVO.setTestSbc(szTestSbc);
			
			if(paramVO.getNscListYn().equals("Y"))
			{
				// 엔스크린 구매연동 여부 및 테스트 계정 확인
				// getCustPairingChk 메소드 안에서 N_SA_ID, N_STB_MAC, TEST_SBC 값 세팅 함.
				resultSet = this.getCustPairingChk(paramVO);
				
				// nScreen(STB) 목록 조회 요청을 했는데 페이링이 되어 있지 않으면 바로 리턴
				if(resultSet != 0)
				{
					imcsLog.timeLog("페어링 정보 없음", String.valueOf(System.currentTimeMillis() - tp1), methodName, methodLine);
					
					tempVO.setContsType("N");
					tempVO.setEmpty(true);
					tempListVO.add(tempVO);
					resultListVO.setList(tempListVO);
					
					paramVO.setResultCode("31000000");
					
					return resultListVO;
				}
			} else { //2018.07.24 권형도 (lgvod119_20180628.c, line:589)
		    	//2018.06.27 - 엔스크린2차 nsc_list_yn이 N이라도 가입자의 검수여부를 조회
		    	//             일반사용자의 경우 양쪽 스크린 모두 검수 카테고리에만 편성된 것이 없어야하기 때문!
				szTestSbc	= this.getTestSbc(paramVO);
				paramVO.setTestSbc(szTestSbc);
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("검수 STB여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			}
			
			int iTotalCount = 0;
			
			if(paramVO.getContsGb().equals("NSC")){ //(lgvod119_20180628.c, line:607)
				// 1. 구매내역 리스트 조회
				tp1	= System.currentTimeMillis();
				tempListVO = this.getNSPurchasedListNsc(paramVO);
				
				if (tempListVO == null || tempListVO.size() ==0 ) { //구매내역 없는 경우
					
					if (paramVO.getNscListYn().equals("Y")) {
						resultListVO = new GetNSPurchasedResultVO();
						tempVO = new GetNSPurchasedResponseVO();
						tempListVO = new ArrayList<>();
						
						tempVO.setContsType("Y");
						tempVO.setEmpty(true);
						tempListVO.add(tempVO);
						resultListVO.setList(tempListVO);
						
						return resultListVO;
						
					} else {
						return resultListVO;
					}
				}
				
				tp2 = System.currentTimeMillis();
				imcsLog.timeLog("구매목록 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
				
				iMainCount = tempListVO.size();
				iTotalCount = Integer.parseInt(tempListVO.get(0).getTotalCnt());
				
				if(iTotalCount > MAX_RES_CNT) iTotalCount = MAX_RES_CNT;
				
				for(int i = 0; i < tempListVO.size(); i ++) {
					tempVO = tempListVO.get(i);
					
					tempVO.setImgUrl(szImgSvrIp);
					
					// 엔스크린 N_SA_ID, N_STB_MAC 세팅
					tempVO.setnSaId(paramVO.getnSaId());
					tempVO.setnStbMac(paramVO.getnStbMac());
					
					if(tempVO.getNscreenYn().length() > 2) {
						nscArray = tempVO.getNscreenYn().split(";");
						tempVO.setNscreenYn(nscArray[0]);
						
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
					
					// 구매타입 정의
					if(tempVO.getCpUseYn().equals("N"))			tempVO.setBuyingType("B");	// 일반구매
					else if(tempVO.getCpUseYn().equals("Y"))	tempVO.setBuyingType("C");	// 쿠폰구매
					else if(tempVO.getCpUseYn().equals("S"))	tempVO.setBuyingType("S");	// 소액결제
					else if(tempVO.getCpUseYn().equals("A"))	tempVO.setBuyingType("A");	// 인앱구매
					else if(tempVO.getCpUseYn().equals("K"))	tempVO.setBuyingType("K");	// 자사쿠폰구매
					else if(tempVO.getCpUseYn().equals("I"))	tempVO.setBuyingType("I");	// 타사쿠폰구매
					else tempVO.setBuyingType(tempVO.getCpUseYn());
					
					// 데이터 프리 구매타입 정의
					if(tempVO.getDatafreeCpUseYn().equals("N"))			tempVO.setDatafreeBuyingType("B");	// 일반구매
					else if(tempVO.getDatafreeCpUseYn().equals("Y"))	tempVO.setDatafreeBuyingType("C");	// 쿠폰구매
					else if(tempVO.getDatafreeCpUseYn().equals("S"))	tempVO.setDatafreeBuyingType("S");	// 소액결제
					else if(tempVO.getDatafreeCpUseYn().equals("A"))	tempVO.setDatafreeBuyingType("A");	// 인앱구매
					else if(tempVO.getDatafreeCpUseYn().equals("K"))	tempVO.setDatafreeBuyingType("K");	// 자사쿠폰구매
					else if(tempVO.getDatafreeCpUseYn().equals("I"))	tempVO.setDatafreeBuyingType("I");	// 타사쿠폰구매
					else tempVO.setDatafreeBuyingType(tempVO.getDatafreeCpUseYn());
				
					
					if(!"".equals(tempVO.getRealBuyingPrice()) && tempVO.getRealBuyingPrice() != null  ){
						
						// split 을 그냥 쓰면 1|2|| 이렇게 공백이 있는 경우 공백을 제거하고 2개만 반환함.
						// splitPreserveAllTokens 을 사용하면 1|| => 3개 반환, 1|2|3| => 4개 반환됨.
						String[] realBuyingPrice = StringUtils.splitPreserveAllTokens(tempVO.getRealBuyingPrice(), "\\|");
						
						if(realBuyingPrice.length >= 3){
							tempVO.setRealBuyingPrice(realBuyingPrice[2]);
						}
					}
						
					//}else{
						
						
					if("".equals(tempVO.getRealBuyingPrice()) || tempVO.getRealBuyingPrice() == null  ){	
						/*if( "B".equals(tempVO.getBuyingType()) || "W".equals(tempVO.getBuyingType()) || "T".equals(tempVO.getBuyingType()) ){
							tempVO.setRealBuyingPrice( Integer.toString( Integer.parseInt(tempVO.getBuyingPrice()) + Integer.parseInt(tempVO.getBuyingPrice())*10/100 ) );
						}else if( "K".equals(tempVO.getBuyingType()) || "I".equals(tempVO.getBuyingType()) || "H".equals(tempVO.getBuyingType()) ){
							tempVO.setRealBuyingPrice( Integer.toString( Integer.parseInt(tempVO.getBalace()) + Integer.parseInt(tempVO.getBalace())*10/100 ) );
						}else if( "A".equals(tempVO.getBuyingType()) ){
							tempVO.setRealBuyingPrice( tempVO.getBalace() );
						}*/
						
						if( "B".equals(tempVO.getBuyingType()) || "W".equals(tempVO.getBuyingType()) || "T".equals(tempVO.getBuyingType())){
							tempVO.setRealBuyingPrice( Integer.toString( Integer.parseInt(tempVO.getBuyingPrice()) + Integer.parseInt(tempVO.getBuyingPrice())*10/100 ) );
						}else if( "K".equals(tempVO.getBuyingType()) || "I".equals(tempVO.getBuyingType()) || "H".equals(tempVO.getBuyingType()) ){
							tempVO.setRealBuyingPrice( Integer.toString( Integer.parseInt(tempVO.getBalace()) + Integer.parseInt(tempVO.getBalace())*10/100 ) );
						}else if( "A".equals(tempVO.getBuyingType()) ){
							tempVO.setRealBuyingPrice( tempVO.getBalace() );
						}
					}
					
					if(!"".equals(tempVO.getRealDatafreeBuyingPrice()) && tempVO.getRealDatafreeBuyingPrice() != null  ){
						
						// split 을 그냥 쓰면 1|2|| 이렇게 공백이 있는 경우 공백을 제거하고 2개만 반환함.
						// splitPreserveAllTokens 을 사용하면 1|| => 3개 반환, 1|2|3| => 4개 반환됨.
						String[] realDatafreeBuyingPrice = 
								StringUtils.splitPreserveAllTokens(tempVO.getRealDatafreeBuyingPrice(), "\\|");
						
						if(realDatafreeBuyingPrice.length >= 3){
							tempVO.setRealDatafreeBuyingPrice(realDatafreeBuyingPrice[2]);
						}
					}
						
					//}else{
					
					if("".equals(tempVO.getRealDatafreeBuyingPrice()) || tempVO.getRealDatafreeBuyingPrice() == null  ){	
						if( "B".equals(tempVO.getDatafreeBuyingType()) || "W".equals(tempVO.getDatafreeBuyingType()) || "T".equals(tempVO.getDatafreeBuyingType()) ){
							tempVO.setRealDatafreeBuyingPrice( Integer.toString( Integer.parseInt(tempVO.getDatafreePrice()) + Integer.parseInt(tempVO.getDatafreePrice())*10/100 ) );
						}else if( "K".equals(tempVO.getDatafreeBuyingType()) || "I".equals(tempVO.getDatafreeBuyingType()) || "H".equals(tempVO.getDatafreeBuyingType()) ){
							tempVO.setRealDatafreeBuyingPrice( Integer.toString( Integer.parseInt(tempVO.getDatafreeBalace()) + Integer.parseInt(tempVO.getDatafreeBalace())*10/100 ) );
						}else if( "A".equals(tempVO.getDatafreeBuyingType()) ){
							tempVO.setRealDatafreeBuyingPrice( tempVO.getDatafreeBalace() );
						}
					}
					
					// 이미지 URL 입력
					tempVO.setImgServerUrl(szImgSvrUrl);	
					
					// 카테고리 명 조회
					String szCateName = "";
					if(tempVO.getCateId() != null && !tempVO.getCateId().trim().equals("")){
						paramVO.setCatId(tempVO.getCateId());

						szCateName = this.getCateName(paramVO);
						tempVO.setBelongingName(szCateName);
					}		
					
					// 카테고리 구분 정보 조회
					String szCatGb		= "";
					//if(tempVO.getContsId() != null && !tempVO.getContsId().trim().equals("")){
					if(tempVO.getCateId() != null && !tempVO.getCateId().trim().equals("")){
						paramVO.setContsId(tempVO.getContsId());
						
						if (paramVO.getNscListYn().equals("N") && tempVO.getNscreenYn().equals("Y") || paramVO.getNscListYn().equals("Y"))
						{
							List<CateInfoVO> cateinfo = this.getCateGbList2(paramVO);
							if (cateinfo != null && cateinfo.size() > 0 ) {
								tempVO.setIptvProdChk(cateinfo.get(0).getIptvProductType());
								tempVO.setNscProdChk(cateinfo.get(0).getNscProductType());
								tempVO.setIptvTestSbc(cateinfo.get(0).getIptvTestSbc());
								tempVO.setNscTestSbc(cateinfo.get(0).getNscTestSbc());
								tempVO.setCatGb(cateinfo.get(0).getCategoryName());
								
								tempVO.setCatGb1(cateinfo.get(0).getCateGb1());
								tempVO.setCatGb2(cateinfo.get(0).getCateGb2());
								tempVO.setCatGb3(cateinfo.get(0).getCateGb3());
								tempVO.setCatGb4(cateinfo.get(0).getCateGb4());
							}
						} else {
							//szCatGb = this.getCateGbList(paramVO);
							CateInfoVO cateInfoVO = this.getCateGbList(paramVO);
							if(cateInfoVO != null){
								szCatGb	= cateInfoVO.getCateGb1() + "\b" + cateInfoVO.getCateGb2() + "\b" + cateInfoVO.getCateGb3();
								tempVO.setCatGb1(cateInfoVO.getCateGb1());
								tempVO.setCatGb2(cateInfoVO.getCateGb2());
								tempVO.setCatGb3(cateInfoVO.getCateGb3());
								tempVO.setCatGb4(cateInfoVO.getCateGb4());

								// 모바일 구매목록 조회에서 엔스크린 여부가 N일 경우에는 아래 변수가 의미가 없어, 엔스크린 여부가 N으로 세팅되게 Default값 세팅
								tempVO.setIptvProdChk("0");
								tempVO.setNscProdChk("0");
								tempVO.setIptvTestSbc("Y");
								tempVO.setNscTestSbc("Y");
								tempVO.setCatGb(szCatGb);
							}else{
								szCatGb = "\b\b";
								tempVO.setCatGb(szCatGb);
							}

						}
					}
					
//					System.out.println("##############################:" + tempVO.getCatGb4());
//					System.out.println("##############################:" + tempVO.getCatGb1());
//					System.out.println("##############################:" + tempVO.getIptvProdChk());
//					System.out.println("##############################:" + tempVO.getNscProdChk());
//					System.out.println("##############################:" + paramVO.getTestSbc());
//					System.out.println("##############################:" + tempVO.getIptvTestSbc());
//					System.out.println("##############################:" + tempVO.getNscTestSbc());
					
					// 2018.06.04 - 엔스크린2차 : I30편성여부 체크 (2018.07.25 권형도) (lgvod119.c line:2703)
					if (paramVO.getNscListYn().equals("Y")) {
						if (
								!tempVO.getCatGb4().equals("I30") || 
								!tempVO.getCatGb1().equals("NSC") ||	
								tempVO.getIptvProdChk().equals("0") || 
								tempVO.getNscProdChk().equals("0") ||
								(paramVO.getTestSbc().equals("N") && 
								 (tempVO.getIptvTestSbc().equals("Y")||tempVO.getNscTestSbc().equals("Y")))
								)
						{
							iTotalCount--;
							continue;
						}
							
					} else if (paramVO.getNscListYn().equals("N")) {
						if (
								!tempVO.getCatGb4().equals("I30") || 
								!tempVO.getCatGb1().equals("NSC") ||	
								tempVO.getIptvProdChk().equals("0") || 
								tempVO.getNscProdChk().equals("0") ||
								(
									paramVO.getTestSbc().equals("N") && (tempVO.getIptvTestSbc().equals("Y")||tempVO.getNscTestSbc().equals("Y"))
								)
								)
						{
							tempVO.setNscreenYn("N");
						}
					}
					
					if(tempVO.getSerCatId() != null && !tempVO.getSerCatId().equals("")) {
						String szSerInfo 	= tempVO.getSerCatId();

						tempVO.setSerCatId(szSerInfo.substring(0, szSerInfo.indexOf("|")));
						tempVO.setSeriesNo(szSerInfo.substring(szSerInfo.indexOf("|") + 1, szSerInfo.length()));
					}
					
					if( i == 0){
						nSurtaxRate = this.getSurtaxRateInfo(paramVO);
					}
					
					tempVO.setSurtaxRate(nSurtaxRate);
						
					resultList.add(tempVO);
				}
				
				tp1	= System.currentTimeMillis();
				
				if(paramVO.getPossessionYn().equals("Y"))		msg	= "구매목록 조회(평생소장)";
				else if(paramVO.getPossessionYn().equals("N"))	msg	= "구매목록 조회(평생소장제외)";
				else											msg = "구매목록 조회";
				
				
				imcsLog.timeLog(msg, String.valueOf(tp1 - tp2), methodName, methodLine);
			}
			
			for (GetNSPurchasedResponseVO item : resultList) {
				item.setTotalCnt(String.valueOf(iTotalCount));
			}
			
			resultListVO.setList(resultList);
			
			if(iMainCount == 0 && paramVO.getNscListYn().equals("Y"))
			{
				resultListVO = new GetNSPurchasedResultVO();
				tempVO = new GetNSPurchasedResponseVO();
				resultList = new ArrayList<>();
				
				tempVO.setContsType("Y");
				tempVO.setEmpty(true);
				resultList.add(tempVO);
				resultListVO.setList(resultList);
				
				return resultListVO;
			}
			
		} catch(ImcsException ie) {
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
		} catch(Exception e) {
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		} finally{
			resultListVO.setResultCode(StringUtil.replaceNull(paramVO.getResultCode(), "20000000"));
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID119) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	
	
	/**
	 * 구매내역 리스트 조회
	 * @param 	GetNSPurchasedRequestVO paramVO
	 * @return  List<GetNSPurchasedResultVO>
	 **/
	public List<GetNSPurchasedResponseVO> getNSPurchasedListNsc(GetNSPurchasedRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();

		String sqlId	= "";
		
		// 각 케이스 별 SQL_ID 별도 구분 (주요 로직은 쿼리 내부에 존재)
		if(paramVO.getNscListYn().equals("N"))
		{
			if(paramVO.getPossessionYn().equals("Y"))
			{
				if(paramVO.getPageCnt().equals("A") || paramVO.getPageNo().equals("A"))
						sqlId	= "lgvod119_001_20180603_001";				// 평생소장 + 페이징X
				else 	sqlId	= "lgvod119_011_20180603_001";				// 평생소장 + 페이징O
			}
			else if(paramVO.getPossessionYn().equals("N"))
			{
				if(paramVO.getPageCnt().equals("A") || paramVO.getPageNo().equals("A"))
						sqlId	= "lgvod119_021_20180603_001";				// 평생소장X + 페이징X
				else	sqlId	= "lgvod119_031_20180603_001";				// 평생소장X + 페이징O
			}
			else
			{
				if(paramVO.getPageCnt().equals("A") || paramVO.getPageNo().equals("A")) 
						sqlId	= "lgvod119_041_20180603_001";				// 전체 + 페이징X
				else	sqlId	= "lgvod119_051_20180603_001";				// 전체 + 페이징O
			}
		}
		else // 2017-11-13 nScreen 구매 목록 조회 여부 추가
		{
			if(paramVO.getPossessionYn().equals("Y"))
			{
				if(paramVO.getPageCnt().equals("A") || paramVO.getPageNo().equals("A"))
				{
					if(paramVO.getTestSbc().equals("N"))
						sqlId = "lgvod119_s08_20180531_001";				// 평생소장 + 페이징X
					else
						sqlId = "lgvod119_s01_20180531_001";				// 평생소장 + 페이징X
				}
				else
				{
					if(paramVO.getTestSbc().equals("N"))
						sqlId = "lgvod119_s09_20180531_001";				// 평생소장 + 페이징O
					else
						sqlId = "lgvod119_s02_20180531_001";				// 평생소장 + 페이징O
				}
			}
			else if(paramVO.getPossessionYn().equals("N"))
			{
				if(paramVO.getPageCnt().equals("A") || paramVO.getPageNo().equals("A"))
				{
					if(paramVO.getTestSbc().equals("N"))
						sqlId = "lgvod119_s10_20180601_001";				// 평생소장 + 페이징X
					else
						sqlId = "lgvod119_s03_20180601_001";				// 평생소장 + 페이징X
				}
				else
				{
					if(paramVO.getTestSbc().equals("N"))
						sqlId = "lgvod119_s11_20180601_001";				// 평생소장 + 페이징O
					else
						sqlId = "lgvod119_s04_20180601_001";				// 평생소장 + 페이징O
				}
			}
			else
			{
				if(paramVO.getPageCnt().equals("A") || paramVO.getPageNo().equals("A"))
				{
					if(paramVO.getTestSbc().equals("N"))
						sqlId = "lgvod119_s12_20180601_001";				// 전체 + 페이징X
					else
						sqlId = "lgvod119_s05_20180601_001";				// 전체 + 페이징X
				}
				else
				{
					if(paramVO.getTestSbc().equals("N"))
						sqlId = "lgvod119_s13_20180601_001";				// 전체 + 페이징O
					else
						sqlId = "lgvod119_s06_20180601_001";				// 전체 + 페이징O
				}
			}
		}		
		
		int querySize = 0;

		List<GetNSPurchasedResponseVO> list = null;

		try {
			
			if(paramVO.getNscListYn().equals("N"))
				list = getNSPurchasedDao.getNSPurchasedListNsc(paramVO);
			else // 엔스크린
				list = getNSPurchasedDao.getNSPurchasedListNsc2(paramVO);
			
			if (list == null || list.isEmpty()) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID119, sqlId, null, "conts_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
				paramVO.setResultCode("21000000");
			} else {
				querySize = list.size();
			}
			
			try{
				//imcsLog.dbLog(ImcsConstants.API_PRO_ID119, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
		} catch (Exception e) {
			paramVO.setResultCode("41000000");
			//imcsLog.failLog(ImcsConstants.API_PRO_ID119, sqlId, null, "conts_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			throw new ImcsException();
		}

		return list;
	}


	/**
	 * 카테고리명 조회 (NoSql)
	 * @param 	GetNSPurchasedRequestVO paramVO
	 * @return  String
	 **/
	public String getCateName(GetNSPurchasedRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		
		String sqlId = "lgvod119_002_20171214_001";
				
		List<String> list = new ArrayList<String>();
		String szCateName	= "";
		
		try {
			try {
				list = getNSPurchasedDao.getCateName(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list == null || list.isEmpty()) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID119, "", null, "par_cat_name:Not Found", methodName, methodLine);
				//paramVO.setResultCode("21000000");
			} else {
				szCateName	= list.get(0);
			}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
//			imcsLog.failLog(ImcsConstants.API_PRO_ID119, sqlId, null, "par_cat:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			throw new ImcsException();
		}

		return szCateName;
	}


	/**
	 * 카테고리 구분 조회 (NoSql)
	 * @param 	GetNSPurchasedRequestVO paramVO
	 * @return  String
	 **/
	public CateInfoVO getCateGbList(GetNSPurchasedRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		
		String sqlId	= "lgvod119_003_20180611_001";
		
		List<CateInfoVO> list = null;
		CateInfoVO cateInfoVO = null;
		String szCatGb		= "";
		
		try {

			try {
				list = getNSPurchasedDao.getCateGbList(paramVO);
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list == null || list.isEmpty() ) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID119, "", null, "par_cat_name" + ImcsConstants.RCV_MSG2 , methodName, methodLine);
				paramVO.setResultCode("21000000");
			} else {
				//cateInfoVO = new CateInfoVO();
				
				if(list.get(0) != null){
					cateInfoVO	= list.get(0);
					szCatGb	= cateInfoVO.getCateGb1() + "\b" + cateInfoVO.getCateGb2() + "\b" + cateInfoVO.getCateGb3();
				}else{
					szCatGb = "\b\b\b";
				}
			}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.failLog(ImcsConstants.API_PRO_ID119, sqlId, null, "par_cate_gb:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			throw new ImcsException();
		}

		return cateInfoVO;
	}
	
	/**
	 * 카테고리 구분 조회 (NoSql)
	 * @param 	GetNSPurchasedRequestVO paramVO
	 * @return  String
	 **/
	public List<CateInfoVO> getCateGbList2(GetNSPurchasedRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		
		String sqlId	= "lgvod119_005_20180614_003";
		
		List<CateInfoVO> list = null;
		
		String szCatGb		= "";
		
		try {
			try {
				list = getNSPurchasedDao.getCateGbList2(paramVO);
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list == null || list.isEmpty() ) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID119, "", null, "par_cat_name" + ImcsConstants.RCV_MSG2 , methodName, methodLine);
				paramVO.setResultCode("21000000");
			} else {
				CateInfoVO cateInfoVO = new CateInfoVO();
				
				if(list.get(0) != null){
					cateInfoVO	= list.get(0);
					szCatGb	= cateInfoVO.getCateGb1() + "\b" + cateInfoVO.getCateGb2() + "\b" + cateInfoVO.getCateGb3();
				}else{
					szCatGb = "\b\b";
				}
				list.get(0).setCategoryName(szCatGb);
			}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
//			imcsLog.failLog(ImcsConstants.API_PRO_ID119, sqlId, null, "par_cate_gb:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			throw new ImcsException();
		}

		return list;
	}
	
	/**
	 * 부가세 요율 조회 (NoSql)
	 * @param 	
	 * @return  String
	 **/
	public String getSurtaxRateInfo(GetNSPurchasedRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		
		String sqlId = "lgvod119_004_20171214_001";
				
		List<String> list = new ArrayList<String>();
		String nSurtaxRate	= "";
		
		try {
			
			try {
				list = getNSPurchasedDao.getSurtaxRateInfo();
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if (list == null || list.isEmpty()) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID119, "", null, "SURTAXRATE INFO:Not Found", methodName, methodLine);				
			} else {
				nSurtaxRate	= list.get(0);
			}
			
			//paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
		} catch (Exception e) {
//			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
//			imcsLog.failLog(ImcsConstants.API_PRO_ID119, sqlId, null, "SURTAXRATE INFO:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			throw new ImcsException();
		}

		return nSurtaxRate;
	}
	
	
	/**
	 * 엔스크린 구매연동 여부 및 테스트 계정 확인
	 * 엔스크린 SA_ID, 엔스크린 STB_MAC, 테스트 계정 여부 값 세팅
	 * 
	 * @param paramVO
	 * @throws Exception
	 */
	public int getCustPairingChk(GetNSPurchasedRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
		
		// 시청목록 리스트와 쿼리가 같음.
		String sqlId = "lgvod119_s07_20171214_001";
				
		List<HashMap> list	= new ArrayList<HashMap>();
		
		int resultVal = -1;
		
		try {

			try {
				GetNSRepositedRequestVO nsRespositedVO = new GetNSRepositedRequestVO();
				nsRespositedVO.setSaId(paramVO.getSaId());
				nsRespositedVO.setStbMac(paramVO.getStbMac());
				
				// 시청목록 리스트와 쿼리가 같음.
				list = getNSRepositedDao.getCustPairingChk(nsRespositedVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if(list == null || list.isEmpty())
			{
				//imcsLog.failLog(ImcsConstants.API_PRO_ID119, "", null, "PAIRING 정보가 없습니다.", methodName, methodLine);
				paramVO.setResultCode("21000000");
				
				resultVal = -1;
			}
			else
			{
				paramVO.setnSaId((String)list.get(0).get("STB_SA_ID")); // 엔스크린 SA_ID
				paramVO.setnStbMac((String)list.get(0).get("STB_MAC")); // 엔스크린 STB_MAC
				paramVO.setTestSbc((String)list.get(0).get("TEST_SBC")); // 테스트 계정 여부
				
				resultVal = 0;
			}
			
		} catch (Exception e) {
//			if(cache.getLastException() != null) 	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
//			imcsLog.failLog(ImcsConstants.API_PRO_ID119, sqlId, null, "PAIRING_INFO:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
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
	public String getTestSbc(GetNSPurchasedRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 		= "lgvod994_001_20171214_001";
		String szTestSbc	= "N";
		String szViewFlag2	= "";
		
		int querySize		= 0;
		
		List<String> list = new ArrayList<String>();

		try {			
			
			try {
				list = getNSPurchasedDao.getTestSbc(paramVO);
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
			
			//paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return szTestSbc;
	}
	
}
