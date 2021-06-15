package kr.co.wincom.imcs.api.getNSSuggestVOD;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSSuggestVODDao {
	
	// 테스트 가입자 여부 조회
	public List<GetNSSuggestVODRequestVO> getTestSbc(GetNSSuggestVODRequestVO paramVO);
	
	// 랭킹 조회(앨범 ID X)
	public List<GetNSSuggestVODRequestVO> getRanking1(GetNSSuggestVODRequestVO paramVO);
	
	// 랭킹 조회(앨범 ID O)
	public List<GetNSSuggestVODRequestVO> getRanking2(GetNSSuggestVODRequestVO paramVO);
	
	// 랭킹 조회(VOD추천정보)
	public List<GetNSSuggestVODRequestVO> getRanking3(GetNSSuggestVODRequestVO paramVO);
	
	// 추천 VOD 조회
	public List<GetNSSuggestVODResponseVO> getNSSuggestVODList(GetNSSuggestVODRequestVO paramVO);
	
	// 추천 VOD 상세조회
	public List<GetNSSuggestVODResponseVO> getNSSuggestVODDtl(GetNSSuggestVODRequestVO paramVO);
			
}
