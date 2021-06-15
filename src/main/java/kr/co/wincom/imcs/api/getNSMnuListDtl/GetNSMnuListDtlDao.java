package kr.co.wincom.imcs.api.getNSMnuListDtl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.wincom.imcs.common.vo.ComDataFreeVO;
import kr.co.wincom.imcs.common.vo.ComDupCHk;
import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.FmInfoVO;
import kr.co.wincom.imcs.common.vo.ComTrailerVO;
import kr.co.wincom.imcs.common.vo.ComWatchaVO;
import kr.co.wincom.imcs.common.vo.OstInfoVO;
import kr.co.wincom.imcs.common.vo.SvodPkgVO;
import kr.co.wincom.imcs.common.vo.StillImageVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSMnuListDtlDao {

	// 검수 STB 여부 조회
	public List<String> testSbc(GetNSMnuListDtlRequestVO paramVO);
	
	// 카테고리ID의 카테고리 level 확인
	@SuppressWarnings("rawtypes")
	public List<HashMap> confirmCatLevelByCatId(GetNSMnuListDtlRequestVO paramVO);
	
	
	// 하위카테고리부터 순차적으로 연결된 상위 카테고리중에서 2레벨의 카테고리 정보 SELECT
	List<String> getParentCategory(GetNSMnuListDtlRequestVO paramVO);
	
	@SuppressWarnings("rawtypes")
	public List<HashMap> getParentVersion(GetNSMnuListDtlRequestVO paramVO);
	
	@SuppressWarnings("rawtypes")
	public List<HashMap> getVersion(GetNSMnuListDtlRequestVO paramVO);
	
}
