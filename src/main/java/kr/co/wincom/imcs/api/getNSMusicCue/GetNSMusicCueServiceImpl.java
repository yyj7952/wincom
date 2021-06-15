package kr.co.wincom.imcs.api.getNSMusicCue;

import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.CommonService;
import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.IMCSLog;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComCueSheetItemVO;
import kr.co.wincom.imcs.common.vo.ComCueSheetMstVO;
import kr.co.wincom.imcs.handler.ImcsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GetNSMusicCueServiceImpl implements GetNSMusicCueService {
	private Log imcsLogger		= LogFactory.getLog("API_getNSMusicCue");
	
	@Autowired
	private GetNSMusicCueDao getNSMusicCueDao;
	
	@Autowired
	private CommonService commonService;

//	public void getNSMusicCue(String szSaId, String szStbMac, String szPid){
//		this.imcsLog	= new IMCSLog(imcsLogger, szSaId, szStbMac, szPid);
//	}
//	
//	private IMCSLog imcsLog = null;
	
	@SuppressWarnings("unused")
	@Override
	public GetNSMusicCueResultVO getNSMusicCue(GetNSMusicCueRequestVO paramVO){
//		this.getNSMusicCue(paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());
		IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
		
		String isLastProcess = ImcsConstants.RCV_MSG5;
		
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
		String methodLine = String.valueOf(stackTraceElement.getLineNumber() - 5);
		String methodName = stackTraceElement.getMethodName();
		imcsLog.debugLog(methodName + " service call");
		
		List<GetNSMusicCueResponseVO> resultVO	= new ArrayList<GetNSMusicCueResponseVO>();
		GetNSMusicCueResultVO resultListVO	= new GetNSMusicCueResultVO();

		String msg	= "";
		
		int nMainCnt = 0;

	    long tp_start = paramVO.getTp_start();
		long tp1, tp2 = 0;

		try{
			tp1	= System.currentTimeMillis();
			imcsLog.timeLog("init...", String.valueOf(tp1 - tp_start), methodName, methodLine); 
			
			//######################################################
			// 로직구현 (시작)
			//######################################################

		    //----------------------------------------------
		   	// 이미지 서버 정보 가져오기
		   	//----------------------------------------------
			String img_chnl_server   = commonService.getImgReplaceUrl2("img_chnl_server", "getNSMusicCue");
			String img_people_server = commonService.getImgReplaceUrl2("img_people_server", "getNSMusicCue");
			String img_still_server = commonService.getImgReplaceUrl2("img_still_server", "getNSMusicCue");
			String img_cuesheet_server   = commonService.getImgReplaceUrl2("img_cuesheet_server", "getNSMusicCue");
			
			//가입자 정보 가져오기 => 검수 여부 (nsvod030_001_20180601)
			tp1 = System.currentTimeMillis();
			String szTestSbc	= "";
			szTestSbc	= this.getTestSbc(paramVO);
			paramVO.setTestSbc(szTestSbc);
			
			if (szTestSbc.equals(""))
				paramVO.setTestSbc("N");
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("검수 STB여부 조회", String.valueOf(tp2 - tp1), methodName, methodLine);
			
		    // ------------------------------------------------------------------------//
		    // SQL - 002 : 해당 앨범의 본편/숏클립 여부 조회 - c_album_gb
		    //-------------------------------------------------------------------------//
		    // 실시간 채널일 경우에는 무조건 본편이므로 체크할 필요 없음
		    // ※ 공연앨범이 아닐 경우 대비해서 우선 파라메터는 추가 설정함
		    //-------------------------------------------------------------------------//
			String albumGb = getNSMusicCueDao.getAlbumGb(paramVO);
			if (albumGb.equals("X")) {
				paramVO.setAlbumGb("0");
			} else {
				paramVO.setAlbumGb(albumGb);
			}
			
		    //앨범 정보를 정상적으로 가져오지 못했을 경우는 오류임
		    //우선은 로직만 잡아놓고 나중에 정리
			if (albumGb == null || albumGb.equals("")) {
				//앨범정보가 없습니다.
				throw new ImcsException();
			}
			
		    // ------------------------------------------------------------------------//
		    // SQL - 010 : 큐시트 정보 가져오기
		    //-------------------------------------------------------------------------//
		    // 본편일 경우 큐시트마스터에서, 숏클립일 경우 큐시트아이템에서 정보를 가져온다
		    //-------------------------------------------------------------------------//
			List<ComCueSheetMstVO> nCueMst = this.getNSMusicCueMst(paramVO);
			
			if (nCueMst ==null || nCueMst.size() == 0) {
				//GetNSMusicCueResponseVO resHeader = new GetNSMusicCueResponseVO();
				//resHeader.setResultFlag("1");
				//resHeader.setResultMsg("큐시트 정보를 가져오지 못했습니다");	
				//결과 헤더 세팅
				resultListVO.setResultHeader("1|큐시트 정보를 가져오지 못했습니다|||||||||");
				return resultListVO;
			}
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("큐시트마스터 정보 가져오기", String.valueOf(tp2 - tp1), methodName, methodLine);
			
		    // ------------------------------------------------------------------------//
		    // SQL - 021 : 큐시트 아이템 정보 가져오기(메인영상: RESULT_TYPE = 'MAIN')
		    //-------------------------------------------------------------------------//
		    int iItemCount = 0;
		    int iMusicCount = 0;
			List<ComCueSheetItemVO> nCueItems = null;
			if (paramVO.getAlbumGb().equals("0")) {
				nCueItems = getNSMusicCueDao.getCueSheetItem1(nCueMst.get(0));
			} else {
				nCueItems = getNSMusicCueDao.getCueSheetItem2(nCueMst.get(0));
			}
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("큐시트 아이템 정보 가져오기 START", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			for (ComCueSheetItemVO nCueItem : nCueItems) {
				iItemCount++;
				nCueItem.setOrderNo(String.format("%03d", iItemCount));
				
		        //노래일 때만 각 아이템별 옴니뷰 정보를 설정하면 된다
		        //옴니뷰 정보는 옴니뷰 갯수, 멤버 수, 가수별 옴니뷰 이미지 정보 등...
		        //노래가 아닐 때는 이미지 정보 제공하지 않음
		        if ( nCueItem.getItemType().equals("MS") )
		        {
		            iMusicCount ++;

					//아이템별 멤버 정보 가져오기
		            //setItemInfo();
		            nCueItem.setCuesheetId(nCueMst.get(0).getCuesheetId());
		            nCueItem = this.getItemInfo(nCueItem, paramVO);
		            
		            nCueItem.setActorImgUrl(img_people_server);
		            nCueItem.setMemberImgUrl(img_people_server);
		        }
		        else
		    	{
		            nCueItem.setActorImgUrl("");
		            nCueItem.setMemberImgUrl("");
		    	}
		        
		        //멤버이미지 파일의 중간 경로를 파일명에 포함하여 제공...
		        if(!nCueItem.getActorImgFile().equals(""))
		        {
		        	nCueItem.setActorImgFile(nCueItem.getActorImgFile().substring(0, 8) + "/" + nCueItem.getActorImgFile());
		        }
				
		        if(nCueItem.getVoteId() == null || "".equals(nCueItem.getVoteId()))
		        {
		        	nCueItem.setVoteId("");
		        	nCueItem.setVoteFlagBgn("");
		        	nCueItem.setVoteFlagEnd("");
		        }
		        else
		        {
		        	if( "2".equals(nCueItem.getVoteStatus()) || ("Y".equals(paramVO.getTestSbc()) && "1".equals(nCueItem.getVoteStatus())))
		        	{
		        		//시작 숏클립
		        		if( "1".equals(nCueItem.getVoteFlag()))
		        		{
				        	nCueItem.setVoteFlagBgn("1");
				        	nCueItem.setVoteFlagEnd("0");
		        		}
		        		//종료 숏클립
		        		else if( "2".equals(nCueItem.getVoteFlag()))
		        		{
				        	nCueItem.setVoteFlagBgn("0");
				        	nCueItem.setVoteFlagEnd("1");
		        		}
		        		//시작/종료 숏클립
		        		else if( "3".equals(nCueItem.getVoteFlag()))
		        		{
				        	nCueItem.setVoteFlagBgn("1");
				        	nCueItem.setVoteFlagEnd("1");
		        		}
		        		//vote_id가 존재 하나 vote_flag가 빈값인 경우, 투표 진행 중
		        		// else if( nCueItem.getVoteFlag() ==null || "".equals(nCueItem.getVoteFlag())
		        		else if( StringUtil.isEmpty(nCueItem.getVoteFlag()))
		        		{
				        	nCueItem.setVoteFlagBgn("0");
				        	nCueItem.setVoteFlagEnd("0");
		        		}
		        	}
		        	else
		        	{
			        	nCueItem.setVoteId("");
			        	nCueItem.setVoteFlagBgn("");
			        	nCueItem.setVoteFlagEnd("");
		        	}
		        }		        
		        
		        //단말요청으로 앨범ID가 없는 경우 강제적으로 ITEM_NO 설정
		        if(nCueItem.getAlbumId().equals(""))
		        {
		        	nCueItem.setAlbumId(nCueItem.getItemNo());
		        }
		        
			}
			
			nCueMst.get(0).setItemCount(String.valueOf(iItemCount));
			nCueMst.get(0).setMusicCount(String.valueOf(iMusicCount));
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("큐시트 아이템 정보 가져오기 END", String.valueOf(tp2 - tp1), methodName, methodLine);
		    // ------------------------------------------------------------------------//
		    // SQL - 022 : 큐시트 아이템 외 부가정보 가져오기(RESULT_TYPE = 'SUB')
		    //-------------------------------------------------------------------------//
		    //##작업중..부가영상은 콘서트/음악방송의 경우만 해당
		    //-------------------------------------------------------------------------//
			List<ComCueSheetItemVO> nAddItems  = new ArrayList<ComCueSheetItemVO>();
			//paramVO.setAlbumGb("0");
			if (paramVO.getAlbumGb().equals("0")) {
				nAddItems = this.getCueAddInfo(paramVO);
//				if (nAddItem!=null) {
//					nAddItem.setItemNo("SUB");
//					nAddItem.setTimeTag("000000");
//					nAddItem.setActorImgUrl(img_still_server);
//					nAddItem.setStatus("0");
//					nAddItem.setMemberName("0");
//				}
				
				if (nAddItems.size() > 0) {
					int i = 0;
					for (ComCueSheetItemVO nAddItem : nAddItems) {
						i++;
						nAddItem.setItemNo("SUB");
						nAddItem.setTimeTag("000000");
						nAddItem.setActorImgUrl(img_still_server);
						nAddItem.setStatus("0");
						nAddItem.setMemberName("0");
						nAddItem.setOrderNo(String.format("%03d", i));
					}
				}
				resultListVO.setCueAddItemList(nAddItems);
				
				tp2	= System.currentTimeMillis();
				imcsLog.timeLog("큐시트 부가영상 정보 가져오기", String.valueOf(tp2 - tp1), methodName, methodLine);
			}
			
		    // ------------------------------------------------------------------------//
		    // SQL - 030 : 큐시트 옴니뷰 정보 가져오기
		    //-------------------------------------------------------------------------//
		    //중요: 공영/방송 요청 시 ITEM 리스트와 '000'의 본영상 옴니뷰만 제공함. 모든 ITEM의 옴니뷰 제공 아님
		    //	      숏클립의 경우 숏클립에 해당하는 옴니뷰만 제공
		    //-------------------------------------------------------------------------//
			List<ComCueSheetItemVO> nOmniVInfo  = new ArrayList<ComCueSheetItemVO>();
			nOmniVInfo = this.getCueOmniVInfo(paramVO, nCueItems.get(0));
			
			for (ComCueSheetItemVO nOmniVItem: nOmniVInfo) {
				if (nCueMst.get(0).getStatus().equals("2")) {

					if (nOmniVItem.getViewFlag().equals("Y")) {
						nOmniVItem.setServiceId(nOmniVItem.getAlbumId());
						
				        // 앨범별 M3U8 정보 설정
						ComCueSheetItemVO m3u8Info = this.getM3U8Info(nOmniVItem, paramVO);
						
						if (m3u8Info!=null) {
							nOmniVItem.setM3u8Castis(m3u8Info.getM3u8Castis());
							nOmniVItem.setM3u8Onnet(m3u8Info.getM3u8Onnet());
						}
					} 
				}
				else
				{
					nOmniVItem.setServiceId(nOmniVItem.getChnlId());
				}
			}
			
			if(resultVO != null){
				nMainCnt = resultVO.size();
			}else{
				paramVO.setResultCode("21000000");
//				imcsLog.failLog(ImcsConstants.NSAPI_PRO_ID030, "", null, "chnl_list:" + ImcsConstants.RCV_MSG3, methodName, methodLine);
			}
			
			tp2	= System.currentTimeMillis();
			imcsLog.timeLog("옴니뷰 정보 가져오기", String.valueOf(tp2 - tp1), methodName, methodLine);
			
			nCueMst.get(0).setImageUrl(img_cuesheet_server + (nCueMst.get(0).getImageFolder() == null?"":nCueMst.get(0).getImageFolder()));
			//System.out.println("####: " + nCueMst.get(0).getImageUrl());
			
			if(!"".equals(nCueMst.get(0).getLogoImgFile()) && !(nCueMst.get(0).getLogoImgFile()==null))
			{
				img_chnl_server = nCueMst.get(0).getImageUrl();
				nCueMst.get(0).setChnlImgFile(nCueMst.get(0).getLogoImgFile());
			}
			
			GetNSMusicCueResponseVO resHeader = new GetNSMusicCueResponseVO();
			resHeader.setResultFlag("0");
			resHeader.setResultMsg("");
			resHeader.setItemCount(nCueMst.get(0).getItemCount());
			resHeader.setMusicCount(nCueMst.get(0).getMusicCount());
			resHeader.setAlbumGb(albumGb);
			resHeader.setServiceId(nCueMst.get(0).getServiceId());
			resHeader.setAngleFlag(nCueMst.get(0).getAngleSvcFlag());
			resHeader.setMemberFlag(nCueMst.get(0).getMemberSvcFlag());
			resHeader.setChannelUrl(img_chnl_server);
			resHeader.setChannelImgFile(nCueMst.get(0).getChnlImgFile());
			resHeader.setImageUrl(nCueMst.get(0).getImageUrl());
			resHeader.setMainImgFile(nCueMst.get(0).getMainImgFile());
			resHeader.setOmniImgFile(nCueMst.get(0).getOmniImgFile());
			
			//결과 헤더 세팅
			resultListVO.setResultHeader(resHeader.toString());
			
			//큐시트 아이템 세팅
			resultListVO.setCueItemList(nCueItems);
			
			//옴니뷰 인포 세팅
			resultListVO.setCueOmniVList(nOmniVInfo);
			//resultListVO.setList(resultVO);
			
			//######################################################
			// 로직구현 (끝)
			//######################################################
			
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
			
			msg	= " svc[" + String.format("%-20s", ImcsConstants.NSAPI_PRO_ID040) + "] sts[" + ImcsConstants.LOG_MSG3 + "] msg[" + isLastProcess + "]"; 
						
			imcsLog.serviceLog(msg, methodName, methodLine);
		}
		
		return resultListVO;
	}
	
	/**
	 * 검수 STB여부 조회
	 * @param paramVO
	 * @return String		TEST_SBC	
	 */
	public String getTestSbc(GetNSMusicCueRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 		= "nsvod030_001_20180601";
		String szTestSbc	= "N";
		
		int querySize		= 0;

		try {			
			List<String> list = getNSMusicCueDao.getTestSbc(paramVO);

			if (list != null && !list.isEmpty()) {
				querySize	= list.size();
				szTestSbc	= StringUtil.replaceNull(list.get(0), "N");
			}else{
				szTestSbc = "N";
			}
			
			try{
				//imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID040, sqlId, null, querySize, methodName, methodLine);
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
	 * @return ComCueSheetMstVO		list	
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<ComCueSheetMstVO> getNSMusicCueMst(GetNSMusicCueRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 		= "nsvod040_010_20180601";
		int querySize		= 0;
		
		List<ComCueSheetMstVO> nCueMst = new ArrayList<ComCueSheetMstVO>();

		try {			
			if (paramVO.getAlbumGb().equals("0")) {
				nCueMst = getNSMusicCueDao.getCueSheetMst1(paramVO);
			} else {
				nCueMst = getNSMusicCueDao.getCueSheetMst2(paramVO);
			}

			if (nCueMst != null && !nCueMst.isEmpty()) {
				querySize	= nCueMst.size();
			} else {
				nCueMst = new ArrayList<ComCueSheetMstVO>();
			}
			
			try{
				//imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID040, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return nCueMst;
	}
	
	/**
	 * 큐시트 아이템별 멤버 정보 가져오기
	 * @param paramVO
	 * 
	 * @return SetItemInfo		list	
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ComCueSheetItemVO getItemInfo(ComCueSheetItemVO nCueItemVO, GetNSMusicCueRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 		= "nsvod040_100_20180601";
		
		int querySize		 = 0;
	    int iOmnivCount      = 0;
	    int iMemberCount     = 0;
	    int	iCheckCount      = 0;
	    String c_actor_id    = "";
	    String c_actor_nm    = "";
	    String c_actor_image = "";
	    
		List<ComCueSheetItemVO> list = new ArrayList<ComCueSheetItemVO>();

		try {			
			list = getNSMusicCueDao.getItemInfo(nCueItemVO);
			
			for (ComCueSheetItemVO item : list) {
				c_actor_nm    = item.getActorName();
				c_actor_image = item.getActorImgFile();
				
				if (c_actor_nm == null) c_actor_nm = " ";
				if (c_actor_image == null || c_actor_image.equals("")) c_actor_image = "";
				
				//옴니뷰 숫자 및 멤버수는 viewing_flag 기준으로 설정
				if (item.getViewFlag().equals("Y")) {
					iOmnivCount ++;
				}
				
				if (item.getViewFlag().equals("Y") && item.getOmnivType().equals("O")) {
					iMemberCount ++;
				} else {
					item.setActorName("");
					item.setActorImgFile("");
					c_actor_nm    = "";
					c_actor_image = "";
				}
				
				//멤버명 및 멤버 이미지는 카메라에 맞춰서 생성
		        if (item.getOmnivType().equals("O"))
		        {
		        	iCheckCount ++;
		        	
		            if ( iCheckCount == 1 )
		            {
		            	nCueItemVO.setMemberName(c_actor_nm);
		            	if(!item.getActorImgFile().equals("")) 
		            	{
		            		c_actor_image =item.getActorImgFile().substring(0,8) + "/" + item.getActorImgFile();
			            	nCueItemVO.setMemberImgFile(nCueItemVO.getMemberImgFile() + c_actor_image);
		            	} 
		            	else
		            	{
		            		nCueItemVO.setMemberImgFile(nCueItemVO.getMemberImgFile() + c_actor_image);
		            	}
		            	
		            }
		            else
		            {
		            	//c_actor_nm = "\b" + item.getActorName();
		            	nCueItemVO.setMemberName(nCueItemVO.getMemberName() + "\b" + c_actor_nm);
		            	
		            	if(!item.getActorImgFile().equals("")) 
		            	{
		            		c_actor_image ="\b" + item.getActorImgFile().substring(0,8) + "/" + item.getActorImgFile();
			            	nCueItemVO.setMemberImgFile(nCueItemVO.getMemberImgFile() + c_actor_image);
		            	} 
		            	else
		            	{
		            		c_actor_image ="\b" + item.getActorImgFile();
		            		nCueItemVO.setMemberImgFile(nCueItemVO.getMemberImgFile() + c_actor_image);
		            	}
		            }
		        }
			}
			
			nCueItemVO.setOmnivCount(String.valueOf(iOmnivCount));
			nCueItemVO.setMemberCount(String.valueOf(iMemberCount));
			
			try{
				//imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID040, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return nCueItemVO;
	}
	
	/**
	 * 큐시트 아이템 외 부가정보 가져오기
	 * @param paramVO
	 * @return getCueAddInfo		list	
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<ComCueSheetItemVO> getCueAddInfo(GetNSMusicCueRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 		= "nsvod040_022_20180601";
		
		int querySize		 = 0;
	    
		//ComCueSheetItemVO addItem = new ComCueSheetItemVO();
		List<ComCueSheetItemVO> listAddItem = new ArrayList<ComCueSheetItemVO>();

		try {			
			listAddItem = getNSMusicCueDao.getCueAddInfo(paramVO);
			try{
				//imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID040, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return listAddItem;
	}
	
	/**
	 * 큐시트 옴니뷰 정보 가져오기
	 * @param paramVO
	 * @return getCueOmniVInfo		list	
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<ComCueSheetItemVO> getCueOmniVInfo(GetNSMusicCueRequestVO paramVO, ComCueSheetItemVO cueItemVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 		= "nsvod040_030_20180601";
		
		int querySize		 = 0;
	
		List<ComCueSheetItemVO> omniVInfo = new ArrayList<ComCueSheetItemVO>();
		List<ComCueSheetMstVO> nCueMst = this.getNSMusicCueMst(paramVO);
		
		try {
			
			if (paramVO.getAlbumGb().equals("1")) {
				omniVInfo = getNSMusicCueDao.getCueOmniVInfo1(nCueMst.get(0));
			} else {
				omniVInfo = getNSMusicCueDao.getCueOmniVInfo2(nCueMst.get(0));				
			}
			
			try{
				//imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID040, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return omniVInfo;
	}
	
	/**
	 * 옴니뷰 영상 M3U8 정보 가져오기
	 * @param paramVO
	 * @return getM3U8Info		list	
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ComCueSheetItemVO getM3U8Info(ComCueSheetItemVO nCueItemVO, GetNSMusicCueRequestVO paramVO) throws Exception {
    	StackTraceElement oStackTrace = Thread.currentThread().getStackTrace()[1];
		String methodName = oStackTrace.getMethodName();
		String methodLine = String.valueOf(oStackTrace.getLineNumber() - 3);
		
		String sqlId 		= "nsvod040_200_20180601";
		
		int querySize		 = 0;
	    
		List<ComCueSheetItemVO> listM3u8 = new ArrayList<ComCueSheetItemVO>();
		ComCueSheetItemVO resM3u8VO = new ComCueSheetItemVO();
		
		int iCount = 0;
		try {			
			listM3u8 = getNSMusicCueDao.getM3U8Info(nCueItemVO);
			
			for (ComCueSheetItemVO item : listM3u8) {
				if (item==null) {
					item = new ComCueSheetItemVO();
					item.setM3u8Castis("");
					item.setM3u8Onnet("");
				}
				
				iCount ++;
				if (iCount == 1)
				{
					resM3u8VO.setM3u8Castis(resM3u8VO.getM3u8Castis() + item.getM3u8Castis()==null?"":item.getM3u8Castis());
					resM3u8VO.setM3u8Onnet(resM3u8VO.getM3u8Onnet() + item.getM3u8Onnet()==null?"":item.getM3u8Onnet());
				}
				else
				{
					resM3u8VO.setM3u8Castis(resM3u8VO.getM3u8Castis() + "\b" + item.getM3u8Castis());
					resM3u8VO.setM3u8Onnet(resM3u8VO.getM3u8Onnet() + "\b"  + item.getM3u8Onnet());
				}

			}
			
			
			try{
				//imcsLog.dbLog(ImcsConstants.NSAPI_PRO_ID040, sqlId, null, querySize, methodName, methodLine);
			}catch(Exception e){}
			
		} catch (Exception e) {
			IMCSLog imcsLog	= new IMCSLog(imcsLogger, paramVO.getSaId(), paramVO.getStbMac(), paramVO.getPid());	
			imcsLog.errorLog(methodName + "-E", e.getClass().getName() + ":" + e.getMessage());
		}
		
		return resM3u8VO;
	}
	

}
