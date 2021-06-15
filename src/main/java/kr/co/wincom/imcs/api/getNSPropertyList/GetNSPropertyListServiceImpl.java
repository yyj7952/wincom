package kr.co.wincom.imcs.api.getNSPropertyList;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;


import kr.co.wincom.imcs.api.getNSPropertyList.GetNSPropertyListRequestVO;
import kr.co.wincom.imcs.api.getNSPropertyList.GetNSPropertyListResponseVO;
import kr.co.wincom.imcs.api.getNSPropertyList.GetNSPropertyListResultVO;
import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.handler.ImcsException;

@Service
public class GetNSPropertyListServiceImpl implements GetNSPropertyListService {
	
	private Log imcsLogger = LogFactory.getLog("API_getNSPropertyList");
	
	@Autowired
	private GetNSPropertyListDao GetNSPropertyListDao;
	
	@Autowired
	private CommonService commonService;

	


	/**
	 * 
	 * @author 
	 * @since 
	 */
	@Override
	public GetNSPropertyListResultVO getNSPropertyList(GetNSPropertyListRequestVO paramVO) {
		
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog("GetNSPropertyList service call");
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		String szChnlImgSvrip	= commonService.getImgReplaceUrl2("img_prop_server", "getNSPropertyList");
		
		List<GetNSPropertyListSubListVO> listResultVO	= new ArrayList<GetNSPropertyListSubListVO>();
		GetNSPropertyListResultVO	resultListVO	= new GetNSPropertyListResultVO();
		
		List<GetNSPropertyListResponseVO> result = new ArrayList<GetNSPropertyListResponseVO>();
		GetNSPropertyListSubListVO list = new GetNSPropertyListSubListVO();
		GetNSPropertyListSubPropertyVO property = new GetNSPropertyListSubPropertyVO();
		
		Map<String, GetNSPropertyListSubListVO> listMap = new LinkedHashMap<>();
		
		Map<String, List<GetNSPropertyListSubPropertyVO>> tempMultiMap2 = new LinkedHashMap<>();
		
		String msg			= "";
		
		long tp1 = 0, tp2 = 0;
		
		try {
			result = this.getNSPropertyListInfo(paramVO);
			tp2	= System.currentTimeMillis();			
			
			
			for (int i = 0; i < result.size(); i++) {
				GetNSPropertyListResponseVO item = result.get(i);
				
    			if(item.getPropGrpId().length() > 0) {
    				tempMultiMap2.put(item.getPropGrpId(), null);
					list = new GetNSPropertyListSubListVO();
					list.setPropGrpId(item.getPropGrpId());
    				list.setPropGrpNm(item.getPropGrpNm());
    				list.setPropGrpType(item.getPropGrpType());
    				list.setLogoUrl(szChnlImgSvrip);
    				list.setLogoFileName1(item.getLogoFileName1());
    				list.setLogoFileName2(item.getLogoFileName2());
    				list.setTag(item.getTag());
    				
    				if(listMap.get(item.getPropGrpId()) == null) {
    					listMap.put(item.getPropGrpId(),  (GetNSPropertyListSubListVO) list.clone());
    				}
    			}	
			}
			List<GetNSPropertyListSubPropertyVO> properListSet = new ArrayList<GetNSPropertyListSubPropertyVO>();	
			
			for(String key : tempMultiMap2.keySet()) {
				for (int i = 0; i < result.size(); i++) {
					GetNSPropertyListResponseVO item = result.get(i);
					
					if(key.equals(item.getPropGrpId())) {
						if(item.getPropId().length() > 0) {
							property = new GetNSPropertyListSubPropertyVO();
							property.setPropId(item.getPropId());
	    					property.setPropNm(item.getPropNm());
	    					property.setPropTag(item.getPropTag());
	    					property.setViewingFlag(item.getViewingFlag());
	    					property.setControlFlag(item.getControlFlag());
	    					property.setInfo1(item.getInfo1());
	    					
	    					properListSet.add((GetNSPropertyListSubPropertyVO) property.clone());
						}
					}
				}
				tempMultiMap2.put(key, properListSet);
				properListSet = new ArrayList<GetNSPropertyListSubPropertyVO>();	
			}
			
			
			for(String key : tempMultiMap2.keySet()) {
	    				
				if(tempMultiMap2.get(key).size() == 0) {
					listResultVO.add((GetNSPropertyListSubListVO) listMap.get(key).clone());
				} else {
					listMap.get(key).setProperty(tempMultiMap2.get(key));
					listResultVO.add((GetNSPropertyListSubListVO) listMap.get(key).clone());
				}
			}
			

			resultListVO.setList(listResultVO);
			
			
			tp1	= System.currentTimeMillis();			
			imcsLog.timeLog("아이돌라이브 속성 정보 조회", String.valueOf(tp1 - tp2), methodLine, methodLine);

			resultListVO.setFlag("0");
			resultListVO.setTotalCount(String.valueOf(listResultVO.size()));
			resultListVO.setMessage("SUCCESS");
			resultListVO.setList(listResultVO);
		} catch(ImcsException ce) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			resultListVO.setFlag("1");
			resultListVO.setTotalCount("0");
			resultListVO.setMessage("FAIL");
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			resultListVO.setFlag("1");
			resultListVO.setTotalCount("0");
			resultListVO.setMessage("FAIL");
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_GETPROPERTYLIST) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
				
		return resultListVO;
	}
	
	/**
	 * 
	 * @param 
	 * @return 
	 **/
	public List<GetNSPropertyListResponseVO> getNSPropertyListInfo(GetNSPropertyListRequestVO paramVO) throws Exception {
		
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodName = oStackTrace.getMethodName();
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
		String msg				= "";
		
		
		List<GetNSPropertyListResponseVO> result = new ArrayList<GetNSPropertyListResponseVO>();
		
		int querySize = 0;

		try {
			try {
	        	try
	        	{
	        		
	        		result = GetNSPropertyListDao.getNSPropertyListInfo(paramVO);
	        		
	        	}catch(Exception e)
	        	{
	        		msg	= "[getNSPropertyListInfo Error : " + e.getMessage() + "]"; 								
					imcsLog.serviceLog(msg, methodName, methodLine);
	        	}
				
			} catch(DataAccessException e) {
				// DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if(result != null && !result.isEmpty()) {
				querySize = result.size();
			}
			
		} catch(Exception e) {

		}

		return result;
	}
}
