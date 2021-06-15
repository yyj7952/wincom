package kr.co.wincom.imcs.api.getNSMnuList2;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;


@Repository
public interface GetNSMnuList2Dao {
	// 테스트 가입자 여부 조회
	public List<String> getTestSbc(GetNSMnuList2RequestVO paramVO);
	
	//카테고리 레벨조회
	public HashMap<String, String> getCateLevel(GetNSMnuList2RequestVO paramVO);
	
	//카테고리 ID조회
	public String getCateId(GetNSMnuList2RequestVO paramVO);

	//부모(Parent) 버전 조회
	public HashMap<String, String> getParentVersion(GetNSMnuList2RequestVO paramVO);
	public HashMap<String, String> getParentVersion2(GetNSMnuList2RequestVO paramVO);

}
