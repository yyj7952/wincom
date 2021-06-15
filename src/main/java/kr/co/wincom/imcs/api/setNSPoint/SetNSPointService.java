package kr.co.wincom.imcs.api.setNSPoint;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



public interface SetNSPointService {
	// 평점설정 API
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public SetNSPointResultVO setNSPoint(SetNSPointRequestVO paramVO);
	 
	// 평점 존재 여부 조회
    public Integer getPointChk(SetNSPointRequestVO paramVO) throws Exception;
    
    // 평점 정보 수정
    public Integer updatePoint(SetNSPointRequestVO paramVO) throws Exception;

    // 평점 정보 등록
    public Integer insertPoint(SetNSPointRequestVO paramVO) throws Exception;
  
}
