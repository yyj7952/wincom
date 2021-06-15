package kr.co.wincom.imcs.api.searchByNSStr;

import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchByNSStrServiceImpl implements SearchByNSStrService {
	private Log imcsLogger = LogFactory.getLog("API_searchByNSStr");
		
	@Autowired
	private SearchByNSStrDao searchByNSStrDao;
	
	@Autowired
	private CommonService commonService;

//	public void searchByNSStr(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public SearchByNSStrResultVO searchByNSStr(SearchByNSStrRequestVO paramVO){
//		this.searchByNSStr(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		SearchByNSStrResultVO resultListVO	= new SearchByNSStrResultVO();
		SearchByNSStrResponseVO tempVO	= new SearchByNSStrResponseVO();
		SearchByNSStrResponseVO tempVO2	= new SearchByNSStrResponseVO();
		List<SearchByNSStrResponseVO> resultVO	= new ArrayList<SearchByNSStrResponseVO>();
		List<SearchByNSStrResponseVO> resultList = new ArrayList<SearchByNSStrResponseVO>();
		
		String msg		= "";
		
		int nMainCnt = 0;

	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
		long tp4 = 0;	
		long tp5 = 0;	
		long tp6 = 0;	
			    	    
		try{
			String szImgSvrIp		= "";		// 이미지 서버 IP
		    String szThmImgSvrIp	= "";		// 썸네일 이미지 서버 IP
		    String szCatImgSvrIp	= "";		// 카테고리 이미지 서버 IP
		    
		    try {
		    	szImgSvrIp		= commonService.getIpInfo("img_server", ImcsConstants.API_PRO_ID939.split("/")[1]);		// 이미지서버 IP 조회
		    	szThmImgSvrIp	= commonService.getIpInfo("snap_server", ImcsConstants.API_PRO_ID939.split("/")[1]);		// 썸네일 이미지 조회
		    	szCatImgSvrIp	= commonService.getIpInfo("img_cat_server", ImcsConstants.API_PRO_ID939.split("/")[1]);		// 썸네일 이미지 조회
			} catch (Exception e) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID939, "", null, "svr_ip:" + ImcsConstants.RCV_MSG1, methodName, methodLine);
				paramVO.setResultCode("31000000");
				
				throw new ImcsException();
			}
		    
		    tp1	= System.currentTimeMillis();
			imcsLog.timeLog("서버IP값 조회", String.valueOf(tp1 - tp_start), methodName, methodLine);

			
			tp4	= System.currentTimeMillis();
			try {
				resultVO = searchByNSStrDao.searchByNSStrList(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
				if(resultVO != null){
					nMainCnt = resultVO.size();
				}
			} catch (Exception e) {
				//imcsLog.failLog(ImcsConstants.API_PRO_ID939, "", null, "sub_list:" + ImcsConstants.RCV_MSG2, methodName, methodLine);
				paramVO.setResultCode("40000000");
				
				throw new ImcsException();
			}
			
			tp5	= System.currentTimeMillis();
			imcsLog.timeLog("앨범정보 조회", String.valueOf(tp4 - tp4), methodName, methodLine);
			
			
			String szTempAlbumId	= "";
			String szTempCntAlbum	= "";
			// String szTempEventYn	= "N";		// 사용안함
			String szAlbumBillFlag	= "";
			String szRrating		= "";			
			
			for(int i = 0; i < nMainCnt; i++){
				tempVO = resultVO.get(i);
				
				//String szTempCatId	= "";	// 필요 없어서 주석처리
				//if( szTempCatId.equals( tempVO.getCatId() ))
				//else		szTempCatId = tempVO.getCatId();
				

				if("ALB".equals(tempVO.getResultType())){
					tempVO.setImgUrl(szImgSvrIp);
					tempVO.setSubCnt(0);
					
					if("DOLBY 5.1".equals(tempVO.getIs51ch()))		tempVO.setIs51ch("Y");
					else											tempVO.setIs51ch("N");
					
					if("06".equals(tempVO.getPrInfo())){
						szRrating = "Y";
					}
					
					//금액 설정
					if("0".equals(tempVO.getProductType())){
						tempVO.setBillFlag("N");
					}else{
						tempVO.setBillFlag(tempVO.getSuggestedPrice());
					}
				}else{
					
					tempVO.setImgUrl(szCatImgSvrIp);
					
					tempVO.setBillFlag(tempVO.getSuggestedPrice());
					szAlbumBillFlag = tempVO.getSuggestedPrice();
				}
				
				// HD 여부 저장
				if("Y".equals(tempVO.getHdcontent()) || "S".equals(tempVO.getHdcontent()) ){
					tempVO.setIsHd("Y");
				}else if("N".equals(tempVO.getHdcontent())){
					tempVO.setIsHd("N");
				}
				
				
				if("".equals(szTempCntAlbum)){
					szTempCntAlbum = tempVO.getContsId();
					szTempAlbumId = tempVO.getContsId();
				}
				
				//유무료 설정
				if(szTempCntAlbum.equals(tempVO.getContsId())){
					
					if("Y".equals(tempVO.getSuggestedPrice())){
						szAlbumBillFlag = "Y";
					}
					else {
						szAlbumBillFlag = "N";
					}
					
//					if(!"0".equals(tempVO.getEventValue())){
//						szTempEventYn = "Y";
//					}
					
				}else{
					szTempCntAlbum = tempVO.getContsId();
				}
				
				if( szTempAlbumId.equals( tempVO.getContsId() ) ){
					//bool = false;
				}else{
					szTempAlbumId = tempVO.getContsId();
					szAlbumBillFlag = tempVO.getSuggestedPrice();
					
				/*	if(!"0".equals(tempVO.getEventValue()))		szTempEventYn = "Y";
					else										szTempEventYn = "N";*/
					
					resultList.add(tempVO2);
					
					//bool = true;
					
					szRrating = "";
				}
				
				tempVO.setSubCnt(tempVO.getSubCnt());
				
				if("Y".equals(szRrating))		tempVO.setPrInfo("06");
				
				if("LIV".equals(tempVO.getResultType())){
					tempVO.setImgUrl(szThmImgSvrIp);
				}
				
				if("SER".equals(tempVO.getCatType()) && "".equals(tempVO.getImgFileName())) {
					String posterImgFileName = searchByNSStrDao.getPosterImageFileName(tempVO.getContsId());
					tempVO.setImgUrl(szImgSvrIp);
					tempVO.setImgFileName(posterImgFileName);
					paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				}
				
				tempVO.setAlbumBillFlag(szAlbumBillFlag);
				
				String is_new = "";
				
				is_new = tempVO.getIsNew();
				
				if(is_new.length() > 9 ){
					is_new = is_new.substring(0,9);
					
					tempVO.setIsNew(is_new);
				}
						
				
				tempVO2 = tempVO;
				
			}
			
			if(nMainCnt > 0 ){
				resultList.add(tempVO);
			}			
			
			tp6	= System.currentTimeMillis();
			imcsLog.timeLog("서브카테고리정보 FETCH", String.valueOf(tp6 - tp5), methodName, methodLine);
			
			resultListVO.setList(resultList);
		}catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			throw ie;
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
			throw new ImcsException();
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			msg = " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID939) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + String.format("%-7s", ImcsConstants.RCV_MSG5 + "]");
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.API_PRO_ID939) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
			
		}
		
		return resultListVO;
	}
}
