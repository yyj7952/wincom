package kr.co.wincom.imcs.api.getNSMultiView;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSMultiViewDao {
	// 멀티뷰 채널정보 조회
	public List<GetNSMultiViewResponseVO> getNSMultiView(GetNSMultiViewRequestVO paramVO);
		
}
