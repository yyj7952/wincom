package kr.co.wincom2.imcs.api.rmFXAllFavor;

import org.springframework.stereotype.Repository;

import kr.co.wincom.imcs.api.rmFXAllFavor.RmFXAllFavorRequestVO;

@Repository
public interface RmFXAllFavorDao2 {
	
	// 찜 목록 삭제
	public int deleteAllFavor(RmFXAllFavorRequestVO requestVO);

}
