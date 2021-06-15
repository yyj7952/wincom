package kr.co.wincom.imcs.api.getNSCustInfo;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface GetNSCustInfoDao {
	
	public String getCustomInfo(GetNSCustInfoRequestVO vo);	
	
}
