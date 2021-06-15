package kr.co.wincom.imcs.api.setNSPassedTime;

import java.util.List;

import kr.co.wincom.imcs.common.vo.ComDupCHk;
import kr.co.wincom.imcs.common.vo.ContTypeVO;
import kr.co.wincom.imcs.common.vo.SvodPkgVO;

public interface SetNSPassedTimeService {
	
	public SetNSPassedTimeResultVO setNSPassedTime(SetNSPassedTimeRequestVO paramVO);
	
	// 컨텐츠 타입 조회
	public ContTypeVO getContsType(SetNSPassedTimeRequestVO paramVO) throws Exception;
	
	// SVOD, PPM 여부 조회
	public List<SvodPkgVO> getSvodPkg(SetNSPassedTimeRequestVO paramVO) throws Exception;
	
	// 데이터 유무 조회1
	public ComDupCHk dataChk1(SetNSPassedTimeRequestVO paramVO) throws Exception;
	
	// 데이터 유무 조회2
	public ComDupCHk dataChk2(SetNSPassedTimeRequestVO paramVO) throws Exception;
	
	// 데이터 유무 조회3
	public ComDupCHk dataChk3(SetNSPassedTimeRequestVO paramVO) throws Exception;
	
	// 데이터 유무 조회4
	public ComDupCHk dataChk4(SetNSPassedTimeRequestVO paramVO) throws Exception;
	
	// 데이터 유무 조회5
	public ComDupCHk dataChk5(SetNSPassedTimeRequestVO paramVO) throws Exception;
	
	// public Integer linkChk(SetNSPassedTimeRequestVO paramVO) throws Exception;
	
	// 이어보기 시간 갱신
	public int updateSetTime(SetNSPassedTimeRequestVO paramVO) throws Exception;
	
	// 이어보기 시간 등록
	public int insertSetTime(SetNSPassedTimeRequestVO paramVO) throws Exception;

	// 이어보기 시간 이력 등록1
	public int updateWatchHis1(SetNSPassedTimeRequestVO paramVO) throws Exception;
	
	// 이어보기 시간 이력 등록2
	public int updateWatchHis2(SetNSPassedTimeRequestVO paramVO) throws Exception;
	 
	// 이어보기 시간 이력 등록2 DAO2
	public int setNSPassedTime2(SetNSPassedTimeRequestVO paramVO) throws Exception;
}
