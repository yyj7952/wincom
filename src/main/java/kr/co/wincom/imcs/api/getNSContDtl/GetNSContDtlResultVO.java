package kr.co.wincom.imcs.api.getNSContDtl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.ComDataFreeVO;
import kr.co.wincom.imcs.common.vo.OstInfoVO;
import kr.co.wincom.imcs.common.vo.StatVO;
import kr.co.wincom.imcs.common.vo.ComWatchaVO;


@SuppressWarnings("serial")
public class GetNSContDtlResultVO extends StatVO implements Serializable {
    private GetNSContDtlResponseVO list;
    private List<OstInfoVO> ostInfo;
    private ComWatchaVO watchaInfo;
    private ComDataFreeVO datafreeInfo;
    
	public GetNSContDtlResponseVO getList() {
		return list;
	}
	public void setList(GetNSContDtlResponseVO list) {
		this.list = list;
	}
	public List<OstInfoVO> getOstInfo() {
		return ostInfo;
	}
	public void setOstInfo(List<OstInfoVO> ostInfo) {
		this.ostInfo = ostInfo;
	}
	public ComWatchaVO getWatchaInfo() {
		return watchaInfo;
	}
	public void setWatchaInfo(ComWatchaVO watchaInfo) {
		this.watchaInfo = watchaInfo;
	}   
	public ComDataFreeVO getDatafreeInfo() {
		return datafreeInfo;
	}
	public void setDatafreeInfo(ComDataFreeVO datafreeInfo) {
		this.datafreeInfo = datafreeInfo;
	}
	
	@Override
	public String toString() {
		StringBuffer sb= new StringBuffer();
		
		if(list != null){
			
			if(this.watchaInfo == null)	watchaInfo = new ComWatchaVO();
			if(this.ostInfo == null) ostInfo = new ArrayList<OstInfoVO>();
			if(this.datafreeInfo == null) datafreeInfo = new ComDataFreeVO();

			//tmpsndbuf_tmp1
			sb.append(StringUtil.nullToSpace(this.list.getContsId())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getContsName())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getChaNum())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getImgUrl1())).append(ImcsConstants.ARRSEP);
			sb.append(StringUtil.nullToSpace(this.list.getImgUrl())).append(ImcsConstants.ARRSEP);
			sb.append(StringUtil.nullToSpace(this.list.getImgUrl2())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getImgFileName())).append(ImcsConstants.ARRSEP);
			sb.append(StringUtil.nullToSpace(this.list.getImgFileName())).append(ImcsConstants.ARRSEP);
			sb.append(StringUtil.nullToSpace(this.list.getImgFileName())).append(ImcsConstants.COLSEP);
			sb.append(this.list.getPriceDesc()).append(ImcsConstants.COLSEP);
			
			//tmpsndbuf_tmp2
			sb.append(StringUtil.nullToSpace(this.list.getRuntime())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getPrInfo())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getIs51Ch())).append(ImcsConstants.COLSEP);
			sb.append(this.list.getSynopsis()).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getIsNew())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getIsUpdate())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getIsBest())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getOverseerName())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getActor())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getIsCaption())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getIsHd())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getEventType())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getPoint())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.list.getProductType(), "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.replaceNull(this.list.getBuyYn(), "")).append(ImcsConstants.COLSEP);
			sb.append(this.list.getExpiredDate()).append(ImcsConstants.COLSEP);
			
			//tmpsndbuf_tmp3
			sb.append(StringUtil.nullToSpace(this.list.getIs3d())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getEventValue())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getPreviewYn())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getServiceIcon())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getOnairDate())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getSeriesDesc())).append(ImcsConstants.COLSEP);
			sb.append(this.list.getBuyDate()).append(ImcsConstants.COLSEP);
			
			//tmpsndbuf_tmp5
			sb.append(StringUtil.nullToSpace(this.list.getTerrCh())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getReleaseDate())).append(ImcsConstants.COLSEP);
			
			sb.append(StringUtil.replaceNull(this.list.getCatGb(), "")).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getSetPointYn())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getTrailerUrl1())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getTrailerFileName1())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getTrailerUrl2())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getTrailerFileName1())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getTrailerUrl3())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getTrailerFileName1())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getDownYn())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getStillImgUrl())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getThumbImgUrl())).append(ImcsConstants.COLSEP);
			
			//tmpsndbuf_tmp6
			sb.append(StringUtil.nullToSpace(this.list.getGenreMid())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getImgUrl3())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getTicketId())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getTasteCatId())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getTasteAlbumId())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getRealHDYn())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getGenreLarge())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getGenreName())).append(ImcsConstants.COLSEP);
			
			//tmpsndbuf_tmp7
			sb.append(StringUtil.nullToSpace(this.list.getQdFlag())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getFmYn())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getAssetId())).append(ImcsConstants.COLSEP);
			
			//tmpsndbuf_tmp8
			sb.append(StringUtil.nullToSpace(this.list.getCouponYn())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getStampYn())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.datafreeInfo.getApprovalId())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.datafreeInfo.getApprovalPrice())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getMycutYn())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getSmiLanguage())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getPreviewFlag())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getReservedPrice())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.watchaInfo.getPointWatcha())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getReservedDate())).append(ImcsConstants.COLSEP);
			
			//tmpsndbuf_tmp9
			sb.append(StringUtil.nullToSpace(this.watchaInfo.getTotRatingCount())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.watchaInfo.getRating01())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.watchaInfo.getRating02())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.watchaInfo.getRating03())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.watchaInfo.getRating04())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.watchaInfo.getRating05())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.watchaInfo.getRating06())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.watchaInfo.getRating07())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.watchaInfo.getRating08())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.watchaInfo.getRating09())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.watchaInfo.getRating10())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.watchaInfo.getCommentCnt())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.watchaInfo.getWatchaUrl())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getGenreUxten())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getPromotionCopy())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getCpProperty())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getCpPropertyUfx())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getPresentYn())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getPresentPrice())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getDatafreeBillYn())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.datafreeInfo.getDatafreePrice())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.datafreeInfo.getDatafreeApprovalPrice())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.datafreeInfo.getDatafreeApprovalId())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.datafreeInfo.getPpvDatafreeApprovalPrice())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.datafreeInfo.getPpvDatafreeApprovalId())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getSeasonYn())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getSurtaxrate())).append(ImcsConstants.COLSEP);
			sb.append(StringUtil.nullToSpace(this.list.getTerrEdDate())).append(ImcsConstants.COLSEP);

			
			sb.append(ImcsConstants.ROWSEP);
			
			if(ostInfo != null && !ostInfo.isEmpty()){
				int OstSize = ostInfo.size();
				for(int j=0; j<OstSize; j++){
					
					OstInfoVO ostInfoVO = ostInfo.get(j);
					sb.append(ostInfoVO.toString());
				}
			}
		}
		
		return sb.toString();
    }
    
}
