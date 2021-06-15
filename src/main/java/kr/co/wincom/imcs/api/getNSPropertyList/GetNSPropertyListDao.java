package kr.co.wincom.imcs.api.getNSPropertyList;

import java.util.List;

import org.springframework.stereotype.Repository;

import kr.co.wincom.imcs.common.vo.StillImageVO;

@Repository
public interface GetNSPropertyListDao {

	List<GetNSPropertyListResponseVO> getNSPropertyListInfo (GetNSPropertyListRequestVO paramVO);
	
}
