package kr.co.wincom.imcs.api.getFXContStat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.DateUtil;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComOfferVO;
import kr.co.wincom.imcs.common.vo.ComSvodVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetFXContStatServiceImpl implements GetFXContStatService {
	private Log imcsLogger		= LogFactory.getLog("API_getFXContStat");
	
	@Autowired
	private GetFXContStatDao getFXContStatDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getFXContStat(String szSaId, String szStbMac, String szPid){
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
	public GetFXContStatResultVO getFXContStat(GetFXContStatRequestVO paramVO)	{
//		this.getFXContStat(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		List<GetFXContStatResponseVO> resultVO		= new ArrayList<GetFXContStatResponseVO>();
		List<GetFXContStatResponseVO> returnVO		= new ArrayList<GetFXContStatResponseVO>();
		GetFXContStatResponseVO tempVO				= new GetFXContStatResponseVO();
		GetFXContStatResultVO	resultListVO		= new GetFXContStatResultVO();
		
		List<ComOfferVO> cpnVO = new ArrayList<ComOfferVO>();
		
		int nChkCnt		= 0;
		int nMainCnt	= 0;
		int nOfferCnt	= 0;
		
		int nResultSet	= 0;
		
		long tp1, tp2	= 0;
		long tp_start	= paramVO.getTp_start();
		
		String msg		= "";
		
		
		try {
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp1 - tp_start), methodName, methodLine);
			
			tempVO.setPremiumYn("N");
			paramVO.setCurrentDate(commonService.getSysdate());
			
			if("M20120905000".equals(paramVO.getSaId())) {
				tempVO.setContsId(paramVO.getAlbumId());
				tempVO.setContsType("1");
				tempVO.setBuyYn("1");
				tempVO.setBillType("1");
				tempVO.setExpireTime("48");
				tempVO.setSalePrice("0");
				tempVO.setEventValue("0");
				tempVO.setTerrYn("0");
				tempVO.setPrice("0");
				tempVO.setSubsYn("1");
				
				returnVO.add(tempVO);
				nResultSet = -1;
			} 
			
			
			if(nResultSet == 0) {
				nChkCnt	= this.getImcsProdId(paramVO);
			
				if(nChkCnt == 0) {
					tempVO.setContsId(paramVO.getAlbumId());
					tempVO.setContsType("1");
					tempVO.setBuyYn("1");
					tempVO.setBillType("1");
					tempVO.setExpireTime("48");
					tempVO.setSalePrice("0");
					tempVO.setEventValue("0");
					tempVO.setTerrYn("0");
					tempVO.setPrice("0");
					tempVO.setSubsYn("1");
					
					returnVO.add(tempVO);
					nResultSet	 = -1;
				}
			}
			
			if(nResultSet == 0) {
				// 가입자 정보 조회 (검수 STB여부, B2B사용자 여부 조회)
				String szB2bCustomerYn	= "";
				
				HashMap<String, String> mTestSbc = new HashMap<String, String>();
				mTestSbc	= this.getTestSbc(paramVO);
				
				if(mTestSbc != null){
					paramVO.setTestSbc(StringUtil.replaceNull(mTestSbc.get("TEST_SBC"), "N"));
					szB2bCustomerYn	= StringUtil.replaceNull(mTestSbc.get("VOD_USE_YN"), "N");
				} else {
					paramVO.setTestSbc("N");
					szB2bCustomerYn	= "N";
				}
				
				
				// 프리미엄 여부 조회
				nMainCnt	= this.getPremiumYn(paramVO);
				if(nMainCnt > 0)	paramVO.setPremiumYn("Y");
				
				// 요금제 가입상품정보 조회
				resultVO	= this.getProdInfo(paramVO);
				tp1	= System.currentTimeMillis();
				imcsLog.timeLog("컨텐츠정보 조회", String.valueOf(tp1 - tp2), methodName, methodLine);
				
				if(resultVO != null)	nMainCnt	= resultVO.size();
				else					nMainCnt	= 0;
				
				for(int i = 0; i < nMainCnt; i++) {
					tempVO	= resultVO.get(i);
					tempVO.setB2bCustomerYn(szB2bCustomerYn);
					tempVO.setPremiumYn(paramVO.getPremiumYn());
					
					paramVO.setResultSet(0);
					paramVO.setContsId(tempVO.getContsId());
					
					GetFXContStatResponseVO contTypeVO = new GetFXContStatResponseVO();
					contTypeVO	= this.getContTypeInfo(paramVO);
					
					if(contTypeVO != null) {
						tempVO.setBillType(contTypeVO.getBillType());
						tempVO.setContsType(contTypeVO.getContsType());
						tempVO.setSalePrice(contTypeVO.getSalePrice());
						tempVO.setEventValue(contTypeVO.getEventValue());
						tempVO.setEventYn(contTypeVO.getEventYn());
						tempVO.setExpireTime(contTypeVO.getExpireTime());
						tempVO.setTerrYn(contTypeVO.getTerrYn());
						tempVO.setTerrEdDate(contTypeVO.getTerrEdDate());
						tempVO.setLicensingWindowEnd(contTypeVO.getLicensingWindowEnd());
						tempVO.setPrice(contTypeVO.getPrice());
						tempVO.setSubsProdId(contTypeVO.getSubsProdId());
						tempVO.setSubsProdName(contTypeVO.getSubsProdName());
						tempVO.setSubsProdPrice(contTypeVO.getSubsProdPrice());
						tempVO.setSubsProdSub(contTypeVO.getSubsProdSub());
						tempVO.setCpnNouseYn(contTypeVO.getCpnNouseYn());
						tempVO.setSubsProdIsu(contTypeVO.getSubsProdIsu());
						tempVO.setSubsProdType(contTypeVO.getSubsProdType());
						tempVO.setAlbumType(contTypeVO.getAlbumType());
						tempVO.setPvodProdName(contTypeVO.getPvodProdName());
						tempVO.setPvodProdDesc(contTypeVO.getPvodProdDesc());
						tempVO.setUflixProdYn(contTypeVO.getUflixProdYn());
						
						tempVO.setBuyYn(contTypeVO.getBuyYn());
						tempVO.setBuyingDate(contTypeVO.getBuyingDate());
					}
					
				
					paramVO.setPpmYn("0");
					
					
					this.getPpmTypeInfo(paramVO, tempVO);
										
					
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("컨텐츠타입, 금액, 지상파여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
					
					if("1".equals(paramVO.getShortYn()) && "1".equals(paramVO.getPkgYn()) && ("1".equals(paramVO.getSvodYn()) || "1".equals(paramVO.getPpmYn())))
						tempVO.setBuyText("해당 프로그램은 단편/시리즈 구매 및\\n월정액 가입이 가능한 상품입니다.\\n구매 또는 가입하실 상품을 선택하세요");
					
					if("1".equals(paramVO.getShortYn()) && "1".equals(paramVO.getPkgYn()) && (!"1".equals(paramVO.getSvodYn()) && !"1".equals(paramVO.getPpmYn())))
						tempVO.setBuyText("해당 프로그램은 단편/시리즈\\n구매가 가능한 상품입니다.\\n구매하실 상품을 선택하세요");
					
					if("1".equals(paramVO.getShortYn()) && !"1".equals(paramVO.getPkgYn()) && ("1".equals(paramVO.getSvodYn()) || "1".equals(paramVO.getPpmYn())))
						tempVO.setBuyText("해당 프로그램은 단편구매 및\\n월정액 가입이 가능한 상품입니다.\\n구매 또는 가입하실 상품을 선택하세요.");
					
					if(!"1".equals(paramVO.getShortYn()) && "1".equals(paramVO.getPkgYn()) && ("1".equals(paramVO.getSvodYn()) || "1".equals(paramVO.getPpmYn())))
						tempVO.setBuyText("해당 프로그램은 시리즈 구매 및\\n월정액 가입이 가능한 상품입니다.\\n구매 또는 가입하실 상품을 선택하세요.");
					
					
					
					if("1".equals(paramVO.getSvodYn())) {
						if("1".equals(paramVO.getSvodYn())) {
							if(paramVO.getDataSubChk() == 1)	tempVO.setSubsYn("0");
							else								tempVO.setSubsYn("1");
						}
					} 
					
				
					if("Y".equals(tempVO.getCpnNouseYn())) {
						msg = " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID151) + "] msg[쿠폰사용 불가 컨텐츠]";
						imcsLog.serviceLog(msg, methodName, methodLine);
					}
					// 쿠폰정보 조회
					else if(!"Y".equals(tempVO.getCpnNouseYn()) && nOfferCnt == 0){
						cpnVO = this.getCpnInfo(paramVO);
						
						nOfferCnt++;
						
						tp1	= System.currentTimeMillis();
						imcsLog.timeLog("가입자 쿠폰 정보 조회", String.valueOf(tp1 - tp2), methodName, methodLine);
					}
					
					returnVO.add(tempVO);

				}
			}
			
			
			resultListVO.setCpnInfo(cpnVO);
			resultListVO.setList(returnVO);
			resultListVO.setResultSet(nResultSet);
		} catch(ImcsException ie) {
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw ie;
		} catch(Exception e) {
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		} finally{
			msg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID151) + "] rtn[" + resultListVO.toString() + "|]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID151) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	
	
	
	
	/**
	 * PPM 정보를 가져오는 주요 로직
	 * @param paramVO
	 * @return
	 * @throws Exception
	 */
	public void getPpmTypeInfo(GetFXContStatRequestVO paramVO, GetFXContStatResponseVO tempVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		int nPPMCnt = 0;
		int nSubCnt	= 0;
		
		List<HashMap<String, String>> lstProdInfo = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> mProdInfo = null;
		
		lstProdInfo	= this.getProdInfo2(paramVO);
		if(lstProdInfo != null && !lstProdInfo.isEmpty())	nPPMCnt	= lstProdInfo.size();
		
		paramVO.setSvodProdBuyYn("1");		// 미가입초기화
		
		
		List<GetFXContStatResponseVO> list	= new ArrayList<GetFXContStatResponseVO>();
		GetFXContStatResponseVO resultVO = null;

		for(int j = 0; j < nPPMCnt; j++) {
			mProdInfo = lstProdInfo.get(j);
			
			if(j == 0)
			{
				for(int k = 0 ; k < nPPMCnt ; k++)
				{
					// 가입체크, 여러 상품 중 하나라도 가입되어 있으면 모든 가입여부를 가입으로 전달한다. 
					paramVO.setPpmProdId(lstProdInfo.get(k).get("IMCS_PROD_ID"));
					this.getPpmProdYn(paramVO);
				}
			}
			
			// 2020.06.29 - 가입중일 때 영화월정액 노출 처리
			if(paramVO.getSvodProdBuyYn().equals("0"))
			{
				if(mProdInfo.get("IMCS_VIEW_CTRL").equals("1") || mProdInfo.get("IMCS_VIEW_CTRL").equals("3")) continue;				
			}
			// 2020.06.29 - 미가입중일 때 영화월정액 노출 처리
			else
			{
				if(mProdInfo.get("IMCS_VIEW_CTRL").equals("1") || mProdInfo.get("IMCS_VIEW_CTRL").equals("2")) continue;
			}
						
			
			// 상품 코드 정보 조회 (PPM)
			paramVO.setPpmProdId(mProdInfo.get("IMCS_PROD_ID"));
			paramVO.setPpmSubYn(mProdInfo.get("PROD_SUB_YN"));					
			
			// PPM 컨텐츠 정보 조회
			list	= this.getPpmType(paramVO);
			
			if(list == null || list.isEmpty()) break;
			
			
			paramVO.setPpmYn("1");
			resultVO	= list.get(0);
			
			if("Y".equals(paramVO.getPpmSubYn())) {
				List<ComSvodVO> lstSvodInfo = new ArrayList<ComSvodVO>();
				ComSvodVO svodInfoVO = null;
				
				try {
					paramVO.setSvodProdId(resultVO.getSubsProdId());
					lstSvodInfo	= getFXContStatDao.getSvodInfo(paramVO);
					
				} catch(Exception e) {
//					imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID151, "", null, "buy_info1_fvod:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
				}
				
				if(lstSvodInfo != null && !lstSvodInfo.isEmpty())	nSubCnt = lstSvodInfo.size();
				
				if(nSubCnt > 0) {
					svodInfoVO = lstSvodInfo.get(0);
					
					resultVO.setSubsProdId(svodInfoVO.getSvodProdId());
					resultVO.setSubsProdName(svodInfoVO.getSvodProdName());
					resultVO.setSubsProdPrice(svodInfoVO.getSvodProdPrice());
					resultVO.setSubsProdSub(svodInfoVO.getSvodProdDesc());
					resultVO.setSubsProdIsu(svodInfoVO.getSvodProdIsuYn());
					resultVO.setSubsProdType(svodInfoVO.getSvodProdIsuType());
				}
			}
			
			
			if("".equals(tempVO.getContsType())){
				tempVO.setBillType(resultVO.getBillType());
				tempVO.setContsType(resultVO.getContsType());
				tempVO.setExpireTime(resultVO.getExpireTime());
			}else{
				
				if("".equals(tempVO.getBillType()))	tempVO.setBillType(resultVO.getBillType());
				else									tempVO.setBillType(tempVO.getBillType() + "\b" + resultVO.getBillType());
				
				if("".equals(tempVO.getContsType()))	tempVO.setContsType(resultVO.getContsType());
				else									tempVO.setContsType(tempVO.getContsType() + "\b" + resultVO.getContsType());
				
				if("".equals(tempVO.getContsType()))	tempVO.setExpireTime(resultVO.getExpireTime());
				else									tempVO.setExpireTime(tempVO.getExpireTime() + "\b" + resultVO.getExpireTime());
				
			}
			
			
			
			if(j == 0) {
				tempVO.setSubsYn(paramVO.getSvodProdBuyYn());
				tempVO.setSubsProdId(resultVO.getSubsProdId());
				tempVO.setSubsProdName(resultVO.getSubsProdName());
				tempVO.setSubsProdPrice(resultVO.getSubsProdPrice());
				tempVO.setSubsProdSub(resultVO.getSubsProdSub());
				tempVO.setSubsProdIsu(resultVO.getSubsProdIsu());
				tempVO.setSubsProdType(resultVO.getSubsProdType());
			} else {
				if(tempVO.getSubsYn().isEmpty()) // 첫번째 row는 아니지만 데이터가 들어간게 없다면, 최초 등록
				{
					tempVO.setSubsYn(paramVO.getSvodProdBuyYn());
					tempVO.setSubsProdId(resultVO.getSubsProdId());
					tempVO.setSubsProdName(resultVO.getSubsProdName());
					tempVO.setSubsProdPrice(resultVO.getSubsProdPrice());
					tempVO.setSubsProdSub(resultVO.getSubsProdSub());
					tempVO.setSubsProdIsu(resultVO.getSubsProdIsu());
					tempVO.setSubsProdType(resultVO.getSubsProdType());
				}
				else
				{
					tempVO.setSubsYn(tempVO.getSubsYn() + "\b" + paramVO.getSvodProdBuyYn());
					tempVO.setSubsProdId(tempVO.getSubsProdId() + "\b" + resultVO.getSubsProdId());
					tempVO.setSubsProdName(tempVO.getSubsProdName() + "\b" + resultVO.getSubsProdName());
					tempVO.setSubsProdPrice(tempVO.getSubsProdPrice() + "\b" + resultVO.getSubsProdPrice());
					tempVO.setSubsProdSub(tempVO.getSubsProdSub() + "\b" + resultVO.getSubsProdSub());
					tempVO.setSubsProdIsu(tempVO.getSubsProdIsu() + "\b" + resultVO.getSubsProdIsu());
					tempVO.setSubsProdType(tempVO.getSubsProdType() + "\b" + resultVO.getSubsProdType());
				}
			}
		}
		
		paramVO.setResultSet(0);
	}
	
	
	
	/**
	 * 컨텐츠 코드 조회 (PPM)
	 * @param paramVO
	 * @return
	 * @throws Exception
	 */
	public List<String> getPpmProdYn(GetFXContStatRequestVO paramVO) throws Exception {
	   	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod151_061_20171214_001";
		
		List<String> list	= new ArrayList<String>();
		
		try {
			try {
				list = getFXContStatDao.getPpmProdYn(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if(list != null && !list.isEmpty()) 
				paramVO.setSvodProdBuyYn("0");
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID151, sqlId, cache, "ppm_yn:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return list;
	}
	
	
	
	
	
	/**
	 * 컨텐츠 타입정보를 가져오는 주요 로직
	 * @param paramVO
	 * @return
	 * @throws Exception
	 */
	public GetFXContStatResponseVO getContTypeInfo(GetFXContStatRequestVO paramVO) throws Exception {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	

		List<GetFXContStatResponseVO> list	= new ArrayList<GetFXContStatResponseVO>();
		GetFXContStatResponseVO resultVO = null;
		GetFXContStatResponseVO returnVO = new GetFXContStatResponseVO();
		
		paramVO.setDataSubChk(0);
		
		int nSubCnt		= 0;
		int nSvodCnt	= 0;
		int nTotalCnt 		= 0;
		int resultSet		= 0;
		Integer nDataChk	= 0;
		
		String szMsg		= "";
				
		list	= this.getContType(paramVO);
		resultSet	= paramVO.getResultSet();
		
		if(resultSet == -1 || list == null || list.isEmpty())	return null;
		else													nSubCnt	= list.size();
		
		
		for (int i= 0; i < nSubCnt; i++) {
			resultVO = list.get(i);
			
			// 구매일, 현재일에 대한 날짜 계산 로직 적용 예정 (변수 등 정리필요)
			DateUtil dateUtil = new DateUtil();
			Date dExpiredDate = dateUtil.getStringToDate(resultVO.getExpiredDate(), "yyyyMMdd");
			Date dCurrentTime = dateUtil.getStringToDate(paramVO.getCurrentDate(), "yyyyMMddHHmmss");
			
			int nExpiredYn = dExpiredDate.compareTo(dCurrentTime);
			if(nExpiredYn < 0)	continue;		// 현재 시간이 만료일보다 크면 Y
			
			paramVO.setContsType(resultVO.getContsType());
			if("3".equals(resultVO.getContsType())) {
				
				paramVO.setSvodYn("1");
				
				// 상품 코드 정보 조회
				List<String> lstProdCd = this.getProdCd(paramVO);
				if(lstProdCd == null || lstProdCd.isEmpty())
					lstProdCd = this.getProdCd2(paramVO);
				
			
				if(lstProdCd != null && !lstProdCd.isEmpty()) 	paramVO.setDataSubChk(lstProdCd.size());
				
				paramVO.setSvodProdId(resultVO.getSubsProdId());
				paramVO.setSvodYn("1");
				
				if(nTotalCnt == 0) {
					returnVO.setBillType(resultVO.getBillType());
					returnVO.setContsType(resultVO.getContsType());
					returnVO.setExpireTime(resultVO.getExpireTime());
				} else {
					returnVO.setBillType(returnVO.getBillType() + "\b" + resultVO.getBillType());
					returnVO.setContsType(returnVO.getContsType() + "\b" + resultVO.getContsType());
					returnVO.setExpireTime(returnVO.getExpireTime() + "\b" + resultVO.getExpireTime());
				}
				
				
				
				List<ComSvodVO> lstSvodInfo = new ArrayList<ComSvodVO>();
				ComSvodVO svodInfoVO = new ComSvodVO();
				
				//lstSvodInfo	= this.getSvodInfo(paramVO);
				/*resultSet	= paramVO.getResultSet();
				if(resultSet == -1) {
					return null;
				}*/
				
				//if(lstSvodInfo != null && !lstSvodInfo.isEmpty())	nSvodCnt = lstSvodInfo.size();
				nSvodCnt = paramVO.getDataSubChk();
				
				for(int j = 0; j < nSvodCnt; j++) {
					//svodInfoVO = lstSvodInfo.get(j);
					
					if(j == 0) {
						returnVO.setSubsProdId(svodInfoVO.getSvodProdId());
						returnVO.setSubsProdName(svodInfoVO.getSvodProdName());
						returnVO.setSubsProdPrice(svodInfoVO.getSvodProdPrice());
						returnVO.setSubsProdSub(svodInfoVO.getSvodProdDesc());
						returnVO.setSubsProdIsu(svodInfoVO.getSvodProdIsuYn());
						returnVO.setSubsProdType(svodInfoVO.getSvodProdIsuType());
					} else {
						returnVO.setSubsProdId(returnVO.getSubsProdId() + "\b" + svodInfoVO.getSvodProdId());
						returnVO.setSubsProdName(returnVO.getSubsProdName() + "\b" + svodInfoVO.getSvodProdName());
						returnVO.setSubsProdPrice(returnVO.getSubsProdPrice() + "\b" + svodInfoVO.getSvodProdPrice());
						returnVO.setSubsProdSub(returnVO.getSubsProdSub() + "\b" + svodInfoVO.getSvodProdDesc());
						returnVO.setSubsProdIsu(returnVO.getSubsProdIsu() + "\b" + svodInfoVO.getSvodProdIsuYn());
						returnVO.setSubsProdType(returnVO.getSubsProdType() + "\b" + svodInfoVO.getSvodProdIsuType());
					}
				}
				
			} else {
				if(nTotalCnt == 0) {
					returnVO.setBillType(resultVO.getBillType());
					returnVO.setContsType(resultVO.getContsType());
					returnVO.setExpireTime(resultVO.getExpireTime());
				} else {
					returnVO.setBillType(returnVO.getBillType() + "\b" + resultVO.getBillType());
					returnVO.setContsType(returnVO.getContsType() + "\b" + resultVO.getContsType());
					returnVO.setExpireTime(returnVO.getExpireTime() + "\b" + resultVO.getExpireTime());
				}
			}
			
			if(nTotalCnt == 0) {
				returnVO.setSalePrice(resultVO.getSalePrice());
				returnVO.setEventValue(resultVO.getEventValue());
				returnVO.setEventYn(resultVO.getEventYn());
				returnVO.setTerrYn(resultVO.getTerrYn());
				returnVO.setTerrEdDate(resultVO.getTerrEdDate());
				returnVO.setLicensingWindowEnd(resultVO.getLicensingWindowEnd());
				returnVO.setPrice(resultVO.getPrice());
				returnVO.setCpnNouseYn(resultVO.getCpnNouseYn());
				returnVO.setAlbumType(resultVO.getAlbumType());
				
				if("3".equals(resultVO.getContsType()))		paramVO.setSvodOnly("1");
				else 										paramVO.setSvodOnly("0");
				
				
				if("0".equals(paramVO.getSvodOnly())) {
					HashMap<String, Object> mDupChk	= new HashMap<String, Object>();
					
					try {
						mDupChk = this.getBuyDupChk(paramVO);
					} catch(Exception e) {
						paramVO.setResultSet(-1);
						return null;
					}
					
					nDataChk	= (Integer) mDupChk.get("DATA_CHK");
					if(nDataChk == null) nDataChk = 0;
					
					if(nDataChk > 0) {
						returnVO.setBuyYn("0");
						returnVO.setBuyingDate(StringUtil.nullToSpace((String) mDupChk.get("BUY_DATE")));
					} else {
						returnVO.setBuyYn("1");
						returnVO.setBuyingDate("");
					}
					
					// PPV 이면서 이벤트 대상인 경우
					if("1".equals(resultVO.getContsType()) &&  "1".equals(resultVO.getEventYn())) {
						
						Integer nEventChk		= 0;
						Integer nEventDupChk	= 0;
						
						// 이벤트 체크
						try {
							if(nEventChk == null)		nEventChk = 0;
							nEventChk		= getFXContStatDao.getEventChk(paramVO);
							
						} catch(Exception e) {
//							imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID151, "fxvod151_f10", null, "event_chk:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
							paramVO.setResultSet(-1);
							
							return null;
						}
						
						// 이벤트 상품 구매내역 체크
						try {
							nEventDupChk	= getFXContStatDao.getEventDupChk(paramVO);
							
							if(nEventDupChk == null)	nEventDupChk = 0;
						} catch(Exception e) {
//							imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID151, "fxvod151_f20", null, "event_buy_chk:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
							paramVO.setResultSet(-1);
							
							return null;
						}
						
						
						if(nEventChk == 0 || nEventDupChk == 0) {
							szMsg = " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID151) + "] 1회 무료 대상";
							imcsLog.serviceLog(szMsg, methodName, methodLine);
						} else {
							returnVO.setSalePrice("0");
						}
					} else {
						returnVO.setSalePrice("0");
					}
				}
				
				// 가격정보 저장
				if("0".equals(resultVO.getContsType()))		returnVO.setPrice("0");
				else										returnVO.setPrice(resultVO.getPrice());
				
				
				// 
				if("2".equals(resultVO.getContsType())) {
					paramVO.setPkgYn("1");
					
					returnVO.setPvodProdName(resultVO.getSubsProdName());
					returnVO.setPvodProdDesc(resultVO.getPvodProdDesc());
				}
				
				if("1".equals(resultVO.getContsType())) {
					paramVO.setShortYn("1");
				}
			}
			
			// 첫번째 ROW가 아닌 경우
			else {
				if("3".equals(resultVO.getContsType())) {}
				else {
					if("0".equals(paramVO.getSvodOnly())) {
						HashMap<String, Object> mDupChk	= new HashMap<String, Object>();
						
						try {
							mDupChk = this.getBuyDupChk(paramVO);
						} catch(Exception e) {
							paramVO.setResultSet(-1);
							return null;
						}
						
						nDataChk	= (Integer) mDupChk.get("DATA_CHK");
						if(nDataChk == null) nDataChk = 0;
						
						if(nDataChk > 0) {
							returnVO.setBuyYn(returnVO.getBuyYn() + "\b0");
							returnVO.setBuyingDate(returnVO.getBuyingDate() + "\b" + StringUtil.nullToSpace((String) mDupChk.get("BUY_DATE")));
						} else {
							returnVO.setBuyYn(returnVO.getBuyYn() + "\b1");
							returnVO.setBuyingDate(returnVO.getBuyingDate() + "\b");
						}
						
						// PPV 이면서 이벤트 대상인 경우
						if("1".equals(resultVO.getContsType()) &&  "1".equals(resultVO.getEventYn())) {
							
							Integer nEventChk		= 0;
							Integer nEventDupChk	= 0;
							
							// 이벤트 체크
							try {
								nEventChk		= getFXContStatDao.getEventChk(paramVO);
								
								if(nEventChk == null)		nEventChk = 0;
							} catch(Exception e) {
//								imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID151, "fxvod151_f10", null, "event_chk:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
								paramVO.setResultSet(-1);
								
								return null;
							}
							
							// 이벤트 상품 구매내역 체크
							try {
								nEventDupChk	= getFXContStatDao.getEventDupChk(paramVO);
								
								if(nEventDupChk == null)	nEventDupChk = 0;
							} catch(Exception e) {
//								imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID151, "fxvod151_f20", null, "event_buy_chk:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
								paramVO.setResultSet(-1);
								
								return null;
							}
							
							
							if(nEventChk == 0 || nEventDupChk == 0) {
								szMsg = " svc[" + String.format("%-20s", ImcsConstants.FXAPI_PRO_ID151) + "] 1회 무료 대상";
								imcsLog.serviceLog(szMsg, methodName, methodLine);
							} else {
								returnVO.setSalePrice("0");
							}
						} else {
							returnVO.setSalePrice("0");
						}
					}
					
					// 가격정보 저장
					if("0".equals(resultVO.getContsType())) returnVO.setPrice(returnVO.getPrice() + "\b0");
					else									returnVO.setPrice(returnVO.getPrice() + "\b" + resultVO.getPrice());
				}
				
				if("2".equals(resultVO.getContsType())) {
					paramVO.setPkgYn("1");
					
					returnVO.setPvodProdName(resultVO.getSubsProdName());
					returnVO.setPvodProdDesc(resultVO.getPvodProdDesc());
				}
				
				if("1".equals(resultVO.getContsType())) {
					paramVO.setShortYn("1");
				}
			}
			
			nTotalCnt++;
		}
		

		return returnVO;
	}
	
	
	
	
	/**
	 * 컨텐츠 코드 조회2
	 * @param paramVO
	 * @return
	 * @throws Exception
	 */
	public List<String> getProdCd2(GetFXContStatRequestVO paramVO) throws Exception {
	   	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod151_032_20171214_001";
		
		List<String> list	= new ArrayList<String>();
		
		try {
			try {
				list = getFXContStatDao.getProdCd2(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID151, sqlId, cache, "cus_prod_chk:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return list;
	}
	
	
	
	
	/**
	 * 컨텐츠 코드 조회
	 * @param paramVO
	 * @return
	 * @throws Exception
	 */
	public List<String> getProdCd(GetFXContStatRequestVO paramVO) throws Exception {
	   	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod151_031_20171214_001";
		
		List<String> list	= new ArrayList<String>();
		
		try {
			try {
				list = getFXContStatDao.getProdCd(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID151, sqlId, cache, "cus_prod_chk:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return list;
	}
	

	/**
	 * 컨텐츠 타입 정보 조회
	 * @param paramVO
	 * @return
	 * @throws Exception
	 */
	public List<GetFXContStatResponseVO> getContType(GetFXContStatRequestVO paramVO) throws Exception {
	   	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod151_021_20171214_001";
		
		List<GetFXContStatResponseVO> list	= new ArrayList<GetFXContStatResponseVO>();
		
		try {
			try {
				list = getFXContStatDao.getContType(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list == null || list.isEmpty()) {
				paramVO.setResultSet(-1);
				
//				imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID151, sqlId, cache, "conts_type:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			}
			
		} catch (Exception e) {
			paramVO.setResultSet(-1);
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID151, sqlId, cache, "conts_type:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		return list;
	}

	
	
	
	/**
	 * 쿠폰정보 조회
	 * @param paramVO
	 * @return
	 * @throws Exception
	 */
	public List<ComOfferVO> getCpnInfo(GetFXContStatRequestVO paramVO) throws Exception {
	   	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod151_080_20171214_001";

		List<ComOfferVO> list	= new ArrayList<ComOfferVO>();
		
		try {
			list = getFXContStatDao.getCpnInfo(paramVO);
			
			if (list == null || list.isEmpty()) {
				paramVO.setResultSet(-1);
				
//				imcsLog.dbLog(ImcsConstants.FXAPI_PRO_ID151, sqlId, null, list.size(), methodName, methodLine);
			}
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID151, sqlId, null, "conts_type:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return list;
	}

	
	
	
	/**
	 * 상품여부 조회
	 * @param 	GetFXContStatRequestVO paramVO
	 * @return  String
	 **/
	@SuppressWarnings("rawtypes")
	public List<HashMap<String, String>> getProdInfo2(GetFXContStatRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod151_050_20171214_001";
		
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		
		try {
			try {
				list = getFXContStatDao.getProdInfo2(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
			paramVO.setResultSet(-1);
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID151, sqlId, cache, "multi_svod:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return list;
	}
	
	
	

	/**
	 * 컨텐츠정보 조회
	 * @param 	GetFXContStatRequestVO paramVO
	 * @return  List<GetFXContStatResponseVO>
	 **/
	public List<GetFXContStatResponseVO> getProdInfo(GetFXContStatRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod151_010_20171214_001";
		
		List<GetFXContStatResponseVO> list	= new ArrayList<GetFXContStatResponseVO>();
		int querySize = 0;
		
		try {
			try {
				list = getFXContStatDao.getProdInfo(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list == null || list.isEmpty()) {
//				imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID151, sqlId, cache, "cont_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			} else {
				querySize	= list.size();
			}
			
			try{
//				imcsLog.dbLog(ImcsConstants.FXAPI_PRO_ID151, sqlId, cache, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID151, sqlId, cache, "cont_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return list;
	}
	
	
	
	
	/**
	 * 프리미엄 여부 조회
	 * @param 	GetFXContStatRequestVO paramVO
	 * @return  String
	 **/
	@SuppressWarnings("rawtypes")
	public int getPremiumYn(GetFXContStatRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod151_062_20171214_001";
		
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		
		int querySize	= 0;
		
		try {
			try {
				list = getFXContStatDao.getPremiumYn(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
//			imcsLog.dbLog(ImcsConstants.FXAPI_PRO_ID151, sqlId, cache, querySize, methodName, methodLine);
			
			if(list != null && list.size() > 0) { 
				querySize	= list.size();
			} else {
		//		paramVO.setResultSet(-1);
//				imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID151, "", cache, "conts_type:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			}
			
		} catch (Exception e) {
		//	paramVO.setResultSet(-1);
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID151, sqlId, cache, "conts_type:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return querySize;
	}
	
	
	/**
	 * 검수 STB 여부 조회
	 * @param 	GetFXContStatRequestVO paramVO
	 * @return  String
	 **/
	@SuppressWarnings("rawtypes")
	public HashMap<String, String> getTestSbc(GetFXContStatRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod151_002_20171214_001";
		
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> mResultVO = null;
		
		int querySize	= 0;
		
		try {
			try {
				list = getFXContStatDao.getTestSbc(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if(list != null && list.size() > 0) { 
				mResultVO	= list.get(0);
				querySize	= list.size();
			}

//			imcsLog.dbLog(ImcsConstants.FXAPI_PRO_ID151, sqlId, cache, querySize, methodName, methodLine);
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}

		return mResultVO;
	}
	
	
	
	
	/**
	 * IMCS 상품여부 조회
	 * @param 	GetFXContStatRequestVO paramVO
	 * @return  int
	 **/
	public int getImcsProdId(GetFXContStatRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod151_001_20171214_001";
		
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		int UflixChk = 0;
		
		try {
			try {
				list = getFXContStatDao.getImcsProdId(paramVO);
				
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			 
			UflixChk = 0;
			for (HashMap item : list) {
				paramVO.setCustomProperty(item.get("IMCS_PRODUCT_PROPERTY")==null?"":item.get("IMCS_PRODUCT_PROPERTY").toString());
				paramVO.setConcurrentCnt(item.get("CONCURRENT_COUNT")==null?"0":item.get("CONCURRENT_COUNT").toString());
				
				if ("N".equals(paramVO.getCustomProperty())) {
					UflixChk = 1;
					break;
				} else if ("01".equals(paramVO.getCustomProperty()) && Integer.parseInt(paramVO.getConcurrentCnt())>0) {
					// 2019.03.08 신규 유플릭스의 경우 일반 유플릭스 상품에 가입하지 않고 프리미엄만 가입할 수 있기 때문에, 신규 프리미엄 유플릭스만 가입한 경우에도 정상 처리 될 수록 처리해 준다.
					UflixChk = 1;
	        		break;
				}
			}
			
//			imcsLog.dbLog(ImcsConstants.FXAPI_PRO_ID151, sqlId, cache, querySize, methodName, methodLine);
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}

		return UflixChk;
	}
	
	
	
	/**
	 * 컨텐츠 타입 정보 조회
	 * @param paramVO
	 * @return
	 * @throws Exception
	 */
	public List<GetFXContStatResponseVO> getPpmType2(GetFXContStatRequestVO paramVO) throws Exception {
	   	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod050_068_20171214_001";
		
		List<GetFXContStatResponseVO> list	= new ArrayList<GetFXContStatResponseVO>();
		
		try {
			try {
				list = getFXContStatDao.getPpmType(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list == null || list.isEmpty()) {
				paramVO.setResultSet(-1);
//				imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID151, sqlId, cache, "conts_type:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			}
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID151, sqlId, cache, "conts_type:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return list;
	}
	
	
	/**
	 * 컨텐츠 타입 정보 조회
	 * @param paramVO
	 * @return
	 * @throws Exception
	 */
	public List<GetFXContStatResponseVO> getPpmType(GetFXContStatRequestVO paramVO) throws Exception {
	   	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod151_070_20171214_001";
		
		List<GetFXContStatResponseVO> list	= new ArrayList<GetFXContStatResponseVO>();
		
		try {
			try {
				list = getFXContStatDao.getPpmType(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
			paramVO.setResultSet(-1);
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID151, sqlId, cache, "ppm_type:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return list;
	}



	/**
	 * 	구매중복 체크
	 * 	1row와 1row 아닌 경우에 따라 해당 로직을 이용하는 경우가 달라 별로 생성함
	 * 	@param 	GetNSContStatRequestVO
	 * 	@return HashMap<String, Object>
	 */
	public HashMap<String, Object> getBuyDupChk(GetFXContStatRequestVO paramVO) {
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		HashMap<String, Object> mDupChk = new HashMap<String, Object>();
		String szBuyDate	= "";
		Integer nDupChk		= 0;
		
		try {
			// FVOD, PPV, PVOD 구매 중복 체크
			if("0".equals(paramVO.getContsType())) 		mDupChk = getFXContStatDao.getBuyDupChk1(paramVO);
			else if("1".equals(paramVO.getContsType()))	mDupChk = getFXContStatDao.getBuyDupChk2(paramVO);
			else 										mDupChk = getFXContStatDao.getBuyDupChk3(paramVO);
			
			if(mDupChk != null && !mDupChk.isEmpty()) {
				nDupChk = (Integer) mDupChk.get("DATA_CHK");
				szBuyDate	= StringUtil.nullToSpace((String) mDupChk.get("BUY_DATE"));
				
				if(nDupChk == null)		nDupChk = 0;
			}
			
			mDupChk.clear();
			mDupChk.put("DATA_CHK", nDupChk);
			mDupChk.put("BUY_DATE", szBuyDate);
		} catch(Exception e) {
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID151, "", null, "buy_info1:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}
		
		return mDupChk;
	}
	
	
	
	
	/**
	 * SVOD 상품정보 조회
	 * VTS소스에서는 해당 로직이 누락되어 있음
	 * @param paramVO
	 * @return
	 */
	public List<ComSvodVO> getSvodInfo(GetFXContStatRequestVO paramVO) {
	   	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId = "fxvod050_040_20171214_001";

		List<ComSvodVO> list	= new ArrayList<ComSvodVO>();
		
		try {
			try {
				list = getFXContStatDao.getSvodInfo(paramVO);
			} catch (DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
		} catch (Exception e) {
			paramVO.setResultSet(-1);
//			imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID151, sqlId, cache, "svod_prod:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
		}

		return list;
	}

}
