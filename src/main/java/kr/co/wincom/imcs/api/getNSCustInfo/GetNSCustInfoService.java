package kr.co.wincom.imcs.api.getNSCustInfo;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public interface GetNSCustInfoService {
	
	public GetNSCustInfoResultVO getNSCustInfo(GetNSCustInfoRequestVO paramVO);

}
