package kr.co.wincom.imcs.api.getNSMainPage;

import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.CateInfoVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetNSMainPageServiceImpl implements GetNSMainPageService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSMainPage");
	
	@Autowired
	private GetNSMainPageDao getNSMainPageDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSMainPage(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public GetNSMainPageResultVO getNSMainPage(GetNSMainPageRequestVO paramVO){
//		this.getNSMainPage(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		GetNSMainPageResultVO resultListVO			= new GetNSMainPageResultVO();
		
		String msg	= "";
		String szImgSvrUrl		= "";		// 이미지 서버 URL
		String szlastAlbumId	= "";
		
		int nMainCnt	= 0;
		int nSubCnt		= 0;
		
	    long tp_start = paramVO.getTp_start();
		long tp1, tp2, tp3, tp4, tp5, tp6 = 0;
		
		
		try{
			// 행정동 정보 조회
			List<String> ldongYn	= new ArrayList<String>();
			ldongYn = getNSMainPageDao.getDongYn(paramVO);
			
			if(ldongYn == null || ldongYn.size() == 0)		paramVO.setDongYn("N");
			else											paramVO.setDongYn("Y");
			
			// 가입자 조회
			String szTestSbc	= getNSMainPageDao.getTestSbc(paramVO);

			if(szTestSbc == null || "".equals(szTestSbc))	szTestSbc	= "N";
			paramVO.setTestSbc(szTestSbc);
			
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("가입자 조회", String.valueOf(tp1 - tp_start), methodName, methodLine); 
		    
			
			// 이미지 서버 URL 조회
		    try { 
		    	szImgSvrUrl	= commonService.getIpInfo("img_resize_server", ImcsConstants.API_PRO_ID509.split("/")[1]);
		    } catch (Exception e){
//				imcsLog.failLog(ImcsConstants.API_PRO_ID509, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
				paramVO.setResultCode("31000000");
				
				throw new ImcsException();
		    }
		    
		    tp1	= System.currentTimeMillis();
			imcsLog.timeLog("서버IP값 조회", String.valueOf(tp1 - tp_start), methodName, methodLine);
			tp4	= System.currentTimeMillis();
			
			
			// 실시간 인기채널 프로그램 조회
			List<GetNSMainPageChannelVO> lChnlList = new ArrayList<GetNSMainPageChannelVO>();
			GetNSMainPageChannelVO chnlListVO = new GetNSMainPageChannelVO();
			
		    try {
		    	lChnlList = getNSMainPageDao.getChnlList(paramVO);

		    	tp2	= System.currentTimeMillis();
				imcsLog.timeLog("실시간 인기채널 프로그램", String.valueOf(tp2 - tp1), methodName, methodLine); 
				
		    	if(lChnlList == null || lChnlList.size() == 0) {
//					imcsLog.failLog(ImcsConstants.API_PRO_ID509, "", null, "periode_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
		    	} else {
		    		int nSelCnt		= 0;
		    		
		    		for(int i = 0; i < lChnlList.size(); i++) {
		    			if(nSelCnt > 3) break;
		    			
		    			chnlListVO = lChnlList.get(i);
		    			
		    			chnlListVO.setLiveServer1("http://" + chnlListVO.getLiveIp1() + ":"+chnlListVO.getLivePort() + "/");
				    	chnlListVO.setLiveServer2("http://" + chnlListVO.getLiveIp2() + ":"+chnlListVO.getLivePort() + "/");
				    	chnlListVO.setLiveServer3("http://" + chnlListVO.getLiveIp3() + ":"+chnlListVO.getLivePort() + "/");
				    	chnlListVO.setLiveServer4("http://" + chnlListVO.getLiveIp4() + ":"+chnlListVO.getLivePort() + "/");
				    	chnlListVO.setLiveServer5("http://" + chnlListVO.getLiveIp5() + ":"+chnlListVO.getLivePort() + "/");
				    	chnlListVO.setLiveServer6("http://" + chnlListVO.getLiveIp6() + ":"+chnlListVO.getLivePort() + "/");
				    	
				    	lChnlList.set(i, chnlListVO);
		    			
		    			nSelCnt++;
		    		}
		    	}
			} catch (Exception e) {
//				imcsLog.failLog(ImcsConstants.API_PRO_ID509, "", null, "periode_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
				resultListVO.setResultCode("40000000");
				
				throw new ImcsException();		
			}
		   
		    tp3	= System.currentTimeMillis();
			imcsLog.timeLog("실시간 인기채널 프로그램 Fetch", String.valueOf(tp3 - tp2), methodName, methodLine);
			
			
		
		    
			// 서브 카테고리 정보 조회			
			List<GetNSMainPageSubVO> lSubList = new ArrayList<GetNSMainPageSubVO>();
			GetNSMainPageSubVO subListVO = new GetNSMainPageSubVO();
			
			List<StillImageVO> lStillImage = new ArrayList<StillImageVO>();
			String szTempAlbumId	= "";
			String szSerCatId		= "";
			
			try {
				lSubList = getNSMainPageDao.getSubList(paramVO);

				tp5	= System.currentTimeMillis();
				imcsLog.timeLog("서브카테고리정보 조회", String.valueOf(tp5 - tp4), methodName, methodLine);
			} catch (Exception e) {
				resultListVO.setResultCode("40000000");
//				imcsLog.failLog(ImcsConstants.API_PRO_ID509, "", null, "sub_list:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
				
				throw new ImcsException();
			}
		   
			 if(lSubList != null){
		    	nSubCnt = lSubList.size();
	    	}
			 
			
		    for(int i = 0; i < nSubCnt; i++){
		    	tp3	= System.currentTimeMillis();
		    	
		    	subListVO = lSubList.get(i);
		    	subListVO.setNscType(paramVO.getNscType());
		    	
	    		/* 해당 로직에 걸릴 일이 없음 c에서 rd1.c_genre_gb에 값 안들어감
	    		 * if("N".equals(lSubList.get(0).getGenreGb()) && 0 > i) {		// i_album_seq>end_num 로직이 의미 없음
	    			break;
	    		}*/
		    	
		    	szlastAlbumId	= subListVO.getContsId();
		    	
		    	// 5.1ch 여부
		    	if("DOLBY 5.1".equals(subListVO.getIs51ch())){
		    		subListVO.setIs51ch("Y");
		    	}else{
		    		subListVO.setIs51ch("N");
		    	}
		    	
		    	// HD 여부
		    	if("Y".equals(subListVO.getHdcontent()) || "S".equals(subListVO.getHdcontent()) ){
		    		subListVO.setIsHd("Y");
		    	}else if("N".equals(subListVO.getHdcontent())){
		    		subListVO.setIsHd("N");
		    	}
		    	
		    	if(szTempAlbumId.equals(subListVO.getContsId())){}
		    	else{
		    		// 스틸 이미지 조회
	    			try {
	    				lStillImage = getNSMainPageDao.getStillImage(subListVO.getContsId());
	    				
	    				if(lStillImage != null) {
	    					subListVO.setStillFileName(StringUtil.nullToSpace(lStillImage.get(0).getImgFileName()));
	    				} else {
	    					break;
	    				}
					} catch (Exception e) {
						resultListVO.setResultCode("40000000");
//						imcsLog.failLog(ImcsConstants.API_PRO_ID509, "", null, "still_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
						
						throw new ImcsException();
					}
	    			
	    			// 시리즈 카테고리 여부 조회
			    	szSerCatId = getNSMainPageDao.getSerCatInfo(subListVO);
			    	subListVO.setSerCatId(szSerCatId);
			    	
			    	szTempAlbumId	= subListVO.getContsId();
		    	}
		    	
		    	subListVO.setImgUrl(szImgSvrUrl);
		    	
		    	lSubList.set(i, subListVO);
		    }

		    // 마지막 AlbumID와 Temp앨범ID가 다르면 이미지, 시리즈카테정보 재조회
		    if(!szlastAlbumId.equals(szTempAlbumId)){
	    		lStillImage = new ArrayList<StillImageVO>();
    			
    			try {
    				lStillImage = getNSMainPageDao.getStillImage(subListVO.getContsId());

    				if(lStillImage != null) {
    					subListVO.setStillFileName(StringUtil.nullToSpace(lStillImage.get(0).getImgFileName()));
    				}
    				
				} catch (Exception e) {
					resultListVO.setResultCode("40000000");
//					imcsLog.failLog(ImcsConstants.API_PRO_ID509, "", null, "still_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
					
					throw new ImcsException();
				}
    			
    			subListVO.setContsId(szTempAlbumId);
    			
    			szSerCatId = getNSMainPageDao.getSerCatInfo(subListVO);
		    	subListVO.setSerCatId(szSerCatId);
		    }
		  
		    
		    tp6	= System.currentTimeMillis();
			imcsLog.timeLog("서브카테고리정보 FETCH", String.valueOf(tp6 - tp5), methodName, methodLine);
		    
			
			
		    // 메인 카테고리 정보 조회
		    List<GetNSMainPageMainVO> lMainList = new ArrayList<GetNSMainPageMainVO>();
		    GetNSMainPageMainVO mainListVO = new GetNSMainPageMainVO();
		    
		    try {
		    	lMainList = getNSMainPageDao.getNSMainList(paramVO);
		    	
		    	if(lMainList == null || lMainList.size() == 0) {
		    		msg = " svc4[" + String.format("%-20s", ImcsConstants.API_PRO_ID509) + "] sts[" + ImcsConstants.LOG_MSG2 + "] msg["+ String.format("%-19s", "categ_info:" + ImcsConstants.RCV_MSG3 +"]");
					imcsLog.serviceLog(msg, methodName, methodLine);
		    	}
			} catch (Exception e) {
				resultListVO.setResultCode("40000000");
//				imcsLog.failLog(ImcsConstants.API_PRO_ID509, "", null, "categ_info:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
				
				throw new ImcsException();
			}
		    
		    tp3	= System.currentTimeMillis();
			imcsLog.timeLog("카테고리정보 조회", String.valueOf(tp3 - tp2), methodName, methodLine);
		    
		    nMainCnt = 0;
	    	
	    	if(lMainList != null){
	    		nMainCnt = lMainList.size();
	    	}else{
//				imcsLog.failLog(ImcsConstants.API_PRO_ID509, "", null, "categ_info:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
	    	}
	    	
	    	for(int i = 0; i < nMainCnt; i++){
	    		mainListVO = lMainList.get(i);
	    		
	    		if("CAT".equals(mainListVO.getResultType())){
	    			CateInfoVO parentCatInfo = new CateInfoVO();
	    			
	    			try {
	    				paramVO.setCatId(mainListVO.getCatId());
	    				parentCatInfo = getNSMainPageDao.getParentCatYn(paramVO);
	    			} catch (Exception e) {
	    				resultListVO.setResultCode("40000000");
//	    				imcsLog.failLog(ImcsConstants.API_PRO_ID509, "", null, "par_yn:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
	    				
	    				throw new ImcsException();
	    			}
	    			
	    			if(parentCatInfo != null){
	    				mainListVO.setParentCatYn(parentCatInfo.getParentCatId());
	    				mainListVO.setnSubCnt(Integer.parseInt(StringUtil.nullToZero(parentCatInfo.getCount())));
	    			}else{
//	    				imcsLog.failLog(ImcsConstants.API_PRO_ID509, "", null, "par_yn:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
	    			}
	    		}
	    		
	    		if("N".equals(mainListVO.getParentCatYn()) && "SER".equals(mainListVO.getCatType())){
	    			mainListVO.setnSubCnt(Integer.parseInt(StringUtil.nullToZero(mainListVO.getContsCnt())));
	    		}
	    		
	    		mainListVO.setImgUrl(szImgSvrUrl);
	    		lMainList.set(i, mainListVO);
	    	}
	    	
	    	
	    	tp4	= System.currentTimeMillis();
			imcsLog.timeLog("카테고리정보 FETCH", String.valueOf(tp4 - tp3), methodName, methodLine);
	    	
			
			resultListVO.setMainList(lMainList);
			resultListVO.setSubList(lSubList);
			resultListVO.setChnlList(lChnlList);
			
			msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID509) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg["+ String.format("%-19s", ImcsConstants.RCV_MSG5 +"]");
			imcsLog.serviceLog(msg, methodName, methodLine);
			
		}catch(ImcsException ie) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			imcsLog.errorLog(methodName + "-E",ie.getClass().getName()+":"+ie.getMessage());
			throw new ImcsException();
		} catch(Exception e) {
			isLastProcess	= ImcsConstants.RCV_MSG6; 
			imcsLog.errorLog(methodName + "-E",e.getClass().getName()+":"+e.getMessage());
			throw new ImcsException();
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID509) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
			
		}
		
		return resultListVO;
	}
	
    
}
