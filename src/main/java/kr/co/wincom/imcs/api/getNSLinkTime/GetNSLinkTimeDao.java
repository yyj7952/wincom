package kr.co.wincom.imcs.api.getNSLinkTime;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSLinkTimeDao {
	// 이어보기 정보 조회
	public String getNSLinkTime(GetNSLinkTimeRequestVO paramVO);
}
