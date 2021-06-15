package kr.co.wincom.imcs.api.getNSVSI;

import java.util.List;

import kr.co.wincom.imcs.common.vo.ComCdVO;
import kr.co.wincom.imcs.common.vo.ComNodeVO;
import kr.co.wincom.imcs.common.vo.ComProdInfoVO;
import kr.co.wincom.imcs.common.vo.M3u8ProfileVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSVSIDao {
	
	// 테스트 가입자 여부 조회
	public List<String> getTestSbc(GetNSVSIRequestVO paramVO);
	
	// 가입 상품 조회
	public List<String> getmProdId(GetNSVSIRequestVO paramVO);
	
	// 실시간 서버 조회
	public List<ComCdVO> getLiveTimeServer();
		
	// 노드 조회
	public List<ComNodeVO> getNode(GetNSVSIRequestVO paramVO);
	
	// 동 여부 조회
	public List<String> getDongYn(GetNSVSIRequestVO paramVO);
	
	// 인기순으로  Nscreen 가상채널 EPG전체 채널정보 조회
	public List<GetNSVSIResponseVO> getNSVSIListH(GetNSVSIRequestVO paramVO);
	
	// 기본으로  Nscreen 가상채널 EPG전체 채널정보 조회
	public List<GetNSVSIResponseVO> getNSVSIListN(GetNSVSIRequestVO paramVO);
	
	// 제목순으로  Nscreen 가상채널 EPG전체 채널정보 조회
	public List<GetNSVSIResponseVO> getNSVSIListA(GetNSVSIRequestVO paramVO);
	
	// 장르명 조회
	public List<String> getComName(GetNSVSIRequestVO paramVO);
	
	// 앨범이 속한 상품 조회
	public List<ComProdInfoVO> getProdInfo(GetNSVSIRequestVO paramVO);
	
	// 선호채널 여부 조회
	public List<String> getFavorYn(GetNSVSIRequestVO paramVO);
	
	// 최소 SAVE_TIME 조회
	public List<String> getSaveTime(GetNSVSIRequestVO paramVO);
	
	// 노드 정보 조회(와이파이)
	public List<String> getNodeCdW(GetNSVSIRequestVO paramVO);
	
	// 노드 정보 조회(해외노드 와이파이)
	public List<String> getNodeCdAW(GetNSVSIRequestVO paramVO);
	
	// 노드 정보 조회(해외노드 LTE)
	public List<String> getNodeCdAL(GetNSVSIRequestVO paramVO);
	
	// 노드 정보 조회(해외노드 LTE)
	public List<String> getNodeCdTW(GetNSVSIRequestVO paramVO);

	// 노드 정보 조회(해외노드 LTE)
	public List<String> getNodeCdTL(GetNSVSIRequestVO paramVO);

	// 노드 정보 조회(와이파이)
	public List<String> ChnlM3u8Search(GetNSVSIRequestVO paramVO);
	
	// 2018.02.23 - 프로야구2.0. 모바일 골프APP m3u8신규 테이블 조회 (서비스ID 기준)
	public List<M3u8ProfileVO> getChnlm3u8(GetNSVSIResponseVO resultVO);
}
