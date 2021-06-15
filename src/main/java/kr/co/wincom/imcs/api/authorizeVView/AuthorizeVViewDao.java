package kr.co.wincom.imcs.api.authorizeVView;

import java.util.List;

import kr.co.wincom.imcs.common.vo.ComCdnVO;
import kr.co.wincom.imcs.common.vo.M3u8ProfileVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;
import kr.co.wincom.imcs.common.vo.AuthorizePlayIpVO;

import org.apache.zookeeper.server.persistence.FileTxnSnapLog.PlayBackListener;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizeVViewDao {
	
	// PPV 여부 조회
	public List<Integer> getPpvYn(AuthorizeVViewRequestVO paramVO);
	
	// 워터마크 정보 조회
	public List<String> getWatermarkCk(AuthorizeVViewRequestVO paramVO);
	
	// 자막 정보 조회
	public List<AuthorizeVViewResponseVO> getSmiInfo(AuthorizeVViewRequestVO paramVO);
	
	// 노드 정보 조회
	public List<String> getNodeCd(AuthorizePlayIpVO playIpVO);
	
	// VOD 서버 정보 조회
	public List<AuthorizePlayIpVO> getVodServer1(AuthorizePlayIpVO playIpVO);
	public List<AuthorizePlayIpVO> getVodServer2(AuthorizePlayIpVO playIpVO);
	public List<AuthorizePlayIpVO> getVodServer3(AuthorizePlayIpVO playIpVO);
	public List<AuthorizePlayIpVO> getVodServer4(AuthorizePlayIpVO playIpVO);

	// 구간점프 이미지 조회
	public List<StillImageVO> getThumnailInfo(AuthorizeVViewRequestVO requestVO);

	// 노드 정보 조회(LTE 빼고)
	public List<String> getNodeCdLoadBalancing1(AuthorizePlayIpVO PlayVO);
	
	// 노드 정보 조회(SVC_NODE가 N이 아닐 때)
	public List<String> getNodeCdLoadBalancing2(AuthorizePlayIpVO PlayVO);	
	
	// VOD m3u8 정보 조회
	public List<M3u8ProfileVO> getVodProfile(AuthorizeVViewRequestVO paramVO);
}
