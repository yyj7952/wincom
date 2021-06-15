package kr.co.wincom.imcs.api.getFXRelation;

import java.io.Serializable;
import java.util.List;

import kr.co.wincom.imcs.common.ImcsConstants;
import kr.co.wincom.imcs.common.util.StringUtil;
import kr.co.wincom.imcs.common.vo.StatVO;
import kr.co.wincom.imcs.common.vo.ComWatchaVO;

public class GetFXRelationResultVO extends StatVO implements Serializable {
	
	private static final long serialVersionUID = 170116096565178079L;
	
	private List<GetFXRelationResponseVO> list;
	private List<ComWatchaVO> wlist;
	private ComWatchaVO watchaVO;
	private String result = "";
	
	public ComWatchaVO getWatchaVO() {
		return watchaVO;
	}

	public void setWatchaVO(ComWatchaVO watchaVO) {
		this.watchaVO = watchaVO;
	}
	
	public List<GetFXRelationResponseVO> getList() {
		return list;
	}

	public void setList(List<GetFXRelationResponseVO> list) {
		this.list = list;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	

	public List<ComWatchaVO> getWlist() {
		return wlist;
	}

	public void setWlist(List<ComWatchaVO> wlist) {
		this.wlist = wlist;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		if(watchaVO == null) 	watchaVO = new ComWatchaVO();
		
		if(this.result != null && !"".equals(this.result)) {
			sb.append(this.result);
		} else {
			if(this.getList() != null && this.getList().size() > 0) {
				StringBuilder record = new StringBuilder();
				
				int i = 0;
				
				for(GetFXRelationResponseVO vo : this.getList()) {
					record.append(StringUtil.replaceNull(vo.getCatId(), "")).append(ImcsConstants.COLSEP);
					record.append(StringUtil.replaceNull(vo.getCatName(), "")).append(ImcsConstants.COLSEP);
					record.append(StringUtil.replaceNull(vo.getAlbumId(), "")).append(ImcsConstants.COLSEP);
					record.append(StringUtil.replaceNull(vo.getAlbumName(), "")).append(ImcsConstants.COLSEP);
					record.append(StringUtil.replaceNull(vo.getImgUrl(), "")).append(ImcsConstants.COLSEP);
					record.append(StringUtil.replaceNull(vo.getImgFileName(), "")).append(ImcsConstants.COLSEP);
					record.append(StringUtil.replaceNull(vo.getChaNum(), "")).append(ImcsConstants.COLSEP);
					record.append(StringUtil.replaceNull(vo.getPrice(), "")).append(ImcsConstants.COLSEP);
					record.append(StringUtil.replaceNull(vo.getPrInfo(), "")).append(ImcsConstants.COLSEP);
					record.append(StringUtil.replaceNull(vo.getRunTime(), "")).append(ImcsConstants.COLSEP);
					record.append(StringUtil.replaceNull(vo.getIs51Ch(), "")).append(ImcsConstants.COLSEP);
					record.append(StringUtil.replaceNull(vo.getIsNew(), "")).append(ImcsConstants.COLSEP);
					record.append(StringUtil.replaceNull(vo.getIsHot(), "")).append(ImcsConstants.COLSEP);
					record.append(StringUtil.replaceNull(vo.getIsCaption(), "")).append(ImcsConstants.COLSEP);
					record.append(StringUtil.replaceNull(vo.getIsHd(), "")).append(ImcsConstants.COLSEP);
					record.append(StringUtil.replaceNull(vo.getPoint(), "")).append(ImcsConstants.COLSEP);
					record.append(StringUtil.replaceNull(vo.getIs3d(), "")).append(ImcsConstants.COLSEP);
					record.append(StringUtil.replaceNull(vo.getOnairDate(), "")).append(ImcsConstants.COLSEP);
					record.append(StringUtil.replaceNull(vo.getSeriesDesc(), "")).append(ImcsConstants.COLSEP);
					record.append(StringUtil.replaceNull(vo.getTerrYn(), "")).append(ImcsConstants.COLSEP);
					record.append(StringUtil.replaceNull(vo.getSeriesYn(), "")).append(ImcsConstants.COLSEP);
					record.append(StringUtil.replaceNull(vo.getSerCatId(), "")).append(ImcsConstants.COLSEP);
					record.append(StringUtil.replaceNull(vo.getRealHd(), "")).append(ImcsConstants.COLSEP).append(ImcsConstants.COLSEP);					
					record.append(wlist.get(i).toWatchaString());
					record.append(StringUtil.replaceNull(vo.getServiceIcon(), "")).append(ImcsConstants.COLSEP);
					record.append(ImcsConstants.ROWSEP);
					
					i++;
				}
				
				sb.append(record.toString());
			}
		}
		return sb.toString();
	}

}
