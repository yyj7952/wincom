package kr.co.wincom.imcs.api.getNSViewInfo;

import java.util.HashMap;

import org.springframework.stereotype.Repository;



@Repository
public interface GetNSViewInfoDao {
	
	public String getSysdate() throws Exception;
	
	// 채널 코드 정보 조회
	public String getChnlInfo(GetNSViewInfoRequestVO paramVO) throws Exception;

	// 이어보기 정보 조회
	public GetNSViewInfoResponseVO getNSViewInfoList(GetNSViewInfoRequestVO paramVO) throws Exception;
	
	// 상품 유무 조회 (PT_PD_PACKAGE)
	public Integer chkProdInfo(GetNSViewInfoRequestVO paramVO) throws Exception;
	
	// 상품 유무 조회 (PT_PD_PACKAGE_RELATION)
	public Integer chkProdInfo2(GetNSViewInfoRequestVO paramVO) throws Exception;
	
	public String getTestSbc(GetNSViewInfoRequestVO paramVO) throws Exception;
	
	public HashMap<String, String> getTestSbcPairing (GetNSViewInfoRequestVO paramVO) throws Exception;
	
	
	public HashMap<String, String> chkBuyInfo(GetNSViewInfoRequestVO paramVO) throws Exception;
	public HashMap<String, String> chkBuyInfoNsc(GetNSViewInfoRequestVO paramVO) throws Exception;
	
	
	public String[] getProductCd (GetNSViewInfoRequestVO paramVO) throws Exception;
	public String[] getProductCdNsc (GetNSViewInfoRequestVO paramVO) throws Exception;
	
	
	public String[] getProductInfo (GetNSViewInfoRequestVO paramVO) throws Exception;
	public String[] getProductInfoNsc (GetNSViewInfoRequestVO paramVO) throws Exception;
	
	
	public String getSetTimeInfo (GetNSViewInfoRequestVO paramVO) throws Exception;
	public String getSetTimeInfoNsc (GetNSViewInfoRequestVO paramVO) throws Exception;
	
	public int kidProductCd(GetNSViewInfoRequestVO paramVO) throws Exception;
}
