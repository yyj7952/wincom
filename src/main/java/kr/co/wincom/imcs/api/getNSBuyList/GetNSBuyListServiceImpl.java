package kr.co.wincom.imcs.api.getNSBuyList;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComDataFreeVO;
import kr.co.wincom.imcs.common.vo.ComTrailerVO;
import kr.co.wincom.imcs.common.vo.ComWatchaVO;
import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.OstInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.common.vo.SvodPkgVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;


@Service
public class GetNSBuyListServiceImpl implements GetNSBuyListService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSBuyList");
	
	@Autowired
	private GetNSBuyListDao getNSBuyListDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSContList(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	/**
	 * @author HONG
	 * @since 2014-04-16
	 * 
	 */
	@Override
	public GetNSBuyListResultVO getNSBuyList(GetNSBuyListRequestVO paramVO)	{
//		this.getNSContList(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		List<GetNSBuyListResponseVO> resultVO	= new ArrayList<GetNSBuyListResponseVO>();
		List<GetNSBuyListResponseVO> resultVOTemp	= new ArrayList<GetNSBuyListResponseVO>();
		
		GetNSBuyListResultVO	resultListVO	= new GetNSBuyListResultVO();
		
		long tp1			= 0;		// timePoint 1
		long tp2			= 0;		// timePoint 2
		String szImgSvrIp	= "";		// 이미지 서버 IP
		String szImgSvrUrl	= "";		// 이미지 서버 URL
		String msg			= "";
		String szRealBuyingPrice = "";
		
		int album_cnt = 0, iTotalCount = 0;
		String[] pStr = null;
		String[] nscArray = null;
		int result_set = 0;	
		
		try {
			int nPageNo			= Integer.parseInt(StringUtil.nullToZero(paramVO.getPageNo()));
			int nPageCnt		= Integer.parseInt(StringUtil.nullToZero(paramVO.getPageCnt()));
			int nStartNo		= 0;
			int nEndNo			= 0;
			
			int nMainCnt        = 0;
			
			if(nPageNo != 0 && nPageCnt != 0) {
				nStartNo = (nPageNo * nPageCnt) - (nPageCnt - 1);
				nEndNo   = (nPageNo * nPageCnt);
			}
			
			paramVO.setStartNum(String.valueOf(nStartNo));
			paramVO.setEndNum(String.valueOf(nEndNo));
			
			paramVO.setappFlag(paramVO.getApplType().substring(0, 1));
			
			if("E".equals(paramVO.getappFlag()) && "Y".equals(paramVO.getNscListYn())) {
				//imcsLog.timeLog("msg[VR앱 사용자는 엔스크린 보기가 지원되지 않습니다.]", String.valueOf(tp2 - tp1), methodName, methodLine);
				msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID110) + "] VR앱 사용자는 엔스크린 보기가 지원되지 않습니다.:"+paramVO.getSaId()+","+paramVO.getStbMac()+")"; 
				imcsLog.serviceLog(msg, methodName, methodLine);
				return resultListVO;
			}
			
			
			tp1 = System.currentTimeMillis();
			
			try {
				szImgSvrIp	= commonService.getIpInfo("img_server", ImcsConstants.NSAPI_PRO_ID110.split("/")[1]);			// 이미지서버 IP 조회
				szImgSvrUrl	= commonService.getIpInfo("img_resize_server", ImcsConstants.NSAPI_PRO_ID110.split("/")[1]);		// 이미지서버 URL 조회				
			} catch(Exception e) {
//				imcsLog.failLog(ImcsConstants.FXAPI_PRO_ID060, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
				throw new ImcsException();
			}
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			
			if("Y".equals(paramVO.getNscListYn()) || "A".equals(paramVO.getNscListYn())) {
				
				tp1 = System.currentTimeMillis();
				result_set = this.getCustPairingChk(paramVO);
				tp2	= System.currentTimeMillis();
				
				if(result_set == -1) {
					imcsLog.timeLog("페어링 정보 없음", String.valueOf(tp2 - tp1), methodName, methodLine);
				}
				
				if("N".equals(paramVO.getnscPairYn()) && "A".equals(paramVO.getNscListYn())){
					paramVO.setNscListYn("N");
				}
				
			} else {
				//2018.08.25 - 엔스크린2차 nsc_list_yn이 N이라도 가입자의 검수여부를 조회
				//             일반사용자의 경우 양쪽 스크린 모두 검수 카테고리에만 편성된 것이 없어야하기 때문!
				if (getNSBuyListDao.getTestSbc(paramVO)==null) {
					paramVO.settestSbc("N");
				} else {
					paramVO.settestSbc(getNSBuyListDao.getTestSbc(paramVO));
				}
				
			}
			
			resultVO = this.getNSBuyLists(paramVO);
			if(resultVO != null) 		nMainCnt = resultVO.size();
			
			
			
			for ( int i = 0; i < nMainCnt; i++) {
				GetNSBuyListResponseVO tempVO = new GetNSBuyListResponseVO();
				tempVO = resultVO.get(i);
				
				nscArray = tempVO.getNscreenYn().split(";");
				tempVO.setNscreenYn(nscArray[0]);
				
				
				if(album_cnt == 0) {
					tempVO.setCntTmp(tempVO.getCnt());
					iTotalCount = Integer.parseInt(tempVO.getCntTmp());
					album_cnt++;
				}

				if(!tempVO.getRealPriceTmp().equals("")) {
					pStr = null;
					pStr = tempVO.getRealPriceTmp().split("\\|");
					if(pStr.length >= 3)
					{
						tempVO.setRealBuyingPrice(pStr[2]);
						
						if(pStr[0].equals("I")){ //아이돌 라이브 유료체널
							tempVO.setBuyContsClass("1");
						}
						
					}
				}
				
				if(!tempVO.getRealDfPriceTmp().equals("")) {
					pStr = null;
					pStr = tempVO.getRealDfPriceTmp().split("\\|");
					if(pStr.length >= 3)
					{
						tempVO.setRealDatafreeBuyingPrice(pStr[2]);
					}
				}
				
				if(tempVO.getRealBuyingPrice().equals("")) {
					if(tempVO.getBuyingType().equals("B") || tempVO.getBuyingType().equals("W") || tempVO.getBuyingType().equals("T")) {
						szRealBuyingPrice = Integer.toString((Integer.parseInt(tempVO.getBuyingPrice()) + (Integer.parseInt(tempVO.getBuyingPrice())*10/100)));
						tempVO.setRealBuyingPrice(szRealBuyingPrice);
					}else if(tempVO.getBuyingType().equals("K") || tempVO.getBuyingType().equals("I") || tempVO.getBuyingType().equals("H")) {
						szRealBuyingPrice = Integer.toString((Integer.parseInt(tempVO.getBalace()) + (Integer.parseInt(tempVO.getBalace())*10/100)));
						tempVO.setRealBuyingPrice(szRealBuyingPrice);
					} else if (tempVO.getBuyingType().equals("A")) {
						szRealBuyingPrice = tempVO.getBalace();
						tempVO.setRealBuyingPrice(szRealBuyingPrice);
					}
				}
				if(tempVO.getRealDatafreeBuyingPrice().equals("")) {
					if(tempVO.getDatafreeBuyingType().equals("B") || tempVO.getDatafreeBuyingType().equals("W") || tempVO.getDatafreeBuyingType().equals("T")) {
						szRealBuyingPrice = Integer.toString((Integer.parseInt(tempVO.getDatafreePrice()) + (Integer.parseInt(tempVO.getDatafreePrice())*10/100)));
						//tempVO.setRealBuyingPrice(szRealBuyingPrice);
						tempVO.setRealDatafreeBuyingPrice(szRealBuyingPrice);
					} else if(tempVO.getDatafreeBuyingType().equals("K") || tempVO.getDatafreeBuyingType().equals("I") || tempVO.getDatafreeBuyingType().equals("H")){
						szRealBuyingPrice = Integer.toString((Integer.parseInt(tempVO.getDatafreeBalace()) + (Integer.parseInt(tempVO.getDatafreeBalace())*10/100)));
						//tempVO.setRealBuyingPrice(szRealBuyingPrice);
						tempVO.setRealDatafreeBuyingPrice(szRealBuyingPrice);
					} else if (tempVO.getBuyingType().equals("A")) {
						szRealBuyingPrice = tempVO.getDatafreeBalace();
						//tempVO.setRealBuyingPrice(szRealBuyingPrice);
						tempVO.setRealDatafreeBuyingPrice(szRealBuyingPrice);
					}
				}
				
				tempVO.setIptvProdChk("0");
				tempVO.setNscProdChk("0");
				tempVO.setIptvTestSbc("Y");
				tempVO.setNscTestSbc("Y");
				
				if(paramVO.getNscListYn().equals("N") && tempVO.getNscreenYn().equals("Y") || paramVO.getNscListYn().equals("Y")) {
					
					tp1 = System.currentTimeMillis();
					result_set = this.getTypeInfo(paramVO, tempVO, tempVO.getContsId());
					tp2 = System.currentTimeMillis();
					
					if(result_set == -1) {
						
						imcsLog.timeLog("편성 및 상품 타입 정보가 없음", String.valueOf(tp2 - tp1), methodName, methodLine);
					}
				}
				
				
				// 2018.06.04 - 엔스크린2차 : I30편성여부 체크
				if(paramVO.getNscListYn().equals("Y")) {
					if(!tempVO.getCatGb2().equals("I30") || !tempVO.getCatGb1().equals("NSC") || 
							tempVO.getIptvProdChk().equals("0") || tempVO.getNscProdChk().equals("0") ||
								paramVO.gettestSbc().equals("N") && tempVO.getIptvTestSbc().equals("Y") || tempVO.getNscTestSbc().equals("Y")) 
					{
						iTotalCount--;
						continue;
					}
				} else if (paramVO.getNscListYn().equals("N")) {
					if(!tempVO.getCatGb2().equals("I30") || !tempVO.getCatGb1().equals("NSC") || 
							tempVO.getIptvProdChk().equals("0") || tempVO.getNscProdChk().equals("0") ||
								paramVO.gettestSbc().equals("N") && tempVO.getIptvTestSbc().equals("Y") || tempVO.getNscTestSbc().equals("Y")) 
					{
						tempVO.setNscreenYn("N");
					}
				}
				
				
				
				tempVO.setSeriesYn("N");
				if(!tempVO.getSerInfo().equals("")) {
					
					tempVO.setSeriesYn("Y");
					pStr = null;
					pStr = tempVO.getSerInfo().split("\\|");
					tempVO.setSerCatId(pStr[0]);
					tempVO.setSeriesNo(pStr[1]);
					tempVO.setCatId(tempVO.getSerCatId());
					
				}
				
				if(tempVO.getContsType().equals("2") && !tempVO.getSeriesYn().equals("Y") && tempVO.getCatId().equals("ZZZZZ"))
				{
					tempVO.setSeriesYn("Y");
					tempVO.setCatId(tempVO.getContsId());
					tempVO.setSeriesNo("1");
				}
				
				// 2019.07.31 - 구매 Screen정보 제공시 VR1.5(OCULUS결제)에서 구매한 정보를 제외하고는 모두 NULL값으로 전달한다.
				if(!(paramVO.getappFlag().equals("E") && (tempVO.getVrBuyInfo().equals("R") || tempVO.getVrBuyInfo().equals("I") || tempVO.getVrBuyInfo().equals("G") || tempVO.getVrBuyInfo().equals("H"))) )
				{
					tempVO.setVrBuyInfo("");
				}
				
				// 2020.03.13 CATEGORY_TYPE(컨텐츠 시청 구분) 설정
				if("B".equals(tempVO.getBookFlag()) == false && "C".equals(tempVO.getBookFlag()) == false)
					tempVO.setCategoryType("");
				else
					tempVO.setCategoryType(tempVO.getBookFlag());
				
				resultVOTemp.add(tempVO);
		
			}
			
			resultListVO.setResHeader("0||" + iTotalCount +"|"+ szImgSvrIp +"|"+ szImgSvrUrl +"|"+ paramVO.getnscPairYn() +"|"+ paramVO.getnSaId() +"|"+ paramVO.getnStbMac() + "|");
			//resultListVO.setList(resultVO);
			resultListVO.setList(resultVOTemp);
			
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
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID110) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}

	/**
	 * 링크타입 정보 조회
	 * @param paramVO
	 * @return
	 */
	public int getCustPairingChk(GetNSBuyListRequestVO paramVO) throws Exception {
    	IMCSLog imcsLog = new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String msg		= "";
		String sqlId	=  "";
		int result_set	= 0;
			
		List<HashMap> list = null;
		
		try {
			try{
				list  = getNSBuyListDao.getCustPairingChk(paramVO);
			}catch(DataAccessException e){
				paramVO.settestSbc("N");
				paramVO.setnscPairYn("N");
				result_set = -1;
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			
			if (list == null || list.size() == 0) {
				paramVO.settestSbc("N");
				paramVO.setnscPairYn("N");	
				//gf_Logging_New(gst_ss_info.pro_id, ":%s:%s] svc[%-18s] msg[PAIRING 정보가 없습니다.] [%s:%d]\n", rd1.c_sa_id, rd1.c_stb_mac, API_NAME, __FILE__, __LINE__);
				msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID110) + "] PAIRING 정보가 없습니다.:"+paramVO.getSaId()+","+paramVO.getStbMac()+")"; 
				imcsLog.serviceLog(msg, methodName, methodLine);
				//return result_set;
			} else {
				paramVO.setnSaId(list.get(0).get("N_SA_ID").toString());
				paramVO.setnStbMac(list.get(0).get("N_STB_MAC").toString());
				paramVO.settestSbc(list.get(0).get("TEST_SBC").toString());
				paramVO.setnscPairYn("Y");
			}
			
		} catch (Exception e) {
			result_set = -1;
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			return result_set;
		}
		
		return result_set;
	}

	
	/**
	 * 구매목록 조회
	 * @param paramVO
	 * @return
	 */
	public List<GetNSBuyListResponseVO> getNSBuyLists(GetNSBuyListRequestVO paramVO) throws Exception {
    	IMCSLog imcsLog = new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String msg		= "";
		String sqlId	=  "nsvod012_001_20171214_001";
			
		List<GetNSBuyListResponseVO> list	= new ArrayList<GetNSBuyListResponseVO>();
		
		try {
			try{
				if("N".equals(paramVO.getNscListYn())) {
					list  = getNSBuyListDao.getNSBuyList_1(paramVO);
				} else if ("Y".equals(paramVO.getNscListYn())) {
					list  = getNSBuyListDao.getNSBuyList_2(paramVO);
				} else if ("A".equals(paramVO.getNscListYn())) {
					list  = getNSBuyListDao.getNSBuyList_3(paramVO);
				}
			}catch(DataAccessException e){
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list == null || list.size() == 0) {
				//gf_Logging_New(gst_ss_info.pro_id, ":%s:%s] svc[%-18s] msg[구매목록이 없습니다.] [%s:%d]\n", rd1.c_sa_id, rd1.c_stb_mac, API_NAME, __FILE__, __LINE__);
				msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID110) + "] 구매목록이 없습니다.:"+paramVO.getSaId()+","+paramVO.getStbMac()+")"; 
				imcsLog.serviceLog(msg, methodName, methodLine);
			}else{
				
			}
			
			paramVO.setResultSet(0);
					
		} catch (Exception e) {
			
			paramVO.setResultSet(-1);
		}
		
		return list;
	}
	
	/**
	 * 편성 및 상품 타입 정보 조회
	 * @param paramVO
	 * @return
	 */
	public int getTypeInfo(GetNSBuyListRequestVO paramVO, GetNSBuyListResponseVO tempVO, String contId) throws Exception {
    	IMCSLog imcsLog = new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3); 
		
		String sqlId	= "nsvod012_001_20171214_001";
		String msg		= "";
		List<HashMap> list = null;
		int result_set	= 0;
		
		try {
			try{
					list  = getNSBuyListDao.getTypeInfo(contId);
			}catch(DataAccessException e){
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list == null || list.size() == 0) {
				result_set = -1;
				//gf_Logging_New(gst_ss_info.pro_id, ":%s:%s] svc[%-18s] msg[편성 및 상품 타입 정보가 없습니다.] [%s:%d]\n", rd1.c_sa_id, rd1.c_stb_mac, API_NAME, __FILE__, __LINE__);
				msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID110) + "] 편성 및 상품 타입 정보가 없습니다.:"+paramVO.getSaId()+","+paramVO.getStbMac()+")"; 
				imcsLog.serviceLog(msg, methodName, methodLine);
			}else{
				tempVO.setCatGb1(list.get(0).get("CAT_GB1")==null?"":list.get(0).get("CAT_GB1").toString());
				tempVO.setCatGb2(list.get(0).get("CAT_GB2")==null?"":list.get(0).get("CAT_GB2").toString());
				tempVO.setIptvProdChk(list.get(0).get("IPTV_PROD_CHK")==null?"":list.get(0).get("IPTV_PROD_CHK").toString());
				tempVO.setNscProdChk(list.get(0).get("NSC_PROD_CHK")==null?"":list.get(0).get("NSC_PROD_CHK").toString());
				tempVO.setIptvTestSbc(list.get(0).get("IPTV_TEST_SBC")==null?"":list.get(0).get("IPTV_TEST_SBC").toString());
				tempVO.setNscTestSbc(list.get(0).get("NSC_TEST_SBC")==null?"":list.get(0).get("NSC_TEST_SBC").toString());
			}
			
					
		} catch (Exception e) {
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID110) + "] 구매목록이 없습니다.:"+paramVO.getSaId()+","+paramVO.getStbMac()+")"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return result_set;
	}

}
