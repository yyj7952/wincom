package kr.co.wincom.imcs.api.getNSVoteAlbum;

import java.util.List;

public interface GetNSVoteAlbumService {
	
	public GetNSVoteAlbumResultVO getNSVoteAlbum(GetNSVoteAlbumRequestVO paramVO);
	
	public void getTestSbc(GetNSVoteAlbumRequestVO paramVO) throws Exception;
	
	public List<GetNSVoteAlbumResponseVO> getVoteAlbumDtlList(GetNSVoteAlbumRequestVO paramVO) throws Exception;
	
	public void getCueSheetInfo(GetNSVoteAlbumRequestVO paramVO, GetNSVoteAlbumResponseVO tempVO) throws Exception;
	
	public void getStillImage(GetNSVoteAlbumRequestVO paramVO, GetNSVoteAlbumResponseVO tempVO) throws Exception;
	
}
