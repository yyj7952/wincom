package kr.co.wincom.imcs.api.rmFXAllFavor;

import org.springframework.stereotype.Repository;

@Repository
public interface RmFXAllFavorDao {

	// 찜목록 전체 삭제
	int deleteAllFavor(RmFXAllFavorRequestVO requestVO);
	
}
