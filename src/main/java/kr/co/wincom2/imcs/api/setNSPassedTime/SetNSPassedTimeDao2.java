package kr.co.wincom2.imcs.api.setNSPassedTime;

import java.util.List;

import kr.co.wincom.imcs.api.setNSPassedTime.SetNSPassedTimeRequestVO;
import kr.co.wincom.imcs.common.vo.ComDupCHk;
import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.SvodPkgVO;

import org.springframework.stereotype.Repository;

@Repository
public interface SetNSPassedTimeDao2 {
	
	// 컨텐츠 타입 조회
	public List<ContTypeVO> getContsType(SetNSPassedTimeRequestVO paramVO);
	
	// SVOD, PPM 여부 조회
	public List<SvodPkgVO> getSvodPkg(SetNSPassedTimeRequestVO paramVO);
	
	// 데이터 유무 조회1
	public List<ComDupCHk> dataChk1(SetNSPassedTimeRequestVO paramVO);
	
	// 데이터 유무 조회2
	public List<ComDupCHk> dataChk2(SetNSPassedTimeRequestVO paramVO);
	
	// 데이터 유무 조회3
	public List<ComDupCHk> dataChk3(SetNSPassedTimeRequestVO paramVO);
	
	// 데이터 유무 조회4
	public List<ComDupCHk> dataChk4(SetNSPassedTimeRequestVO paramVO);
	
	// 데이터 유무 조회5
	public List<ComDupCHk> dataChk5(SetNSPassedTimeRequestVO paramVO);
	
	// 링크 여부 조회1 (PT_VO_SET_TIME_PTT)
	public Integer getLinkChk(SetNSPassedTimeRequestVO paramVO);
	
	// 링크 여부 조회2 (PT_VO_SET_TIME)
	public List<Integer> getLinkChk2(SetNSPassedTimeRequestVO paramVO);
	
	public int updateSetTime(SetNSPassedTimeRequestVO paramVO);
	
	public int insertSetTime(SetNSPassedTimeRequestVO paramVO);
	
	public int updateWatchHis1(SetNSPassedTimeRequestVO paramVO);
	
	public int updateWatchHis2(SetNSPassedTimeRequestVO paramVO);
	
	// PT_VO_WATCH_HISTORY 등록 - 앤스크린이고 buyingDate가 있을 때
	public int updateWatchHis3(SetNSPassedTimeRequestVO paramVO);

	// PT_VO_WATCH_HISTORY 등록 - 앤스크린이고 buyingDate가 없을 때
	public int updateWatchHis4(SetNSPassedTimeRequestVO paramVO);
		
}
