package kr.co.wincom.imcs.api.getNSKidsList;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.util.internal.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.FileUtil;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.handler.ImcsException;

/**
 * 
 * SCREEN_TYPE은 임시로 get_INFO_CAT() 메소드에 설정함.
 * 캐쉬 파일에 있는 것은 get_cache_file_read() 메소드에서 그냥 읽어오면 됨.
 *
 */
@Service
public class GetNSKidsListService
{
	private final static String API_LOG_NAME = "000/getNSKidsList";
	
	private Log imcsLogger	= LogFactory.getLog("API_getNSKidsList");
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private GetNSMakeKidsListService getNSMakeKidsListService;
	
	@Autowired
	private GetNSKidsListDao getNSKidsListDao;
	
	private int WATCH_C_CNT = 10; /* 캐릭터관(2Depth) 시청이력 조회 개수 */
	private int WATCH_B_CNT = 3;  /* 책읽어주는TV(2Depth) 시청이력 조회 개수 */
	private int WATCH_X_CNT = 6;  /* 책읽어주는TV(3Depth이하) 시청이력 조회 개수  */
	private int HOT_C_CNT = 10;   /* 캐릭터관(2Depth) 인기컨텐츠 조회 개수 */
	private int HOT_B_CNT = 10;	  /* 책읽어주는TV(3Depth이하) 인기컨텐츠 조회 개수 */
	
	private int RETRY_CNT = 5;
	
	// NAS에서 로컬로 캐쉬파일 복사할 때 사용(Thread-Safe 함)
	private ConcurrentHashMap<String, String> copyChm = new ConcurrentHashMap<>();
	
	public GetNSKidsListResultVO getNSKidsList(GetNSKidsListRequestVO paramVO)
	{
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog("getNSKidsList service call");
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		GetNSKidsListResultVO resultVO = new GetNSKidsListResultVO();
		
		HashMap<String, String> userInfoMap = null;
		HashMap<String, String> hmCatType = null;
		List<HashMap<String, String>> listCatInfo = null;
		ArrayList<HashMap<String, String>> resultList = new ArrayList<>();
		HashMap<String, String> kids_header = new HashMap<>();
		
		String msg = "";		
		long tp1 = 0, tp2 = 0;
		
		String idx_sa = paramVO.getSaId().substring(paramVO.getSaId().length() - 2);
		int c_idx_sa = Integer.parseInt(idx_sa) % 33;
		
		String c_img_resize_server = "";
		String c_cat_img_server = "";
		String c_img_url_tmp = "";
		
		String CFLAG = "";
		String SFLAG = "";
		String BOOKCATID = ""; // 책읽어주는TV 2Depth일 경우 category_id는 특정 카테고리 정보 기준으로 편성 정보를 조회한다.
		String PARENTCATID = "";
		String customFlag = ""; // 개인정보 제어 Flag (0:개인정보 조회 / 1:개인정보 미조회<DEFAULT:>)
		
		StringBuilder sbHead = new StringBuilder();
		StringBuilder sbBody = new StringBuilder();
		StringBuilder sbGuide_1 = new StringBuilder(); // 캐스트이즈 가이드 영상
		StringBuilder sbGuide_2 = new StringBuilder(); // 온누리넷 가이드 영상
		StringBuilder sbMsg = new StringBuilder();
		
		try {
			c_img_resize_server	= this.commonService.getImgReplaceUrl2("img_resize_server", "getNSKidsList");
			c_cat_img_server = this.commonService.getImgReplaceUrl2("img_cat_server", "getNSKidsList");
			
			kids_header.put("c_cat_img_server", c_cat_img_server);
		} catch(Exception e) {
			throw new ImcsException();
		}
		
		try
		{
			CFLAG = this.commonService.getServerProperties("getNSKidsList.CFLAG");
			SFLAG = this.commonService.getServerProperties("getNSKidsList.SFLAG");
			BOOKCATID = this.commonService.getServerProperties("getNSKidsList.BOOKCATID");
			PARENTCATID = this.commonService.getServerProperties("getNSKidsList.PARENTCATID");
			
			customFlag = this.commonService.getPersonalInfoConfig();
			
			paramVO.setC_idx_sa(c_idx_sa);
			
			tp1 = System.currentTimeMillis();
			
			// 일반/테스트 사용자 확인
			try {
				userInfoMap = this.getNSKidsListDao.getUserInfo(paramVO);
				
				if(userInfoMap != null && userInfoMap.size() > 0)
				{
					paramVO.setTestSbc(userInfoMap.get("TEST_SBC"));
					paramVO.setViewFlag2(userInfoMap.get("VIEW_FLAG2"));
					paramVO.setNcnSaId(userInfoMap.get("STB_SA_ID"));
					paramVO.setNcnStbMac(userInfoMap.get("STB_MAC"));
				}
			} catch(Exception ex) {
				resultVO.setResult("1|사용자 정보 조회 오류||||||||\f");
				throw ex;
			}
			
			tp2	= System.currentTimeMillis();
			
			imcsLog.timeLog("사용자 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			tp1	= System.currentTimeMillis();
			
			// 카테고리 속성 정보 조회
			try {
				hmCatType = this.getNSKidsListDao.getCatType(paramVO);
			} catch(Exception ex) {
				resultVO.setResult("1|카테고리 정보 조회 오류||||||||\f");
				throw ex;
			}
			
			if(hmCatType != null && hmCatType.size() > 0)
			{
				paramVO.setKids_category_type(hmCatType.get("CATEGORY_TYPE"));
				paramVO.setKids_category_level(hmCatType.get("CATEGORY_LEVEL"));
				paramVO.setKids_parent_category_id(hmCatType.get("PARENT_CATEGORY_ID"));
				paramVO.setKids_category_month(hmCatType.get("CATEGORY_MONTH"));
			}
			else
			{
				imcsLog.serviceLog("카테고리 정보 없음", methodName, methodLine);
				
				resultVO.setResult("1|카테고리 정보 없음||||||||\f");
				return resultVO;
			}
			
			// 책읽어주는TV 2레벨 특정 카테고리ID SET
			if(paramVO.getKids_category_type().equals("B") && paramVO.getKids_category_level().equals("3"))
				paramVO.setC_cat_id_cache(BOOKCATID);
			
			imcsLog.timeLog("카테고리 속성 조회", String.valueOf(tp1 - tp2), methodName, methodLine);
			
			// 이미지 캐쉬 서버IP 조회
			// (2Depth 조회 시에만 getI30KidsList에서 조회, 3Depth 이상일 경우에는 getI30KidsMake에서 조회 후 캐쉬)
			if(paramVO.getKids_category_type().equals("C") && paramVO.getKids_category_level().equals("3"))
			{
				c_img_url_tmp = c_cat_img_server;
			}
			else if(paramVO.getKids_category_type().equals("B") && paramVO.getKids_category_level().equals("3"))
			{
				c_img_url_tmp = c_img_resize_server;
			}
			
			/* 캐릭터관 2레벨 */
			if(paramVO.getKids_category_type().equals("C") && paramVO.getKids_category_level().equals("3"))
			{
				try {
					listCatInfo = this.get_INFO_CAT(imcsLog, paramVO, kids_header);
				} catch(Exception ex) {
					resultVO.setResult("1|캐릭터관 카테고리 편성 정보 조회 오류||||||||\f");
					throw ex;
				}
				
				if(listCatInfo != null && listCatInfo.size() > 0)
				{
					// 2019.08.02 - 아이들나라3.0_2차 - 캐릭터관 첫번째 카테고리는 가장 첫번째에 고정으로 노출 한다.
					// HashMap 생성자를 사용해서 복사한다. (깊은 복사 아님)
					resultList.add((new HashMap<String, String>(listCatInfo.get(0))));
					
					// 최근 시청순으로 먼저 resultList에 끼워 넣는다.
					int cont_watch_cnt = this.watchResultListAdd(imcsLog, paramVO, listCatInfo, customFlag, resultList);
					
					// 인기순으로 resultList에 끼워 넣는다.
					this.hotResultListAdd(imcsLog, paramVO, listCatInfo, resultList, cont_watch_cnt);
					
					// 최근 시청 + 인기를 제외한 편성 카테고리 공통
					this.lastResultListAdd(listCatInfo, resultList);
				}
			}
			/* 선생님 추천 모든 레벨 */
			else if(paramVO.getKids_category_type().equals("T"))
			{				
				try {
					listCatInfo = this.get_INFO_CAT(imcsLog, paramVO, kids_header);
				} catch(Exception ex) {
					resultVO.setResult("1|선생님추천 카테고리 편성 정보 조회 오류||||||||\f");
					throw ex;
				}
				
				// 최근 시청 + 인기를 제외한 편성 카테고리 공통
				this.lastResultListAdd(listCatInfo, resultList);
			}
			/* 영어유치원 2, 3레벨 */
			else if(paramVO.getKids_category_type().equals("G") && Integer.parseInt(paramVO.getKids_category_level()) <= 4)
			{				
				try {
					listCatInfo = this.get_INFO_CAT(imcsLog, paramVO, kids_header);
				} catch(Exception ex) {
					resultVO.setResult("1|영어유치원 2,3레벨 카테고리 편성 정보 조회 오류||||||||\f");
					throw ex;
				}
				
				// 최근 시청 + 인기를 제외한 편성 카테고리 공통
				this.lastResultListAdd(listCatInfo, resultList);
			}
			/* 영어유치원 4레벨 이상 (캐쉬) */
			else if(paramVO.getKids_category_type().equals("G") && Integer.parseInt(paramVO.getKids_category_level()) > 4)
			{
				try {
					this.get_cat_version_level(imcsLog, paramVO);
				} catch(Exception ex) {
					resultVO.setResult("1|카테고리 버전 정보 조회 오류||||||||\f");
					throw ex;
				}
				
				tp1 = System.currentTimeMillis();
				
				listCatInfo = this.get_cache_file_read(imcsLog, paramVO, c_img_resize_server);
				
				tp2 = System.currentTimeMillis();
				
				imcsLog.timeLog("Kids Type G 3Lvl 초과 Cache File Read!!!", String.valueOf(tp2 - tp1), methodName, methodLine);
				
				if(listCatInfo != null && listCatInfo.size() > 0)
				{
					// 최근 시청 + 인기를 제외한 편성 카테고리 공통
					this.lastResultListAdd(listCatInfo, resultList);
				}
			}
			/* 책읽어주는TV 2레벨(캐쉬 사용). 2017.03.30 이후 버전 --> 2Depth 일 경우에는 편성정보 조회 O / 특정 카테고리 편성정보 조회 */
			else if(paramVO.getKids_category_type().equals("B") && paramVO.getKids_category_level().equals("3"))
			{
				try {
					this.get_cat_version_level(imcsLog, paramVO);
				} catch(Exception ex) {
					resultVO.setResult("1|카테고리 버전 정보 조회 오류||||||||\f");
					throw ex;
				}
				
				tp1 = System.currentTimeMillis();
				
				listCatInfo = this.get_cache_file_read(imcsLog, paramVO, c_img_resize_server);
				
				tp2 = System.currentTimeMillis();
				
				imcsLog.timeLog("Kids Type B 2Lvl Cache File Read!!!", String.valueOf(tp2 - tp1), methodName, methodLine);
				
				if(listCatInfo != null && listCatInfo.size() > 0)
				{
					// 최근 시청순으로 먼저 resultList에 끼워 넣는다.
					int cont_watch_cnt = this.watchResultListAdd(imcsLog, paramVO, listCatInfo, customFlag, resultList);
					
					// 인기순으로 resultList에 끼워 넣는다.
					this.hotResultListAdd(imcsLog, paramVO, listCatInfo, resultList, cont_watch_cnt);
					
					// 최근 시청 + 인기를 제외한 편성 카테고리 공통
					this.lastResultListAdd(listCatInfo, resultList);
				}
			}
			/* 2019.07.31 - 캐릭터관 / 부모교실도 최근 시청한 카테고리 정보를 달라는 것으로 보아
			 * 3Depth 카테고리 진입시에도 getI30KidsList API를 호출할 것으로 보여서 캐시 조회하도록 수정 */
			else if( (paramVO.getKids_category_type().equals("B") || paramVO.getKids_category_type().equals("C") ||
					paramVO.getKids_category_type().equals("N") ) && Integer.parseInt(paramVO.getKids_category_level()) > 3)
			{
				try {
					this.get_cat_version_level(imcsLog, paramVO);
				} catch(Exception ex) {
					resultVO.setResult("1|카테고리 버전 정보 조회 오류||||||||\f");
					throw ex;
				}
				
				tp1 = System.currentTimeMillis();
				
				listCatInfo = this.get_cache_file_read(imcsLog, paramVO, c_img_resize_server);
				
				tp2 = System.currentTimeMillis();
				
				imcsLog.timeLog("Kids Type B,C,N 2Lvl 초과 Cache File Read!!!", String.valueOf(tp2 - tp1), methodName, methodLine);
				
				if(listCatInfo != null && listCatInfo.size() > 0)
				{
					// 최근 시청순으로 먼저 resultList에 끼워 넣는다.
					this.watchResultListAdd(imcsLog, paramVO, listCatInfo, customFlag, resultList);
					
					// 최근 시청 + 인기를 제외한 편성 카테고리 공통
					this.lastResultListAdd(listCatInfo, resultList);
				}
			}
			 
			if(paramVO.getKids_category_type().equals("C") ||
					(paramVO.getKids_category_type().equals("B") && paramVO.getKids_category_level().equals("3")))
			{
				for(HashMap<String, String> hm : resultList)
				{
					hm.put("c_img_url", c_img_url_tmp);
				}
			}				
			
			// 페이징
			this.response_Pageing(imcsLog, paramVO, resultList, kids_header, sbHead, sbBody, sbGuide_1, sbGuide_2);
			
			// 선생님추천, 영어유치원인 경우 가이드 영상 파일 정보 제공
			if(paramVO.getKids_category_type().equals("T") || paramVO.getKids_category_type().equals("G"))
			{
				sbBody.append(sbGuide_1.toString() + sbGuide_2.toString());
				
				// 영어유치원인 경우 MSG 정보 제공
				if(paramVO.getKids_category_type().equals("G"))
				{
					try {
						List<HashMap<String, String>> list = this.getNSKidsListDao.listMessage_G();
						
						for(HashMap<String, String> hm : list)
							sbMsg.append(String.format("MSG|%s|%s|\f", hm.get("msg_type"), hm.get("message")));
					} catch(Exception ex) {
						sbMsg.append("MSG|00|현재 레벨보다 낮은 수준의 콘텐츠를 이용하고 있습니다. 우리 아이의 레벨테스트 결과를 확인해 보세요.|\f");
						sbMsg.append("MSG|10|아이들나라에서 추천하는 맞춤형 콘텐츠입니다. 우리 아이의 레벨테스트 결과를 확인해 보세요.|\f");
						sbMsg.append("MSG|20|현재 더 높은 수준의 콘텐츠를 이용하고 있습니다. 우리 아이의 레벨테스트 결과를 확인해 보세요.|\f");
						sbMsg.append("MSG|90|현재 레벨에 맞는 콘텐츠가 없습니다. 다른 콘텐츠를 이용해 보세요.|\f");
						
						imcsLog.errorLog(methodName + "-E", ex.getClass().getName() + ":" + ex.getMessage());
					}
					
					sbBody.append(sbMsg.toString());
				}
			}
			
			resultVO.setResult(sbHead.toString() + sbBody.toString().replace("img_resize_server", c_img_resize_server));
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
			userInfoMap = null;
			hmCatType = null;
			listCatInfo = null;
			resultList = null;
			kids_header = null;
			
			sbHead = null;
			sbBody = null;
			sbGuide_1 = null;
			sbGuide_2 = null;
			sbMsg = null;
			
			resultVO.setResultCode(paramVO.getResultCode());
			
			msg	= " svc[" + String.format("%-20s", API_LOG_NAME) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
			
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultVO;
	}
	
	/**
	 * 편성 카테고리 조회
	 * @return
	 * @throws Exception
	 */
	private List<HashMap<String, String>> get_INFO_CAT(IMCSLog imcsLog, GetNSKidsListRequestVO paramVO,
			HashMap<String, String> kids_header) throws Exception
	{
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		List<HashMap<String, String>> list = null;
		long tp1 = 0, tp2 = 0;
		
		int cat_tot_cnt = 0;
		
		// 캐릭터관 2레벨
		if(paramVO.getKids_category_type().equals("C") && paramVO.getKids_category_level().equals("3"))
		{
			tp1 = System.currentTimeMillis();
			
			list = this.getNSKidsListDao.listCategoryInfo_C_2Lvl(paramVO);
			
			tp2 = System.currentTimeMillis();
			
			imcsLog.timeLog("캐릭터관 2레벨 카테고리 편성 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
		}
		// 선생님추천 2레벨
		else if(paramVO.getKids_category_type().equals("T") && paramVO.getKids_category_level().equals("3"))
		{
			tp1 = System.currentTimeMillis();
			
			list = this.getNSKidsListDao.listCategoryInfo_T_2Lvl(paramVO);
			
			tp2 = System.currentTimeMillis();
			
			imcsLog.timeLog("선생님추천 2레벨 카테고리 편성 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
		}
		// 선생님추천 3레벨 이상
		else if(paramVO.getKids_category_type().equals("T") && Integer.parseInt(paramVO.getKids_category_level()) > 3)
		{
			tp1 = System.currentTimeMillis();
			
			list = this.getNSKidsListDao.listCategoryInfo_T_3OverLvl(paramVO);
			
			tp2 = System.currentTimeMillis();
			
			imcsLog.timeLog("선생님추천 3레벨 이상 카테고리 편성 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
		}
		// 영어유치원 2, 3레벨
		else if(paramVO.getKids_category_type().equals("G") && Integer.parseInt(paramVO.getKids_category_level()) <= 4)
		{
			tp1 = System.currentTimeMillis();
			
			list = this.getNSKidsListDao.listCategoryInfo_G_2_3Lvl(paramVO);
			
			tp2 = System.currentTimeMillis();
			
			imcsLog.timeLog("영어유치원 2, 3레벨 카테고리 편성 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
		}
		
		if(list != null && list.size() > 0)
		{
			for(HashMap<String, String> hm : list)
			{
				hm.put("c_3d_yn", "N");
				
				// 일단 SCREEN_TYPE 은 N 으로 설정
				hm.put("c_screen_type", "N");
				
				if(hm.get("c_result_type").equals("CAT"))
				{
					// 카테고리 조회 시, 5번째 필드 CAT_ID는 2번째 필드 CONTENTS_ID와 동일하게 넣어준다.
					hm.put("c_cat_id", hm.get("c_contents_id"));
					
					if(StringUtils.isNotBlank(hm.get("c_recommend_id")) && StringUtils.isNotBlank(hm.get("c_category_flag")))
					{
						if("A".equals(hm.get("c_category_flag").trim()))
						{
							hm.put("c_type", "APP");
						}
						else if("R".equals(hm.get("c_category_flag").trim()))
						{
							hm.put("c_type", "RCM");
						}
					}
				}
				
				if("L".equals(hm.get("c_category_flag").trim()) == false)
				{
					hm.put("c_category_flag", "");
				}
				
				if(paramVO.getKids_category_type().equals("T") || paramVO.getKids_category_type().equals("G"))
				{
					hm.put("c_img_url", kids_header.get("c_cat_img_server"));
					kids_header.put("cat_image_url", kids_header.get("c_cat_img_server"));
					
					if(hm.get("c_result_type").equals("ALB"))
					{
						if(StringUtils.isBlank(kids_header.get("guide_asset_id_1")))
						{
							// 선생님추천, 영어유치원의 가이드영상 M3U8은 쿼리에서
							// M01202I046PPV00H.m3u8,M01202I046PPV00HN.m3u8 이렇게 출력함.
							// 캐스트이즈 M3U8, 온누리넷 M3U8 임.
							String strContsId = hm.get("c_contents_id");
							String strContsNm = hm.get("c_contents_name");
							String[] arrM3u8 = StringUtils.split(strContsId, ",");
							
							kids_header.put("guide_album_id", hm.get("last_album_id"));
							
							kids_header.put("guide_asset_id_1", arrM3u8[0]);
							kids_header.put("guide_album_name_1", strContsNm);
							
							kids_header.put("guide_asset_id_2", arrM3u8[1]);
							kids_header.put("guide_album_name_2", strContsNm);
						}
						else
						{
							// 20190625 - 선생님 추천 티칭 가이드 영상은 하나이므로, 다수개의 티칭 가이드 영상이 있을 시
							// 첫번째 티칭 가이드 영상 외에는 전달하지 않는다.
							if(paramVO.getKids_category_type().equals("G"))
							{
								// 선생님추천, 영어유치원의 가이드영상 M3U8은 쿼리에서
								// M01202I046PPV00H.m3u8,M01202I046PPV00HN.m3u8 이렇게 출력함.
								// 캐스트이즈 M3U8, 온누리넷 M3U8 임.
								String strContsId = hm.get("c_contents_id");
								String strContsNm = hm.get("c_contents_name");
								String[] arrM3u8 = StringUtils.split(strContsId, ",");
								
								kids_header.put("guide_album_id",
										String.format("%s\b%s", kids_header.get("guide_album_id"), hm.get("last_album_id")));
								
								kids_header.put("guide_asset_id_1",
										String.format("%s\b%s", kids_header.get("guide_asset_id_1"), arrM3u8[0]));
								kids_header.put("guide_album_name_1", 
										String.format("%s\b%s", kids_header.get("guide_album_name_1"), strContsNm));
								
								kids_header.put("guide_asset_id_2",
										String.format("%s\b%s", kids_header.get("guide_asset_id_2"), arrM3u8[1]));
								kids_header.put("guide_album_name_2", 
										String.format("%s\b%s", kids_header.get("guide_album_name_2"), strContsNm));
							}
						}
						
						continue;
					}
					
					if(paramVO.getKids_category_type().equals("T") && hm.get("c_result_type").equals("CAT"))
					{
						if(StringUtils.isNotBlank(hm.get("c_cat_level_month")) && hm.get("c_cat_level").equals("4"))
						{
							int iCategory_Month = Integer.parseInt(paramVO.getKids_category_month());
							int iCat_Level_Month = Integer.parseInt(hm.get("c_cat_level_month"));
							
							// 12월 다음 월은 1월인데.. 숫자 비교로는 1이 더 작으므로 전월이라고 판단하기 때문에 if문 추가
							// 마찬가지로 11월 조회시... 1월 대비하여 12월 비교하게 넣어놨는데..
							// 11월일 때에도 12가 이전달로 판단하기 때문에.. 1월일 때에만 12가 작다고 명시
							if(iCategory_Month != iCat_Level_Month
									&& ( iCategory_Month > iCat_Level_Month || (iCategory_Month == 1 && iCat_Level_Month == 12) )
									&& !(iCategory_Month == 12 && iCat_Level_Month == 1))
							{
								String strCatId = hm.get("c_cat_id");
								
								kids_header.put("prior_category_id", strCatId);
								kids_header.put("prior_category_name", String.format("%s %s", hm.get("c_cat_sub_name"), hm.get("c_contents_name")));
								
								// 선생님 추천인 경우 헤더에는 이전월과 이후월, Body 에는 현재월 또는 유저가 선택한 월이 출력되어야함.
								// 그래서 Body에 들어가는 월만 출력하기 위해 body_appdnd 를 사용함.
								hm.put("body_append", "X");
								
								continue;
							}
							else if(iCategory_Month != iCat_Level_Month && (iCategory_Month < iCat_Level_Month || iCat_Level_Month == 1) )
							{
								String strCatId = hm.get("c_cat_id");
								
								kids_header.put("next_category_id", strCatId);
								kids_header.put("next_category_name", String.format("%s %s", hm.get("c_cat_sub_name"), hm.get("c_contents_name")));
								
								// 선생님 추천인 경우 헤더에는 이전월과 이후월, Body 에는 현재월 또는 유저가 선택한 월이 출력되어야함.
								// 그래서 Body에 들어가는 월만 출력하기 위해 body_appdnd 를 사용함.
								hm.put("body_append", "X");
								
								continue;
							}
						}
					}
				}
				
				// 영어 유치원에서만 레벨 정보를 제공 한다.
				if(paramVO.getCategoryType().equals("G") == false)
					hm.put("c_cat_level_month", "");
				
				if(hm.get("c_result_type").equals("CAT"))
					cat_tot_cnt++;
			}
		}
		
		return list;
	}
	
	/**
	 * 시청이력 조회
	 * @param imcsLog
	 * @param paramVO
	 * @param methodName
	 * @param methodLine
	 * @return
	 * @throws Exception
	 */
	private List<HashMap<String, String>> get_WATCH_CAT(IMCSLog imcsLog, GetNSKidsListRequestVO paramVO) throws Exception
	{
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		List<HashMap<String, String>> list = null;
		long tp1 = 0, tp2 = 0;
		
		tp1 = System.currentTimeMillis();
		
		list = this.getNSKidsListDao.listWatchInfo(paramVO);
		
		tp2 = System.currentTimeMillis();
		
		imcsLog.timeLog("시청순 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
		
		return list;
	}
	
	/**
	 * 인기순 조회
	 * @param imcsLog
	 * @param paramVO
	 * @param methodName
	 * @param methodLine
	 * @return
	 * @throws Exception
	 */
	private List<HashMap<String, String>> get_HOT_CAT(IMCSLog imcsLog, GetNSKidsListRequestVO paramVO) throws Exception
	{
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		List<HashMap<String, String>> list = null;
		long tp1 = 0, tp2 = 0;
		
		tp1 = System.currentTimeMillis();
		
		if(paramVO.getKids_category_type().equals("C"))
			list = this.getNSKidsListDao.listHotCatInfo_C();
		else if(paramVO.getKids_category_type().equals("B"))
			list = this.getNSKidsListDao.listHotCatInfo_B();
		
		tp2 = System.currentTimeMillis();
		
		imcsLog.timeLog("인기순 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
		
		return list;
	}
	
	/**
	 * 시청/인기 데이터가 캐시에 없을 때 조회
	 * @param imcsLog
	 * @param paramVO
	 * @param methodName
	 * @param methodLine
	 * @return
	 * @throws Exception
	 */
	private HashMap<String, String> get_WH_INFO_CONT(IMCSLog imcsLog, GetNSKidsListRequestVO paramVO) throws Exception
	{
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		List<HashMap<String, String>> list = null;
		HashMap<String, String> hmWH = null;
		String c_price_desc_tmp = "";
		long tp1 = 0, tp2 = 0;
		
		tp1 = System.currentTimeMillis();
		
		list = this.getNSKidsListDao.listWatchHotInfoCont(paramVO);
		
		if(list != null && list.size() > 0)
		{
			hmWH = list.get(0);
			
			hmWH.put("c_is_51_ch", "N");
			
			if(hmWH.get("c_product_type").equals("0"))
			{
				if(hmWH.get("c_album_type").equals("HD") || hmWH.get("c_album_type").equals("SH"))
					c_price_desc_tmp = "HD ";
				else if(hmWH.get("c_album_type").equals("3D"))
					c_price_desc_tmp = "3D ";
				else if(hmWH.get("c_album_type").equals("SD"))
					c_price_desc_tmp = "SD ";
				
				if(hmWH.get("c_album_type").equals("PR") == false)
					c_price_desc_tmp = String.format("%s무료", c_price_desc_tmp);
				
				hmWH.put("c_price_desc", c_price_desc_tmp);
			}
			else
			{
				if(hmWH.get("c_album_type").equals("HD") || hmWH.get("c_album_type").equals("SH"))
					c_price_desc_tmp = "HD ";
				else if(hmWH.get("c_album_type").equals("PR"))
					c_price_desc_tmp = "예고편 ";
				else if(hmWH.get("c_album_type").equals("3D"))
					c_price_desc_tmp = "3D ";
				else if(hmWH.get("c_album_type").equals("SD"))
					c_price_desc_tmp = "SD ";
				
				c_price_desc_tmp = String.format("%s%s원", c_price_desc_tmp, hmWH.get("c_price"));
				
				hmWH.put("c_price_desc", c_price_desc_tmp);
			}
			
			if(hmWH.get("c_51ch").equals("DOLBY 5.1"))
				hmWH.put("c_is_51_ch", "Y");
		}
		
		tp2 = System.currentTimeMillis();
		
		imcsLog.timeLog("시청/인기 컨텐츠 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
		
		return hmWH;
	}
	
	/**
	 * 시청순. resultList 에 끼워 넣기 위해 중복체크를 한다.
	 * @param watchCatOrHot
	 * @param listCatInfo
	 * @param resultList
	 * @return
	 */
	private MyKidsVO chkWatchJungbokContsId(HashMap<String, String> watchCat,
			List<HashMap<String, String>> listCatInfo,
			ArrayList<HashMap<String, String>> resultList)
	{
		MyKidsVO vo = new MyKidsVO();
		
		String contsId = watchCat.get("tmp_cont_id");
		
		// 먼저 편성 카테고리 정보를 조회하고 resultList 를 조회한다.
		for(int i = 0; i < listCatInfo.size(); i++)
		{
			HashMap<String, String> hm = listCatInfo.get(i);
			
			if(contsId.equals(hm.get("c_contents_id")))
			{
				vo.setIdx(i);
				vo.setCatJungBokYn("Y");
				
				// 편성 카테고리 정보와 같은 것이 있으면 resultList 에 있는지 한번더 조회
				for(HashMap<String, String> resultHm : resultList)
				{
					if(contsId.equals(resultHm.get("c_contents_id")))
					{
						vo.setResultJungBokYn("Y");
						
						return vo;
					}
				}
			}
		}
		
		return vo;
	}
	
	/**
	 * 인기순. resultList 에 끼워 넣기 위해 중복체크를 한다.
	 * @param watchCatOrHot
	 * @param listCatInfo
	 * @param resultList
	 * @return
	 */
	private MyKidsVO chkHotJungbokContsId(GetNSKidsListRequestVO paramVO, HashMap<String, String> hotCat,
			List<HashMap<String, String>> listCatInfo,
			ArrayList<HashMap<String, String>> resultList)
	{
		MyKidsVO vo = new MyKidsVO();
		
		String contsId = "";
		
		if(paramVO.getKids_category_type().equals("C"))
			contsId = hotCat.get("c_character_cont_id");
		else if(paramVO.getKids_category_type().equals("B"))
			contsId = hotCat.get("c_book_cont_id");
		
		// 먼저 편성 카테고리 정보를 조회하고 resultList 를 조회한다.
		for(int i = 0; i < listCatInfo.size(); i++)
		{
			HashMap<String, String> hm = listCatInfo.get(i);
			
			if(contsId.equals(hm.get("c_contents_id")))
			{
				vo.setIdx(i);
				vo.setCatJungBokYn("Y");
				
				// 편성 카테고리 정보와 같은 것이 있으면 resultList 에 있는지 한번더 조회
				for(HashMap<String, String> resultHm : resultList)
				{
					if(contsId.equals(resultHm.get("c_contents_id")))
					{
						vo.setResultJungBokYn("Y");
						
						return vo;
					}
				}
			}
		}
		
		return vo;
	}
	
	/**
	 * 카테고리 버전 조회(2Depth)
	 * @param imcsLog
	 * @param paramVO
	 * @param methodName
	 * @param methodLine
	 * @throws Exception
	 */
	private void get_cat_version_level(IMCSLog imcsLog, GetNSKidsListRequestVO paramVO) throws Exception
	{
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		long tp1 = 0, tp2 = 0;
		
		tp1 = System.currentTimeMillis();
		
		String pCatId = this.getNSKidsListDao.getCatVersionLevel_pCatId(paramVO);
		
		tp2 = System.currentTimeMillis();
		
		imcsLog.timeLog("카테고리 버전 조회(VERSION_INFO_01)", String.valueOf(tp2 - tp1), methodName, methodLine);
		
		if(StringUtils.isBlank(pCatId))
		{	        
	        paramVO.setC_sub_version(paramVO.getC_cat_id_cache());
	        paramVO.setC_sub_P_version(paramVO.getC_cat_id_cache());
	        paramVO.setC_sub_PP_version(paramVO.getC_cat_id_cache());
		}
		else
		{
			paramVO.setC_parent_category(pCatId);
			
			HashMap<String, String> hm = this.getNSKidsListDao.getCatVersionLevel_subVer(paramVO);
			
			tp1 = System.currentTimeMillis();
			
			imcsLog.timeLog("카테고리 버전 조회(VERSION_INFO_02)", String.valueOf(tp1 - tp2), methodName, methodLine);
			
			if(hm == null || hm.size() == 0)
			{
				paramVO.setC_sub_version(paramVO.getC_cat_id_cache());
		        paramVO.setC_sub_P_version(paramVO.getC_cat_id_cache());
		        paramVO.setC_sub_PP_version(paramVO.getC_cat_id_cache());
			}
			else
			{
				paramVO.setC_sub_version(hm.get("c_sub_version"));
		        paramVO.setC_sub_P_version(hm.get("c_sub_P_version"));
		        paramVO.setC_sub_PP_version(hm.get("c_sub_PP_version"));
			}
		}
	}
	
	/**
	 * 캐쉬파일을 Read
	 * 
	 * localPath : C:/Imcs/cache/getNSKidsList
	 * localFilePath : C:/Imcs/cache/getNSKidsList/Ver1-KIDS_1200-VS20191021001-TY.res
	 */
	private List<HashMap<String, String>> get_cache_file_read(IMCSLog imcsLog, GetNSKidsListRequestVO paramVO, String c_img_resize_server) throws Exception
	{
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		List<HashMap<String, String>> list = new ArrayList<>();;
		String retVal = "";
		
		String localPath = this.commonService.getCachePath("LOCAL", API_LOG_NAME.split("/")[1], imcsLog);
		String nasPath = this.commonService.getCachePath("NAS", API_LOG_NAME.split("/")[1], imcsLog);
		
		String localFilePath = String.format("%s/Ver1-KIDS_%s-V%s-T%s.res", 
				localPath, paramVO.getC_cat_id_cache(), paramVO.getC_sub_version(), paramVO.getTestSbc());
		
		String nasFilePath = String.format("%s/Ver1-KIDS_%s-V%s-T%s.res", 
				nasPath, paramVO.getC_cat_id_cache(), paramVO.getC_sub_version(), paramVO.getTestSbc());
		
		String lockNasFilePath = new String(nasFilePath.replace(".res", ".lock"));
		
		// 캐쉬파일 생성, 복사 및 파일내용 읽어서 반환
		retVal = this.getCommonCacheCreateCopy(imcsLog, paramVO, localFilePath, nasFilePath, lockNasFilePath);
		
		// 캐시 파일 응답값을 리스트에 넣는다.
		if(StringUtils.isNotBlank(retVal))
		{
			StringTokenizer st = new StringTokenizer(retVal, "\f");
			
			while(st.hasMoreTokens())
			{
				String str = st.nextToken();
				
				if(StringUtils.isNotBlank(str))
				{
					HashMap<String, String> hm = new HashMap<>();
					
					// NSC
					hm.put("c_result_type", this.commonService.getSearchStrData(str, "|", 1));
					hm.put("c_contents_id", this.commonService.getSearchStrData(str, "|", 2));
					hm.put("c_contents_name", this.commonService.getSearchStrData(str, "|", 3));
					hm.put("c_type", this.commonService.getSearchStrData(str, "|", 4));
					hm.put("c_cat_id", this.commonService.getSearchStrData(str, "|", 5));
					hm.put("c_cat_sub_name", this.commonService.getSearchStrData(str, "|", 6));
					hm.put("c_category_flag", this.commonService.getSearchStrData(str, "|", 7));
					hm.put("c_cat_level_month", this.commonService.getSearchStrData(str, "|", 8));
					hm.put("c_img_url", c_img_resize_server);
					hm.put("c_image_file", this.commonService.getSearchStrData(str, "|", 10));
					hm.put("cat_image_url", this.commonService.getSearchStrData(str, "|", 11));
					hm.put("c_animation_file", this.commonService.getSearchStrData(str, "|", 12));
					hm.put("c_recommend_id", this.commonService.getSearchStrData(str, "|", 13));
					hm.put("c_service_gb", this.commonService.getSearchStrData(str, "|", 14));
					hm.put("c_kids_grade", this.commonService.getSearchStrData(str, "|", 15));
					hm.put("c_runtime", this.commonService.getSearchStrData(str, "|", 16));
					hm.put("c_is_51_ch", this.commonService.getSearchStrData(str, "|", 17));
					hm.put("c_is_caption", this.commonService.getSearchStrData(str, "|", 18));
					hm.put("c_is_hd", this.commonService.getSearchStrData(str, "|", 19));
					hm.put("c_3d_yn", this.commonService.getSearchStrData(str, "|", 20));
					hm.put("c_watcha_point", this.commonService.getSearchStrData(str, "|", 21));
					hm.put("c_cine21_point", this.commonService.getSearchStrData(str, "|", 22));
					hm.put("c_order_gb", this.commonService.getSearchStrData(str, "|", 23));
					hm.put("c_watch_date", this.commonService.getSearchStrData(str, "|", 24));
					hm.put("c_last_watch_yn", this.commonService.getSearchStrData(str, "|", 25));
					hm.put("c_screen_type", this.commonService.getSearchStrData(str, "|", 26));
					
					list.add(hm);
				}
			}
		}
		
		return list;
	}
	
	/**
	 * 캐쉬파일 생성, 복사 및 파일내용 읽어서 반환하는 공통 메소드
	 *  
	 * @param imcsLog - 로그
	 * @param paramVO
	 * @param methodName
	 * @param methodLine
	 * @param localFilePath_org - 로컬파일패스 원본
	 * @param localFilePath - 로컬파일패스
	 * @param nasFilePath - NAS파일패스
	 * @param tmpNasFilePath - NAS lock 파일패스
	 * @return
	 * @throws Exception
	 */
	private String getCommonCacheCreateCopy(IMCSLog imcsLog, GetNSKidsListRequestVO paramVO,
			String localFilePath, String nasFilePath, String lockNasFilePath) throws Exception
	{
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		boolean isOldVersion = false;
		
		File myLocalFile = new File(localFilePath);
		File nasFile = new File(nasFilePath);
		File lockFile = new File(lockNasFilePath);
		
		UUID uuid = UUID.randomUUID();
		String strCopyKey = new String("copy-" + localFilePath);
		String strCopyVal = String.format("%s-%s", paramVO.getSaId(), uuid.toString());
		
		// 먼저 로컬에 파일이 있는지 체크
		if(myLocalFile.exists())
		{
			String tmpVal = this.copyChm.get(strCopyKey);
			
			// lock 파일이 없고 copyChm 에도 값이 없다는 것은 파일 복사가 완료 되었다는 의미
			if(lockFile.exists() == false && tmpVal == null)
			{
				imcsLog.serviceLog(String.format("[NSKids-Cache][%s][%s][%s]", localFilePath, nasFilePath, "1. 로컬 파일 읽음"), methodName, methodLine);
				
				return FileUtil.fileRead(localFilePath, "UTF-8"); // 로컬 파일 읽어서 바로 리턴
			}
			else // 이런 경우는 NAS에서 로컬로 파일복사가 진행중 또는 NAS 파일 생성중임.
			{
				imcsLog.serviceLog(String.format("[NSKids-Cache][%s][%s][%s][%s]",
						localFilePath, nasFilePath, "NAS에서 로컬로 파일복사 진행중 NAS 파일 생성중", "2. 이전 버전의 캐쉬파일 읽음"), methodName, methodLine);
				
				isOldVersion = true; // 이전 버전의 캐쉬파일을 읽어서 리턴
			}
		}
		else
		{
			// lock 파일이 있다는 것은 NAS 파일을 만들고 있는 중이라는 의미
			if(lockFile.exists())
			{
				imcsLog.serviceLog(String.format("[NSKids-Cache][%s][%s][%s][%s]",
						localFilePath, nasFilePath, "lock 파일 존재함", "3. 이전 버전의 캐쉬파일 읽음"), methodName, methodLine);
				
				isOldVersion = true; // 이전 버전의 캐쉬파일을 읽어서 리턴
			}
			else
			{
				// lock 파일이 없고 NAS 파일이 있는 경우 로컬로 파일복사
				if(nasFile.exists())
				{
					// 로컬로 파일복사 완료후 로컬파일 읽어서 리턴
					// putIfAbsent 메소드는 기존에 키값이 없으면 null 또는 자신이 입력한 value가 리턴됨. (Thread-Safe 함)
					String tmpVal = this.copyChm.putIfAbsent(strCopyKey, strCopyVal);
					
					if(tmpVal == null || strCopyVal.equals(tmpVal))
					{
						imcsLog.serviceLog(String.format("[NSKids-Cache][%s][%s][%s]", localFilePath, nasFilePath, "4. NAS에서 로컬로 파일 복사"), methodName, methodLine);
						
						Path fromPath = nasFile.toPath();
						Path toPath = myLocalFile.toPath();
						
						// 로컬로 파일 복사 진행
						// (REPLACE_EXISTING : 기존에 파일이 있으면 덮어씀, COPY_ATTRIBUTES : 파일의 모든 속성 복사)
						Files.copy(fromPath, toPath,
								StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
						
						this.copyChm.remove(strCopyKey);
						
						return FileUtil.fileRead(myLocalFile.getAbsolutePath(), "UTF-8"); // 로컬 파일 읽어서 바로 리턴
					}
					else // NSA에서 로컬로 파일복사가 진행중임.
					{
						imcsLog.serviceLog(String.format("[NSKids-Cache][%s][%s][%s][%s]",
								localFilePath, nasFilePath, "NSA에서 로컬로 파일복사 진행중", "5. 이전 버전의 캐쉬파일 읽음"), methodName, methodLine);
						
						isOldVersion = true; // 이전 버전의 캐쉬파일을 읽어서 리턴
					}
				}
				// lock 파일도 없고 NAS 파일도 없으면 lock 파일을 먼저 생성
				else
				{
					boolean isLockFileCreate = true;
					
					try {
						Files.createFile(lockFile.toPath());
					} catch(FileAlreadyExistsException faee) {
						imcsLog.serviceLog(String.format("[NSKids-Cache][%s][%s][%s]",
								localFilePath, nasFilePath, "6. FileAlreadyExistsException 오류(정상 상황)"), methodName, methodLine);
						isLockFileCreate = false;
					} catch(Exception ex) {
						imcsLog.errorLog("NSKids-Cache", "빈 파일 생성시 오류", methodName + "-E", ex.getClass().getName() + ":" + ex.getMessage());
						
						Files.deleteIfExists(lockFile.toPath());
						isLockFileCreate = false;
					}
					
					if(isLockFileCreate == true)
					{
						imcsLog.serviceLog(String.format("[NSKids-Cache][%s][%s][%s]", localFilePath, nasFilePath, "7. 새로운 캐시파일 생성"), methodName, methodLine);
						
						// 비동기로 캐쉬파일을 생성하고 이전 버전 캐쉬파일 읽어서 리턴
						this.getNSMakeKidsListService.getNSKidsList(paramVO, localFilePath, nasFilePath, lockNasFilePath);
						
						isOldVersion = true;
					}
					else // 파일이 생성되지 않았다는 것은 다른 프로세스에서 이미 파일을 만들었거나 오류 발생
					{
						imcsLog.serviceLog(String.format("[NSKids-Cache][%s][%s][%s][%s]",
								localFilePath, nasFilePath, "다른 프로세스에서 이미 새로운 파일을 만들었거나 또는 오류", "8. 이전 버전의 캐쉬파일 읽음"), methodName, methodLine);
						
						isOldVersion = true; // 이전 버전의 캐쉬파일을 읽어서 리턴
					}
				}
			}
		}
		
		// 이전 버전의 캐쉬 파일을 읽어서 리턴
		if(isOldVersion == true)
		{
			// 1. 이전 버전의 캐쉬 파일을 찾음
			String oldPVerPath = localFilePath.replace(String.format("-V%s-", paramVO.getC_sub_version()), String.format("-V%s-", paramVO.getC_sub_P_version()));
			
			File oldPVerFile = new File(oldPVerPath);
			
			if(oldPVerFile.exists())
			{				
				imcsLog.serviceLog(String.format("[NSKids-Cache][%s][%s]", oldPVerPath, "9. Previous version cache Read"), methodName, methodLine);
				
				return FileUtil.fileRead(oldPVerPath, "UTF-8");
			}
			else // 이전 버전의 이전 버전을 찾음.
			{
				String oldPPVerPath = localFilePath.replace(String.format("-V%s-", paramVO.getC_sub_version()), String.format("-V%s-", paramVO.getC_sub_PP_version()));
				
				File oldPPVerFile = new File(oldPPVerPath);
				
				if(oldPPVerFile.exists())
				{
					imcsLog.serviceLog(String.format("[NSKids-Cache][%s][%s]", oldPPVerPath, "10. Previous-Previous version cache Read"), methodName, methodLine);
					
					return FileUtil.fileRead(oldPPVerPath, "UTF-8");
				}
				else // 이전버전의 이전버전이 없으면 5초동안 retry 한다.
				{
					for(int i = 0; i < RETRY_CNT; i++)
					{
						Thread.sleep(1000);
						
						String tmpVal = this.copyChm.get(strCopyKey);
						
						if(lockFile.exists() == false && myLocalFile.exists() && tmpVal == null)
						{
							imcsLog.serviceLog(String.format("[NSKids-Cache][%s][%s][%s]", localFilePath, nasFilePath, "11. 로컬 파일 읽음"), methodName, methodLine);
							
							return FileUtil.fileRead(localFilePath, "UTF-8"); // 로컬 파일 읽어서 바로 리턴
						}
					}
				}
			}
		}
		
		return "";
	}
	
	/**
	 * 최근 시청순으로 먼저 resultList에 끼워 넣는다.
	 * @throws Exception
	 */
	private int watchResultListAdd(IMCSLog imcsLog,
			GetNSKidsListRequestVO paramVO,
			List<HashMap<String, String>> listCatInfo,
			String customFlag, ArrayList<HashMap<String, String>> resultList) throws Exception
	{
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		List<HashMap<String, String>> listWatchCat = null;
		
		int cont_watch_cnt = 0;
		
		if(customFlag.equals("1") == false)
		{
			try {
				listWatchCat = this.get_WATCH_CAT(imcsLog, paramVO);
			} catch(Exception ex) {
				imcsLog.serviceLog("카테고리 최근 이력 조회 오류", methodName, methodLine);
				imcsLog.errorLog(methodName + "-E", ex.getClass().getName() + ":" + ex.getMessage());
			}
			
			int match_cnt = 0;
			
			// 시청순으로 먼저 resultList 객체에 끼워 넣는다.
			if(listWatchCat != null)
			{
				for(HashMap<String, String> watchCat : listWatchCat)
				{
					MyKidsVO myvo = this.chkWatchJungbokContsId(watchCat, listCatInfo, resultList);
					
					if(myvo.getResultJungBokYn().equals("N") && myvo.getCatJungBokYn().equals("Y"))
					{
						match_cnt++;
						
						HashMap<String, String> hmCat = listCatInfo.get(myvo.getIdx());
						
						hmCat.put("c_order_gb", watchCat.get("tmp_cont_type"));
						hmCat.put("c_watch_date", watchCat.get("tmp_cont_watch_date"));
						hmCat.put("c_screen_type", watchCat.get("screen_type"));
						
						resultList.add(hmCat);
					}
					// 책읽어주는TV 2Depth 일때 시청이력 정보가 특정 카테고리에 존재하는지 체크
					// (존재하면 캐시사용, 존재하지 않으면 컨텐츠 정보 조회)
					// 시청/인기순 데이터가 캐시에 없을 때 조회함
					else if(myvo.getResultJungBokYn().equals("N") && myvo.getCatJungBokYn().equals("N"))
					{
						if(paramVO.getKids_category_type().equals("B") && paramVO.getKids_category_level().equals("3"))
						{
							try {
								paramVO.setC_cont_id(watchCat.get("tmp_cont_id"));
								
								HashMap<String, String> hmWH = this.get_WH_INFO_CONT(imcsLog, paramVO);
								
								if(hmWH != null && hmWH.size() > 0)
								{
									match_cnt++;
									
									hmWH.put("c_order_gb", watchCat.get("tmp_cont_type"));
									hmWH.put("c_watch_date", watchCat.get("tmp_cont_watch_date"));
									hmWH.put("c_screen_type", watchCat.get("screen_type"));
									
									// 포스터 이미지
									if(StringUtils.isNotBlank(paramVO.getC_cont_id()))
									{
										HashMap<String, String> hmPoster = this.getNSKidsListDao.getContsPoster(paramVO.getC_cont_id());
										
										if(hmPoster != null && hmPoster.size() > 0)
										{
											hmWH.put("c_img_file_name_h", hmPoster.get("c_img_file_name_h"));
											hmWH.put("c_img_file_name_v", hmPoster.get("c_img_file_name_v"));
										}
									}
									
									resultList.add(hmWH);
								}
							} catch(Exception ex) {
								imcsLog.serviceLog("시청/인기 컨텐츠 정보 조회 오류", methodName, methodLine);
								imcsLog.errorLog(methodName + "-E", ex.getClass().getName() + ":" + ex.getMessage());
							}
						}
					}
					
					if(paramVO.getKids_category_type().equals("C") && paramVO.getKids_category_level().equals("3"))
					{
						if(match_cnt >= WATCH_C_CNT)
							break;
					}
					else if(paramVO.getKids_category_type().equals("B") && paramVO.getKids_category_level().equals("3"))
					{
						if(match_cnt >= WATCH_B_CNT)
							break;
					}
					else
					{
						if(match_cnt >= WATCH_X_CNT)
							break;
					}
					
					cont_watch_cnt++;
				}
			}
		}
		
		return cont_watch_cnt;
	}
	
	/**
	 * 인기순으로 resultList에 끼워 넣는다.
	 * @param imcsLog
	 * @param paramVO
	 * @param listCatInfo
	 * @param resultList
	 * @throws Exception
	 */
	private void hotResultListAdd(IMCSLog imcsLog,
			GetNSKidsListRequestVO paramVO,
			List<HashMap<String, String>> listCatInfo,
			ArrayList<HashMap<String, String>> resultList, int cont_watch_cnt) throws Exception
	{
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		List<HashMap<String, String>> listHotCat = null;
		
		try {
			listHotCat = this.get_HOT_CAT(imcsLog, paramVO);
		} catch(Exception ex) {
			imcsLog.serviceLog("인기순 조회 오류", methodName, methodLine);
			imcsLog.errorLog(methodName + "-E", ex.getClass().getName() + ":" + ex.getMessage());
		}
		
		int match_cnt = 0;
		
		// 인기순으로 resultList 객체에 끼워 넣는다.
		if(listHotCat != null)
		{
			for(HashMap<String, String> hotCat : listHotCat)
			{
				MyKidsVO myvo = this.chkHotJungbokContsId(paramVO, hotCat, listCatInfo, resultList);
				
				if(myvo.getResultJungBokYn().equals("N") && myvo.getCatJungBokYn().equals("Y"))
				{
					match_cnt++;
					
					HashMap<String, String> hmCat = listCatInfo.get(myvo.getIdx());
					
					hmCat.put("c_order_gb", hotCat.get("cont_type"));
					
					resultList.add(hmCat);
				}
				// 시청/인기순 데이터가 캐시에 없을 때 조회함
				else if(myvo.getResultJungBokYn().equals("N") && myvo.getCatJungBokYn().equals("N"))
				{
					if(paramVO.getKids_category_type().equals("B"))
					{
						try {
							paramVO.setC_cont_id(hotCat.get("c_book_cont_id"));
							
							HashMap<String, String> hmWH = this.get_WH_INFO_CONT(imcsLog, paramVO);
							
							if(hmWH != null && hmWH.size() > 0)
							{
								match_cnt++;
								
								hmWH.put("c_order_gb", hotCat.get("cont_type"));
								
								// 포스터 이미지
								if(StringUtils.isNotBlank(paramVO.getC_cont_id()))
								{
									HashMap<String, String> hmPoster = this.getNSKidsListDao.getContsPoster(paramVO.getC_cont_id());
									
									if(hmPoster != null && hmPoster.size() > 0)
									{
										hmWH.put("c_img_file_name_h", hmPoster.get("c_img_file_name_h"));
										hmWH.put("c_img_file_name_v", hmPoster.get("c_img_file_name_v"));
									}
								}
								
								resultList.add(hmWH);
							}
						} catch(Exception ex) {
							imcsLog.serviceLog("시청/인기 컨텐츠 정보 조회 오류", methodName, methodLine);
							imcsLog.errorLog(methodName + "-E", ex.getClass().getName() + ":" + ex.getMessage());
						}
					}
				}
				
				if(paramVO.getKids_category_type().equals("C"))
				{
					if(match_cnt >= HOT_C_CNT)
						break;
				}
				else if(paramVO.getKids_category_type().equals("B"))
				{
					if(match_cnt >= HOT_B_CNT || (match_cnt + cont_watch_cnt) >= HOT_B_CNT)
						break;
				}
			}
		}
	}
	
	/**
	 * 최근 시청 + 인기를 제외한 편성 카테고리 공통
	 * @param listCatInfo
	 * @param resultList
	 * @throws Exception
	 */
	private void lastResultListAdd(List<HashMap<String, String>> listCatInfo, ArrayList<HashMap<String, String>> resultList)
			throws Exception
	{
		boolean isJungbok = false;
		
		// 최근 시청 + 인기를 제외한 편성 카테고리 전문 생성
		for(HashMap<String, String> hmCatInfo : listCatInfo)
		{
			isJungbok = false;
			
			for(HashMap<String, String> hmResult : resultList)
			{
				if(hmCatInfo.get("c_contents_id").equals(hmResult.get("c_contents_id")))
				{
					isJungbok = true;
					break;
				}
			}
			
			if(isJungbok == false)
				resultList.add(hmCatInfo);
		}
	}
	
	/**
	 * 응답 전문 생성
	 */
	private void response_Pageing(IMCSLog imcsLog,
			GetNSKidsListRequestVO paramVO,
			ArrayList<HashMap<String, String>> resultList,
			HashMap<String, String> kids_header,
			StringBuilder sbHead, StringBuilder sbBody, StringBuilder sbGuide_1, StringBuilder sbGuide_2) throws Exception
	{
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		List<HashMap<String, String>> lastAlbumlist = null;
		
		String aws_svc_flag = null;
		
		// 2019.07.24 - 영어유치원 3Depth 카테고리 진입시에만 최근 시청한 4Depth 카테고리ID 정보 제공 (추후 아이들나라3.0 2차에서 캐릭터관/부모교실에서도 제공 예정)
		// 2019.07.31 - 캐릭터관 / 부모교실도 제공~
		if((paramVO.getKids_category_type().equals("G") || paramVO.getKids_category_type().equals("C"))
				&& paramVO.getKids_category_level().equals("4"))
		{
			try {
				lastAlbumlist = this.getNSKidsListDao.listLastAlbumInfo(paramVO);
				
				if(lastAlbumlist != null && lastAlbumlist.size() > 0)
				{
					kids_header.put("focus_category_id", lastAlbumlist.get(0).get("c_cat_id"));
				}
			} catch(Exception ex) {
				imcsLog.serviceLog("last_album_info 조회 오류", methodName, methodLine);
				imcsLog.errorLog(methodName + "-E", ex.getClass().getName() + ":" + ex.getMessage());
			}
		}
		
		// 2019.08.20 - AWS(아마존) 서비스 Flag 추가 (장애 대응 차원)
		try {
			aws_svc_flag = this.getNSKidsListDao.getAwsSvcFlag();
			
			if(StringUtils.isEmpty(aws_svc_flag))
				aws_svc_flag = "Y";
		} catch(Exception e) {
			aws_svc_flag = "Y";
		}
		
		// 카테고리 타입이 T 이면서 CON은 티칭 가이드 영상이므로, 헤더에 넣어주기 때문에 BODY영역에는 넣지 않는다.
		// 한마디로 resultList 에서 필요없는 데이터는 삭제한다.
		for(Iterator<HashMap<String, String>> iter = resultList.iterator(); iter.hasNext();)
		{
			HashMap<String, String> hm = iter.next();
			
			if(paramVO.getKids_category_type().equals("T") ||
					(paramVO.getKids_category_type().equals("G") && Integer.parseInt(paramVO.getKids_category_level()) <= 4))
			{
				// 선생님 추천인 경우 헤더에는 이전월과 이후월, Body 에는 현재월 또는 유저가 선택한 월이 출력되어야함.
				// 그래서 Body에 들어가는 월만 출력하기 위해 body_appdnd 를 사용함.
				if(hm.get("c_result_type").equals("ALB") || "X".equals(hm.get("body_append")))
				{
					iter.remove();
				}
			}
		}
		
		int i = (Integer.parseInt(paramVO.getPageNo()) * Integer.parseInt(paramVO.getPageCnt())) - Integer.parseInt(paramVO.getPageCnt());
		int j = (Integer.parseInt(paramVO.getPageNo()) * Integer.parseInt(paramVO.getPageCnt()));
		int resultSize = resultList.size();
		
		// 전체 카운트 추가
		kids_header.put("tot_cnt", String.valueOf(resultList.size()));
		
		for(int x=i ; x < j ; x++)
		{
			if(x >= resultSize)
				break;
			
			HashMap<String, String> hm = resultList.get(x);
			
			if(StringUtils.isBlank(hm.get("c_kids_grade")))
				hm.put("c_kids_grade", "");
			
			// c_kids_grade 가 숫자가 아니면 공백으로 치환(기존 소스가 이렇게 되어 있음.)
			if(StringUtils.isNumeric(hm.get("c_kids_grade").trim()) == false)
				hm.put("c_kids_grade", "");
			
			// 카테고리 일때 10번 IMAGE_FILE 을 구한다.
			if(hm.get("c_result_type").equals("CAT"))
			{
				// c_cat_img_file_name 을 일단 c_image_file 에 넣어준다.
				hm.put("c_image_file", hm.get("c_cat_img_file_name"));
				
				// 시리즈인 경우 c_image_file 이 없으면 last_album_id 를 가지고 포스터이미지를 조회한다.
				if(hm.get("c_type").equals("SER") && StringUtils.isBlank(hm.get("c_image_file")) && StringUtils.isNotBlank(hm.get("last_album_id")))
				{
					paramVO.setLast_album_id(hm.get("last_album_id"));
					String posterImg = this.getNSKidsListDao.getPosterImgFileName(paramVO);
					
					hm.put("c_image_file", posterImg);
				}
			}
			else if(hm.get("c_result_type").equals("ALB"))
			{
				// 앨범인 경우 세로 포스터 이미지를 넣어준다.
				if(StringUtils.isBlank(hm.get("c_image_file")) && StringUtils.isNotBlank(hm.get("c_img_file_name_v")))
				{
					hm.put("c_image_file", hm.get("c_img_file_name_v"));
				}
			}
			
			if(lastAlbumlist != null && hm.get("c_result_type").equals("ALB"))
			{
				for(HashMap<String, String> hmLast : lastAlbumlist)
				{
					if(hmLast.get("c_album_id").equals(hm.get("c_contents_id")) && hmLast.get("c_cat_id").equals(hm.get("c_cat_id")))
					{						
						hm.put("c_last_watch_yn", "Y");
					}
				}
			}
			
			// BODY 전문(NSC)
			sbBody.append(String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|\f",
					StringUtil.nullToSpace(hm.get("c_result_type")), 	StringUtil.nullToSpace(hm.get("c_contents_id")), 	StringUtil.nullToSpace(hm.get("c_contents_name")),
					StringUtil.nullToSpace(hm.get("c_type")), 			StringUtil.nullToSpace(hm.get("c_cat_id")),			StringUtil.nullToSpace(hm.get("c_cat_sub_name")),
					StringUtil.nullToSpace(hm.get("c_category_flag")),	StringUtil.nullToSpace(hm.get("c_cat_level_month")), StringUtil.nullToSpace(hm.get("c_img_url")),
					StringUtil.nullToSpace(hm.get("c_image_file")),		StringUtil.nullToSpace(kids_header.get("cat_image_url")), StringUtil.nullToSpace(hm.get("c_animation_file")),
					StringUtil.nullToSpace(hm.get("c_recommend_id")),	StringUtil.nullToSpace(hm.get("c_service_gb")),		StringUtil.nullToSpace(hm.get("c_kids_grade")),
					StringUtil.nullToSpace(hm.get("c_runtime")),		StringUtil.nullToSpace(hm.get("c_is_51_ch")),		StringUtil.nullToSpace(hm.get("c_is_caption")),
					StringUtil.nullToSpace(hm.get("c_is_hd")),			StringUtil.nullToSpace(hm.get("c_3d_yn")),			StringUtil.nullToSpace(hm.get("c_watcha_point")),
					StringUtil.nullToSpace(hm.get("c_cine21_point")),	StringUtil.nullToSpace(hm.get("c_order_gb")),		StringUtil.nullToSpace(hm.get("c_watch_date")),
					StringUtil.nullToSpace(hm.get("c_last_watch_yn")), 	StringUtil.nullToSpace(hm.get("c_screen_type"))
			));
		}
		
		// 응답값 맨 앞에 전체 Header정보 전달(NSC)
		sbHead.append(String.format("0||%s|%s|%s|%s|%s|%s|%s|\f",
				StringUtil.nullToSpace(kids_header.get("tot_cnt")), 			aws_svc_flag,
				StringUtil.nullToSpace(kids_header.get("focus_category_id")), 	StringUtil.nullToSpace(kids_header.get("prior_category_id")),
				StringUtil.nullToSpace(kids_header.get("prior_category_name")), StringUtil.nullToSpace(kids_header.get("next_category_id")),
				StringUtil.nullToSpace(kids_header.get("next_category_name"))
		));
		
		// 선생님 추천, 영어유치원인 경우 가이드 영상 제공(NSC)
		if(paramVO.getKids_category_type().equals("T") || paramVO.getKids_category_type().equals("G"))
		{
			
			if(kids_header.get("guide_asset_id_1") != null || kids_header.get("guide_asset_id_2") != null)
			sbGuide_1.append(String.format("GUD|%s|%s|%s|%s|\f",
					StringUtil.nullToSpace(kids_header.get("guide_asset_id_1")),
					StringUtil.nullToSpace(kids_header.get("guide_asset_id_2")),
					StringUtil.nullToSpace(kids_header.get("guide_album_name_1")),
					StringUtil.nullToSpace(kids_header.get("guide_album_id"))					
			));
		}
	}
}































