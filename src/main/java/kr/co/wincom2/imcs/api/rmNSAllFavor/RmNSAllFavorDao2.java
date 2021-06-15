package kr.co.wincom2.imcs.api.rmNSAllFavor;

import org.springframework.stereotype.Repository;

import kr.co.wincom.imcs.api.rmNSAllFavor.RmNSAllFavorRequestVO;

@Repository
public interface RmNSAllFavorDao2 {
	
	// 찜 목록 삭제
	public int rmNSAllFavor(RmNSAllFavorRequestVO requestVO);

}
