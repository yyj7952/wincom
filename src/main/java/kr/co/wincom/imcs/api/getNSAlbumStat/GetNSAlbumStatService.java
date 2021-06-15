package kr.co.wincom.imcs.api.getNSAlbumStat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.api.authorizeNView.AuthorizeNViewRequestVO;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;

@Service
public class GetNSAlbumStatService
{
	private final static String API_LOG_NAME = "000/getNSAlbumStat";
	
	private Log imcsLogger	= LogFactory.getLog("API_getNSAlbumStat");
	
	@Autowired
	private GetNSAlbumStatDao getNSAlbumStatDao;
	
	public GetNSAlbumStatResultVO getNSAlbumStat(GetNSAlbumStatRequestVO paramVO)
	{
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog("getNSAlbumStat service call");
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		GetNSAlbumStatResultVO resultVO = new GetNSAlbumStatResultVO();
		
		String msg = "";
		long tp1 = 0, tp2 = 0;
		
		String[] arrAlbumIds = null;
		
		HashMap<String, String> hmCustPairingChk = null;
		List<HashMap<String, String>> listAlbumInfo = null;
		List<String> listSvodCheck = null;
		List<HashMap<String, String>> listContsBuyCheck = null;
		ArrayList<HashMap<String, String>> listCopyAlbumInfo= new ArrayList<>();
		List<HashMap<String, String>> listNScreenContsBuyCheck = null;
		List<HashMap<String, String>> listReadingCnt = null;
		
		ArrayList<String> subcription_arr = new ArrayList<>();
		ArrayList<String> buy_arr = new ArrayList<>();
		ArrayList<String> reading_album = new ArrayList<>();
		
		// 최종 데이터가 담긴 리스트. 리스트 갯수는 multiAlbumId 갯수와 같다.
		ArrayList<HashMap<String, String>> listLastData = new ArrayList<>();
		
		ArrayList<String> kidSvodList = new ArrayList<>();
		ArrayList<String> kidSvodListChk = new ArrayList<>();
		
		String szAlbumId = "";
		String szNProductTypeMin = "";
		String szNProductTypeMax = "";
		
		StringBuilder sb = new StringBuilder();
		
		try
		{
			if(StringUtils.isBlank(paramVO.getAlbumFlag()) == true)
			{
				paramVO.setAlbumFlag("A");
			}
			
			// ALBUM_FLAG 체크
			if(paramVO.getAlbumFlag().equals("A") == false && paramVO.getAlbumFlag().equals("P") == false
					&& paramVO.getAlbumFlag().equals("R") == false)
			{
				resultVO.setResult("1|INPUT PARAM ERROR|\f");
				
				msg	= " svc[" + String.format("%-20s", API_LOG_NAME) + "] 정상적인 INPUT PARAM 이 아닙니다.:"+paramVO.getSaId()+","+paramVO.getStbMac()+")"; 
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				return resultVO;
			}
			
			arrAlbumIds = StringUtils.split(paramVO.getMultiAlbumId(), ",");
			paramVO.setArrAlbumIds(arrAlbumIds); // VO 객체에 배열 저장
			
			if(arrAlbumIds.length > 30)
			{
				resultVO.setResult("1|INPUT PARAM ERROR|\f");
				
				msg	= " svc[" + String.format("%-20s", API_LOG_NAME) + "] 앨범 개수 30개 초과:"+paramVO.getSaId()+","+paramVO.getStbMac()+")"; 
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				return resultVO;
			}
			
			for(String aId : arrAlbumIds)
			{
				if(aId.length() != 15)
				{
					resultVO.setResult("1|INPUT PARAM ERROR|\f");
					
					msg	= " svc[" + String.format("%-20s", API_LOG_NAME) + "] 정상적인 앨범ID가 아닙니다.:"+paramVO.getSaId()+","+paramVO.getStbMac()+")"; 
					imcsLog.serviceLog(msg, methodName, methodLine);
					
					return resultVO;
				}
				
				// 앨범의 갯수 만큼 리스트를 만들어준다.
				HashMap<String, String> hmData = new HashMap<>();
				hmData.put("arrAlbumId", aId);
				hmData.put("cSubcription_yn", "1");
				hmData.put("cIsSvodOnly", "N");
				hmData.put("cIsBuy", "1");
				hmData.put("cDataFreeBuy", "1");
				hmData.put("cBuyDate", "");
				hmData.put("cBuyExpired", "");
				hmData.put("szProductTypeMin", "");
				hmData.put("szProductTypeMax", "");
				hmData.put("szNproductTypeMin", "");
				hmData.put("szNproductTypeMax", "");
				hmData.put("cWatchYn", "N");
				hmData.put("cLinkTime", "");
				hmData.put("cNscreenYn", "N");
				hmData.put("cViewingLength", "24");
				hmData.put("cPayYn", "N");
				hmData.put("cTotalBuyYn", "1");
				hmData.put("cTotalSubcriptionYn", "1");
				hmData.put("cReadingCount", "");
				
				listLastData.add(hmData);
			}
			
			tp1 = System.currentTimeMillis();
			
			// custom_info 조회
			try {
				hmCustPairingChk = this.getNSAlbumStatDao.getCustPairingChk(paramVO);
			} catch(Exception ex) {
				resultVO.setResult("1|CUSTOM NOT EXIST|\f");
				throw ex;
			}
			
			if(hmCustPairingChk == null || hmCustPairingChk.size() == 0)	
			{
				msg	= " svc[" + String.format("%-20s", API_LOG_NAME) + "] no data found:"+paramVO.getSaId()+","+paramVO.getStbMac()+")"; 
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				resultVO.setResult("1|CUSTOM NOT EXIST|\f");
				return resultVO;
			}
			
			// 엔스크린 맥가번 세팅
			paramVO.setNcn_sa_id(hmCustPairingChk.get("ncn_sa_id"));
			paramVO.setNcn_stb_mac(hmCustPairingChk.get("ncn_stb_mac"));
			paramVO.setTestSbc(hmCustPairingChk.get("TEST_SBC"));
			
			tp2	= System.currentTimeMillis();
			
			imcsLog.timeLog("가입자 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			// 멀티 앨범 정보 조회
			try {
				listAlbumInfo = this.getNSAlbumStatDao.listMultiAlbumInfo(paramVO);
			} catch(Exception ex) {
				resultVO.setResult("1|메인 쿼리 에러|\f");
				throw ex;
			}
			
			for(HashMap<String, String> hm : listAlbumInfo)
			{
				String nscreenFun = hm.get("NSCREEN_FUNCTION"); // Y;0/3 형식임
				hm.put("NSCREEN_YN", "N"); // 일단 N으로 세팅
				 
				// nscreenFun이 Y로 시작하는 경우 productTypeMin, productTypeMax 세팅
				if(nscreenFun.length() > 0)
				{
					hm.put("NSCREEN_YN", nscreenFun.substring(0, 1));
					
//					if(hm.get("NSCREEN_YN").equals("Y"))
//					{
//						String[] arrProduct = StringUtils.split(nscreenFun.substring(2), "/");
//						
//						// 오름차순 정렬 - ProductType은 절대 두자리 일 수 없음. 불필요한 코드임.
//						Arrays.sort(arrProduct, new Comparator<String>() {
//							@Override
//							public int compare(String type1, String type2) {
//								if(Integer.parseInt(type1) > Integer.parseInt(type2))
//									return 1;
//								else if(Integer.parseInt(type1) < Integer.parseInt(type2))
//									return -1;
//								else
//									return 0;
//							}
//						});
//
//						hm.put("PRODUCTTYPEMIN", arrProduct[0]);
//						hm.put("PRODUCTTYPEMAX", arrProduct[arrProduct.length - 1]);
//					}
						
					if(hm.get("NSCREEN_YN").equals("Y"))
					{
						String preArrProduct = StringUtils.split(nscreenFun, ";")[1];
						String[] arrProduct = StringUtils.split(preArrProduct, "/");
						
						// 오름차순 정렬 - ProductType은 절대 두자리 일 수 없음. 불필요한 코드임.
						Arrays.sort(arrProduct, new Comparator<String>() {
							@Override
							public int compare(String type1, String type2) {
								if(Integer.parseInt(type1) > Integer.parseInt(type2))
									return 1;
								else if(Integer.parseInt(type1) < Integer.parseInt(type2))
									return -1;
								else
									return 0;
							}
						});

						hm.put("PRODUCTTYPEMIN", arrProduct[0]);
						hm.put("PRODUCTTYPEMAX", arrProduct[arrProduct.length - 1]);
						
						// Seamless 단방향 2차
						hm.put("NSCREEN_SEAM", StringUtils.split(nscreenFun, ";")[2]);
						hm.put("SEAMLESS_CHECK", "N");
						if(StringUtils.split(nscreenFun, ";")[2].equals("T") || StringUtils.split(nscreenFun, ";")[2].equals("A")) {
							hm.put("SEAMLESS_CHECK", "Y");
						}
					}
				}
				
				szAlbumId = hm.get("ALBUM_ID");
				szNProductTypeMin = hm.get("NPRODUCTTYPEMIN");
				szNProductTypeMax = hm.get("NPRODUCTTYPEMAX");
				
				// Map에 필요한 정보 세팅
				hm.put("cSubcription_yn", "1");
				hm.put("cIsBuy", "1");
				hm.put("cDataFreeBuy", "1");
				hm.put("cBuyDate", "");
				hm.put("cBuyExpired", "");
				hm.put("cIsSvodOnly", "");
				hm.put("cWatchYn", "N");
				hm.put("cLinkTime", "");
				hm.put("cNcnSubscribeYn", "N");
				hm.put("cNcnBuyYn", "N");
				hm.put("cReadingCount", "");
				
				// SVOD ONLY 여부 설정
				if(szNProductTypeMin.equals("3") && szNProductTypeMax.equals("3"))
					hm.put("cIsSvodOnly", "Y");
				else
					hm.put("cIsSvodOnly", "N");
				
				// 2019.08.09 - 아이들나라3.0_2차 : INPUT PARAM album_flag가 R(시청횟수 조회) 가 아니라면 조회한다.
				if(paramVO.getAlbumFlag().equals("R") == false)
				{
					// SVOD에 편성된 앨범의 경우 가입 여부를 체크 하기 위해
					// product_type이 3인 앨범의 경우 subcription_arr에 담는다
					if(szNProductTypeMin.equals("3") || szNProductTypeMax.equals("3"))
					{
						if(!szNProductTypeMin.equals("0"))// FVOD + SVOD 일경우
						{
							subcription_arr.add(szAlbumId);
						}
					}
				}
				
				// listAlbumInfo 객체를 계속 사용해도 되는데 SCREEN_TYPE을 조건문으로 계속 넣어 주면 실수를 할수 있기 때문에
				// ArrayList 객체를 하나 만들어서 ADD 해줌.
				listCopyAlbumInfo.add(hm);
			}

			tp1 = System.currentTimeMillis();
			
			imcsLog.timeLog("멀티 앨범 정보 조회", String.valueOf(tp1 - tp2), methodName, methodLine);
			
			tp2 = System.currentTimeMillis();

			// SVOD에 편성된 앨범의 경우 가입 여부를 확인한다.
			if(subcription_arr.size() > 0)
			{
				// SVOD 앨범ID를 저장
				paramVO.setArrSvodAlbumIds(subcription_arr);
				
				// SVOD 편성 조회
				try {
					listSvodCheck = this.getNSAlbumStatDao.listSvodCheck(paramVO);
				} catch(Exception ex) {					
					msg	= " svc[" + String.format("%-20s", API_LOG_NAME) + "] SVOD 편성 조회 오류:"+paramVO.getSaId()+","+paramVO.getStbMac()+")"; 
					imcsLog.serviceLog(msg, methodName, methodLine);
				}

				// 가입된 SVOD가 존재하는 경우 가입여부를 '0'으로 바꿔주기 위해 사용
				for(int i = 0; listSvodCheck != null && i < listSvodCheck.size(); i++)
				{
					String svodAid = listSvodCheck.get(i);
					
					for(HashMap<String, String> hm : listCopyAlbumInfo)
					{
						if(hm.get("cSubcription_yn").equals("0") == false)
						{
							if(svodAid.equals(hm.get("ALBUM_ID")))
							{
								hm.put("cSubcription_yn", "0");
								break;
							}
						}
					}
				}
			}
			else // SVOD에 편성된 앨범이 없으므로 모두 미가입 처리
			{
				// 2020.03.05 - 초기화해주므로 필요없는 로직
//				for(HashMap<String, String> hm : listCopyAlbumInfo)
//				{
//					hm.put("cSubcription_yn", "1");
//				}
			}
			
			imcsLog.timeLog("SVOD 편성 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			tp1 = System.currentTimeMillis();
			
			// 2019.08.09 - 아이들나라3.0_2차 : INPUT PARAM album_flag가 R(시청횟수 조회) 가 아니라면 조회한다.
			if(paramVO.getAlbumFlag().equals("R") == false)
			{
				for(HashMap<String, String> hm : listCopyAlbumInfo)
				{
					// 기존 로직에서도 SVOD ONLY가 'N'이고 미가입된 앨범들만 구매여부를 체크 했기 때문에 사용
					if(hm.get("cIsSvodOnly").equals("N") && hm.get("cSubcription_yn").equals("1")
							&& hm.get("NPRODUCTTYPEMIN").equals("0") == false)
					{
							buy_arr.add(hm.get("ALBUM_ID"));
							buy_arr.add(hm.get("ALBUM_ID") + "_D");
					}
				}
			}
			
			if(buy_arr.size() > 0)
			{
				paramVO.setBuy_arr(buy_arr);
				
				try {
					listContsBuyCheck = this.getNSAlbumStatDao.listContsBuyCheck(paramVO);
				} catch(Exception ex) {					
					msg	= " svc[" + String.format("%-20s", API_LOG_NAME) + "] 구매여부 조회 오류:"+paramVO.getSaId()+","+paramVO.getStbMac()+")"; 
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
				
				if(listContsBuyCheck != null && listContsBuyCheck.size() > 0)
				{
 					for(HashMap<String, String> hmBuy : listContsBuyCheck)
					{
						String diff_buy_album = hmBuy.get("ALBUM_ID");
						String buy_expired = hmBuy.get("EXPIRED_DATE");
						String buy_date = hmBuy.get("BUY_DATE");
						
 						for(HashMap<String, String> hm : listCopyAlbumInfo)
						{
							if(hm.get("cIsSvodOnly").equals("N") && hm.get("cSubcription_yn").equals("1"))
							{
								// 2020.03.05 - 데이터프리 잘 잘리는지 확인 필요
								if(diff_buy_album.substring(0, 15).equals(hm.get("ALBUM_ID").substring(0, 15)))
								{
									if(diff_buy_album.indexOf("_D") > -1)
									{
										hm.put("cIsBuy", "0");
										hm.put("cDataFreeBuy", "0");
										hm.put("cBuyDate", buy_date);
										hm.put("cBuyExpired", buy_expired);
									}
									else
									{
										hm.put("cIsBuy", "0");
										
										if(hm.get("cDataFreeBuy").equals("0") == false)
										{
											hm.put("cDataFreeBuy", "1");
										}
										
										// 2020.03.05 - 구매날짜랑 만료일자 쓰는지 다시 한번 체크 필요
										hm.put("cBuyDate", buy_date);
										hm.put("cBuyExpired", buy_expired);
									}
								}
								else if(hm.get("cIsBuy").equals("0") == false)
								{
									hm.put("cIsBuy", "1");
									hm.put("cDataFreeBuy", "1");
									
									if(hm.get("NPRODUCTTYPEMIN").equals("0"))
									{
										hm.put("cIsBuy", "0");
									}
								}
							}
						}
					}
				}
				else
				{
					for(HashMap<String, String> hm : listCopyAlbumInfo)
					{
						if(hm.get("cIsSvodOnly").equals("N") && hm.get("cSubcription_yn").equals("1"))
						{
							hm.put("cIsBuy", "1");
							hm.put("cDataFreeBuy", "1");
							
							if(hm.get("NPRODUCTTYPEMIN").equals("0"))
								hm.put("cIsBuy", "0");
						}
					}
					
					msg	= " svc[" + String.format("%-20s", API_LOG_NAME) + "] 구매한 컨텐츠가 없습니다.:"+paramVO.getSaId()+","+paramVO.getStbMac()+")"; 
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
			}
			
			imcsLog.timeLog("구매 여부 조회", String.valueOf(tp1 - tp2), methodName, methodLine);

			subcription_arr.clear();

			// 2019.08.09 - 아이들나라3.0_2차 : INPUT PARAM album_flag가 R(시청횟수 조회) 가 아니라면 조회한다.
			if(paramVO.getAlbumFlag().equals("R") == false)
			{
				for(HashMap<String, String> hm : listCopyAlbumInfo)
				{
					// 엔스크린 가입자의 경우 I30가입자의 가입 여부 체크를 위해 SVOD에 편성되어 있는지 확인 후 배열에 담는다.
					if(paramVO.getNcn_sa_id().length() != 0 && hm.get("NSCREEN_YN").equals("Y") && hm.get("SEAMLESS_CHECK").equals("Y")
							&& (hm.get("PRODUCTTYPEMIN").equals("3") || hm.get("PRODUCTTYPEMAX").equals("3"))
							&& hm.get("cIsBuy").equals("0") == false
							&& hm.get("cSubcription_yn").equals("0") == false
							&& hm.get("BOOK_YN").equals("N"))
					{
						if(!hm.get("PRODUCTTYPEMIN").equals("0")) {
							subcription_arr.add(hm.get("ALBUM_ID"));
						}
					}
					
					//BOOK_YN = Y 일경우 따로 배열에 담는다
					if(paramVO.getNcn_sa_id().length() != 0 && hm.get("NSCREEN_YN").equals("Y") && hm.get("SEAMLESS_CHECK").equals("Y")
							&& (hm.get("PRODUCTTYPEMIN").equals("3") || hm.get("PRODUCTTYPEMAX").equals("3"))
							&& hm.get("cIsBuy").equals("0") == false
							&& hm.get("cSubcription_yn").equals("0") == false
							&& hm.get("BOOK_YN").equals("Y"))
					{
						kidSvodList.add(hm.get("ALBUM_ID"));
					}
					
				}
			}
			
			tp2 = System.currentTimeMillis();
			
			/** 아래부터 엔스크린 체크 */
			/** 아래부터 엔스크린 체크 */
			/** 아래부터 엔스크린 체크 */
			// 엔스크린 가입자의 경우 I30가입자의 가입 여부 체크를 한다. (엔스크린 컨텐츠 Y && FVOD가 아닌 컨텐츠 대상)
			if(subcription_arr.size() > 0)
			{
				// SVOD 앨범ID를 저장
				paramVO.setArrSvodAlbumIds(null);
				paramVO.setArrSvodAlbumIds(subcription_arr);
				
				
				if (kidSvodList.size() > 0) {
					paramVO.setArrKidSvodIds(kidSvodList);
					kidSvodListChk = this.kidProductCd(paramVO);
				}

				// 가입된 SVOD가 존재하는 경우 가입여부를 '0'으로 바꿔주기 위해 사용
				for(int i = 0; kidSvodListChk != null && i < kidSvodListChk.size(); i++)
				{
					String svodAid = kidSvodListChk.get(i);
					
					for(HashMap<String, String> hm : listCopyAlbumInfo)
					{
						if(hm.get("cNcnSubscribeYn").equals("Y") == false)
						{
							if(svodAid.equals(hm.get("ALBUM_ID")))
							{
								hm.put("cNcnSubscribeYn", "Y");
								break;
							}
						}
					}
				}
				
				
				if(listSvodCheck != null)
					listSvodCheck.clear();
				
				// 엔스크린 SVOD에 편성된 앨범의 경우 가입 여부를 확인한다.
				try {
					listSvodCheck = this.getNSAlbumStatDao.listNScreenSvodCheck(paramVO);
				} catch(Exception ex) {					
					msg	= " svc[" + String.format("%-20s", API_LOG_NAME) + "] 엔스크린 SVOD 편성 조회 오류:"+paramVO.getSaId()+","+paramVO.getStbMac()+")"; 
					imcsLog.serviceLog(msg, methodName, methodLine);
				}

				// 가입된 SVOD가 존재하는 경우 가입여부를 '0'으로 바꿔주기 위해 사용
				for(int i = 0; listSvodCheck != null && i < listSvodCheck.size(); i++)
				{
					String svodAid = listSvodCheck.get(i);
					
					for(HashMap<String, String> hm : listCopyAlbumInfo)
					{
						if(hm.get("cNcnSubscribeYn").equals("Y") == false)
						{
							if(svodAid.equals(hm.get("ALBUM_ID")))
							{
								hm.put("cNcnSubscribeYn", "Y");
								break;
							}
						}
					}
				}
			}
			else
			{
				// 2020.03.05 - 초기화하기 때문에 필요없는 로직
				// SVOD에 편성된 앨범이 없으므로 모두 미가입 처리
//				for(HashMap<String, String> hm : listCopyAlbumInfo)
//				{
//					hm.put("cNcnSubscribeYn", "N");
//				}
			}
			
			imcsLog.timeLog("엔스크린 SVOD 편성 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			// 2019.08.09 - 아이들나라3.0_2차 : INPUT PARAM album_flag가 R(시청횟수 조회) 가 아니라면 조회한다.
			if(paramVO.getAlbumFlag().equals("R") == false)
			{
				buy_arr.clear();
				
				// 기존 로직에서도 SVOD ONLY가 'N'이고 미가입된 앨범들만 구매여부를 체크 했기 때문에 사용
				for(HashMap<String, String> hm : listCopyAlbumInfo)
				{
					if(paramVO.getNcn_sa_id().length() != 0 && hm.get("NSCREEN_YN").equals("Y") && hm.get("SEAMLESS_CHECK").equals("Y")
							&& !(hm.get("PRODUCTTYPEMIN").equals("3") && hm.get("PRODUCTTYPEMAX").equals("3"))
							&& hm.get("PRODUCTTYPEMIN").equals("0") == false
							&& hm.get("cNcnSubscribeYn").equals("Y") == false
							&& hm.get("cSubcription_yn").equals("0") == false
							&& hm.get("cIsBuy").equals("0") == false)
					{
							buy_arr.add(hm.get("ALBUM_ID"));							
					}
				}
			}
			
			tp1 = System.currentTimeMillis();
			if(buy_arr.size() > 0)
			{
				paramVO.setBuy_arr(null);
				paramVO.setBuy_arr(buy_arr);
				
				try {
					listNScreenContsBuyCheck = this.getNSAlbumStatDao.listNScreenContsBuyCheck(paramVO);
				} catch(Exception ex) {					
					msg	= " svc[" + String.format("%-20s", API_LOG_NAME) + "] 엔스크린 구매여부 조회 오류:"+paramVO.getSaId()+","+paramVO.getStbMac()+")"; 
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
				
				if(listNScreenContsBuyCheck != null && listNScreenContsBuyCheck.size() > 0)
				{
					for(HashMap<String, String> hmBuy : listNScreenContsBuyCheck)
					{
						String diff_buy_album = hmBuy.get("ALBUM_ID");
						String buy_expired = hmBuy.get("EXPIRED_DATE");
						String buy_date = hmBuy.get("BUY_DATE");
						
						for(HashMap<String, String> hm : listCopyAlbumInfo)
						{
							if(paramVO.getNcn_sa_id().length() != 0 && hm.get("NSCREEN_YN").equals("Y") && hm.get("SEAMLESS_CHECK").equals("Y")
									&& !(hm.get("PRODUCTTYPEMIN").equals("3") && hm.get("PRODUCTTYPEMAX").equals("3"))
									&& hm.get("PRODUCTTYPEMIN").equals("0") == false
									&& hm.get("cNcnSubscribeYn").equals("Y") == false
									&& hm.get("cSubcription_yn").equals("0") == false
									&& hm.get("cIsBuy").equals("0") == false
									&& hm.get("cNcnBuyYn").equals("Y") == false)
							{
								if(diff_buy_album.equals(hm.get("ALBUM_ID")))
								{
									hm.put("cNcnBuyYn", "Y");
									hm.put("cNbuyDate", buy_date);
									hm.put("cNexpirdDate", buy_expired);
								}
								else
								{
									hm.put("cNcnBuyYn", "N");
								}
							}
						}
					}
				}
				else
				{
					// 2020.03.05 - 초기화하기 떄문에 불필요한 로직
//					for(HashMap<String, String> hm : listCopyAlbumInfo)
//					{
//						if(paramVO.getNcn_sa_id().length() != 0 && hm.get("NSCREEN_YN").equals("Y")
//							&& !(hm.get("PRODUCTTYPEMIN").equals("3") && hm.get("PRODUCTTYPEMAX").equals("3"))
//							&& hm.get("PRODUCTTYPEMIN").equals("0") == false
//							&& hm.get("cNcnSubscribeYn").equals("Y") == false
//							&& hm.get("cSubcription_yn").equals("0") == false
//							&& hm.get("cIsBuy").equals("0") == false)
//						{
//							hm.put("cNcnBuyYn", "N");
//						}
//					}
					
					msg	= " svc[" + String.format("%-20s", API_LOG_NAME) + "] 엔스크린 구매한 컨텐츠가 없습니다.:"+paramVO.getSaId()+","+paramVO.getStbMac()+")"; 
					imcsLog.serviceLog(msg, methodName, methodLine);
				}
			}

			imcsLog.timeLog("엔스크린 구매 여부 조회", String.valueOf(tp1 - tp2), methodName, methodLine);
			
			tp2 = System.currentTimeMillis();
			
			// 2019.08.09 - 아이들나라3.0_2차 : INPUT PARAM album_flag가 P(유무료 조회) 가 아니라면 조회한다.
			if(paramVO.getAlbumFlag().equals("P") == false)
			{
				// reading_album
				for(HashMap<String, String> hm : listCopyAlbumInfo)
				{
					reading_album.add(hm.get("ALBUM_ID"));
				}
				
				if(reading_album.size() > 0)
				{
					paramVO.setArrSvodAlbumIds(null);
					paramVO.setArrSvodAlbumIds(reading_album);
					
					// SVOD에 편성된 앨범의 경우 가입 여부를 확인한다.
					try {
						listReadingCnt = this.getNSAlbumStatDao.listReadingCnt(paramVO);
					} catch(Exception ex) {					
						msg	= " svc[" + String.format("%-20s", API_LOG_NAME) + "] SVOD에 편성된 앨범의 경우 가입 여부를 확인 조회 오류:"+paramVO.getSaId()+","+paramVO.getStbMac()+")"; 
						imcsLog.serviceLog(msg, methodName, methodLine);
					}
					
					if(listReadingCnt != null && listReadingCnt.size() > 0)
					{
						// 가입자의 앨범 시청 여부 및 이어보기 시간 정보 저장
						for(HashMap<String, String> readHm : listReadingCnt)
						{
							String szTempAlbumID = readHm.get("TEMP_ALBUM_ID");
							String szReadCount = readHm.get("READ_COUNT");
							
							for(HashMap<String, String> hm : listCopyAlbumInfo)
							{
								if(szTempAlbumID.equals(hm.get("ALBUM_ID")))
								{
									hm.put("cReadingCount", szReadCount);
									break;
								}
							}
						}
					}
				}
			}
			
			imcsLog.timeLog("시청횟수 조회", String.valueOf(tp2 - tp1), methodName, methodLine);

			for(int i = 0; i < listLastData.size(); i++)
			{
				HashMap<String, String> hmLast = listLastData.get(i);
				
				for(int k = 0; k < listCopyAlbumInfo.size(); k++)
				{
					HashMap<String, String> hm = listCopyAlbumInfo.get(k);
					
					if(hmLast.get("arrAlbumId").equals(hm.get("ALBUM_ID")))
					{
						hmLast.put("cSubcription_yn", hm.get("cSubcription_yn"));
						hmLast.put("cIsSvodOnly", hm.get("cIsSvodOnly"));
						hmLast.put("cIsBuy", hm.get("cIsBuy"));
						hmLast.put("cDataFreeBuy", hm.get("cDataFreeBuy"));
						hmLast.put("szProductTypeMin", hm.get("NPRODUCTTYPEMIN"));
						hmLast.put("cNscreenYn", hm.get("NSCREEN_YN"));
						hmLast.put("cNcnBuyYn", hm.get("cNcnBuyYn"));
						hmLast.put("cNcnSubscribeYn", hm.get("cNcnSubscribeYn"));
						hmLast.put("cReadingCount", hm.get("cReadingCount"));
						
						if(hmLast.get("cIsBuy").equals("1")
								&& hmLast.get("cNcnBuyYn").equals("N")
								&& hmLast.get("cDataFreeBuy").equals("1"))
						{
							hmLast.put("cTotalBuyYn", "1");
						}
						else
							hmLast.put("cTotalBuyYn", "0");
						
						if(hmLast.get("cSubcription_yn").equals("1") && hmLast.get("cNcnSubscribeYn").equals("N"))
							hmLast.put("cTotalSubcriptionYn", "1");
						else
							hmLast.put("cTotalSubcriptionYn", "0");
						break;
					}
				}
			}
			
			for(HashMap<String, String> hmLast : listLastData)
			{				
				if(hmLast.get("cTotalBuyYn").equals("1") && hmLast.get("cTotalSubcriptionYn").equals("1"))
					hmLast.put("cPayYn", "Y");
				else
					hmLast.put("cPayYn", "N");
				
				// 2019.07.09 FVOD에 속한 앨범의 경우, 미구매 처리 한다.
				if(hmLast.get("szProductTypeMin").equals("0"))
				{
					hmLast.put("cTotalBuyYn", "1");
					hmLast.put("cPayYn", "N");
				}
				
				sb.append(String.format("%s|%s|%s|%s|%s|%s|\f",
						hmLast.get("arrAlbumId"), 	hmLast.get("cPayYn"),		hmLast.get("cTotalSubcriptionYn"),
						hmLast.get("cIsSvodOnly"),	hmLast.get("cTotalBuyYn"),	hmLast.get("cReadingCount")));
			}
			
			sb.insert(0, "0||\f"); // 헤더 세팅
			
			resultVO.setResult(sb.toString());
		}
		catch(ImcsException ce)
		{
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", ce.getClass().getName() + ":" + ce.getMessage());
			
			paramVO.setResultCode("31000000");
		}
		catch(Exception e)
		{
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			
			paramVO.setResultCode("31000000");
		}
		finally
		{			
			if(hmCustPairingChk != null) hmCustPairingChk.clear();
			if(listAlbumInfo != null) listAlbumInfo.clear();
			if(listSvodCheck != null) listSvodCheck.clear();
			if(listContsBuyCheck != null) listContsBuyCheck.clear();
			if(listCopyAlbumInfo != null) listCopyAlbumInfo.clear();
			if(listNScreenContsBuyCheck != null) listNScreenContsBuyCheck.clear();
			if(listReadingCnt != null) listReadingCnt.clear();
			if(subcription_arr != null) subcription_arr.clear();
			if(buy_arr != null) buy_arr.clear();
			if(reading_album != null) reading_album.clear();
			if(listLastData != null) listLastData.clear();
			if(sb != null) sb.setLength(0);
			
			resultVO.setResultCode(paramVO.getResultCode());
			
			msg	= " svc[" + String.format("%-20s", API_LOG_NAME) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
			
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultVO;
	}
	
	/**
	 * 엔스크린(NSCREEN) 가입 여부 체크 - 상품코드 리스트
	 * @param paramVO
	 * @throws Exception
	 */
	private ArrayList<String> kidProductCd(GetNSAlbumStatRequestVO paramVO) throws Exception
	{
		StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		ArrayList<String> list = new ArrayList<>();
		String szMsg = "";
		
		try
		{
			list = getNSAlbumStatDao.kidProductCd(paramVO);
			
			if(list == null || list.isEmpty())
			{
				list = new ArrayList<>();
			}
			
		}
		catch(Exception e)
		{
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return list;
	}
	
}




























