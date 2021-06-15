package kr.co.wincom.imcs.api.getNSVoteAlbum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSVoteAlbumDao {
	
	public HashMap<String, String> getTestSbc(GetNSVoteAlbumRequestVO resultVO);
	
	public List<GetNSVoteAlbumResponseVO> getVoteAlbumDtlList(GetNSVoteAlbumRequestVO requestVO);

	public ArrayList<String> getStillImage(GetNSVoteAlbumResponseVO requestVO);
	
	public GetNSVoteAlbumResponseVO getCueSheetInfo1(GetNSVoteAlbumResponseVO requestVO);
	
	public GetNSVoteAlbumResponseVO getCueSheetInfo2(GetNSVoteAlbumResponseVO requestVO);
	
}
