package kr.co.wincom.imcs.api.getNSCatBillInfo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.api.buyNSConts.BuyNSContsRequestVO;
import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.property.ImcsProperties;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.vo.ComDataFreeVO;
import kr.co.wincom.imcs.handler.ImcsException;
import kr.co.wincom.imcs.common.NosqlCacheType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class GetNSCatBillInfoServiceImpl implements GetNSCatBillInfoService {
	private Log imcsLogger = LogFactory.getLog("API_getNSCatBillInfo");
	
	@Autowired
	private GetNSCatBillInfoDao getNSCatBillInfoDao;
	
	@Autowired
	private CommonService commonService;
	
//	public void getNSCatBillInfo(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@Override
	public GetNSCatBillInfoResultVO getNSCatBillInfo(GetNSCatBillInfoRequestVO paramVO){
//		this.getNSCatBillInfo(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		
		imcsLog.debugLog(methodName + " service call");
		
		// NoSQL Db별 쿼리 카운터
		paramVO.setNosqlCacheTypeCnt(new int[NosqlCacheType.values().length]);
		
		List<GetNSCatBillInfoResponseVO> tempVO = new ArrayList<GetNSCatBillInfoResponseVO>();
		List<GetNSCatBillInfoResponseVO> tempVO2 = new ArrayList<GetNSCatBillInfoResponseVO>();
	    List<GetNSCatBillInfoResponseVO> resultVO = new ArrayList<GetNSCatBillInfoResponseVO>();
	    List<GetNSCatBillInfoResponseVO> returnVO = new ArrayList<GetNSCatBillInfoResponseVO>();
	    GetNSCatBillInfoResultVO resultListVO = new GetNSCatBillInfoResultVO();
	    
		String msg	= "";
		
		int nMainCnt = 0;
		int nAlbumCnt = 0;
		Integer resultSet = 0;
		
		int loopCnt = 5;

	    long tp_start = paramVO.getTp_start();
		long tp1 = 0;
		long tp2 = 0;
		long tp3 = 0;
		
		String[] albumArr = null;
		String albumId = "";
		//String isBuy = "";
		String surtaxRate = "";
		
		try{
			
			if( resultSet == 0 && !"".equals(paramVO.getMulitAlbumId()) ){
				
				albumArr = paramVO.getMulitAlbumId().split(",");
				
				nAlbumCnt = albumArr.length;
				
				
				
				if( nAlbumCnt > 30 ) nAlbumCnt = 30;
				
				for(int i = 0; i<nAlbumCnt; i++){
					GetNSCatBillInfoResponseVO infoVO = new GetNSCatBillInfoResponseVO();
					infoVO.setAlbumId(albumArr[i]);
					infoVO.setSubcriptionYn("1");
					tempVO.add(infoVO);
				}	
					
				if(nAlbumCnt % loopCnt == 0){
					nMainCnt = nAlbumCnt / loopCnt;
				}else{
					nMainCnt = nAlbumCnt / loopCnt + 1; 
				}
				
						
				tp1	= System.currentTimeMillis();
				
				List<String> arrAlbumList = null;
				
				for(int i = 0; i < nMainCnt; i++){
					
					tempVO2 = new ArrayList<GetNSCatBillInfoResponseVO>();
					
					arrAlbumList = new ArrayList<String>();
					
					int limitCnt = 0;
					
					if(i == nMainCnt - 1){
						limitCnt = nAlbumCnt;
					}else{
						limitCnt = (loopCnt * i) + loopCnt;
					}
					
					for(int k = (i * loopCnt); k < limitCnt; k++){	
												
						arrAlbumList.add(albumArr[k]);
						
						//imcsLog.serviceLog(k  + " : " + albumArr[k] + " : " + arrAlbumList.size(), methodName, methodLine);
					}
					
					paramVO.setArrAlbumList(arrAlbumList);
					
					try{	
						tempVO2 = this.getAlbumInfo(paramVO);	
						
					}catch(Exception e){
						imcsLog.errorLog(methodName + "-E", e.getClass().getName() + " getAlbumInfo :" + e.getMessage());
					}
					
					
					tp2	= System.currentTimeMillis();
					imcsLog.timeLog("앨범 정보 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
					
					List<String> isSubcription = null;
					
					if(tempVO2 == null){
						//tempVO = new ArrayList<GetNSCatBillInfoResponseVO>();
						//GetNSCatBillInfoResponseVO responseVO = new GetNSCatBillInfoResponseVO();
						//responseVO.setAlbumId(albumId);
						//tempVO.add(responseVO);
						//imcsLog.errorLog(methodName + "-E", "tempVO2 NULL");
					}else{
						arrAlbumList = new ArrayList<String>();
						for(int j = 0; j < tempVO2.size(); j++){
							if(tempVO2.get(j).getProductTypeMin().equals("3") || tempVO2.get(j).getProductTypeMax().equals("3")){
								arrAlbumList.add(tempVO2.get(j).getAlbumId());
							}
						}
						
						if(arrAlbumList != null && arrAlbumList.size() > 0){	
							paramVO.setArrAlbumList(arrAlbumList);
									
							tp1	= System.currentTimeMillis();	
							try{
							
								isSubcription = this.getSubcription(paramVO);
							}catch(Exception e){
								imcsLog.errorLog(methodName + "-E", e.getClass().getName() + " getSubcription :" + e.getMessage());
							}
							
							tp2	= System.currentTimeMillis();
							imcsLog.timeLog("SVOD 편성 조회", String.valueOf(tp2 - tp1), methodName, methodLine);							
						}
						
						
						
						for(int j = 0; j < tempVO2.size(); j++){
							if(isSubcription != null && isSubcription.size() > 0){
								for(int k = 0; k < isSubcription.size(); k++){
									if(tempVO2.get(j).getAlbumId().equals(isSubcription.get(k))){
										tempVO2.get(j).setSubcriptionYn("0");
									}
								}
								
								if(!tempVO2.get(j).getSubcriptionYn().equals("0")){
									tempVO2.get(j).setSubcriptionYn("1");
								}
							}else{
								tempVO2.get(j).setSubcriptionYn("1");
							}
							
							
							
							for(int l = 0; l < nAlbumCnt; l++){
								if(tempVO.get(l).getAlbumId().equals(tempVO2.get(j).getAlbumId())){
									tempVO.remove(l);
									tempVO.add(l, tempVO2.get(j));
											
									
								}
							}
						}
					}
				}
				
				arrAlbumList = new ArrayList<String>();
				
				for(int i = 0; i < nAlbumCnt; i++){
					/*for(int j = 0; j < tempVO2.size(); j++){
						if(tempVO.get(i).getAlbumId() == tempVO2.get(j).getAlbumId()){
							tempVO.add(i, tempVO2.get(j));
						}
					}*/
					
					if("N".equals(tempVO.get(i).getIsSvodOnly()) && "1".equals(tempVO.get(i).getSubcriptionYn())){
						arrAlbumList.add(tempVO.get(i).getAlbumId());
						arrAlbumList.add(tempVO.get(i).getAlbumId() + "_D");
					}
				}
				
				paramVO.setArrAlbumList(arrAlbumList);
				
				List<String> isBuy = new ArrayList<String>();
				
				tp1	= System.currentTimeMillis();
				
				try{	
					if(arrAlbumList != null && arrAlbumList.size() > 0){
						isBuy = this.isBuy(paramVO);
					}
				}catch(Exception e){
					imcsLog.errorLog(methodName + "-E", e.getClass().getName() + " isBuy :" + e.getMessage());
				}
				
				//tmpVO.setBuyYn(isBuy);
				
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("구매 여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
				
				for(int i = 0; i < nAlbumCnt; i++){
					if( i == 0 ){
						
						tp1	= System.currentTimeMillis();					
						
						try{
							surtaxRate = this.getSurtaxRateInfo(paramVO);
						}catch(Exception e){
							imcsLog.errorLog(methodName + "-E", e.getClass().getName() + " getSurtaxRateInfo :" + e.getMessage());
						}
						
						tp2	= System.currentTimeMillis();
						imcsLog.timeLog("부가세요율 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
					}
					tempVO.get(i).setSurtaxRate(surtaxRate);
					if(isBuy != null && isBuy.size() > 0){
						if(tempVO.get(i).getIsSvodOnly().equals("N") && tempVO.get(i).getSubcriptionYn().equals("1") && !tempVO.get(i).getBuyYn().equals("0")){
							
							for(int j = 0; j < isBuy.size(); j++){
								
								if(tempVO.get(i).getAlbumId().equals(isBuy.get(j).substring(0, 15))){
									tempVO.get(i).setBuyYn("0");
									
									break;
								}else{
									tempVO.get(i).setBuyYn("1");
									
									if(tempVO.get(i).getProductTypeMin().equals("0")){								
										tempVO.get(i).setBuyYn("0");
									}
								}
							}
						}
						
					}else{
						if(tempVO.get(i).getIsSvodOnly().equals("N") && tempVO.get(i).getSubcriptionYn().equals("1")){	
							tempVO.get(i).setBuyYn("1");
							if(tempVO.get(i).getProductTypeMin().equals("0")){
								tempVO.get(i).setBuyYn("0");
							}
							
						}					
						
					}
					
					returnVO.add(tempVO.get(i));
				}
				
				
			}
						
			resultListVO.setList(returnVO);
			
		}catch(ImcsException ie) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", ie.getClass().getName() + ie.getMessage());

			// TLO Log 를 Controller Layer 로 전달하기 위해 Setting 한다. (TLO Log 세부값은 finally 문에서 설정된다.)
			ie.setList(resultListVO);
			throw ie;
		} catch(Exception e) {
			isLastProcess = ImcsConstants.RCV_MSG6;
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + e.getMessage());
			
			// TLO Log 를 Controller Layer 로 전달하기 위해 CurationException 으로 변환한다. (TLO Log 세부값은 finally 문에서 설정된다.)
			throw new ImcsException();
		} finally{
			resultListVO.setResultCode(paramVO.getResultCode());
			
			int nUserDB	= paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()];
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID011) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"
						+ " [DB:" + nUserDB + "]"; 
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			msg	= "SurtaxRateCheck DB = ["+surtaxRate+"], File Read = ["+commonService.getSurtaxRate()+"]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);

		}
		
		return resultListVO;
	}
    
//    /**
//     * 앨범 ID로 해당 앨범의 앨범 정보 및 svod only 여부 조회
//     * @param paramVO
//     * @return
//     * @throws Exception
//     */
//    public List<GetNSCatBillInfoResponseVO> getAlbumInfo(GetNSCatBillInfoRequestVO paramVO) throws Exception{
//    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
//    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
//		String methodName = oStackTrace.getMethodName();
//    	
//    	String sqlId =  "nsvod011_001_20180203_002";
//		
//		int querySize = 0;
//		String[] albumArr = null;
//		//List<String> arrAlbumList = new ArrayList<String>();
//		
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
//		
//		List<GetNSCatBillInfoResponseVO> list   = null;
//		List<GetNSCatBillInfoResponseVO> resultVO = null;
//		
//		try {
//			rowKeys.setSqlId(sqlId);
//			
//			//albumArr = paramVO.getMulitAlbumId().split(",");
//			
//			//for(int i = 0; i < albumArr.length; i++){
//			//	arrAlbumList.add(albumArr[i]);
//			//}
//			
//			//paramVO.setArrAlbumList(arrAlbumList);
//			
//			//nAlbumCnt = albumArr.length;
//			
//			albumArr = new String[paramVO.getArrAlbumList().size()];
//			
//			for(int i = 0; i < paramVO.getArrAlbumList().size(); i++){	
//				albumArr[i] = paramVO.getArrAlbumList().get(i);
//				 
//				rowKeys.addRowKeys(paramVO.getArrAlbumList().get(i));
//				checkKey.addVersionTuple("PT_PD_PACKAGE_DETAIL", paramVO.getArrAlbumList().get(i));
//				checkKey.addVersionTuple("PT_LA_ALBUM_MST", paramVO.getArrAlbumList().get(i));				
//			}
//			checkKey.addVersionTuple("PT_PD_PACKAGE");
//			
//			paramVO.setAlbumList(albumArr);
//			
//			binds.add(paramVO);
//			
//			list = cache.getCachedResult(new CacheableExecutor<GetNSCatBillInfoResponseVO>() {
//				@Override
//				public List<GetNSCatBillInfoResponseVO> execute(List<Object> param) throws SQLException {
//					try{
//						GetNSCatBillInfoRequestVO requestVO = (GetNSCatBillInfoRequestVO)param.get(0);
//						
//						List<GetNSCatBillInfoResponseVO> rtnList = getNSCatBillInfoDao.getAlbumInfo(requestVO);
//						
//						return rtnList;
//					}catch(DataAccessException e){
//						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//				}
//				
//				@Override
//				public Class<GetNSCatBillInfoResponseVO> getReturnType() {
//					return GetNSCatBillInfoResponseVO.class;
//				}
//			}, binds, rowKeys, checkKey);
//			
//			if (list == null || list.isEmpty()) {				
//				paramVO.setResultSet(0);
//			} else {
//				querySize = list.size();
//				
//				resultVO = list;
//				
//				for(int i = 0; i < list.size(); i++){	
//					if( "0".equals( resultVO.get(i).getProductTypeMin() ) ){
//						resultVO.get(i).setPrice("0");
//						resultVO.get(i).setInappPrice("0.00");
//					}else if( !"1".equals( resultVO.get(i).getCpPropertyBin() ) ){
//						resultVO.get(i).setInappPrice("0.00");
//					}
//				}
//			}
//			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
//					
//			
//			
//		} catch (Exception e) {
//			if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID011, sqlId, cache, "cont_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
//			
//			paramVO.setResultSet(-1);
//		}
//		
//    	return resultVO;
//    }
    
	
    /**
     * 앨범 ID로 해당 앨범의 앨범 정보 및 svod only 여부 조회
     * @param paramVO
     * @return
     * @throws Exception
     */
    public List<GetNSCatBillInfoResponseVO> getAlbumInfo(GetNSCatBillInfoRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
    	String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		String methodName = oStackTrace.getMethodName();
    			
		String msg = "";
		
		List<GetNSCatBillInfoResponseVO> list   = null;
		List<GetNSCatBillInfoResponseVO> resultVO = null;
		
		try {
			try{
				list = getNSCatBillInfoDao.getAlbumInfo(paramVO);
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if (list == null || list.isEmpty()) {				
				paramVO.setResultSet(0);
			} else {
				resultVO = list;
				
				for(int i = 0; i < list.size(); i++){	
					if( "0".equals( resultVO.get(i).getProductTypeMin() ) ){
						resultVO.get(i).setPrice("0");
						resultVO.get(i).setInappPrice("0.00");
					}else if( !"1".equals( resultVO.get(i).getCpPropertyBin() ) ){
						resultVO.get(i).setInappPrice("0.00");
					}
				}
			}
			
		} catch (Exception e) {
			paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID011, "", null, "getAlbumInfo:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			paramVO.setResultSet(-1);
		}
		
    	return resultVO;
    }
    
    
//    /**
//     * 인앱 가격 조회
//     * @param	GetNSCatBillInfoRequestVO, GetNSCatBillInfoResponseVO
//     * @result	String
//    **/
//    public String getInappInfo(GetNSCatBillInfoRequestVO paramVO, GetNSCatBillInfoResponseVO tempVO) throws Exception{
//    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
//		String methodName = oStackTrace.getMethodName();
//		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
//    	
//    	String sqlId = "nsvod011_003_20171214_001";
//    	String szMsg = "";
//		
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
//		
//		List<String> list   = new ArrayList<String>();
//		String result = null;
//		
//		try {
//			paramVO.setPrice(tempVO.getPrice());
//			
//			rowKeys.setSqlId(sqlId);
//			rowKeys.addRowKeys(paramVO.getPrice());
//			checkKey.addVersionTuple("PT_LA_APPROVAL_INFO");
//			binds.add(paramVO);
//			
//			list = cache.getCachedResult(new CacheableExecutor<String>() {
//				@Override
//				public List<String> execute(List<Object> param) throws SQLException {
//					try{
//						GetNSCatBillInfoRequestVO requestVO = (GetNSCatBillInfoRequestVO)param.get(0);
//						List<String> rtnList  = getNSCatBillInfoDao.getInappInfo(requestVO);
//							
//						return rtnList;
//					}catch(DataAccessException e){
//						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//				}
//				
//				@Override
//				public Class<String> getReturnType() {
//					return String.class;
//				}
//			}, binds, rowKeys, checkKey);
//			
//			if( list != null && !list.isEmpty()){
//				result = (String)list.get(0);
//			}else{
//				szMsg	= "Not Found Approval Info["+ paramVO.getPrice() +"]";
//				imcsLog.serviceLog(szMsg, methodName, methodLine);
//				
//				// 2017.06.07 인앱가격을 0.00으로 변경해야 할 것 같은데, 원화가격을 0원으로 하여 변경
//				result = "0.00";				
//			}
//			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
//			
//		} catch (Exception e) {
//			 imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID011, sqlId, cache, "approval_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
//			 
//			 paramVO.setResultSet(-1);
//		}
//    	return result;
//    }
    
    
    /**
     * 인앱 가격 조회
     * @param	GetNSCatBillInfoRequestVO, GetNSCatBillInfoResponseVO
     * @result	String
    **/
    public String getInappInfo(GetNSCatBillInfoRequestVO paramVO, GetNSCatBillInfoResponseVO tempVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
    	
    	String msg = "";
		
		List<String> list   = new ArrayList<String>();
		String result = null;
		
		try {
			paramVO.setPrice(tempVO.getPrice());
			
			try{
				list  = getNSCatBillInfoDao.getInappInfo(paramVO);	
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}
			
			if( list != null && !list.isEmpty()){
				result = (String)list.get(0);
			}else{
				msg	= "Not Found Approval Info["+ paramVO.getPrice() +"]";
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				// 2017.06.07 인앱가격을 0.00으로 변경해야 할 것 같은데, 원화가격을 0원으로 하여 변경
				result = "0.00";				
			}
			
		} catch (Exception e) {
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID011, "", null, "approval_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			 
			 paramVO.setResultSet(-1);
		}
    	return result;
    }
    
    
//    /**
//     * 앨범 ID가 포함된 svod에 가입 되어 있는지 조회
//     * @param	GetNSCatBillInfoRequestVO
//     * @result	String
//    **/
//    public List<String> getSubcription(GetNSCatBillInfoRequestVO paramVO) throws Exception{
//    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
//		String methodName = oStackTrace.getMethodName();
//		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
//    	
//    	String sqlId = "nsvod011_002_20190227_001";
//    	String szMsg = "";
//		
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
//		
//		List<String> list   = new ArrayList<String>();
//		String result = null;
//		
//		try {
//			//if( "".equals(paramVO.getSaId()) || "".equals(paramVO.getStbMac()) ) return "1";
//			
//			rowKeys.setSaId(paramVO.getSaId());
//			rowKeys.setStbMac(paramVO.getStbMac());
//			rowKeys.setSqlId(sqlId);
//			checkKey.addVersionTuple("PT_VO_CUSTOM_PRODUCT", paramVO.getSaId());			
//			checkKey.addVersionTuple("PT_PD_PACKAGE_RELATION");
//						
//			for(int i = 0; i < paramVO.getArrAlbumList().size(); i++){
//				rowKeys.addRowKeys(paramVO.getArrAlbumList().get(i));
//				checkKey.addVersionTuple("PT_PD_PACKAGE_DETAIL", paramVO.getArrAlbumList().get(i));
//				checkKey.addVersionTuple("PT_LA_ALBUM_MST", paramVO.getArrAlbumList().get(i));
//			}
//			
//			binds.add(paramVO);
//			
//			list = cache.getCachedResult(new CacheableExecutor<String>() {
//				@Override
//				public List<String> execute(List<Object> param) throws SQLException {
//					try{
//						GetNSCatBillInfoRequestVO requestVO = (GetNSCatBillInfoRequestVO)param.get(0);
//						List<String> rtnList  = getNSCatBillInfoDao.getSubcription(requestVO);
//							
//						return rtnList;
//					}catch(DataAccessException e){
//						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//				}
//				
//				@Override
//				public Class<String> getReturnType() {
//					return String.class;
//				}
//			}, binds, rowKeys, checkKey);
//			
//			/*if( list != null && !list.isEmpty()){
//				result = "0";	
//			}else{				
//				result = "1";				
//			}*/
//			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
//			
//		} catch (Exception e) {
//			if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID011, sqlId, cache, "cont_info:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
//			
//			paramVO.setResultSet(-1);
//		}
//    	return list;
//    }
    
    
    /**
     * 앨범 ID가 포함된 svod에 가입 되어 있는지 조회
     * @param	GetNSCatBillInfoRequestVO
     * @result	String
    **/
    public List<String> getSubcription(GetNSCatBillInfoRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
    	String msg = "";
		
		List<String> list   = new ArrayList<String>();
		String result = null;
		
		try {				
			try{
				list  = getNSCatBillInfoDao.getSubcription(paramVO);	
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}		
			
		} catch (Exception e) {
			paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID011, "", null, "getSubcription:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.serviceLog(msg, methodName, methodLine);
			
			paramVO.setResultSet(-1);
		}
    	return list;
    }
    
    
    /**
     * 구매 여부 조회
     * @param	ComDataFreeVO, ChkBuyNSPGRequestVO
     * @result	ComDataFreeVO
    **/
    public List<String> isBuy(GetNSCatBillInfoRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	

    	List<String> list   = new ArrayList<String>();
		
		try {
			//if( "0".equals(tempVO.getProductTypeMin()) ) return "0";
			
			list = getNSCatBillInfoDao.isBuy(paramVO);
			paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
			
			/*if( list != null && !list.isEmpty()){
				result = "0";	
			}else{				
				result = "1";				
			}*/
			
		} catch (Exception e) {

//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID011, "", null, "isBuy:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
		}
    	return list;
    }
    
//    /**
//     * 부가세요율 조회
//     * @param	GetNSCatBillInfoRequestVO
//     * @result	String
//    **/
//    public String getSurtaxRateInfo(GetNSCatBillInfoRequestVO paramVO) throws Exception{
//    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
//		String methodName = oStackTrace.getMethodName();
//		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
//    	
//    	String sqlId = "nsvod011_s04_20171214_001";
//    	String szMsg = "";
//		
//		NosqlResultCache cache = new NosqlResultCache();
//		List<Object> binds = new ArrayList<Object>();
//		RowKeyList rowKeys = new RowKeyList();
//		VersionUpdateCheckKey checkKey = new VersionUpdateCheckKey();
//		
//		List<String> list   = new ArrayList<String>();
//		String result = null;
//		
//		try {
//			
//			rowKeys.setSqlId(sqlId);
//			checkKey.addVersionTuple("PT_CD_COM_CD", "SURTAXRATE");
//			binds.add(paramVO);
//			
//			list = cache.getCachedResult(new CacheableExecutor<String>() {
//				@Override
//				public List<String> execute(List<Object> param) throws SQLException {
//					try{
//						List<String> rtnList  = getNSCatBillInfoDao.getSurtaxRateInfo();
//							
//						return rtnList;
//					}catch(DataAccessException e){
//						//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
//						throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
//					}
//				}
//				
//				@Override
//				public Class<String> getReturnType() {
//					return String.class;
//				}
//			}, binds, rowKeys, checkKey);
//			
//			if( list != null && !list.isEmpty()){
//				result = list.get(0);				
//			}else{				
//				
//				szMsg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID011) + "] sts[SQL_CODE] msg[SURTAXRATE INFO:Not Found]"; 
//							
//				imcsLog.serviceLog(szMsg, methodName, methodLine);
//				
//				paramVO.setResultSet(-1);			
//			}
//			
//			paramVO.getNosqlCacheTypeCnt()[cache.getLastExecutionCacheType().ordinal()]++;
//			
//		} catch (Exception e) {
//			if(cache.getLastException() != null)	paramVO.setResultCode("4100" + cache.getLastException().getErrorCode());
//			else									paramVO.setResultCode("41000000");
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID011, sqlId, cache, "SURTAXRATE INFO:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
//			
//			paramVO.setResultSet(-1);
//		}
//    	return result;
//    }
    
    
    /**
     * 부가세요율 조회
     * @param	GetNSCatBillInfoRequestVO
     * @result	String
    **/
    public String getSurtaxRateInfo(GetNSCatBillInfoRequestVO paramVO) throws Exception{
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
    	
    	String msg = "";
		
		List<String> list   = new ArrayList<String>();
		String result = null;
		
		try {
			try{
				list  = getNSCatBillInfoDao.getSurtaxRateInfo();
				paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] = paramVO.getNosqlCacheTypeCnt()[NosqlCacheType.USERDB.ordinal()] +1;
				
			}catch(DataAccessException e){
				//DB관련 Exception발생 시 getLastException을 받기위해 SQLException으로 셋팅
				throw new SQLException(e.getClass().getName() + ":" + e.getMessage());
			}

			if( list != null && !list.isEmpty()){
				result = list.get(0);				
			}else{				
				
				msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID011) + "] sts[SQL_CODE] msg[SURTAXRATE INFO:Not Found]"; 
				IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());				
				imcsLog.serviceLog(msg, methodName, methodLine);
				
				paramVO.setResultSet(-1);			
			}
			
		} catch (Exception e) {
			paramVO.setResultCode("41000000");
			
//			imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID011, "", null, "SURTAXRATE INFO:" + ImcsConstants.RCV_MSG6, methodName, methodLine);
			
			paramVO.setResultSet(-1);
		}
    	return result;
    }
}
