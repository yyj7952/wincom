package kr.co.wincom.imcs.common;

import kr.co.wincom.imcs.common.vo.ImageServerVO;

import org.springframework.stereotype.Repository;

@Repository
public interface CommonDao {

	public ImageServerVO getImgNodeIp(ImageServerVO vo);

	// 이미지 IP및 URL 조회
	public ImageServerVO getIpInfo(String szServerName);
	
	// 오늘날짜/시간 조회 (YYYYMMDDHH24MISS)
	public String getSysdate();
	
	// 오늘날짜/시간 조회 (YYYYMMDD)
	public String getSysdateYMD();
	
}
