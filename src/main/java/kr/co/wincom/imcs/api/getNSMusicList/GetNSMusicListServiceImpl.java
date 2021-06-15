package kr.co.wincom.imcs.api.getNSMusicList;

import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetNSMusicListServiceImpl implements GetNSMusicListService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSMusicList");
	
	@Autowired
	private GetNSMusicListDao getNSMusicListDao;
	
	@Autowired
	private CommonService commonService;

//	public void getNSMusicList(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@SuppressWarnings("unused")
	@Override
	public GetNSMusicListResultVO getNSMusicList(GetNSMusicListRequestVO paramVO){
//		this.getNSMusicList(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		List<GetNSMusicListResponseVO> resultVO	= new ArrayList<GetNSMusicListResponseVO>();
		GetNSMusicListResultVO resultListVO	= new GetNSMusicListResultVO();

		String msg	= "";
		
		int nMainCnt = 0;
        // header계산
        int iCntDtl  = 0;
        int iCntPre  = 0;
        int iCntOn   = 0;
        int iCntPost = 0;
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
			
		    // c_page_cnt가 지정이 되어있는데 RQS_TYPE=A(전체)면 각각의 Type별(전/중/후)로 c_page_cnt를 적용함.
		    // RQS_TYPE:A(전체) 경우는 0, 1, 2를 차례로 돌림.(순서 0,1,2 순)
			if (paramVO.getRqsType().equals("A")) {
				paramVO.setRqsType("0");
				nRqs_Type = 0;
				iTypeEnd  = 2;
			} else {
				nRqs_Type = Integer.parseInt(paramVO.getRqsType());
				iTypeEnd = Integer.parseInt(paramVO.getRqsType());
			}
			
			//가입자 정보 가져오기 => 검수 여부 (nsvod030_001_20180601)
			tp1 = System.currentTimeMillis();
			String szTestSbc	= "";
			szTestSbc	= this.getTestSbc(paramVO);
			paramVO.setTestSbc(szTestSbc);
			
			if (szTestSbc.equals("Y"))
				paramVO.setViewFlag("T");	// 검수유저일 경우 검수컨텐츠 노출
			else
				paramVO.setViewFlag("V");	// 일반유저
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("검수 STB여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			//이미지 캐쉬 서버 정보 가져오기
			String img_still_server = commonService.getImgReplaceUrl2("img_still_server", "getNSMusicList");
			String img_cachensc_server = commonService.getImgReplaceUrl2("img_cachensc_server", "getNSMusicList");
			for ( int i = nRqs_Type; i <= iTypeEnd; i++) {
				//////////////////////////////////////////////////////////////////////////////////////////////
				// @@ 주의사항 [MAX(R.CATEGORY_ID) AS CATEGORY_ID] 부분..
				//  현재 연동규격서상 카테고리를 특정할 수 없어서 이렇게 지정함. 추후 방향정해지면 수정필요함.
				//////////////////////////////////////////////////////////////////////////////////////////////
				// 검수적용에 따른 쿼리 변경 //////////////////////////////////////////
				boolean bUseTestSbc = false;	// viewing_flag사용여부
				// 검수는 "방송후(2)"에만 적용됨. => (전/중, 후)쿼리를 변경시켜 별도운영함.
				paramVO.setnRqsType(String.valueOf(i));
				
				List<GetNSMusicListResponseVO> lists = this.getNSMusicCuesheetList(paramVO);
				
				if (szTestSbc.equals("Y"))
					paramVO.setViewFlag("T");	// 검수유저일 경우 검수컨텐츠까지 노출
				else
					paramVO.setViewFlag("V");	// 일반유저일 경우 'V'컨텐츠만 노출
				
				for (GetNSMusicListResponseVO item : lists) {
					
		            // SERVICE_ID : 공연예정 또는 공연중인 콘서트의 경우에만 제공(전/중 아닌경우를 배제)
		            if ( !((item.getScheType().equals("0")) || (item.getScheType().equals("1"))) )		// 방송전, 방송중
		                item.setServiceId("");
					
					// IMAGE_URL
		            item.setImageUrl(img_still_server);
		            item.setPosterUrl(img_cachensc_server);
					
		            resultVO.add(item);

		            // header계산
		            iCntDtl++;
		            if (item.getScheType().equals("0"))		// 방송전
		            	iCntPre++;
		            else if (item.getScheType().equals("1"))	// 방송중
		            	iCntOn++;
		            else if (item.getScheType().equals("2"))	// 방송후
		            	iCntPost++;
		            else {	// 테이블에 미정의VALUE 기재되어있음.
		            }
					
				}
			}
			
			//결과 헤더 구성
            //iResult, 성공여부 코드값 (0:성공, 1:실패)
            //headermsg, 결과메시지
            //iCntDtl, 전체 건수
            //iCntOn, 공연 중 전체 건수
            //iCntPre, 공연예정 전체 건수
            //iCntPost 공연 후 전체 건수
			String resultHeader  = String.format("%d|%s|%d|%d|%d|%d|", iResult, "success", iCntDtl, iCntOn, iCntPre, iCntPost );
			resultListVO.setResultHeader(resultHeader);
			
			if(resultVO != null){
				nMainCnt = resultVO.size();
			}else{
				paramVO.setResultCode("21000000");
//				imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID030, "", null, "chnl_list:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			}
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("공연 뮤직 리스트 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			
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
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID030) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
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
	public String getTestSbc(GetNSMusicListRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 		= "nsvod030_001_20180601";
		String szTestSbc	= "N";
		
		int querySize		= 0;

		try {			
			List<String> list = getNSMusicListDao.getTestSbc(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			if (list != null && !list.isEmpty()) {
				querySize	= list.size();
				szTestSbc	= StringUtil.replaceNull(list.get(0), "N");
			}else{
				szTestSbc = "N";
			}
			
			try{
				//imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID030, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return szTestSbc;
	}
	
	/**
	 * 공연뮤직큐시트 조회
	 * @param paramVO
	 * @return GetNSMusicListResponseVO		list	
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<GetNSMusicListResponseVO> getNSMusicCuesheetList(GetNSMusicListRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 		= "nsvod030_002_20180601";
		
		int querySize		= 0;
		List<GetNSMusicListResponseVO> list = new ArrayList<GetNSMusicListResponseVO>();

		try {			
			list = getNSMusicListDao.getNSMusicCuesheetList(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			if (list != null && !list.isEmpty()) {
				querySize	= list.size();
			} else {
				list = new ArrayList<GetNSMusicListResponseVO>();
			}
			
			try{
				//imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID030, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return list;
	}
	

}
