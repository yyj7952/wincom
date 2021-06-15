package kr.co.wincom.imcs.api.getNSCatBillInfo;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSCatBillInfoDao {
	// 앨범 ID로 해당 앨범의 앨범 정보 및 svod only 여부 조회
	public List<GetNSCatBillInfoResponseVO> getAlbumInfo(GetNSCatBillInfoRequestVO paramVO);
	
	// 인앱 가격 조회
	public List<String> getInappInfo(GetNSCatBillInfoRequestVO paramVO);
	
	// 앨범 ID가 포함된 svod에 가입 되어 있는지 조회
	public List<String> getSubcription(GetNSCatBillInfoRequestVO paramVO);
	
	// 구매 여부 조회
	public List<String> isBuy(GetNSCatBillInfoRequestVO paramVO);
	
	// 부가세요율 조회
	public List<String> getSurtaxRateInfo();
		
}
