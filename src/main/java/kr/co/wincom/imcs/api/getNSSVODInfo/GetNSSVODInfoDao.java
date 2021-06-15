package kr.co.wincom.imcs.api.getNSSVODInfo;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSSVODInfoDao {
	
	// 채널 코드 정보 조회
	public String getChnlInfo(GetNSSVODInfoRequestVO paramVO);

	// SVOD 정보 조회
	public List<GetNSSVODInfoResponseVO> getNSSVODInfoList(GetNSSVODInfoRequestVO paramVO);
	
	// 상품 유무 조회 (PT_PD_PACKAGE)
	public Integer chkProdInfo(GetNSSVODInfoRequestVO paramVO);
	
	// 상품 유무 조회 (PT_PD_PACKAGE_RELATION)
	public Integer chkProdInfo2(GetNSSVODInfoRequestVO paramVO);
		
}
