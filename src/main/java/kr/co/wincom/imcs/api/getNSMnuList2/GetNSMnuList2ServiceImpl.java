package kr.co.wincom.imcs.api.getNSMnuList2;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.wincom.imcs.api.getNSMnuList.GetNSMnuListRequestVO;
import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.FileUtil;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

@Service
public class GetNSMnuList2ServiceImpl implements GetNSMnuList2Service {
	private Log imcsLogger = LogFactory.getLog("API_getNSMnuList2");
	
	@Autowired
	private GetNSMnuList2Dao getNSMnuList2Dao;
	
	@Autowired
	private CommonService commonService;
	
	
//	public void getNSMnuList(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public GetNSMnuList2ResultVO getNSMnuList2(GetNSMnuList2RequestVO paramVO){
//		this.getNSMnuList(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		String msg	= "";
		
		GetNSMnuList2ResultVO resultListVO = new GetNSMnuList2ResultVO();
		String result = "";

		String BAKFILE    = "";
		String RESFILE	   = "";
		String RESFILE_ORG = "";
		String LOCALPATH   = "";
		
		LOCALPATH = commonService.getCachePath("COPY_LOCAL", ImcsConstants.NSAPI_PRO_ID9002.split("/")[1], imcsLog);
		
		msg = "LOCALPATH : " + LOCALPATH;
		imcsLog.serviceLog(msg, methodName, methodLine);
		
	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;

		String imgServerUrl	= "";
		String imgStillServerUrl = "";
		String imgCatServer = "";

		try{
			
			try {
				imgServerUrl = commonService.getImgReplaceUrl2("img_server", "getNSMnuList");
				imgStillServerUrl = commonService.getImgReplaceUrl2("img_still_server", "getNSMnuList");
				imgCatServer = commonService.getImgReplaceUrl2("img_cat_server", "getNSMnuList");
				
			} catch (Exception e) {
				//imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID9002, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
				throw new ImcsException();
			}
			
			//검수사용자 조회
			this.getTestSbc(paramVO);
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("검수 STB여부 조회", String.valueOf((tp1 - tp_start)), methodName, methodLine);
			
			//카테고리ID의 카테고리 level 확인
			this.getCateLevel(paramVO);
			
			// 2020.03.09 - 모바일 아이들나라 (LEVEL_GB로 인해 여러 카테고리 캐시파일을 읽어야해서.. 캐시 조회하는 부분 함수로 변경함)
			paramVO.setTokCnt(1);
			paramVO.setEndCnt(0);	// level_gb가 B일 때에는 캐시 파일 조회 개수와 End_cnt의 수가 일치해야 END_STR문자열을 찍어준다. 
			
			result = this.getMnuList_Cache(paramVO);
			
			if(paramVO.getLevelGb().equals("B") && paramVO.getLevelGbCatId() != null)
			{
				int cat_cnt = 0;
				for(String tmpLevelGbCatId : paramVO.getLevelGbCatId())
				{
					cat_cnt++;
					String tmpResult = "";
					paramVO.setCatId(tmpLevelGbCatId);
					tmpResult = this.getMnuList_Cache(paramVO);
					
					if(tmpResult.length() == 0) {
						paramVO.setEndCnt(paramVO.getEndCnt() + 1);
					}
					
					result = result + tmpResult;
					
					// 전체 조회시 .. 모든 카테고리를 다 처리했으면 END_STR을 찍고
					// 페이징시, 마지막 카테고리 때 END_STR이 있었으면 END_STR을 찍는다.
			
				}
		
				
				// 전체 조회시 .. 모든 카테고리를 다 처리했으면 END_STR을 찍고
				// 페이징시, 마지막 카테고리 때 END_STR이 있었으면 END_STR을 찍는다.
				if(paramVO.getPagingFlag() >= paramVO.getHeaderTotCnt())
				{
					result = result + "END_STR" + ImcsConstants.ROWSEP + ImcsConstants.ROWSEP; 
				}
				
				String resultHeader  = String.format("%s|%s|%d|%d|%d|%s|%s|", 
						"0", "", paramVO.getHeaderTotCnt(), paramVO.getHeaderCatCnt(), paramVO.getHeaderVodCnt(), paramVO.getHeaderStillImgUrl(), paramVO.getHeaderCatId());
				
				resultHeader = resultHeader + ImcsConstants.ROWSEP;
				result = resultHeader + result; 
			}
			
			if (result.length() == 0) {
				resultListVO.setResult("0||0|0|0|" + imgStillServerUrl + "|" + paramVO.getCatId() + "|" + "\fEND_STR\f\f");
				return resultListVO;
			} else {
				resultListVO.setResult(result);
			}
			
		} catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ":" + ie.getMessage());
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		} finally{
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID9002) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	
	/**
	 * 검수 STB 여부 조회
	 * @param paramVO
	 * @return
	 */
    public void getTestSbc(GetNSMnuList2RequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		List<String> list   = new ArrayList<String>();
		
		try {
			list = getNSMnuList2Dao.getTestSbc(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			if( list == null || list.isEmpty()){
				paramVO.setTestSbc("N");				
			} else {
				paramVO.setTestSbc(StringUtil.nullToSpace(list.get(0)));
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			paramVO.setTestSbc("N");
		}
		
    }
    
    
    /**
	 * 카테고리 레벨 조회
	 * @param paramVO
	 * @return
	 */
    public void getCateLevel(GetNSMnuList2RequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		
		HashMap<String, String> mCat   = new HashMap<String, String>();
		
		try {
			mCat = getNSMnuList2Dao.getCateLevel(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			if (mCat != null) {
				paramVO.setCategoryLevel(mCat.get("CATEGORY_LEVEL"));
				paramVO.setNscGb(mCat.get("NSC_GB"));
				paramVO.setCategoryName(mCat.get("CATEGORY_NAME"));
			}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
    }    
    
    
    public String getMnuList_Cache(GetNSMnuList2RequestVO paramVO) throws Exception{
    	IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
    	
    	StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
    	
		String imgServerUrl	= "";
		String imgStillServerUrl = "";
		String imgCatServer = "";
		
		try {
			imgServerUrl = commonService.getImgReplaceUrl2("img_server", "getNSMnuList");
			imgStillServerUrl = commonService.getImgReplaceUrl2("img_still_server", "getNSMnuList");
			imgCatServer = commonService.getImgReplaceUrl2("img_cat_server", "getNSMnuList");
			
		} catch (Exception e) {
			//imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID900, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
			throw new ImcsException();
		}
		
    	String msg = null;
    	String result_cache = null;
    	
    	String LOCKFILE    = "";
		String RESFILE	   = "";
		String RESFILE_ORG = "";
		String LOCALPATH   = "";
		
		LOCALPATH = commonService.getCachePath("COPY_LOCAL", ImcsConstants.NSAPI_PRO_ID900.split("/")[1], imcsLog);
		
		msg = "LOCALPATH : " + LOCALPATH;
		imcsLog.serviceLog(msg, methodName, methodLine);
		
		//그냥 폴더인지, bak 폴더인지 판단
		File SUBFOLDER = new File(LOCALPATH);
		if (!SUBFOLDER.exists()) {
			LOCALPATH = LOCALPATH + "_bak";
		}
		
		//파일생성규칙은 pid, IPTV2.0전체버전, 서브카테고리 변경버전, 검수여부, defin_flag , NSC_TYPE, 연령정보, 정렬기준
		//YOUTH_YN 적용후 파일생성 이름
		LOCALPATH = String.format("%s/%s", LOCALPATH, paramVO.getCatId().substring(0, 2));
		msg = "LOCALPATH : " + LOCALPATH;
		imcsLog.serviceLog(msg, methodName, methodLine);
		
		
		
		if (paramVO.getNscGb().equals("UFX") && paramVO.getFxType().equals("H")) {
			RESFILE = String.format("%s/getNSMnuList-%s-T%s-R%s-O%s-UY.res", LOCALPATH, paramVO.getCatId(), paramVO.getTestSbc(), paramVO.getRating(), paramVO.getOrderGb());
			msg = "RESFILE : " + RESFILE;
			imcsLog.serviceLog(msg, methodName, methodLine);
		} else {
			RESFILE = String.format("%s/getNSMnuList-%s-T%s-R%s-O%s-UN.res", LOCALPATH, paramVO.getCatId(), paramVO.getTestSbc(), paramVO.getRating(), paramVO.getOrderGb());
			msg = "RESFILE : " + RESFILE;
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		File fRESFILE = new File(RESFILE);
		if (fRESFILE.exists()) {
			String result = FileUtil.fileRead(RESFILE, "UTF-8");
			String return_result = "";
			
			if(!"".equals(result)) {
				
				// 2020.01.13 - 페이징 처리 함수로 변경
				return_result = getMnuList_Paging(paramVO, result, imgServerUrl, imgStillServerUrl, imgCatServer);
				
				msg = " File [" + RESFILE + "] rcvbuf... [" + fRESFILE.length() + "] bytes ";
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				return return_result;
			
				
			}else{
				msg = " File [" + RESFILE + "] Resfile Read Failed";
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				return result;
			}
		} else { //GetNSMakeMnuList 캐시파일 생성 로직으로 빠짐
			
			msg = " File [" + RESFILE + "] Have No ResFile";
			imcsLog.serviceLog(msg, methodName, methodLine);
			return "";
			
		}
    }
    
    /**
	 * 응답할 파일 Read후에 페이징 처리.
	 * @param String
	 * @return
	 */
    public String getMnuList_Paging(GetNSMnuList2RequestVO paramVO, String ListData, String imgServerUrl, String imgStillServerUrl, String imgCatServer) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		HashMap<String, String> mCat   = new HashMap<String, String>();
		String result = "";
		List<String> levelGbCatId = new ArrayList<String>();
		
		try {
			//여기서부터 캐쉬파일 페이징 처리
			if (!paramVO.getPageNo().equals("0") && !paramVO.getPageCnt().equals("0")) {

				int page_no = Integer.parseInt(paramVO.getPageNo());
				int page_cnt = Integer.parseInt(paramVO.getPageCnt());
				int start_num = (page_no * page_cnt) - (page_cnt - 1);
				int end_num = (page_no * page_cnt);

				String[] arrResult = ListData.split(ImcsConstants.ROWSEP);
				if (!paramVO.getLevelGb().equals("B")) {
					result = arrResult[0] + ImcsConstants.ROWSEP; // header 부분
					result = result.replaceAll("img_still_server", imgStillServerUrl);
				} else if (paramVO.getLevelGb().equals("B") && paramVO.getLevelGbCatId() == null) {
					
					if(!paramVO.getOrderGb().equals("W")) {
						String[] arrlevelGbCatId = arrResult[0].split(ImcsConstants.COLSEP_SPLIT);
						paramVO.setHeaderTotCnt(Integer.parseInt(arrlevelGbCatId[2]));
						paramVO.setHeaderCatCnt(Integer.parseInt(arrlevelGbCatId[3]));
						paramVO.setHeaderVodCnt(Integer.parseInt(arrlevelGbCatId[4]));
						paramVO.setHeaderStillImgUrl(arrlevelGbCatId[5].replaceAll("img_still_server", imgStillServerUrl));
						paramVO.setHeaderCatId(arrlevelGbCatId[6]);
					} else {
						String[] arrlevelGbCatId = arrResult[1].split(ImcsConstants.COLSEP_SPLIT);
						paramVO.setHeaderTotCnt(Integer.parseInt(arrlevelGbCatId[2]));
						paramVO.setHeaderCatCnt(Integer.parseInt(arrlevelGbCatId[3]));
						paramVO.setHeaderVodCnt(Integer.parseInt(arrlevelGbCatId[4]));
						paramVO.setHeaderStillImgUrl(arrlevelGbCatId[5].replaceAll("img_still_server", imgStillServerUrl));
						paramVO.setHeaderCatId(arrlevelGbCatId[6]);
					}
				} else {
					if(!paramVO.getOrderGb().equals("W")) {
						String[] arrlevelGbCatId = arrResult[0].split(ImcsConstants.COLSEP_SPLIT);
						paramVO.setHeaderTotCnt(paramVO.getHeaderTotCnt() + Integer.parseInt(arrlevelGbCatId[2]));
						paramVO.setHeaderCatCnt(paramVO.getHeaderCatCnt() + Integer.parseInt(arrlevelGbCatId[3]));
						paramVO.setHeaderVodCnt(paramVO.getHeaderVodCnt() + Integer.parseInt(arrlevelGbCatId[4]));
					} else {
						String[] arrlevelGbCatId = arrResult[1].split(ImcsConstants.COLSEP_SPLIT);
						paramVO.setHeaderTotCnt(paramVO.getHeaderTotCnt() + Integer.parseInt(arrlevelGbCatId[2]));
						paramVO.setHeaderCatCnt(paramVO.getHeaderCatCnt() + Integer.parseInt(arrlevelGbCatId[3]));
						paramVO.setHeaderVodCnt(paramVO.getHeaderVodCnt() + Integer.parseInt(arrlevelGbCatId[4]));
					}
				}

				long tt1 = System.currentTimeMillis();
				
				int orderFlag = 0;
				
				if(!paramVO.getOrderGb().equals("W")) {
					orderFlag = 1;
				} else {
					orderFlag = 2;
				}
				
				
				for(int i = orderFlag; i < arrResult.length; i++) {
					
					if(paramVO.getLevelGb().equals("B"))
					{
						if(arrResult[i].equals("END_STR"))
						{
							break;
						}
						
						String[] arrlevelGbCatId = arrResult[i].split(ImcsConstants.COLSEP_SPLIT);
						if(paramVO.getLevelGbCatId() == null)
						{
							// 2020.03.09 - 모바일 아이들나라 : level_gb가 B일 때에는 Sub카테고리 정보를 알아야 하기 때문에 별도로 저장한다.									
							if(arrlevelGbCatId[0].equals("CAT") && arrlevelGbCatId[3].equals(""))
							{
								levelGbCatId.add(arrlevelGbCatId[1]);
							}
						}
						else
						{
							// 2020.03.09 - 모바일 아이들나라 : level_gb가 B일 때, 하위 카테고리의 콘텐츠까지 전달한다. (즉, 하위 카테고리는 ALB아니면 전달하지 않는다.)
							if(!(arrlevelGbCatId[0].equals("ALB") || arrResult[i].equals("END_STR")))
							{
								continue;
							}
						}
					}
					
					if ((paramVO.getTokCnt() >= start_num) && (paramVO.getTokCnt() <= end_num)) {
						arrResult[i] = arrResult[i].replaceAll("img_server", imgServerUrl);
						arrResult[i] = arrResult[i].replaceAll("img_still_server", imgStillServerUrl);
						arrResult[i] = arrResult[i].replaceAll("img_cat_server", imgCatServer);
						
						result	= result + arrResult[i] + ImcsConstants.ROWSEP;
						paramVO.setPagingFlag(paramVO.getHeaderTotCnt());
					}
					
					paramVO.setTokCnt(paramVO.getTokCnt() + 1);
				}
				if(paramVO.getLevelGbCatId() == null)
				{
					paramVO.setLevelGbCatId(levelGbCatId);
				}
				
				if (!paramVO.getLevelGb().equals("B") && Integer.parseInt(paramVO.getPageCnt()) >= arrResult.length) {
					result	= result + ImcsConstants.ROWSEP;
				}
				
				long tt2 = System.currentTimeMillis();
				imcsLog.timeLog("파일치환:", String.valueOf((tt2 - tt1)), methodName, methodLine);

			} else {
				String[] arrResult	= ListData.split(ImcsConstants.ROWSEP);
				
				if(!paramVO.getLevelGb().equals("B") || (paramVO.getLevelGb().equals("B") && paramVO.getLevelGbCatId() == null))
				{
					if(!paramVO.getLevelGb().equals("B"))
					{
						result	= arrResult[0] + ImcsConstants.ROWSEP; //header 부분
						result = result.replaceAll("img_still_server", imgStillServerUrl);
					}
					else if(paramVO.getLevelGb().equals("B") && paramVO.getLevelGbCatId() == null)
					{
						if(!paramVO.getOrderGb().equals("W")) {
							String[] arrlevelGbCatId = arrResult[0].split(ImcsConstants.COLSEP_SPLIT);
							paramVO.setHeaderTotCnt(Integer.parseInt(arrlevelGbCatId[2]));
							paramVO.setHeaderCatCnt(Integer.parseInt(arrlevelGbCatId[3]));
							paramVO.setHeaderVodCnt(Integer.parseInt(arrlevelGbCatId[4]));
							paramVO.setHeaderStillImgUrl(arrlevelGbCatId[5].replaceAll("img_still_server", imgStillServerUrl));
							paramVO.setHeaderCatId(arrlevelGbCatId[6]);
						} else {
							String[] arrlevelGbCatId = arrResult[1].split(ImcsConstants.COLSEP_SPLIT);
							paramVO.setHeaderTotCnt(Integer.parseInt(arrlevelGbCatId[2]));
							paramVO.setHeaderCatCnt(Integer.parseInt(arrlevelGbCatId[3]));
							paramVO.setHeaderVodCnt(Integer.parseInt(arrlevelGbCatId[4]));
							paramVO.setHeaderStillImgUrl(arrlevelGbCatId[5].replaceAll("img_still_server", imgStillServerUrl));
							paramVO.setHeaderCatId(arrlevelGbCatId[6]);
						}
					}
					else
					{
						if(!paramVO.getOrderGb().equals("W")) {
							String[] arrlevelGbCatId = arrResult[0].split(ImcsConstants.COLSEP_SPLIT);
							paramVO.setHeaderTotCnt(paramVO.getHeaderTotCnt() + Integer.parseInt(arrlevelGbCatId[2]));
							paramVO.setHeaderCatCnt(paramVO.getHeaderCatCnt() + Integer.parseInt(arrlevelGbCatId[3]));
							paramVO.setHeaderVodCnt(paramVO.getHeaderVodCnt() + Integer.parseInt(arrlevelGbCatId[4]));
						} else {
							String[] arrlevelGbCatId = arrResult[1].split(ImcsConstants.COLSEP_SPLIT);
							paramVO.setHeaderTotCnt(paramVO.getHeaderTotCnt() + Integer.parseInt(arrlevelGbCatId[2]));
							paramVO.setHeaderCatCnt(paramVO.getHeaderCatCnt() + Integer.parseInt(arrlevelGbCatId[3]));
							paramVO.setHeaderVodCnt(paramVO.getHeaderVodCnt() + Integer.parseInt(arrlevelGbCatId[4]));
						}
					}
				}
				
				long tt1 = System.currentTimeMillis();
				for(int i = 1; i < arrResult.length; i++) {
					arrResult[i] = arrResult[i].replaceAll("img_server", imgServerUrl);
					arrResult[i] = arrResult[i].replaceAll("img_still_server", imgStillServerUrl);
					arrResult[i] = arrResult[i].replaceAll("img_cat_server", imgCatServer);
					
					if(paramVO.getLevelGb().equals("B"))
					{
						if(arrResult[i].equals("END_STR"))
						{
							break;
						}
						
						String[] arrlevelGbCatId = arrResult[i].split(ImcsConstants.COLSEP_SPLIT);
						if(paramVO.getLevelGbCatId() == null)
						{
							// 2020.03.09 - 모바일 아이들나라 : level_gb가 B일 때에는 Sub카테고리 정보를 알아야 하기 때문에 별도로 저장한다.									
							if(arrlevelGbCatId[0].equals("CAT") && arrlevelGbCatId[3].equals(""))
							{
								levelGbCatId.add(arrlevelGbCatId[1]);
							}
						}
						else
						{
							// 2020.03.09 - 모바일 아이들나라 : level_gb가 B일 때, 하위 카테고리의 콘텐츠까지 전달한다. (즉, 하위 카테고리는 ALB아니면 전달하지 않는다.)
							if(!arrlevelGbCatId[0].equals("ALB"))
							{
								continue;
							}
						}
					}
					
					result	= result + arrResult[i] + ImcsConstants.ROWSEP;
				}
				if(paramVO.getLevelGbCatId() == null)
				{
					paramVO.setLevelGbCatId(levelGbCatId);
				}
				
				long tt2 = System.currentTimeMillis();
				imcsLog.timeLog("파일치환:", String.valueOf((tt2 - tt1)), methodName, methodLine);	
			}
		} catch (Exception e) {	
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
		}
		
		return result;
    }    
}
