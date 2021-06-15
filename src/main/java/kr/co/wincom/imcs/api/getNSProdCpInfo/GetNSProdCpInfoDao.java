package kr.co.wincom.imcs.api.getNSProdCpInfo;

import kr.co.wincom.imcs.common.vo.ComCpnVO;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSProdCpInfoDao {
	
	public ComCpnVO getNSProdCpInfoList(GetNSProdCpInfoRequestVO paramVO);
		
}
