package kr.co.wincom.imcs.api.chkSubscribeProd;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.wincom.imcs.common.vo.ComSbcVO;

@Repository
public interface ChkSubscribeProdDao {
	
	// 가입자 상태, 개통여부 조회
	public List<ComSbcVO> getSbcInfo(ChkSubscribeProdRequestVO paramVO);
	
	// 동일 SVOD 가입여부 조회
	public List<ChkSubscribeProdResultVO> getBuyContsProdType(ChkSubscribeProdRequestVO paramVO);
	
}
